// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Utils;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public class ChatUtils
{
    public static void sendMessageToPlayer(final String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§8[§aEuphoria§8]:§r " + msg));
    }
    
    public static void sendMessageFromPlayer(final String msg) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(msg);
    }
}
