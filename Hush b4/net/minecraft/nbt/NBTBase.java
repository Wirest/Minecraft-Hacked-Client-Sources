// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public abstract class NBTBase
{
    public static final String[] NBT_TYPES;
    
    static {
        NBT_TYPES = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
    }
    
    abstract void write(final DataOutput p0) throws IOException;
    
    abstract void read(final DataInput p0, final int p1, final NBTSizeTracker p2) throws IOException;
    
    @Override
    public abstract String toString();
    
    public abstract byte getId();
    
    protected static NBTBase createNewByType(final byte id) {
        switch (id) {
            case 0: {
                return new NBTTagEnd();
            }
            case 1: {
                return new NBTTagByte();
            }
            case 2: {
                return new NBTTagShort();
            }
            case 3: {
                return new NBTTagInt();
            }
            case 4: {
                return new NBTTagLong();
            }
            case 5: {
                return new NBTTagFloat();
            }
            case 6: {
                return new NBTTagDouble();
            }
            case 7: {
                return new NBTTagByteArray();
            }
            case 8: {
                return new NBTTagString();
            }
            case 9: {
                return new NBTTagList();
            }
            case 10: {
                return new NBTTagCompound();
            }
            case 11: {
                return new NBTTagIntArray();
            }
            default: {
                return null;
            }
        }
    }
    
    public abstract NBTBase copy();
    
    public boolean hasNoTags() {
        return false;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof NBTBase)) {
            return false;
        }
        final NBTBase nbtbase = (NBTBase)p_equals_1_;
        return this.getId() == nbtbase.getId();
    }
    
    @Override
    public int hashCode() {
        return this.getId();
    }
    
    protected String getString() {
        return this.toString();
    }
    
    public abstract static class NBTPrimitive extends NBTBase
    {
        public abstract long getLong();
        
        public abstract int getInt();
        
        public abstract short getShort();
        
        public abstract byte getByte();
        
        public abstract double getDouble();
        
        public abstract float getFloat();
    }
}
