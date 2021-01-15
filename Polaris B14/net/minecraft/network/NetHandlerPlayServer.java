/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.primitives.Doubles;
/*      */ import com.google.common.primitives.Floats;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.command.ICommandManager;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.network.play.INetHandlerPlayServer;
/*      */ import net.minecraft.network.play.client.C00PacketKeepAlive;
/*      */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*      */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*      */ import net.minecraft.network.play.client.C02PacketUseEntity.Action;
/*      */ import net.minecraft.network.play.client.C03PacketPlayer;
/*      */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*      */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*      */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*      */ import net.minecraft.network.play.client.C09PacketHeldItemChange;
/*      */ import net.minecraft.network.play.client.C0APacketAnimation;
/*      */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*      */ import net.minecraft.network.play.client.C0CPacketInput;
/*      */ import net.minecraft.network.play.client.C0DPacketCloseWindow;
/*      */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*      */ import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
/*      */ import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
/*      */ import net.minecraft.network.play.client.C11PacketEnchantItem;
/*      */ import net.minecraft.network.play.client.C12PacketUpdateSign;
/*      */ import net.minecraft.network.play.client.C13PacketPlayerAbilities;
/*      */ import net.minecraft.network.play.client.C14PacketTabComplete;
/*      */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
/*      */ import net.minecraft.network.play.client.C18PacketSpectate;
/*      */ import net.minecraft.network.play.client.C19PacketResourcePackStatus;
/*      */ import net.minecraft.network.play.server.S00PacketKeepAlive;
/*      */ import net.minecraft.network.play.server.S02PacketChat;
/*      */ import net.minecraft.network.play.server.S07PacketRespawn;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook;
/*      */ import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
/*      */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*      */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S32PacketConfirmTransaction;
/*      */ import net.minecraft.network.play.server.S3APacketTabComplete;
/*      */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.ItemInWorldManager;
/*      */ import net.minecraft.server.management.ServerConfigurationManager;
/*      */ import net.minecraft.server.management.UserListBans;
/*      */ import net.minecraft.server.management.UserListBansEntry;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatisticsFile;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatAllowedCharacters;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.ITickable;
/*      */ import net.minecraft.util.IntHashMap;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
/*      */ {
/*  100 */   private static final Logger logger = ;
/*      */   
/*      */   public final NetworkManager netManager;
/*      */   
/*      */   private final MinecraftServer serverController;
/*      */   
/*      */   public EntityPlayerMP playerEntity;
/*      */   
/*      */   private int networkTickCount;
/*      */   
/*      */   private int field_175090_f;
/*      */   
/*      */   private int floatingTickCount;
/*      */   
/*      */   private boolean field_147366_g;
/*      */   
/*      */   private int field_147378_h;
/*      */   
/*      */   private long lastPingTime;
/*      */   
/*      */   private long lastSentPingPacket;
/*      */   private int chatSpamThresholdCount;
/*      */   private int itemDropThreshold;
/*  123 */   private IntHashMap<Short> field_147372_n = new IntHashMap();
/*      */   private double lastPosX;
/*      */   private double lastPosY;
/*      */   private double lastPosZ;
/*  127 */   private boolean hasMoved = true;
/*      */   
/*      */   public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn)
/*      */   {
/*  131 */     this.serverController = server;
/*  132 */     this.netManager = networkManagerIn;
/*  133 */     networkManagerIn.setNetHandler(this);
/*  134 */     this.playerEntity = playerIn;
/*  135 */     playerIn.playerNetServerHandler = this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void update()
/*      */   {
/*  143 */     this.field_147366_g = false;
/*  144 */     this.networkTickCount += 1;
/*  145 */     this.serverController.theProfiler.startSection("keepAlive");
/*      */     
/*  147 */     if (this.networkTickCount - this.lastSentPingPacket > 40L)
/*      */     {
/*  149 */       this.lastSentPingPacket = this.networkTickCount;
/*  150 */       this.lastPingTime = currentTimeMillis();
/*  151 */       this.field_147378_h = ((int)this.lastPingTime);
/*  152 */       sendPacket(new S00PacketKeepAlive(this.field_147378_h));
/*      */     }
/*      */     
/*  155 */     this.serverController.theProfiler.endSection();
/*      */     
/*  157 */     if (this.chatSpamThresholdCount > 0)
/*      */     {
/*  159 */       this.chatSpamThresholdCount -= 1;
/*      */     }
/*      */     
/*  162 */     if (this.itemDropThreshold > 0)
/*      */     {
/*  164 */       this.itemDropThreshold -= 1;
/*      */     }
/*      */     
/*  167 */     if ((this.playerEntity.getLastActiveTime() > 0L) && (this.serverController.getMaxPlayerIdleMinutes() > 0) && (MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
/*      */     {
/*  169 */       kickPlayerFromServer("You have been idle for too long!");
/*      */     }
/*      */   }
/*      */   
/*      */   public NetworkManager getNetworkManager()
/*      */   {
/*  175 */     return this.netManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void kickPlayerFromServer(String reason)
/*      */   {
/*  183 */     final ChatComponentText chatcomponenttext = new ChatComponentText(reason);
/*  184 */     this.netManager.sendPacket(new S40PacketDisconnect(chatcomponenttext), new GenericFutureListener()
/*      */     {
/*      */       public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception
/*      */       {
/*  188 */         NetHandlerPlayServer.this.netManager.closeChannel(chatcomponenttext);
/*      */       }
/*  190 */     }, new GenericFutureListener[0]);
/*  191 */     this.netManager.disableAutoRead();
/*  192 */     Futures.getUnchecked(this.serverController.addScheduledTask(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  196 */         NetHandlerPlayServer.this.netManager.checkDisconnected();
/*      */       }
/*      */     }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processInput(C0CPacketInput packetIn)
/*      */   {
/*  207 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  208 */     this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
/*      */   }
/*      */   
/*      */   private boolean func_183006_b(C03PacketPlayer p_183006_1_)
/*      */   {
/*  213 */     return (!Doubles.isFinite(p_183006_1_.getPositionX())) || (!Doubles.isFinite(p_183006_1_.getPositionY())) || (!Doubles.isFinite(p_183006_1_.getPositionZ())) || (!Floats.isFinite(p_183006_1_.getPitch())) || (!Floats.isFinite(p_183006_1_.getYaw()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processPlayer(C03PacketPlayer packetIn)
/*      */   {
/*  221 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*      */     
/*  223 */     if (func_183006_b(packetIn))
/*      */     {
/*  225 */       kickPlayerFromServer("Invalid move packet received");
/*      */     }
/*      */     else
/*      */     {
/*  229 */       WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  230 */       this.field_147366_g = true;
/*      */       
/*  232 */       if (!this.playerEntity.playerConqueredTheEnd)
/*      */       {
/*  234 */         double d0 = this.playerEntity.posX;
/*  235 */         double d1 = this.playerEntity.posY;
/*  236 */         double d2 = this.playerEntity.posZ;
/*  237 */         double d3 = 0.0D;
/*  238 */         double d4 = packetIn.getPositionX() - this.lastPosX;
/*  239 */         double d5 = packetIn.getPositionY() - this.lastPosY;
/*  240 */         double d6 = packetIn.getPositionZ() - this.lastPosZ;
/*      */         
/*  242 */         if (packetIn.isMoving())
/*      */         {
/*  244 */           d3 = d4 * d4 + d5 * d5 + d6 * d6;
/*      */           
/*  246 */           if ((!this.hasMoved) && (d3 < 0.25D))
/*      */           {
/*  248 */             this.hasMoved = true;
/*      */           }
/*      */         }
/*      */         
/*  252 */         if (this.hasMoved)
/*      */         {
/*  254 */           this.field_175090_f = this.networkTickCount;
/*      */           
/*  256 */           if (this.playerEntity.ridingEntity != null)
/*      */           {
/*  258 */             float f4 = this.playerEntity.rotationYaw;
/*  259 */             float f = this.playerEntity.rotationPitch;
/*  260 */             this.playerEntity.ridingEntity.updateRiderPosition();
/*  261 */             double d16 = this.playerEntity.posX;
/*  262 */             double d17 = this.playerEntity.posY;
/*  263 */             double d18 = this.playerEntity.posZ;
/*      */             
/*  265 */             if (packetIn.getRotating())
/*      */             {
/*  267 */               f4 = packetIn.getYaw();
/*  268 */               f = packetIn.getPitch();
/*      */             }
/*      */             
/*  271 */             this.playerEntity.onGround = packetIn.isOnGround();
/*  272 */             this.playerEntity.onUpdateEntity();
/*  273 */             this.playerEntity.setPositionAndRotation(d16, d17, d18, f4, f);
/*      */             
/*  275 */             if (this.playerEntity.ridingEntity != null)
/*      */             {
/*  277 */               this.playerEntity.ridingEntity.updateRiderPosition();
/*      */             }
/*      */             
/*  280 */             this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
/*      */             
/*  282 */             if (this.playerEntity.ridingEntity != null)
/*      */             {
/*  284 */               if (d3 > 4.0D)
/*      */               {
/*  286 */                 Entity entity = this.playerEntity.ridingEntity;
/*  287 */                 this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(entity));
/*  288 */                 setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*      */               }
/*      */               
/*  291 */               this.playerEntity.ridingEntity.isAirBorne = true;
/*      */             }
/*      */             
/*  294 */             if (this.hasMoved)
/*      */             {
/*  296 */               this.lastPosX = this.playerEntity.posX;
/*  297 */               this.lastPosY = this.playerEntity.posY;
/*  298 */               this.lastPosZ = this.playerEntity.posZ;
/*      */             }
/*      */             
/*  301 */             worldserver.updateEntity(this.playerEntity);
/*  302 */             return;
/*      */           }
/*      */           
/*  305 */           if (this.playerEntity.isPlayerSleeping())
/*      */           {
/*  307 */             this.playerEntity.onUpdateEntity();
/*  308 */             this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*  309 */             worldserver.updateEntity(this.playerEntity);
/*  310 */             return;
/*      */           }
/*      */           
/*  313 */           double d7 = this.playerEntity.posY;
/*  314 */           this.lastPosX = this.playerEntity.posX;
/*  315 */           this.lastPosY = this.playerEntity.posY;
/*  316 */           this.lastPosZ = this.playerEntity.posZ;
/*  317 */           double d8 = this.playerEntity.posX;
/*  318 */           double d9 = this.playerEntity.posY;
/*  319 */           double d10 = this.playerEntity.posZ;
/*  320 */           float f1 = this.playerEntity.rotationYaw;
/*  321 */           float f2 = this.playerEntity.rotationPitch;
/*      */           
/*  323 */           if ((packetIn.isMoving()) && (packetIn.getPositionY() == -999.0D))
/*      */           {
/*  325 */             packetIn.setMoving(false);
/*      */           }
/*      */           
/*  328 */           if (packetIn.isMoving())
/*      */           {
/*  330 */             d8 = packetIn.getPositionX();
/*  331 */             d9 = packetIn.getPositionY();
/*  332 */             d10 = packetIn.getPositionZ();
/*      */             
/*  334 */             if ((Math.abs(packetIn.getPositionX()) > 3.0E7D) || (Math.abs(packetIn.getPositionZ()) > 3.0E7D))
/*      */             {
/*  336 */               kickPlayerFromServer("Illegal position");
/*  337 */               return;
/*      */             }
/*      */           }
/*      */           
/*  341 */           if (packetIn.getRotating())
/*      */           {
/*  343 */             f1 = packetIn.getYaw();
/*  344 */             f2 = packetIn.getPitch();
/*      */           }
/*      */           
/*  347 */           this.playerEntity.onUpdateEntity();
/*  348 */           this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);
/*      */           
/*  350 */           if (!this.hasMoved)
/*      */           {
/*  352 */             return;
/*      */           }
/*      */           
/*  355 */           double d11 = d8 - this.playerEntity.posX;
/*  356 */           double d12 = d9 - this.playerEntity.posY;
/*  357 */           double d13 = d10 - this.playerEntity.posZ;
/*  358 */           double d14 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
/*  359 */           double d15 = d11 * d11 + d12 * d12 + d13 * d13;
/*      */           
/*  361 */           if ((d15 - d14 > 100.0D) && ((!this.serverController.isSinglePlayer()) || (!this.serverController.getServerOwner().equals(this.playerEntity.getName()))))
/*      */           {
/*  363 */             logger.warn(this.playerEntity.getName() + " moved too quickly! " + d11 + "," + d12 + "," + d13 + " (" + d11 + ", " + d12 + ", " + d13 + ")");
/*  364 */             setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*  365 */             return;
/*      */           }
/*      */           
/*  368 */           float f3 = 0.0625F;
/*  369 */           boolean flag = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f3, f3, f3)).isEmpty();
/*      */           
/*  371 */           if ((this.playerEntity.onGround) && (!packetIn.isOnGround()) && (d12 > 0.0D))
/*      */           {
/*  373 */             this.playerEntity.jump();
/*      */           }
/*      */           
/*  376 */           this.playerEntity.moveEntity(d11, d12, d13);
/*  377 */           this.playerEntity.onGround = packetIn.isOnGround();
/*  378 */           d11 = d8 - this.playerEntity.posX;
/*  379 */           d12 = d9 - this.playerEntity.posY;
/*      */           
/*  381 */           if ((d12 > -0.5D) || (d12 < 0.5D))
/*      */           {
/*  383 */             d12 = 0.0D;
/*      */           }
/*      */           
/*  386 */           d13 = d10 - this.playerEntity.posZ;
/*  387 */           d15 = d11 * d11 + d12 * d12 + d13 * d13;
/*  388 */           boolean flag1 = false;
/*      */           
/*  390 */           if ((d15 > 0.0625D) && (!this.playerEntity.isPlayerSleeping()) && (!this.playerEntity.theItemInWorldManager.isCreative()))
/*      */           {
/*  392 */             flag1 = true;
/*  393 */             logger.warn(this.playerEntity.getName() + " moved wrongly!");
/*      */           }
/*      */           
/*  396 */           this.playerEntity.setPositionAndRotation(d8, d9, d10, f1, f2);
/*  397 */           this.playerEntity.addMovementStat(this.playerEntity.posX - d0, this.playerEntity.posY - d1, this.playerEntity.posZ - d2);
/*      */           
/*  399 */           if (!this.playerEntity.noClip)
/*      */           {
/*  401 */             boolean flag2 = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f3, f3, f3)).isEmpty();
/*      */             
/*  403 */             if ((flag) && ((flag1) || (!flag2)) && (!this.playerEntity.isPlayerSleeping()))
/*      */             {
/*  405 */               setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f1, f2);
/*  406 */               return;
/*      */             }
/*      */           }
/*      */           
/*  410 */           AxisAlignedBB axisalignedbb = this.playerEntity.getEntityBoundingBox().expand(f3, f3, f3).addCoord(0.0D, -0.55D, 0.0D);
/*      */           
/*  412 */           if ((!this.serverController.isFlightAllowed()) && (!this.playerEntity.capabilities.allowFlying) && (!worldserver.checkBlockCollision(axisalignedbb)))
/*      */           {
/*  414 */             if (d12 >= -0.03125D)
/*      */             {
/*  416 */               this.floatingTickCount += 1;
/*      */               
/*  418 */               if (this.floatingTickCount > 80)
/*      */               {
/*  420 */                 logger.warn(this.playerEntity.getName() + " was kicked for floating too long!");
/*  421 */                 kickPlayerFromServer("Flying is not enabled on this server");
/*      */               }
/*      */               
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/*  428 */             this.floatingTickCount = 0;
/*      */           }
/*      */           
/*  431 */           this.playerEntity.onGround = packetIn.isOnGround();
/*  432 */           this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
/*  433 */           this.playerEntity.handleFalling(this.playerEntity.posY - d7, packetIn.isOnGround());
/*      */         }
/*  435 */         else if (this.networkTickCount - this.field_175090_f > 20)
/*      */         {
/*  437 */           setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
/*      */   {
/*  445 */     setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
/*      */   }
/*      */   
/*      */   public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<S08PacketPlayerPosLook.EnumFlags> relativeSet)
/*      */   {
/*  450 */     this.hasMoved = false;
/*  451 */     this.lastPosX = x;
/*  452 */     this.lastPosY = y;
/*  453 */     this.lastPosZ = z;
/*      */     
/*  455 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X))
/*      */     {
/*  457 */       this.lastPosX += this.playerEntity.posX;
/*      */     }
/*      */     
/*  460 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y))
/*      */     {
/*  462 */       this.lastPosY += this.playerEntity.posY;
/*      */     }
/*      */     
/*  465 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Z))
/*      */     {
/*  467 */       this.lastPosZ += this.playerEntity.posZ;
/*      */     }
/*      */     
/*  470 */     float f = yaw;
/*  471 */     float f1 = pitch;
/*      */     
/*  473 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
/*      */     {
/*  475 */       f = yaw + this.playerEntity.rotationYaw;
/*      */     }
/*      */     
/*  478 */     if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
/*      */     {
/*  480 */       f1 = pitch + this.playerEntity.rotationPitch;
/*      */     }
/*      */     
/*  483 */     this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f, f1);
/*  484 */     this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processPlayerDigging(C07PacketPlayerDigging packetIn)
/*      */   {
/*  494 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  495 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  496 */     BlockPos blockpos = packetIn.getPosition();
/*  497 */     this.playerEntity.markPlayerActive();
/*      */     
/*  499 */     switch (packetIn.getStatus())
/*      */     {
/*      */     case START_DESTROY_BLOCK: 
/*  502 */       if (!this.playerEntity.isSpectator())
/*      */       {
/*  504 */         this.playerEntity.dropOneItem(false);
/*      */       }
/*      */       
/*  507 */       return;
/*      */     
/*      */     case RELEASE_USE_ITEM: 
/*  510 */       if (!this.playerEntity.isSpectator())
/*      */       {
/*  512 */         this.playerEntity.dropOneItem(true);
/*      */       }
/*      */       
/*  515 */       return;
/*      */     
/*      */     case STOP_DESTROY_BLOCK: 
/*  518 */       this.playerEntity.stopUsingItem();
/*  519 */       return;
/*      */     
/*      */     case ABORT_DESTROY_BLOCK: 
/*      */     case DROP_ALL_ITEMS: 
/*      */     case DROP_ITEM: 
/*  524 */       double d0 = this.playerEntity.posX - (blockpos.getX() + 0.5D);
/*  525 */       double d1 = this.playerEntity.posY - (blockpos.getY() + 0.5D) + 1.5D;
/*  526 */       double d2 = this.playerEntity.posZ - (blockpos.getZ() + 0.5D);
/*  527 */       double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */       
/*  529 */       if (d3 > 36.0D)
/*      */       {
/*  531 */         return;
/*      */       }
/*  533 */       if (blockpos.getY() >= this.serverController.getBuildLimit())
/*      */       {
/*  535 */         return;
/*      */       }
/*      */       
/*      */ 
/*  539 */       if (packetIn.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK)
/*      */       {
/*  541 */         if ((!this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity)) && (worldserver.getWorldBorder().contains(blockpos)))
/*      */         {
/*  543 */           this.playerEntity.theItemInWorldManager.onBlockClicked(blockpos, packetIn.getFacing());
/*      */         }
/*      */         else
/*      */         {
/*  547 */           this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  552 */         if (packetIn.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
/*      */         {
/*  554 */           this.playerEntity.theItemInWorldManager.blockRemoving(blockpos);
/*      */         }
/*  556 */         else if (packetIn.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
/*      */         {
/*  558 */           this.playerEntity.theItemInWorldManager.cancelDestroyingBlock();
/*      */         }
/*      */         
/*  561 */         if (worldserver.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*      */         {
/*  563 */           this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
/*      */         }
/*      */       }
/*      */       
/*  567 */       return;
/*      */     }
/*      */     
/*      */     
/*  571 */     throw new IllegalArgumentException("Invalid player action");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetIn)
/*      */   {
/*  580 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  581 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  582 */     ItemStack itemstack = this.playerEntity.inventory.getCurrentItem();
/*  583 */     boolean flag = false;
/*  584 */     BlockPos blockpos = packetIn.getPosition();
/*  585 */     EnumFacing enumfacing = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
/*  586 */     this.playerEntity.markPlayerActive();
/*      */     
/*  588 */     if (packetIn.getPlacedBlockDirection() == 255)
/*      */     {
/*  590 */       if (itemstack == null)
/*      */       {
/*  592 */         return;
/*      */       }
/*      */       
/*  595 */       this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, worldserver, itemstack);
/*      */     }
/*  597 */     else if ((blockpos.getY() < this.serverController.getBuildLimit() - 1) || ((enumfacing != EnumFacing.UP) && (blockpos.getY() < this.serverController.getBuildLimit())))
/*      */     {
/*  599 */       if ((this.hasMoved) && (this.playerEntity.getDistanceSq(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D) < 64.0D) && (!this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity)) && (worldserver.getWorldBorder().contains(blockpos)))
/*      */       {
/*  601 */         this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, worldserver, itemstack, blockpos, enumfacing, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
/*      */       }
/*      */       
/*  604 */       flag = true;
/*      */     }
/*      */     else
/*      */     {
/*  608 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("build.tooHigh", new Object[] { Integer.valueOf(this.serverController.getBuildLimit()) });
/*  609 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  610 */       this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(chatcomponenttranslation));
/*  611 */       flag = true;
/*      */     }
/*      */     
/*  614 */     if (flag)
/*      */     {
/*  616 */       this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
/*  617 */       this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos.offset(enumfacing)));
/*      */     }
/*      */     
/*  620 */     itemstack = this.playerEntity.inventory.getCurrentItem();
/*      */     
/*  622 */     if ((itemstack != null) && (itemstack.stackSize == 0))
/*      */     {
/*  624 */       this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
/*  625 */       itemstack = null;
/*      */     }
/*      */     
/*  628 */     if ((itemstack == null) || (itemstack.getMaxItemUseDuration() == 0))
/*      */     {
/*  630 */       this.playerEntity.isChangingQuantityOnly = true;
/*  631 */       this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
/*  632 */       Slot slot = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
/*  633 */       this.playerEntity.openContainer.detectAndSendChanges();
/*  634 */       this.playerEntity.isChangingQuantityOnly = false;
/*      */       
/*  636 */       if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack()))
/*      */       {
/*  638 */         sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, slot.slotNumber, this.playerEntity.inventory.getCurrentItem()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleSpectate(C18PacketSpectate packetIn)
/*      */   {
/*  645 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*      */     
/*  647 */     if (this.playerEntity.isSpectator())
/*      */     {
/*  649 */       Entity entity = null;
/*      */       WorldServer[] arrayOfWorldServer;
/*  651 */       int j = (arrayOfWorldServer = this.serverController.worldServers).length; for (int i = 0; i < j; i++) { WorldServer worldserver = arrayOfWorldServer[i];
/*      */         
/*  653 */         if (worldserver != null)
/*      */         {
/*  655 */           entity = packetIn.getEntity(worldserver);
/*      */           
/*  657 */           if (entity != null) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  664 */       if (entity != null)
/*      */       {
/*  666 */         this.playerEntity.setSpectatingEntity(this.playerEntity);
/*  667 */         this.playerEntity.mountEntity(null);
/*      */         
/*  669 */         if (entity.worldObj != this.playerEntity.worldObj)
/*      */         {
/*  671 */           WorldServer worldserver1 = this.playerEntity.getServerForPlayer();
/*  672 */           WorldServer worldserver2 = (WorldServer)entity.worldObj;
/*  673 */           this.playerEntity.dimension = entity.dimension;
/*  674 */           sendPacket(new S07PacketRespawn(this.playerEntity.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
/*  675 */           worldserver1.removePlayerEntityDangerously(this.playerEntity);
/*  676 */           this.playerEntity.isDead = false;
/*  677 */           this.playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*      */           
/*  679 */           if (this.playerEntity.isEntityAlive())
/*      */           {
/*  681 */             worldserver1.updateEntityWithOptionalForce(this.playerEntity, false);
/*  682 */             worldserver2.spawnEntityInWorld(this.playerEntity);
/*  683 */             worldserver2.updateEntityWithOptionalForce(this.playerEntity, false);
/*      */           }
/*      */           
/*  686 */           this.playerEntity.setWorld(worldserver2);
/*  687 */           this.serverController.getConfigurationManager().preparePlayer(this.playerEntity, worldserver1);
/*  688 */           this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
/*  689 */           this.playerEntity.theItemInWorldManager.setWorld(worldserver2);
/*  690 */           this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, worldserver2);
/*  691 */           this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
/*      */         }
/*      */         else
/*      */         {
/*  695 */           this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleResourcePackStatus(C19PacketResourcePackStatus packetIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void onDisconnect(IChatComponent reason)
/*      */   {
/*  710 */     logger.info(this.playerEntity.getName() + " lost connection: " + reason);
/*  711 */     this.serverController.refreshStatusNextTick();
/*  712 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.left", new Object[] { this.playerEntity.getDisplayName() });
/*  713 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
/*  714 */     this.serverController.getConfigurationManager().sendChatMsg(chatcomponenttranslation);
/*  715 */     this.playerEntity.mountEntityAndWakeUp();
/*  716 */     this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
/*      */     
/*  718 */     if ((this.serverController.isSinglePlayer()) && (this.playerEntity.getName().equals(this.serverController.getServerOwner())))
/*      */     {
/*  720 */       logger.info("Stopping singleplayer server as player logged out");
/*  721 */       this.serverController.initiateShutdown();
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendPacket(final Packet packetIn)
/*      */   {
/*  727 */     if ((packetIn instanceof S02PacketChat))
/*      */     {
/*  729 */       S02PacketChat s02packetchat = (S02PacketChat)packetIn;
/*  730 */       EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.playerEntity.getChatVisibility();
/*      */       
/*  732 */       if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN)
/*      */       {
/*  734 */         return;
/*      */       }
/*      */       
/*  737 */       if ((entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM) && (!s02packetchat.isChat()))
/*      */       {
/*  739 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  745 */       this.netManager.sendPacket(packetIn);
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/*  749 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
/*  750 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
/*  751 */       crashreportcategory.addCrashSectionCallable("Packet class", new Callable()
/*      */       {
/*      */         public String call() throws Exception
/*      */         {
/*  755 */           return packetIn.getClass().getCanonicalName();
/*      */         }
/*  757 */       });
/*  758 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processHeldItemChange(C09PacketHeldItemChange packetIn)
/*      */   {
/*  767 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*      */     
/*  769 */     if ((packetIn.getSlotId() >= 0) && (packetIn.getSlotId() < InventoryPlayer.getHotbarSize()))
/*      */     {
/*  771 */       this.playerEntity.inventory.currentItem = packetIn.getSlotId();
/*  772 */       this.playerEntity.markPlayerActive();
/*      */     }
/*      */     else
/*      */     {
/*  776 */       logger.warn(this.playerEntity.getName() + " tried to set an invalid carried item");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processChatMessage(C01PacketChatMessage packetIn)
/*      */   {
/*  785 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*      */     
/*  787 */     if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN)
/*      */     {
/*  789 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
/*  790 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  791 */       sendPacket(new S02PacketChat(chatcomponenttranslation));
/*      */     }
/*      */     else
/*      */     {
/*  795 */       this.playerEntity.markPlayerActive();
/*  796 */       String s = packetIn.getMessage();
/*  797 */       s = org.apache.commons.lang3.StringUtils.normalizeSpace(s);
/*      */       
/*  799 */       for (int i = 0; i < s.length(); i++)
/*      */       {
/*  801 */         if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
/*      */         {
/*  803 */           kickPlayerFromServer("Illegal characters in chat");
/*  804 */           return;
/*      */         }
/*      */       }
/*      */       
/*  808 */       if (s.startsWith("/"))
/*      */       {
/*  810 */         handleSlashCommand(s);
/*      */       }
/*      */       else
/*      */       {
/*  814 */         IChatComponent ichatcomponent = new ChatComponentTranslation("chat.type.text", new Object[] { this.playerEntity.getDisplayName(), s });
/*  815 */         this.serverController.getConfigurationManager().sendChatMsgImpl(ichatcomponent, false);
/*      */       }
/*      */       
/*  818 */       this.chatSpamThresholdCount += 20;
/*      */       
/*  820 */       if ((this.chatSpamThresholdCount > 200) && (!this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())))
/*      */       {
/*  822 */         kickPlayerFromServer("disconnect.spam");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleSlashCommand(String command)
/*      */   {
/*  832 */     this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
/*      */   }
/*      */   
/*      */   public void handleAnimation(C0APacketAnimation packetIn)
/*      */   {
/*  837 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  838 */     this.playerEntity.markPlayerActive();
/*  839 */     this.playerEntity.swingItem();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processEntityAction(C0BPacketEntityAction packetIn)
/*      */   {
/*  848 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  849 */     this.playerEntity.markPlayerActive();
/*      */     
/*  851 */     switch (packetIn.getAction())
/*      */     {
/*      */     case OPEN_INVENTORY: 
/*  854 */       this.playerEntity.setSneaking(true);
/*  855 */       break;
/*      */     
/*      */     case RIDING_JUMP: 
/*  858 */       this.playerEntity.setSneaking(false);
/*  859 */       break;
/*      */     
/*      */     case START_SPRINTING: 
/*  862 */       this.playerEntity.setSprinting(true);
/*  863 */       break;
/*      */     
/*      */     case STOP_SLEEPING: 
/*  866 */       this.playerEntity.setSprinting(false);
/*  867 */       break;
/*      */     
/*      */     case START_SNEAKING: 
/*  870 */       this.playerEntity.wakeUpPlayer(false, true, true);
/*  871 */       this.hasMoved = false;
/*  872 */       break;
/*      */     
/*      */     case STOP_SNEAKING: 
/*  875 */       if ((this.playerEntity.ridingEntity instanceof EntityHorse))
/*      */       {
/*  877 */         ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.getAuxData());
/*      */       }
/*      */       
/*  880 */       break;
/*      */     
/*      */     case STOP_SPRINTING: 
/*  883 */       if ((this.playerEntity.ridingEntity instanceof EntityHorse))
/*      */       {
/*  885 */         ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
/*      */       }
/*      */       
/*  888 */       break;
/*      */     
/*      */     default: 
/*  891 */       throw new IllegalArgumentException("Invalid client command!");
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processUseEntity(C02PacketUseEntity packetIn)
/*      */   {
/*  901 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  902 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/*  903 */     Entity entity = packetIn.getEntityFromWorld(worldserver);
/*  904 */     this.playerEntity.markPlayerActive();
/*      */     
/*  906 */     if (entity != null)
/*      */     {
/*  908 */       boolean flag = this.playerEntity.canEntityBeSeen(entity);
/*  909 */       double d0 = 36.0D;
/*      */       
/*  911 */       if (!flag)
/*      */       {
/*  913 */         d0 = 9.0D;
/*      */       }
/*      */       
/*  916 */       if (this.playerEntity.getDistanceSqToEntity(entity) < d0)
/*      */       {
/*  918 */         if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT)
/*      */         {
/*  920 */           this.playerEntity.interactWith(entity);
/*      */         }
/*  922 */         else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT)
/*      */         {
/*  924 */           entity.interactAt(this.playerEntity, packetIn.getHitVec());
/*      */         }
/*  926 */         else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK)
/*      */         {
/*  928 */           if (((entity instanceof EntityItem)) || ((entity instanceof EntityXPOrb)) || ((entity instanceof EntityArrow)) || (entity == this.playerEntity))
/*      */           {
/*  930 */             kickPlayerFromServer("Attempting to attack an invalid entity");
/*  931 */             this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
/*  932 */             return;
/*      */           }
/*      */           
/*  935 */           this.playerEntity.attackTargetEntityWithCurrentItem(entity);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processClientStatus(C16PacketClientStatus packetIn)
/*      */   {
/*  947 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  948 */     this.playerEntity.markPlayerActive();
/*  949 */     C16PacketClientStatus.EnumState c16packetclientstatus$enumstate = packetIn.getStatus();
/*      */     
/*  951 */     switch (c16packetclientstatus$enumstate)
/*      */     {
/*      */     case OPEN_INVENTORY_ACHIEVEMENT: 
/*  954 */       if (this.playerEntity.playerConqueredTheEnd)
/*      */       {
/*  956 */         this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
/*      */       }
/*  958 */       else if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled())
/*      */       {
/*  960 */         if ((this.serverController.isSinglePlayer()) && (this.playerEntity.getName().equals(this.serverController.getServerOwner())))
/*      */         {
/*  962 */           this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
/*  963 */           this.serverController.deleteWorldAndStopServer();
/*      */         }
/*      */         else
/*      */         {
/*  967 */           UserListBansEntry userlistbansentry = new UserListBansEntry(this.playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
/*  968 */           this.serverController.getConfigurationManager().getBannedPlayers().addEntry(userlistbansentry);
/*  969 */           this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  974 */         if (this.playerEntity.getHealth() > 0.0F)
/*      */         {
/*  976 */           return;
/*      */         }
/*      */         
/*  979 */         this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
/*      */       }
/*      */       
/*  982 */       break;
/*      */     
/*      */     case PERFORM_RESPAWN: 
/*  985 */       this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
/*  986 */       break;
/*      */     
/*      */     case REQUEST_STATS: 
/*  989 */       this.playerEntity.triggerAchievement(AchievementList.openInventory);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void processCloseWindow(C0DPacketCloseWindow packetIn)
/*      */   {
/*  998 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*  999 */     this.playerEntity.closeContainer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processClickWindow(C0EPacketClickWindow packetIn)
/*      */   {
/* 1009 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1010 */     this.playerEntity.markPlayerActive();
/*      */     
/* 1012 */     if ((this.playerEntity.openContainer.windowId == packetIn.getWindowId()) && (this.playerEntity.openContainer.getCanCraft(this.playerEntity)))
/*      */     {
/* 1014 */       if (this.playerEntity.isSpectator())
/*      */       {
/* 1016 */         List<ItemStack> list = Lists.newArrayList();
/*      */         
/* 1018 */         for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); i++)
/*      */         {
/* 1020 */           list.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(i)).getStack());
/*      */         }
/*      */         
/* 1023 */         this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list);
/*      */       }
/*      */       else
/*      */       {
/* 1027 */         ItemStack itemstack = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), this.playerEntity);
/*      */         
/* 1029 */         if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack))
/*      */         {
/* 1031 */           this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
/* 1032 */           this.playerEntity.isChangingQuantityOnly = true;
/* 1033 */           this.playerEntity.openContainer.detectAndSendChanges();
/* 1034 */           this.playerEntity.updateHeldItem();
/* 1035 */           this.playerEntity.isChangingQuantityOnly = false;
/*      */         }
/*      */         else
/*      */         {
/* 1039 */           this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
/* 1040 */           this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
/* 1041 */           this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
/* 1042 */           List<ItemStack> list1 = Lists.newArrayList();
/*      */           
/* 1044 */           for (int j = 0; j < this.playerEntity.openContainer.inventorySlots.size(); j++)
/*      */           {
/* 1046 */             list1.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(j)).getStack());
/*      */           }
/*      */           
/* 1049 */           this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processEnchantItem(C11PacketEnchantItem packetIn)
/*      */   {
/* 1061 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1062 */     this.playerEntity.markPlayerActive();
/*      */     
/* 1064 */     if ((this.playerEntity.openContainer.windowId == packetIn.getWindowId()) && (this.playerEntity.openContainer.getCanCraft(this.playerEntity)) && (!this.playerEntity.isSpectator()))
/*      */     {
/* 1066 */       this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
/* 1067 */       this.playerEntity.openContainer.detectAndSendChanges();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetIn)
/*      */   {
/* 1076 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/*      */     
/* 1078 */     if (this.playerEntity.theItemInWorldManager.isCreative())
/*      */     {
/* 1080 */       boolean flag = packetIn.getSlotId() < 0;
/* 1081 */       ItemStack itemstack = packetIn.getStack();
/*      */       
/* 1083 */       if ((itemstack != null) && (itemstack.hasTagCompound()) && (itemstack.getTagCompound().hasKey("BlockEntityTag", 10)))
/*      */       {
/* 1085 */         NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");
/*      */         
/* 1087 */         if ((nbttagcompound.hasKey("x")) && (nbttagcompound.hasKey("y")) && (nbttagcompound.hasKey("z")))
/*      */         {
/* 1089 */           BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
/* 1090 */           TileEntity tileentity = this.playerEntity.worldObj.getTileEntity(blockpos);
/*      */           
/* 1092 */           if (tileentity != null)
/*      */           {
/* 1094 */             NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1095 */             tileentity.writeToNBT(nbttagcompound1);
/* 1096 */             nbttagcompound1.removeTag("x");
/* 1097 */             nbttagcompound1.removeTag("y");
/* 1098 */             nbttagcompound1.removeTag("z");
/* 1099 */             itemstack.setTagInfo("BlockEntityTag", nbttagcompound1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1104 */       boolean flag1 = (packetIn.getSlotId() >= 1) && (packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize());
/* 1105 */       boolean flag2 = (itemstack == null) || (itemstack.getItem() != null);
/* 1106 */       boolean flag3 = (itemstack == null) || ((itemstack.getMetadata() >= 0) && (itemstack.stackSize <= 64) && (itemstack.stackSize > 0));
/*      */       
/* 1108 */       if ((flag1) && (flag2) && (flag3))
/*      */       {
/* 1110 */         if (itemstack == null)
/*      */         {
/* 1112 */           this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), null);
/*      */         }
/*      */         else
/*      */         {
/* 1116 */           this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
/*      */         }
/*      */         
/* 1119 */         this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
/*      */       }
/* 1121 */       else if ((flag) && (flag2) && (flag3) && (this.itemDropThreshold < 200))
/*      */       {
/* 1123 */         this.itemDropThreshold += 20;
/* 1124 */         EntityItem entityitem = this.playerEntity.dropPlayerItemWithRandomChoice(itemstack, true);
/*      */         
/* 1126 */         if (entityitem != null)
/*      */         {
/* 1128 */           entityitem.setAgeToCreativeDespawnTime();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processConfirmTransaction(C0FPacketConfirmTransaction packetIn)
/*      */   {
/* 1141 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1142 */     Short oshort = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
/*      */     
/* 1144 */     if ((oshort != null) && (packetIn.getUid() == oshort.shortValue()) && (this.playerEntity.openContainer.windowId == packetIn.getWindowId()) && (!this.playerEntity.openContainer.getCanCraft(this.playerEntity)) && (!this.playerEntity.isSpectator()))
/*      */     {
/* 1146 */       this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void processUpdateSign(C12PacketUpdateSign packetIn)
/*      */   {
/* 1152 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1153 */     this.playerEntity.markPlayerActive();
/* 1154 */     WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
/* 1155 */     BlockPos blockpos = packetIn.getPosition();
/*      */     
/* 1157 */     if (worldserver.isBlockLoaded(blockpos))
/*      */     {
/* 1159 */       TileEntity tileentity = worldserver.getTileEntity(blockpos);
/*      */       
/* 1161 */       if (!(tileentity instanceof TileEntitySign))
/*      */       {
/* 1163 */         return;
/*      */       }
/*      */       
/* 1166 */       TileEntitySign tileentitysign = (TileEntitySign)tileentity;
/*      */       
/* 1168 */       if ((!tileentitysign.getIsEditable()) || (tileentitysign.getPlayer() != this.playerEntity))
/*      */       {
/* 1170 */         this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
/* 1171 */         return;
/*      */       }
/*      */       
/* 1174 */       IChatComponent[] aichatcomponent = packetIn.getLines();
/*      */       
/* 1176 */       for (int i = 0; i < aichatcomponent.length; i++)
/*      */       {
/* 1178 */         tileentitysign.signText[i] = new ChatComponentText(EnumChatFormatting.getTextWithoutFormattingCodes(aichatcomponent[i].getUnformattedText()));
/*      */       }
/*      */       
/* 1181 */       tileentitysign.markDirty();
/* 1182 */       worldserver.markBlockForUpdate(blockpos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processKeepAlive(C00PacketKeepAlive packetIn)
/*      */   {
/* 1191 */     if (packetIn.getKey() == this.field_147378_h)
/*      */     {
/* 1193 */       int i = (int)(currentTimeMillis() - this.lastPingTime);
/* 1194 */       this.playerEntity.ping = ((this.playerEntity.ping * 3 + i) / 4);
/*      */     }
/*      */   }
/*      */   
/*      */   private long currentTimeMillis()
/*      */   {
/* 1200 */     return System.nanoTime() / 1000000L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processPlayerAbilities(C13PacketPlayerAbilities packetIn)
/*      */   {
/* 1208 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1209 */     this.playerEntity.capabilities.isFlying = ((packetIn.isFlying()) && (this.playerEntity.capabilities.allowFlying));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processTabComplete(C14PacketTabComplete packetIn)
/*      */   {
/* 1217 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1218 */     List<String> list = Lists.newArrayList();
/*      */     
/* 1220 */     for (String s : this.serverController.getTabCompletions(this.playerEntity, packetIn.getMessage(), packetIn.getTargetBlock()))
/*      */     {
/* 1222 */       list.add(s);
/*      */     }
/*      */     
/* 1225 */     this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete((String[])list.toArray(new String[list.size()])));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processClientSettings(C15PacketClientSettings packetIn)
/*      */   {
/* 1234 */     PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerForPlayer());
/* 1235 */     this.playerEntity.handleClientSettings(packetIn);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void processVanilla250Packet(net.minecraft.network.play.client.C17PacketCustomPayload packetIn)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: aload_0
/*      */     //   2: aload_0
/*      */     //   3: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   6: invokevirtual 230	net/minecraft/entity/player/EntityPlayerMP:getServerForPlayer	()Lnet/minecraft/world/WorldServer;
/*      */     //   9: invokestatic 236	net/minecraft/network/PacketThreadUtil:checkThreadAndEnqueue	(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V
/*      */     //   12: ldc_w 1660
/*      */     //   15: aload_1
/*      */     //   16: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   19: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   22: ifeq +154 -> 176
/*      */     //   25: new 1667	net/minecraft/network/PacketBuffer
/*      */     //   28: dup
/*      */     //   29: aload_1
/*      */     //   30: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   33: invokestatic 1677	io/netty/buffer/Unpooled:wrappedBuffer	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*      */     //   36: invokespecial 1680	net/minecraft/network/PacketBuffer:<init>	(Lio/netty/buffer/ByteBuf;)V
/*      */     //   39: astore_2
/*      */     //   40: aload_2
/*      */     //   41: invokevirtual 1683	net/minecraft/network/PacketBuffer:readItemStackFromBuffer	()Lnet/minecraft/item/ItemStack;
/*      */     //   44: astore_3
/*      */     //   45: aload_3
/*      */     //   46: ifnull +124 -> 170
/*      */     //   49: aload_3
/*      */     //   50: invokevirtual 1444	net/minecraft/item/ItemStack:getTagCompound	()Lnet/minecraft/nbt/NBTTagCompound;
/*      */     //   53: invokestatic 1689	net/minecraft/item/ItemWritableBook:isNBTValid	(Lnet/minecraft/nbt/NBTTagCompound;)Z
/*      */     //   56: ifne +14 -> 70
/*      */     //   59: new 1691	java/io/IOException
/*      */     //   62: dup
/*      */     //   63: ldc_w 1693
/*      */     //   66: invokespecial 1694	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   69: athrow
/*      */     //   70: aload_0
/*      */     //   71: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   74: getfield 750	net/minecraft/entity/player/EntityPlayerMP:inventory	Lnet/minecraft/entity/player/InventoryPlayer;
/*      */     //   77: invokevirtual 756	net/minecraft/entity/player/InventoryPlayer:getCurrentItem	()Lnet/minecraft/item/ItemStack;
/*      */     //   80: astore 4
/*      */     //   82: aload 4
/*      */     //   84: ifnonnull +9 -> 93
/*      */     //   87: aload_2
/*      */     //   88: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   91: pop
/*      */     //   92: return
/*      */     //   93: aload_3
/*      */     //   94: invokevirtual 1492	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   97: getstatic 1703	net/minecraft/init/Items:writable_book	Lnet/minecraft/item/Item;
/*      */     //   100: if_acmpne +35 -> 135
/*      */     //   103: aload_3
/*      */     //   104: invokevirtual 1492	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   107: aload 4
/*      */     //   109: invokevirtual 1492	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   112: if_acmpne +23 -> 135
/*      */     //   115: aload 4
/*      */     //   117: ldc_w 1705
/*      */     //   120: aload_3
/*      */     //   121: invokevirtual 1444	net/minecraft/item/ItemStack:getTagCompound	()Lnet/minecraft/nbt/NBTTagCompound;
/*      */     //   124: ldc_w 1705
/*      */     //   127: bipush 8
/*      */     //   129: invokevirtual 1709	net/minecraft/nbt/NBTTagCompound:getTagList	(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;
/*      */     //   132: invokevirtual 1488	net/minecraft/item/ItemStack:setTagInfo	(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V
/*      */     //   135: aload_2
/*      */     //   136: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   139: pop
/*      */     //   140: return
/*      */     //   141: astore_3
/*      */     //   142: getstatic 90	net/minecraft/network/NetHandlerPlayServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   145: ldc_w 1711
/*      */     //   148: aload_3
/*      */     //   149: invokeinterface 1715 3 0
/*      */     //   154: aload_2
/*      */     //   155: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   158: pop
/*      */     //   159: return
/*      */     //   160: astore 5
/*      */     //   162: aload_2
/*      */     //   163: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   166: pop
/*      */     //   167: aload 5
/*      */     //   169: athrow
/*      */     //   170: aload_2
/*      */     //   171: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   174: pop
/*      */     //   175: return
/*      */     //   176: ldc_w 1717
/*      */     //   179: aload_1
/*      */     //   180: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   183: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   186: ifeq +208 -> 394
/*      */     //   189: new 1667	net/minecraft/network/PacketBuffer
/*      */     //   192: dup
/*      */     //   193: aload_1
/*      */     //   194: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   197: invokestatic 1677	io/netty/buffer/Unpooled:wrappedBuffer	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*      */     //   200: invokespecial 1680	net/minecraft/network/PacketBuffer:<init>	(Lio/netty/buffer/ByteBuf;)V
/*      */     //   203: astore_2
/*      */     //   204: aload_2
/*      */     //   205: invokevirtual 1683	net/minecraft/network/PacketBuffer:readItemStackFromBuffer	()Lnet/minecraft/item/ItemStack;
/*      */     //   208: astore_3
/*      */     //   209: aload_3
/*      */     //   210: ifnull +178 -> 388
/*      */     //   213: aload_3
/*      */     //   214: invokevirtual 1444	net/minecraft/item/ItemStack:getTagCompound	()Lnet/minecraft/nbt/NBTTagCompound;
/*      */     //   217: invokestatic 1722	net/minecraft/item/ItemEditableBook:validBookTagContents	(Lnet/minecraft/nbt/NBTTagCompound;)Z
/*      */     //   220: ifne +14 -> 234
/*      */     //   223: new 1691	java/io/IOException
/*      */     //   226: dup
/*      */     //   227: ldc_w 1693
/*      */     //   230: invokespecial 1694	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   233: athrow
/*      */     //   234: aload_0
/*      */     //   235: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   238: getfield 750	net/minecraft/entity/player/EntityPlayerMP:inventory	Lnet/minecraft/entity/player/InventoryPlayer;
/*      */     //   241: invokevirtual 756	net/minecraft/entity/player/InventoryPlayer:getCurrentItem	()Lnet/minecraft/item/ItemStack;
/*      */     //   244: astore 4
/*      */     //   246: aload 4
/*      */     //   248: ifnonnull +9 -> 257
/*      */     //   251: aload_2
/*      */     //   252: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   255: pop
/*      */     //   256: return
/*      */     //   257: aload_3
/*      */     //   258: invokevirtual 1492	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   261: getstatic 1725	net/minecraft/init/Items:written_book	Lnet/minecraft/item/Item;
/*      */     //   264: if_acmpne +89 -> 353
/*      */     //   267: aload 4
/*      */     //   269: invokevirtual 1492	net/minecraft/item/ItemStack:getItem	()Lnet/minecraft/item/Item;
/*      */     //   272: getstatic 1703	net/minecraft/init/Items:writable_book	Lnet/minecraft/item/Item;
/*      */     //   275: if_acmpne +78 -> 353
/*      */     //   278: aload 4
/*      */     //   280: ldc_w 1727
/*      */     //   283: new 1729	net/minecraft/nbt/NBTTagString
/*      */     //   286: dup
/*      */     //   287: aload_0
/*      */     //   288: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   291: invokevirtual 429	net/minecraft/entity/player/EntityPlayerMP:getName	()Ljava/lang/String;
/*      */     //   294: invokespecial 1730	net/minecraft/nbt/NBTTagString:<init>	(Ljava/lang/String;)V
/*      */     //   297: invokevirtual 1488	net/minecraft/item/ItemStack:setTagInfo	(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V
/*      */     //   300: aload 4
/*      */     //   302: ldc_w 1732
/*      */     //   305: new 1729	net/minecraft/nbt/NBTTagString
/*      */     //   308: dup
/*      */     //   309: aload_3
/*      */     //   310: invokevirtual 1444	net/minecraft/item/ItemStack:getTagCompound	()Lnet/minecraft/nbt/NBTTagCompound;
/*      */     //   313: ldc_w 1732
/*      */     //   316: invokevirtual 1735	net/minecraft/nbt/NBTTagCompound:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   319: invokespecial 1730	net/minecraft/nbt/NBTTagString:<init>	(Ljava/lang/String;)V
/*      */     //   322: invokevirtual 1488	net/minecraft/item/ItemStack:setTagInfo	(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V
/*      */     //   325: aload 4
/*      */     //   327: ldc_w 1705
/*      */     //   330: aload_3
/*      */     //   331: invokevirtual 1444	net/minecraft/item/ItemStack:getTagCompound	()Lnet/minecraft/nbt/NBTTagCompound;
/*      */     //   334: ldc_w 1705
/*      */     //   337: bipush 8
/*      */     //   339: invokevirtual 1709	net/minecraft/nbt/NBTTagCompound:getTagList	(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;
/*      */     //   342: invokevirtual 1488	net/minecraft/item/ItemStack:setTagInfo	(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V
/*      */     //   345: aload 4
/*      */     //   347: getstatic 1725	net/minecraft/init/Items:written_book	Lnet/minecraft/item/Item;
/*      */     //   350: invokevirtual 1739	net/minecraft/item/ItemStack:setItem	(Lnet/minecraft/item/Item;)V
/*      */     //   353: aload_2
/*      */     //   354: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   357: pop
/*      */     //   358: return
/*      */     //   359: astore_3
/*      */     //   360: getstatic 90	net/minecraft/network/NetHandlerPlayServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   363: ldc_w 1741
/*      */     //   366: aload_3
/*      */     //   367: invokeinterface 1715 3 0
/*      */     //   372: aload_2
/*      */     //   373: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   376: pop
/*      */     //   377: return
/*      */     //   378: astore 5
/*      */     //   380: aload_2
/*      */     //   381: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   384: pop
/*      */     //   385: aload 5
/*      */     //   387: athrow
/*      */     //   388: aload_2
/*      */     //   389: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   392: pop
/*      */     //   393: return
/*      */     //   394: ldc_w 1743
/*      */     //   397: aload_1
/*      */     //   398: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   401: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   404: ifeq +53 -> 457
/*      */     //   407: aload_1
/*      */     //   408: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   411: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   414: istore_2
/*      */     //   415: aload_0
/*      */     //   416: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   419: getfield 856	net/minecraft/entity/player/EntityPlayerMP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   422: astore_3
/*      */     //   423: aload_3
/*      */     //   424: instanceof 1748
/*      */     //   427: ifeq +577 -> 1004
/*      */     //   430: aload_3
/*      */     //   431: checkcast 1748	net/minecraft/inventory/ContainerMerchant
/*      */     //   434: iload_2
/*      */     //   435: invokevirtual 1751	net/minecraft/inventory/ContainerMerchant:setCurrentRecipeIndex	(I)V
/*      */     //   438: goto +566 -> 1004
/*      */     //   441: astore_2
/*      */     //   442: getstatic 90	net/minecraft/network/NetHandlerPlayServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   445: ldc_w 1753
/*      */     //   448: aload_2
/*      */     //   449: invokeinterface 1715 3 0
/*      */     //   454: goto +550 -> 1004
/*      */     //   457: ldc_w 1755
/*      */     //   460: aload_1
/*      */     //   461: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   464: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   467: ifeq +315 -> 782
/*      */     //   470: aload_0
/*      */     //   471: getfield 103	net/minecraft/network/NetHandlerPlayServer:serverController	Lnet/minecraft/server/MinecraftServer;
/*      */     //   474: invokevirtual 1758	net/minecraft/server/MinecraftServer:isCommandBlockEnabled	()Z
/*      */     //   477: ifne +27 -> 504
/*      */     //   480: aload_0
/*      */     //   481: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   484: new 799	net/minecraft/util/ChatComponentTranslation
/*      */     //   487: dup
/*      */     //   488: ldc_w 1760
/*      */     //   491: iconst_0
/*      */     //   492: anewarray 4	java/lang/Object
/*      */     //   495: invokespecial 809	net/minecraft/util/ChatComponentTranslation:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   498: invokevirtual 1763	net/minecraft/entity/player/EntityPlayerMP:addChatMessage	(Lnet/minecraft/util/IChatComponent;)V
/*      */     //   501: goto +503 -> 1004
/*      */     //   504: aload_0
/*      */     //   505: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   508: iconst_2
/*      */     //   509: ldc_w 1765
/*      */     //   512: invokevirtual 1769	net/minecraft/entity/player/EntityPlayerMP:canCommandSenderUseCommand	(ILjava/lang/String;)Z
/*      */     //   515: ifeq +243 -> 758
/*      */     //   518: aload_0
/*      */     //   519: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   522: getfield 532	net/minecraft/entity/player/EntityPlayerMP:capabilities	Lnet/minecraft/entity/player/PlayerCapabilities;
/*      */     //   525: getfield 1772	net/minecraft/entity/player/PlayerCapabilities:isCreativeMode	Z
/*      */     //   528: ifeq +230 -> 758
/*      */     //   531: aload_1
/*      */     //   532: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   535: astore_2
/*      */     //   536: aload_2
/*      */     //   537: invokevirtual 1776	net/minecraft/network/PacketBuffer:readByte	()B
/*      */     //   540: istore_3
/*      */     //   541: aconst_null
/*      */     //   542: astore 4
/*      */     //   544: iload_3
/*      */     //   545: ifne +55 -> 600
/*      */     //   548: aload_0
/*      */     //   549: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   552: getfield 917	net/minecraft/entity/player/EntityPlayerMP:worldObj	Lnet/minecraft/world/World;
/*      */     //   555: new 645	net/minecraft/util/BlockPos
/*      */     //   558: dup
/*      */     //   559: aload_2
/*      */     //   560: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   563: aload_2
/*      */     //   564: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   567: aload_2
/*      */     //   568: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   571: invokespecial 1468	net/minecraft/util/BlockPos:<init>	(III)V
/*      */     //   574: invokevirtual 1474	net/minecraft/world/World:getTileEntity	(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
/*      */     //   577: astore 5
/*      */     //   579: aload 5
/*      */     //   581: instanceof 1778
/*      */     //   584: ifeq +55 -> 639
/*      */     //   587: aload 5
/*      */     //   589: checkcast 1778	net/minecraft/tileentity/TileEntityCommandBlock
/*      */     //   592: invokevirtual 1782	net/minecraft/tileentity/TileEntityCommandBlock:getCommandBlockLogic	()Lnet/minecraft/command/server/CommandBlockLogic;
/*      */     //   595: astore 4
/*      */     //   597: goto +42 -> 639
/*      */     //   600: iload_3
/*      */     //   601: iconst_1
/*      */     //   602: if_icmpne +37 -> 639
/*      */     //   605: aload_0
/*      */     //   606: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   609: getfield 917	net/minecraft/entity/player/EntityPlayerMP:worldObj	Lnet/minecraft/world/World;
/*      */     //   612: aload_2
/*      */     //   613: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   616: invokevirtual 1788	net/minecraft/world/World:getEntityByID	(I)Lnet/minecraft/entity/Entity;
/*      */     //   619: astore 5
/*      */     //   621: aload 5
/*      */     //   623: instanceof 1790
/*      */     //   626: ifeq +13 -> 639
/*      */     //   629: aload 5
/*      */     //   631: checkcast 1790	net/minecraft/entity/EntityMinecartCommandBlock
/*      */     //   634: invokevirtual 1791	net/minecraft/entity/EntityMinecartCommandBlock:getCommandBlockLogic	()Lnet/minecraft/command/server/CommandBlockLogic;
/*      */     //   637: astore 4
/*      */     //   639: aload_2
/*      */     //   640: aload_2
/*      */     //   641: invokevirtual 1794	net/minecraft/network/PacketBuffer:readableBytes	()I
/*      */     //   644: invokevirtual 1798	net/minecraft/network/PacketBuffer:readStringFromBuffer	(I)Ljava/lang/String;
/*      */     //   647: astore 5
/*      */     //   649: aload_2
/*      */     //   650: invokevirtual 1801	net/minecraft/network/PacketBuffer:readBoolean	()Z
/*      */     //   653: istore 6
/*      */     //   655: aload 4
/*      */     //   657: ifnull +93 -> 750
/*      */     //   660: aload 4
/*      */     //   662: aload 5
/*      */     //   664: invokevirtual 1804	net/minecraft/command/server/CommandBlockLogic:setCommand	(Ljava/lang/String;)V
/*      */     //   667: aload 4
/*      */     //   669: iload 6
/*      */     //   671: invokevirtual 1807	net/minecraft/command/server/CommandBlockLogic:setTrackOutput	(Z)V
/*      */     //   674: iload 6
/*      */     //   676: ifne +9 -> 685
/*      */     //   679: aload 4
/*      */     //   681: aconst_null
/*      */     //   682: invokevirtual 1810	net/minecraft/command/server/CommandBlockLogic:setLastOutput	(Lnet/minecraft/util/IChatComponent;)V
/*      */     //   685: aload 4
/*      */     //   687: invokevirtual 1813	net/minecraft/command/server/CommandBlockLogic:updateCommand	()V
/*      */     //   690: aload_0
/*      */     //   691: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   694: new 799	net/minecraft/util/ChatComponentTranslation
/*      */     //   697: dup
/*      */     //   698: ldc_w 1815
/*      */     //   701: iconst_1
/*      */     //   702: anewarray 4	java/lang/Object
/*      */     //   705: dup
/*      */     //   706: iconst_0
/*      */     //   707: aload 5
/*      */     //   709: aastore
/*      */     //   710: invokespecial 809	net/minecraft/util/ChatComponentTranslation:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   713: invokevirtual 1763	net/minecraft/entity/player/EntityPlayerMP:addChatMessage	(Lnet/minecraft/util/IChatComponent;)V
/*      */     //   716: goto +34 -> 750
/*      */     //   719: astore_3
/*      */     //   720: getstatic 90	net/minecraft/network/NetHandlerPlayServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   723: ldc_w 1817
/*      */     //   726: aload_3
/*      */     //   727: invokeinterface 1715 3 0
/*      */     //   732: aload_2
/*      */     //   733: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   736: pop
/*      */     //   737: goto +267 -> 1004
/*      */     //   740: astore 7
/*      */     //   742: aload_2
/*      */     //   743: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   746: pop
/*      */     //   747: aload 7
/*      */     //   749: athrow
/*      */     //   750: aload_2
/*      */     //   751: invokevirtual 1697	net/minecraft/network/PacketBuffer:release	()Z
/*      */     //   754: pop
/*      */     //   755: goto +249 -> 1004
/*      */     //   758: aload_0
/*      */     //   759: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   762: new 799	net/minecraft/util/ChatComponentTranslation
/*      */     //   765: dup
/*      */     //   766: ldc_w 1819
/*      */     //   769: iconst_0
/*      */     //   770: anewarray 4	java/lang/Object
/*      */     //   773: invokespecial 809	net/minecraft/util/ChatComponentTranslation:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*      */     //   776: invokevirtual 1763	net/minecraft/entity/player/EntityPlayerMP:addChatMessage	(Lnet/minecraft/util/IChatComponent;)V
/*      */     //   779: goto +225 -> 1004
/*      */     //   782: ldc_w 1821
/*      */     //   785: aload_1
/*      */     //   786: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   789: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   792: ifeq +119 -> 911
/*      */     //   795: aload_0
/*      */     //   796: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   799: getfield 856	net/minecraft/entity/player/EntityPlayerMP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   802: instanceof 1823
/*      */     //   805: ifeq +199 -> 1004
/*      */     //   808: aload_1
/*      */     //   809: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   812: astore_2
/*      */     //   813: aload_2
/*      */     //   814: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   817: istore_3
/*      */     //   818: aload_2
/*      */     //   819: invokevirtual 1746	net/minecraft/network/PacketBuffer:readInt	()I
/*      */     //   822: istore 4
/*      */     //   824: aload_0
/*      */     //   825: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   828: getfield 856	net/minecraft/entity/player/EntityPlayerMP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   831: checkcast 1823	net/minecraft/inventory/ContainerBeacon
/*      */     //   834: astore 5
/*      */     //   836: aload 5
/*      */     //   838: iconst_0
/*      */     //   839: invokevirtual 1827	net/minecraft/inventory/ContainerBeacon:getSlot	(I)Lnet/minecraft/inventory/Slot;
/*      */     //   842: astore 6
/*      */     //   844: aload 6
/*      */     //   846: invokevirtual 1830	net/minecraft/inventory/Slot:getHasStack	()Z
/*      */     //   849: ifeq +155 -> 1004
/*      */     //   852: aload 6
/*      */     //   854: iconst_1
/*      */     //   855: invokevirtual 1834	net/minecraft/inventory/Slot:decrStackSize	(I)Lnet/minecraft/item/ItemStack;
/*      */     //   858: pop
/*      */     //   859: aload 5
/*      */     //   861: invokevirtual 1838	net/minecraft/inventory/ContainerBeacon:func_180611_e	()Lnet/minecraft/inventory/IInventory;
/*      */     //   864: astore 7
/*      */     //   866: aload 7
/*      */     //   868: iconst_1
/*      */     //   869: iload_3
/*      */     //   870: invokeinterface 1844 3 0
/*      */     //   875: aload 7
/*      */     //   877: iconst_2
/*      */     //   878: iload 4
/*      */     //   880: invokeinterface 1844 3 0
/*      */     //   885: aload 7
/*      */     //   887: invokeinterface 1845 1 0
/*      */     //   892: goto +112 -> 1004
/*      */     //   895: astore_2
/*      */     //   896: getstatic 90	net/minecraft/network/NetHandlerPlayServer:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   899: ldc_w 1847
/*      */     //   902: aload_2
/*      */     //   903: invokeinterface 1715 3 0
/*      */     //   908: goto +96 -> 1004
/*      */     //   911: ldc_w 1849
/*      */     //   914: aload_1
/*      */     //   915: invokevirtual 1665	net/minecraft/network/play/client/C17PacketCustomPayload:getChannelName	()Ljava/lang/String;
/*      */     //   918: invokevirtual 435	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   921: ifeq +83 -> 1004
/*      */     //   924: aload_0
/*      */     //   925: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   928: getfield 856	net/minecraft/entity/player/EntityPlayerMP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   931: instanceof 1851
/*      */     //   934: ifeq +70 -> 1004
/*      */     //   937: aload_0
/*      */     //   938: getfield 113	net/minecraft/network/NetHandlerPlayServer:playerEntity	Lnet/minecraft/entity/player/EntityPlayerMP;
/*      */     //   941: getfield 856	net/minecraft/entity/player/EntityPlayerMP:openContainer	Lnet/minecraft/inventory/Container;
/*      */     //   944: checkcast 1851	net/minecraft/inventory/ContainerRepair
/*      */     //   947: astore_2
/*      */     //   948: aload_1
/*      */     //   949: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   952: ifnull +45 -> 997
/*      */     //   955: aload_1
/*      */     //   956: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   959: invokevirtual 1794	net/minecraft/network/PacketBuffer:readableBytes	()I
/*      */     //   962: iconst_1
/*      */     //   963: if_icmplt +34 -> 997
/*      */     //   966: aload_1
/*      */     //   967: invokevirtual 1671	net/minecraft/network/play/client/C17PacketCustomPayload:getBufferData	()Lnet/minecraft/network/PacketBuffer;
/*      */     //   970: sipush 32767
/*      */     //   973: invokevirtual 1798	net/minecraft/network/PacketBuffer:readStringFromBuffer	(I)Ljava/lang/String;
/*      */     //   976: invokestatic 1854	net/minecraft/util/ChatAllowedCharacters:filterAllowedCharacters	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   979: astore_3
/*      */     //   980: aload_3
/*      */     //   981: invokevirtual 1128	java/lang/String:length	()I
/*      */     //   984: bipush 30
/*      */     //   986: if_icmpgt +18 -> 1004
/*      */     //   989: aload_2
/*      */     //   990: aload_3
/*      */     //   991: invokevirtual 1857	net/minecraft/inventory/ContainerRepair:updateItemName	(Ljava/lang/String;)V
/*      */     //   994: goto +10 -> 1004
/*      */     //   997: aload_2
/*      */     //   998: ldc_w 1765
/*      */     //   1001: invokevirtual 1857	net/minecraft/inventory/ContainerRepair:updateItemName	(Ljava/lang/String;)V
/*      */     //   1004: return
/*      */     // Line number table:
/*      */     //   Java source line #1243	-> byte code offset #0
/*      */     //   Java source line #1245	-> byte code offset #12
/*      */     //   Java source line #1247	-> byte code offset #25
/*      */     //   Java source line #1251	-> byte code offset #40
/*      */     //   Java source line #1253	-> byte code offset #45
/*      */     //   Java source line #1255	-> byte code offset #49
/*      */     //   Java source line #1257	-> byte code offset #59
/*      */     //   Java source line #1260	-> byte code offset #70
/*      */     //   Java source line #1262	-> byte code offset #82
/*      */     //   Java source line #1282	-> byte code offset #87
/*      */     //   Java source line #1264	-> byte code offset #92
/*      */     //   Java source line #1267	-> byte code offset #93
/*      */     //   Java source line #1269	-> byte code offset #115
/*      */     //   Java source line #1282	-> byte code offset #135
/*      */     //   Java source line #1272	-> byte code offset #140
/*      */     //   Java source line #1275	-> byte code offset #141
/*      */     //   Java source line #1277	-> byte code offset #142
/*      */     //   Java source line #1282	-> byte code offset #154
/*      */     //   Java source line #1278	-> byte code offset #159
/*      */     //   Java source line #1281	-> byte code offset #160
/*      */     //   Java source line #1282	-> byte code offset #162
/*      */     //   Java source line #1283	-> byte code offset #167
/*      */     //   Java source line #1282	-> byte code offset #170
/*      */     //   Java source line #1285	-> byte code offset #175
/*      */     //   Java source line #1287	-> byte code offset #176
/*      */     //   Java source line #1289	-> byte code offset #189
/*      */     //   Java source line #1293	-> byte code offset #204
/*      */     //   Java source line #1295	-> byte code offset #209
/*      */     //   Java source line #1297	-> byte code offset #213
/*      */     //   Java source line #1299	-> byte code offset #223
/*      */     //   Java source line #1302	-> byte code offset #234
/*      */     //   Java source line #1304	-> byte code offset #246
/*      */     //   Java source line #1327	-> byte code offset #251
/*      */     //   Java source line #1306	-> byte code offset #256
/*      */     //   Java source line #1309	-> byte code offset #257
/*      */     //   Java source line #1311	-> byte code offset #278
/*      */     //   Java source line #1312	-> byte code offset #300
/*      */     //   Java source line #1313	-> byte code offset #325
/*      */     //   Java source line #1314	-> byte code offset #345
/*      */     //   Java source line #1327	-> byte code offset #353
/*      */     //   Java source line #1317	-> byte code offset #358
/*      */     //   Java source line #1320	-> byte code offset #359
/*      */     //   Java source line #1322	-> byte code offset #360
/*      */     //   Java source line #1327	-> byte code offset #372
/*      */     //   Java source line #1323	-> byte code offset #377
/*      */     //   Java source line #1326	-> byte code offset #378
/*      */     //   Java source line #1327	-> byte code offset #380
/*      */     //   Java source line #1328	-> byte code offset #385
/*      */     //   Java source line #1327	-> byte code offset #388
/*      */     //   Java source line #1330	-> byte code offset #393
/*      */     //   Java source line #1332	-> byte code offset #394
/*      */     //   Java source line #1336	-> byte code offset #407
/*      */     //   Java source line #1337	-> byte code offset #415
/*      */     //   Java source line #1339	-> byte code offset #423
/*      */     //   Java source line #1341	-> byte code offset #430
/*      */     //   Java source line #1343	-> byte code offset #438
/*      */     //   Java source line #1344	-> byte code offset #441
/*      */     //   Java source line #1346	-> byte code offset #442
/*      */     //   Java source line #1348	-> byte code offset #454
/*      */     //   Java source line #1349	-> byte code offset #457
/*      */     //   Java source line #1351	-> byte code offset #470
/*      */     //   Java source line #1353	-> byte code offset #480
/*      */     //   Java source line #1354	-> byte code offset #501
/*      */     //   Java source line #1355	-> byte code offset #504
/*      */     //   Java source line #1357	-> byte code offset #531
/*      */     //   Java source line #1361	-> byte code offset #536
/*      */     //   Java source line #1362	-> byte code offset #541
/*      */     //   Java source line #1364	-> byte code offset #544
/*      */     //   Java source line #1366	-> byte code offset #548
/*      */     //   Java source line #1368	-> byte code offset #579
/*      */     //   Java source line #1370	-> byte code offset #587
/*      */     //   Java source line #1372	-> byte code offset #597
/*      */     //   Java source line #1373	-> byte code offset #600
/*      */     //   Java source line #1375	-> byte code offset #605
/*      */     //   Java source line #1377	-> byte code offset #621
/*      */     //   Java source line #1379	-> byte code offset #629
/*      */     //   Java source line #1383	-> byte code offset #639
/*      */     //   Java source line #1384	-> byte code offset #649
/*      */     //   Java source line #1386	-> byte code offset #655
/*      */     //   Java source line #1388	-> byte code offset #660
/*      */     //   Java source line #1389	-> byte code offset #667
/*      */     //   Java source line #1391	-> byte code offset #674
/*      */     //   Java source line #1393	-> byte code offset #679
/*      */     //   Java source line #1396	-> byte code offset #685
/*      */     //   Java source line #1397	-> byte code offset #690
/*      */     //   Java source line #1399	-> byte code offset #716
/*      */     //   Java source line #1400	-> byte code offset #719
/*      */     //   Java source line #1402	-> byte code offset #720
/*      */     //   Java source line #1406	-> byte code offset #732
/*      */     //   Java source line #1405	-> byte code offset #740
/*      */     //   Java source line #1406	-> byte code offset #742
/*      */     //   Java source line #1407	-> byte code offset #747
/*      */     //   Java source line #1406	-> byte code offset #750
/*      */     //   Java source line #1408	-> byte code offset #755
/*      */     //   Java source line #1411	-> byte code offset #758
/*      */     //   Java source line #1413	-> byte code offset #779
/*      */     //   Java source line #1414	-> byte code offset #782
/*      */     //   Java source line #1416	-> byte code offset #795
/*      */     //   Java source line #1420	-> byte code offset #808
/*      */     //   Java source line #1421	-> byte code offset #813
/*      */     //   Java source line #1422	-> byte code offset #818
/*      */     //   Java source line #1423	-> byte code offset #824
/*      */     //   Java source line #1424	-> byte code offset #836
/*      */     //   Java source line #1426	-> byte code offset #844
/*      */     //   Java source line #1428	-> byte code offset #852
/*      */     //   Java source line #1429	-> byte code offset #859
/*      */     //   Java source line #1430	-> byte code offset #866
/*      */     //   Java source line #1431	-> byte code offset #875
/*      */     //   Java source line #1432	-> byte code offset #885
/*      */     //   Java source line #1434	-> byte code offset #892
/*      */     //   Java source line #1435	-> byte code offset #895
/*      */     //   Java source line #1437	-> byte code offset #896
/*      */     //   Java source line #1440	-> byte code offset #908
/*      */     //   Java source line #1441	-> byte code offset #911
/*      */     //   Java source line #1443	-> byte code offset #937
/*      */     //   Java source line #1445	-> byte code offset #948
/*      */     //   Java source line #1447	-> byte code offset #966
/*      */     //   Java source line #1449	-> byte code offset #980
/*      */     //   Java source line #1451	-> byte code offset #989
/*      */     //   Java source line #1453	-> byte code offset #994
/*      */     //   Java source line #1456	-> byte code offset #997
/*      */     //   Java source line #1459	-> byte code offset #1004
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1005	0	this	NetHandlerPlayServer
/*      */     //   0	1005	1	packetIn	net.minecraft.network.play.client.C17PacketCustomPayload
/*      */     //   39	132	2	packetbuffer3	PacketBuffer
/*      */     //   203	186	2	packetbuffer2	PacketBuffer
/*      */     //   414	21	2	i	int
/*      */     //   441	8	2	exception2	Exception
/*      */     //   535	216	2	packetbuffer	PacketBuffer
/*      */     //   812	7	2	packetbuffer1	PacketBuffer
/*      */     //   895	8	2	exception	Exception
/*      */     //   947	51	2	containerrepair	net.minecraft.inventory.ContainerRepair
/*      */     //   44	77	3	itemstack1	ItemStack
/*      */     //   141	8	3	exception3	Exception
/*      */     //   208	123	3	itemstack	ItemStack
/*      */     //   359	8	3	exception4	Exception
/*      */     //   422	9	3	container	Container
/*      */     //   540	61	3	j	int
/*      */     //   719	8	3	exception1	Exception
/*      */     //   817	53	3	k	int
/*      */     //   979	12	3	s	String
/*      */     //   80	36	4	itemstack3	ItemStack
/*      */     //   244	102	4	itemstack2	ItemStack
/*      */     //   542	144	4	commandblocklogic	net.minecraft.command.server.CommandBlockLogic
/*      */     //   822	57	4	l	int
/*      */     //   160	8	5	localObject1	Object
/*      */     //   378	8	5	localObject2	Object
/*      */     //   577	11	5	tileentity	TileEntity
/*      */     //   619	11	5	entity	Entity
/*      */     //   647	61	5	s1	String
/*      */     //   834	26	5	containerbeacon	net.minecraft.inventory.ContainerBeacon
/*      */     //   653	22	6	flag	boolean
/*      */     //   842	11	6	slot	Slot
/*      */     //   740	8	7	localObject3	Object
/*      */     //   864	22	7	iinventory	net.minecraft.inventory.IInventory
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   40	87	141	java/lang/Exception
/*      */     //   93	135	141	java/lang/Exception
/*      */     //   40	87	160	finally
/*      */     //   93	135	160	finally
/*      */     //   141	154	160	finally
/*      */     //   204	251	359	java/lang/Exception
/*      */     //   257	353	359	java/lang/Exception
/*      */     //   204	251	378	finally
/*      */     //   257	353	378	finally
/*      */     //   359	372	378	finally
/*      */     //   407	438	441	java/lang/Exception
/*      */     //   536	716	719	java/lang/Exception
/*      */     //   536	732	740	finally
/*      */     //   808	892	895	java/lang/Exception
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NetHandlerPlayServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */