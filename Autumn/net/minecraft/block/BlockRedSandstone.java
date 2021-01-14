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

public class BlockRedSandstone extends Block {
   public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockRedSandstone.EnumType.class);

   public BlockRedSandstone() {
      super(Material.rock, BlockSand.EnumType.RED_SAND.getMapColor());
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockRedSandstone.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public int damageDropped(IBlockState state) {
      return ((BlockRedSandstone.EnumType)state.getValue(TYPE)).getMetadata();
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      BlockRedSandstone.EnumType[] var4 = BlockRedSandstone.EnumType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockRedSandstone.EnumType blockredsandstone$enumtype = var4[var6];
         list.add(new ItemStack(itemIn, 1, blockredsandstone$enumtype.getMetadata()));
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, BlockRedSandstone.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockRedSandstone.EnumType)state.getValue(TYPE)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE});
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "red_sandstone", "default"),
      CHISELED(1, "chiseled_red_sandstone", "chiseled"),
      SMOOTH(2, "smooth_red_sandstone", "smooth");

      private static final BlockRedSandstone.EnumType[] META_LOOKUP = new BlockRedSandstone.EnumType[values().length];
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

      public static BlockRedSandstone.EnumType byMetadata(int meta) {
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
         BlockRedSandstone.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockRedSandstone.EnumType blockredsandstone$enumtype = var0[var2];
            META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
         }

      }
   }
}
