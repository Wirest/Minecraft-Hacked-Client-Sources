// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.DamageSource;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityLivingBase;

public class EntityGuardian extends EntityMob
{
    private float field_175482_b;
    private float field_175484_c;
    private float field_175483_bk;
    private float field_175485_bl;
    private float field_175486_bm;
    private EntityLivingBase targetedEntity;
    private int field_175479_bo;
    private boolean field_175480_bp;
    private EntityAIWander wander;
    
    public EntityGuardian(final World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.tasks.addTask(4, new AIGuardianAttack(this));
        final EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
        this.tasks.addTask(5, entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, this.wander = new EntityAIWander(this, 1.0, 80));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
        this.moveHelper = new GuardianMoveHelper(this);
        final float nextFloat = this.rand.nextFloat();
        this.field_175482_b = nextFloat;
        this.field_175484_c = nextFloat;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setElder(tagCompund.getBoolean("Elder"));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Elder", this.isElder());
    }
    
    @Override
    protected PathNavigate getNewNavigator(final World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, 0);
    }
    
    private boolean isSyncedFlagSet(final int flagId) {
        return (this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0x0;
    }
    
    private void setSyncedFlag(final int flagId, final boolean state) {
        final int i = this.dataWatcher.getWatchableObjectInt(16);
        if (state) {
            this.dataWatcher.updateObject(16, i | flagId);
        }
        else {
            this.dataWatcher.updateObject(16, i & ~flagId);
        }
    }
    
    public boolean func_175472_n() {
        return this.isSyncedFlagSet(2);
    }
    
    private void func_175476_l(final boolean p_175476_1_) {
        this.setSyncedFlag(2, p_175476_1_);
    }
    
    public int func_175464_ck() {
        return this.isElder() ? 60 : 80;
    }
    
    public boolean isElder() {
        return this.isSyncedFlagSet(4);
    }
    
    public void setElder(final boolean elder) {
        this.setSyncedFlag(4, elder);
        if (elder) {
            this.setSize(1.9975f, 1.9975f);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0);
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0);
            this.enablePersistence();
            this.wander.setExecutionChance(400);
        }
    }
    
    public void setElder() {
        this.setElder(true);
        final float n = 1.0f;
        this.field_175485_bl = n;
        this.field_175486_bm = n;
    }
    
    private void setTargetedEntity(final int entityId) {
        this.dataWatcher.updateObject(17, entityId);
    }
    
    public boolean hasTargetedEntity() {
        return this.dataWatcher.getWatchableObjectInt(17) != 0;
    }
    
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        }
        if (!this.worldObj.isRemote) {
            return this.getAttackTarget();
        }
        if (this.targetedEntity != null) {
            return this.targetedEntity;
        }
        final Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
        if (entity instanceof EntityLivingBase) {
            return this.targetedEntity = (EntityLivingBase)entity;
        }
        return null;
    }
    
    @Override
    public void onDataWatcherUpdate(final int dataID) {
        super.onDataWatcherUpdate(dataID);
        if (dataID == 16) {
            if (this.isElder() && this.width < 1.0f) {
                this.setSize(1.9975f, 1.9975f);
            }
        }
        else if (dataID == 17) {
            this.field_175479_bo = 0;
            this.targetedEntity = null;
        }
    }
    
    @Override
    public int getTalkInterval() {
        return 160;
    }
    
    @Override
    protected String getLivingSound() {
        return this.isInWater() ? (this.isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle") : "mob.guardian.land.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return this.isInWater() ? (this.isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit") : "mob.guardian.land.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return this.isInWater() ? (this.isElder() ? "mob.guardian.elder.death" : "mob.guardian.death") : "mob.guardian.land.death";
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return (this.worldObj.getBlockState(pos).getBlock().getMaterial() == Material.water) ? (10.0f + this.worldObj.getLightBrightness(pos) - 0.5f) : super.getBlockPathWeight(pos);
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.field_175484_c = this.field_175482_b;
            if (!this.isInWater()) {
                this.field_175483_bk = 2.0f;
                if (this.motionY > 0.0 && this.field_175480_bp && !this.isSilent()) {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0f, 1.0f, false);
                }
                this.field_175480_bp = (this.motionY < 0.0 && this.worldObj.isBlockNormalCube(new BlockPos(this).down(), false));
            }
            else if (this.func_175472_n()) {
                if (this.field_175483_bk < 0.5f) {
                    this.field_175483_bk = 4.0f;
                }
                else {
                    this.field_175483_bk += (0.5f - this.field_175483_bk) * 0.1f;
                }
            }
            else {
                this.field_175483_bk += (0.125f - this.field_175483_bk) * 0.2f;
            }
            this.field_175482_b += this.field_175483_bk;
            this.field_175486_bm = this.field_175485_bl;
            if (!this.isInWater()) {
                this.field_175485_bl = this.rand.nextFloat();
            }
            else if (this.func_175472_n()) {
                this.field_175485_bl += (0.0f - this.field_175485_bl) * 0.25f;
            }
            else {
                this.field_175485_bl += (1.0f - this.field_175485_bl) * 0.06f;
            }
            if (this.func_175472_n() && this.isInWater()) {
                final Vec3 vec3 = this.getLook(0.0f);
                for (int i = 0; i < 2; ++i) {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * this.width - vec3.xCoord * 1.5, this.posY + this.rand.nextDouble() * this.height - vec3.yCoord * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * this.width - vec3.zCoord * 1.5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (this.hasTargetedEntity()) {
                if (this.field_175479_bo < this.func_175464_ck()) {
                    ++this.field_175479_bo;
                }
                final EntityLivingBase entitylivingbase = this.getTargetedEntity();
                if (entitylivingbase != null) {
                    this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    final double d5 = this.func_175477_p(0.0f);
                    double d6 = entitylivingbase.posX - this.posX;
                    double d7 = entitylivingbase.posY + entitylivingbase.height * 0.5f - (this.posY + this.getEyeHeight());
                    double d8 = entitylivingbase.posZ - this.posZ;
                    final double d9 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                    d6 /= d9;
                    d7 /= d9;
                    d8 /= d9;
                    double d10 = this.rand.nextDouble();
                    while (d10 < d9) {
                        d10 += 1.8 - d5 + this.rand.nextDouble() * (1.7 - d5);
                        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d6 * d10, this.posY + d7 * d10 + this.getEyeHeight(), this.posZ + d8 * d10, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        }
        else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.motionZ += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.hasTargetedEntity()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }
    
    public float func_175471_a(final float p_175471_1_) {
        return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
    }
    
    public float func_175469_o(final float p_175469_1_) {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
    }
    
    public float func_175477_p(final float p_175477_1_) {
        return (this.field_175479_bo + p_175477_1_) / this.func_175464_ck();
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.isElder()) {
            final int i = 1200;
            final int j = 1200;
            final int k = 6000;
            final int l = 2;
            if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
                final Potion potion = Potion.digSlowdown;
                for (final EntityPlayerMP entityplayermp : this.worldObj.getPlayers((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, (Predicate<? super EntityPlayerMP>)new Predicate<EntityPlayerMP>() {
                    @Override
                    public boolean apply(final EntityPlayerMP p_apply_1_) {
                        return EntityGuardian.this.getDistanceSqToEntity(p_apply_1_) < 2500.0 && p_apply_1_.theItemInWorldManager.survivalOrAdventure();
                    }
                })) {
                    if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
                        entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0f));
                        entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
                    }
                }
            }
            if (!this.hasHome()) {
                this.setHomePosAndDistance(new BlockPos(this), 16);
            }
        }
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final int i = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);
        if (i > 0) {
            this.entityDropItem(new ItemStack(Items.prismarine_shard, i, 0), 1.0f);
        }
        if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0f);
        }
        else if (this.rand.nextInt(3 + p_70628_2_) > 1) {
            this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0f);
        }
        if (p_70628_1_ && this.isElder()) {
            this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0f);
        }
    }
    
    @Override
    protected void addRandomDrop() {
        final ItemStack itemstack = WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j()).getItemStack(this.rand);
        this.entityDropItem(itemstack, 1.0f);
    }
    
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
    
    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (!this.func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
            if (!source.isExplosion()) {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0f);
                entitylivingbase.playSound("damage.thorns", 0.5f, 1.0f);
            }
        }
        this.wander.makeUpdate();
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }
    
    @Override
    public void moveEntityWithHeading(final float strafe, final float forward) {
        if (this.isServerWorld()) {
            if (this.isInWater()) {
                this.moveFlying(strafe, forward, 0.1f);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= 0.8999999761581421;
                this.motionY *= 0.8999999761581421;
                this.motionZ *= 0.8999999761581421;
                if (!this.func_175472_n() && this.getAttackTarget() == null) {
                    this.motionY -= 0.005;
                }
            }
            else {
                super.moveEntityWithHeading(strafe, forward);
            }
        }
        else {
            super.moveEntityWithHeading(strafe, forward);
        }
    }
    
    static class GuardianTargetSelector implements Predicate<EntityLivingBase>
    {
        private EntityGuardian parentEntity;
        
        public GuardianTargetSelector(final EntityGuardian p_i45832_1_) {
            this.parentEntity = p_i45832_1_;
        }
        
        @Override
        public boolean apply(final EntityLivingBase p_apply_1_) {
            return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSqToEntity(this.parentEntity) > 9.0;
        }
    }
    
    static class AIGuardianAttack extends EntityAIBase
    {
        private EntityGuardian theEntity;
        private int tickCounter;
        
        public AIGuardianAttack(final EntityGuardian p_i45833_1_) {
            this.theEntity = p_i45833_1_;
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }
        
        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.theEntity.isElder() || this.theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) > 9.0);
        }
        
        @Override
        public void startExecuting() {
            this.tickCounter = -10;
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 90.0f, 90.0f);
            this.theEntity.isAirBorne = true;
        }
        
        @Override
        public void resetTask() {
            this.theEntity.setTargetedEntity(0);
            this.theEntity.setAttackTarget(null);
            this.theEntity.wander.makeUpdate();
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
            if (!this.theEntity.canEntityBeSeen(entitylivingbase)) {
                this.theEntity.setAttackTarget(null);
            }
            else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
                    this.theEntity.worldObj.setEntityState(this.theEntity, (byte)21);
                }
                else if (this.tickCounter >= this.theEntity.func_175464_ck()) {
                    float f = 1.0f;
                    if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0f;
                    }
                    if (this.theEntity.isElder()) {
                        f += 2.0f;
                    }
                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.theEntity, this.theEntity), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
                    this.theEntity.setAttackTarget(null);
                }
                else if (this.tickCounter < 60 || this.tickCounter % 20 == 0) {}
                super.updateTask();
            }
        }
    }
    
    static class GuardianMoveHelper extends EntityMoveHelper
    {
        private EntityGuardian entityGuardian;
        
        public GuardianMoveHelper(final EntityGuardian p_i45831_1_) {
            super(p_i45831_1_);
            this.entityGuardian = p_i45831_1_;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.update && !this.entityGuardian.getNavigator().noPath()) {
                final double d0 = this.posX - this.entityGuardian.posX;
                double d2 = this.posY - this.entityGuardian.posY;
                final double d3 = this.posZ - this.entityGuardian.posZ;
                double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                d4 = MathHelper.sqrt_double(d4);
                d2 /= d4;
                final float f = (float)(MathHelper.func_181159_b(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
                this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 30.0f);
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
                final float f2 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f2 - this.entityGuardian.getAIMoveSpeed()) * 0.125f);
                double d5 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5) * 0.05;
                final double d6 = Math.cos(this.entityGuardian.rotationYaw * 3.1415927f / 180.0f);
                final double d7 = Math.sin(this.entityGuardian.rotationYaw * 3.1415927f / 180.0f);
                final EntityGuardian entityGuardian = this.entityGuardian;
                entityGuardian.motionX += d5 * d6;
                final EntityGuardian entityGuardian2 = this.entityGuardian;
                entityGuardian2.motionZ += d5 * d7;
                d5 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75) * 0.05;
                final EntityGuardian entityGuardian3 = this.entityGuardian;
                entityGuardian3.motionY += d5 * (d7 + d6) * 0.25;
                final EntityGuardian entityGuardian4 = this.entityGuardian;
                entityGuardian4.motionY += this.entityGuardian.getAIMoveSpeed() * d2 * 0.1;
                final EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
                final double d8 = this.entityGuardian.posX + d0 / d4 * 2.0;
                final double d9 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d2 / d4 * 1.0;
                final double d10 = this.entityGuardian.posZ + d3 / d4 * 2.0;
                double d11 = entitylookhelper.getLookPosX();
                double d12 = entitylookhelper.getLookPosY();
                double d13 = entitylookhelper.getLookPosZ();
                if (!entitylookhelper.getIsLooking()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }
                this.entityGuardian.getLookHelper().setLookPosition(d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, d13 + (d10 - d13) * 0.125, 10.0f, 40.0f);
                this.entityGuardian.func_175476_l(true);
            }
            else {
                this.entityGuardian.setAIMoveSpeed(0.0f);
                this.entityGuardian.func_175476_l(false);
            }
        }
    }
}
