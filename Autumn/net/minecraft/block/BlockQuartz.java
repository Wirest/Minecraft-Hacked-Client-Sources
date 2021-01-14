package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockQuartz extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockQuartz.EnumType.class);

   public BlockQuartz() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      if (meta == BlockQuartz.EnumType.LINES_Y.getMetadata()) {
         switch(facing.getAxis()) {
         case Z:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
         case X:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
         case Y:
         default:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Y);
         }
      } else {
         return meta == BlockQuartz.EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.CHISELED) : this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT);
      }
   }

   public int damageDropped(IBlockState state) {
      BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue(VARIANT);
      return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? blockquartz$enumtype.getMetadata() : BlockQuartz.EnumType.LINES_Y.getMetadata();
   }

   protected ItemStack createStackedBlock(IBlockState state) {
      BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue(VARIANT);
      return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
      list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
      list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
   }

   public MapColor getMapColor(IBlockState state) {
      return MapColor.quartzColor;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockQuartz.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "default", "default"),
      CHISELED(1, "chiseled", "chiseled"),
      LINES_Y(2, "lines_y", "lines"),
      LINES_X(3, "lines_x", "lines"),
      LINES_Z(4, "lines_z", "lines");

      private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[values().length];
      private final int meta;
      private final String field_176805_h;
      private final String unlocalizedName;

      private EnumType(int meta, String name, String unlocalizedName) {
         this.meta = meta;
         this.field_176805_h = name;
         this.unlocalizedName = unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.unlocalizedName;
      }

      public static BlockQuartz.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.field_176805_h;
      }

      static {
         BlockQuartz.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockQuartz.EnumType blockquartz$enumtype = var0[var2];
            META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
         }

      }
   }
}
