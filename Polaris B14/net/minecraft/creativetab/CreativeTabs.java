/*     */ package net.minecraft.creativetab;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnumEnchantmentType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public abstract class CreativeTabs
/*     */ {
/*  15 */   public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
/*  16 */   public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  20 */       return Item.getItemFromBlock(Blocks.brick_block);
/*     */     }
/*     */   };
/*  23 */   public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  27 */       return Item.getItemFromBlock(Blocks.double_plant);
/*     */     }
/*     */     
/*     */     public int getIconItemDamage() {
/*  31 */       return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
/*     */     }
/*     */   };
/*  34 */   public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  38 */       return Items.redstone;
/*     */     }
/*     */   };
/*  41 */   public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  45 */       return Item.getItemFromBlock(Blocks.golden_rail);
/*     */     }
/*     */   };
/*  48 */   public static final CreativeTabs tabMisc = new CreativeTabs(4, "misc")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  52 */       return Items.lava_bucket;
/*     */     }
/*  54 */   }.setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ALL });
/*  55 */   public static final CreativeTabs tabAllSearch = new CreativeTabs(5, "search")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  59 */       return Items.compass;
/*     */     }
/*  61 */   }.setBackgroundImageName("item_search.png");
/*  62 */   public static final CreativeTabs tabFood = new CreativeTabs(6, "food")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  66 */       return Items.apple;
/*     */     }
/*     */   };
/*  69 */   public static final CreativeTabs tabTools = new CreativeTabs(7, "tools")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  73 */       return Items.iron_axe;
/*     */     }
/*  75 */   }.setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE });
/*  76 */   public static final CreativeTabs tabCombat = new CreativeTabs(8, "combat")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  80 */       return Items.golden_sword;
/*     */     }
/*  82 */   }.setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON });
/*  83 */   public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  87 */       return Items.potionitem;
/*     */     }
/*     */   };
/*  90 */   public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/*  94 */       return Items.stick;
/*     */     }
/*     */   };
/*  97 */   public static final CreativeTabs tabInventory = new CreativeTabs(11, "inventory")
/*     */   {
/*     */     public Item getTabIconItem()
/*     */     {
/* 101 */       return Item.getItemFromBlock(Blocks.chest);
/*     */     }
/* 103 */   }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
/*     */   
/*     */   private final int tabIndex;
/*     */   
/*     */   private final String tabLabel;
/* 108 */   private String theTexture = "items.png";
/* 109 */   private boolean hasScrollbar = true;
/*     */   
/*     */ 
/* 112 */   private boolean drawTitle = true;
/*     */   private EnumEnchantmentType[] enchantmentTypes;
/*     */   private ItemStack iconItemStack;
/*     */   
/*     */   public CreativeTabs(int index, String label)
/*     */   {
/* 118 */     this.tabIndex = index;
/* 119 */     this.tabLabel = label;
/* 120 */     creativeTabArray[index] = this;
/*     */   }
/*     */   
/*     */   public int getTabIndex()
/*     */   {
/* 125 */     return this.tabIndex;
/*     */   }
/*     */   
/*     */   public String getTabLabel()
/*     */   {
/* 130 */     return this.tabLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTranslatedTabLabel()
/*     */   {
/* 138 */     return "itemGroup." + getTabLabel();
/*     */   }
/*     */   
/*     */   public ItemStack getIconItemStack()
/*     */   {
/* 143 */     if (this.iconItemStack == null)
/*     */     {
/* 145 */       this.iconItemStack = new ItemStack(getTabIconItem(), 1, getIconItemDamage());
/*     */     }
/*     */     
/* 148 */     return this.iconItemStack;
/*     */   }
/*     */   
/*     */   public abstract Item getTabIconItem();
/*     */   
/*     */   public int getIconItemDamage()
/*     */   {
/* 155 */     return 0;
/*     */   }
/*     */   
/*     */   public String getBackgroundImageName()
/*     */   {
/* 160 */     return this.theTexture;
/*     */   }
/*     */   
/*     */   public CreativeTabs setBackgroundImageName(String texture)
/*     */   {
/* 165 */     this.theTexture = texture;
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public boolean drawInForegroundOfTab()
/*     */   {
/* 171 */     return this.drawTitle;
/*     */   }
/*     */   
/*     */   public CreativeTabs setNoTitle()
/*     */   {
/* 176 */     this.drawTitle = false;
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldHidePlayerInventory()
/*     */   {
/* 182 */     return this.hasScrollbar;
/*     */   }
/*     */   
/*     */   public CreativeTabs setNoScrollbar()
/*     */   {
/* 187 */     this.hasScrollbar = false;
/* 188 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTabColumn()
/*     */   {
/* 196 */     return this.tabIndex % 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTabInFirstRow()
/*     */   {
/* 204 */     return this.tabIndex < 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumEnchantmentType[] getRelevantEnchantmentTypes()
/*     */   {
/* 212 */     return this.enchantmentTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types)
/*     */   {
/* 220 */     this.enchantmentTypes = types;
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public boolean hasRelevantEnchantmentType(EnumEnchantmentType enchantmentType)
/*     */   {
/* 226 */     if (this.enchantmentTypes == null)
/*     */     {
/* 228 */       return false;
/*     */     }
/*     */     
/*     */     EnumEnchantmentType[] arrayOfEnumEnchantmentType;
/* 232 */     int j = (arrayOfEnumEnchantmentType = this.enchantmentTypes).length; for (int i = 0; i < j; i++) { EnumEnchantmentType enumenchantmenttype = arrayOfEnumEnchantmentType[i];
/*     */       
/* 234 */       if (enumenchantmenttype == enchantmentType)
/*     */       {
/* 236 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 240 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void displayAllReleventItems(List<ItemStack> p_78018_1_)
/*     */   {
/* 249 */     for (Item item : Item.itemRegistry)
/*     */     {
/* 251 */       if ((item != null) && (item.getCreativeTab() == this))
/*     */       {
/* 253 */         item.getSubItems(item, this, p_78018_1_);
/*     */       }
/*     */     }
/*     */     
/* 257 */     if (getRelevantEnchantmentTypes() != null)
/*     */     {
/* 259 */       addEnchantmentBooksToList(p_78018_1_, getRelevantEnchantmentTypes());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addEnchantmentBooksToList(List<ItemStack> itemList, EnumEnchantmentType... enchantmentType)
/*     */   {
/*     */     Enchantment[] arrayOfEnchantment;
/*     */     
/* 268 */     int j = (arrayOfEnchantment = Enchantment.enchantmentsBookList).length; for (int i = 0; i < j; i++) { Enchantment enchantment = arrayOfEnchantment[i];
/*     */       
/* 270 */       if ((enchantment != null) && (enchantment.type != null))
/*     */       {
/* 272 */         boolean flag = false;
/*     */         
/* 274 */         for (int i = 0; (i < enchantmentType.length) && (!flag); i++)
/*     */         {
/* 276 */           if (enchantment.type == enchantmentType[i])
/*     */           {
/* 278 */             flag = true;
/*     */           }
/*     */         }
/*     */         
/* 282 */         if (flag)
/*     */         {
/* 284 */           itemList.add(Items.enchanted_book.getEnchantedItemStack(new net.minecraft.enchantment.EnchantmentData(enchantment, enchantment.getMaxLevel())));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\creativetab\CreativeTabs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */