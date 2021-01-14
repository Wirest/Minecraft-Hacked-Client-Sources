package store.shadowclient.client.utils.player;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class PlayerUtils {
    public static boolean isInLiquid() {
        for(int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.boundingBox.minY, z);
                Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if(block != null && !(block instanceof BlockAir))
                    return block instanceof BlockLiquid;
            }
        }
        return false;
    }

    public static boolean isInsideBlock() {
        for(int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
            for(int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
                for(int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if(block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if(block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if(boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnSameTeam(EntityPlayer entity) {
        if (entity.getTeam() != null && Minecraft.getMinecraft().thePlayer.getTeam() != null) {
           char c1 = entity.getDisplayName().getFormattedText().charAt(1);
           char c2 = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().charAt(1);
           return c1 == c2;
        } else {
           return false;
        }
     }
    
    public static float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return (float)(Minecraft.getMinecraft().thePlayer.getMaxFallHeight() + f);
     }
    
    public static void damage() {
        double offset = 0.060100000351667404D;
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        for(int i = 0; (double)i < (double)getMaxFallDist() / 0.05510000046342611D + 1.0D; ++i) {
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404D, z, false));
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4D, z, false));
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
        
        }
        netHandler.addToSendQueue(new C03PacketPlayer(true));
     }
    
    
    
    public static boolean isMoving2() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0.0f || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0f;
    }
    
    public static void toFwd(double speed) {
        float f = Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292F;
        Minecraft.getMinecraft().thePlayer.motionX -= (double)MathHelper.sin(f) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ += (double)MathHelper.cos(f) * speed;
     }

    public static void setSpeed(final double n) {
        final double[] motionXZ = getMotionXZ(n);
        final double motionX = motionXZ[0];
        final double motionZ = motionXZ[1];
        Minecraft.getMinecraft().thePlayer.motionX = motionX;
        Minecraft.getMinecraft().thePlayer.motionZ = motionZ;
    }
    
    public static double[] getMotionXZ(final double n) {
        final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        float moveForward = movementInput.moveForward;
        float moveStrafe = movementInput.moveStrafe;
        final float yawByMovement = getYawByMovement();
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double cos = Math.cos(Math.toRadians(yawByMovement + 90.0f));
        final double sin = Math.sin(Math.toRadians(yawByMovement + 90.0f));
        return new double[] { moveForward * n * cos + moveStrafe * n * sin, moveForward * n * sin - moveStrafe * n * cos };
    }
    
    public static float getYawByMovement() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        final float moveForward = movementInput.moveForward;
        final float moveStrafe = movementInput.moveStrafe;
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
            }
        }
        return rotationYaw;
    }
    
    public static float getRandomFloat(final float n, final float n2) {
        return n + new Random().nextFloat() * (n2 - n);
    }
}
