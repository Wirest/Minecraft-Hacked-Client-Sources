/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*    */ import net.minecraft.scoreboard.IScoreObjectiveCriteria.EnumRenderType;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ 
/*    */ public class S3BPacketScoreboardObjective
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String objectiveName;
/*    */   private String objectiveValue;
/*    */   private IScoreObjectiveCriteria.EnumRenderType type;
/*    */   private int field_149342_c;
/*    */   
/*    */   public S3BPacketScoreboardObjective() {}
/*    */   
/*    */   public S3BPacketScoreboardObjective(ScoreObjective p_i45224_1_, int p_i45224_2_)
/*    */   {
/* 23 */     this.objectiveName = p_i45224_1_.getName();
/* 24 */     this.objectiveValue = p_i45224_1_.getDisplayName();
/* 25 */     this.type = p_i45224_1_.getCriteria().getRenderType();
/* 26 */     this.field_149342_c = p_i45224_2_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     this.objectiveName = buf.readStringFromBuffer(16);
/* 35 */     this.field_149342_c = buf.readByte();
/*    */     
/* 37 */     if ((this.field_149342_c == 0) || (this.field_149342_c == 2))
/*    */     {
/* 39 */       this.objectiveValue = buf.readStringFromBuffer(32);
/* 40 */       this.type = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(buf.readStringFromBuffer(16));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 49 */     buf.writeString(this.objectiveName);
/* 50 */     buf.writeByte(this.field_149342_c);
/*    */     
/* 52 */     if ((this.field_149342_c == 0) || (this.field_149342_c == 2))
/*    */     {
/* 54 */       buf.writeString(this.objectiveValue);
/* 55 */       buf.writeString(this.type.func_178796_a());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 64 */     handler.handleScoreboardObjective(this);
/*    */   }
/*    */   
/*    */   public String func_149339_c()
/*    */   {
/* 69 */     return this.objectiveName;
/*    */   }
/*    */   
/*    */   public String func_149337_d()
/*    */   {
/* 74 */     return this.objectiveValue;
/*    */   }
/*    */   
/*    */   public int func_149338_e()
/*    */   {
/* 79 */     return this.field_149342_c;
/*    */   }
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType func_179817_d()
/*    */   {
/* 84 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S3BPacketScoreboardObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */