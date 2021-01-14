package net.minecraft.tileentity;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic {
    /**
     * The delay to spawn.
     */
    private int spawnDelay = 20;
    private String mobID = "Pig";

    /**
     * List of minecart to spawn.
     */
    private final List minecartToSpawn = Lists.newArrayList();
    private MobSpawnerBaseLogic.WeightedRandomMinecart randomEntity;
    private double field_98287_c;
    private double field_98284_d;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;

    /**
     * Cached instance of the entity to render inside the spawner.
     */
    private Entity cachedEntity;
    private int maxNearbyEntities = 6;

    /**
     * The distance from which a player activates the spawner.
     */
    private int activatingRangeFromPlayer = 16;

    /**
     * The range coefficient for spawning entities around.
     */
    private int spawnRange = 4;
    private static final String __OBFID = "CL_00000129";

    /**
     * Gets the entity name that should be spawned.
     */
    private String getEntityNameToSpawn() {
        if (getRandomEntity() == null) {
            if (mobID.equals("Minecart")) {
                mobID = "MinecartRideable";
            }

            return mobID;
        } else {
            return getRandomEntity().entityType;
        }
    }

    public void setEntityName(String p_98272_1_) {
        mobID = p_98272_1_;
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to
     * activate it.
     */
    private boolean isActivated() {
        BlockPos var1 = func_177221_b();
        return getSpawnerWorld().func_175636_b(var1.getX() + 0.5D, var1.getY() + 0.5D, var1.getZ() + 0.5D, activatingRangeFromPlayer);
    }

    public void updateSpawner() {
        if (isActivated()) {
            BlockPos var1 = func_177221_b();
            double var6;

            if (getSpawnerWorld().isRemote) {
                double var2 = var1.getX() + getSpawnerWorld().rand.nextFloat();
                double var4 = var1.getY() + getSpawnerWorld().rand.nextFloat();
                var6 = var1.getZ() + getSpawnerWorld().rand.nextFloat();
                getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var2, var4, var6, 0.0D, 0.0D, 0.0D, new int[0]);
                getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, var2, var4, var6, 0.0D, 0.0D, 0.0D, new int[0]);

                if (spawnDelay > 0) {
                    --spawnDelay;
                }

                field_98284_d = field_98287_c;
                field_98287_c = (field_98287_c + 1000.0F / (spawnDelay + 200.0F)) % 360.0D;
            } else {
                if (spawnDelay == -1) {
                    resetTimer();
                }

                if (spawnDelay > 0) {
                    --spawnDelay;
                    return;
                }

                boolean var13 = false;

                for (int var3 = 0; var3 < spawnCount; ++var3) {
                    Entity var14 = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());

                    if (var14 == null) {
                        return;
                    }

                    int var5 = getSpawnerWorld().getEntitiesWithinAABB(var14.getClass(), (new AxisAlignedBB(var1.getX(), var1.getY(), var1.getZ(), var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1)).expand(spawnRange, spawnRange, spawnRange)).size();

                    if (var5 >= maxNearbyEntities) {
                        resetTimer();
                        return;
                    }

                    var6 = var1.getX() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * spawnRange + 0.5D;
                    double var8 = var1.getY() + getSpawnerWorld().rand.nextInt(3) - 1;
                    double var10 = var1.getZ() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * spawnRange + 0.5D;
                    EntityLiving var12 = var14 instanceof EntityLiving ? (EntityLiving) var14 : null;
                    var14.setLocationAndAngles(var6, var8, var10, getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                    if (var12 == null || var12.getCanSpawnHere() && var12.handleLavaMovement()) {
                        func_180613_a(var14, true);
                        getSpawnerWorld().playAuxSFX(2004, var1, 0);

                        if (var12 != null) {
                            var12.spawnExplosionParticle();
                        }

                        var13 = true;
                    }
                }

                if (var13) {
                    resetTimer();
                }
            }
        }
    }

    private Entity func_180613_a(Entity p_180613_1_, boolean p_180613_2_) {
        if (getRandomEntity() != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            p_180613_1_.writeToNBTOptional(var3);
            Iterator var4 = getRandomEntity().field_98222_b.getKeySet().iterator();

            while (var4.hasNext()) {
                String var5 = (String) var4.next();
                NBTBase var6 = getRandomEntity().field_98222_b.getTag(var5);
                var3.setTag(var5, var6.copy());
            }

            p_180613_1_.readFromNBT(var3);

            if (p_180613_1_.worldObj != null && p_180613_2_) {
                p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
            }

            NBTTagCompound var12;

            for (Entity var11 = p_180613_1_; var3.hasKey("Riding", 10); var3 = var12) {
                var12 = var3.getCompoundTag("Riding");
                Entity var13 = EntityList.createEntityByName(var12.getString("id"), p_180613_1_.worldObj);

                if (var13 != null) {
                    NBTTagCompound var7 = new NBTTagCompound();
                    var13.writeToNBTOptional(var7);
                    Iterator var8 = var12.getKeySet().iterator();

                    while (var8.hasNext()) {
                        String var9 = (String) var8.next();
                        NBTBase var10 = var12.getTag(var9);
                        var7.setTag(var9, var10.copy());
                    }

                    var13.readFromNBT(var7);
                    var13.setLocationAndAngles(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);

                    if (p_180613_1_.worldObj != null && p_180613_2_) {
                        p_180613_1_.worldObj.spawnEntityInWorld(var13);
                    }

                    var11.mountEntity(var13);
                }

                var11 = var13;
            }
        } else if (p_180613_1_ instanceof EntityLivingBase && p_180613_1_.worldObj != null && p_180613_2_) {
            ((EntityLiving) p_180613_1_).func_180482_a(p_180613_1_.worldObj.getDifficultyForLocation(new BlockPos(p_180613_1_)), (IEntityLivingData) null);
            p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
        }

        return p_180613_1_;
    }

    private void resetTimer() {
        if (maxSpawnDelay <= minSpawnDelay) {
            spawnDelay = minSpawnDelay;
        } else {
            int var10003 = maxSpawnDelay - minSpawnDelay;
            spawnDelay = minSpawnDelay + getSpawnerWorld().rand.nextInt(var10003);
        }

        if (minecartToSpawn.size() > 0) {
            setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart) WeightedRandom.getRandomItem(getSpawnerWorld().rand, minecartToSpawn));
        }

        func_98267_a(1);
    }

    public void readFromNBT(NBTTagCompound p_98270_1_) {
        mobID = p_98270_1_.getString("EntityId");
        spawnDelay = p_98270_1_.getShort("Delay");
        minecartToSpawn.clear();

        if (p_98270_1_.hasKey("SpawnPotentials", 9)) {
            NBTTagList var2 = p_98270_1_.getTagList("SpawnPotentials", 10);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                minecartToSpawn.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(var2.getCompoundTagAt(var3)));
            }
        }

        if (p_98270_1_.hasKey("SpawnData", 10)) {
            setRandomEntity(new MobSpawnerBaseLogic.WeightedRandomMinecart(p_98270_1_.getCompoundTag("SpawnData"), mobID));
        } else {
            setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart) null);
        }

        if (p_98270_1_.hasKey("MinSpawnDelay", 99)) {
            minSpawnDelay = p_98270_1_.getShort("MinSpawnDelay");
            maxSpawnDelay = p_98270_1_.getShort("MaxSpawnDelay");
            spawnCount = p_98270_1_.getShort("SpawnCount");
        }

        if (p_98270_1_.hasKey("MaxNearbyEntities", 99)) {
            maxNearbyEntities = p_98270_1_.getShort("MaxNearbyEntities");
            activatingRangeFromPlayer = p_98270_1_.getShort("RequiredPlayerRange");
        }

        if (p_98270_1_.hasKey("SpawnRange", 99)) {
            spawnRange = p_98270_1_.getShort("SpawnRange");
        }

        if (getSpawnerWorld() != null) {
            cachedEntity = null;
        }
    }

    public void writeToNBT(NBTTagCompound p_98280_1_) {
        p_98280_1_.setString("EntityId", getEntityNameToSpawn());
        p_98280_1_.setShort("Delay", (short) spawnDelay);
        p_98280_1_.setShort("MinSpawnDelay", (short) minSpawnDelay);
        p_98280_1_.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
        p_98280_1_.setShort("SpawnCount", (short) spawnCount);
        p_98280_1_.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
        p_98280_1_.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
        p_98280_1_.setShort("SpawnRange", (short) spawnRange);

        if (getRandomEntity() != null) {
            p_98280_1_.setTag("SpawnData", getRandomEntity().field_98222_b.copy());
        }

        if (getRandomEntity() != null || minecartToSpawn.size() > 0) {
            NBTTagList var2 = new NBTTagList();

            if (minecartToSpawn.size() > 0) {
                Iterator var3 = minecartToSpawn.iterator();

                while (var3.hasNext()) {
                    MobSpawnerBaseLogic.WeightedRandomMinecart var4 = (MobSpawnerBaseLogic.WeightedRandomMinecart) var3.next();
                    var2.appendTag(var4.func_98220_a());
                }
            } else {
                var2.appendTag(getRandomEntity().func_98220_a());
            }

            p_98280_1_.setTag("SpawnPotentials", var2);
        }
    }

    public Entity func_180612_a(World worldIn) {
        if (cachedEntity == null) {
            Entity var2 = EntityList.createEntityByName(getEntityNameToSpawn(), worldIn);

            if (var2 != null) {
                var2 = func_180613_a(var2, false);
                cachedEntity = var2;
            }
        }

        return cachedEntity;
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int p_98268_1_) {
        if (p_98268_1_ == 1 && getSpawnerWorld().isRemote) {
            spawnDelay = minSpawnDelay;
            return true;
        } else {
            return false;
        }
    }

    private MobSpawnerBaseLogic.WeightedRandomMinecart getRandomEntity() {
        return randomEntity;
    }

    public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
        randomEntity = p_98277_1_;
    }

    public abstract void func_98267_a(int p_98267_1_);

    public abstract World getSpawnerWorld();

    public abstract BlockPos func_177221_b();

    public double func_177222_d() {
        return field_98287_c;
    }

    public double func_177223_e() {
        return field_98284_d;
    }

    public class WeightedRandomMinecart extends WeightedRandom.Item {
        private final NBTTagCompound field_98222_b;
        private final String entityType;
        private static final String __OBFID = "CL_00000130";

        public WeightedRandomMinecart(NBTTagCompound p_i1945_2_) {
            this(p_i1945_2_.getCompoundTag("Properties"), p_i1945_2_.getString("Type"), p_i1945_2_.getInteger("Weight"));
        }

        public WeightedRandomMinecart(NBTTagCompound p_i1946_2_, String p_i1946_3_) {
            this(p_i1946_2_, p_i1946_3_, 1);
        }

        private WeightedRandomMinecart(NBTTagCompound p_i45757_2_, String p_i45757_3_, int p_i45757_4_) {
            super(p_i45757_4_);

            if (p_i45757_3_.equals("Minecart")) {
                if (p_i45757_2_ != null) {
                    p_i45757_3_ = EntityMinecart.EnumMinecartType.func_180038_a(p_i45757_2_.getInteger("Type")).func_180040_b();
                } else {
                    p_i45757_3_ = "MinecartRideable";
                }
            }

            field_98222_b = p_i45757_2_;
            entityType = p_i45757_3_;
        }

        public NBTTagCompound func_98220_a() {
            NBTTagCompound var1 = new NBTTagCompound();
            var1.setTag("Properties", field_98222_b);
            var1.setString("Type", entityType);
            var1.setInteger("Weight", itemWeight);
            return var1;
        }
    }
}
