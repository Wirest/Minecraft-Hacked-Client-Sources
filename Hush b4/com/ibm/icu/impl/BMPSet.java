// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UnicodeSet;

public final class BMPSet
{
    public static int U16_SURROGATE_OFFSET;
    private boolean[] latin1Contains;
    private int[] table7FF;
    private int[] bmpBlockBits;
    private int[] list4kStarts;
    private final int[] list;
    private final int listLength;
    
    public BMPSet(final int[] parentList, final int parentListLength) {
        this.list = parentList;
        this.listLength = parentListLength;
        this.latin1Contains = new boolean[256];
        this.table7FF = new int[64];
        this.bmpBlockBits = new int[64];
        (this.list4kStarts = new int[18])[0] = this.findCodePoint(2048, 0, this.listLength - 1);
        for (int i = 1; i <= 16; ++i) {
            this.list4kStarts[i] = this.findCodePoint(i << 12, this.list4kStarts[i - 1], this.listLength - 1);
        }
        this.list4kStarts[17] = this.listLength - 1;
        this.initBits();
    }
    
    public BMPSet(final BMPSet otherBMPSet, final int[] newParentList, final int newParentListLength) {
        this.list = newParentList;
        this.listLength = newParentListLength;
        this.latin1Contains = otherBMPSet.latin1Contains.clone();
        this.table7FF = otherBMPSet.table7FF.clone();
        this.bmpBlockBits = otherBMPSet.bmpBlockBits.clone();
        this.list4kStarts = otherBMPSet.list4kStarts.clone();
    }
    
    public boolean contains(final int c) {
        if (c <= 255) {
            return this.latin1Contains[c];
        }
        if (c <= 2047) {
            return (this.table7FF[c & 0x3F] & 1 << (c >> 6)) != 0x0;
        }
        if (c >= 55296 && (c < 57344 || c > 65535)) {
            return c <= 1114111 && this.containsSlow(c, this.list4kStarts[13], this.list4kStarts[17]);
        }
        final int lead = c >> 12;
        final int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
        if (twoBits <= 1) {
            return 0 != twoBits;
        }
        return this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]);
    }
    
    public final int span(final CharSequence s, final int start, final int end, final UnicodeSet.SpanCondition spanCondition) {
        int i = start;
        final int limit = Math.min(s.length(), end);
        if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
            while (i < limit) {
                final char c = s.charAt(i);
                if (c <= '\u00ff') {
                    if (!this.latin1Contains[c]) {
                        break;
                    }
                }
                else if (c <= '\u07ff') {
                    if ((this.table7FF[c & '?'] & 1 << (c >> 6)) == 0x0) {
                        break;
                    }
                }
                else {
                    final char c2;
                    if (c < '\ud800' || c >= '\udc00' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '\udc00' || c2 >= '\ue000') {
                        final int lead = c >> 12;
                        final int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                        if (twoBits <= 1) {
                            if (twoBits == 0) {
                                break;
                            }
                        }
                        else if (!this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                            break;
                        }
                    }
                    else {
                        final int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
                        if (!this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                            break;
                        }
                        ++i;
                    }
                }
                ++i;
            }
        }
        else {
            while (i < limit) {
                final char c = s.charAt(i);
                if (c <= '\u00ff') {
                    if (this.latin1Contains[c]) {
                        break;
                    }
                }
                else if (c <= '\u07ff') {
                    if ((this.table7FF[c & '?'] & 1 << (c >> 6)) != 0x0) {
                        break;
                    }
                }
                else {
                    final char c2;
                    if (c < '\ud800' || c >= '\udc00' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '\udc00' || c2 >= '\ue000') {
                        final int lead = c >> 12;
                        final int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                        if (twoBits <= 1) {
                            if (twoBits != 0) {
                                break;
                            }
                        }
                        else if (this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                            break;
                        }
                    }
                    else {
                        final int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
                        if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                            break;
                        }
                        ++i;
                    }
                }
                ++i;
            }
        }
        return i - start;
    }
    
    public final int spanBack(final CharSequence s, int limit, final UnicodeSet.SpanCondition spanCondition) {
        limit = Math.min(s.length(), limit);
        if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
            do {
                final char c = s.charAt(--limit);
                if (c <= '\u00ff') {
                    if (!this.latin1Contains[c]) {
                        return limit + 1;
                    }
                    continue;
                }
                else if (c <= '\u07ff') {
                    if ((this.table7FF[c & '?'] & 1 << (c >> 6)) == 0x0) {
                        return limit + 1;
                    }
                    continue;
                }
                else {
                    final char c2;
                    if (c < '\ud800' || c < '\udc00' || 0 == limit || (c2 = s.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                        final int lead = c >> 12;
                        final int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                        if (twoBits <= 1) {
                            if (twoBits == 0) {
                                return limit + 1;
                            }
                            continue;
                        }
                        else {
                            if (!this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                                return limit + 1;
                            }
                            continue;
                        }
                    }
                    else {
                        final int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
                        if (!this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                            return limit + 1;
                        }
                        --limit;
                    }
                }
            } while (0 != limit);
            return 0;
        }
        do {
            final char c = s.charAt(--limit);
            if (c <= '\u00ff') {
                if (this.latin1Contains[c]) {
                    return limit + 1;
                }
                continue;
            }
            else if (c <= '\u07ff') {
                if ((this.table7FF[c & '?'] & 1 << (c >> 6)) != 0x0) {
                    return limit + 1;
                }
                continue;
            }
            else {
                final char c2;
                if (c < '\ud800' || c < '\udc00' || 0 == limit || (c2 = s.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                    final int lead = c >> 12;
                    final int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                    if (twoBits <= 1) {
                        if (twoBits != 0) {
                            return limit + 1;
                        }
                        continue;
                    }
                    else {
                        if (this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                            return limit + 1;
                        }
                        continue;
                    }
                }
                else {
                    final int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
                    if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                        return limit + 1;
                    }
                    --limit;
                }
            }
        } while (0 != limit);
        return 0;
    }
    
    private static void set32x64Bits(final int[] table, final int start, final int limit) {
        assert 64 == table.length;
        int lead = start >> 6;
        int trail = start & 0x3F;
        int bits = 1 << lead;
        if (start + 1 == limit) {
            final int n = trail;
            table[n] |= bits;
            return;
        }
        final int limitLead = limit >> 6;
        final int limitTrail = limit & 0x3F;
        if (lead == limitLead) {
            while (trail < limitTrail) {
                final int n2 = trail++;
                table[n2] |= bits;
            }
        }
        else {
            if (trail > 0) {
                do {
                    final int n3 = trail++;
                    table[n3] |= bits;
                } while (trail < 64);
                ++lead;
            }
            if (lead < limitLead) {
                bits = ~((1 << lead) - 1);
                if (limitLead < 32) {
                    bits &= (1 << limitLead) - 1;
                }
                for (trail = 0; trail < 64; ++trail) {
                    final int n4 = trail;
                    table[n4] |= bits;
                }
            }
            bits = 1 << limitLead;
            for (trail = 0; trail < limitTrail; ++trail) {
                final int n5 = trail;
                table[n5] |= bits;
            }
        }
    }
    
    private void initBits() {
        int listIndex = 0;
        int limit;
        int start;
        do {
            start = this.list[listIndex++];
            if (listIndex < this.listLength) {
                limit = this.list[listIndex++];
            }
            else {
                limit = 1114112;
            }
            if (start >= 256) {
                break;
            }
            do {
                this.latin1Contains[start++] = true;
            } while (start < limit && start < 256);
        } while (limit <= 256);
        while (start < 2048) {
            set32x64Bits(this.table7FF, start, (limit <= 2048) ? limit : 2048);
            if (limit > 2048) {
                start = 2048;
                break;
            }
            start = this.list[listIndex++];
            if (listIndex < this.listLength) {
                limit = this.list[listIndex++];
            }
            else {
                limit = 1114112;
            }
        }
        int minStart = 2048;
        while (start < 65536) {
            if (limit > 65536) {
                limit = 65536;
            }
            if (start < minStart) {
                start = minStart;
            }
            if (start < limit) {
                if (0x0 != (start & 0x3F)) {
                    start >>= 6;
                    final int[] bmpBlockBits = this.bmpBlockBits;
                    final int n = start & 0x3F;
                    bmpBlockBits[n] |= 65537 << (start >> 6);
                    start = (minStart = start + 1 << 6);
                }
                if (start < limit) {
                    if (start < (limit & 0xFFFFFFC0)) {
                        set32x64Bits(this.bmpBlockBits, start >> 6, limit >> 6);
                    }
                    if (0x0 != (limit & 0x3F)) {
                        limit >>= 6;
                        final int[] bmpBlockBits2 = this.bmpBlockBits;
                        final int n2 = limit & 0x3F;
                        bmpBlockBits2[n2] |= 65537 << (limit >> 6);
                        limit = (minStart = limit + 1 << 6);
                    }
                }
            }
            if (limit == 65536) {
                break;
            }
            start = this.list[listIndex++];
            if (listIndex < this.listLength) {
                limit = this.list[listIndex++];
            }
            else {
                limit = 1114112;
            }
        }
    }
    
    private int findCodePoint(final int c, int lo, int hi) {
        if (c < this.list[lo]) {
            return lo;
        }
        if (lo >= hi || c >= this.list[hi - 1]) {
            return hi;
        }
        while (true) {
            final int i = lo + hi >>> 1;
            if (i == lo) {
                break;
            }
            if (c < this.list[i]) {
                hi = i;
            }
            else {
                lo = i;
            }
        }
        return hi;
    }
    
    private final boolean containsSlow(final int c, final int lo, final int hi) {
        return 0x0 != (this.findCodePoint(c, lo, hi) & 0x1);
    }
    
    static {
        BMPSet.U16_SURROGATE_OFFSET = 56613888;
    }
}
