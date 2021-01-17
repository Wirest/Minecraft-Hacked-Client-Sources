// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0BPacketEntityAction implements Packet<INetHandlerPlayServer>
{
    private int entityID;
    private Action action;
    private int auxData;
    
    public C0BPacketEntityAction() {
    }
    
    public C0BPacketEntityAction(final Entity entity, final Action action) {
        this(entity, action, 0);
    }
    
    public C0BPacketEntityAction(final Entity entity, final Action action, final int auxData) {
        this.entityID = entity.getEntityId();
        this.action = action;
        this.auxData = auxData;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.action = buf.readEnumValue(Action.class);
        this.auxData = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.auxData);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processEntityAction(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public int getAuxData() {
        return this.auxData;
    }
    
    public enum Action
    {
        START_SNEAKING("START_SNEAKING", 0), 
        STOP_SNEAKING("STOP_SNEAKING", 1), 
        STOP_SLEEPING("STOP_SLEEPING", 2), 
        START_SPRINTING("START_SPRINTING", 3), 
        STOP_SPRINTING("STOP_SPRINTING", 4), 
        RIDING_JUMP("RIDING_JUMP", 5), 
        OPEN_INVENTORY("OPEN_INVENTORY", 6);
        
        private Action(final String name, final int ordinal) {
        }
    }
}
