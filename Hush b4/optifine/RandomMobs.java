// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Map;

public class RandomMobs
{
    private static Map locationProperties;
    private static RenderGlobal renderGlobal;
    private static boolean initialized;
    private static Random random;
    private static boolean working;
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_PROPERTIES = ".properties";
    public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
    public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
    private static final String[] DEPENDANT_SUFFIXES;
    
    static {
        RandomMobs.locationProperties = new HashMap();
        RandomMobs.renderGlobal = null;
        RandomMobs.initialized = false;
        RandomMobs.random = new Random();
        RandomMobs.working = false;
        DEPENDANT_SUFFIXES = new String[] { "_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar" };
    }
    
    public static void entityLoaded(final Entity p_entityLoaded_0_, final World p_entityLoaded_1_) {
        if (p_entityLoaded_0_ instanceof EntityLiving && p_entityLoaded_1_ != null) {
            final EntityLiving entityliving = (EntityLiving)p_entityLoaded_0_;
            entityliving.spawnPosition = entityliving.getPosition();
            entityliving.spawnBiome = p_entityLoaded_1_.getBiomeGenForCoords(entityliving.spawnPosition);
            final WorldServer worldserver = Config.getWorldServer();
            if (worldserver != null) {
                final Entity entity = worldserver.getEntityByID(p_entityLoaded_0_.getEntityId());
                if (entity instanceof EntityLiving) {
                    final EntityLiving entityliving2 = (EntityLiving)entity;
                    final UUID uuid = entityliving2.getUniqueID();
                    final long i = uuid.getLeastSignificantBits();
                    final int j = (int)(i & 0x7FFFFFFFL);
                    entityliving.randomMobsId = j;
                }
            }
        }
    }
    
    public static void worldChanged(final World p_worldChanged_0_, final World p_worldChanged_1_) {
        if (p_worldChanged_1_ != null) {
            final List list = p_worldChanged_1_.getLoadedEntityList();
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                entityLoaded(entity, p_worldChanged_1_);
            }
        }
    }
    
    public static ResourceLocation getTextureLocation(final ResourceLocation p_getTextureLocation_0_) {
        if (RandomMobs.working) {
            return p_getTextureLocation_0_;
        }
        ResourceLocation entity2;
        try {
            RandomMobs.working = true;
            if (!RandomMobs.initialized) {
                initialize();
            }
            if (RandomMobs.renderGlobal != null) {
                final Entity entity1 = RandomMobs.renderGlobal.renderedEntity;
                if (!(entity1 instanceof EntityLiving)) {
                    final ResourceLocation resourcelocation2 = p_getTextureLocation_0_;
                    return resourcelocation2;
                }
                final EntityLiving entityliving = (EntityLiving)entity1;
                final String s = p_getTextureLocation_0_.getResourcePath();
                if (!s.startsWith("textures/entity/")) {
                    final ResourceLocation resourcelocation3 = p_getTextureLocation_0_;
                    return resourcelocation3;
                }
                final RandomMobsProperties randommobsproperties = getProperties(p_getTextureLocation_0_);
                if (randommobsproperties == null) {
                    final ResourceLocation resourcelocation4 = p_getTextureLocation_0_;
                    return resourcelocation4;
                }
                final ResourceLocation resourcelocation5 = randommobsproperties.getTextureLocation(p_getTextureLocation_0_, entityliving);
                return resourcelocation5;
            }
            else {
                entity2 = p_getTextureLocation_0_;
            }
        }
        finally {
            RandomMobs.working = false;
        }
        RandomMobs.working = false;
        return entity2;
    }
    
    private static RandomMobsProperties getProperties(final ResourceLocation p_getProperties_0_) {
        final String s = p_getProperties_0_.getResourcePath();
        RandomMobsProperties randommobsproperties = RandomMobs.locationProperties.get(s);
        if (randommobsproperties == null) {
            randommobsproperties = makeProperties(p_getProperties_0_);
            RandomMobs.locationProperties.put(s, randommobsproperties);
        }
        return randommobsproperties;
    }
    
    private static RandomMobsProperties makeProperties(final ResourceLocation p_makeProperties_0_) {
        final String s = p_makeProperties_0_.getResourcePath();
        final ResourceLocation resourcelocation = getPropertyLocation(p_makeProperties_0_);
        if (resourcelocation != null) {
            final RandomMobsProperties randommobsproperties = parseProperties(resourcelocation, p_makeProperties_0_);
            if (randommobsproperties != null) {
                return randommobsproperties;
            }
        }
        final ResourceLocation[] aresourcelocation = getTextureVariants(p_makeProperties_0_);
        return new RandomMobsProperties(s, aresourcelocation);
    }
    
    private static RandomMobsProperties parseProperties(final ResourceLocation p_parseProperties_0_, final ResourceLocation p_parseProperties_1_) {
        try {
            final String s = p_parseProperties_0_.getResourcePath();
            Config.dbg("RandomMobs: " + p_parseProperties_1_.getResourcePath() + ", variants: " + s);
            final InputStream inputstream = Config.getResourceStream(p_parseProperties_0_);
            if (inputstream == null) {
                Config.warn("RandomMobs properties not found: " + s);
                return null;
            }
            final Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            final RandomMobsProperties randommobsproperties = new RandomMobsProperties(properties, s, p_parseProperties_1_);
            return randommobsproperties.isValid(s) ? randommobsproperties : null;
        }
        catch (FileNotFoundException var6) {
            Config.warn("RandomMobs file not found: " + p_parseProperties_1_.getResourcePath());
            return null;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return null;
        }
    }
    
    private static ResourceLocation getPropertyLocation(final ResourceLocation p_getPropertyLocation_0_) {
        final ResourceLocation resourcelocation = getMcpatcherLocation(p_getPropertyLocation_0_);
        if (resourcelocation == null) {
            return null;
        }
        final String s = resourcelocation.getResourceDomain();
        String s3;
        final String s2 = s3 = resourcelocation.getResourcePath();
        if (s2.endsWith(".png")) {
            s3 = s2.substring(0, s2.length() - ".png".length());
        }
        final String s4 = String.valueOf(s3) + ".properties";
        final ResourceLocation resourcelocation2 = new ResourceLocation(s, s4);
        if (Config.hasResource(resourcelocation2)) {
            return resourcelocation2;
        }
        final String s5 = getParentPath(s3);
        if (s5 == null) {
            return null;
        }
        final ResourceLocation resourcelocation3 = new ResourceLocation(s, String.valueOf(s5) + ".properties");
        return Config.hasResource(resourcelocation3) ? resourcelocation3 : null;
    }
    
    public static ResourceLocation getMcpatcherLocation(final ResourceLocation p_getMcpatcherLocation_0_) {
        final String s = p_getMcpatcherLocation_0_.getResourcePath();
        if (!s.startsWith("textures/entity/")) {
            return null;
        }
        final String s2 = "mcpatcher/mob/" + s.substring("textures/entity/".length());
        return new ResourceLocation(p_getMcpatcherLocation_0_.getResourceDomain(), s2);
    }
    
    public static ResourceLocation getLocationIndexed(final ResourceLocation p_getLocationIndexed_0_, final int p_getLocationIndexed_1_) {
        if (p_getLocationIndexed_0_ == null) {
            return null;
        }
        final String s = p_getLocationIndexed_0_.getResourcePath();
        final int i = s.lastIndexOf(46);
        if (i < 0) {
            return null;
        }
        final String s2 = s.substring(0, i);
        final String s3 = s.substring(i);
        final String s4 = String.valueOf(s2) + p_getLocationIndexed_1_ + s3;
        final ResourceLocation resourcelocation = new ResourceLocation(p_getLocationIndexed_0_.getResourceDomain(), s4);
        return resourcelocation;
    }
    
    private static String getParentPath(final String p_getParentPath_0_) {
        for (int i = 0; i < RandomMobs.DEPENDANT_SUFFIXES.length; ++i) {
            final String s = RandomMobs.DEPENDANT_SUFFIXES[i];
            if (p_getParentPath_0_.endsWith(s)) {
                final String s2 = p_getParentPath_0_.substring(0, p_getParentPath_0_.length() - s.length());
                return s2;
            }
        }
        return null;
    }
    
    private static ResourceLocation[] getTextureVariants(final ResourceLocation p_getTextureVariants_0_) {
        final List list = new ArrayList();
        list.add(p_getTextureVariants_0_);
        final ResourceLocation resourcelocation = getMcpatcherLocation(p_getTextureVariants_0_);
        if (resourcelocation == null) {
            return null;
        }
        for (int i = 1; i < list.size() + 10; ++i) {
            final int j = i + 1;
            final ResourceLocation resourcelocation2 = getLocationIndexed(resourcelocation, j);
            if (Config.hasResource(resourcelocation2)) {
                list.add(resourcelocation2);
            }
        }
        if (list.size() <= 1) {
            return null;
        }
        final ResourceLocation[] aresourcelocation = list.toArray(new ResourceLocation[list.size()]);
        Config.dbg("RandomMobs: " + p_getTextureVariants_0_.getResourcePath() + ", variants: " + aresourcelocation.length);
        return aresourcelocation;
    }
    
    public static void resetTextures() {
        RandomMobs.locationProperties.clear();
        if (Config.isRandomMobs()) {
            initialize();
        }
    }
    
    private static void initialize() {
        RandomMobs.renderGlobal = Config.getRenderGlobal();
        if (RandomMobs.renderGlobal != null) {
            RandomMobs.initialized = true;
            final List list = new ArrayList();
            list.add("bat");
            list.add("blaze");
            list.add("cat/black");
            list.add("cat/ocelot");
            list.add("cat/red");
            list.add("cat/siamese");
            list.add("chicken");
            list.add("cow/cow");
            list.add("cow/mooshroom");
            list.add("creeper/creeper");
            list.add("enderman/enderman");
            list.add("enderman/enderman_eyes");
            list.add("ghast/ghast");
            list.add("ghast/ghast_shooting");
            list.add("iron_golem");
            list.add("pig/pig");
            list.add("sheep/sheep");
            list.add("sheep/sheep_fur");
            list.add("silverfish");
            list.add("skeleton/skeleton");
            list.add("skeleton/wither_skeleton");
            list.add("slime/slime");
            list.add("slime/magmacube");
            list.add("snowman");
            list.add("spider/cave_spider");
            list.add("spider/spider");
            list.add("spider_eyes");
            list.add("squid");
            list.add("villager/villager");
            list.add("villager/butcher");
            list.add("villager/farmer");
            list.add("villager/librarian");
            list.add("villager/priest");
            list.add("villager/smith");
            list.add("wither/wither");
            list.add("wither/wither_armor");
            list.add("wither/wither_invulnerable");
            list.add("wolf/wolf");
            list.add("wolf/wolf_angry");
            list.add("wolf/wolf_collar");
            list.add("wolf/wolf_tame");
            list.add("zombie_pigman");
            list.add("zombie/zombie");
            list.add("zombie/zombie_villager");
            for (int i = 0; i < list.size(); ++i) {
                final String s = list.get(i);
                final String s2 = "textures/entity/" + s + ".png";
                final ResourceLocation resourcelocation = new ResourceLocation(s2);
                if (!Config.hasResource(resourcelocation)) {
                    Config.warn("Not found: " + resourcelocation);
                }
                getProperties(resourcelocation);
            }
        }
    }
}
