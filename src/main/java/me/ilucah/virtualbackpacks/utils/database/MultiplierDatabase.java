package me.ilucah.virtualbackpacks.utils.database;

import org.bukkit.OfflinePlayer;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class MultiplierDatabase {

    private Connection connection;
    private final String filePath;
    private String connectionName;

    public MultiplierDatabase(File file) {
        filePath = file.getAbsolutePath();
        try {
            if (this.connection != null && !this.connection.isClosed()) return;
            Class.forName("org.sqlite.JDBC");
            this.connectionName = "jdbc:sqlite:" + this.filePath;
            connection = DriverManager.getConnection(this.connectionName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        createTable();
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS multis "
                    + "(NAME VARCHAR(100),UUID VARCHAR(100),MULTI DOUBLE(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(OfflinePlayer player) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM multis WHERE NAME=?");
            ps.setString(1, player.getName());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayerInDB(OfflinePlayer player) {
        try {
            if (!exists(player)) {
                PreparedStatement ps2 = connection.prepareStatement("INSERT INTO" +
                        " multis (NAME,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, player.getUniqueId().toString());
                ps2.executeUpdate();
                return;
            }
        } catch (SQLException e) {
        }
    }

    public Optional<Double> getMultiFromDB(OfflinePlayer player) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MULTI FROM multis WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            Double points;
            if (rs.next()) {
                points = rs.getDouble("MULTI");
                return Optional.ofNullable(points);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(1D);
    }

    private void setMultiInDB(OfflinePlayer player, Double multi) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE multis SET MULTI=? WHERE NAME=?");
            ps.setDouble(1, multi);
            ps.setString(2, player.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromDB(OfflinePlayer player) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM multis WHERE NAME=?");
            ps.setString(1, player.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMulti(OfflinePlayer player, double multi) {
        if (!exists(player))
            createPlayerInDB(player);
        setMultiInDB(player, multi);
    }

    public Connection getConnection() {
        return connection;
    }
}