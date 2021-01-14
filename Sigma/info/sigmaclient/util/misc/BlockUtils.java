package info.sigmaclient.util.misc;

import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.util.MinecraftUtil;
import info.sigmaclient.util.NetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.Arrays;
import java.util.List;

public class BlockUtils implements MinecraftUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static List<Block> blacklistedBlocks;

    static {
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever);
    }

    /**
     * Gets the yaw and pitch required to look at a given block
     *
     * @param pos
     * @return
     */
    public static float[] getRotationsNeeded(BlockPos pos) {
        double diffX = pos.getX() + 0.5 - mc.thePlayer.posX;
        double diffY = (pos.getY() + 0.5) - (mc.thePlayer.posY + mc.thePlayer.height);
        double diffZ = pos.getZ() + 0.5 - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double((diffX * diffX) + (diffZ * diffZ));
        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D) / 3.141592653589793D) - 90.0F;
        float pitch = (float) -((Math.atan2(diffY, dist) * 180.0D) / 3.141592653589793D);
        return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
    }

    /**
     * Updates the player's directions.
     */
    public static float[] updateDirections(BlockPos pos) {
        float[] looks = BlockUtils.getRotationsNeeded(pos);
        if (mc.thePlayer.isCollidedVertically) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C05PacketPlayerLook(looks[0], looks[1], mc.thePlayer.onGround));
        }
        return looks;
    }

    /**
     * Ensures that the best tool is used for breaking the block.
     */
    public static void updateTool(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0F;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null) {
                continue;
            }
            if ((itemStack.getStrVsBlock(block) > strength)) {
                strength = itemStack.getStrVsBlock(block);
                bestItemIndex = i;
            }
        }
        if (bestItemIndex != -1) {
            mc.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }

    // this is darkmagician's. credits to him.
    public static boolean isInLiquid() {
        if (mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) return false;
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    // this method is N3xuz_DK's I believe. credits to him.
    public static boolean isOnLiquid() {
        if (mc.thePlayer == null) return false;
        boolean onLiquid = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    public static boolean isOnLiquid(double profondeur)
	  {
	    boolean onLiquid = false;
	    
	    if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - profondeur, mc.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
	      onLiquid = true;
	    }
	    return onLiquid;
	  }
    public static boolean isTotalOnLiquid(double profondeur)
	  {	    
	    for(double x = mc.thePlayer.boundingBox.minX; x < mc.thePlayer.boundingBox.maxX; x +=0.01f){
	    	
			for(double z = mc.thePlayer.boundingBox.minZ; z < mc.thePlayer.boundingBox.maxZ; z +=0.01f){
				Block block = mc.theWorld.getBlockState(new BlockPos(x, mc.thePlayer.posY - profondeur,z)).getBlock();
    			if(!(block instanceof BlockLiquid) && !(block instanceof BlockAir)){
    				return false;
    			}
    		}
		}
	    return true;
	  }
    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Block> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }
}
