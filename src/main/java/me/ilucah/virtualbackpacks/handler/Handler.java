package me.ilucah.virtualbackpacks.handler;

import me.ilucah.virtualbackpacks.VirtualBackpacks;
import me.ilucah.virtualbackpacks.autosell.AutosellTask;
import me.ilucah.virtualbackpacks.backpack.BackpackManager;
import me.ilucah.virtualbackpacks.command.CoreCommand;
import me.ilucah.virtualbackpacks.command.implementation.PluginCommand;
import me.ilucah.virtualbackpacks.file.FileManager;
import me.ilucah.virtualbackpacks.listener.BlockBreakListener;
import me.ilucah.virtualbackpacks.listener.BoosterListener;
import me.ilucah.virtualbackpacks.listener.PlayerConnectionListener;
import me.ilucah.virtualbackpacks.multiplier.model.MultiplierManager;
import me.ilucah.virtualbackpacks.settings.BackpackSettings;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitTask;
import org.codemc.worldguardwrapper.WorldGuardWrapper;

public class Handler {

    private final VirtualBackpacks pluginInstance;

    private final FileManager fileManager;
    private final BackpackManager backpackManager;
    private final MultiplierManager multiplierManager;

    private final BackpackSettings settings;

    private final BukkitTask autosellTask;

    public Handler(VirtualBackpacks pluginInstance) {
        this.pluginInstance = pluginInstance;
        this.fileManager = new FileManager(this);
        this.backpackManager = new BackpackManager(this);
        this.multiplierManager = new MultiplierManager(this);

        this.settings = new BackpackSettings(this);
        registerCommands();
        registerListeners();

        autosellTask = createAutosellTask();
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        if (!isInRegion(event.getBlock().getLocation()))
            return;
        event.setDropItems(false);
        event.setExpToDrop(0);
        backpackManager.handleBlockBreak(event.getPlayer(), event.getBlock());
    }

    public boolean isInRegion(Location location) {
        return WorldGuardWrapper.getInstance().getRegions(location).stream().filter(iWrappedRegion -> iWrappedRegion.getId().toLowerCase().startsWith(settings.getRegionId())).findFirst().isPresent();
    }

    public void reload() {
        fileManager.reload();
        settings.reloadSettings();
        multiplierManager.reload();
    }

    public void registerCommands() {
        //pluginInstance.getCommand("virtualbackpacks").setExecutor(new CoreCommand(this));
        PluginCommand command = new PluginCommand(this);
        command.register(pluginInstance);
    }

    public void registerListeners() {
        pluginInstance.getServer().getPluginManager().registerEvents(new BlockBreakListener(this), pluginInstance);
        pluginInstance.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), pluginInstance);
        pluginInstance.getServer().getPluginManager().registerEvents(new BoosterListener(this), pluginInstance);
    }

    public BukkitTask createAutosellTask() {
        return new AutosellTask(this).runTaskTimerAsynchronously(pluginInstance, settings.getAutosellPeriod(), settings.getAutosellPeriod());
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public BackpackManager getBackpackManager() {
        return backpackManager;
    }

    public VirtualBackpacks getPluginInstance() {
        return pluginInstance;
    }

    public BackpackSettings getSettings() {
        return settings;
    }

    public MultiplierManager getMultiplierManager() {
        return multiplierManager;
    }

    public BukkitTask getAutosellTask() {
        return autosellTask;
    }
}
