// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Score;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S3CPacketUpdateScore implements Packet<INetHandlerPlayClient>
{
    private String name;
    private String objective;
    private int value;
    private Action action;
    
    public S3CPacketUpdateScore() {
        this.name = "";
        this.objective = "";
    }
    
    public S3CPacketUpdateScore(final Score scoreIn) {
        this.name = "";
        this.objective = "";
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = Action.CHANGE;
    }
    
    public S3CPacketUpdateScore(final String nameIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    public S3CPacketUpdateScore(final String nameIn, final ScoreObjective objectiveIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.name = buf.readStringFromBuffer(40);
        this.action = buf.readEnumValue(Action.class);
        this.objective = buf.readStringFromBuffer(16);
        if (this.action != Action.REMOVE) {
            this.value = buf.readVarIntFromBuffer();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.name);
        buf.writeEnumValue(this.action);
        buf.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            buf.writeVarIntToBuffer(this.value);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateScore(this);
    }
    
    public String getPlayerName() {
        return this.name;
    }
    
    public String getObjectiveName() {
        return this.objective;
    }
    
    public int getScoreValue() {
        return this.value;
    }
    
    public Action getScoreAction() {
        return this.action;
    }
    
    public enum Action
    {
        CHANGE("CHANGE", 0), 
        REMOVE("REMOVE", 1);
        
        private Action(final String name, final int ordinal) {
        }
    }
}
