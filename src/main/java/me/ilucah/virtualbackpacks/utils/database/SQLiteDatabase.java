package me.ilucah.virtualbackpacks.utils.database;

import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SQLiteDatabase {

    private Connection connection;
    private final String filePath;
    private String connectionName;

    public SQLiteDatabase(File file) {
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
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS prices "
                    + "(NAME VARCHAR(100),MATERIAL VARCHAR(100),PRICE DOUBLE(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM prices WHERE NAME=?");
            ps.setString(1, name);
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

    public void createMaterialInDB(String name) {
        try {
            if (!exists(name)) {
                PreparedStatement ps2 = connection.prepareStatement("INSERT INTO" +
                        " prices (NAME,MATERIAL) VALUES (?,?)");
                ps2.setString(1, name);
                ps2.setString(2, name);
                ps2.executeUpdate();
                return;
            }
        } catch (SQLException e) {
        }
    }

    public Optional<Double> getPriceFromDB(String material) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT PRICE FROM prices WHERE MATERIAL=?");
            ps.setString(1, material);
            ResultSet rs = ps.executeQuery();
            Double points;
            if (rs.next()) {
                points = rs.getDouble("PRICE");
                return Optional.ofNullable(points);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(0D);
    }

    private void setPriceInDB(String material, Double price) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE prices SET PRICE=? WHERE NAME=?");
            ps.setDouble(1, price);
            ps.setString(2, material);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromDB(String material) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM prices WHERE NAME=?");
            ps.setString(1, material);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPrice(String material, double price) {
        if (!exists(material))
            createMaterialInDB(material);
        setPriceInDB(material, price);
    }

    public void map(ConcurrentHashMap<Material, Double> map) {
        try {
            String statement = "SELECT * FROM prices";
            PreparedStatement ps = connection.prepareStatement(statement);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                map.put(XMaterial.valueOf(result.getString("NAME")).parseMaterial(), result.getDouble("PRICE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }
}
