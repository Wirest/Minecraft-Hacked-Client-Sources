// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

abstract class CharsetRecog_Unicode extends CharsetRecognizer
{
    @Override
    abstract String getName();
    
    @Override
    abstract CharsetMatch match(final CharsetDetector p0);
    
    static class CharsetRecog_UTF_16_BE extends CharsetRecog_Unicode
    {
        @Override
        String getName() {
            return "UTF-16BE";
        }
        
        @Override
        CharsetMatch match(final CharsetDetector det) {
            final byte[] input = det.fRawInput;
            if (input.length >= 2 && (input[0] & 0xFF) == 0xFE && (input[1] & 0xFF) == 0xFF) {
                final int confidence = 100;
                return new CharsetMatch(det, this, confidence);
            }
            return null;
        }
    }
    
    static class CharsetRecog_UTF_16_LE extends CharsetRecog_Unicode
    {
        @Override
        String getName() {
            return "UTF-16LE";
        }
        
        @Override
        CharsetMatch match(final CharsetDetector det) {
            final byte[] input = det.fRawInput;
            if (input.length < 2 || (input[0] & 0xFF) != 0xFF || (input[1] & 0xFF) != 0xFE) {
                return null;
            }
            if (input.length >= 4 && input[2] == 0 && input[3] == 0) {
                return null;
            }
            final int confidence = 100;
            return new CharsetMatch(det, this, confidence);
        }
    }
    
    abstract static class CharsetRecog_UTF_32 extends CharsetRecog_Unicode
    {
        abstract int getChar(final byte[] p0, final int p1);
        
        @Override
        abstract String getName();
        
        @Override
        CharsetMatch match(final CharsetDetector det) {
            final byte[] input = det.fRawInput;
            final int limit = det.fRawLength / 4 * 4;
            int numValid = 0;
            int numInvalid = 0;
            boolean hasBOM = false;
            int confidence = 0;
            if (limit == 0) {
                return null;
            }
            if (this.getChar(input, 0) == 65279) {
                hasBOM = true;
            }
            for (int i = 0; i < limit; i += 4) {
                final int ch = this.getChar(input, i);
                if (ch < 0 || ch >= 1114111 || (ch >= 55296 && ch <= 57343)) {
                    ++numInvalid;
                }
                else {
                    ++numValid;
                }
            }
            if (hasBOM && numInvalid == 0) {
                confidence = 100;
            }
            else if (hasBOM && numValid > numInvalid * 10) {
                confidence = 80;
            }
            else if (numValid > 3 && numInvalid == 0) {
                confidence = 100;
            }
            else if (numValid > 0 && numInvalid == 0) {
                confidence = 80;
            }
            else if (numValid > numInvalid * 10) {
                confidence = 25;
            }
            return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
        }
    }
    
    static class CharsetRecog_UTF_32_BE extends CharsetRecog_UTF_32
    {
        @Override
        int getChar(final byte[] input, final int index) {
            return (input[index + 0] & 0xFF) << 24 | (input[index + 1] & 0xFF) << 16 | (input[index + 2] & 0xFF) << 8 | (input[index + 3] & 0xFF);
        }
        
        @Override
        String getName() {
            return "UTF-32BE";
        }
    }
    
    static class CharsetRecog_UTF_32_LE extends CharsetRecog_UTF_32
    {
        @Override
        int getChar(final byte[] input, final int index) {
            return (input[index + 3] & 0xFF) << 24 | (input[index + 2] & 0xFF) << 16 | (input[index + 1] & 0xFF) << 8 | (input[index + 0] & 0xFF);
        }
        
        @Override
        String getName() {
            return "UTF-32LE";
        }
    }
}
