package cedo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import java.util.Random;

public class BypassUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static float range(float min, float max) {
        return min + (new Random().nextFloat() * (max - min));
    }

    public static double range(double min, double max) {
        return min + (new Random().nextDouble() * (max - min));
    }

    public static int range(int min, int max) {
        return min + (new Random().nextInt() * (max - min));
    }

    public static boolean isBlockBelowSlippery() {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock().slipperiness == 0.98f;
    }

}
