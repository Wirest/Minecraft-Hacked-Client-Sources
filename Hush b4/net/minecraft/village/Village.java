// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTBase;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.nbt.NBTTagList;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.util.Vec3i;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import com.google.common.collect.Lists;
import java.util.TreeMap;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.minecraft.world.World;

public class Village
{
    private World worldObj;
    private final List<VillageDoorInfo> villageDoorInfoList;
    private BlockPos centerHelper;
    private BlockPos center;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private TreeMap<String, Integer> playerReputation;
    private List<VillageAggressor> villageAgressors;
    private int numIronGolems;
    
    public Village() {
        this.villageDoorInfoList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = new TreeMap<String, Integer>();
        this.villageAgressors = (List<VillageAggressor>)Lists.newArrayList();
    }
    
    public Village(final World worldIn) {
        this.villageDoorInfoList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = new TreeMap<String, Integer>();
        this.villageAgressors = (List<VillageAggressor>)Lists.newArrayList();
        this.worldObj = worldIn;
    }
    
    public void setWorld(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public void tick(final int p_75560_1_) {
        this.tickCounter = p_75560_1_;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (p_75560_1_ % 20 == 0) {
            this.updateNumVillagers();
        }
        if (p_75560_1_ % 30 == 0) {
            this.updateNumIronGolems();
        }
        final int i = this.numVillagers / 10;
        if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0) {
            final Vec3 vec3 = this.func_179862_a(this.center, 2, 4, 2);
            if (vec3 != null) {
                final EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
                entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
                this.worldObj.spawnEntityInWorld(entityirongolem);
                ++this.numIronGolems;
            }
        }
    }
    
    private Vec3 func_179862_a(final BlockPos p_179862_1_, final int p_179862_2_, final int p_179862_3_, final int p_179862_4_) {
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos = p_179862_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
            if (this.func_179866_a(blockpos) && this.func_179861_a(new BlockPos(p_179862_2_, p_179862_3_, p_179862_4_), blockpos)) {
                return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        return null;
    }
    
    private boolean func_179861_a(final BlockPos p_179861_1_, final BlockPos p_179861_2_) {
        if (!World.doesBlockHaveSolidTopSurface(this.worldObj, p_179861_2_.down())) {
            return false;
        }
        final int i = p_179861_2_.getX() - p_179861_1_.getX() / 2;
        final int j = p_179861_2_.getZ() - p_179861_1_.getZ() / 2;
        for (int k = i; k < i + p_179861_1_.getX(); ++k) {
            for (int l = p_179861_2_.getY(); l < p_179861_2_.getY() + p_179861_1_.getY(); ++l) {
                for (int i2 = j; i2 < j + p_179861_1_.getZ(); ++i2) {
                    if (this.worldObj.getBlockState(new BlockPos(k, l, i2)).getBlock().isNormalCube()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void updateNumIronGolems() {
        final List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityIronGolem>)EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numIronGolems = list.size();
    }
    
    private void updateNumVillagers() {
        final List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityVillager>)EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numVillagers = list.size();
        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }
    }
    
    public BlockPos getCenter() {
        return this.center;
    }
    
    public int getVillageRadius() {
        return this.villageRadius;
    }
    
    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }
    
    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }
    
    public int getNumVillagers() {
        return this.numVillagers;
    }
    
    public boolean func_179866_a(final BlockPos pos) {
        return this.center.distanceSq(pos) < this.villageRadius * this.villageRadius;
    }
    
    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }
    
    public VillageDoorInfo getNearestDoor(final BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : this.villageDoorInfoList) {
            final int j = villagedoorinfo2.getDistanceToDoorBlockSq(pos);
            if (j < i) {
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    public VillageDoorInfo getDoorInfo(final BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : this.villageDoorInfoList) {
            int j = villagedoorinfo2.getDistanceToDoorBlockSq(pos);
            if (j > 256) {
                j *= 1000;
            }
            else {
                j = villagedoorinfo2.getDoorOpeningRestrictionCounter();
            }
            if (j < i) {
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    public VillageDoorInfo getExistedDoor(final BlockPos doorBlock) {
        if (this.center.distanceSq(doorBlock) > this.villageRadius * this.villageRadius) {
            return null;
        }
        for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
                return villagedoorinfo;
            }
        }
        return null;
    }
    
    public void addVillageDoorInfo(final VillageDoorInfo doorInfo) {
        this.villageDoorInfoList.add(doorInfo);
        this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
    }
    
    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }
    
    public void addOrRenewAgressor(final EntityLivingBase entitylivingbaseIn) {
        for (final VillageAggressor village$villageaggressor : this.villageAgressors) {
            if (village$villageaggressor.agressor == entitylivingbaseIn) {
                village$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }
        this.villageAgressors.add(new VillageAggressor(entitylivingbaseIn, this.tickCounter));
    }
    
    public EntityLivingBase findNearestVillageAggressor(final EntityLivingBase entitylivingbaseIn) {
        double d0 = Double.MAX_VALUE;
        VillageAggressor village$villageaggressor = null;
        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            final VillageAggressor village$villageaggressor2 = this.villageAgressors.get(i);
            final double d2 = village$villageaggressor2.agressor.getDistanceSqToEntity(entitylivingbaseIn);
            if (d2 <= d0) {
                village$villageaggressor = village$villageaggressor2;
                d0 = d2;
            }
        }
        return (village$villageaggressor != null) ? village$villageaggressor.agressor : null;
    }
    
    public EntityPlayer getNearestTargetPlayer(final EntityLivingBase villageDefender) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;
        for (final String s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationTooLow(s)) {
                final EntityPlayer entityplayer2 = this.worldObj.getPlayerEntityByName(s);
                if (entityplayer2 == null) {
                    continue;
                }
                final double d2 = entityplayer2.getDistanceSqToEntity(villageDefender);
                if (d2 > d0) {
                    continue;
                }
                entityplayer = entityplayer2;
                d0 = d2;
            }
        }
        return entityplayer;
    }
    
    private void removeDeadAndOldAgressors() {
        final Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
        while (iterator.hasNext()) {
            final VillageAggressor village$villageaggressor = iterator.next();
            if (!village$villageaggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300) {
                iterator.remove();
            }
        }
    }
    
    private void removeDeadAndOutOfRangeDoors() {
        boolean flag = false;
        final boolean flag2 = this.worldObj.rand.nextInt(50) == 0;
        final Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (iterator.hasNext()) {
            final VillageDoorInfo villagedoorinfo = iterator.next();
            if (flag2) {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }
            if (!this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) > 1200) {
                this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
                flag = true;
                villagedoorinfo.setIsDetachedFromVillageFlag(true);
                iterator.remove();
            }
        }
        if (flag) {
            this.updateVillageRadiusAndCenter();
        }
    }
    
    private boolean isWoodDoor(final BlockPos pos) {
        final Block block = this.worldObj.getBlockState(pos).getBlock();
        return block instanceof BlockDoor && block.getMaterial() == Material.wood;
    }
    
    private void updateVillageRadiusAndCenter() {
        final int i = this.villageDoorInfoList.size();
        if (i == 0) {
            this.center = new BlockPos(0, 0, 0);
            this.villageRadius = 0;
        }
        else {
            this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
            int j = 0;
            for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
                j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
            }
            this.villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
        }
    }
    
    public int getReputationForPlayer(final String p_82684_1_) {
        final Integer integer = this.playerReputation.get(p_82684_1_);
        return (integer != null) ? integer : 0;
    }
    
    public int setReputationForPlayer(final String p_82688_1_, final int p_82688_2_) {
        final int i = this.getReputationForPlayer(p_82688_1_);
        final int j = MathHelper.clamp_int(i + p_82688_2_, -30, 10);
        this.playerReputation.put(p_82688_1_, j);
        return j;
    }
    
    public boolean isPlayerReputationTooLow(final String p_82687_1_) {
        return this.getReputationForPlayer(p_82687_1_) <= -15;
    }
    
    public void readVillageDataFromNBT(final NBTTagCompound p_82690_1_) {
        this.numVillagers = p_82690_1_.getInteger("PopSize");
        this.villageRadius = p_82690_1_.getInteger("Radius");
        this.numIronGolems = p_82690_1_.getInteger("Golems");
        this.lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
        this.tickCounter = p_82690_1_.getInteger("Tick");
        this.noBreedTicks = p_82690_1_.getInteger("MTick");
        this.center = new BlockPos(p_82690_1_.getInteger("CX"), p_82690_1_.getInteger("CY"), p_82690_1_.getInteger("CZ"));
        this.centerHelper = new BlockPos(p_82690_1_.getInteger("ACX"), p_82690_1_.getInteger("ACY"), p_82690_1_.getInteger("ACZ"));
        final NBTTagList nbttaglist = p_82690_1_.getTagList("Doors", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }
        final NBTTagList nbttaglist2 = p_82690_1_.getTagList("Players", 10);
        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
            final NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(j);
            if (nbttagcompound2.hasKey("UUID")) {
                final PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
                final GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound2.getString("UUID")));
                if (gameprofile != null) {
                    this.playerReputation.put(gameprofile.getName(), nbttagcompound2.getInteger("S"));
                }
            }
            else {
                this.playerReputation.put(nbttagcompound2.getString("Name"), nbttagcompound2.getInteger("S"));
            }
        }
    }
    
    public void writeVillageDataToNBT(final NBTTagCompound p_82689_1_) {
        p_82689_1_.setInteger("PopSize", this.numVillagers);
        p_82689_1_.setInteger("Radius", this.villageRadius);
        p_82689_1_.setInteger("Golems", this.numIronGolems);
        p_82689_1_.setInteger("Stable", this.lastAddDoorTimestamp);
        p_82689_1_.setInteger("Tick", this.tickCounter);
        p_82689_1_.setInteger("MTick", this.noBreedTicks);
        p_82689_1_.setInteger("CX", this.center.getX());
        p_82689_1_.setInteger("CY", this.center.getY());
        p_82689_1_.setInteger("CZ", this.center.getZ());
        p_82689_1_.setInteger("ACX", this.centerHelper.getX());
        p_82689_1_.setInteger("ACY", this.centerHelper.getY());
        p_82689_1_.setInteger("ACZ", this.centerHelper.getZ());
        final NBTTagList nbttaglist = new NBTTagList();
        for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
            nbttaglist.appendTag(nbttagcompound);
        }
        p_82689_1_.setTag("Doors", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final String s : this.playerReputation.keySet()) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            final PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
            final GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
            if (gameprofile != null) {
                nbttagcompound2.setString("UUID", gameprofile.getId().toString());
                nbttagcompound2.setInteger("S", this.playerReputation.get(s));
                nbttaglist2.appendTag(nbttagcompound2);
            }
        }
        p_82689_1_.setTag("Players", nbttaglist2);
    }
    
    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }
    
    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }
    
    public void setDefaultPlayerReputation(final int p_82683_1_) {
        for (final String s : this.playerReputation.keySet()) {
            this.setReputationForPlayer(s, p_82683_1_);
        }
    }
    
    class VillageAggressor
    {
        public EntityLivingBase agressor;
        public int agressionTime;
        
        VillageAggressor(final EntityLivingBase p_i1674_2_, final int p_i1674_3_) {
            this.agressor = p_i1674_2_;
            this.agressionTime = p_i1674_3_;
        }
    }
}
