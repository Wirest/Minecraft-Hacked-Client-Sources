package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.BlockPosM;
import optifine.Config;
import optifine.Reflector;

public abstract class EntityLiving extends EntityLivingBase
{
    /** Number of ticks since this EntityLiving last produced its sound */
    public int livingSoundTime;

    /** The experience points the Entity gives. */
    protected int experienceValue;
    private EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;

    /** Entity jumping helper */
    protected EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;

    /** Passive tasks (wandering, look, idle, ...) */
    protected final EntityAITasks tasks;

    /** Fighting tasks (used by monsters, wolves, ocelots) */
    protected final EntityAITasks targetTasks;

    /** The active target the Task system uses for tracking */
    private EntityLivingBase attackTarget;
    private EntitySenses senses;

    /** Equipment (armor and held item) for this entity. */
    private ItemStack[] equipment = new ItemStack[5];

    /** Chances for each equipment piece from dropping when this entity dies. */
    protected float[] equipmentDropChances = new float[5];

    /** Whether this entity can pick up items from the ground. */
    private boolean canPickUpLoot;

    /** Whether this entity should NOT despawn. */
    private boolean persistenceRequired;
    private boolean isLeashed;
    private Entity leashedToEntity;
    private NBTTagCompound leashNBTTag;
    private static final String __OBFID = "CL_00001550";
    public int randomMobsId = 0;
    public BiomeGenBase spawnBiome = null;
    public BlockPos spawnPosition = null;

    public EntityLiving(World worldIn)
    {
        super(worldIn);
        this.tasks = new EntityAITasks(worldIn != null && worldIn.theProfiler != null ? worldIn.theProfiler : null);
        this.targetTasks = new EntityAITasks(worldIn != null && worldIn.theProfiler != null ? worldIn.theProfiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = new EntityBodyHelper(this);
        this.navigator = this.func_175447_b(worldIn);
        this.senses = new EntitySenses(this);

        for (int uuid = 0; uuid < this.equipmentDropChances.length; ++uuid)
        {
            this.equipmentDropChances[uuid] = 0.085F;
        }

        UUID var5 = this.getUniqueID();
        long uuidLow = var5.getLeastSignificantBits();
        this.randomMobsId = (int)(uuidLow & 2147483647L);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    }

    protected PathNavigate func_175447_b(World worldIn)
    {
        return new PathNavigateGround(this, worldIn);
    }

    public EntityLookHelper getLookHelper()
    {
        return this.lookHelper;
    }

    public EntityMoveHelper getMoveHelper()
    {
        return this.moveHelper;
    }

    public EntityJumpHelper getJumpHelper()
    {
        return this.jumpHelper;
    }

    public PathNavigate getNavigator()
    {
        return this.navigator;
    }

    /**
     * returns the EntitySenses Object for the EntityLiving
     */
    public EntitySenses getEntitySenses()
    {
        return this.senses;
    }

    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        return this.attackTarget;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase p_70624_1_)
    {
        this.attackTarget = p_70624_1_;
        Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, new Object[] {this, p_70624_1_});
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    public boolean canAttackClass(Class p_70686_1_)
    {
        return p_70686_1_ != EntityGhast.class;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus() {}

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(15, Byte.valueOf((byte)0));
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 80;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        String var1 = this.getLivingSound();

        if (var1 != null)
        {
            this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("mobBaseTick");

        if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++)
        {
            this.livingSoundTime = -this.getTalkInterval();
            this.playLivingSound();
        }

        this.worldObj.theProfiler.endSection();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        if (this.experienceValue > 0)
        {
            int var2 = this.experienceValue;
            ItemStack[] var3 = this.getInventory();

            for (int var4 = 0; var4 < var3.length; ++var4)
            {
                if (var3[var4] != null && this.equipmentDropChances[var4] <= 1.0F)
                {
                    var2 += 1 + this.rand.nextInt(3);
                }
            }

            return var2;
        }
        else
        {
            return this.experienceValue;
        }
    }

    /**
     * Spawns an explosion particle around the Entity's location
     */
    public void spawnExplosionParticle()
    {
        if (this.worldObj.isRemote)
        {
            for (int var1 = 0; var1 < 20; ++var1)
            {
                double var2 = this.rand.nextGaussian() * 0.02D;
                double var4 = this.rand.nextGaussian() * 0.02D;
                double var6 = this.rand.nextGaussian() * 0.02D;
                double var8 = 10.0D;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var2 * var8, this.posY + (double)(this.rand.nextFloat() * this.height) - var4 * var8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var6 * var8, var2, var4, var6, new int[0]);
            }
        }
        else
        {
            this.worldObj.setEntityState(this, (byte)20);
        }
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 20)
        {
            this.spawnExplosionParticle();
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (Config.isSmoothWorld() && this.canSkipUpdate())
        {
            this.onUpdateMinimal();
        }
        else
        {
            super.onUpdate();

            if (!this.worldObj.isRemote)
            {
                this.updateLeashedState();
            }
        }
    }

    protected float func_110146_f(float p_110146_1_, float p_110146_2_)
    {
        this.bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return null;
    }

    protected Item getDropItem()
    {
        return null;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        Item var3 = this.getDropItem();

        if (var3 != null)
        {
            int var4 = this.rand.nextInt(3);

            if (p_70628_2_ > 0)
            {
                var4 += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int var5 = 0; var5 < var4; ++var5)
            {
                this.dropItem(var3, 1);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        tagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
        NBTTagList var2 = new NBTTagList();
        NBTTagCompound var4;

        for (int var6 = 0; var6 < this.equipment.length; ++var6)
        {
            var4 = new NBTTagCompound();

            if (this.equipment[var6] != null)
            {
                this.equipment[var6].writeToNBT(var4);
            }

            var2.appendTag(var4);
        }

        tagCompound.setTag("Equipment", var2);
        NBTTagList var61 = new NBTTagList();

        for (int var5 = 0; var5 < this.equipmentDropChances.length; ++var5)
        {
            var61.appendTag(new NBTTagFloat(this.equipmentDropChances[var5]));
        }

        tagCompound.setTag("DropChances", var61);
        tagCompound.setBoolean("Leashed", this.isLeashed);

        if (this.leashedToEntity != null)
        {
            var4 = new NBTTagCompound();

            if (this.leashedToEntity instanceof EntityLivingBase)
            {
                var4.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
                var4.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
            }
            else if (this.leashedToEntity instanceof EntityHanging)
            {
                BlockPos var7 = ((EntityHanging)this.leashedToEntity).func_174857_n();
                var4.setInteger("X", var7.getX());
                var4.setInteger("Y", var7.getY());
                var4.setInteger("Z", var7.getZ());
            }

            tagCompound.setTag("Leash", var4);
        }

        if (this.isAIDisabled())
        {
            tagCompound.setBoolean("NoAI", this.isAIDisabled());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("CanPickUpLoot", 1))
        {
            this.setCanPickUpLoot(tagCompund.getBoolean("CanPickUpLoot"));
        }

        this.persistenceRequired = tagCompund.getBoolean("PersistenceRequired");
        NBTTagList var2;
        int var3;

        if (tagCompund.hasKey("Equipment", 9))
        {
            var2 = tagCompund.getTagList("Equipment", 10);

            for (var3 = 0; var3 < this.equipment.length; ++var3)
            {
                this.equipment[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
            }
        }

        if (tagCompund.hasKey("DropChances", 9))
        {
            var2 = tagCompund.getTagList("DropChances", 5);

            for (var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                this.equipmentDropChances[var3] = var2.getFloat(var3);
            }
        }

        this.isLeashed = tagCompund.getBoolean("Leashed");

        if (this.isLeashed && tagCompund.hasKey("Leash", 10))
        {
            this.leashNBTTag = tagCompund.getCompoundTag("Leash");
        }

        this.setNoAI(tagCompund.getBoolean("NoAI"));
    }

    public void setMoveForward(float p_70657_1_)
    {
        this.moveForward = p_70657_1_;
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float p_70659_1_)
    {
        super.setAIMoveSpeed(p_70659_1_);
        this.setMoveForward(p_70659_1_);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.worldObj.theProfiler.startSection("looting");

        if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
        {
            List var1 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D));
            Iterator var2 = var1.iterator();

            while (var2.hasNext())
            {
                EntityItem var3 = (EntityItem)var2.next();

                if (!var3.isDead && var3.getEntityItem() != null && !var3.func_174874_s())
                {
                    this.func_175445_a(var3);
                }
            }
        }

        this.worldObj.theProfiler.endSection();
    }

    protected void func_175445_a(EntityItem p_175445_1_)
    {
        ItemStack var2 = p_175445_1_.getEntityItem();
        int var3 = getArmorPosition(var2);

        if (var3 > -1)
        {
            boolean var4 = true;
            ItemStack var5 = this.getEquipmentInSlot(var3);

            if (var5 != null)
            {
                if (var3 == 0)
                {
                    if (var2.getItem() instanceof ItemSword && !(var5.getItem() instanceof ItemSword))
                    {
                        var4 = true;
                    }
                    else if (var2.getItem() instanceof ItemSword && var5.getItem() instanceof ItemSword)
                    {
                        ItemSword var9 = (ItemSword)var2.getItem();
                        ItemSword var10 = (ItemSword)var5.getItem();

                        if (var9.func_150931_i() == var10.func_150931_i())
                        {
                            var4 = var2.getMetadata() > var5.getMetadata() || var2.hasTagCompound() && !var5.hasTagCompound();
                        }
                        else
                        {
                            var4 = var9.func_150931_i() > var10.func_150931_i();
                        }
                    }
                    else if (var2.getItem() instanceof ItemBow && var5.getItem() instanceof ItemBow)
                    {
                        var4 = var2.hasTagCompound() && !var5.hasTagCompound();
                    }
                    else
                    {
                        var4 = false;
                    }
                }
                else if (var2.getItem() instanceof ItemArmor && !(var5.getItem() instanceof ItemArmor))
                {
                    var4 = true;
                }
                else if (var2.getItem() instanceof ItemArmor && var5.getItem() instanceof ItemArmor)
                {
                    ItemArmor var91 = (ItemArmor)var2.getItem();
                    ItemArmor var101 = (ItemArmor)var5.getItem();

                    if (var91.damageReduceAmount == var101.damageReduceAmount)
                    {
                        var4 = var2.getMetadata() > var5.getMetadata() || var2.hasTagCompound() && !var5.hasTagCompound();
                    }
                    else
                    {
                        var4 = var91.damageReduceAmount > var101.damageReduceAmount;
                    }
                }
                else
                {
                    var4 = false;
                }
            }

            if (var4 && this.func_175448_a(var2))
            {
                if (var5 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[var3])
                {
                    this.entityDropItem(var5, 0.0F);
                }

                if (var2.getItem() == Items.diamond && p_175445_1_.getThrower() != null)
                {
                    EntityPlayer var92 = this.worldObj.getPlayerEntityByName(p_175445_1_.getThrower());

                    if (var92 != null)
                    {
                        var92.triggerAchievement(AchievementList.diamondsToYou);
                    }
                }

                this.setCurrentItemOrArmor(var3, var2);
                this.equipmentDropChances[var3] = 2.0F;
                this.persistenceRequired = true;
                this.onItemPickup(p_175445_1_, 1);
                p_175445_1_.setDead();
            }
        }
    }

    protected boolean func_175448_a(ItemStack p_175448_1_)
    {
        return true;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return true;
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity()
    {
        Object result = null;
        Object Result_DEFAULT = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
        Object Result_DENY = Reflector.getFieldValue(Reflector.Event_Result_DENY);

        if (this.persistenceRequired)
        {
            this.entityAge = 0;
        }
        else if ((this.entityAge & 31) == 31 && (result = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, new Object[] {this})) != Result_DEFAULT)
        {
            if (result == Result_DENY)
            {
                this.entityAge = 0;
            }
            else
            {
                this.setDead();
            }
        }
        else
        {
            EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

            if (var1 != null)
            {
                double var2 = var1.posX - this.posX;
                double var4 = var1.posY - this.posY;
                double var6 = var1.posZ - this.posZ;
                double var8 = var2 * var2 + var4 * var4 + var6 * var6;

                if (this.canDespawn() && var8 > 16384.0D)
                {
                    this.setDead();
                }

                if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && var8 > 1024.0D && this.canDespawn())
                {
                    this.setDead();
                }
                else if (var8 < 1024.0D)
                {
                    this.entityAge = 0;
                }
            }
        }
    }

    protected final void updateEntityActionState()
    {
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

    protected void updateAITasks() {}

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 40;
    }

    /**
     * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
     */
    public void faceEntity(Entity p_70625_1_, float p_70625_2_, float p_70625_3_)
    {
        double var4 = p_70625_1_.posX - this.posX;
        double var8 = p_70625_1_.posZ - this.posZ;
        double var6;

        if (p_70625_1_ instanceof EntityLivingBase)
        {
            EntityLivingBase var14 = (EntityLivingBase)p_70625_1_;
            var6 = var14.posY + (double)var14.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
        }
        else
        {
            var6 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
        }

        double var141 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float)(-(Math.atan2(var6, var141) * 180.0D / Math.PI));
        this.rotationPitch = this.updateRotation(this.rotationPitch, var13, p_70625_3_);
        this.rotationYaw = this.updateRotation(this.rotationYaw, var12, p_70625_2_);
    }

    /**
     * Arguments: current rotation, intended rotation, max increment.
     */
    private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
    {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

        if (var4 > p_70663_3_)
        {
            var4 = p_70663_3_;
        }

        if (var4 < -p_70663_3_)
        {
            var4 = -p_70663_3_;
        }

        return p_70663_1_ + var4;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return true;
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    /**
     * Returns render size modifier
     */
    public float getRenderSizeModifier()
    {
        return 1.0F;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight()
    {
        if (this.getAttackTarget() == null)
        {
            return 3;
        }
        else
        {
            int var1 = (int)(this.getHealth() - this.getMaxHealth() * 0.33F);
            var1 -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;

            if (var1 < 0)
            {
                var1 = 0;
            }

            return var1 + 3;
        }
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return this.equipment[0];
    }

    /**
     * 0: Tool in Hand; 1-4: Armor
     */
    public ItemStack getEquipmentInSlot(int slotIn)
    {
        return this.equipment[slotIn];
    }

    public ItemStack getCurrentArmor(int slotIn)
    {
        return this.equipment[slotIn + 1];
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
    {
        this.equipment[slotIn] = stack;
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory()
    {
        return this.equipment;
    }

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean p_82160_1_, int p_82160_2_)
    {
        for (int var3 = 0; var3 < this.getInventory().length; ++var3)
        {
            ItemStack var4 = this.getEquipmentInSlot(var3);
            boolean var5 = this.equipmentDropChances[var3] > 1.0F;

            if (var4 != null && (p_82160_1_ || var5) && this.rand.nextFloat() - (float)p_82160_2_ * 0.01F < this.equipmentDropChances[var3])
            {
                if (!var5 && var4.isItemStackDamageable())
                {
                    int var6 = Math.max(var4.getMaxDamage() - 25, 1);
                    int var7 = var4.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(var6) + 1);

                    if (var7 > var6)
                    {
                        var7 = var6;
                    }

                    if (var7 < 1)
                    {
                        var7 = 1;
                    }

                    var4.setItemDamage(var7);
                }

                this.entityDropItem(var4, 0.0F);
            }
        }
    }

    protected void func_180481_a(DifficultyInstance p_180481_1_)
    {
        if (this.rand.nextFloat() < 0.15F * p_180481_1_.func_180170_c())
        {
            int var2 = this.rand.nextInt(2);
            float var3 = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var2;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var2;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var2;
            }

            for (int var4 = 3; var4 >= 0; --var4)
            {
                ItemStack var5 = this.getCurrentArmor(var4);

                if (var4 < 3 && this.rand.nextFloat() < var3)
                {
                    break;
                }

                if (var5 == null)
                {
                    Item var6 = getArmorItemForSlot(var4 + 1, var2);

                    if (var6 != null)
                    {
                        this.setCurrentItemOrArmor(var4 + 1, new ItemStack(var6));
                    }
                }
            }
        }
    }

    public static int getArmorPosition(ItemStack p_82159_0_)
    {
        if (p_82159_0_.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && p_82159_0_.getItem() != Items.skull)
        {
            if (p_82159_0_.getItem() instanceof ItemArmor)
            {
                switch (((ItemArmor)p_82159_0_.getItem()).armorType)
                {
                    case 0:
                        return 4;

                    case 1:
                        return 3;

                    case 2:
                        return 2;

                    case 3:
                        return 1;
                }
            }

            return 0;
        }
        else
        {
            return 4;
        }
    }

    /**
     * Gets the vanilla armor Item that can go in the slot specified for the given tier.
     */
    public static Item getArmorItemForSlot(int armorSlot, int itemTier)
    {
        switch (armorSlot)
        {
            case 4:
                if (itemTier == 0)
                {
                    return Items.leather_helmet;
                }
                else if (itemTier == 1)
                {
                    return Items.golden_helmet;
                }
                else if (itemTier == 2)
                {
                    return Items.chainmail_helmet;
                }
                else if (itemTier == 3)
                {
                    return Items.iron_helmet;
                }
                else if (itemTier == 4)
                {
                    return Items.diamond_helmet;
                }

            case 3:
                if (itemTier == 0)
                {
                    return Items.leather_chestplate;
                }
                else if (itemTier == 1)
                {
                    return Items.golden_chestplate;
                }
                else if (itemTier == 2)
                {
                    return Items.chainmail_chestplate;
                }
                else if (itemTier == 3)
                {
                    return Items.iron_chestplate;
                }
                else if (itemTier == 4)
                {
                    return Items.diamond_chestplate;
                }

            case 2:
                if (itemTier == 0)
                {
                    return Items.leather_leggings;
                }
                else if (itemTier == 1)
                {
                    return Items.golden_leggings;
                }
                else if (itemTier == 2)
                {
                    return Items.chainmail_leggings;
                }
                else if (itemTier == 3)
                {
                    return Items.iron_leggings;
                }
                else if (itemTier == 4)
                {
                    return Items.diamond_leggings;
                }

            case 1:
                if (itemTier == 0)
                {
                    return Items.leather_boots;
                }
                else if (itemTier == 1)
                {
                    return Items.golden_boots;
                }
                else if (itemTier == 2)
                {
                    return Items.chainmail_boots;
                }
                else if (itemTier == 3)
                {
                    return Items.iron_boots;
                }
                else if (itemTier == 4)
                {
                    return Items.diamond_boots;
                }

            default:
                return null;
        }
    }

    protected void func_180483_b(DifficultyInstance p_180483_1_)
    {
        float var2 = p_180483_1_.func_180170_c();

        if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25F * var2)
        {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0F + var2 * (float)this.rand.nextInt(18)));
        }

        for (int var3 = 0; var3 < 4; ++var3)
        {
            ItemStack var4 = this.getCurrentArmor(var3);

            if (var4 != null && this.rand.nextFloat() < 0.5F * var2)
            {
                EnchantmentHelper.addRandomEnchantment(this.rand, var4, (int)(5.0F + var2 * (float)this.rand.nextInt(18)));
            }
        }
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return p_180482_2_;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return false;
    }

    /**
     * Enable the Entity persistence
     */
    public void enablePersistence()
    {
        this.persistenceRequired = true;
    }

    public void setEquipmentDropChance(int p_96120_1_, float p_96120_2_)
    {
        this.equipmentDropChances[p_96120_1_] = p_96120_2_;
    }

    public boolean canPickUpLoot()
    {
        return this.canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean p_98053_1_)
    {
        this.canPickUpLoot = p_98053_1_;
    }

    public boolean isNoDespawnRequired()
    {
        return this.persistenceRequired;
    }

    /**
     * First layer of player interaction
     */
    public final boolean interactFirst(EntityPlayer playerIn)
    {
        if (this.getLeashed() && this.getLeashedToEntity() == playerIn)
        {
            this.clearLeashed(true, !playerIn.capabilities.isCreativeMode);
            return true;
        }
        else
        {
            ItemStack var2 = playerIn.inventory.getCurrentItem();

            if (var2 != null && var2.getItem() == Items.lead && this.allowLeashing())
            {
                if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed())
                {
                    this.setLeashedToEntity(playerIn, true);
                    --var2.stackSize;
                    return true;
                }

                if (((EntityTameable)this).func_152114_e(playerIn))
                {
                    this.setLeashedToEntity(playerIn, true);
                    --var2.stackSize;
                    return true;
                }
            }

            return this.interact(playerIn) ? true : super.interactFirst(playerIn);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    protected boolean interact(EntityPlayer player)
    {
        return false;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState()
    {
        if (this.leashNBTTag != null)
        {
            this.recreateLeash();
        }

        if (this.isLeashed)
        {
            if (!this.isEntityAlive())
            {
                this.clearLeashed(true, true);
            }

            if (this.leashedToEntity == null || this.leashedToEntity.isDead)
            {
                this.clearLeashed(true, true);
            }
        }
    }

    /**
     * Removes the leash from this entity. Second parameter tells whether to send a packet to surrounding players.
     */
    public void clearLeashed(boolean p_110160_1_, boolean p_110160_2_)
    {
        if (this.isLeashed)
        {
            this.isLeashed = false;
            this.leashedToEntity = null;

            if (!this.worldObj.isRemote && p_110160_2_)
            {
                this.dropItem(Items.lead, 1);
            }

            if (!this.worldObj.isRemote && p_110160_1_ && this.worldObj instanceof WorldServer)
            {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, (Entity)null));
            }
        }
    }

    public boolean allowLeashing()
    {
        return !this.getLeashed() && !(this instanceof IMob);
    }

    public boolean getLeashed()
    {
        return this.isLeashed;
    }

    public Entity getLeashedToEntity()
    {
        return this.leashedToEntity;
    }

    /**
     * Sets the entity to be leashed to.
     */
    public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification)
    {
        this.isLeashed = true;
        this.leashedToEntity = entityIn;

        if (!this.worldObj.isRemote && sendAttachNotification && this.worldObj instanceof WorldServer)
        {
            ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, this.leashedToEntity));
        }
    }

    private void recreateLeash()
    {
        if (this.isLeashed && this.leashNBTTag != null)
        {
            if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4))
            {
                UUID var11 = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
                List var21 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D));
                Iterator var3 = var21.iterator();

                while (var3.hasNext())
                {
                    EntityLivingBase var4 = (EntityLivingBase)var3.next();

                    if (var4.getUniqueID().equals(var11))
                    {
                        this.leashedToEntity = var4;
                        break;
                    }
                }
            }
            else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99))
            {
                BlockPos var1 = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot var2 = EntityLeashKnot.func_174863_b(this.worldObj, var1);

                if (var2 == null)
                {
                    var2 = EntityLeashKnot.func_174862_a(this.worldObj, var1);
                }

                this.leashedToEntity = var2;
            }
            else
            {
                this.clearLeashed(false, true);
            }
        }

        this.leashNBTTag = null;
    }

    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_)
    {
        int var3;

        if (p_174820_1_ == 99)
        {
            var3 = 0;
        }
        else
        {
            var3 = p_174820_1_ - 100 + 1;

            if (var3 < 0 || var3 >= this.equipment.length)
            {
                return false;
            }
        }

        if (p_174820_2_ != null && getArmorPosition(p_174820_2_) != var3 && (var3 != 4 || !(p_174820_2_.getItem() instanceof ItemBlock)))
        {
            return false;
        }
        else
        {
            this.setCurrentItemOrArmor(var3, p_174820_2_);
            return true;
        }
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return super.isServerWorld() && !this.isAIDisabled();
    }

    /**
     * Set whether this Entity's AI is disabled
     */
    protected void setNoAI(boolean p_94061_1_)
    {
        this.dataWatcher.updateObject(15, Byte.valueOf((byte)(p_94061_1_ ? 1 : 0)));
    }

    /**
     * Get whether this Entity's AI is disabled
     */
    private boolean isAIDisabled()
    {
        return this.dataWatcher.getWatchableObjectByte(15) != 0;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (this.noClip)
        {
            return false;
        }
        else
        {
            BlockPosM posM = new BlockPosM(0, 0, 0);

            for (int var1 = 0; var1 < 8; ++var1)
            {
                double var2 = this.posX + (double)(((float)((var1 >> 0) % 2) - 0.5F) * this.width * 0.8F);
                double var4 = this.posY + (double)(((float)((var1 >> 1) % 2) - 0.5F) * 0.1F);
                double var6 = this.posZ + (double)(((float)((var1 >> 2) % 2) - 0.5F) * this.width * 0.8F);
                posM.setXyz(var2, var4 + (double)this.getEyeHeight(), var6);

                if (this.worldObj.getBlockState(posM).getBlock().isVisuallyOpaque())
                {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean canSkipUpdate()
    {
        if (this.isChild())
        {
            return false;
        }
        else if (this.hurtTime > 0)
        {
            return false;
        }
        else if (this.ticksExisted < 20)
        {
            return false;
        }
        else
        {
            World world = this.getEntityWorld();

            if (world == null)
            {
                return false;
            }
            else if (world.playerEntities.size() != 1)
            {
                return false;
            }
            else
            {
                Entity player = (Entity)world.playerEntities.get(0);
                double dx = Math.max(Math.abs(this.posX - player.posX) - 16.0D, 0.0D);
                double dz = Math.max(Math.abs(this.posZ - player.posZ) - 16.0D, 0.0D);
                double distSq = dx * dx + dz * dz;
                return !this.isInRangeToRenderDist(distSq);
            }
        }
    }

    private void onUpdateMinimal()
    {
        ++this.entityAge;

        if (this instanceof EntityMob)
        {
            float brightness = this.getBrightness(1.0F);

            if (brightness > 0.5F)
            {
                this.entityAge += 2;
            }
        }

        this.despawnEntity();
    }

    public static enum SpawnPlacementType
    {
        ON_GROUND("ON_GROUND", 0, "ON_GROUND", 0),
        IN_AIR("IN_AIR", 1, "IN_AIR", 1),
        IN_WATER("IN_WATER", 2, "IN_WATER", 2);
        private static final EntityLiving.SpawnPlacementType[] $VALUES = new EntityLiving.SpawnPlacementType[]{ON_GROUND, IN_AIR, IN_WATER};
        private static final String __OBFID = "CL_00002255";

        private SpawnPlacementType(String p_i46393_1_, int p_i46393_2_, String p_i45893_1_, int p_i45893_2_) {}
    }
}
