// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S07PacketRespawn implements Packet<INetHandlerPlayClient>
{
    private int dimensionID;
    private EnumDifficulty difficulty;
    private WorldSettings.GameType gameType;
    private WorldType worldType;
    
    public S07PacketRespawn() {
    }
    
    public S07PacketRespawn(final int dimensionIDIn, final EnumDifficulty difficultyIn, final WorldType worldTypeIn, final WorldSettings.GameType gameTypeIn) {
        this.dimensionID = dimensionIDIn;
        this.difficulty = difficultyIn;
        this.gameType = gameTypeIn;
        this.worldType = worldTypeIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRespawn(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.dimensionID = buf.readInt();
        this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
        this.gameType = WorldSettings.GameType.getByID(buf.readUnsignedByte());
        this.worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.dimensionID);
        buf.writeByte(this.difficulty.getDifficultyId());
        buf.writeByte(this.gameType.getID());
        buf.writeString(this.worldType.getWorldTypeName());
    }
    
    public int getDimensionID() {
        return this.dimensionID;
    }
    
    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }
    
    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }
    
    public WorldType getWorldType() {
        return this.worldType;
    }
}
