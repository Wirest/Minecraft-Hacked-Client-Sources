package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** The array list containing the tags encapsulated in this list. */
    private List tagList = Lists.newArrayList();

    /**
     * The type byte for the tags in the list - they must all be of the same type.
     */
    private byte tagType = 0;
    private static final String __OBFID = "CL_00001224";

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        if (!this.tagList.isEmpty())
        {
            this.tagType = ((NBTBase)this.tagList.get(0)).getId();
        }
        else
        {
            this.tagType = 0;
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int var2 = 0; var2 < this.tagList.size(); ++var2)
        {
            ((NBTBase)this.tagList.get(var2)).write(output);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        if (depth > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            sizeTracker.read(8L);
            this.tagType = input.readByte();
            int var4 = input.readInt();
            this.tagList = Lists.newArrayList();

            for (int var5 = 0; var5 < var4; ++var5)
            {
                NBTBase var6 = NBTBase.createNewByType(this.tagType);
                var6.read(input, depth + 1, sizeTracker);
                this.tagList.add(var6);
            }
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)9;
    }

    public String toString()
    {
        String var1 = "[";
        int var2 = 0;

        for (Iterator var3 = this.tagList.iterator(); var3.hasNext(); ++var2)
        {
            NBTBase var4 = (NBTBase)var3.next();
            var1 = var1 + "" + var2 + ':' + var4 + ',';
        }

        return var1 + "]";
    }

    /**
     * Adds the provided tag to the end of the list. There is no check to verify this tag is of the same type as any
     * previous tag.
     */
    public void appendTag(NBTBase nbt)
    {
        if (this.tagType == 0)
        {
            this.tagType = nbt.getId();
        }
        else if (this.tagType != nbt.getId())
        {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
        }

        this.tagList.add(nbt);
    }

    /**
     * Set the given index to the given tag
     */
    public void set(int idx, NBTBase nbt)
    {
        if (idx >= 0 && idx < this.tagList.size())
        {
            if (this.tagType == 0)
            {
                this.tagType = nbt.getId();
            }
            else if (this.tagType != nbt.getId())
            {
                LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.tagList.set(idx, nbt);
        }
        else
        {
            LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }

    /**
     * Removes a tag at the given index.
     */
    public NBTBase removeTag(int i)
    {
        return (NBTBase)this.tagList.remove(i);
    }

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags()
    {
        return this.tagList.isEmpty();
    }

    /**
     * Retrieves the NBTTagCompound at the specified index in the list
     */
    public NBTTagCompound getCompoundTagAt(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 10 ? (NBTTagCompound)var2 : new NBTTagCompound();
        }
        else
        {
            return new NBTTagCompound();
        }
    }

    public int[] getIntArray(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 11 ? ((NBTTagIntArray)var2).getIntArray() : new int[0];
        }
        else
        {
            return new int[0];
        }
    }

    public double getDouble(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 6 ? ((NBTTagDouble)var2).getDouble() : 0.0D;
        }
        else
        {
            return 0.0D;
        }
    }

    public float getFloat(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 5 ? ((NBTTagFloat)var2).getFloat() : 0.0F;
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Retrieves the tag String value at the specified index in the list
     */
    public String getStringTagAt(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase var2 = (NBTBase)this.tagList.get(i);
            return var2.getId() == 8 ? var2.getString() : var2.toString();
        }
        else
        {
            return "";
        }
    }

    /**
     * Get the tag at the given position
     */
    public NBTBase get(int idx)
    {
        return (NBTBase)(idx >= 0 && idx < this.tagList.size() ? (NBTBase)this.tagList.get(idx) : new NBTTagEnd());
    }

    /**
     * Returns the number of tags in the list.
     */
    public int tagCount()
    {
        return this.tagList.size();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        NBTTagList var1 = new NBTTagList();
        var1.tagType = this.tagType;
        Iterator var2 = this.tagList.iterator();

        while (var2.hasNext())
        {
            NBTBase var3 = (NBTBase)var2.next();
            NBTBase var4 = var3.copy();
            var1.tagList.add(var4);
        }

        return var1;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagList var2 = (NBTTagList)p_equals_1_;

            if (this.tagType == var2.tagType)
            {
                return this.tagList.equals(var2.tagList);
            }
        }

        return false;
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType()
    {
        return this.tagType;
    }
}
