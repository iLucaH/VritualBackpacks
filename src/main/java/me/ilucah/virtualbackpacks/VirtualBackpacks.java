package me.ilucah.virtualbackpacks;

import me.ilucah.virtualbackpacks.api.VirtualBackpacksAPI;
import me.ilucah.virtualbackpacks.handler.Handler;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class VirtualBackpacks extends JavaPlugin {

    private static VirtualBackpacks instance;

    private Handler handler;
    private VirtualBackpacksAPI api;

    @Override
    public void onEnable() {
        instance = this;
        handler = new Handler(this);
        api = new VirtualBackpacksAPI(handler);
        handler.getBackpackManager().getPriceCache().load();
    }

    @Override
    public void onDisable() {
        try {
            if (!handler.getBackpackManager().getPriceCache().getDatabase().getConnection().isClosed())
                handler.getBackpackManager().getPriceCache().getDatabase().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        handler.getMultiplierManager().getHandle().unloadAll();
        try {
            if (!handler.getMultiplierManager().getHandle().getDatabase().getConnection().isClosed())
                handler.getMultiplierManager().getHandle().getDatabase().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public VirtualBackpacksAPI getAPI() {
        return api;
    }

    public static VirtualBackpacks getInstance() {
        return instance;
    }
}
