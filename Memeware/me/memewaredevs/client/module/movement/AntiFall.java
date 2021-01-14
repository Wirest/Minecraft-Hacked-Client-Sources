
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.function.Consumer;

public class AntiFall extends Module {

    public AntiFall() {
        super("Anti Fall", 0, Module.Category.MOVEMENT);
        this.addDouble("Distance", 5, 1, 10);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        if (!isBlockUnder() && mc.thePlayer.fallDistance >= getDouble("Distance"))
            PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.ticksExisted - 1.542E302, mc.thePlayer.posZ, mc.thePlayer.onGround));
    };

    public static boolean isBlockUnder() { // friend helped make this
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }
}
