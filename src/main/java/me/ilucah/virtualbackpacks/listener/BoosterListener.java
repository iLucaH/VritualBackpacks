package me.ilucah.virtualbackpacks.listener;

import me.ilucah.virtualbackpacks.file.ConfigMessage;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterPeriod;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.xutils.NBTEditor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class BoosterListener implements Listener {

    private final Handler handler;

    public BoosterListener(Handler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)
            return;
        if (event.getItem().getType() == Material.AIR)
            return;
        if (NBTEditor.contains(event.getItem(), "vbpsbooster"))
            boosterInteract(event);
        else if (NBTEditor.contains(event.getItem(), "boosterbox"))
            boosterBoxInteract(event);
    }

    public void boosterBoxInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(NBTEditor.getString(event.getItem(), "boosterbox"));
        if (!box.isPresent())
            return;
        handler.getMultiplierManager().getBoosterManager().openBoosterBox(event.getPlayer(), box.get());
        event.getItem().setType(null);
    }

    public void boosterInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        String handle = NBTEditor.getString(event.getItem(), "vbpsbooster");
        double amount = Double.valueOf(handle.split("::")[0]);
        int tickDuration = Integer.valueOf(handle.split("::")[1]);
        Booster booster = new Booster(amount, tickDuration, BoosterPeriod.TICKS);
        handler.getMultiplierManager().applyBooster(event.getPlayer(), booster);
        ConfigMessage.ACTIVATE_BOOSTER.get().forEach(m -> event.getPlayer().sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(amount)))));
    }
}
