package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIWander extends EntityAIBase {
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int field_179481_f;
    private boolean field_179482_g;
    private static final String __OBFID = "CL_00001608";

    public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
        this(p_i1648_1_, p_i1648_2_, 120);
    }

    public EntityAIWander(EntityCreature p_i45887_1_, double p_i45887_2_, int p_i45887_4_) {
        this.entity = p_i45887_1_;
        this.speed = p_i45887_2_;
        this.field_179481_f = p_i45887_4_;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.field_179482_g) {
            if (this.entity.getAge() >= 100) {
                return false;
            }

            if (this.entity.getRNG().nextInt(this.field_179481_f) != 0) {
                return false;
            }
        }

        Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

        if (var1 == null) {
            return false;
        } else {
            this.xPosition = var1.xCoord;
            this.yPosition = var1.yCoord;
            this.zPosition = var1.zCoord;
            this.field_179482_g = false;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void func_179480_f() {
        this.field_179482_g = true;
    }

    public void func_179479_b(int p_179479_1_) {
        this.field_179481_f = p_179479_1_;
    }
}
