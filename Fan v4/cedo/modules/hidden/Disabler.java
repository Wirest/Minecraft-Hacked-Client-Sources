package cedo.modules.hidden;

import cedo.Fan;
import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.util.BypassUtil;
import cedo.util.server.PacketUtil;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Disabler extends Module {

    public static boolean bypasses = true, wasEnabled;
    private final int randomPacket = Math.round(BypassUtil.range(-2, 2));
    public double offset, packetCount;

    public List<Packet<?>> packets = new ArrayList<>();

    boolean ran;
    int timesRun;
    private int ticksExisted;


    public Disabler() {
        super("Disabler", Keyboard.KEY_NONE, Category.EXPLOIT);

        setDisablerValues();
    }

    public void setDisablerValues() {
        if (Wrapper.authorized) {
            offset = 1.67346739E-7;
            packetCount = 30;
        }
    }

    public void toggle() {

    }

    public void setToggled(boolean toggled) {

    }

    public void onEvent(Event e) {
        if (!Fan.fly.isEnabled()) {
            ran = false;
            timesRun = 0;
        }


        switch (Fan.fly.mode.getSelected()) {
            case "Blink":
            case "Blinkless2":
                if (e instanceof EventPacket) {
                    EventPacket event = (EventPacket) e;
                    if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                        S32PacketConfirmTransaction packet = event.getPacket();
                        if (packet.getActionNumber() < 0) {
                            event.setCancelled(true);
                        }
                    }
                }
                break;

            case "Blinkless":
                if (e instanceof EventMotion && e.isPre()) {
                    if (mc.thePlayer.ticksExisted > 20) {
                        if (mc.thePlayer.ticksExisted % 36 == 0) {
                            PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                            playerCapabilities.isFlying = true;
                            PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer(false));
                        }
                    }
                }
                break;
        }
    }
}
