// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockTallGrass extends BlockBush implements IGrowable
{
    public static final PropertyEnum<EnumType> TYPE;
    
    static {
        TYPE = PropertyEnum.create("type", EnumType.class);
    }
    
    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTallGrass.TYPE, EnumType.DEAD_BUSH));
        final float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.8f, 0.5f + f);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        return this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        final EnumType blocktallgrass$enumtype = state.getValue(BlockTallGrass.TYPE);
        return (blocktallgrass$enumtype == EnumType.DEAD_BUSH) ? 16777215 : ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (rand.nextInt(8) == 0) ? Items.wheat_seeds : null;
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, state.getValue(BlockTallGrass.TYPE).getMeta()));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 1; i < 3; ++i) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue(BlockTallGrass.TYPE) != EnumType.DEAD_BUSH;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
        if (state.getValue(BlockTallGrass.TYPE) == EnumType.FERN) {
            blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.double_plant.canPlaceBlockAt(worldIn, pos)) {
            Blocks.double_plant.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTallGrass.TYPE, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockTallGrass.TYPE).getMeta();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTallGrass.TYPE });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XYZ;
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEAD_BUSH("DEAD_BUSH", 0, 0, "dead_bush"), 
        GRASS("GRASS", 1, 1, "tall_grass"), 
        FERN("FERN", 2, 2, "fern");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blocktallgrass$enumtype = values[i];
                EnumType.META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype;
            }
        }
        
        private EnumType(final String name2, final int ordinal, final int meta, final String name) {
            this.meta = meta;
            this.name = name;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
