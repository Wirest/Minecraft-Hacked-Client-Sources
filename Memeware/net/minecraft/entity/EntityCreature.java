package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {
    public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private BlockPos homePosition;

    /**
     * If -1 there is no maximum distance
     */
    private float maximumHomeDistance;
    private EntityAIBase aiBase;
    private boolean field_110180_bt;
    private static final String __OBFID = "CL_00001558";

    public EntityCreature(World worldIn) {
        super(worldIn);
        this.homePosition = BlockPos.ORIGIN;
        this.maximumHomeDistance = -1.0F;
        this.aiBase = new EntityAIMoveTowardsRestriction(this, 1.0D);
    }

    public float func_180484_a(BlockPos p_180484_1_) {
        return 0.0F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.func_180484_a(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
    }

    /**
     * if the entity got a PathEntity it returns true, else false
     */
    public boolean hasPath() {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.func_180485_d(new BlockPos(this));
    }

    public boolean func_180485_d(BlockPos p_180485_1_) {
        return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(p_180485_1_) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
    }

    public void func_175449_a(BlockPos p_175449_1_, int p_175449_2_) {
        this.homePosition = p_175449_1_;
        this.maximumHomeDistance = (float) p_175449_2_;
    }

    public BlockPos func_180486_cf() {
        return this.homePosition;
    }

    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }

    public void detachHome() {
        this.maximumHomeDistance = -1.0F;
    }

    /**
     * Returns whether a home area is defined for this entity.
     */
    public boolean hasHome() {
        return this.maximumHomeDistance != -1.0F;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState() {
        super.updateLeashedState();

        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
            Entity var1 = this.getLeashedToEntity();
            this.func_175449_a(new BlockPos((int) var1.posX, (int) var1.posY, (int) var1.posZ), 5);
            float var2 = this.getDistanceToEntity(var1);

            if (this instanceof EntityTameable && ((EntityTameable) this).isSitting()) {
                if (var2 > 10.0F) {
                    this.clearLeashed(true, true);
                }

                return;
            }

            if (!this.field_110180_bt) {
                this.tasks.addTask(2, this.aiBase);

                if (this.getNavigator() instanceof PathNavigateGround) {
                    ((PathNavigateGround) this.getNavigator()).func_179690_a(false);
                }

                this.field_110180_bt = true;
            }

            this.func_142017_o(var2);

            if (var2 > 4.0F) {
                this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
            }

            if (var2 > 6.0F) {
                double var3 = (var1.posX - this.posX) / (double) var2;
                double var5 = (var1.posY - this.posY) / (double) var2;
                double var7 = (var1.posZ - this.posZ) / (double) var2;
                this.motionX += var3 * Math.abs(var3) * 0.4D;
                this.motionY += var5 * Math.abs(var5) * 0.4D;
                this.motionZ += var7 * Math.abs(var7) * 0.4D;
            }

            if (var2 > 10.0F) {
                this.clearLeashed(true, true);
            }
        } else if (!this.getLeashed() && this.field_110180_bt) {
            this.field_110180_bt = false;
            this.tasks.removeTask(this.aiBase);

            if (this.getNavigator() instanceof PathNavigateGround) {
                ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
            }

            this.detachHome();
        }
    }

    protected void func_142017_o(float p_142017_1_) {
    }
}
