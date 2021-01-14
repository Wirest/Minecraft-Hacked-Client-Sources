package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
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
   public static final PropertyDirection FACING;
   public static final PropertyBool EYE;

   public BlockEndPortalFrame() {
      super(Material.rock, MapColor.greenColor);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EYE, false));
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
      if ((Boolean)worldIn.getBlockState(pos).getValue(EYE)) {
         this.setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
      }

      this.setBlockBoundsForItemRender();
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(EYE, false);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
      return (Boolean)worldIn.getBlockState(pos).getValue(EYE) ? 15 : 0;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(EYE, (meta & 4) != 0).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
      if ((Boolean)state.getValue(EYE)) {
         i |= 4;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, EYE});
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      EYE = PropertyBool.create("eye");
   }
}
