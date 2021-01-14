package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class S37PacketStatistics implements Packet {
   private Map field_148976_a;

   public S37PacketStatistics() {
   }

   public S37PacketStatistics(Map p_i45173_1_) {
      this.field_148976_a = p_i45173_1_;
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleStatistics(this);
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      int i = buf.readVarIntFromBuffer();
      this.field_148976_a = Maps.newHashMap();

      for(int j = 0; j < i; ++j) {
         StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(32767));
         int k = buf.readVarIntFromBuffer();
         if (statbase != null) {
            this.field_148976_a.put(statbase, k);
         }
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.field_148976_a.size());
      Iterator var2 = this.field_148976_a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         buf.writeString(((StatBase)entry.getKey()).statId);
         buf.writeVarIntToBuffer((Integer)entry.getValue());
      }

   }

   public Map func_148974_c() {
      return this.field_148976_a;
   }
}
