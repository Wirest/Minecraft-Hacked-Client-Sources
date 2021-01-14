package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
   public static final PropertyEnum NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
   private boolean canProvidePower = true;
   private final Set blocksNeedingUpdate = Sets.newHashSet();

   public BlockRedstoneWire() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, 0));
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
      state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
      state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
      state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
      return state;
   }

   private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
      BlockPos blockpos = pos.offset(direction);
      Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
      if (canConnectTo(worldIn.getBlockState(blockpos), direction) || !block.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(blockpos.down()))) {
         return BlockRedstoneWire.EnumAttachPosition.SIDE;
      } else {
         Block block1 = worldIn.getBlockState(pos.up()).getBlock();
         return !block1.isBlockNormalCube() && block.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up())) ? BlockRedstoneWire.EnumAttachPosition.UP : BlockRedstoneWire.EnumAttachPosition.NONE;
      }
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

   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier((Integer)iblockstate.getValue(POWER));
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
   }

   private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
      state = this.calculateCurrentChanges(worldIn, pos, pos, state);
      List list = Lists.newArrayList(this.blocksNeedingUpdate);
      this.blocksNeedingUpdate.clear();
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         BlockPos blockpos = (BlockPos)var5.next();
         worldIn.notifyNeighborsOfStateChange(blockpos, this);
      }

      return state;
   }

   private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
      IBlockState iblockstate = state;
      int i = (Integer)state.getValue(POWER);
      int j = 0;
      int j = this.getMaxCurrentStrength(worldIn, pos2, j);
      this.canProvidePower = false;
      int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
      this.canProvidePower = true;
      if (k > 0 && k > j - 1) {
         j = k;
      }

      int l = 0;
      Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         while(var10.hasNext()) {
            Object enumfacing0 = var10.next();
            EnumFacing enumfacing = (EnumFacing)enumfacing0;
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();
            if (flag) {
               l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }

            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
               if (flag && pos1.getY() >= pos2.getY()) {
                  l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
               }
            } else if (!worldIn.getBlockState(blockpos).getBlock().isNormalCube() && flag && pos1.getY() <= pos2.getY()) {
               l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
         }

         if (l > j) {
            j = l - 1;
         } else if (j > 0) {
            --j;
         } else {
            j = 0;
         }

         if (k > j - 1) {
            j = k;
         }

         if (i != j) {
            state = state.withProperty(POWER, j);
            if (worldIn.getBlockState(pos1) == iblockstate) {
               worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);
            EnumFacing[] var16 = EnumFacing.values();
            int var17 = var16.length;

            for(int var18 = 0; var18 < var17; ++var18) {
               EnumFacing enumfacing1 = var16[var18];
               this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
         }

         return state;
      }
   }

   private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
      if (worldIn.getBlockState(pos).getBlock() == this) {
         worldIn.notifyNeighborsOfStateChange(pos, this);
         EnumFacing[] var3 = EnumFacing.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            EnumFacing enumfacing = var3[var5];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
         }
      }

   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      if (!worldIn.isRemote) {
         this.updateSurroundingRedstone(worldIn, pos, state);
         Iterator var4 = EnumFacing.Plane.VERTICAL.iterator();

         Object enumfacing20;
         while(var4.hasNext()) {
            enumfacing20 = var4.next();
            worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)enumfacing20), this);
         }

         var4 = EnumFacing.Plane.HORIZONTAL.iterator();

         EnumFacing enumfacing2;
         while(var4.hasNext()) {
            enumfacing20 = var4.next();
            enumfacing2 = (EnumFacing)enumfacing20;
            this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing2));
         }

         var4 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var4.hasNext()) {
            enumfacing20 = var4.next();
            enumfacing2 = (EnumFacing)enumfacing20;
            BlockPos blockpos = pos.offset(enumfacing2);
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
            } else {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
         }
      }

   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      super.breakBlock(worldIn, pos, state);
      if (!worldIn.isRemote) {
         EnumFacing[] var4 = EnumFacing.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumFacing enumfacing = var4[var6];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
         }

         this.updateSurroundingRedstone(worldIn, pos, state);
         Iterator var8 = EnumFacing.Plane.HORIZONTAL.iterator();

         Object enumfacing2;
         while(var8.hasNext()) {
            enumfacing2 = var8.next();
            this.notifyWireNeighborsOfStateChange(worldIn, pos.offset((EnumFacing)enumfacing2));
         }

         var8 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var8.hasNext()) {
            enumfacing2 = var8.next();
            BlockPos blockpos = pos.offset((EnumFacing)enumfacing2);
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
            } else {
               this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
         }
      }

   }

   private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
      if (worldIn.getBlockState(pos).getBlock() != this) {
         return strength;
      } else {
         int i = (Integer)worldIn.getBlockState(pos).getValue(POWER);
         return i > strength ? i : strength;
      }
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if (!worldIn.isRemote) {
         if (this.canPlaceBlockAt(worldIn, pos)) {
            this.updateSurroundingRedstone(worldIn, pos, state);
         } else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
         }
      }

   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.redstone;
   }

   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return !this.canProvidePower ? 0 : this.getWeakPower(worldIn, pos, state, side);
   }

   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      if (!this.canProvidePower) {
         return 0;
      } else {
         int i = (Integer)state.getValue(POWER);
         if (i == 0) {
            return 0;
         } else if (side == EnumFacing.UP) {
            return i;
         } else {
            EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
            Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var7.hasNext()) {
               Object enumfacing0 = var7.next();
               EnumFacing enumfacing = (EnumFacing)enumfacing0;
               if (this.func_176339_d(worldIn, pos, enumfacing)) {
                  enumset.add(enumfacing);
               }
            }

            if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
               return i;
            } else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
               return i;
            } else {
               return 0;
            }
         }
      }
   }

   private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      BlockPos blockpos = pos.offset(side);
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      Block block = iblockstate.getBlock();
      boolean flag = block.isNormalCube();
      boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
      return !flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()) ? true : (canConnectTo(iblockstate, side) ? true : (block == Blocks.powered_repeater && iblockstate.getValue(BlockRedstoneDiode.FACING) == side ? true : !flag && canConnectUpwardsTo(worldIn, blockpos.down())));
   }

   protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
      return canConnectUpwardsTo(worldIn.getBlockState(pos));
   }

   protected static boolean canConnectUpwardsTo(IBlockState state) {
      return canConnectTo(state, (EnumFacing)null);
   }

   protected static boolean canConnectTo(IBlockState blockState, EnumFacing side) {
      Block block = blockState.getBlock();
      if (block == Blocks.redstone_wire) {
         return true;
      } else if (Blocks.unpowered_repeater.isAssociated(block)) {
         EnumFacing enumfacing = (EnumFacing)blockState.getValue(BlockRedstoneRepeater.FACING);
         return enumfacing == side || enumfacing.getOpposite() == side;
      } else {
         return block.canProvidePower() && side != null;
      }
   }

   public boolean canProvidePower() {
      return this.canProvidePower;
   }

   private int colorMultiplier(int powerLevel) {
      float f = (float)powerLevel / 15.0F;
      float f1 = f * 0.6F + 0.4F;
      if (powerLevel == 0) {
         f1 = 0.3F;
      }

      float f2 = f * f * 0.7F - 0.5F;
      float f3 = f * f * 0.6F - 0.7F;
      if (f2 < 0.0F) {
         f2 = 0.0F;
      }

      if (f3 < 0.0F) {
         f3 = 0.0F;
      }

      int i = MathHelper.clamp_int((int)(f1 * 255.0F), 0, 255);
      int j = MathHelper.clamp_int((int)(f2 * 255.0F), 0, 255);
      int k = MathHelper.clamp_int((int)(f3 * 255.0F), 0, 255);
      return -16777216 | i << 16 | j << 8 | k;
   }

   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      int i = (Integer)state.getValue(POWER);
      if (i != 0) {
         double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
         double d1 = (double)((float)pos.getY() + 0.0625F);
         double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
         float f = (float)i / 15.0F;
         float f1 = f * 0.6F + 0.4F;
         float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
         float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
         worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3);
      }

   }

   public Item getItem(World worldIn, BlockPos pos) {
      return Items.redstone;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(POWER, meta);
   }

   public int getMetaFromState(IBlockState state) {
      return (Integer)state.getValue(POWER);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{NORTH, EAST, SOUTH, WEST, POWER});
   }

   static enum EnumAttachPosition implements IStringSerializable {
      UP("up"),
      SIDE("side"),
      NONE("none");

      private final String name;

      private EnumAttachPosition(String name) {
         this.name = name;
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this.name;
      }
   }
}
