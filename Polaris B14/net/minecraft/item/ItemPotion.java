/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionHelper;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemPotion extends Item
/*     */ {
/*  29 */   private Map<Integer, List<PotionEffect>> effectCache = Maps.newHashMap();
/*  30 */   private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE = ;
/*     */   
/*     */   public ItemPotion()
/*     */   {
/*  34 */     setMaxStackSize(1);
/*  35 */     setHasSubtypes(true);
/*  36 */     setMaxDamage(0);
/*  37 */     setCreativeTab(CreativeTabs.tabBrewing);
/*     */   }
/*     */   
/*     */   public List<PotionEffect> getEffects(ItemStack stack)
/*     */   {
/*  42 */     if ((stack.hasTagCompound()) && (stack.getTagCompound().hasKey("CustomPotionEffects", 9)))
/*     */     {
/*  44 */       List<PotionEffect> list1 = com.google.common.collect.Lists.newArrayList();
/*  45 */       NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);
/*     */       
/*  47 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/*  49 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  50 */         PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
/*     */         
/*  52 */         if (potioneffect != null)
/*     */         {
/*  54 */           list1.add(potioneffect);
/*     */         }
/*     */       }
/*     */       
/*  58 */       return list1;
/*     */     }
/*     */     
/*     */ 
/*  62 */     List<PotionEffect> list = (List)this.effectCache.get(Integer.valueOf(stack.getMetadata()));
/*     */     
/*  64 */     if (list == null)
/*     */     {
/*  66 */       list = PotionHelper.getPotionEffects(stack.getMetadata(), false);
/*  67 */       this.effectCache.put(Integer.valueOf(stack.getMetadata()), list);
/*     */     }
/*     */     
/*  70 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<PotionEffect> getEffects(int meta)
/*     */   {
/*  76 */     List<PotionEffect> list = (List)this.effectCache.get(Integer.valueOf(meta));
/*     */     
/*  78 */     if (list == null)
/*     */     {
/*  80 */       list = PotionHelper.getPotionEffects(meta, false);
/*  81 */       this.effectCache.put(Integer.valueOf(meta), list);
/*     */     }
/*     */     
/*  84 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  93 */     if (!playerIn.capabilities.isCreativeMode)
/*     */     {
/*  95 */       stack.stackSize -= 1;
/*     */     }
/*     */     
/*  98 */     if (!worldIn.isRemote)
/*     */     {
/* 100 */       List<PotionEffect> list = getEffects(stack);
/*     */       
/* 102 */       if (list != null)
/*     */       {
/* 104 */         for (PotionEffect potioneffect : list)
/*     */         {
/* 106 */           playerIn.addPotionEffect(new PotionEffect(potioneffect));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 111 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */     
/* 113 */     if (!playerIn.capabilities.isCreativeMode)
/*     */     {
/* 115 */       if (stack.stackSize <= 0)
/*     */       {
/* 117 */         return new ItemStack(Items.glass_bottle);
/*     */       }
/*     */       
/* 120 */       playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
/*     */     }
/*     */     
/* 123 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxItemUseDuration(ItemStack stack)
/*     */   {
/* 131 */     return 32;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumAction getItemUseAction(ItemStack stack)
/*     */   {
/* 139 */     return EnumAction.DRINK;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/* 147 */     if (isSplash(itemStackIn.getMetadata()))
/*     */     {
/* 149 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/* 151 */         itemStackIn.stackSize -= 1;
/*     */       }
/*     */       
/* 154 */       worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*     */       
/* 156 */       if (!worldIn.isRemote)
/*     */       {
/* 158 */         worldIn.spawnEntityInWorld(new net.minecraft.entity.projectile.EntityPotion(worldIn, playerIn, itemStackIn));
/*     */       }
/*     */       
/* 161 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 162 */       return itemStackIn;
/*     */     }
/*     */     
/*     */ 
/* 166 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/* 167 */     return itemStackIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSplash(int meta)
/*     */   {
/* 176 */     return (meta & 0x4000) != 0;
/*     */   }
/*     */   
/*     */   public int getColorFromDamage(int meta)
/*     */   {
/* 181 */     return PotionHelper.getLiquidColor(meta, false);
/*     */   }
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass)
/*     */   {
/* 186 */     return renderPass > 0 ? 16777215 : getColorFromDamage(stack.getMetadata());
/*     */   }
/*     */   
/*     */   public boolean isEffectInstant(int meta)
/*     */   {
/* 191 */     List<PotionEffect> list = getEffects(meta);
/*     */     
/* 193 */     if ((list != null) && (!list.isEmpty()))
/*     */     {
/* 195 */       for (PotionEffect potioneffect : list)
/*     */       {
/* 197 */         if (Potion.potionTypes[potioneffect.getPotionID()].isInstant())
/*     */         {
/* 199 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 203 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 207 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getItemStackDisplayName(ItemStack stack)
/*     */   {
/* 213 */     if (stack.getMetadata() == 0)
/*     */     {
/* 215 */       return StatCollector.translateToLocal("item.emptyPotion.name").trim();
/*     */     }
/*     */     
/*     */ 
/* 219 */     String s = "";
/*     */     
/* 221 */     if (isSplash(stack.getMetadata()))
/*     */     {
/* 223 */       s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
/*     */     }
/*     */     
/* 226 */     List<PotionEffect> list = Items.potionitem.getEffects(stack);
/*     */     
/* 228 */     if ((list != null) && (!list.isEmpty()))
/*     */     {
/* 230 */       String s2 = ((PotionEffect)list.get(0)).getEffectName();
/* 231 */       s2 = s2 + ".postfix";
/* 232 */       return s + StatCollector.translateToLocal(s2).trim();
/*     */     }
/*     */     
/*     */ 
/* 236 */     String s1 = PotionHelper.getPotionPrefix(stack.getMetadata());
/* 237 */     return StatCollector.translateToLocal(s1).trim() + " " + super.getItemStackDisplayName(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/* 247 */     if (stack.getMetadata() != 0)
/*     */     {
/* 249 */       List<PotionEffect> list = Items.potionitem.getEffects(stack);
/* 250 */       Multimap<String, AttributeModifier> multimap = com.google.common.collect.HashMultimap.create();
/*     */       
/* 252 */       if ((list != null) && (!list.isEmpty()))
/*     */       {
/* 254 */         for (PotionEffect potioneffect : list)
/*     */         {
/* 256 */           String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
/* 257 */           Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
/* 258 */           Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
/*     */           
/* 260 */           if ((map != null) && (map.size() > 0))
/*     */           {
/* 262 */             for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet())
/*     */             {
/* 264 */               AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
/* 265 */               AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
/* 266 */               multimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
/*     */             }
/*     */           }
/*     */           
/* 270 */           if (potioneffect.getAmplifier() > 0)
/*     */           {
/* 272 */             s1 = s1 + " " + StatCollector.translateToLocal(new StringBuilder("potion.potency.").append(potioneffect.getAmplifier()).toString()).trim();
/*     */           }
/*     */           
/* 275 */           if (potioneffect.getDuration() > 20)
/*     */           {
/* 277 */             s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
/*     */           }
/*     */           
/* 280 */           if (potion.isBadEffect())
/*     */           {
/* 282 */             tooltip.add(EnumChatFormatting.RED + s1);
/*     */           }
/*     */           else
/*     */           {
/* 286 */             tooltip.add(EnumChatFormatting.GRAY + s1);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 292 */         String s = StatCollector.translateToLocal("potion.empty").trim();
/* 293 */         tooltip.add(EnumChatFormatting.GRAY + s);
/*     */       }
/*     */       
/* 296 */       if (!multimap.isEmpty())
/*     */       {
/* 298 */         tooltip.add("");
/* 299 */         tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
/*     */         
/* 301 */         for (Map.Entry<String, AttributeModifier> entry1 : multimap.entries())
/*     */         {
/* 303 */           AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
/* 304 */           double d0 = attributemodifier2.getAmount();
/*     */           double d1;
/*     */           double d1;
/* 307 */           if ((attributemodifier2.getOperation() != 1) && (attributemodifier2.getOperation() != 2))
/*     */           {
/* 309 */             d1 = attributemodifier2.getAmount();
/*     */           }
/*     */           else
/*     */           {
/* 313 */             d1 = attributemodifier2.getAmount() * 100.0D;
/*     */           }
/*     */           
/* 316 */           if (d0 > 0.0D)
/*     */           {
/* 318 */             tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted(new StringBuilder("attribute.modifier.plus.").append(attributemodifier2.getOperation()).toString(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
/*     */           }
/* 320 */           else if (d0 < 0.0D)
/*     */           {
/* 322 */             d1 *= -1.0D;
/* 323 */             tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(new StringBuilder("attribute.modifier.take.").append(attributemodifier2.getOperation()).toString(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasEffect(ItemStack stack)
/*     */   {
/* 332 */     List<PotionEffect> list = getEffects(stack);
/* 333 */     return (list != null) && (!list.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 341 */     super.getSubItems(itemIn, tab, subItems);
/*     */     
/* 343 */     if (SUB_ITEMS_CACHE.isEmpty())
/*     */     {
/* 345 */       for (int i = 0; i <= 15; i++)
/*     */       {
/* 347 */         for (int j = 0; j <= 1; j++)
/*     */         {
/*     */           int lvt_6_1_;
/*     */           int lvt_6_1_;
/* 351 */           if (j == 0)
/*     */           {
/* 353 */             lvt_6_1_ = i | 0x2000;
/*     */           }
/*     */           else
/*     */           {
/* 357 */             lvt_6_1_ = i | 0x4000;
/*     */           }
/*     */           
/* 360 */           for (int l = 0; l <= 2; l++)
/*     */           {
/* 362 */             int i1 = lvt_6_1_;
/*     */             
/* 364 */             if (l != 0)
/*     */             {
/* 366 */               if (l == 1)
/*     */               {
/* 368 */                 i1 = lvt_6_1_ | 0x20;
/*     */               }
/* 370 */               else if (l == 2)
/*     */               {
/* 372 */                 i1 = lvt_6_1_ | 0x40;
/*     */               }
/*     */             }
/*     */             
/* 376 */             List<PotionEffect> list = PotionHelper.getPotionEffects(i1, false);
/*     */             
/* 378 */             if ((list != null) && (!list.isEmpty()))
/*     */             {
/* 380 */               SUB_ITEMS_CACHE.put(list, Integer.valueOf(i1));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 387 */     Iterator iterator = SUB_ITEMS_CACHE.values().iterator();
/*     */     
/* 389 */     while (iterator.hasNext())
/*     */     {
/* 391 */       int j1 = ((Integer)iterator.next()).intValue();
/* 392 */       subItems.add(new ItemStack(itemIn, 1, j1));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */