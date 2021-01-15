/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.Rotations;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class EntityArmorStand extends EntityLivingBase
/*     */ {
/*  28 */   private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
/*  29 */   private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
/*  30 */   private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
/*  31 */   private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
/*  32 */   private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
/*  33 */   private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
/*     */   
/*     */   private final ItemStack[] contents;
/*     */   
/*     */   private boolean canInteract;
/*     */   
/*     */   private long punchCooldown;
/*     */   
/*     */   private int disabledSlots;
/*     */   private boolean field_181028_bj;
/*     */   private Rotations headRotation;
/*     */   private Rotations bodyRotation;
/*     */   private Rotations leftArmRotation;
/*     */   private Rotations rightArmRotation;
/*     */   private Rotations leftLegRotation;
/*     */   private Rotations rightLegRotation;
/*     */   
/*     */   public EntityArmorStand(World worldIn)
/*     */   {
/*  52 */     super(worldIn);
/*  53 */     this.contents = new ItemStack[5];
/*  54 */     this.headRotation = DEFAULT_HEAD_ROTATION;
/*  55 */     this.bodyRotation = DEFAULT_BODY_ROTATION;
/*  56 */     this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
/*  57 */     this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
/*  58 */     this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
/*  59 */     this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
/*  60 */     setSilent(true);
/*  61 */     this.noClip = hasNoGravity();
/*  62 */     setSize(0.5F, 1.975F);
/*     */   }
/*     */   
/*     */   public EntityArmorStand(World worldIn, double posX, double posY, double posZ)
/*     */   {
/*  67 */     this(worldIn);
/*  68 */     setPosition(posX, posY, posZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isServerWorld()
/*     */   {
/*  76 */     return (super.isServerWorld()) && (!hasNoGravity());
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  81 */     super.entityInit();
/*  82 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*  83 */     this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
/*  84 */     this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
/*  85 */     this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
/*  86 */     this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
/*  87 */     this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
/*  88 */     this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getHeldItem()
/*     */   {
/*  96 */     return this.contents[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getEquipmentInSlot(int slotIn)
/*     */   {
/* 104 */     return this.contents[slotIn];
/*     */   }
/*     */   
/*     */   public ItemStack getCurrentArmor(int slotIn)
/*     */   {
/* 109 */     return this.contents[(slotIn + 1)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
/*     */   {
/* 117 */     this.contents[slotIn] = stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack[] getInventory()
/*     */   {
/* 125 */     return this.contents;
/*     */   }
/*     */   
/*     */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
/*     */   {
/*     */     int i;
/*     */     int i;
/* 132 */     if (inventorySlot == 99)
/*     */     {
/* 134 */       i = 0;
/*     */     }
/*     */     else
/*     */     {
/* 138 */       i = inventorySlot - 100 + 1;
/*     */       
/* 140 */       if ((i < 0) || (i >= this.contents.length))
/*     */       {
/* 142 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 146 */     if ((itemStackIn != null) && (net.minecraft.entity.EntityLiving.getArmorPosition(itemStackIn) != i) && ((i != 4) || (!(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock))))
/*     */     {
/* 148 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 152 */     setCurrentItemOrArmor(i, itemStackIn);
/* 153 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 162 */     super.writeEntityToNBT(tagCompound);
/* 163 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 165 */     for (int i = 0; i < this.contents.length; i++)
/*     */     {
/* 167 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */       
/* 169 */       if (this.contents[i] != null)
/*     */       {
/* 171 */         this.contents[i].writeToNBT(nbttagcompound);
/*     */       }
/*     */       
/* 174 */       nbttaglist.appendTag(nbttagcompound);
/*     */     }
/*     */     
/* 177 */     tagCompound.setTag("Equipment", nbttaglist);
/*     */     
/* 179 */     if ((getAlwaysRenderNameTag()) && ((getCustomNameTag() == null) || (getCustomNameTag().length() == 0)))
/*     */     {
/* 181 */       tagCompound.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
/*     */     }
/*     */     
/* 184 */     tagCompound.setBoolean("Invisible", isInvisible());
/* 185 */     tagCompound.setBoolean("Small", isSmall());
/* 186 */     tagCompound.setBoolean("ShowArms", getShowArms());
/* 187 */     tagCompound.setInteger("DisabledSlots", this.disabledSlots);
/* 188 */     tagCompound.setBoolean("NoGravity", hasNoGravity());
/* 189 */     tagCompound.setBoolean("NoBasePlate", hasNoBasePlate());
/*     */     
/* 191 */     if (func_181026_s())
/*     */     {
/* 193 */       tagCompound.setBoolean("Marker", func_181026_s());
/*     */     }
/*     */     
/* 196 */     tagCompound.setTag("Pose", readPoseFromNBT());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 204 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 206 */     if (tagCompund.hasKey("Equipment", 9))
/*     */     {
/* 208 */       NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
/*     */       
/* 210 */       for (int i = 0; i < this.contents.length; i++)
/*     */       {
/* 212 */         this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*     */       }
/*     */     }
/*     */     
/* 216 */     setInvisible(tagCompund.getBoolean("Invisible"));
/* 217 */     setSmall(tagCompund.getBoolean("Small"));
/* 218 */     setShowArms(tagCompund.getBoolean("ShowArms"));
/* 219 */     this.disabledSlots = tagCompund.getInteger("DisabledSlots");
/* 220 */     setNoGravity(tagCompund.getBoolean("NoGravity"));
/* 221 */     setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
/* 222 */     func_181027_m(tagCompund.getBoolean("Marker"));
/* 223 */     this.field_181028_bj = (!func_181026_s());
/* 224 */     this.noClip = hasNoGravity();
/* 225 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Pose");
/* 226 */     writePoseToNBT(nbttagcompound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writePoseToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 234 */     NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
/*     */     
/* 236 */     if (nbttaglist.tagCount() > 0)
/*     */     {
/* 238 */       setHeadRotation(new Rotations(nbttaglist));
/*     */     }
/*     */     else
/*     */     {
/* 242 */       setHeadRotation(DEFAULT_HEAD_ROTATION);
/*     */     }
/*     */     
/* 245 */     NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
/*     */     
/* 247 */     if (nbttaglist1.tagCount() > 0)
/*     */     {
/* 249 */       setBodyRotation(new Rotations(nbttaglist1));
/*     */     }
/*     */     else
/*     */     {
/* 253 */       setBodyRotation(DEFAULT_BODY_ROTATION);
/*     */     }
/*     */     
/* 256 */     NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
/*     */     
/* 258 */     if (nbttaglist2.tagCount() > 0)
/*     */     {
/* 260 */       setLeftArmRotation(new Rotations(nbttaglist2));
/*     */     }
/*     */     else
/*     */     {
/* 264 */       setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
/*     */     }
/*     */     
/* 267 */     NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
/*     */     
/* 269 */     if (nbttaglist3.tagCount() > 0)
/*     */     {
/* 271 */       setRightArmRotation(new Rotations(nbttaglist3));
/*     */     }
/*     */     else
/*     */     {
/* 275 */       setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
/*     */     }
/*     */     
/* 278 */     NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
/*     */     
/* 280 */     if (nbttaglist4.tagCount() > 0)
/*     */     {
/* 282 */       setLeftLegRotation(new Rotations(nbttaglist4));
/*     */     }
/*     */     else
/*     */     {
/* 286 */       setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
/*     */     }
/*     */     
/* 289 */     NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
/*     */     
/* 291 */     if (nbttaglist5.tagCount() > 0)
/*     */     {
/* 293 */       setRightLegRotation(new Rotations(nbttaglist5));
/*     */     }
/*     */     else
/*     */     {
/* 297 */       setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
/*     */     }
/*     */   }
/*     */   
/*     */   private NBTTagCompound readPoseFromNBT()
/*     */   {
/* 303 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/* 305 */     if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
/*     */     {
/* 307 */       nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
/*     */     }
/*     */     
/* 310 */     if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
/*     */     {
/* 312 */       nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
/*     */     }
/*     */     
/* 315 */     if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
/*     */     {
/* 317 */       nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
/*     */     }
/*     */     
/* 320 */     if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
/*     */     {
/* 322 */       nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
/*     */     }
/*     */     
/* 325 */     if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
/*     */     {
/* 327 */       nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
/*     */     }
/*     */     
/* 330 */     if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
/*     */     {
/* 332 */       nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
/*     */     }
/*     */     
/* 335 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBePushed()
/*     */   {
/* 343 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void collideWithEntity(Entity p_82167_1_) {}
/*     */   
/*     */ 
/*     */   protected void collideWithNearbyEntities()
/*     */   {
/* 352 */     List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
/*     */     
/* 354 */     if ((list != null) && (!list.isEmpty()))
/*     */     {
/* 356 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/* 358 */         Entity entity = (Entity)list.get(i);
/*     */         
/* 360 */         if (((entity instanceof EntityMinecart)) && (((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE) && (getDistanceSqToEntity(entity) <= 0.2D))
/*     */         {
/* 362 */           entity.applyEntityCollision(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactAt(EntityPlayer player, Vec3 targetVec3)
/*     */   {
/* 373 */     if (func_181026_s())
/*     */     {
/* 375 */       return false;
/*     */     }
/* 377 */     if ((!this.worldObj.isRemote) && (!player.isSpectator()))
/*     */     {
/* 379 */       int i = 0;
/* 380 */       ItemStack itemstack = player.getCurrentEquippedItem();
/* 381 */       boolean flag = itemstack != null;
/*     */       
/* 383 */       if ((flag) && ((itemstack.getItem() instanceof ItemArmor)))
/*     */       {
/* 385 */         ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
/*     */         
/* 387 */         if (itemarmor.armorType == 3)
/*     */         {
/* 389 */           i = 1;
/*     */         }
/* 391 */         else if (itemarmor.armorType == 2)
/*     */         {
/* 393 */           i = 2;
/*     */         }
/* 395 */         else if (itemarmor.armorType == 1)
/*     */         {
/* 397 */           i = 3;
/*     */         }
/* 399 */         else if (itemarmor.armorType == 0)
/*     */         {
/* 401 */           i = 4;
/*     */         }
/*     */       }
/*     */       
/* 405 */       if ((flag) && ((itemstack.getItem() == Items.skull) || (itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))))
/*     */       {
/* 407 */         i = 4;
/*     */       }
/*     */       
/* 410 */       double d4 = 0.1D;
/* 411 */       double d0 = 0.9D;
/* 412 */       double d1 = 0.4D;
/* 413 */       double d2 = 1.6D;
/* 414 */       int j = 0;
/* 415 */       boolean flag1 = isSmall();
/* 416 */       double d3 = flag1 ? targetVec3.yCoord * 2.0D : targetVec3.yCoord;
/*     */       
/* 418 */       if (d3 >= 0.1D) if ((d3 < 0.1D + (flag1 ? 0.8D : 0.45D)) && (this.contents[1] != null))
/*     */         {
/* 420 */           j = 1;
/*     */           break label381; }
/* 422 */       if (d3 >= 0.9D + (flag1 ? 0.3D : 0.0D)) if ((d3 < 0.9D + (flag1 ? 1.0D : 0.7D)) && (this.contents[3] != null))
/*     */         {
/* 424 */           j = 3;
/*     */           break label381; }
/* 426 */       if (d3 >= 0.4D) if ((d3 < 0.4D + (flag1 ? 1.0D : 0.8D)) && (this.contents[2] != null))
/*     */         {
/* 428 */           j = 2;
/*     */           break label381; }
/* 430 */       if ((d3 >= 1.6D) && (this.contents[4] != null))
/*     */       {
/* 432 */         j = 4;
/*     */       }
/*     */       label381:
/* 435 */       boolean flag2 = this.contents[j] != null;
/*     */       
/* 437 */       if (((this.disabledSlots & 1 << j) != 0) || ((this.disabledSlots & 1 << i) != 0))
/*     */       {
/* 439 */         j = i;
/*     */         
/* 441 */         if ((this.disabledSlots & 1 << i) != 0)
/*     */         {
/* 443 */           if ((this.disabledSlots & 0x1) != 0)
/*     */           {
/* 445 */             return true;
/*     */           }
/*     */           
/* 448 */           j = 0;
/*     */         }
/*     */       }
/*     */       
/* 452 */       if ((flag) && (i == 0) && (!getShowArms()))
/*     */       {
/* 454 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 458 */       if (flag)
/*     */       {
/* 460 */         func_175422_a(player, i);
/*     */       }
/* 462 */       else if (flag2)
/*     */       {
/* 464 */         func_175422_a(player, j);
/*     */       }
/*     */       
/* 467 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 472 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_)
/*     */   {
/* 478 */     ItemStack itemstack = this.contents[p_175422_2_];
/*     */     
/* 480 */     if ((itemstack == null) || ((this.disabledSlots & 1 << p_175422_2_ + 8) == 0))
/*     */     {
/* 482 */       if ((itemstack != null) || ((this.disabledSlots & 1 << p_175422_2_ + 16) == 0))
/*     */       {
/* 484 */         int i = p_175422_1_.inventory.currentItem;
/* 485 */         ItemStack itemstack1 = p_175422_1_.inventory.getStackInSlot(i);
/*     */         
/* 487 */         if ((p_175422_1_.capabilities.isCreativeMode) && ((itemstack == null) || (itemstack.getItem() == Item.getItemFromBlock(Blocks.air))) && (itemstack1 != null))
/*     */         {
/* 489 */           ItemStack itemstack3 = itemstack1.copy();
/* 490 */           itemstack3.stackSize = 1;
/* 491 */           setCurrentItemOrArmor(p_175422_2_, itemstack3);
/*     */         }
/* 493 */         else if ((itemstack1 != null) && (itemstack1.stackSize > 1))
/*     */         {
/* 495 */           if (itemstack == null)
/*     */           {
/* 497 */             ItemStack itemstack2 = itemstack1.copy();
/* 498 */             itemstack2.stackSize = 1;
/* 499 */             setCurrentItemOrArmor(p_175422_2_, itemstack2);
/* 500 */             itemstack1.stackSize -= 1;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 505 */           setCurrentItemOrArmor(p_175422_2_, itemstack1);
/* 506 */           p_175422_1_.inventory.setInventorySlotContents(i, itemstack);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 517 */     if (this.worldObj.isRemote)
/*     */     {
/* 519 */       return false;
/*     */     }
/* 521 */     if (DamageSource.outOfWorld.equals(source))
/*     */     {
/* 523 */       setDead();
/* 524 */       return false;
/*     */     }
/* 526 */     if ((!isEntityInvulnerable(source)) && (!this.canInteract) && (!func_181026_s()))
/*     */     {
/* 528 */       if (source.isExplosion())
/*     */       {
/* 530 */         dropContents();
/* 531 */         setDead();
/* 532 */         return false;
/*     */       }
/* 534 */       if (DamageSource.inFire.equals(source))
/*     */       {
/* 536 */         if (!isBurning())
/*     */         {
/* 538 */           setFire(5);
/*     */         }
/*     */         else
/*     */         {
/* 542 */           damageArmorStand(0.15F);
/*     */         }
/*     */         
/* 545 */         return false;
/*     */       }
/* 547 */       if ((DamageSource.onFire.equals(source)) && (getHealth() > 0.5F))
/*     */       {
/* 549 */         damageArmorStand(4.0F);
/* 550 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 554 */       boolean flag = "arrow".equals(source.getDamageType());
/* 555 */       boolean flag1 = "player".equals(source.getDamageType());
/*     */       
/* 557 */       if ((!flag1) && (!flag))
/*     */       {
/* 559 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 563 */       if ((source.getSourceOfDamage() instanceof EntityArrow))
/*     */       {
/* 565 */         source.getSourceOfDamage().setDead();
/*     */       }
/*     */       
/* 568 */       if (((source.getEntity() instanceof EntityPlayer)) && (!((EntityPlayer)source.getEntity()).capabilities.allowEdit))
/*     */       {
/* 570 */         return false;
/*     */       }
/* 572 */       if (source.isCreativePlayer())
/*     */       {
/* 574 */         playParticles();
/* 575 */         setDead();
/* 576 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 580 */       long i = this.worldObj.getTotalWorldTime();
/*     */       
/* 582 */       if ((i - this.punchCooldown > 5L) && (!flag))
/*     */       {
/* 584 */         this.punchCooldown = i;
/*     */       }
/*     */       else
/*     */       {
/* 588 */         dropBlock();
/* 589 */         playParticles();
/* 590 */         setDead();
/*     */       }
/*     */       
/* 593 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 600 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInRangeToRenderDist(double distance)
/*     */   {
/* 610 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/* 612 */     if ((Double.isNaN(d0)) || (d0 == 0.0D))
/*     */     {
/* 614 */       d0 = 4.0D;
/*     */     }
/*     */     
/* 617 */     d0 *= 64.0D;
/* 618 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */   private void playParticles()
/*     */   {
/* 623 */     if ((this.worldObj instanceof WorldServer))
/*     */     {
/* 625 */       ((WorldServer)this.worldObj).spawnParticle(net.minecraft.util.EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + this.height / 1.5D, this.posZ, 10, this.width / 4.0F, this.height / 4.0F, this.width / 4.0F, 0.05D, new int[] { Block.getStateId(Blocks.planks.getDefaultState()) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void damageArmorStand(float p_175406_1_)
/*     */   {
/* 631 */     float f = getHealth();
/* 632 */     f -= p_175406_1_;
/*     */     
/* 634 */     if (f <= 0.5F)
/*     */     {
/* 636 */       dropContents();
/* 637 */       setDead();
/*     */     }
/*     */     else
/*     */     {
/* 641 */       setHealth(f);
/*     */     }
/*     */   }
/*     */   
/*     */   private void dropBlock()
/*     */   {
/* 647 */     Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
/* 648 */     dropContents();
/*     */   }
/*     */   
/*     */   private void dropContents()
/*     */   {
/* 653 */     for (int i = 0; i < this.contents.length; i++)
/*     */     {
/* 655 */       if ((this.contents[i] != null) && (this.contents[i].stackSize > 0))
/*     */       {
/* 657 */         if (this.contents[i] != null)
/*     */         {
/* 659 */           Block.spawnAsEntity(this.worldObj, new BlockPos(this).up(), this.contents[i]);
/*     */         }
/*     */         
/* 662 */         this.contents[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected float func_110146_f(float p_110146_1_, float p_110146_2_)
/*     */   {
/* 669 */     this.prevRenderYawOffset = this.prevRotationYaw;
/* 670 */     this.renderYawOffset = this.rotationYaw;
/* 671 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 676 */     return isChild() ? this.height * 0.5F : this.height * 0.9F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveEntityWithHeading(float strafe, float forward)
/*     */   {
/* 684 */     if (!hasNoGravity())
/*     */     {
/* 686 */       super.moveEntityWithHeading(strafe, forward);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 695 */     super.onUpdate();
/* 696 */     Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
/*     */     
/* 698 */     if (!this.headRotation.equals(rotations))
/*     */     {
/* 700 */       setHeadRotation(rotations);
/*     */     }
/*     */     
/* 703 */     Rotations rotations1 = this.dataWatcher.getWatchableObjectRotations(12);
/*     */     
/* 705 */     if (!this.bodyRotation.equals(rotations1))
/*     */     {
/* 707 */       setBodyRotation(rotations1);
/*     */     }
/*     */     
/* 710 */     Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(13);
/*     */     
/* 712 */     if (!this.leftArmRotation.equals(rotations2))
/*     */     {
/* 714 */       setLeftArmRotation(rotations2);
/*     */     }
/*     */     
/* 717 */     Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(14);
/*     */     
/* 719 */     if (!this.rightArmRotation.equals(rotations3))
/*     */     {
/* 721 */       setRightArmRotation(rotations3);
/*     */     }
/*     */     
/* 724 */     Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(15);
/*     */     
/* 726 */     if (!this.leftLegRotation.equals(rotations4))
/*     */     {
/* 728 */       setLeftLegRotation(rotations4);
/*     */     }
/*     */     
/* 731 */     Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(16);
/*     */     
/* 733 */     if (!this.rightLegRotation.equals(rotations5))
/*     */     {
/* 735 */       setRightLegRotation(rotations5);
/*     */     }
/*     */     
/* 738 */     boolean flag = func_181026_s();
/*     */     
/* 740 */     if ((!this.field_181028_bj) && (flag))
/*     */     {
/* 742 */       func_181550_a(false);
/*     */     }
/*     */     else
/*     */     {
/* 746 */       if ((!this.field_181028_bj) || (flag))
/*     */       {
/* 748 */         return;
/*     */       }
/*     */       
/* 751 */       func_181550_a(true);
/*     */     }
/*     */     
/* 754 */     this.field_181028_bj = flag;
/*     */   }
/*     */   
/*     */   private void func_181550_a(boolean p_181550_1_)
/*     */   {
/* 759 */     double d0 = this.posX;
/* 760 */     double d1 = this.posY;
/* 761 */     double d2 = this.posZ;
/*     */     
/* 763 */     if (p_181550_1_)
/*     */     {
/* 765 */       setSize(0.5F, 1.975F);
/*     */     }
/*     */     else
/*     */     {
/* 769 */       setSize(0.0F, 0.0F);
/*     */     }
/*     */     
/* 772 */     setPosition(d0, d1, d2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updatePotionMetadata()
/*     */   {
/* 781 */     setInvisible(this.canInteract);
/*     */   }
/*     */   
/*     */   public void setInvisible(boolean invisible)
/*     */   {
/* 786 */     this.canInteract = invisible;
/* 787 */     super.setInvisible(invisible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChild()
/*     */   {
/* 795 */     return isSmall();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onKillCommand()
/*     */   {
/* 803 */     setDead();
/*     */   }
/*     */   
/*     */   public boolean isImmuneToExplosions()
/*     */   {
/* 808 */     return isInvisible();
/*     */   }
/*     */   
/*     */   private void setSmall(boolean p_175420_1_)
/*     */   {
/* 813 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 815 */     if (p_175420_1_)
/*     */     {
/* 817 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else
/*     */     {
/* 821 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     }
/*     */     
/* 824 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */   public boolean isSmall()
/*     */   {
/* 829 */     return (this.dataWatcher.getWatchableObjectByte(10) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   private void setNoGravity(boolean p_175425_1_)
/*     */   {
/* 834 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 836 */     if (p_175425_1_)
/*     */     {
/* 838 */       b0 = (byte)(b0 | 0x2);
/*     */     }
/*     */     else
/*     */     {
/* 842 */       b0 = (byte)(b0 & 0xFFFFFFFD);
/*     */     }
/*     */     
/* 845 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */   public boolean hasNoGravity()
/*     */   {
/* 850 */     return (this.dataWatcher.getWatchableObjectByte(10) & 0x2) != 0;
/*     */   }
/*     */   
/*     */   private void setShowArms(boolean p_175413_1_)
/*     */   {
/* 855 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 857 */     if (p_175413_1_)
/*     */     {
/* 859 */       b0 = (byte)(b0 | 0x4);
/*     */     }
/*     */     else
/*     */     {
/* 863 */       b0 = (byte)(b0 & 0xFFFFFFFB);
/*     */     }
/*     */     
/* 866 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */   public boolean getShowArms()
/*     */   {
/* 871 */     return (this.dataWatcher.getWatchableObjectByte(10) & 0x4) != 0;
/*     */   }
/*     */   
/*     */   private void setNoBasePlate(boolean p_175426_1_)
/*     */   {
/* 876 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 878 */     if (p_175426_1_)
/*     */     {
/* 880 */       b0 = (byte)(b0 | 0x8);
/*     */     }
/*     */     else
/*     */     {
/* 884 */       b0 = (byte)(b0 & 0xFFFFFFF7);
/*     */     }
/*     */     
/* 887 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */   public boolean hasNoBasePlate()
/*     */   {
/* 892 */     return (this.dataWatcher.getWatchableObjectByte(10) & 0x8) != 0;
/*     */   }
/*     */   
/*     */   private void func_181027_m(boolean p_181027_1_)
/*     */   {
/* 897 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 899 */     if (p_181027_1_)
/*     */     {
/* 901 */       b0 = (byte)(b0 | 0x10);
/*     */     }
/*     */     else
/*     */     {
/* 905 */       b0 = (byte)(b0 & 0xFFFFFFEF);
/*     */     }
/*     */     
/* 908 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */   
/*     */   public boolean func_181026_s()
/*     */   {
/* 913 */     return (this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0;
/*     */   }
/*     */   
/*     */   public void setHeadRotation(Rotations p_175415_1_)
/*     */   {
/* 918 */     this.headRotation = p_175415_1_;
/* 919 */     this.dataWatcher.updateObject(11, p_175415_1_);
/*     */   }
/*     */   
/*     */   public void setBodyRotation(Rotations p_175424_1_)
/*     */   {
/* 924 */     this.bodyRotation = p_175424_1_;
/* 925 */     this.dataWatcher.updateObject(12, p_175424_1_);
/*     */   }
/*     */   
/*     */   public void setLeftArmRotation(Rotations p_175405_1_)
/*     */   {
/* 930 */     this.leftArmRotation = p_175405_1_;
/* 931 */     this.dataWatcher.updateObject(13, p_175405_1_);
/*     */   }
/*     */   
/*     */   public void setRightArmRotation(Rotations p_175428_1_)
/*     */   {
/* 936 */     this.rightArmRotation = p_175428_1_;
/* 937 */     this.dataWatcher.updateObject(14, p_175428_1_);
/*     */   }
/*     */   
/*     */   public void setLeftLegRotation(Rotations p_175417_1_)
/*     */   {
/* 942 */     this.leftLegRotation = p_175417_1_;
/* 943 */     this.dataWatcher.updateObject(15, p_175417_1_);
/*     */   }
/*     */   
/*     */   public void setRightLegRotation(Rotations p_175427_1_)
/*     */   {
/* 948 */     this.rightLegRotation = p_175427_1_;
/* 949 */     this.dataWatcher.updateObject(16, p_175427_1_);
/*     */   }
/*     */   
/*     */   public Rotations getHeadRotation()
/*     */   {
/* 954 */     return this.headRotation;
/*     */   }
/*     */   
/*     */   public Rotations getBodyRotation()
/*     */   {
/* 959 */     return this.bodyRotation;
/*     */   }
/*     */   
/*     */   public Rotations getLeftArmRotation()
/*     */   {
/* 964 */     return this.leftArmRotation;
/*     */   }
/*     */   
/*     */   public Rotations getRightArmRotation()
/*     */   {
/* 969 */     return this.rightArmRotation;
/*     */   }
/*     */   
/*     */   public Rotations getLeftLegRotation()
/*     */   {
/* 974 */     return this.leftLegRotation;
/*     */   }
/*     */   
/*     */   public Rotations getRightLegRotation()
/*     */   {
/* 979 */     return this.rightLegRotation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 987 */     return (super.canBeCollidedWith()) && (!func_181026_s());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */