// 
// Decompiled by Procyon v0.5.36
// 

package com.google.thirdparty.publicsuffix;

import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class TrieParser
{
    private static final Joiner PREFIX_JOINER;
    
    static ImmutableMap<String, PublicSuffixType> parseTrie(final CharSequence encoded) {
        final ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        for (int encodedLen = encoded.length(), idx = 0; idx < encodedLen; idx += doParseTrieToBuilder((List<CharSequence>)Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder)) {}
        return builder.build();
    }
    
    private static int doParseTrieToBuilder(final List<CharSequence> stack, final CharSequence encoded, final ImmutableMap.Builder<String, PublicSuffixType> builder) {
        final int encodedLen = encoded.length();
        int idx = 0;
        char c = '\0';
        while (idx < encodedLen) {
            c = encoded.charAt(idx);
            if (c == '&' || c == '?' || c == '!' || c == ':') {
                break;
            }
            if (c == ',') {
                break;
            }
            ++idx;
        }
        stack.add(0, reverse(encoded.subSequence(0, idx)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            final String domain = TrieParser.PREFIX_JOINER.join(stack);
            if (domain.length() > 0) {
                builder.put(domain, PublicSuffixType.fromCode(c));
            }
        }
        ++idx;
        if (c != '?' && c != ',') {
            while (idx < encodedLen) {
                idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
                if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
                    ++idx;
                    break;
                }
            }
        }
        stack.remove(0);
        return idx;
    }
    
    private static CharSequence reverse(final CharSequence s) {
        final int length = s.length();
        if (length <= 1) {
            return s;
        }
        final char[] buffer = new char[length];
        buffer[0] = s.charAt(length - 1);
        for (int i = 1; i < length; ++i) {
            buffer[i] = s.charAt(length - 1 - i);
            if (Character.isSurrogatePair(buffer[i], buffer[i - 1])) {
                swap(buffer, i - 1, i);
            }
        }
        return new String(buffer);
    }
    
    private static void swap(final char[] buffer, final int f, final int s) {
        final char tmp = buffer[f];
        buffer[f] = buffer[s];
        buffer[s] = tmp;
    }
    
    static {
        PREFIX_JOINER = Joiner.on("");
    }
}
