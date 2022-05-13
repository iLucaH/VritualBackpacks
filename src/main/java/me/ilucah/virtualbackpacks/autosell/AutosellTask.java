package me.ilucah.virtualbackpacks.autosell;

import me.ilucah.virtualbackpacks.api.events.AsyncAutosellEvent;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutosellTask extends BukkitRunnable {

    private final Handler handler;

    private Economy econ;

    public AutosellTask(Handler handler) {
        this.handler = handler;
        econ = handler.getPluginInstance().getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    @Override
    public void run() {
        for (Player player : handler.getPluginInstance().getServer().getOnlinePlayers()) {
            if (!handler.getBackpackManager().getUserCache().hasSell(player.getUniqueId()))
                return;
            double sellTotal = handler.getBackpackManager().getUserCache().getUserSell(player.getUniqueId()) * handler.getMultiplierManager().getTotalBooster(player);
            if (sellTotal <= 0)
                return;
            AsyncAutosellEvent event = new AsyncAutosellEvent(player, sellTotal);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            econ.depositPlayer(player, event.getAmount());
            handler.getBackpackManager().getUserCache().flushSell(player.getUniqueId());
            if (handler.getSettings().getSound() != null)
                player.playSound(player.getLocation(), handler.getSettings().getSound(), 1, 1);
            if (handler.getSettings().getMessage() != null)
                handler.getSettings().getMessage().forEach(m -> player.sendMessage(ColorAPI.process(m.replace("{amount}", handler.getSettings().getFormat().format(event.getAmount())))));
        }
    }
}
