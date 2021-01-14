package net.minecraft.nbt;

import com.google.common.collect.Lists;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The array list containing the tags encapsulated in this list.
     */
    private List tagList = Lists.newArrayList();

    /**
     * The type byte for the tags in the list - they must all be of the same
     * type.
     */
    private byte tagType = 0;
    private static final String __OBFID = "CL_00001224";

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    @Override
    void write(DataOutput output) throws IOException {
        if (!tagList.isEmpty()) {
            tagType = ((NBTBase) tagList.get(0)).getId();
        } else {
            tagType = 0;
        }

        output.writeByte(tagType);
        output.writeInt(tagList.size());

        for (int var2 = 0; var2 < tagList.size(); ++var2) {
            ((NBTBase) tagList.get(var2)).write(output);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            sizeTracker.read(8L);
            tagType = input.readByte();
            int var4 = input.readInt();
            tagList = Lists.newArrayList();

            for (int var5 = 0; var5 < var4; ++var5) {
                NBTBase var6 = NBTBase.createNewByType(tagType);
                var6.read(input, depth + 1, sizeTracker);
                tagList.add(var6);
            }
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    @Override
    public byte getId() {
        return (byte) 9;
    }

    @Override
    public String toString() {
        String var1 = "[";
        int var2 = 0;

        for (Iterator var3 = tagList.iterator(); var3.hasNext(); ++var2) {
            NBTBase var4 = (NBTBase) var3.next();
            var1 = var1 + "" + var2 + ':' + var4 + ',';
        }

        return var1 + "]";
    }

    /**
     * Adds the provided tag to the end of the list. There is no check to verify
     * this tag is of the same type as any previous tag.
     */
    public void appendTag(NBTBase nbt) {
        if (tagType == 0) {
            tagType = nbt.getId();
        } else if (tagType != nbt.getId()) {
            NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
            return;
        }

        tagList.add(nbt);
    }

    /**
     * Set the given index to the given tag
     */
    public void set(int idx, NBTBase nbt) {
        if (idx >= 0 && idx < tagList.size()) {
            if (tagType == 0) {
                tagType = nbt.getId();
            } else if (tagType != nbt.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }

            tagList.set(idx, nbt);
        } else {
            NBTTagList.LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }

    /**
     * Removes a tag at the given index.
     */
    public NBTBase removeTag(int i) {
        return (NBTBase) tagList.remove(i);
    }

    /**
     * Return whether this compound has no tags.
     */
    @Override
    public boolean hasNoTags() {
        return tagList.isEmpty();
    }

    /**
     * Retrieves the NBTTagCompound at the specified index in the list
     */
    public NBTTagCompound getCompoundTagAt(int i) {
        if (i >= 0 && i < tagList.size()) {
            NBTBase var2 = (NBTBase) tagList.get(i);
            return var2.getId() == 10 ? (NBTTagCompound) var2 : new NBTTagCompound();
        } else {
            return new NBTTagCompound();
        }
    }

    public int[] getIntArray(int i) {
        if (i >= 0 && i < tagList.size()) {
            NBTBase var2 = (NBTBase) tagList.get(i);
            return var2.getId() == 11 ? ((NBTTagIntArray) var2).getIntArray() : new int[0];
        } else {
            return new int[0];
        }
    }

    public double getDouble(int i) {
        if (i >= 0 && i < tagList.size()) {
            NBTBase var2 = (NBTBase) tagList.get(i);
            return var2.getId() == 6 ? ((NBTTagDouble) var2).getDouble() : 0.0D;
        } else {
            return 0.0D;
        }
    }

    public float getFloat(int i) {
        if (i >= 0 && i < tagList.size()) {
            NBTBase var2 = (NBTBase) tagList.get(i);
            return var2.getId() == 5 ? ((NBTTagFloat) var2).getFloat() : 0.0F;
        } else {
            return 0.0F;
        }
    }

    /**
     * Retrieves the tag String value at the specified index in the list
     */
    public String getStringTagAt(int i) {
        if (i >= 0 && i < tagList.size()) {
            NBTBase var2 = (NBTBase) tagList.get(i);
            return var2.getId() == 8 ? var2.getString() : var2.toString();
        } else {
            return "";
        }
    }

    /**
     * Get the tag at the given position
     */
    public NBTBase get(int idx) {
        return idx >= 0 && idx < tagList.size() ? (NBTBase) tagList.get(idx) : new NBTTagEnd();
    }

    /**
     * Returns the number of tags in the list.
     */
    public int tagCount() {
        return tagList.size();
    }

    /**
     * Creates a clone of the tag.
     */
    @Override
    public NBTBase copy() {
        NBTTagList var1 = new NBTTagList();
        var1.tagType = tagType;
        Iterator var2 = tagList.iterator();

        while (var2.hasNext()) {
            NBTBase var3 = (NBTBase) var2.next();
            NBTBase var4 = var3.copy();
            var1.tagList.add(var4);
        }

        return var1;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagList var2 = (NBTTagList) p_equals_1_;

            if (tagType == var2.tagType) {
                return tagList.equals(var2.tagList);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ tagList.hashCode();
    }

    public int getTagType() {
        return tagType;
    }
}
