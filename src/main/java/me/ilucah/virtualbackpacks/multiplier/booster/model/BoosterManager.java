package me.ilucah.virtualbackpacks.multiplier.booster.model;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.settings.BoosterSettings;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.xutils.NBTEditor;
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
        NBTEditor.set(boosterItem, booster.getAmount() + "::" + booster.getTicks(), "vbpsbooster");
        if (player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItemNaturally(player.getLocation(), boosterItem);
        else
            player.getInventory().addItem(boosterItem);
    }

    public void loadBoosterBoxes() {
        FileConfiguration config = handler.getFileManager().getBoosters();
        System.out.println("x");
        if (config.getConfigurationSection("booster-boxes").getKeys(false) == null)
            return;
        System.out.println("xx");
        for (String string : config.getConfigurationSection("booster-boxes").getKeys(false)) {
            boosterBoxes.put(string, new BoosterBox(config, string));
            handler.getPluginInstance().getLogger().info("Loaded booster box type: " + string);
        }
        System.out.println("xxx");
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

    public Optional<BoosterBox> getBoosterBox(String name) {
        return boosterBoxes.keySet().stream().filter(name::equalsIgnoreCase).map(boosterBoxes::get).findFirst();
    }

    public List<String> getBoosterBoxList() {
        return new ArrayList<>(boosterBoxes.keySet());
    }

}
