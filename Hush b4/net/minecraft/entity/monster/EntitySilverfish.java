// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob
{
    private AISummonSilverfish summonSilverfish;
    
    public EntitySilverfish(final World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.3f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, this.summonSilverfish = new AISummonSilverfish(this));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(5, new AIHideInStone(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    public double getYOffset() {
        return 0.2;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.1f;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.silverfish.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.silverfish.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source instanceof EntityDamageSource || source == DamageSource.magic) {
            this.summonSilverfish.func_179462_f();
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.silverfish.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return null;
    }
    
    @Override
    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return (this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.stone) ? 10.0f : super.getBlockPathWeight(pos);
    }
    
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            final EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0);
            return entityplayer == null;
        }
        return false;
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    static class AIHideInStone extends EntityAIWander
    {
        private final EntitySilverfish field_179485_a;
        private EnumFacing facing;
        private boolean field_179484_c;
        
        public AIHideInStone(final EntitySilverfish p_i45827_1_) {
            super(p_i45827_1_, 1.0, 10);
            this.field_179485_a = p_i45827_1_;
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            if (this.field_179485_a.getAttackTarget() != null) {
                return false;
            }
            if (!this.field_179485_a.getNavigator().noPath()) {
                return false;
            }
            final Random random = this.field_179485_a.getRNG();
            if (random.nextInt(10) == 0) {
                this.facing = EnumFacing.random(random);
                final BlockPos blockpos = new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5, this.field_179485_a.posZ).offset(this.facing);
                final IBlockState iblockstate = this.field_179485_a.worldObj.getBlockState(blockpos);
                if (BlockSilverfish.canContainSilverfish(iblockstate)) {
                    return this.field_179484_c = true;
                }
            }
            this.field_179484_c = false;
            return super.shouldExecute();
        }
        
        @Override
        public boolean continueExecuting() {
            return !this.field_179484_c && super.continueExecuting();
        }
        
        @Override
        public void startExecuting() {
            if (!this.field_179484_c) {
                super.startExecuting();
            }
            else {
                final World world = this.field_179485_a.worldObj;
                final BlockPos blockpos = new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5, this.field_179485_a.posZ).offset(this.facing);
                final IBlockState iblockstate = world.getBlockState(blockpos);
                if (BlockSilverfish.canContainSilverfish(iblockstate)) {
                    world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
                    this.field_179485_a.spawnExplosionParticle();
                    this.field_179485_a.setDead();
                }
            }
        }
    }
    
    static class AISummonSilverfish extends EntityAIBase
    {
        private EntitySilverfish silverfish;
        private int field_179463_b;
        
        public AISummonSilverfish(final EntitySilverfish p_i45826_1_) {
            this.silverfish = p_i45826_1_;
        }
        
        public void func_179462_f() {
            if (this.field_179463_b == 0) {
                this.field_179463_b = 20;
            }
        }
        
        @Override
        public boolean shouldExecute() {
            return this.field_179463_b > 0;
        }
        
        @Override
        public void updateTask() {
            --this.field_179463_b;
            if (this.field_179463_b <= 0) {
                final World world = this.silverfish.worldObj;
                final Random random = this.silverfish.getRNG();
                final BlockPos blockpos = new BlockPos(this.silverfish);
                for (int i = 0; i <= 5 && i >= -5; i = ((i <= 0) ? (1 - i) : (0 - i))) {
                    for (int j = 0; j <= 10 && j >= -10; j = ((j <= 0) ? (1 - j) : (0 - j))) {
                        for (int k = 0; k <= 10 && k >= -10; k = ((k <= 0) ? (1 - k) : (0 - k))) {
                            final BlockPos blockpos2 = blockpos.add(j, i, k);
                            final IBlockState iblockstate = world.getBlockState(blockpos2);
                            if (iblockstate.getBlock() == Blocks.monster_egg) {
                                if (world.getGameRules().getBoolean("mobGriefing")) {
                                    world.destroyBlock(blockpos2, true);
                                }
                                else {
                                    world.setBlockState(blockpos2, iblockstate.getValue(BlockSilverfish.VARIANT).getModelBlock(), 3);
                                }
                                if (random.nextBoolean()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
