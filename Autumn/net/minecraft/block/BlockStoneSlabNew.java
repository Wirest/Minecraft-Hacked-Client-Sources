package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class BlockStoneSlabNew extends BlockSlab {
   public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStoneSlabNew.EnumType.class);

   public BlockStoneSlabNew() {
      super(Material.rock);
      IBlockState iblockstate = this.blockState.getBaseState();
      if (this.isDouble()) {
         iblockstate = iblockstate.withProperty(SEAMLESS, false);
      } else {
         iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(iblockstate.withProperty(VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + ".red_sandstone.name");
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Item.getItemFromBlock(Blocks.stone_slab2);
   }

   public Item getItem(World worldIn, BlockPos pos) {
      return Item.getItemFromBlock(Blocks.stone_slab2);
   }

   public String getUnlocalizedName(int meta) {
      return super.getUnlocalizedName() + "." + BlockStoneSlabNew.EnumType.byMetadata(meta).getUnlocalizedName();
   }

   public IProperty getVariantProperty() {
      return VARIANT;
   }

   public Object getVariant(ItemStack stack) {
      return BlockStoneSlabNew.EnumType.byMetadata(stack.getMetadata() & 7);
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab2)) {
         BlockStoneSlabNew.EnumType[] var4 = BlockStoneSlabNew.EnumType.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockStoneSlabNew.EnumType blockstoneslabnew$enumtype = var4[var6];
            list.add(new ItemStack(itemIn, 1, blockstoneslabnew$enumtype.getMetadata()));
         }
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockStoneSlabNew.EnumType.byMetadata(meta & 7));
      if (this.isDouble()) {
         iblockstate = iblockstate.withProperty(SEAMLESS, (meta & 8) != 0);
      } else {
         iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return iblockstate;
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((BlockStoneSlabNew.EnumType)state.getValue(VARIANT)).getMetadata();
      if (this.isDouble()) {
         if ((Boolean)state.getValue(SEAMLESS)) {
            i |= 8;
         }
      } else if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         i |= 8;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return this.isDouble() ? new BlockState(this, new IProperty[]{SEAMLESS, VARIANT}) : new BlockState(this, new IProperty[]{HALF, VARIANT});
   }

   public MapColor getMapColor(IBlockState state) {
      return ((BlockStoneSlabNew.EnumType)state.getValue(VARIANT)).func_181068_c();
   }

   public int damageDropped(IBlockState state) {
      return ((BlockStoneSlabNew.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public static enum EnumType implements IStringSerializable {
      RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.getMapColor());

      private static final BlockStoneSlabNew.EnumType[] META_LOOKUP = new BlockStoneSlabNew.EnumType[values().length];
      private final int meta;
      private final String name;
      private final MapColor field_181069_e;

      private EnumType(int p_i46391_3_, String p_i46391_4_, MapColor p_i46391_5_) {
         this.meta = p_i46391_3_;
         this.name = p_i46391_4_;
         this.field_181069_e = p_i46391_5_;
      }

      public int getMetadata() {
         return this.meta;
      }

      public MapColor func_181068_c() {
         return this.field_181069_e;
      }

      public String toString() {
         return this.name;
      }

      public static BlockStoneSlabNew.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.name;
      }

      static {
         BlockStoneSlabNew.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockStoneSlabNew.EnumType blockstoneslabnew$enumtype = var0[var2];
            META_LOOKUP[blockstoneslabnew$enumtype.getMetadata()] = blockstoneslabnew$enumtype;
         }

      }
   }
}
