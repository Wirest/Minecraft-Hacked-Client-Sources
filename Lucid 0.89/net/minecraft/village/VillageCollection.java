package net.minecraft.village;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

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

public class VillageCollection extends WorldSavedData
{
    private World worldObj;

    /**
     * This is a black hole. You can add data to this list through a public interface, but you can't query that
     * information in any way and it's not used internally either.
     */
    private final List villagerPositionsList = Lists.newArrayList();
    private final List newDoors = Lists.newArrayList();
    private final List villageList = Lists.newArrayList();
    private int tickCounter;

    public VillageCollection(String name)
    {
        super(name);
    }

    public VillageCollection(World worldIn)
    {
        super(fileNameForProvider(worldIn.provider));
        this.worldObj = worldIn;
        this.markDirty();
    }

    public void setWorldsForAll(World worldIn)
    {
        this.worldObj = worldIn;
        Iterator var2 = this.villageList.iterator();

        while (var2.hasNext())
        {
            Village var3 = (Village)var2.next();
            var3.setWorld(worldIn);
        }
    }

    public void addToVillagerPositionList(BlockPos pos)
    {
        if (this.villagerPositionsList.size() <= 64)
        {
            if (!this.positionInList(pos))
            {
                this.villagerPositionsList.add(pos);
            }
        }
    }

    /**
     * Runs a single tick for the village collection
     */
    public void tick()
    {
        ++this.tickCounter;
        Iterator var1 = this.villageList.iterator();

        while (var1.hasNext())
        {
            Village var2 = (Village)var1.next();
            var2.tick(this.tickCounter);
        }

        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();

        if (this.tickCounter % 400 == 0)
        {
            this.markDirty();
        }
    }

    private void removeAnnihilatedVillages()
    {
        Iterator var1 = this.villageList.iterator();

        while (var1.hasNext())
        {
            Village var2 = (Village)var1.next();

            if (var2.isAnnihilated())
            {
                var1.remove();
                this.markDirty();
            }
        }
    }

    /**
     * Get a list of villages.
     */
    public List getVillageList()
    {
        return this.villageList;
    }

    public Village getNearestVillage(BlockPos doorBlock, int radius)
    {
        Village var3 = null;
        double var4 = 3.4028234663852886E38D;
        Iterator var6 = this.villageList.iterator();

        while (var6.hasNext())
        {
            Village var7 = (Village)var6.next();
            double var8 = var7.getCenter().distanceSq(doorBlock);

            if (var8 < var4)
            {
                float var10 = radius + var7.getVillageRadius();

                if (var8 <= var10 * var10)
                {
                    var3 = var7;
                    var4 = var8;
                }
            }
        }

        return var3;
    }

    private void dropOldestVillagerPosition()
    {
        if (!this.villagerPositionsList.isEmpty())
        {
            this.addDoorsAround((BlockPos)this.villagerPositionsList.remove(0));
        }
    }

    private void addNewDoorsToVillageOrCreateVillage()
    {
        for (int var1 = 0; var1 < this.newDoors.size(); ++var1)
        {
            VillageDoorInfo var2 = (VillageDoorInfo)this.newDoors.get(var1);
            Village var3 = this.getNearestVillage(var2.getDoorBlockPos(), 32);

            if (var3 == null)
            {
                var3 = new Village(this.worldObj);
                this.villageList.add(var3);
                this.markDirty();
            }

            var3.addVillageDoorInfo(var2);
        }

        this.newDoors.clear();
    }

    private void addDoorsAround(BlockPos central)
    {
        byte var2 = 16;
        byte var3 = 4;
        byte var4 = 16;

        for (int var5 = -var2; var5 < var2; ++var5)
        {
            for (int var6 = -var3; var6 < var3; ++var6)
            {
                for (int var7 = -var4; var7 < var4; ++var7)
                {
                    BlockPos var8 = central.add(var5, var6, var7);

                    if (this.isWoodDoor(var8))
                    {
                        VillageDoorInfo var9 = this.checkDoorExistence(var8);

                        if (var9 == null)
                        {
                            this.addToNewDoorsList(var8);
                        }
                        else
                        {
                            var9.func_179849_a(this.tickCounter);
                        }
                    }
                }
            }
        }
    }

    /**
     * returns the VillageDoorInfo if it exists in any village or in the newDoor list, otherwise returns null
     */
    private VillageDoorInfo checkDoorExistence(BlockPos doorBlock)
    {
        Iterator var2 = this.newDoors.iterator();
        VillageDoorInfo var3;

        do
        {
            if (!var2.hasNext())
            {
                var2 = this.villageList.iterator();
                VillageDoorInfo var4;

                do
                {
                    if (!var2.hasNext())
                    {
                        return null;
                    }

                    Village var5 = (Village)var2.next();
                    var4 = var5.getExistedDoor(doorBlock);
                }
                while (var4 == null);

                return var4;
            }

            var3 = (VillageDoorInfo)var2.next();
        }
        while (var3.getDoorBlockPos().getX() != doorBlock.getX() || var3.getDoorBlockPos().getZ() != doorBlock.getZ() || Math.abs(var3.getDoorBlockPos().getY() - doorBlock.getY()) > 1);

        return var3;
    }

    private void addToNewDoorsList(BlockPos doorBlock)
    {
        EnumFacing var2 = BlockDoor.getFacing(this.worldObj, doorBlock);
        EnumFacing var3 = var2.getOpposite();
        int var4 = this.countBlocksCanSeeSky(doorBlock, var2, 5);
        int var5 = this.countBlocksCanSeeSky(doorBlock, var3, var4 + 1);

        if (var4 != var5)
        {
            this.newDoors.add(new VillageDoorInfo(doorBlock, var4 < var5 ? var2 : var3, this.tickCounter));
        }
    }

    /**
     * Check five blocks in the direction. The centerPos will not be checked.
     */
    private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation)
    {
        int var4 = 0;

        for (int var5 = 1; var5 <= 5; ++var5)
        {
            if (this.worldObj.canSeeSky(centerPos.offset(direction, var5)))
            {
                ++var4;

                if (var4 >= limitation)
                {
                    return var4;
                }
            }
        }

        return var4;
    }

    private boolean positionInList(BlockPos pos)
    {
        Iterator var2 = this.villagerPositionsList.iterator();
        BlockPos var3;

        do
        {
            if (!var2.hasNext())
            {
                return false;
            }

            var3 = (BlockPos)var2.next();
        }
        while (!var3.equals(pos));

        return true;
    }

    private boolean isWoodDoor(BlockPos doorPos)
    {
        Block var2 = this.worldObj.getBlockState(doorPos).getBlock();
        return var2 instanceof BlockDoor ? var2.getMaterial() == Material.wood : false;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    @Override
	public void readFromNBT(NBTTagCompound nbt)
    {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList var2 = nbt.getTagList("Villages", 10);

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            Village var5 = new Village();
            var5.readVillageDataFromNBT(var4);
            this.villageList.add(var5);
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    @Override
	public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("Tick", this.tickCounter);
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = this.villageList.iterator();

        while (var3.hasNext())
        {
            Village var4 = (Village)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var4.writeVillageDataToNBT(var5);
            var2.appendTag(var5);
        }

        nbt.setTag("Villages", var2);
    }

    public static String fileNameForProvider(WorldProvider provider)
    {
        return "villages" + provider.getInternalNameSuffix();
    }
}
