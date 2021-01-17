// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Collection;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class DynamicLights
{
    private static Map<Integer, DynamicLight> mapDynamicLights;
    private static long timeUpdateMs;
    private static final double MAX_DIST = 7.5;
    private static final double MAX_DIST_SQ = 56.25;
    private static final int LIGHT_LEVEL_MAX = 15;
    private static final int LIGHT_LEVEL_FIRE = 15;
    private static final int LIGHT_LEVEL_BLAZE = 10;
    private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
    private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
    private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
    private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;
    
    static {
        DynamicLights.mapDynamicLights = new HashMap<Integer, DynamicLight>();
        DynamicLights.timeUpdateMs = 0L;
    }
    
    public static void entityAdded(final Entity p_entityAdded_0_, final RenderGlobal p_entityAdded_1_) {
    }
    
    public static void entityRemoved(final Entity p_entityRemoved_0_, final RenderGlobal p_entityRemoved_1_) {
        synchronized (DynamicLights.mapDynamicLights) {
            final DynamicLight dynamiclight = DynamicLights.mapDynamicLights.remove(IntegerCache.valueOf(p_entityRemoved_0_.getEntityId()));
            if (dynamiclight != null) {
                dynamiclight.updateLitChunks(p_entityRemoved_1_);
            }
        }
        // monitorexit(DynamicLights.mapDynamicLights)
    }
    
    public static void update(final RenderGlobal p_update_0_) {
        final long i = System.currentTimeMillis();
        if (i >= DynamicLights.timeUpdateMs + 50L) {
            DynamicLights.timeUpdateMs = i;
            synchronized (DynamicLights.mapDynamicLights) {
                updateMapDynamicLights(p_update_0_);
                if (DynamicLights.mapDynamicLights.size() > 0) {
                    for (final DynamicLight dynamiclight : DynamicLights.mapDynamicLights.values()) {
                        dynamiclight.update(p_update_0_);
                    }
                }
            }
            // monitorexit(DynamicLights.mapDynamicLights)
        }
    }
    
    private static void updateMapDynamicLights(final RenderGlobal p_updateMapDynamicLights_0_) {
        final World world = p_updateMapDynamicLights_0_.getWorld();
        if (world != null) {
            for (final Entity entity : world.getLoadedEntityList()) {
                final int i = getLightLevel(entity);
                if (i > 0) {
                    final Integer integer = IntegerCache.valueOf(entity.getEntityId());
                    DynamicLight dynamiclight = DynamicLights.mapDynamicLights.get(integer);
                    if (dynamiclight != null) {
                        continue;
                    }
                    dynamiclight = new DynamicLight(entity);
                    DynamicLights.mapDynamicLights.put(integer, dynamiclight);
                }
                else {
                    final Integer integer2 = IntegerCache.valueOf(entity.getEntityId());
                    final DynamicLight dynamiclight2 = DynamicLights.mapDynamicLights.remove(integer2);
                    if (dynamiclight2 == null) {
                        continue;
                    }
                    dynamiclight2.updateLitChunks(p_updateMapDynamicLights_0_);
                }
            }
        }
    }
    
    public static int getCombinedLight(final BlockPos p_getCombinedLight_0_, int p_getCombinedLight_1_) {
        final double d0 = getLightLevel(p_getCombinedLight_0_);
        p_getCombinedLight_1_ = getCombinedLight(d0, p_getCombinedLight_1_);
        return p_getCombinedLight_1_;
    }
    
    public static int getCombinedLight(final Entity p_getCombinedLight_0_, int p_getCombinedLight_1_) {
        final double d0 = getLightLevel(p_getCombinedLight_0_);
        p_getCombinedLight_1_ = getCombinedLight(d0, p_getCombinedLight_1_);
        return p_getCombinedLight_1_;
    }
    
    public static int getCombinedLight(final double p_getCombinedLight_0_, int p_getCombinedLight_2_) {
        if (p_getCombinedLight_0_ > 0.0) {
            final int i = (int)(p_getCombinedLight_0_ * 16.0);
            final int j = p_getCombinedLight_2_ & 0xFF;
            if (i > j) {
                p_getCombinedLight_2_ &= 0xFFFFFF00;
                p_getCombinedLight_2_ |= i;
            }
        }
        return p_getCombinedLight_2_;
    }
    
    public static double getLightLevel(final BlockPos p_getLightLevel_0_) {
        double d0 = 0.0;
        synchronized (DynamicLights.mapDynamicLights) {
            for (final DynamicLight dynamiclight : DynamicLights.mapDynamicLights.values()) {
                int i = dynamiclight.getLastLightLevel();
                if (i > 0) {
                    final double d2 = dynamiclight.getLastPosX();
                    final double d3 = dynamiclight.getLastPosY();
                    final double d4 = dynamiclight.getLastPosZ();
                    final double d5 = p_getLightLevel_0_.getX() - d2;
                    final double d6 = p_getLightLevel_0_.getY() - d3;
                    final double d7 = p_getLightLevel_0_.getZ() - d4;
                    double d8 = d5 * d5 + d6 * d6 + d7 * d7;
                    if (dynamiclight.isUnderwater() && !Config.isClearWater()) {
                        i = Config.limit(i - 2, 0, 15);
                        d8 *= 2.0;
                    }
                    if (d8 > 56.25) {
                        continue;
                    }
                    final double d9 = Math.sqrt(d8);
                    final double d10 = 1.0 - d9 / 7.5;
                    final double d11 = d10 * i;
                    if (d11 <= d0) {
                        continue;
                    }
                    d0 = d11;
                }
            }
        }
        // monitorexit(DynamicLights.mapDynamicLights)
        final double d12 = Config.limit(d0, 0.0, 15.0);
        return d12;
    }
    
    public static int getLightLevel(final ItemStack p_getLightLevel_0_) {
        if (p_getLightLevel_0_ == null) {
            return 0;
        }
        final Item item = p_getLightLevel_0_.getItem();
        if (item instanceof ItemBlock) {
            final ItemBlock itemblock = (ItemBlock)item;
            final Block block = itemblock.getBlock();
            if (block != null) {
                return block.getLightValue();
            }
        }
        return (item == Items.lava_bucket) ? Blocks.lava.getLightValue() : ((item != Items.blaze_rod && item != Items.blaze_powder) ? ((item == Items.glowstone_dust) ? 8 : ((item == Items.prismarine_crystals) ? 8 : ((item == Items.magma_cream) ? 8 : ((item == Items.nether_star) ? (Blocks.beacon.getLightValue() / 2) : 0)))) : 10);
    }
    
    public static int getLightLevel(final Entity p_getLightLevel_0_) {
        if (p_getLightLevel_0_ == Config.getMinecraft().getRenderViewEntity() && !Config.isDynamicHandLight()) {
            return 0;
        }
        if (p_getLightLevel_0_ instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)p_getLightLevel_0_;
            if (entityplayer.isSpectator()) {
                return 0;
            }
        }
        if (p_getLightLevel_0_.isBurning()) {
            return 15;
        }
        if (p_getLightLevel_0_ instanceof EntityFireball) {
            return 15;
        }
        if (p_getLightLevel_0_ instanceof EntityTNTPrimed) {
            return 15;
        }
        if (p_getLightLevel_0_ instanceof EntityBlaze) {
            final EntityBlaze entityblaze = (EntityBlaze)p_getLightLevel_0_;
            return entityblaze.func_70845_n() ? 15 : 10;
        }
        if (p_getLightLevel_0_ instanceof EntityMagmaCube) {
            final EntityMagmaCube entitymagmacube = (EntityMagmaCube)p_getLightLevel_0_;
            return (entitymagmacube.squishFactor > 0.6) ? 13 : 8;
        }
        if (p_getLightLevel_0_ instanceof EntityCreeper) {
            final EntityCreeper entitycreeper = (EntityCreeper)p_getLightLevel_0_;
            if (entitycreeper.getCreeperFlashIntensity(0.0f) > 0.001) {
                return 15;
            }
        }
        if (p_getLightLevel_0_ instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)p_getLightLevel_0_;
            final ItemStack itemstack2 = entitylivingbase.getHeldItem();
            final int i = getLightLevel(itemstack2);
            final ItemStack itemstack3 = entitylivingbase.getEquipmentInSlot(4);
            final int j = getLightLevel(itemstack3);
            return Math.max(i, j);
        }
        if (p_getLightLevel_0_ instanceof EntityItem) {
            final EntityItem entityitem = (EntityItem)p_getLightLevel_0_;
            final ItemStack itemstack4 = getItemStack(entityitem);
            return getLightLevel(itemstack4);
        }
        return 0;
    }
    
    public static void removeLights(final RenderGlobal p_removeLights_0_) {
        synchronized (DynamicLights.mapDynamicLights) {
            final Collection<DynamicLight> collection = DynamicLights.mapDynamicLights.values();
            final Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                final DynamicLight dynamiclight = iterator.next();
                iterator.remove();
                dynamiclight.updateLitChunks(p_removeLights_0_);
            }
        }
        // monitorexit(DynamicLights.mapDynamicLights)
    }
    
    public static void clear() {
        synchronized (DynamicLights.mapDynamicLights) {
            DynamicLights.mapDynamicLights.clear();
        }
        // monitorexit(DynamicLights.mapDynamicLights)
    }
    
    public static int getCount() {
        synchronized (DynamicLights.mapDynamicLights) {
            // monitorexit(DynamicLights.mapDynamicLights)
            return DynamicLights.mapDynamicLights.size();
        }
    }
    
    public static ItemStack getItemStack(final EntityItem p_getItemStack_0_) {
        final ItemStack itemstack = p_getItemStack_0_.getDataWatcher().getWatchableObjectItemStack(10);
        return itemstack;
    }
}
