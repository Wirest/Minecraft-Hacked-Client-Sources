// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.Vec3i;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID FLEEING_SPEED_MODIFIER_UUID;
    public static final AttributeModifier FLEEING_SPEED_MODIFIER;
    private BlockPos homePosition;
    private float maximumHomeDistance;
    private EntityAIBase aiBase;
    private boolean isMovementAITaskSet;
    
    static {
        FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
        FLEEING_SPEED_MODIFIER = new AttributeModifier(EntityCreature.FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0, 2).setSaved(false);
    }
    
    public EntityCreature(final World worldIn) {
        super(worldIn);
        this.homePosition = BlockPos.ORIGIN;
        this.maximumHomeDistance = -1.0f;
        this.aiBase = new EntityAIMoveTowardsRestriction(this, 1.0);
    }
    
    public float getBlockPathWeight(final BlockPos pos) {
        return 0.0f;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0f;
    }
    
    public boolean hasPath() {
        return !this.navigator.noPath();
    }
    
    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }
    
    public boolean isWithinHomeDistanceFromPosition(final BlockPos pos) {
        return this.maximumHomeDistance == -1.0f || this.homePosition.distanceSq(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
    }
    
    public void setHomePosAndDistance(final BlockPos pos, final int distance) {
        this.homePosition = pos;
        this.maximumHomeDistance = (float)distance;
    }
    
    public BlockPos getHomePosition() {
        return this.homePosition;
    }
    
    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }
    
    public void detachHome() {
        this.maximumHomeDistance = -1.0f;
    }
    
    public boolean hasHome() {
        return this.maximumHomeDistance != -1.0f;
    }
    
    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
            final Entity entity = this.getLeashedToEntity();
            this.setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            final float f = this.getDistanceToEntity(entity);
            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
                if (f > 10.0f) {
                    this.clearLeashed(true, true);
                }
                return;
            }
            if (!this.isMovementAITaskSet) {
                this.tasks.addTask(2, this.aiBase);
                if (this.getNavigator() instanceof PathNavigateGround) {
                    ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
                }
                this.isMovementAITaskSet = true;
            }
            this.func_142017_o(f);
            if (f > 4.0f) {
                this.getNavigator().tryMoveToEntityLiving(entity, 1.0);
            }
            if (f > 6.0f) {
                final double d0 = (entity.posX - this.posX) / f;
                final double d2 = (entity.posY - this.posY) / f;
                final double d3 = (entity.posZ - this.posZ) / f;
                this.motionX += d0 * Math.abs(d0) * 0.4;
                this.motionY += d2 * Math.abs(d2) * 0.4;
                this.motionZ += d3 * Math.abs(d3) * 0.4;
            }
            if (f > 10.0f) {
                this.clearLeashed(true, true);
            }
        }
        else if (!this.getLeashed() && this.isMovementAITaskSet) {
            this.isMovementAITaskSet = false;
            this.tasks.removeTask(this.aiBase);
            if (this.getNavigator() instanceof PathNavigateGround) {
                ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
            }
            this.detachHome();
        }
    }
    
    protected void func_142017_o(final float p_142017_1_) {
    }
}
