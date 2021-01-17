// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.entity.monster.EntityMob;
import optifine.BlockPosM;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.monster.IMob;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.MathHelper;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.Item;
import optifine.Config;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityGhast;
import optifine.Reflector;
import net.minecraft.pathfinding.PathNavigateGround;
import java.util.UUID;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityLookHelper;

public abstract class EntityLiving extends EntityLivingBase
{
    public int livingSoundTime;
    protected int experienceValue;
    private EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;
    protected EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;
    private EntityLivingBase attackTarget;
    private EntitySenses senses;
    private ItemStack[] equipment;
    protected float[] equipmentDropChances;
    private boolean canPickUpLoot;
    private boolean persistenceRequired;
    private boolean isLeashed;
    private Entity leashedToEntity;
    private NBTTagCompound leashNBTTag;
    private static final String __OBFID = "CL_00001550";
    public int randomMobsId;
    public BiomeGenBase spawnBiome;
    public BlockPos spawnPosition;
    
    public EntityLiving(final World worldIn) {
        super(worldIn);
        this.equipment = new ItemStack[5];
        this.equipmentDropChances = new float[5];
        this.randomMobsId = 0;
        this.spawnBiome = null;
        this.spawnPosition = null;
        this.tasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
        this.targetTasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = new EntityBodyHelper(this);
        this.navigator = this.getNewNavigator(worldIn);
        this.senses = new EntitySenses(this);
        for (int i = 0; i < this.equipmentDropChances.length; ++i) {
            this.equipmentDropChances[i] = 0.085f;
        }
        final UUID uuid = this.getUniqueID();
        final long j = uuid.getLeastSignificantBits();
        this.randomMobsId = (int)(j & 0x7FFFFFFFL);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
    }
    
    protected PathNavigate getNewNavigator(final World worldIn) {
        return new PathNavigateGround(this, worldIn);
    }
    
    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }
    
    public EntityMoveHelper getMoveHelper() {
        return this.moveHelper;
    }
    
    public EntityJumpHelper getJumpHelper() {
        return this.jumpHelper;
    }
    
    public PathNavigate getNavigator() {
        return this.navigator;
    }
    
    public EntitySenses getEntitySenses() {
        return this.senses;
    }
    
    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }
    
    public void setAttackTarget(final EntityLivingBase entitylivingbaseIn) {
        this.attackTarget = entitylivingbaseIn;
        Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, this, entitylivingbaseIn);
    }
    
    public boolean canAttackClass(final Class cls) {
        return cls != EntityGhast.class;
    }
    
    public void eatGrassBonus() {
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(15, (Byte)0);
    }
    
    public int getTalkInterval() {
        return 80;
    }
    
    public void playLivingSound() {
        final String s = this.getLivingSound();
        if (s != null) {
            this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("mobBaseTick");
        if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playLivingSound();
        }
        this.worldObj.theProfiler.endSection();
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (this.experienceValue > 0) {
            int i = this.experienceValue;
            final ItemStack[] aitemstack = this.getInventory();
            for (int j = 0; j < aitemstack.length; ++j) {
                if (aitemstack[j] != null && this.equipmentDropChances[j] <= 1.0f) {
                    i += 1 + this.rand.nextInt(3);
                }
            }
            return i;
        }
        return this.experienceValue;
    }
    
    public void spawnExplosionParticle() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 20; ++i) {
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                final double d4 = 10.0;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width - d0 * d4, this.posY + this.rand.nextFloat() * this.height - d2 * d4, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width - d3 * d4, d0, d2, d3, new int[0]);
            }
        }
        else {
            this.worldObj.setEntityState(this, (byte)20);
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    public void onUpdate() {
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        }
        else {
            super.onUpdate();
            if (!this.worldObj.isRemote) {
                this.updateLeashedState();
            }
        }
    }
    
    @Override
    protected float func_110146_f(final float p_110146_1_, final float p_110146_2_) {
        this.bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }
    
    protected String getLivingSound() {
        return null;
    }
    
    protected Item getDropItem() {
        return null;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final Item item = this.getDropItem();
        if (item != null) {
            int i = this.rand.nextInt(3);
            if (p_70628_2_ > 0) {
                i += this.rand.nextInt(p_70628_2_ + 1);
            }
            for (int j = 0; j < i; ++j) {
                this.dropItem(item, 1);
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        tagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.equipment.length; ++i) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            if (this.equipment[i] != null) {
                this.equipment[i].writeToNBT(nbttagcompound);
            }
            nbttaglist.appendTag(nbttagcompound);
        }
        tagCompound.setTag("Equipment", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (int j = 0; j < this.equipmentDropChances.length; ++j) {
            nbttaglist2.appendTag(new NBTTagFloat(this.equipmentDropChances[j]));
        }
        tagCompound.setTag("DropChances", nbttaglist2);
        tagCompound.setBoolean("Leashed", this.isLeashed);
        if (this.leashedToEntity != null) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            if (this.leashedToEntity instanceof EntityLivingBase) {
                nbttagcompound2.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
                nbttagcompound2.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
            }
            else if (this.leashedToEntity instanceof EntityHanging) {
                final BlockPos blockpos = ((EntityHanging)this.leashedToEntity).getHangingPosition();
                nbttagcompound2.setInteger("X", blockpos.getX());
                nbttagcompound2.setInteger("Y", blockpos.getY());
                nbttagcompound2.setInteger("Z", blockpos.getZ());
            }
            tagCompound.setTag("Leash", nbttagcompound2);
        }
        if (this.isAIDisabled()) {
            tagCompound.setBoolean("NoAI", this.isAIDisabled());
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(tagCompund.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = tagCompund.getBoolean("PersistenceRequired");
        if (tagCompund.hasKey("Equipment", 9)) {
            final NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
            for (int i = 0; i < this.equipment.length; ++i) {
                this.equipment[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
            }
        }
        if (tagCompund.hasKey("DropChances", 9)) {
            final NBTTagList nbttaglist2 = tagCompund.getTagList("DropChances", 5);
            for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
                this.equipmentDropChances[j] = nbttaglist2.getFloatAt(j);
            }
        }
        this.isLeashed = tagCompund.getBoolean("Leashed");
        if (this.isLeashed && tagCompund.hasKey("Leash", 10)) {
            this.leashNBTTag = tagCompund.getCompoundTag("Leash");
        }
        this.setNoAI(tagCompund.getBoolean("NoAI"));
    }
    
    public void setMoveForward(final float p_70657_1_) {
        this.moveForward = p_70657_1_;
    }
    
    @Override
    public void setAIMoveSpeed(final float speedIn) {
        super.setAIMoveSpeed(speedIn);
        this.setMoveForward(speedIn);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.worldObj.theProfiler.startSection("looting");
        if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
            for (final EntityItem entityitem : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, this.getEntityBoundingBox().expand(1.0, 0.0, 1.0))) {
                if (!entityitem.isDead && entityitem.getEntityItem() != null && !entityitem.cannotPickup()) {
                    this.updateEquipmentIfNeeded(entityitem);
                }
            }
        }
        this.worldObj.theProfiler.endSection();
    }
    
    protected void updateEquipmentIfNeeded(final EntityItem itemEntity) {
        final ItemStack itemstack = itemEntity.getEntityItem();
        final int i = getArmorPosition(itemstack);
        if (i > -1) {
            boolean flag = true;
            final ItemStack itemstack2 = this.getEquipmentInSlot(i);
            if (itemstack2 != null) {
                if (i == 0) {
                    if (itemstack.getItem() instanceof ItemSword && !(itemstack2.getItem() instanceof ItemSword)) {
                        flag = true;
                    }
                    else if (itemstack.getItem() instanceof ItemSword && itemstack2.getItem() instanceof ItemSword) {
                        final ItemSword itemsword = (ItemSword)itemstack.getItem();
                        final ItemSword itemsword2 = (ItemSword)itemstack2.getItem();
                        if (itemsword.getDamageVsEntity() != itemsword2.getDamageVsEntity()) {
                            flag = (itemsword.getDamageVsEntity() > itemsword2.getDamageVsEntity());
                        }
                        else {
                            flag = (itemstack.getMetadata() > itemstack2.getMetadata() || (itemstack.hasTagCompound() && !itemstack2.hasTagCompound()));
                        }
                    }
                    else {
                        flag = (itemstack.getItem() instanceof ItemBow && itemstack2.getItem() instanceof ItemBow && itemstack.hasTagCompound() && !itemstack2.hasTagCompound());
                    }
                }
                else if (itemstack.getItem() instanceof ItemArmor && !(itemstack2.getItem() instanceof ItemArmor)) {
                    flag = true;
                }
                else if (itemstack.getItem() instanceof ItemArmor && itemstack2.getItem() instanceof ItemArmor) {
                    final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                    final ItemArmor itemarmor2 = (ItemArmor)itemstack2.getItem();
                    if (itemarmor.damageReduceAmount != itemarmor2.damageReduceAmount) {
                        flag = (itemarmor.damageReduceAmount > itemarmor2.damageReduceAmount);
                    }
                    else {
                        flag = (itemstack.getMetadata() > itemstack2.getMetadata() || (itemstack.hasTagCompound() && !itemstack2.hasTagCompound()));
                    }
                }
                else {
                    flag = false;
                }
            }
            if (flag && this.func_175448_a(itemstack)) {
                if (itemstack2 != null && this.rand.nextFloat() - 0.1f < this.equipmentDropChances[i]) {
                    this.entityDropItem(itemstack2, 0.0f);
                }
                if (itemstack.getItem() == Items.diamond && itemEntity.getThrower() != null) {
                    final EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(itemEntity.getThrower());
                    if (entityplayer != null) {
                        entityplayer.triggerAchievement(AchievementList.diamondsToYou);
                    }
                }
                this.setCurrentItemOrArmor(i, itemstack);
                this.equipmentDropChances[i] = 2.0f;
                this.persistenceRequired = true;
                this.onItemPickup(itemEntity, 1);
                itemEntity.setDead();
            }
        }
    }
    
    protected boolean func_175448_a(final ItemStack stack) {
        return true;
    }
    
    protected boolean canDespawn() {
        return true;
    }
    
    protected void despawnEntity() {
        Object object = null;
        final Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
        final Object object3 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
        if (this.persistenceRequired) {
            this.entityAge = 0;
        }
        else if ((this.entityAge & 0x1F) == 0x1F && (object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, this)) != object2) {
            if (object == object3) {
                this.entityAge = 0;
            }
            else {
                this.setDead();
            }
        }
        else {
            final EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0);
            if (entityplayer != null) {
                final double d0 = entityplayer.posX - this.posX;
                final double d2 = entityplayer.posY - this.posY;
                final double d3 = entityplayer.posZ - this.posZ;
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (this.canDespawn() && d4 > 16384.0) {
                    this.setDead();
                }
                if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d4 > 1024.0 && this.canDespawn()) {
                    this.setDead();
                }
                else if (d4 < 1024.0) {
                    this.entityAge = 0;
                }
            }
        }
    }
    
    @Override
    protected final void updateEntityActionState() {
        ++this.entityAge;
        this.worldObj.theProfiler.startSection("checkDespawn");
        this.despawnEntity();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("sensing");
        this.senses.clearSensingCache();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("mob tick");
        this.updateAITasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("controls");
        this.worldObj.theProfiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        this.worldObj.theProfiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        this.worldObj.theProfiler.endStartSection("jump");
        this.jumpHelper.doJump();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.endSection();
    }
    
    protected void updateAITasks() {
    }
    
    public int getVerticalFaceSpeed() {
        return 40;
    }
    
    public void faceEntity(final Entity entityIn, final float p_70625_2_, final float p_70625_3_) {
        final double d0 = entityIn.posX - this.posX;
        final double d2 = entityIn.posZ - this.posZ;
        double d3;
        if (entityIn instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            d3 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (this.posY + this.getEyeHeight());
        }
        else {
            d3 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
        }
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(MathHelper.func_181159_b(d3, d4) * 180.0 / 3.141592653589793));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f2, p_70625_3_);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f, p_70625_2_);
    }
    
    private float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float f = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (f > p_70663_3_) {
            f = p_70663_3_;
        }
        if (f < -p_70663_3_) {
            f = -p_70663_3_;
        }
        return p_70663_1_ + f;
    }
    
    public boolean getCanSpawnHere() {
        return true;
    }
    
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }
    
    public float getRenderSizeModifier() {
        return 1.0f;
    }
    
    public int getMaxSpawnedInChunk() {
        return 4;
    }
    
    @Override
    public int getMaxFallHeight() {
        if (this.getAttackTarget() == null) {
            return 3;
        }
        int i = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        i -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;
        if (i < 0) {
            i = 0;
        }
        return i + 3;
    }
    
    @Override
    public ItemStack getHeldItem() {
        return this.equipment[0];
    }
    
    @Override
    public ItemStack getEquipmentInSlot(final int slotIn) {
        return this.equipment[slotIn];
    }
    
    @Override
    public ItemStack getCurrentArmor(final int slotIn) {
        return this.equipment[slotIn + 1];
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
        this.equipment[slotIn] = stack;
    }
    
    @Override
    public ItemStack[] getInventory() {
        return this.equipment;
    }
    
    @Override
    protected void dropEquipment(final boolean p_82160_1_, final int p_82160_2_) {
        for (int i = 0; i < this.getInventory().length; ++i) {
            final ItemStack itemstack = this.getEquipmentInSlot(i);
            final boolean flag = this.equipmentDropChances[i] > 1.0f;
            if (itemstack != null && (p_82160_1_ || flag) && this.rand.nextFloat() - p_82160_2_ * 0.01f < this.equipmentDropChances[i]) {
                if (!flag && itemstack.isItemStackDamageable()) {
                    final int j = Math.max(itemstack.getMaxDamage() - 25, 1);
                    int k = itemstack.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(j) + 1);
                    if (k > j) {
                        k = j;
                    }
                    if (k < 1) {
                        k = 1;
                    }
                    itemstack.setItemDamage(k);
                }
                this.entityDropItem(itemstack, 0.0f);
            }
        }
    }
    
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        if (this.rand.nextFloat() < 0.15f * difficulty.getClampedAdditionalDifficulty()) {
            int i = this.rand.nextInt(2);
            final float f = (this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.1f : 0.25f;
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.rand.nextFloat() < 0.095f) {
                ++i;
            }
            for (int j = 3; j >= 0; --j) {
                final ItemStack itemstack = this.getCurrentArmor(j);
                if (j < 3 && this.rand.nextFloat() < f) {
                    break;
                }
                if (itemstack == null) {
                    final Item item = getArmorItemForSlot(j + 1, i);
                    if (item != null) {
                        this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }
    
    public static int getArmorPosition(final ItemStack stack) {
        if (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull) {
            if (stack.getItem() instanceof ItemArmor) {
                switch (((ItemArmor)stack.getItem()).armorType) {
                    case 0: {
                        return 4;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 2;
                    }
                    case 3: {
                        return 1;
                    }
                }
            }
            return 0;
        }
        return 4;
    }
    
    public static Item getArmorItemForSlot(final int armorSlot, final int itemTier) {
        switch (armorSlot) {
            case 4: {
                if (itemTier == 0) {
                    return Items.leather_helmet;
                }
                if (itemTier == 1) {
                    return Items.golden_helmet;
                }
                if (itemTier == 2) {
                    return Items.chainmail_helmet;
                }
                if (itemTier == 3) {
                    return Items.iron_helmet;
                }
                if (itemTier == 4) {
                    return Items.diamond_helmet;
                }
            }
            case 3: {
                if (itemTier == 0) {
                    return Items.leather_chestplate;
                }
                if (itemTier == 1) {
                    return Items.golden_chestplate;
                }
                if (itemTier == 2) {
                    return Items.chainmail_chestplate;
                }
                if (itemTier == 3) {
                    return Items.iron_chestplate;
                }
                if (itemTier == 4) {
                    return Items.diamond_chestplate;
                }
            }
            case 2: {
                if (itemTier == 0) {
                    return Items.leather_leggings;
                }
                if (itemTier == 1) {
                    return Items.golden_leggings;
                }
                if (itemTier == 2) {
                    return Items.chainmail_leggings;
                }
                if (itemTier == 3) {
                    return Items.iron_leggings;
                }
                if (itemTier == 4) {
                    return Items.diamond_leggings;
                }
            }
            case 1: {
                if (itemTier == 0) {
                    return Items.leather_boots;
                }
                if (itemTier == 1) {
                    return Items.golden_boots;
                }
                if (itemTier == 2) {
                    return Items.chainmail_boots;
                }
                if (itemTier == 3) {
                    return Items.iron_boots;
                }
                if (itemTier == 4) {
                    return Items.diamond_boots;
                }
                break;
            }
        }
        return null;
    }
    
    protected void setEnchantmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        final float f = difficulty.getClampedAdditionalDifficulty();
        if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25f * f) {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0f + f * this.rand.nextInt(18)));
        }
        for (int i = 0; i < 4; ++i) {
            final ItemStack itemstack = this.getCurrentArmor(i);
            if (itemstack != null && this.rand.nextFloat() < 0.5f * f) {
                EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0f + f * this.rand.nextInt(18)));
            }
        }
    }
    
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, final IEntityLivingData livingdata) {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05, 1));
        return livingdata;
    }
    
    public boolean canBeSteered() {
        return false;
    }
    
    public void enablePersistence() {
        this.persistenceRequired = true;
    }
    
    public void setEquipmentDropChance(final int slotIn, final float chance) {
        this.equipmentDropChances[slotIn] = chance;
    }
    
    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }
    
    public void setCanPickUpLoot(final boolean canPickup) {
        this.canPickUpLoot = canPickup;
    }
    
    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }
    
    @Override
    public final boolean interactFirst(final EntityPlayer playerIn) {
        if (this.getLeashed() && this.getLeashedToEntity() == playerIn) {
            this.clearLeashed(true, !playerIn.capabilities.isCreativeMode);
            return true;
        }
        final ItemStack itemstack = playerIn.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.lead && this.allowLeashing()) {
            if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed()) {
                this.setLeashedToEntity(playerIn, true);
                final ItemStack itemStack = itemstack;
                --itemStack.stackSize;
                return true;
            }
            if (((EntityTameable)this).isOwner(playerIn)) {
                this.setLeashedToEntity(playerIn, true);
                final ItemStack itemStack2 = itemstack;
                --itemStack2.stackSize;
                return true;
            }
        }
        return this.interact(playerIn) || super.interactFirst(playerIn);
    }
    
    protected boolean interact(final EntityPlayer player) {
        return false;
    }
    
    protected void updateLeashedState() {
        if (this.leashNBTTag != null) {
            this.recreateLeash();
        }
        if (this.isLeashed) {
            if (!this.isEntityAlive()) {
                this.clearLeashed(true, true);
            }
            if (this.leashedToEntity == null || this.leashedToEntity.isDead) {
                this.clearLeashed(true, true);
            }
        }
    }
    
    public void clearLeashed(final boolean sendPacket, final boolean dropLead) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashedToEntity = null;
            if (!this.worldObj.isRemote && dropLead) {
                this.dropItem(Items.lead, 1);
            }
            if (!this.worldObj.isRemote && sendPacket && this.worldObj instanceof WorldServer) {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, null));
            }
        }
    }
    
    public boolean allowLeashing() {
        return !this.getLeashed() && !(this instanceof IMob);
    }
    
    public boolean getLeashed() {
        return this.isLeashed;
    }
    
    public Entity getLeashedToEntity() {
        return this.leashedToEntity;
    }
    
    public void setLeashedToEntity(final Entity entityIn, final boolean sendAttachNotification) {
        this.isLeashed = true;
        this.leashedToEntity = entityIn;
        if (!this.worldObj.isRemote && sendAttachNotification && this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, this.leashedToEntity));
        }
    }
    
    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4)) {
                final UUID uuid = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
                for (final EntityLivingBase entitylivingbase : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().expand(10.0, 10.0, 10.0))) {
                    if (entitylivingbase.getUniqueID().equals(uuid)) {
                        this.leashedToEntity = entitylivingbase;
                        break;
                    }
                }
            }
            else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                final BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.worldObj, blockpos);
                if (entityleashknot == null) {
                    entityleashknot = EntityLeashKnot.createKnot(this.worldObj, blockpos);
                }
                this.leashedToEntity = entityleashknot;
            }
            else {
                this.clearLeashed(false, true);
            }
        }
        this.leashNBTTag = null;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        int i;
        if (inventorySlot == 99) {
            i = 0;
        }
        else {
            i = inventorySlot - 100 + 1;
            if (i < 0 || i >= this.equipment.length) {
                return false;
            }
        }
        if (itemStackIn == null || getArmorPosition(itemStackIn) == i || (i == 4 && itemStackIn.getItem() instanceof ItemBlock)) {
            this.setCurrentItemOrArmor(i, itemStackIn);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }
    
    public void setNoAI(final boolean disable) {
        this.dataWatcher.updateObject(15, (byte)(disable ? 1 : 0));
    }
    
    public boolean isAIDisabled() {
        return this.dataWatcher.getWatchableObjectByte(15) != 0;
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        }
        final BlockPosM blockposm = new BlockPosM(0, 0, 0);
        for (int i = 0; i < 8; ++i) {
            final double d0 = this.posX + ((i >> 0) % 2 - 0.5f) * this.width * 0.8f;
            final double d2 = this.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
            final double d3 = this.posZ + ((i >> 2) % 2 - 0.5f) * this.width * 0.8f;
            blockposm.setXyz(d0, d2 + this.getEyeHeight(), d3);
            if (this.worldObj.getBlockState(blockposm).getBlock().isVisuallyOpaque()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canSkipUpdate() {
        if (this.isChild()) {
            return false;
        }
        if (this.hurtTime > 0) {
            return false;
        }
        if (this.ticksExisted < 20) {
            return false;
        }
        final World world = this.getEntityWorld();
        if (world == null) {
            return false;
        }
        if (world.playerEntities.size() != 1) {
            return false;
        }
        final Entity entity = world.playerEntities.get(0);
        final double d0 = Math.max(Math.abs(this.posX - entity.posX) - 16.0, 0.0);
        final double d2 = Math.max(Math.abs(this.posZ - entity.posZ) - 16.0, 0.0);
        final double d3 = d0 * d0 + d2 * d2;
        return !this.isInRangeToRenderDist(d3);
    }
    
    private void onUpdateMinimal() {
        ++this.entityAge;
        if (this instanceof EntityMob) {
            final float f = this.getBrightness(1.0f);
            if (f > 0.5f) {
                this.entityAge += 2;
            }
        }
        this.despawnEntity();
    }
    
    public enum SpawnPlacementType
    {
        ON_GROUND("ON_GROUND", 0, "ON_GROUND", 0), 
        IN_AIR("IN_AIR", 1, "IN_AIR", 1), 
        IN_WATER("IN_WATER", 2, "IN_WATER", 2);
        
        private static final SpawnPlacementType[] $VALUES;
        private static final String __OBFID = "CL_00002255";
        
        static {
            $VALUES = new SpawnPlacementType[] { SpawnPlacementType.ON_GROUND, SpawnPlacementType.IN_AIR, SpawnPlacementType.IN_WATER };
        }
        
        private SpawnPlacementType(final String name, final int ordinal, final String p_i18_3_, final int p_i18_4_) {
        }
    }
}
