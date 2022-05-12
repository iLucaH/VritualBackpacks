package me.ilucah.virtualbackpacks.command;

import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CoreCommand implements CommandExecutor, TabCompleter {

    private final Handler handler;

    public CoreCommand(Handler handler) {
        this.handler = handler;
        handler.getPluginInstance().getCommand("virtualbackpacks").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("virtualbackpacks.admin")) {
            ConfigMessage.NO_PERMISSION.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
            return true;
        }
        if (args.length == 0) {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                ConfigMessage.HELP.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                try {
                    handler.reload();
                    ConfigMessage.PLUGIN_RELOAD_SUCCESS.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                } catch (Exception e) {
                    ConfigMessage.PLUGIN_RELOAD_FAILURE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ColorAPI.process("&b* &3Values: &7&o(" + handler.getBackpackManager().getPriceCache().getPrices().size() + ")"));
                handler.getBackpackManager().getPriceCache().getPrices().forEach((material, price) -> sender.sendMessage(ColorAPI.process("&9" + material.name() + ": &7$" + price)));
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!(sender instanceof Player)) {
                    ConfigMessage.PLAYER_ONLY.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                Player player = (Player) sender;
                if (player.getInventory().getItemInHand() == null) {
                    ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                Material material = player.getInventory().getItemInHand().getType();
                if (material.equals(Material.AIR)) {
                    ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                handler.getBackpackManager().getPriceCache().deleteMaterial(material);
                ConfigMessage.PRICE_REMOVED.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{material}", material.name()))));
                return true;
            } else {
                ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("setprice")) {
                if (!(sender instanceof Player)) {
                    ConfigMessage.PLAYER_ONLY.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                Player player = (Player) sender;
                if (player.getInventory().getItemInHand() == null) {
                    ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                Material material = player.getInventory().getItemInHand().getType();
                if (material.equals(Material.AIR)) {
                    ConfigMessage.INVALID_ITEM_IN_HAND.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                double price;
                try {
                    price = Double.valueOf(args[1]);
                } catch (NumberFormatException exc) {
                    ConfigMessage.INVALID_NUMBER.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                    return true;
                }
                handler.getBackpackManager().getPriceCache().setPrice(material, price);
                final double lambdaFinal = price;
                ConfigMessage.PRICE_SET.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{price}", String.valueOf(lambdaFinal)).replace("{material}", material.name()))));
                return true;
            } else {
                ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
                return true;
            }
        } else {
            ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Arrays.asList("list", "setprice", "help", "remove", "reload") : args.length == 2 ? args[1].equalsIgnoreCase("setprice") ? Arrays.asList("amount") : null : null;
    }
}
