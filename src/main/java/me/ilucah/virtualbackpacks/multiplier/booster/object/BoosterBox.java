package me.ilucah.virtualbackpacks.multiplier.booster.object;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.particle.ParticleAnimator;
import me.ilucah.virtualbackpacks.utils.xutils.NMS;
import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import me.ilucah.virtualbackpacks.utils.xutils.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;

public class BoosterBox {

    private final String name;
    private final ItemStack item;
    private final double minBoost, maxBoost;
    private final int minDuration, maxDuration;
    private final BoosterPeriod timeUnit;

    private ParticleEffect particle;
    private ParticleAnimator.ParticleAnimation animation;
    private Sound sound;
    private boolean usingTitle;
    private String titleLine1, titleLine2;
    private List<String> message;

    public BoosterBox(FileConfiguration config, String name) {
        this.name = name;
        String x = "booster-boxes." + name + ".";
        ItemStack baseItem = XMaterial.valueOf(config.getString(x + "material")).parseItem();
        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName(ColorAPI.process(config.getString(x + "display-name")));
        meta.setLore(ColorAPI.process(config.getStringList(x + "display-lore")));
        if (config.getBoolean(x + "glowing"))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        baseItem.setItemMeta(meta);
        if (config.getBoolean(x + "glowing"))
            baseItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        NBTItem item = new NBTItem(baseItem, true);
        item.setString("boosterbox", name);
        this.item = baseItem;

        minBoost = config.getDouble(x + "boost.min-amount");
        maxBoost = config.getDouble(x + "boost.max-amount");
        minDuration = config.getInt(x + "duration.min-amount");
        maxDuration = config.getInt(x + "duration.max-amount");
        timeUnit = BoosterPeriod.valueOf(config.getString(x + "duration.time-unit").toUpperCase());

        if (config.getBoolean(x + "open-actions.particles.enabled")) {
            particle = ParticleEffect.valueOf(config.getString(x + "open-actions.particles.particle"));
            animation = ParticleAnimator.ParticleAnimation.valueOf(config.getString(x + "open-actions.particles.animation").toUpperCase());
        }
        if (config.getBoolean(x + "open-actions.sound.enabled"))
            sound = XSound.valueOf(config.getString(x + "open-actions.sound.sound")).parseSound();
        if (config.getBoolean(x + "open-actions.message.enabled"))
            message = config.getStringList(x + "open-actions.message.message");
        if (usingTitle = config.getBoolean(x + "open-actions.title.enabled")) {
            titleLine1 = config.getString(x + "open-actions.title.message.line1");
            titleLine2 = config.getString(x + "open-actions.title.message.line2");
        }
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getMinBoost() {
        return minBoost;
    }

    public double getMaxBoost() {
        return maxBoost;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public BoosterPeriod getTimeUnit() {
        return timeUnit;
    }

    public ParticleEffect getParticle() {
        return particle;
    }

    public void setParticle(ParticleEffect particle) {
        this.particle = particle;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public boolean isUsingTitle() {
        return usingTitle;
    }

    public void setUsingTitle(boolean usingTitle) {
        this.usingTitle = usingTitle;
    }

    public String getTitleLine1() {
        return titleLine1;
    }

    public void setTitleLine1(String titleLine1) {
        this.titleLine1 = titleLine1;
    }

    public String getTitleLine2() {
        return titleLine2;
    }

    public void setTitleLine2(String titleLine2) {
        this.titleLine2 = titleLine2;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void renderSubsidiary(Player player, Booster booster) {
        if (message != null)
            message.forEach(m -> player.sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name()))));
        if (sound != null)
            player.playSound(player.getLocation(), sound, 1, 1);
        if (particle != null)
            animation.playOutAnimation(player, particle);
        if (usingTitle)
            NMS.playOutTitleEffect(player, ColorAPI.process(titleLine1.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name())), ColorAPI.process(titleLine2.replace("{multi}", String.valueOf(booster.getAmount())).replace("{duration}", String.valueOf(booster.getInitialDuration())).replace("{time_unit}", booster.getTimeUnit().name())));
    }

}
