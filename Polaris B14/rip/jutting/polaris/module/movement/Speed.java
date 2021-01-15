/*     */ package rip.jutting.polaris.module.movement;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockAir;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.EventPreMotionUpdate;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.SlitUtils;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ public class Speed extends Module
/*     */ {
/*     */   public Speed()
/*     */   {
/*  30 */     super("Speed", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*     */   }
/*     */   
/*  33 */   int delay = 0;
/*  34 */   private boolean boost = false;
/*     */   public static int stage;
/*     */   public static double moveSpeed;
/*     */   public static double lastDist;
/*  38 */   public static boolean speed = true;
/*     */   
/*     */   private int setback;
/*     */   private int timeState;
/*     */   public int state;
/*     */   private int cooldownHops;
/*     */   private float i;
/*  45 */   private boolean wasOnWater = false;
/*  46 */   private boolean doTime = true;
/*     */   
/*  48 */   private Timer timer = new Timer();
/*     */   
/*     */   public void setup()
/*     */   {
/*  52 */     ArrayList<String> options = new ArrayList();
/*  53 */     options.add("Ground");
/*  54 */     options.add("Velt");
/*  55 */     options.add("VeltTest");
/*  56 */     options.add("Mineplex");
/*  57 */     options.add("Hypixel");
/*  58 */     options.add("HypixelYPort");
/*  59 */     options.add("Sloth");
/*  60 */     options.add("Janitor");
/*  61 */     options.add("Faithful");
/*  62 */     options.add("AGC");
/*  63 */     options.add("Shiva");
/*  64 */     Polaris.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Ground", options));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  69 */     String mode = Polaris.instance.settingsManager.getSettingByName("Speed Mode").getValString();
/*  70 */     if (mode.equalsIgnoreCase("Ground")) {
/*  71 */       setDisplayName("Speed §7- Ground");
/*     */     }
/*  73 */     if (mode.equalsIgnoreCase("Velt")) {
/*  74 */       setDisplayName("Speed §7- Velt");
/*     */     }
/*  76 */     if (mode.equalsIgnoreCase("VeltGround")) {
/*  77 */       setDisplayName("Speed §7- VeltTest");
/*     */     }
/*  79 */     if (mode.equalsIgnoreCase("Mineplex")) {
/*  80 */       setDisplayName("Speed §7- Mineplex");
/*     */     }
/*  82 */     if (mode.equalsIgnoreCase("Sloth")) {
/*  83 */       setDisplayName("Speed §7- Sloth");
/*     */     }
/*  85 */     if (mode.equalsIgnoreCase("Janitor")) {
/*  86 */       setDisplayName("Speed §7- Janitor");
/*     */     }
/*  88 */     if (mode.equalsIgnoreCase("Faithful")) {
/*  89 */       setDisplayName("Speed §7- Faithful");
/*     */     }
/*  91 */     if (mode.equalsIgnoreCase("Hypixel")) {
/*  92 */       setDisplayName("Speed §7- Hypixel");
/*     */     }
/*  94 */     if (mode.equalsIgnoreCase("AGC")) {
/*  95 */       setDisplayName("Speed §7- AGC");
/*     */     }
/*  97 */     if (mode.equalsIgnoreCase("Shiva")) {
/*  98 */       setDisplayName("Speed §7- Shiva");
/*     */     }
/* 100 */     if (mode.equalsIgnoreCase("HypixelYPort")) {
/* 101 */       setDisplayName("Speed §7- YPort");
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPre(EventPreMotionUpdate event) {
/* 107 */     String mode = Polaris.instance.settingsManager.getSettingByName("Speed Mode").getValString();
/* 108 */     if (mode.equalsIgnoreCase("VeltTest")) {
/* 109 */       if (mc.thePlayer.ticksExisted % 3 != 0) {
/* 110 */         double speed = 0.03D;
/* 111 */         double x = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/* 112 */         double z = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
/* 113 */         MovementInput movementInput = mc.thePlayer.movementInput;
/* 114 */         double n = MovementInput.moveForward * speed * x;
/* 115 */         MovementInput movementInput2 = mc.thePlayer.movementInput;
/* 116 */         double offsetX = n + MovementInput.moveStrafe * speed * z;
/* 117 */         MovementInput movementInput3 = mc.thePlayer.movementInput;
/* 118 */         double n2 = MovementInput.moveForward * speed * z;
/* 119 */         MovementInput movementInput4 = mc.thePlayer.movementInput;
/* 120 */         double offsetZ = n2 - MovementInput.moveStrafe * speed * x;
/* 121 */         double speedMultiplier = 5.0D;
/* 122 */         if (mc.thePlayer.isMoving())
/*     */         {
/* 124 */           if ((SlitUtils.getBlock(new BlockPos(mc.thePlayer.posX + offsetX * speedMultiplier, mc.thePlayer.posY, mc.thePlayer.posZ + offsetZ * speedMultiplier)) instanceof BlockAir))
/*     */           {
/*     */ 
/* 127 */             if ((SlitUtils.getBlock(new BlockPos(mc.thePlayer.posX + offsetX * speedMultiplier, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ + offsetZ * speedMultiplier)) instanceof BlockAir)) {
/* 128 */               mc.thePlayer.setPosition(mc.thePlayer.posX + offsetX * speedMultiplier, mc.thePlayer.posY, 
/* 129 */                 mc.thePlayer.posZ + offsetZ * speedMultiplier);
/* 130 */               mc.timer.timerSpeed = 1.2F;
/*     */             } } }
/*     */       } else {
/* 133 */         mc.timer.timerSpeed = 1.0F;
/*     */       }
/*     */     }
/* 136 */     if ((mode.equalsIgnoreCase("AGC")) && 
/* 137 */       (mc.thePlayer.isMoving())) {
/* 138 */       if ((mc.thePlayer.onGround) && (mc.thePlayer.ticksExisted % 2 == 0)) {
/* 139 */         mc.thePlayer.setSpeed(1.4D);
/* 140 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.03D, mc.thePlayer.posZ);
/* 141 */       } else if (mc.thePlayer.isAirBorne) {
/* 142 */         mc.thePlayer.setSpeed(0.0F);
/* 143 */         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.03D, mc.thePlayer.posZ);
/*     */       }
/*     */     }
/*     */     
/* 147 */     if (mode.equalsIgnoreCase("Velt")) {
/* 148 */       if (mc.thePlayer.isMoving()) {
/* 149 */         if (mc.thePlayer.onGround) {
/* 150 */           for (int i = 0; i < 20; i++) {
/* 151 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/* 152 */               mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*     */           }
/* 154 */           mc.thePlayer.setSpeed(1.25F);
/* 155 */           event.y = (mc.thePlayer.motionY = 0.4D);
/*     */         } else {
/* 157 */           mc.thePlayer.setSpeed((float)Math.sqrt(
/* 158 */             mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
/*     */         }
/*     */       } else {
/* 161 */         mc.thePlayer.motionX = 0.0D;
/* 162 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */     }
/*     */     
/* 166 */     if (mode.equalsIgnoreCase("HypixelYPort")) {
/* 167 */       if (mc.thePlayer.isMoving()) {
/* 168 */         mc.timer.timerSpeed = (1.0F + this.i);
/* 169 */         if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 170 */           this.i += 0.01F;
/*     */         }
/* 172 */         if (this.i > 0.4F) {
/* 173 */           this.i = 0.0F;
/*     */         }
/*     */       }
/* 176 */       rip.jutting.polaris.utils.HypixelUtils.doLatestOnGroundSpeed();
/*     */     }
/* 178 */     if (mode.equalsIgnoreCase("Hypixel")) {
/* 179 */       if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F) && 
/* 180 */         (mc.thePlayer.onGround)) {
/* 181 */         this.cooldownHops = 4;
/* 182 */         moveSpeed *= 1.1500000429153443D;
/* 183 */         this.state = 2;
/*     */       }
/* 185 */       double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
/* 186 */       double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
/* 187 */       lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
/* 188 */       if (mc.thePlayer.isInWater()) {
/* 189 */         this.cooldownHops = 2;
/* 190 */         return;
/*     */       }
/* 192 */       if ((mc.thePlayer.isOnLadder()) || (mc.thePlayer.isEntityInsideOpaqueBlock())) {
/* 193 */         moveSpeed = 0.0D;
/* 194 */         this.wasOnWater = true;
/* 195 */         return;
/*     */       }
/* 197 */       if (this.wasOnWater) {
/* 198 */         moveSpeed = 0.0D;
/* 199 */         this.wasOnWater = false;
/* 200 */         return;
/*     */       }
/* 202 */       if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
/* 203 */         if (!mc.thePlayer.isMoving()) {
/* 204 */           moveSpeed = mc.thePlayer.getSpeed() * 1.064D;
/*     */         } else {
/* 206 */           moveSpeed = mc.thePlayer.getSpeed() * 1.04D;
/*     */         }
/* 208 */         return;
/*     */       }
/* 210 */       double motionY = Minecraft.getMinecraft().thePlayer.motionY;
/* 211 */       if (mc.thePlayer.onGround) {
/* 212 */         mc.timer.timerSpeed = 1.4F;
/* 213 */         this.state = 2;
/* 214 */         this.timeState += 1;
/* 215 */         if (this.timeState > 4) {
/* 216 */           this.timeState = 0;
/*     */         }
/* 218 */         if (Timer.hasReached(3000L)) {
/* 219 */           this.doTime = (!this.doTime);
/* 220 */           this.timer.reset();
/*     */         }
/* 222 */         EntityPlayerSP thePlayer = mc.thePlayer;
/* 223 */         thePlayer.motionY *= 2.049999952316284D;
/* 224 */         if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
/* 225 */           mc.thePlayer.setSpeed(0.3899999294023D);
/*     */         } else {
/* 227 */           mc.thePlayer.setSpeed(0.3122894120002D);
/*     */         }
/*     */       } else {
/* 230 */         mc.timer.timerSpeed = (1.0F + this.i);
/* 231 */         if (mc.thePlayer.ticksExisted % 1 == 0) {
/* 232 */           this.i += 0.01F;
/*     */         }
/* 234 */         if (this.i > 0.25F) {
/* 235 */           this.i = 0.0F;
/*     */         }
/* 237 */         if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
/* 238 */           mc.thePlayer.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + 
/* 239 */             mc.thePlayer.motionZ * mc.thePlayer.motionZ) * 1.01F);
/*     */         } else {
/* 241 */           mc.thePlayer.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + 
/* 242 */             mc.thePlayer.motionZ * mc.thePlayer.motionZ) * 1.007F);
/*     */         }
/*     */       }
/* 245 */       if (round(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == 
/* 246 */         round(mc.thePlayer.getSpeed(), 2)) {
/* 247 */         event.y -= 0.22316090325960147D;
/* 248 */         EntityPlayerSP thePlayer2 = mc.thePlayer;
/* 249 */         thePlayer2.posY -= 0.22316090325960147D;
/*     */       }
/* 251 */       if ((this.state == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
/* 252 */         this.state = 2;
/* 253 */         moveSpeed = 1.35D * mc.thePlayer.getSpeed() + 0.35D;
/* 254 */       } else if (this.state == 2) {
/* 255 */         this.state = 3;
/* 256 */         if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
/* 257 */           mc.thePlayer.jump();
/* 258 */           if (!mc.thePlayer.onGround) {
/* 259 */             moveSpeed = 1.65D * mc.thePlayer.getSpeed();
/*     */           }
/* 261 */           if (this.cooldownHops > 0) {
/* 262 */             this.cooldownHops -= 1;
/*     */           }
/* 264 */           moveSpeed *= 2.149D;
/*     */         }
/* 266 */       } else if (this.state == 3) {
/* 267 */         this.state = 4;
/* 268 */         double difference = 0.66D * (lastDist - mc.thePlayer.getSpeed());
/* 269 */         moveSpeed = lastDist - difference;
/*     */       }
/*     */       else {
/* 272 */         mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size();
/*     */         
/*     */ 
/* 275 */         moveSpeed = lastDist - lastDist / 159.0D;
/*     */       }
/* 277 */       moveSpeed = Math.max(moveSpeed, mc.thePlayer.getSpeed());
/* 278 */       MovementInput movementInput = mc.thePlayer.movementInput;
/* 279 */       float forward = MovementInput.moveForward;
/* 280 */       float strafe = MovementInput.moveStrafe;
/* 281 */       float yaw = mc.thePlayer.rotationYaw;
/* 282 */       if ((forward == 0.0F) && (strafe == 0.0F)) {
/* 283 */         event.x = 0.0D;
/* 284 */         event.z = 0.0D;
/* 285 */       } else if (forward != 0.0F) {
/* 286 */         if (strafe >= 1.0F) {
/* 287 */           yaw += (forward > 0.0F ? -45 : 45);
/* 288 */           strafe = 0.0F;
/* 289 */         } else if (strafe <= -1.0F) {
/* 290 */           yaw += (forward > 0.0F ? 45 : -45);
/* 291 */           strafe = 0.0F;
/*     */         }
/* 293 */         if (forward > 0.0F) {
/* 294 */           forward = 1.0F;
/* 295 */         } else if (forward < 0.0F) {
/* 296 */           forward = -1.0F;
/*     */         }
/*     */       }
/* 299 */       double mx = Math.cos(Math.toRadians(yaw + 90.0F));
/* 300 */       double mz = Math.sin(Math.toRadians(yaw + 90.0F));
/* 301 */       double motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
/* 302 */       double motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
/* 303 */       if (((mc.thePlayer.isUsingItem()) || (mc.thePlayer.isBlocking())) && 
/* 304 */         (!Polaris.instance.moduleManager.getModuleByName("NoSlowdown").isToggled())) {
/* 305 */         motionX *= 0.7500000059604645D;
/* 306 */         motionZ *= 0.7500000059604645D;
/*     */       }
/* 308 */       event.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
/* 309 */       event.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
/* 310 */       mc.thePlayer.stepHeight = 0.6F;
/* 311 */       if ((forward == 0.0F) && (strafe == 0.0F)) {
/* 312 */         event.x = 0.0D;
/* 313 */         event.z = 0.0D;
/*     */       } else {
/* 315 */         boolean collideCheck = false;
/*     */         
/* 317 */         if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).size() > 0) {
/* 318 */           collideCheck = true;
/*     */         }
/* 320 */         if (forward != 0.0F) {
/* 321 */           if (strafe >= 1.0F) {
/* 322 */             yaw += (forward > 0.0F ? -45 : 45);
/* 323 */             strafe = 0.0F;
/* 324 */           } else if (strafe <= -1.0F) {
/* 325 */             yaw += (forward > 0.0F ? 45 : -45);
/* 326 */             strafe = 0.0F;
/*     */           }
/* 328 */           if (forward > 0.0F) {
/* 329 */             forward = 1.0F;
/* 330 */           } else if (forward < 0.0F) {
/* 331 */             forward = -1.0F;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 336 */     if (mode.equalsIgnoreCase("Shiva")) {
/* 337 */       if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F) && (mc.thePlayer.onGround)) {
/* 338 */         this.cooldownHops = 4;
/* 339 */         moveSpeed *= 5.350000042915344D;
/* 340 */         this.state = 2;
/*     */       }
/* 342 */       double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
/* 343 */       double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
/* 344 */       lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
/* 345 */       if (mc.thePlayer.isInWater()) {
/* 346 */         this.cooldownHops = 2;
/* 347 */         return;
/*     */       }
/* 349 */       if ((mc.thePlayer.isOnLadder()) || (mc.thePlayer.isEntityInsideOpaqueBlock())) {
/* 350 */         moveSpeed = 0.0D;
/* 351 */         this.wasOnWater = true;
/* 352 */         return;
/*     */       }
/* 354 */       if (this.wasOnWater) {
/* 355 */         moveSpeed = 0.0D;
/* 356 */         this.wasOnWater = false;
/* 357 */         return;
/*     */       }
/* 359 */       if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
/* 360 */         if (!mc.thePlayer.isMoving()) {
/* 361 */           moveSpeed = mc.thePlayer.getSpeed() * 5.364D;
/*     */         } else {
/* 363 */           moveSpeed = mc.thePlayer.getSpeed() * 5.34D;
/*     */         }
/* 365 */         return;
/*     */       }
/* 367 */       if (mc.thePlayer.onGround) {
/* 368 */         this.state = 2;
/* 369 */         this.timeState += 1;
/* 370 */         if (this.timeState > 4) {
/* 371 */           this.timeState = 0;
/*     */         }
/* 373 */         if (Timer.hasReached(1500L)) {
/* 374 */           this.doTime = (!this.doTime);
/* 375 */           this.timer.reset();
/*     */         }
/*     */         
/* 378 */         if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
/* 379 */           mc.thePlayer.setSpeed(0.34199999294023D);
/*     */         } else {
/* 381 */           mc.thePlayer.setSpeed(0.25199999294023D);
/*     */         }
/* 383 */         mc.timer.timerSpeed = 3.0F;
/*     */       } else {
/* 385 */         mc.timer.timerSpeed = 1.0F;
/*     */       }
/*     */       
/*     */ 
/* 389 */       round(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3);round(mc.thePlayer.getSpeed(), 2);
/*     */       
/*     */ 
/*     */ 
/* 393 */       if ((this.state == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
/* 394 */         this.state = 2;
/* 395 */         moveSpeed = 1.35D * mc.thePlayer.getSpeed() + 5.35D;
/* 396 */       } else if (this.state == 2) {
/* 397 */         this.state = 3;
/* 398 */         if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
/* 399 */           mc.thePlayer.jump();
/* 400 */           if (!mc.thePlayer.onGround) {
/* 401 */             moveSpeed = 5.65D * mc.thePlayer.getSpeed();
/*     */           }
/* 403 */           if (this.cooldownHops > 0) {
/* 404 */             this.cooldownHops -= 1;
/*     */           }
/* 406 */           moveSpeed *= 5.149D;
/*     */         }
/* 408 */       } else if (this.state == 3) {
/* 409 */         this.state = 4;
/* 410 */         double difference = 0.66D * (lastDist - mc.thePlayer.getSpeed());
/* 411 */         moveSpeed = lastDist - difference;
/*     */       }
/*     */       else {
/* 414 */         if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) {
/* 415 */           this.state = 1;
/*     */         }
/* 417 */         moveSpeed = lastDist - lastDist / 159.0D;
/*     */       }
/* 419 */       moveSpeed = Math.max(moveSpeed, mc.thePlayer.getSpeed());
/* 420 */       MovementInput movementInput = mc.thePlayer.movementInput;
/* 421 */       float forward = MovementInput.moveForward;
/* 422 */       float strafe = MovementInput.moveStrafe;
/* 423 */       float yaw = mc.thePlayer.rotationYaw;
/* 424 */       if ((forward == 0.0F) && (strafe == 0.0F)) {
/* 425 */         event.x = 0.0D;
/* 426 */         event.z = 0.0D;
/* 427 */       } else if (forward != 0.0F) {
/* 428 */         if (strafe >= 1.0F) {
/* 429 */           yaw += (forward > 0.0F ? -45 : 45);
/* 430 */           strafe = 0.0F;
/* 431 */         } else if (strafe <= -1.0F) {
/* 432 */           yaw += (forward > 0.0F ? 45 : -45);
/* 433 */           strafe = 0.0F;
/*     */         }
/* 435 */         if (forward > 0.0F) {
/* 436 */           forward = 1.0F;
/* 437 */         } else if (forward < 0.0F) {
/* 438 */           forward = -1.0F;
/*     */         }
/*     */       }
/* 441 */       double mx = Math.cos(Math.toRadians(yaw + 90.0F));
/* 442 */       double mz = Math.sin(Math.toRadians(yaw + 90.0F));
/* 443 */       double motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
/* 444 */       double motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
/* 445 */       if (((mc.thePlayer.isUsingItem()) || (mc.thePlayer.isBlocking())) && 
/* 446 */         (!Polaris.instance.moduleManager.getModuleByName("NoSlowdown").isToggled())) {
/* 447 */         motionX *= 0.7500000059604645D;
/* 448 */         motionZ *= 0.7500000059604645D;
/*     */       }
/* 450 */       event.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
/* 451 */       event.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
/*     */       
/* 453 */       mc.thePlayer.stepHeight = 0.6F;
/* 454 */       if ((forward == 0.0F) && (strafe == 0.0F)) {
/* 455 */         event.x = 0.0D;
/* 456 */         event.z = 0.0D;
/*     */       } else {
/* 458 */         boolean collideCheck = false;
/*     */         
/*     */ 
/* 461 */         if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).size() > 0) {
/* 462 */           collideCheck = true;
/*     */         }
/* 464 */         if (forward != 0.0F) {
/* 465 */           if (strafe >= 1.0F) {
/* 466 */             yaw += (forward > 0.0F ? -45 : 45);
/* 467 */             strafe = 0.0F;
/* 468 */           } else if (strafe <= -1.0F) {
/* 469 */             yaw += (forward > 0.0F ? 45 : -45);
/* 470 */             strafe = 0.0F;
/*     */           }
/* 472 */           if (forward > 0.0F) {
/* 473 */             forward = 1.0F;
/* 474 */           } else if (forward < 0.0F) {
/* 475 */             forward = -1.0F;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 481 */     if ((mode.equalsIgnoreCase("Faithful")) && 
/* 482 */       (mc.thePlayer.isMoving())) {
/* 483 */       mc.timer.timerSpeed = 0.33F;
/* 484 */       mc.thePlayer.setSpeed(2.0F);
/* 485 */       mc.thePlayer.onGround = true;
/*     */     }
/*     */     
/* 488 */     if (mode.equalsIgnoreCase("Mineplex")) {
/* 489 */       mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
/* 490 */       if (mc.thePlayer.isMoving()) {
/* 491 */         mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() + 0.005D);
/* 492 */         if (mc.thePlayer.onGround) {
/* 493 */           mc.thePlayer.jump();
/* 494 */           mc.thePlayer.motionY = 0.42D;
/*     */         } else {
/* 496 */           mc.thePlayer.setSpeed((float)Math.sqrt(
/* 497 */             mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
/*     */         }
/*     */       } else {
/* 500 */         mc.thePlayer.motionX = 0.0D;
/* 501 */         mc.thePlayer.motionZ = 0.0D;
/*     */       }
/*     */     }
/* 504 */     if ((mode.equalsIgnoreCase("Sloth")) && 
/* 505 */       (mc.thePlayer.isMoving())) {
/* 506 */       boolean under = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, 
/* 507 */         mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, 1.6D, mc.thePlayer.motionZ))
/* 508 */         .isEmpty();
/* 509 */       if ((mc.thePlayer.ticksExisted % 2 != 0) && (under)) {
/* 510 */         event.y += 0.42D;
/*     */       }
/* 512 */       mc.thePlayer.motionY = -10.0D;
/* 513 */       if (mc.thePlayer.onGround)
/*     */       {
/* 515 */         mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * (mc.thePlayer.ticksExisted % 2 == 0 ? 4.0F : 0.28F));
/*     */       }
/*     */     }
/*     */     
/* 519 */     if ((mode.equalsIgnoreCase("Janitor")) && 
/* 520 */       (mc.thePlayer.isMoving())) {
/* 521 */       mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * (mc.thePlayer.ticksExisted % 2 == 0 ? 0 : 20));
/*     */     }
/*     */     
/* 524 */     if (mode.equalsIgnoreCase("Ground")) {
/* 525 */       boolean under = mc.theWorld
/* 526 */         .getCollidingBoundingBoxes(mc.thePlayer, 
/* 527 */         mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, 1.6D, mc.thePlayer.motionZ))
/* 528 */         .isEmpty();
/* 529 */       if ((mc.thePlayer.ticksExisted % 2 != 0) && (under)) {
/* 530 */         event.y += 0.42D;
/*     */       }
/* 532 */       if ((!under) && (mc.thePlayer.ticksExisted % 2 != 0)) {
/* 533 */         event.y += 0.2D;
/*     */       }
/* 535 */       mc.thePlayer.motionY = -10.0D;
/* 536 */       mc.timer.timerSpeed = (mc.thePlayer.ticksExisted % 3 == 0 ? 2.435F : 0.92F);
/* 537 */       mc.thePlayer.setSpeed(
/* 538 */         (float)(mc.thePlayer.ticksExisted % 2 == 0 ? mc.thePlayer.getSpeed() * 1.683849D : mc.thePlayer.getSpeed() / 1.5D));
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPre(EventUpdate event) {
/* 544 */     String mode = Polaris.instance.settingsManager.getSettingByName("Speed Mode").getValString();
/*     */   }
/*     */   
/*     */   private boolean isOnLiquid()
/*     */   {
/* 549 */     boolean onLiquid = false;
/* 550 */     int y = (int)(mc.thePlayer.boundingBox.minY - 0.01D);
/* 551 */     for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < 
/* 552 */           MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
/* 553 */       for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < 
/* 554 */             MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
/* 555 */         net.minecraft.block.Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
/* 556 */         if ((block != null) && (!(block instanceof BlockAir))) {
/* 557 */           if (!(block instanceof net.minecraft.block.BlockLiquid))
/* 558 */             return false;
/* 559 */           onLiquid = true;
/*     */         }
/*     */       }
/*     */     }
/* 563 */     return onLiquid;
/*     */   }
/*     */   
/*     */   public static double getBaseMoveSpeed() {
/* 567 */     double baseSpeed = 0.2873D;
/* 568 */     if ((Minecraft.getMinecraft().theWorld != null) && 
/* 569 */       (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))) {
/* 570 */       int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
/* 571 */       baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
/*     */     }
/*     */     
/* 574 */     return baseSpeed;
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 579 */     mc.timer.timerSpeed = 1.0F;
/* 580 */     super.onDisable();
/*     */   }
/*     */   
/*     */   public double round(double value, int places) {
/* 584 */     if (places < 0) {
/* 585 */       throw new IllegalArgumentException();
/*     */     }
/* 587 */     BigDecimal bd = new BigDecimal(value);
/* 588 */     bd = bd.setScale(places, java.math.RoundingMode.HALF_UP);
/* 589 */     return bd.doubleValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */