// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.digest;

import java.util.Random;

class B64
{
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    static void b64from24bit(final byte b2, final byte b1, final byte b0, final int outLen, final StringBuilder buffer) {
        int w = (b2 << 16 & 0xFFFFFF) | (b1 << 8 & 0xFFFF) | (b0 & 0xFF);
        int n = outLen;
        while (n-- > 0) {
            buffer.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(w & 0x3F));
            w >>= 6;
        }
    }
    
    static String getRandomSalt(final int num) {
        final StringBuilder saltString = new StringBuilder();
        for (int i = 1; i <= num; ++i) {
            saltString.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(new Random().nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
        }
        return saltString.toString();
    }
}
