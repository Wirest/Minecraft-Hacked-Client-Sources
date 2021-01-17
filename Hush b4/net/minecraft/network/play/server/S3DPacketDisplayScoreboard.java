// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S3DPacketDisplayScoreboard implements Packet<INetHandlerPlayClient>
{
    private int position;
    private String scoreName;
    
    public S3DPacketDisplayScoreboard() {
    }
    
    public S3DPacketDisplayScoreboard(final int positionIn, final ScoreObjective scoreIn) {
        this.position = positionIn;
        if (scoreIn == null) {
            this.scoreName = "";
        }
        else {
            this.scoreName = scoreIn.getName();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.position = buf.readByte();
        this.scoreName = buf.readStringFromBuffer(16);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.position);
        buf.writeString(this.scoreName);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDisplayScoreboard(this);
    }
    
    public int func_149371_c() {
        return this.position;
    }
    
    public String func_149370_d() {
        return this.scoreName;
    }
}
