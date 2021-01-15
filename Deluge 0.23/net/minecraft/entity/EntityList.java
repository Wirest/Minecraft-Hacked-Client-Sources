package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityList
{
    private static final Logger logger = LogManager.getLogger();

    /** Provides a mapping between entity classes and a string */
    private static final Map stringToClassMapping = Maps.newHashMap();

    /** Provides a mapping between a string and an entity classes */
    private static final Map classToStringMapping = Maps.newHashMap();

    /** provides a mapping between an entityID and an Entity Class */
    private static final Map idToClassMapping = Maps.newHashMap();

    /** provides a mapping between an Entity Class and an entity ID */
    private static final Map classToIDMapping = Maps.newHashMap();
    private static final Map field_180126_g = Maps.newHashMap();

    /** This is a HashMap of the Creative Entity Eggs/Spawners. */
    public static final Map entityEggs = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00001538";

    /**
     * adds a mapping between Entity classes and both a string representation and an ID
     */
    private static void addMapping(Class p_75618_0_, String p_75618_1_, int p_75618_2_)
    {
        if (stringToClassMapping.containsKey(p_75618_1_))
        {
            throw new IllegalArgumentException("ID is already registered: " + p_75618_1_);
        }
        else if (idToClassMapping.containsKey(Integer.valueOf(p_75618_2_)))
        {
            throw new IllegalArgumentException("ID is already registered: " + p_75618_2_);
        }
        else if (p_75618_2_ == 0)
        {
            throw new IllegalArgumentException("Cannot register to reserved id: " + p_75618_2_);
        }
        else if (p_75618_0_ == null)
        {
            throw new IllegalArgumentException("Cannot register null clazz for id: " + p_75618_2_);
        }
        else
        {
            stringToClassMapping.put(p_75618_1_, p_75618_0_);
            classToStringMapping.put(p_75618_0_, p_75618_1_);
            idToClassMapping.put(Integer.valueOf(p_75618_2_), p_75618_0_);
            classToIDMapping.put(p_75618_0_, Integer.valueOf(p_75618_2_));
            field_180126_g.put(p_75618_1_, Integer.valueOf(p_75618_2_));
        }
    }

    /**
     * Adds a entity mapping with egg info.
     */
    private static void addMapping(Class p_75614_0_, String p_75614_1_, int p_75614_2_, int p_75614_3_, int p_75614_4_)
    {
        addMapping(p_75614_0_, p_75614_1_, p_75614_2_);
        entityEggs.put(Integer.valueOf(p_75614_2_), new EntityList.EntityEggInfo(p_75614_2_, p_75614_3_, p_75614_4_));
    }

    /**
     * Create a new instance of an entity in the world by using the entity name.
     */
    public static Entity createEntityByName(String p_75620_0_, World worldIn)
    {
        Entity var2 = null;

        try
        {
            Class var3 = (Class)stringToClassMapping.get(p_75620_0_);

            if (var3 != null)
            {
                var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        return var2;
    }

    /**
     * create a new instance of an entity from NBT store
     */
    public static Entity createEntityFromNBT(NBTTagCompound p_75615_0_, World worldIn)
    {
        Entity var2 = null;

        if ("Minecart".equals(p_75615_0_.getString("id")))
        {
            p_75615_0_.setString("id", EntityMinecart.EnumMinecartType.func_180038_a(p_75615_0_.getInteger("Type")).func_180040_b());
            p_75615_0_.removeTag("Type");
        }

        try
        {
            Class var3 = (Class)stringToClassMapping.get(p_75615_0_.getString("id"));

            if (var3 != null)
            {
                var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        if (var2 != null)
        {
            var2.readFromNBT(p_75615_0_);
        }
        else
        {
            logger.warn("Skipping Entity with id " + p_75615_0_.getString("id"));
        }

        return var2;
    }

    /**
     * Create a new instance of an entity in the world by using an entity ID.
     */
    public static Entity createEntityByID(int p_75616_0_, World worldIn)
    {
        Entity var2 = null;

        try
        {
            Class var3 = getClassFromID(p_75616_0_);

            if (var3 != null)
            {
                var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        if (var2 == null)
        {
            logger.warn("Skipping Entity with id " + p_75616_0_);
        }

        return var2;
    }

    /**
     * gets the entityID of a specific entity
     */
    public static int getEntityID(Entity p_75619_0_)
    {
        Integer var1 = (Integer)classToIDMapping.get(p_75619_0_.getClass());
        return var1 == null ? 0 : var1.intValue();
    }

    /**
     * Return the class assigned to this entity ID.
     */
    public static Class getClassFromID(int p_90035_0_)
    {
        return (Class)idToClassMapping.get(Integer.valueOf(p_90035_0_));
    }

    /**
     * Gets the string representation of a specific entity.
     */
    public static String getEntityString(Entity p_75621_0_)
    {
        return (String)classToStringMapping.get(p_75621_0_.getClass());
    }

    public static int func_180122_a(String p_180122_0_)
    {
        Integer var1 = (Integer)field_180126_g.get(p_180122_0_);
        return var1 == null ? 90 : var1.intValue();
    }

    /**
     * Finds the class using IDtoClassMapping and classToStringMapping
     */
    public static String getStringFromID(int p_75617_0_)
    {
        return (String)classToStringMapping.get(getClassFromID(p_75617_0_));
    }

    public static void func_151514_a() {}

    public static List func_180124_b()
    {
        Set var0 = stringToClassMapping.keySet();
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = var0.iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            Class var4 = (Class)stringToClassMapping.get(var3);

            if ((var4.getModifiers() & 1024) != 1024)
            {
                var1.add(var3);
            }
        }

        var1.add("LightningBolt");
        return var1;
    }

    public static boolean func_180123_a(Entity p_180123_0_, String p_180123_1_)
    {
        String var2 = getEntityString(p_180123_0_);

        if (var2 == null && p_180123_0_ instanceof EntityPlayer)
        {
            var2 = "Player";
        }
        else if (var2 == null && p_180123_0_ instanceof EntityLightningBolt)
        {
            var2 = "LightningBolt";
        }

        return p_180123_1_.equals(var2);
    }

    public static boolean func_180125_b(String p_180125_0_)
    {
        return "Player".equals(p_180125_0_) || func_180124_b().contains(p_180125_0_);
    }

    static
    {
        addMapping(EntityItem.class, "Item", 1);
        addMapping(EntityXPOrb.class, "XPOrb", 2);
        addMapping(EntityLeashKnot.class, "LeashKnot", 8);
        addMapping(EntityPainting.class, "Painting", 9);
        addMapping(EntityArrow.class, "Arrow", 10);
        addMapping(EntitySnowball.class, "Snowball", 11);
        addMapping(EntityLargeFireball.class, "Fireball", 12);
        addMapping(EntitySmallFireball.class, "SmallFireball", 13);
        addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
        addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
        addMapping(EntityPotion.class, "ThrownPotion", 16);
        addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
        addMapping(EntityItemFrame.class, "ItemFrame", 18);
        addMapping(EntityWitherSkull.class, "WitherSkull", 19);
        addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
        addMapping(EntityFallingBlock.class, "FallingSand", 21);
        addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
        addMapping(EntityArmorStand.class, "ArmorStand", 30);
        addMapping(EntityBoat.class, "Boat", 41);
        addMapping(EntityMinecartEmpty.class, EntityMinecart.EnumMinecartType.RIDEABLE.func_180040_b(), 42);
        addMapping(EntityMinecartChest.class, EntityMinecart.EnumMinecartType.CHEST.func_180040_b(), 43);
        addMapping(EntityMinecartFurnace.class, EntityMinecart.EnumMinecartType.FURNACE.func_180040_b(), 44);
        addMapping(EntityMinecartTNT.class, EntityMinecart.EnumMinecartType.TNT.func_180040_b(), 45);
        addMapping(EntityMinecartHopper.class, EntityMinecart.EnumMinecartType.HOPPER.func_180040_b(), 46);
        addMapping(EntityMinecartMobSpawner.class, EntityMinecart.EnumMinecartType.SPAWNER.func_180040_b(), 47);
        addMapping(EntityMinecartCommandBlock.class, EntityMinecart.EnumMinecartType.COMMAND_BLOCK.func_180040_b(), 40);
        addMapping(EntityLiving.class, "Mob", 48);
        addMapping(EntityMob.class, "Monster", 49);
        addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
        addMapping(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
        addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
        addMapping(EntityGiantZombie.class, "Giant", 53);
        addMapping(EntityZombie.class, "Zombie", 54, 44975, 7969893);
        addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
        addMapping(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
        addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
        addMapping(EntityEnderman.class, "Enderman", 58, 1447446, 0);
        addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
        addMapping(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
        addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
        addMapping(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
        addMapping(EntityDragon.class, "EnderDragon", 63);
        addMapping(EntityWither.class, "WitherBoss", 64);
        addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
        addMapping(EntityWitch.class, "Witch", 66, 3407872, 5349438);
        addMapping(EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
        addMapping(EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
        addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
        addMapping(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
        addMapping(EntityCow.class, "Cow", 92, 4470310, 10592673);
        addMapping(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
        addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
        addMapping(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
        addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
        addMapping(EntitySnowman.class, "SnowMan", 97);
        addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
        addMapping(EntityIronGolem.class, "VillagerGolem", 99);
        addMapping(EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
        addMapping(EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
        addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
        addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
    }

    public static class EntityEggInfo
    {
        public final int spawnedID;
        public final int primaryColor;
        public final int secondaryColor;
        public final StatBase field_151512_d;
        public final StatBase field_151513_e;
        private static final String __OBFID = "CL_00001539";

        public EntityEggInfo(int p_i1583_1_, int p_i1583_2_, int p_i1583_3_)
        {
            this.spawnedID = p_i1583_1_;
            this.primaryColor = p_i1583_2_;
            this.secondaryColor = p_i1583_3_;
            this.field_151512_d = StatList.func_151182_a(this);
            this.field_151513_e = StatList.func_151176_b(this);
        }
    }
}
