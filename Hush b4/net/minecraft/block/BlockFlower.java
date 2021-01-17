// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Collection;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.util.IStringSerializable;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.BlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockFlower extends BlockBush
{
    protected PropertyEnum<EnumFlowerType> type;
    
    protected BlockFlower() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), (this.getBlockType() == EnumFlowerColor.RED) ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        EnumFlowerType[] types;
        for (int length = (types = EnumFlowerType.getTypes(this.getBlockType())).length, i = 0; i < length; ++i) {
            final EnumFlowerType blockflower$enumflowertype = types[i];
            list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(this.getTypeProperty(), EnumFlowerType.getType(this.getBlockType(), meta));
    }
    
    public abstract EnumFlowerColor getBlockType();
    
    public IProperty<EnumFlowerType> getTypeProperty() {
        if (this.type == null) {
            this.type = PropertyEnum.create("type", EnumFlowerType.class, new Predicate<EnumFlowerType>() {
                @Override
                public boolean apply(final EnumFlowerType p_apply_1_) {
                    return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
                }
            });
        }
        return this.type;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { this.getTypeProperty() });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
    
    public enum EnumFlowerColor
    {
        YELLOW("YELLOW", 0), 
        RED("RED", 1);
        
        private EnumFlowerColor(final String name, final int ordinal) {
        }
        
        public BlockFlower getBlock() {
            return (this == EnumFlowerColor.YELLOW) ? Blocks.yellow_flower : Blocks.red_flower;
        }
    }
    
    public enum EnumFlowerType implements IStringSerializable
    {
        DANDELION("DANDELION", 0, EnumFlowerColor.YELLOW, 0, "dandelion"), 
        POPPY("POPPY", 1, EnumFlowerColor.RED, 0, "poppy"), 
        BLUE_ORCHID("BLUE_ORCHID", 2, EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"), 
        ALLIUM("ALLIUM", 3, EnumFlowerColor.RED, 2, "allium"), 
        HOUSTONIA("HOUSTONIA", 4, EnumFlowerColor.RED, 3, "houstonia"), 
        RED_TULIP("RED_TULIP", 5, EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"), 
        ORANGE_TULIP("ORANGE_TULIP", 6, EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"), 
        WHITE_TULIP("WHITE_TULIP", 7, EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"), 
        PINK_TULIP("PINK_TULIP", 8, EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"), 
        OXEYE_DAISY("OXEYE_DAISY", 9, EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
        
        private static final EnumFlowerType[][] TYPES_FOR_BLOCK;
        private final EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        static {
            TYPES_FOR_BLOCK = new EnumFlowerType[EnumFlowerColor.values().length][];
            EnumFlowerColor[] values;
            for (int length = (values = EnumFlowerColor.values()).length, i = 0; i < length; ++i) {
                final EnumFlowerColor blockflower$enumflowercolor = values[i];
                final Collection<EnumFlowerType> collection = Collections2.filter(Lists.newArrayList(values()), new Predicate<EnumFlowerType>() {
                    private final /* synthetic */ EnumFlowerColor val$blockflower$enumflowercolor;
                    
                    @Override
                    public boolean apply(final EnumFlowerType p_apply_1_) {
                        return p_apply_1_.getBlockType() == this.val$blockflower$enumflowercolor;
                    }
                });
                EnumFlowerType.TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.toArray(new EnumFlowerType[collection.size()]);
            }
        }
        
        private EnumFlowerType(final String s, final int n, final EnumFlowerColor blockType, final int meta, final String name) {
            this(s, n, blockType, meta, name, name);
        }
        
        private EnumFlowerType(final String name2, final int ordinal, final EnumFlowerColor blockType, final int meta, final String name, final String unlocalizedName) {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public EnumFlowerColor getBlockType() {
            return this.blockType;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        public static EnumFlowerType getType(final EnumFlowerColor blockType, int meta) {
            final EnumFlowerType[] ablockflower$enumflowertype = EnumFlowerType.TYPES_FOR_BLOCK[blockType.ordinal()];
            if (meta < 0 || meta >= ablockflower$enumflowertype.length) {
                meta = 0;
            }
            return ablockflower$enumflowertype[meta];
        }
        
        public static EnumFlowerType[] getTypes(final EnumFlowerColor flowerColor) {
            return EnumFlowerType.TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }
        
        @Override
        public String toString() {
            return this.name;
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
