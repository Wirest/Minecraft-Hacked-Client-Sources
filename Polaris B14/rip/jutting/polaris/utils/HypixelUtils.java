/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HypixelUtils
/*    */ {
/*    */   public static boolean move;
/*    */   public static boolean hop;
/*    */   public static Double prevY;
/* 14 */   public static Minecraft mc = ;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void doLatestOnGroundSpeed()
/*    */   {
/* 22 */     if ((hop) && (mc.thePlayer.posY >= prevY.doubleValue() + 0.399994D)) {
/* 23 */       mc.thePlayer.motionY = -10000.0D;
/* 24 */       mc.thePlayer.posY = prevY.doubleValue();
/* 25 */       hop = false;
/*    */     }
/* 27 */     if ((mc.thePlayer.moveForward != 0.0F) && (!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isEating())) {
/* 28 */       if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
/* 29 */         mc.thePlayer.motionX = 0.0D;
/* 30 */         mc.thePlayer.motionZ = 0.0D;
/* 31 */         if (mc.thePlayer.isCollidedVertically) {
/* 32 */           mc.thePlayer.jump();
/* 33 */           move = true;
/*    */         }
/* 35 */         if ((move) && (mc.thePlayer.isCollidedVertically)) {
/* 36 */           move = false;
/*    */         }
/*    */       }
/* 39 */       if (mc.thePlayer.isCollidedVertically) {
/*    */         EntityPlayerSP thePlayer;
/*    */         EntityPlayerSP entityPlayerSP;
/* 42 */         EntityPlayerSP player = entityPlayerSP = thePlayer = mc.thePlayer;
/* 43 */         entityPlayerSP.motionX *= 0.5079D;
/*    */         EntityPlayerSP thePlayer2;
/*    */         EntityPlayerSP entityPlayerSP2;
/* 46 */         EntityPlayerSP player2 = entityPlayerSP2 = thePlayer2 = mc.thePlayer;
/* 47 */         entityPlayerSP2.motionZ *= 0.5079D;
/* 48 */         doMiniHop();
/*    */       }
/* 50 */       if ((hop) && (!move) && (mc.thePlayer.posY >= prevY.doubleValue() + 0.399994D)) {
/* 51 */         mc.thePlayer.motionY = -100.0D;
/* 52 */         mc.thePlayer.posY = prevY.doubleValue();
/* 53 */         hop = false;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void doMiniHop() {
/* 59 */     hop = true;
/* 60 */     prevY = Double.valueOf(mc.thePlayer.posY);
/* 61 */     mc.thePlayer.jump();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\HypixelUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */