// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.util.Arrays;
import me.CheerioFX.FusionX.utils.Property.Property;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.client.Minecraft;

public class Logger
{
    public static void logChat(final String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§7[§c" + FusionX.theClient.Client_Name + "§7] " + message));
        }
        else {
            System.out.println(message);
        }
    }
    
    public static void logToggleMessage(final String context, final boolean enabled) {
        final Object[] arrobject = { "§e", context, "§7", enabled ? "§aenabled" : "§cdisabled", "§7" };
        logChat(String.format("%s%s%s has been %s%s.", arrobject));
    }
    
    public static void logToggleMessage(final String context, final boolean enabled, final String moduleLabel) {
        final Object[] arrobject = { "§e", context, "§7", String.valueOf(String.valueOf(enabled ? "§aenabled" : "§cdisabled")) + "§7", moduleLabel };
        logChat(String.format("%s%s%s has been %s for %s.", arrobject));
    }
    
    public static void logSetMessage(final String context, final String property) {
        logChat(String.format("%s%s%s set to %s%s%s.", "§e", context, "§7", "§e", property, "§7"));
    }
    
    public static void logSetMessage(final String Module2, final String context, final Property property) {
        final Object[] arrobject = { Module2, "§e", context, "§7", "§e", null, "§7" };
        logChat(String.format("%s's %s%s §7set to %s%s%s.", arrobject));
    }
    
    public static String LogExecutionFail(final String context, final String[] executors) {
        logChat(String.format("Invalid %s %s.", context, Arrays.toString(executors)));
        return String.format("Invalid %s %s.", context, Arrays.toString(executors));
    }
    
    public static String LogExecutionFail(final String context) {
        logChat(String.format("Invalid %s", context));
        return String.format("Invalid %s.", context);
    }
}
