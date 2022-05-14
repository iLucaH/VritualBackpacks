package me.ilucah.virtualbackpacks.api;

import me.ilucah.virtualbackpacks.VirtualBackpacks;
import me.ilucah.virtualbackpacks.backpack.BackpackManager;
import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.multiplier.booster.model.BoosterManager;
import me.ilucah.virtualbackpacks.multiplier.booster.object.Booster;
import me.ilucah.virtualbackpacks.multiplier.model.MultiplierManager;
import me.ilucah.virtualbackpacks.settings.BackpackSettings;
import me.ilucah.virtualbackpacks.settings.BoosterSettings;
import me.ilucah.virtualbackpacks.settings.MultiplierSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class VirtualBackpacksAPI {

    public static VirtualBackpacksAPI getInstance() {
        return VirtualBackpacks.getInstance().getAPI();
    }

    private final Handler handler;

    public VirtualBackpacksAPI(Handler handler) {
        this.handler = handler;
    }

    public void handleBlockBreak(Player player, List<Block> blocks) {
        blocks.iterator().forEachRemaining(b -> {
            if (!handler.isInRegion(b.getLocation()))
                return;
            handler.getBackpackManager().handleBlockBreak(player, b);
        });
    }

    public void handleBlockBreak(Player player, Block block) {
        if (!handler.isInRegion(block.getLocation()))
            return;
        handler.getBackpackManager().handleBlockBreak(player, block);
    }

    public void handleBlockBreak(Player player, Location location) {
        if (!handler.isInRegion(location))
            return;
        if (location.getBlock() == null || location.getBlock().getType() == Material.AIR)
            return;
        handler.getBackpackManager().handleBlockBreak(player, location);
    }

    public void depositToSell(Player player, double amount) {
        handler.getBackpackManager().getUserCache().addSell(player.getUniqueId(), amount);
    }

    public void withdrawFromSell(Player player, double amount) {
        handler.getBackpackManager().getUserCache().removeSell(player.getUniqueId(), amount);
    }

    public void setSell(Player player, double amount) {
        handler.getBackpackManager().getUserCache().setSell(player.getUniqueId(), amount);
    }

    public double getCurrentSell(Player player) {
        return handler.getBackpackManager().getUserCache().getUserSell(player.getUniqueId());
    }

    public double getValue(Material material) {
        return handler.getBackpackManager().getPriceCache().getPrice(material);
    }

    public void setValue(Material material, double value) {
        handler.getBackpackManager().getPriceCache().setPrice(material, value);
    }

    public boolean isInMineRegion(Location location) {
        return handler.isInRegion(location);
    }

    public BackpackSettings getBackpackSettings() {
        return handler.getBackpackManager().getSettings();
    }

    public MultiplierSettings getMultiplierSettings() {
        return handler.getMultiplierManager().getSettings();
    }

    public BoosterSettings getBoosterSettings() {
        return handler.getMultiplierManager().getBoosterManager().getSettings();
    }

    public MultiplierManager getMultiplierManager() {
        return handler.getMultiplierManager();
    }

    public BackpackManager getBackpackManager() {
        return handler.getBackpackManager();
    }

    public BoosterManager getBoosterManager() {
        return handler.getMultiplierManager().getBoosterManager();
    }

    public void applyBooster(Player player, Booster booster) {
        handler.getMultiplierManager().applyBooster(player, booster);
    }

}
