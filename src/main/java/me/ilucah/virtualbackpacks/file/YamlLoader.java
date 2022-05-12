package me.ilucah.virtualbackpacks.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YamlLoader {

    private String yamlName;

    private File dataFolder;
    private JavaPlugin plugin;
    private File file;

    private FileConfiguration config;

    public YamlLoader(String yamlName, JavaPlugin plugin, File dataFolder) {
        this.yamlName = yamlName;
        this.dataFolder = dataFolder;
        this.plugin = plugin;
    }

    public YamlLoader(String yamlName, JavaPlugin plugin) {
        this(yamlName, plugin, plugin.getDataFolder());
    }

    public FileConfiguration load() {
        if (config != null)
            return config;
        try {
            if(!dataFolder.exists())
                dataFolder.mkdirs();
            this.file = new File(dataFolder, yamlName + ".yml");
            boolean isResource = plugin.getResource(yamlName + ".yml") != null;
            if(!file.exists()) {
                if(isResource) {
                    plugin.saveResource(yamlName + ".yml", false);
                } else {
                    file.createNewFile();
                }
            }
            config = YamlConfiguration.loadConfiguration(file);;
            return config;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public FileConfiguration getConfig() {
        File file = new File(dataFolder + "/messages/", yamlName + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
