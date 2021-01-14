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
import optfine.Reflector;

public abstract class MapGenStructure extends MapGenBase {
   private MapGenStructureData structureData;
   protected Map structureMap = Maps.newHashMap();
   private static final String __OBFID = "CL_00000505";
   private LongHashMap structureLongMap = new LongHashMap();

   public abstract String getStructureName();

   protected final void recursiveGenerate(World worldIn, final int chunkX, final int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
      this.func_143027_a(worldIn);
      if (!this.structureLongMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ))) {
         this.rand.nextInt();

         try {
            if (this.canSpawnStructureAtCoords(chunkX, chunkZ)) {
               StructureStart structurestart = this.getStructureStart(chunkX, chunkZ);
               this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), structurestart);
               this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), structurestart);
               this.func_143026_a(chunkX, chunkZ, structurestart);
            }
         } catch (Throwable var10) {
            CrashReport crashreport = CrashReport.makeCrashReport(var10, "Exception preparing structure feature");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
            crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable() {
               private static final String __OBFID = "CL_00000506";

               public String call() throws Exception {
                  return MapGenStructure.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
               }
            });
            crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", chunkX, chunkZ));
            crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable() {
               private static final String __OBFID = "CL_00000507";

               public String call() throws Exception {
                  return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
               }
            });
            crashreportcategory.addCrashSectionCallable("Structure type", new Callable() {
               private static final String __OBFID = "CL_00000508";

               public String call() throws Exception {
                  return MapGenStructure.this.getClass().getCanonicalName();
               }
            });
            throw new ReportedException(crashreport);
         }
      }

   }

   public boolean generateStructure(World worldIn, Random randomIn, ChunkCoordIntPair chunkCoord) {
      this.func_143027_a(worldIn);
      int i = (chunkCoord.chunkXPos << 4) + 8;
      int j = (chunkCoord.chunkZPos << 4) + 8;
      boolean flag = false;
      Iterator var7 = this.structureMap.values().iterator();

      while(var7.hasNext()) {
         Object structurestart0 = var7.next();
         StructureStart structurestart = (StructureStart)structurestart0;
         if (structurestart.isSizeableStructure() && structurestart.func_175788_a(chunkCoord) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
            structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
            structurestart.func_175787_b(chunkCoord);
            flag = true;
            this.func_143026_a(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
         }
      }

      return flag;
   }

   public boolean func_175795_b(BlockPos pos) {
      this.func_143027_a(this.worldObj);
      return this.func_175797_c(pos) != null;
   }

   protected StructureStart func_175797_c(BlockPos pos) {
      Iterator var2 = this.structureMap.values().iterator();

      while(true) {
         StructureStart structurestart;
         do {
            do {
               if (!var2.hasNext()) {
                  return null;
               }

               Object structurestart0 = var2.next();
               structurestart = (StructureStart)structurestart0;
            } while(!structurestart.isSizeableStructure());
         } while(!structurestart.getBoundingBox().isVecInside(pos));

         Iterator iterator = structurestart.getComponents().iterator();

         while(iterator.hasNext()) {
            StructureComponent structurecomponent = (StructureComponent)iterator.next();
            if (structurecomponent.getBoundingBox().isVecInside(pos)) {
               return structurestart;
            }
         }
      }
   }

   public boolean func_175796_a(World worldIn, BlockPos pos) {
      this.func_143027_a(worldIn);
      Iterator var3 = this.structureMap.values().iterator();

      StructureStart structurestart;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         Object structurestart0 = var3.next();
         structurestart = (StructureStart)structurestart0;
      } while(!structurestart.isSizeableStructure() || !structurestart.getBoundingBox().isVecInside(pos));

      return true;
   }

   public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos) {
      this.worldObj = worldIn;
      this.func_143027_a(worldIn);
      this.rand.setSeed(worldIn.getSeed());
      long i = this.rand.nextLong();
      long j = this.rand.nextLong();
      long k = (long)(pos.getX() >> 4) * i;
      long l = (long)(pos.getZ() >> 4) * j;
      this.rand.setSeed(k ^ l ^ worldIn.getSeed());
      this.recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, (ChunkPrimer)null);
      double d0 = Double.MAX_VALUE;
      BlockPos blockpos = null;
      Iterator var14 = this.structureMap.values().iterator();

      while(var14.hasNext()) {
         Object structurestart0 = var14.next();
         StructureStart structurestart = (StructureStart)structurestart0;
         if (structurestart.isSizeableStructure()) {
            StructureComponent structurecomponent = (StructureComponent)structurestart.getComponents().get(0);
            BlockPos blockpos1 = structurecomponent.getBoundingBoxCenter();
            double d1 = blockpos1.distanceSq(pos);
            if (d1 < d0) {
               d0 = d1;
               blockpos = blockpos1;
            }
         }
      }

      if (blockpos != null) {
         return blockpos;
      } else {
         List list = this.getCoordList();
         if (list != null) {
            BlockPos blockpos3 = null;
            Iterator var23 = list.iterator();

            while(var23.hasNext()) {
               Object blockpos2 = var23.next();
               double d2 = ((BlockPos)blockpos2).distanceSq(pos);
               if (d2 < d0) {
                  d0 = d2;
                  blockpos3 = (BlockPos)blockpos2;
               }
            }

            return blockpos3;
         } else {
            return null;
         }
      }
   }

   protected List getCoordList() {
      return null;
   }

   private void func_143027_a(World worldIn) {
      if (this.structureData == null) {
         MapStorage mapstorage1;
         if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
            mapstorage1 = (MapStorage)Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage);
            this.structureData = (MapGenStructureData)mapstorage1.loadData(MapGenStructureData.class, this.getStructureName());
         } else {
            this.structureData = (MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, this.getStructureName());
         }

         if (this.structureData == null) {
            this.structureData = new MapGenStructureData(this.getStructureName());
            if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
               mapstorage1 = (MapStorage)Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage);
               mapstorage1.setData(this.getStructureName(), this.structureData);
            } else {
               worldIn.setItemData(this.getStructureName(), this.structureData);
            }
         } else {
            NBTTagCompound nbttagcompound1 = this.structureData.getTagCompound();
            Iterator var3 = nbttagcompound1.getKeySet().iterator();

            while(var3.hasNext()) {
               String s = (String)var3.next();
               NBTBase nbtbase = nbttagcompound1.getTag(s);
               if (nbtbase.getId() == 10) {
                  NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase;
                  if (nbttagcompound.hasKey("ChunkX") && nbttagcompound.hasKey("ChunkZ")) {
                     int i = nbttagcompound.getInteger("ChunkX");
                     int j = nbttagcompound.getInteger("ChunkZ");
                     StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound, worldIn);
                     if (structurestart != null) {
                        this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
                        this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
                     }
                  }
               }
            }
         }
      }

   }

   private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart start) {
      this.structureData.writeInstance(start.writeStructureComponentsToNBT(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
      this.structureData.markDirty();
   }

   protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

   protected abstract StructureStart getStructureStart(int var1, int var2);
}
