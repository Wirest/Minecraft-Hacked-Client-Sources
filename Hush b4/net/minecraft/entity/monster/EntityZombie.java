// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.Calendar;
import java.util.List;
import net.minecraft.init.Blocks;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.IAttribute;

public class EntityZombie extends EntityMob
{
    protected static final IAttribute reinforcementChance;
    private static final UUID babySpeedBoostUUID;
    private static final AttributeModifier babySpeedBoostModifier;
    private final EntityAIBreakDoor breakDoor;
    private int conversionTime;
    private boolean isBreakDoorsTaskSet;
    private float zombieWidth;
    private float zombieHeight;
    
    static {
        reinforcementChance = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).setDescription("Spawn Reinforcements Chance");
        babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
        babySpeedBoostModifier = new AttributeModifier(EntityZombie.babySpeedBoostUUID, "Baby speed boost", 0.5, 1);
    }
    
    public EntityZombie(final World worldIn) {
        super(worldIn);
        this.breakDoor = new EntityAIBreakDoor(this);
        this.isBreakDoorsTaskSet = false;
        this.zombieWidth = -1.0f;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
        this.setSize(0.6f, 1.95f);
    }
    
    protected void applyEntityAI() {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
        this.getAttributeMap().registerAttribute(EntityZombie.reinforcementChance).setBaseValue(this.rand.nextDouble() * 0.10000000149011612);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(12, (Byte)0);
        this.getDataWatcher().addObject(13, (Byte)0);
        this.getDataWatcher().addObject(14, (Byte)0);
    }
    
    @Override
    public int getTotalArmorValue() {
        int i = super.getTotalArmorValue() + 2;
        if (i > 20) {
            i = 20;
        }
        return i;
    }
    
    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }
    
    public void setBreakDoorsAItask(final boolean par1) {
        if (this.isBreakDoorsTaskSet != par1) {
            this.isBreakDoorsTaskSet = par1;
            if (par1) {
                this.tasks.addTask(1, this.breakDoor);
            }
            else {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }
    
    @Override
    public boolean isChild() {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (this.isChild()) {
            this.experienceValue *= (int)2.5f;
        }
        return super.getExperiencePoints(player);
    }
    
    public void setChild(final boolean childZombie) {
        this.getDataWatcher().updateObject(12, (byte)(childZombie ? 1 : 0));
        if (this.worldObj != null && !this.worldObj.isRemote) {
            final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(EntityZombie.babySpeedBoostModifier);
            if (childZombie) {
                iattributeinstance.applyModifier(EntityZombie.babySpeedBoostModifier);
            }
        }
        this.setChildSize(childZombie);
    }
    
    public boolean isVillager() {
        return this.getDataWatcher().getWatchableObjectByte(13) == 1;
    }
    
    public void setVillager(final boolean villager) {
        this.getDataWatcher().updateObject(13, (byte)(villager ? 1 : 0));
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
            final float f = this.getBrightness(1.0f);
            final BlockPos blockpos = new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.worldObj.canSeeSky(blockpos)) {
                boolean flag = true;
                final ItemStack itemstack = this.getEquipmentInSlot(4);
                if (itemstack != null) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setFire(8);
                }
            }
        }
        if (this.isRiding() && this.getAttackTarget() != null && this.ridingEntity instanceof EntityChicken) {
            ((EntityLiving)this.ridingEntity).getNavigator().setPath(this.getNavigator().getPath(), 1.5);
        }
        super.onLivingUpdate();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (super.attackEntityFrom(source, amount)) {
            EntityLivingBase entitylivingbase = this.getAttackTarget();
            if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase) {
                entitylivingbase = (EntityLivingBase)source.getEntity();
            }
            if (entitylivingbase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < this.getEntityAttribute(EntityZombie.reinforcementChance).getAttributeValue()) {
                final int i = MathHelper.floor_double(this.posX);
                final int j = MathHelper.floor_double(this.posY);
                final int k = MathHelper.floor_double(this.posZ);
                final EntityZombie entityzombie = new EntityZombie(this.worldObj);
                for (int l = 0; l < 50; ++l) {
                    final int i2 = i + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    final int j2 = j + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    final int k2 = k + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
                    if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(i2, j2 - 1, k2)) && this.worldObj.getLightFromNeighbors(new BlockPos(i2, j2, k2)) < 10) {
                        entityzombie.setPosition(i2, j2, k2);
                        if (!this.worldObj.isAnyPlayerWithinRangeAt(i2, j2, k2, 7.0) && this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.worldObj.getCollidingBoundingBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox())) {
                            this.worldObj.spawnEntityInWorld(entityzombie);
                            entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
                            this.getEntityAttribute(EntityZombie.reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806, 0));
                            entityzombie.getEntityAttribute(EntityZombie.reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806, 0));
                            break;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && this.isConverting()) {
            final int i = this.getConversionTimeBoost();
            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.convertToVillager();
            }
        }
        super.onUpdate();
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean flag = super.attackEntityAsMob(entityIn);
        if (flag) {
            final int i = this.worldObj.getDifficulty().getDifficultyId();
            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < i * 0.3f) {
                entityIn.setFire(2 * i);
            }
        }
        return flag;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.zombie.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.zombie.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.rotten_flesh;
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    protected void addRandomDrop() {
        switch (this.rand.nextInt(3)) {
            case 0: {
                this.dropItem(Items.iron_ingot, 1);
                break;
            }
            case 1: {
                this.dropItem(Items.carrot, 1);
                break;
            }
            case 2: {
                this.dropItem(Items.potato, 1);
                break;
            }
        }
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (this.rand.nextFloat() < ((this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.05f : 0.01f)) {
            final int i = this.rand.nextInt(3);
            if (i == 0) {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
            }
            else {
                this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.isChild()) {
            tagCompound.setBoolean("IsBaby", true);
        }
        if (this.isVillager()) {
            tagCompound.setBoolean("IsVillager", true);
        }
        tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        tagCompound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.getBoolean("IsBaby")) {
            this.setChild(true);
        }
        if (tagCompund.getBoolean("IsVillager")) {
            this.setVillager(true);
        }
        if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1) {
            this.startConversion(tagCompund.getInteger("ConversionTime"));
        }
        this.setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }
            final EntityLiving entityliving = (EntityLiving)entityLivingIn;
            final EntityZombie entityzombie = new EntityZombie(this.worldObj);
            entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
            this.worldObj.removeEntity(entityLivingIn);
            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
            entityzombie.setVillager(true);
            if (entityLivingIn.isChild()) {
                entityzombie.setChild(true);
            }
            entityzombie.setNoAI(entityliving.isAIDisabled());
            if (entityliving.hasCustomName()) {
                entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
                entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entityzombie);
            this.worldObj.playAuxSFXAtEntity(null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        }
    }
    
    @Override
    public float getEyeHeight() {
        float f = 1.74f;
        if (this.isChild()) {
            f -= (float)0.81;
        }
        return f;
    }
    
    @Override
    protected boolean func_175448_a(final ItemStack stack) {
        return (stack.getItem() != Items.egg || !this.isChild() || !this.isRiding()) && super.func_175448_a(stack);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        final float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * f);
        if (livingdata == null) {
            livingdata = new GroupData(this.worldObj.rand.nextFloat() < 0.05f, this.worldObj.rand.nextFloat() < 0.05f, (GroupData)null);
        }
        if (livingdata instanceof GroupData) {
            final GroupData entityzombie$groupdata = (GroupData)livingdata;
            if (entityzombie$groupdata.isVillager) {
                this.setVillager(true);
            }
            if (entityzombie$groupdata.isChild) {
                this.setChild(true);
                if (this.worldObj.rand.nextFloat() < 0.05) {
                    final List<EntityChicken> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityChicken>)EntityChicken.class, this.getEntityBoundingBox().expand(5.0, 3.0, 5.0), (Predicate<? super EntityChicken>)EntitySelectors.IS_STANDALONE);
                    if (!list.isEmpty()) {
                        final EntityChicken entitychicken = list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.mountEntity(entitychicken);
                    }
                }
                else if (this.worldObj.rand.nextFloat() < 0.05) {
                    final EntityChicken entitychicken2 = new EntityChicken(this.worldObj);
                    entitychicken2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    entitychicken2.onInitialSpawn(difficulty, null);
                    entitychicken2.setChickenJockey(true);
                    this.worldObj.spawnEntityInWorld(entitychicken2);
                    this.mountEntity(entitychicken2);
                }
            }
        }
        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1f);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        if (this.getEquipmentInSlot(4) == null) {
            final Calendar calendar = this.worldObj.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
                this.setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1f) ? Blocks.lit_pumpkin : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0f;
            }
        }
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806, 0));
        final double d0 = this.rand.nextDouble() * 1.5 * f;
        if (d0 > 1.0) {
            this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }
        if (this.rand.nextFloat() < f * 0.05f) {
            this.getEntityAttribute(EntityZombie.reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
            this.setBreakDoorsAItask(true);
        }
        return livingdata;
    }
    
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.getCurrentEquippedItem();
        if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getMetadata() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness)) {
            if (!player.capabilities.isCreativeMode) {
                final ItemStack itemStack = itemstack;
                --itemStack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                this.startConversion(this.rand.nextInt(2401) + 3600);
            }
            return true;
        }
        return false;
    }
    
    protected void startConversion(final int ticks) {
        this.conversionTime = ticks;
        this.getDataWatcher().updateObject(14, (Byte)1);
        this.removePotionEffect(Potion.weakness.id);
        this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
        this.worldObj.setEntityState(this, (byte)16);
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.remedy", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isConverting();
    }
    
    public boolean isConverting() {
        return this.getDataWatcher().getWatchableObjectByte(14) == 1;
    }
    
    protected void convertToVillager() {
        final EntityVillager entityvillager = new EntityVillager(this.worldObj);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        entityvillager.setLookingForHome();
        if (this.isChild()) {
            entityvillager.setGrowingAge(-24000);
        }
        this.worldObj.removeEntity(this);
        entityvillager.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }
        this.worldObj.spawnEntityInWorld(entityvillager);
        entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        this.worldObj.playAuxSFXAtEntity(null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }
    
    protected int getConversionTimeBoost() {
        int i = 1;
        if (this.rand.nextFloat() < 0.01f) {
            int j = 0;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; ++k) {
                for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; ++l) {
                    for (int i2 = (int)this.posZ - 4; i2 < (int)this.posZ + 4 && j < 14; ++i2) {
                        final Block block = this.worldObj.getBlockState(blockpos$mutableblockpos.func_181079_c(k, l, i2)).getBlock();
                        if (block == Blocks.iron_bars || block == Blocks.bed) {
                            if (this.rand.nextFloat() < 0.3f) {
                                ++i;
                            }
                            ++j;
                        }
                    }
                }
            }
        }
        return i;
    }
    
    public void setChildSize(final boolean isChild) {
        this.multiplySize(isChild ? 0.5f : 1.0f);
    }
    
    @Override
    protected final void setSize(final float width, final float height) {
        final boolean flag = this.zombieWidth > 0.0f && this.zombieHeight > 0.0f;
        this.zombieWidth = width;
        this.zombieHeight = height;
        if (!flag) {
            this.multiplySize(1.0f);
        }
    }
    
    protected final void multiplySize(final float size) {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }
    
    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.35;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
            ((EntityCreeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0f);
        }
    }
    
    class GroupData implements IEntityLivingData
    {
        public boolean isChild;
        public boolean isVillager;
        
        private GroupData(final boolean isBaby, final boolean isVillagerZombie) {
            this.isChild = false;
            this.isVillager = false;
            this.isChild = isBaby;
            this.isVillager = isVillagerZombie;
        }
    }
}
