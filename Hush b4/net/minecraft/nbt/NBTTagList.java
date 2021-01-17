// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.util.Iterator;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase
{
    private static final Logger LOGGER;
    private List<NBTBase> tagList;
    private byte tagType;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public NBTTagList() {
        this.tagList = (List<NBTBase>)Lists.newArrayList();
        this.tagType = 0;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        if (!this.tagList.isEmpty()) {
            this.tagType = this.tagList.get(0).getId();
        }
        else {
            this.tagType = 0;
        }
        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());
        for (int i = 0; i < this.tagList.size(); ++i) {
            this.tagList.get(i).write(output);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(296L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        this.tagType = input.readByte();
        final int i = input.readInt();
        if (this.tagType == 0 && i > 0) {
            throw new RuntimeException("Missing type on ListTag");
        }
        sizeTracker.read(32L * i);
        this.tagList = (List<NBTBase>)Lists.newArrayListWithCapacity(i);
        for (int j = 0; j < i; ++j) {
            final NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
            nbtbase.read(input, depth + 1, sizeTracker);
            this.tagList.add(nbtbase);
        }
    }
    
    @Override
    public byte getId() {
        return 9;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("[");
        for (int i = 0; i < this.tagList.size(); ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(i).append(':').append(this.tagList.get(i));
        }
        return stringbuilder.append(']').toString();
    }
    
    public void appendTag(final NBTBase nbt) {
        if (nbt.getId() == 0) {
            NBTTagList.LOGGER.warn("Invalid TagEnd added to ListTag");
        }
        else {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            }
            else if (this.tagType != nbt.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.add(nbt);
        }
    }
    
    public void set(final int idx, final NBTBase nbt) {
        if (nbt.getId() == 0) {
            NBTTagList.LOGGER.warn("Invalid TagEnd added to ListTag");
        }
        else if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            }
            else if (this.tagType != nbt.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }
            this.tagList.set(idx, nbt);
        }
        else {
            NBTTagList.LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }
    
    public NBTBase removeTag(final int i) {
        return this.tagList.remove(i);
    }
    
    @Override
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }
    
    public NBTTagCompound getCompoundTagAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase nbtbase = this.tagList.get(i);
            return (NBTTagCompound)((nbtbase.getId() == 10) ? nbtbase : new NBTTagCompound());
        }
        return new NBTTagCompound();
    }
    
    public int[] getIntArrayAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase nbtbase = this.tagList.get(i);
            return (nbtbase.getId() == 11) ? ((NBTTagIntArray)nbtbase).getIntArray() : new int[0];
        }
        return new int[0];
    }
    
    public double getDoubleAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase nbtbase = this.tagList.get(i);
            return (nbtbase.getId() == 6) ? ((NBTTagDouble)nbtbase).getDouble() : 0.0;
        }
        return 0.0;
    }
    
    public float getFloatAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase nbtbase = this.tagList.get(i);
            return (nbtbase.getId() == 5) ? ((NBTTagFloat)nbtbase).getFloat() : 0.0f;
        }
        return 0.0f;
    }
    
    public String getStringTagAt(final int i) {
        if (i >= 0 && i < this.tagList.size()) {
            final NBTBase nbtbase = this.tagList.get(i);
            return (nbtbase.getId() == 8) ? nbtbase.getString() : nbtbase.toString();
        }
        return "";
    }
    
    public NBTBase get(final int idx) {
        return (idx >= 0 && idx < this.tagList.size()) ? this.tagList.get(idx) : new NBTTagEnd();
    }
    
    public int tagCount() {
        return this.tagList.size();
    }
    
    @Override
    public NBTBase copy() {
        final NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;
        for (final NBTBase nbtbase : this.tagList) {
            final NBTBase nbtbase2 = nbtbase.copy();
            nbttaglist.tagList.add(nbtbase2);
        }
        return nbttaglist;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            final NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
            if (this.tagType == nbttaglist.tagType) {
                return this.tagList.equals(nbttaglist.tagList);
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }
    
    public int getTagType() {
        return this.tagType;
    }
}
