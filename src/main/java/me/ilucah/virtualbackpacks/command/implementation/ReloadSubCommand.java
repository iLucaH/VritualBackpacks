package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubCommand extends SubCommand {

    private final Handler handler;

    public ReloadSubCommand(Handler handler) {
        super("reload");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        try {
            handler.reload();
            ConfigMessage.PLUGIN_RELOAD_SUCCESS.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        } catch (Exception e) {
            ConfigMessage.PLUGIN_RELOAD_FAILURE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
