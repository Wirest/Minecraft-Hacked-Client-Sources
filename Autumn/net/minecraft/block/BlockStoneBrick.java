package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockStoneBrick extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStoneBrick.EnumType.class);
   public static final int DEFAULT_META;
   public static final int MOSSY_META;
   public static final int CRACKED_META;
   public static final int CHISELED_META;

   public BlockStoneBrick() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public int damageDropped(IBlockState state) {
      return ((BlockStoneBrick.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      BlockStoneBrick.EnumType[] var4 = BlockStoneBrick.EnumType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockStoneBrick.EnumType blockstonebrick$enumtype = var4[var6];
         list.add(new ItemStack(itemIn, 1, blockstonebrick$enumtype.getMetadata()));
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockStoneBrick.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   static {
      DEFAULT_META = BlockStoneBrick.EnumType.DEFAULT.getMetadata();
      MOSSY_META = BlockStoneBrick.EnumType.MOSSY.getMetadata();
      CRACKED_META = BlockStoneBrick.EnumType.CRACKED.getMetadata();
      CHISELED_META = BlockStoneBrick.EnumType.CHISELED.getMetadata();
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "stonebrick", "default"),
      MOSSY(1, "mossy_stonebrick", "mossy"),
      CRACKED(2, "cracked_stonebrick", "cracked"),
      CHISELED(3, "chiseled_stonebrick", "chiseled");

      private static final BlockStoneBrick.EnumType[] META_LOOKUP = new BlockStoneBrick.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      private EnumType(int meta, String name, String unlocalizedName) {
         this.meta = meta;
         this.name = name;
         this.unlocalizedName = unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public static BlockStoneBrick.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         BlockStoneBrick.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockStoneBrick.EnumType blockstonebrick$enumtype = var0[var2];
            META_LOOKUP[blockstonebrick$enumtype.getMetadata()] = blockstonebrick$enumtype;
         }

      }
   }
}
