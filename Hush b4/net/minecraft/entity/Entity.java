// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.concurrent.Callable;
import net.minecraft.world.Explosion;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.command.CommandResultStats;
import java.util.UUID;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.command.ICommandSender;

public abstract class Entity implements ICommandSender
{
    private static final AxisAlignedBB ZERO_AABB;
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;
    public boolean preventEntitySpawning;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public boolean forceSpawn;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    public int fireResistance;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;
    protected BlockPos field_181016_an;
    protected Vec3 field_181017_ao;
    protected EnumFacing field_181018_ap;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    private final CommandResultStats cmdResultStats;
    
    static {
        ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final int id) {
        this.entityId = id;
    }
    
    public void onKillCommand() {
        this.setDead();
    }
    
    public Entity(final World worldIn) {
        this.entityId = Entity.nextEntityID++;
        this.renderDistanceWeight = 1.0;
        this.boundingBox = Entity.ZERO_AABB;
        this.width = 0.6f;
        this.height = 1.8f;
        this.nextStepDistance = 1;
        this.rand = new Random();
        this.fireResistance = 1;
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
        this.cmdResultStats = new CommandResultStats();
        this.worldObj = worldIn;
        this.setPosition(0.0, 0.0, 0.0);
        if (worldIn != null) {
            this.dimension = worldIn.provider.getDimensionId();
        }
        (this.dataWatcher = new DataWatcher(this)).addObject(0, (Byte)0);
        this.dataWatcher.addObject(1, (Short)300);
        this.dataWatcher.addObject(3, (Byte)0);
        this.dataWatcher.addObject(2, "");
        this.dataWatcher.addObject(4, (Byte)0);
        this.entityInit();
    }
    
    protected abstract void entityInit();
    
    public DataWatcher getDataWatcher() {
        return this.dataWatcher;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof Entity && ((Entity)p_equals_1_).entityId == this.entityId;
    }
    
    @Override
    public int hashCode() {
        return this.entityId;
    }
    
    protected void preparePlayerToSpawn() {
        if (this.worldObj != null) {
            while (this.posY > 0.0 && this.posY < 256.0) {
                this.setPosition(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
                    break;
                }
                ++this.posY;
            }
            final double motionX = 0.0;
            this.motionZ = motionX;
            this.motionY = motionX;
            this.motionX = motionX;
            this.rotationPitch = 0.0f;
        }
    }
    
    public void setDead() {
        this.isDead = true;
    }
    
    protected void setSize(final float width, final float height) {
        if (width != this.width || height != this.height) {
            final float f = this.width;
            this.width = width;
            this.height = height;
            this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + this.width, this.getEntityBoundingBox().minY + this.height, this.getEntityBoundingBox().minZ + this.width));
            if (this.width > f && !this.firstUpdate && !this.worldObj.isRemote) {
                this.moveEntity(f - this.width, 0.0, f - this.width);
            }
        }
    }
    
    protected void setRotation(final float yaw, final float pitch) {
        this.rotationYaw = yaw % 360.0f;
        this.rotationPitch = pitch % 360.0f;
    }
    
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f2, z + f));
    }
    
    public void setAngles(final float yaw, final float pitch) {
        final float f = this.rotationPitch;
        final float f2 = this.rotationYaw;
        this.rotationYaw += (float)(yaw * 0.15);
        this.rotationPitch -= (float)(pitch * 0.15);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0f, 90.0f);
        this.prevRotationPitch += this.rotationPitch - f;
        this.prevRotationYaw += this.rotationYaw - f2;
    }
    
    public void onUpdate() {
        this.onEntityUpdate();
    }
    
    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            final MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
            final int i = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= i) {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;
                        if (this.worldObj.provider.getDimensionId() == -1) {
                            j = 0;
                        }
                        else {
                            j = -1;
                        }
                        this.travelToDimension(j);
                    }
                    this.inPortal = false;
                }
            }
            else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }
            this.worldObj.theProfiler.endSection();
        }
        this.spawnRunningParticles();
        this.handleWaterMovement();
        if (this.worldObj.isRemote) {
            this.fire = 0;
        }
        else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            }
            else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.onFire, 1.0f);
                }
                --this.fire;
            }
        }
        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        if (this.posY < -64.0) {
            this.kill();
        }
        if (!this.worldObj.isRemote) {
            this.setFlag(0, this.fire > 0);
        }
        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }
    
    public int getMaxInPortalTime() {
        return 0;
    }
    
    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.lava, 4.0f);
            this.setFire(15);
        }
    }
    
    public void setFire(final int seconds) {
        int i = seconds * 20;
        i = EnchantmentProtection.getFireTimeForEntity(this, i);
        if (this.fire < i) {
            this.fire = i;
        }
    }
    
    public void extinguish() {
        this.fire = 0;
    }
    
    protected void kill() {
        this.setDead();
    }
    
    public boolean isOffsetPositionInLiquid(final double x, final double y, final double z) {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(x, y, z);
        return this.isLiquidPresentInAABB(axisalignedbb);
    }
    
    private boolean isLiquidPresentInAABB(final AxisAlignedBB bb) {
        return this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb);
    }
    
    public void moveEntity(double x, double y, double z) {
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else {
            this.worldObj.theProfiler.startSection("move");
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            double d4 = x;
            final double d5 = y;
            double d6 = z;
            final boolean flag = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
            if (flag) {
                final double d7 = 0.05;
                while (x != 0.0) {
                    if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                        break;
                    }
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= d7;
                    }
                    else {
                        x += d7;
                    }
                    d4 = x;
                }
                while (z != 0.0) {
                    if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                        break;
                    }
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= d7;
                    }
                    else {
                        z += d7;
                    }
                    d6 = z;
                }
                while (x != 0.0 && z != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    }
                    else if (x > 0.0) {
                        x -= d7;
                    }
                    else {
                        x += d7;
                    }
                    d4 = x;
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    }
                    else if (z > 0.0) {
                        z -= d7;
                    }
                    else {
                        z += d7;
                    }
                    d6 = z;
                }
            }
            final List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            for (final AxisAlignedBB axisalignedbb2 : list1) {
                y = axisalignedbb2.calculateYOffset(this.getEntityBoundingBox(), y);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
            final boolean flag2 = this.onGround || (d5 != y && d5 < 0.0);
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                x = axisalignedbb3.calculateXOffset(this.getEntityBoundingBox(), x);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb4 : list1) {
                z = axisalignedbb4.calculateZOffset(this.getEntityBoundingBox(), z);
            }
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
            if (this.stepHeight > 0.0f && flag2 && (d4 != x || d6 != z)) {
                final double d8 = x;
                final double d9 = y;
                final double d10 = z;
                final AxisAlignedBB axisalignedbb5 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = this.stepHeight;
                final List<AxisAlignedBB> list2 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d4, y, d6));
                AxisAlignedBB axisalignedbb6 = this.getEntityBoundingBox();
                final AxisAlignedBB axisalignedbb7 = axisalignedbb6.addCoord(d4, 0.0, d6);
                double d11 = y;
                for (final AxisAlignedBB axisalignedbb8 : list2) {
                    d11 = axisalignedbb8.calculateYOffset(axisalignedbb7, d11);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, d11, 0.0);
                double d12 = d4;
                for (final AxisAlignedBB axisalignedbb9 : list2) {
                    d12 = axisalignedbb9.calculateXOffset(axisalignedbb6, d12);
                }
                axisalignedbb6 = axisalignedbb6.offset(d12, 0.0, 0.0);
                double d13 = d6;
                for (final AxisAlignedBB axisalignedbb10 : list2) {
                    d13 = axisalignedbb10.calculateZOffset(axisalignedbb6, d13);
                }
                axisalignedbb6 = axisalignedbb6.offset(0.0, 0.0, d13);
                AxisAlignedBB axisalignedbb11 = this.getEntityBoundingBox();
                double d14 = y;
                for (final AxisAlignedBB axisalignedbb12 : list2) {
                    d14 = axisalignedbb12.calculateYOffset(axisalignedbb11, d14);
                }
                axisalignedbb11 = axisalignedbb11.offset(0.0, d14, 0.0);
                double d15 = d4;
                for (final AxisAlignedBB axisalignedbb13 : list2) {
                    d15 = axisalignedbb13.calculateXOffset(axisalignedbb11, d15);
                }
                axisalignedbb11 = axisalignedbb11.offset(d15, 0.0, 0.0);
                double d16 = d6;
                for (final AxisAlignedBB axisalignedbb14 : list2) {
                    d16 = axisalignedbb14.calculateZOffset(axisalignedbb11, d16);
                }
                axisalignedbb11 = axisalignedbb11.offset(0.0, 0.0, d16);
                final double d17 = d12 * d12 + d13 * d13;
                final double d18 = d15 * d15 + d16 * d16;
                if (d17 > d18) {
                    x = d12;
                    z = d13;
                    y = -d11;
                    this.setEntityBoundingBox(axisalignedbb6);
                }
                else {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.setEntityBoundingBox(axisalignedbb11);
                }
                for (final AxisAlignedBB axisalignedbb15 : list2) {
                    y = axisalignedbb15.calculateYOffset(this.getEntityBoundingBox(), y);
                }
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
                if (d8 * d8 + d10 * d10 >= x * x + z * z) {
                    x = d8;
                    y = d9;
                    z = d10;
                    this.setEntityBoundingBox(axisalignedbb5);
                }
            }
            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.resetPositionToBB();
            this.isCollidedHorizontally = (d4 != x || d6 != z);
            this.isCollidedVertically = (d5 != y);
            this.onGround = (this.isCollidedVertically && d5 < 0.0);
            this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
            final int i = MathHelper.floor_double(this.posX);
            final int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int k = MathHelper.floor_double(this.posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();
            if (block1.getMaterial() == Material.air) {
                final Block block2 = this.worldObj.getBlockState(blockpos.down()).getBlock();
                if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                    block1 = block2;
                    blockpos = blockpos.down();
                }
            }
            this.updateFallState(y, this.onGround, block1, blockpos);
            if (d4 != x) {
                this.motionX = 0.0;
            }
            if (d6 != z) {
                this.motionZ = 0.0;
            }
            if (d5 != y) {
                block1.onLanded(this.worldObj, this);
            }
            if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
                final double d19 = this.posX - d0;
                double d20 = this.posY - d2;
                final double d21 = this.posZ - d3;
                if (block1 != Blocks.ladder) {
                    d20 = 0.0;
                }
                if (block1 != null && this.onGround) {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
                }
                this.distanceWalkedModified += (float)(MathHelper.sqrt_double(d19 * d19 + d21 * d21) * 0.6);
                this.distanceWalkedOnStepModified += (float)(MathHelper.sqrt_double(d19 * d19 + d20 * d20 + d21 * d21) * 0.6);
                if (this.distanceWalkedOnStepModified > this.nextStepDistance && block1.getMaterial() != Material.air) {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.playSound(this.getSwimSound(), f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                    }
                    this.playStepSound(blockpos, block1);
                }
            }
            try {
                this.doBlockCollisions();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.isWet();
            if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!flag3) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }
            if (flag3 && this.fire > 0) {
                this.playSound("random.fizz", 0.7f, 1.6f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                this.fire = -this.fireResistance;
            }
            this.worldObj.theProfiler.endSection();
        }
    }
    
    private void resetPositionToBB() {
        this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        this.posY = this.getEntityBoundingBox().minY;
        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
    }
    
    protected String getSwimSound() {
        return "game.neutral.swim";
    }
    
    protected void doBlockCollisions() {
        final BlockPos blockpos = new BlockPos(this.getEntityBoundingBox().minX + 0.001, this.getEntityBoundingBox().minY + 0.001, this.getEntityBoundingBox().minZ + 0.001);
        final BlockPos blockpos2 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001, this.getEntityBoundingBox().maxY - 0.001, this.getEntityBoundingBox().maxZ - 0.001);
        if (this.worldObj.isAreaLoaded(blockpos, blockpos2)) {
            for (int i = blockpos.getX(); i <= blockpos2.getX(); ++i) {
                for (int j = blockpos.getY(); j <= blockpos2.getY(); ++j) {
                    for (int k = blockpos.getZ(); k <= blockpos2.getZ(); ++k) {
                        final BlockPos blockpos3 = new BlockPos(i, j, k);
                        final IBlockState iblockstate = this.worldObj.getBlockState(blockpos3);
                        try {
                            iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos3, iblockstate, this);
                        }
                        catch (Throwable throwable) {
                            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos3, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
    }
    
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        Block.SoundType block$soundtype = blockIn.stepSound;
        if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer) {
            block$soundtype = Blocks.snow_layer.stepSound;
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
        }
        else if (!blockIn.getMaterial().isLiquid()) {
            this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.15f, block$soundtype.getFrequency());
        }
    }
    
    public void playSound(final String name, final float volume, final float pitch) {
        if (!this.isSilent()) {
            this.worldObj.playSoundAtEntity(this, name, volume, pitch);
        }
    }
    
    public boolean isSilent() {
        return this.dataWatcher.getWatchableObjectByte(4) == 1;
    }
    
    public void setSilent(final boolean isSilent) {
        this.dataWatcher.updateObject(4, (byte)(isSilent ? 1 : 0));
    }
    
    protected boolean canTriggerWalking() {
        return true;
    }
    
    protected void updateFallState(final double y, final boolean onGroundIn, final Block blockIn, final BlockPos pos) {
        if (onGroundIn) {
            if (this.fallDistance > 0.0f) {
                if (blockIn != null) {
                    blockIn.onFallenUpon(this.worldObj, pos, this, this.fallDistance);
                }
                else {
                    this.fall(this.fallDistance, 1.0f);
                }
                this.fallDistance = 0.0f;
            }
        }
        else if (y < 0.0) {
            this.fallDistance -= (float)y;
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }
    
    protected void dealFireDamage(final int amount) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.inFire, (float)amount);
        }
    }
    
    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }
    
    public void fall(final float distance, final float damageMultiplier) {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.fall(distance, damageMultiplier);
        }
    }
    
    public boolean isWet() {
        return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + this.height, this.posZ));
    }
    
    public boolean isInWater() {
        return this.inWater;
    }
    
    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.4000000059604645, 0.0).contract(0.001, 0.001, 0.001), Material.water, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.resetHeight();
            }
            this.fallDistance = 0.0f;
            this.inWater = true;
            this.fire = 0;
        }
        else {
            this.inWater = false;
        }
        return this.inWater;
    }
    
    protected void resetHeight() {
        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224 + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224) * 0.2f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.playSound(this.getSplashSound(), f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
        final float f2 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
        for (int i = 0; i < 1.0f + this.width * 20.0f; ++i) {
            final float f3 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float f4 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f3, f2 + 1.0f, this.posZ + f4, this.motionX, this.motionY - this.rand.nextFloat() * 0.2f, this.motionZ, new int[0]);
        }
        for (int j = 0; j < 1.0f + this.width * 20.0f; ++j) {
            final float f5 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            final float f6 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f5, f2 + 1.0f, this.posZ + f6, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
    }
    
    public void spawnRunningParticles() {
        if (this.isSprinting() && !this.isInWater()) {
            this.createRunningParticles();
        }
    }
    
    protected void createRunningParticles() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
        final int k = MathHelper.floor_double(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (block.getRenderType() != -1) {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5) * this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + (this.rand.nextFloat() - 0.5) * this.width, -this.motionX * 4.0, 1.5, -this.motionZ * 4.0, Block.getStateId(iblockstate));
        }
    }
    
    protected String getSplashSound() {
        return "game.neutral.swim.splash";
    }
    
    public boolean isInsideOfMaterial(final Material materialIn) {
        final double d0 = this.posY + this.getEyeHeight();
        final BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
        final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (block.getMaterial() == materialIn) {
            final float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111f;
            final float f2 = blockpos.getY() + 1 - f;
            final boolean flag = d0 < f2;
            return (flag || !(this instanceof EntityPlayer)) && flag;
        }
        return false;
    }
    
    public boolean isInLava() {
        return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612, -0.4000000059604645, -0.10000000149011612), Material.lava);
    }
    
    public void moveFlying(float strafe, float forward, final float friction) {
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final float f2 = MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f);
            final float f3 = MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f);
            this.motionX += strafe * f3 - forward * f2;
            this.motionZ += forward * f3 + strafe * f2;
        }
    }
    
    public int getBrightnessForRender(final float partialTicks) {
        final BlockPos blockpos = new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
    }
    
    public float getBrightness(final float partialTicks) {
        final BlockPos blockpos = new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0f;
    }
    
    public void setWorld(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public void setPositionAndRotation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.posX = x;
        this.prevPosX = x;
        this.posY = y;
        this.prevPosY = y;
        this.posZ = z;
        this.prevPosZ = z;
        this.rotationYaw = yaw;
        this.prevRotationYaw = yaw;
        this.rotationPitch = pitch;
        this.prevRotationPitch = pitch;
        final double d0 = this.prevRotationYaw - yaw;
        if (d0 < -180.0) {
            this.prevRotationYaw += 360.0f;
        }
        if (d0 >= 180.0) {
            this.prevRotationYaw -= 360.0f;
        }
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }
    
    public void moveToBlockPosAndAngles(final BlockPos pos, final float rotationYawIn, final float rotationPitchIn) {
        this.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, rotationYawIn, rotationPitchIn);
    }
    
    public void setLocationAndAngles(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.posX = x;
        this.prevPosX = x;
        this.lastTickPosX = x;
        this.posY = y;
        this.prevPosY = y;
        this.lastTickPosY = y;
        this.posZ = z;
        this.prevPosZ = z;
        this.lastTickPosZ = z;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public float getDistanceToEntity(final Entity entityIn) {
        final float f = (float)(this.posX - entityIn.posX);
        final float f2 = (float)(this.posY - entityIn.posY);
        final float f3 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double getDistanceSq(final BlockPos pos) {
        return pos.distanceSq(this.posX, this.posY, this.posZ);
    }
    
    public double getDistanceSqToCenter(final BlockPos pos) {
        return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }
    
    public double getDistance(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public double getDistanceSqToEntity(final Entity entityIn) {
        final double d0 = this.posX - entityIn.posX;
        final double d2 = this.posY - entityIn.posY;
        final double d3 = this.posZ - entityIn.posZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
    }
    
    public void applyEntityCollision(final Entity entityIn) {
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this && !entityIn.noClip && !this.noClip) {
            double d0 = entityIn.posX - this.posX;
            double d2 = entityIn.posZ - this.posZ;
            double d3 = MathHelper.abs_max(d0, d2);
            if (d3 >= 0.009999999776482582) {
                d3 = MathHelper.sqrt_double(d3);
                d0 /= d3;
                d2 /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d0 *= d4;
                d2 *= d4;
                d0 *= 0.05000000074505806;
                d2 *= 0.05000000074505806;
                d0 *= 1.0f - this.entityCollisionReduction;
                d2 *= 1.0f - this.entityCollisionReduction;
                if (this.riddenByEntity == null) {
                    this.addVelocity(-d0, 0.0, -d2);
                }
                if (entityIn.riddenByEntity == null) {
                    entityIn.addVelocity(d0, 0.0, d2);
                }
            }
        }
    }
    
    public void addVelocity(final double x, final double y, final double z) {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
        this.isAirBorne = true;
    }
    
    protected void setBeenAttacked() {
        this.velocityChanged = true;
    }
    
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setBeenAttacked();
        return false;
    }
    
    public Vec3 getLook(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        final float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        final float f2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
        return this.getVectorForRotation(f, f2);
    }
    
    protected final Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }
    
    public Vec3 getPositionEyes(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        }
        final double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
        final double d2 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + this.getEyeHeight();
        final double d3 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
        return new Vec3(d0, d2, d3);
    }
    
    public MovingObjectPosition rayTrace(final double blockReachDistance, final float partialTicks) {
        final Vec3 vec3 = this.getPositionEyes(partialTicks);
        final Vec3 vec4 = this.getLook(partialTicks);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    public boolean canBeCollidedWith() {
        return false;
    }
    
    public boolean canBePushed() {
        return false;
    }
    
    public void addToPlayerScore(final Entity entityIn, final int amount) {
    }
    
    public boolean isInRangeToRender3d(final double x, final double y, final double z) {
        final double d0 = this.posX - x;
        final double d2 = this.posY - y;
        final double d3 = this.posZ - z;
        final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        return this.isInRangeToRenderDist(d4);
    }
    
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 64.0 * this.renderDistanceWeight;
        return distance < d0 * d0;
    }
    
    public boolean writeMountToNBT(final NBTTagCompound tagCompund) {
        final String s = this.getEntityString();
        if (!this.isDead && s != null) {
            tagCompund.setString("id", s);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }
    
    public boolean writeToNBTOptional(final NBTTagCompound tagCompund) {
        final String s = this.getEntityString();
        if (!this.isDead && s != null && this.riddenByEntity == null) {
            tagCompund.setString("id", s);
            this.writeToNBT(tagCompund);
            return true;
        }
        return false;
    }
    
    public void writeToNBT(final NBTTagCompound tagCompund) {
        try {
            tagCompund.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            tagCompund.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            tagCompund.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            tagCompund.setFloat("FallDistance", this.fallDistance);
            tagCompund.setShort("Fire", (short)this.fire);
            tagCompund.setShort("Air", (short)this.getAir());
            tagCompund.setBoolean("OnGround", this.onGround);
            tagCompund.setInteger("Dimension", this.dimension);
            tagCompund.setBoolean("Invulnerable", this.invulnerable);
            tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
            tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0) {
                tagCompund.setString("CustomName", this.getCustomNameTag());
                tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }
            this.cmdResultStats.writeStatsToNBT(tagCompund);
            if (this.isSilent()) {
                tagCompund.setBoolean("Silent", this.isSilent());
            }
            this.writeEntityToNBT(tagCompund);
            if (this.ridingEntity != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                if (this.ridingEntity.writeMountToNBT(nbttagcompound)) {
                    tagCompund.setTag("Riding", nbttagcompound);
                }
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    public void readFromNBT(final NBTTagCompound tagCompund) {
        try {
            final NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
            final NBTTagList nbttaglist2 = tagCompund.getTagList("Motion", 6);
            final NBTTagList nbttaglist3 = tagCompund.getTagList("Rotation", 5);
            this.motionX = nbttaglist2.getDoubleAt(0);
            this.motionY = nbttaglist2.getDoubleAt(1);
            this.motionZ = nbttaglist2.getDoubleAt(2);
            if (Math.abs(this.motionX) > 10.0) {
                this.motionX = 0.0;
            }
            if (Math.abs(this.motionY) > 10.0) {
                this.motionY = 0.0;
            }
            if (Math.abs(this.motionZ) > 10.0) {
                this.motionZ = 0.0;
            }
            final double double1 = nbttaglist.getDoubleAt(0);
            this.posX = double1;
            this.lastTickPosX = double1;
            this.prevPosX = double1;
            final double double2 = nbttaglist.getDoubleAt(1);
            this.posY = double2;
            this.lastTickPosY = double2;
            this.prevPosY = double2;
            final double double3 = nbttaglist.getDoubleAt(2);
            this.posZ = double3;
            this.lastTickPosZ = double3;
            this.prevPosZ = double3;
            final float float1 = nbttaglist3.getFloatAt(0);
            this.rotationYaw = float1;
            this.prevRotationYaw = float1;
            final float float2 = nbttaglist3.getFloatAt(1);
            this.rotationPitch = float2;
            this.prevRotationPitch = float2;
            this.setRotationYawHead(this.rotationYaw);
            this.func_181013_g(this.rotationYaw);
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            this.setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");
            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4)) {
                this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            }
            else if (tagCompund.hasKey("UUID", 8)) {
                this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
                this.setCustomNameTag(tagCompund.getString("CustomName"));
            }
            this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(tagCompund);
            this.setSilent(tagCompund.getBoolean("Silent"));
            this.readEntityFromNBT(tagCompund);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    protected boolean shouldSetPosAfterLoading() {
        return true;
    }
    
    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }
    
    protected abstract void readEntityFromNBT(final NBTTagCompound p0);
    
    protected abstract void writeEntityToNBT(final NBTTagCompound p0);
    
    public void onChunkLoad() {
    }
    
    protected NBTTagList newDoubleNBTList(final double... numbers) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }
    
    protected NBTTagList newFloatNBTList(final float... numbers) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final float f : numbers) {
            nbttaglist.appendTag(new NBTTagFloat(f));
        }
        return nbttaglist;
    }
    
    public EntityItem dropItem(final Item itemIn, final int size) {
        return this.dropItemWithOffset(itemIn, size, 0.0f);
    }
    
    public EntityItem dropItemWithOffset(final Item itemIn, final int size, final float offsetY) {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }
    
    public EntityItem entityDropItem(final ItemStack itemStackIn, final float offsetY) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + offsetY, this.posZ, itemStackIn);
            entityitem.setDefaultPickupDelay();
            this.worldObj.spawnEntityInWorld(entityitem);
            return entityitem;
        }
        return null;
    }
    
    public boolean isEntityAlive() {
        return !this.isDead;
    }
    
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (int i = 0; i < 8; ++i) {
            final int j = MathHelper.floor_double(this.posY + ((i >> 0) % 2 - 0.5f) * 0.1f + this.getEyeHeight());
            final int k = MathHelper.floor_double(this.posX + ((i >> 1) % 2 - 0.5f) * this.width * 0.8f);
            final int l = MathHelper.floor_double(this.posZ + ((i >> 2) % 2 - 0.5f) * this.width * 0.8f);
            if (blockpos$mutableblockpos.getX() != k || blockpos$mutableblockpos.getY() != j || blockpos$mutableblockpos.getZ() != l) {
                blockpos$mutableblockpos.func_181079_c(k, j, l);
                if (this.worldObj.getBlockState(blockpos$mutableblockpos).getBlock().isVisuallyOpaque()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean interactFirst(final EntityPlayer playerIn) {
        return false;
    }
    
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return null;
    }
    
    public void updateRidden() {
        if (this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }
        else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.onUpdate();
            if (this.ridingEntity != null) {
                this.ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw;
                this.entityRiderPitchDelta += this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch;
                while (this.entityRiderYawDelta >= 180.0) {
                    this.entityRiderYawDelta -= 360.0;
                }
                while (this.entityRiderYawDelta < -180.0) {
                    this.entityRiderYawDelta += 360.0;
                }
                while (this.entityRiderPitchDelta >= 180.0) {
                    this.entityRiderPitchDelta -= 360.0;
                }
                while (this.entityRiderPitchDelta < -180.0) {
                    this.entityRiderPitchDelta += 360.0;
                }
                double d0 = this.entityRiderYawDelta * 0.5;
                double d2 = this.entityRiderPitchDelta * 0.5;
                final float f = 10.0f;
                if (d0 > f) {
                    d0 = f;
                }
                if (d0 < -f) {
                    d0 = -f;
                }
                if (d2 > f) {
                    d2 = f;
                }
                if (d2 < -f) {
                    d2 = -f;
                }
                this.entityRiderYawDelta -= d0;
                this.entityRiderPitchDelta -= d2;
            }
        }
    }
    
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }
    
    public double getYOffset() {
        return 0.0;
    }
    
    public double getMountedYOffset() {
        return this.height * 0.75;
    }
    
    public void mountEntity(final Entity entityIn) {
        this.entityRiderPitchDelta = 0.0;
        this.entityRiderYawDelta = 0.0;
        if (entityIn == null) {
            if (this.ridingEntity != null) {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        }
        else {
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            if (entityIn != null) {
                for (Entity entity = entityIn.ridingEntity; entity != null; entity = entity.ridingEntity) {
                    if (entity == this) {
                        return;
                    }
                }
            }
            this.ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }
    
    public void setPositionAndRotation2(final double x, double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
        final List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125, 0.0, 0.03125));
        if (!list.isEmpty()) {
            double d0 = 0.0;
            for (final AxisAlignedBB axisalignedbb : list) {
                if (axisalignedbb.maxY > d0) {
                    d0 = axisalignedbb.maxY;
                }
            }
            y += d0 - this.getEntityBoundingBox().minY;
            this.setPosition(x, y, z);
        }
    }
    
    public float getCollisionBorderSize() {
        return 0.1f;
    }
    
    public Vec3 getLookVec() {
        return null;
    }
    
    public void func_181015_d(final BlockPos p_181015_1_) {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else {
            if (!this.worldObj.isRemote && !p_181015_1_.equals(this.field_181016_an)) {
                this.field_181016_an = p_181015_1_;
                final BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldObj, p_181015_1_);
                final double d0 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.func_181117_a().getZ() : ((double)blockpattern$patternhelper.func_181117_a().getX());
                double d2 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? this.posZ : this.posX;
                d2 = Math.abs(MathHelper.func_181160_c(d2 - (double)((blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) ? 1 : 0), d0, d0 - blockpattern$patternhelper.func_181118_d()));
                final double d3 = MathHelper.func_181160_c(this.posY - 1.0, blockpattern$patternhelper.func_181117_a().getY(), blockpattern$patternhelper.func_181117_a().getY() - blockpattern$patternhelper.func_181119_e());
                this.field_181017_ao = new Vec3(d2, d3, 0.0);
                this.field_181018_ap = blockpattern$patternhelper.getFinger();
            }
            this.inPortal = true;
        }
    }
    
    public int getPortalCooldown() {
        return 300;
    }
    
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }
    
    public void handleStatusUpdate(final byte id) {
    }
    
    public void performHurtAnimation() {
    }
    
    public ItemStack[] getInventory() {
        return null;
    }
    
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
    }
    
    public boolean isBurning() {
        final boolean flag = this.worldObj != null && this.worldObj.isRemote;
        return !this.isImmuneToFire && (this.fire > 0 || (flag && this.getFlag(0)));
    }
    
    public boolean isRiding() {
        return this.ridingEntity != null;
    }
    
    public boolean isSneaking() {
        return this.getFlag(1);
    }
    
    public void setSneaking(final boolean sneaking) {
        this.setFlag(1, sneaking);
    }
    
    public boolean isSprinting() {
        return this.getFlag(3);
    }
    
    public void setSprinting(final boolean sprinting) {
        this.setFlag(3, sprinting);
    }
    
    public boolean isInvisible() {
        return this.getFlag(5);
    }
    
    public boolean isInvisibleToPlayer(final EntityPlayer player) {
        return !player.isSpectator() && this.isInvisible();
    }
    
    public void setInvisible(final boolean invisible) {
        this.setFlag(5, invisible);
    }
    
    public boolean isEating() {
        return this.getFlag(4);
    }
    
    public void setEating(final boolean eating) {
        this.setFlag(4, eating);
    }
    
    protected boolean getFlag(final int flag) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0x0;
    }
    
    protected void setFlag(final int flag, final boolean set) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(0);
        if (set) {
            this.dataWatcher.updateObject(0, (byte)(b0 | 1 << flag));
        }
        else {
            this.dataWatcher.updateObject(0, (byte)(b0 & ~(1 << flag)));
        }
    }
    
    public int getAir() {
        return this.dataWatcher.getWatchableObjectShort(1);
    }
    
    public void setAir(final int air) {
        this.dataWatcher.updateObject(1, (short)air);
    }
    
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        this.attackEntityFrom(DamageSource.lightningBolt, 5.0f);
        ++this.fire;
        if (this.fire == 0) {
            this.setFire(8);
        }
    }
    
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
    }
    
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        final BlockPos blockpos = new BlockPos(x, y, z);
        final double d0 = x - blockpos.getX();
        final double d2 = y - blockpos.getY();
        final double d3 = z - blockpos.getZ();
        final List<AxisAlignedBB> list = this.worldObj.func_147461_a(this.getEntityBoundingBox());
        if (list.isEmpty() && !this.worldObj.isBlockFullCube(blockpos)) {
            return false;
        }
        int i = 3;
        double d4 = 9999.0;
        if (!this.worldObj.isBlockFullCube(blockpos.west()) && d0 < d4) {
            d4 = d0;
            i = 0;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.east()) && 1.0 - d0 < d4) {
            d4 = 1.0 - d0;
            i = 1;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.up()) && 1.0 - d2 < d4) {
            d4 = 1.0 - d2;
            i = 3;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.north()) && d3 < d4) {
            d4 = d3;
            i = 4;
        }
        if (!this.worldObj.isBlockFullCube(blockpos.south()) && 1.0 - d3 < d4) {
            d4 = 1.0 - d3;
            i = 5;
        }
        final float f = this.rand.nextFloat() * 0.2f + 0.1f;
        if (i == 0) {
            this.motionX = -f;
        }
        if (i == 1) {
            this.motionX = f;
        }
        if (i == 3) {
            this.motionY = f;
        }
        if (i == 4) {
            this.motionZ = -f;
        }
        if (i == 5) {
            this.motionZ = f;
        }
        return true;
    }
    
    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        String s = EntityList.getEntityString(this);
        if (s == null) {
            s = "generic";
        }
        return StatCollector.translateToLocal("entity." + s + ".name");
    }
    
    public Entity[] getParts() {
        return null;
    }
    
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn;
    }
    
    public float getRotationYawHead() {
        return 0.0f;
    }
    
    public void setRotationYawHead(final float rotation) {
    }
    
    public void func_181013_g(final float p_181013_1_) {
    }
    
    public boolean canAttackWithItem() {
        return true;
    }
    
    public boolean hitByEntity(final Entity entityIn) {
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, (this.worldObj == null) ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }
    
    public boolean isEntityInvulnerable(final DamageSource source) {
        return this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }
    
    public void copyLocationAndAnglesFrom(final Entity entityIn) {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }
    
    public void copyDataFromOld(final Entity entityIn) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        entityIn.writeToNBT(nbttagcompound);
        this.readFromNBT(nbttagcompound);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.field_181016_an = entityIn.field_181016_an;
        this.field_181017_ao = entityIn.field_181017_ao;
        this.field_181018_ap = entityIn.field_181018_ap;
    }
    
    public void travelToDimension(final int dimensionId) {
        if (!this.worldObj.isRemote && !this.isDead) {
            this.worldObj.theProfiler.startSection("changeDimension");
            final MinecraftServer minecraftserver = MinecraftServer.getServer();
            final int i = this.dimension;
            final WorldServer worldserver = minecraftserver.worldServerForDimension(i);
            WorldServer worldserver2 = minecraftserver.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;
            if (i == 1 && dimensionId == 1) {
                worldserver2 = minecraftserver.worldServerForDimension(0);
                this.dimension = 0;
            }
            this.worldObj.removeEntity(this);
            this.isDead = false;
            this.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(this, i, worldserver, worldserver2);
            this.worldObj.theProfiler.endStartSection("reloading");
            final Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver2);
            if (entity != null) {
                entity.copyDataFromOld(this);
                if (i == 1 && dimensionId == 1) {
                    final BlockPos blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver2.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }
                worldserver2.spawnEntityInWorld(entity);
            }
            this.isDead = true;
            this.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver2.resetUpdateEntityTick();
            this.worldObj.theProfiler.endSection();
        }
    }
    
    public float getExplosionResistance(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn) {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }
    
    public boolean verifyExplosion(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn, final float p_174816_5_) {
        return true;
    }
    
    public int getMaxFallHeight() {
        return 3;
    }
    
    public Vec3 func_181014_aG() {
        return this.field_181017_ao;
    }
    
    public EnumFacing func_181012_aH() {
        return this.field_181018_ap;
    }
    
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }
    
    public void addEntityCrashInfo(final CrashReportCategory category) {
        category.addCrashSectionCallable("Entity Type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(EntityList.getEntityString(Entity.this)) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", this.entityId);
        category.addCrashSectionCallable("Entity Name", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.getName();
            }
        });
        category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
        category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
        category.addCrashSectionCallable("Entity's Rider", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.riddenByEntity.toString();
            }
        });
        category.addCrashSectionCallable("Entity's Vehicle", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Entity.this.ridingEntity.toString();
            }
        });
    }
    
    public boolean canRenderOnFire() {
        return this.isBurning();
    }
    
    public UUID getUniqueID() {
        return this.entityUniqueID;
    }
    
    public boolean isPushedByWater() {
        return true;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        final ChatComponentText chatcomponenttext = new ChatComponentText(this.getName());
        chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatcomponenttext;
    }
    
    public void setCustomNameTag(final String name) {
        this.dataWatcher.updateObject(2, name);
    }
    
    public String getCustomNameTag() {
        return this.dataWatcher.getWatchableObjectString(2);
    }
    
    public boolean hasCustomName() {
        return this.dataWatcher.getWatchableObjectString(2).length() > 0;
    }
    
    public void setAlwaysRenderNameTag(final boolean alwaysRenderNameTag) {
        this.dataWatcher.updateObject(3, (byte)(alwaysRenderNameTag ? 1 : 0));
    }
    
    public boolean getAlwaysRenderNameTag() {
        return this.dataWatcher.getWatchableObjectByte(3) == 1;
    }
    
    public void setPositionAndUpdate(final double x, final double y, final double z) {
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
    }
    
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }
    
    public void onDataWatcherUpdate(final int dataID) {
    }
    
    public EnumFacing getHorizontalFacing() {
        return EnumFacing.getHorizontal(MathHelper.floor_double(this.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
    }
    
    protected HoverEvent getHoverEvent() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final String s = EntityList.getEntityString(this);
        nbttagcompound.setString("id", this.getUniqueID().toString());
        if (s != null) {
            nbttagcompound.setString("type", s);
        }
        nbttagcompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nbttagcompound.toString()));
    }
    
    public boolean isSpectatedByPlayer(final EntityPlayerMP player) {
        return true;
    }
    
    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }
    
    public void setEntityBoundingBox(final AxisAlignedBB bb) {
        this.boundingBox = bb;
    }
    
    public float getEyeHeight() {
        return this.height * 0.85f;
    }
    
    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }
    
    public void setOutsideBorder(final boolean outsideBorder) {
        this.isOutsideBorder = outsideBorder;
    }
    
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        return false;
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return true;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }
    
    @Override
    public Vec3 getPositionVector() {
        return new Vec3(this.posX, this.posY, this.posZ);
    }
    
    @Override
    public World getEntityWorld() {
        return this.worldObj;
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
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
        this.cmdResultStats.func_179672_a(this, type, amount);
    }
    
    public CommandResultStats getCommandStats() {
        return this.cmdResultStats;
    }
    
    public void func_174817_o(final Entity entityIn) {
        this.cmdResultStats.func_179671_a(entityIn.getCommandStats());
    }
    
    public NBTTagCompound getNBTTagCompound() {
        return null;
    }
    
    public void clientUpdateEntityNBT(final NBTTagCompound compound) {
    }
    
    public boolean interactAt(final EntityPlayer player, final Vec3 targetVec3) {
        return false;
    }
    
    public boolean isImmuneToExplosions() {
        return false;
    }
    
    protected void applyEnchantments(final EntityLivingBase entityLivingBaseIn, final Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
        }
        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }
}
