/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 */
package delta;

import delta.OVYt;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Class14 {
    private static String[] cooper$;
    private static final char[] roger$;

    public static String _guyana() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(OVYt.968L.FS1x((String)cooper$[1], (int)2121188546));
            String string = System.getProperty(OVYt.968L.FS1x((String)cooper$[2], (int)-1164372455)) + System.getProperty(OVYt.968L.FS1x((String)cooper$[3], (int)2045073769)) + System.getProperty(OVYt.968L.FS1x((String)cooper$[4], (int)36554596)) + Runtime.getRuntime().availableProcessors() + System.getenv(OVYt.968L.FS1x((String)cooper$[5], (int)-1841553578)) + System.getenv(OVYt.968L.FS1x((String)cooper$[6], (int)1931042643)) + System.getenv(OVYt.968L.FS1x((String)cooper$[7], (int)-1688591452)) + System.getenv(OVYt.968L.FS1x((String)cooper$[8], (int)-831772367));
            return Class14._fleece(messageDigest.digest(string.getBytes()));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new Error(OVYt.968L.FS1x((String)cooper$[9], (int)1747220666), noSuchAlgorithmException);
        }
    }

    private static void _textiles() {
        cooper$ = new String[]{"\uf55c\uf55d\uf55e\uf55f\uf558\uf559\uf55a\uf55b\uf554\uf555\uf52d\uf52e\uf52f\uf528\uf529\uf52a", "\uc48f\uc486\uc4f7", "\u1676\u166a\u1637\u1677\u1678\u1674\u167c", "\u5906\u591a\u5947\u5908\u591b\u590a\u5901", "\uc70b\uc717\uc74a\uc712\uc701\uc716\uc717\uc70d\uc70b\uc70a", "\u1f06\u1f04\u1f19\u1f15\u1f13\u1f05\u1f05\u1f19\u1f04\u1f09\u1f1f\u1f12\u1f13\u1f18\u1f02\u1f1f\u1f10\u1f1f\u1f13\u1f04", "\u5f03\u5f01\u5f1c\u5f10\u5f16\u5f00\u5f00\u5f1c\u5f01\u5f0c\u5f12\u5f01\u5f10\u5f1b\u5f1a\u5f07\u5f16\u5f10\u5f07\u5f06\u5f01\u5f16", "\u23f4\u23f6\u23eb\u23e7\u23e1\u23f7\u23f7\u23eb\u23f6\u23fb\u23e5\u23f6\u23e7\u23ec\u23ed\u23f0\u23e1\u23f3\u2392\u2390\u2397\u2396", "\u297f\u2964\u297c\u2973\u2974\u2963\u296e\u297e\u2977\u296e\u2961\u2963\u297e\u2972\u2974\u2962\u2962\u297e\u2963\u2962", "\u78fb\u78d6\u78dd\u78d5\u78c8\u78d3\u78ce\u78d2\u78d7\u789a\u78cd\u78db\u78c9\u78d4\u789d\u78ce\u789a\u78dc\u78d5\u78cf\u78d4\u78de\u7894"};
    }

    private static String _fleece(byte[] arrby) {
        char[] arrc = new char[arrby.length * (30 - 48 + 24 + -4)];
        for (int i = 246 - 388 + 26 + 116; i < arrby.length; ++i) {
            int n = arrby[i] & 72 - 119 + 65 - 11 + 248;
            arrc[i * (104 - 194 + 170 - 44 + -34)] = roger$[n >>> 206 - 330 + 328 + -200];
            arrc[i * (41 - 46 + 22 + -15) + (123 - 153 + 94 + -63)] = roger$[n & 150 - 268 + 168 - 14 + -21];
        }
        return new String(arrc);
    }

    static {
        Class14._textiles();
        roger$ = OVYt.968L.FS1x((String)cooper$[0], (int)1790047596).toCharArray();
    }
}

