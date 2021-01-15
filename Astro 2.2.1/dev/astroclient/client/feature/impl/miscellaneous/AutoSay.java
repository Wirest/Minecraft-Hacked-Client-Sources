package dev.astroclient.client.feature.impl.miscellaneous;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.event.impl.player.EventTick;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Toggleable(label = "AutoSay", category = Category.MISC)
public class AutoSay extends ToggleableFeature {

    private int counter;

    private String[] messages;

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 3000, 1, 1, 3000);

    private Timer timer = new Timer();

    public String[] loadMessages() {
        File f = new File(Client.INSTANCE.directory, "autosay.txt");

        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                //
            }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null)
                sb.append(nextLine).append("\n");
        } catch (IOException e) {
            //
        }
        return sb.toString().split("\n");
    }

    @Subscribe
    public void onEvent(EventReceivePacket event) {
        messages = loadMessages();
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            String message = packet.getChatComponent().getUnformattedText();
            if (message.contains("by") && message.contains(mc.thePlayer.getName() + ".")) {
                String[] words = message.split(" ");
                String entityName = words[0];
                if (!entityName.equals(mc.thePlayer.getName())) {
                    if (counter >= messages.length) counter = 0;
                    mc.thePlayer.sendChatMessage(String.format(messages[counter], entityName) + ".");
                    counter++;
                }
            }
        }
    }
}
