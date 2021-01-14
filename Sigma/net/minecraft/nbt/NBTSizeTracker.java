package net.minecraft.nbt;

public class NBTSizeTracker {
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L) {
        private static final String __OBFID = "CL_00001902";

        @Override
        public void read(long bits) {
        }
    };
    private final long max;
    private long read;
    private static final String __OBFID = "CL_00001903";

    public NBTSizeTracker(long max) {
        this.max = max;
    }

    /**
     * Tracks the reading of the given amount of bits(!)
     */
    public void read(long bits) {
        read += bits / 8L;

        if (read > max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + read + "bytes where max allowed: " + max);
        }
    }
}
