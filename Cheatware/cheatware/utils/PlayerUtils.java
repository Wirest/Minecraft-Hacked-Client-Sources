package cheatware.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

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
}
