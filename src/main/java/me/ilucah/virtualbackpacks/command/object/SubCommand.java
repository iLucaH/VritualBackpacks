package me.ilucah.virtualbackpacks.command.object;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SubCommand {

    private String subCommand;

    public SubCommand(String subCommand) {
        this.subCommand = subCommand;
    }

    public abstract void perform(CommandSender sender, Command command, String label, String[] args);

    public abstract List<String> getTabComplete(String[] args);

    public String getSubCommandName() {
        return subCommand;
    }

    public void run(CommandSender sender, Command command, String label, String[] args) {
        perform(sender, command, label, args);
    }
}
