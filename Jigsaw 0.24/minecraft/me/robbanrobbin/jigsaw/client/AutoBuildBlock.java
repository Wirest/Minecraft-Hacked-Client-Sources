package me.robbanrobbin.jigsaw.client;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class AutoBuildBlock {

	public AutoBuildBlock(BlockPos pos, Block block) {
		this.pos = pos;
		this.block = block;
	}

	public boolean placed;

	public BlockPos pos;

	public Block block;

}
