// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C15PacketClientSettings implements Packet<INetHandlerPlayServer>
{
    private String lang;
    private int view;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean enableColors;
    private int modelPartFlags;
    
    public C15PacketClientSettings() {
    }
    
    public C15PacketClientSettings(final String langIn, final int viewIn, final EntityPlayer.EnumChatVisibility chatVisibilityIn, final boolean enableColorsIn, final int modelPartFlagsIn) {
        this.lang = langIn;
        this.view = viewIn;
        this.chatVisibility = chatVisibilityIn;
        this.enableColors = enableColorsIn;
        this.modelPartFlags = modelPartFlagsIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.lang = buf.readStringFromBuffer(7);
        this.view = buf.readByte();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(buf.readByte());
        this.enableColors = buf.readBoolean();
        this.modelPartFlags = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.lang);
        buf.writeByte(this.view);
        buf.writeByte(this.chatVisibility.getChatVisibility());
        buf.writeBoolean(this.enableColors);
        buf.writeByte(this.modelPartFlags);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processClientSettings(this);
    }
    
    public String getLang() {
        return this.lang;
    }
    
    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }
    
    public boolean isColorsEnabled() {
        return this.enableColors;
    }
    
    public int getModelPartFlags() {
        return this.modelPartFlags;
    }
}
