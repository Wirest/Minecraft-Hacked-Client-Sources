package net.minecraft.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import me.aristhena.lucid.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLivingBase extends Entity
{
    protected static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    public static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    private BaseAttributeMap attributeMap;
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final Map activePotionsMap = Maps.newHashMap();
    
    /** The equipment this mob was previously wearing, used for syncing. */
    private final ItemStack[] previousEquipment = new ItemStack[5];
    
    /** Whether an arm swing is currently in progress. */
    public boolean isSwingInProgress;
    public int swingProgressInt;
    public int arrowHitTimer;
    
    /**
     * The amount of time remaining this entity should act 'hurt'. (Visual appearance of red tint)
     */
    public int hurtTime;
    
    /** What the hurt time was max set to last. */
    public int maxHurtTime;
    
    /** The yaw at which this entity was last attacked from. */
    public float attackedAtYaw;
    
    /**
     * The amount of time remaining this entity should act 'dead', i.e. have a corpse in the world.
     */
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    
    /**
     * Only relevant when limbYaw is not 0(the entity is moving). Influences where in its swing legs and arms currently
     * are.
     */
    public float limbSwing;
    public int maxHurtResistantTime = 20;
    public float prevCameraPitch;
    public float cameraPitch;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    
    /** Entity head rotation yaw */
    public float rotationYawHead;
    
    /** Entity head rotation yaw at previous tick */
    public float prevRotationYawHead;
    
    /**
     * A factor used to determine how far this entity will move each tick if it is jumping or falling.
     */
    public float jumpMovementFactor = 0.02F;
    
    /** The most recent player that has attacked this entity */
    protected EntityPlayer attackingPlayer;
    
    /**
     * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity
     * should drop items on death.
     */
    protected int recentlyHit;
    
    /**
     * This gets set on entity death, but never used. Looks like a duplicate of isDead
     */
    protected boolean dead;
    
    /** The age of this EntityLiving (used to determine when it dies) */
    protected int entityAge;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;
    protected float movedDistance;
    protected float prevMovedDistance;
    protected float field_70741_aB;
    
    /** The score value of the Mob, the amount of points the mob is worth. */
    protected int scoreValue;
    
    /**
     * Damage taken in the last hit. Mobs are resistant to damage less than this for a short time after taking damage.
     */
    protected float lastDamage;
    
    /** used to check whether entity is jumping. */
    protected boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    protected float randomYawVelocity;
    
    /**
     * The number of updates over which the new position and rotation are to be applied to the entity.
     */
    protected int newPosRotationIncrements;
    
    /** The new X position to be applied to the entity. */
    protected double newPosX;
    
    /** The new Y position to be applied to the entity. */
    protected double newPosY;
    protected double newPosZ;
    
    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationYaw;
    
    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationPitch;
    
    /** Whether the DataWatcher needs to be updated with the active potions */
    private boolean potionsNeedUpdate = true;
    
    /** is only being set, has no uses as of MC 1.1 */
    private EntityLivingBase entityLivingToAttack;
    private int revengeTimer;
    private EntityLivingBase lastAttacker;
    public int auraTicks;
    
    /** Holds the value of ticksExisted when setLastAttacker was last called. */
    private int lastAttackerTime;
    
    /**
     * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed,
     * and slipperiness of the current block.
     */
    private float landMovementFactor;
    
    /** Number of ticks since last jump */
    private int jumpTicks;
    private float absorptionAmount;
    private Timer auraTimer = new Timer();
    
    /**
     * Called by the /kill command.
     */
    @Override
    public void onKillCommand()
    {
	this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }
    
    public EntityLivingBase(World worldIn)
    {
	super(worldIn);
	this.applyEntityAttributes();
	this.setHealth(this.getMaxHealth());
	this.preventEntitySpawning = true;
	this.field_70770_ap = (float) ((Math.random() + 1.0D) * 0.009999999776482582D);
	this.setPosition(this.posX, this.posY, this.posZ);
	this.field_70769_ao = (float) Math.random() * 12398.0F;
	this.rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
	this.rotationYawHead = this.rotationYaw;
	this.stepHeight = 0.6F;
    }
    
    @Override
    protected void entityInit()
    {
	this.dataWatcher.addObject(7, Integer.valueOf(0));
	this.dataWatcher.addObject(8, Byte.valueOf((byte) 0));
	this.dataWatcher.addObject(9, Byte.valueOf((byte) 0));
	this.dataWatcher.addObject(6, Float.valueOf(1.0F));
    }
    
    protected void applyEntityAttributes()
    {
	this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
	this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
	this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
    }
    
    @Override
    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
    {
	if (!this.isInWater())
	{
	    this.handleWaterMovement();
	}
	
	if (!this.worldObj.isRemote && this.fallDistance > 3.0F && onGroundIn)
	{
	    IBlockState var6 = this.worldObj.getBlockState(pos);
	    Block var7 = var6.getBlock();
	    float var8 = MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
	    
	    if (var7.getMaterial() != Material.air)
	    {
		double var9 = Math.min(0.2F + var8 / 15.0F, 10.0F);
		
		if (var9 > 2.5D)
		{
		    var9 = 2.5D;
		}
		
		int var11 = (int) (150.0D * var9);
		((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, var11, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(var6) });
	    }
	}
	
	super.updateFallState(y, onGroundIn, blockIn, pos);
    }
    
    public boolean canBreatheUnderwater()
    {
	return false;
    }
    
    /**
     * Gets called every tick from main Entity class
     */
    @Override
    public void onEntityUpdate()
    {
	this.prevSwingProgress = this.swingProgress;
	super.onEntityUpdate();
	this.worldObj.theProfiler.startSection("livingEntityBaseTick");
	boolean var1 = this instanceof EntityPlayer;
	
	if (this.isEntityAlive())
	{
	    if (this.isEntityInsideOpaqueBlock())
	    {
		this.attackEntityFrom(DamageSource.inWall, 1.0F);
	    }
	    else if (var1 && !this.worldObj.getWorldBorder().contains(this.getEntityBoundingBox()))
	    {
		double var2 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
		
		if (var2 < 0.0D)
		{
		    this.attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-var2 * this.worldObj.getWorldBorder().getDamageAmount())));
		}
	    }
	}
	
	if (this.isImmuneToFire() || this.worldObj.isRemote)
	{
	    this.extinguish();
	}
	
	boolean var7 = var1 && ((EntityPlayer) this).capabilities.disableDamage;
	
	if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water))
	{
	    if (!this.canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !var7)
	    {
		this.setAir(this.decreaseAirSupply(this.getAir()));
		
		if (this.getAir() == -20)
		{
		    this.setAir(0);
		    
		    for (int var3 = 0; var3 < 8; ++var3)
		    {
			float var4 = this.rand.nextFloat() - this.rand.nextFloat();
			float var5 = this.rand.nextFloat() - this.rand.nextFloat();
			float var6 = this.rand.nextFloat() - this.rand.nextFloat();
			this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var4, this.posY + var5, this.posZ + var6, this.motionX, this.motionY, this.motionZ, new int[0]);
		    }
		    
		    this.attackEntityFrom(DamageSource.drown, 2.0F);
		}
	    }
	    
	    if (!this.worldObj.isRemote && this.isRiding() && this.ridingEntity instanceof EntityLivingBase)
	    {
		this.mountEntity((Entity) null);
	    }
	}
	else
	{
	    this.setAir(300);
	}
	
	if (this.isEntityAlive() && this.isWet())
	{
	    this.extinguish();
	}
	
	this.prevCameraPitch = this.cameraPitch;
	
	if (this.hurtTime > 0)
	{
	    --this.hurtTime;
	}
	
	if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP))
	{
	    --this.hurtResistantTime;
	}
	
	if (this.getHealth() <= 0.0F)
	{
	    this.onDeathUpdate();
	}
	
	if (this.recentlyHit > 0)
	{
	    --this.recentlyHit;
	}
	else
	{
	    this.attackingPlayer = null;
	}
	
	if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive())
	{
	    this.lastAttacker = null;
	}
	
	if (this.entityLivingToAttack != null)
	{
	    if (!this.entityLivingToAttack.isEntityAlive())
	    {
		this.setRevengeTarget((EntityLivingBase) null);
	    }
	    else if (this.ticksExisted - this.revengeTimer > 100)
	    {
		this.setRevengeTarget((EntityLivingBase) null);
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
    
    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
	return false;
    }
    
    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
	++this.deathTime;
	
	if (this.deathTime == 20)
	{
	    int var1;
	    
	    if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
	    {
		var1 = this.getExperiencePoints(this.attackingPlayer);
		
		while (var1 > 0)
		{
		    int var2 = EntityXPOrb.getXPSplit(var1);
		    var1 -= var2;
		    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var2));
		}
	    }
	    
	    this.setDead();
	    
	    for (var1 = 0; var1 < 20; ++var1)
	    {
		double var8 = this.rand.nextGaussian() * 0.02D;
		double var4 = this.rand.nextGaussian() * 0.02D;
		double var6 = this.rand.nextGaussian() * 0.02D;
		this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var8, var4, var6, new int[0]);
	    }
	}
    }
    
    /**
     * Entity won't drop items or experience points if this returns false
     */
    protected boolean canDropLoot()
    {
	return !this.isChild();
    }
    
    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int p_70682_1_)
    {
	int var2 = EnchantmentHelper.getRespiration(this);
	return var2 > 0 && this.rand.nextInt(var2 + 1) > 0 ? p_70682_1_ : p_70682_1_ - 1;
    }
    
    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
	return 0;
    }
    
    /**
     * Only use is to identify if class is an instance of player for experience dropping
     */
    protected boolean isPlayer()
    {
	return false;
    }
    
    public Random getRNG()
    {
	return this.rand;
    }
    
    public EntityLivingBase getAITarget()
    {
	return this.entityLivingToAttack;
    }
    
    public int getRevengeTimer()
    {
	return this.revengeTimer;
    }
    
    public void setRevengeTarget(EntityLivingBase livingBase)
    {
	this.entityLivingToAttack = livingBase;
	this.revengeTimer = this.ticksExisted;
    }
    
    public EntityLivingBase getLastAttacker()
    {
	return this.lastAttacker;
    }
    
    public int getLastAttackerTime()
    {
	return this.lastAttackerTime;
    }
    
    public void setLastAttacker(Entity p_130011_1_)
    {
	if (p_130011_1_ instanceof EntityLivingBase)
	{
	    this.lastAttacker = (EntityLivingBase) p_130011_1_;
	}
	else
	{
	    this.lastAttacker = null;
	}
	
	this.lastAttackerTime = this.ticksExisted;
    }
    
    public int getAge()
    {
	return this.entityAge;
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
	tagCompound.setFloat("HealF", this.getHealth());
	tagCompound.setShort("Health", (short) ((int) Math.ceil(this.getHealth())));
	tagCompound.setShort("HurtTime", (short) this.hurtTime);
	tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
	tagCompound.setShort("DeathTime", (short) this.deathTime);
	tagCompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
	ItemStack[] var2 = this.getInventory();
	int var3 = var2.length;
	int var4;
	ItemStack var5;
	
	for (var4 = 0; var4 < var3; ++var4)
	{
	    var5 = var2[var4];
	    
	    if (var5 != null)
	    {
		this.attributeMap.removeAttributeModifiers(var5.getAttributeModifiers());
	    }
	}
	
	tagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
	var2 = this.getInventory();
	var3 = var2.length;
	
	for (var4 = 0; var4 < var3; ++var4)
	{
	    var5 = var2[var4];
	    
	    if (var5 != null)
	    {
		this.attributeMap.applyAttributeModifiers(var5.getAttributeModifiers());
	    }
	}
	
	if (!this.activePotionsMap.isEmpty())
	{
	    NBTTagList var6 = new NBTTagList();
	    Iterator var7 = this.activePotionsMap.values().iterator();
	    
	    while (var7.hasNext())
	    {
		PotionEffect var8 = (PotionEffect) var7.next();
		var6.appendTag(var8.writeCustomPotionEffectToNBT(new NBTTagCompound()));
	    }
	    
	    tagCompound.setTag("ActiveEffects", var6);
	}
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
	this.setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
	
	if (tagCompund.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote)
	{
	    SharedMonsterAttributes.func_151475_a(this.getAttributeMap(), tagCompund.getTagList("Attributes", 10));
	}
	
	if (tagCompund.hasKey("ActiveEffects", 9))
	{
	    NBTTagList var2 = tagCompund.getTagList("ActiveEffects", 10);
	    
	    for (int var3 = 0; var3 < var2.tagCount(); ++var3)
	    {
		NBTTagCompound var4 = var2.getCompoundTagAt(var3);
		PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
		
		if (var5 != null)
		{
		    this.activePotionsMap.put(Integer.valueOf(var5.getPotionID()), var5);
		}
	    }
	}
	
	if (tagCompund.hasKey("HealF", 99))
	{
	    this.setHealth(tagCompund.getFloat("HealF"));
	}
	else
	{
	    NBTBase var6 = tagCompund.getTag("Health");
	    
	    if (var6 == null)
	    {
		this.setHealth(this.getMaxHealth());
	    }
	    else if (var6.getId() == 5)
	    {
		this.setHealth(((NBTTagFloat) var6).getFloat());
	    }
	    else if (var6.getId() == 2)
	    {
		this.setHealth(((NBTTagShort) var6).getShort());
	    }
	}
	
	this.hurtTime = tagCompund.getShort("HurtTime");
	this.deathTime = tagCompund.getShort("DeathTime");
	this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
    }
    
    protected void updatePotionEffects()
    {
	Iterator var1 = this.activePotionsMap.keySet().iterator();
	
	while (var1.hasNext())
	{
	    Integer var2 = (Integer) var1.next();
	    PotionEffect var3 = (PotionEffect) this.activePotionsMap.get(var2);
	    
	    if (!var3.onUpdate(this))
	    {
		if (!this.worldObj.isRemote)
		{
		    var1.remove();
		    this.onFinishedPotionEffect(var3);
		}
	    }
	    else if (var3.getDuration() % 600 == 0)
	    {
		this.onChangedPotionEffect(var3, false);
	    }
	}
	
	if (this.potionsNeedUpdate)
	{
	    if (!this.worldObj.isRemote)
	    {
		this.updatePotionMetadata();
	    }
	    
	    this.potionsNeedUpdate = false;
	}
	
	int var11 = this.dataWatcher.getWatchableObjectInt(7);
	boolean var12 = this.dataWatcher.getWatchableObjectByte(8) > 0;
	
	if (var11 > 0)
	{
	    boolean var4 = false;
	    
	    if (!this.isInvisible())
	    {
		var4 = this.rand.nextBoolean();
	    }
	    else
	    {
		var4 = this.rand.nextInt(15) == 0;
	    }
	    
	    if (var12)
	    {
		var4 &= this.rand.nextInt(5) == 0;
	    }
	    
	    if (var4 && var11 > 0)
	    {
		double var5 = (var11 >> 16 & 255) / 255.0D;
		double var7 = (var11 >> 8 & 255) / 255.0D;
		double var9 = (var11 >> 0 & 255) / 255.0D;
		this.worldObj.spawnParticle(var12 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, var5, var7, var9, new int[0]);
	    }
	}
    }
    
    /**
     * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
     * ambience, and invisibility metadata values
     */
    protected void updatePotionMetadata()
    {
	if (this.activePotionsMap.isEmpty())
	{
	    this.resetPotionEffectMetadata();
	    this.setInvisible(false);
	}
	else
	{
	    int var1 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
	    this.dataWatcher.updateObject(8, Byte.valueOf((byte) (PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0)));
	    this.dataWatcher.updateObject(7, Integer.valueOf(var1));
	    this.setInvisible(this.isPotionActive(Potion.invisibility.id));
	}
    }
    
    /**
     * Resets the potion effect color and ambience metadata values
     */
    protected void resetPotionEffectMetadata()
    {
	this.dataWatcher.updateObject(8, Byte.valueOf((byte) 0));
	this.dataWatcher.updateObject(7, Integer.valueOf(0));
    }
    
    public void clearActivePotions()
    {
	Iterator var1 = this.activePotionsMap.keySet().iterator();
	
	while (var1.hasNext())
	{
	    Integer var2 = (Integer) var1.next();
	    PotionEffect var3 = (PotionEffect) this.activePotionsMap.get(var2);
	    
	    if (!this.worldObj.isRemote)
	    {
		var1.remove();
		this.onFinishedPotionEffect(var3);
	    }
	}
    }
    
    public Collection getActivePotionEffects()
    {
	return this.activePotionsMap.values();
    }
    
    public boolean isPotionActive(int p_82165_1_)
    {
	return this.activePotionsMap.containsKey(Integer.valueOf(p_82165_1_));
    }
    
    public boolean isPotionActive(Potion p_70644_1_)
    {
	return this.activePotionsMap.containsKey(Integer.valueOf(p_70644_1_.id));
    }
    
    /**
     * returns the PotionEffect for the supplied Potion if it is active, null otherwise.
     */
    public PotionEffect getActivePotionEffect(Potion p_70660_1_)
    {
	return (PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70660_1_.id));
    }
    
    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect p_70690_1_)
    {
	if (this.isPotionApplicable(p_70690_1_))
	{
	    if (this.activePotionsMap.containsKey(Integer.valueOf(p_70690_1_.getPotionID())))
	    {
		((PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID()))).combine(p_70690_1_);
		this.onChangedPotionEffect((PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID())), true);
	    }
	    else
	    {
		this.activePotionsMap.put(Integer.valueOf(p_70690_1_.getPotionID()), p_70690_1_);
		this.onNewPotionEffect(p_70690_1_);
	    }
	}
    }
    
    public boolean isPotionApplicable(PotionEffect p_70687_1_)
    {
	if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
	{
	    int var2 = p_70687_1_.getPotionID();
	    
	    if (var2 == Potion.regeneration.id || var2 == Potion.poison.id)
	    {
		return false;
	    }
	}
	
	return true;
    }
    
    /**
     * Returns true if this entity is undead.
     */
    public boolean isEntityUndead()
    {
	return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }
    
    /**
     * Remove the speified potion effect from this entity.
     */
    public void removePotionEffectClient(int p_70618_1_)
    {
	this.activePotionsMap.remove(Integer.valueOf(p_70618_1_));
    }
    
    /**
     * Remove the specified potion effect from this entity.
     */
    public void removePotionEffect(int p_82170_1_)
    {
	PotionEffect var2 = (PotionEffect) this.activePotionsMap.remove(Integer.valueOf(p_82170_1_));
	
	if (var2 != null)
	{
	    this.onFinishedPotionEffect(var2);
	}
    }
    
    protected void onNewPotionEffect(PotionEffect p_70670_1_)
    {
	this.potionsNeedUpdate = true;
	
	if (!this.worldObj.isRemote)
	{
	    Potion.potionTypes[p_70670_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70670_1_.getAmplifier());
	}
    }
    
    protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_)
    {
	this.potionsNeedUpdate = true;
	
	if (p_70695_2_ && !this.worldObj.isRemote)
	{
	    Potion.potionTypes[p_70695_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
	    Potion.potionTypes[p_70695_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
	}
    }
    
    protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
    {
	this.potionsNeedUpdate = true;
	
	if (!this.worldObj.isRemote)
	{
	    Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70688_1_.getAmplifier());
	}
    }
    
    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float healAmount)
    {
	float var2 = this.getHealth();
	
	if (var2 > 0.0F)
	{
	    this.setHealth(var2 + healAmount);
	}
    }
    
    public final float getHealth()
    {
	return this.dataWatcher.getWatchableObjectFloat(6);
    }
    
    public void setHealth(float p_70606_1_)
    {
	this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(p_70606_1_, 0.0F, this.getMaxHealth())));
    }
    
    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
	if (this.isEntityInvulnerable(source))
	{
	    return false;
	}
	else if (this.worldObj.isRemote)
	{
	    return false;
	}
	else
	{
	    this.entityAge = 0;
	    
	    if (this.getHealth() <= 0.0F)
	    {
		return false;
	    }
	    else if (source.isFireDamage() && this.isPotionActive(Potion.fireResistance))
	    {
		return false;
	    }
	    else
	    {
		if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null)
		{
		    this.getEquipmentInSlot(4).damageItem((int) (amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
		    amount *= 0.75F;
		}
		
		this.limbSwingAmount = 1.5F;
		boolean var3 = true;
		
		if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F)
		{
		    if (amount <= this.lastDamage)
		    {
			return false;
		    }
		    
		    this.damageEntity(source, amount - this.lastDamage);
		    this.lastDamage = amount;
		    var3 = false;
		}
		else
		{
		    this.lastDamage = amount;
		    this.hurtResistantTime = this.maxHurtResistantTime;
		    this.damageEntity(source, amount);
		    this.hurtTime = this.maxHurtTime = 10;
		}
		
		this.attackedAtYaw = 0.0F;
		Entity var4 = source.getEntity();
		
		if (var4 != null)
		{
		    if (var4 instanceof EntityLivingBase)
		    {
			this.setRevengeTarget((EntityLivingBase) var4);
		    }
		    
		    if (var4 instanceof EntityPlayer)
		    {
			this.recentlyHit = 100;
			this.attackingPlayer = (EntityPlayer) var4;
		    }
		    else if (var4 instanceof EntityWolf)
		    {
			EntityWolf var5 = (EntityWolf) var4;
			
			if (var5.isTamed())
			{
			    this.recentlyHit = 100;
			    this.attackingPlayer = null;
			}
		    }
		}
		
		if (var3)
		{
		    this.worldObj.setEntityState(this, (byte) 2);
		    
		    if (source != DamageSource.drown)
		    {
			this.setBeenAttacked();
		    }
		    
		    if (var4 != null)
		    {
			double var9 = var4.posX - this.posX;
			double var7;
			
			for (var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
			{
			    var9 = (Math.random() - Math.random()) * 0.01D;
			}
			
			this.attackedAtYaw = (float) (Math.atan2(var7, var9) * 180.0D / Math.PI - this.rotationYaw);
			this.knockBack(var4, amount, var9, var7);
		    }
		    else
		    {
			this.attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
		    }
		}
		
		String var10;
		
		if (this.getHealth() <= 0.0F)
		{
		    var10 = this.getDeathSound();
		    
		    if (var3 && var10 != null)
		    {
			this.playSound(var10, this.getSoundVolume(), this.getSoundPitch());
		    }
		    
		    this.onDeath(source);
		}
		else
		{
		    var10 = this.getHurtSound();
		    
		    if (var3 && var10 != null)
		    {
			this.playSound(var10, this.getSoundVolume(), this.getSoundPitch());
		    }
		}
		
		return true;
	    }
	}
    }
    
    /**
     * Renders broken item particles using the given ItemStack
     */
    public void renderBrokenItemStack(ItemStack p_70669_1_)
    {
	this.playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
	
	for (int var2 = 0; var2 < 5; ++var2)
	{
	    Vec3 var3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
	    var3 = var3.rotatePitch(-this.rotationPitch * (float) Math.PI / 180.0F);
	    var3 = var3.rotateYaw(-this.rotationYaw * (float) Math.PI / 180.0F);
	    double var4 = (-this.rand.nextFloat()) * 0.6D - 0.3D;
	    Vec3 var6 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, var4, 0.6D);
	    var6 = var6.rotatePitch(-this.rotationPitch * (float) Math.PI / 180.0F);
	    var6 = var6.rotateYaw(-this.rotationYaw * (float) Math.PI / 180.0F);
	    var6 = var6.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
	    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var6.xCoord, var6.yCoord, var6.zCoord, var3.xCoord, var3.yCoord + 0.05D, var3.zCoord, new int[] { Item.getIdFromItem(p_70669_1_.getItem()) });
	}
    }
    
    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
	Entity var2 = cause.getEntity();
	EntityLivingBase var3 = this.func_94060_bK();
	
	if (this.scoreValue >= 0 && var3 != null)
	{
	    var3.addToPlayerScore(this, this.scoreValue);
	}
	
	if (var2 != null)
	{
	    var2.onKillEntity(this);
	}
	
	this.dead = true;
	this.getCombatTracker().func_94549_h();
	
	if (!this.worldObj.isRemote)
	{
	    int var4 = 0;
	    
	    if (var2 instanceof EntityPlayer)
	    {
		var4 = EnchantmentHelper.getLootingModifier((EntityLivingBase) var2);
	    }
	    
	    if (this.canDropLoot() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
	    {
		this.dropFewItems(this.recentlyHit > 0, var4);
		this.dropEquipment(this.recentlyHit > 0, var4);
		
		if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025F + var4 * 0.01F)
		{
		    this.addRandomDrop();
		}
	    }
	}
	
	this.worldObj.setEntityState(this, (byte) 3);
    }
    
    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean p_82160_1_, int p_82160_2_)
    {
    }
    
    /**
     * knocks back this entity
     */
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_)
    {
	if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
	{
	    this.isAirBorne = true;
	    float var7 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
	    float var8 = 0.4F;
	    this.motionX /= 2.0D;
	    this.motionY /= 2.0D;
	    this.motionZ /= 2.0D;
	    this.motionX -= p_70653_3_ / var7 * var8;
	    this.motionY += var8;
	    this.motionZ -= p_70653_5_ / var7 * var8;
	    
	    if (this.motionY > 0.4000000059604645D)
	    {
		this.motionY = 0.4000000059604645D;
	    }
	}
    }
    
    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
	return "game.neutral.hurt";
    }
    
    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
	return "game.neutral.die";
    }
    
    /**
     * Causes this Entity to drop a random item.
     */
    protected void addRandomDrop()
    {
    }
    
    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
    }
    
    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
	int var1 = MathHelper.floor_double(this.posX);
	int var2 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
	int var3 = MathHelper.floor_double(this.posZ);
	Block var4 = this.worldObj.getBlockState(new BlockPos(var1, var2, var3)).getBlock();
	return (var4 == Blocks.ladder || var4 == Blocks.vine) && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).isSpectator());
    }
    
    /**
     * Checks whether target entity is alive.
     */
    @Override
    public boolean isEntityAlive()
    {
	return !this.isDead && this.getHealth() > 0.0F;
    }
    
    @Override
    public void fall(float distance, float damageMultiplier)
    {
	super.fall(distance, damageMultiplier);
	PotionEffect var3 = this.getActivePotionEffect(Potion.jump);
	float var4 = var3 != null ? (float) (var3.getAmplifier() + 1) : 0.0F;
	int var5 = MathHelper.ceiling_float_int((distance - 3.0F - var4) * damageMultiplier);
	
	if (var5 > 0)
	{
	    this.playSound(this.getFallSoundString(var5), 1.0F, 1.0F);
	    this.attackEntityFrom(DamageSource.fall, var5);
	    int var6 = MathHelper.floor_double(this.posX);
	    int var7 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
	    int var8 = MathHelper.floor_double(this.posZ);
	    Block var9 = this.worldObj.getBlockState(new BlockPos(var6, var7, var8)).getBlock();
	    
	    if (var9.getMaterial() != Material.air)
	    {
		Block.SoundType var10 = var9.stepSound;
		this.playSound(var10.getStepSound(), var10.getVolume() * 0.5F, var10.getFrequency() * 0.75F);
	    }
	}
    }
    
    protected String getFallSoundString(int damageValue)
    {
	return damageValue > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }
    
    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @Override
    public void performHurtAnimation()
    {
	this.hurtTime = this.maxHurtTime = 10;
	this.attackedAtYaw = 0.0F;
    }
    
    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
	int var1 = 0;
	ItemStack[] var2 = this.getInventory();
	int var3 = var2.length;
	
	for (int var4 = 0; var4 < var3; ++var4)
	{
	    ItemStack var5 = var2[var4];
	    
	    if (var5 != null && var5.getItem() instanceof ItemArmor)
	    {
		int var6 = ((ItemArmor) var5.getItem()).damageReduceAmount;
		var1 += var6;
	    }
	}
	
	return var1;
    }
    
    protected void damageArmor(float p_70675_1_)
    {
    }
    
    /**
     * Reduces damage, depending on armor
     *  
     * @param source The damage source
     * @param damage The damage to deal
     */
    protected float applyArmorCalculations(DamageSource source, float damage)
    {
	if (!source.isUnblockable())
	{
	    int var3 = 25 - this.getTotalArmorValue();
	    float var4 = damage * var3;
	    this.damageArmor(damage);
	    damage = var4 / 25.0F;
	}
	
	return damage;
    }
    
    /**
     * Reduces damage, depending on potions
     *  
     * @param damage The damage to deal
     */
    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
	if (source.isDamageAbsolute())
	{
	    return damage;
	}
	else
	{
	    int var3;
	    int var4;
	    float var5;
	    
	    if (this.isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld)
	    {
		var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
		var4 = 25 - var3;
		var5 = damage * var4;
		damage = var5 / 25.0F;
	    }
	    
	    if (damage <= 0.0F)
	    {
		return 0.0F;
	    }
	    else
	    {
		var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getInventory(), source);
		
		if (var3 > 20)
		{
		    var3 = 20;
		}
		
		if (var3 > 0 && var3 <= 20)
		{
		    var4 = 25 - var3;
		    var5 = damage * var4;
		    damage = var5 / 25.0F;
		}
		
		return damage;
	    }
	}
    }
    
    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
	if (!this.isEntityInvulnerable(damageSrc))
	{
	    damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
	    damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
	    float var3 = damageAmount;
	    damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
	    this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - damageAmount));
	    
	    if (damageAmount != 0.0F)
	    {
		float var4 = this.getHealth();
		this.setHealth(var4 - damageAmount);
		this.getCombatTracker().func_94547_a(damageSrc, var4, damageAmount);
		this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
	    }
	}
    }
    
    public CombatTracker getCombatTracker()
    {
	return this._combatTracker;
    }
    
    public EntityLivingBase func_94060_bK()
    {
	return this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null));
    }
    
    public final float getMaxHealth()
    {
	return (float) this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }
    
    /**
     * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
     */
    public final int getArrowCountInEntity()
    {
	return this.dataWatcher.getWatchableObjectByte(9);
    }
    
    /**
     * sets the amount of arrows stuck in the entity. used for rendering those
     */
    public final void setArrowCountInEntity(int count)
    {
	this.dataWatcher.updateObject(9, Byte.valueOf((byte) count));
    }
    
    /**
     * Returns an integer indicating the end point of the swing animation, used by {@link #swingProgress} to provide a
     * progress indicator. Takes dig speed enchantments into account.
     */
    private int getArmSwingAnimationEnd()
    {
	return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
    }
    
    /**
     * Swings the item the player is holding.
     */
    public void swingItem()
    {
	if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0)
	{
	    this.swingProgressInt = -1;
	    this.isSwingInProgress = true;
	    
	    if (this.worldObj instanceof WorldServer)
	    {
		((WorldServer) this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0BPacketAnimation(this, 0));
	    }
	}
    }
    
    @Override
    public void handleHealthUpdate(byte id)
    {
	String var2;
	
	if (id == 2)
	{
	    this.limbSwingAmount = 1.5F;
	    this.hurtResistantTime = this.maxHurtResistantTime;
	    this.hurtTime = this.maxHurtTime = 10;
	    this.attackedAtYaw = 0.0F;
	    var2 = this.getHurtSound();
	    
	    if (var2 != null)
	    {
		this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
	    }
	    
	    this.attackEntityFrom(DamageSource.generic, 0.0F);
	}
	else if (id == 3)
	{
	    var2 = this.getDeathSound();
	    
	    if (var2 != null)
	    {
		this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
	    }
	    
	    this.setHealth(0.0F);
	    this.onDeath(DamageSource.generic);
	}
	else
	{
	    super.handleHealthUpdate(id);
	}
    }
    
    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    @Override
    protected void kill()
    {
	this.attackEntityFrom(DamageSource.outOfWorld, 4.0F);
    }
    
    /**
     * Updates the arm swing progress counters and animation progress
     */
    protected void updateArmSwingProgress()
    {
	int var1 = this.getArmSwingAnimationEnd();
	
	if (this.isSwingInProgress)
	{
	    ++this.swingProgressInt;
	    
	    if (this.swingProgressInt >= var1)
	    {
		this.swingProgressInt = 0;
		this.isSwingInProgress = false;
	    }
	}
	else
	{
	    this.swingProgressInt = 0;
	}
	
	this.swingProgress = (float) this.swingProgressInt / (float) var1;
    }
    
    public IAttributeInstance getEntityAttribute(IAttribute attribute)
    {
	return this.getAttributeMap().getAttributeInstance(attribute);
    }
    
    public BaseAttributeMap getAttributeMap()
    {
	if (this.attributeMap == null)
	{
	    this.attributeMap = new ServersideAttributeMap();
	}
	
	return this.attributeMap;
    }
    
    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
	return EnumCreatureAttribute.UNDEFINED;
    }
    
    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public abstract ItemStack getHeldItem();
    
    /**
     * 0: Tool in Hand; 1-4: Armor
     */
    public abstract ItemStack getEquipmentInSlot(int var1);
    
    public abstract ItemStack getCurrentArmor(int var1);
    
    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    @Override
    public abstract void setCurrentItemOrArmor(int var1, ItemStack var2);
    
    /**
     * Set sprinting switch for Entity.
     */
    @Override
    public void setSprinting(boolean sprinting)
    {
	super.setSprinting(sprinting);
	IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
	
	if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null)
	{
	    var2.removeModifier(sprintingSpeedBoostModifier);
	}
	
	if (sprinting)
	{
	    var2.applyModifier(sprintingSpeedBoostModifier);
	}
    }
    
    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    @Override
    public abstract ItemStack[] getInventory();
    
    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
	return 1.0F;
    }
    
    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
	return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }
    
    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
	return this.getHealth() <= 0.0F;
    }
    
    /**
     * Moves the entity to a position out of the way of its mount.
     */
    public void dismountEntity(Entity p_110145_1_)
    {
	double var3 = p_110145_1_.posX;
	double var5 = p_110145_1_.getEntityBoundingBox().minY + p_110145_1_.height;
	double var7 = p_110145_1_.posZ;
	byte var9 = 1;
	
	for (int var10 = -var9; var10 <= var9; ++var10)
	{
	    for (int var11 = -var9; var11 < var9; ++var11)
	    {
		if (var10 != 0 || var11 != 0)
		{
		    int var12 = (int) (this.posX + var10);
		    int var13 = (int) (this.posZ + var11);
		    AxisAlignedBB var2 = this.getEntityBoundingBox().offset(var10, 1.0D, var11);
		    
		    if (this.worldObj.func_147461_a(var2).isEmpty())
		    {
			if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var12, (int) this.posY, var13)))
			{
			    this.setPositionAndUpdate(this.posX + var10, this.posY + 1.0D, this.posZ + var11);
			    return;
			}
			
			if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var12, (int) this.posY - 1, var13)) || this.worldObj.getBlockState(new BlockPos(var12, (int) this.posY - 1, var13)).getBlock().getMaterial() == Material.water)
			{
			    var3 = this.posX + var10;
			    var5 = this.posY + 1.0D;
			    var7 = this.posZ + var11;
			}
		    }
		}
	    }
	}
	
	this.setPositionAndUpdate(var3, var5, var7);
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender()
    {
	return false;
    }
    
    protected float func_175134_bD()
    {
	return 0.42F;
    }
    
    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
	this.motionY = this.func_175134_bD();
	
	if (this.isPotionActive(Potion.jump))
	{
	    this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
	}
	
	if (this.isSprinting())
	{
	    float var1 = this.rotationYaw * 0.017453292F;
	    this.motionX -= MathHelper.sin(var1) * 0.2F;
	    this.motionZ += MathHelper.cos(var1) * 0.2F;
	}
	
	this.isAirBorne = true;
    }
    
    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
	this.motionY += 0.03999999910593033D;
    }
    
    protected void func_180466_bG()
    {
	this.motionY += 0.03999999910593033D;
    }
    
    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float strafe, float forward)
    {
	double var8;
	float var10;
	
	if (this.isServerWorld())
	{
	    float var5;
	    float var6;
	    
	    if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying))
	    {
		var8 = this.posY;
		var5 = 0.8F;
		var6 = 0.02F;
		var10 = EnchantmentHelper.getDepthStriderModifier(this);
		
		if (var10 > 3.0F)
		{
		    var10 = 3.0F;
		}
		
		if (!this.onGround)
		{
		    var10 *= 0.5F;
		}
		
		if (var10 > 0.0F)
		{
		    var5 += (0.54600006F - var5) * var10 / 3.0F;
		    var6 += (this.getAIMoveSpeed() * 1.0F - var6) * var10 / 3.0F;
		}
		
		this.moveFlying(strafe, forward, var6);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= var5;
		this.motionY *= 0.800000011920929D;
		this.motionZ *= var5;
		this.motionY -= 0.02D;
		
		if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ))
		{
		    this.motionY = 0.30000001192092896D;
		}
	    }
	    else if (this.isInLava() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying))
	    {
		var8 = this.posY;
		this.moveFlying(strafe, forward, 0.02F);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.5D;
		this.motionY *= 0.5D;
		this.motionZ *= 0.5D;
		this.motionY -= 0.02D;
		
		if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ))
		{
		    this.motionY = 0.30000001192092896D;
		}
	    }
	    else
	    {
		float var3 = 0.91F;
		
		if (this.onGround)
		{
		    var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
		}
		
		float var4 = 0.16277136F / (var3 * var3 * var3);
		
		if (this.onGround)
		{
		    var5 = this.getAIMoveSpeed() * var4;
		}
		else
		{
		    var5 = this.jumpMovementFactor;
		}
		
		this.moveFlying(strafe, forward, var5);
		var3 = 0.91F;
		
		if (this.onGround)
		{
		    var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
		}
		
		if (this.isOnLadder())
		{
		    var6 = 0.15F;
		    this.motionX = MathHelper.clamp_double(this.motionX, (-var6), var6);
		    this.motionZ = MathHelper.clamp_double(this.motionZ, (-var6), var6);
		    this.fallDistance = 0.0F;
		    
		    if (this.motionY < -0.15D)
		    {
			this.motionY = -0.15D;
		    }
		    
		    boolean var7 = this.isSneaking() && this instanceof EntityPlayer;
		    
		    if (var7 && this.motionY < 0.0D)
		    {
			this.motionY = 0.0D;
		    }
		}
		
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		
		if (this.isCollidedHorizontally && this.isOnLadder())
		{
		    this.motionY = 0.2D;
		}
		
		if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int) this.posX, 0, (int) this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int) this.posX, 0, (int) this.posZ)).isLoaded()))
		{
		    if (this.posY > 0.0D)
		    {
			this.motionY = -0.1D;
		    }
		    else
		    {
			this.motionY = 0.0D;
		    }
		}
		else
		{
		    this.motionY -= 0.08D;
		}
		
		this.motionY *= 0.9800000190734863D;
		this.motionX *= var3;
		this.motionZ *= var3;
	    }
	}
	
	this.prevLimbSwingAmount = this.limbSwingAmount;
	var8 = this.posX - this.prevPosX;
	double var9 = this.posZ - this.prevPosZ;
	var10 = MathHelper.sqrt_double(var8 * var8 + var9 * var9) * 4.0F;
	
	if (var10 > 1.0F)
	{
	    var10 = 1.0F;
	}
	
	this.limbSwingAmount += (var10 - this.limbSwingAmount) * 0.4F;
	this.limbSwing += this.limbSwingAmount;
    }
    
    /**
     * the movespeed used for the new AI system
     */
    public float getAIMoveSpeed()
    {
	return this.landMovementFactor;
    }
    
    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float speedIn)
    {
	this.landMovementFactor = speedIn;
    }
    
    public boolean attackEntityAsMob(Entity entityIn)
    {
	this.setLastAttacker(entityIn);
	return false;
    }
    
    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping()
    {
	return false;
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
	super.onUpdate();
	
	if (!this.worldObj.isRemote)
	{
	    int var1 = this.getArrowCountInEntity();
	    
	    if (var1 > 0)
	    {
		if (this.arrowHitTimer <= 0)
		{
		    this.arrowHitTimer = 20 * (30 - var1);
		}
		
		--this.arrowHitTimer;
		
		if (this.arrowHitTimer <= 0)
		{
		    this.setArrowCountInEntity(var1 - 1);
		}
	    }
	    
	    for (int var2 = 0; var2 < 5; ++var2)
	    {
		ItemStack var3 = this.previousEquipment[var2];
		ItemStack var4 = this.getEquipmentInSlot(var2);
		
		if (!ItemStack.areItemStacksEqual(var4, var3))
		{
		    ((WorldServer) this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(this.getEntityId(), var2, var4));
		    
		    if (var3 != null)
		    {
			this.attributeMap.removeAttributeModifiers(var3.getAttributeModifiers());
		    }
		    
		    if (var4 != null)
		    {
			this.attributeMap.applyAttributeModifiers(var4.getAttributeModifiers());
		    }
		    
		    this.previousEquipment[var2] = var4 == null ? null : var4.copy();
		}
	    }
	    
	    if (this.ticksExisted % 20 == 0)
	    {
		this.getCombatTracker().func_94549_h();
	    }
	}
	
	this.onLivingUpdate();
	double var9 = this.posX - this.prevPosX;
	double var10 = this.posZ - this.prevPosZ;
	float var5 = (float) (var9 * var9 + var10 * var10);
	float var6 = this.renderYawOffset;
	float var7 = 0.0F;
	this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
	float var8 = 0.0F;
	
	if (var5 > 0.0025000002F)
	{
	    var8 = 1.0F;
	    var7 = (float) Math.sqrt(var5) * 3.0F;
	    var6 = (float) Math.atan2(var10, var9) * 180.0F / (float) Math.PI - 90.0F;
	}
	
	if (this.swingProgress > 0.0F)
	{
	    var6 = this.rotationYaw;
	}
	
	if (!this.onGround)
	{
	    var8 = 0.0F;
	}
	
	this.onGroundSpeedFactor += (var8 - this.onGroundSpeedFactor) * 0.3F;
	this.worldObj.theProfiler.startSection("headTurn");
	var7 = this.func_110146_f(var6, var7);
	this.worldObj.theProfiler.endSection();
	this.worldObj.theProfiler.startSection("rangeChecks");
	
	while (this.rotationYaw - this.prevRotationYaw < -180.0F)
	{
	    this.prevRotationYaw -= 360.0F;
	}
	
	while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
	{
	    this.prevRotationYaw += 360.0F;
	}
	
	while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
	{
	    this.prevRenderYawOffset -= 360.0F;
	}
	
	while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
	{
	    this.prevRenderYawOffset += 360.0F;
	}
	
	while (this.rotationPitch - this.prevRotationPitch < -180.0F)
	{
	    this.prevRotationPitch -= 360.0F;
	}
	
	while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
	{
	    this.prevRotationPitch += 360.0F;
	}
	
	while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
	{
	    this.prevRotationYawHead -= 360.0F;
	}
	
	while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
	{
	    this.prevRotationYawHead += 360.0F;
	}
	
	this.worldObj.theProfiler.endSection();
	this.movedDistance += var7;
    }
    
    protected float func_110146_f(float p_110146_1_, float p_110146_2_)
    {
	float var3 = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
	this.renderYawOffset += var3 * 0.3F;
	float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
	boolean var5 = var4 < -90.0F || var4 >= 90.0F;
	
	if (var4 < -75.0F)
	{
	    var4 = -75.0F;
	}
	
	if (var4 >= 75.0F)
	{
	    var4 = 75.0F;
	}
	
	this.renderYawOffset = this.rotationYaw - var4;
	
	if (var4 * var4 > 2500.0F)
	{
	    this.renderYawOffset += var4 * 0.2F;
	}
	
	if (var5)
	{
	    p_110146_2_ *= -1.0F;
	}
	
	return p_110146_2_;
    }
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
	if (this.jumpTicks > 0)
	{
	    --this.jumpTicks;
	}
	
	if (this.newPosRotationIncrements > 0)
	{
	    double var1 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
	    double var3 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
	    double var5 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
	    double var7 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
	    this.rotationYaw = (float) (this.rotationYaw + var7 / this.newPosRotationIncrements);
	    this.rotationPitch = (float) (this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
	    --this.newPosRotationIncrements;
	    this.setPosition(var1, var3, var5);
	    this.setRotation(this.rotationYaw, this.rotationPitch);
	}
	else if (!this.isServerWorld())
	{
	    this.motionX *= 0.98D;
	    this.motionY *= 0.98D;
	    this.motionZ *= 0.98D;
	}
	
	if (Math.abs(this.motionX) < 0.005D)
	{
	    this.motionX = 0.0D;
	}
	
	if (Math.abs(this.motionY) < 0.005D)
	{
	    this.motionY = 0.0D;
	}
	
	if (Math.abs(this.motionZ) < 0.005D)
	{
	    this.motionZ = 0.0D;
	}
	
	this.worldObj.theProfiler.startSection("ai");
	
	if (this.isMovementBlocked())
	{
	    this.isJumping = false;
	    this.moveStrafing = 0.0F;
	    this.moveForward = 0.0F;
	    this.randomYawVelocity = 0.0F;
	}
	else if (this.isServerWorld())
	{
	    this.worldObj.theProfiler.startSection("newAi");
	    this.updateEntityActionState();
	    this.worldObj.theProfiler.endSection();
	}
	
	this.worldObj.theProfiler.endSection();
	this.worldObj.theProfiler.startSection("jump");
	
	if (this.isJumping)
	{
	    if (this.isInWater())
	    {
		this.updateAITick();
	    }
	    else if (this.isInLava())
	    {
		this.func_180466_bG();
	    }
	    else if (this.onGround && this.jumpTicks == 0)
	    {
		this.jump();
		this.jumpTicks = 10;
	    }
	}
	else
	{
	    this.jumpTicks = 0;
	}
	
	this.worldObj.theProfiler.endSection();
	this.worldObj.theProfiler.startSection("travel");
	this.moveStrafing *= 0.98F;
	this.moveForward *= 0.98F;
	this.randomYawVelocity *= 0.9F;
	this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
	this.worldObj.theProfiler.endSection();
	this.worldObj.theProfiler.startSection("push");
	
	if (!this.worldObj.isRemote)
	{
	    this.collideWithNearbyEntities();
	}
	
	this.worldObj.theProfiler.endSection();
    }
    
    protected void updateEntityActionState()
    {
    }
    
    protected void collideWithNearbyEntities()
    {
	List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
	
	if (var1 != null && !var1.isEmpty())
	{
	    for (int var2 = 0; var2 < var1.size(); ++var2)
	    {
		Entity var3 = (Entity) var1.get(var2);
		
		if (var3.canBePushed())
		{
		    this.collideWithEntity(var3);
		}
	    }
	}
    }
    
    protected void collideWithEntity(Entity p_82167_1_)
    {
	p_82167_1_.applyEntityCollision(this);
    }
    
    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    @Override
    public void mountEntity(Entity entityIn)
    {
	if (this.ridingEntity != null && entityIn == null)
	{
	    if (!this.worldObj.isRemote)
	    {
		this.dismountEntity(this.ridingEntity);
	    }
	    
	    if (this.ridingEntity != null)
	    {
		this.ridingEntity.riddenByEntity = null;
	    }
	    
	    this.ridingEntity = null;
	}
	else
	{
	    super.mountEntity(entityIn);
	}
    }
    
    /**
     * Handles updating while being ridden by an entity
     */
    @Override
    public void updateRidden()
    {
	super.updateRidden();
	this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
	this.onGroundSpeedFactor = 0.0F;
	this.fallDistance = 0.0F;
    }
    
    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
	this.newPosX = x;
	this.newPosY = y;
	this.newPosZ = z;
	this.newRotationYaw = yaw;
	this.newRotationPitch = pitch;
	this.newPosRotationIncrements = posRotationIncrements;
    }
    
    public void setJumping(boolean p_70637_1_)
    {
	this.isJumping = p_70637_1_;
    }
    
    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
    {
	if (!p_71001_1_.isDead && !this.worldObj.isRemote)
	{
	    EntityTracker var3 = ((WorldServer) this.worldObj).getEntityTracker();
	    
	    if (p_71001_1_ instanceof EntityItem)
	    {
		var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
	    }
	    
	    if (p_71001_1_ instanceof EntityArrow)
	    {
		var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
	    }
	    
	    if (p_71001_1_ instanceof EntityXPOrb)
	    {
		var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
	    }
	}
    }
    
    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(Entity p_70685_1_)
    {
	return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3(p_70685_1_.posX, p_70685_1_.posY + p_70685_1_.getEyeHeight(), p_70685_1_.posZ)) == null;
    }
    
    /**
     * returns a (normalized) vector of where this entity is looking
     */
    @Override
    public Vec3 getLookVec()
    {
	return this.getLook(1.0F);
    }
    
    /**
     * interpolated look vector
     */
    @Override
    public Vec3 getLook(float partialTicks)
    {
	if (partialTicks == 1.0F)
	{
	    return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
	}
	else
	{
	    float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
	    float var3 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
	    return this.getVectorForRotation(var2, var3);
	}
    }
    
    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args: partialTickTime
     */
    public float getSwingProgress(float partialTickTime)
    {
	float var2 = this.swingProgress - this.prevSwingProgress;
	
	if (var2 < 0.0F)
	{
	    ++var2;
	}
	
	return this.prevSwingProgress + var2 * partialTickTime;
    }
    
    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
	return !this.worldObj.isRemote;
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith()
    {
	return !this.isDead;
    }
    
    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed()
    {
	return !this.isDead;
    }
    
    /**
     * Sets that this entity has been attacked.
     */
    @Override
    protected void setBeenAttacked()
    {
	this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
    }
    
    @Override
    public float getRotationYawHead()
    {
	return this.rotationYawHead;
    }
    
    /**
     * Sets the head's yaw rotation of the entity.
     */
    @Override
    public void setRotationYawHead(float rotation)
    {
	this.rotationYawHead = rotation;
    }
    
    public float getAbsorptionAmount()
    {
	return this.absorptionAmount;
    }
    
    public void setAbsorptionAmount(float amount)
    {
	if (amount < 0.0F)
	{
	    amount = 0.0F;
	}
	
	this.absorptionAmount = amount;
    }
    
    public Team getTeam()
    {
	return this.worldObj.getScoreboard().getPlayersTeam(this.getUniqueID().toString());
    }
    
    public boolean isOnSameTeam(EntityLivingBase otherEntity)
    {
	return this.isOnTeam(otherEntity.getTeam());
    }
    
    /**
     * Returns true if the entity is on a specific team.
     */
    public boolean isOnTeam(Team p_142012_1_)
    {
	return this.getTeam() != null ? this.getTeam().isSameTeam(p_142012_1_) : false;
    }
    
    /**
     * Sends an ENTER_COMBAT packet to the client
     */
    public void sendEnterCombat()
    {
    }
    
    /**
     * Sends an END_COMBAT packet to the client
     */
    public void sendEndCombat()
    {
    }
    
    protected void markPotionsDirty()
    {
	this.potionsNeedUpdate = true;
    }
    
    public boolean checkTimer(float time)
    {
	if (auraTimer.delay(time))
	{
	    auraTimer.reset();
	    return true;
	}
	return false;
    }
}
