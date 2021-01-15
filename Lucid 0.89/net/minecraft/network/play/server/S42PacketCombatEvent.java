package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;

public class S42PacketCombatEvent implements Packet
{
    public S42PacketCombatEvent.Event field_179776_a;
    public int field_179774_b;
    public int field_179775_c;
    public int field_179772_d;
    public String field_179773_e;

    public S42PacketCombatEvent() {}

    public S42PacketCombatEvent(CombatTracker combatTrackerIn, S42PacketCombatEvent.Event combatEventType)
    {
        this.field_179776_a = combatEventType;
        EntityLivingBase var3 = combatTrackerIn.func_94550_c();

        switch (S42PacketCombatEvent.SwitchEvent.field_179944_a[combatEventType.ordinal()])
        {
            case 1:
                this.field_179772_d = combatTrackerIn.func_180134_f();
                this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
                break;

            case 2:
                this.field_179774_b = combatTrackerIn.getFighter().getEntityId();
                this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
                this.field_179773_e = combatTrackerIn.getDeathMessage().getUnformattedText();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179776_a = (S42PacketCombatEvent.Event)buf.readEnumValue(S42PacketCombatEvent.Event.class);

        if (this.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT)
        {
            this.field_179772_d = buf.readVarIntFromBuffer();
            this.field_179775_c = buf.readInt();
        }
        else if (this.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED)
        {
            this.field_179774_b = buf.readVarIntFromBuffer();
            this.field_179775_c = buf.readInt();
            this.field_179773_e = buf.readStringFromBuffer(32767);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.field_179776_a);

        if (this.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT)
        {
            buf.writeVarIntToBuffer(this.field_179772_d);
            buf.writeInt(this.field_179775_c);
        }
        else if (this.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED)
        {
            buf.writeVarIntToBuffer(this.field_179774_b);
            buf.writeInt(this.field_179775_c);
            buf.writeString(this.field_179773_e);
        }
    }

    public void func_179771_a(INetHandlerPlayClient handler)
    {
        handler.handleCombatEvent(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_179771_a((INetHandlerPlayClient)handler);
    }

    public static enum Event
    {
        ENTER_COMBAT("ENTER_COMBAT", 0),
        END_COMBAT("END_COMBAT", 1),
        ENTITY_DIED("ENTITY_DIED", 2); 

        private Event(String p_i45969_1_, int p_i45969_2_) {}
    }

    static final class SwitchEvent
    {
        static final int[] field_179944_a = new int[S42PacketCombatEvent.Event.values().length];

        static
        {
            try
            {
                field_179944_a[S42PacketCombatEvent.Event.END_COMBAT.ordinal()] = 1;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_179944_a[S42PacketCombatEvent.Event.ENTITY_DIED.ordinal()] = 2;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
