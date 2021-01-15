/*     */ package com.jagrosh.discordipc.entities;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONObject;
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
/*     */ public class RichPresence
/*     */ {
/*     */   private final String state;
/*     */   private final String details;
/*     */   private final OffsetDateTime startTimestamp;
/*     */   private final OffsetDateTime endTimestamp;
/*     */   private final String largeImageKey;
/*     */   private final String largeImageText;
/*     */   private final String smallImageKey;
/*     */   private final String smallImageText;
/*     */   private final String partyId;
/*     */   private final int partySize;
/*     */   private final int partyMax;
/*     */   private final String matchSecret;
/*     */   private final String joinSecret;
/*     */   private final String spectateSecret;
/*     */   private final boolean instance;
/*     */   
/*     */   public RichPresence(String state, String details, OffsetDateTime startTimestamp, OffsetDateTime endTimestamp, String largeImageKey, String largeImageText, String smallImageKey, String smallImageText, String partyId, int partySize, int partyMax, String matchSecret, String joinSecret, String spectateSecret, boolean instance)
/*     */   {
/*  52 */     this.state = state;
/*  53 */     this.details = details;
/*  54 */     this.startTimestamp = startTimestamp;
/*  55 */     this.endTimestamp = endTimestamp;
/*  56 */     this.largeImageKey = largeImageKey;
/*  57 */     this.largeImageText = largeImageText;
/*  58 */     this.smallImageKey = smallImageKey;
/*  59 */     this.smallImageText = smallImageText;
/*  60 */     this.partyId = partyId;
/*  61 */     this.partySize = partySize;
/*  62 */     this.partyMax = partyMax;
/*  63 */     this.matchSecret = matchSecret;
/*  64 */     this.joinSecret = joinSecret;
/*  65 */     this.spectateSecret = spectateSecret;
/*  66 */     this.instance = instance;
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
/*     */   public JSONObject toJson()
/*     */   {
/*  80 */     return 
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
/*  98 */       new JSONObject().put("state", this.state).put("details", this.details).put("timestamps", new JSONObject().put("start", this.startTimestamp == null ? null : Long.valueOf(this.startTimestamp.toEpochSecond())).put("end", this.endTimestamp == null ? null : Long.valueOf(this.endTimestamp.toEpochSecond()))).put("assets", new JSONObject().put("large_image", this.largeImageKey).put("large_text", this.largeImageText).put("small_image", this.smallImageKey).put("small_text", this.smallImageText)).put("party", this.partyId == null ? null : new JSONObject().put("id", this.partyId).put("size", new JSONArray().put(this.partySize).put(this.partyMax))).put("secrets", new JSONObject().put("join", this.joinSecret).put("spectate", this.spectateSecret).put("match", this.matchSecret)).put("instance", this.instance);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Builder
/*     */   {
/*     */     private String state;
/*     */     
/*     */     private String details;
/*     */     
/*     */     private OffsetDateTime startTimestamp;
/*     */     
/*     */     private OffsetDateTime endTimestamp;
/*     */     
/*     */     private String largeImageKey;
/*     */     
/*     */     private String largeImageText;
/*     */     
/*     */     private String smallImageKey;
/*     */     
/*     */     private String smallImageText;
/*     */     
/*     */     private String partyId;
/*     */     
/*     */     private int partySize;
/*     */     
/*     */     private int partyMax;
/*     */     private String matchSecret;
/*     */     private String joinSecret;
/*     */     private String spectateSecret;
/*     */     private boolean instance;
/*     */     
/*     */     public RichPresence build()
/*     */     {
/* 132 */       return new RichPresence(this.state, this.details, this.startTimestamp, this.endTimestamp, 
/* 133 */         this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText, 
/* 134 */         this.partyId, this.partySize, this.partyMax, this.matchSecret, this.joinSecret, 
/* 135 */         this.spectateSecret, this.instance);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setState(String state)
/*     */     {
/* 147 */       this.state = state;
/* 148 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setDetails(String details)
/*     */     {
/* 160 */       this.details = details;
/* 161 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setStartTimestamp(OffsetDateTime startTimestamp)
/*     */     {
/* 173 */       this.startTimestamp = startTimestamp;
/* 174 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setEndTimestamp(OffsetDateTime endTimestamp)
/*     */     {
/* 186 */       this.endTimestamp = endTimestamp;
/* 187 */       return this;
/*     */     }
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
/*     */     public Builder setLargeImage(String largeImageKey, String largeImageText)
/*     */     {
/* 204 */       this.largeImageKey = largeImageKey;
/* 205 */       this.largeImageText = largeImageText;
/* 206 */       return this;
/*     */     }
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
/*     */     public Builder setLargeImage(String largeImageKey)
/*     */     {
/* 221 */       return setLargeImage(largeImageKey, null);
/*     */     }
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
/*     */     public Builder setSmallImage(String smallImageKey, String smallImageText)
/*     */     {
/* 238 */       this.smallImageKey = smallImageKey;
/* 239 */       this.smallImageText = smallImageText;
/* 240 */       return this;
/*     */     }
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
/*     */     public Builder setSmallImage(String smallImageKey)
/*     */     {
/* 255 */       return setSmallImage(smallImageKey, null);
/*     */     }
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
/*     */     public Builder setParty(String partyId, int partySize, int partyMax)
/*     */     {
/* 273 */       this.partyId = partyId;
/* 274 */       this.partySize = partySize;
/* 275 */       this.partyMax = partyMax;
/* 276 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setMatchSecret(String matchSecret)
/*     */     {
/* 288 */       this.matchSecret = matchSecret;
/* 289 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setJoinSecret(String joinSecret)
/*     */     {
/* 301 */       this.joinSecret = joinSecret;
/* 302 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setSpectateSecret(String spectateSecret)
/*     */     {
/* 314 */       this.spectateSecret = spectateSecret;
/* 315 */       return this;
/*     */     }
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
/*     */     public Builder setInstance(boolean instance)
/*     */     {
/* 329 */       this.instance = instance;
/* 330 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\entities\RichPresence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */