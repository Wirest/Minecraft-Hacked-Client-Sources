/*    */ package net.minecraft.world.gen.structure;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.monster.EntityMagmaCube;
/*    */ import net.minecraft.entity.monster.EntityPigZombie;
/*    */ import net.minecraft.entity.monster.EntitySkeleton;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*    */ 
/*    */ public class MapGenNetherBridge extends MapGenStructure
/*    */ {
/* 15 */   private List<BiomeGenBase.SpawnListEntry> spawnList = Lists.newArrayList();
/*    */   
/*    */   public MapGenNetherBridge()
/*    */   {
/* 19 */     this.spawnList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.monster.EntityBlaze.class, 10, 2, 3));
/* 20 */     this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
/* 21 */     this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
/* 22 */     this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
/*    */   }
/*    */   
/*    */   public String getStructureName()
/*    */   {
/* 27 */     return "Fortress";
/*    */   }
/*    */   
/*    */   public List<BiomeGenBase.SpawnListEntry> getSpawnList()
/*    */   {
/* 32 */     return this.spawnList;
/*    */   }
/*    */   
/*    */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
/*    */   {
/* 37 */     int i = chunkX >> 4;
/* 38 */     int j = chunkZ >> 4;
/* 39 */     this.rand.setSeed(i ^ j << 4 ^ this.worldObj.getSeed());
/* 40 */     this.rand.nextInt();
/* 41 */     return this.rand.nextInt(3) == 0;
/*    */   }
/*    */   
/*    */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*    */   {
/* 46 */     return new Start(this.worldObj, this.rand, chunkX, chunkZ);
/*    */   }
/*    */   
/*    */ 
/*    */   public static class Start
/*    */     extends StructureStart
/*    */   {
/*    */     public Start() {}
/*    */     
/*    */     public Start(World worldIn, Random p_i2040_2_, int p_i2040_3_, int p_i2040_4_)
/*    */     {
/* 57 */       super(p_i2040_4_);
/* 58 */       StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
/* 59 */       this.components.add(structurenetherbridgepieces$start);
/* 60 */       structurenetherbridgepieces$start.buildComponent(structurenetherbridgepieces$start, this.components, p_i2040_2_);
/* 61 */       List<StructureComponent> list = structurenetherbridgepieces$start.field_74967_d;
/*    */       
/* 63 */       while (!list.isEmpty())
/*    */       {
/* 65 */         int i = p_i2040_2_.nextInt(list.size());
/* 66 */         StructureComponent structurecomponent = (StructureComponent)list.remove(i);
/* 67 */         structurecomponent.buildComponent(structurenetherbridgepieces$start, this.components, p_i2040_2_);
/*    */       }
/*    */       
/* 70 */       updateBoundingBox();
/* 71 */       setRandomHeight(worldIn, p_i2040_2_, 48, 70);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenNetherBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */