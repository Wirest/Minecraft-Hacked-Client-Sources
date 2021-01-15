/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class ScorePlayerTeam extends Team
/*     */ {
/*     */   private final Scoreboard theScoreboard;
/*     */   private final String registeredName;
/*  12 */   private final Set<String> membershipSet = Sets.newHashSet();
/*     */   private String teamNameSPT;
/*  14 */   private String namePrefixSPT = "";
/*  15 */   private String colorSuffix = "";
/*  16 */   private boolean allowFriendlyFire = true;
/*  17 */   private boolean canSeeFriendlyInvisibles = true;
/*  18 */   private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
/*  19 */   private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
/*  20 */   private EnumChatFormatting chatFormat = EnumChatFormatting.RESET;
/*     */   
/*     */   public ScorePlayerTeam(Scoreboard theScoreboardIn, String name)
/*     */   {
/*  24 */     this.theScoreboard = theScoreboardIn;
/*  25 */     this.registeredName = name;
/*  26 */     this.teamNameSPT = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRegisteredName()
/*     */   {
/*  34 */     return this.registeredName;
/*     */   }
/*     */   
/*     */   public String getTeamName()
/*     */   {
/*  39 */     return this.teamNameSPT;
/*     */   }
/*     */   
/*     */   public void setTeamName(String name)
/*     */   {
/*  44 */     if (name == null)
/*     */     {
/*  46 */       throw new IllegalArgumentException("Name cannot be null");
/*     */     }
/*     */     
/*     */ 
/*  50 */     this.teamNameSPT = name;
/*  51 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getMembershipCollection()
/*     */   {
/*  57 */     return this.membershipSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getColorPrefix()
/*     */   {
/*  65 */     return this.namePrefixSPT;
/*     */   }
/*     */   
/*     */   public void setNamePrefix(String prefix)
/*     */   {
/*  70 */     if (prefix == null)
/*     */     {
/*  72 */       throw new IllegalArgumentException("Prefix cannot be null");
/*     */     }
/*     */     
/*     */ 
/*  76 */     this.namePrefixSPT = prefix;
/*  77 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getColorSuffix()
/*     */   {
/*  86 */     return this.colorSuffix;
/*     */   }
/*     */   
/*     */   public void setNameSuffix(String suffix)
/*     */   {
/*  91 */     this.colorSuffix = suffix;
/*  92 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */   public String formatString(String input)
/*     */   {
/*  97 */     return getColorPrefix() + input + getColorSuffix();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatPlayerName(Team p_96667_0_, String p_96667_1_)
/*     */   {
/* 105 */     return p_96667_0_ == null ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
/*     */   }
/*     */   
/*     */   public boolean getAllowFriendlyFire()
/*     */   {
/* 110 */     return this.allowFriendlyFire;
/*     */   }
/*     */   
/*     */   public void setAllowFriendlyFire(boolean friendlyFire)
/*     */   {
/* 115 */     this.allowFriendlyFire = friendlyFire;
/* 116 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */   public boolean getSeeFriendlyInvisiblesEnabled()
/*     */   {
/* 121 */     return this.canSeeFriendlyInvisibles;
/*     */   }
/*     */   
/*     */   public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles)
/*     */   {
/* 126 */     this.canSeeFriendlyInvisibles = friendlyInvisibles;
/* 127 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */   public Team.EnumVisible getNameTagVisibility()
/*     */   {
/* 132 */     return this.nameTagVisibility;
/*     */   }
/*     */   
/*     */   public Team.EnumVisible getDeathMessageVisibility()
/*     */   {
/* 137 */     return this.deathMessageVisibility;
/*     */   }
/*     */   
/*     */   public void setNameTagVisibility(Team.EnumVisible p_178772_1_)
/*     */   {
/* 142 */     this.nameTagVisibility = p_178772_1_;
/* 143 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */   public void setDeathMessageVisibility(Team.EnumVisible p_178773_1_)
/*     */   {
/* 148 */     this.deathMessageVisibility = p_178773_1_;
/* 149 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */   
/*     */   public int func_98299_i()
/*     */   {
/* 154 */     int i = 0;
/*     */     
/* 156 */     if (getAllowFriendlyFire())
/*     */     {
/* 158 */       i |= 0x1;
/*     */     }
/*     */     
/* 161 */     if (getSeeFriendlyInvisiblesEnabled())
/*     */     {
/* 163 */       i |= 0x2;
/*     */     }
/*     */     
/* 166 */     return i;
/*     */   }
/*     */   
/*     */   public void func_98298_a(int p_98298_1_)
/*     */   {
/* 171 */     setAllowFriendlyFire((p_98298_1_ & 0x1) > 0);
/* 172 */     setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 0x2) > 0);
/*     */   }
/*     */   
/*     */   public void setChatFormat(EnumChatFormatting p_178774_1_)
/*     */   {
/* 177 */     this.chatFormat = p_178774_1_;
/*     */   }
/*     */   
/*     */   public EnumChatFormatting getChatFormat()
/*     */   {
/* 182 */     return this.chatFormat;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\ScorePlayerTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */