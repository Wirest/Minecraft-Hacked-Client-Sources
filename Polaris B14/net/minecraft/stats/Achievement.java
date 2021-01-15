/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Achievement
/*     */   extends StatBase
/*     */ {
/*     */   public final int displayColumn;
/*     */   public final int displayRow;
/*     */   public final Achievement parentAchievement;
/*     */   private final String achievementDescription;
/*     */   private IStatStringFormat statStringFormatter;
/*     */   public final ItemStack theItemStack;
/*     */   private boolean isSpecial;
/*     */   
/*     */   public Achievement(String p_i46327_1_, String p_i46327_2_, int column, int row, Item p_i46327_5_, Achievement parent)
/*     */   {
/*  53 */     this(p_i46327_1_, p_i46327_2_, column, row, new ItemStack(p_i46327_5_), parent);
/*     */   }
/*     */   
/*     */   public Achievement(String p_i45301_1_, String p_i45301_2_, int column, int row, Block p_i45301_5_, Achievement parent)
/*     */   {
/*  58 */     this(p_i45301_1_, p_i45301_2_, column, row, new ItemStack(p_i45301_5_), parent);
/*     */   }
/*     */   
/*     */   public Achievement(String p_i45302_1_, String p_i45302_2_, int column, int row, ItemStack p_i45302_5_, Achievement parent)
/*     */   {
/*  63 */     super(p_i45302_1_, new ChatComponentTranslation("achievement." + p_i45302_2_, new Object[0]));
/*  64 */     this.theItemStack = p_i45302_5_;
/*  65 */     this.achievementDescription = ("achievement." + p_i45302_2_ + ".desc");
/*  66 */     this.displayColumn = column;
/*  67 */     this.displayRow = row;
/*     */     
/*  69 */     if (column < AchievementList.minDisplayColumn)
/*     */     {
/*  71 */       AchievementList.minDisplayColumn = column;
/*     */     }
/*     */     
/*  74 */     if (row < AchievementList.minDisplayRow)
/*     */     {
/*  76 */       AchievementList.minDisplayRow = row;
/*     */     }
/*     */     
/*  79 */     if (column > AchievementList.maxDisplayColumn)
/*     */     {
/*  81 */       AchievementList.maxDisplayColumn = column;
/*     */     }
/*     */     
/*  84 */     if (row > AchievementList.maxDisplayRow)
/*     */     {
/*  86 */       AchievementList.maxDisplayRow = row;
/*     */     }
/*     */     
/*  89 */     this.parentAchievement = parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Achievement initIndependentStat()
/*     */   {
/*  98 */     this.isIndependent = true;
/*  99 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Achievement setSpecial()
/*     */   {
/* 108 */     this.isSpecial = true;
/* 109 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Achievement registerStat()
/*     */   {
/* 117 */     super.registerStat();
/* 118 */     AchievementList.achievementList.add(this);
/* 119 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAchievement()
/*     */   {
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   public IChatComponent getStatName()
/*     */   {
/* 132 */     IChatComponent ichatcomponent = super.getStatName();
/* 133 */     ichatcomponent.getChatStyle().setColor(getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
/* 134 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */   public Achievement func_150953_b(Class<? extends IJsonSerializable> p_150953_1_)
/*     */   {
/* 139 */     return (Achievement)super.func_150953_b(p_150953_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 147 */     return this.statStringFormatter != null ? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription)) : StatCollector.translateToLocal(this.achievementDescription);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Achievement setStatStringFormatter(IStatStringFormat p_75988_1_)
/*     */   {
/* 155 */     this.statStringFormatter = p_75988_1_;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getSpecial()
/*     */   {
/* 165 */     return this.isSpecial;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\stats\Achievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */