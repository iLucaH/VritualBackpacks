package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterPeriod;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AddBoosterSubCommand extends SubCommand {

    private final Handler handler;

    public AddBoosterSubCommand(Handler handler) {
        super("addbooster");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 5) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            int duration;
            try {
                duration = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            BoosterPeriod period;
            try {
                period = BoosterPeriod.valueOf(args[4].toUpperCase());
            } catch (Exception e) {
                ConfigMessage.INVALID_TIME_UNIT.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            handler.getMultiplierManager().applyBooster(player, new Booster(amount, duration, period));
            ConfigMessage.MULTIPLIER_BOOSTER_ADD.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{player}", player.getName()).replace("{multi}", String.valueOf(amount)).replace("{duration}", String.valueOf(duration)).replace("{time_unit}", period.name()))));
            return;
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return args.length == 3 ? Arrays.asList("multi") : args.length == 4 ? Arrays.asList("duration") : args.length == 5 ? Arrays.asList("ticks", "seconds", "minutes", "hours") : null;
    }
}
