package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower extends BlockBush {
   protected PropertyEnum type;

   protected BlockFlower() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION));
   }

   public int damageDropped(IBlockState state) {
      return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      BlockFlower.EnumFlowerType[] var4 = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFlower.EnumFlowerType blockflower$enumflowertype = var4[var6];
         list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.getType(this.getBlockType(), meta));
   }

   public abstract BlockFlower.EnumFlowerColor getBlockType();

   public IProperty getTypeProperty() {
      if (this.type == null) {
         this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class, new Predicate() {
            public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
               return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
            }
         });
      }

      return this.type;
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{this.getTypeProperty()});
   }

   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XZ;
   }

   public static enum EnumFlowerType implements IStringSerializable {
      DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
      POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
      BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
      ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
      HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
      RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
      ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
      WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
      PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
      OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

      private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
      private final BlockFlower.EnumFlowerColor blockType;
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name) {
         this(blockType, meta, name, name);
      }

      private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
         this.blockType = blockType;
         this.meta = meta;
         this.name = name;
         this.unlocalizedName = unlocalizedName;
      }

      public BlockFlower.EnumFlowerColor getBlockType() {
         return this.blockType;
      }

      public int getMeta() {
         return this.meta;
      }

      public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta) {
         BlockFlower.EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
         if (meta < 0 || meta >= ablockflower$enumflowertype.length) {
            meta = 0;
         }

         return ablockflower$enumflowertype[meta];
      }

      public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor) {
         return TYPES_FOR_BLOCK[flowerColor.ordinal()];
      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         BlockFlower.EnumFlowerColor[] var0 = BlockFlower.EnumFlowerColor.values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            final BlockFlower.EnumFlowerColor blockflower$enumflowercolor = var0[var2];
            Collection collection = Collections2.filter(Lists.newArrayList(values()), new Predicate() {
               public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
                  return p_apply_1_.getBlockType() == blockflower$enumflowercolor;
               }
            });
            TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = (BlockFlower.EnumFlowerType[])((BlockFlower.EnumFlowerType[])collection.toArray(new BlockFlower.EnumFlowerType[collection.size()]));
         }

      }
   }

   public static enum EnumFlowerColor {
      YELLOW,
      RED;

      public BlockFlower getBlock() {
         return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
      }
   }
}
