package me.ilucah.virtualbackpacks.backpack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackpackUserCache {

    private final ConcurrentHashMap<UUID, Double> sells = new ConcurrentHashMap<>();

    public boolean hasSell(UUID uuid) {
        return sells.containsKey(uuid);
    }

    public void submitUser(UUID uuid) {
        sells.putIfAbsent(uuid, 0D);
    }

    public void withdrawUser(UUID uuid) {
        sells.remove(uuid);
    }

    public double getUserSell(UUID uuid) {
        return sells.getOrDefault(uuid, 0D);
    }

    public void flushSell(UUID uuid) {
        sells.put(uuid, 0D);
    }

    public void addSell(UUID uuid, double amount) {
        sells.put(uuid, sells.getOrDefault(uuid, 0D) + amount);
    }

    public void removeSell(UUID uuid, double amount) {
        sells.put(uuid, sells.getOrDefault(uuid, 0D) - amount);
    }

    public void setSell(UUID uuid, double amount) {
        sells.put(uuid, amount);
    }

}
