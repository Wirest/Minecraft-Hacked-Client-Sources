package moonx.ohare.client.module.impl.other;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.awt.*;

public class AntiFreeze extends Module {
    private BooleanValue safetp = new BooleanValue("SafeTP",true);
    public AntiFreeze() {
        super("AntiFreeze", Category.OTHER, new Color(0xA25B41).getRGB());
        setDescription("Anti freeze screen");
        setRenderLabel("Anti Freeze");
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2DPacketOpenWindow) {
            S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) event.getPacket();
            if (packetOpenWindow.getWindowTitle().getUnformattedText().toLowerCase().contains("frozen")) {
                event.setCanceled(true);
            }
        }

        if (event.getPacket() instanceof S02PacketChat && safetp.isEnabled()) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getFormattedText().contains("is currently frozen, you may not attack.")) {
                getMc().thePlayer.setPositionAndUpdate(0, -999, 0);
                Printer.print("The person you tried to attack was frozen, teleported you to spawn.");
            }
        }
    }
}
