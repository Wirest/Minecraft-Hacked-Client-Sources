// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.DataWatcher;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S1CPacketEntityMetadata implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private List<DataWatcher.WatchableObject> field_149378_b;
    
    public S1CPacketEntityMetadata() {
    }
    
    public S1CPacketEntityMetadata(final int entityIdIn, final DataWatcher p_i45217_2_, final boolean p_i45217_3_) {
        this.entityId = entityIdIn;
        if (p_i45217_3_) {
            this.field_149378_b = p_i45217_2_.getAllWatched();
        }
        else {
            this.field_149378_b = p_i45217_2_.getChanged();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(buf);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, buf);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityMetadata(this);
    }
    
    public List<DataWatcher.WatchableObject> func_149376_c() {
        return this.field_149378_b;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
