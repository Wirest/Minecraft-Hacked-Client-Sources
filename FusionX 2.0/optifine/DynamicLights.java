package optifine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class DynamicLights
{
    private static Map<Integer, DynamicLight> mapDynamicLights = new HashMap();
    private static long timeUpdateMs = 0L;
    private static final double MAX_DIST = 7.5D;
    private static final double MAX_DIST_SQ = 56.25D;
    private static final int LIGHT_LEVEL_MAX = 15;
    private static final int LIGHT_LEVEL_FIRE = 15;
    private static final int LIGHT_LEVEL_BLAZE = 10;
    private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
    private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
    private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
    private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;

    public static void entityAdded(Entity entityIn, RenderGlobal renderGlobal) {}

    public static void entityRemoved(Entity entityIn, RenderGlobal renderGlobal)
    {
        Map var2 = mapDynamicLights;

        synchronized (mapDynamicLights)
        {
            DynamicLight dynamicLight = (DynamicLight)mapDynamicLights.remove(IntegerCache.valueOf(entityIn.getEntityId()));

            if (dynamicLight != null)
            {
                dynamicLight.updateLitChunks(renderGlobal);
            }
        }
    }

    public static void update(RenderGlobal renderGlobal)
    {
        long timeNowMs = System.currentTimeMillis();

        if (timeNowMs >= timeUpdateMs + 50L)
        {
            timeUpdateMs = timeNowMs;
            Map var3 = mapDynamicLights;

            synchronized (mapDynamicLights)
            {
                updateMapDynamicLights(renderGlobal);

                if (mapDynamicLights.size() > 0)
                {
                    Collection dynamicLights = mapDynamicLights.values();
                    Iterator it = dynamicLights.iterator();

                    while (it.hasNext())
                    {
                        DynamicLight dynamicLight = (DynamicLight)it.next();
                        dynamicLight.update(renderGlobal);
                    }
                }
            }
        }
    }

    private static void updateMapDynamicLights(RenderGlobal renderGlobal)
    {
        WorldClient world = renderGlobal.getWorld();

        if (world != null)
        {
            List entities = world.getLoadedEntityList();
            Iterator it = entities.iterator();

            while (it.hasNext())
            {
                Entity entity = (Entity)it.next();
                int lightLevel = getLightLevel(entity);
                Integer key;
                DynamicLight dynamicLight;

                if (lightLevel > 0)
                {
                    key = IntegerCache.valueOf(entity.getEntityId());
                    dynamicLight = (DynamicLight)mapDynamicLights.get(key);

                    if (dynamicLight == null)
                    {
                        dynamicLight = new DynamicLight(entity);
                        mapDynamicLights.put(key, dynamicLight);
                    }
                }
                else
                {
                    key = IntegerCache.valueOf(entity.getEntityId());
                    dynamicLight = (DynamicLight)mapDynamicLights.remove(key);

                    if (dynamicLight != null)
                    {
                        dynamicLight.updateLitChunks(renderGlobal);
                    }
                }
            }
        }
    }

    public static int getCombinedLight(BlockPos pos, int combinedLight)
    {
        double lightPlayer = getLightLevel(pos);
        combinedLight = getCombinedLight(lightPlayer, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(Entity entity, int combinedLight)
    {
        double lightPlayer = (double)getLightLevel(entity);
        combinedLight = getCombinedLight(lightPlayer, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(double lightPlayer, int combinedLight)
    {
        if (lightPlayer > 0.0D)
        {
            int lightPlayerFF = (int)(lightPlayer * 16.0D);
            int lightBlockFF = combinedLight & 255;

            if (lightPlayerFF > lightBlockFF)
            {
                combinedLight &= -256;
                combinedLight |= lightPlayerFF;
            }
        }

        return combinedLight;
    }

    public static double getLightLevel(BlockPos pos)
    {
        double lightLevelMax = 0.0D;
        Map lightPlayer = mapDynamicLights;

        synchronized (mapDynamicLights)
        {
            Collection dynamicLights = mapDynamicLights.values();
            Iterator it = dynamicLights.iterator();

            while (true)
            {
                if (!it.hasNext())
                {
                    break;
                }

                DynamicLight dynamicLight = (DynamicLight)it.next();
                int dynamicLightLevel = dynamicLight.getLastLightLevel();

                if (dynamicLightLevel > 0)
                {
                    double px = dynamicLight.getLastPosX();
                    double py = dynamicLight.getLastPosY();
                    double pz = dynamicLight.getLastPosZ();
                    double dx = (double)pos.getX() - px;
                    double dy = (double)pos.getY() - py;
                    double dz = (double)pos.getZ() - pz;
                    double distSq = dx * dx + dy * dy + dz * dz;

                    if (dynamicLight.isUnderwater() && !Config.isClearWater())
                    {
                        dynamicLightLevel = Config.limit(dynamicLightLevel - 2, 0, 15);
                        distSq *= 2.0D;
                    }

                    if (distSq <= 56.25D)
                    {
                        double dist = Math.sqrt(distSq);
                        double light = 1.0D - dist / 7.5D;
                        double lightLevel = light * (double)dynamicLightLevel;

                        if (lightLevel > lightLevelMax)
                        {
                            lightLevelMax = lightLevel;
                        }
                    }
                }
            }
        }

        double lightPlayer1 = Config.limit(lightLevelMax, 0.0D, 15.0D);
        return lightPlayer1;
    }

    public static int getLightLevel(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return 0;
        }
        else
        {
            Item item = itemStack.getItem();

            if (item instanceof ItemBlock)
            {
                ItemBlock itemBlock = (ItemBlock)item;
                Block block = itemBlock.getBlock();

                if (block != null)
                {
                    return block.getLightValue();
                }
            }

            return item == Items.lava_bucket ? Blocks.lava.getLightValue() : (item != Items.blaze_rod && item != Items.blaze_powder ? (item == Items.glowstone_dust ? 8 : (item == Items.prismarine_crystals ? 8 : (item == Items.magma_cream ? 8 : (item == Items.nether_star ? Blocks.beacon.getLightValue() / 2 : 0)))) : 10);
        }
    }

    public static int getLightLevel(Entity entity)
    {
        if (entity == Config.getMinecraft().func_175606_aa() && !Config.isDynamicHandLight())
        {
            return 0;
        }
        else
        {
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer entityItem = (EntityPlayer)entity;

                if (entityItem.func_175149_v())
                {
                    return 0;
                }
            }

            if (entity.isBurning())
            {
                return 15;
            }
            else if (entity instanceof EntityFireball)
            {
                return 15;
            }
            else if (entity instanceof EntityTNTPrimed)
            {
                return 15;
            }
            else if (entity instanceof EntityBlaze)
            {
                EntityBlaze entityItem5 = (EntityBlaze)entity;
                return entityItem5.func_70845_n() ? 15 : 10;
            }
            else if (entity instanceof EntityMagmaCube)
            {
                EntityMagmaCube entityItem4 = (EntityMagmaCube)entity;
                return (double)entityItem4.squishFactor > 0.6D ? 13 : 8;
            }
            else
            {
                if (entity instanceof EntityCreeper)
                {
                    EntityCreeper entityItem1 = (EntityCreeper)entity;

                    if ((double)entityItem1.getCreeperFlashIntensity(0.0F) > 0.001D)
                    {
                        return 15;
                    }
                }

                ItemStack itemStack;

                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entityItem3 = (EntityLivingBase)entity;
                    itemStack = entityItem3.getHeldItem();
                    int levelMain = getLightLevel(itemStack);
                    ItemStack stackHead = entityItem3.getEquipmentInSlot(4);
                    int levelHead = getLightLevel(stackHead);
                    return Math.max(levelMain, levelHead);
                }
                else if (entity instanceof EntityItem)
                {
                    EntityItem entityItem2 = (EntityItem)entity;
                    itemStack = getItemStack(entityItem2);
                    return getLightLevel(itemStack);
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    public static void removeLights(RenderGlobal renderGlobal)
    {
        Map var1 = mapDynamicLights;

        synchronized (mapDynamicLights)
        {
            Collection lights = mapDynamicLights.values();
            Iterator it = lights.iterator();

            while (it.hasNext())
            {
                DynamicLight dynamicLight = (DynamicLight)it.next();
                it.remove();
                dynamicLight.updateLitChunks(renderGlobal);
            }
        }
    }

    public static void clear()
    {
        Map var0 = mapDynamicLights;

        synchronized (mapDynamicLights)
        {
            mapDynamicLights.clear();
        }
    }

    public static int getCount()
    {
        Map var0 = mapDynamicLights;

        synchronized (mapDynamicLights)
        {
            return mapDynamicLights.size();
        }
    }

    public static ItemStack getItemStack(EntityItem entityItem)
    {
        ItemStack itemstack = entityItem.getDataWatcher().getWatchableObjectItemStack(10);
        return itemstack;
    }
}
