package net.minecraft.village;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;

public class VillageCollection extends WorldSavedData {
    private World worldObj;

    /**
     * This is a black hole. You can add data to this list through a public interface, but you can't query that
     * information in any way and it's not used internally either.
     */
    private final List villagerPositionsList = Lists.newArrayList();
    private final List newDoors = Lists.newArrayList();
    private final List villageList = Lists.newArrayList();
    private int tickCounter;
    private static final String __OBFID = "CL_00001635";

    public VillageCollection(String p_i1677_1_) {
        super(p_i1677_1_);
    }

    public VillageCollection(World worldIn) {
        super(func_176062_a(worldIn.provider));
        this.worldObj = worldIn;
        this.markDirty();
    }

    public void func_82566_a(World worldIn) {
        this.worldObj = worldIn;
        Iterator var2 = this.villageList.iterator();

        while (var2.hasNext()) {
            Village var3 = (Village) var2.next();
            var3.func_82691_a(worldIn);
        }
    }

    public void func_176060_a(BlockPos p_176060_1_) {
        if (this.villagerPositionsList.size() <= 64) {
            if (!this.func_176057_e(p_176060_1_)) {
                this.villagerPositionsList.add(p_176060_1_);
            }
        }
    }

    /**
     * Runs a single tick for the village collection
     */
    public void tick() {
        ++this.tickCounter;
        Iterator var1 = this.villageList.iterator();

        while (var1.hasNext()) {
            Village var2 = (Village) var1.next();
            var2.tick(this.tickCounter);
        }

        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();

        if (this.tickCounter % 400 == 0) {
            this.markDirty();
        }
    }

    private void removeAnnihilatedVillages() {
        Iterator var1 = this.villageList.iterator();

        while (var1.hasNext()) {
            Village var2 = (Village) var1.next();

            if (var2.isAnnihilated()) {
                var1.remove();
                this.markDirty();
            }
        }
    }

    /**
     * Get a list of villages.
     */
    public List getVillageList() {
        return this.villageList;
    }

    public Village func_176056_a(BlockPos p_176056_1_, int p_176056_2_) {
        Village var3 = null;
        double var4 = 3.4028234663852886E38D;
        Iterator var6 = this.villageList.iterator();

        while (var6.hasNext()) {
            Village var7 = (Village) var6.next();
            double var8 = var7.func_180608_a().distanceSq(p_176056_1_);

            if (var8 < var4) {
                float var10 = (float) (p_176056_2_ + var7.getVillageRadius());

                if (var8 <= (double) (var10 * var10)) {
                    var3 = var7;
                    var4 = var8;
                }
            }
        }

        return var3;
    }

    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.func_180609_b((BlockPos) this.villagerPositionsList.remove(0));
        }
    }

    private void addNewDoorsToVillageOrCreateVillage() {
        for (int var1 = 0; var1 < this.newDoors.size(); ++var1) {
            VillageDoorInfo var2 = (VillageDoorInfo) this.newDoors.get(var1);
            Village var3 = this.func_176056_a(var2.func_179852_d(), 32);

            if (var3 == null) {
                var3 = new Village(this.worldObj);
                this.villageList.add(var3);
                this.markDirty();
            }

            var3.addVillageDoorInfo(var2);
        }

        this.newDoors.clear();
    }

    private void func_180609_b(BlockPos p_180609_1_) {
        byte var2 = 16;
        byte var3 = 4;
        byte var4 = 16;

        for (int var5 = -var2; var5 < var2; ++var5) {
            for (int var6 = -var3; var6 < var3; ++var6) {
                for (int var7 = -var4; var7 < var4; ++var7) {
                    BlockPos var8 = p_180609_1_.add(var5, var6, var7);

                    if (this.func_176058_f(var8)) {
                        VillageDoorInfo var9 = this.func_176055_c(var8);

                        if (var9 == null) {
                            this.func_176059_d(var8);
                        } else {
                            var9.func_179849_a(this.tickCounter);
                        }
                    }
                }
            }
        }
    }

    private VillageDoorInfo func_176055_c(BlockPos p_176055_1_) {
        Iterator var2 = this.newDoors.iterator();
        VillageDoorInfo var3;

        do {
            if (!var2.hasNext()) {
                var2 = this.villageList.iterator();
                VillageDoorInfo var4;

                do {
                    if (!var2.hasNext()) {
                        return null;
                    }

                    Village var5 = (Village) var2.next();
                    var4 = var5.func_179864_e(p_176055_1_);
                }
                while (var4 == null);

                return var4;
            }

            var3 = (VillageDoorInfo) var2.next();
        }
        while (var3.func_179852_d().getX() != p_176055_1_.getX() || var3.func_179852_d().getZ() != p_176055_1_.getZ() || Math.abs(var3.func_179852_d().getY() - p_176055_1_.getY()) > 1);

        return var3;
    }

    private void func_176059_d(BlockPos p_176059_1_) {
        EnumFacing var2 = BlockDoor.func_176517_h(this.worldObj, p_176059_1_);
        EnumFacing var3 = var2.getOpposite();
        int var4 = this.func_176061_a(p_176059_1_, var2, 5);
        int var5 = this.func_176061_a(p_176059_1_, var3, var4 + 1);

        if (var4 != var5) {
            this.newDoors.add(new VillageDoorInfo(p_176059_1_, var4 < var5 ? var2 : var3, this.tickCounter));
        }
    }

    private int func_176061_a(BlockPos p_176061_1_, EnumFacing p_176061_2_, int p_176061_3_) {
        int var4 = 0;

        for (int var5 = 1; var5 <= 5; ++var5) {
            if (this.worldObj.isAgainstSky(p_176061_1_.offset(p_176061_2_, var5))) {
                ++var4;

                if (var4 >= p_176061_3_) {
                    return var4;
                }
            }
        }

        return var4;
    }

    private boolean func_176057_e(BlockPos p_176057_1_) {
        Iterator var2 = this.villagerPositionsList.iterator();
        BlockPos var3;

        do {
            if (!var2.hasNext()) {
                return false;
            }

            var3 = (BlockPos) var2.next();
        }
        while (!var3.equals(p_176057_1_));

        return true;
    }

    private boolean func_176058_f(BlockPos p_176058_1_) {
        Block var2 = this.worldObj.getBlockState(p_176058_1_).getBlock();
        return var2 instanceof BlockDoor ? var2.getMaterial() == Material.wood : false;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList var2 = nbt.getTagList("Villages", 10);

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            Village var5 = new Village();
            var5.readVillageDataFromNBT(var4);
            this.villageList.add(var5);
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Tick", this.tickCounter);
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = this.villageList.iterator();

        while (var3.hasNext()) {
            Village var4 = (Village) var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var4.writeVillageDataToNBT(var5);
            var2.appendTag(var5);
        }

        nbt.setTag("Villages", var2);
    }

    public static String func_176062_a(WorldProvider p_176062_0_) {
        return "villages" + p_176062_0_.getInternalNameSuffix();
    }
}
