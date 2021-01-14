package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.io.File;
import java.io.PrintWriter;

public class KillSay extends Module {

    public KillSay() {
        super("KillSults", 0, Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventPacket) {
            if (((EventPacket) e).getPacket() instanceof C01PacketChatMessage) {
                if (((C01PacketChatMessage) ((EventPacket) e).getPacket()).getMessage().contains("was Killed by " + mc.thePlayer.getName()) || ((C01PacketChatMessage) ((EventPacket) e).getPacket()).getMessage().contains("was Knocked into the void by " + mc.thePlayer.getName())) {
                    File file = new File(Minecraft.getMinecraft().mcDataDir + "/Fan/" + "killsults.txt");

                    String targetName = ((C01PacketChatMessage) ((EventPacket) e).getPacket()).getMessage().split("was")[0];

                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                            PrintWriter writer = new PrintWriter(file);
                            writer.println("%victim " + "was the reason why birth control was a thing");
                            writer.println("%victim " + "got raped");
                            writer.println("%victim " + "got cedoed");
                            writer.println("%victim " + "should buy fan");
                            writer.println("%victim " + "should download cracked vape");
                            writer.println("%victim " + "L");
                            writer.println("%victim " + "Clown");
                            writer.println("%victim " + "loooooooool");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (file.exists()) {

                    }
                }
            }
        }
    }
}