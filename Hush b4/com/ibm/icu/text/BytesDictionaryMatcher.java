// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.util.BytesTrie;
import java.text.CharacterIterator;
import com.ibm.icu.impl.Assert;

class BytesDictionaryMatcher extends DictionaryMatcher
{
    private final byte[] characters;
    private final int transform;
    
    public BytesDictionaryMatcher(final byte[] chars, final int transform) {
        this.characters = chars;
        Assert.assrt((transform & 0x7F000000) == 0x1000000);
        this.transform = transform;
    }
    
    private int transform(final int c) {
        if (c == 8205) {
            return 255;
        }
        if (c == 8204) {
            return 254;
        }
        final int delta = c - (this.transform & 0x1FFFFF);
        if (delta < 0 || 253 < delta) {
            return -1;
        }
        return delta;
    }
    
    @Override
    public int matches(final CharacterIterator text_, final int maxLength, final int[] lengths, final int[] count_, final int limit, final int[] values) {
        final UCharacterIterator text = UCharacterIterator.getInstance(text_);
        final BytesTrie bt = new BytesTrie(this.characters, 0);
        int c = text.nextCodePoint();
        BytesTrie.Result result = bt.first(this.transform(c));
        int numChars = 1;
        int count = 0;
        while (true) {
            if (result.hasValue()) {
                if (count < limit) {
                    if (values != null) {
                        values[count] = bt.getValue();
                    }
                    lengths[count] = numChars;
                    ++count;
                }
                if (result == BytesTrie.Result.FINAL_VALUE) {
                    break;
                }
            }
            else if (result == BytesTrie.Result.NO_MATCH) {
                break;
            }
            if (numChars >= maxLength) {
                break;
            }
            c = text.nextCodePoint();
            ++numChars;
            result = bt.next(this.transform(c));
        }
        count_[0] = count;
        return numChars;
    }
    
    @Override
    public int getType() {
        return 0;
    }
}
