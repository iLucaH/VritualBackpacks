package me.ilucah.virtualbackpacks.backpack;

import me.ilucah.virtualbackpacks.handler.Handler;
import me.ilucah.virtualbackpacks.utils.database.SQLiteDatabase;
import me.ilucah.virtualbackpacks.utils.xutils.XMaterial;
import org.bukkit.Material;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialPriceCache {

    private final ConcurrentHashMap<Material, Double> prices = new ConcurrentHashMap<>();

    private final SQLiteDatabase db;

    public MaterialPriceCache(Handler handler) {
        db = new SQLiteDatabase(new File(handler.getPluginInstance().getDataFolder(), "data.db"));
    }

    public void setPrice(Material material, double price) {
        prices.put(material, price);
        save(material);
    }

    public void deleteMaterial(Material material) {
        prices.remove(material);
        db.removeFromDB(XMaterial.matchXMaterial(material).name());
    }

    public double getPrice(Material material) {
        return prices.getOrDefault(material, 0D);
    }

    public void save(Material material) {
        db.setPrice(XMaterial.matchXMaterial(material).name(), getPrice(material));
    }

    public void load() {
        db.map(prices);
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

    public ConcurrentHashMap<Material, Double> getPrices() {
        return prices;
    }
}
