/*     */ package rip.jutting.polaris.module.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ 
/*     */ public class LongJump extends Module
/*     */ {
/*     */   public LongJump()
/*     */   {
/*  18 */     super("LongJump", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  23 */     ArrayList<String> options = new ArrayList();
/*  24 */     options.add("Hypixel");
/*  25 */     options.add("Velt");
/*  26 */     options.add("Faithful");
/*  27 */     Polaris.instance.settingsManager.rSetting(new Setting("LongJump Mode", this, "Hypixel", options));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  32 */     String mode = Polaris.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
/*  33 */     if (mode.equalsIgnoreCase("Hypixel")) {
/*  34 */       setDisplayName("LongJump §7- Hypixel");
/*     */     }
/*  36 */     if (mode.equalsIgnoreCase("Velt")) {
/*  37 */       setDisplayName("LongJump §7- Velt");
/*     */     }
/*  39 */     if (mode.equalsIgnoreCase("Faithful")) {
/*  40 */       setDisplayName("LongJump §7- Faithful");
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPre(rip.jutting.polaris.event.events.EventPreMotionUpdate event) {
/*  46 */     String mode = Polaris.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
/*  47 */     if (mode.equalsIgnoreCase("Velt")) {
/*  48 */       if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
/*  49 */         if (mc.thePlayer.onGround) {
/*  50 */           for (int i = 0; i < 20; i++) {
/*  51 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*     */           }
/*  53 */           event.y = (mc.thePlayer.motionY = 0.4D);
/*  54 */           mc.thePlayer.setSpeed(9.0F);
/*     */         }
/*     */       } else {
/*  57 */         mc.thePlayer.motionX = 0.0D;
/*  58 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */     }
/*  61 */     if (mode.equalsIgnoreCase("Faithful")) {
/*  62 */       if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
/*  63 */         mc.timer.timerSpeed = 0.33F;
/*  64 */         if (mc.thePlayer.onGround) {
/*  65 */           event.y = (mc.thePlayer.motionY = 0.6D);
/*  66 */           mc.thePlayer.setSpeed(7.0F);
/*     */         } else {
/*  68 */           mc.thePlayer.setSpeed(7.0F);
/*     */         }
/*     */       } else {
/*  71 */         mc.thePlayer.motionX = 0.0D;
/*  72 */         mc.thePlayer.motionZ = 0.0D;
/*  73 */         mc.timer.timerSpeed = 1.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPost(rip.jutting.polaris.event.events.EventPostMotionUpdate event) {
/*  80 */     String mode = Polaris.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
/*  81 */     if (mode.equalsIgnoreCase("Hypixel")) {
/*  82 */       mc.timer.timerSpeed = 1.0F;
/*  83 */       if ((mc.gameSettings.keyBindForward.isKeyDown()) || (mc.gameSettings.keyBindLeft.isKeyDown()) || (mc.gameSettings.keyBindRight.isKeyDown()) || (mc.gameSettings.keyBindBack.isKeyDown())) {
/*  84 */         float dir = mc.thePlayer.rotationYaw + (mc.thePlayer.moveForward < 0.0F ? 180 : 0) + (mc.thePlayer.moveStrafing > 0.0F ? -90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.4F : mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
/*  85 */         float xDir = (float)Math.cos((dir + 90.0F) * 3.141592653589793D / 180.0D);
/*  86 */         float zDir = (float)Math.sin((dir + 90.0F) * 3.141592653589793D / 180.0D);
/*  87 */         if ((mc.thePlayer.isCollidedVertically) && ((mc.gameSettings.keyBindForward.isKeyDown()) || (mc.gameSettings.keyBindLeft.isKeyDown()) || (mc.gameSettings.keyBindRight.isKeyDown()) || (mc.gameSettings.keyBindBack.isKeyDown()))) {
/*  88 */           mc.thePlayer.jump();
/*  89 */           mc.thePlayer.motionX = (xDir * 0.29F);
/*  90 */           mc.thePlayer.motionZ = (zDir * 0.29F);
/*     */         }
/*  92 */         mc.timer.timerSpeed = 1.0F;
/*  93 */         if ((mc.thePlayer.motionY == 0.33319999363422365D) && ((mc.gameSettings.keyBindForward.isKeyDown()) || (mc.gameSettings.keyBindLeft.isKeyDown()) || (mc.gameSettings.keyBindRight.isKeyDown()) || (mc.gameSettings.keyBindBack.isKeyDown()))) {
/*  94 */           if (mc.thePlayer.isPotionActive(net.minecraft.potion.Potion.moveSpeed)) {
/*  95 */             mc.thePlayer.motionX = (xDir * 1.34D);
/*  96 */             mc.thePlayer.motionZ = (zDir * 1.34D);
/*     */           } else {
/*  98 */             mc.thePlayer.motionX = (xDir * 1.261D);
/*  99 */             mc.thePlayer.motionZ = (zDir * 1.261D);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 108 */     String mode = Polaris.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
/* 109 */     if (mode.equalsIgnoreCase("Faithful")) {
/* 110 */       mc.thePlayer.motionX = 0.0D;
/* 111 */       mc.thePlayer.motionZ = 0.0D;
/*     */     }
/* 113 */     mc.timer.timerSpeed = 1.0F;
/* 114 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\LongJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */