/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.stats.StatBase;
/*    */ import net.minecraft.stats.StatList;
/*    */ 
/*    */ 
/*    */ public class S37PacketStatistics
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private Map<StatBase, Integer> field_148976_a;
/*    */   
/*    */   public S37PacketStatistics() {}
/*    */   
/*    */   public S37PacketStatistics(Map<StatBase, Integer> p_i45173_1_)
/*    */   {
/* 23 */     this.field_148976_a = p_i45173_1_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 31 */     handler.handleStatistics(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     int i = buf.readVarIntFromBuffer();
/* 40 */     this.field_148976_a = Maps.newHashMap();
/*    */     
/* 42 */     for (int j = 0; j < i; j++)
/*    */     {
/* 44 */       StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(32767));
/* 45 */       int k = buf.readVarIntFromBuffer();
/*    */       
/* 47 */       if (statbase != null)
/*    */       {
/* 49 */         this.field_148976_a.put(statbase, Integer.valueOf(k));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 59 */     buf.writeVarIntToBuffer(this.field_148976_a.size());
/*    */     
/* 61 */     for (Map.Entry<StatBase, Integer> entry : this.field_148976_a.entrySet())
/*    */     {
/* 63 */       buf.writeString(((StatBase)entry.getKey()).statId);
/* 64 */       buf.writeVarIntToBuffer(((Integer)entry.getValue()).intValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public Map<StatBase, Integer> func_148974_c()
/*    */   {
/* 70 */     return this.field_148976_a;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S37PacketStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */