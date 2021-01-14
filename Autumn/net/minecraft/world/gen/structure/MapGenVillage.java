package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenVillage extends MapGenStructure {
   public static final List villageSpawnBiomes;
   private int terrainType;
   private int field_82665_g;
   private int field_82666_h;

   public MapGenVillage() {
      this.field_82665_g = 32;
      this.field_82666_h = 8;
   }

   public MapGenVillage(Map p_i2093_1_) {
      this();
      Iterator var2 = p_i2093_1_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         if (((String)entry.getKey()).equals("size")) {
            this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.terrainType, 0);
         } else if (((String)entry.getKey()).equals("distance")) {
            this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
         }
      }

   }

   public String getStructureName() {
      return "Village";
   }

   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
      int i = chunkX;
      int j = chunkZ;
      if (chunkX < 0) {
         chunkX -= this.field_82665_g - 1;
      }

      if (chunkZ < 0) {
         chunkZ -= this.field_82665_g - 1;
      }

      int k = chunkX / this.field_82665_g;
      int l = chunkZ / this.field_82665_g;
      Random random = this.worldObj.setRandomSeed(k, l, 10387312);
      k *= this.field_82665_g;
      l *= this.field_82665_g;
      k += random.nextInt(this.field_82665_g - this.field_82666_h);
      l += random.nextInt(this.field_82665_g - this.field_82666_h);
      if (i == k && j == l) {
         boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, villageSpawnBiomes);
         if (flag) {
            return true;
         }
      }

      return false;
   }

   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
      return new MapGenVillage.Start(this.worldObj, this.rand, chunkX, chunkZ, this.terrainType);
   }

   static {
      villageSpawnBiomes = Arrays.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);
   }

   public static class Start extends StructureStart {
      private boolean hasMoreThanTwoComponents;

      public Start() {
      }

      public Start(World worldIn, Random rand, int x, int z, int p_i2092_5_) {
         super(x, z);
         List list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, p_i2092_5_);
         StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, p_i2092_5_);
         this.components.add(structurevillagepieces$start);
         structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
         List list1 = structurevillagepieces$start.field_74930_j;
         List list2 = structurevillagepieces$start.field_74932_i;

         int k;
         while(!list1.isEmpty() || !list2.isEmpty()) {
            StructureComponent structurecomponent;
            if (list1.isEmpty()) {
               k = rand.nextInt(list2.size());
               structurecomponent = (StructureComponent)list2.remove(k);
               structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
            } else {
               k = rand.nextInt(list1.size());
               structurecomponent = (StructureComponent)list1.remove(k);
               structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
            }
         }

         this.updateBoundingBox();
         k = 0;
         Iterator var13 = this.components.iterator();

         while(var13.hasNext()) {
            StructureComponent structurecomponent1 = (StructureComponent)var13.next();
            if (!(structurecomponent1 instanceof StructureVillagePieces.Road)) {
               ++k;
            }
         }

         this.hasMoreThanTwoComponents = k > 2;
      }

      public boolean isSizeableStructure() {
         return this.hasMoreThanTwoComponents;
      }

      public void writeToNBT(NBTTagCompound tagCompound) {
         super.writeToNBT(tagCompound);
         tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
      }

      public void readFromNBT(NBTTagCompound tagCompound) {
         super.readFromNBT(tagCompound);
         this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
      }
   }
}
