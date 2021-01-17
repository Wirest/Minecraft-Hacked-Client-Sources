// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.storage.MapData;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.Collection;
import net.minecraft.util.Vec4b;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S34PacketMaps implements Packet<INetHandlerPlayClient>
{
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
    
    public S34PacketMaps(final int mapIdIn, final byte scale, final Collection<Vec4b> visiblePlayers, final byte[] colors, final int minX, final int minY, final int maxX, final int maxY) {
        this.mapId = mapIdIn;
        this.mapScale = scale;
        this.mapVisiblePlayersVec4b = visiblePlayers.toArray(new Vec4b[visiblePlayers.size()]);
        this.mapMinX = minX;
        this.mapMinY = minY;
        this.mapMaxX = maxX;
        this.mapMaxY = maxY;
        this.mapDataBytes = new byte[maxX * maxY];
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                this.mapDataBytes[i + j * maxX] = colors[minX + i + (minY + j) * 128];
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.mapId = buf.readVarIntFromBuffer();
        this.mapScale = buf.readByte();
        this.mapVisiblePlayersVec4b = new Vec4b[buf.readVarIntFromBuffer()];
        for (int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
            final short short1 = buf.readByte();
            this.mapVisiblePlayersVec4b[i] = new Vec4b((byte)(short1 >> 4 & 0xF), buf.readByte(), buf.readByte(), (byte)(short1 & 0xF));
        }
        this.mapMaxX = buf.readUnsignedByte();
        if (this.mapMaxX > 0) {
            this.mapMaxY = buf.readUnsignedByte();
            this.mapMinX = buf.readUnsignedByte();
            this.mapMinY = buf.readUnsignedByte();
            this.mapDataBytes = buf.readByteArray();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.mapId);
        buf.writeByte(this.mapScale);
        buf.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);
        Vec4b[] mapVisiblePlayersVec4b;
        for (int length = (mapVisiblePlayersVec4b = this.mapVisiblePlayersVec4b).length, i = 0; i < length; ++i) {
            final Vec4b vec4b = mapVisiblePlayersVec4b[i];
            buf.writeByte((vec4b.func_176110_a() & 0xF) << 4 | (vec4b.func_176111_d() & 0xF));
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
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleMaps(this);
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public void setMapdataTo(final MapData mapdataIn) {
        mapdataIn.scale = this.mapScale;
        mapdataIn.mapDecorations.clear();
        for (int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
            final Vec4b vec4b = this.mapVisiblePlayersVec4b[i];
            mapdataIn.mapDecorations.put("icon-" + i, vec4b);
        }
        for (int j = 0; j < this.mapMaxX; ++j) {
            for (int k = 0; k < this.mapMaxY; ++k) {
                mapdataIn.colors[this.mapMinX + j + (this.mapMinY + k) * 128] = this.mapDataBytes[j + k * this.mapMaxX];
            }
        }
    }
}
