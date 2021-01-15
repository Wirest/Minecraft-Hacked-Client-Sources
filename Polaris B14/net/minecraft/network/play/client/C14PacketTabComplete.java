/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ public class C14PacketTabComplete
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private String message;
/*    */   private BlockPos targetBlock;
/*    */   
/*    */   public C14PacketTabComplete() {}
/*    */   
/*    */   public C14PacketTabComplete(String msg)
/*    */   {
/* 21 */     this(msg, null);
/*    */   }
/*    */   
/*    */   public C14PacketTabComplete(String msg, BlockPos target)
/*    */   {
/* 26 */     this.message = msg;
/* 27 */     this.targetBlock = target;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 35 */     this.message = buf.readStringFromBuffer(32767);
/* 36 */     boolean flag = buf.readBoolean();
/*    */     
/* 38 */     if (flag)
/*    */     {
/* 40 */       this.targetBlock = buf.readBlockPos();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 49 */     buf.writeString(StringUtils.substring(this.message, 0, 32767));
/* 50 */     boolean flag = this.targetBlock != null;
/* 51 */     buf.writeBoolean(flag);
/*    */     
/* 53 */     if (flag)
/*    */     {
/* 55 */       buf.writeBlockPos(this.targetBlock);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 64 */     handler.processTabComplete(this);
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 69 */     return this.message;
/*    */   }
/*    */   
/*    */   public BlockPos getTargetBlock()
/*    */   {
/* 74 */     return this.targetBlock;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C14PacketTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */