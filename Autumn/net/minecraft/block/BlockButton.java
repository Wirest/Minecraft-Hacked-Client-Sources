package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends Block {
   public static final PropertyDirection FACING = PropertyDirection.create("facing");
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private final boolean wooden;

   protected BlockButton(boolean wooden) {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabRedstone);
      this.wooden = wooden;
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public int tickRate(World worldIn) {
      return this.wooden ? 30 : 20;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return func_181088_a(worldIn, pos, side.getOpposite());
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing enumfacing = var3[var5];
         if (func_181088_a(worldIn, pos, enumfacing)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_) {
      BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
      return p_181088_2_ == EnumFacing.DOWN ? World.doesBlockHaveSolidTopSurface(p_181088_0_, blockpos) : p_181088_0_.getBlockState(blockpos).getBlock().isNormalCube();
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return func_181088_a(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, false) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(POWERED, false);
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if (this.checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, ((EnumFacing)state.getValue(FACING)).getOpposite())) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

   }

   private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
      if (this.canPlaceBlockAt(worldIn, pos)) {
         return true;
      } else {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
         return false;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      this.updateBlockBounds(worldIn.getBlockState(pos));
   }

   private void updateBlockBounds(IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      boolean flag = (Boolean)state.getValue(POWERED);
      float f = 0.25F;
      float f1 = 0.375F;
      float f2 = (float)(flag ? 1 : 2) / 16.0F;
      float f3 = 0.125F;
      float f4 = 0.1875F;
      switch(enumfacing) {
      case EAST:
         this.setBlockBounds(0.0F, 0.375F, 0.3125F, f2, 0.625F, 0.6875F);
         break;
      case WEST:
         this.setBlockBounds(1.0F - f2, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
         break;
      case SOUTH:
         this.setBlockBounds(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, f2);
         break;
      case NORTH:
         this.setBlockBounds(0.3125F, 0.375F, 1.0F - f2, 0.6875F, 0.625F, 1.0F);
         break;
      case UP:
         this.setBlockBounds(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + f2, 0.625F);
         break;
      case DOWN:
         this.setBlockBounds(0.3125F, 1.0F - f2, 0.375F, 0.6875F, 1.0F, 0.625F);
      }

   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if ((Boolean)state.getValue(POWERED)) {
         return true;
      } else {
         worldIn.setBlockState(pos, state.withProperty(POWERED, true), 3);
         worldIn.markBlockRangeForRenderUpdate(pos, pos);
         worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
         this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
         worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
         return true;
      }
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      if ((Boolean)state.getValue(POWERED)) {
         this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
      }

      super.breakBlock(worldIn, pos, state);
   }

   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return (Boolean)state.getValue(POWERED) ? 15 : 0;
   }

   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return !(Boolean)state.getValue(POWERED) ? 0 : (state.getValue(FACING) == side ? 15 : 0);
   }

   public boolean canProvidePower() {
      return true;
   }

   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote && (Boolean)state.getValue(POWERED)) {
         if (this.wooden) {
            this.checkForArrows(worldIn, pos, state);
         } else {
            worldIn.setBlockState(pos, state.withProperty(POWERED, false));
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
            worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
         }
      }

   }

   public void setBlockBoundsForItemRender() {
      float f = 0.1875F;
      float f1 = 0.125F;
      float f2 = 0.125F;
      this.setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if (!worldIn.isRemote && this.wooden && !(Boolean)state.getValue(POWERED)) {
         this.checkForArrows(worldIn, pos, state);
      }

   }

   private void checkForArrows(World worldIn, BlockPos pos, IBlockState state) {
      this.updateBlockBounds(state);
      List list = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
      boolean flag = !list.isEmpty();
      boolean flag1 = (Boolean)state.getValue(POWERED);
      if (flag && !flag1) {
         worldIn.setBlockState(pos, state.withProperty(POWERED, true));
         this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
         worldIn.markBlockRangeForRenderUpdate(pos, pos);
         worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
      }

      if (!flag && flag1) {
         worldIn.setBlockState(pos, state.withProperty(POWERED, false));
         this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
         worldIn.markBlockRangeForRenderUpdate(pos, pos);
         worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
      }

      if (flag) {
         worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
      }

   }

   private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing) {
      worldIn.notifyNeighborsOfStateChange(pos, this);
      worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this);
   }

   public IBlockState getStateFromMeta(int meta) {
      EnumFacing enumfacing;
      switch(meta & 7) {
      case 0:
         enumfacing = EnumFacing.DOWN;
         break;
      case 1:
         enumfacing = EnumFacing.EAST;
         break;
      case 2:
         enumfacing = EnumFacing.WEST;
         break;
      case 3:
         enumfacing = EnumFacing.SOUTH;
         break;
      case 4:
         enumfacing = EnumFacing.NORTH;
         break;
      case 5:
      default:
         enumfacing = EnumFacing.UP;
      }

      return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i;
      switch((EnumFacing)state.getValue(FACING)) {
      case EAST:
         i = 1;
         break;
      case WEST:
         i = 2;
         break;
      case SOUTH:
         i = 3;
         break;
      case NORTH:
         i = 4;
         break;
      case UP:
      default:
         i = 5;
         break;
      case DOWN:
         i = 0;
      }

      if ((Boolean)state.getValue(POWERED)) {
         i |= 8;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }
}
