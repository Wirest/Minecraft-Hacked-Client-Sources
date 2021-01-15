/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.util.Vec3;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Teleport extends Module
/*    */ {
/*    */   public Teleport()
/*    */   {
/* 16 */     super("Teleport", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/* 19 */   private boolean tp = false;
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 23 */     MovingObjectPosition ray = rayTrace(500.0D);
/* 24 */     if (ray == null) {
/* 25 */       return;
/*    */     }
/* 27 */     if (org.lwjgl.input.Mouse.isButtonDown(1)) {
/* 28 */       double x_new = ray.getBlockPos().getX() + 0.5D;
/* 29 */       double y_new = ray.getBlockPos().getY() + 1;
/* 30 */       double z_new = ray.getBlockPos().getZ() + 0.5D;
/* 31 */       double distance = mc.thePlayer.getDistance(x_new, y_new, z_new); for (double d = 0.0D; d < distance; d += 2.0D) {
/* 32 */         setPos(mc.thePlayer.posX + (x_new - mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - mc.thePlayer.posX) * d / distance, mc.thePlayer.posY + (y_new - mc.thePlayer.posY) * d / distance, mc.thePlayer.posZ + (z_new - mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - mc.thePlayer.posZ) * d / distance);
/*    */       }
/* 34 */       setPos(x_new, y_new, z_new);
/* 35 */       mc.renderGlobal.loadRenderers();
/*    */     }
/*    */   }
/*    */   
/*    */   public MovingObjectPosition rayTrace(double blockReachDistance) {
/* 40 */     Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
/* 41 */     Vec3 vec4 = mc.thePlayer.getLookVec();
/* 42 */     Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
/* 43 */     return mc.theWorld.rayTraceBlocks(vec3, vec5, !mc.thePlayer.isInWater(), false, false);
/*    */   }
/*    */   
/*    */   public void setPos(double x, double y, double z) {
/* 47 */     mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
/* 48 */     mc.thePlayer.setPosition(x, y, z);
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 53 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Teleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */