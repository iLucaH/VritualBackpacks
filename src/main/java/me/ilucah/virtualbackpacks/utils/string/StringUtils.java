package me.ilucah.virtualbackpacks.utils.string;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.List;

public class StringUtils {

    public static void sendInteractiveMessage(Player player, String staticText, String command, List<String> hoverText) {
        TextComponent text = new TextComponent(staticText);
        text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        text.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(toString(hoverText))).create()));
        player.spigot().sendMessage(text);
    }

    public static String toString(List<String> list) {
        StringBuilder out = new StringBuilder();
        for (String o : list) {
            out.append(o);
            out.append("\n");
        }
        return out.toString();
    }
}
