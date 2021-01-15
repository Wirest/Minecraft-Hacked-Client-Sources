package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
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

public class MapGenStronghold extends MapGenStructure
{
    private List field_151546_e;

    /**
     * is spawned false and set true once the defined BiomeGenBases were compared with the present ones
     */
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords;
    private double field_82671_h;
    private int field_82672_i;
    private static final String __OBFID = "CL_00000481";

    public MapGenStronghold()
    {
        this.structureCoords = new ChunkCoordIntPair[3];
        this.field_82671_h = 32.0D;
        this.field_82672_i = 3;
        this.field_151546_e = Lists.newArrayList();
        BiomeGenBase[] var1 = BiomeGenBase.getBiomeGenArray();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            BiomeGenBase var4 = var1[var3];

            if (var4 != null && var4.minHeight > 0.0F)
            {
                this.field_151546_e.add(var4);
            }
        }
    }

    public MapGenStronghold(Map p_i2068_1_)
    {
        this();
        Iterator var2 = p_i2068_1_.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (((String)var3.getKey()).equals("distance"))
            {
                this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String)var3.getValue(), this.field_82671_h, 1.0D);
            }
            else if (((String)var3.getKey()).equals("count"))
            {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.structureCoords.length, 1)];
            }
            else if (((String)var3.getKey()).equals("spread"))
            {
                this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.field_82672_i, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Stronghold";
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
        if (!this.ranBiomeCheck)
        {
            Random var3 = new Random();
            var3.setSeed(this.worldObj.getSeed());
            double var4 = var3.nextDouble() * Math.PI * 2.0D;
            int var6 = 1;

            for (int var7 = 0; var7 < this.structureCoords.length; ++var7)
            {
                double var8 = (1.25D * (double)var6 + var3.nextDouble()) * this.field_82671_h * (double)var6;
                int var10 = (int)Math.round(Math.cos(var4) * var8);
                int var11 = (int)Math.round(Math.sin(var4) * var8);
                BlockPos var12 = this.worldObj.getWorldChunkManager().findBiomePosition((var10 << 4) + 8, (var11 << 4) + 8, 112, this.field_151546_e, var3);

                if (var12 != null)
                {
                    var10 = var12.getX() >> 4;
                    var11 = var12.getZ() >> 4;
                }

                this.structureCoords[var7] = new ChunkCoordIntPair(var10, var11);
                var4 += (Math.PI * 2D) * (double)var6 / (double)this.field_82672_i;

                if (var7 == this.field_82672_i)
                {
                    var6 += 2 + var3.nextInt(5);
                    this.field_82672_i += 1 + var3.nextInt(2);
                }
            }

            this.ranBiomeCheck = true;
        }

        ChunkCoordIntPair[] var13 = this.structureCoords;
        int var14 = var13.length;

        for (int var5 = 0; var5 < var14; ++var5)
        {
            ChunkCoordIntPair var15 = var13[var5];

            if (p_75047_1_ == var15.chunkXPos && p_75047_2_ == var15.chunkZPos)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a list of other locations at which the structure generation has been run, or null if not relevant to this
     * structure generator.
     */
    protected List getCoordList()
    {
        ArrayList var1 = Lists.newArrayList();
        ChunkCoordIntPair[] var2 = this.structureCoords;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ChunkCoordIntPair var5 = var2[var4];

            if (var5 != null)
            {
                var1.add(var5.getCenterBlock(64));
            }
        }

        return var1;
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        MapGenStronghold.Start var3;

        for (var3 = new MapGenStronghold.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_); var3.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)var3.getComponents().get(0)).strongholdPortalRoom == null; var3 = new MapGenStronghold.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_))
        {
            ;
        }

        return var3;
    }

    public static class Start extends StructureStart
    {
        private static final String __OBFID = "CL_00000482";

        public Start() {}

        public Start(World worldIn, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_)
        {
            super(p_i2067_3_, p_i2067_4_);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 var5 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
            this.components.add(var5);
            var5.buildComponent(var5, this.components, p_i2067_2_);
            List var6 = var5.field_75026_c;

            while (!var6.isEmpty())
            {
                int var7 = p_i2067_2_.nextInt(var6.size());
                StructureComponent var8 = (StructureComponent)var6.remove(var7);
                var8.buildComponent(var5, this.components, p_i2067_2_);
            }

            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, p_i2067_2_, 10);
        }
    }
}
