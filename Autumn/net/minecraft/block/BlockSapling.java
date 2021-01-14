package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable {
   public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
   public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

   protected BlockSapling() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, 0));
      float f = 0.4F;
      this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote) {
         super.updateTick(worldIn, pos, state, rand);
         if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
            this.grow(worldIn, pos, state, rand);
         }
      }

   }

   public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if ((Integer)state.getValue(STAGE) == 0) {
         worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
      } else {
         this.generateTree(worldIn, pos, state, rand);
      }

   }

   public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      WorldGenerator worldgenerator = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
      int i = 0;
      int j = 0;
      boolean flag = false;
      IBlockState iblockstate;
      switch((BlockPlanks.EnumType)state.getValue(TYPE)) {
      case SPRUCE:
         label68:
         for(i = 0; i >= -1; --i) {
            for(j = 0; j >= -1; --j) {
               if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
                  worldgenerator = new WorldGenMegaPineTree(false, rand.nextBoolean());
                  flag = true;
                  break label68;
               }
            }
         }

         if (!flag) {
            j = 0;
            i = 0;
            worldgenerator = new WorldGenTaiga2(true);
         }
         break;
      case BIRCH:
         worldgenerator = new WorldGenForest(true, false);
         break;
      case JUNGLE:
         iblockstate = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
         IBlockState iblockstate1 = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);

         label82:
         for(i = 0; i >= -1; --i) {
            for(j = 0; j >= -1; --j) {
               if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
                  worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
                  flag = true;
                  break label82;
               }
            }
         }

         if (!flag) {
            j = 0;
            i = 0;
            worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
         }
         break;
      case ACACIA:
         worldgenerator = new WorldGenSavannaTree(true);
         break;
      case DARK_OAK:
         label96:
         for(i = 0; i >= -1; --i) {
            for(j = 0; j >= -1; --j) {
               if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                  worldgenerator = new WorldGenCanopyTree(true);
                  flag = true;
                  break label96;
               }
            }
         }

         if (!flag) {
            return;
         }
      case OAK:
      }

      iblockstate = Blocks.air.getDefaultState();
      if (flag) {
         worldIn.setBlockState(pos.add(i, 0, j), iblockstate, 4);
         worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate, 4);
         worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate, 4);
         worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate, 4);
      } else {
         worldIn.setBlockState(pos, iblockstate, 4);
      }

      if (!((WorldGenerator)worldgenerator).generate(worldIn, rand, pos.add(i, 0, j))) {
         if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), state, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
         } else {
            worldIn.setBlockState(pos, state, 4);
         }
      }

   }

   private boolean func_181624_a(World p_181624_1_, BlockPos p_181624_2_, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType p_181624_5_) {
      return this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_);
   }

   public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      return iblockstate.getBlock() == this && iblockstate.getValue(TYPE) == type;
   }

   public int damageDropped(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPlanks.EnumType blockplanks$enumtype = var4[var6];
         list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
      }

   }

   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
      return true;
   }

   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return (double)worldIn.rand.nextFloat() < 0.45D;
   }

   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      this.grow(worldIn, pos, state, rand);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(meta & 7)).withProperty(STAGE, (meta & 8) >> 3);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
      i |= (Integer)state.getValue(STAGE) << 3;
      return i;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE, STAGE});
   }
}
