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
    PRICE_REMOVED("price-removed");

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
