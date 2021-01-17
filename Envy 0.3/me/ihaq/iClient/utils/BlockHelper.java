package me.ihaq.iClient.utils;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockHelper {

	public static boolean isOnLiquid() {
		boolean onLiquid = false;
		int y = (int) (Minecraft.thePlayer.boundingBox.minY - 0.01D);
		for (int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}

	public static boolean isInLiquid() {
		boolean inLiquid = false;
		if ((Minecraft.thePlayer == null) || (Minecraft.thePlayer.boundingBox == null)) {
			return false;
		}
		int y = (int) Minecraft.thePlayer.boundingBox.minY;
		for (int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static boolean isInLiquidNew() {
		boolean inLiquid = false;
		int y = (int) Minecraft.thePlayer.boundingBox.minY;
		for (int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static BlockData getBlockData(BlockPos pos, List list) {
		Minecraft.getMinecraft();

		Minecraft.getMinecraft();

		Minecraft.getMinecraft();

		Minecraft.getMinecraft();

		Minecraft.getMinecraft();
		return !list.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())
				? new BlockData(pos.add(0, -1, 0), EnumFacing.UP)
				: !list.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())
						? new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST)
						: !list.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())
								? new BlockData(pos.add(1, 0, 0), EnumFacing.WEST)
								: !list.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())
										? new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH)
										: !list.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())
												? new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null;
	}

	public static Block getBlock(int x, int y, int z) {
		Minecraft.getMinecraft();
		return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
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
