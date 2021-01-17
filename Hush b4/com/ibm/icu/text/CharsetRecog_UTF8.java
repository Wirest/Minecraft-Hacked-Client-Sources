// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

class CharsetRecog_UTF8 extends CharsetRecognizer
{
    @Override
    String getName() {
        return "UTF-8";
    }
    
    @Override
    CharsetMatch match(final CharsetDetector det) {
        boolean hasBOM = false;
        int numValid = 0;
        int numInvalid = 0;
        final byte[] input = det.fRawInput;
        int trailBytes = 0;
        if (det.fRawLength >= 3 && (input[0] & 0xFF) == 0xEF && (input[1] & 0xFF) == 0xBB && (input[2] & 0xFF) == 0xBF) {
            hasBOM = true;
        }
        for (int i = 0; i < det.fRawLength; ++i) {
            int b = input[i];
            if ((b & 0x80) != 0x0) {
                if ((b & 0xE0) == 0xC0) {
                    trailBytes = 1;
                }
                else if ((b & 0xF0) == 0xE0) {
                    trailBytes = 2;
                }
                else if ((b & 0xF8) == 0xF0) {
                    trailBytes = 3;
                }
                else {
                    if (++numInvalid > 5) {
                        break;
                    }
                    trailBytes = 0;
                }
                while (++i < det.fRawLength) {
                    b = input[i];
                    if ((b & 0xC0) != 0x80) {
                        ++numInvalid;
                        break;
                    }
                    if (--trailBytes == 0) {
                        ++numValid;
                        break;
                    }
                }
            }
        }
        int confidence = 0;
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
        else if (numValid == 0 && numInvalid == 0) {
            confidence = 10;
        }
        else if (numValid > numInvalid * 10) {
            confidence = 25;
        }
        return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
    }
}
