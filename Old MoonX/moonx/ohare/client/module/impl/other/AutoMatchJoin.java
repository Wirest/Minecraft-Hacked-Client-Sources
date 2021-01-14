package moonx.ohare.client.module.impl.other;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;

public class AutoMatchJoin extends Module {
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};
    private TimerUtil timer = new TimerUtil();
    public EnumValue<Type> type = new EnumValue<>("Type", Type.Insane);
    public BooleanValue autoDetect = new BooleanValue("Auto Detect", true);
    public BooleanValue teams = new BooleanValue("Teams only mode", true);
    private NumberValue<Long> delay = new NumberValue<>("Delay",3000L,1000L,10000L,50L);
    private boolean allowedToSend;

    public AutoMatchJoin() {
        super("AutoMatchJoin", Category.OTHER, new Color(0xFF9FAB).getRGB());
        setRenderLabel("Auto Match Join");
    }

    public enum Type {
        Normal, Insane
    }

    @Override
    public void onEnable() {
        allowedToSend = false;
    }

    @Handler
    public void onTick(TickEvent event) {
        if (getMc().thePlayer != null) {
            if (allowedToSend) {
                if (timer.reach(delay.getValue())) {
                    switch (type.getValue()) {
                        case Normal:
                            getMc().thePlayer.sendChatMessage(teams.isEnabled() ? "/play teams_normal" : "/play solo_normal");
                            allowedToSend = false;
                            break;
                        case Insane:
                            getMc().thePlayer.sendChatMessage(teams.isEnabled() ? "/play teams_insane" : "/play solo_insane");
                            allowedToSend = false;
                            break;
                    }
                }
            }
        }
        if (!allowedToSend) timer.reset();
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (getMc().theWorld == null) {
            allowedToSend = false;
            return;
        }
        if (!event.isSending() && !allowedToSend) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : nigga) {
                    if (packet.getChatComponent().getUnformattedText().contains(str) && GuiIngame.isInSkywars) {
                        Moonx.INSTANCE.getNotificationManager().addNotification("Automatically joining another game.", 1500);
                        Printer.print("Automatically joining another game.");
                        allowedToSend = true;
                    }
                }
            }
        }
    }
}
