package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.SubCommand;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListBoosterBoxesSubCommand extends SubCommand {

    private final Handler handler;

    public ListBoosterBoxesSubCommand(Handler handler) {
        super("listboosterboxes");
        this.handler = handler;
    }

    @Override
    public void perform(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ColorAPI.process("&b* &3Booster Boxes: &7&o(" + handler.getMultiplierManager().getBoosterManager().getBoosterBoxList().size() + ")"));
        handler.getMultiplierManager().getBoosterManager().getBoosterBoxList().forEach(m -> sender.sendMessage(ColorAPI.process("&9" + m)));
    }

    @Override
    public List<String> getTabComplete(String[] args) {
        return null;
    }
}
