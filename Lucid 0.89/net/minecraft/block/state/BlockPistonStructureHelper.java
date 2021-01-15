package net.minecraft.block.state;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPistonStructureHelper
{
    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;

    /**
     * This is a List<BlockPos> of all blocks that will be moved by the piston.
     */
    private final List toMove = Lists.newArrayList();

    /**
     * This is a List<BlockPos> of blocks that will be destroyed when a piston attempts to move them.
     */
    private final List toDestroy = Lists.newArrayList();

    public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending)
    {
        this.world = worldIn;
        this.pistonPos = posIn;

        if (extending)
        {
            this.moveDirection = pistonFacing;
            this.blockToMove = posIn.offset(pistonFacing);
        }
        else
        {
            this.moveDirection = pistonFacing.getOpposite();
            this.blockToMove = posIn.offset(pistonFacing, 2);
        }
    }

    public boolean canMove()
    {
        this.toMove.clear();
        this.toDestroy.clear();
        Block var1 = this.world.getBlockState(this.blockToMove).getBlock();

        if (!BlockPistonBase.canPush(var1, this.world, this.blockToMove, this.moveDirection, false))
        {
            if (var1.getMobilityFlag() != 1)
            {
                return false;
            }
            else
            {
                this.toDestroy.add(this.blockToMove);
                return true;
            }
        }
        else if (!this.func_177251_a(this.blockToMove))
        {
            return false;
        }
        else
        {
            for (int var2 = 0; var2 < this.toMove.size(); ++var2)
            {
                BlockPos var3 = (BlockPos)this.toMove.get(var2);

                if (this.world.getBlockState(var3).getBlock() == Blocks.slime_block && !this.func_177250_b(var3))
                {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean func_177251_a(BlockPos origin)
    {
        Block var2 = this.world.getBlockState(origin).getBlock();

        if (var2.getMaterial() == Material.air)
        {
            return true;
        }
        else if (!BlockPistonBase.canPush(var2, this.world, origin, this.moveDirection, false))
        {
            return true;
        }
        else if (origin.equals(this.pistonPos))
        {
            return true;
        }
        else if (this.toMove.contains(origin))
        {
            return true;
        }
        else
        {
            int var3 = 1;

            if (var3 + this.toMove.size() > 12)
            {
                return false;
            }
            else
            {
                while (var2 == Blocks.slime_block)
                {
                    BlockPos var4 = origin.offset(this.moveDirection.getOpposite(), var3);
                    var2 = this.world.getBlockState(var4).getBlock();

                    if (var2.getMaterial() == Material.air || !BlockPistonBase.canPush(var2, this.world, var4, this.moveDirection, false) || var4.equals(this.pistonPos))
                    {
                        break;
                    }

                    ++var3;

                    if (var3 + this.toMove.size() > 12)
                    {
                        return false;
                    }
                }

                int var10 = 0;
                int var5;

                for (var5 = var3 - 1; var5 >= 0; --var5)
                {
                    this.toMove.add(origin.offset(this.moveDirection.getOpposite(), var5));
                    ++var10;
                }

                var5 = 1;

                while (true)
                {
                    BlockPos var6 = origin.offset(this.moveDirection, var5);
                    int var7 = this.toMove.indexOf(var6);

                    if (var7 > -1)
                    {
                        this.func_177255_a(var10, var7);

                        for (int var8 = 0; var8 <= var7 + var10; ++var8)
                        {
                            BlockPos var9 = (BlockPos)this.toMove.get(var8);

                            if (this.world.getBlockState(var9).getBlock() == Blocks.slime_block && !this.func_177250_b(var9))
                            {
                                return false;
                            }
                        }

                        return true;
                    }

                    var2 = this.world.getBlockState(var6).getBlock();

                    if (var2.getMaterial() == Material.air)
                    {
                        return true;
                    }

                    if (!BlockPistonBase.canPush(var2, this.world, var6, this.moveDirection, true) || var6.equals(this.pistonPos))
                    {
                        return false;
                    }

                    if (var2.getMobilityFlag() == 1)
                    {
                        this.toDestroy.add(var6);
                        return true;
                    }

                    if (this.toMove.size() >= 12)
                    {
                        return false;
                    }

                    this.toMove.add(var6);
                    ++var10;
                    ++var5;
                }
            }
        }
    }

    private void func_177255_a(int p_177255_1_, int p_177255_2_)
    {
        ArrayList var3 = Lists.newArrayList();
        ArrayList var4 = Lists.newArrayList();
        ArrayList var5 = Lists.newArrayList();
        var3.addAll(this.toMove.subList(0, p_177255_2_));
        var4.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
        var5.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
        this.toMove.clear();
        this.toMove.addAll(var3);
        this.toMove.addAll(var4);
        this.toMove.addAll(var5);
    }

    private boolean func_177250_b(BlockPos p_177250_1_)
    {
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            EnumFacing var5 = var2[var4];

            if (var5.getAxis() != this.moveDirection.getAxis() && !this.func_177251_a(p_177250_1_.offset(var5)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a List<BlockPos> of all the blocks that are being moved by the piston.
     */
    public List getBlocksToMove()
    {
        return this.toMove;
    }

    /**
     * Returns an List<BlockPos> of all the blocks that are being destroyed by the piston.
     */
    public List getBlocksToDestroy()
    {
        return this.toDestroy;
    }
}
