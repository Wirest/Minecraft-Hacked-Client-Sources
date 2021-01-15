package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection field_176426_a = BlockPistonExtension.field_176326_a;
    public static final PropertyEnum field_176425_b = BlockPistonExtension.field_176325_b;
    private static final String __OBFID = "CL_00000368";

    public BlockPistonMoving()
    {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176426_a, EnumFacing.NORTH).withProperty(field_176425_b, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0F);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }

    public static TileEntity func_176423_a(IBlockState p_176423_0_, EnumFacing p_176423_1_, boolean p_176423_2_, boolean p_176423_3_)
    {
        return new TileEntityPiston(p_176423_0_, p_176423_1_, p_176423_2_, p_176423_3_);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityPiston)
        {
            ((TileEntityPiston)var4).clearPistonTileEntity();
        }
        else
        {
            super.breakBlock(worldIn, pos, state);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    /**
     * Called when a player destroys this Block
     */
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos var4 = pos.offset(((EnumFacing)state.getValue(field_176426_a)).getOpposite());
        IBlockState var5 = worldIn.getBlockState(var4);

        if (var5.getBlock() instanceof BlockPistonBase && ((Boolean)var5.getValue(BlockPistonBase.EXTENDED)).booleanValue())
        {
            worldIn.setBlockToAir(var4);
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
        {
            worldIn.setBlockToAir(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            TileEntityPiston var6 = this.func_176422_e(worldIn, pos);

            if (var6 != null)
            {
                IBlockState var7 = var6.func_174927_b();
                var7.getBlock().dropBlockAsItem(worldIn, pos, var7, 0);
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        return null;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            worldIn.getTileEntity(pos);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityPiston var4 = this.func_176422_e(worldIn, pos);

        if (var4 == null)
        {
            return null;
        }
        else
        {
            float var5 = var4.func_145860_a(0.0F);

            if (var4.isExtending())
            {
                var5 = 1.0F - var5;
            }

            return this.func_176424_a(worldIn, pos, var4.func_174927_b(), var5, var4.func_174930_e());
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        TileEntityPiston var3 = this.func_176422_e(access, pos);

        if (var3 != null)
        {
            IBlockState var4 = var3.func_174927_b();
            Block var5 = var4.getBlock();

            if (var5 == this || var5.getMaterial() == Material.air)
            {
                return;
            }

            float var6 = var3.func_145860_a(0.0F);

            if (var3.isExtending())
            {
                var6 = 1.0F - var6;
            }

            var5.setBlockBoundsBasedOnState(access, pos);

            if (var5 == Blocks.piston || var5 == Blocks.sticky_piston)
            {
                var6 = 0.0F;
            }

            EnumFacing var7 = var3.func_174930_e();
            this.minX = var5.getBlockBoundsMinX() - (double)((float)var7.getFrontOffsetX() * var6);
            this.minY = var5.getBlockBoundsMinY() - (double)((float)var7.getFrontOffsetY() * var6);
            this.minZ = var5.getBlockBoundsMinZ() - (double)((float)var7.getFrontOffsetZ() * var6);
            this.maxX = var5.getBlockBoundsMaxX() - (double)((float)var7.getFrontOffsetX() * var6);
            this.maxY = var5.getBlockBoundsMaxY() - (double)((float)var7.getFrontOffsetY() * var6);
            this.maxZ = var5.getBlockBoundsMaxZ() - (double)((float)var7.getFrontOffsetZ() * var6);
        }
    }

    public AxisAlignedBB func_176424_a(World worldIn, BlockPos p_176424_2_, IBlockState p_176424_3_, float p_176424_4_, EnumFacing p_176424_5_)
    {
        if (p_176424_3_.getBlock() != this && p_176424_3_.getBlock().getMaterial() != Material.air)
        {
            AxisAlignedBB var6 = p_176424_3_.getBlock().getCollisionBoundingBox(worldIn, p_176424_2_, p_176424_3_);

            if (var6 == null)
            {
                return null;
            }
            else
            {
                double var7 = var6.minX;
                double var9 = var6.minY;
                double var11 = var6.minZ;
                double var13 = var6.maxX;
                double var15 = var6.maxY;
                double var17 = var6.maxZ;

                if (p_176424_5_.getFrontOffsetX() < 0)
                {
                    var7 -= (double)((float)p_176424_5_.getFrontOffsetX() * p_176424_4_);
                }
                else
                {
                    var13 -= (double)((float)p_176424_5_.getFrontOffsetX() * p_176424_4_);
                }

                if (p_176424_5_.getFrontOffsetY() < 0)
                {
                    var9 -= (double)((float)p_176424_5_.getFrontOffsetY() * p_176424_4_);
                }
                else
                {
                    var15 -= (double)((float)p_176424_5_.getFrontOffsetY() * p_176424_4_);
                }

                if (p_176424_5_.getFrontOffsetZ() < 0)
                {
                    var11 -= (double)((float)p_176424_5_.getFrontOffsetZ() * p_176424_4_);
                }
                else
                {
                    var17 -= (double)((float)p_176424_5_.getFrontOffsetZ() * p_176424_4_);
                }

                return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
            }
        }
        else
        {
            return null;
        }
    }

    private TileEntityPiston func_176422_e(IBlockAccess p_176422_1_, BlockPos p_176422_2_)
    {
        TileEntity var3 = p_176422_1_.getTileEntity(p_176422_2_);
        return var3 instanceof TileEntityPiston ? (TileEntityPiston)var3 : null;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(field_176426_a, BlockPistonExtension.func_176322_b(meta)).withProperty(field_176425_b, (meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(field_176426_a)).getIndex();

        if (state.getValue(field_176425_b) == BlockPistonExtension.EnumPistonType.STICKY)
        {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {field_176426_a, field_176425_b});
    }
}
