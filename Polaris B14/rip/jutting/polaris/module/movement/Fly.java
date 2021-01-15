/*     */ package rip.jutting.polaris.module.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ 
/*     */ public class Fly extends Module
/*     */ {
/*     */   private double groundX;
/*     */   private double groundY;
/*     */   private double groundZ;
/*     */   private int counter;
/*     */   public float i;
/*     */   public float x;
/*     */   
/*     */   public Fly()
/*     */   {
/*  27 */     super("Fly", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   rip.jutting.polaris.utils.Timer timer = new rip.jutting.polaris.utils.Timer();
/*     */   private int delay;
/*     */   
/*     */   public void setup()
/*     */   {
/*  43 */     ArrayList<String> options = new ArrayList();
/*  44 */     options.add("Vanilla");
/*  45 */     options.add("Hypixel");
/*  46 */     options.add("HypixelFast");
/*  47 */     options.add("HypixelZoom");
/*  48 */     options.add("Velt");
/*  49 */     options.add("VeltFlat");
/*  50 */     options.add("PrevPos");
/*  51 */     options.add("Mineplex");
/*  52 */     options.add("Faithful");
/*  53 */     options.add("AGC");
/*  54 */     Polaris.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Vanilla", options));
/*  55 */     Polaris.instance.settingsManager.rSetting(new Setting("Fly Speed", this, 1.0D, 0.0D, 10.0D, false));
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onPre(rip.jutting.polaris.event.events.EventPreMotionUpdate event) {
/*  60 */     String mode = Polaris.instance.settingsManager.getSettingByName("Fly Mode").getValString();
/*  61 */     if (mode.equalsIgnoreCase("VeltFlat")) {
/*  62 */       if (mc.thePlayer.movementInput.jump) {
/*  63 */         mc.thePlayer.motionY = 1.0D;
/*  64 */       } else if (mc.thePlayer.movementInput.sneak) {
/*  65 */         mc.thePlayer.motionY = -1.0D;
/*     */       } else {
/*  67 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/*  69 */       if (mc.thePlayer.isMoving()) {
/*  70 */         mc.thePlayer.setSpeed(2.0F);
/*  71 */         if (mc.thePlayer.ticksExisted % 10 == 0) {
/*  72 */           rip.jutting.polaris.utils.Utils.breakAnticheats();
/*     */         }
/*     */       } else {
/*  75 */         mc.thePlayer.setSpeed(0.0F);
/*     */       }
/*     */     }
/*  78 */     if (mode.equalsIgnoreCase("Velt")) {
/*  79 */       if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
/*  80 */         event.y = (Minecraft.getMinecraft().thePlayer.motionY = 1.5D);
/*     */       }
/*  82 */       if (Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed) {
/*  83 */         event.y = (Minecraft.getMinecraft().thePlayer.motionY = -1.5D);
/*     */       }
/*  85 */       if ((mc.thePlayer.isMoving()) && (!Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) && 
/*  86 */         (!Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed))
/*     */       {
/*  88 */         if ((Minecraft.getMinecraft().thePlayer.motionY > -0.41D) && 
/*  89 */           (!Minecraft.getMinecraft().thePlayer.onGround)) {
/*  90 */           return;
/*     */         }
/*  92 */         for (int i = 0; i < 10; i++) {
/*  93 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/*  94 */             mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, true));
/*     */         }
/*  96 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(
/*  97 */           net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*  98 */         event.y = (Minecraft.getMinecraft().thePlayer.motionY = 0.455D);
/*  99 */         mc.thePlayer.setSpeed(Polaris.instance.settingsManager.getSettingByName("Fly Speed").getValDouble());
/*     */       }
/*     */     }
/* 102 */     if (mode.equalsIgnoreCase("Hypixel")) {
/* 103 */       if (mc.thePlayer.movementInput.jump) {
/* 104 */         mc.thePlayer.motionY = 0.5D;
/* 105 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 106 */         mc.thePlayer.motionY = -0.5D;
/*     */       } else {
/* 108 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/*     */       
/* 111 */       mc.timer.timerSpeed = 1.0F;
/* 112 */       if (mc.thePlayer.isMoving()) {
/* 113 */         mc.thePlayer.cameraYaw = 
/* 114 */           (0.090909086F * (float)Polaris.instance.settingsManager.getSettingByName("Bobbing").getValDouble());
/* 115 */         if (mc.thePlayer.isPotionActive(net.minecraft.potion.Potion.moveSpeed)) {
/* 116 */           mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() + 0.005D);
/*     */         }
/*     */       } else {
/* 119 */         mc.thePlayer.motionX = 0.0D;
/* 120 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */       
/* 123 */       if (mode.equalsIgnoreCase("HypixelFast")) {
/* 124 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.003D, mc.thePlayer.posZ);
/* 125 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.003D, mc.thePlayer.posZ);
/*     */       }
/* 127 */       if ((mc.thePlayer.ticksExisted % 3 == 0) && (!rip.jutting.polaris.utils.EntityUtils.func2(mc.thePlayer))) {
/* 128 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, mc.thePlayer.posZ);
/* 129 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ);
/*     */       }
/* 131 */       if ((mc.thePlayer.isMoving()) && (!mc.thePlayer.onGround)) {
/* 132 */         mc.timer.timerSpeed = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.9F : 1.3F);
/* 133 */         mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? 0.28F : 0.25F);
/*     */       } else {
/* 135 */         mc.thePlayer.motionX = 0.0D;
/* 136 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/* 138 */       rip.jutting.polaris.utils.AutoUtils.airwalk();
/*     */     }
/*     */     
/* 141 */     if (mode.equalsIgnoreCase("HypixelZoom")) {
/* 142 */       mc.thePlayer.cameraYaw = 
/* 143 */         (0.090909086F * (float)Polaris.instance.settingsManager.getSettingByName("Bobbing").getValDouble());
/* 144 */       if (mc.thePlayer.movementInput.jump) {
/* 145 */         mc.thePlayer.motionY = 0.5D;
/* 146 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 147 */         mc.thePlayer.motionY = -0.5D;
/*     */       } else {
/* 149 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/* 151 */       if ((mc.thePlayer.ticksExisted % 3 == 0) && (!rip.jutting.polaris.utils.EntityUtils.func2(mc.thePlayer))) {
/* 152 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, 
/* 153 */           mc.thePlayer.posZ);
/* 154 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, 
/* 155 */           mc.thePlayer.posZ);
/*     */       }
/* 157 */       if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 158 */         this.i += 0.01F;
/*     */       }
/* 160 */       if (this.i > 0.7F) {
/* 161 */         this.i = 0.7F;
/*     */       }
/* 163 */       if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 164 */         this.x += 0.1F;
/*     */       }
/* 166 */       if (this.x > 0.4F) {
/* 167 */         this.x = 0.0F;
/*     */       }
/* 169 */       if ((mc.thePlayer.isMoving()) && (!mc.thePlayer.onGround)) {
/* 170 */         if (this.i < 0.2D) {
/* 171 */           mc.timer.elapsedPartialTicks = 1.637F;
/*     */         } else {
/* 173 */           mc.timer.elapsedPartialTicks = 0.217F;
/*     */         }
/* 175 */         mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? 0.23F : 0.25F);
/*     */       } else {
/* 177 */         mc.thePlayer.motionX = 0.0D;
/* 178 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */     }
/*     */     
/* 182 */     if (mode.equalsIgnoreCase("Faithful")) {
/* 183 */       if (mc.thePlayer.movementInput.jump) {
/* 184 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0D, mc.thePlayer.posZ);
/* 185 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 186 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4.0D, mc.thePlayer.posZ);
/*     */       } else {
/* 188 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/* 190 */       if ((mc.thePlayer.isMoving()) && 
/* 191 */         (mc.thePlayer.moveForward != 0.0F)) {
/* 192 */         mc.thePlayer.setSpeed(5.0F);
/*     */       }
/*     */       
/* 195 */       mc.timer.timerSpeed = 0.33F;
/* 196 */       mc.thePlayer.onGround = true;
/* 197 */       rip.jutting.polaris.utils.AutoUtils.airwalk();
/*     */     }
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 203 */     String mode = Polaris.instance.settingsManager.getSettingByName("Fly Mode").getValString();
/*     */     
/*     */ 
/* 206 */     if (mode.equalsIgnoreCase("Velt")) {
/* 207 */       setDisplayName("Fly §7- Velt");
/*     */     }
/* 209 */     if (mode.equalsIgnoreCase("HypixelZoom")) {
/* 210 */       setDisplayName("Fly §7- HypixelZoom");
/*     */     }
/* 212 */     if (mode.equalsIgnoreCase("AGC")) {
/* 213 */       setDisplayName("Fly §7- AGC");
/* 214 */       if (mc.thePlayer.movementInput.jump) {
/* 215 */         if (mc.thePlayer.ticksExisted % 5 == 0) {
/* 216 */           mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 2.0D, mc.thePlayer.posZ);
/*     */         }
/* 218 */         if (mc.thePlayer.ticksExisted % 10 == 0) {
/* 219 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/* 220 */             mc.thePlayer.motionY = -0.3D, mc.thePlayer.posZ, false));
/*     */         }
/* 222 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 223 */         if (mc.thePlayer.ticksExisted % 5 == 0) {
/* 224 */           mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 2.0D, mc.thePlayer.posZ);
/*     */         }
/* 226 */         if (mc.thePlayer.ticksExisted % 10 == 0) {
/* 227 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/* 228 */             mc.thePlayer.motionY = -0.3D, mc.thePlayer.posZ, false));
/*     */         }
/*     */       }
/* 231 */       else if (mc.thePlayer.ticksExisted % 4 == 0) {
/* 232 */         mc.thePlayer.motionY = 0.05D;
/*     */       }
/*     */       
/* 235 */       if (mc.thePlayer.isMoving()) {
/* 236 */         mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 3 == 0 ? 5 : 0);
/*     */       }
/*     */     }
/* 239 */     if (mode.equalsIgnoreCase("Faithful")) {
/* 240 */       setDisplayName("Fly §7- Faithful");
/*     */     }
/*     */     
/* 243 */     if (mode.equalsIgnoreCase("HypixelFast")) {
/* 244 */       setDisplayName("Fly §7- HypixelFast");
/* 245 */       mc.thePlayer.cameraYaw = 
/* 246 */         (0.090909086F * (float)Polaris.instance.settingsManager.getSettingByName("Bobbing").getValDouble());
/* 247 */       if (mc.thePlayer.movementInput.jump) {
/* 248 */         mc.thePlayer.motionY = 0.5D;
/* 249 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 250 */         mc.thePlayer.motionY = -0.5D;
/*     */       } else {
/* 252 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/* 254 */       if ((mc.thePlayer.ticksExisted % 3 == 0) && (!rip.jutting.polaris.utils.EntityUtils.func2(mc.thePlayer))) {
/* 255 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, 
/* 256 */           mc.thePlayer.posZ);
/* 257 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, 
/* 258 */           mc.thePlayer.posZ);
/*     */       }
/* 260 */       if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 261 */         this.i += 0.01F;
/*     */       }
/* 263 */       if (this.i > 0.1F) {
/* 264 */         this.i = 0.0F;
/*     */       }
/* 266 */       if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 267 */         this.x += 0.1F;
/*     */       }
/* 269 */       if (this.x > 0.4F) {
/* 270 */         this.x = 0.0F;
/*     */       }
/* 272 */       if ((mc.thePlayer.isMoving()) && (!mc.thePlayer.onGround)) {
/* 273 */         mc.timer.elapsedPartialTicks = 
/*     */         
/* 275 */           ((mc.thePlayer.ticksExisted % 2 == 0 ? 0.23F : 0.15F + this.x) * (float)Polaris.instance.settingsManager.getSettingByName("Fly Speed").getValDouble());
/* 276 */         if (mc.thePlayer.isPotionActive(net.minecraft.potion.Potion.moveSpeed)) {
/* 277 */           mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? 0.25F + this.i : 0.3F);
/*     */         } else {
/* 279 */           mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? 0.23F + this.i : 0.25F);
/*     */         }
/*     */       } else {
/* 282 */         mc.thePlayer.motionX = 0.0D;
/* 283 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */     }
/*     */     
/* 287 */     if (mode.equalsIgnoreCase("Mineplex")) {
/* 288 */       setDisplayName("Fly §7- Mineplex");
/* 289 */       this.counter += 1;
/* 290 */       double x = mc.thePlayer.posX;
/* 291 */       double z = mc.thePlayer.posZ;
/* 292 */       double y = mc.thePlayer.posY;
/* 293 */       double[] dir = moveLooking(0.0F);
/* 294 */       double xDir = dir[0];
/* 295 */       double zDir = dir[1];
/* 296 */       mc.thePlayer.onGround = true;
/* 297 */       mc.thePlayer.motionY = 0.0D;
/* 298 */       if (mc.thePlayer.isMoving()) {
/* 299 */         if (this.counter == 1) {
/* 300 */           mc.thePlayer.motionX = (xDir * 0.8D);
/* 301 */           mc.thePlayer.motionZ = (zDir * 0.8D);
/* 302 */         } else if (this.counter == 5) {
/* 303 */           mc.thePlayer.motionX = (xDir * 0.75D);
/* 304 */           mc.thePlayer.motionZ = (zDir * 0.75D);
/* 305 */         } else if (this.counter == 10) {
/* 306 */           mc.thePlayer.motionX = (xDir * 0.5D);
/* 307 */           mc.thePlayer.motionZ = (zDir * 0.5D);
/* 308 */         } else if (this.counter == 15) {
/* 309 */           mc.thePlayer.motionX = (xDir * 0.25D);
/* 310 */           mc.thePlayer.motionZ = (zDir * 0.25D);
/*     */         }
/* 312 */         else if (this.counter >= 20) {
/* 313 */           mc.thePlayer.motionX = (xDir * 0.0D);
/* 314 */           mc.thePlayer.motionZ = (zDir * 0.0D);
/*     */         }
/*     */       }
/* 317 */       if (rip.jutting.polaris.utils.Timer.hasReached(535L)) {
/* 318 */         this.counter = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 325 */     if (mode.equalsIgnoreCase("Hypixel")) {
/* 326 */       setDisplayName("Fly §7- Hypixel");
/*     */ 
/*     */     }
/* 329 */     else if (mode.equalsIgnoreCase("Vanilla")) {
/* 330 */       setDisplayName("Fly §7- Vanilla");
/* 331 */       if (mc.thePlayer.movementInput.jump) {
/* 332 */         mc.thePlayer.motionY = 
/* 333 */           (Polaris.instance.settingsManager.getSettingByName("Fly Speed").getValDouble() / 2.0D);
/* 334 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 335 */         mc.thePlayer.motionY = 
/* 336 */           (-Polaris.instance.settingsManager.getSettingByName("Fly Speed").getValDouble() / 2.0D);
/*     */       } else {
/* 338 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/*     */       
/* 341 */       if (mc.thePlayer.isMoving()) {
/* 342 */         mc.thePlayer.setSpeed(
/* 343 */           (float)Polaris.instance.settingsManager.getSettingByName("Fly Speed").getValDouble());
/*     */       } else {
/* 345 */         mc.thePlayer.motionX = 0.0D;
/* 346 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/* 348 */     } else if (mode.equalsIgnoreCase("PrevPos")) {
/* 349 */       setDisplayName("Fly §7- PrevPos");
/* 350 */       if (mc.thePlayer.movementInput.jump) {
/* 351 */         mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ);
/* 352 */       } else if (mc.thePlayer.movementInput.sneak) {
/* 353 */         mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
/*     */       } else {
/* 355 */         mc.thePlayer.motionY = 0.0D;
/*     */       }
/*     */       
/* 358 */       if (mc.thePlayer.isMoving()) {
/* 359 */         if (mc.thePlayer.ticksExisted % 2 == 0) {
/* 360 */           mc.thePlayer.setSpeed(3.0F);
/*     */         } else {
/* 362 */           mc.thePlayer.setSpeed(0.0F);
/*     */         }
/* 364 */         if (mc.thePlayer.ticksExisted % 3 == 0) {
/* 365 */           mc.thePlayer.setPosition(mc.thePlayer.posX + 1.0D, mc.thePlayer.posY, mc.thePlayer.posZ + 1.0D);
/* 366 */           mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ);
/*     */         }
/* 368 */         if (mc.thePlayer.ticksExisted % 3 == 0) {
/* 369 */           mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double[] moveLooking(float yawOffset) {
/* 376 */     float dir = mc.thePlayer.rotationYaw + yawOffset;
/* 377 */     if (mc.thePlayer.moveForward < 0.0F) {
/* 378 */       dir += 180.0F;
/*     */     }
/* 380 */     if (mc.thePlayer.moveStrafing > 0.0F) {
/* 381 */       dir -= 90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F);
/*     */     }
/* 383 */     if (mc.thePlayer.moveStrafing < 0.0F) {
/* 384 */       dir += 90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F);
/*     */     }
/*     */     
/* 387 */     float xD = (float)Math.cos((dir + 90.0F) * 3.141592653589793D / 180.0D);
/* 388 */     float zD = (float)Math.sin((dir + 90.0F) * 3.141592653589793D / 180.0D);
/* 389 */     return new double[] { xD, zD };
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 394 */     String mode = Polaris.instance.settingsManager.getSettingByName("Fly Mode").getValString();
/* 395 */     if ((mode.equalsIgnoreCase("Vanilla")) && (mode.equalsIgnoreCase("AGC"))) {
/* 396 */       mc.thePlayer.motionX = 0.0D;
/* 397 */       mc.thePlayer.motionY = 0.0D;
/* 398 */       mc.thePlayer.motionZ = 0.0D;
/*     */     }
/* 400 */     this.i = 0.0F;
/* 401 */     this.x = 0.0F;
/* 402 */     mc.timer.timerSpeed = 1.0F;
/* 403 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Fly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */