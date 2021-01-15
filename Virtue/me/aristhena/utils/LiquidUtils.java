package me.aristhena.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class LiquidUtils {
    public static boolean isOnLiquid() {
        AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0).contract(0.001, 0.001, 0.001);
        int var4 = MathHelper.floor_double((double)par1AxisAlignedBB.minX);
        int var5 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxX + 1.0));
        int var6 = MathHelper.floor_double((double)par1AxisAlignedBB.minY);
        int var7 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxY + 1.0));
        int var8 = MathHelper.floor_double((double)par1AxisAlignedBB.minZ);
        int var9 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxZ + 1.0));
        Vec3 var11 = new Vec3(0.0, 0.0, 0.0);
        int var12 = var4;
        while (var12 < var5) {
            int var13 = var6;
            while (var13 < var7) {
                int var14 = var8;
                while (var14 < var9) {
                    Block var15 = Minecraft.getMinecraft().theWorld.getBlock(var12, var13, var14);
                    if (!(var15 instanceof BlockAir) && !(var15 instanceof BlockLiquid)) {
                        return false;
                    }
                    ++var14;
                }
                ++var13;
            }
            ++var12;
        }
        return true;
    }

    public static boolean isInLiquid() {
        AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001, 0.001, 0.001);
        int var4 = MathHelper.floor_double((double)par1AxisAlignedBB.minX);
        int var5 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxX + 1.0));
        int var6 = MathHelper.floor_double((double)par1AxisAlignedBB.minY);
        int var7 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxY + 1.0));
        int var8 = MathHelper.floor_double((double)par1AxisAlignedBB.minZ);
        int var9 = MathHelper.floor_double((double)(par1AxisAlignedBB.maxZ + 1.0));
        Vec3 var11 = new Vec3(0.0, 0.0, 0.0);
        int var12 = var4;
        while (var12 < var5) {
            int var13 = var6;
            while (var13 < var7) {
                int var14 = var8;
                while (var14 < var9) {
                    Block var15 = Minecraft.getMinecraft().theWorld.getBlock(var12, var13, var14);
                    if (var15 instanceof BlockLiquid) {
                        return true;
                    }
                    ++var14;
                }
                ++var13;
            }
            ++var12;
        }
        return false;
    }
}

