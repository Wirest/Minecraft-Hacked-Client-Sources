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
    private static final String __OBFID = "CL_00002299";

    public S42PacketCombatEvent() {}

    public S42PacketCombatEvent(CombatTracker p_i45970_1_, S42PacketCombatEvent.Event p_i45970_2_)
    {
        this.field_179776_a = p_i45970_2_;
        EntityLivingBase var3 = p_i45970_1_.func_94550_c();

        switch (S42PacketCombatEvent.SwitchEvent.field_179944_a[p_i45970_2_.ordinal()])
        {
            case 1:
                this.field_179772_d = p_i45970_1_.func_180134_f();
                this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
                break;

            case 2:
                this.field_179774_b = p_i45970_1_.func_180135_h().getEntityId();
                this.field_179775_c = var3 == null ? -1 : var3.getEntityId();
                this.field_179773_e = p_i45970_1_.func_151521_b().getUnformattedText();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_179776_a = (S42PacketCombatEvent.Event)data.readEnumValue(S42PacketCombatEvent.Event.class);

        if (this.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT)
        {
            this.field_179772_d = data.readVarIntFromBuffer();
            this.field_179775_c = data.readInt();
        }
        else if (this.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED)
        {
            this.field_179774_b = data.readVarIntFromBuffer();
            this.field_179775_c = data.readInt();
            this.field_179773_e = data.readStringFromBuffer(32767);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeEnumValue(this.field_179776_a);

        if (this.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT)
        {
            data.writeVarIntToBuffer(this.field_179772_d);
            data.writeInt(this.field_179775_c);
        }
        else if (this.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED)
        {
            data.writeVarIntToBuffer(this.field_179774_b);
            data.writeInt(this.field_179775_c);
            data.writeString(this.field_179773_e);
        }
    }

    public void func_179771_a(INetHandlerPlayClient p_179771_1_)
    {
        p_179771_1_.func_175098_a(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_179771_a((INetHandlerPlayClient)handler);
    }

    public static enum Event
    {
        ENTER_COMBAT("ENTER_COMBAT", 0),
        END_COMBAT("END_COMBAT", 1),
        ENTITY_DIED("ENTITY_DIED", 2);

        private static final S42PacketCombatEvent.Event[] $VALUES = new S42PacketCombatEvent.Event[]{ENTER_COMBAT, END_COMBAT, ENTITY_DIED};
        private static final String __OBFID = "CL_00002297";

        private Event(String p_i45969_1_, int p_i45969_2_) {}
    }

    static final class SwitchEvent
    {
        static final int[] field_179944_a = new int[S42PacketCombatEvent.Event.values().length];
        private static final String __OBFID = "CL_00002298";

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
