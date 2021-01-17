// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.util.Arrays;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.lang.reflect.Array;
import com.ibm.icu.impl.UBiDiProps;

public class Bidi
{
    public static final byte LEVEL_DEFAULT_LTR = 126;
    public static final byte LEVEL_DEFAULT_RTL = Byte.MAX_VALUE;
    public static final byte MAX_EXPLICIT_LEVEL = 61;
    public static final byte LEVEL_OVERRIDE = Byte.MIN_VALUE;
    public static final int MAP_NOWHERE = -1;
    public static final byte LTR = 0;
    public static final byte RTL = 1;
    public static final byte MIXED = 2;
    public static final byte NEUTRAL = 3;
    public static final short KEEP_BASE_COMBINING = 1;
    public static final short DO_MIRRORING = 2;
    public static final short INSERT_LRM_FOR_NUMERIC = 4;
    public static final short REMOVE_BIDI_CONTROLS = 8;
    public static final short OUTPUT_REVERSE = 16;
    public static final short REORDER_DEFAULT = 0;
    public static final short REORDER_NUMBERS_SPECIAL = 1;
    public static final short REORDER_GROUP_NUMBERS_WITH_R = 2;
    public static final short REORDER_RUNS_ONLY = 3;
    public static final short REORDER_INVERSE_NUMBERS_AS_L = 4;
    public static final short REORDER_INVERSE_LIKE_DIRECT = 5;
    public static final short REORDER_INVERSE_FOR_NUMBERS_SPECIAL = 6;
    static final short REORDER_COUNT = 7;
    static final short REORDER_LAST_LOGICAL_TO_VISUAL = 1;
    public static final int OPTION_DEFAULT = 0;
    public static final int OPTION_INSERT_MARKS = 1;
    public static final int OPTION_REMOVE_CONTROLS = 2;
    public static final int OPTION_STREAMING = 4;
    static final byte L = 0;
    static final byte R = 1;
    static final byte EN = 2;
    static final byte ES = 3;
    static final byte ET = 4;
    static final byte AN = 5;
    static final byte CS = 6;
    static final byte B = 7;
    static final byte S = 8;
    static final byte WS = 9;
    static final byte ON = 10;
    static final byte LRE = 11;
    static final byte LRO = 12;
    static final byte AL = 13;
    static final byte RLE = 14;
    static final byte RLO = 15;
    static final byte PDF = 16;
    static final byte NSM = 17;
    static final byte BN = 18;
    static final int MASK_R_AL = 8194;
    public static final int CLASS_DEFAULT = 19;
    private static final char CR = '\r';
    private static final char LF = '\n';
    static final int LRM_BEFORE = 1;
    static final int LRM_AFTER = 2;
    static final int RLM_BEFORE = 4;
    static final int RLM_AFTER = 8;
    Bidi paraBidi;
    final UBiDiProps bdp;
    char[] text;
    int originalLength;
    int length;
    int resultLength;
    boolean mayAllocateText;
    boolean mayAllocateRuns;
    byte[] dirPropsMemory;
    byte[] levelsMemory;
    byte[] dirProps;
    byte[] levels;
    boolean isInverse;
    int reorderingMode;
    int reorderingOptions;
    boolean orderParagraphsLTR;
    byte paraLevel;
    byte defaultParaLevel;
    String prologue;
    String epilogue;
    ImpTabPair impTabPair;
    byte direction;
    int flags;
    int lastArabicPos;
    int trailingWSStart;
    int paraCount;
    int[] parasMemory;
    int[] paras;
    int[] simpleParas;
    int runCount;
    BidiRun[] runsMemory;
    BidiRun[] runs;
    BidiRun[] simpleRuns;
    int[] logicalToVisualRunsMap;
    boolean isGoodLogicalToVisualRunsMap;
    BidiClassifier customClassifier;
    InsertPoints insertPoints;
    int controlCount;
    static final byte CONTEXT_RTL_SHIFT = 6;
    static final byte CONTEXT_RTL = 64;
    static final int DirPropFlagMultiRuns;
    static final int[] DirPropFlagLR;
    static final int[] DirPropFlagE;
    static final int[] DirPropFlagO;
    static final int MASK_LTR;
    static final int MASK_RTL;
    static final int MASK_LRX;
    static final int MASK_RLX;
    static final int MASK_OVERRIDE;
    static final int MASK_EXPLICIT;
    static final int MASK_BN_EXPLICIT;
    static final int MASK_B_S;
    static final int MASK_WS;
    static final int MASK_N;
    static final int MASK_ET_NSM_BN;
    static final int MASK_POSSIBLE_N;
    static final int MASK_EMBEDDING;
    private static final int IMPTABPROPS_COLUMNS = 14;
    private static final int IMPTABPROPS_RES = 13;
    private static final short[] groupProp;
    private static final short _L = 0;
    private static final short _R = 1;
    private static final short _EN = 2;
    private static final short _AN = 3;
    private static final short _ON = 4;
    private static final short _S = 5;
    private static final short _B = 6;
    private static final short[][] impTabProps;
    private static final int IMPTABLEVELS_COLUMNS = 8;
    private static final int IMPTABLEVELS_RES = 7;
    private static final byte[][] impTabL_DEFAULT;
    private static final byte[][] impTabR_DEFAULT;
    private static final short[] impAct0;
    private static final ImpTabPair impTab_DEFAULT;
    private static final byte[][] impTabL_NUMBERS_SPECIAL;
    private static final ImpTabPair impTab_NUMBERS_SPECIAL;
    private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R;
    private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R;
    private static final ImpTabPair impTab_GROUP_NUMBERS_WITH_R;
    private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L;
    private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L;
    private static final ImpTabPair impTab_INVERSE_NUMBERS_AS_L;
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT;
    private static final short[] impAct1;
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT;
    private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS;
    private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS;
    private static final short[] impAct2;
    private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS;
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL;
    private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
    private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
    static final int FIRSTALLOC = 10;
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
    public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;
    
    static int DirPropFlag(final byte dir) {
        return 1 << dir;
    }
    
    boolean testDirPropFlagAt(final int flag, final int index) {
        return (DirPropFlag((byte)(this.dirProps[index] & 0xFFFFFFBF)) & flag) != 0x0;
    }
    
    static byte NoContextRTL(final byte dir) {
        return (byte)(dir & 0xFFFFFFBF);
    }
    
    static int DirPropFlagNC(final byte dir) {
        return 1 << (dir & 0xFFFFFFBF);
    }
    
    static final int DirPropFlagLR(final byte level) {
        return Bidi.DirPropFlagLR[level & 0x1];
    }
    
    static final int DirPropFlagE(final byte level) {
        return Bidi.DirPropFlagE[level & 0x1];
    }
    
    static final int DirPropFlagO(final byte level) {
        return Bidi.DirPropFlagO[level & 0x1];
    }
    
    static byte GetLRFromLevel(final byte level) {
        return (byte)(level & 0x1);
    }
    
    static boolean IsDefaultLevel(final byte level) {
        return (level & 0x7E) == 0x7E;
    }
    
    byte GetParaLevelAt(final int index) {
        return (this.defaultParaLevel != 0) ? ((byte)(this.dirProps[index] >> 6)) : this.paraLevel;
    }
    
    static boolean IsBidiControlChar(final int c) {
        return (c & 0xFFFFFFFC) == 0x200C || (c >= 8234 && c <= 8238);
    }
    
    void verifyValidPara() {
        if (this != this.paraBidi) {
            throw new IllegalStateException();
        }
    }
    
    void verifyValidParaOrLine() {
        final Bidi para = this.paraBidi;
        if (this == para) {
            return;
        }
        if (para == null || para != para.paraBidi) {
            throw new IllegalStateException();
        }
    }
    
    void verifyRange(final int index, final int start, final int limit) {
        if (index < start || index >= limit) {
            throw new IllegalArgumentException("Value " + index + " is out of range " + start + " to " + limit);
        }
    }
    
    public Bidi() {
        this(0, 0);
    }
    
    public Bidi(final int maxLength, final int maxRunCount) {
        this.dirPropsMemory = new byte[1];
        this.levelsMemory = new byte[1];
        this.parasMemory = new int[1];
        this.simpleParas = new int[] { 0 };
        this.runsMemory = new BidiRun[0];
        this.simpleRuns = new BidiRun[] { new BidiRun() };
        this.customClassifier = null;
        this.insertPoints = new InsertPoints();
        if (maxLength < 0 || maxRunCount < 0) {
            throw new IllegalArgumentException();
        }
        this.bdp = UBiDiProps.INSTANCE;
        if (maxLength > 0) {
            this.getInitialDirPropsMemory(maxLength);
            this.getInitialLevelsMemory(maxLength);
        }
        else {
            this.mayAllocateText = true;
        }
        if (maxRunCount > 0) {
            if (maxRunCount > 1) {
                this.getInitialRunsMemory(maxRunCount);
            }
        }
        else {
            this.mayAllocateRuns = true;
        }
    }
    
    private Object getMemory(final String label, final Object array, final Class<?> arrayClass, final boolean mayAllocate, final int sizeNeeded) {
        final int len = Array.getLength(array);
        if (sizeNeeded == len) {
            return array;
        }
        if (!mayAllocate) {
            if (sizeNeeded <= len) {
                return array;
            }
            throw new OutOfMemoryError("Failed to allocate memory for " + label);
        }
        else {
            try {
                return Array.newInstance(arrayClass, sizeNeeded);
            }
            catch (Exception e) {
                throw new OutOfMemoryError("Failed to allocate memory for " + label);
            }
        }
    }
    
    private void getDirPropsMemory(final boolean mayAllocate, final int len) {
        final Object array = this.getMemory("DirProps", this.dirPropsMemory, Byte.TYPE, mayAllocate, len);
        this.dirPropsMemory = (byte[])array;
    }
    
    void getDirPropsMemory(final int len) {
        this.getDirPropsMemory(this.mayAllocateText, len);
    }
    
    private void getLevelsMemory(final boolean mayAllocate, final int len) {
        final Object array = this.getMemory("Levels", this.levelsMemory, Byte.TYPE, mayAllocate, len);
        this.levelsMemory = (byte[])array;
    }
    
    void getLevelsMemory(final int len) {
        this.getLevelsMemory(this.mayAllocateText, len);
    }
    
    private void getRunsMemory(final boolean mayAllocate, final int len) {
        final Object array = this.getMemory("Runs", this.runsMemory, BidiRun.class, mayAllocate, len);
        this.runsMemory = (BidiRun[])array;
    }
    
    void getRunsMemory(final int len) {
        this.getRunsMemory(this.mayAllocateRuns, len);
    }
    
    private void getInitialDirPropsMemory(final int len) {
        this.getDirPropsMemory(true, len);
    }
    
    private void getInitialLevelsMemory(final int len) {
        this.getLevelsMemory(true, len);
    }
    
    private void getInitialParasMemory(final int len) {
        final Object array = this.getMemory("Paras", this.parasMemory, Integer.TYPE, true, len);
        this.parasMemory = (int[])array;
    }
    
    private void getInitialRunsMemory(final int len) {
        this.getRunsMemory(true, len);
    }
    
    public void setInverse(final boolean isInverse) {
        this.isInverse = isInverse;
        this.reorderingMode = (isInverse ? 4 : 0);
    }
    
    public boolean isInverse() {
        return this.isInverse;
    }
    
    public void setReorderingMode(final int reorderingMode) {
        if (reorderingMode < 0 || reorderingMode >= 7) {
            return;
        }
        this.reorderingMode = reorderingMode;
        this.isInverse = (reorderingMode == 4);
    }
    
    public int getReorderingMode() {
        return this.reorderingMode;
    }
    
    public void setReorderingOptions(final int options) {
        if ((options & 0x2) != 0x0) {
            this.reorderingOptions = (options & 0xFFFFFFFE);
        }
        else {
            this.reorderingOptions = options;
        }
    }
    
    public int getReorderingOptions() {
        return this.reorderingOptions;
    }
    
    private byte firstL_R_AL() {
        byte result = 10;
        int i = 0;
        while (i < this.prologue.length()) {
            final int uchar = this.prologue.codePointAt(i);
            i += Character.charCount(uchar);
            final byte dirProp = (byte)this.getCustomizedClass(uchar);
            if (result == 10) {
                if (dirProp != 0 && dirProp != 1 && dirProp != 13) {
                    continue;
                }
                result = dirProp;
            }
            else {
                if (dirProp != 7) {
                    continue;
                }
                result = 10;
            }
        }
        return result;
    }
    
    private void getDirProps() {
        int i = 0;
        this.flags = 0;
        byte paraDirDefault = 0;
        final boolean isDefaultLevel = IsDefaultLevel(this.paraLevel);
        final boolean isDefaultLevelInverse = isDefaultLevel && (this.reorderingMode == 5 || this.reorderingMode == 6);
        this.lastArabicPos = -1;
        this.controlCount = 0;
        final boolean removeBidiControls = (this.reorderingOptions & 0x2) != 0x0;
        final int NOT_CONTEXTUAL = 0;
        final int LOOKING_FOR_STRONG = 1;
        final int FOUND_STRONG_CHAR = 2;
        int paraStart = 0;
        byte lastStrongDir = 0;
        int lastStrongLTR = 0;
        if ((this.reorderingOptions & 0x4) > 0) {
            this.length = 0;
            lastStrongLTR = 0;
        }
        byte paraDir;
        int state;
        if (isDefaultLevel) {
            paraDirDefault = (byte)(((this.paraLevel & 0x1) != 0x0) ? 64 : 0);
            final byte lastStrong;
            if (this.prologue != null && (lastStrong = this.firstL_R_AL()) != 10) {
                paraDir = (byte)((lastStrong == 0) ? 0 : 64);
                state = 2;
            }
            else {
                paraDir = paraDirDefault;
                state = 1;
            }
            state = 1;
        }
        else {
            state = 0;
            paraDir = 0;
        }
        i = 0;
        while (i < this.originalLength) {
            final int i2 = i;
            final int uchar = UTF16.charAt(this.text, 0, this.originalLength, i);
            i += UTF16.getCharCount(uchar);
            int i3 = i - 1;
            final byte dirProp = (byte)this.getCustomizedClass(uchar);
            this.flags |= DirPropFlag(dirProp);
            this.dirProps[i3] = (byte)(dirProp | paraDir);
            if (i3 > i2) {
                this.flags |= DirPropFlag((byte)18);
                do {
                    this.dirProps[--i3] = (byte)(0x12 | paraDir);
                } while (i3 > i2);
            }
            if (state == 1) {
                if (dirProp == 0) {
                    state = 2;
                    if (paraDir != 0) {
                        paraDir = 0;
                        for (i3 = paraStart; i3 < i; ++i3) {
                            final byte[] dirProps = this.dirProps;
                            final int n = i3;
                            dirProps[n] &= 0xFFFFFFBF;
                        }
                        continue;
                    }
                    continue;
                }
                else if (dirProp == 1 || dirProp == 13) {
                    state = 2;
                    if (paraDir == 0) {
                        paraDir = 64;
                        for (i3 = paraStart; i3 < i; ++i3) {
                            final byte[] dirProps2 = this.dirProps;
                            final int n2 = i3;
                            dirProps2[n2] |= 0x40;
                        }
                        continue;
                    }
                    continue;
                }
            }
            if (dirProp == 0) {
                lastStrongDir = 0;
                lastStrongLTR = i;
            }
            else if (dirProp == 1) {
                lastStrongDir = 64;
            }
            else if (dirProp == 13) {
                lastStrongDir = 64;
                this.lastArabicPos = i - 1;
            }
            else if (dirProp == 7) {
                if ((this.reorderingOptions & 0x4) != 0x0) {
                    this.length = i;
                }
                if (isDefaultLevelInverse && lastStrongDir == 64 && paraDir != lastStrongDir) {
                    while (paraStart < i) {
                        final byte[] dirProps3 = this.dirProps;
                        final int n3 = paraStart;
                        dirProps3[n3] |= 0x40;
                        ++paraStart;
                    }
                }
                if (i < this.originalLength) {
                    if (uchar != 13 || this.text[i] != '\n') {
                        ++this.paraCount;
                    }
                    if (isDefaultLevel) {
                        state = 1;
                        paraStart = i;
                        paraDir = paraDirDefault;
                        lastStrongDir = paraDirDefault;
                    }
                }
            }
            if (removeBidiControls && IsBidiControlChar(uchar)) {
                ++this.controlCount;
            }
        }
        if (isDefaultLevelInverse && lastStrongDir == 64 && paraDir != lastStrongDir) {
            for (int i3 = paraStart; i3 < this.originalLength; ++i3) {
                final byte[] dirProps4 = this.dirProps;
                final int n4 = i3;
                dirProps4[n4] |= 0x40;
            }
        }
        if (isDefaultLevel) {
            this.paraLevel = this.GetParaLevelAt(0);
        }
        if ((this.reorderingOptions & 0x4) > 0) {
            if (lastStrongLTR > this.length && this.GetParaLevelAt(lastStrongLTR) == 0) {
                this.length = lastStrongLTR;
            }
            if (this.length < this.originalLength) {
                --this.paraCount;
            }
        }
        this.flags |= DirPropFlagLR(this.paraLevel);
        if (this.orderParagraphsLTR && (this.flags & DirPropFlag((byte)7)) != 0x0) {
            this.flags |= DirPropFlag((byte)0);
        }
    }
    
    private byte directionFromFlags() {
        if ((this.flags & Bidi.MASK_RTL) == 0x0 && ((this.flags & DirPropFlag((byte)5)) == 0x0 || (this.flags & Bidi.MASK_POSSIBLE_N) == 0x0)) {
            return 0;
        }
        if ((this.flags & Bidi.MASK_LTR) == 0x0) {
            return 1;
        }
        return 2;
    }
    
    private byte resolveExplicitLevels() {
        int i = 0;
        byte level = this.GetParaLevelAt(0);
        int paraIndex = 0;
        byte dirct = this.directionFromFlags();
        if (dirct == 2 || this.paraCount != 1) {
            if (this.paraCount == 1 && ((this.flags & Bidi.MASK_EXPLICIT) == 0x0 || this.reorderingMode > 1)) {
                for (i = 0; i < this.length; ++i) {
                    this.levels[i] = level;
                }
            }
            else {
                byte embeddingLevel = level;
                byte stackTop = 0;
                final byte[] stack = new byte[61];
                int countOver60 = 0;
                int countOver61 = 0;
                this.flags = 0;
                for (i = 0; i < this.length; ++i) {
                    final byte dirProp = NoContextRTL(this.dirProps[i]);
                    switch (dirProp) {
                        case 11:
                        case 12: {
                            final byte newLevel = (byte)(embeddingLevel + 2 & 0x7E);
                            if (newLevel <= 61) {
                                stack[stackTop] = embeddingLevel;
                                ++stackTop;
                                embeddingLevel = newLevel;
                                if (dirProp == 12) {
                                    embeddingLevel |= 0xFFFFFF80;
                                }
                            }
                            else if ((embeddingLevel & 0x7F) == 0x3D) {
                                ++countOver61;
                            }
                            else {
                                ++countOver60;
                            }
                            this.flags |= DirPropFlag((byte)18);
                            break;
                        }
                        case 14:
                        case 15: {
                            final byte newLevel = (byte)((embeddingLevel & 0x7F) + 1 | 0x1);
                            if (newLevel <= 61) {
                                stack[stackTop] = embeddingLevel;
                                ++stackTop;
                                embeddingLevel = newLevel;
                                if (dirProp == 15) {
                                    embeddingLevel |= 0xFFFFFF80;
                                }
                            }
                            else {
                                ++countOver61;
                            }
                            this.flags |= DirPropFlag((byte)18);
                            break;
                        }
                        case 16: {
                            if (countOver61 > 0) {
                                --countOver61;
                            }
                            else if (countOver60 > 0 && (embeddingLevel & 0x7F) != 0x3D) {
                                --countOver60;
                            }
                            else if (stackTop > 0) {
                                --stackTop;
                                embeddingLevel = stack[stackTop];
                            }
                            this.flags |= DirPropFlag((byte)18);
                            break;
                        }
                        case 7: {
                            stackTop = 0;
                            countOver60 = 0;
                            countOver61 = 0;
                            level = this.GetParaLevelAt(i);
                            if (i + 1 < this.length) {
                                embeddingLevel = this.GetParaLevelAt(i + 1);
                                if (this.text[i] != '\r' || this.text[i + 1] != '\n') {
                                    this.paras[paraIndex++] = i + 1;
                                }
                            }
                            this.flags |= DirPropFlag((byte)7);
                            break;
                        }
                        case 18: {
                            this.flags |= DirPropFlag((byte)18);
                            break;
                        }
                        default: {
                            if (level != embeddingLevel) {
                                level = embeddingLevel;
                                if ((level & 0xFFFFFF80) != 0x0) {
                                    this.flags |= (DirPropFlagO(level) | Bidi.DirPropFlagMultiRuns);
                                }
                                else {
                                    this.flags |= (DirPropFlagE(level) | Bidi.DirPropFlagMultiRuns);
                                }
                            }
                            if ((level & 0xFFFFFF80) == 0x0) {
                                this.flags |= DirPropFlag(dirProp);
                                break;
                            }
                            break;
                        }
                    }
                    this.levels[i] = level;
                }
                if ((this.flags & Bidi.MASK_EMBEDDING) != 0x0) {
                    this.flags |= DirPropFlagLR(this.paraLevel);
                }
                if (this.orderParagraphsLTR && (this.flags & DirPropFlag((byte)7)) != 0x0) {
                    this.flags |= DirPropFlag((byte)0);
                }
                dirct = this.directionFromFlags();
            }
        }
        return dirct;
    }
    
    private byte checkExplicitLevels() {
        this.flags = 0;
        int paraIndex = 0;
        for (int i = 0; i < this.length; ++i) {
            byte level = this.levels[i];
            final byte dirProp = NoContextRTL(this.dirProps[i]);
            if ((level & 0xFFFFFF80) != 0x0) {
                level &= 0x7F;
                this.flags |= DirPropFlagO(level);
            }
            else {
                this.flags |= (DirPropFlagE(level) | DirPropFlag(dirProp));
            }
            if ((level < this.GetParaLevelAt(i) && (0 != level || dirProp != 7)) || 61 < level) {
                throw new IllegalArgumentException("level " + level + " out of bounds at " + i);
            }
            if (dirProp == 7 && i + 1 < this.length && (this.text[i] != '\r' || this.text[i + 1] != '\n')) {
                this.paras[paraIndex++] = i + 1;
            }
        }
        if ((this.flags & Bidi.MASK_EMBEDDING) != 0x0) {
            this.flags |= DirPropFlagLR(this.paraLevel);
        }
        return this.directionFromFlags();
    }
    
    private static short GetStateProps(final short cell) {
        return (short)(cell & 0x1F);
    }
    
    private static short GetActionProps(final short cell) {
        return (short)(cell >> 5);
    }
    
    private static short GetState(final byte cell) {
        return (short)(cell & 0xF);
    }
    
    private static short GetAction(final byte cell) {
        return (short)(cell >> 4);
    }
    
    private void addPoint(final int pos, final int flag) {
        final Point point = new Point();
        int len = this.insertPoints.points.length;
        if (len == 0) {
            this.insertPoints.points = new Point[10];
            len = 10;
        }
        if (this.insertPoints.size >= len) {
            final Point[] savePoints = this.insertPoints.points;
            System.arraycopy(savePoints, 0, this.insertPoints.points = new Point[len * 2], 0, len);
        }
        point.pos = pos;
        point.flag = flag;
        this.insertPoints.points[this.insertPoints.size] = point;
        final InsertPoints insertPoints = this.insertPoints;
        ++insertPoints.size;
    }
    
    private void processPropertySeq(final LevState levState, final short _prop, int start, final int limit) {
        final byte[][] impTab = levState.impTab;
        final short[] impAct = levState.impAct;
        final int start2 = start;
        final short oldStateSeq = levState.state;
        final byte cell = impTab[oldStateSeq][_prop];
        levState.state = GetState(cell);
        final short actionSeq = impAct[GetAction(cell)];
        final byte addLevel = impTab[levState.state][7];
        if (actionSeq != 0) {
            switch (actionSeq) {
                case 1: {
                    levState.startON = start2;
                    break;
                }
                case 2: {
                    start = levState.startON;
                    break;
                }
                case 3: {
                    if (levState.startL2EN >= 0) {
                        this.addPoint(levState.startL2EN, 1);
                    }
                    levState.startL2EN = -1;
                    if (this.insertPoints.points.length == 0 || this.insertPoints.size <= this.insertPoints.confirmed) {
                        levState.lastStrongRTL = -1;
                        final byte level = impTab[oldStateSeq][7];
                        if ((level & 0x1) != 0x0 && levState.startON > 0) {
                            start = levState.startON;
                        }
                        if (_prop == 5) {
                            this.addPoint(start2, 1);
                            this.insertPoints.confirmed = this.insertPoints.size;
                            break;
                        }
                        break;
                    }
                    else {
                        for (int k = levState.lastStrongRTL + 1; k < start2; ++k) {
                            this.levels[k] = (byte)(this.levels[k] - 2 & 0xFFFFFFFE);
                        }
                        this.insertPoints.confirmed = this.insertPoints.size;
                        levState.lastStrongRTL = -1;
                        if (_prop == 5) {
                            this.addPoint(start2, 1);
                            this.insertPoints.confirmed = this.insertPoints.size;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 4: {
                    if (this.insertPoints.points.length > 0) {
                        this.insertPoints.size = this.insertPoints.confirmed;
                    }
                    levState.startON = -1;
                    levState.startL2EN = -1;
                    levState.lastStrongRTL = limit - 1;
                    break;
                }
                case 5: {
                    if (_prop == 3 && NoContextRTL(this.dirProps[start2]) == 5 && this.reorderingMode != 6) {
                        if (levState.startL2EN == -1) {
                            levState.lastStrongRTL = limit - 1;
                            break;
                        }
                        if (levState.startL2EN >= 0) {
                            this.addPoint(levState.startL2EN, 1);
                            levState.startL2EN = -2;
                        }
                        this.addPoint(start2, 1);
                        break;
                    }
                    else {
                        if (levState.startL2EN == -1) {
                            levState.startL2EN = start2;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 6: {
                    levState.lastStrongRTL = limit - 1;
                    levState.startON = -1;
                    break;
                }
                case 7: {
                    int k;
                    for (k = start2 - 1; k >= 0 && (this.levels[k] & 0x1) == 0x0; --k) {}
                    if (k >= 0) {
                        this.addPoint(k, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                    }
                    levState.startON = start2;
                    break;
                }
                case 8: {
                    this.addPoint(start2, 1);
                    this.addPoint(start2, 2);
                    break;
                }
                case 9: {
                    this.insertPoints.size = this.insertPoints.confirmed;
                    if (_prop == 5) {
                        this.addPoint(start2, 4);
                        this.insertPoints.confirmed = this.insertPoints.size;
                        break;
                    }
                    break;
                }
                case 10: {
                    final byte level = (byte)(levState.runLevel + addLevel);
                    for (int k = levState.startON; k < start2; ++k) {
                        if (this.levels[k] < level) {
                            this.levels[k] = level;
                        }
                    }
                    this.insertPoints.confirmed = this.insertPoints.size;
                    levState.startON = start2;
                    break;
                }
                case 11: {
                    final byte level = levState.runLevel;
                    for (int k = start2 - 1; k >= levState.startON; --k) {
                        if (this.levels[k] == level + 3) {
                            while (this.levels[k] == level + 3) {
                                final byte[] levels = this.levels;
                                final int n = k--;
                                levels[n] -= 2;
                            }
                            while (this.levels[k] == level) {
                                --k;
                            }
                        }
                        if (this.levels[k] == level + 2) {
                            this.levels[k] = level;
                        }
                        else {
                            this.levels[k] = (byte)(level + 1);
                        }
                    }
                    break;
                }
                case 12: {
                    final byte level = (byte)(levState.runLevel + 1);
                    for (int k = start2 - 1; k >= levState.startON; --k) {
                        if (this.levels[k] > level) {
                            final byte[] levels2 = this.levels;
                            final int n2 = k;
                            levels2[n2] -= 2;
                        }
                    }
                    break;
                }
                default: {
                    throw new IllegalStateException("Internal ICU error in processPropertySeq");
                }
            }
        }
        if (addLevel != 0 || start < start2) {
            final byte level = (byte)(levState.runLevel + addLevel);
            for (int k = start; k < limit; ++k) {
                this.levels[k] = level;
            }
        }
    }
    
    private byte lastL_R_AL() {
        int i = this.prologue.length();
        while (i > 0) {
            final int uchar = this.prologue.codePointBefore(i);
            i -= Character.charCount(uchar);
            final byte dirProp = (byte)this.getCustomizedClass(uchar);
            if (dirProp == 0) {
                return 0;
            }
            if (dirProp == 1 || dirProp == 13) {
                return 1;
            }
            if (dirProp == 7) {
                return 4;
            }
        }
        return 4;
    }
    
    private byte firstL_R_AL_EN_AN() {
        int i = 0;
        while (i < this.epilogue.length()) {
            final int uchar = this.epilogue.codePointAt(i);
            i += Character.charCount(uchar);
            final byte dirProp = (byte)this.getCustomizedClass(uchar);
            if (dirProp == 0) {
                return 0;
            }
            if (dirProp == 1 || dirProp == 13) {
                return 1;
            }
            if (dirProp == 2) {
                return 2;
            }
            if (dirProp == 5) {
                return 3;
            }
        }
        return 4;
    }
    
    private void resolveImplicitLevels(final int start, final int limit, short sor, short eor) {
        final LevState levState = new LevState();
        short nextStrongProp = 1;
        int nextStrongPos = -1;
        final boolean inverseRTL = start < this.lastArabicPos && (this.GetParaLevelAt(start) & 0x1) > 0 && (this.reorderingMode == 5 || this.reorderingMode == 6);
        levState.startL2EN = -1;
        levState.lastStrongRTL = -1;
        levState.state = 0;
        levState.runLevel = this.levels[start];
        levState.impTab = this.impTabPair.imptab[levState.runLevel & 0x1];
        levState.impAct = this.impTabPair.impact[levState.runLevel & 0x1];
        if (start == 0 && this.prologue != null) {
            final byte lastStrong = this.lastL_R_AL();
            if (lastStrong != 4) {
                sor = lastStrong;
            }
        }
        this.processPropertySeq(levState, sor, start, start);
        short stateImp;
        if (NoContextRTL(this.dirProps[start]) == 17) {
            stateImp = (short)(1 + sor);
        }
        else {
            stateImp = 0;
        }
        int start2 = start;
        int start3 = 0;
        for (int i = start; i <= limit; ++i) {
            short gprop;
            if (i >= limit) {
                gprop = eor;
            }
            else {
                short prop = NoContextRTL(this.dirProps[i]);
                if (inverseRTL) {
                    if (prop == 13) {
                        prop = 1;
                    }
                    else if (prop == 2) {
                        if (nextStrongPos <= i) {
                            nextStrongProp = 1;
                            nextStrongPos = limit;
                            for (int j = i + 1; j < limit; ++j) {
                                final short prop2 = NoContextRTL(this.dirProps[j]);
                                if (prop2 == 0 || prop2 == 1 || prop2 == 13) {
                                    nextStrongProp = prop2;
                                    nextStrongPos = j;
                                    break;
                                }
                            }
                        }
                        if (nextStrongProp == 13) {
                            prop = 5;
                        }
                    }
                }
                gprop = Bidi.groupProp[prop];
            }
            final short oldStateImp = stateImp;
            final short cell = Bidi.impTabProps[oldStateImp][gprop];
            stateImp = GetStateProps(cell);
            short actionImp = GetActionProps(cell);
            if (i == limit && actionImp == 0) {
                actionImp = 1;
            }
            if (actionImp != 0) {
                final short resProp = Bidi.impTabProps[oldStateImp][13];
                switch (actionImp) {
                    case 1: {
                        this.processPropertySeq(levState, resProp, start2, i);
                        start2 = i;
                        break;
                    }
                    case 2: {
                        start3 = i;
                        break;
                    }
                    case 3: {
                        this.processPropertySeq(levState, resProp, start2, start3);
                        this.processPropertySeq(levState, (short)4, start3, i);
                        start2 = i;
                        break;
                    }
                    case 4: {
                        this.processPropertySeq(levState, resProp, start2, start3);
                        start2 = start3;
                        start3 = i;
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Internal ICU error in resolveImplicitLevels");
                    }
                }
            }
        }
        if (limit == this.length && this.epilogue != null) {
            final byte firstStrong = this.firstL_R_AL_EN_AN();
            if (firstStrong != 4) {
                eor = firstStrong;
            }
        }
        this.processPropertySeq(levState, eor, limit, limit);
    }
    
    private void adjustWSLevels() {
        if ((this.flags & Bidi.MASK_WS) != 0x0) {
            int i = this.trailingWSStart;
            while (i > 0) {
                int flag;
                while (i > 0 && ((flag = DirPropFlagNC(this.dirProps[--i])) & Bidi.MASK_WS) != 0x0) {
                    if (this.orderParagraphsLTR && (flag & DirPropFlag((byte)7)) != 0x0) {
                        this.levels[i] = 0;
                    }
                    else {
                        this.levels[i] = this.GetParaLevelAt(i);
                    }
                }
                while (i > 0) {
                    flag = DirPropFlagNC(this.dirProps[--i]);
                    if ((flag & Bidi.MASK_BN_EXPLICIT) != 0x0) {
                        this.levels[i] = this.levels[i + 1];
                    }
                    else {
                        if (this.orderParagraphsLTR && (flag & DirPropFlag((byte)7)) != 0x0) {
                            this.levels[i] = 0;
                            break;
                        }
                        if ((flag & Bidi.MASK_B_S) != 0x0) {
                            this.levels[i] = this.GetParaLevelAt(i);
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    int Bidi_Min(final int x, final int y) {
        return (x < y) ? x : y;
    }
    
    int Bidi_Abs(final int x) {
        return (x >= 0) ? x : (-x);
    }
    
    void setParaRunsOnly(final char[] parmText, byte parmParaLevel) {
        this.reorderingMode = 0;
        final int parmLength = parmText.length;
        if (parmLength == 0) {
            this.setPara(parmText, parmParaLevel, null);
            this.reorderingMode = 3;
            return;
        }
        final int saveOptions = this.reorderingOptions;
        if ((saveOptions & 0x1) > 0) {
            this.reorderingOptions &= 0xFFFFFFFE;
            this.reorderingOptions |= 0x2;
        }
        parmParaLevel &= 0x1;
        this.setPara(parmText, parmParaLevel, null);
        final byte[] saveLevels = new byte[this.length];
        System.arraycopy(this.getLevels(), 0, saveLevels, 0, this.length);
        final int saveTrailingWSStart = this.trailingWSStart;
        final String visualText = this.writeReordered(2);
        final int[] visualMap = this.getVisualMap();
        this.reorderingOptions = saveOptions;
        final int saveLength = this.length;
        final byte saveDirection = this.direction;
        this.reorderingMode = 5;
        parmParaLevel ^= 0x1;
        this.setPara(visualText, parmParaLevel, null);
        BidiLine.getRuns(this);
        int addedRuns = 0;
        final int oldRunCount = this.runCount;
        int runLength;
        for (int visualStart = 0, i = 0; i < oldRunCount; ++i, visualStart += runLength) {
            runLength = this.runs[i].limit - visualStart;
            if (runLength >= 2) {
                for (int logicalStart = this.runs[i].start, j = logicalStart + 1; j < logicalStart + runLength; ++j) {
                    final int index = visualMap[j];
                    final int index2 = visualMap[j - 1];
                    if (this.Bidi_Abs(index - index2) != 1 || saveLevels[index] != saveLevels[index2]) {
                        ++addedRuns;
                    }
                }
            }
        }
        if (addedRuns > 0) {
            this.getRunsMemory(oldRunCount + addedRuns);
            if (this.runCount == 1) {
                this.runsMemory[0] = this.runs[0];
            }
            else {
                System.arraycopy(this.runs, 0, this.runsMemory, 0, this.runCount);
            }
            this.runs = this.runsMemory;
            this.runCount += addedRuns;
            for (int i = oldRunCount; i < this.runCount; ++i) {
                if (this.runs[i] == null) {
                    this.runs[i] = new BidiRun(0, 0, (byte)0);
                }
            }
        }
        for (int i = oldRunCount - 1; i >= 0; --i) {
            int newI = i + addedRuns;
            runLength = ((i == 0) ? this.runs[0].limit : (this.runs[i].limit - this.runs[i - 1].limit));
            final int logicalStart = this.runs[i].start;
            final int indexOddBit = this.runs[i].level & 0x1;
            if (runLength < 2) {
                if (addedRuns > 0) {
                    this.runs[newI].copyFrom(this.runs[i]);
                }
                final int logicalPos = visualMap[logicalStart];
                this.runs[newI].start = logicalPos;
                this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
            }
            else {
                int start;
                int limit;
                int step;
                if (indexOddBit > 0) {
                    start = logicalStart;
                    limit = logicalStart + runLength - 1;
                    step = 1;
                }
                else {
                    start = logicalStart + runLength - 1;
                    limit = logicalStart;
                    step = -1;
                }
                for (int j = start; j != limit; j += step) {
                    final int index = visualMap[j];
                    final int index2 = visualMap[j + step];
                    if (this.Bidi_Abs(index - index2) != 1 || saveLevels[index] != saveLevels[index2]) {
                        final int logicalPos = this.Bidi_Min(visualMap[start], index);
                        this.runs[newI].start = logicalPos;
                        this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
                        this.runs[newI].limit = this.runs[i].limit;
                        final BidiRun bidiRun = this.runs[i];
                        bidiRun.limit -= this.Bidi_Abs(j - start) + 1;
                        final int insertRemove = this.runs[i].insertRemove & 0xA;
                        this.runs[newI].insertRemove = insertRemove;
                        final BidiRun bidiRun2 = this.runs[i];
                        bidiRun2.insertRemove &= ~insertRemove;
                        start = j + step;
                        --addedRuns;
                        --newI;
                    }
                }
                if (addedRuns > 0) {
                    this.runs[newI].copyFrom(this.runs[i]);
                }
                final int logicalPos = this.Bidi_Min(visualMap[start], visualMap[limit]);
                this.runs[newI].start = logicalPos;
                this.runs[newI].level = (byte)(saveLevels[logicalPos] ^ indexOddBit);
            }
        }
        this.paraLevel ^= 0x1;
        this.text = parmText;
        this.length = saveLength;
        this.originalLength = parmLength;
        this.direction = saveDirection;
        this.levels = saveLevels;
        this.trailingWSStart = saveTrailingWSStart;
        if (this.runCount > 1) {
            this.direction = 2;
        }
        this.reorderingMode = 3;
    }
    
    private void setParaSuccess() {
        this.prologue = null;
        this.epilogue = null;
        this.paraBidi = this;
    }
    
    public void setContext(final String prologue, final String epilogue) {
        this.prologue = ((prologue != null && prologue.length() > 0) ? prologue : null);
        this.epilogue = ((epilogue != null && epilogue.length() > 0) ? epilogue : null);
    }
    
    public void setPara(final String text, final byte paraLevel, final byte[] embeddingLevels) {
        if (text == null) {
            this.setPara(new char[0], paraLevel, embeddingLevels);
        }
        else {
            this.setPara(text.toCharArray(), paraLevel, embeddingLevels);
        }
    }
    
    public void setPara(char[] chars, byte paraLevel, final byte[] embeddingLevels) {
        if (paraLevel < 126) {
            this.verifyRange(paraLevel, 0, 62);
        }
        if (chars == null) {
            chars = new char[0];
        }
        if (this.reorderingMode == 3) {
            this.setParaRunsOnly(chars, paraLevel);
            return;
        }
        this.paraBidi = null;
        this.text = chars;
        final int length = this.text.length;
        this.resultLength = length;
        this.originalLength = length;
        this.length = length;
        this.paraLevel = paraLevel;
        this.direction = 0;
        this.paraCount = 1;
        this.dirProps = new byte[0];
        this.levels = new byte[0];
        this.runs = new BidiRun[0];
        this.isGoodLogicalToVisualRunsMap = false;
        this.insertPoints.size = 0;
        this.insertPoints.confirmed = 0;
        if (IsDefaultLevel(paraLevel)) {
            this.defaultParaLevel = paraLevel;
        }
        else {
            this.defaultParaLevel = 0;
        }
        if (this.length == 0) {
            if (IsDefaultLevel(paraLevel)) {
                this.paraLevel &= 0x1;
                this.defaultParaLevel = 0;
            }
            if ((this.paraLevel & 0x1) != 0x0) {
                this.flags = DirPropFlag((byte)1);
                this.direction = 1;
            }
            else {
                this.flags = DirPropFlag((byte)0);
                this.direction = 0;
            }
            this.runCount = 0;
            this.paraCount = 0;
            this.setParaSuccess();
            return;
        }
        this.runCount = -1;
        this.getDirPropsMemory(this.length);
        this.dirProps = this.dirPropsMemory;
        this.getDirProps();
        this.trailingWSStart = this.length;
        if (this.paraCount > 1) {
            this.getInitialParasMemory(this.paraCount);
            (this.paras = this.parasMemory)[this.paraCount - 1] = this.length;
        }
        else {
            this.paras = this.simpleParas;
            this.simpleParas[0] = this.length;
        }
        if (embeddingLevels == null) {
            this.getLevelsMemory(this.length);
            this.levels = this.levelsMemory;
            this.direction = this.resolveExplicitLevels();
        }
        else {
            this.levels = embeddingLevels;
            this.direction = this.checkExplicitLevels();
        }
        switch (this.direction) {
            case 0: {
                paraLevel = (byte)(paraLevel + 1 & 0xFFFFFFFE);
                this.trailingWSStart = 0;
                break;
            }
            case 1: {
                paraLevel |= 0x1;
                this.trailingWSStart = 0;
                break;
            }
            default: {
                switch (this.reorderingMode) {
                    case 0: {
                        this.impTabPair = Bidi.impTab_DEFAULT;
                        break;
                    }
                    case 1: {
                        this.impTabPair = Bidi.impTab_NUMBERS_SPECIAL;
                        break;
                    }
                    case 2: {
                        this.impTabPair = Bidi.impTab_GROUP_NUMBERS_WITH_R;
                        break;
                    }
                    case 3: {
                        throw new InternalError("Internal ICU error in setPara");
                    }
                    case 4: {
                        this.impTabPair = Bidi.impTab_INVERSE_NUMBERS_AS_L;
                        break;
                    }
                    case 5: {
                        if ((this.reorderingOptions & 0x1) != 0x0) {
                            this.impTabPair = Bidi.impTab_INVERSE_LIKE_DIRECT_WITH_MARKS;
                            break;
                        }
                        this.impTabPair = Bidi.impTab_INVERSE_LIKE_DIRECT;
                        break;
                    }
                    case 6: {
                        if ((this.reorderingOptions & 0x1) != 0x0) {
                            this.impTabPair = Bidi.impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
                            break;
                        }
                        this.impTabPair = Bidi.impTab_INVERSE_FOR_NUMBERS_SPECIAL;
                        break;
                    }
                }
                if (embeddingLevels == null && this.paraCount <= 1 && (this.flags & Bidi.DirPropFlagMultiRuns) == 0x0) {
                    this.resolveImplicitLevels(0, this.length, GetLRFromLevel(this.GetParaLevelAt(0)), GetLRFromLevel(this.GetParaLevelAt(this.length - 1)));
                }
                else {
                    int limit = 0;
                    byte level = this.GetParaLevelAt(0);
                    byte nextLevel = this.levels[0];
                    short eor;
                    if (level < nextLevel) {
                        eor = GetLRFromLevel(nextLevel);
                    }
                    else {
                        eor = GetLRFromLevel(level);
                    }
                    do {
                        int start = limit;
                        level = nextLevel;
                        short sor;
                        if (start > 0 && NoContextRTL(this.dirProps[start - 1]) == 7) {
                            sor = GetLRFromLevel(this.GetParaLevelAt(start));
                        }
                        else {
                            sor = eor;
                        }
                        while (++limit < this.length && this.levels[limit] == level) {}
                        if (limit < this.length) {
                            nextLevel = this.levels[limit];
                        }
                        else {
                            nextLevel = this.GetParaLevelAt(this.length - 1);
                        }
                        if ((level & 0x7F) < (nextLevel & 0x7F)) {
                            eor = GetLRFromLevel(nextLevel);
                        }
                        else {
                            eor = GetLRFromLevel(level);
                        }
                        if ((level & 0xFFFFFF80) == 0x0) {
                            this.resolveImplicitLevels(start, limit, sor, eor);
                        }
                        else {
                            do {
                                final byte[] levels = this.levels;
                                final int n = start++;
                                levels[n] &= 0x7F;
                            } while (start < limit);
                        }
                    } while (limit < this.length);
                }
                this.adjustWSLevels();
                break;
            }
        }
        if (this.defaultParaLevel > 0 && (this.reorderingOptions & 0x1) != 0x0 && (this.reorderingMode == 5 || this.reorderingMode == 6)) {
            for (int i = 0; i < this.paraCount; ++i) {
                int last = this.paras[i] - 1;
                if ((this.dirProps[last] & 0x40) != 0x0) {
                    for (int start = (i == 0) ? 0 : this.paras[i - 1], j = last; j >= start; --j) {
                        final byte dirProp = NoContextRTL(this.dirProps[j]);
                        if (dirProp == 0) {
                            if (j < last) {
                                while (NoContextRTL(this.dirProps[last]) == 7) {
                                    --last;
                                }
                            }
                            this.addPoint(last, 4);
                            break;
                        }
                        if ((DirPropFlag(dirProp) & 0x2002) != 0x0) {
                            break;
                        }
                    }
                }
            }
        }
        if ((this.reorderingOptions & 0x2) != 0x0) {
            this.resultLength -= this.controlCount;
        }
        else {
            this.resultLength += this.insertPoints.size;
        }
        this.setParaSuccess();
    }
    
    public void setPara(final AttributedCharacterIterator paragraph) {
        final Boolean runDirection = (Boolean)paragraph.getAttribute(TextAttribute.RUN_DIRECTION);
        byte paraLvl;
        if (runDirection == null) {
            paraLvl = 126;
        }
        else {
            paraLvl = (byte)(runDirection.equals(TextAttribute.RUN_DIRECTION_LTR) ? 0 : 1);
        }
        byte[] lvls = null;
        final int len = paragraph.getEndIndex() - paragraph.getBeginIndex();
        final byte[] embeddingLevels = new byte[len];
        final char[] txt = new char[len];
        int i = 0;
        for (char ch = paragraph.first(); ch != '\uffff'; ch = paragraph.next(), ++i) {
            txt[i] = ch;
            final Integer embedding = (Integer)paragraph.getAttribute(TextAttribute.BIDI_EMBEDDING);
            if (embedding != null) {
                final byte level = embedding.byteValue();
                if (level != 0) {
                    if (level < 0) {
                        lvls = embeddingLevels;
                        embeddingLevels[i] = (byte)(0 - level | 0xFFFFFF80);
                    }
                    else {
                        lvls = embeddingLevels;
                        embeddingLevels[i] = level;
                    }
                }
            }
        }
        final NumericShaper shaper = (NumericShaper)paragraph.getAttribute(TextAttribute.NUMERIC_SHAPING);
        if (shaper != null) {
            shaper.shape(txt, 0, len);
        }
        this.setPara(txt, paraLvl, lvls);
    }
    
    public void orderParagraphsLTR(final boolean ordarParaLTR) {
        this.orderParagraphsLTR = ordarParaLTR;
    }
    
    public boolean isOrderParagraphsLTR() {
        return this.orderParagraphsLTR;
    }
    
    public byte getDirection() {
        this.verifyValidParaOrLine();
        return this.direction;
    }
    
    public String getTextAsString() {
        this.verifyValidParaOrLine();
        return new String(this.text);
    }
    
    public char[] getText() {
        this.verifyValidParaOrLine();
        return this.text;
    }
    
    public int getLength() {
        this.verifyValidParaOrLine();
        return this.originalLength;
    }
    
    public int getProcessedLength() {
        this.verifyValidParaOrLine();
        return this.length;
    }
    
    public int getResultLength() {
        this.verifyValidParaOrLine();
        return this.resultLength;
    }
    
    public byte getParaLevel() {
        this.verifyValidParaOrLine();
        return this.paraLevel;
    }
    
    public int countParagraphs() {
        this.verifyValidParaOrLine();
        return this.paraCount;
    }
    
    public BidiRun getParagraphByIndex(final int paraIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(paraIndex, 0, this.paraCount);
        final Bidi bidi = this.paraBidi;
        int paraStart;
        if (paraIndex == 0) {
            paraStart = 0;
        }
        else {
            paraStart = bidi.paras[paraIndex - 1];
        }
        final BidiRun bidiRun = new BidiRun();
        bidiRun.start = paraStart;
        bidiRun.limit = bidi.paras[paraIndex];
        bidiRun.level = this.GetParaLevelAt(paraStart);
        return bidiRun;
    }
    
    public BidiRun getParagraph(final int charIndex) {
        this.verifyValidParaOrLine();
        final Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex;
        for (paraIndex = 0; charIndex >= bidi.paras[paraIndex]; ++paraIndex) {}
        return this.getParagraphByIndex(paraIndex);
    }
    
    public int getParagraphIndex(final int charIndex) {
        this.verifyValidParaOrLine();
        final Bidi bidi = this.paraBidi;
        this.verifyRange(charIndex, 0, bidi.length);
        int paraIndex;
        for (paraIndex = 0; charIndex >= bidi.paras[paraIndex]; ++paraIndex) {}
        return paraIndex;
    }
    
    public void setCustomClassifier(final BidiClassifier classifier) {
        this.customClassifier = classifier;
    }
    
    public BidiClassifier getCustomClassifier() {
        return this.customClassifier;
    }
    
    public int getCustomizedClass(final int c) {
        final int dir;
        if (this.customClassifier == null || (dir = this.customClassifier.classify(c)) == 19) {
            return this.bdp.getClass(c);
        }
        return dir;
    }
    
    public Bidi setLine(final int start, final int limit) {
        this.verifyValidPara();
        this.verifyRange(start, 0, limit);
        this.verifyRange(limit, 0, this.length + 1);
        if (this.getParagraphIndex(start) != this.getParagraphIndex(limit - 1)) {
            throw new IllegalArgumentException();
        }
        return BidiLine.setLine(this, start, limit);
    }
    
    public byte getLevelAt(final int charIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(charIndex, 0, this.length);
        return BidiLine.getLevelAt(this, charIndex);
    }
    
    public byte[] getLevels() {
        this.verifyValidParaOrLine();
        if (this.length <= 0) {
            return new byte[0];
        }
        return BidiLine.getLevels(this);
    }
    
    public BidiRun getLogicalRun(final int logicalPosition) {
        this.verifyValidParaOrLine();
        this.verifyRange(logicalPosition, 0, this.length);
        return BidiLine.getLogicalRun(this, logicalPosition);
    }
    
    public int countRuns() {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        return this.runCount;
    }
    
    public BidiRun getVisualRun(final int runIndex) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(runIndex, 0, this.runCount);
        return BidiLine.getVisualRun(this, runIndex);
    }
    
    public int getVisualIndex(final int logicalIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(logicalIndex, 0, this.length);
        return BidiLine.getVisualIndex(this, logicalIndex);
    }
    
    public int getLogicalIndex(final int visualIndex) {
        this.verifyValidParaOrLine();
        this.verifyRange(visualIndex, 0, this.resultLength);
        if (this.insertPoints.size == 0 && this.controlCount == 0) {
            if (this.direction == 0) {
                return visualIndex;
            }
            if (this.direction == 1) {
                return this.length - visualIndex - 1;
            }
        }
        BidiLine.getRuns(this);
        return BidiLine.getLogicalIndex(this, visualIndex);
    }
    
    public int[] getLogicalMap() {
        this.countRuns();
        if (this.length <= 0) {
            return new int[0];
        }
        return BidiLine.getLogicalMap(this);
    }
    
    public int[] getVisualMap() {
        this.countRuns();
        if (this.resultLength <= 0) {
            return new int[0];
        }
        return BidiLine.getVisualMap(this);
    }
    
    public static int[] reorderLogical(final byte[] levels) {
        return BidiLine.reorderLogical(levels);
    }
    
    public static int[] reorderVisual(final byte[] levels) {
        return BidiLine.reorderVisual(levels);
    }
    
    public static int[] invertMap(final int[] srcMap) {
        if (srcMap == null) {
            return null;
        }
        return BidiLine.invertMap(srcMap);
    }
    
    public Bidi(final String paragraph, final int flags) {
        this(paragraph.toCharArray(), 0, null, 0, paragraph.length(), flags);
    }
    
    public Bidi(final AttributedCharacterIterator paragraph) {
        this();
        this.setPara(paragraph);
    }
    
    public Bidi(final char[] text, final int textStart, final byte[] embeddings, final int embStart, final int paragraphLength, final int flags) {
        this();
        byte paraLvl = 0;
        switch (flags) {
            default: {
                paraLvl = 0;
                break;
            }
            case 1: {
                paraLvl = 1;
                break;
            }
            case 126: {
                paraLvl = 126;
                break;
            }
            case 127: {
                paraLvl = 127;
                break;
            }
        }
        byte[] paraEmbeddings;
        if (embeddings == null) {
            paraEmbeddings = null;
        }
        else {
            paraEmbeddings = new byte[paragraphLength];
            for (int i = 0; i < paragraphLength; ++i) {
                byte lev = embeddings[i + embStart];
                if (lev < 0) {
                    lev = (byte)(-lev | 0xFFFFFF80);
                }
                else if (lev == 0 && (lev = paraLvl) > 61) {
                    lev &= 0x1;
                }
                paraEmbeddings[i] = lev;
            }
        }
        if (textStart == 0 && embStart == 0 && paragraphLength == text.length) {
            this.setPara(text, paraLvl, paraEmbeddings);
        }
        else {
            final char[] paraText = new char[paragraphLength];
            System.arraycopy(text, textStart, paraText, 0, paragraphLength);
            this.setPara(paraText, paraLvl, paraEmbeddings);
        }
    }
    
    public Bidi createLineBidi(final int lineStart, final int lineLimit) {
        return this.setLine(lineStart, lineLimit);
    }
    
    public boolean isMixed() {
        return !this.isLeftToRight() && !this.isRightToLeft();
    }
    
    public boolean isLeftToRight() {
        return this.getDirection() == 0 && (this.paraLevel & 0x1) == 0x0;
    }
    
    public boolean isRightToLeft() {
        return this.getDirection() == 1 && (this.paraLevel & 0x1) == 0x1;
    }
    
    public boolean baseIsLeftToRight() {
        return this.getParaLevel() == 0;
    }
    
    public int getBaseLevel() {
        return this.getParaLevel();
    }
    
    public int getRunCount() {
        return this.countRuns();
    }
    
    void getLogicalToVisualRunsMap() {
        if (this.isGoodLogicalToVisualRunsMap) {
            return;
        }
        final int count = this.countRuns();
        if (this.logicalToVisualRunsMap == null || this.logicalToVisualRunsMap.length < count) {
            this.logicalToVisualRunsMap = new int[count];
        }
        final long[] keys = new long[count];
        for (int i = 0; i < count; ++i) {
            keys[i] = ((long)this.runs[i].start << 32) + i;
        }
        Arrays.sort(keys);
        for (int i = 0; i < count; ++i) {
            this.logicalToVisualRunsMap[i] = (int)(keys[i] & -1L);
        }
        this.isGoodLogicalToVisualRunsMap = true;
    }
    
    public int getRunLevel(final int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[run]].level;
    }
    
    public int getRunStart(final int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        return this.runs[this.logicalToVisualRunsMap[run]].start;
    }
    
    public int getRunLimit(final int run) {
        this.verifyValidParaOrLine();
        BidiLine.getRuns(this);
        this.verifyRange(run, 0, this.runCount);
        this.getLogicalToVisualRunsMap();
        final int idx = this.logicalToVisualRunsMap[run];
        final int len = (idx == 0) ? this.runs[idx].limit : (this.runs[idx].limit - this.runs[idx - 1].limit);
        return this.runs[idx].start + len;
    }
    
    public static boolean requiresBidi(final char[] text, final int start, final int limit) {
        final int RTLMask = 57378;
        for (int i = start; i < limit; ++i) {
            if ((1 << UCharacter.getDirection(text[i]) & 0xE022) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    public static void reorderVisually(final byte[] levels, final int levelStart, final Object[] objects, final int objectStart, final int count) {
        final byte[] reorderLevels = new byte[count];
        System.arraycopy(levels, levelStart, reorderLevels, 0, count);
        final int[] indexMap = reorderVisual(reorderLevels);
        final Object[] temp = new Object[count];
        System.arraycopy(objects, objectStart, temp, 0, count);
        for (int i = 0; i < count; ++i) {
            objects[objectStart + i] = temp[indexMap[i]];
        }
    }
    
    public String writeReordered(final int options) {
        this.verifyValidParaOrLine();
        if (this.length == 0) {
            return "";
        }
        return BidiWriter.writeReordered(this, options);
    }
    
    public static String writeReverse(final String src, final int options) {
        if (src == null) {
            throw new IllegalArgumentException();
        }
        if (src.length() > 0) {
            return BidiWriter.writeReverse(src, options);
        }
        return "";
    }
    
    public static byte getBaseDirection(final CharSequence paragraph) {
        if (paragraph == null || paragraph.length() == 0) {
            return 3;
        }
        for (int length = paragraph.length(), i = 0; i < length; i = UCharacter.offsetByCodePoints(paragraph, i, 1)) {
            final int c = UCharacter.codePointAt(paragraph, i);
            final byte direction = UCharacter.getDirectionality(c);
            if (direction == 0) {
                return 0;
            }
            if (direction == 1 || direction == 13) {
                return 1;
            }
        }
        return 3;
    }
    
    static {
        DirPropFlagMultiRuns = DirPropFlag((byte)31);
        DirPropFlagLR = new int[] { DirPropFlag((byte)0), DirPropFlag((byte)1) };
        DirPropFlagE = new int[] { DirPropFlag((byte)11), DirPropFlag((byte)14) };
        DirPropFlagO = new int[] { DirPropFlag((byte)12), DirPropFlag((byte)15) };
        MASK_LTR = (DirPropFlag((byte)0) | DirPropFlag((byte)2) | DirPropFlag((byte)5) | DirPropFlag((byte)11) | DirPropFlag((byte)12));
        MASK_RTL = (DirPropFlag((byte)1) | DirPropFlag((byte)13) | DirPropFlag((byte)14) | DirPropFlag((byte)15));
        MASK_LRX = (DirPropFlag((byte)11) | DirPropFlag((byte)12));
        MASK_RLX = (DirPropFlag((byte)14) | DirPropFlag((byte)15));
        MASK_OVERRIDE = (DirPropFlag((byte)12) | DirPropFlag((byte)15));
        MASK_EXPLICIT = (Bidi.MASK_LRX | Bidi.MASK_RLX | DirPropFlag((byte)16));
        MASK_BN_EXPLICIT = (DirPropFlag((byte)18) | Bidi.MASK_EXPLICIT);
        MASK_B_S = (DirPropFlag((byte)7) | DirPropFlag((byte)8));
        MASK_WS = (Bidi.MASK_B_S | DirPropFlag((byte)9) | Bidi.MASK_BN_EXPLICIT);
        MASK_N = (DirPropFlag((byte)10) | Bidi.MASK_WS);
        MASK_ET_NSM_BN = (DirPropFlag((byte)4) | DirPropFlag((byte)17) | Bidi.MASK_BN_EXPLICIT);
        MASK_POSSIBLE_N = (DirPropFlag((byte)6) | DirPropFlag((byte)3) | DirPropFlag((byte)4) | Bidi.MASK_N);
        MASK_EMBEDDING = (DirPropFlag((byte)17) | Bidi.MASK_POSSIBLE_N);
        groupProp = new short[] { 0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10 };
        impTabProps = new short[][] { { 1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 4 }, { 1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 0 }, { 33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 1 }, { 33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 1 }, { 33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 2 }, { 33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 3 }, { 33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 3 }, { 33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 4 }, { 33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 4 }, { 33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 4 }, { 97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 2 }, { 33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 2 }, { 97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 3 }, { 97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 3 }, { 33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 4 }, { 33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 5 }, { 33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 5 }, { 33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 6 } };
        impTabL_DEFAULT = new byte[][] { { 0, 1, 0, 2, 0, 0, 0, 0 }, { 0, 1, 3, 3, 20, 20, 0, 1 }, { 0, 1, 0, 2, 21, 21, 0, 2 }, { 0, 1, 3, 3, 20, 20, 0, 2 }, { 32, 1, 3, 3, 4, 4, 32, 1 }, { 32, 1, 32, 2, 5, 5, 32, 1 } };
        impTabR_DEFAULT = new byte[][] { { 1, 0, 2, 2, 0, 0, 0, 0 }, { 1, 0, 1, 3, 20, 20, 0, 1 }, { 1, 0, 2, 2, 0, 0, 0, 1 }, { 1, 0, 1, 3, 5, 5, 0, 1 }, { 33, 0, 33, 3, 4, 4, 0, 0 }, { 1, 0, 1, 3, 5, 5, 0, 0 } };
        impAct0 = new short[] { 0, 1, 2, 3, 4, 5, 6 };
        impTab_DEFAULT = new ImpTabPair(Bidi.impTabL_DEFAULT, Bidi.impTabR_DEFAULT, Bidi.impAct0, Bidi.impAct0);
        impTabL_NUMBERS_SPECIAL = new byte[][] { { 0, 2, 1, 1, 0, 0, 0, 0 }, { 0, 2, 1, 1, 0, 0, 0, 2 }, { 0, 2, 4, 4, 19, 0, 0, 1 }, { 32, 2, 4, 4, 3, 3, 32, 1 }, { 0, 2, 4, 4, 19, 19, 0, 2 } };
        impTab_NUMBERS_SPECIAL = new ImpTabPair(Bidi.impTabL_NUMBERS_SPECIAL, Bidi.impTabR_DEFAULT, Bidi.impAct0, Bidi.impAct0);
        impTabL_GROUP_NUMBERS_WITH_R = new byte[][] { { 0, 3, 17, 17, 0, 0, 0, 0 }, { 32, 3, 1, 1, 2, 32, 32, 2 }, { 32, 3, 1, 1, 2, 32, 32, 1 }, { 0, 3, 5, 5, 20, 0, 0, 1 }, { 32, 3, 5, 5, 4, 32, 32, 1 }, { 0, 3, 5, 5, 20, 0, 0, 2 } };
        impTabR_GROUP_NUMBERS_WITH_R = new byte[][] { { 2, 0, 1, 1, 0, 0, 0, 0 }, { 2, 0, 1, 1, 0, 0, 0, 1 }, { 2, 0, 20, 20, 19, 0, 0, 1 }, { 34, 0, 4, 4, 3, 0, 0, 0 }, { 34, 0, 4, 4, 3, 0, 0, 1 } };
        impTab_GROUP_NUMBERS_WITH_R = new ImpTabPair(Bidi.impTabL_GROUP_NUMBERS_WITH_R, Bidi.impTabR_GROUP_NUMBERS_WITH_R, Bidi.impAct0, Bidi.impAct0);
        impTabL_INVERSE_NUMBERS_AS_L = new byte[][] { { 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 20, 20, 0, 1 }, { 0, 1, 0, 0, 21, 21, 0, 2 }, { 0, 1, 0, 0, 20, 20, 0, 2 }, { 32, 1, 32, 32, 4, 4, 32, 1 }, { 32, 1, 32, 32, 5, 5, 32, 1 } };
        impTabR_INVERSE_NUMBERS_AS_L = new byte[][] { { 1, 0, 1, 1, 0, 0, 0, 0 }, { 1, 0, 1, 1, 20, 20, 0, 1 }, { 1, 0, 1, 1, 0, 0, 0, 1 }, { 1, 0, 1, 1, 5, 5, 0, 1 }, { 33, 0, 33, 33, 4, 4, 0, 0 }, { 1, 0, 1, 1, 5, 5, 0, 0 } };
        impTab_INVERSE_NUMBERS_AS_L = new ImpTabPair(Bidi.impTabL_INVERSE_NUMBERS_AS_L, Bidi.impTabR_INVERSE_NUMBERS_AS_L, Bidi.impAct0, Bidi.impAct0);
        impTabR_INVERSE_LIKE_DIRECT = new byte[][] { { 1, 0, 2, 2, 0, 0, 0, 0 }, { 1, 0, 1, 2, 19, 19, 0, 1 }, { 1, 0, 2, 2, 0, 0, 0, 1 }, { 33, 48, 6, 4, 3, 3, 48, 0 }, { 33, 48, 6, 4, 5, 5, 48, 3 }, { 33, 48, 6, 4, 5, 5, 48, 2 }, { 33, 48, 6, 4, 3, 3, 48, 1 } };
        impAct1 = new short[] { 0, 1, 11, 12 };
        impTab_INVERSE_LIKE_DIRECT = new ImpTabPair(Bidi.impTabL_DEFAULT, Bidi.impTabR_INVERSE_LIKE_DIRECT, Bidi.impAct0, Bidi.impAct1);
        impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][] { { 0, 99, 0, 1, 0, 0, 0, 0 }, { 0, 99, 0, 1, 18, 48, 0, 4 }, { 32, 99, 32, 1, 2, 48, 32, 3 }, { 0, 99, 85, 86, 20, 48, 0, 3 }, { 48, 67, 85, 86, 4, 48, 48, 3 }, { 48, 67, 5, 86, 20, 48, 48, 4 }, { 48, 67, 85, 6, 20, 48, 48, 4 } };
        impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][] { { 19, 0, 1, 1, 0, 0, 0, 0 }, { 35, 0, 1, 1, 2, 64, 0, 1 }, { 35, 0, 1, 1, 2, 64, 0, 0 }, { 3, 0, 3, 54, 20, 64, 0, 1 }, { 83, 64, 5, 54, 4, 64, 64, 0 }, { 83, 64, 5, 54, 4, 64, 64, 1 }, { 83, 64, 6, 6, 4, 64, 64, 3 } };
        impAct2 = new short[] { 0, 1, 7, 8, 9, 10 };
        impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new ImpTabPair(Bidi.impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, Bidi.impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, Bidi.impAct0, Bidi.impAct2);
        impTab_INVERSE_FOR_NUMBERS_SPECIAL = new ImpTabPair(Bidi.impTabL_NUMBERS_SPECIAL, Bidi.impTabR_INVERSE_LIKE_DIRECT, Bidi.impAct0, Bidi.impAct1);
        impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new byte[][] { { 0, 98, 1, 1, 0, 0, 0, 0 }, { 0, 98, 1, 1, 0, 48, 0, 4 }, { 0, 98, 84, 84, 19, 48, 0, 3 }, { 48, 66, 84, 84, 3, 48, 48, 3 }, { 48, 66, 4, 4, 19, 48, 48, 4 } };
        impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new ImpTabPair(Bidi.impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, Bidi.impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, Bidi.impAct0, Bidi.impAct2);
    }
    
    static class Point
    {
        int pos;
        int flag;
    }
    
    static class InsertPoints
    {
        int size;
        int confirmed;
        Point[] points;
        
        InsertPoints() {
            this.points = new Point[0];
        }
    }
    
    private static class ImpTabPair
    {
        byte[][][] imptab;
        short[][] impact;
        
        ImpTabPair(final byte[][] table1, final byte[][] table2, final short[] act1, final short[] act2) {
            this.imptab = new byte[][][] { table1, table2 };
            this.impact = new short[][] { act1, act2 };
        }
    }
    
    private static class LevState
    {
        byte[][] impTab;
        short[] impAct;
        int startON;
        int startL2EN;
        int lastStrongRTL;
        short state;
        byte runLevel;
    }
}
