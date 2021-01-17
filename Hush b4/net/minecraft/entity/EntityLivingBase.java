// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.scoreboard.Team;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.entity.item.EntityItem;
import java.util.List;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Vec3;
import net.minecraft.entity.passive.EntityWolf;
import java.util.Collection;
import net.minecraft.potion.PotionHelper;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagFloat;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import java.util.Map;
import net.minecraft.util.CombatTracker;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public abstract class EntityLivingBase extends Entity
{
    private static final UUID sprintingSpeedBoostModifierUUID;
    private static final AttributeModifier sprintingSpeedBoostModifier;
    private BaseAttributeMap attributeMap;
    private final CombatTracker _combatTracker;
    private final Map<Integer, PotionEffect> activePotionsMap;
    private final ItemStack[] previousEquipment;
    public boolean isSwingInProgress;
    public int swingProgressInt;
    public int arrowHitTimer;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime;
    public float prevCameraPitch;
    public float cameraPitch;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor;
    protected EntityPlayer attackingPlayer;
    protected int recentlyHit;
    protected boolean dead;
    protected int entityAge;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;
    protected float movedDistance;
    protected float prevMovedDistance;
    protected float field_70741_aB;
    protected int scoreValue;
    protected float lastDamage;
    protected boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    protected float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    private boolean potionsNeedUpdate;
    private EntityLivingBase entityLivingToAttack;
    private int revengeTimer;
    private EntityLivingBase lastAttacker;
    private int lastAttackerTime;
    private float landMovementFactor;
    private int jumpTicks;
    private float absorptionAmount;
    
    static {
        sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
        sprintingSpeedBoostModifier = new AttributeModifier(EntityLivingBase.sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896, 2).setSaved(false);
    }
    
    @Override
    public void onKillCommand() {
        this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }
    
    public EntityLivingBase(final World worldIn) {
        super(worldIn);
        this._combatTracker = new CombatTracker(this);
        this.activePotionsMap = (Map<Integer, PotionEffect>)Maps.newHashMap();
        this.previousEquipment = new ItemStack[5];
        this.maxHurtResistantTime = 20;
        this.jumpMovementFactor = 0.02f;
        this.potionsNeedUpdate = true;
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.field_70770_ap = (float)((Math.random() + 1.0) * 0.009999999776482582);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.field_70769_ao = (float)Math.random() * 12398.0f;
        this.rotationYaw = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.6f;
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(7, 0);
        this.dataWatcher.addObject(8, (Byte)0);
        this.dataWatcher.addObject(9, (Byte)0);
        this.dataWatcher.addObject(6, 1.0f);
    }
    
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final Block blockIn, final BlockPos pos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }
        if (!this.worldObj.isRemote && this.fallDistance > 3.0f && onGroundIn) {
            final IBlockState iblockstate = this.worldObj.getBlockState(pos);
            final Block block = iblockstate.getBlock();
            final float f = (float)MathHelper.ceiling_float_int(this.fallDistance - 3.0f);
            if (block.getMaterial() != Material.air) {
                double d0 = Math.min(0.2f + f / 15.0f, 10.0f);
                if (d0 > 2.5) {
                    d0 = 2.5;
                }
                final int i = (int)(150.0 * d0);
                ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0, 0.0, 0.0, 0.15000000596046448, Block.getStateId(iblockstate));
            }
        }
        super.updateFallState(y, onGroundIn, blockIn, pos);
    }
    
    public boolean canBreatheUnderwater() {
        return false;
    }
    
    @Override
    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("livingEntityBaseTick");
        final boolean flag = this instanceof EntityPlayer;
        if (this.isEntityAlive()) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.inWall, 1.0f);
            }
            else if (flag && !this.worldObj.getWorldBorder().contains(this.getEntityBoundingBox())) {
                final double d0 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
                if (d0 < 0.0) {
                    this.attackEntityFrom(DamageSource.inWall, (float)Math.max(1, MathHelper.floor_double(-d0 * this.worldObj.getWorldBorder().getDamageAmount())));
                }
            }
        }
        if (this.isImmuneToFire() || this.worldObj.isRemote) {
            this.extinguish();
        }
        final boolean flag2 = flag && ((EntityPlayer)this).capabilities.disableDamage;
        if (this.isEntityAlive()) {
            if (this.isInsideOfMaterial(Material.water)) {
                if (!this.canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !flag2) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);
                        for (int i = 0; i < 8; ++i) {
                            final float f = this.rand.nextFloat() - this.rand.nextFloat();
                            final float f2 = this.rand.nextFloat() - this.rand.nextFloat();
                            final float f3 = this.rand.nextFloat() - this.rand.nextFloat();
                            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f, this.posY + f2, this.posZ + f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                        }
                        this.attackEntityFrom(DamageSource.drown, 2.0f);
                    }
                }
                if (!this.worldObj.isRemote && this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {
                    this.mountEntity(null);
                }
            }
            else {
                this.setAir(300);
            }
        }
        if (this.isEntityAlive() && this.isWet()) {
            this.extinguish();
        }
        this.prevCameraPitch = this.cameraPitch;
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
            --this.hurtResistantTime;
        }
        if (this.getHealth() <= 0.0f) {
            this.onDeathUpdate();
        }
        if (this.recentlyHit > 0) {
            --this.recentlyHit;
        }
        else {
            this.attackingPlayer = null;
        }
        if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive()) {
            this.lastAttacker = null;
        }
        if (this.entityLivingToAttack != null) {
            if (!this.entityLivingToAttack.isEntityAlive()) {
                this.setRevengeTarget(null);
            }
            else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget(null);
            }
        }
        this.updatePotionEffects();
        this.prevMovedDistance = this.movedDistance;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.worldObj.theProfiler.endSection();
    }
    
    public boolean isChild() {
        return false;
    }
    
    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                while (i > 0) {
                    final int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }
            this.setDead();
            for (int k = 0; k < 20; ++k) {
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                final double d4 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d2, d3, d4, new int[0]);
            }
        }
    }
    
    protected boolean canDropLoot() {
        return !this.isChild();
    }
    
    protected int decreaseAirSupply(final int p_70682_1_) {
        final int i = EnchantmentHelper.getRespiration(this);
        return (i > 0 && this.rand.nextInt(i + 1) > 0) ? p_70682_1_ : (p_70682_1_ - 1);
    }
    
    protected int getExperiencePoints(final EntityPlayer player) {
        return 0;
    }
    
    protected boolean isPlayer() {
        return false;
    }
    
    public Random getRNG() {
        return this.rand;
    }
    
    public EntityLivingBase getAITarget() {
        return this.entityLivingToAttack;
    }
    
    public int getRevengeTimer() {
        return this.revengeTimer;
    }
    
    public void setRevengeTarget(final EntityLivingBase livingBase) {
        this.entityLivingToAttack = livingBase;
        this.revengeTimer = this.ticksExisted;
    }
    
    public EntityLivingBase getLastAttacker() {
        return this.lastAttacker;
    }
    
    public int getLastAttackerTime() {
        return this.lastAttackerTime;
    }
    
    public void setLastAttacker(final Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            this.lastAttacker = (EntityLivingBase)entityIn;
        }
        else {
            this.lastAttacker = null;
        }
        this.lastAttackerTime = this.ticksExisted;
    }
    
    public int getAge() {
        return this.entityAge;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setFloat("HealF", this.getHealth());
        tagCompound.setShort("Health", (short)Math.ceil(this.getHealth()));
        tagCompound.setShort("HurtTime", (short)this.hurtTime);
        tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
        tagCompound.setShort("DeathTime", (short)this.deathTime);
        tagCompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        ItemStack[] inventory;
        for (int length = (inventory = this.getInventory()).length, i = 0; i < length; ++i) {
            final ItemStack itemstack = inventory[i];
            if (itemstack != null) {
                this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
            }
        }
        tagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        ItemStack[] inventory2;
        for (int length2 = (inventory2 = this.getInventory()).length, j = 0; j < length2; ++j) {
            final ItemStack itemstack2 = inventory2[j];
            if (itemstack2 != null) {
                this.attributeMap.applyAttributeModifiers(itemstack2.getAttributeModifiers());
            }
        }
        if (!this.activePotionsMap.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final PotionEffect potioneffect : this.activePotionsMap.values()) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            tagCompound.setTag("ActiveEffects", nbttaglist);
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
        if (tagCompund.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote) {
            SharedMonsterAttributes.func_151475_a(this.getAttributeMap(), tagCompund.getTagList("Attributes", 10));
        }
        if (tagCompund.hasKey("ActiveEffects", 9)) {
            final NBTTagList nbttaglist = tagCompund.getTagList("ActiveEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                if (potioneffect != null) {
                    this.activePotionsMap.put(potioneffect.getPotionID(), potioneffect);
                }
            }
        }
        if (tagCompund.hasKey("HealF", 99)) {
            this.setHealth(tagCompund.getFloat("HealF"));
        }
        else {
            final NBTBase nbtbase = tagCompund.getTag("Health");
            if (nbtbase == null) {
                this.setHealth(this.getMaxHealth());
            }
            else if (nbtbase.getId() == 5) {
                this.setHealth(((NBTTagFloat)nbtbase).getFloat());
            }
            else if (nbtbase.getId() == 2) {
                this.setHealth(((NBTTagShort)nbtbase).getShort());
            }
        }
        this.hurtTime = tagCompund.getShort("HurtTime");
        this.deathTime = tagCompund.getShort("DeathTime");
        this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
    }
    
    protected void updatePotionEffects() {
        final Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
        while (iterator.hasNext()) {
            final Integer integer = iterator.next();
            final PotionEffect potioneffect = this.activePotionsMap.get(integer);
            if (!potioneffect.onUpdate(this)) {
                if (this.worldObj.isRemote) {
                    continue;
                }
                iterator.remove();
                this.onFinishedPotionEffect(potioneffect);
            }
            else {
                if (potioneffect.getDuration() % 600 != 0) {
                    continue;
                }
                this.onChangedPotionEffect(potioneffect, false);
            }
        }
        if (this.potionsNeedUpdate) {
            if (!this.worldObj.isRemote) {
                this.updatePotionMetadata();
            }
            this.potionsNeedUpdate = false;
        }
        final int i = this.dataWatcher.getWatchableObjectInt(7);
        final boolean flag1 = this.dataWatcher.getWatchableObjectByte(8) > 0;
        if (i > 0) {
            boolean flag2 = false;
            if (!this.isInvisible()) {
                flag2 = this.rand.nextBoolean();
            }
            else {
                flag2 = (this.rand.nextInt(15) == 0);
            }
            if (flag1) {
                flag2 &= (this.rand.nextInt(5) == 0);
            }
            if (flag2 && i > 0) {
                final double d0 = (i >> 16 & 0xFF) / 255.0;
                final double d2 = (i >> 8 & 0xFF) / 255.0;
                final double d3 = (i >> 0 & 0xFF) / 255.0;
                this.worldObj.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, d0, d2, d3, new int[0]);
            }
        }
    }
    
    protected void updatePotionMetadata() {
        if (this.activePotionsMap.isEmpty()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        }
        else {
            final int i = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
            this.dataWatcher.updateObject(8, (byte)(PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0));
            this.dataWatcher.updateObject(7, i);
            this.setInvisible(this.isPotionActive(Potion.invisibility.id));
        }
    }
    
    protected void resetPotionEffectMetadata() {
        this.dataWatcher.updateObject(8, (Byte)0);
        this.dataWatcher.updateObject(7, 0);
    }
    
    public void clearActivePotions() {
        final Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
        while (iterator.hasNext()) {
            final Integer integer = iterator.next();
            final PotionEffect potioneffect = this.activePotionsMap.get(integer);
            if (!this.worldObj.isRemote) {
                iterator.remove();
                this.onFinishedPotionEffect(potioneffect);
            }
        }
    }
    
    public Collection<PotionEffect> getActivePotionEffects() {
        return this.activePotionsMap.values();
    }
    
    public boolean isPotionActive(final int potionId) {
        return this.activePotionsMap.containsKey(potionId);
    }
    
    public boolean isPotionActive(final Potion potionIn) {
        return this.activePotionsMap.containsKey(potionIn.id);
    }
    
    public PotionEffect getActivePotionEffect(final Potion potionIn) {
        return this.activePotionsMap.get(potionIn.id);
    }
    
    public void addPotionEffect(final PotionEffect potioneffectIn) {
        if (this.isPotionApplicable(potioneffectIn)) {
            if (this.activePotionsMap.containsKey(potioneffectIn.getPotionID())) {
                this.activePotionsMap.get(potioneffectIn.getPotionID()).combine(potioneffectIn);
                this.onChangedPotionEffect(this.activePotionsMap.get(potioneffectIn.getPotionID()), true);
            }
            else {
                this.activePotionsMap.put(potioneffectIn.getPotionID(), potioneffectIn);
                this.onNewPotionEffect(potioneffectIn);
            }
        }
    }
    
    public boolean isPotionApplicable(final PotionEffect potioneffectIn) {
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
            final int i = potioneffectIn.getPotionID();
            if (i == Potion.regeneration.id || i == Potion.poison.id) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }
    
    public void removePotionEffectClient(final int potionId) {
        this.activePotionsMap.remove(potionId);
    }
    
    public void removePotionEffect(final int potionId) {
        final PotionEffect potioneffect = this.activePotionsMap.remove(potionId);
        if (potioneffect != null) {
            this.onFinishedPotionEffect(potioneffect);
        }
    }
    
    protected void onNewPotionEffect(final PotionEffect id) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }
    
    protected void onChangedPotionEffect(final PotionEffect id, final boolean p_70695_2_) {
        this.potionsNeedUpdate = true;
        if (p_70695_2_ && !this.worldObj.isRemote) {
            Potion.potionTypes[id.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), id.getAmplifier());
            Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }
    
    protected void onFinishedPotionEffect(final PotionEffect p_70688_1_) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70688_1_.getAmplifier());
        }
    }
    
    public void heal(final float healAmount) {
        final float f = this.getHealth();
        if (f > 0.0f) {
            this.setHealth(f + healAmount);
        }
    }
    
    public final float getHealth() {
        return this.dataWatcher.getWatchableObjectFloat(6);
    }
    
    public void setHealth(final float health) {
        this.dataWatcher.updateObject(6, MathHelper.clamp_float(health, 0.0f, this.getMaxHealth()));
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.worldObj.isRemote) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (source.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
            return false;
        }
        if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null) {
            this.getEquipmentInSlot(4).damageItem((int)(amount * 4.0f + this.rand.nextFloat() * amount * 2.0f), this);
            amount *= 0.75f;
        }
        this.limbSwingAmount = 1.5f;
        boolean flag = true;
        if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0f) {
            if (amount <= this.lastDamage) {
                return false;
            }
            this.damageEntity(source, amount - this.lastDamage);
            this.lastDamage = amount;
            flag = false;
        }
        else {
            this.lastDamage = amount;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(source, amount);
            final int n = 10;
            this.maxHurtTime = n;
            this.hurtTime = n;
        }
        this.attackedAtYaw = 0.0f;
        final Entity entity = source.getEntity();
        if (entity != null) {
            if (entity instanceof EntityLivingBase) {
                this.setRevengeTarget((EntityLivingBase)entity);
            }
            if (entity instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = (EntityPlayer)entity;
            }
            else if (entity instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)entity;
                if (entitywolf.isTamed()) {
                    this.recentlyHit = 100;
                    this.attackingPlayer = null;
                }
            }
        }
        if (flag) {
            this.worldObj.setEntityState(this, (byte)2);
            if (source != DamageSource.drown) {
                this.setBeenAttacked();
            }
            if (entity != null) {
                double d1;
                double d2;
                for (d1 = entity.posX - this.posX, d2 = entity.posZ - this.posZ; d1 * d1 + d2 * d2 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01, d2 = (Math.random() - Math.random()) * 0.01) {}
                this.attackedAtYaw = (float)(MathHelper.func_181159_b(d2, d1) * 180.0 / 3.141592653589793 - this.rotationYaw);
                this.knockBack(entity, amount, d1, d2);
            }
            else {
                this.attackedAtYaw = (float)((int)(Math.random() * 2.0) * 180);
            }
        }
        if (this.getHealth() <= 0.0f) {
            final String s = this.getDeathSound();
            if (flag && s != null) {
                this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
            }
            this.onDeath(source);
        }
        else {
            final String s2 = this.getHurtSound();
            if (flag && s2 != null) {
                this.playSound(s2, this.getSoundVolume(), this.getSoundPitch());
            }
        }
        return true;
    }
    
    public void renderBrokenItemStack(final ItemStack stack) {
        this.playSound("random.break", 0.8f, 0.8f + this.worldObj.rand.nextFloat() * 0.4f);
        for (int i = 0; i < 5; ++i) {
            Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
            vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
            final double d0 = -this.rand.nextFloat() * 0.6 - 0.3;
            Vec3 vec4 = new Vec3((this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
            vec4 = vec4.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
            vec4 = vec4.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
            vec4 = vec4.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec4.xCoord, vec4.yCoord, vec4.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(stack.getItem()));
        }
    }
    
    public void onDeath(final DamageSource cause) {
        final Entity entity = cause.getEntity();
        final EntityLivingBase entitylivingbase = this.func_94060_bK();
        if (this.scoreValue >= 0 && entitylivingbase != null) {
            entitylivingbase.addToPlayerScore(this, this.scoreValue);
        }
        if (entity != null) {
            entity.onKillEntity(this);
        }
        this.dead = true;
        this.getCombatTracker().reset();
        if (!this.worldObj.isRemote) {
            int i = 0;
            if (entity instanceof EntityPlayer) {
                i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
            }
            if (this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
                this.dropFewItems(this.recentlyHit > 0, i);
                this.dropEquipment(this.recentlyHit > 0, i);
                if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025f + i * 0.01f) {
                    this.addRandomDrop();
                }
            }
        }
        this.worldObj.setEntityState(this, (byte)3);
    }
    
    protected void dropEquipment(final boolean p_82160_1_, final int p_82160_2_) {
    }
    
    public void knockBack(final Entity entityIn, final float p_70653_2_, final double p_70653_3_, final double p_70653_5_) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
            this.isAirBorne = true;
            final float f = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
            final float f2 = 0.4f;
            this.motionX /= 2.0;
            this.motionY /= 2.0;
            this.motionZ /= 2.0;
            this.motionX -= p_70653_3_ / f * f2;
            this.motionY += f2;
            this.motionZ -= p_70653_5_ / f * f2;
            if (this.motionY > 0.4000000059604645) {
                this.motionY = 0.4000000059604645;
            }
        }
    }
    
    protected String getHurtSound() {
        return "game.neutral.hurt";
    }
    
    protected String getDeathSound() {
        return "game.neutral.die";
    }
    
    protected void addRandomDrop() {
    }
    
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
    }
    
    public boolean isOnLadder() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor_double(this.posZ);
        final Block block = this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
        return (block == Blocks.ladder || block == Blocks.vine) && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).isSpectator());
    }
    
    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0f;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        final PotionEffect potioneffect = this.getActivePotionEffect(Potion.jump);
        final float f = (potioneffect != null) ? ((float)(potioneffect.getAmplifier() + 1)) : 0.0f;
        final int i = MathHelper.ceiling_float_int((distance - 3.0f - f) * damageMultiplier);
        if (i > 0) {
            this.playSound(this.getFallSoundString(i), 1.0f, 1.0f);
            this.attackEntityFrom(DamageSource.fall, (float)i);
            final int j = MathHelper.floor_double(this.posX);
            final int k = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int l = MathHelper.floor_double(this.posZ);
            final Block block = this.worldObj.getBlockState(new BlockPos(j, k, l)).getBlock();
            if (block.getMaterial() != Material.air) {
                final Block.SoundType block$soundtype = block.stepSound;
                this.playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5f, block$soundtype.getFrequency() * 0.75f);
            }
        }
    }
    
    protected String getFallSoundString(final int damageValue) {
        return (damageValue > 4) ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }
    
    @Override
    public void performHurtAnimation() {
        final int n = 10;
        this.maxHurtTime = n;
        this.hurtTime = n;
        this.attackedAtYaw = 0.0f;
    }
    
    public int getTotalArmorValue() {
        int i = 0;
        ItemStack[] inventory;
        for (int length = (inventory = this.getInventory()).length, k = 0; k < length; ++k) {
            final ItemStack itemstack = inventory[k];
            if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                final int j = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
                i += j;
            }
        }
        return i;
    }
    
    protected void damageArmor(final float p_70675_1_) {
    }
    
    protected float applyArmorCalculations(final DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            final int i = 25 - this.getTotalArmorValue();
            final float f = damage * i;
            this.damageArmor(damage);
            damage = f / 25.0f;
        }
        return damage;
    }
    
    protected float applyPotionDamageCalculations(final DamageSource source, float damage) {
        if (source.isDamageAbsolute()) {
            return damage;
        }
        if (this.isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld) {
            final int i = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            final int j = 25 - i;
            final float f = damage * j;
            damage = f / 25.0f;
        }
        if (damage <= 0.0f) {
            return 0.0f;
        }
        int k = EnchantmentHelper.getEnchantmentModifierDamage(this.getInventory(), source);
        if (k > 20) {
            k = 20;
        }
        if (k > 0 && k <= 20) {
            final int l = 25 - k;
            final float f2 = damage * l;
            damage = f2 / 25.0f;
        }
        return damage;
    }
    
    protected void damageEntity(final DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            final float f;
            damageAmount = (f = this.applyPotionDamageCalculations(damageSrc, damageAmount));
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                final float f2 = this.getHealth();
                this.setHealth(f2 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
            }
        }
    }
    
    public CombatTracker getCombatTracker() {
        return this._combatTracker;
    }
    
    public EntityLivingBase func_94060_bK() {
        return (this._combatTracker.func_94550_c() != null) ? this._combatTracker.func_94550_c() : ((this.attackingPlayer != null) ? this.attackingPlayer : ((this.entityLivingToAttack != null) ? this.entityLivingToAttack : null));
    }
    
    public final float getMaxHealth() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }
    
    public final int getArrowCountInEntity() {
        return this.dataWatcher.getWatchableObjectByte(9);
    }
    
    public final void setArrowCountInEntity(final int count) {
        this.dataWatcher.updateObject(9, (byte)count);
    }
    
    private int getArmSwingAnimationEnd() {
        return this.isPotionActive(Potion.digSpeed) ? (6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1) : (this.isPotionActive(Potion.digSlowdown) ? (6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
    }
    
    public void swingItem() {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            if (this.worldObj instanceof WorldServer) {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0BPacketAnimation(this, 0));
            }
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 2) {
            this.limbSwingAmount = 1.5f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            final int n = 10;
            this.maxHurtTime = n;
            this.hurtTime = n;
            this.attackedAtYaw = 0.0f;
            final String s = this.getHurtSound();
            if (s != null) {
                this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.attackEntityFrom(DamageSource.generic, 0.0f);
        }
        else if (id == 3) {
            final String s2 = this.getDeathSound();
            if (s2 != null) {
                this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.setHealth(0.0f);
            this.onDeath(DamageSource.generic);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    protected void kill() {
        this.attackEntityFrom(DamageSource.outOfWorld, 4.0f);
    }
    
    protected void updateArmSwingProgress() {
        final int i = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= i) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        }
        else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = this.swingProgressInt / (float)i;
    }
    
    public IAttributeInstance getEntityAttribute(final IAttribute attribute) {
        return this.getAttributeMap().getAttributeInstance(attribute);
    }
    
    public BaseAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new ServersideAttributeMap();
        }
        return this.attributeMap;
    }
    
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }
    
    public abstract ItemStack getHeldItem();
    
    public abstract ItemStack getEquipmentInSlot(final int p0);
    
    public abstract ItemStack getCurrentArmor(final int p0);
    
    @Override
    public abstract void setCurrentItemOrArmor(final int p0, final ItemStack p1);
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (iattributeinstance.getModifier(EntityLivingBase.sprintingSpeedBoostModifierUUID) != null) {
            iattributeinstance.removeModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
        if (sprinting) {
            iattributeinstance.applyModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
    }
    
    @Override
    public abstract ItemStack[] getInventory();
    
    protected float getSoundVolume() {
        return 1.0f;
    }
    
    protected float getSoundPitch() {
        return this.isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.5f) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f;
    }
    
    public void dismountEntity(final Entity p_110145_1_) {
        double d0 = p_110145_1_.posX;
        double d2 = p_110145_1_.getEntityBoundingBox().minY + p_110145_1_.height;
        double d3 = p_110145_1_.posZ;
        for (int i = 1, j = -i; j <= i; ++j) {
            for (int k = -i; k < i; ++k) {
                if (j != 0 || k != 0) {
                    final int l = (int)(this.posX + j);
                    final int i2 = (int)(this.posZ + k);
                    final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(j, 1.0, k);
                    if (this.worldObj.func_147461_a(axisalignedbb).isEmpty()) {
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(l, (int)this.posY, i2))) {
                            this.setPositionAndUpdate(this.posX + j, this.posY + 1.0, this.posZ + k);
                            return;
                        }
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(l, (int)this.posY - 1, i2)) || this.worldObj.getBlockState(new BlockPos(l, (int)this.posY - 1, i2)).getBlock().getMaterial() == Material.water) {
                            d0 = this.posX + j;
                            d2 = this.posY + 1.0;
                            d3 = this.posZ + k;
                        }
                    }
                }
            }
        }
        this.setPositionAndUpdate(d0, d2, d3);
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }
    
    protected float getJumpUpwardsMotion() {
        return 0.42f;
    }
    
    protected void jump() {
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
        }
        if (this.isSprinting()) {
            final float f = this.rotationYaw * 0.017453292f;
            this.motionX -= MathHelper.sin(f) * 0.2f;
            this.motionZ += MathHelper.cos(f) * 0.2f;
        }
        this.isAirBorne = true;
    }
    
    protected void updateAITick() {
        this.motionY += 0.03999999910593033;
    }
    
    protected void handleJumpLava() {
        this.motionY += 0.03999999910593033;
    }
    
    public void moveEntityWithHeading(final float strafe, final float forward) {
        if (this.isServerWorld()) {
            if (!this.isInWater() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
                if (!this.isInLava() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
                    float f4 = 0.91f;
                    if (this.onGround) {
                        f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
                    }
                    final float f5 = 0.16277136f / (f4 * f4 * f4);
                    float f6;
                    if (this.onGround) {
                        f6 = this.getAIMoveSpeed() * f5;
                    }
                    else {
                        f6 = this.jumpMovementFactor;
                    }
                    this.moveFlying(strafe, forward, f6);
                    f4 = 0.91f;
                    if (this.onGround) {
                        f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
                    }
                    if (this.isOnLadder()) {
                        final float f7 = 0.15f;
                        this.motionX = MathHelper.clamp_double(this.motionX, -f7, f7);
                        this.motionZ = MathHelper.clamp_double(this.motionZ, -f7, f7);
                        this.fallDistance = 0.0f;
                        if (this.motionY < -0.15) {
                            this.motionY = -0.15;
                        }
                        final boolean flag = this.isSneaking() && this instanceof EntityPlayer;
                        if (flag && this.motionY < 0.0) {
                            this.motionY = 0.0;
                        }
                    }
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    if (this.isCollidedHorizontally && this.isOnLadder()) {
                        this.motionY = 0.2;
                    }
                    if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())) {
                        if (this.posY > 0.0) {
                            this.motionY = -0.1;
                        }
                        else {
                            this.motionY = 0.0;
                        }
                    }
                    else {
                        this.motionY -= 0.08;
                    }
                    this.motionY *= 0.9800000190734863;
                    this.motionX *= f4;
                    this.motionZ *= f4;
                }
                else {
                    final double d1 = this.posY;
                    this.moveFlying(strafe, forward, 0.02f);
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                    this.motionY -= 0.02;
                    if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + d1, this.motionZ)) {
                        this.motionY = 0.30000001192092896;
                    }
                }
            }
            else {
                final double d2 = this.posY;
                float f8 = 0.8f;
                float f9 = 0.02f;
                float f10 = (float)EnchantmentHelper.getDepthStriderModifier(this);
                if (f10 > 3.0f) {
                    f10 = 3.0f;
                }
                if (!this.onGround) {
                    f10 *= 0.5f;
                }
                if (f10 > 0.0f) {
                    f8 += (0.54600006f - f8) * f10 / 3.0f;
                    f9 += (this.getAIMoveSpeed() * 1.0f - f9) * f10 / 3.0f;
                }
                this.moveFlying(strafe, forward, f9);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= f8;
                this.motionY *= 0.800000011920929;
                this.motionZ *= f8;
                this.motionY -= 0.02;
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + d2, this.motionZ)) {
                    this.motionY = 0.30000001192092896;
                }
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double d3 = this.posX - this.prevPosX;
        final double d4 = this.posZ - this.prevPosZ;
        float f11 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) * 4.0f;
        if (f11 > 1.0f) {
            f11 = 1.0f;
        }
        this.limbSwingAmount += (f11 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }
    
    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }
    
    public void setAIMoveSpeed(final float speedIn) {
        this.landMovementFactor = speedIn;
    }
    
    public boolean attackEntityAsMob(final Entity entityIn) {
        this.setLastAttacker(entityIn);
        return false;
    }
    
    public boolean isPlayerSleeping() {
        return false;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            final int i = this.getArrowCountInEntity();
            if (i > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - i);
                }
                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(i - 1);
                }
            }
            for (int j = 0; j < 5; ++j) {
                final ItemStack itemstack = this.previousEquipment[j];
                final ItemStack itemstack2 = this.getEquipmentInSlot(j);
                if (!ItemStack.areItemStacksEqual(itemstack2, itemstack)) {
                    ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(this.getEntityId(), j, itemstack2));
                    if (itemstack != null) {
                        this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
                    }
                    if (itemstack2 != null) {
                        this.attributeMap.applyAttributeModifiers(itemstack2.getAttributeModifiers());
                    }
                    this.previousEquipment[j] = ((itemstack2 == null) ? null : itemstack2.copy());
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }
        }
        this.onLivingUpdate();
        final double d0 = this.posX - this.prevPosX;
        final double d2 = this.posZ - this.prevPosZ;
        final float f = (float)(d0 * d0 + d2 * d2);
        float f2 = this.renderYawOffset;
        float f3 = 0.0f;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f4 = 0.0f;
        if (f > 0.0025000002f) {
            f4 = 1.0f;
            f3 = (float)Math.sqrt(f) * 3.0f;
            f2 = (float)MathHelper.func_181159_b(d2, d0) * 180.0f / 3.1415927f - 90.0f;
        }
        if (this.swingProgress > 0.0f) {
            f2 = this.rotationYaw;
        }
        if (!this.onGround) {
            f4 = 0.0f;
        }
        this.onGroundSpeedFactor += (f4 - this.onGroundSpeedFactor) * 0.3f;
        this.worldObj.theProfiler.startSection("headTurn");
        f3 = this.func_110146_f(f2, f3);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("rangeChecks");
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0f) {
            this.prevRenderYawOffset -= 360.0f;
        }
        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0f) {
            this.prevRenderYawOffset += 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYawHead - this.prevRotationYawHead < -180.0f) {
            this.prevRotationYawHead -= 360.0f;
        }
        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0f) {
            this.prevRotationYawHead += 360.0f;
        }
        this.worldObj.theProfiler.endSection();
        this.movedDistance += f3;
    }
    
    protected float func_110146_f(final float p_110146_1_, float p_110146_2_) {
        final float f = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += f * 0.3f;
        float f2 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
        final boolean flag = f2 < -90.0f || f2 >= 90.0f;
        if (f2 < -75.0f) {
            f2 = -75.0f;
        }
        if (f2 >= 75.0f) {
            f2 = 75.0f;
        }
        this.renderYawOffset = this.rotationYaw - f2;
        if (f2 * f2 > 2500.0f) {
            this.renderYawOffset += f2 * 0.2f;
        }
        if (flag) {
            p_110146_2_ *= -1.0f;
        }
        return p_110146_2_;
    }
    
    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }
        if (this.newPosRotationIncrements > 0) {
            final double d0 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
            final double d2 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
            final double d3 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
            final double d4 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
            this.rotationYaw += (float)(d4 / this.newPosRotationIncrements);
            this.rotationPitch += (float)((this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else if (!this.isServerWorld()) {
            this.motionX *= 0.98;
            this.motionY *= 0.98;
            this.motionZ *= 0.98;
        }
        if (Math.abs(this.motionX) < 0.005) {
            this.motionX = 0.0;
        }
        if (Math.abs(this.motionY) < 0.005) {
            this.motionY = 0.0;
        }
        if (Math.abs(this.motionZ) < 0.005) {
            this.motionZ = 0.0;
        }
        this.worldObj.theProfiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0f;
            this.moveForward = 0.0f;
            this.randomYawVelocity = 0.0f;
        }
        else if (this.isServerWorld()) {
            this.worldObj.theProfiler.startSection("newAi");
            this.updateEntityActionState();
            this.worldObj.theProfiler.endSection();
        }
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.updateAITick();
            }
            else if (this.isInLava()) {
                this.handleJumpLava();
            }
            else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        }
        else {
            this.jumpTicks = 0;
        }
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("travel");
        this.moveStrafing *= 0.98f;
        this.moveForward *= 0.98f;
        this.randomYawVelocity *= 0.9f;
        this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("push");
        if (!this.worldObj.isRemote) {
            this.collideWithNearbyEntities();
        }
        this.worldObj.theProfiler.endSection();
    }
    
    protected void updateEntityActionState() {
    }
    
    protected void collideWithNearbyEntities() {
        final List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224), Predicates.and((Predicate<? super Entity>)EntitySelectors.NOT_SPECTATING, (Predicate<? super Entity>)new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_.canBePushed();
            }
        }));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                this.collideWithEntity(entity);
            }
        }
    }
    
    protected void collideWithEntity(final Entity p_82167_1_) {
        p_82167_1_.applyEntityCollision(this);
    }
    
    @Override
    public void mountEntity(final Entity entityIn) {
        if (this.ridingEntity != null && entityIn == null) {
            if (!this.worldObj.isRemote) {
                this.dismountEntity(this.ridingEntity);
            }
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        }
        else {
            super.mountEntity(entityIn);
        }
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0f;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        this.newPosX = x;
        this.newPosY = y;
        this.newPosZ = z;
        this.newRotationYaw = yaw;
        this.newRotationPitch = pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }
    
    public void setJumping(final boolean p_70637_1_) {
        this.isJumping = p_70637_1_;
    }
    
    public void onItemPickup(final Entity p_71001_1_, final int p_71001_2_) {
        if (!p_71001_1_.isDead && !this.worldObj.isRemote) {
            final EntityTracker entitytracker = ((WorldServer)this.worldObj).getEntityTracker();
            if (p_71001_1_ instanceof EntityItem) {
                entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
            if (p_71001_1_ instanceof EntityArrow) {
                entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
            if (p_71001_1_ instanceof EntityXPOrb) {
                entitytracker.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
        }
    }
    
    public boolean canEntityBeSeen(final Entity entityIn) {
        return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ)) == null;
    }
    
    @Override
    public Vec3 getLookVec() {
        return this.getLook(1.0f);
    }
    
    @Override
    public Vec3 getLook(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        }
        final float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        final float f2 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
        return this.getVectorForRotation(f, f2);
    }
    
    public float getSwingProgress(final float partialTickTime) {
        float f = this.swingProgress - this.prevSwingProgress;
        if (f < 0.0f) {
            ++f;
        }
        return this.prevSwingProgress + f * partialTickTime;
    }
    
    public boolean isServerWorld() {
        return !this.worldObj.isRemote;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public boolean canBePushed() {
        return !this.isDead;
    }
    
    @Override
    protected void setBeenAttacked() {
        this.velocityChanged = (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
    }
    
    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }
    
    @Override
    public void setRotationYawHead(final float rotation) {
        this.rotationYawHead = rotation;
    }
    
    @Override
    public void func_181013_g(final float p_181013_1_) {
        this.renderYawOffset = p_181013_1_;
    }
    
    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }
    
    public void setAbsorptionAmount(float amount) {
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        this.absorptionAmount = amount;
    }
    
    public Team getTeam() {
        return this.worldObj.getScoreboard().getPlayersTeam(this.getUniqueID().toString());
    }
    
    public boolean isOnSameTeam(final EntityLivingBase otherEntity) {
        return this.isOnTeam(otherEntity.getTeam());
    }
    
    public boolean isOnTeam(final Team p_142012_1_) {
        return this.getTeam() != null && this.getTeam().isSameTeam(p_142012_1_);
    }
    
    public void sendEnterCombat() {
    }
    
    public void sendEndCombat() {
    }
    
    protected void markPotionsDirty() {
        this.potionsNeedUpdate = true;
    }
}
