// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.CharTrie;
import java.util.Stack;
import java.util.Iterator;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharacterIteration;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.text.StringCharacterIterator;
import java.util.Set;
import java.text.CharacterIterator;

public class RuleBasedBreakIterator extends BreakIterator
{
    public static final int WORD_NONE = 0;
    public static final int WORD_NONE_LIMIT = 100;
    public static final int WORD_NUMBER = 100;
    public static final int WORD_NUMBER_LIMIT = 200;
    public static final int WORD_LETTER = 200;
    public static final int WORD_LETTER_LIMIT = 300;
    public static final int WORD_KANA = 300;
    public static final int WORD_KANA_LIMIT = 400;
    public static final int WORD_IDEO = 400;
    public static final int WORD_IDEO_LIMIT = 500;
    private static final int START_STATE = 1;
    private static final int STOP_STATE = 0;
    private static final int RBBI_START = 0;
    private static final int RBBI_RUN = 1;
    private static final int RBBI_END = 2;
    private CharacterIterator fText;
    @Deprecated
    RBBIDataWrapper fRData;
    private int fLastRuleStatusIndex;
    private boolean fLastStatusIndexValid;
    private int fDictionaryCharCount;
    private static final String RBBI_DEBUG_ARG = "rbbi";
    @Deprecated
    private static final boolean TRACE;
    private int fBreakType;
    private final UnhandledBreakEngine fUnhandledBreakEngine;
    private int[] fCachedBreakPositions;
    private int fPositionInCache;
    private boolean fUseDictionary;
    private final Set<LanguageBreakEngine> fBreakEngines;
    static final String fDebugEnv;
    
    @Deprecated
    private RuleBasedBreakIterator() {
        this.fText = new StringCharacterIterator("");
        this.fBreakType = 2;
        this.fUnhandledBreakEngine = new UnhandledBreakEngine();
        this.fUseDictionary = true;
        this.fBreakEngines = Collections.synchronizedSet(new HashSet<LanguageBreakEngine>());
        this.fLastStatusIndexValid = true;
        this.fDictionaryCharCount = 0;
        this.fBreakEngines.add(this.fUnhandledBreakEngine);
    }
    
    public static RuleBasedBreakIterator getInstanceFromCompiledRules(final InputStream is) throws IOException {
        final RuleBasedBreakIterator This = new RuleBasedBreakIterator();
        This.fRData = RBBIDataWrapper.get(is);
        return This;
    }
    
    public RuleBasedBreakIterator(final String rules) {
        this();
        try {
            final ByteArrayOutputStream ruleOS = new ByteArrayOutputStream();
            compileRules(rules, ruleOS);
            final byte[] ruleBA = ruleOS.toByteArray();
            final InputStream ruleIS = new ByteArrayInputStream(ruleBA);
            this.fRData = RBBIDataWrapper.get(ruleIS);
        }
        catch (IOException e) {
            final RuntimeException rte = new RuntimeException("RuleBasedBreakIterator rule compilation internal error: " + e.getMessage());
            throw rte;
        }
    }
    
    @Override
    public Object clone() {
        final RuleBasedBreakIterator result = (RuleBasedBreakIterator)super.clone();
        if (this.fText != null) {
            result.fText = (CharacterIterator)this.fText.clone();
        }
        return result;
    }
    
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        try {
            final RuleBasedBreakIterator other = (RuleBasedBreakIterator)that;
            return (this.fRData == other.fRData || (this.fRData != null && other.fRData != null)) && (this.fRData == null || other.fRData == null || this.fRData.fRuleSource.equals(other.fRData.fRuleSource)) && ((this.fText == null && other.fText == null) || (this.fText != null && other.fText != null && this.fText.equals(other.fText)));
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        String retStr = "";
        if (this.fRData != null) {
            retStr = this.fRData.fRuleSource;
        }
        return retStr;
    }
    
    @Override
    public int hashCode() {
        return this.fRData.fRuleSource.hashCode();
    }
    
    @Deprecated
    public void dump() {
        this.fRData.dump();
    }
    
    public static void compileRules(final String rules, final OutputStream ruleBinary) throws IOException {
        RBBIRuleBuilder.compileRules(rules, ruleBinary);
    }
    
    @Override
    public int first() {
        this.fCachedBreakPositions = null;
        this.fDictionaryCharCount = 0;
        this.fPositionInCache = 0;
        this.fLastRuleStatusIndex = 0;
        this.fLastStatusIndexValid = true;
        if (this.fText == null) {
            return -1;
        }
        this.fText.first();
        return this.fText.getIndex();
    }
    
    @Override
    public int last() {
        this.fCachedBreakPositions = null;
        this.fDictionaryCharCount = 0;
        this.fPositionInCache = 0;
        if (this.fText == null) {
            this.fLastRuleStatusIndex = 0;
            this.fLastStatusIndexValid = true;
            return -1;
        }
        this.fLastStatusIndexValid = false;
        final int pos = this.fText.getEndIndex();
        this.fText.setIndex(pos);
        return pos;
    }
    
    @Override
    public int next(int n) {
        int result = this.current();
        while (n > 0) {
            result = this.handleNext();
            --n;
        }
        while (n < 0) {
            result = this.previous();
            ++n;
        }
        return result;
    }
    
    @Override
    public int next() {
        return this.handleNext();
    }
    
    @Override
    public int previous() {
        final CharacterIterator text = this.getText();
        this.fLastStatusIndexValid = false;
        if (this.fCachedBreakPositions != null && this.fPositionInCache > 0) {
            --this.fPositionInCache;
            text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
            return this.fCachedBreakPositions[this.fPositionInCache];
        }
        this.fCachedBreakPositions = null;
        final int offset = this.current();
        int result = this.rulesPrevious();
        if (result == -1) {
            return result;
        }
        if (this.fDictionaryCharCount == 0) {
            return result;
        }
        if (this.fCachedBreakPositions != null) {
            this.fPositionInCache = this.fCachedBreakPositions.length - 2;
            return result;
        }
        while (result < offset) {
            final int nextResult = this.handleNext();
            if (nextResult >= offset) {
                break;
            }
            result = nextResult;
        }
        if (this.fCachedBreakPositions != null) {
            this.fPositionInCache = 0;
            while (this.fPositionInCache < this.fCachedBreakPositions.length) {
                if (this.fCachedBreakPositions[this.fPositionInCache] >= offset) {
                    --this.fPositionInCache;
                    break;
                }
                ++this.fPositionInCache;
            }
        }
        this.fLastStatusIndexValid = false;
        text.setIndex(result);
        return result;
    }
    
    private int rulesPrevious() {
        if (this.fText == null || this.current() == this.fText.getBeginIndex()) {
            this.fLastRuleStatusIndex = 0;
            this.fLastStatusIndexValid = true;
            return -1;
        }
        if (this.fRData.fSRTable != null || this.fRData.fSFTable != null) {
            return this.handlePrevious(this.fRData.fRTable);
        }
        final int start = this.current();
        CharacterIteration.previous32(this.fText);
        int lastResult = this.handlePrevious(this.fRData.fRTable);
        if (lastResult == -1) {
            lastResult = this.fText.getBeginIndex();
            this.fText.setIndex(lastResult);
        }
        int result = lastResult;
        int lastTag = 0;
        boolean breakTagValid = false;
        while (true) {
            result = this.handleNext();
            if (result == -1 || result >= start) {
                break;
            }
            lastResult = result;
            lastTag = this.fLastRuleStatusIndex;
            breakTagValid = true;
        }
        this.fText.setIndex(lastResult);
        this.fLastRuleStatusIndex = lastTag;
        this.fLastStatusIndexValid = breakTagValid;
        return lastResult;
    }
    
    @Override
    public int following(final int offset) {
        final CharacterIterator text = this.getText();
        if (this.fCachedBreakPositions == null || offset < this.fCachedBreakPositions[0] || offset >= this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
            this.fCachedBreakPositions = null;
            return this.rulesFollowing(offset);
        }
        this.fPositionInCache = 0;
        while (this.fPositionInCache < this.fCachedBreakPositions.length && offset >= this.fCachedBreakPositions[this.fPositionInCache]) {
            ++this.fPositionInCache;
        }
        text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
        return text.getIndex();
    }
    
    private int rulesFollowing(final int offset) {
        this.fLastRuleStatusIndex = 0;
        this.fLastStatusIndexValid = true;
        if (this.fText == null || offset >= this.fText.getEndIndex()) {
            this.last();
            return this.next();
        }
        if (offset < this.fText.getBeginIndex()) {
            return this.first();
        }
        int result = 0;
        if (this.fRData.fSRTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.next32(this.fText);
            this.handlePrevious(this.fRData.fSRTable);
            for (result = this.next(); result <= offset; result = this.next()) {}
            return result;
        }
        if (this.fRData.fSFTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.previous32(this.fText);
            this.handleNext(this.fRData.fSFTable);
            for (int oldresult = this.previous(); oldresult > offset; oldresult = result) {
                result = this.previous();
                if (result <= offset) {
                    return oldresult;
                }
            }
            result = this.next();
            if (result <= offset) {
                return this.next();
            }
            return result;
        }
        else {
            this.fText.setIndex(offset);
            if (offset == this.fText.getBeginIndex()) {
                return this.handleNext();
            }
            for (result = this.previous(); result != -1 && result <= offset; result = this.next()) {}
            return result;
        }
    }
    
    @Override
    public int preceding(final int offset) {
        final CharacterIterator text = this.getText();
        if (this.fCachedBreakPositions == null || offset <= this.fCachedBreakPositions[0] || offset > this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
            this.fCachedBreakPositions = null;
            return this.rulesPreceding(offset);
        }
        this.fPositionInCache = 0;
        while (this.fPositionInCache < this.fCachedBreakPositions.length && offset > this.fCachedBreakPositions[this.fPositionInCache]) {
            ++this.fPositionInCache;
        }
        --this.fPositionInCache;
        text.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
        return text.getIndex();
    }
    
    private int rulesPreceding(final int offset) {
        if (this.fText == null || offset > this.fText.getEndIndex()) {
            return this.last();
        }
        if (offset < this.fText.getBeginIndex()) {
            return this.first();
        }
        if (this.fRData.fSFTable != null) {
            this.fText.setIndex(offset);
            CharacterIteration.previous32(this.fText);
            this.handleNext(this.fRData.fSFTable);
            int result;
            for (result = this.previous(); result >= offset; result = this.previous()) {}
            return result;
        }
        if (this.fRData.fSRTable == null) {
            this.fText.setIndex(offset);
            return this.previous();
        }
        this.fText.setIndex(offset);
        CharacterIteration.next32(this.fText);
        this.handlePrevious(this.fRData.fSRTable);
        int result;
        for (int oldresult = this.next(); oldresult < offset; oldresult = result) {
            result = this.next();
            if (result >= offset) {
                return oldresult;
            }
        }
        result = this.previous();
        if (result >= offset) {
            return this.previous();
        }
        return result;
    }
    
    protected static final void checkOffset(final int offset, final CharacterIterator text) {
        if (offset < text.getBeginIndex() || offset > text.getEndIndex()) {
            throw new IllegalArgumentException("offset out of bounds");
        }
    }
    
    @Override
    public boolean isBoundary(final int offset) {
        checkOffset(offset, this.fText);
        if (offset == this.fText.getBeginIndex()) {
            this.first();
            return true;
        }
        if (offset == this.fText.getEndIndex()) {
            this.last();
            return true;
        }
        this.fText.setIndex(offset);
        CharacterIteration.previous32(this.fText);
        final int pos = this.fText.getIndex();
        final boolean result = this.following(pos) == offset;
        return result;
    }
    
    @Override
    public int current() {
        return (this.fText != null) ? this.fText.getIndex() : -1;
    }
    
    private void makeRuleStatusValid() {
        if (!this.fLastStatusIndexValid) {
            final int curr = this.current();
            if (curr == -1 || curr == this.fText.getBeginIndex()) {
                this.fLastRuleStatusIndex = 0;
                this.fLastStatusIndexValid = true;
            }
            else {
                final int pa = this.fText.getIndex();
                this.first();
                int pb = this.current();
                while (this.fText.getIndex() < pa) {
                    pb = this.next();
                }
                Assert.assrt(pa == pb);
            }
            Assert.assrt(this.fLastStatusIndexValid);
            Assert.assrt(this.fLastRuleStatusIndex >= 0 && this.fLastRuleStatusIndex < this.fRData.fStatusTable.length);
        }
    }
    
    public int getRuleStatus() {
        this.makeRuleStatusValid();
        final int idx = this.fLastRuleStatusIndex + this.fRData.fStatusTable[this.fLastRuleStatusIndex];
        final int tagVal = this.fRData.fStatusTable[idx];
        return tagVal;
    }
    
    public int getRuleStatusVec(final int[] fillInArray) {
        this.makeRuleStatusValid();
        final int numStatusVals = this.fRData.fStatusTable[this.fLastRuleStatusIndex];
        if (fillInArray != null) {
            for (int numToCopy = Math.min(numStatusVals, fillInArray.length), i = 0; i < numToCopy; ++i) {
                fillInArray[i] = this.fRData.fStatusTable[this.fLastRuleStatusIndex + i + 1];
            }
        }
        return numStatusVals;
    }
    
    @Override
    public CharacterIterator getText() {
        return this.fText;
    }
    
    @Override
    public void setText(final CharacterIterator newText) {
        this.fText = newText;
        final int firstIdx = this.first();
        if (newText != null) {
            this.fUseDictionary = ((this.fBreakType == 1 || this.fBreakType == 2) && newText.getEndIndex() != firstIdx);
        }
    }
    
    @Deprecated
    void setBreakType(final int type) {
        this.fBreakType = type;
        if (type != 1 && type != 2) {
            this.fUseDictionary = false;
        }
    }
    
    @Deprecated
    int getBreakType() {
        return this.fBreakType;
    }
    
    @Deprecated
    private LanguageBreakEngine getEngineFor(final int c) {
        if (c == Integer.MAX_VALUE || !this.fUseDictionary) {
            return null;
        }
        for (final LanguageBreakEngine candidate : this.fBreakEngines) {
            if (candidate.handles(c, this.fBreakType)) {
                return candidate;
            }
        }
        final int script = UCharacter.getIntPropertyValue(c, 4106);
        LanguageBreakEngine eng = null;
        try {
            switch (script) {
                case 38: {
                    eng = new ThaiBreakEngine();
                    break;
                }
                case 17:
                case 20:
                case 22: {
                    if (this.getBreakType() == 1) {
                        eng = new CjkBreakEngine(false);
                        break;
                    }
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
                case 18: {
                    if (this.getBreakType() == 1) {
                        eng = new CjkBreakEngine(true);
                        break;
                    }
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
                default: {
                    this.fUnhandledBreakEngine.handleChar(c, this.getBreakType());
                    eng = this.fUnhandledBreakEngine;
                    break;
                }
            }
        }
        catch (IOException e) {
            eng = null;
        }
        if (eng != null) {
            this.fBreakEngines.add(eng);
        }
        return eng;
    }
    
    private int handleNext() {
        if (this.fCachedBreakPositions == null || this.fPositionInCache == this.fCachedBreakPositions.length - 1) {
            final int startPos = this.fText.getIndex();
            this.fDictionaryCharCount = 0;
            final int result = this.handleNext(this.fRData.fFTable);
            if (this.fDictionaryCharCount <= 1 || result - startPos <= 1) {
                this.fCachedBreakPositions = null;
                return result;
            }
            this.fText.setIndex(startPos);
            final LanguageBreakEngine e = this.getEngineFor(CharacterIteration.current32(this.fText));
            if (e == null) {
                this.fText.setIndex(result);
                return result;
            }
            final Stack<Integer> breaks = new Stack<Integer>();
            e.findBreaks(this.fText, startPos, result, false, this.getBreakType(), breaks);
            final int breaksSize = breaks.size();
            (this.fCachedBreakPositions = new int[breaksSize + 2])[0] = startPos;
            for (int i = 0; i < breaksSize; ++i) {
                this.fCachedBreakPositions[i + 1] = breaks.elementAt(i);
            }
            this.fCachedBreakPositions[breaksSize + 1] = result;
            this.fPositionInCache = 0;
        }
        if (this.fCachedBreakPositions != null) {
            ++this.fPositionInCache;
            this.fText.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
            return this.fCachedBreakPositions[this.fPositionInCache];
        }
        Assert.assrt(false);
        return -1;
    }
    
    private int handleNext(final short[] stateTable) {
        if (RuleBasedBreakIterator.TRACE) {
            System.out.println("Handle Next   pos      char  state category");
        }
        this.fLastStatusIndexValid = true;
        this.fLastRuleStatusIndex = 0;
        final CharacterIterator text = this.fText;
        final CharTrie trie = this.fRData.fTrie;
        int c = text.current();
        if (c >= 55296) {
            c = CharacterIteration.nextTrail32(text, c);
            if (c == Integer.MAX_VALUE) {
                return -1;
            }
        }
        int result;
        final int initialPosition = result = text.getIndex();
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        short category = 3;
        final short flagsState = stateTable[5];
        int mode = 1;
        if ((flagsState & 0x2) != 0x0) {
            category = 2;
            mode = 0;
            if (RuleBasedBreakIterator.TRACE) {
                System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                System.out.print(RBBIDataWrapper.intToHexString(c, 10));
                System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
            }
        }
        int lookaheadStatus = 0;
        int lookaheadTagIdx = 0;
        int lookaheadResult = 0;
        while (state != 0) {
            if (c == Integer.MAX_VALUE) {
                if (mode == 2) {
                    if (lookaheadResult > result) {
                        result = lookaheadResult;
                        this.fLastRuleStatusIndex = lookaheadTagIdx;
                        break;
                    }
                    break;
                }
                else {
                    mode = 2;
                    category = 1;
                }
            }
            else if (mode == 1) {
                category = (short)trie.getCodePointValue(c);
                if ((category & 0x4000) != 0x0) {
                    ++this.fDictionaryCharCount;
                    category &= 0xFFFFBFFF;
                }
                if (RuleBasedBreakIterator.TRACE) {
                    System.out.print("            " + RBBIDataWrapper.intToString(text.getIndex(), 5));
                    System.out.print(RBBIDataWrapper.intToHexString(c, 10));
                    System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
                }
                c = text.next();
                if (c >= 55296) {
                    c = CharacterIteration.nextTrail32(text, c);
                }
            }
            else {
                mode = 1;
            }
            state = stateTable[row + 4 + category];
            row = this.fRData.getRowIndex(state);
            if (stateTable[row + 0] == -1) {
                result = text.getIndex();
                if (c >= 65536 && c <= 1114111) {
                    --result;
                }
                this.fLastRuleStatusIndex = stateTable[row + 2];
            }
            if (stateTable[row + 1] != 0) {
                if (lookaheadStatus != 0 && stateTable[row + 0] == lookaheadStatus) {
                    result = lookaheadResult;
                    this.fLastRuleStatusIndex = lookaheadTagIdx;
                    lookaheadStatus = 0;
                    if ((flagsState & 0x1) != 0x0) {
                        text.setIndex(result);
                        return result;
                    }
                    continue;
                }
                else {
                    lookaheadResult = text.getIndex();
                    if (c >= 65536 && c <= 1114111) {
                        --lookaheadResult;
                    }
                    lookaheadStatus = stateTable[row + 1];
                    lookaheadTagIdx = stateTable[row + 2];
                }
            }
            else {
                if (stateTable[row + 0] == 0) {
                    continue;
                }
                lookaheadStatus = 0;
            }
        }
        if (result == initialPosition) {
            if (RuleBasedBreakIterator.TRACE) {
                System.out.println("Iterator did not move. Advancing by 1.");
            }
            text.setIndex(initialPosition);
            CharacterIteration.next32(text);
            result = text.getIndex();
        }
        else {
            text.setIndex(result);
        }
        if (RuleBasedBreakIterator.TRACE) {
            System.out.println("result = " + result);
        }
        return result;
    }
    
    private int handlePrevious(final short[] stateTable) {
        if (this.fText == null || stateTable == null) {
            return 0;
        }
        int category = 0;
        int lookaheadStatus = 0;
        int result = 0;
        int initialPosition = 0;
        int lookaheadResult = 0;
        final boolean lookAheadHardBreak = (stateTable[5] & 0x1) != 0x0;
        this.fLastStatusIndexValid = false;
        this.fLastRuleStatusIndex = 0;
        initialPosition = (result = this.fText.getIndex());
        int c = CharacterIteration.previous32(this.fText);
        int state = 1;
        int row = this.fRData.getRowIndex(state);
        category = 3;
        int mode = 1;
        if ((stateTable[5] & 0x2) != 0x0) {
            category = 2;
            mode = 0;
        }
        if (RuleBasedBreakIterator.TRACE) {
            System.out.println("Handle Prev   pos   char  state category ");
        }
        while (true) {
            if (c == Integer.MAX_VALUE) {
                if (mode == 2 || this.fRData.fHeader.fVersion == 1) {
                    if (lookaheadResult < result) {
                        result = lookaheadResult;
                        lookaheadStatus = 0;
                        break;
                    }
                    if (result == initialPosition) {
                        this.fText.setIndex(initialPosition);
                        CharacterIteration.previous32(this.fText);
                        break;
                    }
                    break;
                }
                else {
                    mode = 2;
                    category = 1;
                }
            }
            if (mode == 1) {
                category = (short)this.fRData.fTrie.getCodePointValue(c);
                if ((category & 0x4000) != 0x0) {
                    ++this.fDictionaryCharCount;
                    category &= 0xFFFFBFFF;
                }
            }
            if (RuleBasedBreakIterator.TRACE) {
                System.out.print("             " + this.fText.getIndex() + "   ");
                if (32 <= c && c < 127) {
                    System.out.print("  " + c + "  ");
                }
                else {
                    System.out.print(" " + Integer.toHexString(c) + " ");
                }
                System.out.println(" " + state + "  " + category + " ");
            }
            state = stateTable[row + 4 + category];
            row = this.fRData.getRowIndex(state);
            if (stateTable[row + 0] == -1) {
                result = this.fText.getIndex();
            }
            if (stateTable[row + 1] != 0) {
                if (lookaheadStatus != 0 && stateTable[row + 0] == lookaheadStatus) {
                    result = lookaheadResult;
                    lookaheadStatus = 0;
                    if (lookAheadHardBreak) {
                        break;
                    }
                }
                else {
                    lookaheadResult = this.fText.getIndex();
                    lookaheadStatus = stateTable[row + 1];
                }
            }
            else if (stateTable[row + 0] != 0 && !lookAheadHardBreak) {
                lookaheadStatus = 0;
            }
            if (state == 0) {
                break;
            }
            if (mode == 1) {
                c = CharacterIteration.previous32(this.fText);
            }
            else {
                if (mode != 0) {
                    continue;
                }
                mode = 1;
            }
        }
        if (result == initialPosition) {
            result = this.fText.setIndex(initialPosition);
            CharacterIteration.previous32(this.fText);
            result = this.fText.getIndex();
        }
        this.fText.setIndex(result);
        if (RuleBasedBreakIterator.TRACE) {
            System.out.println("Result = " + result);
        }
        return result;
    }
    
    static {
        TRACE = (ICUDebug.enabled("rbbi") && ICUDebug.value("rbbi").indexOf("trace") >= 0);
        fDebugEnv = (ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null);
    }
}
