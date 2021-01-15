package net.minecraft.util;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum EnumParticleTypes
{
    EXPLOSION_NORMAL("EXPLOSION_NORMAL", 0, "explode", 0, true),
    EXPLOSION_LARGE("EXPLOSION_LARGE", 1, "largeexplode", 1, true),
    EXPLOSION_HUGE("EXPLOSION_HUGE", 2, "hugeexplosion", 2, true),
    FIREWORKS_SPARK("FIREWORKS_SPARK", 3, "fireworksSpark", 3, false),
    WATER_BUBBLE("WATER_BUBBLE", 4, "bubble", 4, false),
    WATER_SPLASH("WATER_SPLASH", 5, "splash", 5, false),
    WATER_WAKE("WATER_WAKE", 6, "wake", 6, false),
    SUSPENDED("SUSPENDED", 7, "suspended", 7, false),
    SUSPENDED_DEPTH("SUSPENDED_DEPTH", 8, "depthsuspend", 8, false),
    CRIT("CRIT", 9, "crit", 9, false),
    CRIT_MAGIC("CRIT_MAGIC", 10, "magicCrit", 10, false),
    SMOKE_NORMAL("SMOKE_NORMAL", 11, "smoke", 11, false),
    SMOKE_LARGE("SMOKE_LARGE", 12, "largesmoke", 12, false),
    SPELL("SPELL", 13, "spell", 13, false),
    SPELL_INSTANT("SPELL_INSTANT", 14, "instantSpell", 14, false),
    SPELL_MOB("SPELL_MOB", 15, "mobSpell", 15, false),
    SPELL_MOB_AMBIENT("SPELL_MOB_AMBIENT", 16, "mobSpellAmbient", 16, false),
    SPELL_WITCH("SPELL_WITCH", 17, "witchMagic", 17, false),
    DRIP_WATER("DRIP_WATER", 18, "dripWater", 18, false),
    DRIP_LAVA("DRIP_LAVA", 19, "dripLava", 19, false),
    VILLAGER_ANGRY("VILLAGER_ANGRY", 20, "angryVillager", 20, false),
    VILLAGER_HAPPY("VILLAGER_HAPPY", 21, "happyVillager", 21, false),
    TOWN_AURA("TOWN_AURA", 22, "townaura", 22, false),
    NOTE("NOTE", 23, "note", 23, false),
    PORTAL("PORTAL", 24, "portal", 24, false),
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 25, "enchantmenttable", 25, false),
    FLAME("FLAME", 26, "flame", 26, false),
    LAVA("LAVA", 27, "lava", 27, false),
    FOOTSTEP("FOOTSTEP", 28, "footstep", 28, false),
    CLOUD("CLOUD", 29, "cloud", 29, false),
    REDSTONE("REDSTONE", 30, "reddust", 30, false),
    SNOWBALL("SNOWBALL", 31, "snowballpoof", 31, false),
    SNOW_SHOVEL("SNOW_SHOVEL", 32, "snowshovel", 32, false),
    SLIME("SLIME", 33, "slime", 33, false),
    HEART("HEART", 34, "heart", 34, false),
    BARRIER("BARRIER", 35, "barrier", 35, false),
    ITEM_CRACK("ITEM_CRACK", 36, "iconcrack_", 36, false, 2),
    BLOCK_CRACK("BLOCK_CRACK", 37, "blockcrack_", 37, false, 1),
    BLOCK_DUST("BLOCK_DUST", 38, "blockdust_", 38, false, 1),
    WATER_DROP("WATER_DROP", 39, "droplet", 39, false),
    ITEM_TAKE("ITEM_TAKE", 40, "take", 40, false),
    MOB_APPEARANCE("MOB_APPEARANCE", 41, "mobappearance", 41, true);
    private final String particleName;
    private final int particleID;
    private final boolean field_179371_S;
    private final int argumentCount;
    private static final Map PARTICLES = Maps.newHashMap();
    private static final String[] PARTICLE_NAMES; 

    private EnumParticleTypes(String p_i46011_1_, int p_i46011_2_, String particleNameIn, int particleIDIn, boolean p_i46011_5_, int argumentCountIn)
    {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.field_179371_S = p_i46011_5_;
        this.argumentCount = argumentCountIn;
    }

    private EnumParticleTypes(String p_i46012_1_, int p_i46012_2_, String particleNameIn, int particleIDIn, boolean p_i46012_5_)
    {
        this(p_i46012_1_, p_i46012_2_, particleNameIn, particleIDIn, p_i46012_5_, 0);
    }

    public static String[] getParticleNames()
    {
        return PARTICLE_NAMES;
    }

    public String getParticleName()
    {
        return this.particleName;
    }

    public int getParticleID()
    {
        return this.particleID;
    }

    public int getArgumentCount()
    {
        return this.argumentCount;
    }

    public boolean func_179344_e()
    {
        return this.field_179371_S;
    }

    public boolean hasArguments()
    {
        return this.argumentCount > 0;
    }

    /**
     * Gets the relative EnumParticleTypes by id.
     */
    public static EnumParticleTypes getParticleFromId(int particleId)
    {
        return (EnumParticleTypes)PARTICLES.get(Integer.valueOf(particleId));
    }

    static {
        ArrayList var0 = Lists.newArrayList();
        EnumParticleTypes[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            EnumParticleTypes var4 = var1[var3];
            PARTICLES.put(Integer.valueOf(var4.getParticleID()), var4);

            if (!var4.getParticleName().endsWith("_"))
            {
                var0.add(var4.getParticleName());
            }
        }

        PARTICLE_NAMES = (String[])var0.toArray(new String[var0.size()]);
    }
}
