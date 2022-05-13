package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveSubCommand extends SubCommand {

    private final Handler handler;

    public RemoveSubCommand(Handler handler) {
        super("remove");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
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
        handler.getBackpackManager().getPriceCache().deleteMaterial(material);
        ConfigMessage.PRICE_REMOVED.get().forEach(m -> sender.sendMessage(ColorAPI.process(m.replace("{material}", material.name()))));
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
