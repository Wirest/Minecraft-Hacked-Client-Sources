// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharacterIteration;
import java.text.StringCharacterIterator;
import java.util.Stack;
import java.text.CharacterIterator;
import java.io.IOException;

class CjkBreakEngine implements LanguageBreakEngine
{
    private static final UnicodeSet fHangulWordSet;
    private static final UnicodeSet fHanWordSet;
    private static final UnicodeSet fKatakanaWordSet;
    private static final UnicodeSet fHiraganaWordSet;
    private final UnicodeSet fWordSet;
    private DictionaryMatcher fDictionary;
    private static final int kMaxKatakanaLength = 8;
    private static final int kMaxKatakanaGroupLength = 20;
    private static final int maxSnlp = 255;
    private static final int kint32max = Integer.MAX_VALUE;
    
    public CjkBreakEngine(final boolean korean) throws IOException {
        this.fDictionary = null;
        this.fDictionary = DictionaryData.loadDictionaryFor("Hira");
        if (korean) {
            this.fWordSet = CjkBreakEngine.fHangulWordSet;
        }
        else {
            (this.fWordSet = new UnicodeSet()).addAll(CjkBreakEngine.fHanWordSet);
            this.fWordSet.addAll(CjkBreakEngine.fKatakanaWordSet);
            this.fWordSet.addAll(CjkBreakEngine.fHiraganaWordSet);
            this.fWordSet.add("\\uff70\\u30fc");
        }
    }
    
    public boolean handles(final int c, final int breakType) {
        return breakType == 1 && this.fWordSet.contains(c);
    }
    
    private static int getKatakanaCost(final int wordlength) {
        final int[] katakanaCost = { 8192, 984, 408, 240, 204, 252, 300, 372, 480 };
        return (wordlength > 8) ? 8192 : katakanaCost[wordlength];
    }
    
    private static boolean isKatakana(final int value) {
        return (value >= 12449 && value <= 12542 && value != 12539) || (value >= 65382 && value <= 65439);
    }
    
    public int findBreaks(final CharacterIterator inText, final int startPos, final int endPos, final boolean reverse, final int breakType, final Stack<Integer> foundBreaks) {
        if (startPos >= endPos) {
            return 0;
        }
        inText.setIndex(startPos);
        final int inputLength = endPos - startPos;
        int[] charPositions = new int[inputLength + 1];
        final StringBuffer s = new StringBuffer("");
        inText.setIndex(startPos);
        while (inText.getIndex() < endPos) {
            s.append(inText.current());
            inText.next();
        }
        final String prenormstr = s.toString();
        final boolean isNormalized = Normalizer.quickCheck(prenormstr, Normalizer.NFKC) == Normalizer.YES || Normalizer.isNormalized(prenormstr, Normalizer.NFKC, 0);
        CharacterIterator text = inText;
        int numChars = 0;
        if (isNormalized) {
            int index = 0;
            charPositions[0] = 0;
            while (index < prenormstr.length()) {
                final int codepoint = prenormstr.codePointAt(index);
                index += Character.charCount(codepoint);
                ++numChars;
                charPositions[numChars] = index;
            }
        }
        else {
            final String normStr = Normalizer.normalize(prenormstr, Normalizer.NFKC);
            text = new StringCharacterIterator(normStr);
            charPositions = new int[normStr.length() + 1];
            final Normalizer normalizer = new Normalizer(prenormstr, Normalizer.NFKC, 0);
            int index2 = 0;
            charPositions[0] = 0;
            while (index2 < normalizer.endIndex()) {
                normalizer.next();
                ++numChars;
                index2 = normalizer.getIndex();
                charPositions[numChars] = index2;
            }
        }
        final int[] bestSnlp = new int[numChars + 1];
        bestSnlp[0] = 0;
        for (int i = 1; i <= numChars; ++i) {
            bestSnlp[i] = Integer.MAX_VALUE;
        }
        final int[] prev = new int[numChars + 1];
        for (int j = 0; j <= numChars; ++j) {
            prev[j] = -1;
        }
        final int maxWordSize = 20;
        final int[] values = new int[numChars];
        final int[] lengths = new int[numChars];
        boolean is_prev_katakana = false;
        for (int k = 0; k < numChars; ++k) {
            text.setIndex(k);
            if (bestSnlp[k] != Integer.MAX_VALUE) {
                final int maxSearchLength = (k + 20 < numChars) ? 20 : (numChars - k);
                final int[] count_ = { 0 };
                this.fDictionary.matches(text, maxSearchLength, lengths, count_, maxSearchLength, values);
                int count = count_[0];
                if ((count == 0 || lengths[0] != 1) && CharacterIteration.current32(text) != Integer.MAX_VALUE && !CjkBreakEngine.fHangulWordSet.contains(CharacterIteration.current32(text))) {
                    values[count] = 255;
                    lengths[count] = 1;
                    ++count;
                }
                for (int l = 0; l < count; ++l) {
                    final int newSnlp = bestSnlp[k] + values[l];
                    if (newSnlp < bestSnlp[lengths[l] + k]) {
                        bestSnlp[lengths[l] + k] = newSnlp;
                        prev[lengths[l] + k] = k;
                    }
                }
                text.setIndex(k);
                final boolean is_katakana = isKatakana(CharacterIteration.current32(text));
                if (!is_prev_katakana && is_katakana) {
                    int m = k + 1;
                    CharacterIteration.next32(text);
                    while (m < numChars && m - k < 20 && isKatakana(CharacterIteration.current32(text))) {
                        CharacterIteration.next32(text);
                        ++m;
                    }
                    if (m - k < 20) {
                        final int newSnlp2 = bestSnlp[k] + getKatakanaCost(m - k);
                        if (newSnlp2 < bestSnlp[m]) {
                            bestSnlp[m] = newSnlp2;
                            prev[m] = k;
                        }
                    }
                }
                is_prev_katakana = is_katakana;
            }
        }
        final int[] t_boundary = new int[numChars + 1];
        int numBreaks = 0;
        if (bestSnlp[numChars] == Integer.MAX_VALUE) {
            t_boundary[numBreaks] = numChars;
            ++numBreaks;
        }
        else {
            for (int i2 = numChars; i2 > 0; i2 = prev[i2]) {
                t_boundary[numBreaks] = i2;
                ++numBreaks;
            }
            Assert.assrt(prev[t_boundary[numBreaks - 1]] == 0);
        }
        if (foundBreaks.size() == 0 || foundBreaks.peek() < startPos) {
            t_boundary[numBreaks++] = 0;
        }
        for (int i2 = numBreaks - 1; i2 >= 0; --i2) {
            final int pos = charPositions[t_boundary[i2]] + startPos;
            if (!foundBreaks.contains(pos) && pos != startPos) {
                foundBreaks.push(charPositions[t_boundary[i2]] + startPos);
            }
        }
        if (!foundBreaks.empty() && foundBreaks.peek() == endPos) {
            foundBreaks.pop();
        }
        if (!foundBreaks.empty()) {
            inText.setIndex(foundBreaks.peek());
        }
        return 0;
    }
    
    static {
        fHangulWordSet = new UnicodeSet();
        fHanWordSet = new UnicodeSet();
        fKatakanaWordSet = new UnicodeSet();
        fHiraganaWordSet = new UnicodeSet();
        CjkBreakEngine.fHangulWordSet.applyPattern("[\\uac00-\\ud7a3]");
        CjkBreakEngine.fHanWordSet.applyPattern("[:Han:]");
        CjkBreakEngine.fKatakanaWordSet.applyPattern("[[:Katakana:]\\uff9e\\uff9f]");
        CjkBreakEngine.fHiraganaWordSet.applyPattern("[:Hiragana:]");
        CjkBreakEngine.fHangulWordSet.freeze();
        CjkBreakEngine.fHanWordSet.freeze();
        CjkBreakEngine.fKatakanaWordSet.freeze();
        CjkBreakEngine.fHiraganaWordSet.freeze();
    }
}
