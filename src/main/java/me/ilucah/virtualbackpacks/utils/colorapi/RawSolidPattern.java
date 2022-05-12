package me.ilucah.virtualbackpacks.utils.colorapi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawSolidPattern implements me.ilucah.virtualbackpacks.utils.colorapi.Pattern {
    
    Pattern pattern = Pattern.compile("<#([0-9A-Fa-f]{6})>");

    public String process(String string) {
        Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            if (color == null)
                color = matcher.group(2);
            string = string.replace(matcher.group(), "" + ColorAPI.getColor(color));
        }
        return string;
    }
}
