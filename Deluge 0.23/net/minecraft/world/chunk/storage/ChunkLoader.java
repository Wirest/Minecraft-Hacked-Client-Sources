package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.NibbleArray;

public class ChunkLoader
{
    private static final String __OBFID = "CL_00000379";

    public static ChunkLoader.AnvilConverterData load(NBTTagCompound nbt)
    {
        int var1 = nbt.getInteger("xPos");
        int var2 = nbt.getInteger("zPos");
        ChunkLoader.AnvilConverterData var3 = new ChunkLoader.AnvilConverterData(var1, var2);
        var3.blocks = nbt.getByteArray("Blocks");
        var3.data = new NibbleArrayReader(nbt.getByteArray("Data"), 7);
        var3.skyLight = new NibbleArrayReader(nbt.getByteArray("SkyLight"), 7);
        var3.blockLight = new NibbleArrayReader(nbt.getByteArray("BlockLight"), 7);
        var3.heightmap = nbt.getByteArray("HeightMap");
        var3.terrainPopulated = nbt.getBoolean("TerrainPopulated");
        var3.entities = nbt.getTagList("Entities", 10);
        var3.tileEntities = nbt.getTagList("TileEntities", 10);
        var3.tileTicks = nbt.getTagList("TileTicks", 10);

        try
        {
            var3.lastUpdated = nbt.getLong("LastUpdate");
        }
        catch (ClassCastException var5)
        {
            var3.lastUpdated = (long)nbt.getInteger("LastUpdate");
        }

        return var3;
    }

    public static void convertToAnvilFormat(ChunkLoader.AnvilConverterData p_76690_0_, NBTTagCompound p_76690_1_, WorldChunkManager p_76690_2_)
    {
        p_76690_1_.setInteger("xPos", p_76690_0_.x);
        p_76690_1_.setInteger("zPos", p_76690_0_.z);
        p_76690_1_.setLong("LastUpdate", p_76690_0_.lastUpdated);
        int[] var3 = new int[p_76690_0_.heightmap.length];

        for (int var4 = 0; var4 < p_76690_0_.heightmap.length; ++var4)
        {
            var3[var4] = p_76690_0_.heightmap[var4];
        }

        p_76690_1_.setIntArray("HeightMap", var3);
        p_76690_1_.setBoolean("TerrainPopulated", p_76690_0_.terrainPopulated);
        NBTTagList var16 = new NBTTagList();
        int var7;

        for (int var5 = 0; var5 < 8; ++var5)
        {
            boolean var6 = true;

            for (var7 = 0; var7 < 16 && var6; ++var7)
            {
                int var8 = 0;

                while (var8 < 16 && var6)
                {
                    int var9 = 0;

                    while (true)
                    {
                        if (var9 < 16)
                        {
                            int var10 = var7 << 11 | var9 << 7 | var8 + (var5 << 4);
                            byte var11 = p_76690_0_.blocks[var10];

                            if (var11 == 0)
                            {
                                ++var9;
                                continue;
                            }

                            var6 = false;
                        }

                        ++var8;
                        break;
                    }
                }
            }

            if (!var6)
            {
                byte[] var19 = new byte[4096];
                NibbleArray var20 = new NibbleArray();
                NibbleArray var21 = new NibbleArray();
                NibbleArray var22 = new NibbleArray();

                for (int var23 = 0; var23 < 16; ++var23)
                {
                    for (int var12 = 0; var12 < 16; ++var12)
                    {
                        for (int var13 = 0; var13 < 16; ++var13)
                        {
                            int var14 = var23 << 11 | var13 << 7 | var12 + (var5 << 4);
                            byte var15 = p_76690_0_.blocks[var14];
                            var19[var12 << 8 | var13 << 4 | var23] = (byte)(var15 & 255);
                            var20.set(var23, var12, var13, p_76690_0_.data.get(var23, var12 + (var5 << 4), var13));
                            var21.set(var23, var12, var13, p_76690_0_.skyLight.get(var23, var12 + (var5 << 4), var13));
                            var22.set(var23, var12, var13, p_76690_0_.blockLight.get(var23, var12 + (var5 << 4), var13));
                        }
                    }
                }

                NBTTagCompound var24 = new NBTTagCompound();
                var24.setByte("Y", (byte)(var5 & 255));
                var24.setByteArray("Blocks", var19);
                var24.setByteArray("Data", var20.getData());
                var24.setByteArray("SkyLight", var21.getData());
                var24.setByteArray("BlockLight", var22.getData());
                var16.appendTag(var24);
            }
        }

        p_76690_1_.setTag("Sections", var16);
        byte[] var17 = new byte[256];

        for (int var18 = 0; var18 < 16; ++var18)
        {
            for (var7 = 0; var7 < 16; ++var7)
            {
                var17[var7 << 4 | var18] = (byte)(p_76690_2_.func_180300_a(new BlockPos(p_76690_0_.x << 4 | var18, 0, p_76690_0_.z << 4 | var7), BiomeGenBase.field_180279_ad).biomeID & 255);
            }
        }

        p_76690_1_.setByteArray("Biomes", var17);
        p_76690_1_.setTag("Entities", p_76690_0_.entities);
        p_76690_1_.setTag("TileEntities", p_76690_0_.tileEntities);

        if (p_76690_0_.tileTicks != null)
        {
            p_76690_1_.setTag("TileTicks", p_76690_0_.tileTicks);
        }
    }

    public static class AnvilConverterData
    {
        public long lastUpdated;
        public boolean terrainPopulated;
        public byte[] heightmap;
        public NibbleArrayReader blockLight;
        public NibbleArrayReader skyLight;
        public NibbleArrayReader data;
        public byte[] blocks;
        public NBTTagList entities;
        public NBTTagList tileEntities;
        public NBTTagList tileTicks;
        public final int x;
        public final int z;
        private static final String __OBFID = "CL_00000380";

        public AnvilConverterData(int p_i1999_1_, int p_i1999_2_)
        {
            this.x = p_i1999_1_;
            this.z = p_i1999_2_;
        }
    }
}
