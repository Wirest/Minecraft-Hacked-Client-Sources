/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.event.HoverEvent.Action;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ 
/*     */ public class StatBase
/*     */ {
/*     */   public final String statId;
/*     */   private final IChatComponent statName;
/*     */   public boolean isIndependent;
/*     */   private final IStatType type;
/*     */   private final IScoreObjectiveCriteria field_150957_c;
/*     */   private Class<? extends IJsonSerializable> field_150956_d;
/*  24 */   private static NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
/*  25 */   public static IStatType simpleStatType = new IStatType()
/*     */   {
/*     */     public String format(int p_75843_1_)
/*     */     {
/*  29 */       return StatBase.numberFormat.format(p_75843_1_);
/*     */     }
/*     */   };
/*  32 */   private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");
/*  33 */   public static IStatType timeStatType = new IStatType()
/*     */   {
/*     */     public String format(int p_75843_1_)
/*     */     {
/*  37 */       double d0 = p_75843_1_ / 20.0D;
/*  38 */       double d1 = d0 / 60.0D;
/*  39 */       double d2 = d1 / 60.0D;
/*  40 */       double d3 = d2 / 24.0D;
/*  41 */       double d4 = d3 / 365.0D;
/*  42 */       return d0 + " s";
/*     */     }
/*     */   };
/*  45 */   public static IStatType distanceStatType = new IStatType()
/*     */   {
/*     */     public String format(int p_75843_1_)
/*     */     {
/*  49 */       double d0 = p_75843_1_ / 100.0D;
/*  50 */       double d1 = d0 / 1000.0D;
/*  51 */       return p_75843_1_ + " cm";
/*     */     }
/*     */   };
/*  54 */   public static IStatType field_111202_k = new IStatType()
/*     */   {
/*     */     public String format(int p_75843_1_)
/*     */     {
/*  58 */       return StatBase.decimalFormat.format(p_75843_1_ * 0.1D);
/*     */     }
/*     */   };
/*     */   
/*     */   public StatBase(String statIdIn, IChatComponent statNameIn, IStatType typeIn)
/*     */   {
/*  64 */     this.statId = statIdIn;
/*  65 */     this.statName = statNameIn;
/*  66 */     this.type = typeIn;
/*  67 */     this.field_150957_c = new ObjectiveStat(this);
/*  68 */     IScoreObjectiveCriteria.INSTANCES.put(this.field_150957_c.getName(), this.field_150957_c);
/*     */   }
/*     */   
/*     */   public StatBase(String statIdIn, IChatComponent statNameIn)
/*     */   {
/*  73 */     this(statIdIn, statNameIn, simpleStatType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StatBase initIndependentStat()
/*     */   {
/*  82 */     this.isIndependent = true;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StatBase registerStat()
/*     */   {
/*  91 */     if (StatList.oneShotStats.containsKey(this.statId))
/*     */     {
/*  93 */       throw new RuntimeException("Duplicate stat id: \"" + ((StatBase)StatList.oneShotStats.get(this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
/*     */     }
/*     */     
/*     */ 
/*  97 */     StatList.allStats.add(this);
/*  98 */     StatList.oneShotStats.put(this.statId, this);
/*  99 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAchievement()
/*     */   {
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   public String format(int p_75968_1_)
/*     */   {
/* 113 */     return this.type.format(p_75968_1_);
/*     */   }
/*     */   
/*     */   public IChatComponent getStatName()
/*     */   {
/* 118 */     IChatComponent ichatcomponent = this.statName.createCopy();
/* 119 */     ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
/* 120 */     ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
/* 121 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */   public IChatComponent func_150955_j()
/*     */   {
/* 126 */     IChatComponent ichatcomponent = getStatName();
/* 127 */     IChatComponent ichatcomponent1 = new ChatComponentText("[").appendSibling(ichatcomponent).appendText("]");
/* 128 */     ichatcomponent1.setChatStyle(ichatcomponent.getChatStyle());
/* 129 */     return ichatcomponent1;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 134 */     if (this == p_equals_1_)
/*     */     {
/* 136 */       return true;
/*     */     }
/* 138 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */     {
/* 140 */       StatBase statbase = (StatBase)p_equals_1_;
/* 141 */       return this.statId.equals(statbase.statId);
/*     */     }
/*     */     
/*     */ 
/* 145 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 151 */     return this.statId.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 156 */     return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.field_150957_c + '}';
/*     */   }
/*     */   
/*     */   public IScoreObjectiveCriteria func_150952_k()
/*     */   {
/* 161 */     return this.field_150957_c;
/*     */   }
/*     */   
/*     */   public Class<? extends IJsonSerializable> func_150954_l()
/*     */   {
/* 166 */     return this.field_150956_d;
/*     */   }
/*     */   
/*     */   public StatBase func_150953_b(Class<? extends IJsonSerializable> p_150953_1_)
/*     */   {
/* 171 */     this.field_150956_d = p_150953_1_;
/* 172 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\stats\StatBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */