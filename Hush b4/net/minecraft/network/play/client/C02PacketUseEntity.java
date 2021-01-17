// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C02PacketUseEntity implements Packet<INetHandlerPlayServer>
{
    private int entityId;
    private Action action;
    private Vec3 hitVec;
    
    public C02PacketUseEntity() {
    }
    
    public C02PacketUseEntity(final Entity entity, final Action action) {
        this.entityId = entity.getEntityId();
        this.action = action;
    }
    
    public C02PacketUseEntity(final Entity entity, final Vec3 hitVec) {
        this(entity, Action.INTERACT_AT);
        this.hitVec = hitVec;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.action = buf.readEnumValue(Action.class);
        if (this.action == Action.INTERACT_AT) {
            this.hitVec = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeEnumValue(this.action);
        if (this.action == Action.INTERACT_AT) {
            buf.writeFloat((float)this.hitVec.xCoord);
            buf.writeFloat((float)this.hitVec.yCoord);
            buf.writeFloat((float)this.hitVec.zCoord);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processUseEntity(this);
    }
    
    public Entity getEntityFromWorld(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public Vec3 getHitVec() {
        return this.hitVec;
    }
    
    public enum Action
    {
        INTERACT("INTERACT", 0), 
        ATTACK("ATTACK", 1), 
        INTERACT_AT("INTERACT_AT", 2);
        
        private Action(final String name, final int ordinal) {
        }
    }
}
