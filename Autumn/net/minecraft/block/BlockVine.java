package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool[] ALL_FACES;

   public BlockVine() {
      super(Material.vine);
      this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.withProperty(UP, worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube());
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isReplaceable(World worldIn, BlockPos pos) {
      return true;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      float f = 0.0625F;
      float f1 = 1.0F;
      float f2 = 1.0F;
      float f3 = 1.0F;
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f6 = 0.0F;
      boolean flag = false;
      if ((Boolean)worldIn.getBlockState(pos).getValue(WEST)) {
         f4 = Math.max(f4, 0.0625F);
         f1 = 0.0F;
         f2 = 0.0F;
         f5 = 1.0F;
         f3 = 0.0F;
         f6 = 1.0F;
         flag = true;
      }

      if ((Boolean)worldIn.getBlockState(pos).getValue(EAST)) {
         f1 = Math.min(f1, 0.9375F);
         f4 = 1.0F;
         f2 = 0.0F;
         f5 = 1.0F;
         f3 = 0.0F;
         f6 = 1.0F;
         flag = true;
      }

      if ((Boolean)worldIn.getBlockState(pos).getValue(NORTH)) {
         f6 = Math.max(f6, 0.0625F);
         f3 = 0.0F;
         f1 = 0.0F;
         f4 = 1.0F;
         f2 = 0.0F;
         f5 = 1.0F;
         flag = true;
      }

      if ((Boolean)worldIn.getBlockState(pos).getValue(SOUTH)) {
         f3 = Math.min(f3, 0.9375F);
         f6 = 1.0F;
         f1 = 0.0F;
         f4 = 1.0F;
         f2 = 0.0F;
         f5 = 1.0F;
         flag = true;
      }

      if (!flag && this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock())) {
         f2 = Math.min(f2, 0.9375F);
         f5 = 1.0F;
         f1 = 0.0F;
         f4 = 1.0F;
         f3 = 0.0F;
         f6 = 1.0F;
      }

      this.setBlockBounds(f1, f2, f3, f4, f5, f6);
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      switch(side) {
      case UP:
         return this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());
      case NORTH:
      case SOUTH:
      case EAST:
      case WEST:
         return this.canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
      default:
         return false;
      }
   }

   private boolean canPlaceOn(Block blockIn) {
      return blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement();
   }

   private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
      IBlockState iblockstate = state;
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         PropertyBool propertybool;
         IBlockState iblockstate1;
         do {
            EnumFacing enumfacing;
            do {
               do {
                  if (!var5.hasNext()) {
                     if (getNumGrownFaces(state) == 0) {
                        return false;
                     }

                     if (iblockstate != state) {
                        worldIn.setBlockState(pos, state, 2);
                     }

                     return true;
                  }

                  Object enumfacing0 = var5.next();
                  enumfacing = (EnumFacing)enumfacing0;
                  propertybool = getPropertyFor(enumfacing);
               } while(!(Boolean)state.getValue(propertybool));
            } while(this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()));

            iblockstate1 = worldIn.getBlockState(pos.up());
         } while(iblockstate1.getBlock() == this && (Boolean)iblockstate1.getValue(propertybool));

         state = state.withProperty(propertybool, false);
      }
   }

   public int getBlockColor() {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   public int getRenderColor(IBlockState state) {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
      return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
         int i = 4;
         int j = 5;
         boolean flag = false;

         label184:
         for(int k = -i; k <= i; ++k) {
            for(int l = -i; l <= i; ++l) {
               for(int i1 = -1; i1 <= 1; ++i1) {
                  if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
                     --j;
                     if (j <= 0) {
                        flag = true;
                        break label184;
                     }
                  }
               }
            }
         }

         EnumFacing enumfacing1 = EnumFacing.random(rand);
         BlockPos blockpos1 = pos.up();
         EnumFacing enumfacing4;
         if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos1)) {
            if (!flag) {
               IBlockState iblockstate2 = state;
               Iterator var23 = EnumFacing.Plane.HORIZONTAL.iterator();

               while(true) {
                  do {
                     if (!var23.hasNext()) {
                        if ((Boolean)iblockstate2.getValue(NORTH) || (Boolean)iblockstate2.getValue(EAST) || (Boolean)iblockstate2.getValue(SOUTH) || (Boolean)iblockstate2.getValue(WEST)) {
                           worldIn.setBlockState(blockpos1, iblockstate2, 2);
                        }

                        return;
                     }

                     Object enumfacing30 = var23.next();
                     enumfacing4 = (EnumFacing)enumfacing30;
                  } while(!rand.nextBoolean() && this.canPlaceOn(worldIn.getBlockState(blockpos1.offset(enumfacing4)).getBlock()));

                  iblockstate2 = iblockstate2.withProperty(getPropertyFor(enumfacing4), false);
               }
            }
         } else {
            BlockPos blockpos3;
            if (enumfacing1.getAxis().isHorizontal() && !(Boolean)state.getValue(getPropertyFor(enumfacing1))) {
               if (!flag) {
                  blockpos3 = pos.offset(enumfacing1);
                  Block block1 = worldIn.getBlockState(blockpos3).getBlock();
                  if (block1.blockMaterial == Material.air) {
                     EnumFacing enumfacing2 = enumfacing1.rotateY();
                     enumfacing4 = enumfacing1.rotateYCCW();
                     boolean flag1 = (Boolean)state.getValue(getPropertyFor(enumfacing2));
                     boolean flag2 = (Boolean)state.getValue(getPropertyFor(enumfacing4));
                     BlockPos blockpos4 = blockpos3.offset(enumfacing2);
                     BlockPos blockpos = blockpos3.offset(enumfacing4);
                     if (flag1 && this.canPlaceOn(worldIn.getBlockState(blockpos4).getBlock())) {
                        worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty(getPropertyFor(enumfacing2), true), 2);
                     } else if (flag2 && this.canPlaceOn(worldIn.getBlockState(blockpos).getBlock())) {
                        worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty(getPropertyFor(enumfacing4), true), 2);
                     } else if (flag1 && worldIn.isAirBlock(blockpos4) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing2)).getBlock())) {
                        worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), true), 2);
                     } else if (flag2 && worldIn.isAirBlock(blockpos) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing4)).getBlock())) {
                        worldIn.setBlockState(blockpos, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), true), 2);
                     } else if (this.canPlaceOn(worldIn.getBlockState(blockpos3.up()).getBlock())) {
                        worldIn.setBlockState(blockpos3, this.getDefaultState(), 2);
                     }
                  } else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
                     worldIn.setBlockState(pos, state.withProperty(getPropertyFor(enumfacing1), true), 2);
                  }
               }
            } else if (pos.getY() > 1) {
               blockpos3 = pos.down();
               IBlockState iblockstate = worldIn.getBlockState(blockpos3);
               Block block = iblockstate.getBlock();
               IBlockState iblockstate3;
               Iterator var14;
               Object enumfacing50;
               EnumFacing enumfacing;
               if (block.blockMaterial == Material.air) {
                  iblockstate3 = state;
                  var14 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var14.hasNext()) {
                     enumfacing50 = var14.next();
                     enumfacing = (EnumFacing)enumfacing50;
                     if (rand.nextBoolean()) {
                        iblockstate3 = iblockstate3.withProperty(getPropertyFor(enumfacing), false);
                     }
                  }

                  if ((Boolean)iblockstate3.getValue(NORTH) || (Boolean)iblockstate3.getValue(EAST) || (Boolean)iblockstate3.getValue(SOUTH) || (Boolean)iblockstate3.getValue(WEST)) {
                     worldIn.setBlockState(blockpos3, iblockstate3, 2);
                  }
               } else if (block == this) {
                  iblockstate3 = iblockstate;
                  var14 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var14.hasNext()) {
                     enumfacing50 = var14.next();
                     enumfacing = (EnumFacing)enumfacing50;
                     PropertyBool propertybool = getPropertyFor(enumfacing);
                     if (rand.nextBoolean() && (Boolean)state.getValue(propertybool)) {
                        iblockstate3 = iblockstate3.withProperty(propertybool, true);
                     }
                  }

                  if ((Boolean)iblockstate3.getValue(NORTH) || (Boolean)iblockstate3.getValue(EAST) || (Boolean)iblockstate3.getValue(SOUTH) || (Boolean)iblockstate3.getValue(WEST)) {
                     worldIn.setBlockState(blockpos3, iblockstate3, 2);
                  }
               }
            }
         }
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.getDefaultState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false);
      return facing.getAxis().isHorizontal() ? iblockstate.withProperty(getPropertyFor(facing.getOpposite()), true) : iblockstate;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
      if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
         player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
         spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
      } else {
         super.harvestBlock(worldIn, player, pos, state, te);
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(SOUTH, (meta & 1) > 0).withProperty(WEST, (meta & 2) > 0).withProperty(NORTH, (meta & 4) > 0).withProperty(EAST, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      if ((Boolean)state.getValue(SOUTH)) {
         i |= 1;
      }

      if ((Boolean)state.getValue(WEST)) {
         i |= 2;
      }

      if ((Boolean)state.getValue(NORTH)) {
         i |= 4;
      }

      if ((Boolean)state.getValue(EAST)) {
         i |= 8;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{UP, NORTH, EAST, SOUTH, WEST});
   }

   public static PropertyBool getPropertyFor(EnumFacing side) {
      switch(side) {
      case UP:
         return UP;
      case NORTH:
         return NORTH;
      case SOUTH:
         return SOUTH;
      case EAST:
         return EAST;
      case WEST:
         return WEST;
      default:
         throw new IllegalArgumentException(side + " is an invalid choice");
      }
   }

   public static int getNumGrownFaces(IBlockState state) {
      int i = 0;
      PropertyBool[] var2 = ALL_FACES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PropertyBool propertybool = var2[var4];
         if ((Boolean)state.getValue(propertybool)) {
            ++i;
         }
      }

      return i;
   }

   static {
      ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
   }
}
