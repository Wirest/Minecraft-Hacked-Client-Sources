package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
    private static final Predicate field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass).func_177637_a(BlockTallGrass.field_176497_a, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));

    /**
     * The entity owner of this AITask
     */
    private EntityLiving grassEaterEntity;

    /**
     * The world the grass eater entity is eating from
     */
    private World entityWorld;

    /**
     * Number of ticks since the entity started to eat grass
     */
    int eatingGrassTimer;
    private static final String __OBFID = "CL_00001582";

    public EntityAIEatGrass(EntityLiving p_i45314_1_) {
        this.grassEaterEntity = p_i45314_1_;
        this.entityWorld = p_i45314_1_.worldObj;
        this.setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos var1 = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
            return field_179505_b.apply(this.entityWorld.getBlockState(var1)) ? true : this.entityWorld.getBlockState(var1.offsetDown()).getBlock() == Blocks.grass;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte) 10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.eatingGrassTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat grass
     */
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);

        if (this.eatingGrassTimer == 4) {
            BlockPos var1 = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);

            if (field_179505_b.apply(this.entityWorld.getBlockState(var1))) {
                if (this.entityWorld.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    this.entityWorld.destroyBlock(var1, false);
                }

                this.grassEaterEntity.eatGrassBonus();
            } else {
                BlockPos var2 = var1.offsetDown();

                if (this.entityWorld.getBlockState(var2).getBlock() == Blocks.grass) {
                    if (this.entityWorld.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                        this.entityWorld.playAuxSFX(2001, var2, Block.getIdFromBlock(Blocks.grass));
                        this.entityWorld.setBlockState(var2, Blocks.dirt.getDefaultState(), 2);
                    }

                    this.grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }
}
