package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient>
{
    private boolean field_192605_a;
    private Map<ResourceLocation, Advancement.Builder> field_192606_b;
    private Set<ResourceLocation> field_192607_c;
    private Map<ResourceLocation, AdvancementProgress> field_192608_d;

    public SPacketAdvancementInfo()
    {
    }

    public SPacketAdvancementInfo(boolean p_i47519_1_, Collection<Advancement> p_i47519_2_, Set<ResourceLocation> p_i47519_3_, Map<ResourceLocation, AdvancementProgress> p_i47519_4_)
    {
        this.field_192605_a = p_i47519_1_;
        this.field_192606_b = Maps.<ResourceLocation, Advancement.Builder>newHashMap();

        for (Advancement advancement : p_i47519_2_)
        {
            this.field_192606_b.put(advancement.func_192067_g(), advancement.func_192075_a());
        }

        this.field_192607_c = p_i47519_3_;
        this.field_192608_d = Maps.<ResourceLocation, AdvancementProgress>newHashMap(p_i47519_4_);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_191981_a(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_192605_a = buf.readBoolean();
        this.field_192606_b = Maps.<ResourceLocation, Advancement.Builder>newHashMap();
        this.field_192607_c = Sets.<ResourceLocation>newLinkedHashSet();
        this.field_192608_d = Maps.<ResourceLocation, AdvancementProgress>newHashMap();
        int i = buf.readVarIntFromBuffer();

        for (int j = 0; j < i; ++j)
        {
            ResourceLocation resourcelocation = buf.func_192575_l();
            Advancement.Builder advancement$builder = Advancement.Builder.func_192060_b(buf);
            this.field_192606_b.put(resourcelocation, advancement$builder);
        }

        i = buf.readVarIntFromBuffer();

        for (int k = 0; k < i; ++k)
        {
            ResourceLocation resourcelocation1 = buf.func_192575_l();
            this.field_192607_c.add(resourcelocation1);
        }

        i = buf.readVarIntFromBuffer();

        for (int l = 0; l < i; ++l)
        {
            ResourceLocation resourcelocation2 = buf.func_192575_l();
            this.field_192608_d.put(resourcelocation2, AdvancementProgress.func_192100_b(buf));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(this.field_192605_a);
        buf.writeVarIntToBuffer(this.field_192606_b.size());

        for (Entry<ResourceLocation, Advancement.Builder> entry : this.field_192606_b.entrySet())
        {
            ResourceLocation resourcelocation = entry.getKey();
            Advancement.Builder advancement$builder = entry.getValue();
            buf.func_192572_a(resourcelocation);
            advancement$builder.func_192057_a(buf);
        }

        buf.writeVarIntToBuffer(this.field_192607_c.size());

        for (ResourceLocation resourcelocation1 : this.field_192607_c)
        {
            buf.func_192572_a(resourcelocation1);
        }

        buf.writeVarIntToBuffer(this.field_192608_d.size());

        for (Entry<ResourceLocation, AdvancementProgress> entry1 : this.field_192608_d.entrySet())
        {
            buf.func_192572_a(entry1.getKey());
            ((AdvancementProgress)entry1.getValue()).func_192104_a(buf);
        }
    }

    public Map<ResourceLocation, Advancement.Builder> func_192603_a()
    {
        return this.field_192606_b;
    }

    public Set<ResourceLocation> func_192600_b()
    {
        return this.field_192607_c;
    }

    public Map<ResourceLocation, AdvancementProgress> func_192604_c()
    {
        return this.field_192608_d;
    }

    public boolean func_192602_d()
    {
        return this.field_192605_a;
    }
}
