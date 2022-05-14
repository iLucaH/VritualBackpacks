package me.ilucah.virtualbackpacks.multiplier.model;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.database.MultiplierDatabase;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;

public class MultiplierCache {

    private final ConcurrentHashMap<OfflinePlayer, Double> permMulti = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<OfflinePlayer, Double> tempMulti = new ConcurrentHashMap<>();

    private final MultiplierDatabase db;

    public MultiplierCache(Handler handler) {
        db = new MultiplierDatabase(new File(handler.getPluginInstance().getDataFolder(), "multidata.db"));
    }

    public ConcurrentHashMap<OfflinePlayer, Double> getPermMultipliers() {
        return permMulti;
    }

    public ConcurrentHashMap<OfflinePlayer, Double> getTempMultipliers() {
        return tempMulti;
    }

    public void addPermMulti(OfflinePlayer offlinePlayer, double multi) {
        permMulti.put(offlinePlayer, permMulti.getOrDefault(offlinePlayer, 1D) + multi);
    }

    public void setPermMulti(OfflinePlayer offlinePlayer, double multi) {
        permMulti.put(offlinePlayer, multi);
    }

    public void subtractPermMulti(OfflinePlayer offlinePlayer, double multi) {
        permMulti.put(offlinePlayer, permMulti.getOrDefault(offlinePlayer, 1D) - multi);
    }

    public double getPermMulti(OfflinePlayer offlinePlayer) {
        return permMulti.containsKey(offlinePlayer) ? permMulti.get(offlinePlayer) : load(offlinePlayer);
    }

    public void addTempMulti(OfflinePlayer offlinePlayer, double multi) {
        tempMulti.put(offlinePlayer, BigDecimal.valueOf(tempMulti.getOrDefault(offlinePlayer, 0D) + multi).setScale(1, RoundingMode.HALF_UP).doubleValue());
    }

    public void setTempMulti(OfflinePlayer offlinePlayer, double multi) {
        tempMulti.put(offlinePlayer, multi);
    }

    public void subtractTempMulti(OfflinePlayer offlinePlayer, double multi) {
        tempMulti.put(offlinePlayer, tempMulti.getOrDefault(offlinePlayer, 0D) - multi);
    }

    public double getTempMulti(OfflinePlayer offlinePlayer) {
        return tempMulti.getOrDefault(offlinePlayer, 0D);
    }

    public double getTotalMulti(OfflinePlayer player) {
        return getTempMulti(player) + getPermMulti(player);
    }

    public void submitUser(OfflinePlayer offlinePlayer) {
        permMulti.putIfAbsent(offlinePlayer, getDatabase().getMultiFromDB(offlinePlayer).orElse(1D));
        tempMulti.putIfAbsent(offlinePlayer, 0D);
    }

    public void withdrawUser(OfflinePlayer offlinePlayer) {
        save(offlinePlayer);
        permMulti.remove(offlinePlayer);
    }

    public void save(OfflinePlayer player) {
        getDatabase().setMulti(player, getPermMulti(player));
    }

    public double load(OfflinePlayer player) {
        if (permMulti.containsKey(player))
            return permMulti.get(player);
        permMulti.put(player, getDatabase().getMultiFromDB(player).orElse(1D));
        return permMulti.get(player);
    }

    public void unloadAll() {
        permMulti.keySet().forEach(this::save);
    }

    public MultiplierDatabase getDatabase() {
        return db;
    }
}
