package net.minecraft.entity.passive;

import com.google.common.base.Predicate;

import java.util.Iterator;
import java.util.Random;

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

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;

    /**
     * This villager's current customer.
     */
    private EntityPlayer buyingPlayer;

    /**
     * Initialises the MerchantRecipeList.java
     */
    private MerchantRecipeList buyingList;
    private int timeUntilReset;

    /**
     * addDefaultEquipmentAndRecipies is called if this is true
     */
    private boolean needsInitilization;
    private boolean field_175565_bs;
    private int wealth;

    /**
     * Last player to trade with this villager, used for aggressivity.
     */
    private String lastBuyingPlayer;
    private int field_175563_bv;
    private int field_175562_bw;
    private boolean isLookingForHome;
    private boolean field_175564_by;
    private InventoryBasic field_175560_bz;
    private static final EntityVillager.ITradeList[][][][] field_175561_bA = new EntityVillager.ITradeList[][][][]{{{{new EntityVillager.EmeraldForItems(Items.wheat, new EntityVillager.PriceInfo(18, 22)), new EntityVillager.EmeraldForItems(Items.potato, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.EmeraldForItems(Items.carrot, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.ListItemForEmeralds(Items.bread, new EntityVillager.PriceInfo(-4, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new EntityVillager.PriceInfo(8, 13)), new EntityVillager.ListItemForEmeralds(Items.pumpkin_pie, new EntityVillager.PriceInfo(-3, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new EntityVillager.PriceInfo(7, 12)), new EntityVillager.ListItemForEmeralds(Items.apple, new EntityVillager.PriceInfo(-5, -7))}, {new EntityVillager.ListItemForEmeralds(Items.cookie, new EntityVillager.PriceInfo(-6, -10)), new EntityVillager.ListItemForEmeralds(Items.cake, new EntityVillager.PriceInfo(1, 1))}}, {{new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ItemAndEmeraldToItem(Items.fish, new EntityVillager.PriceInfo(6, 6), Items.cooked_fish, new EntityVillager.PriceInfo(6, 6))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.fishing_rod, new EntityVillager.PriceInfo(7, 8))}}, {{new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new EntityVillager.PriceInfo(16, 22)), new EntityVillager.ListItemForEmeralds(Items.shears, new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new EntityVillager.PriceInfo(1, 2))}}, {{new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.ListItemForEmeralds(Items.arrow, new EntityVillager.PriceInfo(-12, -8))}, {new EntityVillager.ListItemForEmeralds(Items.bow, new EntityVillager.PriceInfo(2, 3)), new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new EntityVillager.PriceInfo(10, 10), Items.flint, new EntityVillager.PriceInfo(6, 10))}}}, {{{new EntityVillager.EmeraldForItems(Items.paper, new EntityVillager.PriceInfo(24, 36)), new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.EmeraldForItems(Items.book, new EntityVillager.PriceInfo(8, 10)), new EntityVillager.ListItemForEmeralds(Items.compass, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.EmeraldForItems(Items.written_book, new EntityVillager.PriceInfo(2, 2)), new EntityVillager.ListItemForEmeralds(Items.clock, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new EntityVillager.PriceInfo(-5, -3))}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListItemForEmeralds(Items.name_tag, new EntityVillager.PriceInfo(20, 22))}}}, {{{new EntityVillager.EmeraldForItems(Items.rotten_flesh, new EntityVillager.PriceInfo(36, 40)), new EntityVillager.EmeraldForItems(Items.gold_ingot, new EntityVillager.PriceInfo(8, 10))}, {new EntityVillager.ListItemForEmeralds(Items.redstone, new EntityVillager.PriceInfo(-4, -1)), new EntityVillager.ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new EntityVillager.PriceInfo(-2, -1))}, {new EntityVillager.ListItemForEmeralds(Items.ender_eye, new EntityVillager.PriceInfo(7, 11)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new EntityVillager.PriceInfo(-3, -1))}, {new EntityVillager.ListItemForEmeralds(Items.experience_bottle, new EntityVillager.PriceInfo(3, 11))}}}, {{{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.iron_helmet, new EntityVillager.PriceInfo(4, 6))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListItemForEmeralds(Items.iron_chestplate, new EntityVillager.PriceInfo(10, 14))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate, new EntityVillager.PriceInfo(16, 19))}, {new EntityVillager.ListItemForEmeralds(Items.chainmail_boots, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.chainmail_leggings, new EntityVillager.PriceInfo(9, 11)), new EntityVillager.ListItemForEmeralds(Items.chainmail_helmet, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.chainmail_chestplate, new EntityVillager.PriceInfo(11, 15))}}, {{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.iron_axe, new EntityVillager.PriceInfo(6, 8))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_sword, new EntityVillager.PriceInfo(9, 10))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_sword, new EntityVillager.PriceInfo(12, 15)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_axe, new EntityVillager.PriceInfo(9, 12))}}, {{new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_shovel, new EntityVillager.PriceInfo(5, 7))}, {new EntityVillager.EmeraldForItems(Items.iron_ingot, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe, new EntityVillager.PriceInfo(9, 11))}, {new EntityVillager.EmeraldForItems(Items.diamond, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new EntityVillager.PriceInfo(12, 15))}}}, {{{new EntityVillager.EmeraldForItems(Items.porkchop, new EntityVillager.PriceInfo(14, 18)), new EntityVillager.EmeraldForItems(Items.chicken, new EntityVillager.PriceInfo(14, 18))}, {new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.cooked_porkchop, new EntityVillager.PriceInfo(-7, -5)), new EntityVillager.ListItemForEmeralds(Items.cooked_chicken, new EntityVillager.PriceInfo(-8, -6))}}, {{new EntityVillager.EmeraldForItems(Items.leather, new EntityVillager.PriceInfo(9, 12)), new EntityVillager.ListItemForEmeralds(Items.leather_leggings, new EntityVillager.PriceInfo(2, 4))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate, new EntityVillager.PriceInfo(7, 12))}, {new EntityVillager.ListItemForEmeralds(Items.saddle, new EntityVillager.PriceInfo(8, 10))}}}};
    private static final String __OBFID = "CL_00001707";

    public EntityVillager(World worldIn) {
        this(worldIn, 0);
    }

    public EntityVillager(World worldIn, int p_i1748_2_) {
        super(worldIn);
        this.field_175560_bz = new InventoryBasic("Items", false, 8);
        this.setProfession(p_i1748_2_);
        this.setSize(0.6F, 1.8F);
        ((PathNavigateGround) this.getNavigator()).func_179688_b(true);
        ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, new Predicate() {
            private static final String __OBFID = "CL_00002195";

            public boolean func_179530_a(Entity p_179530_1_) {
                return p_179530_1_ instanceof EntityZombie;
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_179530_a((Entity) p_apply_1_);
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

    private void func_175552_ct() {
        if (!this.field_175564_by) {
            this.field_175564_by = true;

            if (this.isChild()) {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            } else if (this.getProfession() == 0) {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    protected void func_175500_n() {
        if (this.getProfession() == 0) {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.func_175500_n();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    protected void updateAITasks() {
        if (--this.randomTickDivider <= 0) {
            BlockPos var1 = new BlockPos(this);
            this.worldObj.getVillageCollection().func_176060_a(var1);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().func_176056_a(var1, 32);

            if (this.villageObj == null) {
                this.detachHome();
            } else {
                BlockPos var2 = this.villageObj.func_180608_a();
                this.func_175449_a(var2, (int) ((float) this.villageObj.getVillageRadius() * 1.0F));

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
                    Iterator var3 = this.buyingList.iterator();

                    while (var3.hasNext()) {
                        MerchantRecipe var4 = (MerchantRecipe) var3.next();

                        if (var4.isRecipeDisabled()) {
                            var4.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.func_175554_cu();
                    this.needsInitilization = false;

                    if (this.villageObj != null && this.lastBuyingPlayer != null) {
                        this.worldObj.setEntityState(this, (byte) 14);
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
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        boolean var3 = var2 != null && var2.getItem() == Items.spawn_egg;

        if (!var3 && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0)) {
                this.setCustomer(p_70085_1_);
                p_70085_1_.displayVillagerTradeGui(this);
            }

            p_70085_1_.triggerAchievement(StatList.timesTalkedToVillagerStat);
            return true;
        } else {
            return super.interact(p_70085_1_);
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Profession", this.getProfession());
        tagCompound.setInteger("Riches", this.wealth);
        tagCompound.setInteger("Career", this.field_175563_bv);
        tagCompound.setInteger("CareerLevel", this.field_175562_bw);
        tagCompound.setBoolean("Willing", this.field_175565_bs);

        if (this.buyingList != null) {
            tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_175560_bz.getSizeInventory(); ++var3) {
            ItemStack var4 = this.field_175560_bz.getStackInSlot(var3);

            if (var4 != null) {
                var2.appendTag(var4.writeToNBT(new NBTTagCompound()));
            }
        }

        tagCompound.setTag("Inventory", var2);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setProfession(tagCompund.getInteger("Profession"));
        this.wealth = tagCompund.getInteger("Riches");
        this.field_175563_bv = tagCompund.getInteger("Career");
        this.field_175562_bw = tagCompund.getInteger("CareerLevel");
        this.field_175565_bs = tagCompund.getBoolean("Willing");

        if (tagCompund.hasKey("Offers", 10)) {
            NBTTagCompound var2 = tagCompund.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(var2);
        }

        NBTTagList var5 = tagCompund.getTagList("Inventory", 10);

        for (int var3 = 0; var3 < var5.tagCount(); ++var3) {
            ItemStack var4 = ItemStack.loadItemStackFromNBT(var5.getCompoundTagAt(var3));

            if (var4 != null) {
                this.field_175560_bz.func_174894_a(var4);
            }
        }

        this.setCanPickUpLoot(true);
        this.func_175552_ct();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        return false;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.villager.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.villager.death";
    }

    public void setProfession(int p_70938_1_) {
        this.dataWatcher.updateObject(16, Integer.valueOf(p_70938_1_));
    }

    public int getProfession() {
        return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
    }

    public boolean isMating() {
        return this.isMating;
    }

    public void setMating(boolean p_70947_1_) {
        this.isMating = p_70947_1_;
    }

    public void setPlaying(boolean p_70939_1_) {
        this.isPlaying = p_70939_1_;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setRevengeTarget(EntityLivingBase p_70604_1_) {
        super.setRevengeTarget(p_70604_1_);

        if (this.villageObj != null && p_70604_1_ != null) {
            this.villageObj.addOrRenewAgressor(p_70604_1_);

            if (p_70604_1_ instanceof EntityPlayer) {
                byte var2 = -1;

                if (this.isChild()) {
                    var2 = -3;
                }

                this.villageObj.setReputationForPlayer(p_70604_1_.getName(), var2);

                if (this.isEntityAlive()) {
                    this.worldObj.setEntityState(this, (byte) 13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause) {
        if (this.villageObj != null) {
            Entity var2 = cause.getEntity();

            if (var2 != null) {
                if (var2 instanceof EntityPlayer) {
                    this.villageObj.setReputationForPlayer(var2.getName(), -2);
                } else if (var2 instanceof IMob) {
                    this.villageObj.endMatingSeason();
                }
            } else {
                EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

                if (var3 != null) {
                    this.villageObj.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    public void setCustomer(EntityPlayer p_70932_1_) {
        this.buyingPlayer = p_70932_1_;
    }

    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    public boolean isTrading() {
        return this.buyingPlayer != null;
    }

    public boolean func_175550_n(boolean p_175550_1_) {
        if (!this.field_175565_bs && p_175550_1_ && this.func_175553_cp()) {
            boolean var2 = false;

            for (int var3 = 0; var3 < this.field_175560_bz.getSizeInventory(); ++var3) {
                ItemStack var4 = this.field_175560_bz.getStackInSlot(var3);

                if (var4 != null) {
                    if (var4.getItem() == Items.bread && var4.stackSize >= 3) {
                        var2 = true;
                        this.field_175560_bz.decrStackSize(var3, 3);
                    } else if ((var4.getItem() == Items.potato || var4.getItem() == Items.carrot) && var4.stackSize >= 12) {
                        var2 = true;
                        this.field_175560_bz.decrStackSize(var3, 12);
                    }
                }

                if (var2) {
                    this.worldObj.setEntityState(this, (byte) 18);
                    this.field_175565_bs = true;
                    break;
                }
            }
        }

        return this.field_175565_bs;
    }

    public void func_175549_o(boolean p_175549_1_) {
        this.field_175565_bs = p_175549_1_;
    }

    public void useRecipe(MerchantRecipe p_70933_1_) {
        p_70933_1_.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int var2 = 3 + this.rand.nextInt(4);

        if (p_70933_1_.func_180321_e() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.field_175565_bs = true;

            if (this.buyingPlayer != null) {
                this.lastBuyingPlayer = this.buyingPlayer.getName();
            } else {
                this.lastBuyingPlayer = null;
            }

            var2 += 5;
        }

        if (p_70933_1_.getItemToBuy().getItem() == Items.emerald) {
            this.wealth += p_70933_1_.getItemToBuy().stackSize;
        }

        if (p_70933_1_.func_180322_j()) {
            this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, var2));
        }
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack p_110297_1_) {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();

            if (p_110297_1_ != null) {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            } else {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
        if (this.buyingList == null) {
            this.func_175554_cu();
        }

        return this.buyingList;
    }

    private void func_175554_cu() {
        EntityVillager.ITradeList[][][] var1 = field_175561_bA[this.getProfession()];

        if (this.field_175563_bv != 0 && this.field_175562_bw != 0) {
            ++this.field_175562_bw;
        } else {
            this.field_175563_bv = this.rand.nextInt(var1.length) + 1;
            this.field_175562_bw = 1;
        }

        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        int var2 = this.field_175563_bv - 1;
        int var3 = this.field_175562_bw - 1;
        EntityVillager.ITradeList[][] var4 = var1[var2];

        if (var3 < var4.length) {
            EntityVillager.ITradeList[] var5 = var4[var3];
            EntityVillager.ITradeList[] var6 = var5;
            int var7 = var5.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                EntityVillager.ITradeList var9 = var6[var8];
                var9.func_179401_a(this.buyingList, this.rand);
            }
        }
    }

    public void setRecipes(MerchantRecipeList p_70930_1_) {
    }

    public IChatComponent getDisplayName() {
        String var1 = this.getCustomNameTag();

        if (var1 != null && var1.length() > 0) {
            return new ChatComponentText(var1);
        } else {
            if (this.buyingList == null) {
                this.func_175554_cu();
            }

            String var2 = null;

            switch (this.getProfession()) {
                case 0:
                    if (this.field_175563_bv == 1) {
                        var2 = "farmer";
                    } else if (this.field_175563_bv == 2) {
                        var2 = "fisherman";
                    } else if (this.field_175563_bv == 3) {
                        var2 = "shepherd";
                    } else if (this.field_175563_bv == 4) {
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
                    if (this.field_175563_bv == 1) {
                        var2 = "armor";
                    } else if (this.field_175563_bv == 2) {
                        var2 = "weapon";
                    } else if (this.field_175563_bv == 3) {
                        var2 = "tool";
                    }

                    break;

                case 4:
                    if (this.field_175563_bv == 1) {
                        var2 = "butcher";
                    } else if (this.field_175563_bv == 2) {
                        var2 = "leather";
                    }
            }

            if (var2 != null) {
                ChatComponentTranslation var3 = new ChatComponentTranslation("entity.Villager." + var2, new Object[0]);
                var3.getChatStyle().setChatHoverEvent(this.func_174823_aP());
                var3.getChatStyle().setInsertion(this.getUniqueID().toString());
                return var3;
            } else {
                return super.getDisplayName();
            }
        }
    }

    public float getEyeHeight() {
        float var1 = 1.62F;

        if (this.isChild()) {
            var1 = (float) ((double) var1 - 0.81D);
        }

        return var1;
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 12) {
            this.func_180489_a(EnumParticleTypes.HEART);
        } else if (p_70103_1_ == 13) {
            this.func_180489_a(EnumParticleTypes.VILLAGER_ANGRY);
        } else if (p_70103_1_ == 14) {
            this.func_180489_a(EnumParticleTypes.VILLAGER_HAPPY);
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    private void func_180489_a(EnumParticleTypes p_180489_1_) {
        for (int var2 = 0; var2 < 5; ++var2) {
            double var3 = this.rand.nextGaussian() * 0.02D;
            double var5 = this.rand.nextGaussian() * 0.02D;
            double var7 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(p_180489_1_, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 1.0D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, var3, var5, var7, new int[0]);
        }
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setProfession(this.worldObj.rand.nextInt(5));
        this.func_175552_ct();
        return p_180482_2_;
    }

    public void setLookingForHome() {
        this.isLookingForHome = true;
    }

    public EntityVillager func_180488_b(EntityAgeable p_180488_1_) {
        EntityVillager var2 = new EntityVillager(this.worldObj);
        var2.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData) null);
        return var2;
    }

    public boolean allowLeashing() {
        return false;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        if (!this.worldObj.isRemote) {
            EntityWitch var2 = new EntityWitch(this.worldObj);
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            var2.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData) null);
            this.worldObj.spawnEntityInWorld(var2);
            this.setDead();
        }
    }

    public InventoryBasic func_175551_co() {
        return this.field_175560_bz;
    }

    protected void func_175445_a(EntityItem p_175445_1_) {
        ItemStack var2 = p_175445_1_.getEntityItem();
        Item var3 = var2.getItem();

        if (this.func_175558_a(var3)) {
            ItemStack var4 = this.field_175560_bz.func_174894_a(var2);

            if (var4 == null) {
                p_175445_1_.setDead();
            } else {
                var2.stackSize = var4.stackSize;
            }
        }
    }

    private boolean func_175558_a(Item p_175558_1_) {
        return p_175558_1_ == Items.bread || p_175558_1_ == Items.potato || p_175558_1_ == Items.carrot || p_175558_1_ == Items.wheat || p_175558_1_ == Items.wheat_seeds;
    }

    public boolean func_175553_cp() {
        return this.func_175559_s(1);
    }

    public boolean func_175555_cq() {
        return this.func_175559_s(2);
    }

    public boolean func_175557_cr() {
        boolean var1 = this.getProfession() == 0;
        return var1 ? !this.func_175559_s(5) : !this.func_175559_s(1);
    }

    private boolean func_175559_s(int p_175559_1_) {
        boolean var2 = this.getProfession() == 0;

        for (int var3 = 0; var3 < this.field_175560_bz.getSizeInventory(); ++var3) {
            ItemStack var4 = this.field_175560_bz.getStackInSlot(var3);

            if (var4 != null) {
                if (var4.getItem() == Items.bread && var4.stackSize >= 3 * p_175559_1_ || var4.getItem() == Items.potato && var4.stackSize >= 12 * p_175559_1_ || var4.getItem() == Items.carrot && var4.stackSize >= 12 * p_175559_1_) {
                    return true;
                }

                if (var2 && var4.getItem() == Items.wheat && var4.stackSize >= 9 * p_175559_1_) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean func_175556_cs() {
        for (int var1 = 0; var1 < this.field_175560_bz.getSizeInventory(); ++var1) {
            ItemStack var2 = this.field_175560_bz.getStackInSlot(var1);

            if (var2 != null && (var2.getItem() == Items.wheat_seeds || var2.getItem() == Items.potato || var2.getItem() == Items.carrot)) {
                return true;
            }
        }

        return false;
    }

    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_) {
        if (super.func_174820_d(p_174820_1_, p_174820_2_)) {
            return true;
        } else {
            int var3 = p_174820_1_ - 300;

            if (var3 >= 0 && var3 < this.field_175560_bz.getSizeInventory()) {
                this.field_175560_bz.setInventorySlotContents(var3, p_174820_2_);
                return true;
            } else {
                return false;
            }
        }
    }

    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        return this.func_180488_b(p_90011_1_);
    }

    static class EmeraldForItems implements EntityVillager.ITradeList {
        public Item field_179405_a;
        public EntityVillager.PriceInfo field_179404_b;
        private static final String __OBFID = "CL_00002194";

        public EmeraldForItems(Item p_i45815_1_, EntityVillager.PriceInfo p_i45815_2_) {
            this.field_179405_a = p_i45815_1_;
            this.field_179404_b = p_i45815_2_;
        }

        public void func_179401_a(MerchantRecipeList p_179401_1_, Random p_179401_2_) {
            int var3 = 1;

            if (this.field_179404_b != null) {
                var3 = this.field_179404_b.func_179412_a(p_179401_2_);
            }

            p_179401_1_.add(new MerchantRecipe(new ItemStack(this.field_179405_a, var3, 0), Items.emerald));
        }
    }

    interface ITradeList {
        void func_179401_a(MerchantRecipeList var1, Random var2);
    }

    static class ItemAndEmeraldToItem implements EntityVillager.ITradeList {
        public ItemStack field_179411_a;
        public EntityVillager.PriceInfo field_179409_b;
        public ItemStack field_179410_c;
        public EntityVillager.PriceInfo field_179408_d;
        private static final String __OBFID = "CL_00002191";

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_) {
            this.field_179411_a = new ItemStack(p_i45813_1_);
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = new ItemStack(p_i45813_3_);
            this.field_179408_d = p_i45813_4_;
        }

        public void func_179401_a(MerchantRecipeList p_179401_1_, Random p_179401_2_) {
            int var3 = 1;

            if (this.field_179409_b != null) {
                var3 = this.field_179409_b.func_179412_a(p_179401_2_);
            }

            int var4 = 1;

            if (this.field_179408_d != null) {
                var4 = this.field_179408_d.func_179412_a(p_179401_2_);
            }

            p_179401_1_.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), var3, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), var4, this.field_179410_c.getMetadata())));
        }
    }

    static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList {
        private static final String __OBFID = "CL_00002193";

        public void func_179401_a(MerchantRecipeList p_179401_1_, Random p_179401_2_) {
            Enchantment var3 = Enchantment.enchantmentsList[p_179401_2_.nextInt(Enchantment.enchantmentsList.length)];
            int var4 = MathHelper.getRandomIntegerInRange(p_179401_2_, var3.getMinLevel(), var3.getMaxLevel());
            ItemStack var5 = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var3, var4));
            int var6 = 2 + p_179401_2_.nextInt(5 + var4 * 10) + 3 * var4;

            if (var6 > 64) {
                var6 = 64;
            }

            p_179401_1_.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, var6), var5));
        }
    }

    static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList {
        public ItemStack field_179407_a;
        public EntityVillager.PriceInfo field_179406_b;
        private static final String __OBFID = "CL_00002192";

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_) {
            this.field_179407_a = new ItemStack(p_i45814_1_);
            this.field_179406_b = p_i45814_2_;
        }

        public void func_179401_a(MerchantRecipeList p_179401_1_, Random p_179401_2_) {
            int var3 = 1;

            if (this.field_179406_b != null) {
                var3 = this.field_179406_b.func_179412_a(p_179401_2_);
            }

            ItemStack var4 = new ItemStack(Items.emerald, var3, 0);
            ItemStack var5 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
            var5 = EnchantmentHelper.addRandomEnchantment(p_179401_2_, var5, 5 + p_179401_2_.nextInt(15));
            p_179401_1_.add(new MerchantRecipe(var4, var5));
        }
    }

    static class ListItemForEmeralds implements EntityVillager.ITradeList {
        public ItemStack field_179403_a;
        public EntityVillager.PriceInfo field_179402_b;
        private static final String __OBFID = "CL_00002190";

        public ListItemForEmeralds(Item p_i45811_1_, EntityVillager.PriceInfo p_i45811_2_) {
            this.field_179403_a = new ItemStack(p_i45811_1_);
            this.field_179402_b = p_i45811_2_;
        }

        public ListItemForEmeralds(ItemStack p_i45812_1_, EntityVillager.PriceInfo p_i45812_2_) {
            this.field_179403_a = p_i45812_1_;
            this.field_179402_b = p_i45812_2_;
        }

        public void func_179401_a(MerchantRecipeList p_179401_1_, Random p_179401_2_) {
            int var3 = 1;

            if (this.field_179402_b != null) {
                var3 = this.field_179402_b.func_179412_a(p_179401_2_);
            }

            ItemStack var4;
            ItemStack var5;

            if (var3 < 0) {
                var4 = new ItemStack(Items.emerald, 1, 0);
                var5 = new ItemStack(this.field_179403_a.getItem(), -var3, this.field_179403_a.getMetadata());
            } else {
                var4 = new ItemStack(Items.emerald, var3, 0);
                var5 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
            }

            p_179401_1_.add(new MerchantRecipe(var4, var5));
        }
    }

    static class PriceInfo extends Tuple {
        private static final String __OBFID = "CL_00002189";

        public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
        }

        public int func_179412_a(Random p_179412_1_) {
            return ((Integer) this.getFirst()).intValue() >= ((Integer) this.getSecond()).intValue() ? ((Integer) this.getFirst()).intValue() : ((Integer) this.getFirst()).intValue() + p_179412_1_.nextInt(((Integer) this.getSecond()).intValue() - ((Integer) this.getFirst()).intValue() + 1);
        }
    }
}
