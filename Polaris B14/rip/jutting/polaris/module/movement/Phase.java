/*     */ package rip.jutting.polaris.module.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.EventCollide;
/*     */ import rip.jutting.polaris.event.events.EventEntityCollision;
/*     */ import rip.jutting.polaris.event.events.EventPreMotionUpdate;
/*     */ import rip.jutting.polaris.event.events.EventPushOutOfBlock;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.PlayerUtils;
/*     */ 
/*     */ public class Phase extends Module
/*     */ {
/*     */   private int reset;
/*     */   
/*     */   public Phase()
/*     */   {
/*  31 */     super("Phase", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*     */   }
/*     */   
/*     */ 
/*  35 */   private double dist = 1.0D;
/*     */   private int resetNext;
/*     */   
/*     */   public void setup()
/*     */   {
/*  40 */     ArrayList<String> options = new ArrayList();
/*  41 */     options.add("Skip");
/*  42 */     options.add("Hypixel");
/*  43 */     options.add("Faithful");
/*  44 */     Polaris.instance.settingsManager.rSetting(new Setting("Phase Mode", this, "Skip", options));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  49 */     String mode = Polaris.instance.settingsManager.getSettingByName("Phase Mode").getValString();
/*  50 */     if (mode.equalsIgnoreCase("Faithful")) {
/*  51 */       setDisplayName("Phase §7- Faithful");
/*     */     }
/*  53 */     this.reset -= 1;
/*  54 */     double xOff = 0.0D;
/*  55 */     double zOff = 0.0D;
/*  56 */     double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  57 */     double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  58 */     if (mode.equalsIgnoreCase("Skip")) {
/*  59 */       setDisplayName("Phase §7- Skip");
/*  60 */       xOff = mc.thePlayer.moveForward * 2.6D * mx + mc.thePlayer.moveStrafing * 2.6D * mz;
/*  61 */       zOff = mc.thePlayer.moveForward * 2.6D * mz + mc.thePlayer.moveStrafing * 2.6D * mx;
/*  62 */       if ((PlayerUtils.isInsideBlock()) && (mc.thePlayer.isSneaking()))
/*  63 */         this.reset = 1;
/*  64 */       if (this.reset > 0) {
/*  65 */         mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0D, zOff);
/*     */       }
/*  67 */     } else if (mode.equalsIgnoreCase("Hypixel")) {
/*  68 */       setDisplayName("Phase §7- Hypixel");
/*  69 */       if (event.getState().equals(rip.jutting.polaris.event.Event.State.POST)) {
/*  70 */         double multiplier = 0.3D;
/*  71 */         double ux = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  72 */         double uz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/*  73 */         MovementInput movementInput = mc.thePlayer.movementInput;
/*  74 */         double n = MovementInput.moveForward * 0.3D * ux;
/*  75 */         MovementInput movementInput2 = mc.thePlayer.movementInput;
/*  76 */         double x = n + MovementInput.moveStrafe * 0.3D * uz;
/*  77 */         MovementInput movementInput3 = mc.thePlayer.movementInput;
/*  78 */         double n2 = MovementInput.moveForward * 0.3D * uz;
/*  79 */         MovementInput movementInput4 = mc.thePlayer.movementInput;
/*  80 */         double z = n2 - MovementInput.moveStrafe * 0.3D * ux;
/*  81 */         if ((mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isOnLadder()) && (!isInsideBlock())) {
/*  82 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
/*  83 */           for (int i = 1; i < 10; i++) {
/*  84 */             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 8.988465674311579E307D, mc.thePlayer.posZ, false));
/*     */           }
/*  86 */           mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onMove(EventPreMotionUpdate event) {
/*  94 */     String mode = Polaris.instance.settingsManager.getSettingByName("Phase Mode").getValString();
/*  95 */     if (mode.equalsIgnoreCase("Faithful")) {
/*  96 */       double xOffset = 0.0D;
/*  97 */       double zOffset = 0.0D;
/*  98 */       double multiplier = 1.05D;
/*  99 */       double multi = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/* 100 */       double mulit2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/* 101 */       xOffset = MovementInput.moveForward * multiplier * multi + MovementInput.moveStrafe * multiplier * mulit2;
/* 102 */       zOffset = MovementInput.moveForward * multiplier * mulit2 - MovementInput.moveStrafe * multiplier * multi;
/* 103 */       if (isInsideBlock()) {
/* 104 */         mc.thePlayer.motionY = 0.0D;
/*     */         
/* 106 */         this.resetNext += 1;
/* 107 */         if (this.resetNext > 2) {
/* 108 */           Polaris.breakAnticheats();
/* 109 */           this.resetNext = 0;
/* 110 */           event.setCancelled(true);
/*     */         }
/* 112 */         mc.thePlayer.boundingBox.offsetAndUpdate(xOffset, 0.0D, zOffset);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void pushOutOfBlock(EventPushOutOfBlock event) {
/* 119 */     String mode = Polaris.instance.settingsManager.getSettingByName("Phase Mode").getValString();
/* 120 */     if (mode.equalsIgnoreCase("Faithful")) {
/* 121 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onEntityCollision(EventEntityCollision event) {
/* 127 */     String mode = Polaris.instance.settingsManager.getSettingByName("Phase Mode").getValString();
/* 128 */     if ((mode.equalsIgnoreCase("Faithful")) && (
/* 129 */       ((isInsideBlock()) && (mc.gameSettings.keyBindJump.pressed)) || (
/* 130 */       (!isInsideBlock()) && (event.getBoundingBox() != null) && (event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY)))) {
/* 131 */       if (mc.thePlayer.isMoving()) {
/* 132 */         mc.thePlayer.setSpeed(0.25F);
/*     */       }
/* 134 */       event.setBoundingBox(null);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onCollision(EventCollide event)
/*     */   {
/* 141 */     String mode = Polaris.instance.settingsManager.getSettingByName("Phase Mode").getValString();
/* 142 */     if ((mode.equalsIgnoreCase("Skip")) && (
/* 143 */       ((PlayerUtils.isInsideBlock()) && (mc.gameSettings.keyBindJump.isKeyDown())) || ((!PlayerUtils.isInsideBlock()) && (event.getBoundingBox() != null) && (event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY) && (mc.thePlayer.isSneaking())))) {
/* 144 */       event.setBoundingBox(null);
/*     */     }
/* 146 */     if ((mode.equalsIgnoreCase("Hypixel")) && 
/* 147 */       (event.getBoundingBox() != null) && (event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY) && (org.lwjgl.input.Keyboard.isKeyDown(29))) {
/* 148 */       event.setBoundingBox(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInsideBlock()
/*     */   {
/* 154 */     for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
/* 155 */       for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
/* 156 */         for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
/* 157 */           net.minecraft.block.Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
/* 158 */           if ((block != null) && (!(block instanceof net.minecraft.block.BlockAir))) {
/* 159 */             AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
/* 160 */             if ((block instanceof net.minecraft.block.BlockHopper)) {
/* 161 */               boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
/*     */             }
/* 163 */             if ((boundingBox != null) && (mc.thePlayer.boundingBox.intersectsWith(boundingBox))) {
/* 164 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Phase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */