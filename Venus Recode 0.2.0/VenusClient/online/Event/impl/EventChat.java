package VenusClient.online.Event.impl;

import VenusClient.online.Event.Event;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class EventChat extends Event {

    private String message;

    public void EventChat(String message) {
        this.message = message;
    }

    public static void addchatmessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.AQUA + "["+ ChatFormatting.DARK_AQUA + "VenusClient Recode" + ChatFormatting.AQUA + "]" + ChatFormatting.WHITE + " " + message));
    }


}
