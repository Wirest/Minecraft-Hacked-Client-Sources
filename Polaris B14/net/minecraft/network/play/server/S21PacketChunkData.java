/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.NibbleArray;
/*     */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*     */ 
/*     */ public class S21PacketChunkData implements net.minecraft.network.Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int chunkX;
/*     */   private int chunkZ;
/*     */   private Extracted extractedData;
/*     */   private boolean field_149279_g;
/*     */   
/*     */   public S21PacketChunkData() {}
/*     */   
/*     */   public S21PacketChunkData(Chunk chunkIn, boolean p_i45196_2_, int p_i45196_3_)
/*     */   {
/*  25 */     this.chunkX = chunkIn.xPosition;
/*  26 */     this.chunkZ = chunkIn.zPosition;
/*  27 */     this.field_149279_g = p_i45196_2_;
/*  28 */     this.extractedData = func_179756_a(chunkIn, p_i45196_2_, !chunkIn.getWorld().provider.getHasNoSky(), p_i45196_3_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  36 */     this.chunkX = buf.readInt();
/*  37 */     this.chunkZ = buf.readInt();
/*  38 */     this.field_149279_g = buf.readBoolean();
/*  39 */     this.extractedData = new Extracted();
/*  40 */     this.extractedData.dataSize = buf.readShort();
/*  41 */     this.extractedData.data = buf.readByteArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  49 */     buf.writeInt(this.chunkX);
/*  50 */     buf.writeInt(this.chunkZ);
/*  51 */     buf.writeBoolean(this.field_149279_g);
/*  52 */     buf.writeShort((short)(this.extractedData.dataSize & 0xFFFF));
/*  53 */     buf.writeByteArray(this.extractedData.data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  61 */     handler.handleChunkData(this);
/*     */   }
/*     */   
/*     */   public byte[] func_149272_d()
/*     */   {
/*  66 */     return this.extractedData.data;
/*     */   }
/*     */   
/*     */   protected static int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_)
/*     */   {
/*  71 */     int i = p_180737_0_ * 2 * 16 * 16 * 16;
/*  72 */     int j = p_180737_0_ * 16 * 16 * 16 / 2;
/*  73 */     int k = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
/*  74 */     int l = p_180737_2_ ? 256 : 0;
/*  75 */     return i + j + k + l;
/*     */   }
/*     */   
/*     */   public static Extracted func_179756_a(Chunk p_179756_0_, boolean p_179756_1_, boolean p_179756_2_, int p_179756_3_)
/*     */   {
/*  80 */     ExtendedBlockStorage[] aextendedblockstorage = p_179756_0_.getBlockStorageArray();
/*  81 */     Extracted s21packetchunkdata$extracted = new Extracted();
/*  82 */     List<ExtendedBlockStorage> list = com.google.common.collect.Lists.newArrayList();
/*     */     
/*  84 */     for (int i = 0; i < aextendedblockstorage.length; i++)
/*     */     {
/*  86 */       ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[i];
/*     */       
/*  88 */       if ((extendedblockstorage != null) && ((!p_179756_1_) || (!extendedblockstorage.isEmpty())) && ((p_179756_3_ & 1 << i) != 0))
/*     */       {
/*  90 */         s21packetchunkdata$extracted.dataSize |= 1 << i;
/*  91 */         list.add(extendedblockstorage);
/*     */       }
/*     */     }
/*     */     
/*  95 */     s21packetchunkdata$extracted.data = new byte[func_180737_a(Integer.bitCount(s21packetchunkdata$extracted.dataSize), p_179756_2_, p_179756_1_)];
/*  96 */     int j = 0;
/*     */     int j;
/*  98 */     int i; for (Iterator localIterator = list.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 102 */         i < j)
/*     */     {
/*  98 */       ExtendedBlockStorage extendedblockstorage1 = (ExtendedBlockStorage)localIterator.next();
/*     */       
/* 100 */       char[] achar = extendedblockstorage1.getData();
/*     */       char[] arrayOfChar1;
/* 102 */       j = (arrayOfChar1 = achar).length;i = 0; continue;char c0 = arrayOfChar1[i];
/*     */       
/* 104 */       s21packetchunkdata$extracted.data[(j++)] = ((byte)(c0 & 0xFF));
/* 105 */       s21packetchunkdata$extracted.data[(j++)] = ((byte)(c0 >> '\b' & 0xFF));i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */     for (ExtendedBlockStorage extendedblockstorage2 : list)
/*     */     {
/* 111 */       j = func_179757_a(extendedblockstorage2.getBlocklightArray().getData(), s21packetchunkdata$extracted.data, j);
/*     */     }
/*     */     
/* 114 */     if (p_179756_2_)
/*     */     {
/* 116 */       for (ExtendedBlockStorage extendedblockstorage3 : list)
/*     */       {
/* 118 */         j = func_179757_a(extendedblockstorage3.getSkylightArray().getData(), s21packetchunkdata$extracted.data, j);
/*     */       }
/*     */     }
/*     */     
/* 122 */     if (p_179756_1_)
/*     */     {
/* 124 */       func_179757_a(p_179756_0_.getBiomeArray(), s21packetchunkdata$extracted.data, j);
/*     */     }
/*     */     
/* 127 */     return s21packetchunkdata$extracted;
/*     */   }
/*     */   
/*     */   private static int func_179757_a(byte[] p_179757_0_, byte[] p_179757_1_, int p_179757_2_)
/*     */   {
/* 132 */     System.arraycopy(p_179757_0_, 0, p_179757_1_, p_179757_2_, p_179757_0_.length);
/* 133 */     return p_179757_2_ + p_179757_0_.length;
/*     */   }
/*     */   
/*     */   public int getChunkX()
/*     */   {
/* 138 */     return this.chunkX;
/*     */   }
/*     */   
/*     */   public int getChunkZ()
/*     */   {
/* 143 */     return this.chunkZ;
/*     */   }
/*     */   
/*     */   public int getExtractedSize()
/*     */   {
/* 148 */     return this.extractedData.dataSize;
/*     */   }
/*     */   
/*     */   public boolean func_149274_i()
/*     */   {
/* 153 */     return this.field_149279_g;
/*     */   }
/*     */   
/*     */   public static class Extracted
/*     */   {
/*     */     public byte[] data;
/*     */     public int dataSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S21PacketChunkData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */