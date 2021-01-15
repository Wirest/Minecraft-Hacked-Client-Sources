package net.minecraft.entity.passive;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;

    /** This villager's current customer. */
    private EntityPlayer buyingPlayer;

    /** Initialises the MerchantRecipeList.java */
    private MerchantRecipeList buyingList;
    private int timeUntilReset;

    /** addDefaultEquipmentAndRecipies is called if this is true */
    private boolean needsInitilization;
    private boolean isWillingToTrade;
    private int wealth;

    /** Last player to trade with this villager, used for aggressivity. */
    private String lastBuyingPlayer;
    private int careerId;

    /** This is the EntityVillager's career level value */
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private InventoryBasic villagerInventory;

    /**
     * A multi-dimensional array mapping the various professions, careers and career levels that a Villager may offer
     */
    private static final EntityVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityVillager.ITradeList[][][][] {{{{new EntityVillager.EmeraldForItems(Items.wheat, new EntityVillager.PriceInfo(18, 22)), new EntityVillager.EmeraldForItems(Items.potato, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.EmeraldForItems(Items.carrot, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.ListItemForEmeralds(Items.bread, new EntityVillager.PriceInfo(-4, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new EntityVillager.PriceInfo(8, 13)), new EntityVillager.ListItemForEmeralds(Items.pumpkin_pie, new EntityVillager.PriceInfo(-3, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new EntityVillager.PriceInfo(7, 12)), new EntityVillager.ListItemForEmeralds(Items.apple, new EntityVillager.PriceInfo(-5, -7))}, {new EntityVillager.ListItemForEmeralds(Items.cookie, new EntityVillager.PriceInfo(-6, -10)), new EntityVillager.ListItemForEmeralds(Items.cake, new EntityVillager.PriceInfo(1, 1))}}, {{new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ItemAndEmeraldToItem(Items.fish, new EntityVillager.PriceInfo(6, 6), Items.cooked_fish, new EntityVillager.PriceInfo(6, 6))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.fishing_rod, new EntityVillager.PriceInfo(7, 8))}}, {{new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new EntityVillager.PriceInfo(16, 22)), new EntityVillager.ListItemForEmeralds(Items.shears, new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new EntityVillager.PriceInfo(1, 2))}}, {{new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.ListItemForEmeralds(Items.arrow, new EntityVillager.PriceInfo(-12, -8))}, {new EntityVillager.ListItemForEmeralds(Items.bow, new EntityVillager.PriceInfo(2, 3)), new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new EntityVillager.PriceInfo(10, 10), Items.flint, new EntityVillager.PriceInfo(6, 10))}}}, {{{new EntityVillager.EmeraldForItems(Items.paper, new EntityVillager.PriceInfo(24, 36)), new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.EmeraldForItems(Items.book, new EntityVillager.PriceInfo(8, 10)), new EntityVillager.ListItemForEmeralds(Items.compass, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.EmeraldForItems(Items.written_book, new EntityVillager.PriceInfo(2, 2)), new EntityVillager.ListItemForEmeralds(Items.clock, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new EntityVillager.PriceInfo(-5, -3))}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListItemForEmeralds(Items.name_tag, new EntityVillager.PriceInfo(20, 22))}}}, {{{new EntityVillager.EmeraldForItems(Items.rotten_flesh, new EntityVillager.PriceInfo(36, 40)), new EntityVillager.EmeraldForItems(Items.gold_ingot, new EntityVillager.PriceInfo(8, 10))}, {new EntityVillager.ListItemForEmeralds(Items.redstone, new EntityVillager.PriceInfo(-4, -1)), new EntityVillager.ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityVillager.PriceInfo(-2, -1))}, {new EntityVillager.ListItemForEmeralds(Items.ender_eye, new EntityVillager.PriceInfo(7, 11)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new EntityVillager.PriceInfo(-3, -1))}, {new EntityVillager.ListItemForEmeralds(Items.experience_bottle, new EntityVillager.PriceInfo(3, 11))}}}, {{{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.iron_helmet, new EntityVillager.PriceInfo(4, 6))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListItemForEmeralds(Items.iron_chestplate, new EntityVillager.PriceInfo(10, 14))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate, new EntityVillager.PriceInfo(16, 19))}, {new EntityVillager.ListItemForEmeralds(Items.chainmail_boots, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.chainmail_leggings, new EntityVillager.PriceInfo(9, 11)), new EntityVillager.ListItemForEmeralds(Items.chainmail_helmet, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.chainmail_chestplate, new EntityVillager.PriceInfo(11, 15))}}, {{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.iron_axe, new EntityVillager.PriceInfo(6, 8))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_sword, new EntityVillager.PriceInfo(9, 10))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_sword, new EntityVillager.PriceInfo(12, 15)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_axe, new EntityVillager.PriceInfo(9, 12))}}, {{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_shovel, new EntityVillager.PriceInfo(5, 7))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe, new EntityVillager.PriceInfo(9, 11))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new EntityVillager.PriceInfo(12, 15))}}}, {{{new EntityVillager.EmeraldForItems(Items.porkchop, new EntityVillager.PriceInfo(14, 18)), new EntityVillager.EmeraldForItems(Items.chicken, new EntityVillager.PriceInfo(14, 18))}, {new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.cooked_porkchop, new EntityVillager.PriceInfo(-7, -5)), new EntityVillager.ListItemForEmeralds(Items.cooked_chicken, new EntityVillager.PriceInfo(-8, -6))}}, {{new EntityVillager.EmeraldForItems(Items.leather, new EntityVillager.PriceInfo(9, 12)), new EntityVillager.ListItemForEmeralds(Items.leather_leggings, new EntityVillager.PriceInfo(2, 4))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate, new EntityVillager.PriceInfo(7, 12))}, {new EntityVillager.ListItemForEmeralds(Items.saddle, new EntityVillager.PriceInfo(8, 10))}}}};

    public EntityVillager(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityVillager(World worldIn, int professionId)
    {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("NoRender", false, 8);
        this.setProfession(professionId);
        this.setSize(0.6F, 1.8F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, new Predicate()
        {
            public boolean isApplicable(Entity entityIn)
            {
                return entityIn instanceof EntityZombie;
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.isApplicable((Entity)p_apply_1_);
            }
        }, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.setCanPickUpLoot(true);
    }

    private void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true;

            if (this.isChild())
            {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            }
            else if (this.getProfession() == 0)
            {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    @Override
	protected void onGrowingAdult()
    {
        if (this.getProfession() == 0)
        {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.onGrowingAdult();
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    @Override
	protected void updateAITasks()
    {
        if (--this.randomTickDivider <= 0)
        {
            BlockPos var1 = new BlockPos(this);
            this.worldObj.getVillageCollection().addToVillagerPositionList(var1);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(var1, 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                BlockPos var2 = this.villageObj.getCenter();
                this.setHomePosAndDistance(var2, (int)(this.villageObj.getVillageRadius() * 1.0F));

                if (this.isLookingForHome)
                {
                    this.isLookingForHome = false;
                    this.villageObj.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    Iterator var3 = this.buyingList.iterator();

                    while (var3.hasNext())
                    {
                        MerchantRecipe var4 = (MerchantRecipe)var3.next();

                        if (var4.isRecipeDisabled())
                        {
                            var4.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.populateBuyingList();
                    this.needsInitilization = false;

                    if (this.villageObj != null && this.lastBuyingPlayer != null)
                    {
                        this.worldObj.setEntityState(this, (byte)14);
                        this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
                    }
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }

        super.updateAITasks();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
	public boolean interact(EntityPlayer player)
    {
        ItemStack var2 = player.inventory.getCurrentItem();
        boolean var3 = var2 != null && var2.getItem() == Items.spawn_egg;

        if (!var3 && this.isEntityAlive() && !this.isTrading() && !this.isChild())
        {
            if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0))
            {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }

            player.triggerAchievement(StatList.timesTalkedToVillagerStat);
            return true;
        }
        else
        {
            return super.interact(player);
        }
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Profession", this.getProfession());
        tagCompound.setInteger("Riches", this.wealth);
        tagCompound.setInteger("Career", this.careerId);
        tagCompound.setInteger("CareerLevel", this.careerLevel);
        tagCompound.setBoolean("Willing", this.isWillingToTrade);

        if (this.buyingList != null)
        {
            tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.villagerInventory.getSizeInventory(); ++var3)
        {
            ItemStack var4 = this.villagerInventory.getStackInSlot(var3);

            if (var4 != null)
            {
                var2.appendTag(var4.writeToNBT(new NBTTagCompound()));
            }
        }

        tagCompound.setTag("Inventory", var2);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setProfession(tagCompund.getInteger("Profession"));
        this.wealth = tagCompund.getInteger("Riches");
        this.careerId = tagCompund.getInteger("Career");
        this.careerLevel = tagCompund.getInteger("CareerLevel");
        this.isWillingToTrade = tagCompund.getBoolean("Willing");

        if (tagCompund.hasKey("Offers", 10))
        {
            NBTTagCompound var2 = tagCompund.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(var2);
        }

        NBTTagList var5 = tagCompund.getTagList("Inventory", 10);

        for (int var3 = 0; var3 < var5.tagCount(); ++var3)
        {
            ItemStack var4 = ItemStack.loadItemStackFromNBT(var5.getCompoundTagAt(var3));

            if (var4 != null)
            {
                this.villagerInventory.func_174894_a(var4);
            }
        }

        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
	protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.villager.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.villager.death";
    }

    public void setProfession(int professionId)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(professionId));
    }

    public int getProfession()
    {
        return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean mating)
    {
        this.isMating = mating;
    }

    public void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    @Override
	public void setRevengeTarget(EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (this.villageObj != null && livingBase != null)
        {
            this.villageObj.addOrRenewAgressor(livingBase);

            if (livingBase instanceof EntityPlayer)
            {
                byte var2 = -1;

                if (this.isChild())
                {
                    var2 = -3;
                }

                this.villageObj.setReputationForPlayer(livingBase.getCommandSenderName(), var2);

                if (this.isEntityAlive())
                {
                    this.worldObj.setEntityState(this, (byte)13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
	public void onDeath(DamageSource cause)
    {
        if (this.villageObj != null)
        {
            Entity var2 = cause.getEntity();

            if (var2 != null)
            {
                if (var2 instanceof EntityPlayer)
                {
                    this.villageObj.setReputationForPlayer(var2.getCommandSenderName(), -2);
                }
                else if (var2 instanceof IMob)
                {
                    this.villageObj.endMatingSeason();
                }
            }
            else
            {
                EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

                if (var3 != null)
                {
                    this.villageObj.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    @Override
	public void setCustomer(EntityPlayer p_70932_1_)
    {
        this.buyingPlayer = p_70932_1_;
    }

    @Override
	public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    public boolean func_175550_n(boolean p_175550_1_)
    {
        if (!this.isWillingToTrade && p_175550_1_ && this.func_175553_cp())
        {
            boolean var2 = false;

            for (int var3 = 0; var3 < this.villagerInventory.getSizeInventory(); ++var3)
            {
                ItemStack var4 = this.villagerInventory.getStackInSlot(var3);

                if (var4 != null)
                {
                    if (var4.getItem() == Items.bread && var4.stackSize >= 3)
                    {
                        var2 = true;
                        this.villagerInventory.decrStackSize(var3, 3);
                    }
                    else if ((var4.getItem() == Items.potato || var4.getItem() == Items.carrot) && var4.stackSize >= 12)
                    {
                        var2 = true;
                        this.villagerInventory.decrStackSize(var3, 12);
                    }
                }

                if (var2)
                {
                    this.worldObj.setEntityState(this, (byte)18);
                    this.isWillingToTrade = true;
                    break;
                }
            }
        }

        return this.isWillingToTrade;
    }

    public void setIsWillingToTrade(boolean willingToTrade)
    {
        this.isWillingToTrade = willingToTrade;
    }

    @Override
	public void useRecipe(MerchantRecipe recipe)
    {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int var2 = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0)
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToTrade = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }

            var2 += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.emerald)
        {
            this.wealth += recipe.getItemToBuy().stackSize;
        }

        if (recipe.getRewardsExp())
        {
            this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, var2));
        }
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    @Override
	public void verifySellingItem(ItemStack stack)
    {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();

            if (stack != null)
            {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else
            {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    @Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_)
    {
        if (this.buyingList == null)
        {
            this.populateBuyingList();
        }

        return this.buyingList;
    }

    private void populateBuyingList()
    {
        EntityVillager.ITradeList[][][] var1 = DEFAULT_TRADE_LIST_MAP[this.getProfession()];

        if (this.careerId != 0 && this.careerLevel != 0)
        {
            ++this.careerLevel;
        }
        else
        {
            this.careerId = this.rand.nextInt(var1.length) + 1;
            this.careerLevel = 1;
        }

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        int var2 = this.careerId - 1;
        int var3 = this.careerLevel - 1;
        EntityVillager.ITradeList[][] var4 = var1[var2];

        if (var3 < var4.length)
        {
            EntityVillager.ITradeList[] var5 = var4[var3];
            EntityVillager.ITradeList[] var6 = var5;
            int var7 = var5.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                EntityVillager.ITradeList var9 = var6[var8];
                var9.modifyMerchantRecipeList(this.buyingList, this.rand);
            }
        }
    }

    @Override
	public void setRecipes(MerchantRecipeList recipeList) {}

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
	public IChatComponent getDisplayName()
    {
        String var1 = this.getCustomNameTag();

        if (var1 != null && var1.length() > 0)
        {
            return new ChatComponentText(var1);
        }
        else
        {
            if (this.buyingList == null)
            {
                this.populateBuyingList();
            }

            String var2 = null;

            switch (this.getProfession())
            {
                case 0:
                    if (this.careerId == 1)
                    {
                        var2 = "farmer";
                    }
                    else if (this.careerId == 2)
                    {
                        var2 = "fisherman";
                    }
                    else if (this.careerId == 3)
                    {
                        var2 = "shepherd";
                    }
                    else if (this.careerId == 4)
                    {
                        var2 = "fletcher";
                    }

                    break;

                case 1:
                    var2 = "librarian";
                    break;

                case 2:
                    var2 = "cleric";
                    break;

                case 3:
                    if (this.careerId == 1)
                    {
                        var2 = "armor";
                    }
                    else if (this.careerId == 2)
                    {
                        var2 = "weapon";
                    }
                    else if (this.careerId == 3)
                    {
                        var2 = "tool";
                    }

                    break;

                case 4:
                    if (this.careerId == 1)
                    {
                        var2 = "butcher";
                    }
                    else if (this.careerId == 2)
                    {
                        var2 = "leather";
                    }
            }

            if (var2 != null)
            {
                ChatComponentTranslation var3 = new ChatComponentTranslation("entity.Villager." + var2, new Object[0]);
                var3.getChatStyle().setChatHoverEvent(this.getHoverEvent());
                var3.getChatStyle().setInsertion(this.getUniqueID().toString());
                return var3;
            }
            else
            {
                return super.getDisplayName();
            }
        }
    }

    @Override
	public float getEyeHeight()
    {
        float var1 = 1.62F;

        if (this.isChild())
        {
            var1 = (float)(var1 - 0.81D);
        }

        return var1;
    }

    @Override
	public void handleHealthUpdate(byte id)
    {
        if (id == 12)
        {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleHealthUpdate(id);
        }
    }

    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int var2 = 0; var2 < 5; ++var2)
        {
            double var3 = this.rand.nextGaussian() * 0.02D;
            double var5 = this.rand.nextGaussian() * 0.02D;
            double var7 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 1.0D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var3, var5, var7, new int[0]);
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setProfession(this.worldObj.rand.nextInt(5));
        this.setAdditionalAItasks();
        return livingdata;
    }

    public void setLookingForHome()
    {
        this.isLookingForHome = true;
    }

    public EntityVillager spawnVillager(EntityAgeable entityageableIn)
    {
        EntityVillager var2 = new EntityVillager(this.worldObj);
        var2.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData)null);
        return var2;
    }

    @Override
	public boolean allowLeashing()
    {
        return false;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    @Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.worldObj.isRemote)
        {
            EntityWitch var2 = new EntityWitch(this.worldObj);
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            var2.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(var2);
            this.setDead();
        }
    }

    public InventoryBasic getVillagerInventory()
    {
        return this.villagerInventory;
    }

    /**
     * Tests if this entity should pickup a weapon or an armor. Entity drops current weapon or armor if the new one is
     * better.
     */
    @Override
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack var2 = itemEntity.getEntityItem();
        Item var3 = var2.getItem();

        if (this.canVillagerPickupItem(var3))
        {
            ItemStack var4 = this.villagerInventory.func_174894_a(var2);

            if (var4 == null)
            {
                itemEntity.setDead();
            }
            else
            {
                var2.stackSize = var4.stackSize;
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn)
    {
        return itemIn == Items.bread || itemIn == Items.potato || itemIn == Items.carrot || itemIn == Items.wheat || itemIn == Items.wheat_seeds;
    }

    public boolean func_175553_cp()
    {
        return this.func_175559_s(1);
    }

    public boolean func_175555_cq()
    {
        return this.func_175559_s(2);
    }

    public boolean func_175557_cr()
    {
        boolean var1 = this.getProfession() == 0;
        return var1 ? !this.func_175559_s(5) : !this.func_175559_s(1);
    }

    private boolean func_175559_s(int p_175559_1_)
    {
        boolean var2 = this.getProfession() == 0;

        for (int var3 = 0; var3 < this.villagerInventory.getSizeInventory(); ++var3)
        {
            ItemStack var4 = this.villagerInventory.getStackInSlot(var3);

            if (var4 != null)
            {
                if (var4.getItem() == Items.bread && var4.stackSize >= 3 * p_175559_1_ || var4.getItem() == Items.potato && var4.stackSize >= 12 * p_175559_1_ || var4.getItem() == Items.carrot && var4.stackSize >= 12 * p_175559_1_)
                {
                    return true;
                }

                if (var2 && var4.getItem() == Items.wheat && var4.stackSize >= 9 * p_175559_1_)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean func_175556_cs()
    {
        for (int var1 = 0; var1 < this.villagerInventory.getSizeInventory(); ++var1)
        {
            ItemStack var2 = this.villagerInventory.getStackInSlot(var1);

            if (var2 != null && (var2.getItem() == Items.wheat_seeds || var2.getItem() == Items.potato || var2.getItem() == Items.carrot))
            {
                return true;
            }
        }

        return false;
    }

    @Override
	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int var3 = inventorySlot - 300;

            if (var3 >= 0 && var3 < this.villagerInventory.getSizeInventory())
            {
                this.villagerInventory.setInventorySlotContents(var3, itemStackIn);
                return true;
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
        return this.spawnVillager(ageable);
    }

    static class EmeraldForItems implements EntityVillager.ITradeList
    {
        public Item field_179405_a;
        public EntityVillager.PriceInfo field_179404_b;

        public EmeraldForItems(Item p_i45815_1_, EntityVillager.PriceInfo p_i45815_2_)
        {
            this.field_179405_a = p_i45815_1_;
            this.field_179404_b = p_i45815_2_;
        }

        @Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int var3 = 1;

            if (this.field_179404_b != null)
            {
                var3 = this.field_179404_b.getPrice(random);
            }

            recipeList.add(new MerchantRecipe(new ItemStack(this.field_179405_a, var3, 0), Items.emerald));
        }
    }

    interface ITradeList
    {
        void modifyMerchantRecipeList(MerchantRecipeList var1, Random var2);
    }

    static class ItemAndEmeraldToItem implements EntityVillager.ITradeList
    {
        public ItemStack field_179411_a;
        public EntityVillager.PriceInfo field_179409_b;
        public ItemStack field_179410_c;
        public EntityVillager.PriceInfo field_179408_d;

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_)
        {
            this.field_179411_a = new ItemStack(p_i45813_1_);
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = new ItemStack(p_i45813_3_);
            this.field_179408_d = p_i45813_4_;
        }

        @Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int var3 = 1;

            if (this.field_179409_b != null)
            {
                var3 = this.field_179409_b.getPrice(random);
            }

            int var4 = 1;

            if (this.field_179408_d != null)
            {
                var4 = this.field_179408_d.getPrice(random);
            }

            recipeList.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), var3, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), var4, this.field_179410_c.getMetadata())));
        }
    }

    static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList
    {

        @Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            Enchantment var3 = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
            int var4 = MathHelper.getRandomIntegerInRange(random, var3.getMinLevel(), var3.getMaxLevel());
            ItemStack var5 = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var3, var4));
            int var6 = 2 + random.nextInt(5 + var4 * 10) + 3 * var4;

            if (var6 > 64)
            {
                var6 = 64;
            }

            recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, var6), var5));
        }
    }

    static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack field_179407_a;
        public EntityVillager.PriceInfo field_179406_b;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_)
        {
            this.field_179407_a = new ItemStack(p_i45814_1_);
            this.field_179406_b = p_i45814_2_;
        }

        @Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int var3 = 1;

            if (this.field_179406_b != null)
            {
                var3 = this.field_179406_b.getPrice(random);
            }

            ItemStack var4 = new ItemStack(Items.emerald, var3, 0);
            ItemStack var5 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
            var5 = EnchantmentHelper.addRandomEnchantment(random, var5, 5 + random.nextInt(15));
            recipeList.add(new MerchantRecipe(var4, var5));
        }
    }

    static class ListItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack field_179403_a;
        public EntityVillager.PriceInfo field_179402_b;

        public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo)
        {
            this.field_179403_a = new ItemStack(par1Item);
            this.field_179402_b = priceInfo;
        }

        public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo)
        {
            this.field_179403_a = stack;
            this.field_179402_b = priceInfo;
        }

        @Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int var3 = 1;

            if (this.field_179402_b != null)
            {
                var3 = this.field_179402_b.getPrice(random);
            }

            ItemStack var4;
            ItemStack var5;

            if (var3 < 0)
            {
                var4 = new ItemStack(Items.emerald, 1, 0);
                var5 = new ItemStack(this.field_179403_a.getItem(), -var3, this.field_179403_a.getMetadata());
            }
            else
            {
                var4 = new ItemStack(Items.emerald, var3, 0);
                var5 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
            }

            recipeList.add(new MerchantRecipe(var4, var5));
        }
    }

    static class PriceInfo extends Tuple
    {

        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
        }

        public int getPrice(Random rand)
        {
            return ((Integer)this.getFirst()).intValue() >= ((Integer)this.getSecond()).intValue() ? ((Integer)this.getFirst()).intValue() : ((Integer)this.getFirst()).intValue() + rand.nextInt(((Integer)this.getSecond()).intValue() - ((Integer)this.getFirst()).intValue() + 1);
        }
    }
}
