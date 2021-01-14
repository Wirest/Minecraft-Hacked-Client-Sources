package moonx.ohare.client.module.impl.other;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.module.Module;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoGG extends Module {
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ","1st Place: ","Last team standing!","Winner #1 (", "Top Survivors","Winners - "};

    public AutoGG() {
        super("AutoGG", Category.OTHER, new Color(0xAEFFDE).getRGB());
        setDescription("Automatically ggs to be gay");
        setRenderLabel("Auto GG");
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : nigga) {
                    if (packet.getChatComponent().getUnformattedText().contains(str)) {
                        getMc().thePlayer.sendChatMessage("/achat gg");
                    }
                }
            }
        }
    }
}