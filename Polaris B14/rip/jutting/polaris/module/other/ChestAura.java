/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import rip.jutting.polaris.event.Event.State;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.utils.BlockUtils;
/*    */ import rip.jutting.polaris.utils.Timerxd;
/*    */ 
/*    */ public class ChestAura extends Module
/*    */ {
/* 22 */   public static List openedBlocks = new CopyOnWriteArrayList();
/* 23 */   private Timerxd time = new Timerxd();
/* 24 */   private boolean shouldBreak = false;
/*    */   BlockPos globalPos;
/*    */   private boolean healingBot;
/*    */   
/*    */   public ChestAura() {
/* 29 */     super("ChestAura", 0, Category.PLAYER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 34 */     if (event.getState().equals(Event.State.PRE)) {
/* 35 */       if ((mc.currentScreen instanceof GuiContainer)) {
/* 36 */         this.time.updateLastTime();
/*    */       }
/*    */       
/* 39 */       int direction = 3;
/*    */       
/* 41 */       for (byte radius = 3; direction >= -radius; direction--) {
/* 42 */         for (int x = -radius; x <= radius; x++) {
/* 43 */           for (int z = -radius; z <= radius; z++) {
/* 44 */             BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5D + x, 
/* 45 */               mc.thePlayer.posY - 0.5D + direction, mc.thePlayer.posZ - 0.5D + z);
/* 46 */             Minecraft.getMinecraft();
/* 47 */             Block block = mc.theWorld.getBlockState(pos).getBlock();
/* 48 */             if ((BlockUtils.getFacingDirection(pos) != null) && (pos != null) && 
/* 49 */               (!(mc.currentScreen instanceof GuiContainer)))
/*    */             {
/*    */ 
/*    */ 
/* 53 */               if (mc.thePlayer.getDistance(mc.thePlayer.posX + x, mc.thePlayer.posY + direction, mc.thePlayer.posZ + z) < mc.playerController.getBlockReachDistance() - 
/* 54 */                 0.5D)
/* 55 */                 if ((block instanceof net.minecraft.block.BlockChest)) {
/* 56 */                   this.shouldBreak = true;
/* 57 */                   float[] rotations = BlockUtils.getBlockRotations(pos.getX(), pos.getY(), 
/* 58 */                     pos.getZ());
/* 59 */                   if (!this.healingBot) {
/* 60 */                     event.setYaw(rotations[0]);
/* 61 */                     event.setPitch(rotations[1]);
/*    */                   }
/*    */                   
/* 64 */                   this.globalPos = pos;
/* 65 */                   return;
/*    */                 } }
/*    */           }
/*    */         }
/*    */       }
/* 70 */     } else if ((event.getState().equals(Event.State.POST)) && (this.globalPos != null) && 
/* 71 */       (!(mc.currentScreen instanceof GuiContainer)) && (!openedBlocks.contains(this.globalPos)) && 
/* 72 */       (this.shouldBreak)) {
/* 73 */       mc.thePlayer.swingItem();
/* 74 */       net.minecraft.util.EnumFacing var9 = BlockUtils.getFacingDirection(this.globalPos);
/* 75 */       if ((var9 != null) && (this.time.hasPassed(400.0D))) {
/* 76 */         mc.playerController.processRightClickBlock(mc.thePlayer, mc.theWorld, 
/* 77 */           mc.thePlayer.getCurrentEquippedItem(), this.globalPos, var9, 
/* 78 */           new Vec3(this.globalPos.getX(), this.globalPos.getY(), 
/* 79 */           this.globalPos.getZ()));
/* 80 */         openedBlocks.add(this.globalPos);
/* 81 */         this.time.updateLastTime();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onDisable()
/*    */   {
/* 89 */     if (mc.theWorld != null) {
/* 90 */       openedBlocks.clear();
/*    */     }
/* 92 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\ChestAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */