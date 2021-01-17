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

public class S2DPacketOpenWindow implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private String inventoryType;
    private IChatComponent windowTitle;
    private int slotCount;
    private int entityId;
    
    public S2DPacketOpenWindow() {
    }
    
    public S2DPacketOpenWindow(final int incomingWindowId, final String incomingWindowTitle, final IChatComponent windowTitleIn) {
        this(incomingWindowId, incomingWindowTitle, windowTitleIn, 0);
    }
    
    public S2DPacketOpenWindow(final int windowIdIn, final String guiId, final IChatComponent windowTitleIn, final int slotCountIn) {
        this.windowId = windowIdIn;
        this.inventoryType = guiId;
        this.windowTitle = windowTitleIn;
        this.slotCount = slotCountIn;
    }
    
    public S2DPacketOpenWindow(final int windowIdIn, final String guiId, final IChatComponent windowTitleIn, final int slotCountIn, final int incomingEntityId) {
        this(windowIdIn, guiId, windowTitleIn, slotCountIn);
        this.entityId = incomingEntityId;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleOpenWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.inventoryType = buf.readStringFromBuffer(32);
        this.windowTitle = buf.readChatComponent();
        this.slotCount = buf.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = buf.readInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeString(this.inventoryType);
        buf.writeChatComponent(this.windowTitle);
        buf.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            buf.writeInt(this.entityId);
        }
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public String getGuiId() {
        return this.inventoryType;
    }
    
    public IChatComponent getWindowTitle() {
        return this.windowTitle;
    }
    
    public int getSlotCount() {
        return this.slotCount;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public boolean hasSlots() {
        return this.slotCount > 0;
    }
}
