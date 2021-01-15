/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CBSpoof
/*    */   extends Module
/*    */ {
/*    */   public CBSpoof()
/*    */   {
/* 20 */     super("CBSpoof", 0, Category.OTHER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 25 */     Polaris.sendMessage("Sending C17PacketCustomPayload packet.");
/* 26 */     mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("CB|INIT", new PacketBuffer(Unpooled.buffer()).writeString("CB|INIT")));
/* 27 */     mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("CB-Binary", new PacketBuffer(Unpooled.buffer()).writeString("CB-Binary")));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\CBSpoof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */