package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
    private static final String __OBFID = "CL_00001219";

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 0;
    }

    @Override
    public String toString() {
        return "END";
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        return new NBTTagEnd();
    }
}
