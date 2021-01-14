package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWoodSlab extends BlockSlab {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

   public BlockWoodSlab() {
      super(Material.wood);
      IBlockState iblockstate = this.blockState.getBaseState();
      if (!this.isDouble()) {
         iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(iblockstate.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public MapColor getMapColor(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).func_181070_c();
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Item.getItemFromBlock(Blocks.wooden_slab);
   }

   public Item getItem(World worldIn, BlockPos pos) {
      return Item.getItemFromBlock(Blocks.wooden_slab);
   }

   public String getUnlocalizedName(int meta) {
      return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.byMetadata(meta).getUnlocalizedName();
   }

   public IProperty getVariantProperty() {
      return VARIANT;
   }

   public Object getVariant(ItemStack stack) {
      return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 7);
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab)) {
         BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockPlanks.EnumType blockplanks$enumtype = var4[var6];
            list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
         }
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta & 7));
      if (!this.isDouble()) {
         iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return iblockstate;
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
      if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         i |= 8;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return this.isDouble() ? new BlockState(this, new IProperty[]{VARIANT}) : new BlockState(this, new IProperty[]{HALF, VARIANT});
   }

   public int damageDropped(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
   }
}
