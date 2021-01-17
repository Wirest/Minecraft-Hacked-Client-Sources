// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import java.util.Iterator;
import com.ibm.icu.text.UnicodeSet;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.VersionInfo;

public final class UCharacterProperty
{
    public static final UCharacterProperty INSTANCE;
    public Trie2_16 m_trie_;
    public VersionInfo m_unicodeVersion_;
    public static final char LATIN_CAPITAL_LETTER_I_WITH_DOT_ABOVE_ = '\u0130';
    public static final char LATIN_SMALL_LETTER_DOTLESS_I_ = '\u0131';
    public static final char LATIN_SMALL_LETTER_I_ = 'i';
    public static final int TYPE_MASK = 31;
    public static final int SRC_NONE = 0;
    public static final int SRC_CHAR = 1;
    public static final int SRC_PROPSVEC = 2;
    public static final int SRC_NAMES = 3;
    public static final int SRC_CASE = 4;
    public static final int SRC_BIDI = 5;
    public static final int SRC_CHAR_AND_PROPSVEC = 6;
    public static final int SRC_CASE_AND_NORM = 7;
    public static final int SRC_NFC = 8;
    public static final int SRC_NFKC = 9;
    public static final int SRC_NFKC_CF = 10;
    public static final int SRC_NFC_CANON_ITER = 11;
    public static final int SRC_COUNT = 12;
    static final int MY_MASK = 30;
    private static final int GC_CN_MASK;
    private static final int GC_CC_MASK;
    private static final int GC_CS_MASK;
    private static final int GC_ZS_MASK;
    private static final int GC_ZL_MASK;
    private static final int GC_ZP_MASK;
    private static final int GC_Z_MASK;
    BinaryProperty[] binProps;
    private static final int[] gcbToHst;
    IntProperty[] intProps;
    Trie2_16 m_additionalTrie_;
    int[] m_additionalVectors_;
    int m_additionalColumnsCount_;
    int m_maxBlockScriptValue_;
    int m_maxJTGValue_;
    public char[] m_scriptExtensions_;
    private static final String DATA_FILE_NAME_ = "data/icudt51b/uprops.icu";
    private static final int DATA_BUFFER_SIZE_ = 25000;
    private static final int LEAD_SURROGATE_SHIFT_ = 10;
    private static final int SURROGATE_OFFSET_ = -56613888;
    private static final int NUMERIC_TYPE_VALUE_SHIFT_ = 6;
    private static final int NTV_NONE_ = 0;
    private static final int NTV_DECIMAL_START_ = 1;
    private static final int NTV_DIGIT_START_ = 11;
    private static final int NTV_NUMERIC_START_ = 21;
    private static final int NTV_FRACTION_START_ = 176;
    private static final int NTV_LARGE_START_ = 480;
    private static final int NTV_BASE60_START_ = 768;
    private static final int NTV_RESERVED_START_ = 804;
    public static final int SCRIPT_X_MASK = 12583167;
    private static final int EAST_ASIAN_MASK_ = 917504;
    private static final int EAST_ASIAN_SHIFT_ = 17;
    private static final int BLOCK_MASK_ = 130816;
    private static final int BLOCK_SHIFT_ = 8;
    public static final int SCRIPT_MASK_ = 255;
    public static final int SCRIPT_X_WITH_COMMON = 4194304;
    public static final int SCRIPT_X_WITH_INHERITED = 8388608;
    public static final int SCRIPT_X_WITH_OTHER = 12582912;
    private static final int WHITE_SPACE_PROPERTY_ = 0;
    private static final int DASH_PROPERTY_ = 1;
    private static final int HYPHEN_PROPERTY_ = 2;
    private static final int QUOTATION_MARK_PROPERTY_ = 3;
    private static final int TERMINAL_PUNCTUATION_PROPERTY_ = 4;
    private static final int MATH_PROPERTY_ = 5;
    private static final int HEX_DIGIT_PROPERTY_ = 6;
    private static final int ASCII_HEX_DIGIT_PROPERTY_ = 7;
    private static final int ALPHABETIC_PROPERTY_ = 8;
    private static final int IDEOGRAPHIC_PROPERTY_ = 9;
    private static final int DIACRITIC_PROPERTY_ = 10;
    private static final int EXTENDER_PROPERTY_ = 11;
    private static final int NONCHARACTER_CODE_POINT_PROPERTY_ = 12;
    private static final int GRAPHEME_EXTEND_PROPERTY_ = 13;
    private static final int GRAPHEME_LINK_PROPERTY_ = 14;
    private static final int IDS_BINARY_OPERATOR_PROPERTY_ = 15;
    private static final int IDS_TRINARY_OPERATOR_PROPERTY_ = 16;
    private static final int RADICAL_PROPERTY_ = 17;
    private static final int UNIFIED_IDEOGRAPH_PROPERTY_ = 18;
    private static final int DEFAULT_IGNORABLE_CODE_POINT_PROPERTY_ = 19;
    private static final int DEPRECATED_PROPERTY_ = 20;
    private static final int LOGICAL_ORDER_EXCEPTION_PROPERTY_ = 21;
    private static final int XID_START_PROPERTY_ = 22;
    private static final int XID_CONTINUE_PROPERTY_ = 23;
    private static final int ID_START_PROPERTY_ = 24;
    private static final int ID_CONTINUE_PROPERTY_ = 25;
    private static final int GRAPHEME_BASE_PROPERTY_ = 26;
    private static final int S_TERM_PROPERTY_ = 27;
    private static final int VARIATION_SELECTOR_PROPERTY_ = 28;
    private static final int PATTERN_SYNTAX = 29;
    private static final int PATTERN_WHITE_SPACE = 30;
    private static final int LB_MASK = 66060288;
    private static final int LB_SHIFT = 20;
    private static final int SB_MASK = 1015808;
    private static final int SB_SHIFT = 15;
    private static final int WB_MASK = 31744;
    private static final int WB_SHIFT = 10;
    private static final int GCB_MASK = 992;
    private static final int GCB_SHIFT = 5;
    private static final int DECOMPOSITION_TYPE_MASK_ = 31;
    private static final int FIRST_NIBBLE_SHIFT_ = 4;
    private static final int LAST_NIBBLE_MASK_ = 15;
    private static final int AGE_SHIFT_ = 24;
    private static final byte[] DATA_FORMAT;
    private static final int TAB = 9;
    private static final int CR = 13;
    private static final int U_A = 65;
    private static final int U_F = 70;
    private static final int U_Z = 90;
    private static final int U_a = 97;
    private static final int U_f = 102;
    private static final int U_z = 122;
    private static final int DEL = 127;
    private static final int NL = 133;
    private static final int NBSP = 160;
    private static final int CGJ = 847;
    private static final int FIGURESP = 8199;
    private static final int HAIRSP = 8202;
    private static final int RLM = 8207;
    private static final int NNBSP = 8239;
    private static final int WJ = 8288;
    private static final int INHSWAP = 8298;
    private static final int NOMDIG = 8303;
    private static final int U_FW_A = 65313;
    private static final int U_FW_F = 65318;
    private static final int U_FW_Z = 65338;
    private static final int U_FW_a = 65345;
    private static final int U_FW_f = 65350;
    private static final int U_FW_z = 65370;
    private static final int ZWNBSP = 65279;
    
    public final int getProperty(final int ch) {
        return this.m_trie_.get(ch);
    }
    
    public int getAdditional(final int codepoint, final int column) {
        assert column >= 0;
        if (column >= this.m_additionalColumnsCount_) {
            return 0;
        }
        return this.m_additionalVectors_[this.m_additionalTrie_.get(codepoint) + column];
    }
    
    public VersionInfo getAge(final int codepoint) {
        final int version = this.getAdditional(codepoint, 0) >> 24;
        return VersionInfo.getInstance(version >> 4 & 0xF, version & 0xF, 0, 0);
    }
    
    private static final boolean isgraphPOSIX(final int c) {
        return (getMask(UCharacter.getType(c)) & (UCharacterProperty.GC_CC_MASK | UCharacterProperty.GC_CS_MASK | UCharacterProperty.GC_CN_MASK | UCharacterProperty.GC_Z_MASK)) == 0x0;
    }
    
    public boolean hasBinaryProperty(final int c, final int which) {
        return which >= 0 && 57 > which && this.binProps[which].contains(c);
    }
    
    public int getType(final int c) {
        return this.getProperty(c) & 0x1F;
    }
    
    public int getIntPropertyValue(final int c, final int which) {
        if (which < 4096) {
            if (0 <= which && which < 57) {
                return this.binProps[which].contains(c) ? 1 : 0;
            }
        }
        else {
            if (which < 4117) {
                return this.intProps[which - 4096].getValue(c);
            }
            if (which == 8192) {
                return getMask(this.getType(c));
            }
        }
        return 0;
    }
    
    public int getIntPropertyMaxValue(final int which) {
        if (which < 4096) {
            if (0 <= which && which < 57) {
                return 1;
            }
        }
        else if (which < 4117) {
            return this.intProps[which - 4096].getMaxValue(which);
        }
        return -1;
    }
    
    public final int getSource(final int which) {
        if (which < 0) {
            return 0;
        }
        if (which < 57) {
            return this.binProps[which].getSource();
        }
        if (which < 4096) {
            return 0;
        }
        if (which < 4117) {
            return this.intProps[which - 4096].getSource();
        }
        if (which < 16384) {
            switch (which) {
                case 8192:
                case 12288: {
                    return 1;
                }
                default: {
                    return 0;
                }
            }
        }
        else if (which < 16397) {
            switch (which) {
                case 16384: {
                    return 2;
                }
                case 16385: {
                    return 5;
                }
                case 16386:
                case 16388:
                case 16390:
                case 16391:
                case 16392:
                case 16393:
                case 16394:
                case 16396: {
                    return 4;
                }
                case 16387:
                case 16389:
                case 16395: {
                    return 3;
                }
                default: {
                    return 0;
                }
            }
        }
        else {
            switch (which) {
                case 28672: {
                    return 2;
                }
                default: {
                    return 0;
                }
            }
        }
    }
    
    public static int getRawSupplementary(final char lead, final char trail) {
        return (lead << 10) + trail - 56613888;
    }
    
    public int getMaxValues(final int column) {
        switch (column) {
            case 0: {
                return this.m_maxBlockScriptValue_;
            }
            case 2: {
                return this.m_maxJTGValue_;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static final int getMask(final int type) {
        return 1 << type;
    }
    
    public static int getEuropeanDigit(final int ch) {
        if ((ch > 122 && ch < 65313) || ch < 65 || (ch > 90 && ch < 97) || ch > 65370 || (ch > 65338 && ch < 65345)) {
            return -1;
        }
        if (ch <= 122) {
            return ch + 10 - ((ch <= 90) ? 65 : 97);
        }
        if (ch <= 65338) {
            return ch + 10 - 65313;
        }
        return ch + 10 - 65345;
    }
    
    public int digit(final int c) {
        final int value = getNumericTypeValue(this.getProperty(c)) - 1;
        if (value <= 9) {
            return value;
        }
        return -1;
    }
    
    public int getNumericValue(final int c) {
        final int ntv = getNumericTypeValue(this.getProperty(c));
        if (ntv == 0) {
            return getEuropeanDigit(c);
        }
        if (ntv < 11) {
            return ntv - 1;
        }
        if (ntv < 21) {
            return ntv - 11;
        }
        if (ntv < 176) {
            return ntv - 21;
        }
        if (ntv < 480) {
            return -2;
        }
        if (ntv < 768) {
            final int mant = (ntv >> 5) - 14;
            int exp = (ntv & 0x1F) + 2;
            if (exp < 9 || (exp == 9 && mant <= 2)) {
                int numValue = mant;
                do {
                    numValue *= 10;
                } while (--exp > 0);
                return numValue;
            }
            return -2;
        }
        else {
            if (ntv < 804) {
                int numValue2 = (ntv >> 2) - 191;
                final int exp = (ntv & 0x3) + 1;
                switch (exp) {
                    case 4: {
                        numValue2 *= 12960000;
                        break;
                    }
                    case 3: {
                        numValue2 *= 216000;
                        break;
                    }
                    case 2: {
                        numValue2 *= 3600;
                        break;
                    }
                    case 1: {
                        numValue2 *= 60;
                        break;
                    }
                }
                return numValue2;
            }
            return -2;
        }
    }
    
    public double getUnicodeNumericValue(final int c) {
        final int ntv = getNumericTypeValue(this.getProperty(c));
        if (ntv == 0) {
            return -1.23456789E8;
        }
        if (ntv < 11) {
            return ntv - 1;
        }
        if (ntv < 21) {
            return ntv - 11;
        }
        if (ntv < 176) {
            return ntv - 21;
        }
        if (ntv < 480) {
            final int numerator = (ntv >> 4) - 12;
            final int denominator = (ntv & 0xF) + 1;
            return numerator / (double)denominator;
        }
        if (ntv < 768) {
            final int mant = (ntv >> 5) - 14;
            int exp = (ntv & 0x1F) + 2;
            double numValue = mant;
            while (exp >= 4) {
                numValue *= 10000.0;
                exp -= 4;
            }
            switch (exp) {
                case 3: {
                    numValue *= 1000.0;
                    break;
                }
                case 2: {
                    numValue *= 100.0;
                    break;
                }
                case 1: {
                    numValue *= 10.0;
                    break;
                }
            }
            return numValue;
        }
        if (ntv < 804) {
            int numValue2 = (ntv >> 2) - 191;
            final int exp2 = (ntv & 0x3) + 1;
            switch (exp2) {
                case 4: {
                    numValue2 *= 12960000;
                    break;
                }
                case 3: {
                    numValue2 *= 216000;
                    break;
                }
                case 2: {
                    numValue2 *= 3600;
                    break;
                }
                case 1: {
                    numValue2 *= 60;
                    break;
                }
            }
            return numValue2;
        }
        return -1.23456789E8;
    }
    
    private static final int getNumericTypeValue(final int props) {
        return props >> 6;
    }
    
    private static final int ntvGetType(final int ntv) {
        return (ntv == 0) ? 0 : ((ntv < 11) ? 1 : ((ntv < 21) ? 2 : 3));
    }
    
    private UCharacterProperty() throws IOException {
        this.binProps = new BinaryProperty[] { new BinaryProperty(1, 256), new BinaryProperty(1, 128), new BinaryProperty(5) {
                @Override
                boolean contains(final int c) {
                    return UBiDiProps.INSTANCE.isBidiControl(c);
                }
            }, new BinaryProperty(5) {
                @Override
                boolean contains(final int c) {
                    return UBiDiProps.INSTANCE.isMirrored(c);
                }
            }, new BinaryProperty(1, 2), new BinaryProperty(1, 524288), new BinaryProperty(1, 1048576), new BinaryProperty(1, 1024), new BinaryProperty(1, 2048), new BinaryProperty(8) {
                @Override
                boolean contains(final int c) {
                    final Normalizer2Impl impl = Norm2AllModes.getNFCInstance().impl;
                    return impl.isCompNo(impl.getNorm16(c));
                }
            }, new BinaryProperty(1, 67108864), new BinaryProperty(1, 8192), new BinaryProperty(1, 16384), new BinaryProperty(1, 64), new BinaryProperty(1, 4), new BinaryProperty(1, 33554432), new BinaryProperty(1, 16777216), new BinaryProperty(1, 512), new BinaryProperty(1, 32768), new BinaryProperty(1, 65536), new BinaryProperty(5) {
                @Override
                boolean contains(final int c) {
                    return UBiDiProps.INSTANCE.isJoinControl(c);
                }
            }, new BinaryProperty(1, 2097152), new CaseBinaryProperty(22), new BinaryProperty(1, 32), new BinaryProperty(1, 4096), new BinaryProperty(1, 8), new BinaryProperty(1, 131072), new CaseBinaryProperty(27), new BinaryProperty(1, 16), new BinaryProperty(1, 262144), new CaseBinaryProperty(30), new BinaryProperty(1, 1), new BinaryProperty(1, 8388608), new BinaryProperty(1, 4194304), new CaseBinaryProperty(34), new BinaryProperty(1, 134217728), new BinaryProperty(1, 268435456), new NormInertBinaryProperty(8, 37), new NormInertBinaryProperty(9, 38), new NormInertBinaryProperty(8, 39), new NormInertBinaryProperty(9, 40), new BinaryProperty(11) {
                @Override
                boolean contains(final int c) {
                    return Norm2AllModes.getNFCInstance().impl.ensureCanonIterData().isCanonSegmentStarter(c);
                }
            }, new BinaryProperty(1, 536870912), new BinaryProperty(1, 1073741824), new BinaryProperty(6) {
                @Override
                boolean contains(final int c) {
                    return UCharacter.isUAlphabetic(c) || UCharacter.isDigit(c);
                }
            }, new BinaryProperty(1) {
                @Override
                boolean contains(final int c) {
                    if (c <= 159) {
                        return c == 9 || c == 32;
                    }
                    return UCharacter.getType(c) == 12;
                }
            }, new BinaryProperty(1) {
                @Override
                boolean contains(final int c) {
                    return isgraphPOSIX(c);
                }
            }, new BinaryProperty(1) {
                @Override
                boolean contains(final int c) {
                    return UCharacter.getType(c) == 12 || isgraphPOSIX(c);
                }
            }, new BinaryProperty(1) {
                @Override
                boolean contains(final int c) {
                    return (c <= 102 && c >= 65 && (c <= 70 || c >= 97)) || (c >= 65313 && c <= 65350 && (c <= 65318 || c >= 65345)) || UCharacter.getType(c) == 9;
                }
            }, new CaseBinaryProperty(49), new CaseBinaryProperty(50), new CaseBinaryProperty(51), new CaseBinaryProperty(52), new CaseBinaryProperty(53), new BinaryProperty(7) {
                @Override
                boolean contains(int c) {
                    final String nfd = Norm2AllModes.getNFCInstance().impl.getDecomposition(c);
                    if (nfd != null) {
                        c = nfd.codePointAt(0);
                        if (Character.charCount(c) != nfd.length()) {
                            c = -1;
                        }
                    }
                    else if (c < 0) {
                        return false;
                    }
                    if (c >= 0) {
                        final UCaseProps csp = UCaseProps.INSTANCE;
                        UCaseProps.dummyStringBuilder.setLength(0);
                        return csp.toFullFolding(c, UCaseProps.dummyStringBuilder, 0) >= 0;
                    }
                    final String folded = UCharacter.foldCase(nfd, true);
                    return !folded.equals(nfd);
                }
            }, new CaseBinaryProperty(55), new BinaryProperty(10) {
                @Override
                boolean contains(final int c) {
                    final Normalizer2Impl kcf = Norm2AllModes.getNFKC_CFInstance().impl;
                    final String src = UTF16.valueOf(c);
                    final StringBuilder dest = new StringBuilder();
                    final Normalizer2Impl.ReorderingBuffer buffer = new Normalizer2Impl.ReorderingBuffer(kcf, dest, 5);
                    kcf.compose(src, 0, src.length(), false, true, buffer);
                    return !Normalizer2Impl.UTF16Plus.equal(dest, src);
                }
            } };
        this.intProps = new IntProperty[] { new BiDiIntProperty() {
                @Override
                int getValue(final int c) {
                    return UBiDiProps.INSTANCE.getClass(c);
                }
            }, new IntProperty(0, 130816, 8), new CombiningClassIntProperty(8) {
                @Override
                int getValue(final int c) {
                    return Norm2AllModes.getNFCInstance().decomp.getCombiningClass(c);
                }
            }, new IntProperty(2, 31, 0), new IntProperty(0, 917504, 17), new IntProperty(1) {
                @Override
                int getValue(final int c) {
                    return UCharacterProperty.this.getType(c);
                }
                
                @Override
                int getMaxValue(final int which) {
                    return 29;
                }
            }, new BiDiIntProperty() {
                @Override
                int getValue(final int c) {
                    return UBiDiProps.INSTANCE.getJoiningGroup(c);
                }
            }, new BiDiIntProperty() {
                @Override
                int getValue(final int c) {
                    return UBiDiProps.INSTANCE.getJoiningType(c);
                }
            }, new IntProperty(2, 66060288, 20), new IntProperty(1) {
                @Override
                int getValue(final int c) {
                    return ntvGetType(getNumericTypeValue(UCharacterProperty.this.getProperty(c)));
                }
                
                @Override
                int getMaxValue(final int which) {
                    return 3;
                }
            }, new IntProperty(0, 255, 0) {
                @Override
                int getValue(final int c) {
                    return UScript.getScript(c);
                }
            }, new IntProperty(2) {
                @Override
                int getValue(final int c) {
                    final int gcb = (UCharacterProperty.this.getAdditional(c, 2) & 0x3E0) >>> 5;
                    if (gcb < UCharacterProperty.gcbToHst.length) {
                        return UCharacterProperty.gcbToHst[gcb];
                    }
                    return 0;
                }
                
                @Override
                int getMaxValue(final int which) {
                    return 5;
                }
            }, new NormQuickCheckIntProperty(8, 4108, 1), new NormQuickCheckIntProperty(9, 4109, 1), new NormQuickCheckIntProperty(8, 4110, 2), new NormQuickCheckIntProperty(9, 4111, 2), new CombiningClassIntProperty(8) {
                @Override
                int getValue(final int c) {
                    return Norm2AllModes.getNFCInstance().impl.getFCD16(c) >> 8;
                }
            }, new CombiningClassIntProperty(8) {
                @Override
                int getValue(final int c) {
                    return Norm2AllModes.getNFCInstance().impl.getFCD16(c) & 0xFF;
                }
            }, new IntProperty(2, 992, 5), new IntProperty(2, 1015808, 15), new IntProperty(2, 31744, 10) };
        if (this.binProps.length != 57) {
            throw new RuntimeException("binProps.length!=UProperty.BINARY_LIMIT");
        }
        if (this.intProps.length != 21) {
            throw new RuntimeException("intProps.length!=(UProperty.INT_LIMIT-UProperty.INT_START)");
        }
        final InputStream is = ICUData.getRequiredStream("data/icudt51b/uprops.icu");
        final BufferedInputStream bis = new BufferedInputStream(is, 25000);
        this.m_unicodeVersion_ = ICUBinary.readHeaderAndDataVersion(bis, UCharacterProperty.DATA_FORMAT, new IsAcceptable());
        final DataInputStream ds = new DataInputStream(bis);
        final int propertyOffset = ds.readInt();
        ds.readInt();
        ds.readInt();
        final int additionalOffset = ds.readInt();
        final int additionalVectorsOffset = ds.readInt();
        this.m_additionalColumnsCount_ = ds.readInt();
        final int scriptExtensionsOffset = ds.readInt();
        final int reservedOffset7 = ds.readInt();
        ds.readInt();
        ds.readInt();
        this.m_maxBlockScriptValue_ = ds.readInt();
        this.m_maxJTGValue_ = ds.readInt();
        ds.skipBytes(16);
        this.m_trie_ = Trie2_16.createFromSerialized(ds);
        int expectedTrieLength = (propertyOffset - 16) * 4;
        int trieLength = this.m_trie_.getSerializedLength();
        if (trieLength > expectedTrieLength) {
            throw new IOException("uprops.icu: not enough bytes for main trie");
        }
        ds.skipBytes(expectedTrieLength - trieLength);
        ds.skipBytes((additionalOffset - propertyOffset) * 4);
        if (this.m_additionalColumnsCount_ > 0) {
            this.m_additionalTrie_ = Trie2_16.createFromSerialized(ds);
            expectedTrieLength = (additionalVectorsOffset - additionalOffset) * 4;
            trieLength = this.m_additionalTrie_.getSerializedLength();
            if (trieLength > expectedTrieLength) {
                throw new IOException("uprops.icu: not enough bytes for additional-properties trie");
            }
            ds.skipBytes(expectedTrieLength - trieLength);
            final int size = scriptExtensionsOffset - additionalVectorsOffset;
            this.m_additionalVectors_ = new int[size];
            for (int i = 0; i < size; ++i) {
                this.m_additionalVectors_[i] = ds.readInt();
            }
        }
        final int numChars = (reservedOffset7 - scriptExtensionsOffset) * 2;
        if (numChars > 0) {
            this.m_scriptExtensions_ = new char[numChars];
            for (int i = 0; i < numChars; ++i) {
                this.m_scriptExtensions_[i] = ds.readChar();
            }
        }
        is.close();
    }
    
    public UnicodeSet addPropertyStarts(final UnicodeSet set) {
        final Iterator<Trie2.Range> trieIterator = this.m_trie_.iterator();
        Trie2.Range range;
        while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate) {
            set.add(range.startCodePoint);
        }
        set.add(9);
        set.add(10);
        set.add(14);
        set.add(28);
        set.add(32);
        set.add(133);
        set.add(134);
        set.add(127);
        set.add(8202);
        set.add(8208);
        set.add(8298);
        set.add(8304);
        set.add(65279);
        set.add(65280);
        set.add(160);
        set.add(161);
        set.add(8199);
        set.add(8200);
        set.add(8239);
        set.add(8240);
        set.add(12295);
        set.add(12296);
        set.add(19968);
        set.add(19969);
        set.add(20108);
        set.add(20109);
        set.add(19977);
        set.add(19978);
        set.add(22235);
        set.add(22236);
        set.add(20116);
        set.add(20117);
        set.add(20845);
        set.add(20846);
        set.add(19971);
        set.add(19972);
        set.add(20843);
        set.add(20844);
        set.add(20061);
        set.add(20062);
        set.add(97);
        set.add(123);
        set.add(65);
        set.add(91);
        set.add(65345);
        set.add(65371);
        set.add(65313);
        set.add(65339);
        set.add(103);
        set.add(71);
        set.add(65351);
        set.add(65319);
        set.add(8288);
        set.add(65520);
        set.add(65532);
        set.add(917504);
        set.add(921600);
        set.add(847);
        set.add(848);
        return set;
    }
    
    public void upropsvec_addPropertyStarts(final UnicodeSet set) {
        if (this.m_additionalColumnsCount_ > 0) {
            final Iterator<Trie2.Range> trieIterator = this.m_additionalTrie_.iterator();
            Trie2.Range range;
            while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate) {
                set.add(range.startCodePoint);
            }
        }
    }
    
    static {
        GC_CN_MASK = getMask(0);
        GC_CC_MASK = getMask(15);
        GC_CS_MASK = getMask(18);
        GC_ZS_MASK = getMask(12);
        GC_ZL_MASK = getMask(13);
        GC_ZP_MASK = getMask(14);
        GC_Z_MASK = (UCharacterProperty.GC_ZS_MASK | UCharacterProperty.GC_ZL_MASK | UCharacterProperty.GC_ZP_MASK);
        gcbToHst = new int[] { 0, 0, 0, 0, 1, 0, 4, 5, 3, 2 };
        DATA_FORMAT = new byte[] { 85, 80, 114, 111 };
        try {
            INSTANCE = new UCharacterProperty();
        }
        catch (IOException e) {
            throw new MissingResourceException(e.getMessage(), "", "");
        }
    }
    
    private class BinaryProperty
    {
        int column;
        int mask;
        
        BinaryProperty(final int column, final int mask) {
            this.column = column;
            this.mask = mask;
        }
        
        BinaryProperty(final int source) {
            this.column = source;
            this.mask = 0;
        }
        
        final int getSource() {
            return (this.mask == 0) ? this.column : 2;
        }
        
        boolean contains(final int c) {
            return (UCharacterProperty.this.getAdditional(c, this.column) & this.mask) != 0x0;
        }
    }
    
    private class CaseBinaryProperty extends BinaryProperty
    {
        int which;
        
        CaseBinaryProperty(final int which) {
            super(4);
            this.which = which;
        }
        
        @Override
        boolean contains(final int c) {
            return UCaseProps.INSTANCE.hasBinaryProperty(c, this.which);
        }
    }
    
    private class NormInertBinaryProperty extends BinaryProperty
    {
        int which;
        
        NormInertBinaryProperty(final int source, final int which) {
            super(source);
            this.which = which;
        }
        
        @Override
        boolean contains(final int c) {
            return Norm2AllModes.getN2WithImpl(this.which - 37).isInert(c);
        }
    }
    
    private class IntProperty
    {
        int column;
        int mask;
        int shift;
        
        IntProperty(final int column, final int mask, final int shift) {
            this.column = column;
            this.mask = mask;
            this.shift = shift;
        }
        
        IntProperty(final int source) {
            this.column = source;
            this.mask = 0;
        }
        
        final int getSource() {
            return (this.mask == 0) ? this.column : 2;
        }
        
        int getValue(final int c) {
            return (UCharacterProperty.this.getAdditional(c, this.column) & this.mask) >>> this.shift;
        }
        
        int getMaxValue(final int which) {
            return (UCharacterProperty.this.getMaxValues(this.column) & this.mask) >>> this.shift;
        }
    }
    
    private class BiDiIntProperty extends IntProperty
    {
        BiDiIntProperty() {
            super(5);
        }
        
        @Override
        int getMaxValue(final int which) {
            return UBiDiProps.INSTANCE.getMaxValue(which);
        }
    }
    
    private class CombiningClassIntProperty extends IntProperty
    {
        CombiningClassIntProperty(final int source) {
            super(source);
        }
        
        @Override
        int getMaxValue(final int which) {
            return 255;
        }
    }
    
    private class NormQuickCheckIntProperty extends IntProperty
    {
        int which;
        int max;
        
        NormQuickCheckIntProperty(final int source, final int which, final int max) {
            super(source);
            this.which = which;
            this.max = max;
        }
        
        @Override
        int getValue(final int c) {
            return Norm2AllModes.getN2WithImpl(this.which - 4108).getQuickCheck(c);
        }
        
        @Override
        int getMaxValue(final int which) {
            return this.max;
        }
    }
    
    private static final class IsAcceptable implements ICUBinary.Authenticate
    {
        public boolean isDataVersionAcceptable(final byte[] version) {
            return version[0] == 7;
        }
    }
}
