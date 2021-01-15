/*    */ package rip.jutting.polaris.module.player;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.gui.GuiChat;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.Event2D;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class InvMove extends Module
/*    */ {
/*    */   public InvMove()
/*    */   {
/* 16 */     super("InvMove", 0, Category.PLAYER);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender(Event2D event) {
/* 21 */     if ((mc.currentScreen != null) && (!(mc.currentScreen instanceof GuiChat))) {
/* 22 */       if (Keyboard.isKeyDown(200)) {
/* 23 */         pitch(mc.thePlayer.rotationPitch - 2.0F);
/*    */       }
/* 25 */       if (Keyboard.isKeyDown(208)) {
/* 26 */         pitch(mc.thePlayer.rotationPitch + 2.0F);
/*    */       }
/* 28 */       if (Keyboard.isKeyDown(203)) {
/* 29 */         yaw(mc.thePlayer.rotationYaw - 3.0F);
/*    */       }
/* 31 */       if (Keyboard.isKeyDown(205)) {
/* 32 */         yaw(mc.thePlayer.rotationYaw + 3.0F);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void pitch(float pitch) {
/* 38 */     Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
/*    */   }
/*    */   
/*    */   public static void yaw(float yaw) {
/* 42 */     Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\InvMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */