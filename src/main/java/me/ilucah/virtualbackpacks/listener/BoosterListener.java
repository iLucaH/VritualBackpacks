package me.ilucah.virtualbackpacks.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterPeriod;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class BoosterListener implements Listener {

    private final Handler handler;

    public BoosterListener(Handler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        System.out.println("A");
        if (event.getItem() == null)
            return;
        System.out.println("B");
        if (event.getItem().getType() == Material.AIR)
            return;
        System.out.println("C");
        NBTItem item = new NBTItem(event.getItem());
        if (item.hasKey("vbpsbooster")) {
            boosterInteract(event);
            System.out.println("D");
        } else if (item.hasKey("boosterbox")) {
            boosterBoxInteract(event);
            System.out.println("E");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null)
            return;
        if (event.getItemInHand().getType() == Material.AIR)
            return;
        NBTItem item = new NBTItem(event.getItemInHand());
        if (item.hasKey("vbpsbooster")) {
            boosterInteract(event);
            System.out.println("D");
        } else if (item.hasKey("boosterbox")) {
            boosterBoxInteract(event);
            System.out.println("E");
        }
    }

    public void boosterBoxInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(new NBTItem(event.getItem()).getString("boosterbox"));
        if (!box.isPresent())
            return;
        handler.getMultiplierManager().getBoosterManager().openBoosterBox(event.getPlayer(), box.get());
        event.getItem().setType(null);
    }

    public void boosterInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        String handle = new NBTItem(event.getItem()).getString("vbpsbooster");
        double amount = Double.valueOf(handle.split("::")[0]);
        int tickDuration = Integer.valueOf(handle.split("::")[1]);
        Booster booster = new Booster(amount, tickDuration, BoosterPeriod.TICKS);
        handler.getMultiplierManager().applyBooster(event.getPlayer(), booster);
        ConfigMessage.ACTIVATE_BOOSTER.get().forEach(m -> event.getPlayer().sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(amount)))));
    }

    public void boosterBoxInteract(BlockPlaceEvent event) {
        event.setCancelled(true);
        Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(new NBTItem(event.getItemInHand()).getString("boosterbox"));
        if (!box.isPresent())
            return;
        handler.getMultiplierManager().getBoosterManager().openBoosterBox(event.getPlayer(), box.get());
        event.getItemInHand().setType(null);
    }

    public void boosterInteract(BlockPlaceEvent event) {
        event.setCancelled(true);
        String handle = new NBTItem(event.getItemInHand()).getString("vbpsbooster");
        double amount = Double.valueOf(handle.split("::")[0]);
        int tickDuration = Integer.valueOf(handle.split("::")[1]);
        Booster booster = new Booster(amount, tickDuration, BoosterPeriod.TICKS);
        handler.getMultiplierManager().applyBooster(event.getPlayer(), booster);
        ConfigMessage.ACTIVATE_BOOSTER.get().forEach(m -> event.getPlayer().sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(amount)))));
    }
}
