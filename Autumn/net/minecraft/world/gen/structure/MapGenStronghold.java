package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenStronghold extends MapGenStructure {
   private List field_151546_e;
   private boolean ranBiomeCheck;
   private ChunkCoordIntPair[] structureCoords;
   private double field_82671_h;
   private int field_82672_i;

   public MapGenStronghold() {
      this.structureCoords = new ChunkCoordIntPair[3];
      this.field_82671_h = 32.0D;
      this.field_82672_i = 3;
      this.field_151546_e = Lists.newArrayList();
      BiomeGenBase[] var1 = BiomeGenBase.getBiomeGenArray();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BiomeGenBase biomegenbase = var1[var3];
         if (biomegenbase != null && biomegenbase.minHeight > 0.0F) {
            this.field_151546_e.add(biomegenbase);
         }
      }

   }

   public MapGenStronghold(Map p_i2068_1_) {
      this();
      Iterator var2 = p_i2068_1_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         if (((String)entry.getKey()).equals("distance")) {
            this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String)entry.getValue(), this.field_82671_h, 1.0D);
         } else if (((String)entry.getKey()).equals("count")) {
            this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.structureCoords.length, 1)];
         } else if (((String)entry.getKey()).equals("spread")) {
            this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82672_i, 1);
         }
      }

   }

   public String getStructureName() {
      return "Stronghold";
   }

   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
      if (!this.ranBiomeCheck) {
         Random random = new Random();
         random.setSeed(this.worldObj.getSeed());
         double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
         int i = 1;

         for(int j = 0; j < this.structureCoords.length; ++j) {
            double d1 = (1.25D * (double)i + random.nextDouble()) * this.field_82671_h * (double)i;
            int k = (int)Math.round(Math.cos(d0) * d1);
            int l = (int)Math.round(Math.sin(d0) * d1);
            BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((k << 4) + 8, (l << 4) + 8, 112, this.field_151546_e, random);
            if (blockpos != null) {
               k = blockpos.getX() >> 4;
               l = blockpos.getZ() >> 4;
            }

            this.structureCoords[j] = new ChunkCoordIntPair(k, l);
            d0 += 6.283185307179586D * (double)i / (double)this.field_82672_i;
            if (j == this.field_82672_i) {
               i += 2 + random.nextInt(5);
               this.field_82672_i += 1 + random.nextInt(2);
            }
         }

         this.ranBiomeCheck = true;
      }

      ChunkCoordIntPair[] var13 = this.structureCoords;
      int var14 = var13.length;

      for(int var5 = 0; var5 < var14; ++var5) {
         ChunkCoordIntPair chunkcoordintpair = var13[var5];
         if (chunkX == chunkcoordintpair.chunkXPos && chunkZ == chunkcoordintpair.chunkZPos) {
            return true;
         }
      }

      return false;
   }

   protected List getCoordList() {
      List list = Lists.newArrayList();
      ChunkCoordIntPair[] var2 = this.structureCoords;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ChunkCoordIntPair chunkcoordintpair = var2[var4];
         if (chunkcoordintpair != null) {
            list.add(chunkcoordintpair.getCenterBlock(64));
         }
      }

      return list;
   }

   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
      MapGenStronghold.Start mapgenstronghold$start;
      for(mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ)) {
      }

      return mapgenstronghold$start;
   }

   public static class Start extends StructureStart {
      public Start() {
      }

      public Start(World worldIn, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_) {
         super(p_i2067_3_, p_i2067_4_);
         StructureStrongholdPieces.prepareStructurePieces();
         StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
         this.components.add(structurestrongholdpieces$stairs2);
         structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
         List list = structurestrongholdpieces$stairs2.field_75026_c;

         while(!list.isEmpty()) {
            int i = p_i2067_2_.nextInt(list.size());
            StructureComponent structurecomponent = (StructureComponent)list.remove(i);
            structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
         }

         this.updateBoundingBox();
         this.markAvailableHeight(worldIn, p_i2067_2_, 10);
      }
   }
}
