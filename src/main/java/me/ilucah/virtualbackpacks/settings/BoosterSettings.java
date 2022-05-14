package me.ilucah.virtualbackpacks.settings;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.particle.ParticleAnimator;
import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import me.ilucah.virtualbackpacks.utils.xutils.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.particle.ParticleEffect;

import javax.annotation.Nullable;
import java.util.List;

public class BoosterSettings {

    private final Handler handler;

    private ItemStack baseItem;
    private List<String> boosterItemLore;

    private List<String> activationMessage;
    private Sound activationSound;
    private ParticleEffect activationParticle;
    private ParticleAnimator.ParticleAnimation activationParticleAnimation;
    private boolean usingActivationTitle;
    private String activationTitleLine1, activationTitleLine2;

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
        this.baseItem = defaultItem;

        // here we shall initialise the activation actions.
        if (config.getBoolean("activation-actions.particles.enabled")) {
            activationParticleAnimation = ParticleAnimator.ParticleAnimation.valueOf(config.getString("activation-actions.particles.animation").toUpperCase());
            activationParticle = ParticleEffect.valueOf(config.getString("activation-actions.particles.particle"));
        }
        if (config.getBoolean("activation-actions.message.enabled"))
            activationMessage = config.getStringList("activation-actions.message.message");
        if (config.getBoolean("activation-actions.sound.enabled"))
            activationSound = XSound.valueOf(config.getString("activation-actions.sound.sound")).parseSound();
        usingActivationTitle = config.getBoolean("activation-actions.title.enabled");
        if (usingActivationTitle) {
            activationTitleLine1 = config.getString("activation-actions.title.message.line1");
            activationTitleLine2 = config.getString("activation-actions.title.message.line2");
        }
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public List<String> getBoosterItemLore() {
        return boosterItemLore;
    }

    @Nullable
    public List<String> getActivationMessage() {
        return activationMessage;
    }

    public void setActivationMessage(List<String> activationMessage) {
        this.activationMessage = activationMessage;
    }

    @Nullable
    public Sound getActivationSound() {
        return activationSound;
    }

    public void setActivationSound(Sound activationSound) {
        this.activationSound = activationSound;
    }

    @Nullable
    public ParticleEffect getActivationParticle() {
        return activationParticle;
    }

    public void setActivationParticle(ParticleEffect activationParticle) {
        this.activationParticle = activationParticle;
    }

    public boolean isUsingActivationTitle() {
        return usingActivationTitle;
    }

    public void setUsingActivationTitle(boolean usingActivationTitle) {
        this.usingActivationTitle = usingActivationTitle;
    }

    @Nullable
    public String getActivationTitleLine1() {
        return activationTitleLine1;
    }

    public void setActivationTitleLine1(String activationTitleLine1) {
        this.activationTitleLine1 = activationTitleLine1;
    }

    @Nullable
    public String getActivationTitleLine2() {
        return activationTitleLine2;
    }

    public void setActivationTitleLine2(String activationTitleLine2) {
        this.activationTitleLine2 = activationTitleLine2;
    }

    @Nullable
    public ParticleAnimator.ParticleAnimation getActivationParticleAnimation() {
        return activationParticleAnimation;
    }

    public void setActivationParticleAnimation(ParticleAnimator.ParticleAnimation activationParticleAnimation) {
        this.activationParticleAnimation = activationParticleAnimation;
    }
}
