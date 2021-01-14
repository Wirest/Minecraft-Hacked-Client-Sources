package modification.modules.player;

import modification.enummerates.Category;
import modification.events.EventProcessPacket;
import modification.extenders.Module;
import modification.interfaces.Event;
import net.minecraft.network.play.server.S02PacketChat;

public final class AutoPlay
        extends Module {
    public AutoPlay(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventProcessPacket)) {
            EventProcessPacket localEventProcessPacket = (EventProcessPacket) paramEvent;
            if ((localEventProcessPacket.packet instanceof S02PacketChat)) {
                S02PacketChat localS02PacketChat = (S02PacketChat) localEventProcessPacket.packet;
                if (localS02PacketChat.getChatComponent().getFormattedText().contains("Play Again")) {
                    MC.thePlayer.sendChatMessage("/playagain now");
                }
            }
        }
    }

    protected void onDeactivated() {
    }
}




