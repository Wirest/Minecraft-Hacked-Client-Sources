package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
   protected World currentWorld;
   protected Random randomGenerator;
   protected BlockPos field_180294_c;
   protected ChunkProviderSettings chunkProviderSettings;
   protected WorldGenerator clayGen = new WorldGenClay(4);
   protected WorldGenerator sandGen;
   protected WorldGenerator gravelAsSandGen;
   protected WorldGenerator dirtGen;
   protected WorldGenerator gravelGen;
   protected WorldGenerator graniteGen;
   protected WorldGenerator dioriteGen;
   protected WorldGenerator andesiteGen;
   protected WorldGenerator coalGen;
   protected WorldGenerator ironGen;
   protected WorldGenerator goldGen;
   protected WorldGenerator redstoneGen;
   protected WorldGenerator diamondGen;
   protected WorldGenerator lapisGen;
   protected WorldGenFlowers yellowFlowerGen;
   protected WorldGenerator mushroomBrownGen;
   protected WorldGenerator mushroomRedGen;
   protected WorldGenerator bigMushroomGen;
   protected WorldGenerator reedGen;
   protected WorldGenerator cactusGen;
   protected WorldGenerator waterlilyGen;
   protected int waterlilyPerChunk;
   protected int treesPerChunk;
   protected int flowersPerChunk;
   protected int grassPerChunk;
   protected int deadBushPerChunk;
   protected int mushroomsPerChunk;
   protected int reedsPerChunk;
   protected int cactiPerChunk;
   protected int sandPerChunk;
   protected int sandPerChunk2;
   protected int clayPerChunk;
   protected int bigMushroomsPerChunk;
   public boolean generateLakes;

   public BiomeDecorator() {
      this.sandGen = new WorldGenSand(Blocks.sand, 7);
      this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
      this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
      this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
      this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
      this.bigMushroomGen = new WorldGenBigMushroom();
      this.reedGen = new WorldGenReed();
      this.cactusGen = new WorldGenCactus();
      this.waterlilyGen = new WorldGenWaterlily();
      this.flowersPerChunk = 2;
      this.grassPerChunk = 1;
      this.sandPerChunk = 1;
      this.sandPerChunk2 = 3;
      this.clayPerChunk = 1;
      this.generateLakes = true;
   }

   public void decorate(World worldIn, Random random, BiomeGenBase p_180292_3_, BlockPos p_180292_4_) {
      if (this.currentWorld != null) {
         throw new RuntimeException("Already decorating");
      } else {
         this.currentWorld = worldIn;
         String s = worldIn.getWorldInfo().getGeneratorOptions();
         if (s != null) {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(s).func_177864_b();
         } else {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
         }

         this.randomGenerator = random;
         this.field_180294_c = p_180292_4_;
         this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
         this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
         this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
         this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
         this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
         this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
         this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
         this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
         this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
         this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
         this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
         this.genDecorations(p_180292_3_);
         this.currentWorld = null;
         this.randomGenerator = null;
      }
   }

   protected void genDecorations(BiomeGenBase biomeGenBaseIn) {
      this.generateOres();

      int k1;
      int l5;
      int j10;
      for(k1 = 0; k1 < this.sandPerChunk2; ++k1) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l5, 0, j10)));
      }

      for(k1 = 0; k1 < this.clayPerChunk; ++k1) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l5, 0, j10)));
      }

      for(k1 = 0; k1 < this.sandPerChunk; ++k1) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l5, 0, j10)));
      }

      k1 = this.treesPerChunk;
      if (this.randomGenerator.nextInt(10) == 0) {
         ++k1;
      }

      int i14;
      BlockPos blockpos3;
      for(l5 = 0; l5 < k1; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         WorldGenAbstractTree worldgenabstracttree = biomeGenBaseIn.genBigTreeChance(this.randomGenerator);
         worldgenabstracttree.func_175904_e();
         blockpos3 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14));
         if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos3)) {
            worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos3);
         }
      }

      for(l5 = 0; l5 < this.bigMushroomsPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)));
      }

      BlockPos blockpos6;
      int j17;
      int k19;
      for(l5 = 0; l5 < this.flowersPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() + 32;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            blockpos6 = this.field_180294_c.add(j10, k19, i14);
            BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeGenBaseIn.pickRandomFlower(this.randomGenerator, blockpos6);
            BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
            if (blockflower.getMaterial() != Material.air) {
               this.yellowFlowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
               this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos6);
            }
         }
      }

      for(l5 = 0; l5 < this.grassPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            biomeGenBaseIn.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < this.deadBushPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            (new WorldGenDeadBush()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < this.waterlilyPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);

            BlockPos blockpos7;
            for(blockpos6 = this.field_180294_c.add(j10, k19, i14); blockpos6.getY() > 0; blockpos6 = blockpos7) {
               blockpos7 = blockpos6.down();
               if (!this.currentWorld.isAirBlock(blockpos7)) {
                  break;
               }
            }

            this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, blockpos6);
         }
      }

      for(l5 = 0; l5 < this.mushroomsPerChunk; ++l5) {
         if (this.randomGenerator.nextInt(4) == 0) {
            j10 = this.randomGenerator.nextInt(16) + 8;
            i14 = this.randomGenerator.nextInt(16) + 8;
            BlockPos blockpos2 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14));
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, blockpos2);
         }

         if (this.randomGenerator.nextInt(8) == 0) {
            j10 = this.randomGenerator.nextInt(16) + 8;
            i14 = this.randomGenerator.nextInt(16) + 8;
            j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
            if (j17 > 0) {
               k19 = this.randomGenerator.nextInt(j17);
               blockpos6 = this.field_180294_c.add(j10, k19, i14);
               this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, blockpos6);
            }
         }
      }

      if (this.randomGenerator.nextInt(4) == 0) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.currentWorld.getHeight(this.field_180294_c.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = this.randomGenerator.nextInt(i14);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l5, j17, j10));
         }
      }

      if (this.randomGenerator.nextInt(8) == 0) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.currentWorld.getHeight(this.field_180294_c.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = this.randomGenerator.nextInt(i14);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l5, j17, j10));
         }
      }

      for(l5 = 0; l5 < this.reedsPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < 10; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j10, k19, i14));
         }
      }

      if (this.randomGenerator.nextInt(32) == 0) {
         l5 = this.randomGenerator.nextInt(16) + 8;
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.currentWorld.getHeight(this.field_180294_c.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = this.randomGenerator.nextInt(i14);
            (new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l5, j17, j10));
         }
      }

      for(l5 = 0; l5 < this.cactiPerChunk; ++l5) {
         j10 = this.randomGenerator.nextInt(16) + 8;
         i14 = this.randomGenerator.nextInt(16) + 8;
         j17 = this.currentWorld.getHeight(this.field_180294_c.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = this.randomGenerator.nextInt(j17);
            this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j10, k19, i14));
         }
      }

      if (this.generateLakes) {
         for(l5 = 0; l5 < 50; ++l5) {
            j10 = this.randomGenerator.nextInt(16) + 8;
            i14 = this.randomGenerator.nextInt(16) + 8;
            j17 = this.randomGenerator.nextInt(248) + 8;
            if (j17 > 0) {
               k19 = this.randomGenerator.nextInt(j17);
               blockpos6 = this.field_180294_c.add(j10, k19, i14);
               (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, blockpos6);
            }
         }

         for(l5 = 0; l5 < 20; ++l5) {
            j10 = this.randomGenerator.nextInt(16) + 8;
            i14 = this.randomGenerator.nextInt(16) + 8;
            j17 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
            blockpos3 = this.field_180294_c.add(j10, j17, i14);
            (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, blockpos3);
         }
      }

   }

   protected void genStandardOre1(int blockCount, WorldGenerator generator, int minHeight, int maxHeight) {
      int j;
      if (maxHeight < minHeight) {
         j = minHeight;
         minHeight = maxHeight;
         maxHeight = j;
      } else if (maxHeight == minHeight) {
         if (minHeight < 255) {
            ++maxHeight;
         } else {
            --minHeight;
         }
      }

      for(j = 0; j < blockCount; ++j) {
         BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxHeight - minHeight) + minHeight, this.randomGenerator.nextInt(16));
         generator.generate(this.currentWorld, this.randomGenerator, blockpos);
      }

   }

   protected void genStandardOre2(int blockCount, WorldGenerator generator, int centerHeight, int spread) {
      for(int i = 0; i < blockCount; ++i) {
         BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(spread) + this.randomGenerator.nextInt(spread) + centerHeight - spread, this.randomGenerator.nextInt(16));
         generator.generate(this.currentWorld, this.randomGenerator, blockpos);
      }

   }

   protected void generateOres() {
      this.genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
      this.genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
   }
}
