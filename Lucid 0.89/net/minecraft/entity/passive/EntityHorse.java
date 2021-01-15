package net.minecraft.entity.passive;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHorse extends EntityAnimal implements IInvBasic
{
    private static final Predicate horseBreedingSelector = new Predicate()
    {
        public boolean func_179873_a(Entity p_179873_1_)
        {
            return p_179873_1_ instanceof EntityHorse && ((EntityHorse)p_179873_1_).isBreeding();
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.func_179873_a((Entity)p_apply_1_);
        }
    };
    private static final IAttribute horseJumpStrength = (new RangedAttribute((IAttribute)null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private static final String[] horseArmorTextures = new String[] {null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png"};
    private static final String[] HORSE_ARMOR_TEXTURES_ABBR = new String[] {"", "meo", "goo", "dio"};
    private static final int[] armorValues = new int[] {0, 5, 7, 11};
    private static final String[] horseTextures = new String[] {"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] HORSE_TEXTURES_ABBR = new String[] {"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] horseMarkingTextures = new String[] {null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[] {"", "wo_", "wmo", "wdo", "bdo"};
    private int eatingHaystackCounter;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int field_110278_bp;
    public int field_110279_bq;
    protected boolean horseJumping;
    private AnimalChest horseChest;
    private boolean hasReproduced;

    /**
     * "The higher this value, the more likely the horse is to be tamed next time a player rides it."
     */
    protected int temper;
    protected float jumpPower;
    private boolean field_110294_bI;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;

    /** Used to determine the sound that the horse should make when it steps */
    private int gallopTime;
    private String texturePrefix;
    private String[] horseTexturesArray = new String[3];
    private boolean field_175508_bO = false;

    public EntityHorse(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 1.6F);
        this.isImmuneToFire = false;
        this.setChested(false);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.initHorseChest();
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
        this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(20, Integer.valueOf(0));
        this.dataWatcher.addObject(21, String.valueOf(""));
        this.dataWatcher.addObject(22, Integer.valueOf(0));
    }

    public void setHorseType(int type)
    {
        this.dataWatcher.updateObject(19, Byte.valueOf((byte)type));
        this.resetTexturePrefix();
    }

    /**
     * Returns the horse type. 0 = Normal, 1 = Donkey, 2 = Mule, 3 = Undead Horse, 4 = Skeleton Horse
     */
    public int getHorseType()
    {
        return this.dataWatcher.getWatchableObjectByte(19);
    }

    public void setHorseVariant(int variant)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(variant));
        this.resetTexturePrefix();
    }

    public int getHorseVariant()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        if (this.hasCustomName())
        {
            return this.getCustomNameTag();
        }
        else
        {
            int var1 = this.getHorseType();

            switch (var1)
            {
                case 0:
                default:
                    return StatCollector.translateToLocal("entity.horse.name");

                case 1:
                    return StatCollector.translateToLocal("entity.donkey.name");

                case 2:
                    return StatCollector.translateToLocal("entity.mule.name");

                case 3:
                    return StatCollector.translateToLocal("entity.zombiehorse.name");

                case 4:
                    return StatCollector.translateToLocal("entity.skeletonhorse.name");
            }
        }
    }

    private boolean getHorseWatchableBoolean(int p_110233_1_)
    {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
    }

    private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_)
    {
        int var3 = this.dataWatcher.getWatchableObjectInt(16);

        if (p_110208_2_)
        {
            this.dataWatcher.updateObject(16, Integer.valueOf(var3 | p_110208_1_));
        }
        else
        {
            this.dataWatcher.updateObject(16, Integer.valueOf(var3 & ~p_110208_1_));
        }
    }

    public boolean isAdultHorse()
    {
        return !this.isChild();
    }

    public boolean isTame()
    {
        return this.getHorseWatchableBoolean(2);
    }

    public boolean func_110253_bW()
    {
        return this.isAdultHorse();
    }

    public String getOwnerId()
    {
        return this.dataWatcher.getWatchableObjectString(21);
    }

    public void setOwnerId(String id)
    {
        this.dataWatcher.updateObject(21, id);
    }

    public float getHorseSize()
    {
        int var1 = this.getGrowingAge();
        return var1 >= 0 ? 1.0F : 0.5F + (-24000 - var1) / -24000.0F * 0.5F;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    @Override
	public void setScaleForAge(boolean p_98054_1_)
    {
        if (p_98054_1_)
        {
            this.setScale(this.getHorseSize());
        }
        else
        {
            this.setScale(1.0F);
        }
    }

    public boolean isHorseJumping()
    {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean tamed)
    {
        this.setHorseWatchableBoolean(2, tamed);
    }

    public void setHorseJumping(boolean jumping)
    {
        this.horseJumping = jumping;
    }

    @Override
	public boolean allowLeashing()
    {
        return !this.isUndead() && super.allowLeashing();
    }

    @Override
	protected void func_142017_o(float p_142017_1_)
    {
        if (p_142017_1_ > 6.0F && this.isEatingHaystack())
        {
            this.setEatingHaystack(false);
        }
    }

    public boolean isChested()
    {
        return this.getHorseWatchableBoolean(8);
    }

    /**
     * Returns type of armor from DataWatcher (0 = iron, 1 = gold, 2 = diamond)
     */
    public int getHorseArmorIndexSynced()
    {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

    /**
     * 0 = iron, 1 = gold, 2 = diamond
     */
    private int getHorseArmorIndex(ItemStack itemStackIn)
    {
        if (itemStackIn == null)
        {
            return 0;
        }
        else
        {
            Item var2 = itemStackIn.getItem();
            return var2 == Items.iron_horse_armor ? 1 : (var2 == Items.golden_horse_armor ? 2 : (var2 == Items.diamond_horse_armor ? 3 : 0));
        }
    }

    public boolean isEatingHaystack()
    {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isRearing()
    {
        return this.getHorseWatchableBoolean(64);
    }

    public boolean isBreeding()
    {
        return this.getHorseWatchableBoolean(16);
    }

    public boolean getHasReproduced()
    {
        return this.hasReproduced;
    }

    /**
     * Set horse armor stack (for example: new ItemStack(NoRender.iron_horse_armor))
     */
    public void setHorseArmorStack(ItemStack itemStackIn)
    {
        this.dataWatcher.updateObject(22, Integer.valueOf(this.getHorseArmorIndex(itemStackIn)));
        this.resetTexturePrefix();
    }

    public void setBreeding(boolean breeding)
    {
        this.setHorseWatchableBoolean(16, breeding);
    }

    public void setChested(boolean chested)
    {
        this.setHorseWatchableBoolean(8, chested);
    }

    public void setHasReproduced(boolean hasReproducedIn)
    {
        this.hasReproduced = hasReproducedIn;
    }

    public void setHorseSaddled(boolean saddled)
    {
        this.setHorseWatchableBoolean(4, saddled);
    }

    public int getTemper()
    {
        return this.temper;
    }

    public void setTemper(int temperIn)
    {
        this.temper = temperIn;
    }

    public int increaseTemper(int p_110198_1_)
    {
        int var2 = MathHelper.clamp_int(this.getTemper() + p_110198_1_, 0, this.getMaxTemper());
        this.setTemper(var2);
        return var2;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity var3 = source.getEntity();
        return this.riddenByEntity != null && this.riddenByEntity.equals(var3) ? false : super.attackEntityFrom(source, amount);
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    @Override
	public int getTotalArmorValue()
    {
        return armorValues[this.getHorseArmorIndexSynced()];
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
	public boolean canBePushed()
    {
        return this.riddenByEntity == null;
    }

    public boolean prepareChunkForSpawn()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(new BlockPos(var1, 0, var2));
        return true;
    }

    public void dropChests()
    {
        if (!this.worldObj.isRemote && this.isChested())
        {
            this.dropItem(Item.getItemFromBlock(Blocks.chest), 1);
            this.setChested(false);
        }
    }

    private void func_110266_cB()
    {
        this.openHorseMouth();

        if (!this.isSilent())
        {
            this.worldObj.playSoundAtEntity(this, "eating", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }
    }

    @Override
	public void fall(float distance, float damageMultiplier)
    {
        if (distance > 1.0F)
        {
            this.playSound("mob.horse.land", 0.4F, 1.0F);
        }

        int var3 = MathHelper.ceiling_float_int((distance * 0.5F - 3.0F) * damageMultiplier);

        if (var3 > 0)
        {
            this.attackEntityFrom(DamageSource.fall, var3);

            if (this.riddenByEntity != null)
            {
                this.riddenByEntity.attackEntityFrom(DamageSource.fall, var3);
            }

            Block var4 = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ)).getBlock();

            if (var4.getMaterial() != Material.air && !this.isSilent())
            {
                Block.SoundType var5 = var4.stepSound;
                this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.5F, var5.getFrequency() * 0.75F);
            }
        }
    }

    /**
     * Returns number of slots depending horse type
     */
    private int getChestSize()
    {
        int var1 = this.getHorseType();
        return this.isChested() && (var1 == 1 || var1 == 2) ? 17 : 2;
    }

    private void initHorseChest()
    {
        AnimalChest var1 = this.horseChest;
        this.horseChest = new AnimalChest("HorseChest", this.getChestSize());
        this.horseChest.setCustomName(this.getCommandSenderName());

        if (var1 != null)
        {
            var1.func_110132_b(this);
            int var2 = Math.min(var1.getSizeInventory(), this.horseChest.getSizeInventory());

            for (int var3 = 0; var3 < var2; ++var3)
            {
                ItemStack var4 = var1.getStackInSlot(var3);

                if (var4 != null)
                {
                    this.horseChest.setInventorySlotContents(var3, var4.copy());
                }
            }
        }

        this.horseChest.func_110134_a(this);
        this.func_110232_cE();
    }

    private void func_110232_cE()
    {
        if (!this.worldObj.isRemote)
        {
            this.setHorseSaddled(this.horseChest.getStackInSlot(0) != null);

            if (this.canWearArmor())
            {
                this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
            }
        }
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    @Override
	public void onInventoryChanged(InventoryBasic p_76316_1_)
    {
        int var2 = this.getHorseArmorIndexSynced();
        boolean var3 = this.isHorseSaddled();
        this.func_110232_cE();

        if (this.ticksExisted > 20)
        {
            if (var2 == 0 && var2 != this.getHorseArmorIndexSynced())
            {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            }
            else if (var2 != this.getHorseArmorIndexSynced())
            {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            }

            if (!var3 && this.isHorseSaddled())
            {
                this.playSound("mob.horse.leather", 0.5F, 1.0F);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        this.prepareChunkForSpawn();
        return super.getCanSpawnHere();
    }

    protected EntityHorse getClosestHorse(Entity entityIn, double distance)
    {
        double var4 = Double.MAX_VALUE;
        Entity var6 = null;
        List var7 = this.worldObj.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), horseBreedingSelector);
        Iterator var8 = var7.iterator();

        while (var8.hasNext())
        {
            Entity var9 = (Entity)var8.next();
            double var10 = var9.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);

            if (var10 < var4)
            {
                var6 = var9;
                var4 = var10;
            }
        }

        return (EntityHorse)var6;
    }

    public double getHorseJumpStrength()
    {
        return this.getEntityAttribute(horseJumpStrength).getAttributeValue();
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        this.openHorseMouth();
        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.death" : (var1 == 4 ? "mob.horse.skeleton.death" : (var1 != 1 && var1 != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    @Override
	protected Item getDropItem()
    {
        boolean var1 = this.rand.nextInt(4) == 0;
        int var2 = this.getHorseType();
        return var2 == 4 ? Items.bone : (var2 == 3 ? (var1 ? null : Items.rotten_flesh) : Items.leather);
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        this.openHorseMouth();

        if (this.rand.nextInt(3) == 0)
        {
            this.makeHorseRear();
        }

        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.hit" : (var1 == 4 ? "mob.horse.skeleton.hit" : (var1 != 1 && var1 != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean isHorseSaddled()
    {
        return this.getHorseWatchableBoolean(4);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        this.openHorseMouth();

        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked())
        {
            this.makeHorseRear();
        }

        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.idle" : (var1 == 4 ? "mob.horse.skeleton.idle" : (var1 != 1 && var1 != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String getAngrySoundName()
    {
        this.openHorseMouth();
        this.makeHorseRear();
        int var1 = this.getHorseType();
        return var1 != 3 && var1 != 4 ? (var1 != 1 && var1 != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    @Override
	protected void playStepSound(BlockPos pos, Block blockIn)
    {
        Block.SoundType var3 = blockIn.stepSound;

        if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
        {
            var3 = Blocks.snow_layer.stepSound;
        }

        if (!blockIn.getMaterial().isLiquid())
        {
            int var4 = this.getHorseType();

            if (this.riddenByEntity != null && var4 != 1 && var4 != 2)
            {
                ++this.gallopTime;

                if (this.gallopTime > 5 && this.gallopTime % 3 == 0)
                {
                    this.playSound("mob.horse.gallop", var3.getVolume() * 0.15F, var3.getFrequency());

                    if (var4 == 0 && this.rand.nextInt(10) == 0)
                    {
                        this.playSound("mob.horse.breathe", var3.getVolume() * 0.6F, var3.getFrequency());
                    }
                }
                else if (this.gallopTime <= 5)
                {
                    this.playSound("mob.horse.wood", var3.getVolume() * 0.15F, var3.getFrequency());
                }
            }
            else if (var3 == Block.soundTypeWood)
            {
                this.playSound("mob.horse.wood", var3.getVolume() * 0.15F, var3.getFrequency());
            }
            else
            {
                this.playSound("mob.horse.soft", var3.getVolume() * 0.15F, var3.getFrequency());
            }
        }
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(horseJumpStrength);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
	public int getMaxSpawnedInChunk()
    {
        return 6;
    }

    public int getMaxTemper()
    {
        return 100;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
	protected float getSoundVolume()
    {
        return 0.8F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    @Override
	public int getTalkInterval()
    {
        return 400;
    }

    public boolean func_110239_cn()
    {
        return this.getHorseType() == 0 || this.getHorseArmorIndexSynced() > 0;
    }

    private void resetTexturePrefix()
    {
        this.texturePrefix = null;
    }

    public boolean func_175507_cI()
    {
        return this.field_175508_bO;
    }

    private void setHorseTexturePaths()
    {
        this.texturePrefix = "horse/";
        this.horseTexturesArray[0] = null;
        this.horseTexturesArray[1] = null;
        this.horseTexturesArray[2] = null;
        int var1 = this.getHorseType();
        int var2 = this.getHorseVariant();
        int var3;

        if (var1 == 0)
        {
            var3 = var2 & 255;
            int var4 = (var2 & 65280) >> 8;

            if (var3 >= horseTextures.length)
            {
                this.field_175508_bO = false;
                return;
            }

            this.horseTexturesArray[0] = horseTextures[var3];
            this.texturePrefix = this.texturePrefix + HORSE_TEXTURES_ABBR[var3];

            if (var4 >= horseMarkingTextures.length)
            {
                this.field_175508_bO = false;
                return;
            }

            this.horseTexturesArray[1] = horseMarkingTextures[var4];
            this.texturePrefix = this.texturePrefix + HORSE_MARKING_TEXTURES_ABBR[var4];
        }
        else
        {
            this.horseTexturesArray[0] = "";
            this.texturePrefix = this.texturePrefix + "_" + var1 + "_";
        }

        var3 = this.getHorseArmorIndexSynced();

        if (var3 >= horseArmorTextures.length)
        {
            this.field_175508_bO = false;
        }
        else
        {
            this.horseTexturesArray[2] = horseArmorTextures[var3];
            this.texturePrefix = this.texturePrefix + HORSE_ARMOR_TEXTURES_ABBR[var3];
            this.field_175508_bO = true;
        }
    }

    public String getHorseTexture()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }

        return this.texturePrefix;
    }

    public String[] getVariantTexturePaths()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }

        return this.horseTexturesArray;
    }

    public void openGUI(EntityPlayer playerEntity)
    {
        if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == playerEntity) && this.isTame())
        {
            this.horseChest.setCustomName(this.getCommandSenderName());
            playerEntity.displayGUIHorse(this, this.horseChest);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
	public boolean interact(EntityPlayer player)
    {
        ItemStack var2 = player.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.spawn_egg)
        {
            return super.interact(player);
        }
        else if (!this.isTame() && this.isUndead())
        {
            return false;
        }
        else if (this.isTame() && this.isAdultHorse() && player.isSneaking())
        {
            this.openGUI(player);
            return true;
        }
        else if (this.func_110253_bW() && this.riddenByEntity != null)
        {
            return super.interact(player);
        }
        else
        {
            if (var2 != null)
            {
                boolean var3 = false;

                if (this.canWearArmor())
                {
                    byte var4 = -1;

                    if (var2.getItem() == Items.iron_horse_armor)
                    {
                        var4 = 1;
                    }
                    else if (var2.getItem() == Items.golden_horse_armor)
                    {
                        var4 = 2;
                    }
                    else if (var2.getItem() == Items.diamond_horse_armor)
                    {
                        var4 = 3;
                    }

                    if (var4 >= 0)
                    {
                        if (!this.isTame())
                        {
                            this.makeHorseRearWithSound();
                            return true;
                        }

                        this.openGUI(player);
                        return true;
                    }
                }

                if (!var3 && !this.isUndead())
                {
                    float var7 = 0.0F;
                    short var5 = 0;
                    byte var6 = 0;

                    if (var2.getItem() == Items.wheat)
                    {
                        var7 = 2.0F;
                        var5 = 20;
                        var6 = 3;
                    }
                    else if (var2.getItem() == Items.sugar)
                    {
                        var7 = 1.0F;
                        var5 = 30;
                        var6 = 3;
                    }
                    else if (Block.getBlockFromItem(var2.getItem()) == Blocks.hay_block)
                    {
                        var7 = 20.0F;
                        var5 = 180;
                    }
                    else if (var2.getItem() == Items.apple)
                    {
                        var7 = 3.0F;
                        var5 = 60;
                        var6 = 3;
                    }
                    else if (var2.getItem() == Items.golden_carrot)
                    {
                        var7 = 4.0F;
                        var5 = 60;
                        var6 = 5;

                        if (this.isTame() && this.getGrowingAge() == 0)
                        {
                            var3 = true;
                            this.setInLove(player);
                        }
                    }
                    else if (var2.getItem() == Items.golden_apple)
                    {
                        var7 = 10.0F;
                        var5 = 240;
                        var6 = 10;

                        if (this.isTame() && this.getGrowingAge() == 0)
                        {
                            var3 = true;
                            this.setInLove(player);
                        }
                    }

                    if (this.getHealth() < this.getMaxHealth() && var7 > 0.0F)
                    {
                        this.heal(var7);
                        var3 = true;
                    }

                    if (!this.isAdultHorse() && var5 > 0)
                    {
                        this.addGrowth(var5);
                        var3 = true;
                    }

                    if (var6 > 0 && (var3 || !this.isTame()) && var6 < this.getMaxTemper())
                    {
                        var3 = true;
                        this.increaseTemper(var6);
                    }

                    if (var3)
                    {
                        this.func_110266_cB();
                    }
                }

                if (!this.isTame() && !var3)
                {
                    if (var2 != null && var2.interactWithEntity(player, this))
                    {
                        return true;
                    }

                    this.makeHorseRearWithSound();
                    return true;
                }

                if (!var3 && this.canCarryChest() && !this.isChested() && var2.getItem() == Item.getItemFromBlock(Blocks.chest))
                {
                    this.setChested(true);
                    this.playSound("mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    var3 = true;
                    this.initHorseChest();
                }

                if (!var3 && this.func_110253_bW() && !this.isHorseSaddled() && var2.getItem() == Items.saddle)
                {
                    this.openGUI(player);
                    return true;
                }

                if (var3)
                {
                    if (!player.capabilities.isCreativeMode && --var2.stackSize == 0)
                    {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                    }

                    return true;
                }
            }

            if (this.func_110253_bW() && this.riddenByEntity == null)
            {
                if (var2 != null && var2.interactWithEntity(player, this))
                {
                    return true;
                }
                else
                {
                    this.mountTo(player);
                    return true;
                }
            }
            else
            {
                return super.interact(player);
            }
        }
    }

    private void mountTo(EntityPlayer player)
    {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);

        if (!this.worldObj.isRemote)
        {
            player.mountEntity(this);
        }
    }

    /**
     * Return true if the horse entity can wear an armor
     */
    public boolean canWearArmor()
    {
        return this.getHorseType() == 0;
    }

    /**
     * Return true if the horse entity can carry a chest.
     */
    public boolean canCarryChest()
    {
        int var1 = this.getHorseType();
        return var1 == 2 || var1 == 1;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    @Override
	protected boolean isMovementBlocked()
    {
        return this.riddenByEntity != null && this.isHorseSaddled() ? true : this.isEatingHaystack() || this.isRearing();
    }

    /**
     * Used to know if the horse can be leashed, if he can mate, or if we can interact with him
     */
    public boolean isUndead()
    {
        int var1 = this.getHorseType();
        return var1 == 3 || var1 == 4;
    }

    /**
     * Return true if the horse entity is sterile (Undead || Mule)
     */
    public boolean isSterile()
    {
        return this.isUndead() || this.getHorseType() == 2;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
	public boolean isBreedingItem(ItemStack stack)
    {
        return false;
    }

    private void func_110210_cH()
    {
        this.field_110278_bp = 1;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
	public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (!this.worldObj.isRemote)
        {
            this.dropChestItems();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
	public void onLivingUpdate()
    {
        if (this.rand.nextInt(200) == 0)
        {
            this.func_110210_cH();
        }

        super.onLivingUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
            {
                this.heal(1.0F);
            }

            if (!this.isEatingHaystack() && this.riddenByEntity == null && this.rand.nextInt(300) == 0 && this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ))).getBlock() == Blocks.grass)
            {
                this.setEatingHaystack(true);
            }

            if (this.isEatingHaystack() && ++this.eatingHaystackCounter > 50)
            {
                this.eatingHaystackCounter = 0;
                this.setEatingHaystack(false);
            }

            if (this.isBreeding() && !this.isAdultHorse() && !this.isEatingHaystack())
            {
                EntityHorse var1 = this.getClosestHorse(this, 16.0D);

                if (var1 != null && this.getDistanceSqToEntity(var1) > 4.0D)
                {
                    this.navigator.getPathToEntityLiving(var1);
                }
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isRemote && this.dataWatcher.hasObjectChanged())
        {
            this.dataWatcher.func_111144_e();
            this.resetTexturePrefix();
        }

        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30)
        {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(128, false);
        }

        if (!this.worldObj.isRemote && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20)
        {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }

        if (this.field_110278_bp > 0 && ++this.field_110278_bp > 8)
        {
            this.field_110278_bp = 0;
        }

        if (this.field_110279_bq > 0)
        {
            ++this.field_110279_bq;

            if (this.field_110279_bq > 300)
            {
                this.field_110279_bq = 0;
            }
        }

        this.prevHeadLean = this.headLean;

        if (this.isEatingHaystack())
        {
            this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;

            if (this.headLean > 1.0F)
            {
                this.headLean = 1.0F;
            }
        }
        else
        {
            this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;

            if (this.headLean < 0.0F)
            {
                this.headLean = 0.0F;
            }
        }

        this.prevRearingAmount = this.rearingAmount;

        if (this.isRearing())
        {
            this.prevHeadLean = this.headLean = 0.0F;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;

            if (this.rearingAmount > 1.0F)
            {
                this.rearingAmount = 1.0F;
            }
        }
        else
        {
            this.field_110294_bI = false;
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;

            if (this.rearingAmount < 0.0F)
            {
                this.rearingAmount = 0.0F;
            }
        }

        this.prevMouthOpenness = this.mouthOpenness;

        if (this.getHorseWatchableBoolean(128))
        {
            this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;

            if (this.mouthOpenness > 1.0F)
            {
                this.mouthOpenness = 1.0F;
            }
        }
        else
        {
            this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;

            if (this.mouthOpenness < 0.0F)
            {
                this.mouthOpenness = 0.0F;
            }
        }
    }

    private void openHorseMouth()
    {
        if (!this.worldObj.isRemote)
        {
            this.openMouthCounter = 1;
            this.setHorseWatchableBoolean(128, true);
        }
    }

    /**
     * Return true if the horse entity ready to mate. (no rider, not riding, tame, adult, not steril...)
     */
    private boolean canMate()
    {
        return this.riddenByEntity == null && this.ridingEntity == null && this.isTame() && this.isAdultHorse() && !this.isSterile() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Override
	public void setEating(boolean eating)
    {
        this.setHorseWatchableBoolean(32, eating);
    }

    public void setEatingHaystack(boolean p_110227_1_)
    {
        this.setEating(p_110227_1_);
    }

    public void setRearing(boolean rearing)
    {
        if (rearing)
        {
            this.setEatingHaystack(false);
        }

        this.setHorseWatchableBoolean(64, rearing);
    }

    private void makeHorseRear()
    {
        if (!this.worldObj.isRemote)
        {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }
    }

    public void makeHorseRearWithSound()
    {
        this.makeHorseRear();
        String var1 = this.getAngrySoundName();

        if (var1 != null)
        {
            this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void dropChestItems()
    {
        this.dropItemsInChest(this, this.horseChest);
        this.dropChests();
    }

    private void dropItemsInChest(Entity entityIn, AnimalChest animalChestIn)
    {
        if (animalChestIn != null && !this.worldObj.isRemote)
        {
            for (int var3 = 0; var3 < animalChestIn.getSizeInventory(); ++var3)
            {
                ItemStack var4 = animalChestIn.getStackInSlot(var3);

                if (var4 != null)
                {
                    this.entityDropItem(var4, 0.0F);
                }
            }
        }
    }

    public boolean setTamedBy(EntityPlayer player)
    {
        this.setOwnerId(player.getUniqueID().toString());
        this.setHorseTamed(true);
        return true;
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    @Override
	public void moveEntityWithHeading(float strafe, float forward)
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase && this.isHorseSaddled())
        {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafe = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
            forward = ((EntityLivingBase)this.riddenByEntity).moveForward;

            if (forward <= 0.0F)
            {
                forward *= 0.25F;
                this.gallopTime = 0;
            }

            if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.field_110294_bI)
            {
                strafe = 0.0F;
                forward = 0.0F;
            }

            if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround)
            {
                this.motionY = this.getHorseJumpStrength() * this.jumpPower;

                if (this.isPotionActive(Potion.jump))
                {
                    this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
                }

                this.setHorseJumping(true);
                this.isAirBorne = true;

                if (forward > 0.0F)
                {
                    float var3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                    float var4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                    this.motionX += -0.4F * var3 * this.jumpPower;
                    this.motionZ += 0.4F * var4 * this.jumpPower;
                    this.playSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote)
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(strafe, forward);
            }

            if (this.onGround)
            {
                this.jumpPower = 0.0F;
                this.setHorseJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double var8 = this.posX - this.prevPosX;
            double var5 = this.posZ - this.prevPosZ;
            float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;

            if (var7 > 1.0F)
            {
                var7 = 1.0F;
            }

            this.limbSwingAmount += (var7 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(strafe, forward);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("EatingHaystack", this.isEatingHaystack());
        tagCompound.setBoolean("ChestedHorse", this.isChested());
        tagCompound.setBoolean("HasReproduced", this.getHasReproduced());
        tagCompound.setBoolean("Bred", this.isBreeding());
        tagCompound.setInteger("Type", this.getHorseType());
        tagCompound.setInteger("Variant", this.getHorseVariant());
        tagCompound.setInteger("Temper", this.getTemper());
        tagCompound.setBoolean("Tame", this.isTame());
        tagCompound.setString("OwnerUUID", this.getOwnerId());

        if (this.isChested())
        {
            NBTTagList var2 = new NBTTagList();

            for (int var3 = 2; var3 < this.horseChest.getSizeInventory(); ++var3)
            {
                ItemStack var4 = this.horseChest.getStackInSlot(var3);

                if (var4 != null)
                {
                    NBTTagCompound var5 = new NBTTagCompound();
                    var5.setByte("Slot", (byte)var3);
                    var4.writeToNBT(var5);
                    var2.appendTag(var5);
                }
            }

            tagCompound.setTag("NoRender", var2);
        }

        if (this.horseChest.getStackInSlot(1) != null)
        {
            tagCompound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }

        if (this.horseChest.getStackInSlot(0) != null)
        {
            tagCompound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setEatingHaystack(tagCompund.getBoolean("EatingHaystack"));
        this.setBreeding(tagCompund.getBoolean("Bred"));
        this.setChested(tagCompund.getBoolean("ChestedHorse"));
        this.setHasReproduced(tagCompund.getBoolean("HasReproduced"));
        this.setHorseType(tagCompund.getInteger("Type"));
        this.setHorseVariant(tagCompund.getInteger("Variant"));
        this.setTemper(tagCompund.getInteger("Temper"));
        this.setHorseTamed(tagCompund.getBoolean("Tame"));
        String var2 = "";

        if (tagCompund.hasKey("OwnerUUID", 8))
        {
            var2 = tagCompund.getString("OwnerUUID");
        }
        else
        {
            String var3 = tagCompund.getString("Owner");
            var2 = PreYggdrasilConverter.func_152719_a(var3);
        }

        if (var2.length() > 0)
        {
            this.setOwnerId(var2);
        }

        IAttributeInstance var8 = this.getAttributeMap().getAttributeInstanceByName("Speed");

        if (var8 != null)
        {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var8.getBaseValue() * 0.25D);
        }

        if (this.isChested())
        {
            NBTTagList var4 = tagCompund.getTagList("NoRender", 10);
            this.initHorseChest();

            for (int var5 = 0; var5 < var4.tagCount(); ++var5)
            {
                NBTTagCompound var6 = var4.getCompoundTagAt(var5);
                int var7 = var6.getByte("Slot") & 255;

                if (var7 >= 2 && var7 < this.horseChest.getSizeInventory())
                {
                    this.horseChest.setInventorySlotContents(var7, ItemStack.loadItemStackFromNBT(var6));
                }
            }
        }

        ItemStack var9;

        if (tagCompund.hasKey("ArmorItem", 10))
        {
            var9 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("ArmorItem"));

            if (var9 != null && isArmorItem(var9.getItem()))
            {
                this.horseChest.setInventorySlotContents(1, var9);
            }
        }

        if (tagCompund.hasKey("SaddleItem", 10))
        {
            var9 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("SaddleItem"));

            if (var9 != null && var9.getItem() == Items.saddle)
            {
                this.horseChest.setInventorySlotContents(0, var9);
            }
        }
        else if (tagCompund.getBoolean("Saddle"))
        {
            this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }

        this.func_110232_cE();
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
	public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (otherAnimal.getClass() != this.getClass())
        {
            return false;
        }
        else
        {
            EntityHorse var2 = (EntityHorse)otherAnimal;

            if (this.canMate() && var2.canMate())
            {
                int var3 = this.getHorseType();
                int var4 = var2.getHorseType();
                return var3 == var4 || var3 == 0 && var4 == 1 || var3 == 1 && var4 == 0;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
	public EntityAgeable createChild(EntityAgeable ageable)
    {
        EntityHorse var2 = (EntityHorse)ageable;
        EntityHorse var3 = new EntityHorse(this.worldObj);
        int var4 = this.getHorseType();
        int var5 = var2.getHorseType();
        int var6 = 0;

        if (var4 == var5)
        {
            var6 = var4;
        }
        else if (var4 == 0 && var5 == 1 || var4 == 1 && var5 == 0)
        {
            var6 = 2;
        }

        if (var6 == 0)
        {
            int var8 = this.rand.nextInt(9);
            int var7;

            if (var8 < 4)
            {
                var7 = this.getHorseVariant() & 255;
            }
            else if (var8 < 8)
            {
                var7 = var2.getHorseVariant() & 255;
            }
            else
            {
                var7 = this.rand.nextInt(7);
            }

            int var9 = this.rand.nextInt(5);

            if (var9 < 2)
            {
                var7 |= this.getHorseVariant() & 65280;
            }
            else if (var9 < 4)
            {
                var7 |= var2.getHorseVariant() & 65280;
            }
            else
            {
                var7 |= this.rand.nextInt(5) << 8 & 65280;
            }

            var3.setHorseVariant(var7);
        }

        var3.setHorseType(var6);
        double var13 = this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + this.getModifiedMaxHealth();
        var3.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(var13 / 3.0D);
        double var14 = this.getEntityAttribute(horseJumpStrength).getBaseValue() + ageable.getEntityAttribute(horseJumpStrength).getBaseValue() + this.getModifiedJumpStrength();
        var3.getEntityAttribute(horseJumpStrength).setBaseValue(var14 / 3.0D);
        double var11 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + this.getModifiedMovementSpeed();
        var3.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var11 / 3.0D);
        return var3;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        Object livingdata1 = super.onInitialSpawn(difficulty, livingdata);
        int var4 = 0;
        int var8;

        if (livingdata1 instanceof EntityHorse.GroupData)
        {
            var8 = ((EntityHorse.GroupData)livingdata1).horseType;
            var4 = ((EntityHorse.GroupData)livingdata1).horseVariant & 255 | this.rand.nextInt(5) << 8;
        }
        else
        {
            if (this.rand.nextInt(10) == 0)
            {
                var8 = 1;
            }
            else
            {
                int var5 = this.rand.nextInt(7);
                int var6 = this.rand.nextInt(5);
                var8 = 0;
                var4 = var5 | var6 << 8;
            }

            livingdata1 = new EntityHorse.GroupData(var8, var4);
        }

        this.setHorseType(var8);
        this.setHorseVariant(var4);

        if (this.rand.nextInt(5) == 0)
        {
            this.setGrowingAge(-24000);
        }

        if (var8 != 4 && var8 != 3)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getModifiedMaxHealth());

            if (var8 == 0)
            {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.getModifiedMovementSpeed());
            }
            else
            {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.17499999701976776D);
            }
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
        }

        if (var8 != 2 && var8 != 1)
        {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(this.getModifiedJumpStrength());
        }
        else
        {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
        }

        this.setHealth(this.getMaxHealth());
        return (IEntityLivingData)livingdata1;
    }

    public float getGrassEatingAmount(float p_110258_1_)
    {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }

    public float getRearingAmount(float p_110223_1_)
    {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }

    public float getMouthOpennessAngle(float p_110201_1_)
    {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }

    public void setJumpPower(int jumpPowerIn)
    {
        if (this.isHorseSaddled())
        {
            if (jumpPowerIn < 0)
            {
                jumpPowerIn = 0;
            }
            else
            {
                this.field_110294_bI = true;
                this.makeHorseRear();
            }

            if (jumpPowerIn >= 90)
            {
                this.jumpPower = 1.0F;
            }
            else
            {
                this.jumpPower = 0.4F + 0.4F * jumpPowerIn / 90.0F;
            }
        }
    }

    /**
     * "Spawns particles for the horse entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
     */
    protected void spawnHorseParticles(boolean p_110216_1_)
    {
        EnumParticleTypes var2 = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;

        for (int var3 = 0; var3 < 7; ++var3)
        {
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            double var8 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(var2, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var4, var6, var8, new int[0]);
        }
    }

    @Override
	public void handleHealthUpdate(byte id)
    {
        if (id == 7)
        {
            this.spawnHorseParticles(true);
        }
        else if (id == 6)
        {
            this.spawnHorseParticles(false);
        }
        else
        {
            super.handleHealthUpdate(id);
        }
    }

    @Override
	public void updateRiderPosition()
    {
        super.updateRiderPosition();

        if (this.prevRearingAmount > 0.0F)
        {
            float var1 = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0F);
            float var2 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0F);
            float var3 = 0.7F * this.prevRearingAmount;
            float var4 = 0.15F * this.prevRearingAmount;
            this.riddenByEntity.setPosition(this.posX + var3 * var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + var4, this.posZ - var3 * var2);

            if (this.riddenByEntity instanceof EntityLivingBase)
            {
                ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
            }
        }
    }

    /**
     * Returns randomized max health
     */
    private float getModifiedMaxHealth()
    {
        return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
    }

    /**
     * Returns randomized jump strength
     */
    private double getModifiedJumpStrength()
    {
        return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
    }

    /**
     * Returns randomized movement speed
     */
    private double getModifiedMovementSpeed()
    {
        return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
    }

    /**
     * Returns true if given item is horse armor
     */
    public static boolean isArmorItem(Item p_146085_0_)
    {
        return p_146085_0_ == Items.iron_horse_armor || p_146085_0_ == Items.golden_horse_armor || p_146085_0_ == Items.diamond_horse_armor;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
	public boolean isOnLadder()
    {
        return false;
    }

    @Override
	public float getEyeHeight()
    {
        return this.height;
    }

    @Override
	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (inventorySlot == 499 && this.canCarryChest())
        {
            if (itemStackIn == null && this.isChested())
            {
                this.setChested(false);
                this.initHorseChest();
                return true;
            }

            if (itemStackIn != null && itemStackIn.getItem() == Item.getItemFromBlock(Blocks.chest) && !this.isChested())
            {
                this.setChested(true);
                this.initHorseChest();
                return true;
            }
        }

        int var3 = inventorySlot - 400;

        if (var3 >= 0 && var3 < 2 && var3 < this.horseChest.getSizeInventory())
        {
            if (var3 == 0 && itemStackIn != null && itemStackIn.getItem() != Items.saddle)
            {
                return false;
            }
            else if (var3 == 1 && (itemStackIn != null && !isArmorItem(itemStackIn.getItem()) || !this.canWearArmor()))
            {
                return false;
            }
            else
            {
                this.horseChest.setInventorySlotContents(var3, itemStackIn);
                this.func_110232_cE();
                return true;
            }
        }
        else
        {
            int var4 = inventorySlot - 500 + 2;

            if (var4 >= 2 && var4 < this.horseChest.getSizeInventory())
            {
                this.horseChest.setInventorySlotContents(var4, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public static class GroupData implements IEntityLivingData
    {
        public int horseType;
        public int horseVariant;

        public GroupData(int type, int variant)
        {
            this.horseType = type;
            this.horseVariant = variant;
        }
    }
}
