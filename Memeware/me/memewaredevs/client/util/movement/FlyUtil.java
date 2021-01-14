
package me.memewaredevs.client.util.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class FlyUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static double getGroundLevel() {
        for (int i = (int) Math.round(FlyUtil.mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = FlyUtil.mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (!FlyUtil.isColliding(box) || !(box.minY <= FlyUtil.mc.thePlayer.posY)) {
                continue;
            }
            return i;
        }
        return 0.0;
    }

    public static boolean isColliding(final AxisAlignedBB box) {
        return FlyUtil.mc.theWorld.checkBlockCollision(box);
    }

    public static double fall() {
        double i;
        for (i = FlyUtil.mc.thePlayer.posY; i > FlyUtil.getGroundLevel(); i -= 8.0) {
            if (i < FlyUtil.getGroundLevel()) {
                i = FlyUtil.getGroundLevel();
            }
            FlyUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    FlyUtil.mc.thePlayer.posX, i, FlyUtil.mc.thePlayer.posZ, true));
        }
        return i;
    }

    public static void ascend() {
        for (double i = FlyUtil.getGroundLevel(); i < FlyUtil.mc.thePlayer.posY; i += 8.0) {
            if (i > FlyUtil.mc.thePlayer.posY) {
                i = FlyUtil.mc.thePlayer.posY;
            }
            FlyUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    FlyUtil.mc.thePlayer.posX, i, FlyUtil.mc.thePlayer.posZ, true));
        }
    }
}
