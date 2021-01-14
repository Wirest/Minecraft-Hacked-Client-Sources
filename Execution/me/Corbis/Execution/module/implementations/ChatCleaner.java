package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ChatCleaner extends Module {

    public ChatCleaner() {
        super("ChatCleaner", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();
            String message = packet.getMessage();
            if(message.startsWith("/")){
                return;
            }
            List<String> split = Arrays.asList(message.split(" "));
            ArrayList<String> form = new ArrayList<>();
            for (String s : split) {
                String replacement = s;
                if (s.equalsIgnoreCase("i")) {
                    replacement = "I";
                } else if (s.equalsIgnoreCase("dont")) {
                    replacement = "don't";
                } else if (s.equalsIgnoreCase("Theyre")) {
                    replacement = "they're";
                } else if (s.equalsIgnoreCase("k") || s.equalsIgnoreCase("kk") || s.equalsIgnoreCase("ok")) {
                    replacement = "okay";
                }
                if (split.indexOf(s) == 0) {
                    replacement = replacement.substring(0, 1).toUpperCase() + replacement.substring(1).toLowerCase();
                }
                if (split.indexOf(s) == split.size() - 1) {
                    form.add(replacement + ".");
                } else {
                    form.add(replacement + " ");
                }
            }
            StringJoiner joined = new StringJoiner("");
            for (String s : form) {
                joined.add(s);
            }
            String result = joined.toString();
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C01PacketChatMessage(result));
            event.setCancelled(true);
        }
    }


}
