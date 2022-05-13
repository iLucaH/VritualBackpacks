package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ViewMultiSubCommand extends SubCommand {

    private final Handler handler;

    public ViewMultiSubCommand(Handler handler) {
        super("viewmulti");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            ConfigMessage.MULTIPLIER_VIEW.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{player}", player.getName()).replace("{multi}", String.valueOf(handler.getMultiplierManager().getTotalBooster(player))))));
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
