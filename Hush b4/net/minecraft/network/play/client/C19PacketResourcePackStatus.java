// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C19PacketResourcePackStatus implements Packet<INetHandlerPlayServer>
{
    private String hash;
    private Action status;
    
    public C19PacketResourcePackStatus() {
    }
    
    public C19PacketResourcePackStatus(String hashIn, final Action statusIn) {
        if (hashIn.length() > 40) {
            hashIn = hashIn.substring(0, 40);
        }
        this.hash = hashIn;
        this.status = statusIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.hash = buf.readStringFromBuffer(40);
        this.status = buf.readEnumValue(Action.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.hash);
        buf.writeEnumValue(this.status);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleResourcePackStatus(this);
    }
    
    public enum Action
    {
        SUCCESSFULLY_LOADED("SUCCESSFULLY_LOADED", 0), 
        DECLINED("DECLINED", 1), 
        FAILED_DOWNLOAD("FAILED_DOWNLOAD", 2), 
        ACCEPTED("ACCEPTED", 3);
        
        private Action(final String name, final int ordinal) {
        }
    }
}
