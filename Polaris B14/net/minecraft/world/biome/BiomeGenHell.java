/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.monster.EntityGhast;
/*    */ import net.minecraft.entity.monster.EntityPigZombie;
/*    */ 
/*    */ public class BiomeGenHell extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenHell(int p_i1981_1_)
/*    */   {
/* 11 */     super(p_i1981_1_);
/* 12 */     this.spawnableMonsterList.clear();
/* 13 */     this.spawnableCreatureList.clear();
/* 14 */     this.spawnableWaterCreatureList.clear();
/* 15 */     this.spawnableCaveCreatureList.clear();
/* 16 */     this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityGhast.class, 50, 4, 4));
/* 17 */     this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
/* 18 */     this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.monster.EntityMagmaCube.class, 1, 4, 4));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */