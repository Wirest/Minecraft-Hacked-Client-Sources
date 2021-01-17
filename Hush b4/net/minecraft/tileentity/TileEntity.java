// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.Packet;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.BlockJukebox;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity
{
    private static final Logger logger;
    private static Map<String, Class<? extends TileEntity>> nameToClassMap;
    private static Map<Class<? extends TileEntity>, String> classToNameMap;
    protected World worldObj;
    protected BlockPos pos;
    protected boolean tileEntityInvalid;
    private int blockMetadata;
    protected Block blockType;
    
    static {
        logger = LogManager.getLogger();
        TileEntity.nameToClassMap = (Map<String, Class<? extends TileEntity>>)Maps.newHashMap();
        TileEntity.classToNameMap = (Map<Class<? extends TileEntity>, String>)Maps.newHashMap();
        addMapping(TileEntityFurnace.class, "Furnace");
        addMapping(TileEntityChest.class, "Chest");
        addMapping(TileEntityEnderChest.class, "EnderChest");
        addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
        addMapping(TileEntityDispenser.class, "Trap");
        addMapping(TileEntityDropper.class, "Dropper");
        addMapping(TileEntitySign.class, "Sign");
        addMapping(TileEntityMobSpawner.class, "MobSpawner");
        addMapping(TileEntityNote.class, "Music");
        addMapping(TileEntityPiston.class, "Piston");
        addMapping(TileEntityBrewingStand.class, "Cauldron");
        addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
        addMapping(TileEntityEndPortal.class, "Airportal");
        addMapping(TileEntityCommandBlock.class, "Control");
        addMapping(TileEntityBeacon.class, "Beacon");
        addMapping(TileEntitySkull.class, "Skull");
        addMapping(TileEntityDaylightDetector.class, "DLDetector");
        addMapping(TileEntityHopper.class, "Hopper");
        addMapping(TileEntityComparator.class, "Comparator");
        addMapping(TileEntityFlowerPot.class, "FlowerPot");
        addMapping(TileEntityBanner.class, "Banner");
    }
    
    public TileEntity() {
        this.pos = BlockPos.ORIGIN;
        this.blockMetadata = -1;
    }
    
    private static void addMapping(final Class<? extends TileEntity> cl, final String id) {
        if (TileEntity.nameToClassMap.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id: " + id);
        }
        TileEntity.nameToClassMap.put(id, cl);
        TileEntity.classToNameMap.put(cl, id);
    }
    
    public World getWorld() {
        return this.worldObj;
    }
    
    public void setWorldObj(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public boolean hasWorldObj() {
        return this.worldObj != null;
    }
    
    public void readFromNBT(final NBTTagCompound compound) {
        this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }
    
    public void writeToNBT(final NBTTagCompound compound) {
        final String s = TileEntity.classToNameMap.get(this.getClass());
        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        compound.setString("id", s);
        compound.setInteger("x", this.pos.getX());
        compound.setInteger("y", this.pos.getY());
        compound.setInteger("z", this.pos.getZ());
    }
    
    public static TileEntity createAndLoadEntity(final NBTTagCompound nbt) {
        TileEntity tileentity = null;
        try {
            final Class<? extends TileEntity> oclass = TileEntity.nameToClassMap.get(nbt.getString("id"));
            if (oclass != null) {
                tileentity = (TileEntity)oclass.newInstance();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (tileentity != null) {
            tileentity.readFromNBT(nbt);
        }
        else {
            TileEntity.logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
        }
        return tileentity;
    }
    
    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            final IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }
        return this.blockMetadata;
    }
    
    public void markDirty() {
        if (this.worldObj != null) {
            final IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            this.worldObj.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.air) {
                this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double d0 = this.pos.getX() + 0.5 - x;
        final double d2 = this.pos.getY() + 0.5 - y;
        final double d3 = this.pos.getZ() + 0.5 - z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double getMaxRenderDistanceSquared() {
        return 4096.0;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlockType() {
        if (this.blockType == null) {
            this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
        }
        return this.blockType;
    }
    
    public Packet getDescriptionPacket() {
        return null;
    }
    
    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }
    
    public void invalidate() {
        this.tileEntityInvalid = true;
    }
    
    public void validate() {
        this.tileEntityInvalid = false;
    }
    
    public boolean receiveClientEvent(final int id, final int type) {
        return false;
    }
    
    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }
    
    public void addInfoToCrashReport(final CrashReportCategory reportCategory) {
        reportCategory.addCrashSectionCallable("Name", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(TileEntity.classToNameMap.get(TileEntity.this.getClass())) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.worldObj != null) {
            CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            reportCategory.addCrashSectionCallable("Actual block type", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    final int i = Block.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", i, Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName());
                    }
                    catch (Throwable var3) {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    final IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
                    final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    }
                    final String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                }
            });
        }
    }
    
    public void setPos(final BlockPos posIn) {
        this.pos = posIn;
    }
    
    public boolean func_183000_F() {
        return false;
    }
}
