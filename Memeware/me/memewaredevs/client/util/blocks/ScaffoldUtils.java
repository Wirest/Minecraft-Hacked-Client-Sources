package me.memewaredevs.client.util.blocks;

import me.memewaredevs.client.util.blocks.misc.BlockInfo;
import me.memewaredevs.client.util.blocks.position.Vec3d;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;
import java.util.List;

public class ScaffoldUtils {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static List<Block> blockBlacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.chest,
			Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet,
			Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox,
			Blocks.iron_ore, Blocks.lapis_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
			Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
			Blocks.wooden_button, Blocks.lever, Blocks.enchanting_table);

	public static BlockInfo getBlockData(BlockPos var1) {
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock()))
			return new BlockInfo(var1.add(0, -1, 0), EnumFacing.UP);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock()))
			return new BlockInfo(var1.add(-1, 0, 0), EnumFacing.EAST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock()))
			return new BlockInfo(var1.add(1, 0, 0), EnumFacing.WEST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock()))
			return new BlockInfo(var1.add(0, 0, -1), EnumFacing.SOUTH);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock()))
			return new BlockInfo(var1.add(0, 0, 1), EnumFacing.NORTH);
		BlockPos add = var1.add(-1, 0, 0);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock()))
			return new BlockInfo(add.add(-1, 0, 0), EnumFacing.EAST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock()))
			return new BlockInfo(add.add(1, 0, 0), EnumFacing.WEST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock()))
			return new BlockInfo(add.add(0, 0, -1), EnumFacing.SOUTH);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock()))
			return new BlockInfo(add.add(0, 0, 1), EnumFacing.NORTH);
		BlockPos add2 = var1.add(1, 0, 0);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock()))
			return new BlockInfo(add2.add(-1, 0, 0), EnumFacing.EAST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock()))
			return new BlockInfo(add2.add(1, 0, 0), EnumFacing.WEST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock()))
			return new BlockInfo(add2.add(0, 0, -1), EnumFacing.SOUTH);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock()))
			return new BlockInfo(add2.add(0, 0, 1), EnumFacing.NORTH);
		BlockPos add3 = var1.add(0, 0, -1);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock()))
			return new BlockInfo(add3.add(-1, 0, 0), EnumFacing.EAST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock()))
			return new BlockInfo(add3.add(1, 0, 0), EnumFacing.WEST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock()))
			return new BlockInfo(add3.add(0, 0, -1), EnumFacing.SOUTH);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock()))
			return new BlockInfo(add3.add(0, 0, 1), EnumFacing.NORTH);
		BlockPos add4 = var1.add(0, 0, 1);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock()))
			return new BlockInfo(add4.add(-1, 0, 0), EnumFacing.EAST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock()))
			return new BlockInfo(add4.add(1, 0, 0), EnumFacing.WEST);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock()))
			return new BlockInfo(add4.add(0, 0, -1), EnumFacing.SOUTH);
		if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock()))
			return new BlockInfo(add4.add(0, 0, 1), EnumFacing.NORTH);
		return null;
	}

	public static boolean isEmpty(ItemStack stack) {
		return stack == null;
	}

	public static Vec3d getVec3d(BlockPos pos, EnumFacing face) {
		double x = pos.getX() + 0.5026836562;
		double y = pos.getY() + 0.5026836562;
		double z = pos.getZ() + 0.5026836562;
		x += (double) face.getFrontOffsetX() / 2;
		z += (double) face.getFrontOffsetZ() / 2;
		y += (double) face.getFrontOffsetY() / 2;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += randomNumber(0.30526836562, -0.30526836562);
			z += randomNumber(0.30526836562, -0.30526836562);
		} else {
			y += randomNumber(0.30526836562, -0.30526836562);
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += randomNumber(0.30526836562, -0.30526836562);
		}
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += randomNumber(0.30526836562, -0.30526836562);
		}
		return new Vec3d(x, y, z);
	}

	public static double randomNumber(double max, double min) {
		return (Math.random() * (max - min)) + min;
	}

	public static boolean invCheck() {
		for (int i = 36; i < 45; ++i) {
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !isValid(mc.thePlayer.inventoryContainer.getSlot(i).getStack()))
				continue;
			return false;
		}
		return true;
	}

	public static boolean isValid(ItemStack item) {
		if (isEmpty(item)) {
			return false;
		}
		if (item.getUnlocalizedName().equalsIgnoreCase("tile.chest")) {
			return false;
		}
		if (!(item.getItem() instanceof ItemBlock)) {
			return false;
		}
		return !blockBlacklist.contains(((ItemBlock) item.getItem()).getBlock());
	}

	public static boolean contains(Block block) {
		return blockBlacklist.contains(block);
	}

	public static int getBlockSlot() {
		for (int i = 36; i < 45; ++i) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock)
				if (!contains(((ItemBlock) stack.getItem()).getBlock()))
					return i - 36;
		}
		return -1;
	}

	public static void swap(int slot, int hotBarNumber) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotBarNumber, 2, mc.thePlayer);
	}
}
