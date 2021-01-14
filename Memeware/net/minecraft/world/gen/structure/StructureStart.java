package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public abstract class StructureStart {
    /**
     * List of all StructureComponents that are part of this structure
     */
    protected LinkedList components = new LinkedList();
    protected StructureBoundingBox boundingBox;
    private int field_143024_c;
    private int field_143023_d;
    private static final String __OBFID = "CL_00000513";

    public StructureStart() {
    }

    public StructureStart(int p_i43002_1_, int p_i43002_2_) {
        this.field_143024_c = p_i43002_1_;
        this.field_143023_d = p_i43002_2_;
    }

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public LinkedList getComponents() {
        return this.components;
    }

    /**
     * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
     */
    public void generateStructure(World worldIn, Random p_75068_2_, StructureBoundingBox p_75068_3_) {
        Iterator var4 = this.components.iterator();

        while (var4.hasNext()) {
            StructureComponent var5 = (StructureComponent) var4.next();

            if (var5.getBoundingBox().intersectsWith(p_75068_3_) && !var5.addComponentParts(worldIn, p_75068_2_, p_75068_3_)) {
                var4.remove();
            }
        }
    }

    /**
     * Calculates total bounding box based on components' bounding boxes and saves it to boundingBox
     */
    protected void updateBoundingBox() {
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        Iterator var1 = this.components.iterator();

        while (var1.hasNext()) {
            StructureComponent var2 = (StructureComponent) var1.next();
            this.boundingBox.expandTo(var2.getBoundingBox());
        }
    }

    public NBTTagCompound func_143021_a(int p_143021_1_, int p_143021_2_) {
        NBTTagCompound var3 = new NBTTagCompound();
        var3.setString("id", MapGenStructureIO.func_143033_a(this));
        var3.setInteger("ChunkX", p_143021_1_);
        var3.setInteger("ChunkZ", p_143021_2_);
        var3.setTag("BB", this.boundingBox.func_151535_h());
        NBTTagList var4 = new NBTTagList();
        Iterator var5 = this.components.iterator();

        while (var5.hasNext()) {
            StructureComponent var6 = (StructureComponent) var5.next();
            var4.appendTag(var6.func_143010_b());
        }

        var3.setTag("Children", var4);
        this.func_143022_a(var3);
        return var3;
    }

    public void func_143022_a(NBTTagCompound p_143022_1_) {
    }

    public void func_143020_a(World worldIn, NBTTagCompound p_143020_2_) {
        this.field_143024_c = p_143020_2_.getInteger("ChunkX");
        this.field_143023_d = p_143020_2_.getInteger("ChunkZ");

        if (p_143020_2_.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(p_143020_2_.getIntArray("BB"));
        }

        NBTTagList var3 = p_143020_2_.getTagList("Children", 10);

        for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
            this.components.add(MapGenStructureIO.func_143032_b(var3.getCompoundTagAt(var4), worldIn));
        }

        this.func_143017_b(p_143020_2_);
    }

    public void func_143017_b(NBTTagCompound p_143017_1_) {
    }

    /**
     * offsets the structure Bounding Boxes up to a certain height, typically 63 - 10
     */
    protected void markAvailableHeight(World worldIn, Random p_75067_2_, int p_75067_3_) {
        int var4 = 63 - p_75067_3_;
        int var5 = this.boundingBox.getYSize() + 1;

        if (var5 < var4) {
            var5 += p_75067_2_.nextInt(var4 - var5);
        }

        int var6 = var5 - this.boundingBox.maxY;
        this.boundingBox.offset(0, var6, 0);
        Iterator var7 = this.components.iterator();

        while (var7.hasNext()) {
            StructureComponent var8 = (StructureComponent) var7.next();
            var8.getBoundingBox().offset(0, var6, 0);
        }
    }

    protected void setRandomHeight(World worldIn, Random p_75070_2_, int p_75070_3_, int p_75070_4_) {
        int var5 = p_75070_4_ - p_75070_3_ + 1 - this.boundingBox.getYSize();
        boolean var6 = true;
        int var10;

        if (var5 > 1) {
            var10 = p_75070_3_ + p_75070_2_.nextInt(var5);
        } else {
            var10 = p_75070_3_;
        }

        int var7 = var10 - this.boundingBox.minY;
        this.boundingBox.offset(0, var7, 0);
        Iterator var8 = this.components.iterator();

        while (var8.hasNext()) {
            StructureComponent var9 = (StructureComponent) var8.next();
            var9.getBoundingBox().offset(0, var7, 0);
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isSizeableStructure() {
        return true;
    }

    public boolean func_175788_a(ChunkCoordIntPair p_175788_1_) {
        return true;
    }

    public void func_175787_b(ChunkCoordIntPair p_175787_1_) {
    }

    public int func_143019_e() {
        return this.field_143024_c;
    }

    public int func_143018_f() {
        return this.field_143023_d;
    }
}
