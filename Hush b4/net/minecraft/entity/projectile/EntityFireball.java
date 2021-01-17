// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public abstract class EntityFireball extends Entity
{
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    
    public EntityFireball(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(1.0f, 1.0f);
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
    
    public EntityFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        final double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1;
        this.accelerationY = accelY / d0 * 0.1;
        this.accelerationZ = accelZ / d0 * 0.1;
    }
    
    public EntityFireball(final World worldIn, final EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.shootingEntity = shooter;
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        accelX += this.rand.nextGaussian() * 0.4;
        accelY += this.rand.nextGaussian() * 0.4;
        accelZ += this.rand.nextGaussian() * 0.4;
        final double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1;
        this.accelerationY = accelY / d0 * 0.1;
        this.accelerationZ = accelZ / d0 * 0.1;
    }
    
    @Override
    public void onUpdate() {
        if (this.worldObj.isRemote || ((this.shootingEntity == null || !this.shootingEntity.isDead) && this.worldObj.isBlockLoaded(new BlockPos(this)))) {
            super.onUpdate();
            this.setFire(1);
            if (this.inGround) {
                if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                    ++this.ticksAlive;
                    if (this.ticksAlive == 600) {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksAlive = 0;
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
            Entity entity = null;
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d0 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (!entity2.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
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
            if (movingobjectposition != null) {
                this.onImpact(movingobjectposition);
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0 / 3.141592653589793) + 90.0f;
            this.rotationPitch = (float)(MathHelper.func_181159_b(f2, this.motionY) * 180.0 / 3.141592653589793) - 90.0f;
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
            float f3 = this.getMotionFactor();
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    final float f4 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                f3 = 0.8f;
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= f3;
            this.motionY *= f3;
            this.motionZ *= f3;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        else {
            this.setDead();
        }
    }
    
    protected float getMotionFactor() {
        return 0.95f;
    }
    
    protected abstract void onImpact(final MovingObjectPosition p0);
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
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
        this.inGround = (tagCompund.getByte("inGround") == 1);
        if (tagCompund.hasKey("direction", 9)) {
            final NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
            this.motionX = nbttaglist.getDoubleAt(0);
            this.motionY = nbttaglist.getDoubleAt(1);
            this.motionZ = nbttaglist.getDoubleAt(2);
        }
        else {
            this.setDead();
        }
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 1.0f;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setBeenAttacked();
        if (source.getEntity() != null) {
            final Vec3 vec3 = source.getEntity().getLookVec();
            if (vec3 != null) {
                this.motionX = vec3.xCoord;
                this.motionY = vec3.yCoord;
                this.motionZ = vec3.zCoord;
                this.accelerationX = this.motionX * 0.1;
                this.accelerationY = this.motionY * 0.1;
                this.accelerationZ = this.motionZ * 0.1;
            }
            if (source.getEntity() instanceof EntityLivingBase) {
                this.shootingEntity = (EntityLivingBase)source.getEntity();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 15728880;
    }
}
