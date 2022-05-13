package me.ilucah.virtualbackpacks.command.object;

import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    private final ConcurrentMap<String, SubCommand> subCommands;

    private String command;
    private boolean isPlayerOnly;

    public AbstractCommand(String command) {
        this.command = command;
        subCommands = new ConcurrentHashMap<>();
    }

    public void addSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getSubCommandName().toLowerCase(), subCommand);
    }

    public List<SubCommand> getSubCommands() {
        return subCommands.values().stream().collect(Collectors.toList());
    }

    public void register(JavaPlugin plugin) {
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(this);
    }

    public boolean isPlayerOnlyCommand() {
        return isPlayerOnly;
    }

    public void setPlayerOnlyCommand(boolean val) {
        this.isPlayerOnly = val;
    }

    public abstract void performWhenNoArgs(CommandSender sender, Command command, String label, String[] args);

    public abstract void performWhenNoSubCommand(CommandSender sender, Command command, String label, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("virtualbackpacks.admin")) {
            ConfigMessage.NO_PERMISSION.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
            return true;
        }
        if (args.length == 0)
            performWhenNoArgs(sender, command, label, args);
        else {
            if (subCommands.containsKey(args[0].toLowerCase()))
                subCommands.get(args[0].toLowerCase()).run(sender, command, label, args);
            else
                performWhenNoSubCommand(sender, command, label, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("virtualbackpacks.admin"))
            return null;
        switch (args.length) {
            case 1:
                return new ArrayList<>(subCommands.keySet());
            default:
                return subCommands.containsKey(args[0].toLowerCase()) ? subCommands.get(args[0].toLowerCase()).getTabComplete(args) : null;
        }
    }
}
