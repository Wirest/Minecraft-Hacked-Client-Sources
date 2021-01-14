package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.storage.MapStorage;
import optifine.Reflector;

public abstract class MapGenStructure extends MapGenBase {
    private MapGenStructureData field_143029_e;

    /**
     * Used to store a list of all structures that have been recursively generated. Used so that during recursive
     * generation, the structure generator can avoid generating structures that intersect ones that have already been
     * placed.
     */
    protected Map structureMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00000505";
    private LongHashMap structureLongMap = new LongHashMap();

    public abstract String getStructureName();

    protected final void func_180701_a(World worldIn, final int p_180701_2_, final int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_) {
        this.func_143027_a(worldIn);

        if (!this.structureLongMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_))) {
            this.rand.nextInt();

            try {
                if (this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_)) {
                    StructureStart var10 = this.getStructureStart(p_180701_2_, p_180701_3_);
                    this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_)), var10);
                    this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_), var10);
                    this.func_143026_a(p_180701_2_, p_180701_3_, var10);
                }
            } catch (Throwable var101) {
                CrashReport var8 = CrashReport.makeCrashReport(var101, "Exception preparing structure feature");
                CrashReportCategory var9 = var8.makeCategory("Feature being prepared");
                var9.addCrashSectionCallable("Is feature chunk", new Callable() {
                    private static final String __OBFID = "CL_00000506";

                    public String call() {
                        return MapGenStructure.this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_) ? "True" : "False";
                    }
                });
                var9.addCrashSection("Chunk location", String.format("%d,%d", new Object[]{Integer.valueOf(p_180701_2_), Integer.valueOf(p_180701_3_)}));
                var9.addCrashSectionCallable("Chunk pos hash", new Callable() {
                    private static final String __OBFID = "CL_00000507";

                    public String call() {
                        return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_));
                    }
                });
                var9.addCrashSectionCallable("Structure type", new Callable() {
                    public String call() {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(var8);
            }
        }
    }

    public boolean func_175794_a(World worldIn, Random p_175794_2_, ChunkCoordIntPair p_175794_3_) {
        this.func_143027_a(worldIn);
        int var4 = (p_175794_3_.chunkXPos << 4) + 8;
        int var5 = (p_175794_3_.chunkZPos << 4) + 8;
        boolean var6 = false;
        Iterator var7 = this.structureMap.values().iterator();

        while (var7.hasNext()) {
            StructureStart var8 = (StructureStart) var7.next();

            if (var8.isSizeableStructure() && var8.func_175788_a(p_175794_3_) && var8.getBoundingBox().intersectsWith(var4, var5, var4 + 15, var5 + 15)) {
                var8.generateStructure(worldIn, p_175794_2_, new StructureBoundingBox(var4, var5, var4 + 15, var5 + 15));
                var8.func_175787_b(p_175794_3_);
                var6 = true;
                this.func_143026_a(var8.func_143019_e(), var8.func_143018_f(), var8);
            }
        }

        return var6;
    }

    public boolean func_175795_b(BlockPos p_175795_1_) {
        this.func_143027_a(this.worldObj);
        return this.func_175797_c(p_175795_1_) != null;
    }

    protected StructureStart func_175797_c(BlockPos p_175797_1_) {
        Iterator var2 = this.structureMap.values().iterator();

        while (var2.hasNext()) {
            StructureStart var3 = (StructureStart) var2.next();

            if (var3.isSizeableStructure() && var3.getBoundingBox().func_175898_b(p_175797_1_)) {
                Iterator var4 = var3.getComponents().iterator();

                while (var4.hasNext()) {
                    StructureComponent var5 = (StructureComponent) var4.next();

                    if (var5.getBoundingBox().func_175898_b(p_175797_1_)) {
                        return var3;
                    }
                }
            }
        }

        return null;
    }

    public boolean func_175796_a(World worldIn, BlockPos p_175796_2_) {
        this.func_143027_a(worldIn);
        Iterator var3 = this.structureMap.values().iterator();
        StructureStart var4;

        do {
            if (!var3.hasNext()) {
                return false;
            }

            var4 = (StructureStart) var3.next();
        }
        while (!var4.isSizeableStructure() || !var4.getBoundingBox().func_175898_b(p_175796_2_));

        return true;
    }

    public BlockPos func_180706_b(World worldIn, BlockPos p_180706_2_) {
        this.worldObj = worldIn;
        this.func_143027_a(worldIn);
        this.rand.setSeed(worldIn.getSeed());
        long var3 = this.rand.nextLong();
        long var5 = this.rand.nextLong();
        long var7 = (long) (p_180706_2_.getX() >> 4) * var3;
        long var9 = (long) (p_180706_2_.getZ() >> 4) * var5;
        this.rand.setSeed(var7 ^ var9 ^ worldIn.getSeed());
        this.func_180701_a(worldIn, p_180706_2_.getX() >> 4, p_180706_2_.getZ() >> 4, 0, 0, (ChunkPrimer) null);
        double var11 = Double.MAX_VALUE;
        BlockPos var13 = null;
        Iterator var14 = this.structureMap.values().iterator();
        BlockPos var17;
        double var18;

        while (var14.hasNext()) {
            StructureStart var20 = (StructureStart) var14.next();

            if (var20.isSizeableStructure()) {
                StructureComponent var21 = (StructureComponent) var20.getComponents().get(0);
                var17 = var21.func_180776_a();
                var18 = var17.distanceSq(p_180706_2_);

                if (var18 < var11) {
                    var11 = var18;
                    var13 = var17;
                }
            }
        }

        if (var13 != null) {
            return var13;
        } else {
            List var201 = this.getCoordList();

            if (var201 != null) {
                BlockPos var211 = null;
                Iterator var22 = var201.iterator();

                while (var22.hasNext()) {
                    var17 = (BlockPos) var22.next();
                    var18 = var17.distanceSq(p_180706_2_);

                    if (var18 < var11) {
                        var11 = var18;
                        var211 = var17;
                    }
                }

                return var211;
            } else {
                return null;
            }
        }
    }

    /**
     * Returns a list of other locations at which the structure generation has been run, or null if not relevant to this
     * structure generator.
     */
    protected List getCoordList() {
        return null;
    }

    private void func_143027_a(World worldIn) {
        if (this.field_143029_e == null) {
            MapStorage var2;

            if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
                var2 = (MapStorage) Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage, new Object[0]);
                this.field_143029_e = (MapGenStructureData) var2.loadData(MapGenStructureData.class, this.getStructureName());
            } else {
                this.field_143029_e = (MapGenStructureData) worldIn.loadItemData(MapGenStructureData.class, this.getStructureName());
            }

            if (this.field_143029_e == null) {
                this.field_143029_e = new MapGenStructureData(this.getStructureName());

                if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
                    var2 = (MapStorage) Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage, new Object[0]);
                    var2.setData(this.getStructureName(), this.field_143029_e);
                } else {
                    worldIn.setItemData(this.getStructureName(), this.field_143029_e);
                }
            } else {
                NBTTagCompound var21 = this.field_143029_e.func_143041_a();
                Iterator var3 = var21.getKeySet().iterator();

                while (var3.hasNext()) {
                    String var4 = (String) var3.next();
                    NBTBase var5 = var21.getTag(var4);

                    if (var5.getId() == 10) {
                        NBTTagCompound var6 = (NBTTagCompound) var5;

                        if (var6.hasKey("ChunkX") && var6.hasKey("ChunkZ")) {
                            int var7 = var6.getInteger("ChunkX");
                            int var8 = var6.getInteger("ChunkZ");
                            StructureStart var9 = MapGenStructureIO.func_143035_a(var6, worldIn);

                            if (var9 != null) {
                                this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var7, var8)), var9);
                                this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(var7, var8), var9);
                            }
                        }
                    }
                }
            }
        }
    }

    private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart p_143026_3_) {
        this.field_143029_e.func_143043_a(p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
        this.field_143029_e.markDirty();
    }

    protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

    protected abstract StructureStart getStructureStart(int var1, int var2);
}
