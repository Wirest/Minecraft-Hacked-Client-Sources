package net.minecraft.block;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block {
   public static final PropertyDirection field_176264_a;
   public static final PropertyBool field_176263_b;
   public static final PropertyBool field_176265_M;
   public static final PropertyBool field_176266_N;
   private static final String __OBFID = "CL_00000329";

   static {
      field_176264_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      field_176263_b = PropertyBool.create("powered");
      field_176265_M = PropertyBool.create("attached");
      field_176266_N = PropertyBool.create("suspended");
   }

   public BlockTripWireHook() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(field_176264_a, EnumFacing.NORTH).withProperty(field_176263_b, false).withProperty(field_176265_M, false).withProperty(field_176266_N, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
      this.setTickRandomly(true);
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.withProperty(field_176266_N, !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()));
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var3.hasNext()) {
         EnumFacing var4 = (EnumFacing)var3.next();
         if (worldIn.getBlockState(pos.offset(var4)).getBlock().isNormalCube()) {
            return true;
         }
      }

      return false;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState var9 = this.getDefaultState().withProperty(field_176263_b, false).withProperty(field_176265_M, false).withProperty(field_176266_N, false);
      if (facing.getAxis().isHorizontal()) {
         var9 = var9.withProperty(field_176264_a, facing);
      }

      return var9;
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      this.func_176260_a(worldIn, pos, state, false, false, -1, (IBlockState)null);
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if (neighborBlock != this && this.func_176261_e(worldIn, pos, state)) {
         EnumFacing var5 = (EnumFacing)state.getValue(field_176264_a);
         if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().isNormalCube()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
         }
      }

   }

   public void func_176260_a(World worldIn, BlockPos p_176260_2_, IBlockState p_176260_3_, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
      EnumFacing var8 = (EnumFacing)p_176260_3_.getValue(field_176264_a);
      boolean var9 = (Boolean)p_176260_3_.getValue(field_176265_M);
      boolean var10 = (Boolean)p_176260_3_.getValue(field_176263_b);
      boolean var11 = !World.doesBlockHaveSolidTopSurface(worldIn, p_176260_2_.offsetDown());
      boolean var12 = !p_176260_4_;
      boolean var13 = false;
      int var14 = 0;
      IBlockState[] var15 = new IBlockState[42];

      BlockPos var17;
      for(int var16 = 1; var16 < 42; ++var16) {
         var17 = p_176260_2_.offset(var8, var16);
         IBlockState var18 = worldIn.getBlockState(var17);
         if (var18.getBlock() == Blocks.tripwire_hook) {
            if (var18.getValue(field_176264_a) == var8.getOpposite()) {
               var14 = var16;
            }
            break;
         }

         if (var18.getBlock() != Blocks.tripwire && var16 != p_176260_6_) {
            var15[var16] = null;
            var12 = false;
         } else {
            if (var16 == p_176260_6_) {
               var18 = (IBlockState)Objects.firstNonNull(p_176260_7_, var18);
            }

            boolean var19 = !(Boolean)var18.getValue(BlockTripWire.field_176295_N);
            boolean var20 = (Boolean)var18.getValue(BlockTripWire.field_176293_a);
            boolean var21 = (Boolean)var18.getValue(BlockTripWire.field_176290_b);
            var12 &= var21 == var11;
            var13 |= var19 && var20;
            var15[var16] = var18;
            if (var16 == p_176260_6_) {
               worldIn.scheduleUpdate(p_176260_2_, this, this.tickRate(worldIn));
               var12 &= var19;
            }
         }
      }

      var12 &= var14 > 1;
      var13 &= var12;
      IBlockState var22 = this.getDefaultState().withProperty(field_176265_M, var12).withProperty(field_176263_b, var13);
      if (var14 > 0) {
         var17 = p_176260_2_.offset(var8, var14);
         EnumFacing var24 = var8.getOpposite();
         worldIn.setBlockState(var17, var22.withProperty(field_176264_a, var24), 3);
         this.func_176262_b(worldIn, var17, var24);
         this.func_180694_a(worldIn, var17, var12, var13, var9, var10);
      }

      this.func_180694_a(worldIn, p_176260_2_, var12, var13, var9, var10);
      if (!p_176260_4_) {
         worldIn.setBlockState(p_176260_2_, var22.withProperty(field_176264_a, var8), 3);
         if (p_176260_5_) {
            this.func_176262_b(worldIn, p_176260_2_, var8);
         }
      }

      if (var9 != var12) {
         for(int var23 = 1; var23 < var14; ++var23) {
            BlockPos var25 = p_176260_2_.offset(var8, var23);
            IBlockState var26 = var15[var23];
            if (var26 != null && worldIn.getBlockState(var25).getBlock() != Blocks.air) {
               worldIn.setBlockState(var25, var26.withProperty(field_176265_M, var12), 3);
            }
         }
      }

   }

   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      this.func_176260_a(worldIn, pos, state, false, true, -1, (IBlockState)null);
   }

   private void func_180694_a(World worldIn, BlockPos p_180694_2_, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
      if (p_180694_4_ && !p_180694_6_) {
         worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
      } else if (!p_180694_4_ && p_180694_6_) {
         worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
      } else if (p_180694_3_ && !p_180694_5_) {
         worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
      } else if (!p_180694_3_ && p_180694_5_) {
         worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
      }

   }

   private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
      worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
      worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
   }

   private boolean func_176261_e(World worldIn, BlockPos p_176261_2_, IBlockState p_176261_3_) {
      if (!this.canPlaceBlockAt(worldIn, p_176261_2_)) {
         this.dropBlockAsItem(worldIn, p_176261_2_, p_176261_3_, 0);
         worldIn.setBlockToAir(p_176261_2_);
         return false;
      } else {
         return true;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
      float var3 = 0.1875F;
      switch((EnumFacing)access.getBlockState(pos).getValue(field_176264_a)) {
      case EAST:
         this.setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
         break;
      case WEST:
         this.setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
         break;
      case SOUTH:
         this.setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
         break;
      case NORTH:
         this.setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
      }

   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      boolean var4 = (Boolean)state.getValue(field_176265_M);
      boolean var5 = (Boolean)state.getValue(field_176263_b);
      if (var4 || var5) {
         this.func_176260_a(worldIn, pos, state, true, false, -1, (IBlockState)null);
      }

      if (var5) {
         worldIn.notifyNeighborsOfStateChange(pos, this);
         worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue(field_176264_a)).getOpposite()), this);
      }

      super.breakBlock(worldIn, pos, state);
   }

   public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return (Boolean)state.getValue(field_176263_b) ? 15 : 0;
   }

   public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return !(Boolean)state.getValue(field_176263_b) ? 0 : (state.getValue(field_176264_a) == side ? 15 : 0);
   }

   public boolean canProvidePower() {
      return true;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT_MIPPED;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(field_176264_a, EnumFacing.getHorizontal(meta & 3)).withProperty(field_176263_b, (meta & 8) > 0).withProperty(field_176265_M, (meta & 4) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)state.getValue(field_176264_a)).getHorizontalIndex();
      if ((Boolean)state.getValue(field_176263_b)) {
         var3 |= 8;
      }

      if ((Boolean)state.getValue(field_176265_M)) {
         var3 |= 4;
      }

      return var3;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{field_176264_a, field_176263_b, field_176265_M, field_176266_N});
   }

   static final class SwitchEnumFacing {
      static final int[] field_177056_a = new int[EnumFacing.values().length];
      private static final String __OBFID = "CL_00002050";

      static {
         try {
            field_177056_a[EnumFacing.EAST.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_177056_a[EnumFacing.WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_177056_a[EnumFacing.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_177056_a[EnumFacing.NORTH.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
