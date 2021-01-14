package modification.modules.world;

import modification.enummerates.Category;
import modification.events.EventPreMotion;
import modification.extenders.Module;
import modification.interfaces.Event;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.Vec3;

public final class DeathBack
        extends Module {
    private Vec3 vec3;

    public DeathBack(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.vec3 = null;
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventPreMotion)) {
            if (MC.thePlayer.deathTime > 0) {
                this.vec3 = new Vec3(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ);
            }
            if (this.vec3 != null) {
                MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, MC.thePlayer.onGround));
                MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.vec3.xCoord, this.vec3.yCoord, this.vec3.zCoord, MC.thePlayer.onGround));
                this.vec3 = null;
            }
        }
    }

    protected void onDeactivated() {
    }
}




