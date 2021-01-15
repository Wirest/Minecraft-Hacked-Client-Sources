/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.List;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ public class RegionFile
/*     */ {
/*  20 */   private static final byte[] emptySector = new byte['က'];
/*     */   private final File fileName;
/*     */   private RandomAccessFile dataFile;
/*  23 */   private final int[] offsets = new int['Ѐ'];
/*  24 */   private final int[] chunkTimestamps = new int['Ѐ'];
/*     */   
/*     */   private List<Boolean> sectorFree;
/*     */   
/*     */   private int sizeDelta;
/*     */   private long lastModified;
/*     */   
/*     */   public RegionFile(File fileNameIn)
/*     */   {
/*  33 */     this.fileName = fileNameIn;
/*  34 */     this.sizeDelta = 0;
/*     */     
/*     */     try
/*     */     {
/*  38 */       if (fileNameIn.exists())
/*     */       {
/*  40 */         this.lastModified = fileNameIn.lastModified();
/*     */       }
/*     */       
/*  43 */       this.dataFile = new RandomAccessFile(fileNameIn, "rw");
/*     */       
/*  45 */       if (this.dataFile.length() < 4096L)
/*     */       {
/*  47 */         for (int i = 0; i < 1024; i++)
/*     */         {
/*  49 */           this.dataFile.writeInt(0);
/*     */         }
/*     */         
/*  52 */         for (int i1 = 0; i1 < 1024; i1++)
/*     */         {
/*  54 */           this.dataFile.writeInt(0);
/*     */         }
/*     */         
/*  57 */         this.sizeDelta += 8192;
/*     */       }
/*     */       
/*  60 */       if ((this.dataFile.length() & 0xFFF) != 0L)
/*     */       {
/*  62 */         for (int j1 = 0; j1 < (this.dataFile.length() & 0xFFF); j1++)
/*     */         {
/*  64 */           this.dataFile.write(0);
/*     */         }
/*     */       }
/*     */       
/*  68 */       int k1 = (int)this.dataFile.length() / 4096;
/*  69 */       this.sectorFree = Lists.newArrayListWithCapacity(k1);
/*     */       
/*  71 */       for (int j = 0; j < k1; j++)
/*     */       {
/*  73 */         this.sectorFree.add(Boolean.valueOf(true));
/*     */       }
/*     */       
/*  76 */       this.sectorFree.set(0, Boolean.valueOf(false));
/*  77 */       this.sectorFree.set(1, Boolean.valueOf(false));
/*  78 */       this.dataFile.seek(0L);
/*     */       
/*  80 */       for (int l1 = 0; l1 < 1024; l1++)
/*     */       {
/*  82 */         int k = this.dataFile.readInt();
/*  83 */         this.offsets[l1] = k;
/*     */         
/*  85 */         if ((k != 0) && ((k >> 8) + (k & 0xFF) <= this.sectorFree.size()))
/*     */         {
/*  87 */           for (int l = 0; l < (k & 0xFF); l++)
/*     */           {
/*  89 */             this.sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  94 */       for (int i2 = 0; i2 < 1024; i2++)
/*     */       {
/*  96 */         int j2 = this.dataFile.readInt();
/*  97 */         this.chunkTimestamps[i2] = j2;
/*     */       }
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 102 */       ioexception.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized DataInputStream getChunkDataInputStream(int x, int z)
/*     */   {
/* 111 */     if (outOfBounds(x, z))
/*     */     {
/* 113 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 119 */       int i = getOffset(x, z);
/*     */       
/* 121 */       if (i == 0)
/*     */       {
/* 123 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 127 */       int j = i >> 8;
/* 128 */       int k = i & 0xFF;
/*     */       
/* 130 */       if (j + k > this.sectorFree.size())
/*     */       {
/* 132 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 136 */       this.dataFile.seek(j * 4096);
/* 137 */       int l = this.dataFile.readInt();
/*     */       
/* 139 */       if (l > 4096 * k)
/*     */       {
/* 141 */         return null;
/*     */       }
/* 143 */       if (l <= 0)
/*     */       {
/* 145 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 149 */       byte b0 = this.dataFile.readByte();
/*     */       
/* 151 */       if (b0 == 1)
/*     */       {
/* 153 */         byte[] abyte1 = new byte[l - 1];
/* 154 */         this.dataFile.read(abyte1);
/* 155 */         return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte1))));
/*     */       }
/* 157 */       if (b0 == 2)
/*     */       {
/* 159 */         byte[] abyte = new byte[l - 1];
/* 160 */         this.dataFile.read(abyte);
/* 161 */         return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte))));
/*     */       }
/*     */       
/*     */ 
/* 165 */       return null;
/*     */     }
/*     */     catch (IOException var9) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataOutputStream getChunkDataOutputStream(int x, int z)
/*     */   {
/* 183 */     return outOfBounds(x, z) ? null : new DataOutputStream(new DeflaterOutputStream(new ChunkBuffer(x, z)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void write(int x, int z, byte[] data, int length)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       int i = getOffset(x, z);
/* 194 */       int j = i >> 8;
/* 195 */       int k = i & 0xFF;
/* 196 */       int l = (length + 5) / 4096 + 1;
/*     */       
/* 198 */       if (l >= 256)
/*     */       {
/* 200 */         return;
/*     */       }
/*     */       
/* 203 */       if ((j != 0) && (k == l))
/*     */       {
/* 205 */         write(j, data, length);
/*     */       }
/*     */       else
/*     */       {
/* 209 */         for (int i1 = 0; i1 < k; i1++)
/*     */         {
/* 211 */           this.sectorFree.set(j + i1, Boolean.valueOf(true));
/*     */         }
/*     */         
/* 214 */         int l1 = this.sectorFree.indexOf(Boolean.valueOf(true));
/* 215 */         int j1 = 0;
/*     */         
/* 217 */         if (l1 != -1)
/*     */         {
/* 219 */           for (int k1 = l1; k1 < this.sectorFree.size(); k1++)
/*     */           {
/* 221 */             if (j1 != 0)
/*     */             {
/* 223 */               if (((Boolean)this.sectorFree.get(k1)).booleanValue())
/*     */               {
/* 225 */                 j1++;
/*     */               }
/*     */               else
/*     */               {
/* 229 */                 j1 = 0;
/*     */               }
/*     */             }
/* 232 */             else if (((Boolean)this.sectorFree.get(k1)).booleanValue())
/*     */             {
/* 234 */               l1 = k1;
/* 235 */               j1 = 1;
/*     */             }
/*     */             
/* 238 */             if (j1 >= l) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 245 */         if (j1 >= l)
/*     */         {
/* 247 */           j = l1;
/* 248 */           setOffset(x, z, l1 << 8 | l);
/*     */           
/* 250 */           for (int j2 = 0; j2 < l; j2++)
/*     */           {
/* 252 */             this.sectorFree.set(j + j2, Boolean.valueOf(false));
/*     */           }
/*     */           
/* 255 */           write(j, data, length);
/*     */         }
/*     */         else
/*     */         {
/* 259 */           this.dataFile.seek(this.dataFile.length());
/* 260 */           j = this.sectorFree.size();
/*     */           
/* 262 */           for (int i2 = 0; i2 < l; i2++)
/*     */           {
/* 264 */             this.dataFile.write(emptySector);
/* 265 */             this.sectorFree.add(Boolean.valueOf(false));
/*     */           }
/*     */           
/* 268 */           this.sizeDelta += 4096 * l;
/* 269 */           write(j, data, length);
/* 270 */           setOffset(x, z, j << 8 | l);
/*     */         }
/*     */       }
/*     */       
/* 274 */       setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 278 */       ioexception.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void write(int sectorNumber, byte[] data, int length)
/*     */     throws IOException
/*     */   {
/* 287 */     this.dataFile.seek(sectorNumber * 4096);
/* 288 */     this.dataFile.writeInt(length + 1);
/* 289 */     this.dataFile.writeByte(2);
/* 290 */     this.dataFile.write(data, 0, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean outOfBounds(int x, int z)
/*     */   {
/* 298 */     return (x < 0) || (x >= 32) || (z < 0) || (z >= 32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getOffset(int x, int z)
/*     */   {
/* 306 */     return this.offsets[(x + z * 32)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChunkSaved(int x, int z)
/*     */   {
/* 314 */     return getOffset(x, z) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void setOffset(int x, int z, int offset)
/*     */     throws IOException
/*     */   {
/* 322 */     this.offsets[(x + z * 32)] = offset;
/* 323 */     this.dataFile.seek((x + z * 32) * 4);
/* 324 */     this.dataFile.writeInt(offset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void setChunkTimestamp(int x, int z, int timestamp)
/*     */     throws IOException
/*     */   {
/* 332 */     this.chunkTimestamps[(x + z * 32)] = timestamp;
/* 333 */     this.dataFile.seek(4096 + (x + z * 32) * 4);
/* 334 */     this.dataFile.writeInt(timestamp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 342 */     if (this.dataFile != null)
/*     */     {
/* 344 */       this.dataFile.close();
/*     */     }
/*     */   }
/*     */   
/*     */   class ChunkBuffer extends ByteArrayOutputStream
/*     */   {
/*     */     private int chunkX;
/*     */     private int chunkZ;
/*     */     
/*     */     public ChunkBuffer(int x, int z)
/*     */     {
/* 355 */       super();
/* 356 */       this.chunkX = x;
/* 357 */       this.chunkZ = z;
/*     */     }
/*     */     
/*     */     public void close() throws IOException
/*     */     {
/* 362 */       RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\RegionFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */