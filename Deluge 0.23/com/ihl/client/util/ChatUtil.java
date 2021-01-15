package com.ihl.client.util;

import com.ihl.client.Client;
import com.ihl.client.Helper;
import com.ihl.client.util.part.ChatColor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;

public class ChatUtil {

    public static String clearFormat(String message) {
        return message.replaceAll("(?i)\u00a7([a-f0-9klmnor])", "");
    }

    public static String addFormat(String message, String regex) {
        return message.replaceAll("(?i)" + regex + "([0-9a-fklmnor])", "\u00a7$1");
    }

    public static void send(String message) {
        if (Helper.world() != null && Helper.player() != null) {
            message = "[v][[t]" + Client.NAME + "[v]][t]: " + message;
            Helper.player().addChatMessage(makeComponent(message));
        }
    }

    private static IChatComponent makeComponent(String message) {
        message = message.replaceAll("\t", "    ");

        for(String key : ColorUtil.colors.keySet()) {
            ChatColor c = ColorUtil.colors.get(key);
            message = message.replace(c.regex, "&"+c.color);
        }

        message = addFormat(message, "&");
        String[] parts = message.split("\u00a7");
        IChatComponent icc = new ChatComponentText("");
        for (String part : parts) {
            if (part.length() > 0) {
                char c = part.charAt(0);
                part = part.substring(1);
                ChatStyle style = new ChatStyle();
                switch (c) {
                    case 'k':
                        style.setObfuscated(true);
                        break;
                    case 'l':
                        style.setBold(true);
                        break;
                    case 'm':
                        style.setUnderlined(true);
                        break;
                    case 'n':
                        style.setStrikethrough(true);
                        break;
                    case 'o':
                        style.setItalic(true);
                        break;
                    default:
                        style.setColor(charToFormat(c));
                        break;
                }
                String[] lines = part.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    icc.appendSibling(new ChatComponentText(line).setChatStyle(style));
                    if (i != lines.length - 1) {
                        icc.appendSibling(new ChatComponentText("\n"));
                    }
                }
            }
        }
        return icc;
    }

    public static EnumChatFormatting charToFormat(char c) {
        for (EnumChatFormatting ecf : EnumChatFormatting.values()) {
            if (ecf.formattingCode == c)
                return ecf;
        }
        return EnumChatFormatting.RESET;
    }
}
