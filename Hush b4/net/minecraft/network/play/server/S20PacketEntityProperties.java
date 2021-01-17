// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S20PacketEntityProperties implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private final List<Snapshot> field_149444_b;
    
    public S20PacketEntityProperties() {
        this.field_149444_b = (List<Snapshot>)Lists.newArrayList();
    }
    
    public S20PacketEntityProperties(final int entityIdIn, final Collection<IAttributeInstance> p_i45236_2_) {
        this.field_149444_b = (List<Snapshot>)Lists.newArrayList();
        this.entityId = entityIdIn;
        for (final IAttributeInstance iattributeinstance : p_i45236_2_) {
            this.field_149444_b.add(new Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.func_111122_c()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        for (int i = buf.readInt(), j = 0; j < i; ++j) {
            final String s = buf.readStringFromBuffer(64);
            final double d0 = buf.readDouble();
            final List<AttributeModifier> list = (List<AttributeModifier>)Lists.newArrayList();
            for (int k = buf.readVarIntFromBuffer(), l = 0; l < k; ++l) {
                final UUID uuid = buf.readUuid();
                list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
            }
            this.field_149444_b.add(new Snapshot(s, d0, list));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeInt(this.field_149444_b.size());
        for (final Snapshot s20packetentityproperties$snapshot : this.field_149444_b) {
            buf.writeString(s20packetentityproperties$snapshot.func_151409_a());
            buf.writeDouble(s20packetentityproperties$snapshot.func_151410_b());
            buf.writeVarIntToBuffer(s20packetentityproperties$snapshot.func_151408_c().size());
            for (final AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
                buf.writeUuid(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityProperties(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public List<Snapshot> func_149441_d() {
        return this.field_149444_b;
    }
    
    public class Snapshot
    {
        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection<AttributeModifier> field_151411_d;
        
        public Snapshot(final String p_i45235_2_, final double p_i45235_3_, final Collection<AttributeModifier> p_i45235_5_) {
            this.field_151412_b = p_i45235_2_;
            this.field_151413_c = p_i45235_3_;
            this.field_151411_d = p_i45235_5_;
        }
        
        public String func_151409_a() {
            return this.field_151412_b;
        }
        
        public double func_151410_b() {
            return this.field_151413_c;
        }
        
        public Collection<AttributeModifier> func_151408_c() {
            return this.field_151411_d;
        }
    }
}
