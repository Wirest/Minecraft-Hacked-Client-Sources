// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class EntityAIEatGrass extends EntityAIBase
{
    private static final Predicate<IBlockState> field_179505_b;
    private EntityLiving grassEaterEntity;
    private World entityWorld;
    int eatingGrassTimer;
    
    static {
        field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass).where(BlockTallGrass.TYPE, (Predicate<? extends BlockTallGrass.EnumType>)Predicates.equalTo((V)BlockTallGrass.EnumType.GRASS));
    }
    
    public EntityAIEatGrass(final EntityLiving grassEaterEntityIn) {
        this.grassEaterEntity = grassEaterEntityIn;
        this.entityWorld = grassEaterEntityIn.worldObj;
        this.setMutexBits(7);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
        return EntityAIEatGrass.field_179505_b.apply(this.entityWorld.getBlockState(blockpos)) || this.entityWorld.getBlockState(blockpos.down()).getBlock() == Blocks.grass;
    }
    
    @Override
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }
    
    @Override
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.eatingGrassTimer > 0;
    }
    
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }
    
    @Override
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            final BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
            if (EntityAIEatGrass.field_179505_b.apply(this.entityWorld.getBlockState(blockpos))) {
                if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                    this.entityWorld.destroyBlock(blockpos, false);
                }
                this.grassEaterEntity.eatGrassBonus();
            }
            else {
                final BlockPos blockpos2 = blockpos.down();
                if (this.entityWorld.getBlockState(blockpos2).getBlock() == Blocks.grass) {
                    if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                        this.entityWorld.playAuxSFX(2001, blockpos2, Block.getIdFromBlock(Blocks.grass));
                        this.entityWorld.setBlockState(blockpos2, Blocks.dirt.getDefaultState(), 2);
                    }
                    this.grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }
}
