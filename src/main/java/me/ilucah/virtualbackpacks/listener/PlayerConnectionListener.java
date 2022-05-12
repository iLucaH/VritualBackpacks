package me.ilucah.virtualbackpacks.listener;

import me.ilucah.virtualbackpacks.handler.Handler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final Handler handler;

    public PlayerConnectionListener(Handler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        handler.getBackpackManager().getUserCache().submitUser(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        handler.getBackpackManager().getUserCache().withdrawUser(event.getPlayer().getUniqueId());
    }
}
