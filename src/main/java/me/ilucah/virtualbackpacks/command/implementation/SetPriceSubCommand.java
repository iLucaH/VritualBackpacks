package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetPriceSubCommand extends SubCommand {

    private final Handler handler;

    public SetPriceSubCommand(Handler handler) {
        super("setprice");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                ConfigMessage.PLAYER_ONLY.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            Player player = (Player) sender;
            if (player.getInventory().getItemInHand() == null) {
                ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            Material material = player.getInventory().getItemInHand().getType();
            if (material.equals(Material.AIR)) {
                ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            double price;
            try {
                price = Double.valueOf(args[1]);
            } catch (NumberFormatException exc) {
                ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return;
            }
            handler.getBackpackManager().getPriceCache().setPrice(material, price);
            final double lambdaFinal = price;
            ConfigMessage.PRICE_SET.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{price}", String.valueOf(lambdaFinal)).replace("{material}", material.name()))));
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
        }
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return args.length == 2 ? Arrays.asList("amount") : null;
    }
}
