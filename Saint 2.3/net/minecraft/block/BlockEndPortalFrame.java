package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockEndPortalFrame extends Block {
   public static final PropertyDirection field_176508_a;
   public static final PropertyBool field_176507_b;
   private static final String __OBFID = "CL_00000237";

   static {
      field_176508_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      field_176507_b = PropertyBool.create("eye");
   }

   public BlockEndPortalFrame() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(field_176508_a, EnumFacing.NORTH).withProperty(field_176507_b, false));
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
   }

   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
      super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
      if ((Boolean)worldIn.getBlockState(pos).getValue(field_176507_b)) {
         this.setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
      }

      this.setBlockBoundsForItemRender();
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(field_176508_a, placer.func_174811_aO().getOpposite()).withProperty(field_176507_b, false);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
      return (Boolean)worldIn.getBlockState(pos).getValue(field_176507_b) ? 15 : 0;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(field_176507_b, (meta & 4) != 0).withProperty(field_176508_a, EnumFacing.getHorizontal(meta & 3));
   }

   public int getMetaFromState(IBlockState state) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)state.getValue(field_176508_a)).getHorizontalIndex();
      if ((Boolean)state.getValue(field_176507_b)) {
         var3 |= 4;
      }

      return var3;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{field_176508_a, field_176507_b});
   }
}
