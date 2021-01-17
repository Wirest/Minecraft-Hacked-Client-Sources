// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagList;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.List;

public abstract class MobSpawnerBaseLogic
{
    private int spawnDelay;
    private String mobID;
    private final List<WeightedRandomMinecart> minecartToSpawn;
    private WeightedRandomMinecart randomEntity;
    private double mobRotation;
    private double prevMobRotation;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int spawnCount;
    private Entity cachedEntity;
    private int maxNearbyEntities;
    private int activatingRangeFromPlayer;
    private int spawnRange;
    
    public MobSpawnerBaseLogic() {
        this.spawnDelay = 20;
        this.mobID = "Pig";
        this.minecartToSpawn = (List<WeightedRandomMinecart>)Lists.newArrayList();
        this.minSpawnDelay = 200;
        this.maxSpawnDelay = 800;
        this.spawnCount = 4;
        this.maxNearbyEntities = 6;
        this.activatingRangeFromPlayer = 16;
        this.spawnRange = 4;
    }
    
    private String getEntityNameToSpawn() {
        if (this.getRandomEntity() == null) {
            if (this.mobID != null && this.mobID.equals("Minecart")) {
                this.mobID = "MinecartRideable";
            }
            return this.mobID;
        }
        return this.getRandomEntity().entityType;
    }
    
    public void setEntityName(final String name) {
        this.mobID = name;
    }
    
    private boolean isActivated() {
        final BlockPos blockpos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5, this.activatingRangeFromPlayer);
    }
    
    public void updateSpawner() {
        if (this.isActivated()) {
            final BlockPos blockpos = this.getSpawnerPosition();
            if (this.getSpawnerWorld().isRemote) {
                final double d3 = blockpos.getX() + this.getSpawnerWorld().rand.nextFloat();
                final double d4 = blockpos.getY() + this.getSpawnerWorld().rand.nextFloat();
                final double d5 = blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + 1000.0f / (this.spawnDelay + 200.0f)) % 360.0;
            }
            else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < this.spawnCount; ++i) {
                    final Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                    if (entity == null) {
                        return;
                    }
                    final int j = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
                    if (j >= this.maxNearbyEntities) {
                        this.resetTimer();
                        return;
                    }
                    final double d6 = blockpos.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5;
                    final double d7 = blockpos.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1;
                    final double d8 = blockpos.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5;
                    final EntityLiving entityliving = (entity instanceof EntityLiving) ? ((EntityLiving)entity) : null;
                    entity.setLocationAndAngles(d6, d7, d8, this.getSpawnerWorld().rand.nextFloat() * 360.0f, 0.0f);
                    if (entityliving == null || (entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
                        this.spawnNewEntity(entity, true);
                        this.getSpawnerWorld().playAuxSFX(2004, blockpos, 0);
                        if (entityliving != null) {
                            entityliving.spawnExplosionParticle();
                        }
                        flag = true;
                    }
                }
                if (flag) {
                    this.resetTimer();
                }
            }
        }
    }
    
    private Entity spawnNewEntity(final Entity entityIn, final boolean spawn) {
        if (this.getRandomEntity() != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entityIn.writeToNBTOptional(nbttagcompound);
            for (final String s : this.getRandomEntity().nbtData.getKeySet()) {
                final NBTBase nbtbase = this.getRandomEntity().nbtData.getTag(s);
                nbttagcompound.setTag(s, nbtbase.copy());
            }
            entityIn.readFromNBT(nbttagcompound);
            if (entityIn.worldObj != null && spawn) {
                entityIn.worldObj.spawnEntityInWorld(entityIn);
            }
            Entity entity = entityIn;
            while (nbttagcompound.hasKey("Riding", 10)) {
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
                final Entity entity2 = EntityList.createEntityByName(nbttagcompound2.getString("id"), entityIn.worldObj);
                if (entity2 != null) {
                    final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                    entity2.writeToNBTOptional(nbttagcompound3);
                    for (final String s2 : nbttagcompound2.getKeySet()) {
                        final NBTBase nbtbase2 = nbttagcompound2.getTag(s2);
                        nbttagcompound3.setTag(s2, nbtbase2.copy());
                    }
                    entity2.readFromNBT(nbttagcompound3);
                    entity2.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    if (entityIn.worldObj != null && spawn) {
                        entityIn.worldObj.spawnEntityInWorld(entity2);
                    }
                    entity.mountEntity(entity2);
                }
                entity = entity2;
                nbttagcompound = nbttagcompound2;
            }
        }
        else if (entityIn instanceof EntityLivingBase && entityIn.worldObj != null && spawn) {
            if (entityIn instanceof EntityLiving) {
                ((EntityLiving)entityIn).onInitialSpawn(entityIn.worldObj.getDifficultyForLocation(new BlockPos(entityIn)), null);
            }
            entityIn.worldObj.spawnEntityInWorld(entityIn);
        }
        return entityIn;
    }
    
    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        }
        else {
            final int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }
        if (this.minecartToSpawn.size() > 0) {
            this.setRandomEntity(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }
        this.func_98267_a(1);
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        this.mobID = nbt.getString("EntityId");
        this.spawnDelay = nbt.getShort("Delay");
        this.minecartToSpawn.clear();
        if (nbt.hasKey("SpawnPotentials", 9)) {
            final NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.minecartToSpawn.add(new WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
            }
        }
        if (nbt.hasKey("SpawnData", 10)) {
            this.setRandomEntity(new WeightedRandomMinecart(nbt.getCompoundTag("SpawnData"), this.mobID));
        }
        else {
            this.setRandomEntity(null);
        }
        if (nbt.hasKey("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }
        if (nbt.hasKey("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }
        if (nbt.hasKey("SpawnRange", 99)) {
            this.spawnRange = nbt.getShort("SpawnRange");
        }
        if (this.getSpawnerWorld() != null) {
            this.cachedEntity = null;
        }
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        final String s = this.getEntityNameToSpawn();
        if (!StringUtils.isNullOrEmpty(s)) {
            nbt.setString("EntityId", s);
            nbt.setShort("Delay", (short)this.spawnDelay);
            nbt.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
            nbt.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            nbt.setShort("SpawnCount", (short)this.spawnCount);
            nbt.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            nbt.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
            nbt.setShort("SpawnRange", (short)this.spawnRange);
            if (this.getRandomEntity() != null) {
                nbt.setTag("SpawnData", this.getRandomEntity().nbtData.copy());
            }
            if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
                final NBTTagList nbttaglist = new NBTTagList();
                if (this.minecartToSpawn.size() > 0) {
                    for (final WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn) {
                        nbttaglist.appendTag(mobspawnerbaselogic$weightedrandomminecart.toNBT());
                    }
                }
                else {
                    nbttaglist.appendTag(this.getRandomEntity().toNBT());
                }
                nbt.setTag("SpawnPotentials", nbttaglist);
            }
        }
    }
    
    public Entity func_180612_a(final World worldIn) {
        if (this.cachedEntity == null) {
            Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), worldIn);
            if (entity != null) {
                entity = this.spawnNewEntity(entity, false);
                this.cachedEntity = entity;
            }
        }
        return this.cachedEntity;
    }
    
    public boolean setDelayToMin(final int delay) {
        if (delay == 1 && this.getSpawnerWorld().isRemote) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }
    
    private WeightedRandomMinecart getRandomEntity() {
        return this.randomEntity;
    }
    
    public void setRandomEntity(final WeightedRandomMinecart p_98277_1_) {
        this.randomEntity = p_98277_1_;
    }
    
    public abstract void func_98267_a(final int p0);
    
    public abstract World getSpawnerWorld();
    
    public abstract BlockPos getSpawnerPosition();
    
    public double getMobRotation() {
        return this.mobRotation;
    }
    
    public double getPrevMobRotation() {
        return this.prevMobRotation;
    }
    
    public class WeightedRandomMinecart extends WeightedRandom.Item
    {
        private final NBTTagCompound nbtData;
        private final String entityType;
        
        public WeightedRandomMinecart(final MobSpawnerBaseLogic mobSpawnerBaseLogic, final NBTTagCompound tagCompound) {
            this(mobSpawnerBaseLogic, tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
        }
        
        public WeightedRandomMinecart(final MobSpawnerBaseLogic mobSpawnerBaseLogic, final NBTTagCompound tagCompound, final String type) {
            this(mobSpawnerBaseLogic, tagCompound, type, 1);
        }
        
        private WeightedRandomMinecart(final NBTTagCompound tagCompound, String type, final int weight) {
            super(weight);
            if (type.equals("Minecart")) {
                if (tagCompound != null) {
                    type = EntityMinecart.EnumMinecartType.byNetworkID(tagCompound.getInteger("Type")).getName();
                }
                else {
                    type = "MinecartRideable";
                }
            }
            this.nbtData = tagCompound;
            this.entityType = type;
        }
        
        public NBTTagCompound toNBT() {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("Properties", this.nbtData);
            nbttagcompound.setString("Type", this.entityType);
            nbttagcompound.setInteger("Weight", this.itemWeight);
            return nbttagcompound;
        }
    }
}
