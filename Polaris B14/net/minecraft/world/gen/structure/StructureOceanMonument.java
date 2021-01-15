/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.monster.EntityGuardian;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ 
/*     */ public class StructureOceanMonument extends MapGenStructure
/*     */ {
/*     */   private int field_175800_f;
/*     */   private int field_175801_g;
/*  25 */   public static final List<BiomeGenBase> field_175802_d = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver });
/*  26 */   private static final List<BiomeGenBase.SpawnListEntry> field_175803_h = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */   public StructureOceanMonument()
/*     */   {
/*  30 */     this.field_175800_f = 32;
/*  31 */     this.field_175801_g = 5;
/*     */   }
/*     */   
/*     */   public StructureOceanMonument(Map<String, String> p_i45608_1_)
/*     */   {
/*  36 */     this();
/*     */     
/*  38 */     for (Map.Entry<String, String> entry : p_i45608_1_.entrySet())
/*     */     {
/*  40 */       if (((String)entry.getKey()).equals("spacing"))
/*     */       {
/*  42 */         this.field_175800_f = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_175800_f, 1);
/*     */       }
/*  44 */       else if (((String)entry.getKey()).equals("separation"))
/*     */       {
/*  46 */         this.field_175801_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_175801_g, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getStructureName()
/*     */   {
/*  53 */     return "Monument";
/*     */   }
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
/*     */   {
/*  58 */     int i = chunkX;
/*  59 */     int j = chunkZ;
/*     */     
/*  61 */     if (chunkX < 0)
/*     */     {
/*  63 */       chunkX -= this.field_175800_f - 1;
/*     */     }
/*     */     
/*  66 */     if (chunkZ < 0)
/*     */     {
/*  68 */       chunkZ -= this.field_175800_f - 1;
/*     */     }
/*     */     
/*  71 */     int k = chunkX / this.field_175800_f;
/*  72 */     int l = chunkZ / this.field_175800_f;
/*  73 */     Random random = this.worldObj.setRandomSeed(k, l, 10387313);
/*  74 */     k *= this.field_175800_f;
/*  75 */     l *= this.field_175800_f;
/*  76 */     k += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
/*  77 */     l += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
/*     */     
/*  79 */     if ((i == k) && (j == l))
/*     */     {
/*  81 */       if (this.worldObj.getWorldChunkManager().getBiomeGenerator(new net.minecraft.util.BlockPos(i * 16 + 8, 64, j * 16 + 8), null) != BiomeGenBase.deepOcean)
/*     */       {
/*  83 */         return false;
/*     */       }
/*     */       
/*  86 */       boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, field_175802_d);
/*     */       
/*  88 */       if (flag)
/*     */       {
/*  90 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*     */   {
/*  99 */     return new StartMonument(this.worldObj, this.rand, chunkX, chunkZ);
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> func_175799_b()
/*     */   {
/* 104 */     return field_175803_h;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 109 */     field_175803_h.add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
/*     */   }
/*     */   
/*     */   public static class StartMonument extends StructureStart
/*     */   {
/* 114 */     private Set<ChunkCoordIntPair> field_175791_c = com.google.common.collect.Sets.newHashSet();
/*     */     
/*     */     private boolean field_175790_d;
/*     */     
/*     */ 
/*     */     public StartMonument() {}
/*     */     
/*     */     public StartMonument(World worldIn, Random p_i45607_2_, int p_i45607_3_, int p_i45607_4_)
/*     */     {
/* 123 */       super(p_i45607_4_);
/* 124 */       func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
/*     */     }
/*     */     
/*     */     private void func_175789_b(World worldIn, Random p_175789_2_, int p_175789_3_, int p_175789_4_)
/*     */     {
/* 129 */       p_175789_2_.setSeed(worldIn.getSeed());
/* 130 */       long i = p_175789_2_.nextLong();
/* 131 */       long j = p_175789_2_.nextLong();
/* 132 */       long k = p_175789_3_ * i;
/* 133 */       long l = p_175789_4_ * j;
/* 134 */       p_175789_2_.setSeed(k ^ l ^ worldIn.getSeed());
/* 135 */       int i1 = p_175789_3_ * 16 + 8 - 29;
/* 136 */       int j1 = p_175789_4_ * 16 + 8 - 29;
/* 137 */       net.minecraft.util.EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
/* 138 */       this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, i1, j1, enumfacing));
/* 139 */       updateBoundingBox();
/* 140 */       this.field_175790_d = true;
/*     */     }
/*     */     
/*     */     public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
/*     */     {
/* 145 */       if (!this.field_175790_d)
/*     */       {
/* 147 */         this.components.clear();
/* 148 */         func_175789_b(worldIn, rand, getChunkPosX(), getChunkPosZ());
/*     */       }
/*     */       
/* 151 */       super.generateStructure(worldIn, rand, structurebb);
/*     */     }
/*     */     
/*     */     public boolean func_175788_a(ChunkCoordIntPair pair)
/*     */     {
/* 156 */       return this.field_175791_c.contains(pair) ? false : super.func_175788_a(pair);
/*     */     }
/*     */     
/*     */     public void func_175787_b(ChunkCoordIntPair pair)
/*     */     {
/* 161 */       super.func_175787_b(pair);
/* 162 */       this.field_175791_c.add(pair);
/*     */     }
/*     */     
/*     */     public void writeToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 167 */       super.writeToNBT(tagCompound);
/* 168 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/* 170 */       for (ChunkCoordIntPair chunkcoordintpair : this.field_175791_c)
/*     */       {
/* 172 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 173 */         nbttagcompound.setInteger("X", chunkcoordintpair.chunkXPos);
/* 174 */         nbttagcompound.setInteger("Z", chunkcoordintpair.chunkZPos);
/* 175 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */       
/* 178 */       tagCompound.setTag("Processed", nbttaglist);
/*     */     }
/*     */     
/*     */     public void readFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 183 */       super.readFromNBT(tagCompound);
/*     */       
/* 185 */       if (tagCompound.hasKey("Processed", 9))
/*     */       {
/* 187 */         NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
/*     */         
/* 189 */         for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */         {
/* 191 */           NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 192 */           this.field_175791_c.add(new ChunkCoordIntPair(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureOceanMonument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */