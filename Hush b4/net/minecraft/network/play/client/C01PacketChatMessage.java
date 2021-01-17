// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C01PacketChatMessage implements Packet<INetHandlerPlayServer>
{
    private String message;
    
    public C01PacketChatMessage() {
    }
    
    public C01PacketChatMessage(String messageIn) {
        if (messageIn.length() > 100) {
            messageIn = messageIn.substring(0, 100);
        }
        this.message = messageIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.message = buf.readStringFromBuffer(100);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.message);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processChatMessage(this);
    }
    
    public String getMessage() {
        return this.message;
    }
}
