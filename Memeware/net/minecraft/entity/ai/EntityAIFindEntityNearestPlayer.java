package net.minecraft.entity.ai;

import com.google.common.base.Predicate;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {
    private static final Logger field_179436_a = LogManager.getLogger();
    private EntityLiving field_179434_b;
    private final Predicate field_179435_c;
    private final EntityAINearestAttackableTarget.Sorter field_179432_d;
    private EntityLivingBase field_179433_e;
    private static final String __OBFID = "CL_00002248";

    public EntityAIFindEntityNearestPlayer(EntityLiving p_i45882_1_) {
        this.field_179434_b = p_i45882_1_;

        if (p_i45882_1_ instanceof EntityCreature) {
            field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179435_c = new Predicate() {
            private static final String __OBFID = "CL_00002247";

            public boolean func_179880_a(Entity p_179880_1_) {
                if (!(p_179880_1_ instanceof EntityPlayer)) {
                    return false;
                } else {
                    double var2 = EntityAIFindEntityNearestPlayer.this.func_179431_f();

                    if (p_179880_1_.isSneaking()) {
                        var2 *= 0.800000011920929D;
                    }

                    if (p_179880_1_.isInvisible()) {
                        float var4 = ((EntityPlayer) p_179880_1_).getArmorVisibility();

                        if (var4 < 0.1F) {
                            var4 = 0.1F;
                        }

                        var2 *= (double) (0.7F * var4);
                    }

                    return (double) p_179880_1_.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > var2 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase) p_179880_1_, false, true);
                }
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_179880_a((Entity) p_apply_1_);
            }
        };
        this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(p_i45882_1_);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        double var1 = this.func_179431_f();
        List var3 = this.field_179434_b.worldObj.func_175647_a(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(var1, 4.0D, var1), this.field_179435_c);
        Collections.sort(var3, this.field_179432_d);

        if (var3.isEmpty()) {
            return false;
        } else {
            this.field_179433_e = (EntityLivingBase) var3.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        EntityLivingBase var1 = this.field_179434_b.getAttackTarget();

        if (var1 == null) {
            return false;
        } else if (!var1.isEntityAlive()) {
            return false;
        } else {
            Team var2 = this.field_179434_b.getTeam();
            Team var3 = var1.getTeam();

            if (var2 != null && var3 == var2) {
                return false;
            } else {
                double var4 = this.func_179431_f();
                return this.field_179434_b.getDistanceSqToEntity(var1) > var4 * var4 ? false : !(var1 instanceof EntityPlayerMP) || !((EntityPlayerMP) var1).theItemInWorldManager.isCreative();
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.field_179434_b.setAttackTarget(this.field_179433_e);
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.field_179434_b.setAttackTarget((EntityLivingBase) null);
        super.startExecuting();
    }

    protected double func_179431_f() {
        IAttributeInstance var1 = this.field_179434_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return var1 == null ? 16.0D : var1.getAttributeValue();
    }
}
