package optifine;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RandomMobs {
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_PROPERTIES = ".properties";
    public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
    public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
    private static final String[] DEPENDANT_SUFFIXES = {"_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar"};
    private static Map locationProperties = new HashMap();
    private static RenderGlobal renderGlobal = null;
    private static boolean initialized = false;
    private static Random random = new Random();
    private static boolean working = false;

    public static void entityLoaded(Entity paramEntity, World paramWorld) {
        if (((paramEntity instanceof EntityLiving)) && (paramWorld != null)) {
            EntityLiving localEntityLiving1 = (EntityLiving) paramEntity;
            localEntityLiving1.spawnPosition = localEntityLiving1.getPosition();
            localEntityLiving1.spawnBiome = paramWorld.getBiomeGenForCoords(localEntityLiving1.spawnPosition);
            WorldServer localWorldServer = Config.getWorldServer();
            if (localWorldServer != null) {
                Entity localEntity = localWorldServer.getEntityByID(paramEntity.getEntityId());
                if ((localEntity instanceof EntityLiving)) {
                    EntityLiving localEntityLiving2 = (EntityLiving) localEntity;
                    UUID localUUID = localEntityLiving2.getUniqueID();
                    long l = localUUID.getLeastSignificantBits();
                    int i = (int) (l & 0x7FFFFFFF);
                    localEntityLiving1.randomMobsId = i;
                }
            }
        }
    }

    public static void worldChanged(World paramWorld1, World paramWorld2) {
        if (paramWorld2 != null) {
            List localList = paramWorld2.getLoadedEntityList();
            for (int i = 0; i < localList.size(); i++) {
                Entity localEntity = (Entity) localList.get(i);
                entityLoaded(localEntity, paramWorld2);
            }
        }
    }

    public static ResourceLocation getTextureLocation(ResourceLocation paramResourceLocation) {
        if (working) {
            return paramResourceLocation;
        }
        ResourceLocation localResourceLocation;
        try {
            working = true;
            if (!initialized) {
                initialize();
            }
            if (renderGlobal != null) {
                Entity localEntity = renderGlobal.renderedEntity;
                if (!(localEntity instanceof EntityLiving)) {
                    localObject1 = paramResourceLocation;
                    localObject2 = localObject1;
                    return (ResourceLocation) localObject2;
                }
                Object localObject1 = (EntityLiving) localEntity;
                Object localObject2 = paramResourceLocation.getResourcePath();
                if (!((String) localObject2).startsWith("textures/entity/")) {
                    localObject3 = paramResourceLocation;
                    localObject4 = localObject3;
                    return (ResourceLocation) localObject4;
                }
                Object localObject3 = getProperties(paramResourceLocation);
                if (localObject3 == null) {
                    localObject4 = paramResourceLocation;
                    localObject5 = localObject4;
                    return (ResourceLocation) localObject5;
                }
                Object localObject4 = ((RandomMobsProperties) localObject3).getTextureLocation(paramResourceLocation, (EntityLiving) localObject1);
                Object localObject5 = localObject4;
                return (ResourceLocation) localObject5;
            }
            localResourceLocation = paramResourceLocation;
        } finally {
            working = false;
        }
        return localResourceLocation;
    }

    private static RandomMobsProperties getProperties(ResourceLocation paramResourceLocation) {
        String str = paramResourceLocation.getResourcePath();
        RandomMobsProperties localRandomMobsProperties = (RandomMobsProperties) locationProperties.get(str);
        if (localRandomMobsProperties == null) {
            localRandomMobsProperties = makeProperties(paramResourceLocation);
            locationProperties.put(str, localRandomMobsProperties);
        }
        return localRandomMobsProperties;
    }

    private static RandomMobsProperties makeProperties(ResourceLocation paramResourceLocation) {
        String str = paramResourceLocation.getResourcePath();
        ResourceLocation localResourceLocation = getPropertyLocation(paramResourceLocation);
        if (localResourceLocation != null) {
            localObject = parseProperties(localResourceLocation, paramResourceLocation);
            if (localObject != null) {
                return (RandomMobsProperties) localObject;
            }
        }
        Object localObject = getTextureVariants(paramResourceLocation);
        return new RandomMobsProperties(str, (ResourceLocation[]) localObject);
    }

    private static RandomMobsProperties parseProperties(ResourceLocation paramResourceLocation1, ResourceLocation paramResourceLocation2) {
        try {
            String str = paramResourceLocation1.getResourcePath();
            Config.dbg("RandomMobs: " + paramResourceLocation2.getResourcePath() + ", variants: " + str);
            InputStream localInputStream = Config.getResourceStream(paramResourceLocation1);
            if (localInputStream == null) {
                Config.warn("RandomMobs properties not found: " + str);
                return null;
            }
            Properties localProperties = new Properties();
            localProperties.load(localInputStream);
            localInputStream.close();
            RandomMobsProperties localRandomMobsProperties = new RandomMobsProperties(localProperties, str, paramResourceLocation2);
            return !localRandomMobsProperties.isValid(str) ? null : localRandomMobsProperties;
        } catch (FileNotFoundException localFileNotFoundException) {
            Config.warn("RandomMobs file not found: " + paramResourceLocation2.getResourcePath());
            return null;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return null;
    }

    private static ResourceLocation getPropertyLocation(ResourceLocation paramResourceLocation) {
        ResourceLocation localResourceLocation1 = getMcpatcherLocation(paramResourceLocation);
        if (localResourceLocation1 == null) {
            return null;
        }
        String str1 = localResourceLocation1.getResourceDomain();
        String str2 = localResourceLocation1.getResourcePath();
        String str3 = str2;
        if (str2.endsWith(".png")) {
            str3 = str2.substring(0, str2.length() - ".png".length());
        }
        String str4 = str3 + ".properties";
        ResourceLocation localResourceLocation2 = new ResourceLocation(str1, str4);
        if (Config.hasResource(localResourceLocation2)) {
            return localResourceLocation2;
        }
        String str5 = getParentPath(str3);
        if (str5 == null) {
            return null;
        }
        ResourceLocation localResourceLocation3 = new ResourceLocation(str1, str5 + ".properties");
        return Config.hasResource(localResourceLocation3) ? localResourceLocation3 : null;
    }

    public static ResourceLocation getMcpatcherLocation(ResourceLocation paramResourceLocation) {
        String str1 = paramResourceLocation.getResourcePath();
        if (!str1.startsWith("textures/entity/")) {
            return null;
        }
        String str2 = "mcpatcher/mob/" + str1.substring("textures/entity/".length());
        return new ResourceLocation(paramResourceLocation.getResourceDomain(), str2);
    }

    public static ResourceLocation getLocationIndexed(ResourceLocation paramResourceLocation, int paramInt) {
        if (paramResourceLocation == null) {
            return null;
        }
        String str1 = paramResourceLocation.getResourcePath();
        int i = str1.lastIndexOf('.');
        if (i < 0) {
            return null;
        }
        String str2 = str1.substring(0, i);
        String str3 = str1.substring(i);
        String str4 = str2 + paramInt + str3;
        ResourceLocation localResourceLocation = new ResourceLocation(paramResourceLocation.getResourceDomain(), str4);
        return localResourceLocation;
    }

    private static String getParentPath(String paramString) {
        for (int i = 0; i < DEPENDANT_SUFFIXES.length; i++) {
            String str1 = DEPENDANT_SUFFIXES[i];
            if (paramString.endsWith(str1)) {
                String str2 = paramString.substring(0, paramString.length() - str1.length());
                return str2;
            }
        }
        return null;
    }

    private static ResourceLocation[] getTextureVariants(ResourceLocation paramResourceLocation) {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(paramResourceLocation);
        ResourceLocation localResourceLocation1 = getMcpatcherLocation(paramResourceLocation);
        if (localResourceLocation1 == null) {
            return null;
        }
        for (int i = 1; i < (localArrayList.size() | 0xA); i++) {
            int j = i | 0x1;
            ResourceLocation localResourceLocation2 = getLocationIndexed(localResourceLocation1, j);
            if (Config.hasResource(localResourceLocation2)) {
                localArrayList.add(localResourceLocation2);
            }
        }
        if (localArrayList.size() <= 1) {
            return null;
        }
        ResourceLocation[] arrayOfResourceLocation = (ResourceLocation[]) (ResourceLocation[]) localArrayList.toArray(new ResourceLocation[localArrayList.size()]);
        Config.dbg("RandomMobs: " + paramResourceLocation.getResourcePath() + ", variants: " + arrayOfResourceLocation.length);
        return arrayOfResourceLocation;
    }

    public static void resetTextures() {
        locationProperties.clear();
        if (Config.isRandomMobs()) {
            initialize();
        }
    }

    private static void initialize() {
        renderGlobal = Config.getRenderGlobal();
        if (renderGlobal != null) {
            initialized = true;
            ArrayList localArrayList = new ArrayList();
            localArrayList.add("bat");
            localArrayList.add("blaze");
            localArrayList.add("cat/black");
            localArrayList.add("cat/ocelot");
            localArrayList.add("cat/red");
            localArrayList.add("cat/siamese");
            localArrayList.add("chicken");
            localArrayList.add("cow/cow");
            localArrayList.add("cow/mooshroom");
            localArrayList.add("creeper/creeper");
            localArrayList.add("enderman/enderman");
            localArrayList.add("enderman/enderman_eyes");
            localArrayList.add("ghast/ghast");
            localArrayList.add("ghast/ghast_shooting");
            localArrayList.add("iron_golem");
            localArrayList.add("pig/pig");
            localArrayList.add("sheep/sheep");
            localArrayList.add("sheep/sheep_fur");
            localArrayList.add("silverfish");
            localArrayList.add("skeleton/skeleton");
            localArrayList.add("skeleton/wither_skeleton");
            localArrayList.add("slime/slime");
            localArrayList.add("slime/magmacube");
            localArrayList.add("snowman");
            localArrayList.add("spider/cave_spider");
            localArrayList.add("spider/spider");
            localArrayList.add("spider_eyes");
            localArrayList.add("squid");
            localArrayList.add("villager/villager");
            localArrayList.add("villager/butcher");
            localArrayList.add("villager/farmer");
            localArrayList.add("villager/librarian");
            localArrayList.add("villager/priest");
            localArrayList.add("villager/smith");
            localArrayList.add("wither/wither");
            localArrayList.add("wither/wither_armor");
            localArrayList.add("wither/wither_invulnerable");
            localArrayList.add("wolf/wolf");
            localArrayList.add("wolf/wolf_angry");
            localArrayList.add("wolf/wolf_collar");
            localArrayList.add("wolf/wolf_tame");
            localArrayList.add("zombie_pigman");
            localArrayList.add("zombie/zombie");
            localArrayList.add("zombie/zombie_villager");
            for (int i = 0; i < localArrayList.size(); i++) {
                String str1 = (String) localArrayList.get(i);
                String str2 = "textures/entity/" + str1 + ".png";
                ResourceLocation localResourceLocation = new ResourceLocation(str2);
                if (!Config.hasResource(localResourceLocation)) {
                    Config.warn("Not found: " + localResourceLocation);
                }
                getProperties(localResourceLocation);
            }
        }
    }
}




