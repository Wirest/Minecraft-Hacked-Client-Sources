// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.text.DecimalFormat;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.Vec3;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static Minecraft mc;
    public static FontRenderer fr;
    private static boolean canTP;
    private static int delay;
    private static BlockPos endPos;
    
    static {
        Wrapper.mc = Minecraft.getMinecraft();
        Wrapper.fr = Minecraft.getMinecraft().fontRendererObj;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }
    
    public static boolean isMoving(final Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
    }
    
    public static BlockPos getBlockPos(final BlockPos inBlockPos) {
        return inBlockPos;
    }
    
    public static Block getBlockAtPos(final BlockPos inBlockPos) {
        final IBlockState s = Wrapper.mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + (Wrapper.mc.thePlayer.motionY + 0.1) - 1.0, inPlayer.posZ));
    }
    
    public static BlockPos getBlockPosUnderPlayer(final EntityPlayer inPlayer) {
        return new BlockPos(inPlayer.posX, inPlayer.posY + (Wrapper.mc.thePlayer.motionY + 0.1) - 1.0, inPlayer.posZ);
    }
    
    public static Block getBlockAbovePlayer(final EntityPlayer inPlayer, double blocks) {
        blocks += inPlayer.height;
        return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + blocks, inPlayer.posZ));
    }
    
    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlockAtPos(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }
    
    public static BlockPos getBlockPos(final double x, final double y, final double z) {
        return getBlockPos(new BlockPos(x, y, z));
    }
    
    public static Block getBlockUnderPlayer2(final EntityPlayer inPlayer, final double height) {
        return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }
    
    public static MovingObjectPosition getMouseOver() {
        return getMinecraft().objectMouseOver;
    }
    
    public static boolean isOnLiquid(AxisAlignedBB boundingBox) {
        boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        final int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                final Block block = getBlock(new BlockPos(x, y, z));
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static boolean isInLiquid(AxisAlignedBB par1AxisAlignedBB) {
        par1AxisAlignedBB = par1AxisAlignedBB.contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        if (getWorld().getChunkFromBlockCoords(new BlockPos(getPlayer().posX, getPlayer().posY, getPlayer().posZ)) == null) {
            return false;
        }
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = getBlock(new BlockPos(var11, var12, var13));
                    if (var14 instanceof BlockLiquid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }
    
    public static String getDirection() {
        return getMinecraft().func_175606_aa().func_174811_aO().name();
    }
    
    public Float[] getRotationToPosition(final Entity e, final BlockPos pos) {
        final double x = pos.getX() - e.posX;
        final double y = pos.getY() - (e.posY + e.getEyeHeight());
        final double z = pos.getZ() - e.posZ;
        final double helper = MathHelper.sqrt_double(x * x + z * z);
        float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
        final float newPitch = (float)(-Math.toDegrees(Math.atan(y / helper)));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return new Float[] { newYaw, newPitch };
    }
    
    public static boolean isInBlock(final Entity e, final float offset) {
        for (int x = MathHelper.floor_double(e.getEntityBoundingBox().minX); x < MathHelper.floor_double(e.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(e.getEntityBoundingBox().minY); y < MathHelper.floor_double(e.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(e.getEntityBoundingBox().minZ); z < MathHelper.floor_double(e.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = getWorld().getBlockState(new BlockPos(x, y + offset, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
                        final AxisAlignedBB boundingBox = block.getCollisionBoundingBox(getWorld(), new BlockPos(x, y + offset, z), getWorld().getBlockState(new BlockPos(x, y + offset, z)));
                        if (boundingBox != null && e.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static float[] getBlockRotations(final double x, final double y, final double z) {
        final double var4 = x - Wrapper.mc.thePlayer.posX + 0.5;
        final double var5 = z - Wrapper.mc.thePlayer.posZ + 0.5;
        final double var6 = y - (Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight() - 1.0);
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[] { var8, (float)(-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793)) };
    }
    
    public static float getDistanceToGround(final Entity e) {
        if (getPlayer().isCollidedVertically) {
            return 0.0f;
        }
        float a = (float)e.posY;
        while (a > 0.0f) {
            final int[] stairs = { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
            final int[] exemptIds = { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177 };
            final Block block = getBlock(new BlockPos(e.posX, a - 1.0f, e.posZ));
            if (!(block instanceof BlockAir)) {
                if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                    return ((float)(e.posY - a - 0.5) < 0.0f) ? 0.0f : ((float)(e.posY - a - 0.5));
                }
                int[] arrayOfInt1;
                for (int j = (arrayOfInt1 = stairs).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a - 1.0) < 0.0f) ? 0.0f : ((float)(e.posY - a - 1.0));
                    }
                }
                for (int j = (arrayOfInt1 = exemptIds).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a) < 0.0f) ? 0.0f : ((float)(e.posY - a));
                    }
                }
                return (float)(e.posY - a + block.getBlockBoundsMaxY() - 1.0);
            }
            else {
                --a;
            }
        }
        return 0.0f;
    }
    
    public static Block getBlockatPosSpeed(final EntityPlayer inPlayer, final float x, final float z) {
        final double posX = inPlayer.posX + inPlayer.motionX * x;
        final double posZ = inPlayer.posZ + inPlayer.motionZ * z;
        return getBlockAtPos(new BlockPos(posX, inPlayer.posY, posZ));
    }
    
    public static boolean isMoving() {
        return isMoving(Wrapper.mc.thePlayer);
    }
    
    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid() && getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid()) {
            onLiquid = true;
        }
        return onLiquid;
    }
    
    public static boolean isInLiquid() {
        boolean inLiquid = false;
        if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.0, 0.30000001192092896).getMaterial().isLiquid() || getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.0, -0.30000001192092896).getMaterial().isLiquid()) {
            inLiquid = true;
        }
        return inLiquid;
    }
    
    public static boolean isInFire() {
        boolean isInFire = false;
        if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.0, 0.30000001192092896).getMaterial() == Material.fire || getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.0, -0.30000001192092896).getMaterial() == Material.fire) {
            isInFire = true;
        }
        return isInFire;
    }
    
    public static String liquidCollision() {
        String colission = "";
        if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858).getMaterial().isLiquid()) {
            colission = "Positive";
        }
        if (getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858).getMaterial().isLiquid()) {
            colission = "Negative";
        }
        return colission;
    }
    
    public static boolean stairCollision() {
        boolean collision = false;
        if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockatPosSpeed(Wrapper.mc.thePlayer, 1.05f, 1.05f) instanceof BlockStairs) {
            collision = true;
        }
        return collision;
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Wrapper.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(Wrapper.mc.theWorld, new BlockPos(x, y, z), Wrapper.mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null && Wrapper.mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }
    
    public static void sendPacket(final Packet p) {
        Wrapper.mc.getNetHandler().getNetworkManager().sendPacket(p);
    }
    
    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }
    
    public static void sendPacketDirect(final Packet packet) {
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
    
    public static ItemStack getHeldItem(final EntityPlayer e) {
        return e.getHeldItem();
    }
    
    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        final double endX = endPos.getX() + 0.5;
        final double endY = endPos.getY() + 1.0;
        final double endZ = endPos.getZ() + 0.5;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            final boolean next = false;
            final double diffX = curX - endX;
            final double diffY = curY - endY;
            final double diffZ = curZ - endZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
    
    public static void vclip(final double blocks) {
        final boolean isNegative = blocks < 0.0;
        final double yPos = Wrapper.mc.thePlayer.posY + (blocks + 0.002);
        Wrapper.mc.thePlayer.setLocationAndAngles(Wrapper.mc.thePlayer.posX, yPos, Wrapper.mc.thePlayer.posZ, Wrapper.mc.thePlayer.rotationYaw, Wrapper.mc.thePlayer.rotationPitch);
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, yPos - (isNegative ? 0.1 : 0.0), Wrapper.mc.thePlayer.posZ, true));
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - (isNegative ? 1 : 0), Wrapper.mc.thePlayer.posZ, false));
    }
    
    public static void damagePlayer(final double damage) {
        for (int i = 0; i < 80.0 + 40.0 * (damage - 0.5); ++i) {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + 0.049, Wrapper.mc.thePlayer.posZ, false));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ, false));
        }
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ, true));
    }
    
    public static void bypassTP(final int x, final int y, final int z) {
        Wrapper.endPos = new BlockPos(x, y, z);
        final double[] startPos = { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ };
        blinkToPos(startPos, Wrapper.endPos, 0.0);
        Wrapper.mc.thePlayer.setPosition(Wrapper.endPos.getX() + 0.5, Wrapper.endPos.getY() + 1, Wrapper.endPos.getZ() + 0.5);
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.endPos.getX() + 0.5, Wrapper.endPos.getY() - 1.0, Wrapper.endPos.getZ() + 0.5, false));
    }
    
    public static void bypassTP2(int x, final int y, int z) {
        if (x < 0) {
            --x;
        }
        if (z < 0) {
            --z;
        }
        Wrapper.endPos = new BlockPos(x, y, z);
        final double[] startPos = { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ };
        blinkToPos(startPos, Wrapper.endPos, 0.0);
        Wrapper.mc.thePlayer.setPosition(Wrapper.endPos.getX() + 0.5, Wrapper.endPos.getY() + 1, Wrapper.endPos.getZ() + 0.5);
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.endPos.getX() + 0.5, Wrapper.endPos.getY() - 1.0, Wrapper.endPos.getZ() + 0.5, false));
    }
    
    public static void phaseTP(final double x, final double y, final double z) {
        FusionX.addChatMessage("a");
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.002, z, true));
        Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    }
    
    public static int removeDecimals(final double number) {
        final DecimalFormat decimalFormat = new DecimalFormat("####################################");
        return Integer.parseInt(decimalFormat.format(number));
    }
}
