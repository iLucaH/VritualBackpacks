package me.ilucah.virtualbackpacks.command.implementation;

import me.ilucah.virtualbackpacks.command.object.AbstractCommand;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PluginCommand extends AbstractCommand {

    public PluginCommand(Handler handler) {
        super("virtualbackpacks");

        addSubCommand(new ReloadSubCommand(handler));
        addSubCommand(new HelpSubCommand());
        addSubCommand(new ListPricesSubCommand(handler));
        addSubCommand(new RemoveSubCommand(handler));
        addSubCommand(new SetPriceSubCommand(handler));
        addSubCommand(new ViewMultiSubCommand(handler));
        addSubCommand(new SetMultiSubCommand(handler));
        addSubCommand(new AddMultiSubCommand(handler));
        addSubCommand(new AddBoosterSubCommand(handler));
        addSubCommand(new RemoveMultiSubCommand(handler));
        addSubCommand(new GiveBoosterSubCommand(handler));
        addSubCommand(new GiveBoosterBoxSubCommand(handler));
        addSubCommand(new ListBoosterBoxesSubCommand(handler));
    }

    @Override
    public void performWhenNoArgs(CommandSender sender, Command command, String label, String[] args) {
        ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
    }

    @Override
    public void performWhenNoSubCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigMessage.INCORRECT_USAGE.get().forEach(m -> sender.sendMessage(ColorAPI.process(m)));
    }
}
