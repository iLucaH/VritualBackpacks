package me.ilucah.virtualbackpacks.settings;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.xutils.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.util.List;

public class BackpackSettings {

    private final Handler handler;

    private boolean fortuneEnabled;
    private String regionId;

    private int autosellPeriod;

    private List<String> message;
    private Sound sound;

    private DecimalFormat format;

    public BackpackSettings(Handler handler) {
        this.handler = handler;
        reloadSettings();
    }

    public void reloadSettings() {
        FileConfiguration config = handler.getFileManager().getConfig();
        fortuneEnabled = config.getBoolean("fortune");
        regionId = config.getString("region-name-id").toLowerCase();
        autosellPeriod = config.getInt("autosell.period");
        format = new DecimalFormat(config.getString("number-format"));

        if (config.getBoolean("autosell.notification.message.enabled"))
            message = config.getStringList("autosell.notification.message.message");
        if (config.getBoolean("autosell.notification.sound.enabled"))
            sound = XSound.valueOf(config.getString("autosell.notification.sound.sound")).parseSound();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setFortuneEnabled(boolean fortuneEnabled) {
        this.fortuneEnabled = fortuneEnabled;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public boolean isFortuneEnabled() {
        return fortuneEnabled;
    }

    public int getAutosellPeriod() {
        return autosellPeriod;
    }

    public void setAutosellPeriod(int autosellPeriod) {
        this.autosellPeriod = autosellPeriod;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public DecimalFormat getFormat() {
        return format;
    }

    public void setFormat(DecimalFormat format) {
        this.format = format;
    }
}
