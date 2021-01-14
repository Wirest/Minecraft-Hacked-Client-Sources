package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventPostMotion;
import modification.events.EventSendPacket;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Fly
        extends Module {
    private final Value<String> mode = new Value("Mode", "Fast", new String[]{"Fast", "Intave", "Vanilla"}, this, new String[0]);
    private boolean flag;
    private double startX;
    private double startY;
    private double startZ;

    public Fly(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.flag = false;
        this.startX = MC.thePlayer.posX;
        this.startY = MC.thePlayer.posY;
        this.startZ = MC.thePlayer.posZ;
    }

    public void onEvent(Event paramEvent) {
        Object localObject;
        if ((paramEvent instanceof EventPostMotion)) {
            float f = 2.5F;
            localObject = (String) this.mode.value;
            int i = -1;
            switch (((String) localObject).hashCode()) {
                case 2182268:
                    if (((String) localObject).equals("Fast")) {
                        i = 0;
                    }
                    break;
                case 70739:
                    if (((String) localObject).equals("Fly")) {
                        i = 1;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    if (MC.thePlayer.ticksExisted << 4 == 0) {
                        MC.thePlayer.motionY = 0.02D;
                        MC.timer.timerSpeed = 0.3F;
                        if (MC.thePlayer.moveForward > 0.0F) {
                            Modification.ENTITY_UTIL.portPlayer(2.5D, false, 0.0D);
                        }
                    } else {
                        MC.thePlayer.motionY = -0.01D;
                        MC.timer.timerSpeed = 2.0F;
                    }
                    break;
                case 1:
                    MC.thePlayer.capabilities.isFlying = true;
            }
        }
        if (((paramEvent instanceof EventSendPacket)) && ("Intave".equals(this.mode.value))) {
            EventSendPacket localEventSendPacket = (EventSendPacket) paramEvent;
            if ((localEventSendPacket.packet instanceof C03PacketPlayer)) {
                localObject = (C03PacketPlayer) localEventSendPacket.packet;
                ((C03PacketPlayer) localObject).onGround = MC.thePlayer.onGround;
            }
        }
    }

    protected void onDeactivated() {
        MC.timer.timerSpeed = 1.0F;
        MC.thePlayer.motionX = 0.0D;
        MC.thePlayer.motionZ = 0.0D;
        MC.thePlayer.motionY = -0.3D;
        MC.thePlayer.capabilities.isFlying = false;
    }
}




