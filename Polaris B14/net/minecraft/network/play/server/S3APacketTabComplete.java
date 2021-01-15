/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S3APacketTabComplete
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String[] matches;
/*    */   
/*    */   public S3APacketTabComplete() {}
/*    */   
/*    */   public S3APacketTabComplete(String[] matchesIn)
/*    */   {
/* 18 */     this.matches = matchesIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.matches = new String[buf.readVarIntFromBuffer()];
/*    */     
/* 28 */     for (int i = 0; i < this.matches.length; i++)
/*    */     {
/* 30 */       this.matches[i] = buf.readStringFromBuffer(32767);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     buf.writeVarIntToBuffer(this.matches.length);
/*    */     String[] arrayOfString;
/* 41 */     int j = (arrayOfString = this.matches).length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
/*    */       
/* 43 */       buf.writeString(s);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 52 */     handler.handleTabComplete(this);
/*    */   }
/*    */   
/*    */   public String[] func_149630_c()
/*    */   {
/* 57 */     return this.matches;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S3APacketTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */