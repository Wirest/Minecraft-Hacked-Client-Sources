/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S37PacketStatistics;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ import net.minecraft.util.TupleIntJsonSerializable;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class StatisticsFile extends StatFileWriter
/*     */ {
/*  28 */   private static final Logger logger = ;
/*     */   private final MinecraftServer mcServer;
/*     */   private final File statsFile;
/*  31 */   private final Set<StatBase> field_150888_e = Sets.newHashSet();
/*  32 */   private int field_150885_f = 65236;
/*  33 */   private boolean field_150886_g = false;
/*     */   
/*     */   public StatisticsFile(MinecraftServer serverIn, File statsFileIn)
/*     */   {
/*  37 */     this.mcServer = serverIn;
/*  38 */     this.statsFile = statsFileIn;
/*     */   }
/*     */   
/*     */   public void readStatFile()
/*     */   {
/*  43 */     if (this.statsFile.isFile())
/*     */     {
/*     */       try
/*     */       {
/*  47 */         this.statsData.clear();
/*  48 */         this.statsData.putAll(parseJson(FileUtils.readFileToString(this.statsFile)));
/*     */       }
/*     */       catch (IOException ioexception)
/*     */       {
/*  52 */         logger.error("Couldn't read statistics file " + this.statsFile, ioexception);
/*     */       }
/*     */       catch (com.google.gson.JsonParseException jsonparseexception)
/*     */       {
/*  56 */         logger.error("Couldn't parse statistics file " + this.statsFile, jsonparseexception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveStatFile()
/*     */   {
/*     */     try
/*     */     {
/*  65 */       FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/*  69 */       logger.error("Couldn't save stats", ioexception);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
/*     */   {
/*  78 */     int i = statIn.isAchievement() ? readStat(statIn) : 0;
/*  79 */     super.unlockAchievement(playerIn, statIn, p_150873_3_);
/*  80 */     this.field_150888_e.add(statIn);
/*     */     
/*  82 */     if ((statIn.isAchievement()) && (i == 0) && (p_150873_3_ > 0))
/*     */     {
/*  84 */       this.field_150886_g = true;
/*     */       
/*  86 */       if (this.mcServer.isAnnouncingPlayerAchievements())
/*     */       {
/*  88 */         this.mcServer.getConfigurationManager().sendChatMsg(new net.minecraft.util.ChatComponentTranslation("chat.type.achievement", new Object[] { playerIn.getDisplayName(), statIn.func_150955_j() }));
/*     */       }
/*     */     }
/*     */     
/*  92 */     if ((statIn.isAchievement()) && (i > 0) && (p_150873_3_ == 0))
/*     */     {
/*  94 */       this.field_150886_g = true;
/*     */       
/*  96 */       if (this.mcServer.isAnnouncingPlayerAchievements())
/*     */       {
/*  98 */         this.mcServer.getConfigurationManager().sendChatMsg(new net.minecraft.util.ChatComponentTranslation("chat.type.achievement.taken", new Object[] { playerIn.getDisplayName(), statIn.func_150955_j() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<StatBase> func_150878_c()
/*     */   {
/* 105 */     Set<StatBase> set = Sets.newHashSet(this.field_150888_e);
/* 106 */     this.field_150888_e.clear();
/* 107 */     this.field_150886_g = false;
/* 108 */     return set;
/*     */   }
/*     */   
/*     */   public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_)
/*     */   {
/* 113 */     JsonElement jsonelement = new JsonParser().parse(p_150881_1_);
/*     */     
/* 115 */     if (!jsonelement.isJsonObject())
/*     */     {
/* 117 */       return Maps.newHashMap();
/*     */     }
/*     */     
/*     */ 
/* 121 */     JsonObject jsonobject = jsonelement.getAsJsonObject();
/* 122 */     Map<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();
/*     */     
/* 124 */     for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*     */     {
/* 126 */       StatBase statbase = StatList.getOneShotStat((String)entry.getKey());
/*     */       
/* 128 */       if (statbase != null)
/*     */       {
/* 130 */         TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
/*     */         
/* 132 */         if ((((JsonElement)entry.getValue()).isJsonPrimitive()) && (((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()))
/*     */         {
/* 134 */           tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
/*     */         }
/* 136 */         else if (((JsonElement)entry.getValue()).isJsonObject())
/*     */         {
/* 138 */           JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
/*     */           
/* 140 */           if ((jsonobject1.has("value")) && (jsonobject1.get("value").isJsonPrimitive()) && (jsonobject1.get("value").getAsJsonPrimitive().isNumber()))
/*     */           {
/* 142 */             tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
/*     */           }
/*     */           
/* 145 */           if ((jsonobject1.has("progress")) && (statbase.func_150954_l() != null))
/*     */           {
/*     */             try
/*     */             {
/* 149 */               Constructor<? extends IJsonSerializable> constructor = statbase.func_150954_l().getConstructor(new Class[0]);
/* 150 */               IJsonSerializable ijsonserializable = (IJsonSerializable)constructor.newInstance(new Object[0]);
/* 151 */               ijsonserializable.fromJson(jsonobject1.get("progress"));
/* 152 */               tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
/*     */             }
/*     */             catch (Throwable throwable)
/*     */             {
/* 156 */               logger.warn("Invalid statistic progress in " + this.statsFile, throwable);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 161 */         map.put(statbase, tupleintjsonserializable);
/*     */       }
/*     */       else
/*     */       {
/* 165 */         logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + (String)entry.getKey() + " is");
/*     */       }
/*     */     }
/*     */     
/* 169 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_)
/*     */   {
/* 175 */     JsonObject jsonobject = new JsonObject();
/*     */     
/* 177 */     for (Map.Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet())
/*     */     {
/* 179 */       if (((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue() != null)
/*     */       {
/* 181 */         JsonObject jsonobject1 = new JsonObject();
/* 182 */         jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
/*     */         
/*     */         try
/*     */         {
/* 186 */           jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue().getSerializableElement());
/*     */         }
/*     */         catch (Throwable throwable)
/*     */         {
/* 190 */           logger.warn("Couldn't save statistic " + ((StatBase)entry.getKey()).getStatName() + ": error serializing progress", throwable);
/*     */         }
/*     */         
/* 193 */         jsonobject.add(((StatBase)entry.getKey()).statId, jsonobject1);
/*     */       }
/*     */       else
/*     */       {
/* 197 */         jsonobject.addProperty(((StatBase)entry.getKey()).statId, Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
/*     */       }
/*     */     }
/*     */     
/* 201 */     return jsonobject.toString();
/*     */   }
/*     */   
/*     */   public void func_150877_d()
/*     */   {
/* 206 */     for (StatBase statbase : this.statsData.keySet())
/*     */     {
/* 208 */       this.field_150888_e.add(statbase);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_150876_a(EntityPlayerMP p_150876_1_)
/*     */   {
/* 214 */     int i = this.mcServer.getTickCounter();
/* 215 */     Map<StatBase, Integer> map = Maps.newHashMap();
/*     */     
/* 217 */     if ((this.field_150886_g) || (i - this.field_150885_f > 300))
/*     */     {
/* 219 */       this.field_150885_f = i;
/*     */       
/* 221 */       for (StatBase statbase : func_150878_c())
/*     */       {
/* 223 */         map.put(statbase, Integer.valueOf(readStat(statbase)));
/*     */       }
/*     */     }
/*     */     
/* 227 */     p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
/*     */   }
/*     */   
/*     */   public void sendAchievements(EntityPlayerMP player)
/*     */   {
/* 232 */     Map<StatBase, Integer> map = Maps.newHashMap();
/*     */     
/* 234 */     for (Achievement achievement : AchievementList.achievementList)
/*     */     {
/* 236 */       if (hasAchievementUnlocked(achievement))
/*     */       {
/* 238 */         map.put(achievement, Integer.valueOf(readStat(achievement)));
/* 239 */         this.field_150888_e.remove(achievement);
/*     */       }
/*     */     }
/*     */     
/* 243 */     player.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
/*     */   }
/*     */   
/*     */   public boolean func_150879_e()
/*     */   {
/* 248 */     return this.field_150886_g;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\stats\StatisticsFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */