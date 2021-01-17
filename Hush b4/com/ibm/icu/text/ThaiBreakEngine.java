// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Stack;
import java.text.CharacterIterator;
import com.ibm.icu.lang.UCharacter;
import java.io.IOException;

class ThaiBreakEngine implements LanguageBreakEngine
{
    private static final byte THAI_LOOKAHEAD = 3;
    private static final byte THAI_ROOT_COMBINE_THRESHOLD = 3;
    private static final byte THAI_PREFIX_COMBINE_THRESHOLD = 3;
    private static final char THAI_PAIYANNOI = '\u0e2f';
    private static final char THAI_MAIYAMOK = '\u0e46';
    private static final byte THAI_MIN_WORD = 2;
    private DictionaryMatcher fDictionary;
    private static UnicodeSet fThaiWordSet;
    private static UnicodeSet fEndWordSet;
    private static UnicodeSet fBeginWordSet;
    private static UnicodeSet fSuffixSet;
    private static UnicodeSet fMarkSet;
    
    public ThaiBreakEngine() throws IOException {
        this.fDictionary = DictionaryData.loadDictionaryFor("Thai");
    }
    
    public boolean handles(final int c, final int breakType) {
        if (breakType == 1 || breakType == 2) {
            final int script = UCharacter.getIntPropertyValue(c, 4106);
            return script == 38;
        }
        return false;
    }
    
    public int findBreaks(final CharacterIterator fIter, final int rangeStart, final int rangeEnd, final boolean reverse, final int breakType, final Stack<Integer> foundBreaks) {
        if (rangeEnd - rangeStart < 2) {
            return 0;
        }
        int wordsFound = 0;
        final PossibleWord[] words = new PossibleWord[3];
        for (int i = 0; i < 3; ++i) {
            words[i] = new PossibleWord();
        }
        fIter.setIndex(rangeStart);
        int current;
        while ((current = fIter.getIndex()) < rangeEnd) {
            int wordLength = 0;
            final int candidates = words[wordsFound % 3].candidates(fIter, this.fDictionary, rangeEnd);
            if (candidates == 1) {
                wordLength = words[wordsFound % 3].acceptMarked(fIter);
                ++wordsFound;
            }
            else if (candidates > 1) {
                boolean foundBest = false;
                if (fIter.getIndex() < rangeEnd) {
                Label_0240:
                    do {
                        int wordsMatched = 1;
                        if (words[(wordsFound + 1) % 3].candidates(fIter, this.fDictionary, rangeEnd) > 0) {
                            if (wordsMatched < 2) {
                                words[wordsFound % 3].markCurrent();
                                wordsMatched = 2;
                            }
                            if (fIter.getIndex() >= rangeEnd) {
                                break;
                            }
                            while (words[(wordsFound + 2) % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0) {
                                if (!words[(wordsFound + 1) % 3].backUp(fIter)) {
                                    continue Label_0240;
                                }
                            }
                            words[wordsFound % 3].markCurrent();
                            foundBest = true;
                        }
                    } while (words[wordsFound % 3].backUp(fIter) && !foundBest);
                }
                wordLength = words[wordsFound % 3].acceptMarked(fIter);
                ++wordsFound;
            }
            if (fIter.getIndex() < rangeEnd && wordLength < 3) {
                if (words[wordsFound % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0 && (wordLength == 0 || words[wordsFound % 3].longestPrefix() < 3)) {
                    int remaining = rangeEnd - (current + wordLength);
                    int pc = fIter.current();
                    int chars = 0;
                    while (true) {
                        fIter.next();
                        final int uc = fIter.current();
                        ++chars;
                        if (--remaining <= 0) {
                            break;
                        }
                        if (ThaiBreakEngine.fEndWordSet.contains(pc) && ThaiBreakEngine.fBeginWordSet.contains(uc)) {
                            final int candidate = words[(wordsFound + 1) % 3].candidates(fIter, this.fDictionary, rangeEnd);
                            fIter.setIndex(current + wordLength + chars);
                            if (candidate > 0) {
                                break;
                            }
                        }
                        pc = uc;
                    }
                    if (wordLength <= 0) {
                        ++wordsFound;
                    }
                    wordLength += chars;
                }
                else {
                    fIter.setIndex(current + wordLength);
                }
            }
            int currPos;
            while ((currPos = fIter.getIndex()) < rangeEnd && ThaiBreakEngine.fMarkSet.contains(fIter.current())) {
                fIter.next();
                wordLength += fIter.getIndex() - currPos;
            }
            if (fIter.getIndex() < rangeEnd && wordLength > 0) {
                int uc;
                if (words[wordsFound % 3].candidates(fIter, this.fDictionary, rangeEnd) <= 0 && ThaiBreakEngine.fSuffixSet.contains(uc = fIter.current())) {
                    if (uc == 3631) {
                        if (!ThaiBreakEngine.fSuffixSet.contains(fIter.previous())) {
                            fIter.next();
                            fIter.next();
                            ++wordLength;
                            uc = fIter.current();
                        }
                        else {
                            fIter.next();
                        }
                    }
                    if (uc == 3654) {
                        if (fIter.previous() != '\u0e46') {
                            fIter.next();
                            fIter.next();
                            ++wordLength;
                        }
                        else {
                            fIter.next();
                        }
                    }
                }
                else {
                    fIter.setIndex(current + wordLength);
                }
            }
            if (wordLength > 0) {
                foundBreaks.push(current + wordLength);
            }
        }
        if (foundBreaks.peek() >= rangeEnd) {
            foundBreaks.pop();
            --wordsFound;
        }
        return wordsFound;
    }
    
    static {
        ThaiBreakEngine.fThaiWordSet = new UnicodeSet();
        ThaiBreakEngine.fMarkSet = new UnicodeSet();
        ThaiBreakEngine.fEndWordSet = new UnicodeSet();
        ThaiBreakEngine.fBeginWordSet = new UnicodeSet();
        ThaiBreakEngine.fSuffixSet = new UnicodeSet();
        ThaiBreakEngine.fThaiWordSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]]");
        ThaiBreakEngine.fThaiWordSet.compact();
        ThaiBreakEngine.fMarkSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]&[:M:]]");
        ThaiBreakEngine.fMarkSet.add(32);
        (ThaiBreakEngine.fEndWordSet = ThaiBreakEngine.fThaiWordSet).remove(3633);
        ThaiBreakEngine.fEndWordSet.remove(3648, 3652);
        ThaiBreakEngine.fBeginWordSet.add(3585, 3630);
        ThaiBreakEngine.fBeginWordSet.add(3648, 3652);
        ThaiBreakEngine.fSuffixSet.add(3631);
        ThaiBreakEngine.fSuffixSet.add(3654);
        ThaiBreakEngine.fMarkSet.compact();
        ThaiBreakEngine.fEndWordSet.compact();
        ThaiBreakEngine.fBeginWordSet.compact();
        ThaiBreakEngine.fSuffixSet.compact();
        ThaiBreakEngine.fThaiWordSet.freeze();
        ThaiBreakEngine.fMarkSet.freeze();
        ThaiBreakEngine.fEndWordSet.freeze();
        ThaiBreakEngine.fBeginWordSet.freeze();
        ThaiBreakEngine.fSuffixSet.freeze();
    }
    
    static class PossibleWord
    {
        private static final int POSSIBLE_WORD_LIST_MAX = 20;
        private int[] lengths;
        private int[] count;
        private int prefix;
        private int offset;
        private int mark;
        private int current;
        
        public PossibleWord() {
            this.lengths = new int[20];
            this.count = new int[1];
            this.offset = -1;
        }
        
        public int candidates(final CharacterIterator fIter, final DictionaryMatcher dict, final int rangeEnd) {
            final int start = fIter.getIndex();
            if (start != this.offset) {
                this.offset = start;
                this.prefix = dict.matches(fIter, rangeEnd - start, this.lengths, this.count, this.lengths.length);
                if (this.count[0] <= 0) {
                    fIter.setIndex(start);
                }
            }
            if (this.count[0] > 0) {
                fIter.setIndex(start + this.lengths[this.count[0] - 1]);
            }
            this.current = this.count[0] - 1;
            this.mark = this.current;
            return this.count[0];
        }
        
        public int acceptMarked(final CharacterIterator fIter) {
            fIter.setIndex(this.offset + this.lengths[this.mark]);
            return this.lengths[this.mark];
        }
        
        public boolean backUp(final CharacterIterator fIter) {
            if (this.current > 0) {
                final int offset = this.offset;
                final int[] lengths = this.lengths;
                final int current = this.current - 1;
                this.current = current;
                fIter.setIndex(offset + lengths[current]);
                return true;
            }
            return false;
        }
        
        public int longestPrefix() {
            return this.prefix;
        }
        
        public void markCurrent() {
            this.mark = this.current;
        }
    }
}
