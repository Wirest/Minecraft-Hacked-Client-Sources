// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Utf8
{
    public static int encodedLength(final CharSequence sequence) {
        int utf8Length;
        int utf16Length;
        int i;
        for (utf16Length = (utf8Length = sequence.length()), i = 0; i < utf16Length && sequence.charAt(i) < '\u0080'; ++i) {}
        while (i < utf16Length) {
            final char c = sequence.charAt(i);
            if (c >= '\u0800') {
                utf8Length += encodedLengthGeneral(sequence, i);
                break;
            }
            utf8Length += '\u007f' - c >>> 31;
            ++i;
        }
        if (utf8Length < utf16Length) {
            throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + 4294967296L));
        }
        return utf8Length;
    }
    
    private static int encodedLengthGeneral(final CharSequence sequence, final int start) {
        final int utf16Length = sequence.length();
        int utf8Length = 0;
        for (int i = start; i < utf16Length; ++i) {
            final char c = sequence.charAt(i);
            if (c < '\u0800') {
                utf8Length += '\u007f' - c >>> 31;
            }
            else {
                utf8Length += 2;
                if ('\ud800' <= c && c <= '\udfff') {
                    final int cp = Character.codePointAt(sequence, i);
                    if (cp < 65536) {
                        throw new IllegalArgumentException("Unpaired surrogate at index " + i);
                    }
                    ++i;
                }
            }
        }
        return utf8Length;
    }
    
    public static boolean isWellFormed(final byte[] bytes) {
        return isWellFormed(bytes, 0, bytes.length);
    }
    
    public static boolean isWellFormed(final byte[] bytes, final int off, final int len) {
        final int end = off + len;
        Preconditions.checkPositionIndexes(off, end, bytes.length);
        for (int i = off; i < end; ++i) {
            if (bytes[i] < 0) {
                return isWellFormedSlowPath(bytes, i, end);
            }
        }
        return true;
    }
    
    private static boolean isWellFormedSlowPath(final byte[] bytes, final int off, final int end) {
        int index = off;
        while (index < end) {
            final int byte1;
            if ((byte1 = bytes[index++]) < 0) {
                if (byte1 < -32) {
                    if (index == end) {
                        return false;
                    }
                    if (byte1 < -62 || bytes[index++] > -65) {
                        return false;
                    }
                    continue;
                }
                else if (byte1 < -16) {
                    if (index + 1 >= end) {
                        return false;
                    }
                    final int byte2 = bytes[index++];
                    if (byte2 > -65 || (byte1 == -32 && byte2 < -96) || (byte1 == -19 && -96 <= byte2) || bytes[index++] > -65) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (index + 2 >= end) {
                        return false;
                    }
                    final int byte2 = bytes[index++];
                    if (byte2 > -65 || (byte1 << 28) + (byte2 + 112) >> 30 != 0 || bytes[index++] > -65 || bytes[index++] > -65) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    private Utf8() {
    }
}
