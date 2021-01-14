package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ChatBypass extends Module {

    public ChatBypass(){
        super("ChatBypass", Keyboard.KEY_NONE, Category.EXPLOIT);
    }
    boolean ticker = true;
    @EventTarget
    public void onSendPacket(EventSendPacket event){
        if(event.getPacket() instanceof C01PacketChatMessage){
            C01PacketChatMessage p = (C01PacketChatMessage) event.getPacket();
            if(p.getMessage().startsWith("/")){
                return;
            }
            ticker = !ticker;
            String finalmsg = "";
            ArrayList<String> splitshit = new ArrayList<>();
            String[] msg = p.getMessage().split(" ");
            for (int i = 0; i < msg.length; i++) {
                char[] characters = msg[i].toCharArray();
                for (int i2 = 0; i2 < characters.length; i2++) {

                    splitshit.add(characters[i2] + (ticker ? "\u05fc" : "\u05fc"));
                }
                splitshit.add(" ");
            }
            for (int i = 0; i < splitshit.size(); i++) {
                finalmsg += splitshit.get(i);
            }
            p.setMessage(finalmsg.replaceFirst("%", ""));
            splitshit.clear();
        }
    }

}
