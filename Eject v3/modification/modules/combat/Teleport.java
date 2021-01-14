package modification.modules.combat;

import modification.enummerates.Category;
import modification.events.EventMouseClicked;
import modification.events.EventPostMotion;
import modification.events.EventSendPacket;
import modification.extenders.Module;
import modification.interfaces.Event;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

public final class Teleport
        extends Module {
    private Vec3 endPos;
    private int count;

    public Teleport(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.endPos = null;
        this.count = 0;
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventMouseClicked)) && (((EventMouseClicked) paramEvent).button == 0) && (MC.objectMouseOver != null) && (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) {
            this.endPos = new Vec3(MC.objectMouseOver.getBlockPos());
        }
        if (((paramEvent instanceof EventPostMotion)) && (this.endPos != null)) {
            double d1 = Math.toRadians(MC.thePlayer.rotationYaw);
            double d2 = -Math.sin(d1) * 10.0D;
            double d3 = Math.cos(d1) * 10.0D;
            MC.thePlayer.setPositionAndUpdate(MC.thePlayer.posX + d2, MC.thePlayer.posY + 1.0D, MC.thePlayer.posZ + d3);
            MC.thePlayer.setPositionAndUpdate(MC.thePlayer.posX + d2, MC.thePlayer.posY - 1.0D, MC.thePlayer.posZ + d3);
            MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY + 1.0D, MC.thePlayer.posZ);
            MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY - 1.0D, MC.thePlayer.posZ);
        }
        if (((paramEvent instanceof EventSendPacket)) && (this.endPos != null)) {
            EventSendPacket localEventSendPacket = (EventSendPacket) paramEvent;
            if ((localEventSendPacket.packet instanceof C03PacketPlayer)) {
                ((C03PacketPlayer) localEventSendPacket.packet).onGround = true;
            }
        }
    }

    protected void onDeactivated() {
    }
}




