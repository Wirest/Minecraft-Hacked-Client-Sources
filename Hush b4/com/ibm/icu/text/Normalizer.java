// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.impl.Norm2AllModes;
import java.nio.CharBuffer;
import java.text.CharacterIterator;

public final class Normalizer implements Cloneable
{
    private UCharacterIterator text;
    private Normalizer2 norm2;
    private Mode mode;
    private int options;
    private int currentIndex;
    private int nextIndex;
    private StringBuilder buffer;
    private int bufferPos;
    public static final int UNICODE_3_2 = 32;
    public static final int DONE = -1;
    public static final Mode NONE;
    public static final Mode NFD;
    public static final Mode NFKD;
    public static final Mode NFC;
    public static final Mode DEFAULT;
    public static final Mode NFKC;
    public static final Mode FCD;
    @Deprecated
    public static final Mode NO_OP;
    @Deprecated
    public static final Mode COMPOSE;
    @Deprecated
    public static final Mode COMPOSE_COMPAT;
    @Deprecated
    public static final Mode DECOMP;
    @Deprecated
    public static final Mode DECOMP_COMPAT;
    @Deprecated
    public static final int IGNORE_HANGUL = 1;
    public static final QuickCheckResult NO;
    public static final QuickCheckResult YES;
    public static final QuickCheckResult MAYBE;
    public static final int FOLD_CASE_DEFAULT = 0;
    public static final int INPUT_IS_FCD = 131072;
    public static final int COMPARE_IGNORE_CASE = 65536;
    public static final int COMPARE_CODE_POINT_ORDER = 32768;
    public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
    public static final int COMPARE_NORM_OPTIONS_SHIFT = 20;
    private static final int COMPARE_EQUIV = 524288;
    
    public Normalizer(final String str, final Mode mode, final int opt) {
        this.text = UCharacterIterator.getInstance(str);
        this.mode = mode;
        this.options = opt;
        this.norm2 = mode.getNormalizer2(opt);
        this.buffer = new StringBuilder();
    }
    
    public Normalizer(final CharacterIterator iter, final Mode mode, final int opt) {
        this.text = UCharacterIterator.getInstance((CharacterIterator)iter.clone());
        this.mode = mode;
        this.options = opt;
        this.norm2 = mode.getNormalizer2(opt);
        this.buffer = new StringBuilder();
    }
    
    public Normalizer(final UCharacterIterator iter, final Mode mode, final int options) {
        try {
            this.text = (UCharacterIterator)iter.clone();
            this.mode = mode;
            this.options = options;
            this.norm2 = mode.getNormalizer2(options);
            this.buffer = new StringBuilder();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e.toString());
        }
    }
    
    public Object clone() {
        try {
            final Normalizer copy = (Normalizer)super.clone();
            copy.text = (UCharacterIterator)this.text.clone();
            copy.mode = this.mode;
            copy.options = this.options;
            copy.norm2 = this.norm2;
            copy.buffer = new StringBuilder(this.buffer);
            copy.bufferPos = this.bufferPos;
            copy.currentIndex = this.currentIndex;
            copy.nextIndex = this.nextIndex;
            return copy;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static final Normalizer2 getComposeNormalizer2(final boolean compat, final int options) {
        return (compat ? Normalizer.NFKC : Normalizer.NFC).getNormalizer2(options);
    }
    
    private static final Normalizer2 getDecomposeNormalizer2(final boolean compat, final int options) {
        return (compat ? Normalizer.NFKD : Normalizer.NFD).getNormalizer2(options);
    }
    
    public static String compose(final String str, final boolean compat) {
        return compose(str, compat, 0);
    }
    
    public static String compose(final String str, final boolean compat, final int options) {
        return getComposeNormalizer2(compat, options).normalize(str);
    }
    
    public static int compose(final char[] source, final char[] target, final boolean compat, final int options) {
        return compose(source, 0, source.length, target, 0, target.length, compat, options);
    }
    
    public static int compose(final char[] src, final int srcStart, final int srcLimit, final char[] dest, final int destStart, final int destLimit, final boolean compat, final int options) {
        final CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
        final CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
        getComposeNormalizer2(compat, options).normalize(srcBuffer, app);
        return app.length();
    }
    
    public static String decompose(final String str, final boolean compat) {
        return decompose(str, compat, 0);
    }
    
    public static String decompose(final String str, final boolean compat, final int options) {
        return getDecomposeNormalizer2(compat, options).normalize(str);
    }
    
    public static int decompose(final char[] source, final char[] target, final boolean compat, final int options) {
        return decompose(source, 0, source.length, target, 0, target.length, compat, options);
    }
    
    public static int decompose(final char[] src, final int srcStart, final int srcLimit, final char[] dest, final int destStart, final int destLimit, final boolean compat, final int options) {
        final CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
        final CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
        getDecomposeNormalizer2(compat, options).normalize(srcBuffer, app);
        return app.length();
    }
    
    public static String normalize(final String str, final Mode mode, final int options) {
        return mode.getNormalizer2(options).normalize(str);
    }
    
    public static String normalize(final String src, final Mode mode) {
        return normalize(src, mode, 0);
    }
    
    public static int normalize(final char[] source, final char[] target, final Mode mode, final int options) {
        return normalize(source, 0, source.length, target, 0, target.length, mode, options);
    }
    
    public static int normalize(final char[] src, final int srcStart, final int srcLimit, final char[] dest, final int destStart, final int destLimit, final Mode mode, final int options) {
        final CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
        final CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
        mode.getNormalizer2(options).normalize(srcBuffer, app);
        return app.length();
    }
    
    public static String normalize(final int char32, final Mode mode, final int options) {
        if (mode == Normalizer.NFD && options == 0) {
            String decomposition = Norm2AllModes.getNFCInstance().impl.getDecomposition(char32);
            if (decomposition == null) {
                decomposition = UTF16.valueOf(char32);
            }
            return decomposition;
        }
        return normalize(UTF16.valueOf(char32), mode, options);
    }
    
    public static String normalize(final int char32, final Mode mode) {
        return normalize(char32, mode, 0);
    }
    
    public static QuickCheckResult quickCheck(final String source, final Mode mode) {
        return quickCheck(source, mode, 0);
    }
    
    public static QuickCheckResult quickCheck(final String source, final Mode mode, final int options) {
        return mode.getNormalizer2(options).quickCheck(source);
    }
    
    public static QuickCheckResult quickCheck(final char[] source, final Mode mode, final int options) {
        return quickCheck(source, 0, source.length, mode, options);
    }
    
    public static QuickCheckResult quickCheck(final char[] source, final int start, final int limit, final Mode mode, final int options) {
        final CharBuffer srcBuffer = CharBuffer.wrap(source, start, limit - start);
        return mode.getNormalizer2(options).quickCheck(srcBuffer);
    }
    
    public static boolean isNormalized(final char[] src, final int start, final int limit, final Mode mode, final int options) {
        final CharBuffer srcBuffer = CharBuffer.wrap(src, start, limit - start);
        return mode.getNormalizer2(options).isNormalized(srcBuffer);
    }
    
    public static boolean isNormalized(final String str, final Mode mode, final int options) {
        return mode.getNormalizer2(options).isNormalized(str);
    }
    
    public static boolean isNormalized(final int char32, final Mode mode, final int options) {
        return isNormalized(UTF16.valueOf(char32), mode, options);
    }
    
    public static int compare(final char[] s1, final int s1Start, final int s1Limit, final char[] s2, final int s2Start, final int s2Limit, final int options) {
        if (s1 == null || s1Start < 0 || s1Limit < 0 || s2 == null || s2Start < 0 || s2Limit < 0 || s1Limit < s1Start || s2Limit < s2Start) {
            throw new IllegalArgumentException();
        }
        return internalCompare(CharBuffer.wrap(s1, s1Start, s1Limit - s1Start), CharBuffer.wrap(s2, s2Start, s2Limit - s2Start), options);
    }
    
    public static int compare(final String s1, final String s2, final int options) {
        return internalCompare(s1, s2, options);
    }
    
    public static int compare(final char[] s1, final char[] s2, final int options) {
        return internalCompare(CharBuffer.wrap(s1), CharBuffer.wrap(s2), options);
    }
    
    public static int compare(final int char32a, final int char32b, final int options) {
        return internalCompare(UTF16.valueOf(char32a), UTF16.valueOf(char32b), options | 0x20000);
    }
    
    public static int compare(final int char32a, final String str2, final int options) {
        return internalCompare(UTF16.valueOf(char32a), str2, options);
    }
    
    public static int concatenate(final char[] left, final int leftStart, final int leftLimit, final char[] right, final int rightStart, final int rightLimit, final char[] dest, final int destStart, final int destLimit, final Mode mode, final int options) {
        if (dest == null) {
            throw new IllegalArgumentException();
        }
        if (right == dest && rightStart < destLimit && destStart < rightLimit) {
            throw new IllegalArgumentException("overlapping right and dst ranges");
        }
        final StringBuilder destBuilder = new StringBuilder(leftLimit - leftStart + rightLimit - rightStart + 16);
        destBuilder.append(left, leftStart, leftLimit - leftStart);
        final CharBuffer rightBuffer = CharBuffer.wrap(right, rightStart, rightLimit - rightStart);
        mode.getNormalizer2(options).append(destBuilder, rightBuffer);
        final int destLength = destBuilder.length();
        if (destLength <= destLimit - destStart) {
            destBuilder.getChars(0, destLength, dest, destStart);
            return destLength;
        }
        throw new IndexOutOfBoundsException(Integer.toString(destLength));
    }
    
    public static String concatenate(final char[] left, final char[] right, final Mode mode, final int options) {
        final StringBuilder dest = new StringBuilder(left.length + right.length + 16).append(left);
        return mode.getNormalizer2(options).append(dest, CharBuffer.wrap(right)).toString();
    }
    
    public static String concatenate(final String left, final String right, final Mode mode, final int options) {
        final StringBuilder dest = new StringBuilder(left.length() + right.length() + 16).append(left);
        return mode.getNormalizer2(options).append(dest, right).toString();
    }
    
    public static int getFC_NFKC_Closure(final int c, final char[] dest) {
        final String closure = getFC_NFKC_Closure(c);
        final int length = closure.length();
        if (length != 0 && dest != null && length <= dest.length) {
            closure.getChars(0, length, dest, 0);
        }
        return length;
    }
    
    public static String getFC_NFKC_Closure(final int c) {
        final Normalizer2 nfkc = NFKCModeImpl.INSTANCE.normalizer2;
        final UCaseProps csp = UCaseProps.INSTANCE;
        final StringBuilder folded = new StringBuilder();
        final int folded1Length = csp.toFullFolding(c, folded, 0);
        if (folded1Length < 0) {
            final Normalizer2Impl nfkcImpl = ((Norm2AllModes.Normalizer2WithImpl)nfkc).impl;
            if (nfkcImpl.getCompQuickCheck(nfkcImpl.getNorm16(c)) != 0) {
                return "";
            }
            folded.appendCodePoint(c);
        }
        else if (folded1Length > 31) {
            folded.appendCodePoint(folded1Length);
        }
        final String kc1 = nfkc.normalize(folded);
        final String kc2 = nfkc.normalize(UCharacter.foldCase(kc1, 0));
        if (kc1.equals(kc2)) {
            return "";
        }
        return kc2;
    }
    
    public int current() {
        if (this.bufferPos < this.buffer.length() || this.nextNormalize()) {
            return this.buffer.codePointAt(this.bufferPos);
        }
        return -1;
    }
    
    public int next() {
        if (this.bufferPos < this.buffer.length() || this.nextNormalize()) {
            final int c = this.buffer.codePointAt(this.bufferPos);
            this.bufferPos += Character.charCount(c);
            return c;
        }
        return -1;
    }
    
    public int previous() {
        if (this.bufferPos > 0 || this.previousNormalize()) {
            final int c = this.buffer.codePointBefore(this.bufferPos);
            this.bufferPos -= Character.charCount(c);
            return c;
        }
        return -1;
    }
    
    public void reset() {
        this.text.setToStart();
        final int n = 0;
        this.nextIndex = n;
        this.currentIndex = n;
        this.clearBuffer();
    }
    
    public void setIndexOnly(final int index) {
        this.text.setIndex(index);
        this.nextIndex = index;
        this.currentIndex = index;
        this.clearBuffer();
    }
    
    @Deprecated
    public int setIndex(final int index) {
        this.setIndexOnly(index);
        return this.current();
    }
    
    @Deprecated
    public int getBeginIndex() {
        return 0;
    }
    
    @Deprecated
    public int getEndIndex() {
        return this.endIndex();
    }
    
    public int first() {
        this.reset();
        return this.next();
    }
    
    public int last() {
        this.text.setToLimit();
        final int index = this.text.getIndex();
        this.nextIndex = index;
        this.currentIndex = index;
        this.clearBuffer();
        return this.previous();
    }
    
    public int getIndex() {
        if (this.bufferPos < this.buffer.length()) {
            return this.currentIndex;
        }
        return this.nextIndex;
    }
    
    public int startIndex() {
        return 0;
    }
    
    public int endIndex() {
        return this.text.getLength();
    }
    
    public void setMode(final Mode newMode) {
        this.mode = newMode;
        this.norm2 = this.mode.getNormalizer2(this.options);
    }
    
    public Mode getMode() {
        return this.mode;
    }
    
    public void setOption(final int option, final boolean value) {
        if (value) {
            this.options |= option;
        }
        else {
            this.options &= ~option;
        }
        this.norm2 = this.mode.getNormalizer2(this.options);
    }
    
    public int getOption(final int option) {
        if ((this.options & option) != 0x0) {
            return 1;
        }
        return 0;
    }
    
    public int getText(final char[] fillIn) {
        return this.text.getText(fillIn);
    }
    
    public int getLength() {
        return this.text.getLength();
    }
    
    public String getText() {
        return this.text.getText();
    }
    
    public void setText(final StringBuffer newText) {
        final UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
        if (newIter == null) {
            throw new IllegalStateException("Could not create a new UCharacterIterator");
        }
        this.text = newIter;
        this.reset();
    }
    
    public void setText(final char[] newText) {
        final UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
        if (newIter == null) {
            throw new IllegalStateException("Could not create a new UCharacterIterator");
        }
        this.text = newIter;
        this.reset();
    }
    
    public void setText(final String newText) {
        final UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
        if (newIter == null) {
            throw new IllegalStateException("Could not create a new UCharacterIterator");
        }
        this.text = newIter;
        this.reset();
    }
    
    public void setText(final CharacterIterator newText) {
        final UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
        if (newIter == null) {
            throw new IllegalStateException("Could not create a new UCharacterIterator");
        }
        this.text = newIter;
        this.reset();
    }
    
    public void setText(final UCharacterIterator newText) {
        try {
            final UCharacterIterator newIter = (UCharacterIterator)newText.clone();
            if (newIter == null) {
                throw new IllegalStateException("Could not create a new UCharacterIterator");
            }
            this.text = newIter;
            this.reset();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Could not clone the UCharacterIterator");
        }
    }
    
    private void clearBuffer() {
        this.buffer.setLength(0);
        this.bufferPos = 0;
    }
    
    private boolean nextNormalize() {
        this.clearBuffer();
        this.currentIndex = this.nextIndex;
        this.text.setIndex(this.nextIndex);
        int c = this.text.nextCodePoint();
        if (c < 0) {
            return false;
        }
        final StringBuilder segment = new StringBuilder().appendCodePoint(c);
        while ((c = this.text.nextCodePoint()) >= 0) {
            if (this.norm2.hasBoundaryBefore(c)) {
                this.text.moveCodePointIndex(-1);
                break;
            }
            segment.appendCodePoint(c);
        }
        this.nextIndex = this.text.getIndex();
        this.norm2.normalize(segment, this.buffer);
        return this.buffer.length() != 0;
    }
    
    private boolean previousNormalize() {
        this.clearBuffer();
        this.nextIndex = this.currentIndex;
        this.text.setIndex(this.currentIndex);
        final StringBuilder segment = new StringBuilder();
        int c;
        while ((c = this.text.previousCodePoint()) >= 0) {
            if (c <= 65535) {
                segment.insert(0, (char)c);
            }
            else {
                segment.insert(0, Character.toChars(c));
            }
            if (this.norm2.hasBoundaryBefore(c)) {
                break;
            }
        }
        this.currentIndex = this.text.getIndex();
        this.norm2.normalize(segment, this.buffer);
        this.bufferPos = this.buffer.length();
        return this.buffer.length() != 0;
    }
    
    private static int internalCompare(CharSequence s1, CharSequence s2, int options) {
        final int normOptions = options >>> 20;
        options |= 0x80000;
        if ((options & 0x20000) == 0x0 || (options & 0x1) != 0x0) {
            Normalizer2 n2;
            if ((options & 0x1) != 0x0) {
                n2 = Normalizer.NFD.getNormalizer2(normOptions);
            }
            else {
                n2 = Normalizer.FCD.getNormalizer2(normOptions);
            }
            final int spanQCYes1 = n2.spanQuickCheckYes(s1);
            final int spanQCYes2 = n2.spanQuickCheckYes(s2);
            if (spanQCYes1 < s1.length()) {
                final StringBuilder fcd1 = new StringBuilder(s1.length() + 16).append(s1, 0, spanQCYes1);
                s1 = n2.normalizeSecondAndAppend(fcd1, s1.subSequence(spanQCYes1, s1.length()));
            }
            if (spanQCYes2 < s2.length()) {
                final StringBuilder fcd2 = new StringBuilder(s2.length() + 16).append(s2, 0, spanQCYes2);
                s2 = n2.normalizeSecondAndAppend(fcd2, s2.subSequence(spanQCYes2, s2.length()));
            }
        }
        return cmpEquivFold(s1, s2, options);
    }
    
    private static final CmpEquivLevel[] createCmpEquivLevelStack() {
        return new CmpEquivLevel[] { new CmpEquivLevel(), new CmpEquivLevel() };
    }
    
    static int cmpEquivFold(CharSequence cs1, CharSequence cs2, final int options) {
        CmpEquivLevel[] stack1 = null;
        CmpEquivLevel[] stack2 = null;
        Normalizer2Impl nfcImpl;
        if ((options & 0x80000) != 0x0) {
            nfcImpl = Norm2AllModes.getNFCInstance().impl;
        }
        else {
            nfcImpl = null;
        }
        UCaseProps csp;
        StringBuilder fold1;
        StringBuilder fold2;
        if ((options & 0x10000) != 0x0) {
            csp = UCaseProps.INSTANCE;
            fold1 = new StringBuilder();
            fold2 = new StringBuilder();
        }
        else {
            csp = null;
            fold2 = (fold1 = null);
        }
        int s1 = 0;
        int limit1 = cs1.length();
        int s2 = 0;
        int limit2 = cs2.length();
        int level3;
        int level2 = level3 = 0;
        int c3;
        int c2 = c3 = -1;
        while (true) {
            Label_0177: {
                if (c3 < 0) {
                    while (s1 == limit1) {
                        if (level3 == 0) {
                            c3 = -1;
                            break Label_0177;
                        }
                        do {
                            --level3;
                            cs1 = stack1[level3].cs;
                        } while (cs1 == null);
                        s1 = stack1[level3].s;
                        limit1 = cs1.length();
                    }
                    c3 = cs1.charAt(s1++);
                }
            }
            Label_0253: {
                if (c2 < 0) {
                    while (s2 == limit2) {
                        if (level2 == 0) {
                            c2 = -1;
                            break Label_0253;
                        }
                        do {
                            --level2;
                            cs2 = stack2[level2].cs;
                        } while (cs2 == null);
                        s2 = stack2[level2].s;
                        limit2 = cs2.length();
                    }
                    c2 = cs2.charAt(s2++);
                }
            }
            if (c3 == c2) {
                if (c3 < 0) {
                    return 0;
                }
                c2 = (c3 = -1);
            }
            else {
                if (c3 < 0) {
                    return -1;
                }
                if (c2 < 0) {
                    return 1;
                }
                int cp1 = c3;
                if (UTF16.isSurrogate((char)c3)) {
                    if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c3)) {
                        final char c4;
                        if (s1 != limit1 && Character.isLowSurrogate(c4 = cs1.charAt(s1))) {
                            cp1 = Character.toCodePoint((char)c3, c4);
                        }
                    }
                    else {
                        final char c4;
                        if (0 <= s1 - 2 && Character.isHighSurrogate(c4 = cs1.charAt(s1 - 2))) {
                            cp1 = Character.toCodePoint(c4, (char)c3);
                        }
                    }
                }
                int cp2 = c2;
                if (UTF16.isSurrogate((char)c2)) {
                    if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2)) {
                        final char c4;
                        if (s2 != limit2 && Character.isLowSurrogate(c4 = cs2.charAt(s2))) {
                            cp2 = Character.toCodePoint((char)c2, c4);
                        }
                    }
                    else {
                        final char c4;
                        if (0 <= s2 - 2 && Character.isHighSurrogate(c4 = cs2.charAt(s2 - 2))) {
                            cp2 = Character.toCodePoint(c4, (char)c2);
                        }
                    }
                }
                int length;
                if (level3 == 0 && (options & 0x10000) != 0x0 && (length = csp.toFullFolding(cp1, fold1, options)) >= 0) {
                    if (UTF16.isSurrogate((char)c3)) {
                        if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c3)) {
                            ++s1;
                        }
                        else {
                            --s2;
                            c2 = cs2.charAt(s2 - 1);
                        }
                    }
                    if (stack1 == null) {
                        stack1 = createCmpEquivLevelStack();
                    }
                    stack1[0].cs = cs1;
                    stack1[0].s = s1;
                    ++level3;
                    if (length <= 31) {
                        fold1.delete(0, fold1.length() - length);
                    }
                    else {
                        fold1.setLength(0);
                        fold1.appendCodePoint(length);
                    }
                    cs1 = fold1;
                    s1 = 0;
                    limit1 = fold1.length();
                    c3 = -1;
                }
                else if (level2 == 0 && (options & 0x10000) != 0x0 && (length = csp.toFullFolding(cp2, fold2, options)) >= 0) {
                    if (UTF16.isSurrogate((char)c2)) {
                        if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2)) {
                            ++s2;
                        }
                        else {
                            --s1;
                            c3 = cs1.charAt(s1 - 1);
                        }
                    }
                    if (stack2 == null) {
                        stack2 = createCmpEquivLevelStack();
                    }
                    stack2[0].cs = cs2;
                    stack2[0].s = s2;
                    ++level2;
                    if (length <= 31) {
                        fold2.delete(0, fold2.length() - length);
                    }
                    else {
                        fold2.setLength(0);
                        fold2.appendCodePoint(length);
                    }
                    cs2 = fold2;
                    s2 = 0;
                    limit2 = fold2.length();
                    c2 = -1;
                }
                else {
                    final String decomp1;
                    if (level3 < 2 && (options & 0x80000) != 0x0 && (decomp1 = nfcImpl.getDecomposition(cp1)) != null) {
                        if (UTF16.isSurrogate((char)c3)) {
                            if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c3)) {
                                ++s1;
                            }
                            else {
                                --s2;
                                c2 = cs2.charAt(s2 - 1);
                            }
                        }
                        if (stack1 == null) {
                            stack1 = createCmpEquivLevelStack();
                        }
                        stack1[level3].cs = cs1;
                        stack1[level3].s = s1;
                        if (++level3 < 2) {
                            stack1[level3++].cs = null;
                        }
                        cs1 = decomp1;
                        s1 = 0;
                        limit1 = decomp1.length();
                        c3 = -1;
                    }
                    else {
                        final String decomp2;
                        if (level2 >= 2 || (options & 0x80000) == 0x0 || (decomp2 = nfcImpl.getDecomposition(cp2)) == null) {
                            if (c3 >= 55296 && c2 >= 55296 && (options & 0x8000) != 0x0) {
                                if (c3 > 56319 || s1 == limit1 || !Character.isLowSurrogate(cs1.charAt(s1))) {
                                    if (!Character.isLowSurrogate((char)c3) || 0 == s1 - 1 || !Character.isHighSurrogate(cs1.charAt(s1 - 2))) {
                                        c3 -= 10240;
                                    }
                                }
                                if (c2 > 56319 || s2 == limit2 || !Character.isLowSurrogate(cs2.charAt(s2))) {
                                    if (!Character.isLowSurrogate((char)c2) || 0 == s2 - 1 || !Character.isHighSurrogate(cs2.charAt(s2 - 2))) {
                                        c2 -= 10240;
                                    }
                                }
                            }
                            return c3 - c2;
                        }
                        if (UTF16.isSurrogate((char)c2)) {
                            if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2)) {
                                ++s2;
                            }
                            else {
                                --s1;
                                c3 = cs1.charAt(s1 - 1);
                            }
                        }
                        if (stack2 == null) {
                            stack2 = createCmpEquivLevelStack();
                        }
                        stack2[level2].cs = cs2;
                        stack2[level2].s = s2;
                        if (++level2 < 2) {
                            stack2[level2++].cs = null;
                        }
                        cs2 = decomp2;
                        s2 = 0;
                        limit2 = decomp2.length();
                        c2 = -1;
                    }
                }
            }
        }
    }
    
    static {
        NONE = new NONEMode();
        NFD = new NFDMode();
        NFKD = new NFKDMode();
        NFC = new NFCMode();
        DEFAULT = Normalizer.NFC;
        NFKC = new NFKCMode();
        FCD = new FCDMode();
        NO_OP = Normalizer.NONE;
        COMPOSE = Normalizer.NFC;
        COMPOSE_COMPAT = Normalizer.NFKC;
        DECOMP = Normalizer.NFD;
        DECOMP_COMPAT = Normalizer.NFKD;
        NO = new QuickCheckResult(0);
        YES = new QuickCheckResult(1);
        MAYBE = new QuickCheckResult(2);
    }
    
    private static final class ModeImpl
    {
        private final Normalizer2 normalizer2;
        
        private ModeImpl(final Normalizer2 n2) {
            this.normalizer2 = n2;
        }
    }
    
    private static final class NFDModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)Norm2AllModes.getNFCInstance().decomp);
        }
    }
    
    private static final class NFKDModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)Norm2AllModes.getNFKCInstance().decomp);
        }
    }
    
    private static final class NFCModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)Norm2AllModes.getNFCInstance().comp);
        }
    }
    
    private static final class NFKCModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)Norm2AllModes.getNFKCInstance().comp);
        }
    }
    
    private static final class FCDModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl(Norm2AllModes.getFCDNormalizer2());
        }
    }
    
    private static final class Unicode32
    {
        private static final UnicodeSet INSTANCE;
        
        static {
            INSTANCE = new UnicodeSet("[:age=3.2:]").freeze();
        }
    }
    
    private static final class NFD32ModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)new FilteredNormalizer2(Norm2AllModes.getNFCInstance().decomp, Unicode32.INSTANCE));
        }
    }
    
    private static final class NFKD32ModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().decomp, Unicode32.INSTANCE));
        }
    }
    
    private static final class NFC32ModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)new FilteredNormalizer2(Norm2AllModes.getNFCInstance().comp, Unicode32.INSTANCE));
        }
    }
    
    private static final class NFKC32ModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().comp, Unicode32.INSTANCE));
        }
    }
    
    private static final class FCD32ModeImpl
    {
        private static final ModeImpl INSTANCE;
        
        static {
            INSTANCE = new ModeImpl((Normalizer2)new FilteredNormalizer2(Norm2AllModes.getFCDNormalizer2(), Unicode32.INSTANCE));
        }
    }
    
    public abstract static class Mode
    {
        @Deprecated
        protected abstract Normalizer2 getNormalizer2(final int p0);
    }
    
    private static final class NONEMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return Norm2AllModes.NOOP_NORMALIZER2;
        }
    }
    
    private static final class NFDMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return ((options & 0x20) != 0x0) ? NFD32ModeImpl.INSTANCE.normalizer2 : NFDModeImpl.INSTANCE.normalizer2;
        }
    }
    
    private static final class NFKDMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return ((options & 0x20) != 0x0) ? NFKD32ModeImpl.INSTANCE.normalizer2 : NFKDModeImpl.INSTANCE.normalizer2;
        }
    }
    
    private static final class NFCMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return ((options & 0x20) != 0x0) ? NFC32ModeImpl.INSTANCE.normalizer2 : NFCModeImpl.INSTANCE.normalizer2;
        }
    }
    
    private static final class NFKCMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return ((options & 0x20) != 0x0) ? NFKC32ModeImpl.INSTANCE.normalizer2 : NFKCModeImpl.INSTANCE.normalizer2;
        }
    }
    
    private static final class FCDMode extends Mode
    {
        @Override
        protected Normalizer2 getNormalizer2(final int options) {
            return ((options & 0x20) != 0x0) ? FCD32ModeImpl.INSTANCE.normalizer2 : FCDModeImpl.INSTANCE.normalizer2;
        }
    }
    
    public static final class QuickCheckResult
    {
        private QuickCheckResult(final int value) {
        }
    }
    
    private static final class CmpEquivLevel
    {
        CharSequence cs;
        int s;
    }
    
    private static final class CharsAppendable implements Appendable
    {
        private final char[] chars;
        private final int start;
        private final int limit;
        private int offset;
        
        public CharsAppendable(final char[] dest, final int destStart, final int destLimit) {
            this.chars = dest;
            this.offset = destStart;
            this.start = destStart;
            this.limit = destLimit;
        }
        
        public int length() {
            final int len = this.offset - this.start;
            if (this.offset <= this.limit) {
                return len;
            }
            throw new IndexOutOfBoundsException(Integer.toString(len));
        }
        
        public Appendable append(final char c) {
            if (this.offset < this.limit) {
                this.chars[this.offset] = c;
            }
            ++this.offset;
            return this;
        }
        
        public Appendable append(final CharSequence s) {
            return this.append(s, 0, s.length());
        }
        
        public Appendable append(final CharSequence s, int sStart, final int sLimit) {
            final int len = sLimit - sStart;
            if (len <= this.limit - this.offset) {
                while (sStart < sLimit) {
                    this.chars[this.offset++] = s.charAt(sStart++);
                }
            }
            else {
                this.offset += len;
            }
            return this;
        }
    }
}
