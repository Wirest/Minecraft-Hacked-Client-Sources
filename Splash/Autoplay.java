package splash.client.modules.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.network.EventPacketReceive;

public class Autoplay extends Module {

    public Autoplay() {
        super("Autoplay", "Auto joins games.", ModuleCategory.MISC);
    }

    @Collect
    public void onEventReceive(EventPacketReceive eventPacketReceive) {
        if (eventPacketReceive.getReceivedPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) eventPacketReceive.getReceivedPacket();

            if (!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
                String message = s02PacketChat.getChatComponent().getUnformattedText();

                if (message.contains("You won! Want to play again?") || message.contains("You died! Want to play again?") && isHypixel()) {
                    mc.getNetHandler().addToSendQueueNoEvent(new C01PacketChatMessage("/play solo_insane"));
                    if (message.contains("You won! Want to play again?") && isHypixel() && mc.thePlayer.ticksExisted % 25 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C01PacketChatMessage("/play solo_insane"));
                    }
                }

            }
        }
    }
    public boolean isHypixel () {
        return mc.getCurrentServerData().serverIP.contains("hypixel");
    }
}
