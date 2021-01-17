// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockCarrot;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class EntityRabbit extends EntityAnimal
{
    private AIAvoidEntity<EntityWolf> aiAvoidWolves;
    private int field_175540_bm;
    private int field_175535_bn;
    private boolean field_175536_bo;
    private boolean field_175537_bp;
    private int currentMoveTypeDuration;
    private EnumMoveType moveType;
    private int carrotTicks;
    private EntityPlayer field_175543_bt;
    
    public EntityRabbit(final World worldIn) {
        super(worldIn);
        this.field_175540_bm = 0;
        this.field_175535_bn = 0;
        this.field_175536_bo = false;
        this.field_175537_bp = false;
        this.currentMoveTypeDuration = 0;
        this.moveType = EnumMoveType.HOP;
        this.carrotTicks = 0;
        this.field_175543_bt = null;
        this.setSize(0.6f, 0.7f);
        this.jumpHelper = new RabbitJumpHelper(this);
        this.moveHelper = new RabbitMoveHelper(this);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.navigator.setHeightRequirement(2.5f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIPanic(this, 1.33));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Items.carrot, false));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Items.golden_carrot, false));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Item.getItemFromBlock(Blocks.yellow_flower), false));
        this.tasks.addTask(3, new EntityAIMate(this, 0.8));
        this.tasks.addTask(5, new AIRaidFarm(this));
        this.tasks.addTask(5, new EntityAIWander(this, 0.6));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.aiAvoidWolves = new AIAvoidEntity<EntityWolf>(this, EntityWolf.class, 16.0f, 1.33, 1.33);
        this.tasks.addTask(4, this.aiAvoidWolves);
        this.setMovementSpeed(0.0);
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        return (this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5) ? 0.5f : this.moveType.func_180074_b();
    }
    
    public void setMoveType(final EnumMoveType type) {
        this.moveType = type;
    }
    
    public float func_175521_o(final float p_175521_1_) {
        return (this.field_175535_bn == 0) ? 0.0f : ((this.field_175540_bm + p_175521_1_) / this.field_175535_bn);
    }
    
    public void setMovementSpeed(final double newSpeed) {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }
    
    public void setJumping(final boolean jump, final EnumMoveType moveTypeIn) {
        super.setJumping(jump);
        if (!jump) {
            if (this.moveType == EnumMoveType.ATTACK) {
                this.moveType = EnumMoveType.HOP;
            }
        }
        else {
            this.setMovementSpeed(1.5 * moveTypeIn.getSpeed());
            this.playSound(this.getJumpingSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
        this.field_175536_bo = jump;
    }
    
    public void doMovementAction(final EnumMoveType movetype) {
        this.setJumping(true, movetype);
        this.field_175535_bn = movetype.func_180073_d();
        this.field_175540_bm = 0;
    }
    
    public boolean func_175523_cj() {
        return this.field_175536_bo;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (Byte)0);
    }
    
    public void updateAITasks() {
        if (this.moveHelper.getSpeed() > 0.8) {
            this.setMoveType(EnumMoveType.SPRINT);
        }
        else if (this.moveType != EnumMoveType.ATTACK) {
            this.setMoveType(EnumMoveType.HOP);
        }
        if (this.currentMoveTypeDuration > 0) {
            --this.currentMoveTypeDuration;
        }
        if (this.carrotTicks > 0) {
            this.carrotTicks -= this.rand.nextInt(3);
            if (this.carrotTicks < 0) {
                this.carrotTicks = 0;
            }
        }
        if (this.onGround) {
            if (!this.field_175537_bp) {
                this.setJumping(false, EnumMoveType.NONE);
                this.func_175517_cu();
            }
            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
                final EntityLivingBase entitylivingbase = this.getAttackTarget();
                if (entitylivingbase != null && this.getDistanceSqToEntity(entitylivingbase) < 16.0) {
                    this.calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
                    this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
                    this.doMovementAction(EnumMoveType.ATTACK);
                    this.field_175537_bp = true;
                }
            }
            final RabbitJumpHelper entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper;
            if (!entityrabbit$rabbitjumphelper.getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    final PathEntity pathentity = this.navigator.getPath();
                    Vec3 vec3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()) {
                        vec3 = pathentity.getPosition(this);
                    }
                    this.calculateRotationYaw(vec3.xCoord, vec3.zCoord);
                    this.doMovementAction(this.moveType);
                }
            }
            else if (!entityrabbit$rabbitjumphelper.func_180065_d()) {
                this.func_175518_cr();
            }
        }
        this.field_175537_bp = this.onGround;
    }
    
    @Override
    public void spawnRunningParticles() {
    }
    
    private void calculateRotationYaw(final double x, final double z) {
        this.rotationYaw = (float)(MathHelper.func_181159_b(z - this.posZ, x - this.posX) * 180.0 / 3.141592653589793) - 90.0f;
    }
    
    private void func_175518_cr() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }
    
    private void func_175520_cs() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }
    
    private void updateMoveTypeDuration() {
        this.currentMoveTypeDuration = this.getMoveTypeDuration();
    }
    
    private void func_175517_cu() {
        this.updateMoveTypeDuration();
        this.func_175520_cs();
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.field_175540_bm != this.field_175535_bn) {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote) {
                this.worldObj.setEntityState(this, (byte)1);
            }
            ++this.field_175540_bm;
        }
        else if (this.field_175535_bn != 0) {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("RabbitType", this.getRabbitType());
        tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setRabbitType(tagCompund.getInteger("RabbitType"));
        this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
    }
    
    protected String getJumpingSound() {
        return "mob.rabbit.hop";
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.rabbit.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.rabbit.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.rabbit.death";
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (this.getRabbitType() == 99) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0f);
        }
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Override
    public int getTotalArmorValue() {
        return (this.getRabbitType() == 99) ? 8 : super.getTotalArmorValue();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void addRandomDrop() {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0f);
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_), j = 0; j < i; ++j) {
            this.dropItem(Items.rabbit_hide, 1);
        }
        for (int i = this.rand.nextInt(2), k = 0; k < i; ++k) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_rabbit, 1);
            }
            else {
                this.dropItem(Items.rabbit, 1);
            }
        }
    }
    
    private boolean isRabbitBreedingItem(final Item itemIn) {
        return itemIn == Items.carrot || itemIn == Items.golden_carrot || itemIn == Item.getItemFromBlock(Blocks.yellow_flower);
    }
    
    @Override
    public EntityRabbit createChild(final EntityAgeable ageable) {
        final EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);
        if (ageable instanceof EntityRabbit) {
            entityrabbit.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)ageable).getRabbitType());
        }
        return entityrabbit;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && this.isRabbitBreedingItem(stack.getItem());
    }
    
    public int getRabbitType() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }
    
    public void setRabbitType(final int rabbitTypeId) {
        if (rabbitTypeId == 99) {
            this.tasks.removeTask(this.aiAvoidWolves);
            this.tasks.addTask(4, new AIEvilAttack(this));
            this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
            }
        }
        this.dataWatcher.updateObject(18, (byte)rabbitTypeId);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.rand.nextInt(6);
        boolean flag = false;
        if (livingdata instanceof RabbitTypeData) {
            i = ((RabbitTypeData)livingdata).typeData;
            flag = true;
        }
        else {
            livingdata = new RabbitTypeData(i);
        }
        this.setRabbitType(i);
        if (flag) {
            this.setGrowingAge(-24000);
        }
        return livingdata;
    }
    
    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }
    
    protected int getMoveTypeDuration() {
        return this.moveType.getDuration();
    }
    
    protected void createEatingParticles() {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, Block.getStateId(Blocks.carrots.getStateFromMeta(7)));
        this.carrotTicks = 100;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 1) {
            this.createRunningParticles();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    enum EnumMoveType
    {
        NONE("NONE", 0, 0.0f, 0.0f, 30, 1), 
        HOP("HOP", 1, 0.8f, 0.2f, 20, 10), 
        STEP("STEP", 2, 1.0f, 0.45f, 14, 14), 
        SPRINT("SPRINT", 3, 1.75f, 0.4f, 1, 8), 
        ATTACK("ATTACK", 4, 2.0f, 0.7f, 7, 8);
        
        private final float speed;
        private final float field_180077_g;
        private final int duration;
        private final int field_180085_i;
        
        private EnumMoveType(final String name, final int ordinal, final float typeSpeed, final float p_i45866_4_, final int typeDuration, final int p_i45866_6_) {
            this.speed = typeSpeed;
            this.field_180077_g = p_i45866_4_;
            this.duration = typeDuration;
            this.field_180085_i = p_i45866_6_;
        }
        
        public float getSpeed() {
            return this.speed;
        }
        
        public float func_180074_b() {
            return this.field_180077_g;
        }
        
        public int getDuration() {
            return this.duration;
        }
        
        public int func_180073_d() {
            return this.field_180085_i;
        }
    }
    
    static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
    {
        private EntityRabbit entityInstance;
        
        public AIAvoidEntity(final EntityRabbit p_i46403_1_, final Class<T> p_i46403_2_, final float p_i46403_3_, final double p_i46403_4_, final double p_i46403_6_) {
            super(p_i46403_1_, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
            this.entityInstance = p_i46403_1_;
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
        }
    }
    
    static class AIEvilAttack extends EntityAIAttackOnCollide
    {
        public AIEvilAttack(final EntityRabbit p_i45867_1_) {
            super(p_i45867_1_, EntityLivingBase.class, 1.4, true);
        }
        
        @Override
        protected double func_179512_a(final EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }
    
    static class AIPanic extends EntityAIPanic
    {
        private EntityRabbit theEntity;
        
        public AIPanic(final EntityRabbit p_i45861_1_, final double speedIn) {
            super(p_i45861_1_, speedIn);
            this.theEntity = p_i45861_1_;
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.theEntity.setMovementSpeed(this.speed);
        }
    }
    
    static class AIRaidFarm extends EntityAIMoveToBlock
    {
        private final EntityRabbit field_179500_c;
        private boolean field_179498_d;
        private boolean field_179499_e;
        
        public AIRaidFarm(final EntityRabbit p_i45860_1_) {
            super(p_i45860_1_, 0.699999988079071, 16);
            this.field_179499_e = false;
            this.field_179500_c = p_i45860_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            if (this.runDelay <= 0) {
                if (!this.field_179500_c.worldObj.getGameRules().getBoolean("mobGriefing")) {
                    return false;
                }
                this.field_179499_e = false;
                this.field_179498_d = this.field_179500_c.isCarrotEaten();
            }
            return super.shouldExecute();
        }
        
        @Override
        public boolean continueExecuting() {
            return this.field_179499_e && super.continueExecuting();
        }
        
        @Override
        public void startExecuting() {
            super.startExecuting();
        }
        
        @Override
        public void resetTask() {
            super.resetTask();
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.field_179500_c.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, 10.0f, (float)this.field_179500_c.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                final World world = this.field_179500_c.worldObj;
                final BlockPos blockpos = this.destinationBlock.up();
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (this.field_179499_e && block instanceof BlockCarrot && iblockstate.getValue((IProperty<Integer>)BlockCarrot.AGE) == 7) {
                    world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                    world.destroyBlock(blockpos, true);
                    this.field_179500_c.createEatingParticles();
                }
                this.field_179499_e = false;
                this.runDelay = 10;
            }
        }
        
        @Override
        protected boolean shouldMoveTo(final World worldIn, BlockPos pos) {
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block == Blocks.farmland) {
                pos = pos.up();
                final IBlockState iblockstate = worldIn.getBlockState(pos);
                block = iblockstate.getBlock();
                if (block instanceof BlockCarrot && iblockstate.getValue((IProperty<Integer>)BlockCarrot.AGE) == 7 && this.field_179498_d && !this.field_179499_e) {
                    return this.field_179499_e = true;
                }
            }
            return false;
        }
    }
    
    public class RabbitJumpHelper extends EntityJumpHelper
    {
        private EntityRabbit theEntity;
        private boolean field_180068_d;
        
        public RabbitJumpHelper(final EntityRabbit rabbit) {
            super(rabbit);
            this.field_180068_d = false;
            this.theEntity = rabbit;
        }
        
        public boolean getIsJumping() {
            return this.isJumping;
        }
        
        public boolean func_180065_d() {
            return this.field_180068_d;
        }
        
        public void func_180066_a(final boolean p_180066_1_) {
            this.field_180068_d = p_180066_1_;
        }
        
        @Override
        public void doJump() {
            if (this.isJumping) {
                this.theEntity.doMovementAction(EnumMoveType.STEP);
                this.isJumping = false;
            }
        }
    }
    
    static class RabbitMoveHelper extends EntityMoveHelper
    {
        private EntityRabbit theEntity;
        
        public RabbitMoveHelper(final EntityRabbit p_i45862_1_) {
            super(p_i45862_1_);
            this.theEntity = p_i45862_1_;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.theEntity.onGround && !this.theEntity.func_175523_cj()) {
                this.theEntity.setMovementSpeed(0.0);
            }
            super.onUpdateMoveHelper();
        }
    }
    
    public static class RabbitTypeData implements IEntityLivingData
    {
        public int typeData;
        
        public RabbitTypeData(final int type) {
            this.typeData = type;
        }
    }
}
