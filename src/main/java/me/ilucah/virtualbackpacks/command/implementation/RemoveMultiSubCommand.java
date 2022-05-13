package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RemoveMultiSubCommand extends SubCommand {

    private final Handler handler;

    public RemoveMultiSubCommand(Handler handler) {
        super("removemulti");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 3) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            handler.getMultiplierManager().getHandle().subtractPermMulti(player, amount);
            ConfigMessage.MULTIPLIER_REMOVE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{player}", player.getName()).replace("{multi}", String.valueOf(amount)))));
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return args.length == 3 ? Arrays.asList("multi") : null;
    }
}
