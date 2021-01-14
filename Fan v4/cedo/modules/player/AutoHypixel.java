package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.util.time.Timer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class AutoHypixel extends Module {

    public String knownMode, knownType;
    public Timer delay = new Timer();

    public AutoHypixel() {
        super("AutoHypixel", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e) {
        if (e instanceof EventPacket && e.isPre() && e.isIncoming()) {
            EventPacket event = (EventPacket) e;
            if ((event.getPacket() instanceof S3EPacketTeams)) {
                String message = StringUtils.stripControlCodes(((S3EPacketTeams) event.getPacket()).func_149311_e());
                if (message.equals("Mode: Normal")) {
                    knownMode = "normal";
                }
                if (message.equals("Mode: Insane")) {
                    knownMode = "insane";
                }
                if (message.equals("Mode: Mega")) {
                    knownType = "mega";
                    knownMode = "normal";
                }
                if (message.equals("Teams left")) {
                    knownType = "teams";
                }
            }
            if (event.getPacket() instanceof S45PacketTitle) {
                if (((S45PacketTitle) event.getPacket()).getMessage() == null)
                    return;

                String message = ((S45PacketTitle) event.getPacket()).getMessage().getUnformattedText();
                if (message.equals("YOU DIED!") || message.equals("GAME END") || message.equals("VICTORY!") || message.equals("You are now a spectator!")) {
                    if (knownType != null && knownMode != null) {
                        if (delay.hasTimeElapsed(2000, true)) {
                            mc.thePlayer.sendChatMessage("/play " + knownType + "_" + knownMode);
                        }
                    }
                }
            }
            if (event.getPacket() instanceof S02PacketChat) {
                String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();
                if (message.equals("Teaming is not allowed on Solo mode!")) {
                    knownType = "solo";
                }
            }
        }
    }
}
