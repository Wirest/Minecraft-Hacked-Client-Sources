// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.Arrays;

public class NbtTagValue
{
    private String[] parents;
    private String name;
    private int type;
    private String value;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_PATTERN = 1;
    private static final int TYPE_IPATTERN = 2;
    private static final int TYPE_REGEX = 3;
    private static final int TYPE_IREGEX = 4;
    private static final String PREFIX_PATTERN = "pattern:";
    private static final String PREFIX_IPATTERN = "ipattern:";
    private static final String PREFIX_REGEX = "regex:";
    private static final String PREFIX_IREGEX = "iregex:";
    
    public NbtTagValue(final String p_i69_1_, String p_i69_2_) {
        this.parents = null;
        this.name = null;
        this.type = 0;
        this.value = null;
        final String[] astring = Config.tokenize(p_i69_1_, ".");
        this.parents = Arrays.copyOfRange(astring, 0, astring.length - 1);
        this.name = astring[astring.length - 1];
        if (p_i69_2_.startsWith("pattern:")) {
            this.type = 1;
            p_i69_2_ = p_i69_2_.substring("pattern:".length());
        }
        else if (p_i69_2_.startsWith("ipattern:")) {
            this.type = 2;
            p_i69_2_ = p_i69_2_.substring("ipattern:".length()).toLowerCase();
        }
        else if (p_i69_2_.startsWith("regex:")) {
            this.type = 3;
            p_i69_2_ = p_i69_2_.substring("regex:".length());
        }
        else if (p_i69_2_.startsWith("iregex:")) {
            this.type = 4;
            p_i69_2_ = p_i69_2_.substring("iregex:".length()).toLowerCase();
        }
        else {
            this.type = 0;
        }
        p_i69_2_ = StringEscapeUtils.unescapeJava(p_i69_2_);
        this.value = p_i69_2_;
    }
    
    public boolean matches(final NBTTagCompound p_matches_1_) {
        if (p_matches_1_ == null) {
            return false;
        }
        NBTBase nbtbase = p_matches_1_;
        for (int i = 0; i < this.parents.length; ++i) {
            final String s = this.parents[i];
            nbtbase = getChildTag(nbtbase, s);
            if (nbtbase == null) {
                return false;
            }
        }
        if (this.name.equals("*")) {
            return this.matchesAnyChild(nbtbase);
        }
        nbtbase = getChildTag(nbtbase, this.name);
        return nbtbase != null && this.matches(nbtbase);
    }
    
    private boolean matchesAnyChild(final NBTBase p_matchesAnyChild_1_) {
        if (p_matchesAnyChild_1_ instanceof NBTTagCompound) {
            final NBTTagCompound nbttagcompound = (NBTTagCompound)p_matchesAnyChild_1_;
            for (final String s : nbttagcompound.getKeySet()) {
                final NBTBase nbtbase = nbttagcompound.getTag(s);
                if (this.matches(nbtbase)) {
                    return true;
                }
            }
        }
        if (p_matchesAnyChild_1_ instanceof NBTTagList) {
            final NBTTagList nbttaglist = (NBTTagList)p_matchesAnyChild_1_;
            for (int i = nbttaglist.tagCount(), j = 0; j < i; ++j) {
                final NBTBase nbtbase2 = nbttaglist.get(j);
                if (this.matches(nbtbase2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static NBTBase getChildTag(final NBTBase p_getChildTag_0_, final String p_getChildTag_1_) {
        if (p_getChildTag_0_ instanceof NBTTagCompound) {
            final NBTTagCompound nbttagcompound = (NBTTagCompound)p_getChildTag_0_;
            return nbttagcompound.getTag(p_getChildTag_1_);
        }
        if (p_getChildTag_0_ instanceof NBTTagList) {
            final NBTTagList nbttaglist = (NBTTagList)p_getChildTag_0_;
            final int i = Config.parseInt(p_getChildTag_1_, -1);
            return (i < 0) ? null : nbttaglist.get(i);
        }
        return null;
    }
    
    private boolean matches(final NBTBase p_matches_1_) {
        if (p_matches_1_ == null) {
            return false;
        }
        final String s = getValue(p_matches_1_);
        if (s == null) {
            return false;
        }
        switch (this.type) {
            case 0: {
                return s.equals(this.value);
            }
            case 1: {
                return this.matchesPattern(s, this.value);
            }
            case 2: {
                return this.matchesPattern(s.toLowerCase(), this.value);
            }
            case 3: {
                return this.matchesRegex(s, this.value);
            }
            case 4: {
                return this.matchesRegex(s.toLowerCase(), this.value);
            }
            default: {
                throw new IllegalArgumentException("Unknown NbtTagValue type: " + this.type);
            }
        }
    }
    
    private boolean matchesPattern(final String p_matchesPattern_1_, final String p_matchesPattern_2_) {
        return StrUtils.equalsMask(p_matchesPattern_1_, p_matchesPattern_2_, '*', '?');
    }
    
    private boolean matchesRegex(final String p_matchesRegex_1_, final String p_matchesRegex_2_) {
        return p_matchesRegex_1_.matches(p_matchesRegex_2_);
    }
    
    private static String getValue(final NBTBase p_getValue_0_) {
        if (p_getValue_0_ == null) {
            return null;
        }
        if (p_getValue_0_ instanceof NBTTagString) {
            final NBTTagString nbttagstring = (NBTTagString)p_getValue_0_;
            return nbttagstring.getString();
        }
        if (p_getValue_0_ instanceof NBTTagInt) {
            final NBTTagInt nbttagint = (NBTTagInt)p_getValue_0_;
            return Integer.toString(nbttagint.getInt());
        }
        if (p_getValue_0_ instanceof NBTTagByte) {
            final NBTTagByte nbttagbyte = (NBTTagByte)p_getValue_0_;
            return Byte.toString(nbttagbyte.getByte());
        }
        if (p_getValue_0_ instanceof NBTTagShort) {
            final NBTTagShort nbttagshort = (NBTTagShort)p_getValue_0_;
            return Short.toString(nbttagshort.getShort());
        }
        if (p_getValue_0_ instanceof NBTTagLong) {
            final NBTTagLong nbttaglong = (NBTTagLong)p_getValue_0_;
            return Long.toString(nbttaglong.getLong());
        }
        if (p_getValue_0_ instanceof NBTTagFloat) {
            final NBTTagFloat nbttagfloat = (NBTTagFloat)p_getValue_0_;
            return Float.toString(nbttagfloat.getFloat());
        }
        if (p_getValue_0_ instanceof NBTTagDouble) {
            final NBTTagDouble nbttagdouble = (NBTTagDouble)p_getValue_0_;
            return Double.toString(nbttagdouble.getDouble());
        }
        return p_getValue_0_.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < this.parents.length; ++i) {
            final String s = this.parents[i];
            if (i > 0) {
                stringbuffer.append(".");
            }
            stringbuffer.append(s);
        }
        if (stringbuffer.length() > 0) {
            stringbuffer.append(".");
        }
        stringbuffer.append(this.name);
        stringbuffer.append(" = ");
        stringbuffer.append(this.value);
        return stringbuffer.toString();
    }
}
