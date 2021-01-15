package nivia.utils.utils;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import nivia.utils.Helper;

import java.util.Arrays;
import java.util.List;
public class BlockUtils {
    private List<Block> invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table);
    public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
        EntitySnowball temp = new EntitySnowball(Helper.world());
        temp.posX = (double)x + 0.5D;
        temp.posY = (double)y + 0.5D;
        temp.posZ = (double)z + 0.5D;
        temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
        temp.posY += (double)facing.getDirectionVec().getY() * 0.25D;
        temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
        return Helper.combatUtils().faceTarget(temp, 100,100, false);
    }
    public static float[] getAngles(EntityPlayerSP player, BlockPos blockPos) {
        double difX = blockPos.getX() + 0.5D - player.posX;
        double difY = blockPos.getY() - (player.posY + player.getEyeHeight());
        double difZ = blockPos.getZ() + 0.5D - player.posZ;
        double sqrt = Math.sqrt(difX * difX + difZ * difZ);
        float yaw = (float) (Math.atan2(difZ, difX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(difY, sqrt) * 180.0D / Math.PI);
        return new float[] { yaw, pitch };
    }
	public static boolean isOnLiquid() {
		boolean onLiquid = false;
		if (getBlockAtPosC(Helper.player(), 0.3F, 0.1F, 0.3F).getMaterial().isLiquid() &&
				getBlockAtPosC(Helper.player(), -0.3F, 0.1F, -0.3F).getMaterial().isLiquid()){
			onLiquid = true;
		}
		return onLiquid;
	}
	public static boolean isOnLadder() {
        if (Helper.player() == null) {
            return false;
        }
        boolean onLadder = false;
        final int y = (int)Helper.player().getEntityBoundingBox().offset(0.0, 1.0, 0.0).minY;
        for (int x = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minX); x < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minZ); z < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                        return false;
                    }
                    onLadder = true;
                }
            }
        }
        return onLadder || Helper.player().isOnLadder();
    }

    public static boolean isOnIce() {
        if (Helper.player() == null) {
            return false;
        }
        boolean onIce = false;
        final int y = (int)Helper.player().getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minX); x < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minZ); z < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockIce) && !(block instanceof BlockPackedIce)) {
                        return false;
                    }
                    onIce = true;
                }
            }
        }
        return onIce;
    }
    public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Helper.mc().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    AxisAlignedBB boundingBox;
                    Block block = Helper.mc().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block == null || block instanceof BlockAir || (boundingBox = block.getCollisionBoundingBox(Helper.mc().theWorld, new BlockPos(x, y, z), Helper.mc().theWorld.getBlockState(new BlockPos(x, y, z)))) == null
                            || !Helper.mc().thePlayer.boundingBox.intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }
    public Block getBlockByIDorName(String message){
        Block tBlock = null;
        try {
            tBlock = Block.getBlockById(Integer.parseInt(message));
        } catch (NumberFormatException e) {
            Block block = null;
            loop: {
                for (final Object object : Block.blockRegistry) {
                    block = (Block)object;
                    final String label = block.getLocalizedName().replace(" ", "");
                    if (label.toLowerCase().startsWith(message) || label.toLowerCase().contains(message)) {
                        break loop;
                    }
                }
            }
            if(block != null)
            tBlock = block;
        }
        return tBlock;
    }
    public static boolean isBlockUnderPlayer(Material material, float height) {
        if (getBlockAtPosC(Helper.player(), 0.31F, height, 0.31F).getMaterial() == material &&
                getBlockAtPosC(Helper.player(), -0.31F, height, -0.31F).getMaterial() == material &&
                getBlockAtPosC(Helper.player(), -0.31F, height, 0.31F).getMaterial() == material &&
                getBlockAtPosC(Helper.player(), 0.31F, height, -0.31F).getMaterial() == material){
            return true;
        }else{
            return false;
        }
    }
    public static boolean canSeeBlock(float x, float y, float z) {
        return getFacing(new BlockPos(x, y, z)) != null;
    }
    public static EnumFacing getFacing(BlockPos pos) {
        EnumFacing[] orderedValues = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN};
        EnumFacing[] var2 = orderedValues;
        int var3 = orderedValues.length;
        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing facing = var2[var4];
            EntitySnowball temp = new EntitySnowball(Helper.world());
            temp.posX = (double)pos.getX() + 0.5D;
            temp.posY = (double)pos.getY() + 0.5D;
            temp.posZ = (double)pos.getZ() + 0.5D;
            temp.posX += (double)facing.getDirectionVec().getX() * 0.5D;
            temp.posY += (double)facing.getDirectionVec().getY() * 0.5D;
            temp.posZ += (double)facing.getDirectionVec().getZ() * 0.5D;
            if(Helper.mc().thePlayer.canEntityBeSeen(temp)) {
                return facing;
            }
        }

        return null;
    }

    public BlockData getBlockData1(final BlockPos pos) {
        final List<Block> invalid = this.invalid;
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add = pos.add(-1, 0, 0);
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
      
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add2 = pos.add(1, 0, 0);
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add3 = pos.add(0, 0, -1);
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }

        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add4 = pos.add(0, 0, 1);
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockData blockData = null;
        return blockData;
    }
    
    public int getBlockSlot() {
	int i = 36;
	while (i < 45) {
	    ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
	    if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
		return i - 36;
	    }
	    ++i;
	}
	return -1;
    }

    public int getBestSlot() {
	if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBlock)
	    return Minecraft.getMinecraft().thePlayer.inventory.currentItem;

	for (int i = 0; i < 8; i++) {
	    if (Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i) != null) {
		if (Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
		    return i;
		}
	    }
	}
	return -1;
    }

    public static BlockData getBlockData(BlockPos pos, List list) {
        return !list.contains(Helper.mc().theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())? new BlockData(pos.add(0, -1, 0), EnumFacing.UP):(!list.contains(Helper.mc().theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())?new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST):(!list.contains(Helper.mc().theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())?new BlockData(pos.add(1, 0, 0), EnumFacing.WEST):(!list.contains(Helper.mc().theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())?new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH):(!list.contains(Helper.mc().theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())?new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH):null))));
    }
	public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
		return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
	}
    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
	}
    public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double height) {
        return getBlock(new BlockPos(inPlayer.posX, (inPlayer.posY + inPlayer.height) + height, inPlayer.posZ));
    }
    public static Block getBlock(final int x, final int y, final int z) {
        return Helper.world().getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return Helper.world().getBlockState(pos).getBlock();
    }
    
    public static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}
