/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.NibbleArray;
/*     */ 
/*     */ public class ChunkLoader
/*     */ {
/*     */   public static AnvilConverterData load(NBTTagCompound nbt)
/*     */   {
/*  14 */     int i = nbt.getInteger("xPos");
/*  15 */     int j = nbt.getInteger("zPos");
/*  16 */     AnvilConverterData chunkloader$anvilconverterdata = new AnvilConverterData(i, j);
/*  17 */     chunkloader$anvilconverterdata.blocks = nbt.getByteArray("Blocks");
/*  18 */     chunkloader$anvilconverterdata.data = new NibbleArrayReader(nbt.getByteArray("Data"), 7);
/*  19 */     chunkloader$anvilconverterdata.skyLight = new NibbleArrayReader(nbt.getByteArray("SkyLight"), 7);
/*  20 */     chunkloader$anvilconverterdata.blockLight = new NibbleArrayReader(nbt.getByteArray("BlockLight"), 7);
/*  21 */     chunkloader$anvilconverterdata.heightmap = nbt.getByteArray("HeightMap");
/*  22 */     chunkloader$anvilconverterdata.terrainPopulated = nbt.getBoolean("TerrainPopulated");
/*  23 */     chunkloader$anvilconverterdata.entities = nbt.getTagList("Entities", 10);
/*  24 */     chunkloader$anvilconverterdata.tileEntities = nbt.getTagList("TileEntities", 10);
/*  25 */     chunkloader$anvilconverterdata.tileTicks = nbt.getTagList("TileTicks", 10);
/*     */     
/*     */     try
/*     */     {
/*  29 */       chunkloader$anvilconverterdata.lastUpdated = nbt.getLong("LastUpdate");
/*     */     }
/*     */     catch (ClassCastException var5)
/*     */     {
/*  33 */       chunkloader$anvilconverterdata.lastUpdated = nbt.getInteger("LastUpdate");
/*     */     }
/*     */     
/*  36 */     return chunkloader$anvilconverterdata;
/*     */   }
/*     */   
/*     */   public static void convertToAnvilFormat(AnvilConverterData p_76690_0_, NBTTagCompound p_76690_1_, WorldChunkManager p_76690_2_)
/*     */   {
/*  41 */     p_76690_1_.setInteger("xPos", p_76690_0_.x);
/*  42 */     p_76690_1_.setInteger("zPos", p_76690_0_.z);
/*  43 */     p_76690_1_.setLong("LastUpdate", p_76690_0_.lastUpdated);
/*  44 */     int[] aint = new int[p_76690_0_.heightmap.length];
/*     */     
/*  46 */     for (int i = 0; i < p_76690_0_.heightmap.length; i++)
/*     */     {
/*  48 */       aint[i] = p_76690_0_.heightmap[i];
/*     */     }
/*     */     
/*  51 */     p_76690_1_.setIntArray("HeightMap", aint);
/*  52 */     p_76690_1_.setBoolean("TerrainPopulated", p_76690_0_.terrainPopulated);
/*  53 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  55 */     for (int j = 0; j < 8; j++)
/*     */     {
/*  57 */       boolean flag = true;
/*     */       
/*  59 */       for (int k = 0; (k < 16) && (flag); k++)
/*     */       {
/*  61 */         for (int l = 0; (l < 16) && (flag); l++)
/*     */         {
/*  63 */           for (int i1 = 0; i1 < 16; i1++)
/*     */           {
/*  65 */             int j1 = k << 11 | i1 << 7 | l + (j << 4);
/*  66 */             int k1 = p_76690_0_.blocks[j1];
/*     */             
/*  68 */             if (k1 != 0)
/*     */             {
/*  70 */               flag = false;
/*  71 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  77 */       if (!flag)
/*     */       {
/*  79 */         byte[] abyte1 = new byte['က'];
/*  80 */         NibbleArray nibblearray = new NibbleArray();
/*  81 */         NibbleArray nibblearray1 = new NibbleArray();
/*  82 */         NibbleArray nibblearray2 = new NibbleArray();
/*     */         
/*  84 */         for (int j3 = 0; j3 < 16; j3++)
/*     */         {
/*  86 */           for (int l1 = 0; l1 < 16; l1++)
/*     */           {
/*  88 */             for (int i2 = 0; i2 < 16; i2++)
/*     */             {
/*  90 */               int j2 = j3 << 11 | i2 << 7 | l1 + (j << 4);
/*  91 */               int k2 = p_76690_0_.blocks[j2];
/*  92 */               abyte1[(l1 << 8 | i2 << 4 | j3)] = ((byte)(k2 & 0xFF));
/*  93 */               nibblearray.set(j3, l1, i2, p_76690_0_.data.get(j3, l1 + (j << 4), i2));
/*  94 */               nibblearray1.set(j3, l1, i2, p_76690_0_.skyLight.get(j3, l1 + (j << 4), i2));
/*  95 */               nibblearray2.set(j3, l1, i2, p_76690_0_.blockLight.get(j3, l1 + (j << 4), i2));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 100 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 101 */         nbttagcompound.setByte("Y", (byte)(j & 0xFF));
/* 102 */         nbttagcompound.setByteArray("Blocks", abyte1);
/* 103 */         nbttagcompound.setByteArray("Data", nibblearray.getData());
/* 104 */         nbttagcompound.setByteArray("SkyLight", nibblearray1.getData());
/* 105 */         nbttagcompound.setByteArray("BlockLight", nibblearray2.getData());
/* 106 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 110 */     p_76690_1_.setTag("Sections", nbttaglist);
/* 111 */     byte[] abyte = new byte['Ā'];
/* 112 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 114 */     for (int l2 = 0; l2 < 16; l2++)
/*     */     {
/* 116 */       for (int i3 = 0; i3 < 16; i3++)
/*     */       {
/* 118 */         blockpos$mutableblockpos.func_181079_c(p_76690_0_.x << 4 | l2, 0, p_76690_0_.z << 4 | i3);
/* 119 */         abyte[(i3 << 4 | l2)] = ((byte)(p_76690_2_.getBiomeGenerator(blockpos$mutableblockpos, BiomeGenBase.field_180279_ad).biomeID & 0xFF));
/*     */       }
/*     */     }
/*     */     
/* 123 */     p_76690_1_.setByteArray("Biomes", abyte);
/* 124 */     p_76690_1_.setTag("Entities", p_76690_0_.entities);
/* 125 */     p_76690_1_.setTag("TileEntities", p_76690_0_.tileEntities);
/*     */     
/* 127 */     if (p_76690_0_.tileTicks != null)
/*     */     {
/* 129 */       p_76690_1_.setTag("TileTicks", p_76690_0_.tileTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AnvilConverterData
/*     */   {
/*     */     public long lastUpdated;
/*     */     public boolean terrainPopulated;
/*     */     public byte[] heightmap;
/*     */     public NibbleArrayReader blockLight;
/*     */     public NibbleArrayReader skyLight;
/*     */     public NibbleArrayReader data;
/*     */     public byte[] blocks;
/*     */     public NBTTagList entities;
/*     */     public NBTTagList tileEntities;
/*     */     public NBTTagList tileTicks;
/*     */     public final int x;
/*     */     public final int z;
/*     */     
/*     */     public AnvilConverterData(int p_i1999_1_, int p_i1999_2_)
/*     */     {
/* 150 */       this.x = p_i1999_1_;
/* 151 */       this.z = p_i1999_2_;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\ChunkLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */