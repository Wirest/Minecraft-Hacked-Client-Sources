/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S03PacketTimeUpdate
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private long totalWorldTime;
/*    */   private long worldTime;
/*    */   
/*    */   public S03PacketTimeUpdate() {}
/*    */   
/*    */   public S03PacketTimeUpdate(long totalWorldTimeIn, long totalTimeIn, boolean doDayLightCycle)
/*    */   {
/* 19 */     this.totalWorldTime = totalWorldTimeIn;
/* 20 */     this.worldTime = totalTimeIn;
/*    */     
/* 22 */     if (!doDayLightCycle)
/*    */     {
/* 24 */       this.worldTime = (-this.worldTime);
/*    */       
/* 26 */       if (this.worldTime == 0L)
/*    */       {
/* 28 */         this.worldTime = -1L;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.totalWorldTime = buf.readLong();
/* 39 */     this.worldTime = buf.readLong();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     buf.writeLong(this.totalWorldTime);
/* 48 */     buf.writeLong(this.worldTime);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 56 */     handler.handleTimeUpdate(this);
/*    */   }
/*    */   
/*    */   public long getTotalWorldTime()
/*    */   {
/* 61 */     return this.totalWorldTime;
/*    */   }
/*    */   
/*    */   public long getWorldTime()
/*    */   {
/* 66 */     return this.worldTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S03PacketTimeUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */