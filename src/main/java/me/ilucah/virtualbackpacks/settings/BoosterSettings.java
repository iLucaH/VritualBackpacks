package me.ilucah.virtualbackpacks.settings;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BoosterSettings {

    private final Handler handler;

    private ItemStack baseItem;
    private List<String> boosterItemLore;

    public BoosterSettings(Handler handler) {
        this.handler = handler;
        reloadSettings();
    }

    public void reloadSettings() {
        FileConfiguration config = handler.getFileManager().getBoosters();
        boosterItemLore = config.getStringList("item.display-lore");
        ItemStack defaultItem = XMaterial.valueOf(config.getString("item.material")).parseItem();
        ItemMeta meta = defaultItem.getItemMeta();
        meta.setDisplayName(ColorAPI.process(config.getString("item.display-name")));
        if (config.getBoolean("item.glowing"))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(boosterItemLore);
        defaultItem.setItemMeta(meta);
        if (config.getBoolean("item.glowing"))
            defaultItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        // creating the base item
        this.baseItem = defaultItem;
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public List<String> getBoosterItemLore() {
        return boosterItemLore;
    }
}
