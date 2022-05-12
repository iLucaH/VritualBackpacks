package me.ilucah.virtualbackpacks.listener;

import me.ilucah.virtualbackpacks.handler.Handler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final Handler handler;

    public BlockBreakListener(Handler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handler.handleBlockBreak(event);
    }
}
