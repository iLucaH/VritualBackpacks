package me.ilucah.virtualbackpacks.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterBox;
import me.ilucah.virtualbackpacks.multiplier.booster.object.BoosterPeriod;
import me.ilucah.virtualbackpacks.settings.BoosterSettings;
import me.ilucah.virtualbackpacks.utils.colorapi.ColorAPI;
import me.ilucah.virtualbackpacks.utils.particle.ParticleAnimator;
import me.ilucah.virtualbackpacks.utils.xutils.NMS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (event.getItem() == null)
            return;
        if (event.getItem().getType() == Material.AIR)
            return;
        NBTItem item = new NBTItem(event.getItem());
        if (item.hasKey("vbpsbooster")) {
            boosterInteract(event);
        } else if (item.hasKey("boosterbox")) {
            boosterBoxInteract(event);
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
        } else if (item.hasKey("boosterbox")) {
            boosterBoxInteract(event);
        }
    }

    private void boosterBoxInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(new NBTItem(event.getItem()).getString("boosterbox"));
        if (!box.isPresent())
            return;
        handler.getMultiplierManager().getBoosterManager().openBoosterBox(event.getPlayer(), box.get());
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }

    private void boosterInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        String handle = new NBTItem(event.getItem()).getString("vbpsbooster");
        double amount = Double.valueOf(handle.split("::")[0]);
        int duration = Integer.valueOf(handle.split("::")[1]);
        BoosterPeriod period = BoosterPeriod.valueOf(handle.split("::")[2]);
        Booster booster = new Booster(amount, duration, period);
        handler.getMultiplierManager().applyBooster(event.getPlayer(), booster);
        runActivationSubsidiaryActions(event.getPlayer(), amount, duration, period);
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }

    private void boosterBoxInteract(BlockPlaceEvent event) {
        event.setCancelled(true);
        Optional<BoosterBox> box = handler.getMultiplierManager().getBoosterManager().getBoosterBox(new NBTItem(event.getItemInHand()).getString("boosterbox"));
        if (!box.isPresent())
            return;
        handler.getMultiplierManager().getBoosterManager().openBoosterBox(event.getPlayer(), box.get());
        event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
    }

    private void boosterInteract(BlockPlaceEvent event) {
        event.setCancelled(true);
        String handle = new NBTItem(event.getItemInHand()).getString("vbpsbooster");
        double amount = Double.valueOf(handle.split("::")[0]);
        int duration = Integer.valueOf(handle.split("::")[1]);
        BoosterPeriod period = BoosterPeriod.valueOf(handle.split("::")[2]);
        Booster booster = new Booster(amount, duration, period);
        handler.getMultiplierManager().applyBooster(event.getPlayer(), booster);
        runActivationSubsidiaryActions(event.getPlayer(), amount, duration, period);
        event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
    }

    private void runActivationSubsidiaryActions(Player player, double amount, int duration, BoosterPeriod period) {
        BoosterSettings settings = handler.getMultiplierManager().getBoosterManager().getSettings();
        if (settings.getActivationMessage() != null)
            settings.getActivationMessage().forEach(m -> player.sendMessage(ColorAPI.process(m.replace("{multi}", String.valueOf(amount)).replace("{duration}", String.valueOf(duration)).replace("{time_unit}", period.name()))));
        if (settings.getActivationParticle() != null)
            settings.getActivationParticleAnimation().playOutAnimation(player, settings.getActivationParticle());
        if (settings.getActivationSound() != null)
            player.playSound(player.getLocation(), settings.getActivationSound(), 1, 1);
        if (settings.isUsingActivationTitle())
            NMS.playOutTitleEffect(player, ColorAPI.process(settings.getActivationTitleLine1().replace("{multi}", String.valueOf(amount)).replace("{duration}", String.valueOf(duration)).replace("{time_unit}", period.name())), ColorAPI.process(settings.getActivationTitleLine2().replace("{multi}", String.valueOf(amount)).replace("{duration}", String.valueOf(duration)).replace("{time_unit}", period.name())));
    }
}
