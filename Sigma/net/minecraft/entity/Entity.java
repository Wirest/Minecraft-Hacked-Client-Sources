package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import info.sigmaclient.Client;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.movement.Highjump;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.movement.NoSlowdown;
import info.sigmaclient.module.impl.movement.Sprint;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.module.impl.render.Freecam;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.misc.ChatUtil;
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
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Entity implements ICommandSender {
    private static final AxisAlignedBB field_174836_a = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;

    /**
     * Blocks entities from spawning when they do their AABB check to make sure
     * the spot is clear of entities that can prevent spawning.
     */
    public boolean preventEntitySpawning;

    /**
     * The entity that is riding this entity
     */
    public Entity riddenByEntity;

    /**
     * The entity we are currently riding
     */
    public Entity ridingEntity;
    public boolean forceSpawn;

    /**
     * Reference to the World object.
     */
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;

    /**
     * Entity position X
     */
    public double posX;

    /**
     * Entity position Y
     */
    public double posY;

    /**
     * Entity position Z
     */
    public double posZ;

    /**
     * Entity motion X
     */
    public double motionX;

    /**
     * Entity motion Y
     */
    public double motionY;

    /**
     * Entity motion Z
     */
    public double motionZ;

    /**
     * Entity rotation Yaw
     */
    public float rotationYaw;

    /**
     * Entity rotation Pitch
     */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;

    /**
     * Axis aligned bounding box.
     */
    public AxisAlignedBB boundingBox;
    public boolean onGround;

    /**
     * True if after a move this entity has collided with something on X- or
     * Z-axis
     */
    public boolean isCollidedHorizontally;

    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;

    /**
     * True if after a move this entity has collided with something either
     * vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    private boolean isOutsideBorder;

    /**
     * gets set by setEntityDead, so this must be the flag whether an Entity is
     * dead (inactive may be better term)
     */
    public boolean isDead;

    /**
     * How wide this entity is considered to be
     */
    public float width;

    /**
     * How high this entity is considered to be
     */
    public float height;

    /**
     * The previous ticks distance walked multiplied by 0.6
     */
    public float prevDistanceWalkedModified;

    /**
     * The distance walked multiplied by 0.6
     */
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;

    /**
     * The distance that has to be exceeded in order to triger a new step sound
     * and an onEntityWalking event on a block
     */
    private int nextStepDistance;

    /**
     * The entity's X coordinate at the previous tick, used to calculate
     * position during rendering routines
     */
    public double lastTickPosX;

    /**
     * The entity's Y coordinate at the previous tick, used to calculate
     * position during rendering routines
     */
    public double lastTickPosY;

    /**
     * The entity's Z coordinate at the previous tick, used to calculate
     * position during rendering routines
     */
    public double lastTickPosZ;

    /**
     * How high this entity can step up when running into a block to try to get
     * over it (currently make note the entity will always step up this amount
     * and not just the amount needed)
     */
    public float stepHeight;

    /**
     * Whether this entity won't clip with collision or not (make note it won't
     * disable gravity)
     */
    public boolean noClip;

    /**
     * Reduces the velocity applied by entity collisions by the specified
     * percent.
     */
    public float entityCollisionReduction;
    protected Random rand;

    /**
     * How many ticks has this entity had ran since being alive
     */
    public int ticksExisted;

    /**
     * The amount of ticks you have to stand inside of fire before be set on
     * fire
     */
    public int fireResistance;
    private int fire;

    /**
     * Whether this entity is currently inside of water (if it handles water
     * movement that is)
     */
    protected boolean inWater;

    /**
     * Remaining time an entity will be "immune" to further damage after being
     * hurt.
     */
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

    /**
     * Has this entity been added to the chunk its within
     */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;

    /**
     * Render entity even if it is outside the camera frustum. Only true in
     * EntityFish for now. Used in RenderGlobal: render if ignoreFrustumCheck or
     * in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;

    /**
     * Whether the entity is inside a Portal
     */
    protected boolean inPortal;
    protected int portalCounter;

    /**
     * Which dimension the player is in (-1 = the Nether, 0 = normal world)
     */
    public int dimension;
    protected int teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    private final CommandResultStats field_174837_as;
    private static final String __OBFID = "CL_00001533";

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int id) {
        entityId = id;
    }

    public void func_174812_G() {
        setDead();
    }

    public Entity(World worldIn) {
        entityId = Entity.nextEntityID++;
        renderDistanceWeight = 1.0D;
        boundingBox = Entity.field_174836_a;
        width = 0.6F;
        height = 1.8F;
        nextStepDistance = 1;
        rand = new Random();
        fireResistance = 1;
        firstUpdate = true;
        entityUniqueID = MathHelper.func_180182_a(rand);
        field_174837_as = new CommandResultStats();
        worldObj = worldIn;
        setPosition(0.0D, 0.0D, 0.0D);

        if (worldIn != null) {
            dimension = worldIn.provider.getDimensionId();
        }

        dataWatcher = new DataWatcher(this);
        dataWatcher.addObject(0, Byte.valueOf((byte) 0));
        dataWatcher.addObject(1, Short.valueOf((short) 300));
        dataWatcher.addObject(3, Byte.valueOf((byte) 0));
        dataWatcher.addObject(2, "");
        dataWatcher.addObject(4, Byte.valueOf((byte) 0));
        entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher() {
        return dataWatcher;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return p_equals_1_ instanceof Entity ? ((Entity) p_equals_1_).entityId == entityId : false;
    }

    @Override
    public int hashCode() {
        return entityId;
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other
     * requirements for this entity to be spawned (only actually used on players
     * though its also on Entity)
     */
    protected void preparePlayerToSpawn() {
        if (worldObj != null) {
            while (posY > 0.0D && posY < 256.0D) {
                setPosition(posX, posY, posZ);

                if (worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
                    break;
                }

                ++posY;
            }

            motionX = motionY = motionZ = 0.0D;
            rotationPitch = 0.0F;
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        isDead = true;
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float var3 = this.width;
            this.width = width;
            this.height = height;
            func_174826_a(new AxisAlignedBB(getEntityBoundingBox().minX, getEntityBoundingBox().minY,
                    getEntityBoundingBox().minZ, getEntityBoundingBox().minX + this.width,
                    getEntityBoundingBox().minY + this.height, getEntityBoundingBox().minZ + this.width));

            if (this.width > var3 && !firstUpdate && !worldObj.isRemote) {
                moveEntity(var3 - this.width, 0.0D, var3 - this.width);
            }
        }
    }

    /**
     * Sets the rotation of the entity. Args: yaw, pitch (both in degrees)
     */
    protected void setRotation(float yaw, float pitch) {
        rotationYaw = yaw % 360.0F;
        rotationPitch = pitch % 360.0F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set
     * up a bounding box.
     */
    public void setPosition(double x, double y, double z) {
        posX = x;
        posY = y;
        posZ = z;
        float var7 = width / 2.0F;
        float var8 = height;
        func_174826_a(new AxisAlignedBB(x - var7, y, z - var7, x + var7, y + var8, z + var7));
    }

    /**
     * Adds 15% to the entity's yaw and subtracts 15% from the pitch. Clamps
     * pitch from -90 to 90. Both arguments in degrees.
     */
    public void setAngles(float yaw, float pitch) {
        float var3 = rotationPitch;
        float var4 = rotationYaw;
        rotationYaw = (float) (rotationYaw + yaw * 0.15D);
        rotationPitch = (float) (rotationPitch - pitch * 0.15D);
        rotationPitch = MathHelper.clamp_float(rotationPitch, -90.0F, 90.0F);
        prevRotationPitch += rotationPitch - var3;
        prevRotationYaw += rotationYaw - var4;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        onEntityUpdate();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate() {
        worldObj.theProfiler.startSection("entityBaseTick");

        if (ridingEntity != null && ridingEntity.isDead) {
            ridingEntity = null;
        }

        prevDistanceWalkedModified = distanceWalkedModified;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw = rotationYaw;

        if (!worldObj.isRemote && worldObj instanceof WorldServer) {
            worldObj.theProfiler.startSection("portal");
            MinecraftServer var1 = ((WorldServer) worldObj).func_73046_m();
            int var2 = getMaxInPortalTime();

            if (inPortal) {
                if (var1.getAllowNether()) {
                    if (ridingEntity == null && portalCounter++ >= var2) {
                        portalCounter = var2;
                        timeUntilPortal = getPortalCooldown();
                        byte var3;

                        if (worldObj.provider.getDimensionId() == -1) {
                            var3 = 0;
                        } else {
                            var3 = -1;
                        }

                        travelToDimension(var3);
                    }

                    inPortal = false;
                }
            } else {
                if (portalCounter > 0) {
                    portalCounter -= 4;
                }

                if (portalCounter < 0) {
                    portalCounter = 0;
                }
            }

            if (timeUntilPortal > 0) {
                --timeUntilPortal;
            }

            worldObj.theProfiler.endSection();
        }

        func_174830_Y();
        if (Client.getModuleManager().get(NoSlowdown.class).isEnabled() && PlayerUtil.isInLiquid()) {

        } else {
            handleWaterMovement();
        }

        if (worldObj.isRemote) {
            fire = 0;
        } else if (fire > 0) {
            if (isImmuneToFire) {
                fire -= 4;

                if (fire < 0) {
                    fire = 0;
                }
            } else {
                if (fire % 20 == 0) {
                    attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --fire;
            }
        }

        if (func_180799_ab()) {
            setOnFireFromLava();
            fallDistance *= 0.5F;
        }

        if (posY < -64.0D) {
            kill();
        }

        if (!worldObj.isRemote) {
            setFlag(0, fire > 0);
        }

        firstUpdate = false;
        worldObj.theProfiler.endSection();
    }

    /**
     * Return the amount of time this entity should stay in a portal before
     * being transported.
     */
    public int getMaxInPortalTime() {
        return 0;
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava() {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.lava, 4.0F);
            setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of
     * existing fire.
     */
    public void setFire(int seconds) {
        int var2 = seconds * 20;
        var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);

        if (fire < var2) {
            fire = var2;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish() {
        fire = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill() {
        setDead();
    }

    /**
     * Checks if the offset position from the entity's current position is
     * inside of liquid. Args: x, y, z
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        AxisAlignedBB var7 = getEntityBoundingBox().offset(x, y, z);
        return func_174809_b(var7);
    }

    private boolean func_174809_b(AxisAlignedBB p_174809_1_) {
        return worldObj.getCollidingBoundingBoxes(this, p_174809_1_).isEmpty() && !worldObj.isAnyLiquid(p_174809_1_);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double x, double y, double z)
    {
        if (this.noClip)
        {
            this.func_174826_a(this.getEntityBoundingBox().offset(x, y, z));
            this.func_174829_m();
        }
        else
        {
            this.worldObj.theProfiler.startSection("move");
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;

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

            double d3 = x;
            double d4 = y;
            double d5 = z;
            //TODO SAFEWALK
            boolean flag = (onGround) && (isSneaking() || Client.getModuleManager().get(Scaffold.class).isEnabled()) && this instanceof EntityPlayer;
            Module longjump = Client.getModuleManager().get(LongJump.class);
            if(longjump.isEnabled() && onGround){
            	if(((Options)longjump.getSetting(LongJump.MODE).getValue()).getSelected().equals("NCP") || ((Options)longjump.getSetting(LongJump.MODE).getValue()).getSelected().equals("Hypixel")){
            		if((Boolean)longjump.getSetting(LongJump.GLIDE).getValue()){
            			flag = true;
            		}
            	}
            }
            if(this.isCollidedVertically && Client.getModuleManager().get(Scaffold.class).isEnabled() && (Scaffold.currentMode.equalsIgnoreCase("Legit") || Scaffold.currentMode.equalsIgnoreCase("Cubecraft"))){
            	flag = true;
            }
 
            Module highjump = Client.getModuleManager().get(Highjump.class);
            if(highjump.isEnabled()){
            	if(((Options) highjump.getSetting(Highjump.MODE).getValue()).getSelected().equals("Mineplex") && onGround){
            		flag = true;
            	}
            }
            if (flag)
            {
                double d6;

                for (d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x)
                {
                    if (x < d6 && x >= -d6)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= d6;
                    }
                    else
                    {
                        x += d6;
                    }
                }

                for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z)
                {
                    if (z < d6 && z >= -d6)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= d6;
                    }
                    else
                    {
                        z += d6;
                    }
                }

                for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z)
                {
                    if (x < d6 && x >= -d6)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= d6;
                    }
                    else
                    {
                        x += d6;
                    }

                    d3 = x;

                    if (z < d6 && z >= -d6)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= d6;
                    }
                    else
                    {
                        z += d6;
                    }
                }
            }

            List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            for (AxisAlignedBB axisalignedbb1 : list1)
            {
                y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y);
            }

            this.func_174826_a(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
            boolean flag1 = this.onGround || d4 != y && d4 < 0.0D;

            for (AxisAlignedBB axisalignedbb2 : list1)
            {
                x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x);
            }

            this.func_174826_a(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb13 : list1)
            {
                z = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z);
            }

            this.func_174826_a(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));
            EventStep em = (EventStep) EventSystem.getInstance(EventStep.class);
            if (this == Minecraft.getMinecraft().thePlayer) {
                em.fire(true, stepHeight);
            }
            if (em.getStepHeight() > 0.0F && flag1 && (d3 != x || d5 != z))
            {
                double d11 = x;
                double d7 = y;
                double d8 = z;
                AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
                this.func_174826_a(axisalignedbb);
                y = (double)em.getStepHeight();
                List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d3, y, d5));
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
                double d9 = y;

                for (AxisAlignedBB axisalignedbb6 : list)
                {
                    d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
                double d15 = d3;

                for (AxisAlignedBB axisalignedbb7 : list)
                {
                    d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
                }

                axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
                double d16 = d5;

                for (AxisAlignedBB axisalignedbb8 : list)
                {
                    d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
                double d17 = y;

                for (AxisAlignedBB axisalignedbb9 : list)
                {
                    d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
                double d18 = d3;

                for (AxisAlignedBB axisalignedbb10 : list)
                {
                    d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
                }

                axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
                double d19 = d5;

                for (AxisAlignedBB axisalignedbb11 : list)
                {
                    d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d10 = d18 * d18 + d19 * d19;

                if (d20 > d10)
                {
                    x = d15;
                    z = d16;
                    y = -d9;
                    this.func_174826_a(axisalignedbb4);
                }
                else
                {
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.func_174826_a(axisalignedbb14);
                }

                for (AxisAlignedBB axisalignedbb12 : list)
                {
                    y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y);
                }

                this.func_174826_a(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

                if (d11 * d11 + d8 * d8 >= x * x + z * z)
                {
                    x = d11;
                    y = d7;
                    z = d8;
                    this.func_174826_a(axisalignedbb3);
                }
                if (this == Minecraft.getMinecraft().thePlayer)
                    em.fire(false, stepHeight, 1D + y);
            }

            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.func_174829_m();
            this.isCollidedHorizontally = d3 != x || d5 != z;
            this.isCollidedVertically = d4 != y;
            this.onGround = this.isCollidedVertically && d4 < 0.0D;
            if(Client.getModuleManager().isEnabled(Fly.class) && !((Options) Client.getModuleManager().get(Fly.class).getSetting(Fly.MODE).getValue()).getSelected().equals("Vanilla")) {
            	if((Boolean) Client.getModuleManager().get(Fly.class).getSetting(Fly.BOBBING).getValue()){
            		this.onGround = true;
            	}
            }
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();

            if (block1.getMaterial() == Material.air)
            {
                Block block = this.worldObj.getBlockState(blockpos.offsetDown()).getBlock();

                if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
                {
                    block1 = block;
                    blockpos = blockpos.offsetDown();
                }
            }

            this.func_180433_a(y, this.onGround, block1, blockpos);

            if (d3 != x)
            {
                this.motionX = 0.0D;
            }

            if (d5 != z)
            {
                this.motionZ = 0.0D;
            }

            if (d4 != y)
            {
                block1.onLanded(this.worldObj, this);
            }
            if (this.canTriggerWalking() && (!flag || Client.getModuleManager().isEnabled(Scaffold.class)) && this.ridingEntity == null)
            {
                double d12 = this.posX - d0;
                double d13 = this.posY - d1;
                double d14 = this.posZ - d2;

                if (block1 != Blocks.ladder)
                {
                    d13 = 0.0D;
                }

                if (block1 != null && this.onGround)
                {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
                }

                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && block1.getMaterial() != Material.air)
                {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater())
                    {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;

                        if (f > 1.0F)
                        {
                            f = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }

                    this.func_180429_a(blockpos, block1);
                }
            }

            try
            {
                this.doBlockCollisions();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag2 = this.isWet();

            if (this.worldObj.func_147470_e(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D)))
            {
                this.dealFireDamage(1);

                if (!flag2)
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

            if (flag2 && this.fire > 0)
            {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }

            this.worldObj.theProfiler.endSection();
        }
    }

    private void func_174829_m() {
        posX = (getEntityBoundingBox().minX + getEntityBoundingBox().maxX) / 2.0D;
        posY = getEntityBoundingBox().minY;
        posZ = (getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ) / 2.0D;
    }

    protected String getSwimSound() {
        return "game.neutral.swim";
    }

    protected void doBlockCollisions() {
        BlockPos var1 = new BlockPos(getEntityBoundingBox().minX + 0.001D, getEntityBoundingBox().minY + 0.001D,
                getEntityBoundingBox().minZ + 0.001D);
        BlockPos var2 = new BlockPos(getEntityBoundingBox().maxX - 0.001D, getEntityBoundingBox().maxY - 0.001D,
                getEntityBoundingBox().maxZ - 0.001D);

        if (worldObj.isAreaLoaded(var1, var2)) {
            for (int var3 = var1.getX(); var3 <= var2.getX(); ++var3) {
                for (int var4 = var1.getY(); var4 <= var2.getY(); ++var4) {
                    for (int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5) {
                        BlockPos var6 = new BlockPos(var3, var4, var5);
                        IBlockState var7 = worldObj.getBlockState(var6);

                        try {
                            var7.getBlock().onEntityCollidedWithBlock(worldObj, var6, var7, this);
                        } catch (Throwable var11) {
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

    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        Block.SoundType var3 = p_180429_2_.stepSound;

        if (worldObj.getBlockState(p_180429_1_.offsetUp()).getBlock() == Blocks.snow_layer) {
            var3 = Blocks.snow_layer.stepSound;
            playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
        } else if (!p_180429_2_.getMaterial().isLiquid()) {
            playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
        }
    }

    public void playSound(String name, float volume, float pitch) {
        if (!isSlient()) {
            worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }

    /**
     * @return True if this entity will not play sounds
     */
    public boolean isSlient() {
        return dataWatcher.getWatchableObjectByte(4) == 1;
    }

    public void func_174810_b(boolean p_174810_1_) {
        dataWatcher.updateObject(4, Byte.valueOf((byte) (p_174810_1_ ? 1 : 0)));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they
     * walk on. used for spiders and wolves to prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return true;
    }

    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
        if (p_180433_3_) {
            if (fallDistance > 0.0F) {
                if (p_180433_4_ != null) {
                    p_180433_4_.onFallenUpon(worldObj, p_180433_5_, this, fallDistance);
                } else {
                    fall(fallDistance, 1.0F);
                }

                fallDistance = 0.0F;
            }
        } else if (p_180433_1_ < 0.0D) {
            fallDistance = (float) (fallDistance - p_180433_1_);
        }
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity
     * isn't immune to fire damage. Args: amountDamage
     */
    protected void dealFireDamage(int amount) {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.inFire, amount);
        }
    }

    public final boolean isImmuneToFire() {
        return isImmuneToFire;
    }

    public void fall(float distance, float damageMultiplier) {
        if (riddenByEntity != null) {
            riddenByEntity.fall(distance, damageMultiplier);
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain
     * (used in wolves).
     */
    public boolean isWet() {
        return inWater || worldObj.func_175727_C(new BlockPos(posX, posY, posZ))
                || worldObj.func_175727_C(new BlockPos(posX, posY + height, posZ));
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a
     * result of handleWaterMovement() returning true)
     */
    public boolean isInWater() {
        return inWater;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters
     * velocity to the entity
     */
    public boolean handleWaterMovement() {
        if (worldObj.handleMaterialAcceleration(
                getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D),
                Material.water, this)) {
            if (!inWater && !firstUpdate) {
                resetHeight();
            }

            fallDistance = 0.0F;
            inWater = true;
            fire = 0;
        } else {
            inWater = false;
        }

        return inWater;
    }

    /**
     * sets the players height back to normal after doing things like sleeping
     * and dieing
     */
    protected void resetHeight() {
        float var1 = MathHelper.sqrt_double(
                motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D)
                * 0.2F;

        if (var1 > 1.0F) {
            var1 = 1.0F;
        }

        playSound(getSplashSound(), var1, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
        float var2 = MathHelper.floor_double(getEntityBoundingBox().minY);
        int var3;
        float var4;
        float var5;

        for (var3 = 0; var3 < 1.0F + width * 20.0F; ++var3) {
            var4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            var5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + var4, var2 + 1.0F, posZ + var5, motionX,
                    motionY - rand.nextFloat() * 0.2F, motionZ, new int[0]);
        }

        for (var3 = 0; var3 < 1.0F + width * 20.0F; ++var3) {
            var4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            var5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + var4, var2 + 1.0F, posZ + var5, motionX,
                    motionY, motionZ, new int[0]);
        }
    }

    public void func_174830_Y() {
        if (isSprinting() && !isInWater()) {
            func_174808_Z();
        }
    }

    protected void func_174808_Z() {
        int var1 = MathHelper.floor_double(posX);
        int var2 = MathHelper.floor_double(posY - 0.20000000298023224D);
        int var3 = MathHelper.floor_double(posZ);
        BlockPos var4 = new BlockPos(var1, var2, var3);
        IBlockState var5 = worldObj.getBlockState(var4);
        Block var6 = var5.getBlock();

        if (var6.getRenderType() != -1) {
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + (rand.nextFloat() - 0.5D) * width,
                    getEntityBoundingBox().minY + 0.1D, posZ + (rand.nextFloat() - 0.5D) * width, -motionX * 4.0D, 1.5D,
                    -motionZ * 4.0D, new int[]{Block.getStateId(var5)});
        }
    }

    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }

    /**
     * Checks if the current block the entity is within of the specified
     * material type
     */
    public boolean isInsideOfMaterial(Material materialIn) {
        double var2 = posY + getEyeHeight();
        BlockPos var4 = new BlockPos(posX, var2, posZ);
        IBlockState var5 = worldObj.getBlockState(var4);
        Block var6 = var5.getBlock();

        if (var6.getMaterial() == materialIn) {
            float var7 = BlockLiquid.getLiquidHeightPercent(var5.getBlock().getMetaFromState(var5)) - 0.11111111F;
            float var8 = var4.getY() + 1 - var7;
            boolean var9 = var2 < var8;
            return !var9 && this instanceof EntityPlayer ? false : var9;
        } else {
            return false;
        }
    }

    public boolean func_180799_ab() {
        return worldObj.isMaterialInBB(
                getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D),
                Material.lava);
    }

    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float strafe, float forward, float friction) {
        float var4 = strafe * strafe + forward * forward;

        if (var4 >= 1.0E-4F) {
            var4 = MathHelper.sqrt_float(var4);

            if (var4 < 1.0F) {
                var4 = 1.0F;
            }

            var4 = friction / var4;
            strafe *= var4;
            forward *= var4;
            float var5 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F);
            float var6 = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F);
            motionX += strafe * var6 - forward * var5;
            motionZ += forward * var6 + strafe * var5;
        }
    }

    public int getBrightnessForRender(float p_70070_1_) {
        BlockPos var2 = new BlockPos(posX, 0.0D, posZ);

        if (worldObj.isBlockLoaded(var2)) {
            double var3 = (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * 0.66D;
            int var5 = MathHelper.floor_double(posY + var3);
            return worldObj.getCombinedLight(var2.offsetUp(var5), 0);
        } else {
            return 0;
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_) {
        BlockPos var2 = new BlockPos(posX, 0.0D, posZ);

        if (worldObj.isBlockLoaded(var2)) {
            double var3 = (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * 0.66D;
            int var5 = MathHelper.floor_double(posY + var3);
            return worldObj.getLightBrightness(var2.offsetUp(var5));
        } else {
            return 0.0F;
        }
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn) {
        worldObj = worldIn;
    }

    /**
     * Sets the entity's position and rotation.
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        prevPosX = posX = x;
        prevPosY = posY = y;
        prevPosZ = posZ = z;
        prevRotationYaw = rotationYaw = yaw;
        prevRotationPitch = rotationPitch = pitch;
        double var9 = prevRotationYaw - yaw;

        if (var9 < -180.0D) {
            prevRotationYaw += 360.0F;
        }

        if (var9 >= 180.0D) {
            prevRotationYaw -= 360.0F;
        }

        setPosition(posX, posY, posZ);
        setRotation(yaw, pitch);
    }

    public void func_174828_a(BlockPos p_174828_1_, float p_174828_2_, float p_174828_3_) {
        setLocationAndAngles(p_174828_1_.getX() + 0.5D, p_174828_1_.getY(), p_174828_1_.getZ() + 0.5D, p_174828_2_,
                p_174828_3_);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        lastTickPosX = prevPosX = posX = x;
        lastTickPosY = prevPosY = posY = y;
        lastTickPosZ = prevPosZ = posZ = z;
        rotationYaw = yaw;
        rotationPitch = pitch;
        setPosition(posX, posY, posZ);
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntity(Entity entityIn) {
        float var2 = (float) (posX - entityIn.posX);
        float var3 = (float) (posY - entityIn.posY);
        float var4 = (float) (posZ - entityIn.posZ);
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }

    /**
     * Gets the squared distance to the position. Args: x, y, z
     */
    public double getDistanceSq(double x, double y, double z) {
        double var7 = posX - x;
        double var9 = posY - y;
        double var11 = posZ - z;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double getDistanceSq(BlockPos p_174818_1_) {
        return p_174818_1_.distanceSq(posX, posY, posZ);
    }

    public double func_174831_c(BlockPos p_174831_1_) {
        return p_174831_1_.distanceSqToCenter(posX, posY, posZ);
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double x, double y, double z) {
        double var7 = posX - x;
        double var9 = posY - y;
        double var11 = posZ - z;
        return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }

    /**
     * Returns the squared distance to the entity. Args: entity
     */
    public double getDistanceSqToEntity(Entity entityIn) {
        double var2 = posX - entityIn.posX;
        double var4 = posY - entityIn.posY;
        double var6 = posZ - entityIn.posZ;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each
     * other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn) {
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this) {
            if (!entityIn.noClip && !noClip) {
                double var2 = entityIn.posX - posX;
                double var4 = entityIn.posZ - posZ;
                double var6 = MathHelper.abs_max(var2, var4);

                if (var6 >= 0.009999999776482582D) {
                    var6 = MathHelper.sqrt_double(var6);
                    var2 /= var6;
                    var4 /= var6;
                    double var8 = 1.0D / var6;

                    if (var8 > 1.0D) {
                        var8 = 1.0D;
                    }

                    var2 *= var8;
                    var4 *= var8;
                    var2 *= 0.05000000074505806D;
                    var4 *= 0.05000000074505806D;
                    var2 *= 1.0F - entityCollisionReduction;
                    var4 *= 1.0F - entityCollisionReduction;

                    if (riddenByEntity == null) {
                        addVelocity(-var2, 0.0D, -var4);
                    }

                    if (entityIn.riddenByEntity == null) {
                        entityIn.addVelocity(var2, 0.0D, var4);
                    }
                }
            }
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double x, double y, double z) {
        motionX += x;
        motionY += y;
        motionZ += z;
        isAirBorne = true;
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked() {
        velocityChanged = true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (func_180431_b(source)) {
            return false;
        } else {
            setBeenAttacked();
            return false;
        }
    }

    /**
     * interpolated look vector
     */
    public Vec3 getLook(float p_70676_1_) {
        if (p_70676_1_ == 1.0F) {
            return func_174806_f(rotationPitch, rotationYaw);
        } else {
            float var2 = prevRotationPitch + (rotationPitch - prevRotationPitch) * p_70676_1_;
            float var3 = prevRotationYaw + (rotationYaw - prevRotationYaw) * p_70676_1_;
            return func_174806_f(var2, var3);
        }
    }

    protected final Vec3 func_174806_f(float yaw, float pitch) {
        float p1 = MathHelper.cos(-pitch * 0.017453292F - (float) Math.PI);
        float p2 = MathHelper.sin(-pitch * 0.017453292F - (float) Math.PI);
        float y1 = -MathHelper.cos(-yaw * 0.017453292F);
        float y2 = MathHelper.sin(-yaw * 0.017453292F);
        return new Vec3(p2 * y1, y2, p1 * y1);
    }

    public Vec3 func_174824_e(float partial) {
        if (partial == 1.0F) {
            return new Vec3(posX, posY + getEyeHeight(), posZ);
        } else {
            double var2 = prevPosX + (posX - prevPosX) * partial;
            double var4 = prevPosY + (posY - prevPosY) * partial + getEyeHeight();
            double var6 = prevPosZ + (posZ - prevPosZ) * partial;
            return new Vec3(var2, var4, var6);
        }
    }

    public MovingObjectPosition func_174822_a(double p_174822_1_, float p_174822_3_) {
        Vec3 var4 = func_174824_e(p_174822_3_);
        Vec3 var5 = getLook(p_174822_3_);
        Vec3 var6 = var4.addVector(var5.xCoord * p_174822_1_, var5.yCoord * p_174822_1_, var5.zCoord * p_174822_1_);
        
        return worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }

    /**
     * Returns true if other Entities should be prevented from moving through
     * this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities
     * when colliding.
     */
    public boolean canBePushed() {
        return false;
    }

    /**
     * Adds a value to the player score. Currently not actually used and the
     * entity passed in does nothing. Args: entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entityIn, int amount) {
    }

    public boolean isInRangeToRender3d(double x, double y, double z) {
        double var7 = posX - x;
        double var9 = posY - y;
        double var11 = posZ - z;
        double var13 = var7 * var7 + var9 * var9 + var11 * var11;
        return isInRangeToRenderDist(var13);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance
     * and comparing it to its average edge length * 64 * renderDistanceWeight
     * Args: distance
     */
    public boolean isInRangeToRenderDist(double distance) {
        double var3 = getEntityBoundingBox().getAverageEdgeLength();
        var3 *= 64.0D * renderDistanceWeight;
        return distance < var3 * var3;
    }

    /**
     * Like writeToNBTOptional but does not check if the entity is ridden. Used
     * for saving ridden entities with their riders.
     */
    public boolean writeMountToNBT(NBTTagCompound tagCompund) {
        String var2 = getEntityString();

        if (!isDead && var2 != null) {
            tagCompund.setString("id", var2);
            writeToNBT(tagCompund);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Either write this entity to the NBT tag given and return true, or return
     * false without doing anything. If this returns false the entity is not
     * saved on disk. Ridden entities return false here as they are saved with
     * their rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
        String var2 = getEntityString();

        if (!isDead && var2 != null && riddenByEntity == null) {
            tagCompund.setString("id", var2);
            writeToNBT(tagCompund);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Save the entity to NBT (calls an abstract helper method to write extra
     * data)
     */
    public void writeToNBT(NBTTagCompound tagCompund) {
        try {
            tagCompund.setTag("Pos", newDoubleNBTList(new double[]{posX, posY, posZ}));
            tagCompund.setTag("Motion", newDoubleNBTList(new double[]{motionX, motionY, motionZ}));
            tagCompund.setTag("Rotation", newFloatNBTList(new float[]{rotationYaw, rotationPitch}));
            tagCompund.setFloat("FallDistance", fallDistance);
            tagCompund.setShort("Fire", (short) fire);
            tagCompund.setShort("Air", (short) getAir());
            tagCompund.setBoolean("OnGround", onGround);
            tagCompund.setInteger("Dimension", dimension);
            tagCompund.setBoolean("Invulnerable", invulnerable);
            tagCompund.setInteger("PortalCooldown", timeUntilPortal);
            tagCompund.setLong("UUIDMost", getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", getUniqueID().getLeastSignificantBits());

            if (getCustomNameTag() != null && getCustomNameTag().length() > 0) {
                tagCompund.setString("CustomName", getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
            }

            field_174837_as.func_179670_b(tagCompund);

            if (isSlient()) {
                tagCompund.setBoolean("Silent", isSlient());
            }

            writeEntityToNBT(tagCompund);

            if (ridingEntity != null) {
                NBTTagCompound var2 = new NBTTagCompound();

                if (ridingEntity.writeMountToNBT(var2)) {
                    tagCompund.setTag("Riding", var2);
                }
            }
        } catch (Throwable var5) {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Saving entity NBT");
            CrashReportCategory var4 = var3.makeCategory("Entity being saved");
            addEntityCrashInfo(var4);
            throw new ReportedException(var3);
        }
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read
     * specialized data)
     */
    public void readFromNBT(NBTTagCompound tagCompund) {
        try {
            NBTTagList var2 = tagCompund.getTagList("Pos", 6);
            NBTTagList var6 = tagCompund.getTagList("Motion", 6);
            NBTTagList var7 = tagCompund.getTagList("Rotation", 5);
            motionX = var6.getDouble(0);
            motionY = var6.getDouble(1);
            motionZ = var6.getDouble(2);

            if (Math.abs(motionX) > 10.0D) {
                motionX = 0.0D;
            }

            if (Math.abs(motionY) > 10.0D) {
                motionY = 0.0D;
            }

            if (Math.abs(motionZ) > 10.0D) {
                motionZ = 0.0D;
            }

            prevPosX = lastTickPosX = posX = var2.getDouble(0);
            prevPosY = lastTickPosY = posY = var2.getDouble(1);
            prevPosZ = lastTickPosZ = posZ = var2.getDouble(2);
            prevRotationYaw = rotationYaw = var7.getFloat(0);
            prevRotationPitch = rotationPitch = var7.getFloat(1);
            fallDistance = tagCompund.getFloat("FallDistance");
            fire = tagCompund.getShort("Fire");
            setAir(tagCompund.getShort("Air"));
            onGround = tagCompund.getBoolean("OnGround");
            dimension = tagCompund.getInteger("Dimension");
            invulnerable = tagCompund.getBoolean("Invulnerable");
            timeUntilPortal = tagCompund.getInteger("PortalCooldown");

            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
                entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            } else if (tagCompund.hasKey("UUID", 8)) {
                entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
            }

            setPosition(posX, posY, posZ);
            setRotation(rotationYaw, rotationPitch);

            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
                setCustomNameTag(tagCompund.getString("CustomName"));
            }

            setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            field_174837_as.func_179668_a(tagCompund);
            func_174810_b(tagCompund.getBoolean("Silent"));
            readEntityFromNBT(tagCompund);

            if (shouldSetPosAfterLoading()) {
                setPosition(posX, posY, posZ);
            }
        } catch (Throwable var5) {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Loading entity NBT");
            CrashReportCategory var4 = var3.makeCategory("Entity being loaded");
            addEntityCrashInfo(var4);
            throw new ReportedException(var3);
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound tagCompund);

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound tagCompound);

    public void onChunkLoad() {
    }

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double... numbers) {
        NBTTagList var2 = new NBTTagList();
        double[] var3 = numbers;
        int var4 = numbers.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            double var6 = var3[var5];
            var2.appendTag(new NBTTagDouble(var6));
        }

        return var2;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float... numbers) {
        NBTTagList var2 = new NBTTagList();
        float[] var3 = numbers;
        int var4 = numbers.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            float var6 = var3[var5];
            var2.appendTag(new NBTTagFloat(var6));
        }

        return var2;
    }

    public EntityItem dropItem(Item itemIn, int size) {
        return dropItemWithOffset(itemIn, size, 0.0F);
    }

    public EntityItem dropItemWithOffset(Item itemIn, int size, float p_145778_3_) {
        return entityDropItem(new ItemStack(itemIn, size, 0), p_145778_3_);
    }

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            EntityItem var3 = new EntityItem(worldObj, posX, posY + offsetY, posZ, itemStackIn);
            var3.setDefaultPickupDelay();
            worldObj.spawnEntityInWorld(var3);
            return var3;
        } else {
            return null;
        }
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive() {
        return !isDead;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        if (noClip || Client.getModuleManager().get(Freecam.class).isEnabled()) {
            return false;
        } else {
            for (int var1 = 0; var1 < 8; ++var1) {
                double var2 = posX + ((var1 >> 0) % 2 - 0.5F) * width * 0.8F;
                double var4 = posY + ((var1 >> 1) % 2 - 0.5F) * 0.1F;
                double var6 = posZ + ((var1 >> 2) % 2 - 0.5F) * width * 0.8F;

                if (worldObj.getBlockState(new BlockPos(var2, var4 + getEyeHeight(), var6)).getBlock()
                        .isVisuallyOpaque()) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and
     * blocks. This enables the entity to be pushable on contact, like boats or
     * minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden() {
        if (ridingEntity.isDead) {
            ridingEntity = null;
        } else {
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
            onUpdate();

            if (ridingEntity != null) {
                ridingEntity.updateRiderPosition();
                entityRiderYawDelta += ridingEntity.rotationYaw - ridingEntity.prevRotationYaw;

                for (entityRiderPitchDelta += ridingEntity.rotationPitch
                        - ridingEntity.prevRotationPitch; entityRiderYawDelta >= 180.0D; entityRiderYawDelta -= 360.0D) {
                    ;
                }

                while (entityRiderYawDelta < -180.0D) {
                    entityRiderYawDelta += 360.0D;
                }

                while (entityRiderPitchDelta >= 180.0D) {
                    entityRiderPitchDelta -= 360.0D;
                }

                while (entityRiderPitchDelta < -180.0D) {
                    entityRiderPitchDelta += 360.0D;
                }

                double var1 = entityRiderYawDelta * 0.5D;
                double var3 = entityRiderPitchDelta * 0.5D;
                float var5 = 10.0F;

                if (var1 > var5) {
                    var1 = var5;
                }

                if (var1 < (-var5)) {
                    var1 = (-var5);
                }

                if (var3 > var5) {
                    var3 = var5;
                }

                if (var3 < (-var5)) {
                    var3 = (-var5);
                }

                entityRiderYawDelta -= var1;
                entityRiderPitchDelta -= var3;
            }
        }
    }

    public void updateRiderPosition() {
        if (riddenByEntity != null) {
            riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        return 0.0D;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    public double getMountedYOffset() {
        return height * 0.75D;
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn) {
        entityRiderPitchDelta = 0.0D;
        entityRiderYawDelta = 0.0D;

        if (entityIn == null) {
            if (ridingEntity != null) {
                setLocationAndAngles(ridingEntity.posX, ridingEntity.getEntityBoundingBox().minY + ridingEntity.height,
                        ridingEntity.posZ, rotationYaw, rotationPitch);
                ridingEntity.riddenByEntity = null;
            }

            ridingEntity = null;
        } else {
            if (ridingEntity != null) {
                ridingEntity.riddenByEntity = null;
            }

            if (entityIn != null) {
                for (Entity var2 = entityIn.ridingEntity; var2 != null; var2 = var2.ridingEntity) {
                    if (var2 == this) {
                        return;
                    }
                }
            }

            ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }

    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_,
                              float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        setRotation(p_180426_7_, p_180426_8_);
        List var11 = worldObj.getCollidingBoundingBoxes(this,
                getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));

        if (!var11.isEmpty()) {
            double var12 = 0.0D;
            Iterator var14 = var11.iterator();

            while (var14.hasNext()) {
                AxisAlignedBB var15 = (AxisAlignedBB) var14.next();

                if (var15.maxY > var12) {
                    var12 = var15.maxY;
                }
            }

            p_180426_3_ += var12 - getEntityBoundingBox().minY;
            setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        }
    }

    public float getCollisionBorderSize() {
        return 0.1F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec() {
        return null;
    }

    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal() {
        if (timeUntilPortal > 0) {
            timeUntilPortal = getPortalCooldown();
        } else {
            double var1 = prevPosX - posX;
            double var3 = prevPosZ - posZ;

            if (!worldObj.isRemote && !inPortal) {
                int var5;

                if (MathHelper.abs((float) var1) > MathHelper.abs((float) var3)) {
                    var5 = var1 > 0.0D ? EnumFacing.WEST.getHorizontalIndex() : EnumFacing.EAST.getHorizontalIndex();
                } else {
                    var5 = var3 > 0.0D ? EnumFacing.NORTH.getHorizontalIndex() : EnumFacing.SOUTH.getHorizontalIndex();
                }

                teleportDirection = var5;
            }

            inPortal = true;
        }
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown() {
        return 300;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;
    }

    public void handleHealthUpdate(byte p_70103_1_) {
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in
     * multiplayer.
     */
    public void performHurtAnimation() {
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it
     * seems)
     */
    public ItemStack[] getInventory() {
        return null;
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
     * armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire
     * effect on rendering.
     */
    public boolean isBurning() {
        boolean var1 = worldObj != null && worldObj.isRemote;
        return !isImmuneToFire && (fire > 0 || var1 && getFlag(0));
    }

    /**
     * Returns true if the entity is riding another entity, used by render to
     * rotate the legs to be in 'sit' position for players.
     */
    public boolean isRiding() {
        return ridingEntity != null;
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        return getFlag(1);
    }

    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean sneaking) {
        setFlag(1, sneaking);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting() {
        return getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        setFlag(3, sprinting);
    }

    public boolean isInvisible() {
        return getFlag(5);
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses. Determines if an
     * entity is visible or not to a specfic player, if the entity is normally
     * invisible. For EntityLivingBase subclasses, returning false when
     * invisible will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer playerIn) {
        return playerIn.func_175149_v() ? false : isInvisible();
    }

    public void setInvisible(boolean invisible) {
        setFlag(5, invisible);
    }

    public boolean isEating() {
        return getFlag(4);
    }

    public void setEating(boolean eating) {
        setFlag(4, eating);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is
     * burning; 1) is sneaking; 2) is riding something; 3) is sprinting; 4) is
     * eating
     */
    protected boolean getFlag(int flag) {
        return (dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know
     * flags.
     */
    protected void setFlag(int flag, boolean set) {
        byte var3 = dataWatcher.getWatchableObjectByte(0);

        if (set) {
            dataWatcher.updateObject(0, Byte.valueOf((byte) (var3 | 1 << flag)));
        } else {
            dataWatcher.updateObject(0, Byte.valueOf((byte) (var3 & ~(1 << flag))));
        }
    }

    public int getAir() {
        return dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int air) {
        dataWatcher.updateObject(1, Short.valueOf((short) air));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        attackEntityFrom(DamageSource.field_180137_b, 5.0F);
        ++fire;

        if (fire == 0) {
            setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn) {
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPos var7 = new BlockPos(x, y, z);
        double var8 = x - var7.getX();
        double var10 = y - var7.getY();
        double var12 = z - var7.getZ();
        List var14 = worldObj.func_147461_a(getEntityBoundingBox());

        if (var14.isEmpty() && !worldObj.func_175665_u(var7)) {
            return false;
        } else {
            byte var15 = 3;
            double var16 = 9999.0D;

            if (!worldObj.func_175665_u(var7.offsetWest()) && var8 < var16) {
                var16 = var8;
                var15 = 0;
            }

            if (!worldObj.func_175665_u(var7.offsetEast()) && 1.0D - var8 < var16) {
                var16 = 1.0D - var8;
                var15 = 1;
            }

            if (!worldObj.func_175665_u(var7.offsetUp()) && 1.0D - var10 < var16) {
                var16 = 1.0D - var10;
                var15 = 3;
            }

            if (!worldObj.func_175665_u(var7.offsetNorth()) && var12 < var16) {
                var16 = var12;
                var15 = 4;
            }

            if (!worldObj.func_175665_u(var7.offsetSouth()) && 1.0D - var12 < var16) {
                var16 = 1.0D - var12;
                var15 = 5;
            }

            float var18 = rand.nextFloat() * 0.2F + 0.1F;

            if (var15 == 0) {
                motionX = (-var18);
            }

            if (var15 == 1) {
                motionX = var18;
            }

            if (var15 == 3) {
                motionY = var18;
            }

            if (var15 == 4) {
                motionZ = (-var18);
            }

            if (var15 == 5) {
                motionZ = var18;
            }

            return true;
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
        isInWeb = true;
        fallDistance = 0.0F;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        if (hasCustomName()) {
            return getCustomNameTag();
        } else {
            String var1 = EntityList.getEntityString(this);

            if (var1 == null) {
                var1 = "generic";
            }

            return StatCollector.translateToLocal("entity." + var1 + ".name");
        }
    }

    /**
     * Return the Entity parts making up this Entity (currently only for
     * dragons)
     */
    public Entity[] getParts() {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn;
    }

    public float getRotationYawHead() {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation) {
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem() {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack
     * will not happen.
     */
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",
                new Object[]{this.getClass().getSimpleName(), getName(), Integer.valueOf(entityId),
                        worldObj == null ? "~NULL~" : worldObj.getWorldInfo().getWorldName(), Double.valueOf(posX),
                        Double.valueOf(posY), Double.valueOf(posZ)});
    }

    public boolean func_180431_b(DamageSource p_180431_1_) {
        return invulnerable && p_180431_1_ != DamageSource.outOfWorld && !p_180431_1_.func_180136_u();
    }

    /**
     * Sets this entity's location and angles to the location and angles of the
     * passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn) {
        setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    public void func_180432_n(Entity p_180432_1_) {
        NBTTagCompound var2 = new NBTTagCompound();
        p_180432_1_.writeToNBT(var2);
        readFromNBT(var2);
        timeUntilPortal = p_180432_1_.timeUntilPortal;
        teleportDirection = p_180432_1_.teleportDirection;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to
     * teleport to
     */
    public void travelToDimension(int dimensionId) {
        if (!worldObj.isRemote && !isDead) {
            worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer var2 = MinecraftServer.getServer();
            int var3 = dimension;
            WorldServer var4 = var2.worldServerForDimension(var3);
            WorldServer var5 = var2.worldServerForDimension(dimensionId);
            dimension = dimensionId;

            if (var3 == 1 && dimensionId == 1) {
                var5 = var2.worldServerForDimension(0);
                dimension = 0;
            }

            worldObj.removeEntity(this);
            isDead = false;
            worldObj.theProfiler.startSection("reposition");
            var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
            worldObj.theProfiler.endStartSection("reloading");
            Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);

            if (var6 != null) {
                var6.func_180432_n(this);

                if (var3 == 1 && dimensionId == 1) {
                    BlockPos var7 = worldObj.func_175672_r(var5.getSpawnPoint());
                    var6.func_174828_a(var7, var6.rotationYaw, var6.rotationPitch);
                }

                var5.spawnEntityInWorld(var6);
            }

            isDead = true;
            worldObj.theProfiler.endSection();
            var4.resetUpdateEntityTick();
            var5.resetUpdateEntityTick();
            worldObj.theProfiler.endSection();
        }
    }

    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion p_180428_1_, World worldIn, BlockPos p_180428_3_,
                                        IBlockState p_180428_4_) {
        return p_180428_4_.getBlock().getExplosionResistance(this);
    }

    public boolean func_174816_a(Explosion p_174816_1_, World worldIn, BlockPos p_174816_3_, IBlockState p_174816_4_,
                                 float p_174816_5_) {
        return true;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in
     * pathfinder)
     */
    public int getMaxFallHeight() {
        return 3;
    }

    public int getTeleportDirection() {
        return teleportDirection;
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a
     * tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category) {
        category.addCrashSectionCallable("Entity Type", new Callable() {
            private static final String __OBFID = "CL_00001534";

            @Override
            public String call() {
                return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", Integer.valueOf(entityId));
        category.addCrashSectionCallable("Entity Name", new Callable() {
            private static final String __OBFID = "CL_00001535";

            @Override
            public String call() {
                return Entity.this.getName();
            }
        });
        category.addCrashSection("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f",
                new Object[]{Double.valueOf(posX), Double.valueOf(posY), Double.valueOf(posZ)}));
        category.addCrashSection("Entity\'s Block location", CrashReportCategory.getCoordinateInfo(
                MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)));
        category.addCrashSection("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f",
                new Object[]{Double.valueOf(motionX), Double.valueOf(motionY), Double.valueOf(motionZ)}));
        category.addCrashSectionCallable("Entity\'s Rider", new Callable() {
            private static final String __OBFID = "CL_00002259";

            public String func_180118_a() {
                return riddenByEntity.toString();
            }

            @Override
            public Object call() {
                return func_180118_a();
            }
        });
        category.addCrashSectionCallable("Entity\'s Vehicle", new Callable() {
            private static final String __OBFID = "CL_00002258";

            public String func_180116_a() {
                return ridingEntity.toString();
            }

            @Override
            public Object call() {
                return func_180116_a();
            }
        });
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire() {
        return isBurning();
    }

    public UUID getUniqueID() {
        return entityUniqueID;
    }

    public boolean isPushedByWater() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentText var1 = new ChatComponentText(getName());
        var1.getChatStyle().setChatHoverEvent(func_174823_aP());
        var1.getChatStyle().setInsertion(getUniqueID().toString());
        return var1;
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String p_96094_1_) {
        dataWatcher.updateObject(2, p_96094_1_);
    }

    public String getCustomNameTag() {
        return dataWatcher.getWatchableObjectString(2);
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return dataWatcher.getWatchableObjectString(2).length() > 0;
    }

    public void setAlwaysRenderNameTag(boolean p_174805_1_) {
        dataWatcher.updateObject(3, Byte.valueOf((byte) (p_174805_1_ ? 1 : 0)));
    }

    public boolean getAlwaysRenderNameTag() {
        return dataWatcher.getWatchableObjectByte(3) == 1;
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
        setLocationAndAngles(p_70634_1_, p_70634_3_, p_70634_5_, rotationYaw, rotationPitch);
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return getAlwaysRenderNameTag();
    }

    public void func_145781_i(int p_145781_1_) {
    }

    public EnumFacing func_174811_aO() {
        return EnumFacing.getHorizontal(MathHelper.floor_double(rotationYaw * 4.0F / 360.0F + 0.5D) & 3);
    }

    protected HoverEvent func_174823_aP() {
        NBTTagCompound var1 = new NBTTagCompound();
        String var2 = EntityList.getEntityString(this);
        var1.setString("id", getUniqueID().toString());

        if (var2 != null) {
            var1.setString("type", var2);
        }

        var1.setString("name", getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(var1.toString()));
    }

    public boolean func_174827_a(EntityPlayerMP p_174827_1_) {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return boundingBox;
    }

    public void func_174826_a(AxisAlignedBB p_174826_1_) {
        boundingBox = p_174826_1_;
    }

    public float getEyeHeight() {
        return height * 0.85F;
    }

    public boolean isOutsideBorder() {
        return isOutsideBorder;
    }

    public void setOutsideBorder(boolean p_174821_1_) {
        isOutsideBorder = p_174821_1_;
    }

    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_) {
        return false;
    }

    /**
     * Notifies this sender of some sort of information. This is for messages
     * intended to display to the user. Used for typical output (like "you asked
     * for whether or not this game rule is set, so here's your answer" ),
     * warnings (like "I fetched this block for you by ID, but I'd like you to
     * know that every time you do this, I die a little inside "), and errors
     * (like "it's not called iron_pixacke, silly").
     */
    @Override
    public void addChatMessage(IChatComponent message) {
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return true;
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(posX, posY + 0.5D, posZ);
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(posX, posY, posZ);
    }

    @Override
    public World getEntityWorld() {
        return worldObj;
    }

    @Override
    public Entity getCommandSenderEntity() {
        return this;
    }

    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    @Override
    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {
        field_174837_as.func_179672_a(this, p_174794_1_, p_174794_2_);
    }

    public CommandResultStats func_174807_aT() {
        return field_174837_as;
    }

    public void func_174817_o(Entity p_174817_1_) {
        field_174837_as.func_179671_a(p_174817_1_.func_174807_aT());
    }

    public NBTTagCompound func_174819_aU() {
        return null;
    }

    public void func_174834_g(NBTTagCompound p_174834_1_) {
    }

    public boolean func_174825_a(EntityPlayer p_174825_1_, Vec3 p_174825_2_) {
        return false;
    }

    public boolean func_180427_aV() {
        return false;
    }

    protected void func_174815_a(EntityLivingBase p_174815_1_, Entity p_174815_2_) {
        if (p_174815_2_ instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase) p_174815_2_, p_174815_1_);
        }

        EnchantmentHelper.func_151385_b(p_174815_1_, p_174815_2_);
    }
}
