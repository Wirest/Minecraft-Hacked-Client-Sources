package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockBoundingBoxSubject extends EventCancellable {

    public int x;
    public int y;
    public int z;
    public Block block;
    public AxisAlignedBB boundingBox;
    public BlockPos blockPos;

    public BlockBoundingBoxSubject(Block block, AxisAlignedBB boundingBox, BlockPos blockPos, final int x, final int y, final int z) {
        this.block = block;
        this.boundingBox = boundingBox;
        this.blockPos = blockPos;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}

