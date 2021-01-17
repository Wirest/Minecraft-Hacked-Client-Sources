// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntitySquid extends EntityWaterMob
{
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float field_70871_bB;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    
    public EntitySquid(final World worldIn) {
        super(worldIn);
        this.setSize(0.95f, 0.95f);
        this.rand.setSeed(1 + this.getEntityId());
        this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.tasks.addTask(0, new AIMoveRandom(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }
    
    @Override
    protected String getLivingSound() {
        return null;
    }
    
    @Override
    protected String getHurtSound() {
        return null;
    }
    
    @Override
    protected String getDeathSound() {
        return null;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected Item getDropItem() {
        return null;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(3 + p_70628_2_) + 1, j = 0; j < i; ++j) {
            this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0f);
        }
    }
    
    @Override
    public boolean isInWater() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.6000000238418579, 0.0), Material.water, this);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if (this.squidRotation > 6.283185307179586) {
            if (this.worldObj.isRemote) {
                this.squidRotation = 6.2831855f;
            }
            else {
                this.squidRotation -= (float)6.283185307179586;
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
                }
                this.worldObj.setEntityState(this, (byte)19);
            }
        }
        if (this.inWater) {
            if (this.squidRotation < 3.1415927f) {
                final float f = this.squidRotation / 3.1415927f;
                this.tentacleAngle = MathHelper.sin(f * f * 3.1415927f) * 3.1415927f * 0.25f;
                if (f > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                    this.field_70871_bB = 1.0f;
                }
                else {
                    this.field_70871_bB *= 0.8f;
                }
            }
            else {
                this.tentacleAngle = 0.0f;
                this.randomMotionSpeed *= 0.9f;
                this.field_70871_bB *= 0.99f;
            }
            if (!this.worldObj.isRemote) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-(float)MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0f / 3.1415927f - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw += (float)(3.141592653589793 * this.field_70871_bB * 1.5);
            this.squidPitch += (-(float)MathHelper.func_181159_b(f2, this.motionY) * 180.0f / 3.1415927f - this.squidPitch) * 0.1f;
        }
        else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927f * 0.25f;
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY -= 0.08;
                this.motionY *= 0.9800000190734863;
                this.motionZ = 0.0;
            }
            this.squidPitch += (float)((-90.0f - this.squidPitch) * 0.02);
        }
    }
    
    @Override
    public void moveEntityWithHeading(final float strafe, final float forward) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 45.0 && this.posY < this.worldObj.func_181545_F() && super.getCanSpawnHere();
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 19) {
            this.squidRotation = 0.0f;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public void func_175568_b(final float randomMotionVecXIn, final float randomMotionVecYIn, final float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }
    
    public boolean func_175567_n() {
        return this.randomMotionVecX != 0.0f || this.randomMotionVecY != 0.0f || this.randomMotionVecZ != 0.0f;
    }
    
    static class AIMoveRandom extends EntityAIBase
    {
        private EntitySquid squid;
        
        public AIMoveRandom(final EntitySquid p_i45859_1_) {
            this.squid = p_i45859_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            final int i = this.squid.getAge();
            if (i > 100) {
                this.squid.func_175568_b(0.0f, 0.0f, 0.0f);
            }
            else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.func_175567_n()) {
                final float f = this.squid.getRNG().nextFloat() * 3.1415927f * 2.0f;
                final float f2 = MathHelper.cos(f) * 0.2f;
                final float f3 = -0.1f + this.squid.getRNG().nextFloat() * 0.2f;
                final float f4 = MathHelper.sin(f) * 0.2f;
                this.squid.func_175568_b(f2, f3, f4);
            }
        }
    }
}
