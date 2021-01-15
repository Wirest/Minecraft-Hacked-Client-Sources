package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import me.aristhena.lucid.eventapi.events.StepConfirmEvent;
import me.aristhena.lucid.eventapi.events.StepEvent;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.modules.movement.Fly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Entity implements ICommandSender
{
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;
    
    /**
     * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
     * prevent spawning.
     */
    public boolean preventEntitySpawning;
    
    /** The entity that is riding this entity */
    public Entity riddenByEntity;
    
    /** The entity we are currently riding */
    public Entity ridingEntity;
    public boolean forceSpawn;
    
    /** Reference to the World object. */
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    
    /** Entity position X */
    public double posX;
    
    /** Entity position Y */
    public double posY;
    
    /** Entity position Z */
    public double posZ;
    
    /** Entity motion X */
    public double motionX;
    
    /** Entity motion Y */
    public double motionY;
    
    /** Entity motion Z */
    public double motionZ;
    
    /** Entity rotation Yaw */
    public float rotationYaw;
    
    /** Entity rotation Pitch */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    
    /** Axis aligned bounding box. */
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    
    /**
     * True if after a move this entity has collided with something on X- or Z-axis
     */
    public boolean isCollidedHorizontally;
    
    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;
    
    /**
     * True if after a move this entity has collided with something either vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    private boolean isOutsideBorder;
    
    /**
     * gets set by setEntityDead, so this must be the flag whether an Entity is dead (inactive may be better term)
     */
    public boolean isDead;
    
    /** How wide this entity is considered to be */
    public float width;
    
    /** How high this entity is considered to be */
    public float height;
    
    /** The previous ticks distance walked multiplied by 0.6 */
    public float prevDistanceWalkedModified;
    
    /** The distance walked multiplied by 0.6 */
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    
    /**
     * The distance that has to be exceeded in order to triger a new step sound and an onEntityWalking event on a block
     */
    private int nextStepDistance;
    
    /**
     * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosX;
    
    /**
     * The entity's Y coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosY;
    
    /**
     * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosZ;
    
    /**
     * How high this entity can step up when running into a block to try to get over it (currently make note the entity
     * will always step up this amount and not just the amount needed)
     */
    public float stepHeight;
    
    /**
     * Whether this entity won't clip with collision or not (make note it won't disable gravity)
     */
    public boolean noClip;
    
    /**
     * Reduces the velocity applied by entity collisions by the specified percent.
     */
    public float entityCollisionReduction;
    protected Random rand;
    
    /** How many ticks has this entity had ran since being alive */
    public int ticksExisted;
    
    /**
     * The amount of ticks you have to stand inside of fire before be set on fire
     */
    public int fireResistance;
    private int fire;
    
    /**
     * Whether this entity is currently inside of water (if it handles water movement that is)
     */
    protected boolean inWater;
    
    /**
     * Remaining time an entity will be "immune" to further damage after being hurt.
     */
    public int hurtResistantTime = 0;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    
    /** Has this entity been added to the chunk its within */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    
    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    
    /** Whether the entity is inside a Portal */
    protected boolean inPortal;
    protected int portalCounter;
    
    /** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
    public int dimension;
    protected int teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    
    /** The command result statistics for this Entity. */
    private final CommandResultStats cmdResultStats;
    
    public int getEntityId()
    {
	return this.entityId;
    }
    
    public void setEntityId(int id)
    {
	this.entityId = id;
    }
    
    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
	this.setDead();
    }
    
    public Entity(World worldIn)
    {
	this.entityId = nextEntityID++;
	this.renderDistanceWeight = 1.0D;
	this.boundingBox = ZERO_AABB;
	this.width = 0.6F;
	this.height = 1.8F;
	this.nextStepDistance = 1;
	this.rand = new Random();
	this.fireResistance = 1;
	this.firstUpdate = true;
	this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
	this.cmdResultStats = new CommandResultStats();
	this.worldObj = worldIn;
	this.setPosition(0.0D, 0.0D, 0.0D);
	
	if (worldIn != null)
	{
	    this.dimension = worldIn.provider.getDimensionId();
	}
	
	this.dataWatcher = new DataWatcher(this);
	this.dataWatcher.addObject(0, Byte.valueOf((byte) 0));
	this.dataWatcher.addObject(1, Short.valueOf((short) 300));
	this.dataWatcher.addObject(3, Byte.valueOf((byte) 0));
	this.dataWatcher.addObject(2, "");
	this.dataWatcher.addObject(4, Byte.valueOf((byte) 0));
	this.entityInit();
    }
    
    protected abstract void entityInit();
    
    public DataWatcher getDataWatcher()
    {
	return this.dataWatcher;
    }
    
    @Override
    public boolean equals(Object p_equals_1_)
    {
	return p_equals_1_ instanceof Entity ? ((Entity) p_equals_1_).entityId == this.entityId : false;
    }
    
    @Override
    public int hashCode()
    {
	return this.entityId;
    }
    
    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn()
    {
	if (this.worldObj != null)
	{
	    while (this.posY > 0.0D && this.posY < 256.0D)
	    {
		this.setPosition(this.posX, this.posY, this.posZ);
		
		if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
		{
		    break;
		}
		
		++this.posY;
	    }
	    
	    this.motionX = this.motionY = this.motionZ = 0.0D;
	    this.rotationPitch = 0.0F;
	}
    }
    
    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
	this.isDead = true;
    }
    
    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float width, float height)
    {
	if (width != this.width || height != this.height)
	{
	    float var3 = this.width;
	    this.width = width;
	    this.height = height;
	    this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + this.width, this.getEntityBoundingBox().minY + this.height, this.getEntityBoundingBox().minZ + this.width));
	    
	    if (this.width > var3 && !this.firstUpdate && !this.worldObj.isRemote)
	    {
		this.moveEntity(var3 - this.width, 0.0D, var3 - this.width);
	    }
	}
    }
    
    /**
     * Sets the rotation of the entity. Args: yaw, pitch (both in degrees)
     */
    protected void setRotation(float yaw, float pitch)
    {
	this.rotationYaw = yaw % 360.0F;
	this.rotationPitch = pitch % 360.0F;
    }
    
    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
	this.posX = x;
	this.posY = y;
	this.posZ = z;
	float var7 = this.width / 2.0F;
	float var8 = this.height;
	this.setEntityBoundingBox(new AxisAlignedBB(x - var7, y, z - var7, x + var7, y + var8, z + var7));
    }
    
    /**
     * Adds 15% to the entity's yaw and subtracts 15% from the pitch. Clamps pitch from -90 to 90. Both arguments in
     * degrees.
     */
    public void setAngles(float yaw, float pitch)
    {
	float var3 = this.rotationPitch;
	float var4 = this.rotationYaw;
	this.rotationYaw = (float) (this.rotationYaw + yaw * 0.15D);
	this.rotationPitch = (float) (this.rotationPitch - pitch * 0.15D);
	this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
	this.prevRotationPitch += this.rotationPitch - var3;
	this.prevRotationYaw += this.rotationYaw - var4;
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
	this.onEntityUpdate();
    }
    
    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
	this.worldObj.theProfiler.startSection("entityBaseTick");
	
	if (this.ridingEntity != null && this.ridingEntity.isDead)
	{
	    this.ridingEntity = null;
	}
	
	this.prevDistanceWalkedModified = this.distanceWalkedModified;
	this.prevPosX = this.posX;
	this.prevPosY = this.posY;
	this.prevPosZ = this.posZ;
	this.prevRotationPitch = this.rotationPitch;
	this.prevRotationYaw = this.rotationYaw;
	
	if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
	{
	    this.worldObj.theProfiler.startSection("portal");
	    MinecraftServer var1 = ((WorldServer) this.worldObj).getMinecraftServer();
	    int var2 = this.getMaxInPortalTime();
	    
	    if (this.inPortal)
	    {
		if (var1.getAllowNether())
		{
		    if (this.ridingEntity == null && this.portalCounter++ >= var2)
		    {
			this.portalCounter = var2;
			this.timeUntilPortal = this.getPortalCooldown();
			byte var3;
			
			if (this.worldObj.provider.getDimensionId() == -1)
			{
			    var3 = 0;
			}
			else
			{
			    var3 = -1;
			}
			
			this.travelToDimension(var3);
		    }
		    
		    this.inPortal = false;
		}
	    }
	    else
	    {
		if (this.portalCounter > 0)
		{
		    this.portalCounter -= 4;
		}
		
		if (this.portalCounter < 0)
		{
		    this.portalCounter = 0;
		}
	    }
	    
	    if (this.timeUntilPortal > 0)
	    {
		--this.timeUntilPortal;
	    }
	    
	    this.worldObj.theProfiler.endSection();
	}
	
	this.spawnRunningParticles();
	this.handleWaterMovement();
	
	if (this.worldObj.isRemote)
	{
	    this.fire = 0;
	}
	else if (this.fire > 0)
	{
	    if (this.isImmuneToFire)
	    {
		this.fire -= 4;
		
		if (this.fire < 0)
		{
		    this.fire = 0;
		}
	    }
	    else
	    {
		if (this.fire % 20 == 0)
		{
		    this.attackEntityFrom(DamageSource.onFire, 1.0F);
		}
		
		--this.fire;
	    }
	}
	
	if (this.isInLava())
	{
	    this.setOnFireFromLava();
	    this.fallDistance *= 0.5F;
	}
	
	if (this.posY < -64.0D)
	{
	    this.kill();
	}
	
	if (!this.worldObj.isRemote)
	{
	    this.setFlag(0, this.fire > 0);
	}
	
	this.firstUpdate = false;
	this.worldObj.theProfiler.endSection();
    }
    
    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime()
    {
	return 0;
    }
    
    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava()
    {
	if (!this.isImmuneToFire)
	{
	    this.attackEntityFrom(DamageSource.lava, 4.0F);
	    this.setFire(15);
	}
    }
    
    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int seconds)
    {
	int var2 = seconds * 20;
	var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);
	
	if (this.fire < var2)
	{
	    this.fire = var2;
	}
    }
    
    /**
     * Removes fire from entity.
     */
    public void extinguish()
    {
	this.fire = 0;
    }
    
    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
	this.setDead();
    }
    
    /**
     * Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z)
    {
	AxisAlignedBB var7 = this.getEntityBoundingBox().offset(x, y, z);
	return this.isLiquidPresentInAABB(var7);
    }
    
    /**
     * Determines if a liquid is present within the specified AxisAlignedBB.
     */
    private boolean isLiquidPresentInAABB(AxisAlignedBB bb)
    {
	return this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb);
    }
    
    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double x, double y, double z)
    {
	//if (this == Minecraft.getMinecraft().thePlayer) 
	if (this.noClip)
	{
	    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
	    this.resetPositionToBB();
	}
	else
	{
	    this.worldObj.theProfiler.startSection("move");
	    double var7 = this.posX;
	    double var9 = this.posY;
	    double var11 = this.posZ;
	    
	    if (this.isInWeb)
	    {
		this.isInWeb = false;
		x *= 0.25D;
		y *= 0.05000000074505806D;
		z *= 0.25D;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
	    }
	    
	    double var13 = x;
	    double var15 = y;
	    double var17 = z;
	    boolean var19 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
	    
	    if (var19)
	    {
		double var20;
		
		for (var20 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); var13 = x)
		{
		    if (x < var20 && x >= -var20)
		    {
			x = 0.0D;
		    }
		    else if (x > 0.0D)
		    {
			x -= var20;
		    }
		    else
		    {
			x += var20;
		    }
		}
		
		for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); var17 = z)
		{
		    if (z < var20 && z >= -var20)
		    {
			z = 0.0D;
		    }
		    else if (z > 0.0D)
		    {
			z -= var20;
		    }
		    else
		    {
			z += var20;
		    }
		}
		
		for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); var17 = z)
		{
		    if (x < var20 && x >= -var20)
		    {
			x = 0.0D;
		    }
		    else if (x > 0.0D)
		    {
			x -= var20;
		    }
		    else
		    {
			x += var20;
		    }
		    
		    var13 = x;
		    
		    if (z < var20 && z >= -var20)
		    {
			z = 0.0D;
		    }
		    else if (z > 0.0D)
		    {
			z -= var20;
		    }
		    else
		    {
			z += var20;
		    }
		}
	    }
	    
	    List var53 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
	    AxisAlignedBB var21 = this.getEntityBoundingBox();
	    AxisAlignedBB var23;
	    
	    for (Iterator var22 = var53.iterator(); var22.hasNext(); y = var23.calculateYOffset(this.getEntityBoundingBox(), y))
	    {
		var23 = (AxisAlignedBB) var22.next();
	    }
	    
	    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
	    boolean var54 = this.onGround || var15 != y && var15 < 0.0D;
	    AxisAlignedBB var24;
	    Iterator var55;
	    
	    for (var55 = var53.iterator(); var55.hasNext(); x = var24.calculateXOffset(this.getEntityBoundingBox(), x))
	    {
		var24 = (AxisAlignedBB) var55.next();
	    }
	    
	    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
	    
	    for (var55 = var53.iterator(); var55.hasNext(); z = var24.calculateZOffset(this.getEntityBoundingBox(), z))
	    {
		var24 = (AxisAlignedBB) var55.next();
	    }
	    
	    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));
	    
	    if (this.stepHeight > 0.0F && var54 && (var13 != x || var17 != z))
	    {
		StepEvent event = new StepEvent(stepHeight);
		event.call();
		
		double var56 = x;
		double var25 = y;
		double var27 = z;
		AxisAlignedBB var29 = this.getEntityBoundingBox();
		this.setEntityBoundingBox(var21);
		y = this.stepHeight;
		List var30 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(var13, y, var17));
		AxisAlignedBB var31 = this.getEntityBoundingBox();
		AxisAlignedBB var32 = var31.addCoord(var13, 0.0D, var17);
		double var33 = y;
		AxisAlignedBB var36;
		
		for (Iterator var35 = var30.iterator(); var35.hasNext(); var33 = var36.calculateYOffset(var32, var33))
		{
		    var36 = (AxisAlignedBB) var35.next();
		}
		
		var31 = var31.offset(0.0D, var33, 0.0D);
		double var67 = var13;
		AxisAlignedBB var38;
		
		for (Iterator var37 = var30.iterator(); var37.hasNext(); var67 = var38.calculateXOffset(var31, var67))
		{
		    var38 = (AxisAlignedBB) var37.next();
		}
		
		var31 = var31.offset(var67, 0.0D, 0.0D);
		double var68 = var17;
		AxisAlignedBB var40;
		
		for (Iterator var39 = var30.iterator(); var39.hasNext(); var68 = var40.calculateZOffset(var31, var68))
		{
		    var40 = (AxisAlignedBB) var39.next();
		}
		
		var31 = var31.offset(0.0D, 0.0D, var68);
		AxisAlignedBB var69 = this.getEntityBoundingBox();
		double var70 = y;
		AxisAlignedBB var43;
		
		for (Iterator var42 = var30.iterator(); var42.hasNext(); var70 = var43.calculateYOffset(var69, var70))
		{
		    var43 = (AxisAlignedBB) var42.next();
		}
		
		var69 = var69.offset(0.0D, var70, 0.0D);
		double var71 = var13;
		AxisAlignedBB var45;
		
		for (Iterator var44 = var30.iterator(); var44.hasNext(); var71 = var45.calculateXOffset(var69, var71))
		{
		    var45 = (AxisAlignedBB) var44.next();
		}
		
		var69 = var69.offset(var71, 0.0D, 0.0D);
		double var72 = var17;
		AxisAlignedBB var47;
		
		for (Iterator var46 = var30.iterator(); var46.hasNext(); var72 = var47.calculateZOffset(var69, var72))
		{
		    var47 = (AxisAlignedBB) var46.next();
		}
		
		var69 = var69.offset(0.0D, 0.0D, var72);
		double var73 = var67 * var67 + var68 * var68;
		double var48 = var71 * var71 + var72 * var72;
		
		if (var73 > var48)
		{
		    x = var67;
		    z = var68;
		    this.setEntityBoundingBox(var31);
		}
		else
		{
		    x = var71;
		    z = var72;
		    this.setEntityBoundingBox(var69);
		}
		
		y = (-this.stepHeight);
		AxisAlignedBB var51;
		
		for (Iterator var50 = var30.iterator(); var50.hasNext(); y = var51.calculateYOffset(this.getEntityBoundingBox(), y))
		{
		    var51 = (AxisAlignedBB) var50.next();
		}
		
		this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
		
		if (var56 * var56 + var27 * var27 >= x * x + z * z)
		{
		    x = var56;
		    y = var25;
		    z = var27;
		    this.setEntityBoundingBox(var29);
		    
		    if (event.bypass)
		    {
			if (this.stepHeight > 0.0F && var54 && (var13 != x || var17 != z))
			{
			    double var561 = x;
			    double var251 = y;
			    double var271 = z;
			    AxisAlignedBB var291 = this.getEntityBoundingBox();
			    this.setEntityBoundingBox(var21);
			    y = event.stepHeight;
			    List var301 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(var13, y, var17));
			    AxisAlignedBB var311 = this.getEntityBoundingBox();
			    AxisAlignedBB var321 = var311.addCoord(var13, 0.0D, var17);
			    double var331 = y;
			    AxisAlignedBB var361;
			    
			    for (Iterator var35 = var301.iterator(); var35.hasNext(); var331 = var361.calculateYOffset(var321, var331))
			    {
				var361 = (AxisAlignedBB) var35.next();
			    }
			    
			    var311 = var311.offset(0.0D, var331, 0.0D);
			    double var671 = var13;
			    AxisAlignedBB var381;
			    
			    for (Iterator var37 = var301.iterator(); var37.hasNext(); var671 = var381.calculateXOffset(var311, var671))
			    {
				var381 = (AxisAlignedBB) var37.next();
			    }
			    
			    var311 = var311.offset(var671, 0.0D, 0.0D);
			    double var681 = var17;
			    AxisAlignedBB var401;
			    
			    for (Iterator var39 = var301.iterator(); var39.hasNext(); var681 = var401.calculateZOffset(var311, var681))
			    {
				var401 = (AxisAlignedBB) var39.next();
			    }
			    
			    var311 = var311.offset(0.0D, 0.0D, var681);
			    AxisAlignedBB var691 = this.getEntityBoundingBox();
			    double var701 = y;
			    AxisAlignedBB var431;
			    
			    for (Iterator var42 = var301.iterator(); var42.hasNext(); var701 = var431.calculateYOffset(var691, var701))
			    {
				var431 = (AxisAlignedBB) var42.next();
			    }
			    
			    var691 = var691.offset(0.0D, var701, 0.0D);
			    double var711 = var13;
			    AxisAlignedBB var451;
			    
			    for (Iterator var44 = var301.iterator(); var44.hasNext(); var711 = var451.calculateXOffset(var691, var711))
			    {
				var451 = (AxisAlignedBB) var44.next();
			    }
			    
			    var691 = var691.offset(var711, 0.0D, 0.0D);
			    double var721 = var17;
			    AxisAlignedBB var471;
			    
			    for (Iterator var46 = var301.iterator(); var46.hasNext(); var721 = var471.calculateZOffset(var691, var721))
			    {
				var471 = (AxisAlignedBB) var46.next();
			    }
			    
			    var691 = var691.offset(0.0D, 0.0D, var721);
			    double var731 = var671 * var671 + var681 * var681;
			    double var481 = var711 * var711 + var721 * var721;
			    
			    if (var731 > var481)
			    {
				x = var671;
				z = var681;
				this.setEntityBoundingBox(var311);
			    }
			    else
			    {
				x = var711;
				z = var721;
				this.setEntityBoundingBox(var691);
			    }
			    
			    y = (double) (-this.stepHeight);
			    AxisAlignedBB var511;
			    
			    for (Iterator var50 = var301.iterator(); var50.hasNext(); y = var511.calculateYOffset(this.getEntityBoundingBox(), y))
			    {
				var511 = (AxisAlignedBB) var50.next();
			    }
			    
			    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
			    
			    if (var561 * var561 + var271 * var271 >= x * x + z * z)
			    {
				x = var561;
				y = var251;
				z = var271;
				this.setEntityBoundingBox(var291);
			    }
			    else
			    {
				StepConfirmEvent eventConfirm = new StepConfirmEvent();
				eventConfirm.call();
			    }
			}
		    }
		}
	    }
	    
	    this.worldObj.theProfiler.endSection();
	    this.worldObj.theProfiler.startSection("rest");
	    this.resetPositionToBB();
	    this.isCollidedHorizontally = var13 != x || var17 != z;
	    this.isCollidedVertically = var15 != y;
	    this.onGround = (((ModuleManager.getModule(Fly.class).enabled) && (this == Minecraft.getMinecraft().thePlayer)) || ((this.isCollidedVertically) && (var15 < 0.0D)));
	    this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
	    int var57 = MathHelper.floor_double(this.posX);
	    int var58 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
	    int var59 = MathHelper.floor_double(this.posZ);
	    BlockPos var26 = new BlockPos(var57, var58, var59);
	    Block var60 = this.worldObj.getBlockState(var26).getBlock();
	    
	    if (var60.getMaterial() == Material.air)
	    {
		Block var28 = this.worldObj.getBlockState(var26.down()).getBlock();
		
		if (var28 instanceof BlockFence || var28 instanceof BlockWall || var28 instanceof BlockFenceGate)
		{
		    var60 = var28;
		    var26 = var26.down();
		}
	    }
	    
	    this.updateFallState(y, this.onGround, var60, var26);
	    
	    if (var13 != x)
	    {
		this.motionX = 0.0D;
	    }
	    
	    if (var17 != z)
	    {
		this.motionZ = 0.0D;
	    }
	    
	    if (var15 != y)
	    {
		var60.onLanded(this.worldObj, this);
	    }
	    
	    if (this.canTriggerWalking() && !var19 && this.ridingEntity == null)
	    {
		double var61 = this.posX - var7;
		double var64 = this.posY - var9;
		double var66 = this.posZ - var11;
		
		if (var60 != Blocks.ladder)
		{
		    var64 = 0.0D;
		}
		
		if (var60 != null && this.onGround)
		{
		    var60.onEntityCollidedWithBlock(this.worldObj, var26, this);
		}
		
		this.distanceWalkedModified = (float) (this.distanceWalkedModified + MathHelper.sqrt_double(var61 * var61 + var66 * var66) * 0.6D);
		this.distanceWalkedOnStepModified = (float) (this.distanceWalkedOnStepModified + MathHelper.sqrt_double(var61 * var61 + var64 * var64 + var66 * var66) * 0.6D);
		
		if (this.distanceWalkedOnStepModified > this.nextStepDistance && var60.getMaterial() != Material.air)
		{
		    this.nextStepDistance = (int) this.distanceWalkedOnStepModified + 1;
		    
		    if (this.isInWater())
		    {
			float var34 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
			
			if (var34 > 1.0F)
			{
			    var34 = 1.0F;
			}
			
			this.playSound(this.getSwimSound(), var34, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
		    }
		    
		    this.playStepSound(var26, var60);
		}
	    }
	    
	    try
	    {
		this.doBlockCollisions();
	    }
	    catch (Throwable var52)
	    {
		CrashReport var63 = CrashReport.makeCrashReport(var52, "Checking entity block collision");
		CrashReportCategory var65 = var63.makeCategory("Entity being checked for collision");
		this.addEntityCrashInfo(var65);
		throw new ReportedException(var63);
	    }
	    
	    boolean var62 = this.isWet();
	    
	    if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D)))
	    {
		this.dealFireDamage(1);
		
		if (!var62)
		{
		    ++this.fire;
		    
		    if (this.fire == 0)
		    {
			this.setFire(8);
		    }
		}
	    }
	    else if (this.fire <= 0)
	    {
		this.fire = -this.fireResistance;
	    }
	    
	    if (var62 && this.fire > 0)
	    {
		this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
		this.fire = -this.fireResistance;
	    }
	    
	    this.worldObj.theProfiler.endSection();
	}
    }
    
    /**
     * Resets the entity's position to the center (planar) and bottom (vertical) points of its bounding box.
     */
    private void resetPositionToBB()
    {
	this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
	this.posY = this.getEntityBoundingBox().minY;
	this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
    }
    
    protected String getSwimSound()
    {
	return "game.neutral.swim";
    }
    
    protected void doBlockCollisions()
    {
	BlockPos var1 = new BlockPos(this.getEntityBoundingBox().minX + 0.001D, this.getEntityBoundingBox().minY + 0.001D, this.getEntityBoundingBox().minZ + 0.001D);
	BlockPos var2 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001D, this.getEntityBoundingBox().maxY - 0.001D, this.getEntityBoundingBox().maxZ - 0.001D);
	
	if (this.worldObj.isAreaLoaded(var1, var2))
	{
	    for (int var3 = var1.getX(); var3 <= var2.getX(); ++var3)
	    {
		for (int var4 = var1.getY(); var4 <= var2.getY(); ++var4)
		{
		    for (int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5)
		    {
			BlockPos var6 = new BlockPos(var3, var4, var5);
			IBlockState var7 = this.worldObj.getBlockState(var6);
			
			try
			{
			    var7.getBlock().onEntityCollidedWithBlock(this.worldObj, var6, var7, this);
			}
			catch (Throwable var11)
			{
			    CrashReport var9 = CrashReport.makeCrashReport(var11, "Colliding entity with block");
			    CrashReportCategory var10 = var9.makeCategory("Block being collided with");
			    CrashReportCategory.addBlockInfo(var10, var6, var7);
			    throw new ReportedException(var9);
			}
		    }
		}
	    }
	}
    }
    
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
	Block.SoundType var3 = blockIn.stepSound;
	
	if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
	{
	    var3 = Blocks.snow_layer.stepSound;
	    this.playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
	}
	else if (!blockIn.getMaterial().isLiquid())
	{
	    this.playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
	}
    }
    
    public void playSound(String name, float volume, float pitch)
    {
	if (!this.isSilent())
	{
	    this.worldObj.playSoundAtEntity(this, name, volume, pitch);
	}
    }
    
    /**
     * @return True if this entity will not play sounds
     */
    public boolean isSilent()
    {
	return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }
    
    /**
     * When set to true the entity will not play sounds.
     *  
     * @param isSilent Should the mob be set to silent?
     */
    public void setSilent(boolean isSilent)
    {
	this.dataWatcher.updateObject(4, Byte.valueOf((byte) (isSilent ? 1 : 0)));
    }
    
    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
	return true;
    }
    
    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
    {
	if (onGroundIn)
	{
	    if (this.fallDistance > 0.0F)
	    {
		if (blockIn != null)
		{
		    blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
		}
		else
		{
		    this.fall(this.fallDistance, 1.0F);
		}
		
		this.fallDistance = 0.0F;
	    }
	}
	else if (y < 0.0D)
	{
	    this.fallDistance = (float) (this.fallDistance - y);
	}
    }
    
    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
	return null;
    }
    
    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int amount)
    {
	if (!this.isImmuneToFire)
	{
	    this.attackEntityFrom(DamageSource.inFire, amount);
	}
    }
    
    public final boolean isImmuneToFire()
    {
	return this.isImmuneToFire;
    }
    
    public void fall(float distance, float damageMultiplier)
    {
	if (this.riddenByEntity != null)
	{
	    this.riddenByEntity.fall(distance, damageMultiplier);
	}
    }
    
    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet()
    {
	return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + this.height, this.posZ));
    }
    
    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
	return this.inWater;
    }
    
    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
	if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
	{
	    if (!this.inWater && !this.firstUpdate)
	    {
		this.resetHeight();
	    }
	    
	    this.fallDistance = 0.0F;
	    this.inWater = true;
	    this.fire = 0;
	}
	else
	{
	    this.inWater = false;
	}
	
	return this.inWater;
    }
    
    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight()
    {
	float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
	
	if (var1 > 1.0F)
	{
	    var1 = 1.0F;
	}
	
	this.playSound(this.getSplashSound(), var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
	float var2 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
	int var3;
	float var4;
	float var5;
	
	for (var3 = 0; var3 < 1.0F + this.width * 20.0F; ++var3)
	{
	    var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
	    var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
	    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var4, var2 + 1.0F, this.posZ + var5, this.motionX, this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ, new int[0]);
	}
	
	for (var3 = 0; var3 < 1.0F + this.width * 20.0F; ++var3)
	{
	    var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
	    var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
	    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + var4, var2 + 1.0F, this.posZ + var5, this.motionX, this.motionY, this.motionZ, new int[0]);
	}
    }
    
    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles()
    {
	if (this.isSprinting() && !this.isInWater())
	{
	    this.createRunningParticles();
	}
    }
    
    protected void createRunningParticles()
    {
	int var1 = MathHelper.floor_double(this.posX);
	int var2 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
	int var3 = MathHelper.floor_double(this.posZ);
	BlockPos var4 = new BlockPos(var1, var2, var3);
	IBlockState var5 = this.worldObj.getBlockState(var4);
	Block var6 = var5.getBlock();
	
	if (var6.getRenderType() != -1)
	{
	    this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(var5) });
	}
    }
    
    protected String getSplashSound()
    {
	return "game.neutral.swim.splash";
    }
    
    /**
     * Checks if the current block the entity is within of the specified material type
     */
    public boolean isInsideOfMaterial(Material materialIn)
    {
	double var2 = this.posY + this.getEyeHeight();
	BlockPos var4 = new BlockPos(this.posX, var2, this.posZ);
	IBlockState var5 = this.worldObj.getBlockState(var4);
	Block var6 = var5.getBlock();
	
	if (var6.getMaterial() == materialIn)
	{
	    float var7 = BlockLiquid.getLiquidHeightPercent(var5.getBlock().getMetaFromState(var5)) - 0.11111111F;
	    float var8 = var4.getY() + 1 - var7;
	    boolean var9 = var2 < var8;
	    return !var9 && this instanceof EntityPlayer ? false : var9;
	}
	else
	{
	    return false;
	}
    }
    
    public boolean isInLava()
    {
	return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }
    
    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float strafe, float forward, float friction)
    {
	float var4 = strafe * strafe + forward * forward;
	
	if (var4 >= 1.0E-4F)
	{
	    var4 = MathHelper.sqrt_float(var4);
	    
	    if (var4 < 1.0F)
	    {
		var4 = 1.0F;
	    }
	    
	    var4 = friction / var4;
	    strafe *= var4;
	    forward *= var4;
	    float var5 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
	    float var6 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
	    
	    this.motionX += strafe * var6 - forward * var5;
	    this.motionZ += forward * var6 + strafe * var5;
	}
    }
    
    public void moveFlyingFucked(float strafe, float forward, float friction)
    {
	if (isCollidedVertically) friction = 0.2905F;
	float var4 = strafe * strafe + forward * forward;
	
	if (var4 >= 1.0E-4F)
	{
	    var4 = MathHelper.sqrt_float(var4);
	    
	    if (var4 < 1.0F)
	    {
		var4 = 1.0F;
	    }
	    
	    var4 = friction / var4;
	    strafe *= var4;
	    forward *= var4;
	    float var5 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
	    float var6 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
	    if (isCollidedVertically)
	    {
		this.motionX = strafe * var6 - forward * var5;
		this.motionZ = forward * var6 + strafe * var5;
	    }
	    else
	    {
		this.motionX += strafe * var6 - forward * var5;
		this.motionZ += forward * var6 + strafe * var5;
	    }
	}
    }
    
    public int getBrightnessForRender(float partialTicks)
    {
	BlockPos var2 = new BlockPos(this.posX, 0.0D, this.posZ);
	
	if (this.worldObj.isBlockLoaded(var2))
	{
	    double var3 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66D;
	    int var5 = MathHelper.floor_double(this.posY + var3);
	    return this.worldObj.getCombinedLight(var2.up(var5), 0);
	}
	else
	{
	    return 0;
	}
    }
    
    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float partialTicks)
    {
	BlockPos var2 = new BlockPos(this.posX, 0.0D, this.posZ);
	
	if (this.worldObj.isBlockLoaded(var2))
	{
	    double var3 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66D;
	    int var5 = MathHelper.floor_double(this.posY + var3);
	    return this.worldObj.getLightBrightness(var2.up(var5));
	}
	else
	{
	    return 0.0F;
	}
    }
    
    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn)
    {
	this.worldObj = worldIn;
    }
    
    /**
     * Sets the entity's position and rotation.
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
	this.prevPosX = this.posX = x;
	this.prevPosY = this.posY = y;
	this.prevPosZ = this.posZ = z;
	this.prevRotationYaw = this.rotationYaw = yaw;
	this.prevRotationPitch = this.rotationPitch = pitch;
	double var9 = this.prevRotationYaw - yaw;
	
	if (var9 < -180.0D)
	{
	    this.prevRotationYaw += 360.0F;
	}
	
	if (var9 >= 180.0D)
	{
	    this.prevRotationYaw -= 360.0F;
	}
	
	this.setPosition(this.posX, this.posY, this.posZ);
	this.setRotation(yaw, pitch);
    }
    
    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn)
    {
	this.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
    }
    
    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
	this.lastTickPosX = this.prevPosX = this.posX = x;
	this.lastTickPosY = this.prevPosY = this.posY = y;
	this.lastTickPosZ = this.prevPosZ = this.posZ = z;
	this.rotationYaw = yaw;
	this.rotationPitch = pitch;
	this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntity(Entity entityIn)
    {
	float var2 = (float) (this.posX - entityIn.posX);
	float var3 = (float) (this.posY - entityIn.posY);
	float var4 = (float) (this.posZ - entityIn.posZ);
	return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }
    
    /**
     * Gets the squared distance to the position. Args: x, y, z
     */
    public double getDistanceSq(double x, double y, double z)
    {
	double var7 = this.posX - x;
	double var9 = this.posY - y;
	double var11 = this.posZ - z;
	return var7 * var7 + var9 * var9 + var11 * var11;
    }
    
    public double getDistanceSq(BlockPos pos)
    {
	return pos.distanceSq(this.posX, this.posY, this.posZ);
    }
    
    public double getDistanceSqToCenter(BlockPos pos)
    {
	return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }
    
    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double x, double y, double z)
    {
	double var7 = this.posX - x;
	double var9 = this.posY - y;
	double var11 = this.posZ - z;
	return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }
    
    /**
     * Returns the squared distance to the entity. Args: entity
     */
    public double getDistanceSqToEntity(Entity entityIn)
    {
	double var2 = this.posX - entityIn.posX;
	double var4 = this.posY - entityIn.posY;
	double var6 = this.posZ - entityIn.posZ;
	return var2 * var2 + var4 * var4 + var6 * var6;
    }
    
    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
    }
    
    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn)
    {
	if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this)
	{
	    if (!entityIn.noClip && !this.noClip)
	    {
		double var2 = entityIn.posX - this.posX;
		double var4 = entityIn.posZ - this.posZ;
		double var6 = MathHelper.abs_max(var2, var4);
		
		if (var6 >= 0.009999999776482582D)
		{
		    var6 = MathHelper.sqrt_double(var6);
		    var2 /= var6;
		    var4 /= var6;
		    double var8 = 1.0D / var6;
		    
		    if (var8 > 1.0D)
		    {
			var8 = 1.0D;
		    }
		    
		    var2 *= var8;
		    var4 *= var8;
		    var2 *= 0.05000000074505806D;
		    var4 *= 0.05000000074505806D;
		    var2 *= 1.0F - this.entityCollisionReduction;
		    var4 *= 1.0F - this.entityCollisionReduction;
		    
		    if (this.riddenByEntity == null)
		    {
			this.addVelocity(-var2, 0.0D, -var4);
		    }
		    
		    if (entityIn.riddenByEntity == null)
		    {
			entityIn.addVelocity(var2, 0.0D, var4);
		    }
		}
	    }
	}
    }
    
    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double x, double y, double z)
    {
	this.motionX += x;
	this.motionY += y;
	this.motionZ += z;
	this.isAirBorne = true;
    }
    
    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked()
    {
	this.velocityChanged = true;
    }
    
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
	if (this.isEntityInvulnerable(source))
	{
	    return false;
	}
	else
	{
	    this.setBeenAttacked();
	    return false;
	}
    }
    
    /**
     * interpolated look vector
     */
    public Vec3 getLook(float partialTicks)
    {
	if (partialTicks == 1.0F)
	{
	    return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
	}
	else
	{
	    float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
	    float var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
	    return this.getVectorForRotation(var2, var3);
	}
    }
    
    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     *  
     * @param pitch The rotational pitch of the entity.
     * @param yaw The rotational yaw of the entity.
     */
    protected final Vec3 getVectorForRotation(float pitch, float yaw)
    {
	float var3 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
	float var4 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
	float var5 = -MathHelper.cos(-pitch * 0.017453292F);
	float var6 = MathHelper.sin(-pitch * 0.017453292F);
	return new Vec3(var4 * var5, var6, var3 * var5);
    }
    
    public Vec3 getPositionEyes(float partialTicks)
    {
	if (partialTicks == 1.0F)
	{
	    return new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ);
	}
	else
	{
	    double var2 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
	    double var4 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + this.getEyeHeight();
	    double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
	    return new Vec3(var2, var4, var6);
	}
    }
    
    public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks)
    {
	Vec3 var4 = this.getPositionEyes(partialTicks);
	Vec3 var5 = this.getLook(partialTicks);
	Vec3 var6 = var4.addVector(var5.xCoord * blockReachDistance, var5.yCoord * blockReachDistance, var5.zCoord * blockReachDistance);
	return this.worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
	return false;
    }
    
    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
	return false;
    }
    
    /**
     * Adds a value to the player score. Currently not actually used and the entity passed in does nothing. Args:
     * entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entityIn, int amount)
    {
    }
    
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
	double var7 = this.posX - x;
	double var9 = this.posY - y;
	double var11 = this.posZ - z;
	double var13 = var7 * var7 + var9 * var9 + var11 * var11;
	return this.isInRangeToRenderDist(var13);
    }
    
    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double distance)
    {
	double var3 = this.getEntityBoundingBox().getAverageEdgeLength();
	var3 *= 64.0D * this.renderDistanceWeight;
	return distance < var3 * var3;
    }
    
    /**
     * Like writeToNBTOptional but does not check if the entity is ridden. Used for saving ridden entities with their
     * riders.
     */
    public boolean writeMountToNBT(NBTTagCompound tagCompund)
    {
	String var2 = this.getEntityString();
	
	if (!this.isDead && var2 != null)
	{
	    tagCompund.setString("id", var2);
	    this.writeToNBT(tagCompund);
	    return true;
	}
	else
	{
	    return false;
	}
    }
    
    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
	String var2 = this.getEntityString();
	
	if (!this.isDead && var2 != null && this.riddenByEntity == null)
	{
	    tagCompund.setString("id", var2);
	    this.writeToNBT(tagCompund);
	    return true;
	}
	else
	{
	    return false;
	}
    }
    
    /**
     * Save the entity to NBT (calls an abstract helper method to write extra data)
     */
    public void writeToNBT(NBTTagCompound tagCompund)
    {
	try
	{
	    tagCompund.setTag("Pos", this.newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
	    tagCompund.setTag("Motion", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
	    tagCompund.setTag("Rotation", this.newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
	    tagCompund.setFloat("FallDistance", this.fallDistance);
	    tagCompund.setShort("Fire", (short) this.fire);
	    tagCompund.setShort("Air", (short) this.getAir());
	    tagCompund.setBoolean("OnGround", this.onGround);
	    tagCompund.setInteger("Dimension", this.dimension);
	    tagCompund.setBoolean("Invulnerable", this.invulnerable);
	    tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
	    tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
	    tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
	    
	    if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0)
	    {
		tagCompund.setString("CustomName", this.getCustomNameTag());
		tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
	    }
	    
	    this.cmdResultStats.func_179670_b(tagCompund);
	    
	    if (this.isSilent())
	    {
		tagCompund.setBoolean("Silent", this.isSilent());
	    }
	    
	    this.writeEntityToNBT(tagCompund);
	    
	    if (this.ridingEntity != null)
	    {
		NBTTagCompound var2 = new NBTTagCompound();
		
		if (this.ridingEntity.writeMountToNBT(var2))
		{
		    tagCompund.setTag("Riding", var2);
		}
	    }
	}
	catch (Throwable var5)
	{
	    CrashReport var3 = CrashReport.makeCrashReport(var5, "Saving entity NBT");
	    CrashReportCategory var4 = var3.makeCategory("Entity being saved");
	    this.addEntityCrashInfo(var4);
	    throw new ReportedException(var3);
	}
    }
    
    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound tagCompund)
    {
	try
	{
	    NBTTagList var2 = tagCompund.getTagList("Pos", 6);
	    NBTTagList var6 = tagCompund.getTagList("Motion", 6);
	    NBTTagList var7 = tagCompund.getTagList("Rotation", 5);
	    this.motionX = var6.getDoubleAt(0);
	    this.motionY = var6.getDoubleAt(1);
	    this.motionZ = var6.getDoubleAt(2);
	    
	    if (Math.abs(this.motionX) > 10.0D)
	    {
		this.motionX = 0.0D;
	    }
	    
	    if (Math.abs(this.motionY) > 10.0D)
	    {
		this.motionY = 0.0D;
	    }
	    
	    if (Math.abs(this.motionZ) > 10.0D)
	    {
		this.motionZ = 0.0D;
	    }
	    
	    this.prevPosX = this.lastTickPosX = this.posX = var2.getDoubleAt(0);
	    this.prevPosY = this.lastTickPosY = this.posY = var2.getDoubleAt(1);
	    this.prevPosZ = this.lastTickPosZ = this.posZ = var2.getDoubleAt(2);
	    this.prevRotationYaw = this.rotationYaw = var7.getFloatAt(0);
	    this.prevRotationPitch = this.rotationPitch = var7.getFloatAt(1);
	    this.fallDistance = tagCompund.getFloat("FallDistance");
	    this.fire = tagCompund.getShort("Fire");
	    this.setAir(tagCompund.getShort("Air"));
	    this.onGround = tagCompund.getBoolean("OnGround");
	    this.dimension = tagCompund.getInteger("Dimension");
	    this.invulnerable = tagCompund.getBoolean("Invulnerable");
	    this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
	    
	    if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4))
	    {
		this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
	    }
	    else if (tagCompund.hasKey("UUID", 8))
	    {
		this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
	    }
	    
	    this.setPosition(this.posX, this.posY, this.posZ);
	    this.setRotation(this.rotationYaw, this.rotationPitch);
	    
	    if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
	    {
		this.setCustomNameTag(tagCompund.getString("CustomName"));
	    }
	    
	    this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
	    this.cmdResultStats.func_179668_a(tagCompund);
	    this.setSilent(tagCompund.getBoolean("Silent"));
	    this.readEntityFromNBT(tagCompund);
	    
	    if (this.shouldSetPosAfterLoading())
	    {
		this.setPosition(this.posX, this.posY, this.posZ);
	    }
	}
	catch (Throwable var5)
	{
	    CrashReport var3 = CrashReport.makeCrashReport(var5, "Loading entity NBT");
	    CrashReportCategory var4 = var3.makeCategory("Entity being loaded");
	    this.addEntityCrashInfo(var4);
	    throw new ReportedException(var3);
	}
    }
    
    protected boolean shouldSetPosAfterLoading()
    {
	return true;
    }
    
    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString()
    {
	return EntityList.getEntityString(this);
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound var1);
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound var1);
    
    public void onChunkLoad()
    {
    }
    
    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double... numbers)
    {
	NBTTagList var2 = new NBTTagList();
	double[] var3 = numbers;
	int var4 = numbers.length;
	
	for (int var5 = 0; var5 < var4; ++var5)
	{
	    double var6 = var3[var5];
	    var2.appendTag(new NBTTagDouble(var6));
	}
	
	return var2;
    }
    
    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float... numbers)
    {
	NBTTagList var2 = new NBTTagList();
	float[] var3 = numbers;
	int var4 = numbers.length;
	
	for (int var5 = 0; var5 < var4; ++var5)
	{
	    float var6 = var3[var5];
	    var2.appendTag(new NBTTagFloat(var6));
	}
	
	return var2;
    }
    
    public EntityItem dropItem(Item itemIn, int size)
    {
	return this.dropItemWithOffset(itemIn, size, 0.0F);
    }
    
    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY)
    {
	return this.entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }
    
    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY)
    {
	if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
	{
	    EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
	    var3.setDefaultPickupDelay();
	    this.worldObj.spawnEntityInWorld(var3);
	    return var3;
	}
	else
	{
	    return null;
	}
    }
    
    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
	return !this.isDead;
    }
    
    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
	if (this.noClip)
	{
	    return false;
	}
	else
	{
	    for (int var1 = 0; var1 < 8; ++var1)
	    {
		double var2 = this.posX + ((var1 >> 0) % 2 - 0.5F) * this.width * 0.8F;
		double var4 = this.posY + ((var1 >> 1) % 2 - 0.5F) * 0.1F;
		double var6 = this.posZ + ((var1 >> 2) % 2 - 0.5F) * this.width * 0.8F;
		
		if (this.worldObj.getBlockState(new BlockPos(var2, var4 + this.getEyeHeight(), var6)).getBlock().isVisuallyOpaque())
		{
		    return true;
		}
	    }
	    
	    return false;
	}
    }
    
    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn)
    {
	return false;
    }
    
    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
	return null;
    }
    
    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
	if (this.ridingEntity.isDead)
	{
	    this.ridingEntity = null;
	}
	else
	{
	    this.motionX = 0.0D;
	    this.motionY = 0.0D;
	    this.motionZ = 0.0D;
	    this.onUpdate();
	    
	    if (this.ridingEntity != null)
	    {
		this.ridingEntity.updateRiderPosition();
		this.entityRiderYawDelta += this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw;
		
		for (this.entityRiderPitchDelta += this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch; this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D)
		{
		    ;
		}
		
		while (this.entityRiderYawDelta < -180.0D)
		{
		    this.entityRiderYawDelta += 360.0D;
		}
		
		while (this.entityRiderPitchDelta >= 180.0D)
		{
		    this.entityRiderPitchDelta -= 360.0D;
		}
		
		while (this.entityRiderPitchDelta < -180.0D)
		{
		    this.entityRiderPitchDelta += 360.0D;
		}
		
		double var1 = this.entityRiderYawDelta * 0.5D;
		double var3 = this.entityRiderPitchDelta * 0.5D;
		float var5 = 10.0F;
		
		if (var1 > var5)
		{
		    var1 = var5;
		}
		
		if (var1 < (-var5))
		{
		    var1 = (-var5);
		}
		
		if (var3 > var5)
		{
		    var3 = var5;
		}
		
		if (var3 < (-var5))
		{
		    var3 = (-var5);
		}
		
		this.entityRiderYawDelta -= var1;
		this.entityRiderPitchDelta -= var3;
	    }
	}
    }
    
    public void updateRiderPosition()
    {
	if (this.riddenByEntity != null)
	{
	    this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	}
    }
    
    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
	return 0.0D;
    }
    
    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
	return this.height * 0.75D;
    }
    
    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn)
    {
	this.entityRiderPitchDelta = 0.0D;
	this.entityRiderYawDelta = 0.0D;
	
	if (entityIn == null)
	{
	    if (this.ridingEntity != null)
	    {
		this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
		this.ridingEntity.riddenByEntity = null;
	    }
	    
	    this.ridingEntity = null;
	}
	else
	{
	    if (this.ridingEntity != null)
	    {
		this.ridingEntity.riddenByEntity = null;
	    }
	    
	    if (entityIn != null)
	    {
		for (Entity var2 = entityIn.ridingEntity; var2 != null; var2 = var2.ridingEntity)
		{
		    if (var2 == this)
		    {
			return;
		    }
		}
	    }
	    
	    this.ridingEntity = entityIn;
	    entityIn.riddenByEntity = this;
	}
    }
    
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
	this.setPosition(x, y, z);
	this.setRotation(yaw, pitch);
	List var11 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));
	
	if (!var11.isEmpty())
	{
	    double var12 = 0.0D;
	    Iterator var14 = var11.iterator();
	    
	    while (var14.hasNext())
	    {
		AxisAlignedBB var15 = (AxisAlignedBB) var14.next();
		
		if (var15.maxY > var12)
		{
		    var12 = var15.maxY;
		}
	    }
	    
	    y += var12 - this.getEntityBoundingBox().minY;
	    this.setPosition(x, y, z);
	}
    }
    
    public float getCollisionBorderSize()
    {
	return 0.1F;
    }
    
    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec()
    {
	return null;
    }
    
    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal()
    {
	if (this.timeUntilPortal > 0)
	{
	    this.timeUntilPortal = this.getPortalCooldown();
	}
	else
	{
	    double var1 = this.prevPosX - this.posX;
	    double var3 = this.prevPosZ - this.posZ;
	    
	    if (!this.worldObj.isRemote && !this.inPortal)
	    {
		int var5;
		
		if (MathHelper.abs((float) var1) > MathHelper.abs((float) var3))
		{
		    var5 = var1 > 0.0D ? EnumFacing.WEST.getHorizontalIndex() : EnumFacing.EAST.getHorizontalIndex();
		}
		else
		{
		    var5 = var3 > 0.0D ? EnumFacing.NORTH.getHorizontalIndex() : EnumFacing.SOUTH.getHorizontalIndex();
		}
		
		this.teleportDirection = var5;
	    }
	    
	    this.inPortal = true;
	}
    }
    
    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown()
    {
	return 300;
    }
    
    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z)
    {
	this.motionX = x;
	this.motionY = y;
	this.motionZ = z;
    }
    
    public void handleHealthUpdate(byte id)
    {
    }
    
    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
    }
    
    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory()
    {
	return null;
    }
    
    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
    {
    }
    
    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
	boolean var1 = this.worldObj != null && this.worldObj.isRemote;
	return !this.isImmuneToFire && (this.fire > 0 || var1 && this.getFlag(0));
    }
    
    /**
     * Returns true if the entity is riding another entity, used by render to rotate the legs to be in 'sit' position
     * for players.
     */
    public boolean isRiding()
    {
	return this.ridingEntity != null;
    }
    
    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking()
    {
	return this.getFlag(1);
    }
    
    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean sneaking)
    {
	this.setFlag(1, sneaking);
    }
    
    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting()
    {
	return this.getFlag(3);
    }
    
    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting)
    {
	this.setFlag(3, sprinting);
    }
    
    public boolean isInvisible()
    {
	return this.getFlag(5);
    }
    
    /**
     * Only used by renderer in EntityLivingBase subclasses.
     * Determines if an entity is visible or not to a specfic player, if the entity is normally invisible.
     * For EntityLivingBase subclasses, returning false when invisible will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer player)
    {
	return player.isSpectator() ? false : this.isInvisible();
    }
    
    public void setInvisible(boolean invisible)
    {
	this.setFlag(5, invisible);
    }
    
    public boolean isEating()
    {
	return this.getFlag(4);
    }
    
    public void setEating(boolean eating)
    {
	this.setFlag(4, eating);
    }
    
    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is burning; 1) is sneaking; 2) is riding
     * something; 3) is sprinting; 4) is eating
     */
    protected boolean getFlag(int flag)
    {
	return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }
    
    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int flag, boolean set)
    {
	byte var3 = this.dataWatcher.getWatchableObjectByte(0);
	
	if (set)
	{
	    this.dataWatcher.updateObject(0, Byte.valueOf((byte) (var3 | 1 << flag)));
	}
	else
	{
	    this.dataWatcher.updateObject(0, Byte.valueOf((byte) (var3 & ~(1 << flag))));
	}
    }
    
    public int getAir()
    {
	return this.dataWatcher.getWatchableObjectShort(1);
    }
    
    public void setAir(int air)
    {
	this.dataWatcher.updateObject(1, Short.valueOf((short) air));
    }
    
    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
	this.attackEntityFrom(DamageSource.lightningBolt, 5.0F);
	++this.fire;
	
	if (this.fire == 0)
	{
	    this.setFire(8);
	}
    }
    
    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn)
    {
    }
    
    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
	BlockPos var7 = new BlockPos(x, y, z);
	double var8 = x - var7.getX();
	double var10 = y - var7.getY();
	double var12 = z - var7.getZ();
	List var14 = this.worldObj.func_147461_a(this.getEntityBoundingBox());
	
	if (var14.isEmpty() && !this.worldObj.func_175665_u(var7))
	{
	    return false;
	}
	else
	{
	    byte var15 = 3;
	    double var16 = 9999.0D;
	    
	    if (!this.worldObj.func_175665_u(var7.west()) && var8 < var16)
	    {
		var16 = var8;
		var15 = 0;
	    }
	    
	    if (!this.worldObj.func_175665_u(var7.east()) && 1.0D - var8 < var16)
	    {
		var16 = 1.0D - var8;
		var15 = 1;
	    }
	    
	    if (!this.worldObj.func_175665_u(var7.up()) && 1.0D - var10 < var16)
	    {
		var16 = 1.0D - var10;
		var15 = 3;
	    }
	    
	    if (!this.worldObj.func_175665_u(var7.north()) && var12 < var16)
	    {
		var16 = var12;
		var15 = 4;
	    }
	    
	    if (!this.worldObj.func_175665_u(var7.south()) && 1.0D - var12 < var16)
	    {
		var16 = 1.0D - var12;
		var15 = 5;
	    }
	    
	    float var18 = this.rand.nextFloat() * 0.2F + 0.1F;
	    
	    if (var15 == 0)
	    {
		this.motionX = (-var18);
	    }
	    
	    if (var15 == 1)
	    {
		this.motionX = var18;
	    }
	    
	    if (var15 == 3)
	    {
		this.motionY = var18;
	    }
	    
	    if (var15 == 4)
	    {
		this.motionZ = (-var18);
	    }
	    
	    if (var15 == 5)
	    {
		this.motionZ = var18;
	    }
	    
	    return true;
	}
    }
    
    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
	this.isInWeb = true;
	this.fallDistance = 0.0F;
    }
    
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
    public String getCommandSenderName()
    {
	if (this.hasCustomName())
	{
	    return this.getCustomNameTag();
	}
	else
	{
	    String var1 = EntityList.getEntityString(this);
	    
	    if (var1 == null)
	    {
		var1 = "generic";
	    }
	    
	    return StatCollector.translateToLocal("entity." + var1 + ".name");
	}
    }
    
    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts()
    {
	return null;
    }
    
    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn)
    {
	return this == entityIn;
    }
    
    public float getRotationYawHead()
    {
	return 0.0F;
    }
    
    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation)
    {
    }
    
    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
	return true;
    }
    
    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
	return false;
    }
    
    @Override
    public String toString()
    {
	return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.getCommandSenderName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) });
    }
    
    public boolean isEntityInvulnerable(DamageSource source)
    {
	return this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }
    
    /**
     * Sets this entity's location and angles to the location and angles of the passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn)
    {
	this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }
    
    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    public void copyDataFromOld(Entity entityIn)
    {
	NBTTagCompound var2 = new NBTTagCompound();
	entityIn.writeToNBT(var2);
	this.readFromNBT(var2);
	this.timeUntilPortal = entityIn.timeUntilPortal;
	this.teleportDirection = entityIn.teleportDirection;
    }
    
    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId)
    {
	if (!this.worldObj.isRemote && !this.isDead)
	{
	    this.worldObj.theProfiler.startSection("changeDimension");
	    MinecraftServer var2 = MinecraftServer.getServer();
	    int var3 = this.dimension;
	    WorldServer var4 = var2.worldServerForDimension(var3);
	    WorldServer var5 = var2.worldServerForDimension(dimensionId);
	    this.dimension = dimensionId;
	    
	    if (var3 == 1 && dimensionId == 1)
	    {
		var5 = var2.worldServerForDimension(0);
		this.dimension = 0;
	    }
	    
	    this.worldObj.removeEntity(this);
	    this.isDead = false;
	    this.worldObj.theProfiler.startSection("reposition");
	    var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
	    this.worldObj.theProfiler.endStartSection("reloading");
	    Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);
	    
	    if (var6 != null)
	    {
		var6.copyDataFromOld(this);
		
		if (var3 == 1 && dimensionId == 1)
		{
		    BlockPos var7 = this.worldObj.getTopSolidOrLiquidBlock(var5.getSpawnPoint());
		    var6.moveToBlockPosAndAngles(var7, var6.rotationYaw, var6.rotationPitch);
		}
		
		var5.spawnEntityInWorld(var6);
	    }
	    
	    this.isDead = true;
	    this.worldObj.theProfiler.endSection();
	    var4.resetUpdateEntityTick();
	    var5.resetUpdateEntityTick();
	    this.worldObj.theProfiler.endSection();
	}
    }
    
    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
    {
	return blockStateIn.getBlock().getExplosionResistance(this);
    }
    
    public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
    {
	return true;
    }
    
    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight()
    {
	return 3;
    }
    
    public int getTeleportDirection()
    {
	return this.teleportDirection;
    }
    
    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate()
    {
	return false;
    }
    
    public void addEntityCrashInfo(CrashReportCategory category)
    {
	category.addCrashSectionCallable("Entity Type", new Callable()
	{
	    @Override
	    public String call()
	    {
		return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
	    }
	});
	category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
	category.addCrashSectionCallable("Entity Name", new Callable()
	{
	    @Override
	    public String call()
	    {
		return Entity.this.getCommandSenderName();
	    }
	});
	category.addCrashSection("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) }));
	category.addCrashSection("Entity\'s Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
	category.addCrashSection("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ) }));
	category.addCrashSectionCallable("Entity\'s Rider", new Callable()
	{
	    public String func_180118_a()
	    {
		return Entity.this.riddenByEntity.toString();
	    }
	    
	    @Override
	    public Object call()
	    {
		return this.func_180118_a();
	    }
	});
	category.addCrashSectionCallable("Entity\'s Vehicle", new Callable()
	{
	    public String func_180116_a()
	    {
		return Entity.this.ridingEntity.toString();
	    }
	    
	    @Override
	    public Object call()
	    {
		return this.func_180116_a();
	    }
	});
    }
    
    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
	return this.isBurning();
    }
    
    public UUID getUniqueID()
    {
	return this.entityUniqueID;
    }
    
    public boolean isPushedByWater()
    {
	return true;
    }
    
    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    public IChatComponent getDisplayName()
    {
	ChatComponentText var1 = new ChatComponentText(this.getCommandSenderName());
	var1.getChatStyle().setChatHoverEvent(this.getHoverEvent());
	var1.getChatStyle().setInsertion(this.getUniqueID().toString());
	return var1;
    }
    
    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name)
    {
	this.dataWatcher.updateObject(2, name);
    }
    
    public String getCustomNameTag()
    {
	return this.dataWatcher.getWatchableObjectString(2);
    }
    
    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
	return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }
    
    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag)
    {
	this.dataWatcher.updateObject(3, Byte.valueOf((byte) (alwaysRenderNameTag ? 1 : 0)));
    }
    
    public boolean getAlwaysRenderNameTag()
    {
	return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }
    
    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double x, double y, double z)
    {
	this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
    }
    
    public boolean getAlwaysRenderNameTagForRender()
    {
	return this.getAlwaysRenderNameTag();
    }
    
    public void func_145781_i(int p_145781_1_)
    {
    }
    
    public EnumFacing getHorizontalFacing()
    {
	return EnumFacing.getHorizontal(MathHelper.floor_double(this.rotationYaw * 4.0F / 360.0F + 0.5D) & 3);
    }
    
    protected HoverEvent getHoverEvent()
    {
	NBTTagCompound var1 = new NBTTagCompound();
	String var2 = EntityList.getEntityString(this);
	var1.setString("id", this.getUniqueID().toString());
	
	if (var2 != null)
	{
	    var1.setString("type", var2);
	}
	
	var1.setString("name", this.getCommandSenderName());
	return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(var1.toString()));
    }
    
    public boolean func_174827_a(EntityPlayerMP player)
    {
	return true;
    }
    
    public AxisAlignedBB getEntityBoundingBox()
    {
	return this.boundingBox;
    }
    
    public void setEntityBoundingBox(AxisAlignedBB bb)
    {
	this.boundingBox = bb;
    }
    
    public float getEyeHeight()
    {
	return this.height * 0.85F;
    }
    
    public boolean isOutsideBorder()
    {
	return this.isOutsideBorder;
    }
    
    public void setOutsideBorder(boolean outsideBorder)
    {
	this.isOutsideBorder = outsideBorder;
    }
    
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
	return false;
    }
    
    /**
     * Send a chat message to the CommandSender
     *  
     * @param component The ChatComponent to send
     */
    @Override
    public void addChatMessage(IChatComponent component)
    {
    }
    
    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     *  
     * @param permLevel The permission level required to execute the command
     * @param commandName The name of the command
     */
    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
	return true;
    }
    
    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    @Override
    public BlockPos getPosition()
    {
	return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }
    
    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    @Override
    public Vec3 getPositionVector()
    {
	return new Vec3(this.posX, this.posY, this.posZ);
    }
    
    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    @Override
    public World getEntityWorld()
    {
	return this.worldObj;
    }
    
    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    @Override
    public Entity getCommandSenderEntity()
    {
	return this;
    }
    
    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    @Override
    public boolean sendCommandFeedback()
    {
	return false;
    }
    
    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
	this.cmdResultStats.func_179672_a(this, type, amount);
    }
    
    public CommandResultStats getCommandStats()
    {
	return this.cmdResultStats;
    }
    
    public void func_174817_o(Entity entityIn)
    {
	this.cmdResultStats.func_179671_a(entityIn.getCommandStats());
    }
    
    public NBTTagCompound getNBTTagCompound()
    {
	return null;
    }
    
    public void func_174834_g(NBTTagCompound compound)
    {
    }
    
    /**
     * Bhop version of interactWith that includes vector information on where precisely the player targeted.
     */
    public boolean interactAt(EntityPlayer player, Vec3 p_174825_2_)
    {
	return false;
    }
    
    public boolean func_180427_aV()
    {
	return false;
    }
    
    protected void func_174815_a(EntityLivingBase entityLivingBaseIn, Entity entityIn)
    {
	if (entityIn instanceof EntityLivingBase)
	{
	    EnchantmentHelper.func_151384_a((EntityLivingBase) entityIn, entityLivingBaseIn);
	}
	
	EnchantmentHelper.func_151385_b(entityLivingBaseIn, entityIn);
    }
}
