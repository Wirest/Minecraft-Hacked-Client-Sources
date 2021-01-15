/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ 
/*    */ 
/*    */ public class S3DPacketDisplayScoreboard
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int position;
/*    */   private String scoreName;
/*    */   
/*    */   public S3DPacketDisplayScoreboard() {}
/*    */   
/*    */   public S3DPacketDisplayScoreboard(int positionIn, ScoreObjective scoreIn)
/*    */   {
/* 20 */     this.position = positionIn;
/*    */     
/* 22 */     if (scoreIn == null)
/*    */     {
/* 24 */       this.scoreName = "";
/*    */     }
/*    */     else
/*    */     {
/* 28 */       this.scoreName = scoreIn.getName();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 37 */     this.position = buf.readByte();
/* 38 */     this.scoreName = buf.readStringFromBuffer(16);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 46 */     buf.writeByte(this.position);
/* 47 */     buf.writeString(this.scoreName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 55 */     handler.handleDisplayScoreboard(this);
/*    */   }
/*    */   
/*    */   public int func_149371_c()
/*    */   {
/* 60 */     return this.position;
/*    */   }
/*    */   
/*    */   public String func_149370_d()
/*    */   {
/* 65 */     return this.scoreName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S3DPacketDisplayScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */