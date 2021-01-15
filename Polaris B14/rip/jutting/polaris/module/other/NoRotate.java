/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*    */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*    */ 
/*    */ public class NoRotate extends rip.jutting.polaris.module.Module
/*    */ {
/*    */   public NoRotate()
/*    */   {
/* 11 */     super("NoRotate", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 16 */     if (((event.getPacket() instanceof S08PacketPlayerPosLook)) && 
/* 17 */       (mc.theWorld != null)) {
/* 18 */       S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
/* 19 */       packet.yaw = mc.thePlayer.rotationYaw;
/* 20 */       packet.pitch = mc.thePlayer.rotationPitch;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\NoRotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */