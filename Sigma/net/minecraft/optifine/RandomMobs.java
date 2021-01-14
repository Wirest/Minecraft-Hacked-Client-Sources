package net.minecraft.optifine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class RandomMobs {
    private static Map textureVariantsMap = new HashMap();
    private static RenderGlobal renderGlobal = null;
    private static boolean initialized = false;
    private static Random random = new Random();
    private static Field fieldEntityUuid = RandomMobs.getField(Entity.class, UUID.class);
    private static boolean working = false;

    public static void entityLoaded(Entity entity) {
        if (entity instanceof EntityLiving) {
            EntityLiving el = (EntityLiving) entity;
            WorldServer ws = Config.getWorldServer();

            if (ws != null) {
                Entity es = ws.getEntityByID(entity.getEntityId());

                if (es instanceof EntityLiving) {
                    EntityLiving els = (EntityLiving) es;

                    if (RandomMobs.fieldEntityUuid != null) {
                        try {
                            Object e = RandomMobs.fieldEntityUuid.get(els);
                            RandomMobs.fieldEntityUuid.set(el, e);
                        } catch (Exception var6) {
                            var6.printStackTrace();
                            RandomMobs.fieldEntityUuid = null;
                        }
                    }
                }
            }
        }
    }

    private static Field getField(Class cls, Class fieldType) {
        try {
            Field[] e = cls.getDeclaredFields();

            for (Field field : e) {
                Class type = field.getType();

                if (type == fieldType) {
                    field.setAccessible(true);
                    return field;
                }
            }

            return null;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void worldChanged(World oldWorld, World newWorld) {
        if (newWorld != null) {
            List entityList = newWorld.getLoadedEntityList();

            for (int e = 0; e < entityList.size(); ++e) {
                Entity entity = (Entity) entityList.get(e);
                RandomMobs.entityLoaded(entity);
            }
        }
    }

    public static ResourceLocation getTextureLocation(ResourceLocation loc) {
        if (RandomMobs.working) {
            return loc;
        } else {
            ResourceLocation var6;

            try {
                RandomMobs.working = true;

                if (!RandomMobs.initialized) {
                    RandomMobs.initialize();
                }

                if (RandomMobs.renderGlobal == null) {
                    ResourceLocation entity1 = loc;
                    return entity1;
                }

                Entity entity = RandomMobs.renderGlobal.renderedEntity;
                ResourceLocation name1;

                if (entity == null) {
                    name1 = loc;
                    return name1;
                }

                if (!(entity instanceof EntityLiving)) {
                    name1 = loc;
                    return name1;
                }

                String name = loc.getResourcePath();

                if (!name.startsWith("textures/entity/")) {
                    ResourceLocation uuidLow1 = loc;
                    return uuidLow1;
                }

                long uuidLow = entity.getUniqueID().getLeastSignificantBits();
                int id = (int) (uuidLow & 2147483647L);
                var6 = RandomMobs.getTextureLocation(loc, id);
            } finally {
                RandomMobs.working = false;
            }

            return var6;
        }
    }

    private static ResourceLocation getTextureLocation(ResourceLocation loc, int randomId) {
        if (randomId <= 0) {
            return loc;
        } else {
            String name = loc.getResourcePath();
            ResourceLocation[] texLocs = ((ResourceLocation[]) RandomMobs.textureVariantsMap.get(name));

            if (texLocs == null) {
                texLocs = RandomMobs.getTextureVariants(loc);
                RandomMobs.textureVariantsMap.put(name, texLocs);
            }

            if (texLocs != null && texLocs.length > 0) {
                int index = randomId % texLocs.length;
                ResourceLocation texLoc = texLocs[index];
                return texLoc;
            } else {
                return loc;
            }
        }
    }

    private static ResourceLocation[] getTextureVariants(ResourceLocation loc) {
        TextureUtils.getTexture(loc);
        ResourceLocation[] texLocs = new ResourceLocation[0];
        String name = loc.getResourcePath();
        int pointPos = name.lastIndexOf(46);

        if (pointPos < 0) {
            return texLocs;
        } else {
            String prefix = name.substring(0, pointPos);
            String suffix = name.substring(pointPos);
            String texEntStr = "textures/entity/";

            if (!prefix.startsWith(texEntStr)) {
                return texLocs;
            } else {
                prefix = prefix.substring(texEntStr.length());
                prefix = "mcpatcher/mob/" + prefix;
                int countVariants = RandomMobs.getCountTextureVariants(prefix, suffix);

                if (countVariants <= 1) {
                    return texLocs;
                } else {
                    texLocs = new ResourceLocation[countVariants];
                    texLocs[0] = loc;

                    for (int i = 1; i < texLocs.length; ++i) {
                        int texNum = i + 1;
                        String texName = prefix + texNum + suffix;
                        texLocs[i] = new ResourceLocation(loc.getResourceDomain(), texName);
                        TextureUtils.getTexture(texLocs[i]);
                    }

                    Config.dbg("RandomMobs: " + loc + ", variants: " + texLocs.length);
                    return texLocs;
                }
            }
        }
    }

    private static int getCountTextureVariants(String prefix, String suffix) {
        short maxNum = 1000;

        for (int num = 2; num < maxNum; ++num) {
            String variant = prefix + num + suffix;
            ResourceLocation loc = new ResourceLocation(variant);

            if (!Config.hasResource(loc)) {
                return num - 1;
            }
        }

        return maxNum;
    }

    public static void resetTextures() {
        RandomMobs.textureVariantsMap.clear();

        if (Config.isRandomMobs()) {
            RandomMobs.initialize();
        }
    }

    private static void initialize() {
        RandomMobs.renderGlobal = Config.getRenderGlobal();

        if (RandomMobs.renderGlobal != null) {
            RandomMobs.initialized = true;
            ArrayList list = new ArrayList();
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
                String name = (String) list.get(i);
                String tex = "textures/entity/" + name + ".png";
                ResourceLocation texLoc = new ResourceLocation(tex);

                if (!Config.hasResource(texLoc)) {
                    Config.warn("Not found: " + texLoc);
                }

                RandomMobs.getTextureLocation(texLoc, 100);
            }
        }
    }
}
