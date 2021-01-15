/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIEatGrass;
/*     */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySheep extends EntityAnimal
/*     */ {
/*  42 */   private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
/*     */   {
/*     */     public boolean canInteractWith(EntityPlayer playerIn)
/*     */     {
/*  46 */       return false;
/*     */     }
/*  42 */   }, 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     2, 1);
/*  49 */   private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private int sheepTimer;
/*     */   
/*     */ 
/*  56 */   private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);
/*     */   
/*     */   public static float[] func_175513_a(EnumDyeColor dyeColor)
/*     */   {
/*  60 */     return (float[])DYE_TO_RGB.get(dyeColor);
/*     */   }
/*     */   
/*     */   public EntitySheep(World worldIn)
/*     */   {
/*  65 */     super(worldIn);
/*  66 */     setSize(0.9F, 1.3F);
/*  67 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  68 */     this.tasks.addTask(0, new EntityAISwimming(this));
/*  69 */     this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
/*  70 */     this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
/*  71 */     this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
/*  72 */     this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
/*  73 */     this.tasks.addTask(5, this.entityAIEatGrass);
/*  74 */     this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
/*  75 */     this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
/*  76 */     this.tasks.addTask(8, new EntityAILookIdle(this));
/*  77 */     this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
/*  78 */     this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/*  83 */     this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
/*  84 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  93 */     if (this.worldObj.isRemote)
/*     */     {
/*  95 */       this.sheepTimer = Math.max(0, this.sheepTimer - 1);
/*     */     }
/*     */     
/*  98 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 103 */     super.applyEntityAttributes();
/* 104 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/* 105 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/* 110 */     super.entityInit();
/* 111 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 119 */     if (!getSheared())
/*     */     {
/* 121 */       entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getFleeceColor().getMetadata()), 0.0F);
/*     */     }
/*     */     
/* 124 */     int i = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + p_70628_2_);
/*     */     
/* 126 */     for (int j = 0; j < i; j++)
/*     */     {
/* 128 */       if (isBurning())
/*     */       {
/* 130 */         dropItem(Items.cooked_mutton, 1);
/*     */       }
/*     */       else
/*     */       {
/* 134 */         dropItem(Items.mutton, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 141 */     return Item.getItemFromBlock(Blocks.wool);
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 146 */     if (id == 10)
/*     */     {
/* 148 */       this.sheepTimer = 40;
/*     */     }
/*     */     else
/*     */     {
/* 152 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getHeadRotationPointY(float p_70894_1_)
/*     */   {
/* 158 */     return this.sheepTimer < 4 ? (this.sheepTimer - p_70894_1_) / 4.0F : (this.sheepTimer >= 4) && (this.sheepTimer <= 36) ? 1.0F : this.sheepTimer <= 0 ? 0.0F : -(this.sheepTimer - 40 - p_70894_1_) / 4.0F;
/*     */   }
/*     */   
/*     */   public float getHeadRotationAngleX(float p_70890_1_)
/*     */   {
/* 163 */     if ((this.sheepTimer > 4) && (this.sheepTimer <= 36))
/*     */     {
/* 165 */       float f = (this.sheepTimer - 4 - p_70890_1_) / 32.0F;
/* 166 */       return 0.62831855F + 0.2199115F * MathHelper.sin(f * 28.7F);
/*     */     }
/*     */     
/*     */ 
/* 170 */     return this.sheepTimer > 0 ? 0.62831855F : this.rotationPitch / 57.295776F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/* 179 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 181 */     if ((itemstack != null) && (itemstack.getItem() == Items.shears) && (!getSheared()) && (!isChild()))
/*     */     {
/* 183 */       if (!this.worldObj.isRemote)
/*     */       {
/* 185 */         setSheared(true);
/* 186 */         int i = 1 + this.rand.nextInt(3);
/*     */         
/* 188 */         for (int j = 0; j < i; j++)
/*     */         {
/* 190 */           EntityItem entityitem = entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getFleeceColor().getMetadata()), 1.0F);
/* 191 */           entityitem.motionY += this.rand.nextFloat() * 0.05F;
/* 192 */           entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
/* 193 */           entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
/*     */         }
/*     */       }
/*     */       
/* 197 */       itemstack.damageItem(1, player);
/* 198 */       playSound("mob.sheep.shear", 1.0F, 1.0F);
/*     */     }
/*     */     
/* 201 */     return super.interact(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 209 */     super.writeEntityToNBT(tagCompound);
/* 210 */     tagCompound.setBoolean("Sheared", getSheared());
/* 211 */     tagCompound.setByte("Color", (byte)getFleeceColor().getMetadata());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 219 */     super.readEntityFromNBT(tagCompund);
/* 220 */     setSheared(tagCompund.getBoolean("Sheared"));
/* 221 */     setFleeceColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 229 */     return "mob.sheep.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 237 */     return "mob.sheep.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 245 */     return "mob.sheep.say";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 250 */     playSound("mob.sheep.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumDyeColor getFleeceColor()
/*     */   {
/* 258 */     return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 0xF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFleeceColor(EnumDyeColor color)
/*     */   {
/* 266 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/* 267 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xF0 | color.getMetadata() & 0xF)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getSheared()
/*     */   {
/* 275 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSheared(boolean sheared)
/*     */   {
/* 283 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 285 */     if (sheared)
/*     */     {
/* 287 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x10)));
/*     */     }
/*     */     else
/*     */     {
/* 291 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumDyeColor getRandomSheepColor(Random random)
/*     */   {
/* 300 */     int i = random.nextInt(100);
/* 301 */     return random.nextInt(500) == 0 ? EnumDyeColor.PINK : i < 18 ? EnumDyeColor.BROWN : i < 15 ? EnumDyeColor.SILVER : i < 10 ? EnumDyeColor.GRAY : i < 5 ? EnumDyeColor.BLACK : EnumDyeColor.WHITE;
/*     */   }
/*     */   
/*     */   public EntitySheep createChild(EntityAgeable ageable)
/*     */   {
/* 306 */     EntitySheep entitysheep = (EntitySheep)ageable;
/* 307 */     EntitySheep entitysheep1 = new EntitySheep(this.worldObj);
/* 308 */     entitysheep1.setFleeceColor(getDyeColorMixFromParents(this, entitysheep));
/* 309 */     return entitysheep1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void eatGrassBonus()
/*     */   {
/* 318 */     setSheared(false);
/*     */     
/* 320 */     if (isChild())
/*     */     {
/* 322 */       addGrowth(60);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/* 332 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 333 */     setFleeceColor(getRandomSheepColor(this.worldObj.rand));
/* 334 */     return livingdata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother)
/*     */   {
/* 342 */     int i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
/* 343 */     int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
/* 344 */     this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
/* 345 */     this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
/* 346 */     ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).worldObj);
/*     */     int k;
/*     */     int k;
/* 349 */     if ((itemstack != null) && (itemstack.getItem() == Items.dye))
/*     */     {
/* 351 */       k = itemstack.getMetadata();
/*     */     }
/*     */     else
/*     */     {
/* 355 */       k = this.worldObj.rand.nextBoolean() ? i : j;
/*     */     }
/*     */     
/* 358 */     return EnumDyeColor.byDyeDamage(k);
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 363 */     return 0.95F * this.height;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 368 */     DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 1.0F, 1.0F, 1.0F });
/* 369 */     DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] { 0.85F, 0.5F, 0.2F });
/* 370 */     DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] { 0.7F, 0.3F, 0.85F });
/* 371 */     DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] { 0.4F, 0.6F, 0.85F });
/* 372 */     DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] { 0.9F, 0.9F, 0.2F });
/* 373 */     DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] { 0.5F, 0.8F, 0.1F });
/* 374 */     DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] { 0.95F, 0.5F, 0.65F });
/* 375 */     DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] { 0.3F, 0.3F, 0.3F });
/* 376 */     DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] { 0.6F, 0.6F, 0.6F });
/* 377 */     DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] { 0.3F, 0.5F, 0.6F });
/* 378 */     DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] { 0.5F, 0.25F, 0.7F });
/* 379 */     DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] { 0.2F, 0.3F, 0.7F });
/* 380 */     DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] { 0.4F, 0.3F, 0.2F });
/* 381 */     DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] { 0.4F, 0.5F, 0.2F });
/* 382 */     DYE_TO_RGB.put(EnumDyeColor.RED, new float[] { 0.6F, 0.2F, 0.2F });
/* 383 */     DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] { 0.1F, 0.1F, 0.1F });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntitySheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */