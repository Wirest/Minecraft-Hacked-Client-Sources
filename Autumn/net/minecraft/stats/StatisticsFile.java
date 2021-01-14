package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsFile extends StatFileWriter {
   private static final Logger logger = LogManager.getLogger();
   private final MinecraftServer mcServer;
   private final File statsFile;
   private final Set field_150888_e = Sets.newHashSet();
   private int field_150885_f = -300;
   private boolean field_150886_g = false;

   public StatisticsFile(MinecraftServer serverIn, File statsFileIn) {
      this.mcServer = serverIn;
      this.statsFile = statsFileIn;
   }

   public void readStatFile() {
      if (this.statsFile.isFile()) {
         try {
            this.statsData.clear();
            this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
         } catch (IOException var2) {
            logger.error("Couldn't read statistics file " + this.statsFile, var2);
         } catch (JsonParseException var3) {
            logger.error("Couldn't parse statistics file " + this.statsFile, var3);
         }
      }

   }

   public void saveStatFile() {
      try {
         FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
      } catch (IOException var2) {
         logger.error("Couldn't save stats", var2);
      }

   }

   public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
      int i = statIn.isAchievement() ? this.readStat(statIn) : 0;
      super.unlockAchievement(playerIn, statIn, p_150873_3_);
      this.field_150888_e.add(statIn);
      if (statIn.isAchievement() && i == 0 && p_150873_3_ > 0) {
         this.field_150886_g = true;
         if (this.mcServer.isAnnouncingPlayerAchievements()) {
            this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", new Object[]{playerIn.getDisplayName(), statIn.func_150955_j()}));
         }
      }

      if (statIn.isAchievement() && i > 0 && p_150873_3_ == 0) {
         this.field_150886_g = true;
         if (this.mcServer.isAnnouncingPlayerAchievements()) {
            this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", new Object[]{playerIn.getDisplayName(), statIn.func_150955_j()}));
         }
      }

   }

   public Set func_150878_c() {
      Set set = Sets.newHashSet(this.field_150888_e);
      this.field_150888_e.clear();
      this.field_150886_g = false;
      return set;
   }

   public Map parseJson(String p_150881_1_) {
      JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);
      if (!jsonelement.isJsonObject()) {
         return Maps.newHashMap();
      } else {
         JsonObject jsonobject = jsonelement.getAsJsonObject();
         Map map = Maps.newHashMap();
         Iterator var5 = jsonobject.entrySet().iterator();

         while(true) {
            while(var5.hasNext()) {
               Entry entry = (Entry)var5.next();
               StatBase statbase = StatList.getOneShotStat((String)entry.getKey());
               if (statbase != null) {
                  TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
                  if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()) {
                     tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
                  } else if (((JsonElement)entry.getValue()).isJsonObject()) {
                     JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
                     if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                        tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                     }

                     if (jsonobject1.has("progress") && statbase.func_150954_l() != null) {
                        try {
                           Constructor constructor = statbase.func_150954_l().getConstructor();
                           IJsonSerializable ijsonserializable = (IJsonSerializable)constructor.newInstance();
                           ijsonserializable.fromJson(jsonobject1.get("progress"));
                           tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                        } catch (Throwable var12) {
                           logger.warn("Invalid statistic progress in " + this.statsFile, var12);
                        }
                     }
                  }

                  map.put(statbase, tupleintjsonserializable);
               } else {
                  logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + (String)entry.getKey() + " is");
               }
            }

            return map;
         }
      }
   }

   public static String dumpJson(Map p_150880_0_) {
      JsonObject jsonobject = new JsonObject();
      Iterator var2 = p_150880_0_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         if (((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue() != null) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("value", ((TupleIntJsonSerializable)entry.getValue()).getIntegerValue());

            try {
               jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue().getSerializableElement());
            } catch (Throwable var6) {
               logger.warn("Couldn't save statistic " + ((StatBase)entry.getKey()).getStatName() + ": error serializing progress", var6);
            }

            jsonobject.add(((StatBase)entry.getKey()).statId, jsonobject1);
         } else {
            jsonobject.addProperty(((StatBase)entry.getKey()).statId, ((TupleIntJsonSerializable)entry.getValue()).getIntegerValue());
         }
      }

      return jsonobject.toString();
   }

   public void func_150877_d() {
      Iterator var1 = this.statsData.keySet().iterator();

      while(var1.hasNext()) {
         StatBase statbase = (StatBase)var1.next();
         this.field_150888_e.add(statbase);
      }

   }

   public void func_150876_a(EntityPlayerMP p_150876_1_) {
      int i = this.mcServer.getTickCounter();
      Map map = Maps.newHashMap();
      if (this.field_150886_g || i - this.field_150885_f > 300) {
         this.field_150885_f = i;
         Iterator var4 = this.func_150878_c().iterator();

         while(var4.hasNext()) {
            StatBase statbase = (StatBase)var4.next();
            map.put(statbase, this.readStat(statbase));
         }
      }

      p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
   }

   public void sendAchievements(EntityPlayerMP player) {
      Map map = Maps.newHashMap();
      Iterator var3 = AchievementList.achievementList.iterator();

      while(var3.hasNext()) {
         Achievement achievement = (Achievement)var3.next();
         if (this.hasAchievementUnlocked(achievement)) {
            map.put(achievement, this.readStat(achievement));
            this.field_150888_e.remove(achievement);
         }
      }

      player.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
   }

   public boolean func_150879_e() {
      return this.field_150886_g;
   }
}
