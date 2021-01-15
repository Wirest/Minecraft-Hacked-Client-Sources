/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ public class S34PacketMaps
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int mapId;
/*     */   private byte mapScale;
/*     */   private Vec4b[] mapVisiblePlayersVec4b;
/*     */   private int mapMinX;
/*     */   private int mapMinY;
/*     */   private int mapMaxX;
/*     */   private int mapMaxY;
/*     */   private byte[] mapDataBytes;
/*     */   
/*     */   public S34PacketMaps() {}
/*     */   
/*     */   public S34PacketMaps(int mapIdIn, byte scale, Collection<Vec4b> visiblePlayers, byte[] colors, int minX, int minY, int maxX, int maxY)
/*     */   {
/*  28 */     this.mapId = mapIdIn;
/*  29 */     this.mapScale = scale;
/*  30 */     this.mapVisiblePlayersVec4b = ((Vec4b[])visiblePlayers.toArray(new Vec4b[visiblePlayers.size()]));
/*  31 */     this.mapMinX = minX;
/*  32 */     this.mapMinY = minY;
/*  33 */     this.mapMaxX = maxX;
/*  34 */     this.mapMaxY = maxY;
/*  35 */     this.mapDataBytes = new byte[maxX * maxY];
/*     */     
/*  37 */     for (int i = 0; i < maxX; i++)
/*     */     {
/*  39 */       for (int j = 0; j < maxY; j++)
/*     */       {
/*  41 */         this.mapDataBytes[(i + j * maxX)] = colors[(minX + i + (minY + j) * 128)];
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  51 */     this.mapId = buf.readVarIntFromBuffer();
/*  52 */     this.mapScale = buf.readByte();
/*  53 */     this.mapVisiblePlayersVec4b = new Vec4b[buf.readVarIntFromBuffer()];
/*     */     
/*  55 */     for (int i = 0; i < this.mapVisiblePlayersVec4b.length; i++)
/*     */     {
/*  57 */       short short1 = (short)buf.readByte();
/*  58 */       this.mapVisiblePlayersVec4b[i] = new Vec4b((byte)(short1 >> 4 & 0xF), buf.readByte(), buf.readByte(), (byte)(short1 & 0xF));
/*     */     }
/*     */     
/*  61 */     this.mapMaxX = buf.readUnsignedByte();
/*     */     
/*  63 */     if (this.mapMaxX > 0)
/*     */     {
/*  65 */       this.mapMaxY = buf.readUnsignedByte();
/*  66 */       this.mapMinX = buf.readUnsignedByte();
/*  67 */       this.mapMinY = buf.readUnsignedByte();
/*  68 */       this.mapDataBytes = buf.readByteArray();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  77 */     buf.writeVarIntToBuffer(this.mapId);
/*  78 */     buf.writeByte(this.mapScale);
/*  79 */     buf.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);
/*     */     Vec4b[] arrayOfVec4b;
/*  81 */     int j = (arrayOfVec4b = this.mapVisiblePlayersVec4b).length; for (int i = 0; i < j; i++) { Vec4b vec4b = arrayOfVec4b[i];
/*     */       
/*  83 */       buf.writeByte((vec4b.func_176110_a() & 0xF) << 4 | vec4b.func_176111_d() & 0xF);
/*  84 */       buf.writeByte(vec4b.func_176112_b());
/*  85 */       buf.writeByte(vec4b.func_176113_c());
/*     */     }
/*     */     
/*  88 */     buf.writeByte(this.mapMaxX);
/*     */     
/*  90 */     if (this.mapMaxX > 0)
/*     */     {
/*  92 */       buf.writeByte(this.mapMaxY);
/*  93 */       buf.writeByte(this.mapMinX);
/*  94 */       buf.writeByte(this.mapMinY);
/*  95 */       buf.writeByteArray(this.mapDataBytes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 104 */     handler.handleMaps(this);
/*     */   }
/*     */   
/*     */   public int getMapId()
/*     */   {
/* 109 */     return this.mapId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMapdataTo(MapData mapdataIn)
/*     */   {
/* 117 */     mapdataIn.scale = this.mapScale;
/* 118 */     mapdataIn.mapDecorations.clear();
/*     */     
/* 120 */     for (int i = 0; i < this.mapVisiblePlayersVec4b.length; i++)
/*     */     {
/* 122 */       Vec4b vec4b = this.mapVisiblePlayersVec4b[i];
/* 123 */       mapdataIn.mapDecorations.put("icon-" + i, vec4b);
/*     */     }
/*     */     
/* 126 */     for (int j = 0; j < this.mapMaxX; j++)
/*     */     {
/* 128 */       for (int k = 0; k < this.mapMaxY; k++)
/*     */       {
/* 130 */         mapdataIn.colors[(this.mapMinX + j + (this.mapMinY + k) * 128)] = this.mapDataBytes[(j + k * this.mapMaxX)];
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S34PacketMaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */