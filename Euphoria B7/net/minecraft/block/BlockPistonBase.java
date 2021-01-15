package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");

    /** This piston is the sticky one? */
    private final boolean isSticky;
    private static final String __OBFID = "CL_00000366";

    public BlockPistonBase(boolean p_i45443_1_)
    {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = p_i45443_1_;
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, func_180695_a(worldIn, pos, placer)), 2);

        if (!worldIn.isRemote)
        {
            this.func_176316_e(worldIn, pos, state);
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            this.func_176316_e(worldIn, pos, state);
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
        {
            this.func_176316_e(worldIn, pos, state);
        }
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, func_180695_a(worldIn, pos, placer)).withProperty(EXTENDED, Boolean.valueOf(false));
    }

    private void func_176316_e(World worldIn, BlockPos p_176316_2_, IBlockState p_176316_3_)
    {
        EnumFacing var4 = (EnumFacing)p_176316_3_.getValue(FACING);
        boolean var5 = this.func_176318_b(worldIn, p_176316_2_, var4);

        if (var5 && !((Boolean)p_176316_3_.getValue(EXTENDED)).booleanValue())
        {
            if ((new BlockPistonStructureHelper(worldIn, p_176316_2_, var4, true)).func_177253_a())
            {
                worldIn.addBlockEvent(p_176316_2_, this, 0, var4.getIndex());
            }
        }
        else if (!var5 && ((Boolean)p_176316_3_.getValue(EXTENDED)).booleanValue())
        {
            worldIn.setBlockState(p_176316_2_, p_176316_3_.withProperty(EXTENDED, Boolean.valueOf(false)), 2);
            worldIn.addBlockEvent(p_176316_2_, this, 1, var4.getIndex());
        }
    }

    private boolean func_176318_b(World worldIn, BlockPos p_176318_2_, EnumFacing p_176318_3_)
    {
        EnumFacing[] var4 = EnumFacing.values();
        int var5 = var4.length;
        int var6;

        for (var6 = 0; var6 < var5; ++var6)
        {
            EnumFacing var7 = var4[var6];

            if (var7 != p_176318_3_ && worldIn.func_175709_b(p_176318_2_.offset(var7), var7))
            {
                return true;
            }
        }

        if (worldIn.func_175709_b(p_176318_2_, EnumFacing.NORTH))
        {
            return true;
        }
        else
        {
            BlockPos var9 = p_176318_2_.offsetUp();
            EnumFacing[] var10 = EnumFacing.values();
            var6 = var10.length;

            for (int var11 = 0; var11 < var6; ++var11)
            {
                EnumFacing var8 = var10[var11];

                if (var8 != EnumFacing.DOWN && worldIn.func_175709_b(var9.offset(var8), var8))
                {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        EnumFacing var6 = (EnumFacing)state.getValue(FACING);

        if (!worldIn.isRemote)
        {
            boolean var7 = this.func_176318_b(worldIn, pos, var6);

            if (var7 && eventID == 1)
            {
                worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!var7 && eventID == 0)
            {
                return false;
            }
        }

        if (eventID == 0)
        {
            if (!this.func_176319_a(worldIn, pos, var6, true))
            {
                return false;
            }

            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (eventID == 1)
        {
            TileEntity var13 = worldIn.getTileEntity(pos.offset(var6));

            if (var13 instanceof TileEntityPiston)
            {
                ((TileEntityPiston)var13).clearPistonTileEntity();
            }

            worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.field_176426_a, var6).withProperty(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.func_176423_a(this.getStateFromMeta(eventParam), var6, false, true));

            if (this.isSticky)
            {
                BlockPos var8 = pos.add(var6.getFrontOffsetX() * 2, var6.getFrontOffsetY() * 2, var6.getFrontOffsetZ() * 2);
                Block var9 = worldIn.getBlockState(var8).getBlock();
                boolean var10 = false;

                if (var9 == Blocks.piston_extension)
                {
                    TileEntity var11 = worldIn.getTileEntity(var8);

                    if (var11 instanceof TileEntityPiston)
                    {
                        TileEntityPiston var12 = (TileEntityPiston)var11;

                        if (var12.func_174930_e() == var6 && var12.isExtending())
                        {
                            var12.clearPistonTileEntity();
                            var10 = true;
                        }
                    }
                }

                if (!var10 && var9.getMaterial() != Material.air && func_180696_a(var9, worldIn, var8, var6.getOpposite(), false) && (var9.getMobilityFlag() == 0 || var9 == Blocks.piston || var9 == Blocks.sticky_piston))
                {
                    this.func_176319_a(worldIn, pos, var6, false);
                }
            }
            else
            {
                worldIn.setBlockToAir(pos.offset(var6));
            }

            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "tile.piston.in", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        IBlockState var3 = access.getBlockState(pos);

        if (var3.getBlock() == this && ((Boolean)var3.getValue(EXTENDED)).booleanValue())
        {
            float var4 = 0.25F;
            EnumFacing var5 = (EnumFacing)var3.getValue(FACING);

            if (var5 != null)
            {
                switch (BlockPistonBase.SwitchEnumFacing.field_177243_a[var5.ordinal()])
                {
                    case 1:
                        this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 2:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                        break;

                    case 3:
                        this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 4:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                        break;

                    case 5:
                        this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 6:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
                }
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *  
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public static EnumFacing func_176317_b(int p_176317_0_)
    {
        int var1 = p_176317_0_ & 7;
        return var1 > 5 ? null : EnumFacing.getFront(var1);
    }

    public static EnumFacing func_180695_a(World worldIn, BlockPos p_180695_1_, EntityLivingBase p_180695_2_)
    {
        if (MathHelper.abs((float)p_180695_2_.posX - (float)p_180695_1_.getX()) < 2.0F && MathHelper.abs((float)p_180695_2_.posZ - (float)p_180695_1_.getZ()) < 2.0F)
        {
            double var3 = p_180695_2_.posY + (double)p_180695_2_.getEyeHeight();

            if (var3 - (double)p_180695_1_.getY() > 2.0D)
            {
                return EnumFacing.UP;
            }

            if ((double)p_180695_1_.getY() - var3 > 0.0D)
            {
                return EnumFacing.DOWN;
            }
        }

        return p_180695_2_.func_174811_aO().getOpposite();
    }

    public static boolean func_180696_a(Block p_180696_0_, World worldIn, BlockPos p_180696_2_, EnumFacing p_180696_3_, boolean p_180696_4_)
    {
        if (p_180696_0_ == Blocks.obsidian)
        {
            return false;
        }
        else if (!worldIn.getWorldBorder().contains(p_180696_2_))
        {
            return false;
        }
        else if (p_180696_2_.getY() >= 0 && (p_180696_3_ != EnumFacing.DOWN || p_180696_2_.getY() != 0))
        {
            if (p_180696_2_.getY() <= worldIn.getHeight() - 1 && (p_180696_3_ != EnumFacing.UP || p_180696_2_.getY() != worldIn.getHeight() - 1))
            {
                if (p_180696_0_ != Blocks.piston && p_180696_0_ != Blocks.sticky_piston)
                {
                    if (p_180696_0_.getBlockHardness(worldIn, p_180696_2_) == -1.0F)
                    {
                        return false;
                    }

                    if (p_180696_0_.getMobilityFlag() == 2)
                    {
                        return false;
                    }

                    if (p_180696_0_.getMobilityFlag() == 1)
                    {
                        if (!p_180696_4_)
                        {
                            return false;
                        }

                        return true;
                    }
                }
                else if (((Boolean)worldIn.getBlockState(p_180696_2_).getValue(EXTENDED)).booleanValue())
                {
                    return false;
                }

                return !(p_180696_0_ instanceof ITileEntityProvider);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean func_176319_a(World worldIn, BlockPos p_176319_2_, EnumFacing p_176319_3_, boolean p_176319_4_)
    {
        if (!p_176319_4_)
        {
            worldIn.setBlockToAir(p_176319_2_.offset(p_176319_3_));
        }

        BlockPistonStructureHelper var5 = new BlockPistonStructureHelper(worldIn, p_176319_2_, p_176319_3_, p_176319_4_);
        List var6 = var5.func_177254_c();
        List var7 = var5.func_177252_d();

        if (!var5.func_177253_a())
        {
            return false;
        }
        else
        {
            int var8 = var6.size() + var7.size();
            Block[] var9 = new Block[var8];
            EnumFacing var10 = p_176319_4_ ? p_176319_3_ : p_176319_3_.getOpposite();
            int var11;
            BlockPos var12;

            for (var11 = var7.size() - 1; var11 >= 0; --var11)
            {
                var12 = (BlockPos)var7.get(var11);
                Block var13 = worldIn.getBlockState(var12).getBlock();
                var13.dropBlockAsItem(worldIn, var12, worldIn.getBlockState(var12), 0);
                worldIn.setBlockToAir(var12);
                --var8;
                var9[var8] = var13;
            }

            IBlockState var19;

            for (var11 = var6.size() - 1; var11 >= 0; --var11)
            {
                var12 = (BlockPos)var6.get(var11);
                var19 = worldIn.getBlockState(var12);
                Block var14 = var19.getBlock();
                var14.getMetaFromState(var19);
                worldIn.setBlockToAir(var12);
                var12 = var12.offset(var10);
                worldIn.setBlockState(var12, Blocks.piston_extension.getDefaultState().withProperty(FACING, p_176319_3_), 4);
                worldIn.setTileEntity(var12, BlockPistonMoving.func_176423_a(var19, p_176319_3_, p_176319_4_, false));
                --var8;
                var9[var8] = var14;
            }

            BlockPos var16 = p_176319_2_.offset(p_176319_3_);

            if (p_176319_4_)
            {
                BlockPistonExtension.EnumPistonType var17 = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                var19 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.field_176326_a, p_176319_3_).withProperty(BlockPistonExtension.field_176325_b, var17);
                IBlockState var20 = Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.field_176426_a, p_176319_3_).withProperty(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
                worldIn.setBlockState(var16, var20, 4);
                worldIn.setTileEntity(var16, BlockPistonMoving.func_176423_a(var19, p_176319_3_, true, false));
            }

            int var18;

            for (var18 = var7.size() - 1; var18 >= 0; --var18)
            {
                worldIn.notifyNeighborsOfStateChange((BlockPos)var7.get(var18), var9[var8++]);
            }

            for (var18 = var6.size() - 1; var18 >= 0; --var18)
            {
                worldIn.notifyNeighborsOfStateChange((BlockPos)var6.get(var18), var9[var8++]);
            }

            if (p_176319_4_)
            {
                worldIn.notifyNeighborsOfStateChange(var16, Blocks.piston_head);
                worldIn.notifyNeighborsOfStateChange(p_176319_2_, this);
            }

            return true;
        }
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, func_176317_b(meta)).withProperty(EXTENDED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(EXTENDED)).booleanValue())
        {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, EXTENDED});
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_177243_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002037";

        static
        {
            try
            {
                field_177243_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_177243_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_177243_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_177243_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177243_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177243_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
