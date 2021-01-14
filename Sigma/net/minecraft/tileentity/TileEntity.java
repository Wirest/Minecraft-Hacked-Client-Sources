package net.minecraft.tileentity;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
    private static final Logger logger = LogManager.getLogger();

    /**
     * A HashMap storing string names of classes mapping to the actual
     * java.lang.Class type.
     */
    private static Map nameToClassMap = Maps.newHashMap();

    /**
     * A HashMap storing the classes and mapping to the string names (reverse of
     * nameToClassMap).
     */
    private static Map classToNameMap = Maps.newHashMap();

    /**
     * the instance of the world the tile entity is in.
     */
    protected World worldObj;
    protected BlockPos pos;
    protected boolean tileEntityInvalid;
    private int blockMetadata;

    /**
     * the Block type that this TileEntity is contained within
     */
    protected Block blockType;
    private static final String __OBFID = "CL_00000340";

    public TileEntity() {
        pos = BlockPos.ORIGIN;
        blockMetadata = -1;
    }

    /**
     * Adds a new two-way mapping between the class and its string name in both
     * hashmaps.
     */
    private static void addMapping(Class cl, String id) {
        if (TileEntity.nameToClassMap.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id: " + id);
        } else {
            TileEntity.nameToClassMap.put(id, cl);
            TileEntity.classToNameMap.put(cl, id);
        }
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorld() {
        return worldObj;
    }

    /**
     * Sets the worldObj for this tileEntity.
     */
    public void setWorldObj(World worldIn) {
        worldObj = worldIn;
    }

    /**
     * Returns true if the worldObj isn't null.
     */
    public boolean hasWorldObj() {
        return worldObj != null;
    }

    public void readFromNBT(NBTTagCompound compound) {
        pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }

    public void writeToNBT(NBTTagCompound compound) {
        String var2 = (String) TileEntity.classToNameMap.get(this.getClass());

        if (var2 == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            compound.setString("id", var2);
            compound.setInteger("x", pos.getX());
            compound.setInteger("y", pos.getY());
            compound.setInteger("z", pos.getZ());
        }
    }

    /**
     * Creates a new entity and loads its data from the specified NBT.
     */
    public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
        TileEntity var1 = null;

        try {
            Class var2 = (Class) TileEntity.nameToClassMap.get(nbt.getString("id"));

            if (var2 != null) {
                var1 = (TileEntity) var2.newInstance();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        if (var1 != null) {
            var1.readFromNBT(nbt);
        } else {
            TileEntity.logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
        }

        return var1;
    }

    public int getBlockMetadata() {
        if (blockMetadata == -1) {
            IBlockState var1 = worldObj.getBlockState(pos);
            blockMetadata = var1.getBlock().getMetaFromState(var1);
        }

        return blockMetadata;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved
     * to disk later - the game won't think it hasn't changed and skip it.
     */
    public void markDirty() {
        if (worldObj != null) {
            IBlockState var1 = worldObj.getBlockState(pos);
            blockMetadata = var1.getBlock().getMetaFromState(var1);
            worldObj.func_175646_b(pos, this);

            if (getBlockType() != Blocks.air) {
                worldObj.updateComparatorOutputLevel(pos, getBlockType());
            }
        }
    }

    /**
     * Returns the square of the distance between this entity and the passed in
     * coordinates.
     */
    public double getDistanceSq(double p_145835_1_, double p_145835_3_, double p_145835_5_) {
        double var7 = pos.getX() + 0.5D - p_145835_1_;
        double var9 = pos.getY() + 0.5D - p_145835_3_;
        double var11 = pos.getZ() + 0.5D - p_145835_5_;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double getMaxRenderDistanceSquared() {
        return 4096.0D;
    }

    public BlockPos getPos() {
        return pos;
    }

    /**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType() {
        if (blockType == null) {
            blockType = worldObj.getBlockState(pos).getBlock();
        }

        return blockType;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket() {
        return null;
    }

    public boolean isInvalid() {
        return tileEntityInvalid;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate() {
        tileEntityInvalid = true;
    }

    /**
     * validates a tile entity
     */
    public void validate() {
        tileEntityInvalid = false;
    }

    public boolean receiveClientEvent(int id, int type) {
        return false;
    }

    public void updateContainingBlockInfo() {
        blockType = null;
        blockMetadata = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory reportCategory) {
        reportCategory.addCrashSectionCallable("Name", new Callable() {
            private static final String __OBFID = "CL_00000341";

            @Override
            public String call() {
                return (String) TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });

        if (worldObj != null) {
            CrashReportCategory.addBlockInfo(reportCategory, pos, getBlockType(), getBlockMetadata());
            reportCategory.addCrashSectionCallable("Actual block type", new Callable() {
                private static final String __OBFID = "CL_00000343";

                @Override
                public String call() {
                    int var1 = Block.getIdFromBlock(worldObj.getBlockState(pos).getBlock());

                    try {
                        return String.format("ID #%d (%s // %s)", new Object[]{Integer.valueOf(var1), Block.getBlockById(var1).getUnlocalizedName(), Block.getBlockById(var1).getClass().getCanonicalName()});
                    } catch (Throwable var3) {
                        return "ID #" + var1;
                    }
                }
            });
            reportCategory.addCrashSectionCallable("Actual block data value", new Callable() {
                private static final String __OBFID = "CL_00000344";

                @Override
                public String call() {
                    IBlockState var1 = worldObj.getBlockState(pos);
                    int var2 = var1.getBlock().getMetaFromState(var1);

                    if (var2 < 0) {
                        return "Unknown? (Got " + var2 + ")";
                    } else {
                        String var3 = String.format("%4s", new Object[]{Integer.toBinaryString(var2)}).replace(" ", "0");
                        return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[]{Integer.valueOf(var2), var3});
                    }
                }
            });
        }
    }

    public void setPos(BlockPos posIn) {
        pos = posIn;
    }

    static {
        TileEntity.addMapping(TileEntityFurnace.class, "Furnace");
        TileEntity.addMapping(TileEntityChest.class, "Chest");
        TileEntity.addMapping(TileEntityEnderChest.class, "EnderChest");
        TileEntity.addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
        TileEntity.addMapping(TileEntityDispenser.class, "Trap");
        TileEntity.addMapping(TileEntityDropper.class, "Dropper");
        TileEntity.addMapping(TileEntitySign.class, "Sign");
        TileEntity.addMapping(TileEntityMobSpawner.class, "MobSpawner");
        TileEntity.addMapping(TileEntityNote.class, "Music");
        TileEntity.addMapping(TileEntityPiston.class, "Piston");
        TileEntity.addMapping(TileEntityBrewingStand.class, "Cauldron");
        TileEntity.addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
        TileEntity.addMapping(TileEntityEndPortal.class, "Airportal");
        TileEntity.addMapping(TileEntityCommandBlock.class, "Control");
        TileEntity.addMapping(TileEntityBeacon.class, "Beacon");
        TileEntity.addMapping(TileEntitySkull.class, "Skull");
        TileEntity.addMapping(TileEntityDaylightDetector.class, "DLDetector");
        TileEntity.addMapping(TileEntityHopper.class, "Hopper");
        TileEntity.addMapping(TileEntityComparator.class, "Comparator");
        TileEntity.addMapping(TileEntityFlowerPot.class, "FlowerPot");
        TileEntity.addMapping(TileEntityBanner.class, "Banner");
    }
}
