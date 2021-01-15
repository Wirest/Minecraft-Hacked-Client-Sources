/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.BlockMycelium;
/*    */ 
/*    */ public class BiomeGenMushroomIsland extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenMushroomIsland(int p_i1984_1_)
/*    */   {
/* 10 */     super(p_i1984_1_);
/* 11 */     this.theBiomeDecorator.treesPerChunk = -100;
/* 12 */     this.theBiomeDecorator.flowersPerChunk = -100;
/* 13 */     this.theBiomeDecorator.grassPerChunk = -100;
/* 14 */     this.theBiomeDecorator.mushroomsPerChunk = 1;
/* 15 */     this.theBiomeDecorator.bigMushroomsPerChunk = 1;
/* 16 */     this.topBlock = net.minecraft.init.Blocks.mycelium.getDefaultState();
/* 17 */     this.spawnableMonsterList.clear();
/* 18 */     this.spawnableCreatureList.clear();
/* 19 */     this.spawnableWaterCreatureList.clear();
/* 20 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.passive.EntityMooshroom.class, 8, 4, 8));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */