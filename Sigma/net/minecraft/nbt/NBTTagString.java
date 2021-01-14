package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
    /**
     * The string value for the tag (cannot be empty).
     */
    private String data;
    private static final String __OBFID = "CL_00001228";

    public NBTTagString() {
        data = "";
    }

    public NBTTagString(String data) {
        this.data = data;

        if (data == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        output.writeUTF(data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        data = input.readUTF();
        sizeTracker.read(16 * data.length());
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 8;
    }

    @Override
    public String toString() {
        return "\"" + data.replace("\"", "\\\"") + "\"";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        return new NBTTagString(data);
    }

    /**
     * Return whether this compound has no tags.
     */
    @Override
    public boolean hasNoTags() {
        return data.isEmpty();
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) {
            return false;
        } else {
            NBTTagString var2 = (NBTTagString) p_equals_1_;
            return data == null && var2.data == null || data != null && data.equals(var2.data);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ data.hashCode();
    }

    @Override
    public String getString() {
        return data;
    }
}
