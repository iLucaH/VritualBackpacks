package me.ilucah.virtualbackpacks.settings;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.xutils.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class MultiplierSettings {

    private final Handler handler;

    private List<String> message;
    private Sound sound;

    public MultiplierSettings(Handler handler) {
        this.handler = handler;
        reload();
    }

    public void reload() {
        FileConfiguration config = handler.getFileManager().getConfig();
        if (config.getBoolean("booster.expire-notification.message.enabled"))
            message = config.getStringList("booster.expire-notification.message.message");
        if (config.getBoolean("booster.expire-notification.sound.enabled"))
            sound = XSound.valueOf(config.getString("booster.expire-notification.sound.sound")).parseSound();
    }

    @Nullable
    public List<String> getMessage() {
        return message;
    }

    @Nullable
    public Sound getSound() {
        return sound;
    }
}
