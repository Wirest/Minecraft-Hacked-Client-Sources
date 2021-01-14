
package me.memewaredevs.client.util.blocks.misc;

import me.memewaredevs.client.util.blocks.position.Vec3d;
import me.memewaredevs.client.util.misc.MinecraftUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockUtil implements MinecraftUtil {

    public static void placeHeldItemUnderPlayer() {
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1,
                mc.thePlayer.posZ);
        final Vec3d vec = new Vec3d(blockPos).addVector(0.4F, 0.4F, 0.4F);
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP,
                vec.scale(0.4));
    }
}
