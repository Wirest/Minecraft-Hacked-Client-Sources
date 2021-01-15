/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.Block.SoundType;
/*      */ import net.minecraft.block.BlockFence;
/*      */ import net.minecraft.block.BlockFenceGate;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.block.state.pattern.BlockPattern.PatternHelper;
/*      */ import net.minecraft.command.CommandResultStats;
/*      */ import net.minecraft.command.CommandResultStats.Type;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.event.HoverEvent;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumFacing.Axis;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.Explosion;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import rip.jutting.polaris.event.events.EventSafewalk;
/*      */ 
/*      */ public abstract class Entity implements net.minecraft.command.ICommandSender
/*      */ {
/*   54 */   private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*      */   
/*      */ 
/*      */   private static int nextEntityID;
/*      */   
/*      */ 
/*      */   private int entityId;
/*      */   
/*      */ 
/*      */   public double renderDistanceWeight;
/*      */   
/*      */ 
/*      */   public boolean preventEntitySpawning;
/*      */   
/*      */ 
/*      */   public Entity riddenByEntity;
/*      */   
/*      */ 
/*      */   public Entity ridingEntity;
/*      */   
/*      */ 
/*      */   public boolean forceSpawn;
/*      */   
/*      */ 
/*      */   public World worldObj;
/*      */   
/*      */ 
/*      */   public double prevPosX;
/*      */   
/*      */ 
/*      */   public double prevPosY;
/*      */   
/*      */ 
/*      */   public double prevPosZ;
/*      */   
/*      */ 
/*      */   public double posX;
/*      */   
/*      */ 
/*      */   public double posY;
/*      */   
/*      */ 
/*      */   public double posZ;
/*      */   
/*      */ 
/*      */   public double motionX;
/*      */   
/*      */ 
/*      */   public double motionY;
/*      */   
/*      */ 
/*      */   public double motionZ;
/*      */   
/*      */ 
/*      */   public float rotationYaw;
/*      */   
/*      */ 
/*      */   public float rotationPitch;
/*      */   
/*      */ 
/*      */   public float prevRotationYaw;
/*      */   
/*      */ 
/*      */   public float prevRotationPitch;
/*      */   
/*      */ 
/*      */   public AxisAlignedBB boundingBox;
/*      */   
/*      */ 
/*      */   public boolean onGround;
/*      */   
/*      */ 
/*      */   public boolean isCollidedHorizontally;
/*      */   
/*      */ 
/*      */   public boolean isCollidedVertically;
/*      */   
/*      */ 
/*      */   public boolean isCollided;
/*      */   
/*      */ 
/*      */   public boolean velocityChanged;
/*      */   
/*      */ 
/*      */   protected boolean isInWeb;
/*      */   
/*      */ 
/*      */   private boolean isOutsideBorder;
/*      */   
/*      */ 
/*      */   public boolean isDead;
/*      */   
/*      */ 
/*      */   public float width;
/*      */   
/*      */ 
/*      */   public float height;
/*      */   
/*      */ 
/*      */   public float prevDistanceWalkedModified;
/*      */   
/*      */ 
/*      */   public float distanceWalkedModified;
/*      */   
/*      */ 
/*      */   public float distanceWalkedOnStepModified;
/*      */   
/*      */   public float fallDistance;
/*      */   
/*      */   private int nextStepDistance;
/*      */   
/*      */   public double lastTickPosX;
/*      */   
/*      */   public double lastTickPosY;
/*      */   
/*      */   public double lastTickPosZ;
/*      */   
/*      */   public float stepHeight;
/*      */   
/*      */   public boolean noClip;
/*      */   
/*      */   public float entityCollisionReduction;
/*      */   
/*      */   protected Random rand;
/*      */   
/*      */   public int ticksExisted;
/*      */   
/*      */   public int fireResistance;
/*      */   
/*      */   private int fire;
/*      */   
/*      */   protected boolean inWater;
/*      */   
/*      */   public int hurtResistantTime;
/*      */   
/*      */   protected boolean firstUpdate;
/*      */   
/*      */   protected boolean isImmuneToFire;
/*      */   
/*      */   protected DataWatcher dataWatcher;
/*      */   
/*      */   private double entityRiderPitchDelta;
/*      */   
/*      */   private double entityRiderYawDelta;
/*      */   
/*      */   public boolean addedToChunk;
/*      */   
/*      */   public int chunkCoordX;
/*      */   
/*      */   public int chunkCoordY;
/*      */   
/*      */   public int chunkCoordZ;
/*      */   
/*      */   public int serverPosX;
/*      */   
/*      */   public int serverPosY;
/*      */   
/*      */   public int serverPosZ;
/*      */   
/*      */   public boolean ignoreFrustumCheck;
/*      */   
/*      */   public boolean isAirBorne;
/*      */   
/*      */   public int timeUntilPortal;
/*      */   
/*      */   protected boolean inPortal;
/*      */   
/*      */   protected int portalCounter;
/*      */   
/*      */   public int dimension;
/*      */   
/*      */   protected BlockPos field_181016_an;
/*      */   
/*      */   protected Vec3 field_181017_ao;
/*      */   
/*      */   protected EnumFacing field_181018_ap;
/*      */   
/*      */   private boolean invulnerable;
/*      */   
/*      */   protected UUID entityUniqueID;
/*      */   
/*      */   private final CommandResultStats cmdResultStats;
/*      */   
/*      */ 
/*      */   public int getEntityId()
/*      */   {
/*  240 */     return this.entityId;
/*      */   }
/*      */   
/*      */   public void setEntityId(int id)
/*      */   {
/*  245 */     this.entityId = id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onKillCommand()
/*      */   {
/*  253 */     setDead();
/*      */   }
/*      */   
/*      */   public Entity(World worldIn)
/*      */   {
/*  258 */     this.entityId = (nextEntityID++);
/*  259 */     this.renderDistanceWeight = 1.0D;
/*  260 */     this.boundingBox = ZERO_AABB;
/*  261 */     this.width = 0.6F;
/*  262 */     this.height = 1.8F;
/*  263 */     this.nextStepDistance = 1;
/*  264 */     this.rand = new Random();
/*  265 */     this.fireResistance = 1;
/*  266 */     this.firstUpdate = true;
/*  267 */     this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
/*  268 */     this.cmdResultStats = new CommandResultStats();
/*  269 */     this.worldObj = worldIn;
/*  270 */     setPosition(0.0D, 0.0D, 0.0D);
/*      */     
/*  272 */     if (worldIn != null)
/*      */     {
/*  274 */       this.dimension = worldIn.provider.getDimensionId();
/*      */     }
/*      */     
/*  277 */     this.dataWatcher = new DataWatcher(this);
/*  278 */     this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
/*  279 */     this.dataWatcher.addObject(1, Short.valueOf((short)300));
/*  280 */     this.dataWatcher.addObject(3, Byte.valueOf((byte)0));
/*  281 */     this.dataWatcher.addObject(2, "");
/*  282 */     this.dataWatcher.addObject(4, Byte.valueOf((byte)0));
/*  283 */     entityInit();
/*      */   }
/*      */   
/*      */   protected abstract void entityInit();
/*      */   
/*      */   public DataWatcher getDataWatcher()
/*      */   {
/*  290 */     return this.dataWatcher;
/*      */   }
/*      */   
/*      */   public boolean equals(Object p_equals_1_)
/*      */   {
/*  295 */     return ((Entity)p_equals_1_).entityId == this.entityId;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/*  300 */     return this.entityId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void preparePlayerToSpawn()
/*      */   {
/*  309 */     if (this.worldObj != null)
/*      */     {
/*  311 */       while ((this.posY > 0.0D) && (this.posY < 256.0D))
/*      */       {
/*  313 */         setPosition(this.posX, this.posY, this.posZ);
/*      */         
/*  315 */         if (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*  320 */         this.posY += 1.0D;
/*      */       }
/*      */       
/*  323 */       this.motionX = (this.motionY = this.motionZ = 0.0D);
/*  324 */       this.rotationPitch = 0.0F;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDead()
/*      */   {
/*  333 */     this.isDead = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setSize(float width, float height)
/*      */   {
/*  341 */     if ((width != this.width) || (height != this.height))
/*      */     {
/*  343 */       float f = this.width;
/*  344 */       this.width = width;
/*  345 */       this.height = height;
/*  346 */       setEntityBoundingBox(new AxisAlignedBB(getEntityBoundingBox().minX, getEntityBoundingBox().minY, getEntityBoundingBox().minZ, getEntityBoundingBox().minX + this.width, getEntityBoundingBox().minY + this.height, getEntityBoundingBox().minZ + this.width));
/*      */       
/*  348 */       if ((this.width > f) && (!this.firstUpdate) && (!this.worldObj.isRemote))
/*      */       {
/*  350 */         moveEntity(f - this.width, 0.0D, f - this.width);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRotation(float yaw, float pitch)
/*      */   {
/*  360 */     this.rotationYaw = (yaw % 360.0F);
/*  361 */     this.rotationPitch = (pitch % 360.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPosition(double x, double y, double z)
/*      */   {
/*  369 */     this.posX = x;
/*  370 */     this.posY = y;
/*  371 */     this.posZ = z;
/*  372 */     float f = this.width / 2.0F;
/*  373 */     float f1 = this.height;
/*  374 */     setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAngles(float yaw, float pitch)
/*      */   {
/*  383 */     float f = this.rotationPitch;
/*  384 */     float f1 = this.rotationYaw;
/*  385 */     this.rotationYaw = ((float)(this.rotationYaw + yaw * 0.15D));
/*  386 */     this.rotationPitch = ((float)(this.rotationPitch - pitch * 0.15D));
/*  387 */     this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
/*  388 */     this.prevRotationPitch += this.rotationPitch - f;
/*  389 */     this.prevRotationYaw += this.rotationYaw - f1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/*  397 */     onEntityUpdate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityUpdate()
/*      */   {
/*  405 */     this.worldObj.theProfiler.startSection("entityBaseTick");
/*      */     
/*  407 */     if ((this.ridingEntity != null) && (this.ridingEntity.isDead))
/*      */     {
/*  409 */       this.ridingEntity = null;
/*      */     }
/*      */     
/*  412 */     this.prevDistanceWalkedModified = this.distanceWalkedModified;
/*  413 */     this.prevPosX = this.posX;
/*  414 */     this.prevPosY = this.posY;
/*  415 */     this.prevPosZ = this.posZ;
/*  416 */     this.prevRotationPitch = this.rotationPitch;
/*  417 */     this.prevRotationYaw = this.rotationYaw;
/*      */     
/*  419 */     if ((!this.worldObj.isRemote) && ((this.worldObj instanceof WorldServer)))
/*      */     {
/*  421 */       this.worldObj.theProfiler.startSection("portal");
/*  422 */       MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
/*  423 */       int i = getMaxInPortalTime();
/*      */       
/*  425 */       if (this.inPortal)
/*      */       {
/*  427 */         if (minecraftserver.getAllowNether())
/*      */         {
/*  429 */           if ((this.ridingEntity == null) && (this.portalCounter++ >= i))
/*      */           {
/*  431 */             this.portalCounter = i;
/*  432 */             this.timeUntilPortal = getPortalCooldown();
/*      */             int j;
/*      */             int j;
/*  435 */             if (this.worldObj.provider.getDimensionId() == -1)
/*      */             {
/*  437 */               j = 0;
/*      */             }
/*      */             else
/*      */             {
/*  441 */               j = -1;
/*      */             }
/*      */             
/*  444 */             travelToDimension(j);
/*      */           }
/*      */           
/*  447 */           this.inPortal = false;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  452 */         if (this.portalCounter > 0)
/*      */         {
/*  454 */           this.portalCounter -= 4;
/*      */         }
/*      */         
/*  457 */         if (this.portalCounter < 0)
/*      */         {
/*  459 */           this.portalCounter = 0;
/*      */         }
/*      */       }
/*      */       
/*  463 */       if (this.timeUntilPortal > 0)
/*      */       {
/*  465 */         this.timeUntilPortal -= 1;
/*      */       }
/*      */       
/*  468 */       this.worldObj.theProfiler.endSection();
/*      */     }
/*      */     
/*  471 */     spawnRunningParticles();
/*  472 */     handleWaterMovement();
/*      */     
/*  474 */     if (this.worldObj.isRemote)
/*      */     {
/*  476 */       this.fire = 0;
/*      */     }
/*  478 */     else if (this.fire > 0)
/*      */     {
/*  480 */       if (this.isImmuneToFire)
/*      */       {
/*  482 */         this.fire -= 4;
/*      */         
/*  484 */         if (this.fire < 0)
/*      */         {
/*  486 */           this.fire = 0;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  491 */         if (this.fire % 20 == 0)
/*      */         {
/*  493 */           attackEntityFrom(DamageSource.onFire, 1.0F);
/*      */         }
/*      */         
/*  496 */         this.fire -= 1;
/*      */       }
/*      */     }
/*      */     
/*  500 */     if (isInLava())
/*      */     {
/*  502 */       setOnFireFromLava();
/*  503 */       this.fallDistance *= 0.5F;
/*      */     }
/*      */     
/*  506 */     if (this.posY < -64.0D)
/*      */     {
/*  508 */       kill();
/*      */     }
/*      */     
/*  511 */     if (!this.worldObj.isRemote)
/*      */     {
/*  513 */       setFlag(0, this.fire > 0);
/*      */     }
/*      */     
/*  516 */     this.firstUpdate = false;
/*  517 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxInPortalTime()
/*      */   {
/*  525 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setOnFireFromLava()
/*      */   {
/*  533 */     if (!this.isImmuneToFire)
/*      */     {
/*  535 */       attackEntityFrom(DamageSource.lava, 4.0F);
/*  536 */       setFire(15);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFire(int seconds)
/*      */   {
/*  545 */     int i = seconds * 20;
/*  546 */     i = net.minecraft.enchantment.EnchantmentProtection.getFireTimeForEntity(this, i);
/*      */     
/*  548 */     if (this.fire < i)
/*      */     {
/*  550 */       this.fire = i;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void extinguish()
/*      */   {
/*  559 */     this.fire = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void kill()
/*      */   {
/*  567 */     setDead();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOffsetPositionInLiquid(double x, double y, double z)
/*      */   {
/*  575 */     AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
/*  576 */     return isLiquidPresentInAABB(axisalignedbb);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isLiquidPresentInAABB(AxisAlignedBB bb)
/*      */   {
/*  584 */     return (this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty()) && (!this.worldObj.isAnyLiquid(bb));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void moveEntity(double x, double y, double z)
/*      */   {
/*  592 */     if (this.noClip)
/*      */     {
/*  594 */       setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
/*  595 */       resetPositionToBB();
/*      */     }
/*      */     else
/*      */     {
/*  599 */       this.worldObj.theProfiler.startSection("move");
/*  600 */       double d0 = this.posX;
/*  601 */       double d1 = this.posY;
/*  602 */       double d2 = this.posZ;
/*      */       
/*  604 */       if (this.isInWeb)
/*      */       {
/*  606 */         this.isInWeb = false;
/*  607 */         x *= 0.25D;
/*  608 */         y *= 0.05000000074505806D;
/*  609 */         z *= 0.25D;
/*  610 */         this.motionX = 0.0D;
/*  611 */         this.motionY = 0.0D;
/*  612 */         this.motionZ = 0.0D;
/*      */       }
/*      */       
/*  615 */       double d3 = x;
/*  616 */       double d4 = y;
/*  617 */       double d5 = z;
/*  618 */       boolean flag = (this.onGround) && (isSneaking()) && ((this instanceof EntityPlayer));
/*      */       
/*  620 */       EventSafewalk event = new EventSafewalk(flag);
/*  621 */       event.call();
/*      */       
/*  623 */       if (event.isCancelled())
/*      */       {
/*  625 */         flag = true;
/*      */       }
/*      */       
/*      */ 
/*  629 */       if (flag)
/*      */       {
/*      */ 
/*      */ 
/*  633 */         double d6 = 0.05D;
/*      */         for (;;) {
/*  635 */           if ((x < d6) && (x >= -d6))
/*      */           {
/*  637 */             x = 0.0D;
/*      */           }
/*  639 */           else if (x > 0.0D)
/*      */           {
/*  641 */             x -= d6;
/*      */           }
/*      */           else
/*      */           {
/*  645 */             x += d6;
/*      */           }
/*  633 */           d3 = x; if (x != 0.0D) { if (!this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty()) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         do
/*      */         {
/*  651 */           if ((z < d6) && (z >= -d6))
/*      */           {
/*  653 */             z = 0.0D;
/*      */           }
/*  655 */           else if (z > 0.0D)
/*      */           {
/*  657 */             z -= d6;
/*      */           }
/*      */           else
/*      */           {
/*  661 */             z += d6;
/*      */           }
/*  649 */           d5 = z; if (z == 0.0D) break; } while (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty());
/*  665 */         for (; 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  665 */             (x != 0.0D) && (z != 0.0D) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty()); d5 = z)
/*      */         {
/*  667 */           if ((x < d6) && (x >= -d6))
/*      */           {
/*  669 */             x = 0.0D;
/*      */           }
/*  671 */           else if (x > 0.0D)
/*      */           {
/*  673 */             x -= d6;
/*      */           }
/*      */           else
/*      */           {
/*  677 */             x += d6;
/*      */           }
/*      */           
/*  680 */           d3 = x;
/*      */           
/*  682 */           if ((z < d6) && (z >= -d6))
/*      */           {
/*  684 */             z = 0.0D;
/*      */           }
/*  686 */           else if (z > 0.0D)
/*      */           {
/*  688 */             z -= d6;
/*      */           }
/*      */           else
/*      */           {
/*  692 */             z += d6;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  697 */       List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(x, y, z));
/*  698 */       AxisAlignedBB axisalignedbb = getEntityBoundingBox();
/*      */       
/*  700 */       for (AxisAlignedBB axisalignedbb1 : list1)
/*      */       {
/*  702 */         y = axisalignedbb1.calculateYOffset(getEntityBoundingBox(), y);
/*      */       }
/*      */       
/*  705 */       setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
/*  706 */       boolean flag1 = (this.onGround) || ((d4 != y) && (d4 < 0.0D));
/*      */       
/*  708 */       for (AxisAlignedBB axisalignedbb2 : list1)
/*      */       {
/*  710 */         x = axisalignedbb2.calculateXOffset(getEntityBoundingBox(), x);
/*      */       }
/*      */       
/*  713 */       setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));
/*      */       
/*  715 */       for (AxisAlignedBB axisalignedbb13 : list1)
/*      */       {
/*  717 */         z = axisalignedbb13.calculateZOffset(getEntityBoundingBox(), z);
/*      */       }
/*      */       
/*  720 */       setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));
/*      */       
/*  722 */       if ((this.stepHeight > 0.0F) && (flag1) && ((d3 != x) || (d5 != z)))
/*      */       {
/*  724 */         double d11 = x;
/*  725 */         double d7 = y;
/*  726 */         double d8 = z;
/*  727 */         AxisAlignedBB axisalignedbb3 = getEntityBoundingBox();
/*  728 */         setEntityBoundingBox(axisalignedbb);
/*  729 */         y = this.stepHeight;
/*  730 */         List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().addCoord(d3, y, d5));
/*  731 */         AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
/*  732 */         AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
/*  733 */         double d9 = y;
/*      */         
/*  735 */         for (AxisAlignedBB axisalignedbb6 : list)
/*      */         {
/*  737 */           d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
/*      */         }
/*      */         
/*  740 */         axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
/*  741 */         double d15 = d3;
/*      */         
/*  743 */         for (AxisAlignedBB axisalignedbb7 : list)
/*      */         {
/*  745 */           d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
/*      */         }
/*      */         
/*  748 */         axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
/*  749 */         double d16 = d5;
/*      */         
/*  751 */         for (AxisAlignedBB axisalignedbb8 : list)
/*      */         {
/*  753 */           d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
/*      */         }
/*      */         
/*  756 */         axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
/*  757 */         AxisAlignedBB axisalignedbb14 = getEntityBoundingBox();
/*  758 */         double d17 = y;
/*      */         
/*  760 */         for (AxisAlignedBB axisalignedbb9 : list)
/*      */         {
/*  762 */           d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
/*      */         }
/*      */         
/*  765 */         axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
/*  766 */         double d18 = d3;
/*      */         
/*  768 */         for (AxisAlignedBB axisalignedbb10 : list)
/*      */         {
/*  770 */           d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
/*      */         }
/*      */         
/*  773 */         axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
/*  774 */         double d19 = d5;
/*      */         
/*  776 */         for (AxisAlignedBB axisalignedbb11 : list)
/*      */         {
/*  778 */           d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
/*      */         }
/*      */         
/*  781 */         axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
/*  782 */         double d20 = d15 * d15 + d16 * d16;
/*  783 */         double d10 = d18 * d18 + d19 * d19;
/*      */         
/*  785 */         if (d20 > d10)
/*      */         {
/*  787 */           x = d15;
/*  788 */           z = d16;
/*  789 */           y = -d9;
/*  790 */           setEntityBoundingBox(axisalignedbb4);
/*      */         }
/*      */         else
/*      */         {
/*  794 */           x = d18;
/*  795 */           z = d19;
/*  796 */           y = -d17;
/*  797 */           setEntityBoundingBox(axisalignedbb14);
/*      */         }
/*      */         
/*  800 */         for (AxisAlignedBB axisalignedbb12 : list)
/*      */         {
/*  802 */           y = axisalignedbb12.calculateYOffset(getEntityBoundingBox(), y);
/*      */         }
/*      */         
/*  805 */         setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
/*      */         
/*  807 */         if (d11 * d11 + d8 * d8 >= x * x + z * z)
/*      */         {
/*  809 */           x = d11;
/*  810 */           y = d7;
/*  811 */           z = d8;
/*  812 */           setEntityBoundingBox(axisalignedbb3);
/*      */         }
/*      */       }
/*      */       
/*  816 */       this.worldObj.theProfiler.endSection();
/*  817 */       this.worldObj.theProfiler.startSection("rest");
/*  818 */       resetPositionToBB();
/*  819 */       this.isCollidedHorizontally = ((d3 != x) || (d5 != z));
/*  820 */       this.isCollidedVertically = (d4 != y);
/*  821 */       this.onGround = ((this.isCollidedVertically) && (d4 < 0.0D));
/*  822 */       this.isCollided = ((this.isCollidedHorizontally) || (this.isCollidedVertically));
/*  823 */       int i = MathHelper.floor_double(this.posX);
/*  824 */       int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  825 */       int k = MathHelper.floor_double(this.posZ);
/*  826 */       BlockPos blockpos = new BlockPos(i, j, k);
/*  827 */       Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
/*      */       
/*  829 */       if (block1.getMaterial() == Material.air)
/*      */       {
/*  831 */         Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*      */         
/*  833 */         if (((block instanceof BlockFence)) || ((block instanceof net.minecraft.block.BlockWall)) || ((block instanceof BlockFenceGate)))
/*      */         {
/*  835 */           block1 = block;
/*  836 */           blockpos = blockpos.down();
/*      */         }
/*      */       }
/*      */       
/*  840 */       updateFallState(y, this.onGround, block1, blockpos);
/*      */       
/*  842 */       if (d3 != x)
/*      */       {
/*  844 */         this.motionX = 0.0D;
/*      */       }
/*      */       
/*  847 */       if (d5 != z)
/*      */       {
/*  849 */         this.motionZ = 0.0D;
/*      */       }
/*      */       
/*  852 */       if (d4 != y)
/*      */       {
/*  854 */         block1.onLanded(this.worldObj, this);
/*      */       }
/*      */       
/*  857 */       if ((canTriggerWalking()) && (!flag) && (this.ridingEntity == null))
/*      */       {
/*  859 */         double d12 = this.posX - d0;
/*  860 */         double d13 = this.posY - d1;
/*  861 */         double d14 = this.posZ - d2;
/*      */         
/*  863 */         if (block1 != Blocks.ladder)
/*      */         {
/*  865 */           d13 = 0.0D;
/*      */         }
/*      */         
/*  868 */         if ((block1 != null) && (this.onGround))
/*      */         {
/*  870 */           block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
/*      */         }
/*      */         
/*  873 */         this.distanceWalkedModified = ((float)(this.distanceWalkedModified + MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D));
/*  874 */         this.distanceWalkedOnStepModified = ((float)(this.distanceWalkedOnStepModified + MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D));
/*      */         
/*  876 */         if ((this.distanceWalkedOnStepModified > this.nextStepDistance) && (block1.getMaterial() != Material.air))
/*      */         {
/*  878 */           this.nextStepDistance = ((int)this.distanceWalkedOnStepModified + 1);
/*      */           
/*  880 */           if (isInWater())
/*      */           {
/*  882 */             float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
/*      */             
/*  884 */             if (f > 1.0F)
/*      */             {
/*  886 */               f = 1.0F;
/*      */             }
/*      */             
/*  889 */             playSound(getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/*      */           }
/*      */           
/*  892 */           playStepSound(blockpos, block1);
/*      */         }
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  898 */         doBlockCollisions();
/*      */       }
/*      */       catch (Throwable throwable)
/*      */       {
/*  902 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
/*  903 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
/*  904 */         addEntityCrashInfo(crashreportcategory);
/*  905 */         throw new ReportedException(crashreport);
/*      */       }
/*      */       
/*  908 */       boolean flag2 = isWet();
/*      */       
/*  910 */       if (this.worldObj.isFlammableWithin(getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D)))
/*      */       {
/*  912 */         dealFireDamage(1);
/*      */         
/*  914 */         if (!flag2)
/*      */         {
/*  916 */           this.fire += 1;
/*      */           
/*  918 */           if (this.fire == 0)
/*      */           {
/*  920 */             setFire(8);
/*      */           }
/*      */         }
/*      */       }
/*  924 */       else if (this.fire <= 0)
/*      */       {
/*  926 */         this.fire = (-this.fireResistance);
/*      */       }
/*      */       
/*  929 */       if ((flag2) && (this.fire > 0))
/*      */       {
/*  931 */         playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/*  932 */         this.fire = (-this.fireResistance);
/*      */       }
/*      */       
/*  935 */       this.worldObj.theProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetPositionToBB()
/*      */   {
/*  944 */     this.posX = ((getEntityBoundingBox().minX + getEntityBoundingBox().maxX) / 2.0D);
/*  945 */     this.posY = getEntityBoundingBox().minY;
/*  946 */     this.posZ = ((getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ) / 2.0D);
/*      */   }
/*      */   
/*      */   protected String getSwimSound()
/*      */   {
/*  951 */     return "game.neutral.swim";
/*      */   }
/*      */   
/*      */   protected void doBlockCollisions()
/*      */   {
/*  956 */     BlockPos blockpos = new BlockPos(getEntityBoundingBox().minX + 0.001D, getEntityBoundingBox().minY + 0.001D, getEntityBoundingBox().minZ + 0.001D);
/*  957 */     BlockPos blockpos1 = new BlockPos(getEntityBoundingBox().maxX - 0.001D, getEntityBoundingBox().maxY - 0.001D, getEntityBoundingBox().maxZ - 0.001D);
/*      */     
/*  959 */     if (this.worldObj.isAreaLoaded(blockpos, blockpos1))
/*      */     {
/*  961 */       for (int i = blockpos.getX(); i <= blockpos1.getX(); i++)
/*      */       {
/*  963 */         for (int j = blockpos.getY(); j <= blockpos1.getY(); j++)
/*      */         {
/*  965 */           for (int k = blockpos.getZ(); k <= blockpos1.getZ(); k++)
/*      */           {
/*  967 */             BlockPos blockpos2 = new BlockPos(i, j, k);
/*  968 */             IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);
/*      */             
/*      */             try
/*      */             {
/*  972 */               iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
/*      */             }
/*      */             catch (Throwable throwable)
/*      */             {
/*  976 */               CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
/*  977 */               CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
/*  978 */               CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
/*  979 */               throw new ReportedException(crashreport);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, Block blockIn)
/*      */   {
/*  989 */     Block.SoundType block$soundtype = blockIn.stepSound;
/*      */     
/*  991 */     if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
/*      */     {
/*  993 */       block$soundtype = Blocks.snow_layer.stepSound;
/*  994 */       playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */     }
/*  996 */     else if (!blockIn.getMaterial().isLiquid())
/*      */     {
/*  998 */       playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */     }
/*      */   }
/*      */   
/*      */   public void playSound(String name, float volume, float pitch)
/*      */   {
/* 1004 */     if (!isSilent())
/*      */     {
/* 1006 */       this.worldObj.playSoundAtEntity(this, name, volume, pitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSilent()
/*      */   {
/* 1015 */     return this.dataWatcher.getWatchableObjectByte(4) == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSilent(boolean isSilent)
/*      */   {
/* 1023 */     this.dataWatcher.updateObject(4, Byte.valueOf((byte)(isSilent ? 1 : 0)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canTriggerWalking()
/*      */   {
/* 1032 */     return true;
/*      */   }
/*      */   
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
/*      */   {
/* 1037 */     if (onGroundIn)
/*      */     {
/* 1039 */       if (this.fallDistance > 0.0F)
/*      */       {
/* 1041 */         if (blockIn != null)
/*      */         {
/* 1043 */           blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
/*      */         }
/*      */         else
/*      */         {
/* 1047 */           fall(this.fallDistance, 1.0F);
/*      */         }
/*      */         
/* 1050 */         this.fallDistance = 0.0F;
/*      */       }
/*      */     }
/* 1053 */     else if (y < 0.0D)
/*      */     {
/* 1055 */       this.fallDistance = ((float)(this.fallDistance - y));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AxisAlignedBB getCollisionBoundingBox()
/*      */   {
/* 1064 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dealFireDamage(int amount)
/*      */   {
/* 1073 */     if (!this.isImmuneToFire)
/*      */     {
/* 1075 */       attackEntityFrom(DamageSource.inFire, amount);
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean isImmuneToFire()
/*      */   {
/* 1081 */     return this.isImmuneToFire;
/*      */   }
/*      */   
/*      */   public void fall(float distance, float damageMultiplier)
/*      */   {
/* 1086 */     if (this.riddenByEntity != null)
/*      */     {
/* 1088 */       this.riddenByEntity.fall(distance, damageMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isWet()
/*      */   {
/* 1097 */     return (this.inWater) || (this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ))) || (this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + this.height, this.posZ)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInWater()
/*      */   {
/* 1106 */     return this.inWater;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean handleWaterMovement()
/*      */   {
/* 1114 */     if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
/*      */     {
/* 1116 */       if ((!this.inWater) && (!this.firstUpdate))
/*      */       {
/* 1118 */         resetHeight();
/*      */       }
/*      */       
/* 1121 */       this.fallDistance = 0.0F;
/* 1122 */       this.inWater = true;
/* 1123 */       this.fire = 0;
/*      */     }
/*      */     else
/*      */     {
/* 1127 */       this.inWater = false;
/*      */     }
/*      */     
/* 1130 */     return this.inWater;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resetHeight()
/*      */   {
/* 1138 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
/*      */     
/* 1140 */     if (f > 1.0F)
/*      */     {
/* 1142 */       f = 1.0F;
/*      */     }
/*      */     
/* 1145 */     playSound(getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/* 1146 */     float f1 = MathHelper.floor_double(getEntityBoundingBox().minY);
/*      */     
/* 1148 */     for (int i = 0; i < 1.0F + this.width * 20.0F; i++)
/*      */     {
/* 1150 */       float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/* 1151 */       float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/* 1152 */       this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f2, f1 + 1.0F, this.posZ + f3, this.motionX, this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ, new int[0]);
/*      */     }
/*      */     
/* 1155 */     for (int j = 0; j < 1.0F + this.width * 20.0F; j++)
/*      */     {
/* 1157 */       float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/* 1158 */       float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
/* 1159 */       this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f4, f1 + 1.0F, this.posZ + f5, this.motionX, this.motionY, this.motionZ, new int[0]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void spawnRunningParticles()
/*      */   {
/* 1168 */     if ((isSprinting()) && (!isInWater()))
/*      */     {
/* 1170 */       createRunningParticles();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void createRunningParticles()
/*      */   {
/* 1176 */     int i = MathHelper.floor_double(this.posX);
/* 1177 */     int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/* 1178 */     int k = MathHelper.floor_double(this.posZ);
/* 1179 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 1180 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 1181 */     Block block = iblockstate.getBlock();
/*      */     
/* 1183 */     if (block.getRenderType() != -1)
/*      */     {
/* 1185 */       this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(iblockstate) });
/*      */     }
/*      */   }
/*      */   
/*      */   protected String getSplashSound()
/*      */   {
/* 1191 */     return "game.neutral.swim.splash";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInsideOfMaterial(Material materialIn)
/*      */   {
/* 1199 */     double d0 = this.posY + getEyeHeight();
/* 1200 */     BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
/* 1201 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 1202 */     Block block = iblockstate.getBlock();
/*      */     
/* 1204 */     if (block.getMaterial() == materialIn)
/*      */     {
/* 1206 */       float f = net.minecraft.block.BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
/* 1207 */       float f1 = blockpos.getY() + 1 - f;
/* 1208 */       boolean flag = d0 < f1;
/* 1209 */       return (!flag) && ((this instanceof EntityPlayer)) ? false : flag;
/*      */     }
/*      */     
/*      */ 
/* 1213 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isInLava()
/*      */   {
/* 1219 */     return this.worldObj.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void moveFlying(float strafe, float forward, float friction)
/*      */   {
/* 1227 */     float f = strafe * strafe + forward * forward;
/*      */     
/* 1229 */     if (f >= 1.0E-4F)
/*      */     {
/* 1231 */       f = MathHelper.sqrt_float(f);
/*      */       
/* 1233 */       if (f < 1.0F)
/*      */       {
/* 1235 */         f = 1.0F;
/*      */       }
/*      */       
/* 1238 */       f = friction / f;
/* 1239 */       strafe *= f;
/* 1240 */       forward *= f;
/* 1241 */       float f1 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
/* 1242 */       float f2 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 1243 */       this.motionX += strafe * f2 - forward * f1;
/* 1244 */       this.motionZ += forward * f2 + strafe * f1;
/*      */     }
/*      */   }
/*      */   
/*      */   public int getBrightnessForRender(float partialTicks)
/*      */   {
/* 1250 */     BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
/* 1251 */     return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getBrightness(float partialTicks)
/*      */   {
/* 1259 */     BlockPos blockpos = new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ);
/* 1260 */     return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWorld(World worldIn)
/*      */   {
/* 1268 */     this.worldObj = worldIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
/*      */   {
/* 1276 */     this.prevPosX = (this.posX = x);
/* 1277 */     this.prevPosY = (this.posY = y);
/* 1278 */     this.prevPosZ = (this.posZ = z);
/* 1279 */     this.prevRotationYaw = (this.rotationYaw = yaw);
/* 1280 */     this.prevRotationPitch = (this.rotationPitch = pitch);
/* 1281 */     double d0 = this.prevRotationYaw - yaw;
/*      */     
/* 1283 */     if (d0 < -180.0D)
/*      */     {
/* 1285 */       this.prevRotationYaw += 360.0F;
/*      */     }
/*      */     
/* 1288 */     if (d0 >= 180.0D)
/*      */     {
/* 1290 */       this.prevRotationYaw -= 360.0F;
/*      */     }
/*      */     
/* 1293 */     setPosition(this.posX, this.posY, this.posZ);
/* 1294 */     setRotation(yaw, pitch);
/*      */   }
/*      */   
/*      */   public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn)
/*      */   {
/* 1299 */     setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
/*      */   {
/* 1307 */     this.lastTickPosX = (this.prevPosX = this.posX = x);
/* 1308 */     this.lastTickPosY = (this.prevPosY = this.posY = y);
/* 1309 */     this.lastTickPosZ = (this.prevPosZ = this.posZ = z);
/* 1310 */     this.rotationYaw = yaw;
/* 1311 */     this.rotationPitch = pitch;
/* 1312 */     setPosition(this.posX, this.posY, this.posZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getDistanceToEntity(Entity entityIn)
/*      */   {
/* 1320 */     float f = (float)(this.posX - entityIn.posX);
/* 1321 */     float f1 = (float)(this.posY - entityIn.posY);
/* 1322 */     float f2 = (float)(this.posZ - entityIn.posZ);
/* 1323 */     return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDistanceSq(double x, double y, double z)
/*      */   {
/* 1331 */     double d0 = this.posX - x;
/* 1332 */     double d1 = this.posY - y;
/* 1333 */     double d2 = this.posZ - z;
/* 1334 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*      */   }
/*      */   
/*      */   public double getDistanceSq(BlockPos pos)
/*      */   {
/* 1339 */     return pos.distanceSq(this.posX, this.posY, this.posZ);
/*      */   }
/*      */   
/*      */   public double getDistanceSqToCenter(BlockPos pos)
/*      */   {
/* 1344 */     return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDistance(double x, double y, double z)
/*      */   {
/* 1352 */     double d0 = this.posX - x;
/* 1353 */     double d1 = this.posY - y;
/* 1354 */     double d2 = this.posZ - z;
/* 1355 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDistanceSqToEntity(Entity entityIn)
/*      */   {
/* 1363 */     double d0 = this.posX - entityIn.posX;
/* 1364 */     double d1 = this.posY - entityIn.posY;
/* 1365 */     double d2 = this.posZ - entityIn.posZ;
/* 1366 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onCollideWithPlayer(EntityPlayer entityIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void applyEntityCollision(Entity entityIn)
/*      */   {
/* 1381 */     if ((entityIn.riddenByEntity != this) && (entityIn.ridingEntity != this))
/*      */     {
/* 1383 */       if ((!entityIn.noClip) && (!this.noClip))
/*      */       {
/* 1385 */         double d0 = entityIn.posX - this.posX;
/* 1386 */         double d1 = entityIn.posZ - this.posZ;
/* 1387 */         double d2 = MathHelper.abs_max(d0, d1);
/*      */         
/* 1389 */         if (d2 >= 0.009999999776482582D)
/*      */         {
/* 1391 */           d2 = MathHelper.sqrt_double(d2);
/* 1392 */           d0 /= d2;
/* 1393 */           d1 /= d2;
/* 1394 */           double d3 = 1.0D / d2;
/*      */           
/* 1396 */           if (d3 > 1.0D)
/*      */           {
/* 1398 */             d3 = 1.0D;
/*      */           }
/*      */           
/* 1401 */           d0 *= d3;
/* 1402 */           d1 *= d3;
/* 1403 */           d0 *= 0.05000000074505806D;
/* 1404 */           d1 *= 0.05000000074505806D;
/* 1405 */           d0 *= (1.0F - this.entityCollisionReduction);
/* 1406 */           d1 *= (1.0F - this.entityCollisionReduction);
/*      */           
/* 1408 */           if (this.riddenByEntity == null)
/*      */           {
/* 1410 */             addVelocity(-d0, 0.0D, -d1);
/*      */           }
/*      */           
/* 1413 */           if (entityIn.riddenByEntity == null)
/*      */           {
/* 1415 */             entityIn.addVelocity(d0, 0.0D, d1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addVelocity(double x, double y, double z)
/*      */   {
/* 1427 */     this.motionX += x;
/* 1428 */     this.motionY += y;
/* 1429 */     this.motionZ += z;
/* 1430 */     this.isAirBorne = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setBeenAttacked()
/*      */   {
/* 1438 */     this.velocityChanged = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/* 1446 */     if (isEntityInvulnerable(source))
/*      */     {
/* 1448 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1452 */     setBeenAttacked();
/* 1453 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getLook(float partialTicks)
/*      */   {
/* 1462 */     if (partialTicks == 1.0F)
/*      */     {
/* 1464 */       return getVectorForRotation(this.rotationPitch, this.rotationYaw);
/*      */     }
/*      */     
/*      */ 
/* 1468 */     float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
/* 1469 */     float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
/* 1470 */     return getVectorForRotation(f, f1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Vec3 getVectorForRotation(float pitch, float yaw)
/*      */   {
/* 1479 */     float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
/* 1480 */     float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
/* 1481 */     float f2 = -MathHelper.cos(-pitch * 0.017453292F);
/* 1482 */     float f3 = MathHelper.sin(-pitch * 0.017453292F);
/* 1483 */     return new Vec3(f1 * f2, f3, f * f2);
/*      */   }
/*      */   
/*      */   public Vec3 getPositionEyes(float partialTicks)
/*      */   {
/* 1488 */     if (partialTicks == 1.0F)
/*      */     {
/* 1490 */       return new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ);
/*      */     }
/*      */     
/*      */ 
/* 1494 */     double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
/* 1495 */     double d1 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + getEyeHeight();
/* 1496 */     double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
/* 1497 */     return new Vec3(d0, d1, d2);
/*      */   }
/*      */   
/*      */ 
/*      */   public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks)
/*      */   {
/* 1503 */     Vec3 vec3 = getPositionEyes(partialTicks);
/* 1504 */     Vec3 vec31 = getLook(partialTicks);
/* 1505 */     Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
/* 1506 */     return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBeCollidedWith()
/*      */   {
/* 1514 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBePushed()
/*      */   {
/* 1522 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addToPlayerScore(Entity entityIn, int amount) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInRangeToRender3d(double x, double y, double z)
/*      */   {
/* 1535 */     double d0 = this.posX - x;
/* 1536 */     double d1 = this.posY - y;
/* 1537 */     double d2 = this.posZ - z;
/* 1538 */     double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 1539 */     return isInRangeToRenderDist(d3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInRangeToRenderDist(double distance)
/*      */   {
/* 1548 */     double d0 = getEntityBoundingBox().getAverageEdgeLength();
/*      */     
/* 1550 */     if (Double.isNaN(d0))
/*      */     {
/* 1552 */       d0 = 1.0D;
/*      */     }
/*      */     
/* 1555 */     d0 = d0 * 64.0D * this.renderDistanceWeight;
/* 1556 */     return distance < d0 * d0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean writeMountToNBT(NBTTagCompound tagCompund)
/*      */   {
/* 1565 */     String s = getEntityString();
/*      */     
/* 1567 */     if ((!this.isDead) && (s != null))
/*      */     {
/* 1569 */       tagCompund.setString("id", s);
/* 1570 */       writeToNBT(tagCompund);
/* 1571 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1575 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean writeToNBTOptional(NBTTagCompound tagCompund)
/*      */   {
/* 1586 */     String s = getEntityString();
/*      */     
/* 1588 */     if ((!this.isDead) && (s != null) && (this.riddenByEntity == null))
/*      */     {
/* 1590 */       tagCompund.setString("id", s);
/* 1591 */       writeToNBT(tagCompund);
/* 1592 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1596 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeToNBT(NBTTagCompound tagCompund)
/*      */   {
/*      */     try
/*      */     {
/* 1607 */       tagCompund.setTag("Pos", newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
/* 1608 */       tagCompund.setTag("Motion", newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
/* 1609 */       tagCompund.setTag("Rotation", newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
/* 1610 */       tagCompund.setFloat("FallDistance", this.fallDistance);
/* 1611 */       tagCompund.setShort("Fire", (short)this.fire);
/* 1612 */       tagCompund.setShort("Air", (short)getAir());
/* 1613 */       tagCompund.setBoolean("OnGround", this.onGround);
/* 1614 */       tagCompund.setInteger("Dimension", this.dimension);
/* 1615 */       tagCompund.setBoolean("Invulnerable", this.invulnerable);
/* 1616 */       tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
/* 1617 */       tagCompund.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
/* 1618 */       tagCompund.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());
/*      */       
/* 1620 */       if ((getCustomNameTag() != null) && (getCustomNameTag().length() > 0))
/*      */       {
/* 1622 */         tagCompund.setString("CustomName", getCustomNameTag());
/* 1623 */         tagCompund.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
/*      */       }
/*      */       
/* 1626 */       this.cmdResultStats.writeStatsToNBT(tagCompund);
/*      */       
/* 1628 */       if (isSilent())
/*      */       {
/* 1630 */         tagCompund.setBoolean("Silent", isSilent());
/*      */       }
/*      */       
/* 1633 */       writeEntityToNBT(tagCompund);
/*      */       
/* 1635 */       if (this.ridingEntity != null)
/*      */       {
/* 1637 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*      */         
/* 1639 */         if (this.ridingEntity.writeMountToNBT(nbttagcompound))
/*      */         {
/* 1641 */           tagCompund.setTag("Riding", nbttagcompound);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 1647 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
/* 1648 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
/* 1649 */       addEntityCrashInfo(crashreportcategory);
/* 1650 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*      */     try
/*      */     {
/* 1661 */       NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
/* 1662 */       NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
/* 1663 */       NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
/* 1664 */       this.motionX = nbttaglist1.getDoubleAt(0);
/* 1665 */       this.motionY = nbttaglist1.getDoubleAt(1);
/* 1666 */       this.motionZ = nbttaglist1.getDoubleAt(2);
/*      */       
/* 1668 */       if (Math.abs(this.motionX) > 10.0D)
/*      */       {
/* 1670 */         this.motionX = 0.0D;
/*      */       }
/*      */       
/* 1673 */       if (Math.abs(this.motionY) > 10.0D)
/*      */       {
/* 1675 */         this.motionY = 0.0D;
/*      */       }
/*      */       
/* 1678 */       if (Math.abs(this.motionZ) > 10.0D)
/*      */       {
/* 1680 */         this.motionZ = 0.0D;
/*      */       }
/*      */       
/* 1683 */       this.prevPosX = (this.lastTickPosX = this.posX = nbttaglist.getDoubleAt(0));
/* 1684 */       this.prevPosY = (this.lastTickPosY = this.posY = nbttaglist.getDoubleAt(1));
/* 1685 */       this.prevPosZ = (this.lastTickPosZ = this.posZ = nbttaglist.getDoubleAt(2));
/* 1686 */       this.prevRotationYaw = (this.rotationYaw = nbttaglist2.getFloatAt(0));
/* 1687 */       this.prevRotationPitch = (this.rotationPitch = nbttaglist2.getFloatAt(1));
/* 1688 */       setRotationYawHead(this.rotationYaw);
/* 1689 */       func_181013_g(this.rotationYaw);
/* 1690 */       this.fallDistance = tagCompund.getFloat("FallDistance");
/* 1691 */       this.fire = tagCompund.getShort("Fire");
/* 1692 */       setAir(tagCompund.getShort("Air"));
/* 1693 */       this.onGround = tagCompund.getBoolean("OnGround");
/* 1694 */       this.dimension = tagCompund.getInteger("Dimension");
/* 1695 */       this.invulnerable = tagCompund.getBoolean("Invulnerable");
/* 1696 */       this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
/*      */       
/* 1698 */       if ((tagCompund.hasKey("UUIDMost", 4)) && (tagCompund.hasKey("UUIDLeast", 4)))
/*      */       {
/* 1700 */         this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
/*      */       }
/* 1702 */       else if (tagCompund.hasKey("UUID", 8))
/*      */       {
/* 1704 */         this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
/*      */       }
/*      */       
/* 1707 */       setPosition(this.posX, this.posY, this.posZ);
/* 1708 */       setRotation(this.rotationYaw, this.rotationPitch);
/*      */       
/* 1710 */       if ((tagCompund.hasKey("CustomName", 8)) && (tagCompund.getString("CustomName").length() > 0))
/*      */       {
/* 1712 */         setCustomNameTag(tagCompund.getString("CustomName"));
/*      */       }
/*      */       
/* 1715 */       setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
/* 1716 */       this.cmdResultStats.readStatsFromNBT(tagCompund);
/* 1717 */       setSilent(tagCompund.getBoolean("Silent"));
/* 1718 */       readEntityFromNBT(tagCompund);
/*      */       
/* 1720 */       if (shouldSetPosAfterLoading())
/*      */       {
/* 1722 */         setPosition(this.posX, this.posY, this.posZ);
/*      */       }
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 1727 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
/* 1728 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
/* 1729 */       addEntityCrashInfo(crashreportcategory);
/* 1730 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean shouldSetPosAfterLoading()
/*      */   {
/* 1736 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String getEntityString()
/*      */   {
/* 1744 */     return EntityList.getEntityString(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void readEntityFromNBT(NBTTagCompound paramNBTTagCompound);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void writeEntityToNBT(NBTTagCompound paramNBTTagCompound);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onChunkLoad() {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected NBTTagList newDoubleNBTList(double... numbers)
/*      */   {
/* 1766 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     double[] arrayOfDouble;
/* 1768 */     int j = (arrayOfDouble = numbers).length; for (int i = 0; i < j; i++) { double d0 = arrayOfDouble[i];
/*      */       
/* 1770 */       nbttaglist.appendTag(new net.minecraft.nbt.NBTTagDouble(d0));
/*      */     }
/*      */     
/* 1773 */     return nbttaglist;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected NBTTagList newFloatNBTList(float... numbers)
/*      */   {
/* 1781 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     float[] arrayOfFloat;
/* 1783 */     int j = (arrayOfFloat = numbers).length; for (int i = 0; i < j; i++) { float f = arrayOfFloat[i];
/*      */       
/* 1785 */       nbttaglist.appendTag(new net.minecraft.nbt.NBTTagFloat(f));
/*      */     }
/*      */     
/* 1788 */     return nbttaglist;
/*      */   }
/*      */   
/*      */   public EntityItem dropItem(Item itemIn, int size)
/*      */   {
/* 1793 */     return dropItemWithOffset(itemIn, size, 0.0F);
/*      */   }
/*      */   
/*      */   public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY)
/*      */   {
/* 1798 */     return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY)
/*      */   {
/* 1806 */     if ((itemStackIn.stackSize != 0) && (itemStackIn.getItem() != null))
/*      */     {
/* 1808 */       EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
/* 1809 */       entityitem.setDefaultPickupDelay();
/* 1810 */       this.worldObj.spawnEntityInWorld(entityitem);
/* 1811 */       return entityitem;
/*      */     }
/*      */     
/*      */ 
/* 1815 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityAlive()
/*      */   {
/* 1824 */     return !this.isDead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityInsideOpaqueBlock()
/*      */   {
/* 1832 */     if (this.noClip)
/*      */     {
/* 1834 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1838 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
/*      */     
/* 1840 */     for (int i = 0; i < 8; i++)
/*      */     {
/* 1842 */       int j = MathHelper.floor_double(this.posY + ((i >> 0) % 2 - 0.5F) * 0.1F + getEyeHeight());
/* 1843 */       int k = MathHelper.floor_double(this.posX + ((i >> 1) % 2 - 0.5F) * this.width * 0.8F);
/* 1844 */       int l = MathHelper.floor_double(this.posZ + ((i >> 2) % 2 - 0.5F) * this.width * 0.8F);
/*      */       
/* 1846 */       if ((blockpos$mutableblockpos.getX() != k) || (blockpos$mutableblockpos.getY() != j) || (blockpos$mutableblockpos.getZ() != l))
/*      */       {
/* 1848 */         blockpos$mutableblockpos.func_181079_c(k, j, l);
/*      */         
/* 1850 */         if (this.worldObj.getBlockState(blockpos$mutableblockpos).getBlock().isVisuallyOpaque())
/*      */         {
/* 1852 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1857 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean interactFirst(EntityPlayer playerIn)
/*      */   {
/* 1866 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AxisAlignedBB getCollisionBox(Entity entityIn)
/*      */   {
/* 1875 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateRidden()
/*      */   {
/* 1883 */     if (this.ridingEntity.isDead)
/*      */     {
/* 1885 */       this.ridingEntity = null;
/*      */     }
/*      */     else
/*      */     {
/* 1889 */       this.motionX = 0.0D;
/* 1890 */       this.motionY = 0.0D;
/* 1891 */       this.motionZ = 0.0D;
/* 1892 */       onUpdate();
/*      */       
/* 1894 */       if (this.ridingEntity != null)
/*      */       {
/* 1896 */         this.ridingEntity.updateRiderPosition();
/* 1897 */         this.entityRiderYawDelta += this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw;
/*      */         
/* 1899 */         for (this.entityRiderPitchDelta += this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch; this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1904 */         while (this.entityRiderYawDelta < -180.0D)
/*      */         {
/* 1906 */           this.entityRiderYawDelta += 360.0D;
/*      */         }
/*      */         
/* 1909 */         while (this.entityRiderPitchDelta >= 180.0D)
/*      */         {
/* 1911 */           this.entityRiderPitchDelta -= 360.0D;
/*      */         }
/*      */         
/* 1914 */         while (this.entityRiderPitchDelta < -180.0D)
/*      */         {
/* 1916 */           this.entityRiderPitchDelta += 360.0D;
/*      */         }
/*      */         
/* 1919 */         double d0 = this.entityRiderYawDelta * 0.5D;
/* 1920 */         double d1 = this.entityRiderPitchDelta * 0.5D;
/* 1921 */         float f = 10.0F;
/*      */         
/* 1923 */         if (d0 > f)
/*      */         {
/* 1925 */           d0 = f;
/*      */         }
/*      */         
/* 1928 */         if (d0 < -f)
/*      */         {
/* 1930 */           d0 = -f;
/*      */         }
/*      */         
/* 1933 */         if (d1 > f)
/*      */         {
/* 1935 */           d1 = f;
/*      */         }
/*      */         
/* 1938 */         if (d1 < -f)
/*      */         {
/* 1940 */           d1 = -f;
/*      */         }
/*      */         
/* 1943 */         this.entityRiderYawDelta -= d0;
/* 1944 */         this.entityRiderPitchDelta -= d1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateRiderPosition()
/*      */   {
/* 1951 */     if (this.riddenByEntity != null)
/*      */     {
/* 1953 */       this.riddenByEntity.setPosition(this.posX, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getYOffset()
/*      */   {
/* 1962 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getMountedYOffset()
/*      */   {
/* 1970 */     return this.height * 0.75D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void mountEntity(Entity entityIn)
/*      */   {
/* 1978 */     this.entityRiderPitchDelta = 0.0D;
/* 1979 */     this.entityRiderYawDelta = 0.0D;
/*      */     
/* 1981 */     if (entityIn == null)
/*      */     {
/* 1983 */       if (this.ridingEntity != null)
/*      */       {
/* 1985 */         setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
/* 1986 */         this.ridingEntity.riddenByEntity = null;
/*      */       }
/*      */       
/* 1989 */       this.ridingEntity = null;
/*      */     }
/*      */     else
/*      */     {
/* 1993 */       if (this.ridingEntity != null)
/*      */       {
/* 1995 */         this.ridingEntity.riddenByEntity = null;
/*      */       }
/*      */       
/* 1998 */       if (entityIn != null)
/*      */       {
/* 2000 */         for (Entity entity = entityIn.ridingEntity; entity != null; entity = entity.ridingEntity)
/*      */         {
/* 2002 */           if (entity == this)
/*      */           {
/* 2004 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2009 */       this.ridingEntity = entityIn;
/* 2010 */       entityIn.riddenByEntity = this;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*      */   {
/* 2016 */     setPosition(x, y, z);
/* 2017 */     setRotation(yaw, pitch);
/* 2018 */     List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));
/*      */     
/* 2020 */     if (!list.isEmpty())
/*      */     {
/* 2022 */       double d0 = 0.0D;
/*      */       
/* 2024 */       for (AxisAlignedBB axisalignedbb : list)
/*      */       {
/* 2026 */         if (axisalignedbb.maxY > d0)
/*      */         {
/* 2028 */           d0 = axisalignedbb.maxY;
/*      */         }
/*      */       }
/*      */       
/* 2032 */       y += d0 - getEntityBoundingBox().minY;
/* 2033 */       setPosition(x, y, z);
/*      */     }
/*      */   }
/*      */   
/*      */   public float getCollisionBorderSize()
/*      */   {
/* 2039 */     return 0.1F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getLookVec()
/*      */   {
/* 2047 */     return null;
/*      */   }
/*      */   
/*      */   public void func_181015_d(BlockPos p_181015_1_)
/*      */   {
/* 2052 */     if (this.timeUntilPortal > 0)
/*      */     {
/* 2054 */       this.timeUntilPortal = getPortalCooldown();
/*      */     }
/*      */     else
/*      */     {
/* 2058 */       if ((!this.worldObj.isRemote) && (!p_181015_1_.equals(this.field_181016_an)))
/*      */       {
/* 2060 */         this.field_181016_an = p_181015_1_;
/* 2061 */         BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, p_181015_1_);
/* 2062 */         double d0 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? blockpattern$patternhelper.func_181117_a().getZ() : blockpattern$patternhelper.func_181117_a().getX();
/* 2063 */         double d1 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
/* 2064 */         d1 = Math.abs(MathHelper.func_181160_c(d1 - (blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == net.minecraft.util.EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - blockpattern$patternhelper.func_181118_d()));
/* 2065 */         double d2 = MathHelper.func_181160_c(this.posY - 1.0D, blockpattern$patternhelper.func_181117_a().getY(), blockpattern$patternhelper.func_181117_a().getY() - blockpattern$patternhelper.func_181119_e());
/* 2066 */         this.field_181017_ao = new Vec3(d1, d2, 0.0D);
/* 2067 */         this.field_181018_ap = blockpattern$patternhelper.getFinger();
/*      */       }
/*      */       
/* 2070 */       this.inPortal = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPortalCooldown()
/*      */   {
/* 2079 */     return 300;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVelocity(double x, double y, double z)
/*      */   {
/* 2087 */     this.motionX = x;
/* 2088 */     this.motionY = y;
/* 2089 */     this.motionZ = z;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void handleStatusUpdate(byte id) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void performHurtAnimation() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack[] getInventory()
/*      */   {
/* 2108 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBurning()
/*      */   {
/* 2123 */     boolean flag = (this.worldObj != null) && (this.worldObj.isRemote);
/* 2124 */     return (!this.isImmuneToFire) && ((this.fire > 0) || ((flag) && (getFlag(0))));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRiding()
/*      */   {
/* 2133 */     return this.ridingEntity != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSneaking()
/*      */   {
/* 2141 */     return getFlag(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSneaking(boolean sneaking)
/*      */   {
/* 2149 */     setFlag(1, sneaking);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSprinting()
/*      */   {
/* 2157 */     return getFlag(3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSprinting(boolean sprinting)
/*      */   {
/* 2165 */     setFlag(3, sprinting);
/*      */   }
/*      */   
/*      */   public boolean isInvisible()
/*      */   {
/* 2170 */     return getFlag(5);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInvisibleToPlayer(EntityPlayer player)
/*      */   {
/* 2180 */     return player.isSpectator() ? false : isInvisible();
/*      */   }
/*      */   
/*      */   public void setInvisible(boolean invisible)
/*      */   {
/* 2185 */     setFlag(5, invisible);
/*      */   }
/*      */   
/*      */   public boolean isEating()
/*      */   {
/* 2190 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void setEating(boolean eating)
/*      */   {
/* 2195 */     setFlag(4, eating);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean getFlag(int flag)
/*      */   {
/* 2204 */     return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setFlag(int flag, boolean set)
/*      */   {
/* 2212 */     byte b0 = this.dataWatcher.getWatchableObjectByte(0);
/*      */     
/* 2214 */     if (set)
/*      */     {
/* 2216 */       this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 | 1 << flag)));
/*      */     }
/*      */     else
/*      */     {
/* 2220 */       this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 & (1 << flag ^ 0xFFFFFFFF))));
/*      */     }
/*      */   }
/*      */   
/*      */   public int getAir()
/*      */   {
/* 2226 */     return this.dataWatcher.getWatchableObjectShort(1);
/*      */   }
/*      */   
/*      */   public void setAir(int air)
/*      */   {
/* 2231 */     this.dataWatcher.updateObject(1, Short.valueOf((short)air));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onStruckByLightning(EntityLightningBolt lightningBolt)
/*      */   {
/* 2239 */     attackEntityFrom(DamageSource.lightningBolt, 5.0F);
/* 2240 */     this.fire += 1;
/*      */     
/* 2242 */     if (this.fire == 0)
/*      */     {
/* 2244 */       setFire(8);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onKillEntity(EntityLivingBase entityLivingIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean pushOutOfBlocks(double x, double y, double z)
/*      */   {
/* 2257 */     BlockPos blockpos = new BlockPos(x, y, z);
/* 2258 */     double d0 = x - blockpos.getX();
/* 2259 */     double d1 = y - blockpos.getY();
/* 2260 */     double d2 = z - blockpos.getZ();
/* 2261 */     List<AxisAlignedBB> list = this.worldObj.func_147461_a(getEntityBoundingBox());
/*      */     
/* 2263 */     if ((list.isEmpty()) && (!this.worldObj.isBlockFullCube(blockpos)))
/*      */     {
/* 2265 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2269 */     int i = 3;
/* 2270 */     double d3 = 9999.0D;
/*      */     
/* 2272 */     if ((!this.worldObj.isBlockFullCube(blockpos.west())) && (d0 < d3))
/*      */     {
/* 2274 */       d3 = d0;
/* 2275 */       i = 0;
/*      */     }
/*      */     
/* 2278 */     if ((!this.worldObj.isBlockFullCube(blockpos.east())) && (1.0D - d0 < d3))
/*      */     {
/* 2280 */       d3 = 1.0D - d0;
/* 2281 */       i = 1;
/*      */     }
/*      */     
/* 2284 */     if ((!this.worldObj.isBlockFullCube(blockpos.up())) && (1.0D - d1 < d3))
/*      */     {
/* 2286 */       d3 = 1.0D - d1;
/* 2287 */       i = 3;
/*      */     }
/*      */     
/* 2290 */     if ((!this.worldObj.isBlockFullCube(blockpos.north())) && (d2 < d3))
/*      */     {
/* 2292 */       d3 = d2;
/* 2293 */       i = 4;
/*      */     }
/*      */     
/* 2296 */     if ((!this.worldObj.isBlockFullCube(blockpos.south())) && (1.0D - d2 < d3))
/*      */     {
/* 2298 */       d3 = 1.0D - d2;
/* 2299 */       i = 5;
/*      */     }
/*      */     
/* 2302 */     float f = this.rand.nextFloat() * 0.2F + 0.1F;
/*      */     
/* 2304 */     if (i == 0)
/*      */     {
/* 2306 */       this.motionX = (-f);
/*      */     }
/*      */     
/* 2309 */     if (i == 1)
/*      */     {
/* 2311 */       this.motionX = f;
/*      */     }
/*      */     
/* 2314 */     if (i == 3)
/*      */     {
/* 2316 */       this.motionY = f;
/*      */     }
/*      */     
/* 2319 */     if (i == 4)
/*      */     {
/* 2321 */       this.motionZ = (-f);
/*      */     }
/*      */     
/* 2324 */     if (i == 5)
/*      */     {
/* 2326 */       this.motionZ = f;
/*      */     }
/*      */     
/* 2329 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInWeb()
/*      */   {
/* 2338 */     this.isInWeb = true;
/* 2339 */     this.fallDistance = 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/* 2347 */     if (hasCustomName())
/*      */     {
/* 2349 */       return getCustomNameTag();
/*      */     }
/*      */     
/*      */ 
/* 2353 */     String s = EntityList.getEntityString(this);
/*      */     
/* 2355 */     if (s == null)
/*      */     {
/* 2357 */       s = "generic";
/*      */     }
/*      */     
/* 2360 */     return net.minecraft.util.StatCollector.translateToLocal("entity." + s + ".name");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Entity[] getParts()
/*      */   {
/* 2369 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityEqual(Entity entityIn)
/*      */   {
/* 2377 */     return this == entityIn;
/*      */   }
/*      */   
/*      */   public float getRotationYawHead()
/*      */   {
/* 2382 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRotationYawHead(float rotation) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void func_181013_g(float p_181013_1_) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canAttackWithItem()
/*      */   {
/* 2401 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hitByEntity(Entity entityIn)
/*      */   {
/* 2409 */     return false;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 2414 */     return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) });
/*      */   }
/*      */   
/*      */   public boolean isEntityInvulnerable(DamageSource source)
/*      */   {
/* 2419 */     return (this.invulnerable) && (source != DamageSource.outOfWorld) && (!source.isCreativePlayer());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyLocationAndAnglesFrom(Entity entityIn)
/*      */   {
/* 2427 */     setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyDataFromOld(Entity entityIn)
/*      */   {
/* 2435 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2436 */     entityIn.writeToNBT(nbttagcompound);
/* 2437 */     readFromNBT(nbttagcompound);
/* 2438 */     this.timeUntilPortal = entityIn.timeUntilPortal;
/* 2439 */     this.field_181016_an = entityIn.field_181016_an;
/* 2440 */     this.field_181017_ao = entityIn.field_181017_ao;
/* 2441 */     this.field_181018_ap = entityIn.field_181018_ap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void travelToDimension(int dimensionId)
/*      */   {
/* 2449 */     if ((!this.worldObj.isRemote) && (!this.isDead))
/*      */     {
/* 2451 */       this.worldObj.theProfiler.startSection("changeDimension");
/* 2452 */       MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 2453 */       int i = this.dimension;
/* 2454 */       WorldServer worldserver = minecraftserver.worldServerForDimension(i);
/* 2455 */       WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
/* 2456 */       this.dimension = dimensionId;
/*      */       
/* 2458 */       if ((i == 1) && (dimensionId == 1))
/*      */       {
/* 2460 */         worldserver1 = minecraftserver.worldServerForDimension(0);
/* 2461 */         this.dimension = 0;
/*      */       }
/*      */       
/* 2464 */       this.worldObj.removeEntity(this);
/* 2465 */       this.isDead = false;
/* 2466 */       this.worldObj.theProfiler.startSection("reposition");
/* 2467 */       minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver1);
/* 2468 */       this.worldObj.theProfiler.endStartSection("reloading");
/* 2469 */       Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);
/*      */       
/* 2471 */       if (entity != null)
/*      */       {
/* 2473 */         entity.copyDataFromOld(this);
/*      */         
/* 2475 */         if ((i == 1) && (dimensionId == 1))
/*      */         {
/* 2477 */           BlockPos blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
/* 2478 */           entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
/*      */         }
/*      */         
/* 2481 */         worldserver1.spawnEntityInWorld(entity);
/*      */       }
/*      */       
/* 2484 */       this.isDead = true;
/* 2485 */       this.worldObj.theProfiler.endSection();
/* 2486 */       worldserver.resetUpdateEntityTick();
/* 2487 */       worldserver1.resetUpdateEntityTick();
/* 2488 */       this.worldObj.theProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
/*      */   {
/* 2497 */     return blockStateIn.getBlock().getExplosionResistance(this);
/*      */   }
/*      */   
/*      */   public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
/*      */   {
/* 2502 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxFallHeight()
/*      */   {
/* 2510 */     return 3;
/*      */   }
/*      */   
/*      */   public Vec3 func_181014_aG()
/*      */   {
/* 2515 */     return this.field_181017_ao;
/*      */   }
/*      */   
/*      */   public EnumFacing func_181012_aH()
/*      */   {
/* 2520 */     return this.field_181018_ap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean doesEntityNotTriggerPressurePlate()
/*      */   {
/* 2528 */     return false;
/*      */   }
/*      */   
/*      */   public void addEntityCrashInfo(CrashReportCategory category)
/*      */   {
/* 2533 */     category.addCrashSectionCallable("Entity Type", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2537 */         return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
/*      */       }
/* 2539 */     });
/* 2540 */     category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
/* 2541 */     category.addCrashSectionCallable("Entity Name", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2545 */         return Entity.this.getName();
/*      */       }
/* 2547 */     });
/* 2548 */     category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) }));
/* 2549 */     category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
/* 2550 */     category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ) }));
/* 2551 */     category.addCrashSectionCallable("Entity's Rider", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2555 */         return Entity.this.riddenByEntity.toString();
/*      */       }
/* 2557 */     });
/* 2558 */     category.addCrashSectionCallable("Entity's Vehicle", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2562 */         return Entity.this.ridingEntity.toString();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canRenderOnFire()
/*      */   {
/* 2572 */     return isBurning();
/*      */   }
/*      */   
/*      */   public UUID getUniqueID()
/*      */   {
/* 2577 */     return this.entityUniqueID;
/*      */   }
/*      */   
/*      */   public boolean isPushedByWater()
/*      */   {
/* 2582 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChatComponent getDisplayName()
/*      */   {
/* 2590 */     ChatComponentText chatcomponenttext = new ChatComponentText(getName());
/* 2591 */     chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 2592 */     chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/* 2593 */     return chatcomponenttext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomNameTag(String name)
/*      */   {
/* 2601 */     this.dataWatcher.updateObject(2, name);
/*      */   }
/*      */   
/*      */   public String getCustomNameTag()
/*      */   {
/* 2606 */     return this.dataWatcher.getWatchableObjectString(2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasCustomName()
/*      */   {
/* 2614 */     return this.dataWatcher.getWatchableObjectString(2).length() > 0;
/*      */   }
/*      */   
/*      */   public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag)
/*      */   {
/* 2619 */     this.dataWatcher.updateObject(3, Byte.valueOf((byte)(alwaysRenderNameTag ? 1 : 0)));
/*      */   }
/*      */   
/*      */   public boolean getAlwaysRenderNameTag()
/*      */   {
/* 2624 */     return this.dataWatcher.getWatchableObjectByte(3) == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPositionAndUpdate(double x, double y, double z)
/*      */   {
/* 2632 */     setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
/*      */   }
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender()
/*      */   {
/* 2637 */     return getAlwaysRenderNameTag();
/*      */   }
/*      */   
/*      */ 
/*      */   public void onDataWatcherUpdate(int dataID) {}
/*      */   
/*      */ 
/*      */   public EnumFacing getHorizontalFacing()
/*      */   {
/* 2646 */     return EnumFacing.getHorizontal(MathHelper.floor_double(this.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3);
/*      */   }
/*      */   
/*      */   protected HoverEvent getHoverEvent()
/*      */   {
/* 2651 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2652 */     String s = EntityList.getEntityString(this);
/* 2653 */     nbttagcompound.setString("id", getUniqueID().toString());
/*      */     
/* 2655 */     if (s != null)
/*      */     {
/* 2657 */       nbttagcompound.setString("type", s);
/*      */     }
/*      */     
/* 2660 */     nbttagcompound.setString("name", getName());
/* 2661 */     return new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nbttagcompound.toString()));
/*      */   }
/*      */   
/*      */   public boolean isSpectatedByPlayer(EntityPlayerMP player)
/*      */   {
/* 2666 */     return true;
/*      */   }
/*      */   
/*      */   public AxisAlignedBB getEntityBoundingBox()
/*      */   {
/* 2671 */     return this.boundingBox;
/*      */   }
/*      */   
/*      */   public void setEntityBoundingBox(AxisAlignedBB bb)
/*      */   {
/* 2676 */     this.boundingBox = bb;
/*      */   }
/*      */   
/*      */   public float getEyeHeight()
/*      */   {
/* 2681 */     return this.height * 0.85F;
/*      */   }
/*      */   
/*      */   public boolean isOutsideBorder()
/*      */   {
/* 2686 */     return this.isOutsideBorder;
/*      */   }
/*      */   
/*      */   public void setOutsideBorder(boolean outsideBorder)
/*      */   {
/* 2691 */     this.isOutsideBorder = outsideBorder;
/*      */   }
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*      */   {
/* 2696 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChatMessage(IChatComponent component) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*      */   {
/* 2711 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getPosition()
/*      */   {
/* 2720 */     return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getPositionVector()
/*      */   {
/* 2729 */     return new Vec3(this.posX, this.posY, this.posZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public World getEntityWorld()
/*      */   {
/* 2738 */     return this.worldObj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Entity getCommandSenderEntity()
/*      */   {
/* 2746 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean sendCommandFeedback()
/*      */   {
/* 2754 */     return false;
/*      */   }
/*      */   
/*      */   public void setCommandStat(CommandResultStats.Type type, int amount)
/*      */   {
/* 2759 */     this.cmdResultStats.func_179672_a(this, type, amount);
/*      */   }
/*      */   
/*      */   public CommandResultStats getCommandStats()
/*      */   {
/* 2764 */     return this.cmdResultStats;
/*      */   }
/*      */   
/*      */   public void func_174817_o(Entity entityIn)
/*      */   {
/* 2769 */     this.cmdResultStats.func_179671_a(entityIn.getCommandStats());
/*      */   }
/*      */   
/*      */   public NBTTagCompound getNBTTagCompound()
/*      */   {
/* 2774 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clientUpdateEntityNBT(NBTTagCompound compound) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean interactAt(EntityPlayer player, Vec3 targetVec3)
/*      */   {
/* 2789 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isImmuneToExplosions()
/*      */   {
/* 2794 */     return false;
/*      */   }
/*      */   
/*      */   protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn)
/*      */   {
/* 2799 */     if ((entityIn instanceof EntityLivingBase))
/*      */     {
/* 2801 */       EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
/*      */     }
/*      */     
/* 2804 */     EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */