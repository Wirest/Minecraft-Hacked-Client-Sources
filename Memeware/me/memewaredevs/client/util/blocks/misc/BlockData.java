package me.memewaredevs.client.util.blocks.misc;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockData {
    public BlockData(BlockPos position, EnumFacing face) {
        this.position = position;
        this.face = face;
    }

    public EnumFacing face;
    public BlockPos position;
}
