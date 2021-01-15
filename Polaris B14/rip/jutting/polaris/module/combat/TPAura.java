/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFence;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity.Action;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Category;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.RotationUtils;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ public class TPAura extends Module
/*     */ {
/*     */   private int ticks;
/*  49 */   private List<EntityLivingBase> loaded = new ArrayList();
/*     */   private EntityLivingBase target;
/*     */   private int tpdelay;
/*     */   public boolean criticals;
/*     */   private Timer timer;
/*     */   public Timer timer2;
/*  55 */   boolean ni = true;
/*  56 */   boolean ye = false;
/*     */   
/*     */   public static float yaw;
/*     */   
/*     */   public static float pitch;
/*     */   private int delay;
/*  62 */   private Timer blockTimer = new Timer();
/*  63 */   private static EntityLivingBase en = null;
/*  64 */   boolean attack = false;
/*     */   double x;
/*     */   double y;
/*     */   double z;
/*     */   double xPreEn;
/*     */   double yPreEn;
/*     */   double zPreEn;
/*     */   double xPre;
/*     */   double yPre;
/*     */   double zPre;
/*  74 */   int stage = 0;
/*  75 */   ArrayList<Vec3> positions = new ArrayList();
/*  76 */   ArrayList<Vec3> positionsBack = new ArrayList();
/*     */   public static final double maxXZTP = 9.5D;
/*     */   public static final int maxYTP = 9;
/*     */   public static Entity looky;
/*     */   
/*     */   public TPAura() {
/*  82 */     super("TPAura", 0, Category.COMBAT);
/*  83 */     this.ticks = 0;
/*  84 */     this.timer = new Timer();
/*  85 */     this.timer2 = new Timer();
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  90 */     ArrayList<String> options = new ArrayList();
/*  91 */     options.add("AGC");
/*  92 */     options.add("Raytrace");
/*  93 */     options.add("Faithful");
/*  94 */     Polaris.instance.settingsManager.rSetting(new Setting("TPAura Mode", this, "AGC", options));
/*     */   }
/*     */   
/*     */   public void onEnable()
/*     */   {
/*  99 */     this.target = null;
/* 100 */     this.timer.reset();
/* 101 */     this.timer2.reset();
/* 102 */     this.stage = 0;
/* 103 */     this.x = 0.0D;
/* 104 */     this.y = 0.0D;
/* 105 */     this.z = 0.0D;
/* 106 */     this.xPreEn = 0.0D;
/* 107 */     this.yPreEn = 0.0D;
/* 108 */     this.zPreEn = 0.0D;
/* 109 */     this.attack = false;
/* 110 */     super.onEnable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPacketReceive(EventReceivePacket event) {
/* 115 */     String mode = Polaris.instance.settingsManager.getSettingByName("TPAura Mode").getValString();
/* 116 */     if ((mode.equalsIgnoreCase("Faithful")) && 
/* 117 */       ((event.getPacket() instanceof net.minecraft.network.play.client.C0APacketAnimation))) {
/* 118 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event)
/*     */   {
/* 125 */     String mode = Polaris.instance.settingsManager.getSettingByName("TPAura Mode").getValString();
/* 126 */     if (mode.equalsIgnoreCase("AGC")) {
/* 127 */       setDisplayName("TPAura §7- AGC");
/* 128 */       this.ticks += 1;
/* 129 */       this.tpdelay += 1;
/* 130 */       if (this.ticks >= 20 - speed()) {
/* 131 */         this.ticks = 0;
/* 132 */         for (Object object : mc.theWorld.loadedEntityList) {
/* 133 */           if ((object instanceof EntityLivingBase)) {
/* 134 */             EntityLivingBase entity = (EntityLivingBase)object;
/* 135 */             if (!(entity instanceof EntityPlayerSP))
/*     */             {
/*     */ 
/* 138 */               if (mc.thePlayer.getDistanceToEntity(entity) <= 10.0F)
/*     */               {
/*     */ 
/* 141 */                 if (entity.isEntityAlive()) {
/* 142 */                   if (this.tpdelay >= 4) {
/* 143 */                     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
/* 144 */                       entity.posX, entity.posY, entity.posZ, false));
/*     */                   }
/* 146 */                   if (mc.thePlayer.getDistanceToEntity(entity) < 10.0F)
/* 147 */                     attack(entity);
/*     */                 } }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 154 */     if (mode.equalsIgnoreCase("Faithful")) {
/* 155 */       setDisplayName("TPAura §7- Faithful");
/* 156 */       this.ticks += 1;
/* 157 */       this.tpdelay += 1;
/* 158 */       if (this.ticks >= 20 - speed()) {
/* 159 */         this.ticks = 0;
/* 160 */         for (Object object : mc.theWorld.loadedEntityList) {
/* 161 */           if ((object instanceof EntityLivingBase)) {
/* 162 */             EntityLivingBase entity = (EntityLivingBase)object;
/* 163 */             if (!(entity instanceof EntityPlayerSP))
/*     */             {
/*     */ 
/* 166 */               if (mc.thePlayer.getDistanceToEntity(entity) <= 3.22F)
/*     */               {
/*     */ 
/* 169 */                 if (entity.isEntityAlive())
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */                   if (mc.thePlayer.getDistanceToEntity(entity) < 3.22F)
/* 178 */                     attack(entity); }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 185 */     if (mode.equalsIgnoreCase("Raytrace")) {
/* 186 */       setDisplayName("TPAura §7- Raytrace");
/* 187 */       looky = closeEntity();
/* 188 */       LookAtEntity(looky);
/* 189 */       if (mc.thePlayer.ticksExisted % 5 == 0) {
/* 190 */         updateStages();
/* 191 */         this.timer.reset();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attack(EntityLivingBase entity)
/*     */   {
/* 205 */     attack(entity, this.criticals);
/*     */   }
/*     */   
/*     */   public Entity closeEntity() {
/* 209 */     Entity close = null;
/* 210 */     for (Object o : mc.theWorld.loadedEntityList) {
/* 211 */       Entity e = (Entity)o;
/* 212 */       if (((e instanceof net.minecraft.client.entity.EntityOtherPlayerMP)) && (e.isEntityAlive()) && (
/* 213 */         (close == null) || (mc.thePlayer.getDistanceToEntity(e) < mc.thePlayer.getDistanceToEntity(close)))) {
/* 214 */         close = e;
/*     */       }
/*     */     }
/*     */     
/* 218 */     return close;
/*     */   }
/*     */   
/*     */   public void attack(EntityLivingBase entity, boolean crit) {
/* 222 */     mc.thePlayer.swingItem();
/* 223 */     float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), 
/* 224 */       entity.getCreatureAttribute());
/* 225 */     boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround) && 
/* 226 */       (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater()) && 
/* 227 */       (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
/* 228 */     mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
/* 229 */     if ((crit) || (vanillaCrit)) {
/* 230 */       mc.thePlayer.onCriticalHit(entity);
/*     */     }
/* 232 */     if (sharpLevel > 0.0F) {
/* 233 */       mc.thePlayer.onEnchantmentCritical(entity);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 238 */     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
/*     */   }
/*     */   
/*     */   public static float[] getRotations(Entity ent)
/*     */   {
/* 243 */     double x = ent.posX;
/* 244 */     double z = ent.posZ;
/* 245 */     double y = ent.boundingBox.maxY - 4.0D;
/* 246 */     return getRotationFromPosition(x, z, y);
/*     */   }
/*     */   
/*     */   public static float[] getRotationFromPosition(double x, double z, double y) {
/* 250 */     double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
/* 251 */     double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
/* 252 */     double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
/* 253 */     double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
/* 254 */     float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
/* 255 */     float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
/* 256 */     return new float[] { yaw, pitch };
/*     */   }
/*     */   
/*     */   public static void LookAtEntity(Entity entity) {
/* 260 */     float[] rotations = getRotations(entity);
/* 261 */     yaw = rotations[0];
/* 262 */     pitch = rotations[1];
/*     */   }
/*     */   
/*     */   public MovingObjectPosition rayTrace(double blockReachDistance) {
/* 266 */     Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
/* 267 */     Vec3 vec4 = mc.thePlayer.getLookVec();
/* 268 */     Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, 
/* 269 */       vec4.zCoord * blockReachDistance);
/* 270 */     return mc.theWorld.rayTraceBlocks(vec3, vec5, !mc.thePlayer.isInWater(), false, false);
/*     */   }
/*     */   
/*     */   private int speed() {
/* 274 */     return 6;
/*     */   }
/*     */   
/*     */   public void updateStages() {
/* 278 */     this.positions.clear();
/* 279 */     this.positionsBack.clear();
/* 280 */     int targets = 0;
/* 281 */     List<EntityLivingBase> list = getEntities(3.7D);
/* 282 */     list.sort(new java.util.Comparator() {
/*     */       public int compare(EntityLivingBase o1, EntityLivingBase o2) {
/* 284 */         if (TPAura.mc.thePlayer.getDistanceToEntity(o1) > TPAura.mc.thePlayer.getDistanceToEntity(o2)) {
/* 285 */           return 1;
/*     */         }
/* 287 */         if (TPAura.mc.thePlayer.getDistanceToEntity(o1) < TPAura.mc.thePlayer.getDistanceToEntity(o2)) {
/* 288 */           return -1;
/*     */         }
/* 290 */         if (TPAura.mc.thePlayer.getDistanceToEntity(o1) == TPAura.mc.thePlayer.getDistanceToEntity(o2)) {
/* 291 */           return 0;
/*     */         }
/* 293 */         return 0;
/*     */       }
/*     */     });
/* 296 */     for (EntityLivingBase en : list)
/*     */     {
/* 298 */       boolean up = false;
/* 299 */       this.positions.clear();
/* 300 */       this.positionsBack.clear();
/* 301 */       en = en;
/* 302 */       doReach(mc.thePlayer.getDistanceToEntity(en), up, list);
/* 303 */       this.stage = 0;
/* 304 */       targets++;
/*     */     }
/*     */   }
/*     */   
/*     */   public void doReach(double range, boolean up, List<EntityLivingBase> list) {
/* 309 */     if (mc.thePlayer.getDistanceToEntity(en) <= 3.7D)
/*     */     {
/* 311 */       attack(en);
/* 312 */       return;
/*     */     }
/* 314 */     this.attack = infiniteReach(range, 9.5D, 9.0D, this.positionsBack, this.positions, en);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean infiniteReach(double range, double maxXZTP, double maxYTP, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions, EntityLivingBase en)
/*     */   {
/* 320 */     int ind = 0;
/* 321 */     this.xPreEn = en.posX;
/* 322 */     this.yPreEn = en.posY;
/* 323 */     this.zPreEn = en.posZ;
/* 324 */     this.xPre = mc.thePlayer.posX;
/* 325 */     this.yPre = mc.thePlayer.posY;
/* 326 */     this.zPre = mc.thePlayer.posZ;
/* 327 */     boolean attack = true;
/* 328 */     boolean up = false;
/* 329 */     boolean tpUpOneBlock = false;
/*     */     
/*     */ 
/* 332 */     boolean hit = false;
/* 333 */     boolean tpStraight = false;
/*     */     
/* 335 */     boolean sneaking = mc.thePlayer.isSneaking();
/*     */     
/* 337 */     positions.clear();
/* 338 */     positionsBack.clear();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 343 */     double step = maxXZTP / range;
/* 344 */     int steps = 0;
/* 345 */     for (int i = 0; i < range; i++) {
/* 346 */       steps++;
/*     */       
/* 348 */       if (maxXZTP * steps > range) {
/*     */         break;
/*     */       }
/*     */     }
/* 352 */     MovingObjectPosition rayTrace = null;
/* 353 */     MovingObjectPosition rayTrace1 = null;
/* 354 */     MovingObjectPosition rayTraceCarpet = null;
/*     */     
/* 356 */     if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, en.posY, en.posZ), false, false, true)) || 
/* 357 */       ((rayTrace1 = rayTracePos(
/* 358 */       new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), 
/* 359 */       new Vec3(en.posX, en.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false, 
/* 360 */       true)) != null)) {
/* 361 */       if (((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 
/* 362 */         new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true)) != null) || 
/* 363 */         ((rayTrace1 = rayTracePos(
/* 364 */         new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), 
/* 365 */         mc.thePlayer.posZ), 
/* 366 */         new Vec3(en.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false, 
/* 367 */         true)) != null)) {
/* 368 */         MovingObjectPosition trace = null;
/* 369 */         if (rayTrace == null) {
/* 370 */           trace = rayTrace1;
/*     */         }
/* 372 */         if (rayTrace1 == null) {
/* 373 */           trace = rayTrace;
/*     */         }
/* 375 */         if (trace != null)
/*     */         {
/*     */ 
/*     */ 
/* 379 */           if (trace.getBlockPos() != null) {
/* 380 */             boolean fence = false;
/* 381 */             BlockPos target = trace.getBlockPos();
/*     */             
/* 383 */             up = true;
/* 384 */             this.y = target.up().getY();
/* 385 */             this.yPreEn = target.up().getY();
/* 386 */             Block lastBlock = null;
/* 387 */             Boolean found = Boolean.valueOf(false);
/* 388 */             for (int i = 0; i < maxYTP; i++) {
/* 389 */               MovingObjectPosition tr = rayTracePos(
/* 390 */                 new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ), 
/* 391 */                 new Vec3(en.posX, target.getY() + i, en.posZ), false, false, true);
/* 392 */               if (tr != null)
/*     */               {
/*     */ 
/* 395 */                 if (tr.getBlockPos() != null)
/*     */                 {
/*     */ 
/*     */ 
/* 399 */                   BlockPos blockPos = tr.getBlockPos();
/* 400 */                   Block block = mc.theWorld.getBlockState(blockPos).getBlock();
/* 401 */                   if (block.getMaterial() != Material.air) {
/* 402 */                     lastBlock = block;
/*     */                   }
/*     */                   else {
/* 405 */                     fence = lastBlock instanceof BlockFence;
/* 406 */                     this.y = (target.getY() + i);
/* 407 */                     this.yPreEn = (target.getY() + i);
/* 408 */                     if (fence) {
/* 409 */                       this.y += 1.0D;
/* 410 */                       this.yPreEn += 1.0D;
/* 411 */                       if (i + 1 > maxYTP) {
/* 412 */                         found = Boolean.valueOf(false);
/* 413 */                         break;
/*     */                       }
/*     */                     }
/* 416 */                     found = Boolean.valueOf(true);
/* 417 */                     break;
/*     */                   } } } }
/* 419 */             double difX = mc.thePlayer.posX - this.xPreEn;
/* 420 */             double difZ = mc.thePlayer.posZ - this.zPreEn;
/* 421 */             double divider = step * 0.0D;
/* 422 */             if (!found.booleanValue()) {
/* 423 */               attack = false;
/* 424 */               return false;
/*     */             }
/*     */           } else {
/* 427 */             attack = false;
/* 428 */             return false;
/*     */           }
/*     */         }
/*     */       } else {
/* 432 */         MovingObjectPosition ent = rayTracePos(
/* 433 */           new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 
/* 434 */           new Vec3(en.posX, en.posY, en.posZ), false, false, false);
/* 435 */         if ((ent != null) && (ent.entityHit == null)) {
/* 436 */           this.y = mc.thePlayer.posY;
/* 437 */           this.yPreEn = mc.thePlayer.posY;
/*     */         } else {
/* 439 */           this.y = mc.thePlayer.posY;
/* 440 */           this.yPreEn = en.posY;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 445 */     if (!attack) {
/* 446 */       return false;
/*     */     }
/* 448 */     if (sneaking)
/*     */     {
/* 450 */       mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
/*     */     }
/* 452 */     for (int i = 0; i < steps; i++) {
/* 453 */       ind++;
/* 454 */       if ((i == 1) && (up)) {
/* 455 */         this.x = mc.thePlayer.posX;
/* 456 */         this.y = this.yPreEn;
/* 457 */         this.z = mc.thePlayer.posZ;
/* 458 */         sendPacket(false, positionsBack, positions);
/*     */       }
/* 460 */       if (i != steps - 1)
/*     */       {
/* 462 */         double difX = mc.thePlayer.posX - this.xPreEn;
/* 463 */         double difY = mc.thePlayer.posY - this.yPreEn;
/* 464 */         double difZ = mc.thePlayer.posZ - this.zPreEn;
/* 465 */         double divider = step * i;
/* 466 */         this.x = (mc.thePlayer.posX - difX * divider);
/* 467 */         this.y = (mc.thePlayer.posY - difY * (up ? 1.0D : divider));
/* 468 */         this.z = (mc.thePlayer.posZ - difZ * divider);
/*     */         
/* 470 */         sendPacket(false, positionsBack, positions);
/*     */       }
/*     */       else
/*     */       {
/* 474 */         double difX = mc.thePlayer.posX - this.xPreEn;
/* 475 */         double difY = mc.thePlayer.posY - this.yPreEn;
/* 476 */         double difZ = mc.thePlayer.posZ - this.zPreEn;
/* 477 */         double divider = step * i;
/* 478 */         this.x = (mc.thePlayer.posX - difX * divider);
/* 479 */         this.y = (mc.thePlayer.posY - difY * (up ? 1.0D : divider));
/* 480 */         this.z = (mc.thePlayer.posZ - difZ * divider);
/*     */         
/* 482 */         sendPacket(false, positionsBack, positions);
/* 483 */         double xDist = this.x - this.xPreEn;
/* 484 */         double zDist = this.z - this.zPreEn;
/* 485 */         double yDist = this.y - en.posY;
/* 486 */         double dist = Math.sqrt(xDist * xDist + zDist * zDist);
/* 487 */         if (dist > 3.7D) {
/* 488 */           this.x = this.xPreEn;
/* 489 */           this.y = this.yPreEn;
/* 490 */           this.z = this.zPreEn;
/* 491 */           sendPacket(false, positionsBack, positions);
/* 492 */         } else if ((dist > 0.05D) && (up)) {
/* 493 */           this.x = this.xPreEn;
/* 494 */           this.y = this.yPreEn;
/* 495 */           this.z = this.zPreEn;
/* 496 */           sendPacket(false, positionsBack, positions);
/*     */         }
/* 498 */         if ((Math.abs(yDist) < maxYTP) && (mc.thePlayer.getDistanceToEntity(en) >= 3.7D)) {
/* 499 */           this.x = this.xPreEn;
/* 500 */           this.y = en.posY;
/* 501 */           this.z = this.zPreEn;
/* 502 */           sendPacket(false, positionsBack, positions);
/*     */           
/* 504 */           attackInf(en);
/*     */         } else {
/* 506 */           attack = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 512 */     for (int i = positions.size() - 2; i > -1; i--)
/*     */     {
/* 514 */       this.x = ((Vec3)positions.get(i)).xCoord;
/* 515 */       this.y = ((Vec3)positions.get(i)).yCoord;
/* 516 */       this.z = ((Vec3)positions.get(i)).zCoord;
/*     */       
/* 518 */       sendPacket(false, positionsBack, positions);
/*     */     }
/* 520 */     this.x = mc.thePlayer.posX;
/* 521 */     this.y = mc.thePlayer.posY;
/* 522 */     this.z = mc.thePlayer.posZ;
/* 523 */     sendPacket(false, positionsBack, positions);
/* 524 */     if (!attack) {
/* 525 */       if (sneaking) {
/* 526 */         mc.getNetHandler().getNetworkManager().sendPacket(
/* 527 */           new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
/*     */       }
/* 529 */       positions.clear();
/* 530 */       positionsBack.clear();
/* 531 */       return false;
/*     */     }
/* 533 */     if (sneaking)
/*     */     {
/* 535 */       mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
/*     */     }
/* 537 */     return true;
/*     */   }
/*     */   
/*     */   private void attackInf(EntityLivingBase en) {
/* 541 */     if (mc.thePlayer.isBlocking()) {
/* 542 */       mc.thePlayer.sendQueue.addToSendQueue(
/* 543 */         new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
/*     */     }
/* 545 */     mc.thePlayer.swingItem();
/*     */     
/*     */ 
/*     */ 
/* 549 */     mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(en, C02PacketUseEntity.Action.ATTACK));
/* 550 */     if (mc.thePlayer.isBlocking()) {
/* 551 */       mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
/* 552 */       mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
/* 553 */       if (mc.thePlayer.isBlocking()) {
/* 554 */         mc.thePlayer.sendQueue.addToSendQueue(
/* 555 */           new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendPacket(boolean goingBack, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions)
/*     */   {
/* 562 */     C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true);
/*     */     
/* 564 */     if (goingBack) {
/* 565 */       positionsBack.add(new Vec3(this.x, this.y, this.z));
/* 566 */       return;
/*     */     }
/* 568 */     positions.add(new Vec3(this.x, this.y, this.z));
/*     */   }
/*     */   
/*     */ 
/*     */   public MovingObjectPosition rayTracePos(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
/*     */   {
/* 574 */     float[] rots = getFacePosRemote(vec32, vec31);
/* 575 */     float yaw = rots[0];
/* 576 */     double angleA = Math.toRadians(normalizeAngle(yaw));
/* 577 */     double angleB = Math.toRadians(normalizeAngle(yaw) + 180.0F);
/* 578 */     double size = 2.1D;
/* 579 */     double size2 = 2.1D;
/* 580 */     Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord, 
/* 581 */       vec31.zCoord + Math.sin(angleA) * size);
/* 582 */     Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord, 
/* 583 */       vec31.zCoord + Math.sin(angleB) * size);
/* 584 */     Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord, 
/* 585 */       vec32.zCoord + Math.sin(angleA) * size);
/* 586 */     Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord, 
/* 587 */       vec32.zCoord + Math.sin(angleB) * size);
/* 588 */     Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord, 
/* 589 */       vec31.zCoord + Math.sin(angleA) * size2);
/* 590 */     Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord, 
/* 591 */       vec31.zCoord + Math.sin(angleB) * size2);
/* 592 */     Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord, 
/* 593 */       vec32.zCoord + Math.sin(angleA) * size2);
/* 594 */     Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord, 
/* 595 */       vec32.zCoord + Math.sin(angleB) * size2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 604 */     MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid, 
/* 605 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/* 606 */     MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid, 
/* 607 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/* 608 */     MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid, 
/* 609 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 617 */     MovingObjectPosition trace4 = null;
/* 618 */     MovingObjectPosition trace5 = null;
/* 619 */     if ((trace2 != null) || (trace1 != null) || (trace3 != null) || (trace4 != null) || (trace5 != null)) {
/* 620 */       if (returnLastUncollidableBlock) {
/* 621 */         if ((trace5 != null) && (
/* 622 */           (getBlock(trace5.getBlockPos()).getMaterial() != Material.air) || (trace5.entityHit != null)))
/*     */         {
/* 624 */           return trace5;
/*     */         }
/* 626 */         if ((trace4 != null) && (
/* 627 */           (getBlock(trace4.getBlockPos()).getMaterial() != Material.air) || (trace4.entityHit != null)))
/*     */         {
/* 629 */           return trace4;
/*     */         }
/* 631 */         if ((trace3 != null) && (
/* 632 */           (getBlock(trace3.getBlockPos()).getMaterial() != Material.air) || (trace3.entityHit != null)))
/*     */         {
/* 634 */           return trace3;
/*     */         }
/* 636 */         if ((trace1 != null) && (
/* 637 */           (getBlock(trace1.getBlockPos()).getMaterial() != Material.air) || (trace1.entityHit != null)))
/*     */         {
/* 639 */           return trace1;
/*     */         }
/* 641 */         if ((trace2 != null) && (
/* 642 */           (getBlock(trace2.getBlockPos()).getMaterial() != Material.air) || (trace2.entityHit != null)))
/*     */         {
/* 644 */           return trace2;
/*     */         }
/*     */       } else {
/* 647 */         if (trace5 != null) {
/* 648 */           return trace5;
/*     */         }
/* 650 */         if (trace4 != null) {
/* 651 */           return trace4;
/*     */         }
/* 653 */         if (trace3 != null)
/*     */         {
/* 655 */           return trace3;
/*     */         }
/* 657 */         if (trace1 != null)
/*     */         {
/* 659 */           return trace1;
/*     */         }
/* 661 */         if (trace2 != null)
/*     */         {
/* 663 */           return trace2;
/*     */         }
/*     */       }
/*     */     }
/* 667 */     if (trace2 == null) {
/* 668 */       if (trace3 == null) {
/* 669 */         if (trace1 == null) {
/* 670 */           if (trace5 == null) {
/* 671 */             if (trace4 == null) {
/* 672 */               return null;
/*     */             }
/* 674 */             return trace4;
/*     */           }
/* 676 */           return trace5;
/*     */         }
/* 678 */         return trace1;
/*     */       }
/* 680 */       return trace3;
/*     */     }
/* 682 */     return trace2;
/*     */   }
/*     */   
/*     */   public boolean rayTraceWide(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
/*     */   {
/* 687 */     float yaw = getFacePosRemote(vec32, vec31)[0];
/* 688 */     yaw = normalizeAngle(yaw);
/* 689 */     yaw += 180.0F;
/* 690 */     yaw = MathHelper.wrapAngleTo180_float(yaw);
/* 691 */     double angleA = Math.toRadians(yaw);
/* 692 */     double angleB = Math.toRadians(yaw + 180.0F);
/* 693 */     double size = 2.1D;
/* 694 */     double size2 = 2.1D;
/* 695 */     Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord, 
/* 696 */       vec31.zCoord + Math.sin(angleA) * size);
/* 697 */     Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord, 
/* 698 */       vec31.zCoord + Math.sin(angleB) * size);
/* 699 */     Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord, 
/* 700 */       vec32.zCoord + Math.sin(angleA) * size);
/* 701 */     Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord, 
/* 702 */       vec32.zCoord + Math.sin(angleB) * size);
/* 703 */     Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord, 
/* 704 */       vec31.zCoord + Math.sin(angleA) * size2);
/* 705 */     Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord, 
/* 706 */       vec31.zCoord + Math.sin(angleB) * size2);
/* 707 */     Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord, 
/* 708 */       vec32.zCoord + Math.sin(angleA) * size2);
/* 709 */     Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord, 
/* 710 */       vec32.zCoord + Math.sin(angleB) * size2);
/*     */     
/*     */ 
/*     */ 
/* 714 */     MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid, 
/* 715 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/* 716 */     MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid, 
/* 717 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/* 718 */     MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid, 
/* 719 */       ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
/*     */     
/*     */ 
/*     */ 
/* 723 */     MovingObjectPosition trace4 = null;
/* 724 */     MovingObjectPosition trace5 = null;
/* 725 */     if (returnLastUncollidableBlock) {
/* 726 */       return ((trace1 != null) && (getBlock(trace1.getBlockPos()).getMaterial() != Material.air)) || 
/* 727 */         ((trace2 != null) && (getBlock(trace2.getBlockPos()).getMaterial() != Material.air)) || 
/* 728 */         ((trace3 != null) && (getBlock(trace3.getBlockPos()).getMaterial() != Material.air)) || 
/* 729 */         ((trace4 != null) && (getBlock(trace4.getBlockPos()).getMaterial() != Material.air)) || (
/* 730 */         (trace5 != null) && (getBlock(trace5.getBlockPos()).getMaterial() != Material.air));
/*     */     }
/* 732 */     return (trace1 != null) || (trace2 != null) || (trace3 != null) || (trace5 != null) || (trace4 != null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static float[] getFacePosRemote(Vec3 src, Vec3 dest)
/*     */   {
/* 738 */     double diffX = dest.xCoord - src.xCoord;
/* 739 */     double diffY = dest.yCoord - src.yCoord;
/* 740 */     double diffZ = dest.zCoord - src.zCoord;
/* 741 */     double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
/* 742 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
/* 743 */     float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
/* 744 */     return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
/*     */   }
/*     */   
/*     */   public static double normalizeAngle(double angle) {
/* 748 */     return (angle + 360.0D) % 360.0D;
/*     */   }
/*     */   
/*     */   public static float normalizeAngle(float angle) {
/* 752 */     return (angle + 360.0F) % 360.0F;
/*     */   }
/*     */   
/*     */   public Block getBlock(BlockPos pos) {
/* 756 */     return mc.theWorld.getBlockState(pos).getBlock();
/*     */   }
/*     */   
/*     */   public List<EntityLivingBase> getEntities(double range) {
/* 760 */     List<EntityLivingBase> list = new ArrayList();
/* 761 */     for (Object ent : mc.theWorld.loadedEntityList) {
/* 762 */       if (((ent instanceof EntityLivingBase)) && 
/* 763 */         (isEntityValid((EntityLivingBase)ent)) && (mc.thePlayer.getDistanceToEntity((Entity)ent) <= range) && 
/* 764 */         (list.size() <= 1)) {
/* 765 */         list.add((EntityLivingBase)ent);
/*     */       }
/*     */     }
/*     */     
/* 769 */     return list;
/*     */   }
/*     */   
/*     */   public boolean isEntityInFov(EntityLivingBase entity)
/*     */   {
/* 774 */     double local_03 = entity.posX - mc.thePlayer.posX;
/* 775 */     double local_05 = entity.posZ - mc.thePlayer.posZ;
/* 776 */     double local_07 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (
/* 777 */       entity.posY + entity.getEyeHeight());
/* 778 */     double local_09 = Math.sqrt(local_03 * local_03 + local_05 * local_05);
/* 779 */     float local_11 = (float)(Math.atan2(local_05, local_03) * 180.0D / 3.141592653589793D) - 90.0F;
/* 780 */     float local_12 = (float)(Math.atan2(local_07, local_09) * 180.0D / 3.141592653589793D);
/* 781 */     double local_13 = RotationUtils.getDistanceBetweenAngles(local_11, mc.thePlayer.rotationYaw % 360.0F);
/* 782 */     double local_15; double local_17 = Math.sqrt(local_13 * local_13 + (local_15 = 
/* 783 */       RotationUtils.getDistanceBetweenAngles(local_12, mc.thePlayer.rotationPitch % 360.0F)) * local_15);
/* 784 */     return local_17 < 360.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityLivingBase getClosestEntity()
/*     */   {
/* 793 */     double dist = 3.7D;
/* 794 */     EntityLivingBase entity = null;
/*     */     
/* 796 */     for (Object object : mc.theWorld.loadedEntityList) {
/* 797 */       if ((object instanceof EntityLivingBase)) {
/* 798 */         EntityLivingBase e = (EntityLivingBase)object;
/*     */         
/* 800 */         if ((e.getDistanceToEntity(mc.thePlayer) < dist) && (isEntityValid(e)) && 
/* 801 */           (e.getDistanceToEntity(mc.thePlayer) <= 3.7D)) {
/* 802 */           dist = e.getDistanceToEntity(mc.thePlayer);
/* 803 */           entity = e;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 808 */     return entity;
/*     */   }
/*     */   
/*     */   public boolean weaponCheck() {
/* 812 */     if (this.ni) {
/* 813 */       if ((mc.thePlayer.getHeldItem() != null) && (mc.thePlayer.getHeldItem().getItem() != null)) {
/* 814 */         if (((mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)) || 
/* 815 */           ((mc.thePlayer.getHeldItem().getItem() instanceof ItemTool))) {
/* 816 */           return true;
/*     */         }
/* 818 */         return false;
/*     */       }
/* 820 */       return false;
/*     */     }
/*     */     
/* 823 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPlayerValid(EntityPlayer player) {
/* 827 */     if (!teamCheck(player)) {
/* 828 */       return false;
/*     */     }
/* 830 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEntityValidType(EntityLivingBase entity) {
/* 834 */     if (!weaponCheck()) {
/* 835 */       return false;
/*     */     }
/* 837 */     if ((entity instanceof EntityPlayer)) {
/* 838 */       if (!this.ni) {
/* 839 */         return false;
/*     */       }
/* 841 */       if (!isPlayerValid((EntityPlayer)entity)) {
/* 842 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 846 */     if ((entity.isInvisible()) && 
/* 847 */       (!this.ni))
/*     */     {
/*     */ 
/* 850 */       return false;
/*     */     }
/* 852 */     if ((entity instanceof net.minecraft.entity.monster.EntityMob)) {
/* 853 */       if (this.ye) {
/* 854 */         return true;
/*     */       }
/* 856 */       return false; }
/* 857 */     if ((entity instanceof EntityAnimal)) {
/* 858 */       if (this.ye) {
/* 859 */         return true;
/*     */       }
/* 861 */       return false; }
/* 862 */     if (!(entity instanceof EntityPlayer)) {
/* 863 */       if (this.ni) {
/* 864 */         return true;
/*     */       }
/* 866 */       return false;
/*     */     }
/* 868 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEntityValid(EntityLivingBase entity) {
/* 872 */     return (entity != null) && (entity != mc.thePlayer) && (isEntityValidType(entity)) && (isEntityInFov(entity)) && 
/* 873 */       (entity.isEntityAlive()) && (!rip.jutting.polaris.friend.FriendManager.isFriend(entity.getName()));
/*     */   }
/*     */   
/*     */   private boolean teamCheck(EntityLivingBase player) {
/* 877 */     player = (EntityPlayer)player;
/* 878 */     boolean md = true;
/* 879 */     return !mc.thePlayer.isOnSameTeam(player);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\TPAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */