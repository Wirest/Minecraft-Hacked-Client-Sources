// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state;

import java.util.Collection;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockPistonBase;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper
{
    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toMove;
    private final List<BlockPos> toDestroy;
    
    public BlockPistonStructureHelper(final World worldIn, final BlockPos posIn, final EnumFacing pistonFacing, final boolean extending) {
        this.toMove = (List<BlockPos>)Lists.newArrayList();
        this.toDestroy = (List<BlockPos>)Lists.newArrayList();
        this.world = worldIn;
        this.pistonPos = posIn;
        if (extending) {
            this.moveDirection = pistonFacing;
            this.blockToMove = posIn.offset(pistonFacing);
        }
        else {
            this.moveDirection = pistonFacing.getOpposite();
            this.blockToMove = posIn.offset(pistonFacing, 2);
        }
    }
    
    public boolean canMove() {
        this.toMove.clear();
        this.toDestroy.clear();
        final Block block = this.world.getBlockState(this.blockToMove).getBlock();
        if (!BlockPistonBase.canPush(block, this.world, this.blockToMove, this.moveDirection, false)) {
            if (block.getMobilityFlag() != 1) {
                return false;
            }
            this.toDestroy.add(this.blockToMove);
            return true;
        }
        else {
            if (!this.func_177251_a(this.blockToMove)) {
                return false;
            }
            for (int i = 0; i < this.toMove.size(); ++i) {
                final BlockPos blockpos = this.toMove.get(i);
                if (this.world.getBlockState(blockpos).getBlock() == Blocks.slime_block && !this.func_177250_b(blockpos)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private boolean func_177251_a(final BlockPos origin) {
        Block block = this.world.getBlockState(origin).getBlock();
        if (block.getMaterial() == Material.air) {
            return true;
        }
        if (!BlockPistonBase.canPush(block, this.world, origin, this.moveDirection, false)) {
            return true;
        }
        if (origin.equals(this.pistonPos)) {
            return true;
        }
        if (this.toMove.contains(origin)) {
            return true;
        }
        int i = 1;
        if (i + this.toMove.size() > 12) {
            return false;
        }
        while (block == Blocks.slime_block) {
            final BlockPos blockpos = origin.offset(this.moveDirection.getOpposite(), i);
            block = this.world.getBlockState(blockpos).getBlock();
            if (block.getMaterial() == Material.air || !BlockPistonBase.canPush(block, this.world, blockpos, this.moveDirection, false)) {
                break;
            }
            if (blockpos.equals(this.pistonPos)) {
                break;
            }
            if (++i + this.toMove.size() > 12) {
                return false;
            }
        }
        int i2 = 0;
        for (int j = i - 1; j >= 0; --j) {
            this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
            ++i2;
        }
        int j2 = 1;
        while (true) {
            final BlockPos blockpos2 = origin.offset(this.moveDirection, j2);
            final int k = this.toMove.indexOf(blockpos2);
            if (k > -1) {
                this.func_177255_a(i2, k);
                for (int l = 0; l <= k + i2; ++l) {
                    final BlockPos blockpos3 = this.toMove.get(l);
                    if (this.world.getBlockState(blockpos3).getBlock() == Blocks.slime_block && !this.func_177250_b(blockpos3)) {
                        return false;
                    }
                }
                return true;
            }
            block = this.world.getBlockState(blockpos2).getBlock();
            if (block.getMaterial() == Material.air) {
                return true;
            }
            if (!BlockPistonBase.canPush(block, this.world, blockpos2, this.moveDirection, true) || blockpos2.equals(this.pistonPos)) {
                return false;
            }
            if (block.getMobilityFlag() == 1) {
                this.toDestroy.add(blockpos2);
                return true;
            }
            if (this.toMove.size() >= 12) {
                return false;
            }
            this.toMove.add(blockpos2);
            ++i2;
            ++j2;
        }
    }
    
    private void func_177255_a(final int p_177255_1_, final int p_177255_2_) {
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list2 = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list3 = (List<BlockPos>)Lists.newArrayList();
        list.addAll(this.toMove.subList(0, p_177255_2_));
        list2.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
        list3.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
        this.toMove.clear();
        this.toMove.addAll(list);
        this.toMove.addAll(list2);
        this.toMove.addAll(list3);
    }
    
    private boolean func_177250_b(final BlockPos p_177250_1_) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (enumfacing.getAxis() != this.moveDirection.getAxis() && !this.func_177251_a(p_177250_1_.offset(enumfacing))) {
                return false;
            }
        }
        return true;
    }
    
    public List<BlockPos> getBlocksToMove() {
        return this.toMove;
    }
    
    public List<BlockPos> getBlocksToDestroy() {
        return this.toDestroy;
    }
}
