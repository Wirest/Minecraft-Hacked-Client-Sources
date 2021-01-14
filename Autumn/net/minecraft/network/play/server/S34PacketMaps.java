package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class S34PacketMaps implements Packet {
   private int mapId;
   private byte mapScale;
   private Vec4b[] mapVisiblePlayersVec4b;
   private int mapMinX;
   private int mapMinY;
   private int mapMaxX;
   private int mapMaxY;
   private byte[] mapDataBytes;

   public S34PacketMaps() {
   }

   public S34PacketMaps(int mapIdIn, byte scale, Collection visiblePlayers, byte[] colors, int minX, int minY, int maxX, int maxY) {
      this.mapId = mapIdIn;
      this.mapScale = scale;
      this.mapVisiblePlayersVec4b = (Vec4b[])((Vec4b[])visiblePlayers.toArray(new Vec4b[visiblePlayers.size()]));
      this.mapMinX = minX;
      this.mapMinY = minY;
      this.mapMaxX = maxX;
      this.mapMaxY = maxY;
      this.mapDataBytes = new byte[maxX * maxY];

      for(int i = 0; i < maxX; ++i) {
         for(int j = 0; j < maxY; ++j) {
            this.mapDataBytes[i + j * maxX] = colors[minX + i + (minY + j) * 128];
         }
      }

   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.mapId = buf.readVarIntFromBuffer();
      this.mapScale = buf.readByte();
      this.mapVisiblePlayersVec4b = new Vec4b[buf.readVarIntFromBuffer()];

      for(int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
         short short1 = (short)buf.readByte();
         this.mapVisiblePlayersVec4b[i] = new Vec4b((byte)(short1 >> 4 & 15), buf.readByte(), buf.readByte(), (byte)(short1 & 15));
      }

      this.mapMaxX = buf.readUnsignedByte();
      if (this.mapMaxX > 0) {
         this.mapMaxY = buf.readUnsignedByte();
         this.mapMinX = buf.readUnsignedByte();
         this.mapMinY = buf.readUnsignedByte();
         this.mapDataBytes = buf.readByteArray();
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.mapId);
      buf.writeByte(this.mapScale);
      buf.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);
      Vec4b[] var2 = this.mapVisiblePlayersVec4b;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Vec4b vec4b = var2[var4];
         buf.writeByte((vec4b.func_176110_a() & 15) << 4 | vec4b.func_176111_d() & 15);
         buf.writeByte(vec4b.func_176112_b());
         buf.writeByte(vec4b.func_176113_c());
      }

      buf.writeByte(this.mapMaxX);
      if (this.mapMaxX > 0) {
         buf.writeByte(this.mapMaxY);
         buf.writeByte(this.mapMinX);
         buf.writeByte(this.mapMinY);
         buf.writeByteArray(this.mapDataBytes);
      }

   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleMaps(this);
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapdataTo(MapData mapdataIn) {
      mapdataIn.scale = this.mapScale;
      mapdataIn.mapDecorations.clear();

      int j;
      for(j = 0; j < this.mapVisiblePlayersVec4b.length; ++j) {
         Vec4b vec4b = this.mapVisiblePlayersVec4b[j];
         mapdataIn.mapDecorations.put("icon-" + j, vec4b);
      }

      for(j = 0; j < this.mapMaxX; ++j) {
         for(int k = 0; k < this.mapMaxY; ++k) {
            mapdataIn.colors[this.mapMinX + j + (this.mapMinY + k) * 128] = this.mapDataBytes[j + k * this.mapMaxX];
         }
      }

   }
}
