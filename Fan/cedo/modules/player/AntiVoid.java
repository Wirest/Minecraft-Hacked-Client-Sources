package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            if (!isBlockUnder() && mc.thePlayer.fallDistance > 3 && mc.thePlayer.ticksExisted % 5 == 0) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextFloat(6F, 10F), mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.ticksExisted % 2 == 0));
            }
        }
    }
}