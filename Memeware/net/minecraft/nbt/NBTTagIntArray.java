package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {
    /**
     * The array of saved integers
     */
    private int[] intArray;
    private static final String __OBFID = "CL_00001221";

    NBTTagIntArray() {
    }

    public NBTTagIntArray(int[] p_i45132_1_) {
        this.intArray = p_i45132_1_;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeInt(this.intArray.length);

        for (int var2 = 0; var2 < this.intArray.length; ++var2) {
            output.writeInt(this.intArray[var2]);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        int var4 = input.readInt();
        sizeTracker.read((long) (32 * var4));
        this.intArray = new int[var4];

        for (int var5 = 0; var5 < var4; ++var5) {
            this.intArray[var5] = input.readInt();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return (byte) 11;
    }

    public String toString() {
        String var1 = "[";
        int[] var2 = this.intArray;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int var5 = var2[var4];
            var1 = var1 + var5 + ",";
        }

        return var1 + "]";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy() {
        int[] var1 = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
        return new NBTTagIntArray(var1);
    }

    public boolean equals(Object p_equals_1_) {
        return super.equals(p_equals_1_) ? Arrays.equals(this.intArray, ((NBTTagIntArray) p_equals_1_).intArray) : false;
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }

    public int[] getIntArray() {
        return this.intArray;
    }
}
