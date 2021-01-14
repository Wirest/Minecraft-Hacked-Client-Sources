package com.etb.client.module.modules.world;

import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class AutoGG extends Module {
    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ","1st Place: ","Last team standing!","Winner #1 (", "Top Survivors","Winners - "};

    public AutoGG() {
        super("AutoGG", Category.WORLD, new Color(0xAEFFDE).getRGB());
        setDescription("Automatically gg's at the end of games.");
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : strings) {
                    if (packet.getChatComponent().getUnformattedText().contains(str)) {
                        mc.thePlayer.sendChatMessage("/achat gg");
                    }
                }
            }
        }
    }
}