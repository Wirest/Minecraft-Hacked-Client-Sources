// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockDoublePlant extends BlockBush implements IGrowable
{
    public static final PropertyEnum<EnumPlantType> VARIANT;
    public static final PropertyEnum<EnumBlockHalf> HALF;
    public static final PropertyEnum<EnumFacing> field_181084_N;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
        HALF = PropertyEnum.create("half", EnumBlockHalf.class);
        field_181084_N = BlockDirectional.FACING;
    }
    
    public BlockDoublePlant() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.VARIANT, EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.field_181084_N, EnumFacing.NORTH));
        this.setHardness(0.0f);
        this.setStepSound(BlockDoublePlant.soundTypeGrass);
        this.setUnlocalizedName("doublePlant");
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public EnumPlantType getVariant(final IBlockAccess worldIn, final BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            iblockstate = this.getActualState(iblockstate, worldIn, pos);
            return iblockstate.getValue(BlockDoublePlant.VARIANT);
        }
        return EnumPlantType.FERN;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            return true;
        }
        final EnumPlantType blockdoubleplant$enumplanttype = this.getActualState(iblockstate, worldIn, pos).getValue(BlockDoublePlant.VARIANT);
        return blockdoubleplant$enumplanttype == EnumPlantType.FERN || blockdoubleplant$enumplanttype == EnumPlantType.GRASS;
    }
    
    @Override
    protected void checkAndDropBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            final boolean flag = state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER;
            final BlockPos blockpos = flag ? pos : pos.up();
            final BlockPos blockpos2 = flag ? pos.down() : pos;
            final Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
            final Block block2 = flag ? worldIn.getBlockState(blockpos2).getBlock() : this;
            if (block == this) {
                worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
            }
            if (block2 == this) {
                worldIn.setBlockState(blockpos2, Blocks.air.getDefaultState(), 3);
                if (!flag) {
                    this.dropBlockAsItem(worldIn, blockpos2, state, 0);
                }
            }
        }
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos.up());
        return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            return null;
        }
        final EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
        return (blockdoubleplant$enumplanttype == EnumPlantType.FERN) ? null : ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? ((rand.nextInt(8) == 0) ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF) != EnumBlockHalf.UPPER && state.getValue(BlockDoublePlant.VARIANT) != EnumPlantType.GRASS) ? state.getValue(BlockDoublePlant.VARIANT).getMeta() : 0;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        final EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(worldIn, pos);
        return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
    }
    
    public void placeAt(final World worldIn, final BlockPos lowerPos, final EnumPlantType variant, final int flags) {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, variant), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER), flags);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER), 2);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (worldIn.isRemote || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(BlockDoublePlant.HALF) != EnumBlockHalf.LOWER || !this.onHarvest(worldIn, pos, state, player)) {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.down()).getBlock() == this) {
                if (!player.capabilities.isCreativeMode) {
                    final IBlockState iblockstate = worldIn.getBlockState(pos.down());
                    final EnumPlantType blockdoubleplant$enumplanttype = iblockstate.getValue(BlockDoublePlant.VARIANT);
                    if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                    else if (!worldIn.isRemote) {
                        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
                            this.onHarvest(worldIn, pos, iblockstate, player);
                            worldIn.setBlockToAir(pos.down());
                        }
                        else {
                            worldIn.destroyBlock(pos.down(), true);
                        }
                    }
                    else {
                        worldIn.setBlockToAir(pos.down());
                    }
                }
                else {
                    worldIn.setBlockToAir(pos.down());
                }
            }
        }
        else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this) {
            worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    private boolean onHarvest(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        final EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
        if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
            return false;
        }
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        final int i = ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
        Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 2, i));
        return true;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumPlantType[] values;
        for (int length = (values = EnumPlantType.values()).length, i = 0; i < length; ++i) {
            final EnumPlantType blockdoubleplant$enumplanttype = values[i];
            list.add(new ItemStack(itemIn, 1, blockdoubleplant$enumplanttype.getMeta()));
        }
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return this.getVariant(worldIn, pos).getMeta();
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        final EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(worldIn, pos);
        return blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        Block.spawnAsEntity(worldIn, pos, new ItemStack(this, 1, this.getVariant(worldIn, pos).getMeta()));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, EnumPlantType.byMetadata(meta & 0x7));
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(BlockDoublePlant.VARIANT, (EnumPlantType)iblockstate.getValue((IProperty<V>)BlockDoublePlant.VARIANT));
            }
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) ? (0x8 | state.getValue(BlockDoublePlant.field_181084_N).getHorizontalIndex()) : state.getValue(BlockDoublePlant.VARIANT).getMeta();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDoublePlant.HALF, BlockDoublePlant.VARIANT, BlockDoublePlant.field_181084_N });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
    
    public enum EnumBlockHalf implements IStringSerializable
    {
        UPPER("UPPER", 0), 
        LOWER("LOWER", 1);
        
        private EnumBlockHalf(final String name, final int ordinal) {
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumBlockHalf.UPPER) ? "upper" : "lower";
        }
    }
    
    public enum EnumPlantType implements IStringSerializable
    {
        SUNFLOWER("SUNFLOWER", 0, 0, "sunflower"), 
        SYRINGA("SYRINGA", 1, 1, "syringa"), 
        GRASS("GRASS", 2, 2, "double_grass", "grass"), 
        FERN("FERN", 3, 3, "double_fern", "fern"), 
        ROSE("ROSE", 4, 4, "double_rose", "rose"), 
        PAEONIA("PAEONIA", 5, 5, "paeonia");
        
        private static final EnumPlantType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumPlantType[values().length];
            EnumPlantType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumPlantType blockdoubleplant$enumplanttype = values[i];
                EnumPlantType.META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype;
            }
        }
        
        private EnumPlantType(final String s, final int n, final int meta, final String name) {
            this(s, n, meta, name, name);
        }
        
        private EnumPlantType(final String name2, final int ordinal, final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumPlantType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumPlantType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumPlantType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
    }
}
