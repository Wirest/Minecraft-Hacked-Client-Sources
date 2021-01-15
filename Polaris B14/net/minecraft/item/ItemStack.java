/*      */ package net.minecraft.item;
/*      */ 
/*      */ import com.google.common.collect.Multimap;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.List;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.enchantment.EnchantmentDurability;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.item.EntityItemFrame;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.event.HoverEvent.Action;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.RegistryNamespaced;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public final class ItemStack
/*      */ {
/*   38 */   public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");
/*      */   
/*      */ 
/*      */   public int stackSize;
/*      */   
/*      */   public int animationsToGo;
/*      */   
/*      */   private Item item;
/*      */   
/*      */   private NBTTagCompound stackTagCompound;
/*      */   
/*      */   private int itemDamage;
/*      */   
/*      */   private EntityItemFrame itemFrame;
/*      */   
/*      */   private Block canDestroyCacheBlock;
/*      */   
/*      */   private boolean canDestroyCacheResult;
/*      */   
/*      */   private Block canPlaceOnCacheBlock;
/*      */   
/*      */   private boolean canPlaceOnCacheResult;
/*      */   
/*      */ 
/*      */   public ItemStack(Block blockIn)
/*      */   {
/*   64 */     this(blockIn, 1);
/*      */   }
/*      */   
/*      */   public ItemStack(Block blockIn, int amount)
/*      */   {
/*   69 */     this(blockIn, amount, 0);
/*      */   }
/*      */   
/*      */   public ItemStack(Block blockIn, int amount, int meta)
/*      */   {
/*   74 */     this(Item.getItemFromBlock(blockIn), amount, meta);
/*      */   }
/*      */   
/*      */   public ItemStack(Item itemIn)
/*      */   {
/*   79 */     this(itemIn, 1);
/*      */   }
/*      */   
/*      */   public ItemStack(Item itemIn, int amount)
/*      */   {
/*   84 */     this(itemIn, amount, 0);
/*      */   }
/*      */   
/*      */   public ItemStack(Item itemIn, int amount, int meta)
/*      */   {
/*   89 */     this.canDestroyCacheBlock = null;
/*   90 */     this.canDestroyCacheResult = false;
/*   91 */     this.canPlaceOnCacheBlock = null;
/*   92 */     this.canPlaceOnCacheResult = false;
/*   93 */     this.item = itemIn;
/*   94 */     this.stackSize = amount;
/*   95 */     this.itemDamage = meta;
/*      */     
/*   97 */     if (this.itemDamage < 0)
/*      */     {
/*   99 */       this.itemDamage = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt)
/*      */   {
/*  105 */     ItemStack itemstack = new ItemStack();
/*  106 */     itemstack.readFromNBT(nbt);
/*  107 */     return itemstack.getItem() != null ? itemstack : null;
/*      */   }
/*      */   
/*      */   public ItemStack()
/*      */   {
/*  112 */     this.canDestroyCacheBlock = null;
/*  113 */     this.canDestroyCacheResult = false;
/*  114 */     this.canPlaceOnCacheBlock = null;
/*  115 */     this.canPlaceOnCacheResult = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack splitStack(int amount)
/*      */   {
/*  123 */     ItemStack itemstack = new ItemStack(this.item, amount, this.itemDamage);
/*      */     
/*  125 */     if (this.stackTagCompound != null)
/*      */     {
/*  127 */       itemstack.stackTagCompound = ((NBTTagCompound)this.stackTagCompound.copy());
/*      */     }
/*      */     
/*  130 */     this.stackSize -= amount;
/*  131 */     return itemstack;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Item getItem()
/*      */   {
/*  139 */     return this.item;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*      */   {
/*  148 */     boolean flag = getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
/*      */     
/*  150 */     if (flag)
/*      */     {
/*  152 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*      */     }
/*      */     
/*  155 */     return flag;
/*      */   }
/*      */   
/*      */   public float getStrVsBlock(Block blockIn)
/*      */   {
/*  160 */     return getItem().getStrVsBlock(this, blockIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn)
/*      */   {
/*  169 */     return getItem().onItemRightClick(this, worldIn, playerIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn)
/*      */   {
/*  177 */     return getItem().onItemUseFinish(this, worldIn, playerIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NBTTagCompound writeToNBT(NBTTagCompound nbt)
/*      */   {
/*  185 */     ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
/*  186 */     nbt.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
/*  187 */     nbt.setByte("Count", (byte)this.stackSize);
/*  188 */     nbt.setShort("Damage", (short)this.itemDamage);
/*      */     
/*  190 */     if (this.stackTagCompound != null)
/*      */     {
/*  192 */       nbt.setTag("tag", this.stackTagCompound);
/*      */     }
/*      */     
/*  195 */     return nbt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readFromNBT(NBTTagCompound nbt)
/*      */   {
/*  203 */     if (nbt.hasKey("id", 8))
/*      */     {
/*  205 */       this.item = Item.getByNameOrId(nbt.getString("id"));
/*      */     }
/*      */     else
/*      */     {
/*  209 */       this.item = Item.getItemById(nbt.getShort("id"));
/*      */     }
/*      */     
/*  212 */     this.stackSize = nbt.getByte("Count");
/*  213 */     this.itemDamage = nbt.getShort("Damage");
/*      */     
/*  215 */     if (this.itemDamage < 0)
/*      */     {
/*  217 */       this.itemDamage = 0;
/*      */     }
/*      */     
/*  220 */     if (nbt.hasKey("tag", 10))
/*      */     {
/*  222 */       this.stackTagCompound = nbt.getCompoundTag("tag");
/*      */       
/*  224 */       if (this.item != null)
/*      */       {
/*  226 */         this.item.updateItemStackNBT(this.stackTagCompound);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStackSize()
/*      */   {
/*  236 */     return getItem().getItemStackLimit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isStackable()
/*      */   {
/*  244 */     return (getMaxStackSize() > 1) && ((!isItemStackDamageable()) || (!isItemDamaged()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemStackDamageable()
/*      */   {
/*  252 */     return this.item != null;
/*      */   }
/*      */   
/*      */   public boolean getHasSubtypes()
/*      */   {
/*  257 */     return this.item.getHasSubtypes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemDamaged()
/*      */   {
/*  265 */     return (isItemStackDamageable()) && (this.itemDamage > 0);
/*      */   }
/*      */   
/*      */   public int getItemDamage()
/*      */   {
/*  270 */     return this.itemDamage;
/*      */   }
/*      */   
/*      */   public int getMetadata()
/*      */   {
/*  275 */     return this.itemDamage;
/*      */   }
/*      */   
/*      */   public void setItemDamage(int meta)
/*      */   {
/*  280 */     this.itemDamage = meta;
/*      */     
/*  282 */     if (this.itemDamage < 0)
/*      */     {
/*  284 */       this.itemDamage = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxDamage()
/*      */   {
/*  293 */     return this.item.getMaxDamage();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attemptDamageItem(int amount, Random rand)
/*      */   {
/*  304 */     if (!isItemStackDamageable())
/*      */     {
/*  306 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  310 */     if (amount > 0)
/*      */     {
/*  312 */       int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
/*  313 */       int j = 0;
/*      */       
/*  315 */       for (int k = 0; (i > 0) && (k < amount); k++)
/*      */       {
/*  317 */         if (EnchantmentDurability.negateDamage(this, i, rand))
/*      */         {
/*  319 */           j++;
/*      */         }
/*      */       }
/*      */       
/*  323 */       amount -= j;
/*      */       
/*  325 */       if (amount <= 0)
/*      */       {
/*  327 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  331 */     this.itemDamage += amount;
/*  332 */     return this.itemDamage > getMaxDamage();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void damageItem(int amount, EntityLivingBase entityIn)
/*      */   {
/*  341 */     if ((!(entityIn instanceof EntityPlayer)) || (!((EntityPlayer)entityIn).capabilities.isCreativeMode))
/*      */     {
/*  343 */       if (isItemStackDamageable())
/*      */       {
/*  345 */         if (attemptDamageItem(amount, entityIn.getRNG()))
/*      */         {
/*  347 */           entityIn.renderBrokenItemStack(this);
/*  348 */           this.stackSize -= 1;
/*      */           
/*  350 */           if ((entityIn instanceof EntityPlayer))
/*      */           {
/*  352 */             EntityPlayer entityplayer = (EntityPlayer)entityIn;
/*  353 */             entityplayer.triggerAchievement(net.minecraft.stats.StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
/*      */             
/*  355 */             if ((this.stackSize == 0) && ((getItem() instanceof ItemBow)))
/*      */             {
/*  357 */               entityplayer.destroyCurrentEquippedItem();
/*      */             }
/*      */           }
/*      */           
/*  361 */           if (this.stackSize < 0)
/*      */           {
/*  363 */             this.stackSize = 0;
/*      */           }
/*      */           
/*  366 */           this.itemDamage = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn)
/*      */   {
/*  377 */     boolean flag = this.item.hitEntity(this, entityIn, playerIn);
/*      */     
/*  379 */     if (flag)
/*      */     {
/*  381 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn)
/*      */   {
/*  390 */     boolean flag = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
/*      */     
/*  392 */     if (flag)
/*      */     {
/*  394 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this.item)]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canHarvestBlock(Block blockIn)
/*      */   {
/*  403 */     return this.item.canHarvestBlock(blockIn);
/*      */   }
/*      */   
/*      */   public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn)
/*      */   {
/*  408 */     return this.item.itemInteractionForEntity(this, playerIn, entityIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack copy()
/*      */   {
/*  416 */     ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
/*      */     
/*  418 */     if (this.stackTagCompound != null)
/*      */     {
/*  420 */       itemstack.stackTagCompound = ((NBTTagCompound)this.stackTagCompound.copy());
/*      */     }
/*      */     
/*  423 */     return itemstack;
/*      */   }
/*      */   
/*      */   public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB)
/*      */   {
/*  428 */     return (stackA == null) && (stackB == null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
/*      */   {
/*  436 */     return (stackA == null) && (stackB == null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isItemStackEqual(ItemStack other)
/*      */   {
/*  444 */     return this.stackSize == other.stackSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB)
/*      */   {
/*  452 */     return (stackA == null) && (stackB == null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemEqual(ItemStack other)
/*      */   {
/*  461 */     return (other != null) && (this.item == other.item) && (this.itemDamage == other.itemDamage);
/*      */   }
/*      */   
/*      */   public String getUnlocalizedName()
/*      */   {
/*  466 */     return this.item.getUnlocalizedName(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ItemStack copyItemStack(ItemStack stack)
/*      */   {
/*  474 */     return stack == null ? null : stack.copy();
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/*  479 */     return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem)
/*      */   {
/*  488 */     if (this.animationsToGo > 0)
/*      */     {
/*  490 */       this.animationsToGo -= 1;
/*      */     }
/*      */     
/*  493 */     this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
/*      */   }
/*      */   
/*      */   public void onCrafting(World worldIn, EntityPlayer playerIn, int amount)
/*      */   {
/*  498 */     playerIn.addStat(net.minecraft.stats.StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
/*  499 */     this.item.onCreated(this, worldIn, playerIn);
/*      */   }
/*      */   
/*      */   public boolean getIsItemStackEqual(ItemStack p_179549_1_)
/*      */   {
/*  504 */     return isItemStackEqual(p_179549_1_);
/*      */   }
/*      */   
/*      */   public int getMaxItemUseDuration()
/*      */   {
/*  509 */     return getItem().getMaxItemUseDuration(this);
/*      */   }
/*      */   
/*      */   public EnumAction getItemUseAction()
/*      */   {
/*  514 */     return getItem().getItemUseAction(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft)
/*      */   {
/*  522 */     getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasTagCompound()
/*      */   {
/*  530 */     return this.stackTagCompound != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NBTTagCompound getTagCompound()
/*      */   {
/*  538 */     return this.stackTagCompound;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public NBTTagCompound getSubCompound(String key, boolean create)
/*      */   {
/*  546 */     if ((this.stackTagCompound != null) && (this.stackTagCompound.hasKey(key, 10)))
/*      */     {
/*  548 */       return this.stackTagCompound.getCompoundTag(key);
/*      */     }
/*  550 */     if (create)
/*      */     {
/*  552 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  553 */       setTagInfo(key, nbttagcompound);
/*  554 */       return nbttagcompound;
/*      */     }
/*      */     
/*      */ 
/*  558 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public NBTTagList getEnchantmentTagList()
/*      */   {
/*  564 */     return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTagCompound(NBTTagCompound nbt)
/*      */   {
/*  572 */     this.stackTagCompound = nbt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName()
/*      */   {
/*  580 */     String s = getItem().getItemStackDisplayName(this);
/*      */     
/*  582 */     if ((this.stackTagCompound != null) && (this.stackTagCompound.hasKey("display", 10)))
/*      */     {
/*  584 */       NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/*      */       
/*  586 */       if (nbttagcompound.hasKey("Name", 8))
/*      */       {
/*  588 */         s = nbttagcompound.getString("Name");
/*      */       }
/*      */     }
/*      */     
/*  592 */     return s;
/*      */   }
/*      */   
/*      */   public ItemStack setStackDisplayName(String displayName)
/*      */   {
/*  597 */     if (this.stackTagCompound == null)
/*      */     {
/*  599 */       this.stackTagCompound = new NBTTagCompound();
/*      */     }
/*      */     
/*  602 */     if (!this.stackTagCompound.hasKey("display", 10))
/*      */     {
/*  604 */       this.stackTagCompound.setTag("display", new NBTTagCompound());
/*      */     }
/*      */     
/*  607 */     this.stackTagCompound.getCompoundTag("display").setString("Name", displayName);
/*  608 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearCustomName()
/*      */   {
/*  616 */     if (this.stackTagCompound != null)
/*      */     {
/*  618 */       if (this.stackTagCompound.hasKey("display", 10))
/*      */       {
/*  620 */         NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/*  621 */         nbttagcompound.removeTag("Name");
/*      */         
/*  623 */         if (nbttagcompound.hasNoTags())
/*      */         {
/*  625 */           this.stackTagCompound.removeTag("display");
/*      */           
/*  627 */           if (this.stackTagCompound.hasNoTags())
/*      */           {
/*  629 */             setTagCompound(null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasDisplayName()
/*      */   {
/*  641 */     return !this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound == null ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8);
/*      */   }
/*      */   
/*      */   public List<String> getTooltip(EntityPlayer playerIn, boolean advanced)
/*      */   {
/*  646 */     List<String> list = com.google.common.collect.Lists.newArrayList();
/*  647 */     String s = getDisplayName();
/*      */     
/*  649 */     if (hasDisplayName())
/*      */     {
/*  651 */       s = EnumChatFormatting.ITALIC + s;
/*      */     }
/*      */     
/*  654 */     s = s + EnumChatFormatting.RESET;
/*      */     
/*  656 */     if (advanced)
/*      */     {
/*  658 */       String s1 = "";
/*      */       
/*  660 */       if (s.length() > 0)
/*      */       {
/*  662 */         s = s + " (";
/*  663 */         s1 = ")";
/*      */       }
/*      */       
/*  666 */       int i = Item.getIdFromItem(this.item);
/*      */       
/*  668 */       if (getHasSubtypes())
/*      */       {
/*  670 */         s = s + String.format("#%04d/%d%s", new Object[] { Integer.valueOf(i), Integer.valueOf(this.itemDamage), s1 });
/*      */       }
/*      */       else
/*      */       {
/*  674 */         s = s + String.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });
/*      */       }
/*      */     }
/*  677 */     else if ((!hasDisplayName()) && (this.item == Items.filled_map))
/*      */     {
/*  679 */       s = s + " #" + this.itemDamage;
/*      */     }
/*      */     
/*  682 */     list.add(s);
/*  683 */     int i1 = 0;
/*      */     
/*  685 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("HideFlags", 99)))
/*      */     {
/*  687 */       i1 = this.stackTagCompound.getInteger("HideFlags");
/*      */     }
/*      */     
/*  690 */     if ((i1 & 0x20) == 0)
/*      */     {
/*  692 */       this.item.addInformation(this, playerIn, list, advanced);
/*      */     }
/*      */     int j1;
/*  695 */     if (hasTagCompound())
/*      */     {
/*  697 */       if ((i1 & 0x1) == 0)
/*      */       {
/*  699 */         NBTTagList nbttaglist = getEnchantmentTagList();
/*      */         
/*  701 */         if (nbttaglist != null)
/*      */         {
/*  703 */           for (int j = 0; j < nbttaglist.tagCount(); j++)
/*      */           {
/*  705 */             int k = nbttaglist.getCompoundTagAt(j).getShort("id");
/*  706 */             int l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
/*      */             
/*  708 */             if (Enchantment.getEnchantmentById(k) != null)
/*      */             {
/*  710 */               list.add(Enchantment.getEnchantmentById(k).getTranslatedName(l));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  716 */       if (this.stackTagCompound.hasKey("display", 10))
/*      */       {
/*  718 */         NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
/*      */         
/*  720 */         if (nbttagcompound.hasKey("color", 3))
/*      */         {
/*  722 */           if (advanced)
/*      */           {
/*  724 */             list.add("Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());
/*      */           }
/*      */           else
/*      */           {
/*  728 */             list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
/*      */           }
/*      */         }
/*      */         
/*  732 */         if (nbttagcompound.getTagId("Lore") == 9)
/*      */         {
/*  734 */           NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);
/*      */           
/*  736 */           if (nbttaglist1.tagCount() > 0)
/*      */           {
/*  738 */             for (j1 = 0; j1 < nbttaglist1.tagCount(); j1++)
/*      */             {
/*  740 */               list.add(EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + nbttaglist1.getStringTagAt(j1));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  747 */     Multimap<String, AttributeModifier> multimap = getAttributeModifiers();
/*      */     
/*  749 */     if ((!multimap.isEmpty()) && ((i1 & 0x2) == 0))
/*      */     {
/*  751 */       list.add("");
/*      */       
/*  753 */       for (Map.Entry<String, AttributeModifier> entry : multimap.entries())
/*      */       {
/*  755 */         AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
/*  756 */         double d0 = attributemodifier.getAmount();
/*      */         
/*  758 */         if (attributemodifier.getID() == Item.itemModifierUUID)
/*      */         {
/*  760 */           d0 += EnchantmentHelper.func_152377_a(this, net.minecraft.entity.EnumCreatureAttribute.UNDEFINED);
/*      */         }
/*      */         
/*      */         double d1;
/*      */         double d1;
/*  765 */         if ((attributemodifier.getOperation() != 1) && (attributemodifier.getOperation() != 2))
/*      */         {
/*  767 */           d1 = d0;
/*      */         }
/*      */         else
/*      */         {
/*  771 */           d1 = d0 * 100.0D;
/*      */         }
/*      */         
/*  774 */         if (d0 > 0.0D)
/*      */         {
/*  776 */           list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted(new StringBuilder("attribute.modifier.plus.").append(attributemodifier.getOperation()).toString(), new Object[] { DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey()) }));
/*      */         }
/*  778 */         else if (d0 < 0.0D)
/*      */         {
/*  780 */           d1 *= -1.0D;
/*  781 */           list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(new StringBuilder("attribute.modifier.take.").append(attributemodifier.getOperation()).toString(), new Object[] { DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey()) }));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  786 */     if ((hasTagCompound()) && (getTagCompound().getBoolean("Unbreakable")) && ((i1 & 0x4) == 0))
/*      */     {
/*  788 */       list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
/*      */     }
/*      */     
/*  791 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("CanDestroy", 9)) && ((i1 & 0x8) == 0))
/*      */     {
/*  793 */       NBTTagList nbttaglist2 = this.stackTagCompound.getTagList("CanDestroy", 8);
/*      */       
/*  795 */       if (nbttaglist2.tagCount() > 0)
/*      */       {
/*  797 */         list.add("");
/*  798 */         list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));
/*      */         
/*  800 */         for (int k1 = 0; k1 < nbttaglist2.tagCount(); k1++)
/*      */         {
/*  802 */           Block block = Block.getBlockFromName(nbttaglist2.getStringTagAt(k1));
/*      */           
/*  804 */           if (block != null)
/*      */           {
/*  806 */             list.add(EnumChatFormatting.DARK_GRAY + block.getLocalizedName());
/*      */           }
/*      */           else
/*      */           {
/*  810 */             list.add(EnumChatFormatting.DARK_GRAY + "missingno");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  816 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("CanPlaceOn", 9)) && ((i1 & 0x10) == 0))
/*      */     {
/*  818 */       NBTTagList nbttaglist3 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
/*      */       
/*  820 */       if (nbttaglist3.tagCount() > 0)
/*      */       {
/*  822 */         list.add("");
/*  823 */         list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));
/*      */         
/*  825 */         for (int l1 = 0; l1 < nbttaglist3.tagCount(); l1++)
/*      */         {
/*  827 */           Block block1 = Block.getBlockFromName(nbttaglist3.getStringTagAt(l1));
/*      */           
/*  829 */           if (block1 != null)
/*      */           {
/*  831 */             list.add(EnumChatFormatting.DARK_GRAY + block1.getLocalizedName());
/*      */           }
/*      */           else
/*      */           {
/*  835 */             list.add(EnumChatFormatting.DARK_GRAY + "missingno");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  841 */     if (advanced)
/*      */     {
/*  843 */       if (isItemDamaged())
/*      */       {
/*  845 */         list.add("Durability: " + (getMaxDamage() - getItemDamage()) + " / " + getMaxDamage());
/*      */       }
/*      */       
/*  848 */       list.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation)Item.itemRegistry.getNameForObject(this.item)).toString());
/*      */       
/*  850 */       if (hasTagCompound())
/*      */       {
/*  852 */         list.add(EnumChatFormatting.DARK_GRAY + "NBT: " + getTagCompound().getKeySet().size() + " tag(s)");
/*      */       }
/*      */     }
/*      */     
/*  856 */     return list;
/*      */   }
/*      */   
/*      */   public boolean hasEffect()
/*      */   {
/*  861 */     return getItem().hasEffect(this);
/*      */   }
/*      */   
/*      */   public EnumRarity getRarity()
/*      */   {
/*  866 */     return getItem().getRarity(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemEnchantable()
/*      */   {
/*  874 */     return getItem().isItemTool(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addEnchantment(Enchantment ench, int level)
/*      */   {
/*  882 */     if (this.stackTagCompound == null)
/*      */     {
/*  884 */       setTagCompound(new NBTTagCompound());
/*      */     }
/*      */     
/*  887 */     if (!this.stackTagCompound.hasKey("ench", 9))
/*      */     {
/*  889 */       this.stackTagCompound.setTag("ench", new NBTTagList());
/*      */     }
/*      */     
/*  892 */     NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
/*  893 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  894 */     nbttagcompound.setShort("id", (short)ench.effectId);
/*  895 */     nbttagcompound.setShort("lvl", (short)(byte)level);
/*  896 */     nbttaglist.appendTag(nbttagcompound);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemEnchanted()
/*      */   {
/*  904 */     return (this.stackTagCompound != null) && (this.stackTagCompound.hasKey("ench", 9));
/*      */   }
/*      */   
/*      */   public void setTagInfo(String key, NBTBase value)
/*      */   {
/*  909 */     if (this.stackTagCompound == null)
/*      */     {
/*  911 */       setTagCompound(new NBTTagCompound());
/*      */     }
/*      */     
/*  914 */     this.stackTagCompound.setTag(key, value);
/*      */   }
/*      */   
/*      */   public boolean canEditBlocks()
/*      */   {
/*  919 */     return getItem().canItemEditBlocks();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnItemFrame()
/*      */   {
/*  927 */     return this.itemFrame != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setItemFrame(EntityItemFrame frame)
/*      */   {
/*  935 */     this.itemFrame = frame;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityItemFrame getItemFrame()
/*      */   {
/*  943 */     return this.itemFrame;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRepairCost()
/*      */   {
/*  951 */     return (hasTagCompound()) && (this.stackTagCompound.hasKey("RepairCost", 3)) ? this.stackTagCompound.getInteger("RepairCost") : 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRepairCost(int cost)
/*      */   {
/*  959 */     if (!hasTagCompound())
/*      */     {
/*  961 */       this.stackTagCompound = new NBTTagCompound();
/*      */     }
/*      */     
/*  964 */     this.stackTagCompound.setInteger("RepairCost", cost);
/*      */   }
/*      */   
/*      */ 
/*      */   public Multimap<String, AttributeModifier> getAttributeModifiers()
/*      */   {
/*      */     Multimap<String, AttributeModifier> multimap;
/*  971 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("AttributeModifiers", 9)))
/*      */     {
/*  973 */       Multimap<String, AttributeModifier> multimap = com.google.common.collect.HashMultimap.create();
/*  974 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
/*      */       
/*  976 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */       {
/*  978 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  979 */         AttributeModifier attributemodifier = net.minecraft.entity.SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
/*      */         
/*  981 */         if ((attributemodifier != null) && (attributemodifier.getID().getLeastSignificantBits() != 0L) && (attributemodifier.getID().getMostSignificantBits() != 0L))
/*      */         {
/*  983 */           multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  989 */       multimap = getItem().getItemAttributeModifiers();
/*      */     }
/*      */     
/*  992 */     return multimap;
/*      */   }
/*      */   
/*      */   public void setItem(Item newItem)
/*      */   {
/*  997 */     this.item = newItem;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChatComponent getChatComponent()
/*      */   {
/* 1005 */     ChatComponentText chatcomponenttext = new ChatComponentText(getDisplayName());
/*      */     
/* 1007 */     if (hasDisplayName())
/*      */     {
/* 1009 */       chatcomponenttext.getChatStyle().setItalic(Boolean.valueOf(true));
/*      */     }
/*      */     
/* 1012 */     IChatComponent ichatcomponent = new ChatComponentText("[").appendSibling(chatcomponenttext).appendText("]");
/*      */     
/* 1014 */     if (this.item != null)
/*      */     {
/* 1016 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 1017 */       writeToNBT(nbttagcompound);
/* 1018 */       ichatcomponent.getChatStyle().setChatHoverEvent(new net.minecraft.event.HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nbttagcompound.toString())));
/* 1019 */       ichatcomponent.getChatStyle().setColor(getRarity().rarityColor);
/*      */     }
/*      */     
/* 1022 */     return ichatcomponent;
/*      */   }
/*      */   
/*      */   public boolean canDestroy(Block blockIn)
/*      */   {
/* 1027 */     if (blockIn == this.canDestroyCacheBlock)
/*      */     {
/* 1029 */       return this.canDestroyCacheResult;
/*      */     }
/*      */     
/*      */ 
/* 1033 */     this.canDestroyCacheBlock = blockIn;
/*      */     
/* 1035 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("CanDestroy", 9)))
/*      */     {
/* 1037 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
/*      */       
/* 1039 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */       {
/* 1041 */         Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
/*      */         
/* 1043 */         if (block == blockIn)
/*      */         {
/* 1045 */           this.canDestroyCacheResult = true;
/* 1046 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1051 */     this.canDestroyCacheResult = false;
/* 1052 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean canPlaceOn(Block blockIn)
/*      */   {
/* 1058 */     if (blockIn == this.canPlaceOnCacheBlock)
/*      */     {
/* 1060 */       return this.canPlaceOnCacheResult;
/*      */     }
/*      */     
/*      */ 
/* 1064 */     this.canPlaceOnCacheBlock = blockIn;
/*      */     
/* 1066 */     if ((hasTagCompound()) && (this.stackTagCompound.hasKey("CanPlaceOn", 9)))
/*      */     {
/* 1068 */       NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
/*      */       
/* 1070 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*      */       {
/* 1072 */         Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
/*      */         
/* 1074 */         if (block == blockIn)
/*      */         {
/* 1076 */           this.canPlaceOnCacheResult = true;
/* 1077 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1082 */     this.canPlaceOnCacheResult = false;
/* 1083 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */