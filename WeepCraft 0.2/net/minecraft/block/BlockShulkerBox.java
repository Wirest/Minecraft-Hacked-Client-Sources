package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShulkerBox extends BlockContainer
{
    public static final PropertyEnum<EnumFacing> field_190957_a = PropertyDirection.create("facing");
    private final EnumDyeColor field_190958_b;

    public BlockShulkerBox(EnumDyeColor p_i47248_1_)
    {
        super(Material.ROCK, MapColor.AIR);
        this.field_190958_b = p_i47248_1_;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_190957_a, EnumFacing.UP));
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityShulkerBox(this.field_190958_b);
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean causesSuffocation(IBlockState p_176214_1_)
    {
        return true;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return true;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else if (playerIn.isSpectator())
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityShulkerBox)
            {
                EnumFacing enumfacing = (EnumFacing)state.getValue(field_190957_a);
                boolean flag;

                if (((TileEntityShulkerBox)tileentity).func_190591_p() == TileEntityShulkerBox.AnimationStatus.CLOSED)
                {
                    AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB.addCoord((double)(0.5F * (float)enumfacing.getFrontOffsetX()), (double)(0.5F * (float)enumfacing.getFrontOffsetY()), (double)(0.5F * (float)enumfacing.getFrontOffsetZ())).func_191195_a((double)enumfacing.getFrontOffsetX(), (double)enumfacing.getFrontOffsetY(), (double)enumfacing.getFrontOffsetZ());
                    flag = !worldIn.collidesWithAnyBlock(axisalignedbb.offset(pos.offset(enumfacing)));
                }
                else
                {
                    flag = true;
                }

                if (flag)
                {
                    playerIn.addStat(StatList.field_191272_ae);
                    playerIn.displayGUIChest((IInventory)tileentity);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(field_190957_a, facing);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {field_190957_a});
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(field_190957_a)).getIndex();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(field_190957_a, enumfacing);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityShulkerBox)
        {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
            tileentityshulkerbox.func_190579_a(player.capabilities.isCreativeMode);
            tileentityshulkerbox.fillWithLoot(player);
        }
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityShulkerBox)
            {
                ((TileEntityShulkerBox)tileentity).func_190575_a(stack.getDisplayName());
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityShulkerBox)
        {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;

            if (!tileentityshulkerbox.func_190590_r() && tileentityshulkerbox.func_190582_F())
            {
                ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound.setTag("BlockEntityTag", ((TileEntityShulkerBox)tileentity).func_190580_f(nbttagcompound1));
                itemstack.setTagCompound(nbttagcompound);

                if (tileentityshulkerbox.hasCustomName())
                {
                    itemstack.setStackDisplayName(tileentityshulkerbox.getName());
                    tileentityshulkerbox.func_190575_a("");
                }

                spawnAsEntity(worldIn, pos, itemstack);
            }

            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(worldIn, pos, state);
    }

    public void func_190948_a(ItemStack p_190948_1_, @Nullable World p_190948_2_, List<String> p_190948_3_, ITooltipFlag p_190948_4_)
    {
        super.func_190948_a(p_190948_1_, p_190948_2_, p_190948_3_, p_190948_4_);
        NBTTagCompound nbttagcompound = p_190948_1_.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

            if (nbttagcompound1.hasKey("LootTable", 8))
            {
                p_190948_3_.add("???????");
            }

            if (nbttagcompound1.hasKey("Items", 9))
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>func_191197_a(27, ItemStack.field_190927_a);
                ItemStackHelper.func_191283_b(nbttagcompound1, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist)
                {
                    if (!itemstack.func_190926_b())
                    {
                        ++j;

                        if (i <= 4)
                        {
                            ++i;
                            p_190948_3_.add(String.format("%s x%d", itemstack.getDisplayName(), itemstack.func_190916_E()));
                        }
                    }
                }

                if (j - i > 0)
                {
                    p_190948_3_.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
                }
            }
        }
    }

    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntity tileentity = source.getTileEntity(pos);
        return tileentity instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox)tileentity).func_190584_a(state) : FULL_BLOCK_AABB;
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(pos));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack itemstack = super.getItem(worldIn, pos, state);
        TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
        NBTTagCompound nbttagcompound = tileentityshulkerbox.func_190580_f(new NBTTagCompound());

        if (!nbttagcompound.hasNoTags())
        {
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
        }

        return itemstack;
    }

    public static EnumDyeColor func_190955_b(Item p_190955_0_)
    {
        return func_190954_c(Block.getBlockFromItem(p_190955_0_));
    }

    public static EnumDyeColor func_190954_c(Block p_190954_0_)
    {
        return p_190954_0_ instanceof BlockShulkerBox ? ((BlockShulkerBox)p_190954_0_).func_190956_e() : EnumDyeColor.PURPLE;
    }

    public static Block func_190952_a(EnumDyeColor p_190952_0_)
    {
        switch (p_190952_0_)
        {
            case WHITE:
                return Blocks.field_190977_dl;

            case ORANGE:
                return Blocks.field_190978_dm;

            case MAGENTA:
                return Blocks.field_190979_dn;

            case LIGHT_BLUE:
                return Blocks.field_190980_do;

            case YELLOW:
                return Blocks.field_190981_dp;

            case LIME:
                return Blocks.field_190982_dq;

            case PINK:
                return Blocks.field_190983_dr;

            case GRAY:
                return Blocks.field_190984_ds;

            case SILVER:
                return Blocks.field_190985_dt;

            case CYAN:
                return Blocks.field_190986_du;

            case PURPLE:
            default:
                return Blocks.field_190987_dv;

            case BLUE:
                return Blocks.field_190988_dw;

            case BROWN:
                return Blocks.field_190989_dx;

            case GREEN:
                return Blocks.field_190990_dy;

            case RED:
                return Blocks.field_190991_dz;

            case BLACK:
                return Blocks.field_190975_dA;
        }
    }

    public EnumDyeColor func_190956_e()
    {
        return this.field_190958_b;
    }

    public static ItemStack func_190953_b(EnumDyeColor p_190953_0_)
    {
        return new ItemStack(func_190952_a(p_190953_0_));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(field_190957_a, rot.rotate((EnumFacing)state.getValue(field_190957_a)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(field_190957_a)));
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        p_193383_2_ = this.getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
        EnumFacing enumfacing = (EnumFacing)p_193383_2_.getValue(field_190957_a);
        TileEntityShulkerBox.AnimationStatus tileentityshulkerbox$animationstatus = ((TileEntityShulkerBox)p_193383_1_.getTileEntity(p_193383_3_)).func_190591_p();
        return tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.CLOSED && (tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.OPENED || enumfacing != p_193383_4_.getOpposite() && enumfacing != p_193383_4_) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
