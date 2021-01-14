package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
    /**
     * The byte array stored in the tag.
     */
    private byte[] data;
    private static final String __OBFID = "CL_00001213";

    NBTTagByteArray() {
    }

    public NBTTagByteArray(byte[] data) {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        output.write(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        int var4 = input.readInt();
        sizeTracker.read((long) (8 * var4));
        this.data = new byte[var4];
        input.readFully(this.data);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return (byte) 7;
    }

    public String toString() {
        return "[" + this.data.length + " bytes]";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy() {
        byte[] var1 = new byte[this.data.length];
        System.arraycopy(this.data, 0, var1, 0, this.data.length);
        return new NBTTagByteArray(var1);
    }

    public boolean equals(Object p_equals_1_) {
        return super.equals(p_equals_1_) ? Arrays.equals(this.data, ((NBTTagByteArray) p_equals_1_).data) : false;
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] getByteArray() {
        return this.data;
    }
}
