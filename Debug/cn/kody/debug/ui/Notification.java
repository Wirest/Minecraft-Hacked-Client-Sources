package cn.kody.debug.ui;

import java.awt.Color;

import cn.kody.debug.Client;
import cn.kody.debug.utils.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Notification {
    public static void tellPlayer(final String p_i359_1_, final Type Type) {
        if (Type == Type.INFO) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§b[" + Client.CLIENT_NAME + "]§r§7 " + p_i359_1_));
        }
        else if (Type == Type.WARN) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§b[" + Client.CLIENT_NAME + "]§r§e " + p_i359_1_));
        }
        else if (Type == Type.ERROR) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§b[" + Client.CLIENT_NAME + "]§r§c " + p_i359_1_));
        }
        else if (Type == Type.IRC) {
            if (p_i359_1_ != null && Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(p_i359_1_));
            }
        }
    }

    public static int rainbow(final int n) {
        return Color.getHSBColor((float)(Math.ceil((System.currentTimeMillis() + n) / 10.0) % 360.0 / 360.0), 0.5f, 1.0f).getRGB();
    }
    
    public static int reAlpha(final int n, final float n2) {
        final Color color = new Color(n);
        return new Color(0.003921569f * color.getRed(), 0.003921569f * color.getGreen(), 0.003921569f * color.getBlue(), n2).getRGB();
    }
}
