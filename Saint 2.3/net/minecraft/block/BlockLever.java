package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
   public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private static final String __OBFID = "CL_00000264";

   protected BlockLever() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
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
      return side == EnumFacing.UP && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : this.func_176358_d(worldIn, pos.offset(side.getOpposite()));
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return this.func_176358_d(worldIn, pos.offsetWest()) ? true : (this.func_176358_d(worldIn, pos.offsetEast()) ? true : (this.func_176358_d(worldIn, pos.offsetNorth()) ? true : (this.func_176358_d(worldIn, pos.offsetSouth()) ? true : (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : this.func_176358_d(worldIn, pos.offsetUp())))));
   }

   protected boolean func_176358_d(World worldIn, BlockPos p_176358_2_) {
      return worldIn.getBlockState(p_176358_2_).getBlock().isNormalCube();
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState var9 = this.getDefaultState().withProperty(POWERED, false);
      if (this.func_176358_d(worldIn, pos.offset(facing.getOpposite()))) {
         return var9.withProperty(FACING, BlockLever.EnumOrientation.func_176856_a(facing, placer.func_174811_aO()));
      } else {
         Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

         EnumFacing var11;
         do {
            if (!var10.hasNext()) {
               if (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                  return var9.withProperty(FACING, BlockLever.EnumOrientation.func_176856_a(EnumFacing.UP, placer.func_174811_aO()));
               }

               return var9;
            }

            var11 = (EnumFacing)var10.next();
         } while(var11 == facing || !this.func_176358_d(worldIn, pos.offset(var11.getOpposite())));

         return var9.withProperty(FACING, BlockLever.EnumOrientation.func_176856_a(var11, placer.func_174811_aO()));
      }
   }

   public static int func_176357_a(EnumFacing p_176357_0_) {
      switch(p_176357_0_) {
      case DOWN:
         return 0;
      case UP:
         return 5;
      case NORTH:
         return 4;
      case SOUTH:
         return 3;
      case WEST:
         return 2;
      case EAST:
         return 1;
      default:
         return -1;
      }
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if (this.func_176356_e(worldIn, pos) && !this.func_176358_d(worldIn, pos.offset(((BlockLever.EnumOrientation)state.getValue(FACING)).func_176852_c().getOpposite()))) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

   }

   private boolean func_176356_e(World worldIn, BlockPos p_176356_2_) {
      if (this.canPlaceBlockAt(worldIn, p_176356_2_)) {
         return true;
      } else {
         this.dropBlockAsItem(worldIn, p_176356_2_, worldIn.getBlockState(p_176356_2_), 0);
         worldIn.setBlockToAir(p_176356_2_);
         return false;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
      float var3 = 0.1875F;
      switch((BlockLever.EnumOrientation)access.getBlockState(pos).getValue(FACING)) {
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
         break;
      case UP_Z:
      case UP_X:
         var3 = 0.25F;
         this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
         break;
      case DOWN_X:
      case DOWN_Z:
         var3 = 0.25F;
         this.setBlockBounds(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
      }

   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote) {
         return true;
      } else {
         state = state.cycleProperty(POWERED);
         worldIn.setBlockState(pos, state, 3);
         worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, (Boolean)state.getValue(POWERED) ? 0.6F : 0.5F);
         worldIn.notifyNeighborsOfStateChange(pos, this);
         EnumFacing var9 = ((BlockLever.EnumOrientation)state.getValue(FACING)).func_176852_c();
         worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
         return true;
      }
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      if ((Boolean)state.getValue(POWERED)) {
         worldIn.notifyNeighborsOfStateChange(pos, this);
         EnumFacing var4 = ((BlockLever.EnumOrientation)state.getValue(FACING)).func_176852_c();
         worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
      }

      super.breakBlock(worldIn, pos, state);
   }

   public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return (Boolean)state.getValue(POWERED) ? 15 : 0;
   }

   public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return !(Boolean)state.getValue(POWERED) ? 0 : (((BlockLever.EnumOrientation)state.getValue(FACING)).func_176852_c() == side ? 15 : 0);
   }

   public boolean canProvidePower() {
      return true;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.func_176853_a(meta & 7)).withProperty(POWERED, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      byte var2 = 0;
      int var3 = var2 | ((BlockLever.EnumOrientation)state.getValue(FACING)).func_176855_a();
      if ((Boolean)state.getValue(POWERED)) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }

   public static enum EnumOrientation implements IStringSerializable {
      DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN),
      EAST("EAST", 1, 1, "east", EnumFacing.EAST),
      WEST("WEST", 2, 2, "west", EnumFacing.WEST),
      SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH),
      NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH),
      UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP),
      UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP),
      DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);

      private static final BlockLever.EnumOrientation[] field_176869_i = new BlockLever.EnumOrientation[values().length];
      private final int field_176866_j;
      private final String field_176867_k;
      private final EnumFacing field_176864_l;
      private static final BlockLever.EnumOrientation[] $VALUES = new BlockLever.EnumOrientation[]{DOWN_X, EAST, WEST, SOUTH, NORTH, UP_Z, UP_X, DOWN_Z};
      private static final String __OBFID = "CL_00002102";

      static {
         BlockLever.EnumOrientation[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockLever.EnumOrientation var3 = var0[var2];
            field_176869_i[var3.func_176855_a()] = var3;
         }

      }

      private EnumOrientation(String p_i45709_1_, int p_i45709_2_, int p_i45709_3_, String p_i45709_4_, EnumFacing p_i45709_5_) {
         this.field_176866_j = p_i45709_3_;
         this.field_176867_k = p_i45709_4_;
         this.field_176864_l = p_i45709_5_;
      }

      public int func_176855_a() {
         return this.field_176866_j;
      }

      public EnumFacing func_176852_c() {
         return this.field_176864_l;
      }

      public String toString() {
         return this.field_176867_k;
      }

      public static BlockLever.EnumOrientation func_176853_a(int p_176853_0_) {
         if (p_176853_0_ < 0 || p_176853_0_ >= field_176869_i.length) {
            p_176853_0_ = 0;
         }

         return field_176869_i[p_176853_0_];
      }

      public static BlockLever.EnumOrientation func_176856_a(EnumFacing p_176856_0_, EnumFacing p_176856_1_) {
         switch(p_176856_0_) {
         case DOWN:
            switch(p_176856_1_.getAxis()) {
            case X:
               return DOWN_X;
            case Z:
               return DOWN_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
            }
         case UP:
            switch(p_176856_1_.getAxis()) {
            case X:
               return UP_X;
            case Z:
               return UP_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
            }
         case NORTH:
            return NORTH;
         case SOUTH:
            return SOUTH;
         case WEST:
            return WEST;
         case EAST:
            return EAST;
         default:
            throw new IllegalArgumentException("Invalid facing: " + p_176856_0_);
         }
      }

      public String getName() {
         return this.field_176867_k;
      }
   }

   static final class SwitchEnumFacing {
      static final int[] FACING_LOOKUP;
      static final int[] ORIENTATION_LOOKUP;
      static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];
      private static final String __OBFID = "CL_00002103";

      static {
         try {
            AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var16) {
         }

         try {
            AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
         } catch (NoSuchFieldError var15) {
         }

         ORIENTATION_LOOKUP = new int[BlockLever.EnumOrientation.values().length];

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.EAST.ordinal()] = 1;
         } catch (NoSuchFieldError var14) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var12) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.NORTH.ordinal()] = 4;
         } catch (NoSuchFieldError var11) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_Z.ordinal()] = 5;
         } catch (NoSuchFieldError var10) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_X.ordinal()] = 6;
         } catch (NoSuchFieldError var9) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
         }

         FACING_LOOKUP = new int[EnumFacing.values().length];

         try {
            FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
