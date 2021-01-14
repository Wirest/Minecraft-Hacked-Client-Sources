package net.minecraft.nbt;

import com.google.common.collect.Maps;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase {
    private static final Logger logger = LogManager.getLogger();

    /**
     * The key-value pairs for the tag. Each key is a UTF string, each value is
     * a tag.
     */
    private Map tagMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00001215";

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        Iterator var2 = tagMap.keySet().iterator();

        while (var2.hasNext()) {
            String var3 = (String) var2.next();
            NBTBase var4 = (NBTBase) tagMap.get(var3);
            NBTTagCompound.writeEntry(var3, var4, output);
        }

        output.writeByte(0);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            tagMap.clear();
            byte var4;

            while ((var4 = NBTTagCompound.readType(input, sizeTracker)) != 0) {
                String var5 = NBTTagCompound.readKey(input, sizeTracker);
                sizeTracker.read(16 * var5.length());
                NBTBase var6 = NBTTagCompound.readNBT(var4, var5, input, depth + 1, sizeTracker);
                tagMap.put(var5, var6);
            }
        }
    }

    /**
     * Gets a set with the names of the keys in the tag compound.
     */
    public Set getKeySet() {
        return tagMap.keySet();
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 10;
    }

    /**
     * Stores the given tag into the map with the given string key. This is
     * mostly used to store tag lists.
     */
    public void setTag(String key, NBTBase value) {
        tagMap.put(key, value);
    }

    /**
     * Stores a new NBTTagByte with the given byte value into the map with the
     * given string key.
     */
    public void setByte(String key, byte value) {
        tagMap.put(key, new NBTTagByte(value));
    }

    /**
     * Stores a new NBTTagShort with the given short value into the map with the
     * given string key.
     */
    public void setShort(String key, short value) {
        tagMap.put(key, new NBTTagShort(value));
    }

    /**
     * Stores a new NBTTagInt with the given integer value into the map with the
     * given string key.
     */
    public void setInteger(String key, int value) {
        tagMap.put(key, new NBTTagInt(value));
    }

    /**
     * Stores a new NBTTagLong with the given long value into the map with the
     * given string key.
     */
    public void setLong(String key, long value) {
        tagMap.put(key, new NBTTagLong(value));
    }

    /**
     * Stores a new NBTTagFloat with the given float value into the map with the
     * given string key.
     */
    public void setFloat(String key, float value) {
        tagMap.put(key, new NBTTagFloat(value));
    }

    /**
     * Stores a new NBTTagDouble with the given double value into the map with
     * the given string key.
     */
    public void setDouble(String key, double value) {
        tagMap.put(key, new NBTTagDouble(value));
    }

    /**
     * Stores a new NBTTagString with the given string value into the map with
     * the given string key.
     */
    public void setString(String key, String value) {
        tagMap.put(key, new NBTTagString(value));
    }

    /**
     * Stores a new NBTTagByteArray with the given array as data into the map
     * with the given string key.
     */
    public void setByteArray(String key, byte[] value) {
        tagMap.put(key, new NBTTagByteArray(value));
    }

    /**
     * Stores a new NBTTagIntArray with the given array as data into the map
     * with the given string key.
     */
    public void setIntArray(String key, int[] value) {
        tagMap.put(key, new NBTTagIntArray(value));
    }

    /**
     * Stores the given boolean value as a NBTTagByte, storing 1 for true and 0
     * for false, using the given string key.
     */
    public void setBoolean(String key, boolean value) {
        setByte(key, (byte) (value ? 1 : 0));
    }

    /**
     * gets a generic tag with the specified name
     */
    public NBTBase getTag(String key) {
        return (NBTBase) tagMap.get(key);
    }

    /**
     * Get the Type-ID for the entry with the given key
     */
    public byte getTagType(String key) {
        NBTBase var2 = (NBTBase) tagMap.get(key);
        return var2 != null ? var2.getId() : 0;
    }

    /**
     * Returns whether the given string has been previously stored as a key in
     * the map.
     */
    public boolean hasKey(String key) {
        return tagMap.containsKey(key);
    }

    public boolean hasKey(String key, int type) {
        byte var3 = getTagType(key);

        if (var3 == type) {
            return true;
        } else if (type != 99) {
            if (var3 > 0) {
                ;
            }

            return false;
        } else {
            return var3 == 1 || var3 == 2 || var3 == 3 || var3 == 4 || var3 == 5 || var3 == 6;
        }
    }

    /**
     * Retrieves a byte value using the specified key, or 0 if no such key was
     * stored.
     */
    public byte getByte(String key) {
        try {
            return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) tagMap.get(key)).getByte();
        } catch (ClassCastException var3) {
            return (byte) 0;
        }
    }

    /**
     * Retrieves a short value using the specified key, or 0 if no such key was
     * stored.
     */
    public short getShort(String key) {
        try {
            return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) tagMap.get(key)).getShort();
        } catch (ClassCastException var3) {
            return (short) 0;
        }
    }

    /**
     * Retrieves an integer value using the specified key, or 0 if no such key
     * was stored.
     */
    public int getInteger(String key) {
        try {
            return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) tagMap.get(key)).getInt();
        } catch (ClassCastException var3) {
            return 0;
        }
    }

    /**
     * Retrieves a long value using the specified key, or 0 if no such key was
     * stored.
     */
    public long getLong(String key) {
        try {
            return !this.hasKey(key, 99) ? 0L : ((NBTBase.NBTPrimitive) tagMap.get(key)).getLong();
        } catch (ClassCastException var3) {
            return 0L;
        }
    }

    /**
     * Retrieves a float value using the specified key, or 0 if no such key was
     * stored.
     */
    public float getFloat(String key) {
        try {
            return !this.hasKey(key, 99) ? 0.0F : ((NBTBase.NBTPrimitive) tagMap.get(key)).getFloat();
        } catch (ClassCastException var3) {
            return 0.0F;
        }
    }

    /**
     * Retrieves a double value using the specified key, or 0 if no such key was
     * stored.
     */
    public double getDouble(String key) {
        try {
            return !this.hasKey(key, 99) ? 0.0D : ((NBTBase.NBTPrimitive) tagMap.get(key)).getDouble();
        } catch (ClassCastException var3) {
            return 0.0D;
        }
    }

    /**
     * Retrieves a string value using the specified key, or an empty string if
     * no such key was stored.
     */
    public String getString(String key) {
        try {
            return !this.hasKey(key, 8) ? "" : ((NBTBase) tagMap.get(key)).getString();
        } catch (ClassCastException var3) {
            return "";
        }
    }

    /**
     * Retrieves a byte array using the specified key, or a zero-length array if
     * no such key was stored.
     */
    public byte[] getByteArray(String key) {
        try {
            return !this.hasKey(key, 7) ? new byte[0] : ((NBTTagByteArray) tagMap.get(key)).getByteArray();
        } catch (ClassCastException var3) {
            throw new ReportedException(createCrashReport(key, 7, var3));
        }
    }

    /**
     * Retrieves an int array using the specified key, or a zero-length array if
     * no such key was stored.
     */
    public int[] getIntArray(String key) {
        try {
            return !this.hasKey(key, 11) ? new int[0] : ((NBTTagIntArray) tagMap.get(key)).getIntArray();
        } catch (ClassCastException var3) {
            throw new ReportedException(createCrashReport(key, 11, var3));
        }
    }

    /**
     * Retrieves a NBTTagCompound subtag matching the specified key, or a new
     * empty NBTTagCompound if no such key was stored.
     */
    public NBTTagCompound getCompoundTag(String key) {
        try {
            return !this.hasKey(key, 10) ? new NBTTagCompound() : (NBTTagCompound) tagMap.get(key);
        } catch (ClassCastException var3) {
            throw new ReportedException(createCrashReport(key, 10, var3));
        }
    }

    /**
     * Gets the NBTTagList object with the given name. Args: name, NBTBase type
     */
    public NBTTagList getTagList(String key, int type) {
        try {
            if (getTagType(key) != 9) {
                return new NBTTagList();
            } else {
                NBTTagList var3 = (NBTTagList) tagMap.get(key);
                return var3.tagCount() > 0 && var3.getTagType() != type ? new NBTTagList() : var3;
            }
        } catch (ClassCastException var4) {
            throw new ReportedException(createCrashReport(key, 9, var4));
        }
    }

    /**
     * Retrieves a boolean value using the specified key, or false if no such
     * key was stored. This uses the getByte method.
     */
    public boolean getBoolean(String key) {
        return getByte(key) != 0;
    }

    /**
     * Remove the specified tag.
     */
    public void removeTag(String key) {
        tagMap.remove(key);
    }

    @Override
    public String toString() {
        String var1 = "{";
        String var3;

        for (Iterator var2 = tagMap.keySet().iterator(); var2.hasNext(); var1 = var1 + var3 + ':' + tagMap.get(var3) + ',') {
            var3 = (String) var2.next();
        }

        return var1 + "}";
    }

    /**
     * Return whether this compound has no tags.
     */
    @Override
    public boolean hasNoTags() {
        return tagMap.isEmpty();
    }

    /**
     * Create a crash report which indicates a NBT read error.
     */
    private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex) {
        CrashReport var4 = CrashReport.makeCrashReport(ex, "Reading NBT data");
        CrashReportCategory var5 = var4.makeCategoryDepth("Corrupt NBT tag", 1);
        var5.addCrashSectionCallable("Tag type found", new Callable() {
            private static final String __OBFID = "CL_00001216";

            @Override
            public String call() {
                return NBTBase.NBT_TYPES[((NBTBase) tagMap.get(key)).getId()];
            }
        });
        var5.addCrashSectionCallable("Tag type expected", new Callable() {
            private static final String __OBFID = "CL_00001217";

            @Override
            public String call() {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        var5.addCrashSection("Tag name", key);
        return var4;
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        NBTTagCompound var1 = new NBTTagCompound();
        Iterator var2 = tagMap.keySet().iterator();

        while (var2.hasNext()) {
            String var3 = (String) var2.next();
            var1.setTag(var3, ((NBTBase) tagMap.get(var3)).copy());
        }

        return var1;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagCompound var2 = (NBTTagCompound) p_equals_1_;
            return tagMap.entrySet().equals(var2.tagMap.entrySet());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ tagMap.hashCode();
    }

    private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
        output.writeByte(data.getId());

        if (data.getId() != 0) {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
        return input.readUTF();
    }

    static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) {
        NBTBase var5 = NBTBase.createNewByType(id);

        try {
            var5.read(input, depth, sizeTracker);
            return var5;
        } catch (IOException var9) {
            CrashReport var7 = CrashReport.makeCrashReport(var9, "Loading NBT data");
            CrashReportCategory var8 = var7.makeCategory("NBT Tag");
            var8.addCrashSection("Tag name", key);
            var8.addCrashSection("Tag type", Byte.valueOf(id));
            throw new ReportedException(var7);
        }
    }

    /**
     * Merges this NBTTagCompound with the given compound. Any sub-compounds are
     * merged using the same methods, other types of tags are overwritten from
     * the given compound.
     */
    public void merge(NBTTagCompound other) {
        Iterator var2 = other.tagMap.keySet().iterator();

        while (var2.hasNext()) {
            String var3 = (String) var2.next();
            NBTBase var4 = (NBTBase) other.tagMap.get(var3);

            if (var4.getId() == 10) {
                if (this.hasKey(var3, 10)) {
                    NBTTagCompound var5 = getCompoundTag(var3);
                    var5.merge((NBTTagCompound) var4);
                } else {
                    setTag(var3, var4.copy());
                }
            } else {
                setTag(var3, var4.copy());
            }
        }
    }
}
