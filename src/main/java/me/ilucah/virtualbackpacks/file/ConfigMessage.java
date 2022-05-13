package me.ilucah.virtualbackpacks.file;

import me.ilucah.virtualbackpacks.VirtualBackpacks;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum ConfigMessage {

    NO_PERMISSION("no-permission"),
    INCORRECT_USAGE("incorrect-usage"),
    HELP("help"),
    PLAYER_ONLY("player-only"),
    INVALID_ITEM_IN_HAND("invalid-item-in-hand"),
    INVALID_NUMBER("invalid-number"),
    PRICE_SET("price-set"),
    PRICE_REMOVED("price-removed"),
    PLUGIN_RELOAD_SUCCESS("plugin-reload-success"),
    PLUGIN_RELOAD_FAILURE("plugin-reload-failure"),
    MULTIPLIER_VIEW("multiplier-view"),
    MULTIPLIER_SET("multiplier-set"),
    MULTIPLIER_ADD("multiplier-add"),
    MULTIPLIER_REMOVE("multiplier-remove"),
    MULTIPLIER_BOOSTER_ADD("multiplier-booster-add"),
    INVALID_TIME_UNIT("invalid-time-unit");

    private static final ConcurrentHashMap<String, List<String>> messages = new ConcurrentHashMap<>();

    private String s;

    ConfigMessage(String s) {
        this.s = s;
    }

    public List<String> get() {
        if (!messages.containsKey(s))
            messages.put(s, VirtualBackpacks.getInstance().getHandler().getFileManager().getMessages().getStringList(s));
        return messages.get(s);
    }
}
