package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenNetherBridge extends MapGenStructure
{
    private List spawnList = Lists.newArrayList();
    private static final String __OBFID = "CL_00000451";

    public MapGenNetherBridge()
    {
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }

    public String getStructureName()
    {
        return "Fortress";
    }

    public List getSpawnList()
    {
        return this.spawnList;
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
        int var3 = p_75047_1_ >> 4;
        int var4 = p_75047_2_ >> 4;
        this.rand.setSeed((long)(var3 ^ var4 << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) != 0 ? false : (p_75047_1_ != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : p_75047_2_ == (var4 << 4) + 4 + this.rand.nextInt(8));
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new MapGenNetherBridge.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }

    public static class Start extends StructureStart
    {
        private static final String __OBFID = "CL_00000452";

        public Start() {}

        public Start(World worldIn, Random p_i2040_2_, int p_i2040_3_, int p_i2040_4_)
        {
            super(p_i2040_3_, p_i2040_4_);
            StructureNetherBridgePieces.Start var5 = new StructureNetherBridgePieces.Start(p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
            this.components.add(var5);
            var5.buildComponent(var5, this.components, p_i2040_2_);
            List var6 = var5.field_74967_d;

            while (!var6.isEmpty())
            {
                int var7 = p_i2040_2_.nextInt(var6.size());
                StructureComponent var8 = (StructureComponent)var6.remove(var7);
                var8.buildComponent(var5, this.components, p_i2040_2_);
            }

            this.updateBoundingBox();
            this.setRandomHeight(worldIn, p_i2040_2_, 48, 70);
        }
    }
}
