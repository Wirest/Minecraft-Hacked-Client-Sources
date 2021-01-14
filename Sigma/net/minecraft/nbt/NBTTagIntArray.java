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
        intArray = p_i45132_1_;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(intArray.length);

        for (int element : intArray) {
            output.writeInt(element);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        int var4 = input.readInt();
        sizeTracker.read(32 * var4);
        intArray = new int[var4];

        for (int var5 = 0; var5 < var4; ++var5) {
            intArray[var5] = input.readInt();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 11;
    }

    @Override
    public String toString() {
        String var1 = "[";
        int[] var2 = intArray;
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
    @Override
    public NBTBase copy() {
        int[] var1 = new int[intArray.length];
        System.arraycopy(intArray, 0, var1, 0, intArray.length);
        return new NBTTagIntArray(var1);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return super.equals(p_equals_1_) ? Arrays.equals(intArray, ((NBTTagIntArray) p_equals_1_).intArray) : false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(intArray);
    }

    public int[] getIntArray() {
        return intArray;
    }
}
