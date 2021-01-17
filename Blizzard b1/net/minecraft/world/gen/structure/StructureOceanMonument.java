package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class StructureOceanMonument extends MapGenStructure
{
    private int field_175800_f;
    private int field_175801_g;
    public static final List field_175802_d = Arrays.asList(new BiomeGenBase[] {BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver});
    private static final List field_175803_h = Lists.newArrayList();
    private static final String __OBFID = "CL_00001996";

    public StructureOceanMonument()
    {
        this.field_175800_f = 32;
        this.field_175801_g = 5;
    }

    public StructureOceanMonument(Map p_i45608_1_)
    {
        this();
        Iterator var2 = p_i45608_1_.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (((String)var3.getKey()).equals("spacing"))
            {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.field_175800_f, 1);
            }
            else if (((String)var3.getKey()).equals("separation"))
            {
                this.field_175801_g = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.field_175801_g, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Monument";
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
        int var3 = p_75047_1_;
        int var4 = p_75047_2_;

        if (p_75047_1_ < 0)
        {
            p_75047_1_ -= this.field_175800_f - 1;
        }

        if (p_75047_2_ < 0)
        {
            p_75047_2_ -= this.field_175800_f - 1;
        }

        int var5 = p_75047_1_ / this.field_175800_f;
        int var6 = p_75047_2_ / this.field_175800_f;
        Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387313);
        var5 *= this.field_175800_f;
        var6 *= this.field_175800_f;
        var5 += (var7.nextInt(this.field_175800_f - this.field_175801_g) + var7.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        var6 += (var7.nextInt(this.field_175800_f - this.field_175801_g) + var7.nextInt(this.field_175800_f - this.field_175801_g)) / 2;

        if (var3 == var5 && var4 == var6)
        {
            if (this.worldObj.getWorldChunkManager().func_180300_a(new BlockPos(var3 * 16 + 8, 64, var4 * 16 + 8), (BiomeGenBase)null) != BiomeGenBase.deepOcean)
            {
                return false;
            }

            boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 29, field_175802_d);

            if (var8)
            {
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new StructureOceanMonument.StartMonument(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }

    public List func_175799_b()
    {
        return field_175803_h;
    }

    static
    {
        field_175803_h.add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument extends StructureStart
    {
        private Set field_175791_c = Sets.newHashSet();
        private boolean field_175790_d;
        private static final String __OBFID = "CL_00001995";

        public StartMonument() {}

        public StartMonument(World worldIn, Random p_i45607_2_, int p_i45607_3_, int p_i45607_4_)
        {
            super(p_i45607_3_, p_i45607_4_);
            this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
        }

        private void func_175789_b(World worldIn, Random p_175789_2_, int p_175789_3_, int p_175789_4_)
        {
            p_175789_2_.setSeed(worldIn.getSeed());
            long var5 = p_175789_2_.nextLong();
            long var7 = p_175789_2_.nextLong();
            long var9 = (long)p_175789_3_ * var5;
            long var11 = (long)p_175789_4_ * var7;
            p_175789_2_.setSeed(var9 ^ var11 ^ worldIn.getSeed());
            int var13 = p_175789_3_ * 16 + 8 - 29;
            int var14 = p_175789_4_ * 16 + 8 - 29;
            EnumFacing var15 = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, var13, var14, var15));
            this.updateBoundingBox();
            this.field_175790_d = true;
        }

        public void generateStructure(World worldIn, Random p_75068_2_, StructureBoundingBox p_75068_3_)
        {
            if (!this.field_175790_d)
            {
                this.components.clear();
                this.func_175789_b(worldIn, p_75068_2_, this.func_143019_e(), this.func_143018_f());
            }

            super.generateStructure(worldIn, p_75068_2_, p_75068_3_);
        }

        public boolean func_175788_a(ChunkCoordIntPair p_175788_1_)
        {
            return this.field_175791_c.contains(p_175788_1_) ? false : super.func_175788_a(p_175788_1_);
        }

        public void func_175787_b(ChunkCoordIntPair p_175787_1_)
        {
            super.func_175787_b(p_175787_1_);
            this.field_175791_c.add(p_175787_1_);
        }

        public void func_143022_a(NBTTagCompound p_143022_1_)
        {
            super.func_143022_a(p_143022_1_);
            NBTTagList var2 = new NBTTagList();
            Iterator var3 = this.field_175791_c.iterator();

            while (var3.hasNext())
            {
                ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
                NBTTagCompound var5 = new NBTTagCompound();
                var5.setInteger("X", var4.chunkXPos);
                var5.setInteger("Z", var4.chunkZPos);
                var2.appendTag(var5);
            }

            p_143022_1_.setTag("Processed", var2);
        }

        public void func_143017_b(NBTTagCompound p_143017_1_)
        {
            super.func_143017_b(p_143017_1_);

            if (p_143017_1_.hasKey("Processed", 9))
            {
                NBTTagList var2 = p_143017_1_.getTagList("Processed", 10);

                for (int var3 = 0; var3 < var2.tagCount(); ++var3)
                {
                    NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                    this.field_175791_c.add(new ChunkCoordIntPair(var4.getInteger("X"), var4.getInteger("Z")));
                }
            }
        }
    }
}
