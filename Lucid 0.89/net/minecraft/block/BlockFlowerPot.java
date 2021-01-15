package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot extends BlockContainer
{
    public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
    public static final PropertyEnum CONTENTS = PropertyEnum.create("contents", BlockFlowerPot.EnumFlowerType.class);

    public BlockFlowerPot()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, Integer.valueOf(0)));
        this.setBlockBoundsForItemRender();
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
	public void setBlockBoundsForItemRender()
    {
        float var1 = 0.375F;
        float var2 = var1 / 2.0F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 3;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    @Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityFlowerPot)
        {
            Item var5 = ((TileEntityFlowerPot)var4).getFlowerPotItem();

            if (var5 instanceof ItemBlock)
            {
                return Block.getBlockFromItem(var5).colorMultiplier(worldIn, pos, renderPass);
            }
        }

        return 16777215;
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack var9 = playerIn.inventory.getCurrentItem();

        if (var9 != null && var9.getItem() instanceof ItemBlock)
        {
            TileEntityFlowerPot var10 = this.getTileEntity(worldIn, pos);

            if (var10 == null)
            {
                return false;
            }
            else if (var10.getFlowerPotItem() != null)
            {
                return false;
            }
            else
            {
                Block var11 = Block.getBlockFromItem(var9.getItem());

                if (!this.canNotContain(var11, var9.getMetadata()))
                {
                    return false;
                }
                else
                {
                    var10.setFlowerPotData(var9.getItem(), var9.getMetadata());
                    var10.markDirty();
                    worldIn.markBlockForUpdate(pos);

                    if (!playerIn.capabilities.isCreativeMode && --var9.stackSize <= 0)
                    {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }

    private boolean canNotContain(Block blockIn, int meta)
    {
        return blockIn != Blocks.yellow_flower && blockIn != Blocks.red_flower && blockIn != Blocks.cactus && blockIn != Blocks.brown_mushroom && blockIn != Blocks.red_mushroom && blockIn != Blocks.sapling && blockIn != Blocks.deadbush ? blockIn == Blocks.tallgrass && meta == BlockTallGrass.EnumType.FERN.getMeta() : true;
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        TileEntityFlowerPot var3 = this.getTileEntity(worldIn, pos);
        return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotItem() : Items.flower_pot;
    }

    @Override
	public int getDamageValue(World worldIn, BlockPos pos)
    {
        TileEntityFlowerPot var3 = this.getTileEntity(worldIn, pos);
        return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotData() : 0;
    }

    /**
     * Returns true only if block is flowerPot
     */
    @Override
	public boolean isFlowerPot()
    {
        return true;
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityFlowerPot var4 = this.getTileEntity(worldIn, pos);

        if (var4 != null && var4.getFlowerPotItem() != null)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(var4.getFlowerPotItem(), 1, var4.getFlowerPotData()));
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        super.onBlockHarvested(worldIn, pos, state, player);

        if (player.capabilities.isCreativeMode)
        {
            TileEntityFlowerPot var5 = this.getTileEntity(worldIn, pos);

            if (var5 != null)
            {
                var5.setFlowerPotData((Item)null, 0);
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.flower_pot;
    }

    private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos)
    {
        TileEntity var3 = worldIn.getTileEntity(pos);
        return var3 instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)var3 : null;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        Object var3 = null;
        int var4 = 0;

        switch (meta)
        {
            case 1:
                var3 = Blocks.red_flower;
                var4 = BlockFlower.EnumFlowerType.POPPY.getMeta();
                break;

            case 2:
                var3 = Blocks.yellow_flower;
                break;

            case 3:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.OAK.getMetadata();
                break;

            case 4:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.SPRUCE.getMetadata();
                break;

            case 5:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.BIRCH.getMetadata();
                break;

            case 6:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.JUNGLE.getMetadata();
                break;

            case 7:
                var3 = Blocks.red_mushroom;
                break;

            case 8:
                var3 = Blocks.brown_mushroom;
                break;

            case 9:
                var3 = Blocks.cactus;
                break;

            case 10:
                var3 = Blocks.deadbush;
                break;

            case 11:
                var3 = Blocks.tallgrass;
                var4 = BlockTallGrass.EnumType.FERN.getMeta();
                break;

            case 12:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.ACACIA.getMetadata();
                break;

            case 13:
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.DARK_OAK.getMetadata();
        }

        return new TileEntityFlowerPot(Item.getItemFromBlock((Block)var3), var4);
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {CONTENTS, LEGACY_DATA});
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LEGACY_DATA)).intValue();
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        BlockFlowerPot.EnumFlowerType var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
        TileEntity var5 = worldIn.getTileEntity(pos);

        if (var5 instanceof TileEntityFlowerPot)
        {
            TileEntityFlowerPot var6 = (TileEntityFlowerPot)var5;
            Item var7 = var6.getFlowerPotItem();

            if (var7 instanceof ItemBlock)
            {
                int var8 = var6.getFlowerPotData();
                Block var9 = Block.getBlockFromItem(var7);

                if (var9 == Blocks.sapling)
                {
                    switch (BlockFlowerPot.SwitchEnumType.WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.byMetadata(var8).ordinal()])
                    {
                        case 1:
                            var4 = BlockFlowerPot.EnumFlowerType.OAK_SAPLING;
                            break;

                        case 2:
                            var4 = BlockFlowerPot.EnumFlowerType.SPRUCE_SAPLING;
                            break;

                        case 3:
                            var4 = BlockFlowerPot.EnumFlowerType.BIRCH_SAPLING;
                            break;

                        case 4:
                            var4 = BlockFlowerPot.EnumFlowerType.JUNGLE_SAPLING;
                            break;

                        case 5:
                            var4 = BlockFlowerPot.EnumFlowerType.ACACIA_SAPLING;
                            break;

                        case 6:
                            var4 = BlockFlowerPot.EnumFlowerType.DARK_OAK_SAPLING;
                            break;

                        default:
                            var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                }
                else if (var9 == Blocks.tallgrass)
                {
                    switch (var8)
                    {
                        case 0:
                            var4 = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                            break;

                        case 2:
                            var4 = BlockFlowerPot.EnumFlowerType.FERN;
                            break;

                        default:
                            var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                }
                else if (var9 == Blocks.yellow_flower)
                {
                    var4 = BlockFlowerPot.EnumFlowerType.DANDELION;
                }
                else if (var9 == Blocks.red_flower)
                {
                    switch (BlockFlowerPot.SwitchEnumType.FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, var8).ordinal()])
                    {
                        case 1:
                            var4 = BlockFlowerPot.EnumFlowerType.POPPY;
                            break;

                        case 2:
                            var4 = BlockFlowerPot.EnumFlowerType.BLUE_ORCHID;
                            break;

                        case 3:
                            var4 = BlockFlowerPot.EnumFlowerType.ALLIUM;
                            break;

                        case 4:
                            var4 = BlockFlowerPot.EnumFlowerType.HOUSTONIA;
                            break;

                        case 5:
                            var4 = BlockFlowerPot.EnumFlowerType.RED_TULIP;
                            break;

                        case 6:
                            var4 = BlockFlowerPot.EnumFlowerType.ORANGE_TULIP;
                            break;

                        case 7:
                            var4 = BlockFlowerPot.EnumFlowerType.WHITE_TULIP;
                            break;

                        case 8:
                            var4 = BlockFlowerPot.EnumFlowerType.PINK_TULIP;
                            break;

                        case 9:
                            var4 = BlockFlowerPot.EnumFlowerType.OXEYE_DAISY;
                            break;

                        default:
                            var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                }
                else if (var9 == Blocks.red_mushroom)
                {
                    var4 = BlockFlowerPot.EnumFlowerType.MUSHROOM_RED;
                }
                else if (var9 == Blocks.brown_mushroom)
                {
                    var4 = BlockFlowerPot.EnumFlowerType.MUSHROOM_BROWN;
                }
                else if (var9 == Blocks.deadbush)
                {
                    var4 = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                }
                else if (var9 == Blocks.cactus)
                {
                    var4 = BlockFlowerPot.EnumFlowerType.CACTUS;
                }
            }
        }

        return state.withProperty(CONTENTS, var4);
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        EMPTY("EMPTY", 0, "empty"),
        POPPY("POPPY", 1, "rose"),
        BLUE_ORCHID("BLUE_ORCHID", 2, "blue_orchid"),
        ALLIUM("ALLIUM", 3, "allium"),
        HOUSTONIA("HOUSTONIA", 4, "houstonia"),
        RED_TULIP("RED_TULIP", 5, "red_tulip"),
        ORANGE_TULIP("ORANGE_TULIP", 6, "orange_tulip"),
        WHITE_TULIP("WHITE_TULIP", 7, "white_tulip"),
        PINK_TULIP("PINK_TULIP", 8, "pink_tulip"),
        OXEYE_DAISY("OXEYE_DAISY", 9, "oxeye_daisy"),
        DANDELION("DANDELION", 10, "dandelion"),
        OAK_SAPLING("OAK_SAPLING", 11, "oak_sapling"),
        SPRUCE_SAPLING("SPRUCE_SAPLING", 12, "spruce_sapling"),
        BIRCH_SAPLING("BIRCH_SAPLING", 13, "birch_sapling"),
        JUNGLE_SAPLING("JUNGLE_SAPLING", 14, "jungle_sapling"),
        ACACIA_SAPLING("ACACIA_SAPLING", 15, "acacia_sapling"),
        DARK_OAK_SAPLING("DARK_OAK_SAPLING", 16, "dark_oak_sapling"),
        MUSHROOM_RED("MUSHROOM_RED", 17, "mushroom_red"),
        MUSHROOM_BROWN("MUSHROOM_BROWN", 18, "mushroom_brown"),
        DEAD_BUSH("DEAD_BUSH", 19, "dead_bush"),
        FERN("FERN", 20, "fern"),
        CACTUS("CACTUS", 21, "cactus");
        private final String name; 

        private EnumFlowerType(String p_i45715_1_, int p_i45715_2_, String name)
        {
            this.name = name;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        @Override
		public String getName()
        {
            return this.name;
        }
    }

    static final class SwitchEnumType
    {
        static final int[] WOOD_TYPE_LOOKUP;

        static final int[] FLOWER_TYPE_LOOKUP = new int[BlockFlower.EnumFlowerType.values().length];

        static
        {
            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.POPPY.ordinal()] = 1;
            }
            catch (NoSuchFieldError var15)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.BLUE_ORCHID.ordinal()] = 2;
            }
            catch (NoSuchFieldError var14)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.ALLIUM.ordinal()] = 3;
            }
            catch (NoSuchFieldError var13)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.HOUSTONIA.ordinal()] = 4;
            }
            catch (NoSuchFieldError var12)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.RED_TULIP.ordinal()] = 5;
            }
            catch (NoSuchFieldError var11)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.ORANGE_TULIP.ordinal()] = 6;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.WHITE_TULIP.ordinal()] = 7;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.PINK_TULIP.ordinal()] = 8;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                FLOWER_TYPE_LOOKUP[BlockFlower.EnumFlowerType.OXEYE_DAISY.ordinal()] = 9;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            WOOD_TYPE_LOOKUP = new int[BlockPlanks.EnumType.values().length];

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.OAK.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.SPRUCE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.BIRCH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.JUNGLE.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.ACACIA.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
