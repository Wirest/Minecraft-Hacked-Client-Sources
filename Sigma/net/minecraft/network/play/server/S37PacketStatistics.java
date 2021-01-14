package net.minecraft.network.play.server;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class S37PacketStatistics implements Packet {
    private Map field_148976_a;
    private static final String __OBFID = "CL_00001283";

    public S37PacketStatistics() {
    }

    public S37PacketStatistics(Map p_i45173_1_) {
        this.field_148976_a = p_i45173_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleStatistics(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        int var2 = data.readVarIntFromBuffer();
        this.field_148976_a = Maps.newHashMap();

        for (int var3 = 0; var3 < var2; ++var3) {
            StatBase var4 = StatList.getOneShotStat(data.readStringFromBuffer(32767));
            int var5 = data.readVarIntFromBuffer();

            if (var4 != null) {
                this.field_148976_a.put(var4, Integer.valueOf(var5));
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_148976_a.size());
        Iterator var2 = this.field_148976_a.entrySet().iterator();

        while (var2.hasNext()) {
            Entry var3 = (Entry) var2.next();
            data.writeString(((StatBase) var3.getKey()).statId);
            data.writeVarIntToBuffer(((Integer) var3.getValue()).intValue());
        }
    }

    public Map func_148974_c() {
        return this.field_148976_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
