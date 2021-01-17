// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S02PacketChat implements Packet<INetHandlerPlayClient>
{
    private IChatComponent chatComponent;
    private byte type;
    
    public S02PacketChat() {
    }
    
    public S02PacketChat(final IChatComponent component) {
        this(component, (byte)1);
    }
    
    public S02PacketChat(final IChatComponent message, final byte typeIn) {
        this.chatComponent = message;
        this.type = typeIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chatComponent = buf.readChatComponent();
        this.type = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeChatComponent(this.chatComponent);
        buf.writeByte(this.type);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChat(this);
    }
    
    public IChatComponent getChatComponent() {
        return this.chatComponent;
    }
    
    public boolean isChat() {
        return this.type == 1 || this.type == 2;
    }
    
    public byte getType() {
        return this.type;
    }
}
