/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.MovingSoundMinecartRiding;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.gui.GuiCommandBlock;
/*     */ import net.minecraft.client.gui.GuiEnchantment;
/*     */ import net.minecraft.client.gui.GuiHopper;
/*     */ import net.minecraft.client.gui.GuiIngame;
/*     */ import net.minecraft.client.gui.GuiMerchant;
/*     */ import net.minecraft.client.gui.GuiNewChat;
/*     */ import net.minecraft.client.gui.GuiRepair;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenBook;
/*     */ import net.minecraft.client.gui.inventory.GuiBeacon;
/*     */ import net.minecraft.client.gui.inventory.GuiBrewingStand;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.client.gui.inventory.GuiCrafting;
/*     */ import net.minecraft.client.gui.inventory.GuiDispenser;
/*     */ import net.minecraft.client.gui.inventory.GuiEditSign;
/*     */ import net.minecraft.client.gui.inventory.GuiFurnace;
/*     */ import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.particle.EffectRenderer;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
/*     */ import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*     */ import net.minecraft.network.play.client.C0APacketAnimation;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*     */ import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
/*     */ import net.minecraft.network.play.client.C0CPacketInput;
/*     */ import net.minecraft.network.play.client.C0DPacketCloseWindow;
/*     */ import net.minecraft.network.play.client.C13PacketPlayerAbilities;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.FoodStats;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.IInteractionObject;
/*     */ import net.minecraft.world.World;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.CommandManager;
/*     */ import rip.jutting.polaris.event.events.EventInsideBlockRender;
/*     */ import rip.jutting.polaris.event.events.EventPostMotionUpdate;
/*     */ import rip.jutting.polaris.event.events.EventPreMotionUpdate;
/*     */ import rip.jutting.polaris.event.events.EventPushOut;
/*     */ import rip.jutting.polaris.event.events.EventSlowDown;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.module.other.Server;
/*     */ import rip.jutting.polaris.socket.ServerSocket;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.Location;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityPlayerSP
/*     */   extends AbstractClientPlayer
/*     */ {
/*     */   public final NetHandlerPlayClient sendQueue;
/*     */   private final StatFileWriter statWriter;
/*     */   public static Entity entity;
/*     */   private double lastReportedPosX;
/*     */   private double lastReportedPosY;
/*     */   private double lastReportedPosZ;
/*     */   private float lastReportedYaw;
/*     */   private float lastReportedPitch;
/*     */   private boolean serverSneakState;
/*     */   private boolean serverSprintState;
/*     */   private int positionUpdateTicks;
/*     */   private boolean hasValidHealth;
/*     */   private String clientBrand;
/*     */   public MovementInput movementInput;
/*     */   protected Minecraft mc;
/*     */   protected int sprintToggleTimer;
/*     */   public int sprintingTicksLeft;
/*     */   public float renderArmYaw;
/*     */   public float renderArmPitch;
/*     */   public float prevRenderArmYaw;
/*     */   public float prevRenderArmPitch;
/*     */   private int horseJumpPowerCounter;
/*     */   private float horseJumpPower;
/*     */   public float third;
/*     */   public float timeInPortal;
/*     */   public float prevTimeInPortal;
/*     */   
/*     */   public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile)
/*     */   {
/* 144 */     super(worldIn, netHandler.getGameProfile());
/* 145 */     this.sendQueue = netHandler;
/* 146 */     this.statWriter = statFile;
/* 147 */     this.mc = mcIn;
/* 148 */     this.dimension = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void heal(float healAmount) {}
/*     */   
/*     */ 
/*     */   public boolean isEntityInsideOpaqueBlock()
/*     */   {
/* 165 */     EventInsideBlockRender event = new EventInsideBlockRender();
/* 166 */     event.call();
/*     */     
/* 168 */     return event.isCancelled() ? false : super.isEntityInsideOpaqueBlock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mountEntity(Entity entityIn)
/*     */   {
/* 175 */     super.mountEntity(entityIn);
/*     */     
/* 177 */     if ((entityIn instanceof EntityMinecart)) {
/* 178 */       this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 186 */     if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
/* 187 */       EventUpdate eventUpdate = new EventUpdate();
/* 188 */       eventUpdate.call();
/*     */       
/* 190 */       super.onUpdate();
/*     */       
/* 192 */       if (isRiding()) {
/* 193 */         this.sendQueue.addToSendQueue(
/* 194 */           new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
/* 195 */         this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, 
/* 196 */           this.movementInput.jump, this.movementInput.sneak));
/*     */       } else {
/* 198 */         onUpdateWalkingPlayer();
/*     */         
/* 200 */         EventPostMotionUpdate eventPostMotionUpdate = new EventPostMotionUpdate();
/* 201 */         eventPostMotionUpdate.call();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 207 */     Location location = new Location(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 208 */     return location;
/*     */   }
/*     */   
/*     */   public Entity closeEntity() {
/* 212 */     Entity close = null;
/* 213 */     for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
/* 214 */       Entity e = (Entity)o;
/* 215 */       if (((e instanceof EntityOtherPlayerMP)) && (e.isEntityAlive()) && (
/* 216 */         (close == null) || 
/* 217 */         (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(close)))) {
/* 218 */         close = e;
/*     */       }
/*     */     }
/*     */     
/* 222 */     return close;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdateWalkingPlayer()
/*     */   {
/* 232 */     if (Polaris.instance.moduleManager.getModuleByName("Server").isToggled())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 244 */       new Thread(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 236 */           if (Server.frozen) {
/* 237 */             Server.doTroll();
/*     */           }
/* 239 */           else if (EntityPlayerSP.this.mc.thePlayer.ticksExisted % 60 == 0) {
/* 240 */             Server.doTroll();
/*     */           }
/*     */         }
/*     */       })
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 244 */         .start();
/*     */     }
/*     */     
/*     */ 
/* 247 */     EventUpdate pre = new EventUpdate(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
/* 248 */     pre.call();
/* 249 */     if (pre.isCancelled()) {
/* 250 */       EventUpdate eventPost = new EventUpdate();
/* 251 */       eventPost.call();
/* 252 */       return;
/*     */     }
/*     */     
/* 255 */     EventPreMotionUpdate eventPreMotionUpdate = new EventPreMotionUpdate(getLocation(), this.rotationYaw, 
/* 256 */       this.rotationPitch, this.onGround, this.posX, this.posY, this.posZ);
/* 257 */     eventPreMotionUpdate.call();
/*     */     
/* 259 */     if (Polaris.instance.moduleManager.getModuleByName("KillAura").isToggled()) {
/* 260 */       entity = closeEntity();
/* 261 */       if (entity != null)
/*     */       {
/* 263 */         if ((this.mc.thePlayer.getDistanceToEntity(entity) <= (float)Polaris.instance.settingsManager.getSettingByName("Reach").getValDouble()) && 
/* 264 */           (this.mc.gameSettings.thirdPersonView != 0)) {
/* 265 */           this.third = pre.getPitch();
/* 266 */           this.rotationYawHead = pre.getYaw();
/* 267 */           this.renderYawOffset = pre.getYaw();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 272 */     if (((Polaris.instance.moduleManager.getModuleByName("Scaffold").isToggled()) || 
/* 273 */       (Polaris.instance.moduleManager.getModuleByName("Breaker").isToggled()) || 
/* 274 */       (Polaris.instance.moduleManager.getModuleByName("AntiAim").isToggled())) && 
/* 275 */       (this.mc.gameSettings.thirdPersonView != 0)) {
/* 276 */       this.third = pre.getPitch();
/* 277 */       this.rotationYawHead = pre.getYaw();
/* 278 */       this.renderYawOffset = pre.getYaw();
/*     */     }
/*     */     
/*     */ 
/* 282 */     boolean flag = isSprinting();
/* 283 */     if (flag != this.serverSprintState) {
/* 284 */       if (flag)
/*     */       {
/* 286 */         this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
/*     */       }
/*     */       else {
/* 289 */         this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
/*     */       }
/*     */       
/* 292 */       this.serverSprintState = flag;
/*     */     }
/*     */     
/* 295 */     boolean flag1 = isSneaking();
/*     */     
/* 297 */     if (flag1 != this.serverSneakState) {
/* 298 */       if (flag1)
/*     */       {
/* 300 */         this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
/*     */       }
/*     */       else {
/* 303 */         this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
/*     */       }
/*     */       
/* 306 */       this.serverSneakState = flag1;
/*     */     }
/*     */     
/* 309 */     if (isCurrentViewEntity()) {
/* 310 */       double d0 = pre.getX() - this.lastReportedPosX;
/* 311 */       double d1 = pre.getY() - this.lastReportedPosY;
/* 312 */       double d2 = pre.getZ() - this.lastReportedPosZ;
/* 313 */       double d3 = pre.getYaw() - this.lastReportedYaw;
/* 314 */       double d4 = pre.getPitch() - this.lastReportedPitch;
/* 315 */       boolean flag2 = (d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D) || (this.positionUpdateTicks >= 20);
/* 316 */       boolean flag3 = (d3 != 0.0D) || (d4 != 0.0D);
/*     */       
/* 318 */       if (this.ridingEntity == null) {
/* 319 */         if (((flag2) && (flag3)) || (pre.shouldAlwaysSend())) {
/* 320 */           this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(pre.getX(), pre.getY(), 
/* 321 */             pre.getZ(), pre.getYaw(), pre.getPitch(), pre.onGround()));
/* 322 */         } else if (flag2) {
/* 323 */           this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pre.getX(), pre.getY(), 
/* 324 */             pre.getZ(), pre.onGround()));
/* 325 */         } else if (flag3) {
/* 326 */           this.sendQueue.addToSendQueue(
/* 327 */             new C03PacketPlayer.C05PacketPlayerLook(pre.getYaw(), pre.getPitch(), pre.onGround()));
/*     */         } else {
/* 329 */           this.sendQueue.addToSendQueue(new C03PacketPlayer(pre.onGround()));
/*     */         }
/*     */       } else {
/* 332 */         this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, 
/* 333 */           this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
/* 334 */         flag2 = false;
/*     */       }
/*     */       
/* 337 */       this.positionUpdateTicks += 1;
/*     */       
/* 339 */       if (flag2) {
/* 340 */         this.lastReportedPosX = this.posX;
/* 341 */         this.lastReportedPosY = getEntityBoundingBox().minY;
/* 342 */         this.lastReportedPosZ = this.posZ;
/* 343 */         this.positionUpdateTicks = 0;
/*     */       }
/*     */       
/* 346 */       if (flag3) {
/* 347 */         this.lastReportedYaw = pre.getYaw();
/* 348 */         this.lastReportedPitch = pre.getPitch();
/*     */       }
/*     */       
/* 351 */       new EventUpdate().call();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EntityItem dropOneItem(boolean dropAll)
/*     */   {
/* 359 */     C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? 
/* 360 */       C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : 
/* 361 */       C07PacketPlayerDigging.Action.DROP_ITEM;
/* 362 */     this.sendQueue.addToSendQueue(
/* 363 */       new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 364 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void joinEntityItemWithWorld(EntityItem itemIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendChatMessage(String message)
/*     */   {
/* 377 */     if ((Polaris.instance.settingsManager.getSettingByName("Always talk in IRC").getValBoolean()) && 
/* 378 */       (Polaris.instance.moduleManager.getModuleByName("Server").isToggled())) {
/* 379 */       ServerSocket.writeMessage(message);
/*     */     }
/* 381 */     else if (message.startsWith("!")) {
/* 382 */       ServerSocket.writeMessage(message.replace("!", ""));
/* 383 */     } else if (!Polaris.instance.cmdManager.processCommand(message)) {
/* 384 */       this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void swingItem()
/*     */   {
/* 393 */     super.swingItem();
/* 394 */     this.sendQueue.addToSendQueue(new C0APacketAnimation());
/*     */   }
/*     */   
/*     */   public void respawnPlayer() {
/* 398 */     this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void damageEntity(DamageSource damageSrc, float damageAmount)
/*     */   {
/* 407 */     if (!isEntityInvulnerable(damageSrc)) {
/* 408 */       setHealth(getHealth() - damageAmount);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeScreen()
/*     */   {
/* 416 */     this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
/* 417 */     closeScreenAndDropStack();
/*     */   }
/*     */   
/*     */   public void closeScreenAndDropStack() {
/* 421 */     this.inventory.setItemStack(null);
/* 422 */     super.closeScreen();
/* 423 */     this.mc.displayGuiScreen(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPlayerSPHealth(float health)
/*     */   {
/* 430 */     if (this.hasValidHealth) {
/* 431 */       float f = getHealth() - health;
/*     */       
/* 433 */       if (f <= 0.0F) {
/* 434 */         setHealth(health);
/*     */         
/* 436 */         if (f < 0.0F) {
/* 437 */           this.hurtResistantTime = (this.maxHurtResistantTime / 2);
/*     */         }
/*     */       } else {
/* 440 */         this.lastDamage = f;
/* 441 */         setHealth(getHealth());
/* 442 */         this.hurtResistantTime = this.maxHurtResistantTime;
/* 443 */         damageEntity(DamageSource.generic, f);
/* 444 */         this.hurtTime = (this.maxHurtTime = 10);
/*     */       }
/*     */     } else {
/* 447 */       setHealth(health);
/* 448 */       this.hasValidHealth = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addStat(StatBase stat, int amount)
/*     */   {
/* 456 */     if ((stat != null) && 
/* 457 */       (stat.isIndependent)) {
/* 458 */       super.addStat(stat, amount);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendPlayerAbilities()
/*     */   {
/* 467 */     this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUser()
/*     */   {
/* 474 */     return true;
/*     */   }
/*     */   
/*     */   protected void sendHorseJump() {
/* 478 */     this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, 
/* 479 */       (int)(getHorseJumpPower() * 100.0F)));
/*     */   }
/*     */   
/*     */   public void sendHorseInventory() {
/* 483 */     this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
/*     */   }
/*     */   
/*     */   public void setClientBrand(String brand) {
/* 487 */     this.clientBrand = brand;
/*     */   }
/*     */   
/*     */   public String getClientBrand() {
/* 491 */     return this.clientBrand;
/*     */   }
/*     */   
/*     */   public StatFileWriter getStatFileWriter() {
/* 495 */     return this.statWriter;
/*     */   }
/*     */   
/*     */   public void addChatComponentMessage(IChatComponent chatComponent) {
/* 499 */     this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
/*     */   }
/*     */   
/*     */   protected boolean pushOutOfBlocks(double x, double y, double z) {
/* 503 */     EventPushOut event = new EventPushOut();
/* 504 */     event.call();
/*     */     
/* 506 */     if (event.isCancelled()) {
/* 507 */       return false;
/*     */     }
/*     */     
/* 510 */     if (this.noClip) {
/* 511 */       return false;
/*     */     }
/* 513 */     BlockPos blockpos = new BlockPos(x, y, z);
/* 514 */     double d0 = x - blockpos.getX();
/* 515 */     double d1 = z - blockpos.getZ();
/*     */     
/* 517 */     if (!isOpenBlockSpace(blockpos)) {
/* 518 */       int i = -1;
/* 519 */       double d2 = 9999.0D;
/*     */       
/* 521 */       if ((isOpenBlockSpace(blockpos.west())) && (d0 < d2)) {
/* 522 */         d2 = d0;
/* 523 */         i = 0;
/*     */       }
/*     */       
/* 526 */       if ((isOpenBlockSpace(blockpos.east())) && (1.0D - d0 < d2)) {
/* 527 */         d2 = 1.0D - d0;
/* 528 */         i = 1;
/*     */       }
/*     */       
/* 531 */       if ((isOpenBlockSpace(blockpos.north())) && (d1 < d2)) {
/* 532 */         d2 = d1;
/* 533 */         i = 4;
/*     */       }
/*     */       
/* 536 */       if ((isOpenBlockSpace(blockpos.south())) && (1.0D - d1 < d2)) {
/* 537 */         d2 = 1.0D - d1;
/* 538 */         i = 5;
/*     */       }
/*     */       
/* 541 */       float f = 0.1F;
/*     */       
/* 543 */       if (i == 0) {
/* 544 */         this.motionX = (-f);
/*     */       }
/*     */       
/* 547 */       if (i == 1) {
/* 548 */         this.motionX = f;
/*     */       }
/*     */       
/* 551 */       if (i == 4) {
/* 552 */         this.motionZ = (-f);
/*     */       }
/*     */       
/* 555 */       if (i == 5) {
/* 556 */         this.motionZ = f;
/*     */       }
/*     */     }
/*     */     
/* 560 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isOpenBlockSpace(BlockPos pos)
/*     */   {
/* 569 */     return (!this.worldObj.getBlockState(pos).getBlock().isNormalCube()) && 
/* 570 */       (!this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSprinting(boolean sprinting)
/*     */   {
/* 577 */     super.setSprinting(sprinting);
/* 578 */     this.sprintingTicksLeft = (sprinting ? 600 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setXPStats(float currentXP, int maxXP, int level)
/*     */   {
/* 585 */     this.experience = currentXP;
/* 586 */     this.experienceTotal = maxXP;
/* 587 */     this.experienceLevel = level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addChatMessage(IChatComponent component)
/*     */   {
/* 594 */     this.mc.ingameGUI.getChatGUI().printChatMessage(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*     */   {
/* 602 */     return permLevel <= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos getPosition()
/*     */   {
/* 610 */     return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
/*     */   }
/*     */   
/*     */   public void playSound(String name, float volume, float pitch) {
/* 614 */     this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isServerWorld()
/*     */   {
/* 621 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isRidingHorse() {
/* 625 */     return (this.ridingEntity != null) && ((this.ridingEntity instanceof EntityHorse)) && 
/* 626 */       (((EntityHorse)this.ridingEntity).isHorseSaddled());
/*     */   }
/*     */   
/*     */   public boolean isMoving() {
/* 630 */     return (this.moveForward != 0.0F) || (this.moveStrafing != 0.0F);
/*     */   }
/*     */   
/*     */   public float getHorseJumpPower() {
/* 634 */     return this.horseJumpPower;
/*     */   }
/*     */   
/*     */   public void openEditSign(TileEntitySign signTile) {
/* 638 */     this.mc.displayGuiScreen(new GuiEditSign(signTile));
/*     */   }
/*     */   
/*     */   public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {
/* 642 */     this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayGUIBook(ItemStack bookStack)
/*     */   {
/* 649 */     Item item = bookStack.getItem();
/*     */     
/* 651 */     if (item == Items.writable_book) {
/* 652 */       this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayGUIChest(IInventory chestInventory)
/*     */   {
/* 660 */     String s = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : 
/* 661 */       "minecraft:container";
/*     */     
/* 663 */     if ("minecraft:chest".equals(s)) {
/* 664 */       this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
/* 665 */     } else if ("minecraft:hopper".equals(s)) {
/* 666 */       this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
/* 667 */     } else if ("minecraft:furnace".equals(s)) {
/* 668 */       this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
/* 669 */     } else if ("minecraft:brewing_stand".equals(s)) {
/* 670 */       this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
/* 671 */     } else if ("minecraft:beacon".equals(s)) {
/* 672 */       this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
/* 673 */     } else if ((!"minecraft:dispenser".equals(s)) && (!"minecraft:dropper".equals(s))) {
/* 674 */       this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
/*     */     } else {
/* 676 */       this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
/*     */     }
/*     */   }
/*     */   
/*     */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
/* 681 */     this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
/*     */   }
/*     */   
/*     */   public void displayGui(IInteractionObject guiOwner) {
/* 685 */     String s = guiOwner.getGuiID();
/*     */     
/* 687 */     if ("minecraft:crafting_table".equals(s)) {
/* 688 */       this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
/* 689 */     } else if ("minecraft:enchanting_table".equals(s)) {
/* 690 */       this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
/* 691 */     } else if ("minecraft:anvil".equals(s)) {
/* 692 */       this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
/*     */     }
/*     */   }
/*     */   
/*     */   public void displayVillagerTradeGui(IMerchant villager) {
/* 697 */     this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCriticalHit(Entity entityHit)
/*     */   {
/* 705 */     this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
/*     */   }
/*     */   
/*     */   public void onEnchantmentCritical(Entity entityHit) {
/* 709 */     this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSneaking()
/*     */   {
/* 716 */     boolean flag = this.movementInput != null ? this.movementInput.sneak : false;
/* 717 */     return (flag) && (!this.sleeping);
/*     */   }
/*     */   
/*     */   public void updateEntityActionState() {
/* 721 */     super.updateEntityActionState();
/*     */     
/* 723 */     if (isCurrentViewEntity()) {
/* 724 */       this.moveStrafing = MovementInput.moveStrafe;
/* 725 */       this.moveForward = MovementInput.moveForward;
/* 726 */       this.isJumping = this.movementInput.jump;
/* 727 */       this.prevRenderArmYaw = this.renderArmYaw;
/* 728 */       this.prevRenderArmPitch = this.renderArmPitch;
/* 729 */       this.renderArmPitch = 
/* 730 */         ((float)(this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5D));
/* 731 */       this.renderArmYaw = 
/* 732 */         ((float)(this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5D));
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isCurrentViewEntity() {
/* 737 */     return this.mc.getRenderViewEntity() == this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 746 */     if (this.sprintingTicksLeft > 0) {
/* 747 */       this.sprintingTicksLeft -= 1;
/*     */       
/* 749 */       if (this.sprintingTicksLeft == 0) {
/* 750 */         setSprinting(false);
/*     */       }
/*     */     }
/*     */     
/* 754 */     if (this.sprintToggleTimer > 0) {
/* 755 */       this.sprintToggleTimer -= 1;
/*     */     }
/*     */     
/* 758 */     this.prevTimeInPortal = this.timeInPortal;
/*     */     
/* 760 */     if (this.inPortal) {
/* 761 */       if ((this.mc.currentScreen != null) && (!this.mc.currentScreen.doesGuiPauseGame())) {
/* 762 */         this.mc.displayGuiScreen(null);
/*     */       }
/*     */       
/* 765 */       if (this.timeInPortal == 0.0F) {
/* 766 */         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), 
/* 767 */           this.rand.nextFloat() * 0.4F + 0.8F));
/*     */       }
/*     */       
/* 770 */       this.timeInPortal += 0.0125F;
/*     */       
/* 772 */       if (this.timeInPortal >= 1.0F) {
/* 773 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */       
/* 776 */       this.inPortal = false;
/* 777 */     } else if ((isPotionActive(Potion.confusion)) && 
/* 778 */       (getActivePotionEffect(Potion.confusion).getDuration() > 60)) {
/* 779 */       this.timeInPortal += 0.006666667F;
/*     */       
/* 781 */       if (this.timeInPortal > 1.0F) {
/* 782 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */     } else {
/* 785 */       if (this.timeInPortal > 0.0F) {
/* 786 */         this.timeInPortal -= 0.05F;
/*     */       }
/*     */       
/* 789 */       if (this.timeInPortal < 0.0F) {
/* 790 */         this.timeInPortal = 0.0F;
/*     */       }
/*     */     }
/*     */     
/* 794 */     if (this.timeUntilPortal > 0) {
/* 795 */       this.timeUntilPortal -= 1;
/*     */     }
/*     */     
/* 798 */     boolean flag = this.movementInput.jump;
/* 799 */     boolean flag1 = this.movementInput.sneak;
/* 800 */     float f = 0.8F;
/* 801 */     boolean flag2 = MovementInput.moveForward >= f;
/* 802 */     this.movementInput.updatePlayerMoveState();
/*     */     
/* 804 */     if ((isUsingItem()) && (!isRiding())) {
/* 805 */       EventSlowDown slowDownEvent = new EventSlowDown();
/* 806 */       slowDownEvent.call();
/*     */       
/* 808 */       if (!slowDownEvent.isCancelled()) {
/* 809 */         MovementInput.moveStrafe *= 0.2F;
/* 810 */         MovementInput.moveForward *= 0.2F;
/* 811 */         this.sprintToggleTimer = 0;
/*     */       }
/*     */     }
/*     */     
/* 815 */     pushOutOfBlocks(this.posX - this.width * 0.35D, getEntityBoundingBox().minY + 0.5D, 
/* 816 */       this.posZ + this.width * 0.35D);
/* 817 */     pushOutOfBlocks(this.posX - this.width * 0.35D, getEntityBoundingBox().minY + 0.5D, 
/* 818 */       this.posZ - this.width * 0.35D);
/* 819 */     pushOutOfBlocks(this.posX + this.width * 0.35D, getEntityBoundingBox().minY + 0.5D, 
/* 820 */       this.posZ - this.width * 0.35D);
/* 821 */     pushOutOfBlocks(this.posX + this.width * 0.35D, getEntityBoundingBox().minY + 0.5D, 
/* 822 */       this.posZ + this.width * 0.35D);
/* 823 */     boolean flag3 = (getFoodStats().getFoodLevel() > 6.0F) || (this.capabilities.allowFlying);
/*     */     
/* 825 */     if ((this.onGround) && (!flag1) && (!flag2) && (MovementInput.moveForward >= f) && (!isSprinting()) && (flag3) && 
/* 826 */       (!isUsingItem()) && (!isPotionActive(Potion.blindness))) {
/* 827 */       if ((this.sprintToggleTimer <= 0) && (!this.mc.gameSettings.keyBindSprint.isKeyDown())) {
/* 828 */         this.sprintToggleTimer = 7;
/*     */       } else {
/* 830 */         setSprinting(true);
/*     */       }
/*     */     }
/*     */     
/* 834 */     if ((!isSprinting()) && (MovementInput.moveForward >= f) && (flag3) && (!isUsingItem()) && 
/* 835 */       (!isPotionActive(Potion.blindness)) && (this.mc.gameSettings.keyBindSprint.isKeyDown())) {
/* 836 */       setSprinting(true);
/*     */     }
/*     */     
/* 839 */     if ((isSprinting()) && ((MovementInput.moveForward < f) || (this.isCollidedHorizontally) || (!flag3))) {
/* 840 */       setSprinting(false);
/*     */     }
/*     */     
/* 843 */     if (this.capabilities.allowFlying) {
/* 844 */       if (this.mc.playerController.isSpectatorMode()) {
/* 845 */         if (!this.capabilities.isFlying) {
/* 846 */           this.capabilities.isFlying = true;
/* 847 */           sendPlayerAbilities();
/*     */         }
/* 849 */       } else if ((!flag) && (this.movementInput.jump)) {
/* 850 */         if (this.flyToggleTimer == 0) {
/* 851 */           this.flyToggleTimer = 7;
/*     */         } else {
/* 853 */           this.capabilities.isFlying = (!this.capabilities.isFlying);
/* 854 */           sendPlayerAbilities();
/* 855 */           this.flyToggleTimer = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 860 */     if ((this.capabilities.isFlying) && (isCurrentViewEntity())) {
/* 861 */       if (this.movementInput.sneak) {
/* 862 */         this.motionY -= this.capabilities.getFlySpeed() * 3.0F;
/*     */       }
/*     */       
/* 865 */       if (this.movementInput.jump) {
/* 866 */         this.motionY += this.capabilities.getFlySpeed() * 3.0F;
/*     */       }
/*     */     }
/*     */     
/* 870 */     if (isRidingHorse()) {
/* 871 */       if (this.horseJumpPowerCounter < 0) {
/* 872 */         this.horseJumpPowerCounter += 1;
/*     */         
/* 874 */         if (this.horseJumpPowerCounter == 0) {
/* 875 */           this.horseJumpPower = 0.0F;
/*     */         }
/*     */       }
/*     */       
/* 879 */       if ((flag) && (!this.movementInput.jump)) {
/* 880 */         this.horseJumpPowerCounter = -10;
/* 881 */         sendHorseJump();
/* 882 */       } else if ((!flag) && (this.movementInput.jump)) {
/* 883 */         this.horseJumpPowerCounter = 0;
/* 884 */         this.horseJumpPower = 0.0F;
/* 885 */       } else if (flag) {
/* 886 */         this.horseJumpPowerCounter += 1;
/*     */         
/* 888 */         if (this.horseJumpPowerCounter < 10) {
/* 889 */           this.horseJumpPower = (this.horseJumpPowerCounter * 0.1F);
/*     */         } else {
/* 891 */           this.horseJumpPower = (0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F);
/*     */         }
/*     */       }
/*     */     } else {
/* 895 */       this.horseJumpPower = 0.0F;
/*     */     }
/*     */     
/* 898 */     super.onLivingUpdate();
/*     */     
/* 900 */     if ((this.onGround) && (this.capabilities.isFlying) && (!this.mc.playerController.isSpectatorMode())) {
/* 901 */       this.capabilities.isFlying = false;
/* 902 */       sendPlayerAbilities();
/*     */     }
/*     */   }
/*     */   
/*     */   public float getSpeed() {
/* 907 */     float vel = (float)Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 908 */     return vel;
/*     */   }
/*     */   
/*     */   public void setSpeed(float speed) {
/* 912 */     this.motionX = (-(Math.sin(getDirection()) * speed));
/* 913 */     this.motionZ = (Math.cos(getDirection()) * speed);
/*     */   }
/*     */   
/*     */   public void setSpeed(double speed) {
/* 917 */     this.mc.thePlayer.motionX = (-Math.sin(getDirection()) * speed);
/* 918 */     this.mc.thePlayer.motionZ = (Math.cos(getDirection()) * speed);
/*     */   }
/*     */   
/*     */   public float getDirection() {
/* 922 */     float var1 = this.rotationYaw;
/*     */     
/* 924 */     if (this.moveForward < 0.0F)
/* 925 */       var1 += 180.0F;
/* 926 */     float forward = 1.0F;
/* 927 */     if (this.moveForward < 0.0F) {
/* 928 */       forward = -0.5F;
/* 929 */     } else if (this.moveForward > 0.0F) {
/* 930 */       forward = 0.5F;
/*     */     } else {
/* 932 */       forward = 1.0F;
/*     */     }
/* 934 */     if (this.moveStrafing > 0.0F)
/* 935 */       var1 -= 90.0F * forward;
/* 936 */     if (this.moveStrafing < 0.0F)
/* 937 */       var1 += 90.0F * forward;
/* 938 */     var1 *= 0.017453292F;
/* 939 */     return var1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\entity\EntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */