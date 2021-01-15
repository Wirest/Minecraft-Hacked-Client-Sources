package net.minecraft.stats;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsFile extends StatFileWriter
{
    private static final Logger logger = LogManager.getLogger();
    private final MinecraftServer mcServer;
    private final File statsFile;
    private final Set field_150888_e = Sets.newHashSet();
    private int field_150885_f = -300;
    private boolean field_150886_g = false;

    public StatisticsFile(MinecraftServer serverIn, File statsFileIn)
    {
        this.mcServer = serverIn;
        this.statsFile = statsFileIn;
    }

    public void readStatFile()
    {
        if (this.statsFile.isFile())
        {
            try
            {
                this.statsData.clear();
                this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
            }
            catch (IOException var2)
            {
                logger.error("Couldn\'t read statistics file " + this.statsFile, var2);
            }
            catch (JsonParseException var3)
            {
                logger.error("Couldn\'t parse statistics file " + this.statsFile, var3);
            }
        }
    }

    public void saveStatFile()
    {
        try
        {
            FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
        }
        catch (IOException var2)
        {
            logger.error("Couldn\'t save stats", var2);
        }
    }

    @Override
	public void func_150873_a(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        int var4 = statIn.isAchievement() ? this.readStat(statIn) : 0;
        super.func_150873_a(playerIn, statIn, p_150873_3_);
        this.field_150888_e.add(statIn);

        if (statIn.isAchievement() && var4 == 0 && p_150873_3_ > 0)
        {
            this.field_150886_g = true;

            if (this.mcServer.isAnnouncingPlayerAchievements())
            {
                this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", new Object[] {playerIn.getDisplayName(), statIn.func_150955_j()}));
            }
        }

        if (statIn.isAchievement() && var4 > 0 && p_150873_3_ == 0)
        {
            this.field_150886_g = true;

            if (this.mcServer.isAnnouncingPlayerAchievements())
            {
                this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", new Object[] {playerIn.getDisplayName(), statIn.func_150955_j()}));
            }
        }
    }

    public Set func_150878_c()
    {
        HashSet var1 = Sets.newHashSet(this.field_150888_e);
        this.field_150888_e.clear();
        this.field_150886_g = false;
        return var1;
    }

    public Map parseJson(String p_150881_1_)
    {
        JsonElement var2 = (new JsonParser()).parse(p_150881_1_);

        if (!var2.isJsonObject())
        {
            return Maps.newHashMap();
        }
        else
        {
            JsonObject var3 = var2.getAsJsonObject();
            HashMap var4 = Maps.newHashMap();
            Iterator var5 = var3.entrySet().iterator();

            while (var5.hasNext())
            {
                Entry var6 = (Entry)var5.next();
                StatBase var7 = StatList.getOneShotStat((String)var6.getKey());

                if (var7 != null)
                {
                    TupleIntJsonSerializable var8 = new TupleIntJsonSerializable();

                    if (((JsonElement)var6.getValue()).isJsonPrimitive() && ((JsonElement)var6.getValue()).getAsJsonPrimitive().isNumber())
                    {
                        var8.setIntegerValue(((JsonElement)var6.getValue()).getAsInt());
                    }
                    else if (((JsonElement)var6.getValue()).isJsonObject())
                    {
                        JsonObject var9 = ((JsonElement)var6.getValue()).getAsJsonObject();

                        if (var9.has("value") && var9.get("value").isJsonPrimitive() && var9.get("value").getAsJsonPrimitive().isNumber())
                        {
                            var8.setIntegerValue(var9.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (var9.has("progress") && var7.func_150954_l() != null)
                        {
                            try
                            {
                                Constructor var10 = var7.func_150954_l().getConstructor(new Class[0]);
                                IJsonSerializable var11 = (IJsonSerializable)var10.newInstance(new Object[0]);
                                var11.func_152753_a(var9.get("progress"));
                                var8.setJsonSerializableValue(var11);
                            }
                            catch (Throwable var12)
                            {
                                logger.warn("Invalid statistic progress in " + this.statsFile, var12);
                            }
                        }
                    }

                    var4.put(var7, var8);
                }
                else
                {
                    logger.warn("Invalid statistic in " + this.statsFile + ": Don\'t know what " + (String)var6.getKey() + " is");
                }
            }

            return var4;
        }
    }

    public static String dumpJson(Map p_150880_0_)
    {
        JsonObject var1 = new JsonObject();
        Iterator var2 = p_150880_0_.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (((TupleIntJsonSerializable)var3.getValue()).getJsonSerializableValue() != null)
            {
                JsonObject var4 = new JsonObject();
                var4.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable)var3.getValue()).getIntegerValue()));

                try
                {
                    var4.add("progress", ((TupleIntJsonSerializable)var3.getValue()).getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable var6)
                {
                    logger.warn("Couldn\'t save statistic " + ((StatBase)var3.getKey()).getStatName() + ": error serializing progress", var6);
                }

                var1.add(((StatBase)var3.getKey()).statId, var4);
            }
            else
            {
                var1.addProperty(((StatBase)var3.getKey()).statId, Integer.valueOf(((TupleIntJsonSerializable)var3.getValue()).getIntegerValue()));
            }
        }

        return var1.toString();
    }

    public void func_150877_d()
    {
        Iterator var1 = this.statsData.keySet().iterator();

        while (var1.hasNext())
        {
            StatBase var2 = (StatBase)var1.next();
            this.field_150888_e.add(var2);
        }
    }

    public void func_150876_a(EntityPlayerMP p_150876_1_)
    {
        int var2 = this.mcServer.getTickCounter();
        HashMap var3 = Maps.newHashMap();

        if (this.field_150886_g || var2 - this.field_150885_f > 300)
        {
            this.field_150885_f = var2;
            Iterator var4 = this.func_150878_c().iterator();

            while (var4.hasNext())
            {
                StatBase var5 = (StatBase)var4.next();
                var3.put(var5, Integer.valueOf(this.readStat(var5)));
            }
        }

        p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var3));
    }

    public void func_150884_b(EntityPlayerMP p_150884_1_)
    {
        HashMap var2 = Maps.newHashMap();
        Iterator var3 = AchievementList.achievementList.iterator();

        while (var3.hasNext())
        {
            Achievement var4 = (Achievement)var3.next();

            if (this.hasAchievementUnlocked(var4))
            {
                var2.put(var4, Integer.valueOf(this.readStat(var4)));
                this.field_150888_e.remove(var4);
            }
        }

        p_150884_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var2));
    }

    public boolean func_150879_e()
    {
        return this.field_150886_g;
    }
}
