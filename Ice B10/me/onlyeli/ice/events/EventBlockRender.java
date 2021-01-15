package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventBlockRender extends EventCancellable {

	private final Block block;
	private final BlockPos pos;
	private boolean renderFullBlock;

	public EventBlockRender(Block block, BlockPos pos) {
		this.block = block;
		this.pos = pos;
	}

	public Block getBlock() {
		return block;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setRenderFullBlock(boolean bool) {
		renderFullBlock = bool;
	}

	public boolean shouldRenderFullBlock() {
		return renderFullBlock;
	}

}
