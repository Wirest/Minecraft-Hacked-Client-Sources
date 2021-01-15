/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class MapGenVillage extends MapGenStructure
/*     */ {
/*  15 */   public static final List<BiomeGenBase> villageSpawnBiomes = java.util.Arrays.asList(new BiomeGenBase[] { BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna });
/*     */   
/*     */   private int terrainType;
/*     */   
/*     */   private int field_82665_g;
/*     */   private int field_82666_h;
/*     */   
/*     */   public MapGenVillage()
/*     */   {
/*  24 */     this.field_82665_g = 32;
/*  25 */     this.field_82666_h = 8;
/*     */   }
/*     */   
/*     */   public MapGenVillage(Map<String, String> p_i2093_1_)
/*     */   {
/*  30 */     this();
/*     */     
/*  32 */     for (Map.Entry<String, String> entry : p_i2093_1_.entrySet())
/*     */     {
/*  34 */       if (((String)entry.getKey()).equals("size"))
/*     */       {
/*  36 */         this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.terrainType, 0);
/*     */       }
/*  38 */       else if (((String)entry.getKey()).equals("distance"))
/*     */       {
/*  40 */         this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getStructureName()
/*     */   {
/*  47 */     return "Village";
/*     */   }
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
/*     */   {
/*  52 */     int i = chunkX;
/*  53 */     int j = chunkZ;
/*     */     
/*  55 */     if (chunkX < 0)
/*     */     {
/*  57 */       chunkX -= this.field_82665_g - 1;
/*     */     }
/*     */     
/*  60 */     if (chunkZ < 0)
/*     */     {
/*  62 */       chunkZ -= this.field_82665_g - 1;
/*     */     }
/*     */     
/*  65 */     int k = chunkX / this.field_82665_g;
/*  66 */     int l = chunkZ / this.field_82665_g;
/*  67 */     Random random = this.worldObj.setRandomSeed(k, l, 10387312);
/*  68 */     k *= this.field_82665_g;
/*  69 */     l *= this.field_82665_g;
/*  70 */     k += random.nextInt(this.field_82665_g - this.field_82666_h);
/*  71 */     l += random.nextInt(this.field_82665_g - this.field_82666_h);
/*     */     
/*  73 */     if ((i == k) && (j == l))
/*     */     {
/*  75 */       boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, villageSpawnBiomes);
/*     */       
/*  77 */       if (flag)
/*     */       {
/*  79 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*     */   {
/*  88 */     return new Start(this.worldObj, this.rand, chunkX, chunkZ, this.terrainType);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     private boolean hasMoreThanTwoComponents;
/*     */     
/*     */     public Start() {}
/*     */     
/*     */     public Start(World worldIn, Random rand, int x, int z, int p_i2092_5_)
/*     */     {
/* 101 */       super(z);
/* 102 */       List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, p_i2092_5_);
/* 103 */       StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, p_i2092_5_);
/* 104 */       this.components.add(structurevillagepieces$start);
/* 105 */       structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
/* 106 */       List<StructureComponent> list1 = structurevillagepieces$start.field_74930_j;
/* 107 */       List<StructureComponent> list2 = structurevillagepieces$start.field_74932_i;
/*     */       
/* 109 */       while ((!list1.isEmpty()) || (!list2.isEmpty()))
/*     */       {
/* 111 */         if (list1.isEmpty())
/*     */         {
/* 113 */           int i = rand.nextInt(list2.size());
/* 114 */           StructureComponent structurecomponent = (StructureComponent)list2.remove(i);
/* 115 */           structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
/*     */         }
/*     */         else
/*     */         {
/* 119 */           int j = rand.nextInt(list1.size());
/* 120 */           StructureComponent structurecomponent2 = (StructureComponent)list1.remove(j);
/* 121 */           structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
/*     */         }
/*     */       }
/*     */       
/* 125 */       updateBoundingBox();
/* 126 */       int k = 0;
/*     */       
/* 128 */       for (StructureComponent structurecomponent1 : this.components)
/*     */       {
/* 130 */         if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
/*     */         {
/* 132 */           k++;
/*     */         }
/*     */       }
/*     */       
/* 136 */       this.hasMoreThanTwoComponents = (k > 2);
/*     */     }
/*     */     
/*     */     public boolean isSizeableStructure()
/*     */     {
/* 141 */       return this.hasMoreThanTwoComponents;
/*     */     }
/*     */     
/*     */     public void writeToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 146 */       super.writeToNBT(tagCompound);
/* 147 */       tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
/*     */     }
/*     */     
/*     */     public void readFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 152 */       super.readFromNBT(tagCompound);
/* 153 */       this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */