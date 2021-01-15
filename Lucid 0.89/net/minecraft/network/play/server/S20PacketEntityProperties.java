package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S20PacketEntityProperties implements Packet
{
    private int field_149445_a;
    private final List field_149444_b = Lists.newArrayList();

    public S20PacketEntityProperties() {}

    public S20PacketEntityProperties(int p_i45236_1_, Collection p_i45236_2_)
    {
        this.field_149445_a = p_i45236_1_;
        Iterator var3 = p_i45236_2_.iterator();

        while (var3.hasNext())
        {
            IAttributeInstance var4 = (IAttributeInstance)var3.next();
            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(var4.getAttribute().getAttributeUnlocalizedName(), var4.getBaseValue(), var4.func_111122_c()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149445_a = buf.readVarIntFromBuffer();
        int var2 = buf.readInt();

        for (int var3 = 0; var3 < var2; ++var3)
        {
            String var4 = buf.readStringFromBuffer(64);
            double var5 = buf.readDouble();
            ArrayList var7 = Lists.newArrayList();
            int var8 = buf.readVarIntFromBuffer();

            for (int var9 = 0; var9 < var8; ++var9)
            {
                UUID var10 = buf.readUuid();
                var7.add(new AttributeModifier(var10, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
            }

            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(var4, var5, var7));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149445_a);
        buf.writeInt(this.field_149444_b.size());
        Iterator var2 = this.field_149444_b.iterator();

        while (var2.hasNext())
        {
            S20PacketEntityProperties.Snapshot var3 = (S20PacketEntityProperties.Snapshot)var2.next();
            buf.writeString(var3.func_151409_a());
            buf.writeDouble(var3.func_151410_b());
            buf.writeVarIntToBuffer(var3.func_151408_c().size());
            Iterator var4 = var3.func_151408_c().iterator();

            while (var4.hasNext())
            {
                AttributeModifier var5 = (AttributeModifier)var4.next();
                buf.writeUuid(var5.getID());
                buf.writeDouble(var5.getAmount());
                buf.writeByte(var5.getOperation());
            }
        }
    }

    public void func_180754_a(INetHandlerPlayClient handler)
    {
        handler.handleEntityProperties(this);
    }

    public int func_149442_c()
    {
        return this.field_149445_a;
    }

    public List func_149441_d()
    {
        return this.field_149444_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180754_a((INetHandlerPlayClient)handler);
    }

    public class Snapshot
    {
        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection field_151411_d;

        public Snapshot(String p_i45235_2_, double p_i45235_3_, Collection p_i45235_5_)
        {
            this.field_151412_b = p_i45235_2_;
            this.field_151413_c = p_i45235_3_;
            this.field_151411_d = p_i45235_5_;
        }

        public String func_151409_a()
        {
            return this.field_151412_b;
        }

        public double func_151410_b()
        {
            return this.field_151413_c;
        }

        public Collection func_151408_c()
        {
            return this.field_151411_d;
        }
    }
}
