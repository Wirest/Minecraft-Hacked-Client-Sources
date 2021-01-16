package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity
{
    private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.<ItemStack>createKey(EntityFireworkRocket.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final DataParameter<Integer> field_191512_b = EntityDataManager.<Integer>createKey(EntityFireworkRocket.class, DataSerializers.VARINT);

    /** The age of the firework in ticks. */
    private int fireworkAge;

    /**
     * The lifetime of the firework in ticks. When the age reaches the lifetime the firework explodes.
     */
    private int lifetime;
    private EntityLivingBase field_191513_e;

    public EntityFireworkRocket(World worldIn)
    {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
        this.dataManager.register(FIREWORK_ITEM, ItemStack.field_190927_a);
        this.dataManager.register(field_191512_b, Integer.valueOf(0));
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 4096.0D && !this.func_191511_j();
    }

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return super.isInRangeToRender3d(x, y, z) && !this.func_191511_j();
    }

    public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem)
    {
        super(worldIn);
        this.fireworkAge = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        int i = 1;

        if (!givenItem.func_190926_b() && givenItem.hasTagCompound())
        {
            this.dataManager.set(FIREWORK_ITEM, givenItem.copy());
            NBTTagCompound nbttagcompound = givenItem.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }

        this.motionX = this.rand.nextGaussian() * 0.001D;
        this.motionZ = this.rand.nextGaussian() * 0.001D;
        this.motionY = 0.05D;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    public EntityFireworkRocket(World p_i47367_1_, ItemStack p_i47367_2_, EntityLivingBase p_i47367_3_)
    {
        this(p_i47367_1_, p_i47367_3_.posX, p_i47367_3_.posY, p_i47367_3_.posZ, p_i47367_2_);
        this.dataManager.set(field_191512_b, Integer.valueOf(p_i47367_3_.getEntityId()));
        this.field_191513_e = p_i47367_3_;
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.func_191511_j())
        {
            if (this.field_191513_e == null)
            {
                Entity entity = this.world.getEntityByID(((Integer)this.dataManager.get(field_191512_b)).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    this.field_191513_e = (EntityLivingBase)entity;
                }
            }

            if (this.field_191513_e != null)
            {
                if (this.field_191513_e.isElytraFlying())
                {
                    Vec3d vec3d = this.field_191513_e.getLookVec();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    this.field_191513_e.motionX += vec3d.xCoord * 0.1D + (vec3d.xCoord * 1.5D - this.field_191513_e.motionX) * 0.5D;
                    this.field_191513_e.motionY += vec3d.yCoord * 0.1D + (vec3d.yCoord * 1.5D - this.field_191513_e.motionY) * 0.5D;
                    this.field_191513_e.motionZ += vec3d.zCoord * 0.1D + (vec3d.zCoord * 1.5D - this.field_191513_e.motionZ) * 0.5D;
                }

                this.setPosition(this.field_191513_e.posX, this.field_191513_e.posY, this.field_191513_e.posZ);
                this.motionX = this.field_191513_e.motionX;
                this.motionY = this.field_191513_e.motionY;
                this.motionZ = this.field_191513_e.motionZ;
            }
        }
        else
        {
            this.motionX *= 1.15D;
            this.motionZ *= 1.15D;
            this.motionY += 0.04D;
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }

        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (this.fireworkAge == 0 && !this.isSilent())
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.fireworkAge;

        if (this.world.isRemote && this.fireworkAge % 2 < 2)
        {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D);
        }

        if (!this.world.isRemote && this.fireworkAge > this.lifetime)
        {
            this.world.setEntityState(this, (byte)17);
            this.func_191510_k();
            this.setDead();
        }
    }

    private void func_191510_k()
    {
        float f = 0.0F;
        ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.func_190926_b() ? null : itemstack.getSubCompound("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getTagList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.hasNoTags())
        {
            f = (float)(5 + nbttaglist.tagCount() * 2);
        }

        if (f > 0.0F)
        {
            if (this.field_191513_e != null)
            {
                this.field_191513_e.attackEntityFrom(DamageSource.field_191552_t, (float)(5 + nbttaglist.tagCount() * 2));
            }

            double d0 = 5.0D;
            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

            for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expandXyz(5.0D)))
            {
                if (entitylivingbase != this.field_191513_e && this.getDistanceSqToEntity(entitylivingbase) <= 25.0D)
                {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i)
                    {
                        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, new Vec3d(entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.height * 0.5D * (double)i, entitylivingbase.posZ), false, true, false);

                        if (raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS)
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (flag)
                    {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)this.getDistanceToEntity(entitylivingbase)) / 5.0D);
                        entitylivingbase.attackEntityFrom(DamageSource.field_191552_t, f1);
                    }
                }
            }
        }
    }

    public boolean func_191511_j()
    {
        return ((Integer)this.dataManager.get(field_191512_b)).intValue() > 0;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && this.world.isRemote)
        {
            ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
            NBTTagCompound nbttagcompound = itemstack.func_190926_b() ? null : itemstack.getSubCompound("Fireworks");
            this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
        }

        super.handleStatusUpdate(id);
    }

    public static void registerFixesFireworkRocket(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityFireworkRocket.class, new String[] {"FireworksItem"}));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Life", this.fireworkAge);
        compound.setInteger("LifeTime", this.lifetime);
        ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);

        if (!itemstack.func_190926_b())
        {
            compound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.fireworkAge = compound.getInteger("Life");
        this.lifetime = compound.getInteger("LifeTime");
        NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.func_190926_b())
            {
                this.dataManager.set(FIREWORK_ITEM, itemstack);
            }
        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
