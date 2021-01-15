/*     */ package rip.jutting.polaris.module.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ 
/*     */ public class Step extends Module
/*     */ {
/*     */   private boolean didSend;
/*     */   private int sendTicks;
/*     */   
/*     */   public Step()
/*     */   {
/*  17 */     super("Step", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setup()
/*     */   {
/*  25 */     ArrayList<String> options = new ArrayList();
/*  26 */     options.add("Vanilla");
/*  27 */     options.add("NCP");
/*  28 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("Step Mode", this, "Vanilla", options));
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  33 */     String mode = Polaris.instance.settingsManager.getSettingByName("Step Mode").getValString();
/*  34 */     if (mode.equalsIgnoreCase("Vanilla")) {
/*  35 */       setDisplayName("Step §7- Vanilla");
/*  36 */       mc.thePlayer.stepHeight = 3.0F;
/*     */     }
/*  38 */     if (mode.equalsIgnoreCase("NCP")) {
/*  39 */       setDisplayName("Step §7- NCP");
/*     */     }
/*  41 */     if (mode.equalsIgnoreCase("NCP")) {
/*  42 */       setDisplayName("Step §7- NCP");
/*  43 */       mc.thePlayer.stepHeight = 0.6F;
/*  44 */       if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically) && (mc.thePlayer.isCollided))
/*     */       {
/*  46 */         if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically) && (mc.thePlayer.isCollided))
/*     */         {
/*  48 */           mc.thePlayer.setSprinting(false);
/*  49 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/*  50 */             mc.thePlayer.posY + 0.419999284D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*  51 */           mc.thePlayer.setSprinting(false);
/*  52 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/*  53 */             mc.thePlayer.posY + 0.752999372D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*  54 */           mc.thePlayer.setSprinting(false);
/*  55 */           mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ);
/*  56 */           mc.thePlayer.setSprinting(false);
/*     */           
/*  58 */           mc.timer.timerSpeed = 0.5F;
/*  59 */           new Thread(new Runnable()
/*     */           {
/*     */             public void run() {
/*     */               try {
/*  63 */                 Thread.sleep(100L);
/*     */               }
/*     */               catch (InterruptedException localInterruptedException) {}
/*  66 */               Step.mc.timer.timerSpeed = 1.0F;
/*     */             }
/*     */           })
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */             .start();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canStep() {
/*  75 */     double xOff = 0.0D;
/*  76 */     double zOff = 0.0D;
/*  77 */     double multiplier = 1.0D;
/*  78 */     double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  79 */     double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  80 */     MovementInput movementInput = mc.thePlayer.movementInput;
/*  81 */     double n = MovementInput.moveForward * 1.0D * mx;
/*  82 */     MovementInput movementInput2 = mc.thePlayer.movementInput;
/*  83 */     xOff = n + MovementInput.moveStrafe * 1.0D * mz;
/*  84 */     MovementInput movementInput3 = mc.thePlayer.movementInput;
/*  85 */     double n2 = MovementInput.moveForward * 1.0D * mz;
/*  86 */     MovementInput movementInput4 = mc.thePlayer.movementInput;
/*  87 */     zOff = n2 - MovementInput.moveStrafe * 1.0D * mx;
/*  88 */     return Polaris.getBlock(mc.thePlayer.posX + xOff, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ + zOff) == net.minecraft.block.Block.getBlockById(0);
/*     */   }
/*     */   
/*     */   public void onEnable()
/*     */   {
/*  93 */     this.didSend = false;
/*  94 */     this.sendTicks = 0;
/*  95 */     super.onEnable();
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 100 */     mc.thePlayer.stepHeight = 0.5F;
/* 101 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */