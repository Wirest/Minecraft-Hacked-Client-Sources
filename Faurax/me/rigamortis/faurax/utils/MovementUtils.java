package me.rigamortis.faurax.utils;

import net.minecraft.client.*;
import net.minecraft.world.*;
import java.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class MovementUtils
{
    public Minecraft mc;
    
    public MovementUtils() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void teleport(final double x, final double y, final double z) {
        double playerX = this.mc.thePlayer.posX;
        double playerY = this.mc.thePlayer.posY;
        double playerZ = this.mc.thePlayer.posZ;
        double xDistance = x - playerX;
        double yDistance = y - playerY;
        double zDistance = z - playerZ;
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
        if (distance < 5.0) {
            this.setPosition(x, y, z);
            return;
        }
        distance /= 8.0;
        xDistance /= distance;
        yDistance /= distance;
        zDistance /= distance;
        for (int i = 0; i < distance; ++i) {
            playerX += xDistance;
            playerY += yDistance;
            playerZ += zDistance;
            this.setPosition(playerX, playerY, playerZ);
            try {
                Thread.sleep(10L);
            }
            catch (Exception ex) {}
        }
        this.setPosition(x, y, z);
    }
    
    public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = this.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(this.mc.theWorld, new BlockPos(x, y, z), this.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && this.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack) {
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
            this.mc.thePlayer.setPosition(curX, curY, curZ);
            ++count;
        }
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    private void setPosition(final double x, final double y, final double z) {
        this.mc.thePlayer.setPosition(x, y, z);
        this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
    }
    
    public boolean shouldSprint() {
        final boolean hasFood = this.mc.thePlayer.getFoodStats().getFoodLevel() > 6;
        final boolean isNotCollided = !this.mc.thePlayer.isCollidedHorizontally;
        final boolean isMoving = this.mc.thePlayer.motionX != 0.0 || this.mc.thePlayer.motionZ != 0.0;
        final boolean isSneaking = !this.mc.thePlayer.isSneaking();
        return hasFood && isSneaking && isNotCollided && isMoving;
    }
    
    public boolean shouldStep() {
        final boolean isCollided = this.mc.thePlayer.isCollidedHorizontally;
        final boolean onGround = this.mc.thePlayer.onGround;
        final boolean onLadder = !this.mc.thePlayer.isOnLadder();
        final boolean isInWater = !this.mc.thePlayer.isInWater();
        return isCollided && onLadder && isInWater && onGround;
    }
    
    public boolean isInLiquid() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.boundingBox.minY, z);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public boolean isOnAir() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            final int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
            if (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
                final BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.posY - 1, z);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                return block instanceof BlockAir;
            }
        }
        return false;
    }
    
    public int blocksUnderPlayer(final double posX, final double posY, final double posZ) {
        int height = 0;
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.posY - height, z);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if (block instanceof BlockAir) {
                    ++height;
                }
            }
        }
        return height;
    }
    
    public boolean isOnLiquidOld() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            final int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
            if (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
                final BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.posY - 1, z);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                return block instanceof BlockLiquid;
            }
        }
        return false;
    }
    
    public boolean isOnLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0).contract(0.001, 0.001, 0.001);
        int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var2 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var3 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        var4 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var7 = new Vec3(0.0, 0.0, 0.0);
        for (int var8 = var4; var8 < var2; ++var8) {
            for (int var9 = var3; var9 < var4; ++var9) {
                for (int var10 = var5; var10 < var6; ++var10) {
                    final Block var11 = (Block)Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(var8, var9, var10));
                    if (!(var11 instanceof BlockAir) && !(var11 instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
