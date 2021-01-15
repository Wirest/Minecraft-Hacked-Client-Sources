/*     */ package com.jagrosh.discordipc.entities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class User
/*     */ {
/*     */   private final String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String discriminator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final long id;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String avatar;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public User(String name, String discriminator, long id, String avatar)
/*     */   {
/*  43 */     this.name = name;
/*  44 */     this.discriminator = discriminator;
/*  45 */     this.id = id;
/*  46 */     this.avatar = avatar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  56 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDiscriminator()
/*     */   {
/*  66 */     return this.discriminator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getIdLong()
/*     */   {
/*  76 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/*  86 */     return Long.toString(this.id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAvatarId()
/*     */   {
/*  96 */     return this.avatar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAvatarUrl()
/*     */   {
/* 106 */     return 
/* 107 */       "https://cdn.discordapp.com/avatars/" + getId() + "/" + getAvatarId() + (getAvatarId().startsWith("a_") ? ".gif" : ".png");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultAvatarId()
/*     */   {
/* 117 */     return DefaultAvatar.values()[(Integer.parseInt(getDiscriminator()) % DefaultAvatar.values().length)].toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultAvatarUrl()
/*     */   {
/* 127 */     return "https://discordapp.com/assets/" + getDefaultAvatarId() + ".png";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEffectiveAvatarUrl()
/*     */   {
/* 138 */     return getAvatarUrl() == null ? getDefaultAvatarUrl() : getAvatarUrl();
/*     */   }
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
/*     */   public boolean isBot()
/*     */   {
/* 152 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAsMention()
/*     */   {
/* 164 */     return "<@" + this.id + '>';
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 170 */     if (!(o instanceof User))
/* 171 */       return false;
/* 172 */     User oUser = (User)o;
/* 173 */     return (this == oUser) || (this.id == oUser.id);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 179 */     return Long.hashCode(this.id);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 185 */     return "U:" + getName() + '(' + this.id + ')';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum DefaultAvatar
/*     */   {
/* 194 */     BLURPLE("6debd47ed13483642cf09e832ed0bc1b"), 
/* 195 */     GREY("322c936a8c8be1b803cd94861bdfa868"), 
/* 196 */     GREEN("dd4dbc0016779df1378e7812eabaa04d"), 
/* 197 */     ORANGE("0e291f67c9274a1abdddeb3fd919cbaa"), 
/* 198 */     RED("1cbd08c76f8af6dddce02c5138971129");
/*     */     
/*     */     private final String text;
/*     */     
/*     */     private DefaultAvatar(String text)
/*     */     {
/* 204 */       this.text = text;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 210 */       return this.text;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\entities\User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */