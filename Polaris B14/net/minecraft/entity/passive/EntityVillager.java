/*      */ package net.minecraft.entity.passive;
/*      */ 
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.enchantment.EnchantmentData;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityAgeable;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityLivingData;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.INpc;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*      */ import net.minecraft.entity.ai.EntityAIFollowGolem;
/*      */ import net.minecraft.entity.ai.EntityAIHarvestFarmland;
/*      */ import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIMoveIndoors;
/*      */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*      */ import net.minecraft.entity.ai.EntityAIOpenDoor;
/*      */ import net.minecraft.entity.ai.EntityAIPlay;
/*      */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*      */ import net.minecraft.entity.ai.EntityAISwimming;
/*      */ import net.minecraft.entity.ai.EntityAITasks;
/*      */ import net.minecraft.entity.ai.EntityAITradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIVillagerInteract;
/*      */ import net.minecraft.entity.ai.EntityAIVillagerMate;
/*      */ import net.minecraft.entity.ai.EntityAIWander;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest2;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.monster.EntityWitch;
/*      */ import net.minecraft.entity.monster.EntityZombie;
/*      */ import net.minecraft.entity.monster.IMob;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.item.EnumDyeColor;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemEnchantedBook;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Tuple;
/*      */ import net.minecraft.village.MerchantRecipe;
/*      */ import net.minecraft.village.MerchantRecipeList;
/*      */ import net.minecraft.village.Village;
/*      */ import net.minecraft.village.VillageCollection;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class EntityVillager
/*      */   extends EntityAgeable
/*      */   implements IMerchant, INpc
/*      */ {
/*      */   private int randomTickDivider;
/*      */   private boolean isMating;
/*      */   private boolean isPlaying;
/*      */   Village villageObj;
/*      */   private EntityPlayer buyingPlayer;
/*      */   private MerchantRecipeList buyingList;
/*      */   private int timeUntilReset;
/*      */   private boolean needsInitilization;
/*      */   private boolean isWillingToMate;
/*      */   private int wealth;
/*      */   private String lastBuyingPlayer;
/*      */   private int careerId;
/*      */   private int careerLevel;
/*      */   private boolean isLookingForHome;
/*      */   private boolean areAdditionalTasksSet;
/*      */   private InventoryBasic villagerInventory;
/*   96 */   private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = { { { { new EmeraldForItems(Items.wheat, new PriceInfo(18, 22)), new EmeraldForItems(Items.potato, new PriceInfo(15, 19)), new EmeraldForItems(Items.carrot, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.bread, new PriceInfo(-4, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.pumpkin_pie, new PriceInfo(-3, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.apple, new PriceInfo(-5, -7)) }, { new ListItemForEmeralds(Items.cookie, new PriceInfo(-6, -10)), new ListItemForEmeralds(Items.cake, new PriceInfo(1, 1)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.fish, new PriceInfo(6, 6), Items.cooked_fish, new PriceInfo(6, 6)) }, { new ListEnchantedItemForEmeralds(Items.fishing_rod, new PriceInfo(7, 8)) } }, { { new EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.shears, new PriceInfo(3, 4)) }, { new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new PriceInfo(1, 2)) } }, { { new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.arrow, new PriceInfo(-12, -8)) }, { new ListItemForEmeralds(Items.bow, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new PriceInfo(10, 10), Items.flint, new PriceInfo(6, 10)) } } }, { { { new EmeraldForItems(Items.paper, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds() }, { new EmeraldForItems(Items.book, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.compass, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new PriceInfo(3, 4)) }, { new EmeraldForItems(Items.written_book, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.clock, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new PriceInfo(-5, -3)) }, { new ListEnchantedBookForEmeralds() }, { new ListEnchantedBookForEmeralds() }, { new ListItemForEmeralds(Items.name_tag, new PriceInfo(20, 22)) } } }, { { { new EmeraldForItems(Items.rotten_flesh, new PriceInfo(36, 40)), new EmeraldForItems(Items.gold_ingot, new PriceInfo(8, 10)) }, { new ListItemForEmeralds(Items.redstone, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1)) }, { new ListItemForEmeralds(Items.ender_eye, new PriceInfo(7, 11)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new PriceInfo(-3, -1)) }, { new ListItemForEmeralds(Items.experience_bottle, new PriceInfo(3, 11)) } } }, { { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_helmet, new PriceInfo(4, 6)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.iron_chestplate, new PriceInfo(10, 14)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_chestplate, new PriceInfo(16, 19)) }, { new ListItemForEmeralds(Items.chainmail_boots, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_leggings, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.chainmail_helmet, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_chestplate, new PriceInfo(11, 15)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_axe, new PriceInfo(6, 8)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_sword, new PriceInfo(9, 10)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_sword, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.diamond_axe, new PriceInfo(9, 12)) } }, { { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.iron_shovel, new PriceInfo(5, 7)) }, { new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_pickaxe, new PriceInfo(9, 11)) }, { new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new PriceInfo(12, 15)) } } }, { { { new EmeraldForItems(Items.porkchop, new PriceInfo(14, 18)), new EmeraldForItems(Items.chicken, new PriceInfo(14, 18)) }, { new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.cooked_porkchop, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.cooked_chicken, new PriceInfo(-8, -6)) } }, { { new EmeraldForItems(Items.leather, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.leather_leggings, new PriceInfo(2, 4)) }, { new ListEnchantedItemForEmeralds(Items.leather_chestplate, new PriceInfo(7, 12)) }, { new ListItemForEmeralds(Items.saddle, new PriceInfo(8, 10)) } } } };
/*      */   
/*      */   public EntityVillager(World worldIn)
/*      */   {
/*  100 */     this(worldIn, 0);
/*      */   }
/*      */   
/*      */   public EntityVillager(World worldIn, int professionId)
/*      */   {
/*  105 */     super(worldIn);
/*  106 */     this.villagerInventory = new InventoryBasic("Items", false, 8);
/*  107 */     setProfession(professionId);
/*  108 */     setSize(0.6F, 1.8F);
/*  109 */     ((PathNavigateGround)getNavigator()).setBreakDoors(true);
/*  110 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  111 */     this.tasks.addTask(0, new EntityAISwimming(this));
/*  112 */     this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
/*  113 */     this.tasks.addTask(1, new EntityAITradePlayer(this));
/*  114 */     this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
/*  115 */     this.tasks.addTask(2, new EntityAIMoveIndoors(this));
/*  116 */     this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
/*  117 */     this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
/*  118 */     this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
/*  119 */     this.tasks.addTask(6, new EntityAIVillagerMate(this));
/*  120 */     this.tasks.addTask(7, new EntityAIFollowGolem(this));
/*  121 */     this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
/*  122 */     this.tasks.addTask(9, new EntityAIVillagerInteract(this));
/*  123 */     this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
/*  124 */     this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
/*  125 */     setCanPickUpLoot(true);
/*      */   }
/*      */   
/*      */   private void setAdditionalAItasks()
/*      */   {
/*  130 */     if (!this.areAdditionalTasksSet)
/*      */     {
/*  132 */       this.areAdditionalTasksSet = true;
/*      */       
/*  134 */       if (isChild())
/*      */       {
/*  136 */         this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
/*      */       }
/*  138 */       else if (getProfession() == 0)
/*      */       {
/*  140 */         this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onGrowingAdult()
/*      */   {
/*  151 */     if (getProfession() == 0)
/*      */     {
/*  153 */       this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
/*      */     }
/*      */     
/*  156 */     super.onGrowingAdult();
/*      */   }
/*      */   
/*      */   protected void applyEntityAttributes()
/*      */   {
/*  161 */     super.applyEntityAttributes();
/*  162 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
/*      */   }
/*      */   
/*      */   protected void updateAITasks() {
/*      */     BlockPos blockpos1;
/*  167 */     if (--this.randomTickDivider <= 0)
/*      */     {
/*  169 */       BlockPos blockpos = new BlockPos(this);
/*  170 */       this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
/*  171 */       this.randomTickDivider = (70 + this.rand.nextInt(50));
/*  172 */       this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);
/*      */       
/*  174 */       if (this.villageObj == null)
/*      */       {
/*  176 */         detachHome();
/*      */       }
/*      */       else
/*      */       {
/*  180 */         blockpos1 = this.villageObj.getCenter();
/*  181 */         setHomePosAndDistance(blockpos1, (int)(this.villageObj.getVillageRadius() * 1.0F));
/*      */         
/*  183 */         if (this.isLookingForHome)
/*      */         {
/*  185 */           this.isLookingForHome = false;
/*  186 */           this.villageObj.setDefaultPlayerReputation(5);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  191 */     if ((!isTrading()) && (this.timeUntilReset > 0))
/*      */     {
/*  193 */       this.timeUntilReset -= 1;
/*      */       
/*  195 */       if (this.timeUntilReset <= 0)
/*      */       {
/*  197 */         if (this.needsInitilization)
/*      */         {
/*  199 */           for (MerchantRecipe merchantrecipe : this.buyingList)
/*      */           {
/*  201 */             if (merchantrecipe.isRecipeDisabled())
/*      */             {
/*  203 */               merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
/*      */             }
/*      */           }
/*      */           
/*  207 */           populateBuyingList();
/*  208 */           this.needsInitilization = false;
/*      */           
/*  210 */           if ((this.villageObj != null) && (this.lastBuyingPlayer != null))
/*      */           {
/*  212 */             this.worldObj.setEntityState(this, (byte)14);
/*  213 */             this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
/*      */           }
/*      */         }
/*      */         
/*  217 */         addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
/*      */       }
/*      */     }
/*      */     
/*  221 */     super.updateAITasks();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean interact(EntityPlayer player)
/*      */   {
/*  229 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*  230 */     boolean flag = (itemstack != null) && (itemstack.getItem() == Items.spawn_egg);
/*      */     
/*  232 */     if ((!flag) && (isEntityAlive()) && (!isTrading()) && (!isChild()))
/*      */     {
/*  234 */       if ((!this.worldObj.isRemote) && ((this.buyingList == null) || (this.buyingList.size() > 0)))
/*      */       {
/*  236 */         setCustomer(player);
/*  237 */         player.displayVillagerTradeGui(this);
/*      */       }
/*      */       
/*  240 */       player.triggerAchievement(StatList.timesTalkedToVillagerStat);
/*  241 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  245 */     return super.interact(player);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void entityInit()
/*      */   {
/*  251 */     super.entityInit();
/*  252 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/*  260 */     super.writeEntityToNBT(tagCompound);
/*  261 */     tagCompound.setInteger("Profession", getProfession());
/*  262 */     tagCompound.setInteger("Riches", this.wealth);
/*  263 */     tagCompound.setInteger("Career", this.careerId);
/*  264 */     tagCompound.setInteger("CareerLevel", this.careerLevel);
/*  265 */     tagCompound.setBoolean("Willing", this.isWillingToMate);
/*      */     
/*  267 */     if (this.buyingList != null)
/*      */     {
/*  269 */       tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
/*      */     }
/*      */     
/*  272 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/*  274 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
/*      */     {
/*  276 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  278 */       if (itemstack != null)
/*      */       {
/*  280 */         nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
/*      */       }
/*      */     }
/*      */     
/*  284 */     tagCompound.setTag("Inventory", nbttaglist);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  292 */     super.readEntityFromNBT(tagCompund);
/*  293 */     setProfession(tagCompund.getInteger("Profession"));
/*  294 */     this.wealth = tagCompund.getInteger("Riches");
/*  295 */     this.careerId = tagCompund.getInteger("Career");
/*  296 */     this.careerLevel = tagCompund.getInteger("CareerLevel");
/*  297 */     this.isWillingToMate = tagCompund.getBoolean("Willing");
/*      */     
/*  299 */     if (tagCompund.hasKey("Offers", 10))
/*      */     {
/*  301 */       NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Offers");
/*  302 */       this.buyingList = new MerchantRecipeList(nbttagcompound);
/*      */     }
/*      */     
/*  305 */     NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
/*      */     
/*  307 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */     {
/*  309 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*      */       
/*  311 */       if (itemstack != null)
/*      */       {
/*  313 */         this.villagerInventory.func_174894_a(itemstack);
/*      */       }
/*      */     }
/*      */     
/*  317 */     setCanPickUpLoot(true);
/*  318 */     setAdditionalAItasks();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canDespawn()
/*      */   {
/*  326 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getLivingSound()
/*      */   {
/*  334 */     return isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getHurtSound()
/*      */   {
/*  342 */     return "mob.villager.hit";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDeathSound()
/*      */   {
/*  350 */     return "mob.villager.death";
/*      */   }
/*      */   
/*      */   public void setProfession(int professionId)
/*      */   {
/*  355 */     this.dataWatcher.updateObject(16, Integer.valueOf(professionId));
/*      */   }
/*      */   
/*      */   public int getProfession()
/*      */   {
/*  360 */     return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
/*      */   }
/*      */   
/*      */   public boolean isMating()
/*      */   {
/*  365 */     return this.isMating;
/*      */   }
/*      */   
/*      */   public void setMating(boolean mating)
/*      */   {
/*  370 */     this.isMating = mating;
/*      */   }
/*      */   
/*      */   public void setPlaying(boolean playing)
/*      */   {
/*  375 */     this.isPlaying = playing;
/*      */   }
/*      */   
/*      */   public boolean isPlaying()
/*      */   {
/*  380 */     return this.isPlaying;
/*      */   }
/*      */   
/*      */   public void setRevengeTarget(EntityLivingBase livingBase)
/*      */   {
/*  385 */     super.setRevengeTarget(livingBase);
/*      */     
/*  387 */     if ((this.villageObj != null) && (livingBase != null))
/*      */     {
/*  389 */       this.villageObj.addOrRenewAgressor(livingBase);
/*      */       
/*  391 */       if ((livingBase instanceof EntityPlayer))
/*      */       {
/*  393 */         int i = -1;
/*      */         
/*  395 */         if (isChild())
/*      */         {
/*  397 */           i = -3;
/*      */         }
/*      */         
/*  400 */         this.villageObj.setReputationForPlayer(livingBase.getName(), i);
/*      */         
/*  402 */         if (isEntityAlive())
/*      */         {
/*  404 */           this.worldObj.setEntityState(this, (byte)13);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDeath(DamageSource cause)
/*      */   {
/*  415 */     if (this.villageObj != null)
/*      */     {
/*  417 */       Entity entity = cause.getEntity();
/*      */       
/*  419 */       if (entity != null)
/*      */       {
/*  421 */         if ((entity instanceof EntityPlayer))
/*      */         {
/*  423 */           this.villageObj.setReputationForPlayer(entity.getName(), -2);
/*      */         }
/*  425 */         else if ((entity instanceof IMob))
/*      */         {
/*  427 */           this.villageObj.endMatingSeason();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  432 */         EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
/*      */         
/*  434 */         if (entityplayer != null)
/*      */         {
/*  436 */           this.villageObj.endMatingSeason();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  441 */     super.onDeath(cause);
/*      */   }
/*      */   
/*      */   public void setCustomer(EntityPlayer p_70932_1_)
/*      */   {
/*  446 */     this.buyingPlayer = p_70932_1_;
/*      */   }
/*      */   
/*      */   public EntityPlayer getCustomer()
/*      */   {
/*  451 */     return this.buyingPlayer;
/*      */   }
/*      */   
/*      */   public boolean isTrading()
/*      */   {
/*  456 */     return this.buyingPlayer != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getIsWillingToMate(boolean updateFirst)
/*      */   {
/*  464 */     if ((!this.isWillingToMate) && (updateFirst) && (func_175553_cp()))
/*      */     {
/*  466 */       boolean flag = false;
/*      */       
/*  468 */       for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
/*      */       {
/*  470 */         ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */         
/*  472 */         if (itemstack != null)
/*      */         {
/*  474 */           if ((itemstack.getItem() == Items.bread) && (itemstack.stackSize >= 3))
/*      */           {
/*  476 */             flag = true;
/*  477 */             this.villagerInventory.decrStackSize(i, 3);
/*      */           }
/*  479 */           else if (((itemstack.getItem() == Items.potato) || (itemstack.getItem() == Items.carrot)) && (itemstack.stackSize >= 12))
/*      */           {
/*  481 */             flag = true;
/*  482 */             this.villagerInventory.decrStackSize(i, 12);
/*      */           }
/*      */         }
/*      */         
/*  486 */         if (flag)
/*      */         {
/*  488 */           this.worldObj.setEntityState(this, (byte)18);
/*  489 */           this.isWillingToMate = true;
/*  490 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  495 */     return this.isWillingToMate;
/*      */   }
/*      */   
/*      */   public void setIsWillingToMate(boolean willingToTrade)
/*      */   {
/*  500 */     this.isWillingToMate = willingToTrade;
/*      */   }
/*      */   
/*      */   public void useRecipe(MerchantRecipe recipe)
/*      */   {
/*  505 */     recipe.incrementToolUses();
/*  506 */     this.livingSoundTime = (-getTalkInterval());
/*  507 */     playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
/*  508 */     int i = 3 + this.rand.nextInt(4);
/*      */     
/*  510 */     if ((recipe.getToolUses() == 1) || (this.rand.nextInt(5) == 0))
/*      */     {
/*  512 */       this.timeUntilReset = 40;
/*  513 */       this.needsInitilization = true;
/*  514 */       this.isWillingToMate = true;
/*      */       
/*  516 */       if (this.buyingPlayer != null)
/*      */       {
/*  518 */         this.lastBuyingPlayer = this.buyingPlayer.getName();
/*      */       }
/*      */       else
/*      */       {
/*  522 */         this.lastBuyingPlayer = null;
/*      */       }
/*      */       
/*  525 */       i += 5;
/*      */     }
/*      */     
/*  528 */     if (recipe.getItemToBuy().getItem() == Items.emerald)
/*      */     {
/*  530 */       this.wealth += recipe.getItemToBuy().stackSize;
/*      */     }
/*      */     
/*  533 */     if (recipe.getRewardsExp())
/*      */     {
/*  535 */       this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void verifySellingItem(ItemStack stack)
/*      */   {
/*  545 */     if ((!this.worldObj.isRemote) && (this.livingSoundTime > -getTalkInterval() + 20))
/*      */     {
/*  547 */       this.livingSoundTime = (-getTalkInterval());
/*      */       
/*  549 */       if (stack != null)
/*      */       {
/*  551 */         playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
/*      */       }
/*      */       else
/*      */       {
/*  555 */         playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_)
/*      */   {
/*  562 */     if (this.buyingList == null)
/*      */     {
/*  564 */       populateBuyingList();
/*      */     }
/*      */     
/*  567 */     return this.buyingList;
/*      */   }
/*      */   
/*      */   private void populateBuyingList()
/*      */   {
/*  572 */     ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[getProfession()];
/*      */     
/*  574 */     if ((this.careerId != 0) && (this.careerLevel != 0))
/*      */     {
/*  576 */       this.careerLevel += 1;
/*      */     }
/*      */     else
/*      */     {
/*  580 */       this.careerId = (this.rand.nextInt(aentityvillager$itradelist.length) + 1);
/*  581 */       this.careerLevel = 1;
/*      */     }
/*      */     
/*  584 */     if (this.buyingList == null)
/*      */     {
/*  586 */       this.buyingList = new MerchantRecipeList();
/*      */     }
/*      */     
/*  589 */     int i = this.careerId - 1;
/*  590 */     int j = this.careerLevel - 1;
/*  591 */     ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];
/*      */     
/*  593 */     if ((j >= 0) && (j < aentityvillager$itradelist1.length))
/*      */     {
/*  595 */       ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];
/*      */       ITradeList[] arrayOfITradeList1;
/*  597 */       int j = (arrayOfITradeList1 = aentityvillager$itradelist2).length; for (int i = 0; i < j; i++) { ITradeList entityvillager$itradelist = arrayOfITradeList1[i];
/*      */         
/*  599 */         entityvillager$itradelist.modifyMerchantRecipeList(this.buyingList, this.rand);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRecipes(MerchantRecipeList recipeList) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public IChatComponent getDisplayName()
/*      */   {
/*  613 */     String s = getCustomNameTag();
/*      */     
/*  615 */     if ((s != null) && (s.length() > 0))
/*      */     {
/*  617 */       ChatComponentText chatcomponenttext = new ChatComponentText(s);
/*  618 */       chatcomponenttext.getChatStyle().setChatHoverEvent(getHoverEvent());
/*  619 */       chatcomponenttext.getChatStyle().setInsertion(getUniqueID().toString());
/*  620 */       return chatcomponenttext;
/*      */     }
/*      */     
/*      */ 
/*  624 */     if (this.buyingList == null)
/*      */     {
/*  626 */       populateBuyingList();
/*      */     }
/*      */     
/*  629 */     String s1 = null;
/*      */     
/*  631 */     switch (getProfession())
/*      */     {
/*      */     case 0: 
/*  634 */       if (this.careerId == 1)
/*      */       {
/*  636 */         s1 = "farmer";
/*      */       }
/*  638 */       else if (this.careerId == 2)
/*      */       {
/*  640 */         s1 = "fisherman";
/*      */       }
/*  642 */       else if (this.careerId == 3)
/*      */       {
/*  644 */         s1 = "shepherd";
/*      */       }
/*  646 */       else if (this.careerId == 4)
/*      */       {
/*  648 */         s1 = "fletcher";
/*      */       }
/*      */       
/*  651 */       break;
/*      */     
/*      */     case 1: 
/*  654 */       s1 = "librarian";
/*  655 */       break;
/*      */     
/*      */     case 2: 
/*  658 */       s1 = "cleric";
/*  659 */       break;
/*      */     
/*      */     case 3: 
/*  662 */       if (this.careerId == 1)
/*      */       {
/*  664 */         s1 = "armor";
/*      */       }
/*  666 */       else if (this.careerId == 2)
/*      */       {
/*  668 */         s1 = "weapon";
/*      */       }
/*  670 */       else if (this.careerId == 3)
/*      */       {
/*  672 */         s1 = "tool";
/*      */       }
/*      */       
/*  675 */       break;
/*      */     
/*      */     case 4: 
/*  678 */       if (this.careerId == 1)
/*      */       {
/*  680 */         s1 = "butcher";
/*      */       }
/*  682 */       else if (this.careerId == 2)
/*      */       {
/*  684 */         s1 = "leather";
/*      */       }
/*      */       break;
/*      */     }
/*  688 */     if (s1 != null)
/*      */     {
/*  690 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("entity.Villager." + s1, new Object[0]);
/*  691 */       chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
/*  692 */       chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
/*  693 */       return chatcomponenttranslation;
/*      */     }
/*      */     
/*      */ 
/*  697 */     return super.getDisplayName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public float getEyeHeight()
/*      */   {
/*  704 */     float f = 1.62F;
/*      */     
/*  706 */     if (isChild())
/*      */     {
/*  708 */       f = (float)(f - 0.81D);
/*      */     }
/*      */     
/*  711 */     return f;
/*      */   }
/*      */   
/*      */   public void handleStatusUpdate(byte id)
/*      */   {
/*  716 */     if (id == 12)
/*      */     {
/*  718 */       spawnParticles(EnumParticleTypes.HEART);
/*      */     }
/*  720 */     else if (id == 13)
/*      */     {
/*  722 */       spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
/*      */     }
/*  724 */     else if (id == 14)
/*      */     {
/*  726 */       spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
/*      */     }
/*      */     else
/*      */     {
/*  730 */       super.handleStatusUpdate(id);
/*      */     }
/*      */   }
/*      */   
/*      */   private void spawnParticles(EnumParticleTypes particleType)
/*      */   {
/*  736 */     for (int i = 0; i < 5; i++)
/*      */     {
/*  738 */       double d0 = this.rand.nextGaussian() * 0.02D;
/*  739 */       double d1 = this.rand.nextGaussian() * 0.02D;
/*  740 */       double d2 = this.rand.nextGaussian() * 0.02D;
/*  741 */       this.worldObj.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 1.0D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*      */   {
/*  751 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*  752 */     setProfession(this.worldObj.rand.nextInt(5));
/*  753 */     setAdditionalAItasks();
/*  754 */     return livingdata;
/*      */   }
/*      */   
/*      */   public void setLookingForHome()
/*      */   {
/*  759 */     this.isLookingForHome = true;
/*      */   }
/*      */   
/*      */   public EntityVillager createChild(EntityAgeable ageable)
/*      */   {
/*  764 */     EntityVillager entityvillager = new EntityVillager(this.worldObj);
/*  765 */     entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)), null);
/*  766 */     return entityvillager;
/*      */   }
/*      */   
/*      */   public boolean allowLeashing()
/*      */   {
/*  771 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onStruckByLightning(EntityLightningBolt lightningBolt)
/*      */   {
/*  779 */     if ((!this.worldObj.isRemote) && (!this.isDead))
/*      */     {
/*  781 */       EntityWitch entitywitch = new EntityWitch(this.worldObj);
/*  782 */       entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  783 */       entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entitywitch)), null);
/*  784 */       entitywitch.setNoAI(isAIDisabled());
/*      */       
/*  786 */       if (hasCustomName())
/*      */       {
/*  788 */         entitywitch.setCustomNameTag(getCustomNameTag());
/*  789 */         entitywitch.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
/*      */       }
/*      */       
/*  792 */       this.worldObj.spawnEntityInWorld(entitywitch);
/*  793 */       setDead();
/*      */     }
/*      */   }
/*      */   
/*      */   public InventoryBasic getVillagerInventory()
/*      */   {
/*  799 */     return this.villagerInventory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateEquipmentIfNeeded(EntityItem itemEntity)
/*      */   {
/*  808 */     ItemStack itemstack = itemEntity.getEntityItem();
/*  809 */     Item item = itemstack.getItem();
/*      */     
/*  811 */     if (canVillagerPickupItem(item))
/*      */     {
/*  813 */       ItemStack itemstack1 = this.villagerInventory.func_174894_a(itemstack);
/*      */       
/*  815 */       if (itemstack1 == null)
/*      */       {
/*  817 */         itemEntity.setDead();
/*      */       }
/*      */       else
/*      */       {
/*  821 */         itemstack.stackSize = itemstack1.stackSize;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean canVillagerPickupItem(Item itemIn)
/*      */   {
/*  828 */     return (itemIn == Items.bread) || (itemIn == Items.potato) || (itemIn == Items.carrot) || (itemIn == Items.wheat) || (itemIn == Items.wheat_seeds);
/*      */   }
/*      */   
/*      */   public boolean func_175553_cp()
/*      */   {
/*  833 */     return hasEnoughItems(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canAbondonItems()
/*      */   {
/*  842 */     return hasEnoughItems(2);
/*      */   }
/*      */   
/*      */   public boolean func_175557_cr()
/*      */   {
/*  847 */     boolean flag = getProfession() == 0;
/*  848 */     return !hasEnoughItems(5);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasEnoughItems(int multiplier)
/*      */   {
/*  856 */     boolean flag = getProfession() == 0;
/*      */     
/*  858 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
/*      */     {
/*  860 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  862 */       if (itemstack != null)
/*      */       {
/*  864 */         if (((itemstack.getItem() == Items.bread) && (itemstack.stackSize >= 3 * multiplier)) || ((itemstack.getItem() == Items.potato) && (itemstack.stackSize >= 12 * multiplier)) || ((itemstack.getItem() == Items.carrot) && (itemstack.stackSize >= 12 * multiplier)))
/*      */         {
/*  866 */           return true;
/*      */         }
/*      */         
/*  869 */         if ((flag) && (itemstack.getItem() == Items.wheat) && (itemstack.stackSize >= 9 * multiplier))
/*      */         {
/*  871 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  876 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFarmItemInInventory()
/*      */   {
/*  884 */     for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
/*      */     {
/*  886 */       ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
/*      */       
/*  888 */       if ((itemstack != null) && ((itemstack.getItem() == Items.wheat_seeds) || (itemstack.getItem() == Items.potato) || (itemstack.getItem() == Items.carrot)))
/*      */       {
/*  890 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  894 */     return false;
/*      */   }
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*      */   {
/*  899 */     if (super.replaceItemInInventory(inventorySlot, itemStackIn))
/*      */     {
/*  901 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  905 */     int i = inventorySlot - 300;
/*      */     
/*  907 */     if ((i >= 0) && (i < this.villagerInventory.getSizeInventory()))
/*      */     {
/*  909 */       this.villagerInventory.setInventorySlotContents(i, itemStackIn);
/*  910 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  914 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   static class EmeraldForItems
/*      */     implements EntityVillager.ITradeList
/*      */   {
/*      */     public Item sellItem;
/*      */     public EntityVillager.PriceInfo price;
/*      */     
/*      */     public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn)
/*      */     {
/*  926 */       this.sellItem = itemIn;
/*  927 */       this.price = priceIn;
/*      */     }
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
/*      */     {
/*  932 */       int i = 1;
/*      */       
/*  934 */       if (this.price != null)
/*      */       {
/*  936 */         i = this.price.getPrice(random);
/*      */       }
/*      */       
/*  939 */       recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract interface ITradeList
/*      */   {
/*      */     public abstract void modifyMerchantRecipeList(MerchantRecipeList paramMerchantRecipeList, Random paramRandom);
/*      */   }
/*      */   
/*      */   static class ItemAndEmeraldToItem implements EntityVillager.ITradeList
/*      */   {
/*      */     public ItemStack field_179411_a;
/*      */     public EntityVillager.PriceInfo field_179409_b;
/*      */     public ItemStack field_179410_c;
/*      */     public EntityVillager.PriceInfo field_179408_d;
/*      */     
/*      */     public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_)
/*      */     {
/*  957 */       this.field_179411_a = new ItemStack(p_i45813_1_);
/*  958 */       this.field_179409_b = p_i45813_2_;
/*  959 */       this.field_179410_c = new ItemStack(p_i45813_3_);
/*  960 */       this.field_179408_d = p_i45813_4_;
/*      */     }
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
/*      */     {
/*  965 */       int i = 1;
/*      */       
/*  967 */       if (this.field_179409_b != null)
/*      */       {
/*  969 */         i = this.field_179409_b.getPrice(random);
/*      */       }
/*      */       
/*  972 */       int j = 1;
/*      */       
/*  974 */       if (this.field_179408_d != null)
/*      */       {
/*  976 */         j = this.field_179408_d.getPrice(random);
/*      */       }
/*      */       
/*  979 */       recipeList.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), i, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), j, this.field_179410_c.getMetadata())));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList
/*      */   {
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
/*      */     {
/*  987 */       Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
/*  988 */       int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
/*  989 */       ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
/*  990 */       int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
/*      */       
/*  992 */       if (j > 64)
/*      */       {
/*  994 */         j = 64;
/*      */       }
/*      */       
/*  997 */       recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList
/*      */   {
/*      */     public ItemStack field_179407_a;
/*      */     public EntityVillager.PriceInfo field_179406_b;
/*      */     
/*      */     public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_)
/*      */     {
/* 1008 */       this.field_179407_a = new ItemStack(p_i45814_1_);
/* 1009 */       this.field_179406_b = p_i45814_2_;
/*      */     }
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
/*      */     {
/* 1014 */       int i = 1;
/*      */       
/* 1016 */       if (this.field_179406_b != null)
/*      */       {
/* 1018 */         i = this.field_179406_b.getPrice(random);
/*      */       }
/*      */       
/* 1021 */       ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
/* 1022 */       ItemStack itemstack1 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
/* 1023 */       itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
/* 1024 */       recipeList.add(new MerchantRecipe(itemstack, itemstack1));
/*      */     }
/*      */   }
/*      */   
/*      */   static class ListItemForEmeralds implements EntityVillager.ITradeList
/*      */   {
/*      */     public ItemStack field_179403_a;
/*      */     public EntityVillager.PriceInfo field_179402_b;
/*      */     
/*      */     public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo)
/*      */     {
/* 1035 */       this.field_179403_a = new ItemStack(par1Item);
/* 1036 */       this.field_179402_b = priceInfo;
/*      */     }
/*      */     
/*      */     public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo)
/*      */     {
/* 1041 */       this.field_179403_a = stack;
/* 1042 */       this.field_179402_b = priceInfo;
/*      */     }
/*      */     
/*      */     public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
/*      */     {
/* 1047 */       int i = 1;
/*      */       
/* 1049 */       if (this.field_179402_b != null)
/*      */       {
/* 1051 */         i = this.field_179402_b.getPrice(random);
/*      */       }
/*      */       
/*      */       ItemStack itemstack1;
/*      */       ItemStack itemstack;
/*      */       ItemStack itemstack1;
/* 1057 */       if (i < 0)
/*      */       {
/* 1059 */         ItemStack itemstack = new ItemStack(Items.emerald, 1, 0);
/* 1060 */         itemstack1 = new ItemStack(this.field_179403_a.getItem(), -i, this.field_179403_a.getMetadata());
/*      */       }
/*      */       else
/*      */       {
/* 1064 */         itemstack = new ItemStack(Items.emerald, i, 0);
/* 1065 */         itemstack1 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
/*      */       }
/*      */       
/* 1068 */       recipeList.add(new MerchantRecipe(itemstack, itemstack1));
/*      */     }
/*      */   }
/*      */   
/*      */   static class PriceInfo extends Tuple<Integer, Integer>
/*      */   {
/*      */     public PriceInfo(int p_i45810_1_, int p_i45810_2_)
/*      */     {
/* 1076 */       super(Integer.valueOf(p_i45810_2_));
/*      */     }
/*      */     
/*      */     public int getPrice(Random rand)
/*      */     {
/* 1081 */       return ((Integer)getFirst()).intValue() >= ((Integer)getSecond()).intValue() ? ((Integer)getFirst()).intValue() : ((Integer)getFirst()).intValue() + rand.nextInt(((Integer)getSecond()).intValue() - ((Integer)getFirst()).intValue() + 1);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */