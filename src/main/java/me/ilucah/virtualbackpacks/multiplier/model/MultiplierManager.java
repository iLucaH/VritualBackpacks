package me.ilucah.virtualbackpacks.multiplier.model;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.model.BoosterManager;
import me.ilucah.virtualbackpacks.settings.MultiplierSettings;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MultiplierManager {

    private final Handler handler;

    private final MultiplierCache cache;
    private final MultiplierSettings settings;
    private final BoosterManager boosterManager;

    public MultiplierManager(Handler handler) {
        this.handler = handler;
        this.cache = new MultiplierCache(handler);
        this.settings = new MultiplierSettings(handler);
        this.boosterManager = new BoosterManager(handler);
    }

    public void applyBooster(Player player, Booster booster) {
        cache.addTempMulti(player, booster.getAmount());
        Bukkit.getScheduler().scheduleAsyncDelayedTask(handler.getPluginInstance(), () -> {
            cache.subtractTempMulti(player, booster.getAmount());
            if (settings.getMessage() != null)
                settings.getMessage().forEach(m -> player.sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name()))));
            if (settings.getSound() != null)
                player.playSound(player.getLocation(), settings.getSound(), 1, 1);
        }, booster.getTicks());
    }

    public void applyBooster(OfflinePlayer player, Booster booster) {
        cache.addTempMulti(player, booster.getAmount());
        Bukkit.getScheduler().scheduleAsyncDelayedTask(handler.getPluginInstance(), () -> {
            cache.subtractTempMulti(player, booster.getAmount());
            if (!player.isOnline())
                return;
            if (settings.getMessage() != null)
                settings.getMessage().forEach(m -> player.getPlayer().sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name()))));
            if (settings.getSound() != null)
                player.getPlayer().playSound(player.getPlayer().getLocation(), settings.getSound(), 1, 1);
        }, booster.getTicks());
    }

    public double getTotalBooster(Player player) {
        return cache.getTotalMulti(player);
    }

    public double getTotalBooster(OfflinePlayer player) {
        return cache.getTotalMulti(player);
    }

    public MultiplierCache getHandle() {
        return cache;
    }

    public double getTempBooster(Player player) {
        return cache.getTempMulti(player);
    }

    public double getPermBooster(Player player) {
        return cache.getPermMulti(player);
    }

    public void reload() {
        settings.reload();
        boosterManager.reload();
    }

    public BoosterManager getBoosterManager() {
        return boosterManager;
    }

}
