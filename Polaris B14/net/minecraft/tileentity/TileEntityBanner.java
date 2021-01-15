/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TileEntityBanner
/*     */   extends TileEntity
/*     */ {
/*     */   private int baseColor;
/*     */   private NBTTagList patterns;
/*     */   private boolean field_175119_g;
/*     */   private List<EnumBannerPattern> patternList;
/*     */   private List<EnumDyeColor> colorList;
/*     */   private String patternResourceLocation;
/*     */   
/*     */   public void setItemValues(ItemStack stack)
/*     */   {
/*  32 */     this.patterns = null;
/*     */     
/*  34 */     if ((stack.hasTagCompound()) && (stack.getTagCompound().hasKey("BlockEntityTag", 10)))
/*     */     {
/*  36 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
/*     */       
/*  38 */       if (nbttagcompound.hasKey("Patterns"))
/*     */       {
/*  40 */         this.patterns = ((NBTTagList)nbttagcompound.getTagList("Patterns", 10).copy());
/*     */       }
/*     */       
/*  43 */       if (nbttagcompound.hasKey("Base", 99))
/*     */       {
/*  45 */         this.baseColor = nbttagcompound.getInteger("Base");
/*     */       }
/*     */       else
/*     */       {
/*  49 */         this.baseColor = (stack.getMetadata() & 0xF);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  54 */       this.baseColor = (stack.getMetadata() & 0xF);
/*     */     }
/*     */     
/*  57 */     this.patternList = null;
/*  58 */     this.colorList = null;
/*  59 */     this.patternResourceLocation = "";
/*  60 */     this.field_175119_g = true;
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/*  65 */     super.writeToNBT(compound);
/*  66 */     func_181020_a(compound, this.baseColor, this.patterns);
/*     */   }
/*     */   
/*     */   public static void func_181020_a(NBTTagCompound p_181020_0_, int p_181020_1_, NBTTagList p_181020_2_)
/*     */   {
/*  71 */     p_181020_0_.setInteger("Base", p_181020_1_);
/*     */     
/*  73 */     if (p_181020_2_ != null)
/*     */     {
/*  75 */       p_181020_0_.setTag("Patterns", p_181020_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/*  81 */     super.readFromNBT(compound);
/*  82 */     this.baseColor = compound.getInteger("Base");
/*  83 */     this.patterns = compound.getTagList("Patterns", 10);
/*  84 */     this.patternList = null;
/*  85 */     this.colorList = null;
/*  86 */     this.patternResourceLocation = null;
/*  87 */     this.field_175119_g = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/*  96 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  97 */     writeToNBT(nbttagcompound);
/*  98 */     return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
/*     */   }
/*     */   
/*     */   public int getBaseColor()
/*     */   {
/* 103 */     return this.baseColor;
/*     */   }
/*     */   
/*     */   public static int getBaseColor(ItemStack stack)
/*     */   {
/* 108 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/* 109 */     return (nbttagcompound != null) && (nbttagcompound.hasKey("Base")) ? nbttagcompound.getInteger("Base") : stack.getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getPatterns(ItemStack stack)
/*     */   {
/* 117 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/* 118 */     return (nbttagcompound != null) && (nbttagcompound.hasKey("Patterns")) ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
/*     */   }
/*     */   
/*     */   public List<EnumBannerPattern> getPatternList()
/*     */   {
/* 123 */     initializeBannerData();
/* 124 */     return this.patternList;
/*     */   }
/*     */   
/*     */   public NBTTagList func_181021_d()
/*     */   {
/* 129 */     return this.patterns;
/*     */   }
/*     */   
/*     */   public List<EnumDyeColor> getColorList()
/*     */   {
/* 134 */     initializeBannerData();
/* 135 */     return this.colorList;
/*     */   }
/*     */   
/*     */   public String func_175116_e()
/*     */   {
/* 140 */     initializeBannerData();
/* 141 */     return this.patternResourceLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeBannerData()
/*     */   {
/* 150 */     if ((this.patternList == null) || (this.colorList == null) || (this.patternResourceLocation == null))
/*     */     {
/* 152 */       if (!this.field_175119_g)
/*     */       {
/* 154 */         this.patternResourceLocation = "";
/*     */       }
/*     */       else
/*     */       {
/* 158 */         this.patternList = Lists.newArrayList();
/* 159 */         this.colorList = Lists.newArrayList();
/* 160 */         this.patternList.add(EnumBannerPattern.BASE);
/* 161 */         this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
/* 162 */         this.patternResourceLocation = ("b" + this.baseColor);
/*     */         
/* 164 */         if (this.patterns != null)
/*     */         {
/* 166 */           for (int i = 0; i < this.patterns.tagCount(); i++)
/*     */           {
/* 168 */             NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
/* 169 */             EnumBannerPattern tileentitybanner$enumbannerpattern = EnumBannerPattern.getPatternByID(nbttagcompound.getString("Pattern"));
/*     */             
/* 171 */             if (tileentitybanner$enumbannerpattern != null)
/*     */             {
/* 173 */               this.patternList.add(tileentitybanner$enumbannerpattern);
/* 174 */               int j = nbttagcompound.getInteger("Color");
/* 175 */               this.colorList.add(EnumDyeColor.byDyeDamage(j));
/* 176 */               this.patternResourceLocation = (this.patternResourceLocation + tileentitybanner$enumbannerpattern.getPatternID() + j);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void removeBannerData(ItemStack stack)
/*     */   {
/* 189 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/*     */     
/* 191 */     if ((nbttagcompound != null) && (nbttagcompound.hasKey("Patterns", 9)))
/*     */     {
/* 193 */       NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
/*     */       
/* 195 */       if (nbttaglist.tagCount() > 0)
/*     */       {
/* 197 */         nbttaglist.removeTag(nbttaglist.tagCount() - 1);
/*     */         
/* 199 */         if (nbttaglist.hasNoTags())
/*     */         {
/* 201 */           stack.getTagCompound().removeTag("BlockEntityTag");
/*     */           
/* 203 */           if (stack.getTagCompound().hasNoTags())
/*     */           {
/* 205 */             stack.setTagCompound(null);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumBannerPattern
/*     */   {
/* 214 */     BASE("base", "b"), 
/* 215 */     SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "), 
/* 216 */     SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"), 
/* 217 */     SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "), 
/* 218 */     SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "), 
/* 219 */     STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"), 
/* 220 */     STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "), 
/* 221 */     STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "), 
/* 222 */     STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"), 
/* 223 */     STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "), 
/* 224 */     STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "), 
/* 225 */     STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"), 
/* 226 */     STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "), 
/* 227 */     STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "), 
/* 228 */     CROSS("cross", "cr", "# #", " # ", "# #"), 
/* 229 */     STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "), 
/* 230 */     TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"), 
/* 231 */     TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "), 
/* 232 */     TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "), 
/* 233 */     TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "), 
/* 234 */     DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "), 
/* 235 */     DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"), 
/* 236 */     DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "), 
/* 237 */     DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "), 
/* 238 */     CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "), 
/* 239 */     RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "), 
/* 240 */     HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "), 
/* 241 */     HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "), 
/* 242 */     HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"), 
/* 243 */     HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"), 
/* 244 */     BORDER("border", "bo", "###", "# #", "###"), 
/* 245 */     CURLY_BORDER("curly_border", "cbo", new ItemStack(Blocks.vine)), 
/* 246 */     CREEPER("creeper", "cre", new ItemStack(Items.skull, 1, 4)), 
/* 247 */     GRADIENT("gradient", "gra", "# #", " # ", " # "), 
/* 248 */     GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"), 
/* 249 */     BRICKS("bricks", "bri", new ItemStack(Blocks.brick_block)), 
/* 250 */     SKULL("skull", "sku", new ItemStack(Items.skull, 1, 1)), 
/* 251 */     FLOWER("flower", "flo", new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())), 
/* 252 */     MOJANG("mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));
/*     */     
/*     */     private String patternName;
/*     */     private String patternID;
/*     */     private String[] craftingLayers;
/*     */     private ItemStack patternCraftingStack;
/*     */     
/*     */     private EnumBannerPattern(String name, String id)
/*     */     {
/* 261 */       this.craftingLayers = new String[3];
/* 262 */       this.patternName = name;
/* 263 */       this.patternID = id;
/*     */     }
/*     */     
/*     */     private EnumBannerPattern(String name, String id, ItemStack craftingItem)
/*     */     {
/* 268 */       this(name, id);
/* 269 */       this.patternCraftingStack = craftingItem;
/*     */     }
/*     */     
/*     */     private EnumBannerPattern(String name, String id, String craftingTop, String craftingMid, String craftingBot)
/*     */     {
/* 274 */       this(name, id);
/* 275 */       this.craftingLayers[0] = craftingTop;
/* 276 */       this.craftingLayers[1] = craftingMid;
/* 277 */       this.craftingLayers[2] = craftingBot;
/*     */     }
/*     */     
/*     */     public String getPatternName()
/*     */     {
/* 282 */       return this.patternName;
/*     */     }
/*     */     
/*     */     public String getPatternID()
/*     */     {
/* 287 */       return this.patternID;
/*     */     }
/*     */     
/*     */     public String[] getCraftingLayers()
/*     */     {
/* 292 */       return this.craftingLayers;
/*     */     }
/*     */     
/*     */     public boolean hasValidCrafting()
/*     */     {
/* 297 */       return (this.patternCraftingStack != null) || (this.craftingLayers[0] != null);
/*     */     }
/*     */     
/*     */     public boolean hasCraftingStack()
/*     */     {
/* 302 */       return this.patternCraftingStack != null;
/*     */     }
/*     */     
/*     */     public ItemStack getCraftingStack()
/*     */     {
/* 307 */       return this.patternCraftingStack;
/*     */     }
/*     */     
/*     */     public static EnumBannerPattern getPatternByID(String id) {
/*     */       EnumBannerPattern[] arrayOfEnumBannerPattern;
/* 312 */       int j = (arrayOfEnumBannerPattern = values()).length; for (int i = 0; i < j; i++) { EnumBannerPattern tileentitybanner$enumbannerpattern = arrayOfEnumBannerPattern[i];
/*     */         
/* 314 */         if (tileentitybanner$enumbannerpattern.patternID.equals(id))
/*     */         {
/* 316 */           return tileentitybanner$enumbannerpattern;
/*     */         }
/*     */       }
/*     */       
/* 320 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */