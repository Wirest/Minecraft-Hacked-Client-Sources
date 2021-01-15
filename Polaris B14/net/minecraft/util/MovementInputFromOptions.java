/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiChat;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class MovementInputFromOptions extends MovementInput
/*    */ {
/*    */   private final GameSettings gameSettings;
/*    */   
/*    */   public MovementInputFromOptions(GameSettings gameSettingsIn)
/*    */   {
/* 17 */     this.gameSettings = gameSettingsIn;
/*    */   }
/*    */   
/*    */   public void updatePlayerMoveState()
/*    */   {
/* 22 */     if ((Polaris.instance.moduleManager.getModuleByName("InvMove").isToggled()) && (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)))
/*    */     {
/* 24 */       moveStrafe = 0.0F;
/* 25 */       moveForward = 0.0F;
/* 26 */       if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
/* 27 */         moveForward += 1.0F;
/*    */       }
/* 29 */       if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
/* 30 */         moveForward -= 1.0F;
/*    */       }
/* 32 */       if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
/* 33 */         moveStrafe += 1.0F;
/*    */       }
/* 35 */       if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
/* 36 */         moveStrafe -= 1.0F;
/*    */       }
/* 38 */       this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
/* 39 */       this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
/* 40 */       if (this.sneak)
/*    */       {
/* 42 */         moveStrafe = (float)(moveStrafe * 0.3D);
/* 43 */         moveForward = (float)(moveForward * 0.3D);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 48 */       moveStrafe = 0.0F;
/* 49 */       moveForward = 0.0F;
/* 50 */       if (this.gameSettings.keyBindForward.isKeyDown()) {
/* 51 */         moveForward += 1.0F;
/*    */       }
/* 53 */       if (this.gameSettings.keyBindBack.isKeyDown()) {
/* 54 */         moveForward -= 1.0F;
/*    */       }
/* 56 */       if (this.gameSettings.keyBindLeft.isKeyDown()) {
/* 57 */         moveStrafe += 1.0F;
/*    */       }
/* 59 */       if (this.gameSettings.keyBindRight.isKeyDown()) {
/* 60 */         moveStrafe -= 1.0F;
/*    */       }
/* 62 */       this.jump = this.gameSettings.keyBindJump.isKeyDown();
/* 63 */       this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
/* 64 */       if (this.sneak)
/*    */       {
/* 66 */         moveStrafe = (float)(moveStrafe * 0.3D);
/* 67 */         moveForward = (float)(moveForward * 0.3D);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MovementInputFromOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */