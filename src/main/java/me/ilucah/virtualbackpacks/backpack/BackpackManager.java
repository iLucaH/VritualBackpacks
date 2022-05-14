package me.ilucah.virtualbackpacks.backpack;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.settings.BackpackSettings;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class BackpackManager {

    private final Handler handler;

    private final BackpackUserCache userCache = new BackpackUserCache();
    private final MaterialPriceCache priceCache;

    private final BackpackSettings settings;

    public BackpackManager(Handler handler) {
        this.handler = handler;
        this.priceCache = new MaterialPriceCache(handler);
        this.settings = new BackpackSettings(handler);
    }

    public void handleBlockBreak(Player player, Block block) {
        int fort = settings.isFortuneEnabled() ? player.getInventory().getItemInHand() != null ? player.getInventory().getItemInHand().getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_BLOCKS, 1) : 1 : 1;
        userCache.addSell(player.getUniqueId(), priceCache.getPrice(block.getType()) * fort);
    }

    public void handleBlockBreak(Player player, Location location) {
        int fort = settings.isFortuneEnabled() ? player.getInventory().getItemInHand() != null ? player.getInventory().getItemInHand().getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_BLOCKS, 1) : 1 : 1;
        userCache.addSell(player.getUniqueId(), priceCache.getPrice(location.getBlock().getType()) * fort);
    }

    public BackpackUserCache getUserCache() {
        return userCache;
    }

    public MaterialPriceCache getPriceCache() {
        return priceCache;
    }

    public BackpackSettings getSettings() {
        return settings;
    }
}
