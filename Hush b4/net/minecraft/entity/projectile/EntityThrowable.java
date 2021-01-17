// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import java.util.UUID;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public abstract class EntityThrowable extends Entity implements IProjectile
{
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    
    public EntityThrowable(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    public EntityThrowable(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.thrower = throwerIn;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(throwerIn.posX, throwerIn.posY + throwerIn.getEyeHeight(), throwerIn.posZ, throwerIn.rotationYaw, throwerIn.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        final float f = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f;
        this.motionY = -MathHelper.sin((this.rotationPitch + this.getInaccuracy()) / 180.0f * 3.1415927f) * f;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getVelocity(), 1.0f);
    }
    
    public EntityThrowable(final World worldIn, final double x, final double y, final double z) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ticksInGround = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
    }
    
    protected float getVelocity() {
        return 1.5f;
    }
    
    protected float getInaccuracy() {
        return 0.0f;
    }
    
    @Override
    public void setThrowableHeading(double x, double y, double z, final float velocity, final float inaccuracy) {
        final float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= f;
        y /= f;
        z /= f;
        x += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        y += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        z += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        final float f2 = MathHelper.sqrt_double(x * x + z * z);
        final float n = (float)(MathHelper.func_181159_b(x, z) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(MathHelper.func_181159_b(y, f2) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(x * x + z * z);
            final float n = (float)(MathHelper.func_181159_b(x, z) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(MathHelper.func_181159_b(y, f) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
        }
    }
    
    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.throwableShake > 0) {
            --this.throwableShake;
        }
        if (this.inGround) {
            if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
                return;
            }
            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2f;
            this.motionY *= this.rand.nextFloat() * 0.2f;
            this.motionZ *= this.rand.nextFloat() * 0.2f;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else {
            ++this.ticksInAir;
        }
        Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
        Vec3 vec4 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec4);
        vec3 = new Vec3(this.posX, this.posY, this.posZ);
        vec4 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec4 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        if (!this.worldObj.isRemote) {
            Entity entity = null;
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d0 = 0.0;
            final EntityLivingBase entitylivingbase = this.getThrower();
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity2 = list.get(j);
                if (entity2.canBeCollidedWith() && (entity2 != entitylivingbase || this.ticksInAir >= 5)) {
                    final float f = 0.3f;
                    final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f, f, f);
                    final MovingObjectPosition movingobjectposition2 = axisalignedbb.calculateIntercept(vec3, vec4);
                    if (movingobjectposition2 != null) {
                        final double d2 = vec3.squareDistanceTo(movingobjectposition2.hitVec);
                        if (d2 < d0 || d0 == 0.0) {
                            entity = entity2;
                            d0 = d2;
                        }
                    }
                }
            }
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
                this.func_181015_d(movingobjectposition.getBlockPos());
            }
            else {
                this.onImpact(movingobjectposition);
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
        this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f2) * 180.0 / 3.141592653589793);
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
        float f3 = 0.99f;
        final float f4 = this.getGravityVelocity();
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                final float f5 = 0.25f;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f5, this.posY - this.motionY * f5, this.posZ - this.motionZ * f5, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
            f3 = 0.8f;
        }
        this.motionX *= f3;
        this.motionY *= f3;
        this.motionZ *= f3;
        this.motionY -= f4;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    protected float getGravityVelocity() {
        return 0.03f;
    }
    
    protected abstract void onImpact(final MovingObjectPosition p0);
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        tagCompound.setByte("shake", (byte)this.throwableShake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }
        tagCompound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.throwableShake = (tagCompund.getByte("shake") & 0xFF);
        this.inGround = (tagCompund.getByte("inGround") == 1);
        this.thrower = null;
        this.throwerName = tagCompund.getString("ownerName");
        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }
        this.thrower = this.getThrower();
    }
    
    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
            if (this.thrower == null && this.worldObj instanceof WorldServer) {
                try {
                    final Entity entity = ((WorldServer)this.worldObj).getEntityFromUuid(UUID.fromString(this.throwerName));
                    if (entity instanceof EntityLivingBase) {
                        this.thrower = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable var2) {
                    this.thrower = null;
                }
            }
        }
        return this.thrower;
    }
}
