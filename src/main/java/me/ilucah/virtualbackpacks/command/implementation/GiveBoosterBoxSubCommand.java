package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GiveBoosterBoxSubCommand extends SubCommand {

    private final Handler handler;

    public GiveBoosterBoxSubCommand(Handler handler) {
        super("giveboosterbox");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 3 || args.length == 4) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            if (!player.isOnline()) {
                ConfigMessage.PLAYER_NOT_ONLINE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(args[2]);
            if (!box.isPresent()) {
                ConfigMessage.UNKNOWN_BOOSTER_BOX.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            int itemAmount = 1;
            if (args.length == 4) {
                try {
                    itemAmount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return;
                }
            }
            int lambdaFinalAmount = itemAmount;
            ConfigMessage.BOOSTER_BOX_GIVE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{type}", box.get().getName()).replace("{amount}", String.valueOf(lambdaFinalAmount)).replace("{player}", player.getName()))));
            handler.getMultiplierManager().getBoosterManager().giveBoosterBox(player.getPlayer(), box.get(), itemAmount);
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return args.length == 3 ? handler.getMultiplierManager().getBoosterManager().getBoosterBoxList() : args.length == 4 ? Arrays.asList("amount") : null;
    }
}
