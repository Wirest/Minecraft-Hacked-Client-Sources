package cedo.util;

import cedo.Wrapper;
import cedo.util.server.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

    public static void damage() {
        double damageOffset = 0, damageY = 0, damageYTwo = 0;
        if (Wrapper.authorized) {
            damageOffset = 0.06011F;
            damageY = 0.000495765F;
            damageYTwo = 0.0049575F;
        }
        NetHandlerPlayClient netHandler = mc.getNetHandler();
        EntityPlayerSP player = mc.thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        for (int i = 0; i < (getMaxFallDist() / (damageOffset - 0.005F)) + 1; i++) {
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + damageOffset, z, false));
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + damageY, z, false));
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + damageYTwo + damageOffset * 0.000001, z, false));
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? (potioneffect.getAmplifier() + 1) : 0;
        return mc.thePlayer.getMaxFallHeight() + f;
    }
}
