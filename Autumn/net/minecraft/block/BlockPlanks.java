package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockPlanks extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

   public BlockPlanks() {
      super(Material.wood);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public int damageDropped(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPlanks.EnumType blockplanks$enumtype = var4[var6];
         list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
   }

   public MapColor getMapColor(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).func_181070_c();
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      OAK(0, "oak", MapColor.woodColor),
      SPRUCE(1, "spruce", MapColor.obsidianColor),
      BIRCH(2, "birch", MapColor.sandColor),
      JUNGLE(3, "jungle", MapColor.dirtColor),
      ACACIA(4, "acacia", MapColor.adobeColor),
      DARK_OAK(5, "dark_oak", "big_oak", MapColor.brownColor);

      private static final BlockPlanks.EnumType[] META_LOOKUP = new BlockPlanks.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;
      private final MapColor field_181071_k;

      private EnumType(int p_i46388_3_, String p_i46388_4_, MapColor p_i46388_5_) {
         this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
      }

      private EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_) {
         this.meta = p_i46389_3_;
         this.name = p_i46389_4_;
         this.unlocalizedName = p_i46389_5_;
         this.field_181071_k = p_i46389_6_;
      }

      public int getMetadata() {
         return this.meta;
      }

      public MapColor func_181070_c() {
         return this.field_181071_k;
      }

      public String toString() {
         return this.name;
      }

      public static BlockPlanks.EnumType byMetadata(int meta) {
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
         BlockPlanks.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockPlanks.EnumType blockplanks$enumtype = var0[var2];
            META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
         }

      }
   }
}
