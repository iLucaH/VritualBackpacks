package me.ilucah.virtualbackpacks.file;

import me.ilucah.virtualbackpacks.VirtualBackpacks;
import me.ilucah.virtualbackpacks.handler.Handler;
import org.bukkit.configuration.file.FileConfiguration;

public class FileManager {

    private final VirtualBackpacks plugin;

    private FileConfiguration config, messages;

    public FileManager(Handler handler) {
        this.plugin = handler.getPluginInstance();
        reload();
    }

    public void reload() {
        this.config = new YamlLoader("config", plugin).load();
        this.messages = new YamlLoader("messages", plugin).load();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }
}
