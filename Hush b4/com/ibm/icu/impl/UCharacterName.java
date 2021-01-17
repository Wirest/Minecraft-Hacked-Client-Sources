// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.ibm.icu.text.UnicodeSet;
import java.util.Locale;

public final class UCharacterName
{
    public static final UCharacterName INSTANCE;
    public static final int LINES_PER_GROUP_ = 32;
    public int m_groupcount_;
    int m_groupsize_;
    private char[] m_tokentable_;
    private byte[] m_tokenstring_;
    private char[] m_groupinfo_;
    private byte[] m_groupstring_;
    private AlgorithmName[] m_algorithm_;
    private char[] m_groupoffsets_;
    private char[] m_grouplengths_;
    private static final String NAME_FILE_NAME_ = "data/icudt51b/unames.icu";
    private static final int GROUP_SHIFT_ = 5;
    private static final int GROUP_MASK_ = 31;
    private static final int NAME_BUFFER_SIZE_ = 100000;
    private static final int OFFSET_HIGH_OFFSET_ = 1;
    private static final int OFFSET_LOW_OFFSET_ = 2;
    private static final int SINGLE_NIBBLE_MAX_ = 11;
    private int[] m_nameSet_;
    private int[] m_ISOCommentSet_;
    private StringBuffer m_utilStringBuffer_;
    private int[] m_utilIntBuffer_;
    private int m_maxISOCommentLength_;
    private int m_maxNameLength_;
    private static final String[] TYPE_NAMES_;
    private static final String UNKNOWN_TYPE_NAME_ = "unknown";
    private static final int NON_CHARACTER_ = 30;
    private static final int LEAD_SURROGATE_ = 31;
    private static final int TRAIL_SURROGATE_ = 32;
    static final int EXTENDED_CATEGORY_ = 33;
    
    public String getName(final int ch, final int choice) {
        if (ch < 0 || ch > 1114111 || choice > 4) {
            return null;
        }
        String result = null;
        result = this.getAlgName(ch, choice);
        if (result == null || result.length() == 0) {
            if (choice == 2) {
                result = this.getExtendedName(ch);
            }
            else {
                result = this.getGroupName(ch, choice);
            }
        }
        return result;
    }
    
    public int getCharFromName(final int choice, final String name) {
        if (choice >= 4 || name == null || name.length() == 0) {
            return -1;
        }
        int result = getExtendedChar(name.toLowerCase(Locale.ENGLISH), choice);
        if (result >= -1) {
            return result;
        }
        final String upperCaseName = name.toUpperCase(Locale.ENGLISH);
        if (choice == 0 || choice == 2) {
            int count = 0;
            if (this.m_algorithm_ != null) {
                count = this.m_algorithm_.length;
            }
            --count;
            while (count >= 0) {
                result = this.m_algorithm_[count].getChar(upperCaseName);
                if (result >= 0) {
                    return result;
                }
                --count;
            }
        }
        if (choice == 2) {
            result = this.getGroupChar(upperCaseName, 0);
            if (result == -1) {
                result = this.getGroupChar(upperCaseName, 3);
            }
        }
        else {
            result = this.getGroupChar(upperCaseName, choice);
        }
        return result;
    }
    
    public int getGroupLengths(int index, final char[] offsets, final char[] lengths) {
        char length = '\uffff';
        byte b = 0;
        byte n = 0;
        index *= this.m_groupsize_;
        int stringoffset = UCharacterUtility.toInt(this.m_groupinfo_[index + 1], this.m_groupinfo_[index + 2]);
        offsets[0] = '\0';
        int i = 0;
        while (i < 32) {
            b = this.m_groupstring_[stringoffset];
            for (int shift = 4; shift >= 0; shift -= 4) {
                n = (byte)(b >> shift & 0xF);
                if (length == '\uffff' && n > 11) {
                    length = (char)(n - 12 << 4);
                }
                else {
                    if (length != '\uffff') {
                        lengths[i] = (char)((length | n) + 12);
                    }
                    else {
                        lengths[i] = (char)n;
                    }
                    if (i < 32) {
                        offsets[i + 1] = (char)(offsets[i] + lengths[i]);
                    }
                    length = '\uffff';
                    ++i;
                }
            }
            ++stringoffset;
        }
        return stringoffset;
    }
    
    public String getGroupName(int index, int length, final int choice) {
        if (choice != 0 && choice != 2) {
            if (59 >= this.m_tokentable_.length || this.m_tokentable_[59] == '\uffff') {
                int fieldIndex = (choice == 4) ? 2 : choice;
                do {
                    final int oldindex = index;
                    index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, length, (byte)59);
                    length -= index - oldindex;
                } while (--fieldIndex > 0);
            }
            else {
                length = 0;
            }
        }
        synchronized (this.m_utilStringBuffer_) {
            this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
            int i = 0;
            while (i < length) {
                final byte b = this.m_groupstring_[index + i];
                ++i;
                if (b >= this.m_tokentable_.length) {
                    if (b == 59) {
                        break;
                    }
                    this.m_utilStringBuffer_.append(b);
                }
                else {
                    char token = this.m_tokentable_[b & 0xFF];
                    if (token == '\ufffe') {
                        token = this.m_tokentable_[b << 8 | (this.m_groupstring_[index + i] & 0xFF)];
                        ++i;
                    }
                    if (token == '\uffff') {
                        if (b == 59) {
                            if (this.m_utilStringBuffer_.length() == 0 && choice == 2) {
                                continue;
                            }
                            break;
                        }
                        else {
                            this.m_utilStringBuffer_.append((char)(b & 0xFF));
                        }
                    }
                    else {
                        UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
                    }
                }
            }
            if (this.m_utilStringBuffer_.length() > 0) {
                return this.m_utilStringBuffer_.toString();
            }
        }
        return null;
    }
    
    public String getExtendedName(final int ch) {
        String result = this.getName(ch, 0);
        if (result == null && result == null) {
            result = this.getExtendedOr10Name(ch);
        }
        return result;
    }
    
    public int getGroup(final int codepoint) {
        int endGroup = this.m_groupcount_;
        final int msb = getCodepointMSB(codepoint);
        int result = 0;
        while (result < endGroup - 1) {
            final int gindex = result + endGroup >> 1;
            if (msb < this.getGroupMSB(gindex)) {
                endGroup = gindex;
            }
            else {
                result = gindex;
            }
        }
        return result;
    }
    
    public String getExtendedOr10Name(final int ch) {
        String result = null;
        if (result == null) {
            final int type = getType(ch);
            if (type >= UCharacterName.TYPE_NAMES_.length) {
                result = "unknown";
            }
            else {
                result = UCharacterName.TYPE_NAMES_[type];
            }
            synchronized (this.m_utilStringBuffer_) {
                this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                this.m_utilStringBuffer_.append('<');
                this.m_utilStringBuffer_.append(result);
                this.m_utilStringBuffer_.append('-');
                final String chStr = Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
                for (int zeros = 4 - chStr.length(); zeros > 0; --zeros) {
                    this.m_utilStringBuffer_.append('0');
                }
                this.m_utilStringBuffer_.append(chStr);
                this.m_utilStringBuffer_.append('>');
                result = this.m_utilStringBuffer_.toString();
            }
        }
        return result;
    }
    
    public int getGroupMSB(final int gindex) {
        if (gindex >= this.m_groupcount_) {
            return -1;
        }
        return this.m_groupinfo_[gindex * this.m_groupsize_];
    }
    
    public static int getCodepointMSB(final int codepoint) {
        return codepoint >> 5;
    }
    
    public static int getGroupLimit(final int msb) {
        return (msb << 5) + 32;
    }
    
    public static int getGroupMin(final int msb) {
        return msb << 5;
    }
    
    public static int getGroupOffset(final int codepoint) {
        return codepoint & 0x1F;
    }
    
    public static int getGroupMinFromCodepoint(final int codepoint) {
        return codepoint & 0xFFFFFFE0;
    }
    
    public int getAlgorithmLength() {
        return this.m_algorithm_.length;
    }
    
    public int getAlgorithmStart(final int index) {
        return this.m_algorithm_[index].m_rangestart_;
    }
    
    public int getAlgorithmEnd(final int index) {
        return this.m_algorithm_[index].m_rangeend_;
    }
    
    public String getAlgorithmName(final int index, final int codepoint) {
        String result = null;
        synchronized (this.m_utilStringBuffer_) {
            this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
            this.m_algorithm_[index].appendName(codepoint, this.m_utilStringBuffer_);
            result = this.m_utilStringBuffer_.toString();
        }
        return result;
    }
    
    public synchronized String getGroupName(final int ch, final int choice) {
        final int msb = getCodepointMSB(ch);
        final int group = this.getGroup(ch);
        if (msb == this.m_groupinfo_[group * this.m_groupsize_]) {
            final int index = this.getGroupLengths(group, this.m_groupoffsets_, this.m_grouplengths_);
            final int offset = ch & 0x1F;
            return this.getGroupName(index + this.m_groupoffsets_[offset], this.m_grouplengths_[offset], choice);
        }
        return null;
    }
    
    public int getMaxCharNameLength() {
        if (this.initNameSetsLengths()) {
            return this.m_maxNameLength_;
        }
        return 0;
    }
    
    public int getMaxISOCommentLength() {
        if (this.initNameSetsLengths()) {
            return this.m_maxISOCommentLength_;
        }
        return 0;
    }
    
    public void getCharNameCharacters(final UnicodeSet set) {
        this.convert(this.m_nameSet_, set);
    }
    
    public void getISOCommentCharacters(final UnicodeSet set) {
        this.convert(this.m_ISOCommentSet_, set);
    }
    
    boolean setToken(final char[] token, final byte[] tokenstring) {
        if (token != null && tokenstring != null && token.length > 0 && tokenstring.length > 0) {
            this.m_tokentable_ = token;
            this.m_tokenstring_ = tokenstring;
            return true;
        }
        return false;
    }
    
    boolean setAlgorithm(final AlgorithmName[] alg) {
        if (alg != null && alg.length != 0) {
            this.m_algorithm_ = alg;
            return true;
        }
        return false;
    }
    
    boolean setGroupCountSize(final int count, final int size) {
        if (count <= 0 || size <= 0) {
            return false;
        }
        this.m_groupcount_ = count;
        this.m_groupsize_ = size;
        return true;
    }
    
    boolean setGroup(final char[] group, final byte[] groupstring) {
        if (group != null && groupstring != null && group.length > 0 && groupstring.length > 0) {
            this.m_groupinfo_ = group;
            this.m_groupstring_ = groupstring;
            return true;
        }
        return false;
    }
    
    private UCharacterName() throws IOException {
        this.m_groupcount_ = 0;
        this.m_groupsize_ = 0;
        this.m_groupoffsets_ = new char[33];
        this.m_grouplengths_ = new char[33];
        this.m_nameSet_ = new int[8];
        this.m_ISOCommentSet_ = new int[8];
        this.m_utilStringBuffer_ = new StringBuffer();
        this.m_utilIntBuffer_ = new int[2];
        final InputStream is = ICUData.getRequiredStream("data/icudt51b/unames.icu");
        final BufferedInputStream b = new BufferedInputStream(is, 100000);
        final UCharacterNameReader reader = new UCharacterNameReader(b);
        reader.read(this);
        b.close();
    }
    
    private String getAlgName(final int ch, final int choice) {
        if (choice == 0 || choice == 2) {
            synchronized (this.m_utilStringBuffer_) {
                this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                for (int index = this.m_algorithm_.length - 1; index >= 0; --index) {
                    if (this.m_algorithm_[index].contains(ch)) {
                        this.m_algorithm_[index].appendName(ch, this.m_utilStringBuffer_);
                        return this.m_utilStringBuffer_.toString();
                    }
                }
            }
        }
        return null;
    }
    
    private synchronized int getGroupChar(final String name, final int choice) {
        for (int i = 0; i < this.m_groupcount_; ++i) {
            final int startgpstrindex = this.getGroupLengths(i, this.m_groupoffsets_, this.m_grouplengths_);
            final int result = this.getGroupChar(startgpstrindex, this.m_grouplengths_, name, choice);
            if (result != -1) {
                return this.m_groupinfo_[i * this.m_groupsize_] << 5 | result;
            }
        }
        return -1;
    }
    
    private int getGroupChar(int index, final char[] length, final String name, final int choice) {
        byte b = 0;
        final int namelen = name.length();
        for (int result = 0; result <= 32; ++result) {
            int nindex = 0;
            int len = length[result];
            if (choice != 0 && choice != 2) {
                int fieldIndex = (choice == 4) ? 2 : choice;
                do {
                    final int oldindex = index;
                    index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, len, (byte)59);
                    len -= index - oldindex;
                } while (--fieldIndex > 0);
            }
            int count = 0;
            while (count < len && nindex != -1 && nindex < namelen) {
                b = this.m_groupstring_[index + count];
                ++count;
                if (b >= this.m_tokentable_.length) {
                    if (name.charAt(nindex++) == (b & 0xFF)) {
                        continue;
                    }
                    nindex = -1;
                }
                else {
                    char token = this.m_tokentable_[b & 0xFF];
                    if (token == '\ufffe') {
                        token = this.m_tokentable_[b << 8 | (this.m_groupstring_[index + count] & 0xFF)];
                        ++count;
                    }
                    if (token == '\uffff') {
                        if (name.charAt(nindex++) == (b & 0xFF)) {
                            continue;
                        }
                        nindex = -1;
                    }
                    else {
                        nindex = UCharacterUtility.compareNullTermByteSubString(name, this.m_tokenstring_, nindex, token);
                    }
                }
            }
            if (namelen == nindex && (count == len || this.m_groupstring_[index + count] == 59)) {
                return result;
            }
            index += len;
        }
        return -1;
    }
    
    private static int getType(final int ch) {
        if (UCharacterUtility.isNonCharacter(ch)) {
            return 30;
        }
        int result = UCharacter.getType(ch);
        if (result == 18) {
            if (ch <= 56319) {
                result = 31;
            }
            else {
                result = 32;
            }
        }
        return result;
    }
    
    private static int getExtendedChar(final String name, final int choice) {
        if (name.charAt(0) == '<') {
            if (choice == 2) {
                final int endIndex = name.length() - 1;
                if (name.charAt(endIndex) == '>') {
                    int startIndex = name.lastIndexOf(45);
                    if (startIndex >= 0) {
                        ++startIndex;
                        int result = -1;
                        try {
                            result = Integer.parseInt(name.substring(startIndex, endIndex), 16);
                        }
                        catch (NumberFormatException e) {
                            return -1;
                        }
                        final String type = name.substring(1, startIndex - 1);
                        final int length = UCharacterName.TYPE_NAMES_.length;
                        int i = 0;
                        while (i < length) {
                            if (type.compareTo(UCharacterName.TYPE_NAMES_[i]) == 0) {
                                if (getType(result) == i) {
                                    return result;
                                }
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                    }
                }
            }
            return -1;
        }
        return -2;
    }
    
    private static void add(final int[] set, final char ch) {
        final int n = ch >>> 5;
        set[n] |= 1 << (ch & '\u001f');
    }
    
    private static boolean contains(final int[] set, final char ch) {
        return (set[ch >>> 5] & 1 << (ch & '\u001f')) != 0x0;
    }
    
    private static int add(final int[] set, final String str) {
        final int result = str.length();
        for (int i = result - 1; i >= 0; --i) {
            add(set, str.charAt(i));
        }
        return result;
    }
    
    private static int add(final int[] set, final StringBuffer str) {
        final int result = str.length();
        for (int i = result - 1; i >= 0; --i) {
            add(set, str.charAt(i));
        }
        return result;
    }
    
    private int addAlgorithmName(int maxlength) {
        int result = 0;
        for (int i = this.m_algorithm_.length - 1; i >= 0; --i) {
            result = this.m_algorithm_[i].add(this.m_nameSet_, maxlength);
            if (result > maxlength) {
                maxlength = result;
            }
        }
        return maxlength;
    }
    
    private int addExtendedName(int maxlength) {
        for (int i = UCharacterName.TYPE_NAMES_.length - 1; i >= 0; --i) {
            final int length = 9 + add(this.m_nameSet_, UCharacterName.TYPE_NAMES_[i]);
            if (length > maxlength) {
                maxlength = length;
            }
        }
        return maxlength;
    }
    
    private int[] addGroupName(final int offset, final int length, final byte[] tokenlength, final int[] set) {
        int resultnlength = 0;
        int resultplength = 0;
        while (resultplength < length) {
            char b = (char)(this.m_groupstring_[offset + resultplength] & 0xFF);
            ++resultplength;
            if (b == ';') {
                break;
            }
            if (b >= this.m_tokentable_.length) {
                add(set, b);
                ++resultnlength;
            }
            else {
                char token = this.m_tokentable_[b & '\u00ff'];
                if (token == '\ufffe') {
                    b = (char)(b << 8 | (this.m_groupstring_[offset + resultplength] & 0xFF));
                    token = this.m_tokentable_[b];
                    ++resultplength;
                }
                if (token == '\uffff') {
                    add(set, b);
                    ++resultnlength;
                }
                else {
                    byte tlength = tokenlength[b];
                    if (tlength == 0) {
                        synchronized (this.m_utilStringBuffer_) {
                            this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                            UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
                            tlength = (byte)add(set, this.m_utilStringBuffer_);
                        }
                        tokenlength[b] = tlength;
                    }
                    resultnlength += tlength;
                }
            }
        }
        this.m_utilIntBuffer_[0] = resultnlength;
        this.m_utilIntBuffer_[1] = resultplength;
        return this.m_utilIntBuffer_;
    }
    
    private void addGroupName(int maxlength) {
        int maxisolength = 0;
        final char[] offsets = new char[34];
        final char[] lengths = new char[34];
        final byte[] tokenlengths = new byte[this.m_tokentable_.length];
        for (int i = 0; i < this.m_groupcount_; ++i) {
            final int offset = this.getGroupLengths(i, offsets, lengths);
            for (int linenumber = 0; linenumber < 32; ++linenumber) {
                int lineoffset = offset + offsets[linenumber];
                int length = lengths[linenumber];
                if (length != 0) {
                    int[] parsed = this.addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
                    if (parsed[0] > maxlength) {
                        maxlength = parsed[0];
                    }
                    lineoffset += parsed[1];
                    if (parsed[1] < length) {
                        length -= parsed[1];
                        parsed = this.addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
                        if (parsed[0] > maxlength) {
                            maxlength = parsed[0];
                        }
                        lineoffset += parsed[1];
                        if (parsed[1] < length) {
                            length -= parsed[1];
                            parsed = this.addGroupName(lineoffset, length, tokenlengths, this.m_ISOCommentSet_);
                            if (parsed[1] > maxisolength) {
                                maxisolength = length;
                            }
                        }
                    }
                }
            }
        }
        this.m_maxISOCommentLength_ = maxisolength;
        this.m_maxNameLength_ = maxlength;
    }
    
    private boolean initNameSetsLengths() {
        if (this.m_maxNameLength_ > 0) {
            return true;
        }
        final String extra = "0123456789ABCDEF<>-";
        for (int i = extra.length() - 1; i >= 0; --i) {
            add(this.m_nameSet_, extra.charAt(i));
        }
        this.m_maxNameLength_ = this.addAlgorithmName(0);
        this.addGroupName(this.m_maxNameLength_ = this.addExtendedName(this.m_maxNameLength_));
        return true;
    }
    
    private void convert(final int[] set, final UnicodeSet uset) {
        uset.clear();
        if (!this.initNameSetsLengths()) {
            return;
        }
        for (char c = '\u00ff'; c > '\0'; --c) {
            if (contains(set, c)) {
                uset.add(c);
            }
        }
    }
    
    static {
        try {
            INSTANCE = new UCharacterName();
        }
        catch (IOException e) {
            throw new MissingResourceException("Could not construct UCharacterName. Missing unames.icu", "", "");
        }
        TYPE_NAMES_ = new String[] { "unassigned", "uppercase letter", "lowercase letter", "titlecase letter", "modifier letter", "other letter", "non spacing mark", "enclosing mark", "combining spacing mark", "decimal digit number", "letter number", "other number", "space separator", "line separator", "paragraph separator", "control", "format", "private use area", "surrogate", "dash punctuation", "start punctuation", "end punctuation", "connector punctuation", "other punctuation", "math symbol", "currency symbol", "modifier symbol", "other symbol", "initial punctuation", "final punctuation", "noncharacter", "lead surrogate", "trail surrogate" };
    }
    
    static final class AlgorithmName
    {
        static final int TYPE_0_ = 0;
        static final int TYPE_1_ = 1;
        private int m_rangestart_;
        private int m_rangeend_;
        private byte m_type_;
        private byte m_variant_;
        private char[] m_factor_;
        private String m_prefix_;
        private byte[] m_factorstring_;
        private StringBuffer m_utilStringBuffer_;
        private int[] m_utilIntBuffer_;
        
        AlgorithmName() {
            this.m_utilStringBuffer_ = new StringBuffer();
            this.m_utilIntBuffer_ = new int[256];
        }
        
        boolean setInfo(final int rangestart, final int rangeend, final byte type, final byte variant) {
            if (rangestart >= 0 && rangestart <= rangeend && rangeend <= 1114111 && (type == 0 || type == 1)) {
                this.m_rangestart_ = rangestart;
                this.m_rangeend_ = rangeend;
                this.m_type_ = type;
                this.m_variant_ = variant;
                return true;
            }
            return false;
        }
        
        boolean setFactor(final char[] factor) {
            if (factor.length == this.m_variant_) {
                this.m_factor_ = factor;
                return true;
            }
            return false;
        }
        
        boolean setPrefix(final String prefix) {
            if (prefix != null && prefix.length() > 0) {
                this.m_prefix_ = prefix;
                return true;
            }
            return false;
        }
        
        boolean setFactorString(final byte[] string) {
            this.m_factorstring_ = string;
            return true;
        }
        
        boolean contains(final int ch) {
            return this.m_rangestart_ <= ch && ch <= this.m_rangeend_;
        }
        
        void appendName(final int ch, final StringBuffer str) {
            str.append(this.m_prefix_);
            switch (this.m_type_) {
                case 0: {
                    str.append(Utility.hex(ch, this.m_variant_));
                    break;
                }
                case 1: {
                    int offset = ch - this.m_rangestart_;
                    final int[] indexes = this.m_utilIntBuffer_;
                    synchronized (this.m_utilIntBuffer_) {
                        for (int i = this.m_variant_ - 1; i > 0; --i) {
                            final int factor = this.m_factor_[i] & '\u00ff';
                            indexes[i] = offset % factor;
                            offset /= factor;
                        }
                        indexes[0] = offset;
                        str.append(this.getFactorString(indexes, this.m_variant_));
                    }
                    break;
                }
            }
        }
        
        int getChar(final String name) {
            final int prefixlen = this.m_prefix_.length();
            if (name.length() < prefixlen || !this.m_prefix_.equals(name.substring(0, prefixlen))) {
                return -1;
            }
            switch (this.m_type_) {
                case 0: {
                    try {
                        final int result = Integer.parseInt(name.substring(prefixlen), 16);
                        if (this.m_rangestart_ <= result && result <= this.m_rangeend_) {
                            return result;
                        }
                        break;
                    }
                    catch (NumberFormatException e) {
                        return -1;
                    }
                }
                case 1: {
                    for (int ch = this.m_rangestart_; ch <= this.m_rangeend_; ++ch) {
                        int offset = ch - this.m_rangestart_;
                        final int[] indexes = this.m_utilIntBuffer_;
                        synchronized (this.m_utilIntBuffer_) {
                            for (int i = this.m_variant_ - 1; i > 0; --i) {
                                final int factor = this.m_factor_[i] & '\u00ff';
                                indexes[i] = offset % factor;
                                offset /= factor;
                            }
                            indexes[0] = offset;
                            if (this.compareFactorString(indexes, this.m_variant_, name, prefixlen)) {
                                return ch;
                            }
                        }
                    }
                    break;
                }
            }
            return -1;
        }
        
        int add(final int[] set, final int maxlength) {
            int length = add(set, this.m_prefix_);
            switch (this.m_type_) {
                case 0: {
                    length += this.m_variant_;
                    break;
                }
                case 1: {
                    for (int i = this.m_variant_ - 1; i > 0; --i) {
                        int maxfactorlength = 0;
                        int count = 0;
                        for (int factor = this.m_factor_[i]; factor > 0; --factor) {
                            synchronized (this.m_utilStringBuffer_) {
                                this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                                count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
                                add(set, this.m_utilStringBuffer_);
                                if (this.m_utilStringBuffer_.length() > maxfactorlength) {
                                    maxfactorlength = this.m_utilStringBuffer_.length();
                                }
                            }
                        }
                        length += maxfactorlength;
                    }
                    break;
                }
            }
            if (length > maxlength) {
                return length;
            }
            return maxlength;
        }
        
        private String getFactorString(final int[] index, final int length) {
            int size = this.m_factor_.length;
            if (index == null || length != size) {
                return null;
            }
            synchronized (this.m_utilStringBuffer_) {
                this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                int count = 0;
                --size;
                for (int i = 0; i <= size; ++i) {
                    final int factor = this.m_factor_[i];
                    count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
                    count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
                    if (i != size) {
                        count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i] - 1);
                    }
                }
                return this.m_utilStringBuffer_.toString();
            }
        }
        
        private boolean compareFactorString(final int[] index, final int length, final String str, final int offset) {
            int size = this.m_factor_.length;
            if (index == null || length != size) {
                return false;
            }
            int count = 0;
            int strcount = offset;
            --size;
            for (int i = 0; i <= size; ++i) {
                final int factor = this.m_factor_[i];
                count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
                strcount = UCharacterUtility.compareNullTermByteSubString(str, this.m_factorstring_, strcount, count);
                if (strcount < 0) {
                    return false;
                }
                if (i != size) {
                    count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i]);
                }
            }
            return strcount == str.length();
        }
    }
}
