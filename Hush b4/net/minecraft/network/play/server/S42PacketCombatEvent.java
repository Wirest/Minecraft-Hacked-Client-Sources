// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.CombatTracker;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S42PacketCombatEvent implements Packet<INetHandlerPlayClient>
{
    public Event eventType;
    public int field_179774_b;
    public int field_179775_c;
    public int field_179772_d;
    public String deathMessage;
    
    public S42PacketCombatEvent() {
    }
    
    public S42PacketCombatEvent(final CombatTracker combatTrackerIn, final Event combatEventType) {
        this.eventType = combatEventType;
        final EntityLivingBase entitylivingbase = combatTrackerIn.func_94550_c();
        switch (combatEventType) {
            case END_COMBAT: {
                this.field_179772_d = combatTrackerIn.func_180134_f();
                this.field_179775_c = ((entitylivingbase == null) ? -1 : entitylivingbase.getEntityId());
                break;
            }
            case ENTITY_DIED: {
                this.field_179774_b = combatTrackerIn.getFighter().getEntityId();
                this.field_179775_c = ((entitylivingbase == null) ? -1 : entitylivingbase.getEntityId());
                this.deathMessage = combatTrackerIn.getDeathMessage().getUnformattedText();
                break;
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.eventType = buf.readEnumValue(Event.class);
        if (this.eventType == Event.END_COMBAT) {
            this.field_179772_d = buf.readVarIntFromBuffer();
            this.field_179775_c = buf.readInt();
        }
        else if (this.eventType == Event.ENTITY_DIED) {
            this.field_179774_b = buf.readVarIntFromBuffer();
            this.field_179775_c = buf.readInt();
            this.deathMessage = buf.readStringFromBuffer(32767);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.eventType);
        if (this.eventType == Event.END_COMBAT) {
            buf.writeVarIntToBuffer(this.field_179772_d);
            buf.writeInt(this.field_179775_c);
        }
        else if (this.eventType == Event.ENTITY_DIED) {
            buf.writeVarIntToBuffer(this.field_179774_b);
            buf.writeInt(this.field_179775_c);
            buf.writeString(this.deathMessage);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCombatEvent(this);
    }
    
    public enum Event
    {
        ENTER_COMBAT("ENTER_COMBAT", 0), 
        END_COMBAT("END_COMBAT", 1), 
        ENTITY_DIED("ENTITY_DIED", 2);
        
        private Event(final String name, final int ordinal) {
        }
    }
}
