/*      */ package net.minecraft.entity;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.ai.EntityAITasks;
/*      */ import net.minecraft.entity.ai.EntityJumpHelper;
/*      */ import net.minecraft.entity.ai.EntityLookHelper;
/*      */ import net.minecraft.entity.ai.EntityMoveHelper;
/*      */ import net.minecraft.entity.ai.EntitySenses;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.monster.EntityGhast;
/*      */ import net.minecraft.entity.monster.EntityMob;
/*      */ import net.minecraft.entity.monster.IMob;
/*      */ import net.minecraft.entity.passive.EntityTameable;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemBlock;
/*      */ import net.minecraft.item.ItemBow;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemSword;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagFloat;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.pathfinding.PathNavigate;
/*      */ import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import optfine.BlockPosM;
/*      */ import optfine.Config;
/*      */ import optfine.Reflector;
/*      */ 
/*      */ 
/*      */ public abstract class EntityLiving
/*      */   extends EntityLivingBase
/*      */ {
/*      */   public int livingSoundTime;
/*      */   protected int experienceValue;
/*      */   private EntityLookHelper lookHelper;
/*      */   protected EntityMoveHelper moveHelper;
/*      */   protected EntityJumpHelper jumpHelper;
/*      */   private EntityBodyHelper bodyHelper;
/*      */   protected PathNavigate navigator;
/*      */   protected final EntityAITasks tasks;
/*      */   protected final EntityAITasks targetTasks;
/*      */   private EntityLivingBase attackTarget;
/*      */   private EntitySenses senses;
/*   70 */   private ItemStack[] equipment = new ItemStack[5];
/*      */   
/*      */ 
/*   73 */   protected float[] equipmentDropChances = new float[5];
/*      */   
/*      */   private boolean canPickUpLoot;
/*      */   
/*      */   private boolean persistenceRequired;
/*      */   
/*      */   private boolean isLeashed;
/*      */   
/*      */   private Entity leashedToEntity;
/*      */   private NBTTagCompound leashNBTTag;
/*      */   private static final String __OBFID = "CL_00001550";
/*   84 */   public int randomMobsId = 0;
/*   85 */   public BiomeGenBase spawnBiome = null;
/*   86 */   public BlockPos spawnPosition = null;
/*      */   
/*      */   public EntityLiving(World worldIn)
/*      */   {
/*   90 */     super(worldIn);
/*   91 */     this.tasks = new EntityAITasks((worldIn != null) && (worldIn.theProfiler != null) ? worldIn.theProfiler : null);
/*   92 */     this.targetTasks = new EntityAITasks((worldIn != null) && (worldIn.theProfiler != null) ? worldIn.theProfiler : null);
/*   93 */     this.lookHelper = new EntityLookHelper(this);
/*   94 */     this.moveHelper = new EntityMoveHelper(this);
/*   95 */     this.jumpHelper = new EntityJumpHelper(this);
/*   96 */     this.bodyHelper = new EntityBodyHelper(this);
/*   97 */     this.navigator = getNewNavigator(worldIn);
/*   98 */     this.senses = new EntitySenses(this);
/*      */     
/*  100 */     for (int i = 0; i < this.equipmentDropChances.length; i++)
/*      */     {
/*  102 */       this.equipmentDropChances[i] = 0.085F;
/*      */     }
/*      */     
/*  105 */     UUID uuid = getUniqueID();
/*  106 */     long j = uuid.getLeastSignificantBits();
/*  107 */     this.randomMobsId = ((int)(j & 0x7FFFFFFF));
/*      */   }
/*      */   
/*      */   protected void applyEntityAttributes()
/*      */   {
/*  112 */     super.applyEntityAttributes();
/*  113 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PathNavigate getNewNavigator(World worldIn)
/*      */   {
/*  121 */     return new PathNavigateGround(this, worldIn);
/*      */   }
/*      */   
/*      */   public EntityLookHelper getLookHelper()
/*      */   {
/*  126 */     return this.lookHelper;
/*      */   }
/*      */   
/*      */   public EntityMoveHelper getMoveHelper()
/*      */   {
/*  131 */     return this.moveHelper;
/*      */   }
/*      */   
/*      */   public EntityJumpHelper getJumpHelper()
/*      */   {
/*  136 */     return this.jumpHelper;
/*      */   }
/*      */   
/*      */   public PathNavigate getNavigator()
/*      */   {
/*  141 */     return this.navigator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntitySenses getEntitySenses()
/*      */   {
/*  149 */     return this.senses;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityLivingBase getAttackTarget()
/*      */   {
/*  157 */     return this.attackTarget;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
/*      */   {
/*  165 */     this.attackTarget = entitylivingbaseIn;
/*  166 */     Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, new Object[] { this, entitylivingbaseIn });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canAttackClass(Class cls)
/*      */   {
/*  174 */     return cls != EntityGhast.class;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void eatGrassBonus() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void entityInit()
/*      */   {
/*  187 */     super.entityInit();
/*  188 */     this.dataWatcher.addObject(15, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTalkInterval()
/*      */   {
/*  196 */     return 80;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playLivingSound()
/*      */   {
/*  204 */     String s = getLivingSound();
/*      */     
/*  206 */     if (s != null)
/*      */     {
/*  208 */       playSound(s, getSoundVolume(), getSoundPitch());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityUpdate()
/*      */   {
/*  217 */     super.onEntityUpdate();
/*  218 */     this.worldObj.theProfiler.startSection("mobBaseTick");
/*      */     
/*  220 */     if ((isEntityAlive()) && (this.rand.nextInt(1000) < this.livingSoundTime++))
/*      */     {
/*  222 */       this.livingSoundTime = (-getTalkInterval());
/*  223 */       playLivingSound();
/*      */     }
/*      */     
/*  226 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getExperiencePoints(EntityPlayer player)
/*      */   {
/*  234 */     if (this.experienceValue > 0)
/*      */     {
/*  236 */       int i = this.experienceValue;
/*  237 */       ItemStack[] aitemstack = getInventory();
/*      */       
/*  239 */       for (int j = 0; j < aitemstack.length; j++)
/*      */       {
/*  241 */         if ((aitemstack[j] != null) && (this.equipmentDropChances[j] <= 1.0F))
/*      */         {
/*  243 */           i += 1 + this.rand.nextInt(3);
/*      */         }
/*      */       }
/*      */       
/*  247 */       return i;
/*      */     }
/*      */     
/*      */ 
/*  251 */     return this.experienceValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void spawnExplosionParticle()
/*      */   {
/*  260 */     if (this.worldObj.isRemote)
/*      */     {
/*  262 */       for (int i = 0; i < 20; i++)
/*      */       {
/*  264 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  265 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  266 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  267 */         double d3 = 10.0D;
/*  268 */         this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width - d0 * d3, this.posY + this.rand.nextFloat() * this.height - d1 * d3, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width - d2 * d3, d0, d1, d2, new int[0]);
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  273 */       this.worldObj.setEntityState(this, (byte)20);
/*      */     }
/*      */   }
/*      */   
/*      */   public void handleStatusUpdate(byte id)
/*      */   {
/*  279 */     if (id == 20)
/*      */     {
/*  281 */       spawnExplosionParticle();
/*      */     }
/*      */     else
/*      */     {
/*  285 */       super.handleStatusUpdate(id);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/*  294 */     if ((Config.isSmoothWorld()) && (canSkipUpdate()))
/*      */     {
/*  296 */       onUpdateMinimal();
/*      */     }
/*      */     else
/*      */     {
/*  300 */       super.onUpdate();
/*      */       
/*  302 */       if (!this.worldObj.isRemote)
/*      */       {
/*  304 */         updateLeashedState();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected float func_110146_f(float p_110146_1_, float p_110146_2_)
/*      */   {
/*  311 */     this.bodyHelper.updateRenderAngles();
/*  312 */     return p_110146_2_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getLivingSound()
/*      */   {
/*  320 */     return null;
/*      */   }
/*      */   
/*      */   protected Item getDropItem()
/*      */   {
/*  325 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*      */   {
/*  333 */     Item item = getDropItem();
/*      */     
/*  335 */     if (item != null)
/*      */     {
/*  337 */       int i = this.rand.nextInt(3);
/*      */       
/*  339 */       if (p_70628_2_ > 0)
/*      */       {
/*  341 */         i += this.rand.nextInt(p_70628_2_ + 1);
/*      */       }
/*      */       
/*  344 */       for (int j = 0; j < i; j++)
/*      */       {
/*  346 */         dropItem(item, 1);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/*  356 */     super.writeEntityToNBT(tagCompound);
/*  357 */     tagCompound.setBoolean("CanPickUpLoot", canPickUpLoot());
/*  358 */     tagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
/*  359 */     NBTTagList nbttaglist = new NBTTagList();
/*      */     
/*  361 */     for (int i = 0; i < this.equipment.length; i++)
/*      */     {
/*  363 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*      */       
/*  365 */       if (this.equipment[i] != null)
/*      */       {
/*  367 */         this.equipment[i].writeToNBT(nbttagcompound);
/*      */       }
/*      */       
/*  370 */       nbttaglist.appendTag(nbttagcompound);
/*      */     }
/*      */     
/*  373 */     tagCompound.setTag("Equipment", nbttaglist);
/*  374 */     NBTTagList nbttaglist1 = new NBTTagList();
/*      */     
/*  376 */     for (int j = 0; j < this.equipmentDropChances.length; j++)
/*      */     {
/*  378 */       nbttaglist1.appendTag(new NBTTagFloat(this.equipmentDropChances[j]));
/*      */     }
/*      */     
/*  381 */     tagCompound.setTag("DropChances", nbttaglist1);
/*  382 */     tagCompound.setBoolean("Leashed", this.isLeashed);
/*      */     
/*  384 */     if (this.leashedToEntity != null)
/*      */     {
/*  386 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*      */       
/*  388 */       if ((this.leashedToEntity instanceof EntityLivingBase))
/*      */       {
/*  390 */         nbttagcompound1.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
/*  391 */         nbttagcompound1.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
/*      */       }
/*  393 */       else if ((this.leashedToEntity instanceof EntityHanging))
/*      */       {
/*  395 */         BlockPos blockpos = ((EntityHanging)this.leashedToEntity).getHangingPosition();
/*  396 */         nbttagcompound1.setInteger("X", blockpos.getX());
/*  397 */         nbttagcompound1.setInteger("Y", blockpos.getY());
/*  398 */         nbttagcompound1.setInteger("Z", blockpos.getZ());
/*      */       }
/*      */       
/*  401 */       tagCompound.setTag("Leash", nbttagcompound1);
/*      */     }
/*      */     
/*  404 */     if (isAIDisabled())
/*      */     {
/*  406 */       tagCompound.setBoolean("NoAI", isAIDisabled());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  415 */     super.readEntityFromNBT(tagCompund);
/*      */     
/*  417 */     if (tagCompund.hasKey("CanPickUpLoot", 1))
/*      */     {
/*  419 */       setCanPickUpLoot(tagCompund.getBoolean("CanPickUpLoot"));
/*      */     }
/*      */     
/*  422 */     this.persistenceRequired = tagCompund.getBoolean("PersistenceRequired");
/*      */     
/*  424 */     if (tagCompund.hasKey("Equipment", 9))
/*      */     {
/*  426 */       NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
/*      */       
/*  428 */       for (int i = 0; i < this.equipment.length; i++)
/*      */       {
/*  430 */         this.equipment[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*      */       }
/*      */     }
/*      */     
/*  434 */     if (tagCompund.hasKey("DropChances", 9))
/*      */     {
/*  436 */       NBTTagList nbttaglist1 = tagCompund.getTagList("DropChances", 5);
/*      */       
/*  438 */       for (int j = 0; j < nbttaglist1.tagCount(); j++)
/*      */       {
/*  440 */         this.equipmentDropChances[j] = nbttaglist1.getFloatAt(j);
/*      */       }
/*      */     }
/*      */     
/*  444 */     this.isLeashed = tagCompund.getBoolean("Leashed");
/*      */     
/*  446 */     if ((this.isLeashed) && (tagCompund.hasKey("Leash", 10)))
/*      */     {
/*  448 */       this.leashNBTTag = tagCompund.getCompoundTag("Leash");
/*      */     }
/*      */     
/*  451 */     setNoAI(tagCompund.getBoolean("NoAI"));
/*      */   }
/*      */   
/*      */   public void setMoveForward(float p_70657_1_)
/*      */   {
/*  456 */     this.moveForward = p_70657_1_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAIMoveSpeed(float speedIn)
/*      */   {
/*  464 */     super.setAIMoveSpeed(speedIn);
/*  465 */     setMoveForward(speedIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLivingUpdate()
/*      */   {
/*  474 */     super.onLivingUpdate();
/*  475 */     this.worldObj.theProfiler.startSection("looting");
/*      */     
/*  477 */     if ((!this.worldObj.isRemote) && (canPickUpLoot()) && (!this.dead) && (this.worldObj.getGameRules().getBoolean("mobGriefing")))
/*      */     {
/*  479 */       for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
/*      */       {
/*  481 */         if ((!entityitem.isDead) && (entityitem.getEntityItem() != null) && (!entityitem.cannotPickup()))
/*      */         {
/*  483 */           updateEquipmentIfNeeded(entityitem);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  488 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateEquipmentIfNeeded(EntityItem itemEntity)
/*      */   {
/*  497 */     ItemStack itemstack = itemEntity.getEntityItem();
/*  498 */     int i = getArmorPosition(itemstack);
/*      */     
/*  500 */     if (i > -1)
/*      */     {
/*  502 */       boolean flag = true;
/*  503 */       ItemStack itemstack1 = getEquipmentInSlot(i);
/*      */       
/*  505 */       if (itemstack1 != null)
/*      */       {
/*  507 */         if (i == 0)
/*      */         {
/*  509 */           if (((itemstack.getItem() instanceof ItemSword)) && (!(itemstack1.getItem() instanceof ItemSword)))
/*      */           {
/*  511 */             flag = true;
/*      */           }
/*  513 */           else if (((itemstack.getItem() instanceof ItemSword)) && ((itemstack1.getItem() instanceof ItemSword)))
/*      */           {
/*  515 */             ItemSword itemsword = (ItemSword)itemstack.getItem();
/*  516 */             ItemSword itemsword1 = (ItemSword)itemstack1.getItem();
/*      */             
/*  518 */             if (itemsword.getDamageVsEntity() != itemsword1.getDamageVsEntity())
/*      */             {
/*  520 */               flag = itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity();
/*      */             }
/*      */             else
/*      */             {
/*  524 */               flag = (itemstack.getMetadata() > itemstack1.getMetadata()) || ((itemstack.hasTagCompound()) && (!itemstack1.hasTagCompound()));
/*      */             }
/*      */           }
/*  527 */           else if (((itemstack.getItem() instanceof ItemBow)) && ((itemstack1.getItem() instanceof ItemBow)))
/*      */           {
/*  529 */             flag = (itemstack.hasTagCompound()) && (!itemstack1.hasTagCompound());
/*      */           }
/*      */           else
/*      */           {
/*  533 */             flag = false;
/*      */           }
/*      */         }
/*  536 */         else if (((itemstack.getItem() instanceof ItemArmor)) && (!(itemstack1.getItem() instanceof ItemArmor)))
/*      */         {
/*  538 */           flag = true;
/*      */         }
/*  540 */         else if (((itemstack.getItem() instanceof ItemArmor)) && ((itemstack1.getItem() instanceof ItemArmor)))
/*      */         {
/*  542 */           ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
/*  543 */           ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();
/*      */           
/*  545 */           if (itemarmor.damageReduceAmount != itemarmor1.damageReduceAmount)
/*      */           {
/*  547 */             flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
/*      */           }
/*      */           else
/*      */           {
/*  551 */             flag = (itemstack.getMetadata() > itemstack1.getMetadata()) || ((itemstack.hasTagCompound()) && (!itemstack1.hasTagCompound()));
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  556 */           flag = false;
/*      */         }
/*      */       }
/*      */       
/*  560 */       if ((flag) && (func_175448_a(itemstack)))
/*      */       {
/*  562 */         if ((itemstack1 != null) && (this.rand.nextFloat() - 0.1F < this.equipmentDropChances[i]))
/*      */         {
/*  564 */           entityDropItem(itemstack1, 0.0F);
/*      */         }
/*      */         
/*  567 */         if ((itemstack.getItem() == Items.diamond) && (itemEntity.getThrower() != null))
/*      */         {
/*  569 */           EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(itemEntity.getThrower());
/*      */           
/*  571 */           if (entityplayer != null)
/*      */           {
/*  573 */             entityplayer.triggerAchievement(AchievementList.diamondsToYou);
/*      */           }
/*      */         }
/*      */         
/*  577 */         setCurrentItemOrArmor(i, itemstack);
/*  578 */         this.equipmentDropChances[i] = 2.0F;
/*  579 */         this.persistenceRequired = true;
/*  580 */         onItemPickup(itemEntity, 1);
/*  581 */         itemEntity.setDead();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean func_175448_a(ItemStack stack)
/*      */   {
/*  588 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean canDespawn()
/*      */   {
/*  596 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void despawnEntity()
/*      */   {
/*  604 */     Object object = null;
/*  605 */     Object object1 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
/*  606 */     Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
/*      */     
/*  608 */     if (this.persistenceRequired)
/*      */     {
/*  610 */       this.entityAge = 0;
/*      */     } else {
/*  612 */       if ((this.entityAge & 0x1F) == 31) { if ((object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, new Object[] { this })) != object1)
/*      */         {
/*  614 */           if (object == object2)
/*      */           {
/*  616 */             this.entityAge = 0;
/*  617 */             return;
/*      */           }
/*      */           
/*  620 */           setDead();
/*      */           
/*  622 */           return;
/*      */         }
/*      */       }
/*  625 */       EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
/*      */       
/*  627 */       if (entityplayer != null)
/*      */       {
/*  629 */         double d0 = entityplayer.posX - this.posX;
/*  630 */         double d1 = entityplayer.posY - this.posY;
/*  631 */         double d2 = entityplayer.posZ - this.posZ;
/*  632 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */         
/*  634 */         if ((canDespawn()) && (d3 > 16384.0D))
/*      */         {
/*  636 */           setDead();
/*      */         }
/*      */         
/*  639 */         if ((this.entityAge > 600) && (this.rand.nextInt(800) == 0) && (d3 > 1024.0D) && (canDespawn()))
/*      */         {
/*  641 */           setDead();
/*      */         }
/*  643 */         else if (d3 < 1024.0D)
/*      */         {
/*  645 */           this.entityAge = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void updateEntityActionState()
/*      */   {
/*  653 */     this.entityAge += 1;
/*  654 */     this.worldObj.theProfiler.startSection("checkDespawn");
/*  655 */     despawnEntity();
/*  656 */     this.worldObj.theProfiler.endSection();
/*  657 */     this.worldObj.theProfiler.startSection("sensing");
/*  658 */     this.senses.clearSensingCache();
/*  659 */     this.worldObj.theProfiler.endSection();
/*  660 */     this.worldObj.theProfiler.startSection("targetSelector");
/*  661 */     this.targetTasks.onUpdateTasks();
/*  662 */     this.worldObj.theProfiler.endSection();
/*  663 */     this.worldObj.theProfiler.startSection("goalSelector");
/*  664 */     this.tasks.onUpdateTasks();
/*  665 */     this.worldObj.theProfiler.endSection();
/*  666 */     this.worldObj.theProfiler.startSection("navigation");
/*  667 */     this.navigator.onUpdateNavigation();
/*  668 */     this.worldObj.theProfiler.endSection();
/*  669 */     this.worldObj.theProfiler.startSection("mob tick");
/*  670 */     updateAITasks();
/*  671 */     this.worldObj.theProfiler.endSection();
/*  672 */     this.worldObj.theProfiler.startSection("controls");
/*  673 */     this.worldObj.theProfiler.startSection("move");
/*  674 */     this.moveHelper.onUpdateMoveHelper();
/*  675 */     this.worldObj.theProfiler.endStartSection("look");
/*  676 */     this.lookHelper.onUpdateLook();
/*  677 */     this.worldObj.theProfiler.endStartSection("jump");
/*  678 */     this.jumpHelper.doJump();
/*  679 */     this.worldObj.theProfiler.endSection();
/*  680 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateAITasks() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getVerticalFaceSpeed()
/*      */   {
/*  693 */     return 40;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void faceEntity(Entity entityIn, float p_70625_2_, float p_70625_3_)
/*      */   {
/*  701 */     double d0 = entityIn.posX - this.posX;
/*  702 */     double d1 = entityIn.posZ - this.posZ;
/*      */     double d2;
/*      */     double d2;
/*  705 */     if ((entityIn instanceof EntityLivingBase))
/*      */     {
/*  707 */       EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
/*  708 */       d2 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (this.posY + getEyeHeight());
/*      */     }
/*      */     else
/*      */     {
/*  712 */       d2 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (this.posY + getEyeHeight());
/*      */     }
/*      */     
/*  715 */     double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
/*  716 */     float f = (float)(MathHelper.func_181159_b(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/*  717 */     float f1 = (float)-(MathHelper.func_181159_b(d2, d3) * 180.0D / 3.141592653589793D);
/*  718 */     this.rotationPitch = updateRotation(this.rotationPitch, f1, p_70625_3_);
/*  719 */     this.rotationYaw = updateRotation(this.rotationYaw, f, p_70625_2_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
/*      */   {
/*  727 */     float f = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
/*      */     
/*  729 */     if (f > p_70663_3_)
/*      */     {
/*  731 */       f = p_70663_3_;
/*      */     }
/*      */     
/*  734 */     if (f < -p_70663_3_)
/*      */     {
/*  736 */       f = -p_70663_3_;
/*      */     }
/*      */     
/*  739 */     return p_70663_1_ + f;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCanSpawnHere()
/*      */   {
/*  747 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNotColliding()
/*      */   {
/*  755 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getRenderSizeModifier()
/*      */   {
/*  763 */     return 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSpawnedInChunk()
/*      */   {
/*  771 */     return 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxFallHeight()
/*      */   {
/*  779 */     if (getAttackTarget() == null)
/*      */     {
/*  781 */       return 3;
/*      */     }
/*      */     
/*      */ 
/*  785 */     int i = (int)(getHealth() - getMaxHealth() * 0.33F);
/*  786 */     i -= (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;
/*      */     
/*  788 */     if (i < 0)
/*      */     {
/*  790 */       i = 0;
/*      */     }
/*      */     
/*  793 */     return i + 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getHeldItem()
/*      */   {
/*  802 */     return this.equipment[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack getEquipmentInSlot(int slotIn)
/*      */   {
/*  810 */     return this.equipment[slotIn];
/*      */   }
/*      */   
/*      */   public ItemStack getCurrentArmor(int slotIn)
/*      */   {
/*  815 */     return this.equipment[(slotIn + 1)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
/*      */   {
/*  823 */     this.equipment[slotIn] = stack;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack[] getInventory()
/*      */   {
/*  831 */     return this.equipment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dropEquipment(boolean p_82160_1_, int p_82160_2_)
/*      */   {
/*  839 */     for (int i = 0; i < getInventory().length; i++)
/*      */     {
/*  841 */       ItemStack itemstack = getEquipmentInSlot(i);
/*  842 */       boolean flag = this.equipmentDropChances[i] > 1.0F;
/*      */       
/*  844 */       if ((itemstack != null) && ((p_82160_1_) || (flag)) && (this.rand.nextFloat() - p_82160_2_ * 0.01F < this.equipmentDropChances[i]))
/*      */       {
/*  846 */         if ((!flag) && (itemstack.isItemStackDamageable()))
/*      */         {
/*  848 */           int j = Math.max(itemstack.getMaxDamage() - 25, 1);
/*  849 */           int k = itemstack.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(j) + 1);
/*      */           
/*  851 */           if (k > j)
/*      */           {
/*  853 */             k = j;
/*      */           }
/*      */           
/*  856 */           if (k < 1)
/*      */           {
/*  858 */             k = 1;
/*      */           }
/*      */           
/*  861 */           itemstack.setItemDamage(k);
/*      */         }
/*      */         
/*  864 */         entityDropItem(itemstack, 0.0F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
/*      */   {
/*  874 */     if (this.rand.nextFloat() < 0.15F * difficulty.getClampedAdditionalDifficulty())
/*      */     {
/*  876 */       int i = this.rand.nextInt(2);
/*  877 */       float f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;
/*      */       
/*  879 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  881 */         i++;
/*      */       }
/*      */       
/*  884 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  886 */         i++;
/*      */       }
/*      */       
/*  889 */       if (this.rand.nextFloat() < 0.095F)
/*      */       {
/*  891 */         i++;
/*      */       }
/*      */       
/*  894 */       for (int j = 3; j >= 0; j--)
/*      */       {
/*  896 */         ItemStack itemstack = getCurrentArmor(j);
/*      */         
/*  898 */         if ((j < 3) && (this.rand.nextFloat() < f)) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*  903 */         if (itemstack == null)
/*      */         {
/*  905 */           Item item = getArmorItemForSlot(j + 1, i);
/*      */           
/*  907 */           if (item != null)
/*      */           {
/*  909 */             setCurrentItemOrArmor(j + 1, new ItemStack(item));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getArmorPosition(ItemStack stack)
/*      */   {
/*  918 */     if ((stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin)) && (stack.getItem() != Items.skull))
/*      */     {
/*  920 */       if ((stack.getItem() instanceof ItemArmor))
/*      */       {
/*  922 */         switch (((ItemArmor)stack.getItem()).armorType)
/*      */         {
/*      */         case 0: 
/*  925 */           return 4;
/*      */         
/*      */         case 1: 
/*  928 */           return 3;
/*      */         
/*      */         case 2: 
/*  931 */           return 2;
/*      */         
/*      */         case 3: 
/*  934 */           return 1;
/*      */         }
/*      */         
/*      */       }
/*  938 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  942 */     return 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Item getArmorItemForSlot(int armorSlot, int itemTier)
/*      */   {
/*  951 */     switch (armorSlot)
/*      */     {
/*      */     case 4: 
/*  954 */       if (itemTier == 0)
/*      */       {
/*  956 */         return Items.leather_helmet;
/*      */       }
/*  958 */       if (itemTier == 1)
/*      */       {
/*  960 */         return Items.golden_helmet;
/*      */       }
/*  962 */       if (itemTier == 2)
/*      */       {
/*  964 */         return Items.chainmail_helmet;
/*      */       }
/*  966 */       if (itemTier == 3)
/*      */       {
/*  968 */         return Items.iron_helmet;
/*      */       }
/*  970 */       if (itemTier == 4)
/*      */       {
/*  972 */         return Items.diamond_helmet;
/*      */       }
/*      */     
/*      */     case 3: 
/*  976 */       if (itemTier == 0)
/*      */       {
/*  978 */         return Items.leather_chestplate;
/*      */       }
/*  980 */       if (itemTier == 1)
/*      */       {
/*  982 */         return Items.golden_chestplate;
/*      */       }
/*  984 */       if (itemTier == 2)
/*      */       {
/*  986 */         return Items.chainmail_chestplate;
/*      */       }
/*  988 */       if (itemTier == 3)
/*      */       {
/*  990 */         return Items.iron_chestplate;
/*      */       }
/*  992 */       if (itemTier == 4)
/*      */       {
/*  994 */         return Items.diamond_chestplate;
/*      */       }
/*      */     
/*      */     case 2: 
/*  998 */       if (itemTier == 0)
/*      */       {
/* 1000 */         return Items.leather_leggings;
/*      */       }
/* 1002 */       if (itemTier == 1)
/*      */       {
/* 1004 */         return Items.golden_leggings;
/*      */       }
/* 1006 */       if (itemTier == 2)
/*      */       {
/* 1008 */         return Items.chainmail_leggings;
/*      */       }
/* 1010 */       if (itemTier == 3)
/*      */       {
/* 1012 */         return Items.iron_leggings;
/*      */       }
/* 1014 */       if (itemTier == 4)
/*      */       {
/* 1016 */         return Items.diamond_leggings;
/*      */       }
/*      */     
/*      */     case 1: 
/* 1020 */       if (itemTier == 0)
/*      */       {
/* 1022 */         return Items.leather_boots;
/*      */       }
/* 1024 */       if (itemTier == 1)
/*      */       {
/* 1026 */         return Items.golden_boots;
/*      */       }
/* 1028 */       if (itemTier == 2)
/*      */       {
/* 1030 */         return Items.chainmail_boots;
/*      */       }
/* 1032 */       if (itemTier == 3)
/*      */       {
/* 1034 */         return Items.iron_boots;
/*      */       }
/* 1036 */       if (itemTier == 4)
/*      */       {
/* 1038 */         return Items.diamond_boots;
/*      */       }
/*      */       break;
/*      */     }
/* 1042 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty)
/*      */   {
/* 1051 */     float f = difficulty.getClampedAdditionalDifficulty();
/*      */     
/* 1053 */     if ((getHeldItem() != null) && (this.rand.nextFloat() < 0.25F * f))
/*      */     {
/* 1055 */       EnchantmentHelper.addRandomEnchantment(this.rand, getHeldItem(), (int)(5.0F + f * this.rand.nextInt(18)));
/*      */     }
/*      */     
/* 1058 */     for (int i = 0; i < 4; i++)
/*      */     {
/* 1060 */       ItemStack itemstack = getCurrentArmor(i);
/*      */       
/* 1062 */       if ((itemstack != null) && (this.rand.nextFloat() < 0.5F * f))
/*      */       {
/* 1064 */         EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0F + f * this.rand.nextInt(18)));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*      */   {
/* 1075 */     getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
/* 1076 */     return livingdata;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBeSteered()
/*      */   {
/* 1085 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enablePersistence()
/*      */   {
/* 1093 */     this.persistenceRequired = true;
/*      */   }
/*      */   
/*      */   public void setEquipmentDropChance(int slotIn, float chance)
/*      */   {
/* 1098 */     this.equipmentDropChances[slotIn] = chance;
/*      */   }
/*      */   
/*      */   public boolean canPickUpLoot()
/*      */   {
/* 1103 */     return this.canPickUpLoot;
/*      */   }
/*      */   
/*      */   public void setCanPickUpLoot(boolean canPickup)
/*      */   {
/* 1108 */     this.canPickUpLoot = canPickup;
/*      */   }
/*      */   
/*      */   public boolean isNoDespawnRequired()
/*      */   {
/* 1113 */     return this.persistenceRequired;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean interactFirst(EntityPlayer playerIn)
/*      */   {
/* 1121 */     if ((getLeashed()) && (getLeashedToEntity() == playerIn))
/*      */     {
/* 1123 */       clearLeashed(true, !playerIn.capabilities.isCreativeMode);
/* 1124 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1128 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*      */     
/* 1130 */     if ((itemstack != null) && (itemstack.getItem() == Items.lead) && (allowLeashing()))
/*      */     {
/* 1132 */       if ((!(this instanceof EntityTameable)) || (!((EntityTameable)this).isTamed()))
/*      */       {
/* 1134 */         setLeashedToEntity(playerIn, true);
/* 1135 */         itemstack.stackSize -= 1;
/* 1136 */         return true;
/*      */       }
/*      */       
/* 1139 */       if (((EntityTameable)this).isOwner(playerIn))
/*      */       {
/* 1141 */         setLeashedToEntity(playerIn, true);
/* 1142 */         itemstack.stackSize -= 1;
/* 1143 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1147 */     return interact(playerIn) ? true : super.interactFirst(playerIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean interact(EntityPlayer player)
/*      */   {
/* 1156 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateLeashedState()
/*      */   {
/* 1164 */     if (this.leashNBTTag != null)
/*      */     {
/* 1166 */       recreateLeash();
/*      */     }
/*      */     
/* 1169 */     if (this.isLeashed)
/*      */     {
/* 1171 */       if (!isEntityAlive())
/*      */       {
/* 1173 */         clearLeashed(true, true);
/*      */       }
/*      */       
/* 1176 */       if ((this.leashedToEntity == null) || (this.leashedToEntity.isDead))
/*      */       {
/* 1178 */         clearLeashed(true, true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearLeashed(boolean sendPacket, boolean dropLead)
/*      */   {
/* 1188 */     if (this.isLeashed)
/*      */     {
/* 1190 */       this.isLeashed = false;
/* 1191 */       this.leashedToEntity = null;
/*      */       
/* 1193 */       if ((!this.worldObj.isRemote) && (dropLead))
/*      */       {
/* 1195 */         dropItem(Items.lead, 1);
/*      */       }
/*      */       
/* 1198 */       if ((!this.worldObj.isRemote) && (sendPacket) && ((this.worldObj instanceof WorldServer)))
/*      */       {
/* 1200 */         ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, null));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean allowLeashing()
/*      */   {
/* 1207 */     return (!getLeashed()) && (!(this instanceof IMob));
/*      */   }
/*      */   
/*      */   public boolean getLeashed()
/*      */   {
/* 1212 */     return this.isLeashed;
/*      */   }
/*      */   
/*      */   public Entity getLeashedToEntity()
/*      */   {
/* 1217 */     return this.leashedToEntity;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification)
/*      */   {
/* 1225 */     this.isLeashed = true;
/* 1226 */     this.leashedToEntity = entityIn;
/*      */     
/* 1228 */     if ((!this.worldObj.isRemote) && (sendAttachNotification) && ((this.worldObj instanceof WorldServer)))
/*      */     {
/* 1230 */       ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S1BPacketEntityAttach(1, this, this.leashedToEntity));
/*      */     }
/*      */   }
/*      */   
/*      */   private void recreateLeash()
/*      */   {
/* 1236 */     if ((this.isLeashed) && (this.leashNBTTag != null))
/*      */     {
/* 1238 */       if ((this.leashNBTTag.hasKey("UUIDMost", 4)) && (this.leashNBTTag.hasKey("UUIDLeast", 4)))
/*      */       {
/* 1240 */         UUID uuid = new UUID(this.leashNBTTag.getLong("UUIDMost"), this.leashNBTTag.getLong("UUIDLeast"));
/*      */         
/* 1242 */         for (EntityLivingBase entitylivingbase : this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D)))
/*      */         {
/* 1244 */           if (entitylivingbase.getUniqueID().equals(uuid))
/*      */           {
/* 1246 */             this.leashedToEntity = entitylivingbase;
/* 1247 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1251 */       else if ((this.leashNBTTag.hasKey("X", 99)) && (this.leashNBTTag.hasKey("Y", 99)) && (this.leashNBTTag.hasKey("Z", 99)))
/*      */       {
/* 1253 */         BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
/* 1254 */         EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.worldObj, blockpos);
/*      */         
/* 1256 */         if (entityleashknot == null)
/*      */         {
/* 1258 */           entityleashknot = EntityLeashKnot.createKnot(this.worldObj, blockpos);
/*      */         }
/*      */         
/* 1261 */         this.leashedToEntity = entityleashknot;
/*      */       }
/*      */       else
/*      */       {
/* 1265 */         clearLeashed(false, true);
/*      */       }
/*      */     }
/*      */     
/* 1269 */     this.leashNBTTag = null;
/*      */   }
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*      */   {
/*      */     int i;
/*      */     int i;
/* 1276 */     if (inventorySlot == 99)
/*      */     {
/* 1278 */       i = 0;
/*      */     }
/*      */     else
/*      */     {
/* 1282 */       i = inventorySlot - 100 + 1;
/*      */       
/* 1284 */       if ((i < 0) || (i >= this.equipment.length))
/*      */       {
/* 1286 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1290 */     if ((itemStackIn == null) || (getArmorPosition(itemStackIn) == i) || ((i == 4) && ((itemStackIn.getItem() instanceof ItemBlock))))
/*      */     {
/* 1292 */       setCurrentItemOrArmor(i, itemStackIn);
/* 1293 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1297 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isServerWorld()
/*      */   {
/* 1306 */     return (super.isServerWorld()) && (!isAIDisabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNoAI(boolean disable)
/*      */   {
/* 1314 */     this.dataWatcher.updateObject(15, Byte.valueOf((byte)(disable ? 1 : 0)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAIDisabled()
/*      */   {
/* 1322 */     return this.dataWatcher.getWatchableObjectByte(15) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEntityInsideOpaqueBlock()
/*      */   {
/* 1330 */     if (this.noClip)
/*      */     {
/* 1332 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1336 */     BlockPosM blockposm = new BlockPosM(0, 0, 0);
/*      */     
/* 1338 */     for (int i = 0; i < 8; i++)
/*      */     {
/* 1340 */       double d0 = this.posX + ((i >> 0) % 2 - 0.5F) * this.width * 0.8F;
/* 1341 */       double d1 = this.posY + ((i >> 1) % 2 - 0.5F) * 0.1F;
/* 1342 */       double d2 = this.posZ + ((i >> 2) % 2 - 0.5F) * this.width * 0.8F;
/* 1343 */       blockposm.setXyz(d0, d1 + getEyeHeight(), d2);
/*      */       
/* 1345 */       if (this.worldObj.getBlockState(blockposm).getBlock().isVisuallyOpaque())
/*      */       {
/* 1347 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1351 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean canSkipUpdate()
/*      */   {
/* 1357 */     if (isChild())
/*      */     {
/* 1359 */       return false;
/*      */     }
/* 1361 */     if (this.hurtTime > 0)
/*      */     {
/* 1363 */       return false;
/*      */     }
/* 1365 */     if (this.ticksExisted < 20)
/*      */     {
/* 1367 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1371 */     World world = getEntityWorld();
/*      */     
/* 1373 */     if (world == null)
/*      */     {
/* 1375 */       return false;
/*      */     }
/* 1377 */     if (world.playerEntities.size() != 1)
/*      */     {
/* 1379 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1383 */     Entity entity = (Entity)world.playerEntities.get(0);
/* 1384 */     double d0 = Math.abs(this.posX - entity.posX) - 16.0D;
/* 1385 */     double d1 = Math.abs(this.posZ - entity.posZ) - 16.0D;
/* 1386 */     double d2 = d0 * d0 + d1 * d1;
/* 1387 */     return !isInRangeToRenderDist(d2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void onUpdateMinimal()
/*      */   {
/* 1394 */     this.entityAge += 1;
/*      */     
/* 1396 */     if ((this instanceof EntityMob))
/*      */     {
/* 1398 */       float f = getBrightness(1.0F);
/*      */       
/* 1400 */       if (f > 0.5F)
/*      */       {
/* 1402 */         this.entityAge += 2;
/*      */       }
/*      */     }
/*      */     
/* 1406 */     despawnEntity();
/*      */   }
/*      */   
/*      */   public static enum SpawnPlacementType
/*      */   {
/* 1411 */     ON_GROUND("ON_GROUND", 0), 
/* 1412 */     IN_AIR("IN_AIR", 1), 
/* 1413 */     IN_WATER("IN_WATER", 2);
/*      */     
/* 1415 */     private static final SpawnPlacementType[] $VALUES = { ON_GROUND, IN_AIR, IN_WATER };
/*      */     private static final String __OBFID = "CL_00002255";
/*      */     
/*      */     private SpawnPlacementType(String p_i14_3_, int p_i14_4_) {}
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */