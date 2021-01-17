// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.util.Tuple;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.MathHelper;
import net.minecraft.enchantment.Enchantment;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.Village;
import net.minecraft.entity.INpc;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.EntityAgeable;

public class EntityVillager extends EntityAgeable implements IMerchant, INpc
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;
    private EntityPlayer buyingPlayer;
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;
    private String lastBuyingPlayer;
    private int careerId;
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private InventoryBasic villagerInventory;
    private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP;
    
    static {
        DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][] { { { { new EmeraldForItems(Items.wheat, new PriceInfo(18, 22)), new EmeraldForItems(Items.potato, new PriceInfo(15, 19)), new EmeraldForItems(Items.carrot, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.bread, new PriceInfo(-4, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.pumpkin_pie, new PriceInfo(-3, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.apple, new PriceInfo(-5, -7)) }, { new ListItemForEmeralds(Items.cookie, new PriceInfo(-6, -10)), new ListItemForEmeralds(Items.cake, new PriceInfo(1, 1)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.fish, new PriceInfo(6, 6), Items.cooked_fish, new PriceInfo(6, 6)) }, { new ListEnchantedItemForEmeralds(Items.fishing_rod, new PriceInfo(7, 8)) } }, { { new EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.shears, new PriceInfo(3, 4)) }, { new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new PriceInfo(1, 2)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.arrow, new PriceInfo(-12, -8)) }, { new ListItemForEmeralds(Items.bow, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new PriceInfo(10, 10), Items.flint, new PriceInfo(6, 10)) } } }, { { { new EmeraldForItems(Items.paper, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds() }, { new EmeraldForItems(Items.book, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.compass, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new PriceInfo(3, 4)) }, { new EmeraldForItems(Items.written_book, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.clock, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new PriceInfo(-5, -3)) }, { new ListEnchantedBookForEmeralds() }, { new ListEnchantedBookForEmeralds() }, { new ListItemForEmeralds(Items.name_tag, new PriceInfo(20, 22)) } } }, { { { new EmeraldForItems(Items.rotten_flesh, new PriceInfo(36, 40)), new EmeraldForItems(Items.gold_ingot, new PriceInfo(8, 10)) }, { new ListItemForEmeralds(Items.redstone, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1)) }, { new ListItemForEmeralds(Items.ender_eye, new PriceInfo(7, 11)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new PriceInfo(-3, -1)) }, { new ListItemForEmeralds(Items.experience_bottle, new PriceInfo(3, 11)) } } }, { { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_helmet, new PriceInfo(4, 6)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.iron_chestplate, new PriceInfo(10, 14)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_chestplate, new PriceInfo(16, 19)) }, { new ListItemForEmeralds(Items.chainmail_boots, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_leggings, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.chainmail_helmet, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_chestplate, new PriceInfo(11, 15)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_axe, new PriceInfo(6, 8)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_sword, new PriceInfo(9, 10)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_sword, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.diamond_axe, new PriceInfo(9, 12)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.iron_shovel, new PriceInfo(5, 7)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_pickaxe, new PriceInfo(9, 11)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new PriceInfo(12, 15)) } } }, { { { new EmeraldForItems(Items.porkchop, new PriceInfo(14, 18)), new EmeraldForItems(Items.chicken, new PriceInfo(14, 18)) }, { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.cooked_porkchop, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.cooked_chicken, new PriceInfo(-8, -6)) } }, { { new EmeraldForItems(Items.leather, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.leather_leggings, new PriceInfo(2, 4)) }, { new ListEnchantedItemForEmeralds(Items.leather_chestplate, new PriceInfo(7, 12)) }, { new ListItemForEmeralds(Items.saddle, new PriceInfo(8, 10)) } } } };
    }
    
    public EntityVillager(final World worldIn) {
        this(worldIn, 0);
    }
    
    public EntityVillager(final World worldIn, final int professionId) {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
        this.setProfession(professionId);
        this.setSize(0.6f, 1.8f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<Object>(this, EntityZombie.class, 8.0f, 0.6, 0.6));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.setCanPickUpLoot(true);
    }
    
    private void setAdditionalAItasks() {
        if (!this.areAdditionalTasksSet) {
            this.areAdditionalTasksSet = true;
            if (this.isChild()) {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32));
            }
            else if (this.getProfession() == 0) {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6));
            }
        }
    }
    
    @Override
    protected void onGrowingAdult() {
        if (this.getProfession() == 0) {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6));
        }
        super.onGrowingAdult();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
    }
    
    @Override
    protected void updateAITasks() {
        final int randomTickDivider = this.randomTickDivider - 1;
        this.randomTickDivider = randomTickDivider;
        if (randomTickDivider <= 0) {
            final BlockPos blockpos = new BlockPos(this);
            this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);
            if (this.villageObj == null) {
                this.detachHome();
            }
            else {
                final BlockPos blockpos2 = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockpos2, (int)(this.villageObj.getVillageRadius() * 1.0f));
                if (this.isLookingForHome) {
                    this.isLookingForHome = false;
                    this.villageObj.setDefaultPlayerReputation(5);
                }
            }
        }
        if (!this.isTrading() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.needsInitilization) {
                    for (final MerchantRecipe merchantrecipe : this.buyingList) {
                        if (merchantrecipe.isRecipeDisabled()) {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }
                    this.populateBuyingList();
                    this.needsInitilization = false;
                    if (this.villageObj != null && this.lastBuyingPlayer != null) {
                        this.worldObj.setEntityState(this, (byte)14);
                        this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
                    }
                }
                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }
        super.updateAITasks();
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        final boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;
        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0)) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            player.triggerAchievement(StatList.timesTalkedToVillagerStat);
            return true;
        }
        return super.interact(player);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Profession", this.getProfession());
        tagCompound.setInteger("Riches", this.wealth);
        tagCompound.setInteger("Career", this.careerId);
        tagCompound.setInteger("CareerLevel", this.careerLevel);
        tagCompound.setBoolean("Willing", this.isWillingToMate);
        if (this.buyingList != null) {
            tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack != null) {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }
        tagCompound.setTag("Inventory", nbttaglist);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setProfession(tagCompund.getInteger("Profession"));
        this.wealth = tagCompund.getInteger("Riches");
        this.careerId = tagCompund.getInteger("Career");
        this.careerLevel = tagCompund.getInteger("CareerLevel");
        this.isWillingToMate = tagCompund.getBoolean("Willing");
        if (tagCompund.hasKey("Offers", 10)) {
            final NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }
        final NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
            if (itemstack != null) {
                this.villagerInventory.func_174894_a(itemstack);
            }
        }
        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    protected String getLivingSound() {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.villager.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.villager.death";
    }
    
    public void setProfession(final int professionId) {
        this.dataWatcher.updateObject(16, professionId);
    }
    
    public int getProfession() {
        return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
    }
    
    public boolean isMating() {
        return this.isMating;
    }
    
    public void setMating(final boolean mating) {
        this.isMating = mating;
    }
    
    public void setPlaying(final boolean playing) {
        this.isPlaying = playing;
    }
    
    public boolean isPlaying() {
        return this.isPlaying;
    }
    
    @Override
    public void setRevengeTarget(final EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (this.villageObj != null && livingBase != null) {
            this.villageObj.addOrRenewAgressor(livingBase);
            if (livingBase instanceof EntityPlayer) {
                int i = -1;
                if (this.isChild()) {
                    i = -3;
                }
                this.villageObj.setReputationForPlayer(livingBase.getName(), i);
                if (this.isEntityAlive()) {
                    this.worldObj.setEntityState(this, (byte)13);
                }
            }
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (this.villageObj != null) {
            final Entity entity = cause.getEntity();
            if (entity != null) {
                if (entity instanceof EntityPlayer) {
                    this.villageObj.setReputationForPlayer(entity.getName(), -2);
                }
                else if (entity instanceof IMob) {
                    this.villageObj.endMatingSeason();
                }
            }
            else {
                final EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0);
                if (entityplayer != null) {
                    this.villageObj.endMatingSeason();
                }
            }
        }
        super.onDeath(cause);
    }
    
    @Override
    public void setCustomer(final EntityPlayer p_70932_1_) {
        this.buyingPlayer = p_70932_1_;
    }
    
    @Override
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }
    
    public boolean isTrading() {
        return this.buyingPlayer != null;
    }
    
    public boolean getIsWillingToMate(final boolean updateFirst) {
        if (!this.isWillingToMate && updateFirst && this.func_175553_cp()) {
            boolean flag = false;
            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
                if (itemstack != null) {
                    if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot) && itemstack.stackSize >= 12) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }
                if (flag) {
                    this.worldObj.setEntityState(this, (byte)18);
                    this.isWillingToMate = true;
                    break;
                }
            }
        }
        return this.isWillingToMate;
    }
    
    public void setIsWillingToMate(final boolean willingToTrade) {
        this.isWillingToMate = willingToTrade;
    }
    
    @Override
    public void useRecipe(final MerchantRecipe recipe) {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);
        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;
            if (this.buyingPlayer != null) {
                this.lastBuyingPlayer = this.buyingPlayer.getName();
            }
            else {
                this.lastBuyingPlayer = null;
            }
            i += 5;
        }
        if (recipe.getItemToBuy().getItem() == Items.emerald) {
            this.wealth += recipe.getItemToBuy().stackSize;
        }
        if (recipe.getRewardsExp()) {
            this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5, this.posZ, i));
        }
    }
    
    @Override
    public void verifySellingItem(final ItemStack stack) {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            if (stack != null) {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }
    
    @Override
    public MerchantRecipeList getRecipes(final EntityPlayer p_70934_1_) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        return this.buyingList;
    }
    
    private void populateBuyingList() {
        final ITradeList[][][] aentityvillager$itradelist = EntityVillager.DEFAULT_TRADE_LIST_MAP[this.getProfession()];
        if (this.careerId != 0 && this.careerLevel != 0) {
            ++this.careerLevel;
        }
        else {
            this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
            this.careerLevel = 1;
        }
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }
        final int i = this.careerId - 1;
        final int j = this.careerLevel - 1;
        final ITradeList[][] aentityvillager$itradelist2 = aentityvillager$itradelist[i];
        if (j >= 0 && j < aentityvillager$itradelist2.length) {
            final ITradeList[] aentityvillager$itradelist3 = aentityvillager$itradelist2[j];
            ITradeList[] array;
            for (int length = (array = aentityvillager$itradelist3).length, k = 0; k < length; ++k) {
                final ITradeList entityvillager$itradelist = array[k];
                entityvillager$itradelist.modifyMerchantRecipeList(this.buyingList, this.rand);
            }
        }
    }
    
    @Override
    public void setRecipes(final MerchantRecipeList recipeList) {
    }
    
    @Override
    public IChatComponent getDisplayName() {
        final String s = this.getCustomNameTag();
        if (s != null && s.length() > 0) {
            final ChatComponentText chatcomponenttext = new ChatComponentText(s);
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttext;
        }
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        String s2 = null;
        switch (this.getProfession()) {
            case 0: {
                if (this.careerId == 1) {
                    s2 = "farmer";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "fisherman";
                    break;
                }
                if (this.careerId == 3) {
                    s2 = "shepherd";
                    break;
                }
                if (this.careerId == 4) {
                    s2 = "fletcher";
                    break;
                }
                break;
            }
            case 1: {
                s2 = "librarian";
                break;
            }
            case 2: {
                s2 = "cleric";
                break;
            }
            case 3: {
                if (this.careerId == 1) {
                    s2 = "armor";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "weapon";
                    break;
                }
                if (this.careerId == 3) {
                    s2 = "tool";
                    break;
                }
                break;
            }
            case 4: {
                if (this.careerId == 1) {
                    s2 = "butcher";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "leather";
                    break;
                }
                break;
            }
        }
        if (s2 != null) {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("entity.Villager." + s2, new Object[0]);
            chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttranslation;
        }
        return super.getDisplayName();
    }
    
    @Override
    public float getEyeHeight() {
        float f = 1.62f;
        if (this.isChild()) {
            f -= (float)0.81;
        }
        return f;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 12) {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    private void spawnParticles(final EnumParticleTypes particleType) {
        for (int i = 0; i < 5; ++i) {
            final double d0 = this.rand.nextGaussian() * 0.02;
            final double d2 = this.rand.nextGaussian() * 0.02;
            final double d3 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 1.0 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setProfession(this.worldObj.rand.nextInt(5));
        this.setAdditionalAItasks();
        return livingdata;
    }
    
    public void setLookingForHome() {
        this.isLookingForHome = true;
    }
    
    @Override
    public EntityVillager createChild(final EntityAgeable ageable) {
        final EntityVillager entityvillager = new EntityVillager(this.worldObj);
        entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }
    
    @Override
    public boolean allowLeashing() {
        return false;
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        if (!this.worldObj.isRemote && !this.isDead) {
            final EntityWitch entitywitch = new EntityWitch(this.worldObj);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entitywitch)), null);
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entitywitch);
            this.setDead();
        }
    }
    
    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }
    
    @Override
    protected void updateEquipmentIfNeeded(final EntityItem itemEntity) {
        final ItemStack itemstack = itemEntity.getEntityItem();
        final Item item = itemstack.getItem();
        if (this.canVillagerPickupItem(item)) {
            final ItemStack itemstack2 = this.villagerInventory.func_174894_a(itemstack);
            if (itemstack2 == null) {
                itemEntity.setDead();
            }
            else {
                itemstack.stackSize = itemstack2.stackSize;
            }
        }
    }
    
    private boolean canVillagerPickupItem(final Item itemIn) {
        return itemIn == Items.bread || itemIn == Items.potato || itemIn == Items.carrot || itemIn == Items.wheat || itemIn == Items.wheat_seeds;
    }
    
    public boolean func_175553_cp() {
        return this.hasEnoughItems(1);
    }
    
    public boolean canAbondonItems() {
        return this.hasEnoughItems(2);
    }
    
    public boolean func_175557_cr() {
        final boolean flag = this.getProfession() == 0;
        return flag ? (!this.hasEnoughItems(5)) : (!this.hasEnoughItems(1));
    }
    
    private boolean hasEnoughItems(final int multiplier) {
        final boolean flag = this.getProfession() == 0;
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack != null) {
                if ((itemstack.getItem() == Items.bread && itemstack.stackSize >= 3 * multiplier) || (itemstack.getItem() == Items.potato && itemstack.stackSize >= 12 * multiplier) || (itemstack.getItem() == Items.carrot && itemstack.stackSize >= 12 * multiplier)) {
                    return true;
                }
                if (flag && itemstack.getItem() == Items.wheat && itemstack.stackSize >= 9 * multiplier) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isFarmItemInInventory() {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack != null && (itemstack.getItem() == Items.wheat_seeds || itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
            return true;
        }
        final int i = inventorySlot - 300;
        if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
            this.villagerInventory.setInventorySlotContents(i, itemStackIn);
            return true;
        }
        return false;
    }
    
    static class EmeraldForItems implements ITradeList
    {
        public Item sellItem;
        public PriceInfo price;
        
        public EmeraldForItems(final Item itemIn, final PriceInfo priceIn) {
            this.sellItem = itemIn;
            this.price = priceIn;
        }
        
        @Override
        public void modifyMerchantRecipeList(final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.price != null) {
                i = this.price.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
        }
    }
    
    static class ItemAndEmeraldToItem implements ITradeList
    {
        public ItemStack field_179411_a;
        public PriceInfo field_179409_b;
        public ItemStack field_179410_c;
        public PriceInfo field_179408_d;
        
        public ItemAndEmeraldToItem(final Item p_i45813_1_, final PriceInfo p_i45813_2_, final Item p_i45813_3_, final PriceInfo p_i45813_4_) {
            this.field_179411_a = new ItemStack(p_i45813_1_);
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = new ItemStack(p_i45813_3_);
            this.field_179408_d = p_i45813_4_;
        }
        
        @Override
        public void modifyMerchantRecipeList(final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.field_179409_b != null) {
                i = this.field_179409_b.getPrice(random);
            }
            int j = 1;
            if (this.field_179408_d != null) {
                j = this.field_179408_d.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), i, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), j, this.field_179410_c.getMetadata())));
        }
    }
    
    static class ListEnchantedBookForEmeralds implements ITradeList
    {
        @Override
        public void modifyMerchantRecipeList(final MerchantRecipeList recipeList, final Random random) {
            final Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
            final int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            final ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (j > 64) {
                j = 64;
            }
            recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
        }
    }
    
    static class ListEnchantedItemForEmeralds implements ITradeList
    {
        public ItemStack field_179407_a;
        public PriceInfo field_179406_b;
        
        public ListEnchantedItemForEmeralds(final Item p_i45814_1_, final PriceInfo p_i45814_2_) {
            this.field_179407_a = new ItemStack(p_i45814_1_);
            this.field_179406_b = p_i45814_2_;
        }
        
        @Override
        public void modifyMerchantRecipeList(final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.field_179406_b != null) {
                i = this.field_179406_b.getPrice(random);
            }
            final ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
            ItemStack itemstack2 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
            itemstack2 = EnchantmentHelper.addRandomEnchantment(random, itemstack2, 5 + random.nextInt(15));
            recipeList.add(new MerchantRecipe(itemstack, itemstack2));
        }
    }
    
    static class ListItemForEmeralds implements ITradeList
    {
        public ItemStack field_179403_a;
        public PriceInfo field_179402_b;
        
        public ListItemForEmeralds(final Item par1Item, final PriceInfo priceInfo) {
            this.field_179403_a = new ItemStack(par1Item);
            this.field_179402_b = priceInfo;
        }
        
        public ListItemForEmeralds(final ItemStack stack, final PriceInfo priceInfo) {
            this.field_179403_a = stack;
            this.field_179402_b = priceInfo;
        }
        
        @Override
        public void modifyMerchantRecipeList(final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.field_179402_b != null) {
                i = this.field_179402_b.getPrice(random);
            }
            ItemStack itemstack;
            ItemStack itemstack2;
            if (i < 0) {
                itemstack = new ItemStack(Items.emerald, 1, 0);
                itemstack2 = new ItemStack(this.field_179403_a.getItem(), -i, this.field_179403_a.getMetadata());
            }
            else {
                itemstack = new ItemStack(Items.emerald, i, 0);
                itemstack2 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
            }
            recipeList.add(new MerchantRecipe(itemstack, itemstack2));
        }
    }
    
    static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(final int p_i45810_1_, final int p_i45810_2_) {
            super(p_i45810_1_, p_i45810_2_);
        }
        
        public int getPrice(final Random rand) {
            return (((Tuple<Integer, B>)this).getFirst() >= ((Tuple<A, Integer>)this).getSecond()) ? ((Tuple<Integer, B>)this).getFirst() : (((Tuple<Integer, B>)this).getFirst() + rand.nextInt(((Tuple<A, Integer>)this).getSecond() - ((Tuple<Integer, B>)this).getFirst() + 1));
        }
    }
    
    interface ITradeList
    {
        void modifyMerchantRecipeList(final MerchantRecipeList p0, final Random p1);
    }
}
