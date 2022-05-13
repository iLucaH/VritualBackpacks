package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpSubCommand extends SubCommand {

    public HelpSubCommand() {
        super("reload");
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        ConfigMessage.HELP.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
