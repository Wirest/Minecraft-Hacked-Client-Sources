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
    /**
     * A list of all the biomes villages can spawn in.
     */
    public static final List villageSpawnBiomes = Arrays.asList(new BiomeGenBase[]{BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna});

    /**
     * World terrain type, 0 for normal, 1 for flat map
     */
    private int terrainType;
    private int field_82665_g;
    private int field_82666_h;
    private static final String __OBFID = "CL_00000514";

    public MapGenVillage() {
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }

    public MapGenVillage(Map p_i2093_1_) {
        this();
        Iterator var2 = p_i2093_1_.entrySet().iterator();

        while (var2.hasNext()) {
            Entry var3 = (Entry) var2.next();

            if (((String) var3.getKey()).equals("size")) {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String) var3.getValue(), this.terrainType, 0);
            } else if (((String) var3.getKey()).equals("distance")) {
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String) var3.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }

    public String getStructureName() {
        return "Village";
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        int var3 = p_75047_1_;
        int var4 = p_75047_2_;

        if (p_75047_1_ < 0) {
            p_75047_1_ -= this.field_82665_g - 1;
        }

        if (p_75047_2_ < 0) {
            p_75047_2_ -= this.field_82665_g - 1;
        }

        int var5 = p_75047_1_ / this.field_82665_g;
        int var6 = p_75047_2_ / this.field_82665_g;
        Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387312);
        var5 *= this.field_82665_g;
        var6 *= this.field_82665_g;
        var5 += var7.nextInt(this.field_82665_g - this.field_82666_h);
        var6 += var7.nextInt(this.field_82665_g - this.field_82666_h);

        if (var3 == var5 && var4 == var6) {
            boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 0, villageSpawnBiomes);

            if (var8) {
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
        return new MapGenVillage.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_, this.terrainType);
    }

    public static class Start extends StructureStart {
        private boolean hasMoreThanTwoComponents;
        private static final String __OBFID = "CL_00000515";

        public Start() {
        }

        public Start(World worldIn, Random p_i2092_2_, int p_i2092_3_, int p_i2092_4_, int p_i2092_5_) {
            super(p_i2092_3_, p_i2092_4_);
            List var6 = StructureVillagePieces.getStructureVillageWeightedPieceList(p_i2092_2_, p_i2092_5_);
            StructureVillagePieces.Start var7 = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, p_i2092_2_, (p_i2092_3_ << 4) + 2, (p_i2092_4_ << 4) + 2, var6, p_i2092_5_);
            this.components.add(var7);
            var7.buildComponent(var7, this.components, p_i2092_2_);
            List var8 = var7.field_74930_j;
            List var9 = var7.field_74932_i;
            int var10;

            while (!var8.isEmpty() || !var9.isEmpty()) {
                StructureComponent var11;

                if (var8.isEmpty()) {
                    var10 = p_i2092_2_.nextInt(var9.size());
                    var11 = (StructureComponent) var9.remove(var10);
                    var11.buildComponent(var7, this.components, p_i2092_2_);
                } else {
                    var10 = p_i2092_2_.nextInt(var8.size());
                    var11 = (StructureComponent) var8.remove(var10);
                    var11.buildComponent(var7, this.components, p_i2092_2_);
                }
            }

            this.updateBoundingBox();
            var10 = 0;
            Iterator var13 = this.components.iterator();

            while (var13.hasNext()) {
                StructureComponent var12 = (StructureComponent) var13.next();

                if (!(var12 instanceof StructureVillagePieces.Road)) {
                    ++var10;
                }
            }

            this.hasMoreThanTwoComponents = var10 > 2;
        }

        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        public void func_143022_a(NBTTagCompound p_143022_1_) {
            super.func_143022_a(p_143022_1_);
            p_143022_1_.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public void func_143017_b(NBTTagCompound p_143017_1_) {
            super.func_143017_b(p_143017_1_);
            this.hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
        }
    }
}
