package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListPricesSubCommand extends SubCommand {

    private final Handler handler;

    public ListPricesSubCommand(Handler handler) {
        super("listprices");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ColorAPI.process("&b* &3Values: &7&o(" + handler.getBackpackManager().getPriceCache().getPrices().size() + ")"));
        handler.getBackpackManager().getPriceCache().getPrices().forEach((material, price) -> sender.sendMessage(ColorAPI.process("&9" + material.name() + ": &7$" + price)));
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
