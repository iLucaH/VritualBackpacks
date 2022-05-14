package me.ilucah.virtualbackpacks.multiplier.booster.model;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.settings.BoosterSettings;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BoosterManager {

    private final Handler handler;

    private final BoosterSettings settings;
    private final ConcurrentHashMap<String, BoosterBox> boosterBoxes = new ConcurrentHashMap<>();

    public BoosterManager(Handler handler) {
        this.handler = handler;
        this.settings = new BoosterSettings(handler);
        loadBoosterBoxes();
    }

    public void giveBoosterItem(Player player, Booster booster, int amount) {
        ItemStack boosterItem = settings.getBaseItem().clone();
        boosterItem.setAmount(amount);
        ItemMeta meta = boosterItem.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.getLore().forEach(m -> lore.add(ColorAPI.process(m.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name()))));
        meta.setLore(lore);
        boosterItem.setItemMeta(meta);
        NBTItem item = new NBTItem(boosterItem, true);
        item.setString("vbpsbooster", booster.getAmount() + "::" + booster.getInitialDuration() + "::" + booster.getTimeUnit().name());
        if (player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItemNaturally(player.getLocation(), boosterItem);
        else
            player.getInventory().addItem(boosterItem);
    }

    public void loadBoosterBoxes() {
        FileConfiguration config = handler.getFileManager().getBoosters();
        if (config.getConfigurationSection("booster-boxes").getKeys(false) == null)
            return;
        for (String string : config.getConfigurationSection("booster-boxes").getKeys(false)) {
            boosterBoxes.put(string, new BoosterBox(config, string));
        }
    }

    public void openBoosterBox(Player player, BoosterBox boosterBox) {
        double boost = ThreadLocalRandom.current().nextDouble(boosterBox.getMinBoost(), boosterBox.getMaxBoost());
        int duration = ThreadLocalRandom.current().nextInt(boosterBox.getMinDuration(), boosterBox.getMaxDuration());
        Booster booster = new Booster(boost, duration, boosterBox.getTimeUnit());
        giveBoosterItem(player, booster, 1);
        boosterBox.renderSubsidiary(player, booster);
    }

    public void giveBoosterBox(Player player, BoosterBox boosterBox, int amount) {
        ItemStack boosterItem = boosterBox.getItem().clone();
        boosterItem.setAmount(amount);
        if (player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItemNaturally(player.getLocation(), boosterItem);
        else
            player.getInventory().addItem(boosterItem);
    }

    public void reload() {
        settings.reloadSettings();
        boosterBoxes.clear();
        loadBoosterBoxes();
    }

    public BoosterSettings getSettings() {
        return settings;
    }

    public void applyBooster(Player player, Booster booster) {
        handler.getMultiplierManager().getHandle().addTempMulti(player, booster.getAmount());
        Bukkit.getScheduler().scheduleAsyncDelayedTask(handler.getPluginInstance(), () -> {
            handler.getMultiplierManager().getHandle().subtractTempMulti(player, booster.getAmount());
            if (!player.isOnline())
                return;
            if (handler.getMultiplierManager().getSettings().getMessage() != null)
                handler.getMultiplierManager().getSettings().getMessage().forEach(m -> player.sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name()))));
            if (handler.getMultiplierManager().getSettings().getSound() != null)
                player.playSound(player.getLocation(), handler.getMultiplierManager().getSettings().getSound(), 1, 1);
        }, booster.getTicks());
    }

    public Optional<BoosterBox> getBoosterBox(String name) {
        return boosterBoxes.keySet().stream().filter(name::equalsIgnoreCase).map(boosterBoxes::get).findFirst();
    }

    public List<String> getBoosterBoxList() {
        return new ArrayList<>(boosterBoxes.keySet());
    }

}
