// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyVector;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyParsers;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyHash;

public class SVGAttr
{
    private static final int if = 0;
    private static final int else = 1;
    private static TinyHash try;
    private static TinyString new;
    private static TinyString for;
    private static TinyString byte;
    private static TinyString do;
    private static TinyString int;
    private static TinyString long;
    TinyParsers char;
    protected int case;
    protected int a;
    protected int goto;
    
    public SVGAttr() {
        this(0, 0);
    }
    
    public SVGAttr(final int a, final int goto1) {
        this.a = a;
        this.goto = goto1;
        this.char = new TinyParsers();
    }
    
    public Object attributeValue(final int n, final int n2, final char[] array, final int n3, final int n4) {
        int n5 = 0;
        Object o = null;
        final int index = TinyString.getIndex(SVG.VALUES, array, n3, n4);
        switch (n2) {
            case 5:
            case 9:
            case 21:
            case 42:
            case 102: {
                n5 = TinyNumber.parseInt(array, 0, n4, 10);
                break;
            }
            case 100: {
                n5 = array[0];
                break;
            }
            case 105: {
                o = this.a(array, n4);
                break;
            }
            case 1:
            case 2:
            case 13:
            case 22:
            case 27:
            case 39:
            case 64:
            case 70:
            case 75:
            case 85:
            case 86:
            case 92:
            case 95:
            case 106:
            case 122:
            case 126: {
                n5 = index;
                break;
            }
            case 8:
            case 12:
            case 28:
            case 34:
            case 44:
            case 68:
            case 69:
            case 90:
            case 93:
            case 104:
            case 115: {
                o = new TinyString(array, n3, n4);
                break;
            }
            case 63: {
                o = this.char.parsePoints(array, n4);
                break;
            }
            case 18:
            case 19:
            case 65:
            case 72:
            case 73:
            case 109:
            case 111:
            case 112:
            case 123:
            case 124:
            case 125: {
                n5 = TinyNumber.parseFix(array, n3, n4);
                break;
            }
            case 107: {
                if (n == 30) {
                    n5 = this.a(0, array, n3, n4);
                }
                else {
                    n5 = TinyNumber.parseFix(array, n3, n4);
                }
                break;
            }
            case 41: {
                if (n == 30) {
                    n5 = this.a(1, array, n3, n4);
                }
                else {
                    n5 = TinyNumber.parseFix(array, n3, n4);
                }
                break;
            }
            case 20: {
                if (n == 15 || n == 21) {
                    o = this.char.parsePath(array, n4, true);
                }
                else {
                    o = this.char.parsePath(array, n4, false);
                }
                break;
            }
            case 15:
            case 78:
            case 82: {
                o = this.int(array, n3, n4);
                break;
            }
            case 83: {
                o = this.if(array, n4);
                break;
            }
            case 29:
            case 84:
            case 87:
            case 89: {
                n5 = this.a(array, n3, n4);
                break;
            }
            case 26:
            case 55:
            case 56:
            case 79:
            case 88: {
                n5 = this.a(array, n3, n4);
                if (n5 >= 256) {
                    n5 = 255;
                }
                if (n5 < 0) {
                    n5 = 0;
                }
                break;
            }
            case 16:
            case 31:
            case 33: {
                break;
            }
            case 38:
            case 94: {
                final TinyVector transfroms = this.char.parseTransfroms(array, n4);
                final TinyTransform tinyTransform = new TinyTransform();
                tinyTransform.setMatrix(TinyTransform.getTinyMatrix(transfroms));
                o = tinyTransform;
                break;
            }
            case 25: {
                switch (n) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 28: {
                        n5 = index;
                        break;
                    }
                    default: {
                        o = this.int(array, n3, n4);
                        break;
                    }
                }
                break;
            }
            case 6: {
                n5 = TinyString.getIndex(SVG.ATTRIBUTES, array, n3, n4);
                break;
            }
            case 11:
            case 23:
            case 24:
            case 52:
            case 53:
            case 67: {
                o = this.a(new TinyString(array, n3, n4));
                break;
            }
            case 66: {
                if (index == 25) {
                    n5 = -1;
                }
                else {
                    n5 = TinyNumber.parseFix(array, n3, n4);
                }
                break;
            }
            case 103: {
                o = this.for(array, n3, n4);
                break;
            }
            case 47:
            case 49: {
                o = this.char.parseNumbers(array, n4);
                break;
            }
            case 48: {
                o = this.if(array, n3, n4);
                break;
            }
            case 71: {
                if (index == 10) {
                    n5 = Integer.MIN_VALUE;
                }
                else if (index == 11) {
                    n5 = Integer.MAX_VALUE;
                }
                else {
                    n5 = TinyNumber.parseFix(array, n3, n4);
                }
                break;
            }
            case 61: {
                o = this.char.parsePath(array, n4, false);
                break;
            }
            default: {
                return null;
            }
        }
        if (o != null) {
            return o;
        }
        return new TinyNumber(n5);
    }
    
    private final int a(final char[] array, final int n, final int n2) {
        if (TinyString.getIndex(SVG.VALUES, array, n, n2) == 26) {
            return Integer.MIN_VALUE;
        }
        return TinyNumber.parseFix(array, n, n2);
    }
    
    private final int a(final int n, final char[] array, final int n2, final int n3) {
        this.case = 0;
        final TinyString trim = new TinyString(array, n2, n3).trim();
        int n4;
        if (trim.endsWith(SVGAttr.new)) {
            final int fix = TinyNumber.parseFix(array, n2, n3 - 1);
            if (n == 0) {
                n4 = TinyUtil.div(TinyUtil.mul(this.a, fix), 25600);
            }
            else {
                n4 = TinyUtil.div(TinyUtil.mul(this.goto, fix), 25600);
            }
        }
        else {
            final int fix2 = TinyNumber.parseFix(array, n2, n3 - 2);
            if (trim.endsWith(SVGAttr.for)) {
                n4 = TinyUtil.mul(fix2, 3870);
            }
            else if (trim.endsWith(SVGAttr.byte)) {
                n4 = TinyUtil.mul(fix2, 323);
            }
            else if (trim.endsWith(SVGAttr.do)) {
                n4 = TinyUtil.mul(fix2, 23223);
            }
            else if (trim.endsWith(SVGAttr.int)) {
                n4 = TinyUtil.mul(fix2, 9143);
            }
            else if (trim.endsWith(SVGAttr.long)) {
                n4 = TinyUtil.mul(fix2, 914);
            }
            else {
                n4 = TinyNumber.parseFix(array, n2, n3) >> 8;
            }
        }
        return n4;
    }
    
    private final TinyColor int(final char[] array, int n, int n2) {
        this.case = 0;
        final int index = TinyString.getIndex(SVG.VALUES, array, n, n2);
        if (index == 35) {
            return TinyColor.NONE;
        }
        if (index == 26) {
            return TinyColor.INHERIT;
        }
        if (index == 35) {
            return TinyColor.NONE;
        }
        if (index == 17) {
            return TinyColor.CURRENT;
        }
        final char[] charArray = "url(#".toCharArray();
        final char[] charArray2 = "rgb(".toCharArray();
        if (array == null || n2 < 2) {
            return TinyColor.INHERIT;
        }
        final char[] array2 = new char[32];
        System.arraycopy(array, n, array2, 0, n2);
        n = 0;
        TinyColor tinyColor;
        if (array2[n] == '#') {
            if (n2 == 4) {
                final char c = array2[n + 2];
                final char c2 = array2[n + 3];
                n2 += 3;
                array2[n + 2] = array2[n + 1];
                array2[n + 4] = (array2[n + 3] = c);
                array2[n + 6] = (array2[n + 5] = c2);
            }
            tinyColor = new TinyColor(0xFF000000 | TinyNumber.parseInt(array2, n + 1, n2, 16));
        }
        else if (0 == TinyString.compareTo(array2, n, charArray2.length, charArray2, 0, charArray2.length)) {
            final TinyString tinyString = new TinyString(array2, n + charArray2.length, n2 - charArray2.length - 1);
            tinyColor = this.char.parseRGB(tinyString.data, tinyString.count);
        }
        else if (0 == TinyString.compareTo(array2, n, charArray.length, charArray, 0, charArray.length)) {
            tinyColor = new TinyColor(new TinyString(array2, n + charArray.length, n2 - charArray.length - 1));
        }
        else {
            tinyColor = (TinyColor)SVGAttr.try.get(new TinyString(array2, n, n2));
        }
        if (tinyColor == null) {
            tinyColor = TinyColor.INHERIT;
        }
        return tinyColor;
    }
    
    private final SVGRect a(final char[] array, final int n) {
        this.case = 0;
        final SVGRect svgRect = new SVGRect();
        final TinyRect rect = this.char.parseRect(array, n);
        if (array != null) {
            svgRect.x = rect.xmin;
            svgRect.y = rect.ymin;
            svgRect.width = rect.xmax - rect.xmin;
            svgRect.height = rect.ymax - rect.ymin;
        }
        return svgRect;
    }
    
    private final int[] if(final char[] array, final int n) {
        this.case = 0;
        final int index = TinyString.getIndex(SVG.VALUES, array, 0, n);
        if (index == 26) {
            return SVG.VAL_STROKEDASHARRAYINHERIT;
        }
        if (index == 35) {
            return SVG.VAL_STROKEDASHARRAYNONE;
        }
        final int[] dashArray = this.char.parseDashArray(array, n);
        if (dashArray == null) {
            return SVG.VAL_STROKEDASHARRAYNONE;
        }
        return dashArray;
    }
    
    TinyVector for(final char[] array, final int n, final int n2) {
        final TinyVector tinyVector = new TinyVector(10);
        final a a = new a(new TinyString(array, n, n2), new TinyString(";".toCharArray()), false);
        while (a.do()) {
            final TinyString if1 = a.if();
            if (if1 == null) {
                break;
            }
            tinyVector.addElement(if1);
        }
        return tinyVector;
    }
    
    TinyVector if(final char[] array, final int n, final int n2) {
        final TinyVector tinyVector = new TinyVector(10);
        final a a = new a(new TinyString(array, n, n2), new TinyString(";".toCharArray()), false);
        while (a.do()) {
            final TinyString if1 = a.if();
            if (if1 == null) {
                break;
            }
            tinyVector.addElement(TinyPath.pathToPoints(this.char.parseSpline(if1.data, if1.count)));
        }
        return tinyVector;
    }
    
    SMILTime a(TinyString trim) {
        final SMILTime smilTime = new SMILTime();
        if (trim == null) {
            return smilTime;
        }
        final TinyString tinyString = new TinyString(".begin".toCharArray());
        final TinyString tinyString2 = new TinyString(".end".toCharArray());
        trim = trim.trim();
        if (trim.count == 0) {
            return smilTime;
        }
        final char c = trim.data[0];
        if (c == '-' || c == '+' || (c >= '0' && c <= '9')) {
            smilTime.type = 1;
            this.a(smilTime, trim);
            return smilTime;
        }
        if (0 == TinyString.compareTo(trim.data, 0, trim.count, SVG.VALUES[25], 0, SVG.VALUES[25].length)) {
            smilTime.type = 0;
            smilTime.timeValue = 0;
            return smilTime;
        }
        TinyString tinyString3 = trim;
        final int lastIndex = trim.lastIndexOf(43, trim.count - 1);
        if (lastIndex != -1) {
            tinyString3 = trim.substring(0, lastIndex);
            this.case = this.a(smilTime, trim.substring(lastIndex));
            if (this.case != 0) {
                tinyString3 = trim;
            }
        }
        final int lastIndex2 = trim.lastIndexOf(45, trim.count - 1);
        if (lastIndex2 != -1) {
            tinyString3 = trim.substring(0, lastIndex2);
            this.case = this.a(smilTime, trim.substring(lastIndex2));
            if (this.case != 0) {
                tinyString3 = trim;
            }
        }
        smilTime.type = 2;
        smilTime.timeValue = 0;
        smilTime.idValue = new TinyString(tinyString3.data);
        return smilTime;
    }
    
    int a(final SMILTime smilTime, final TinyString tinyString) {
        final int n = 0;
        if (smilTime == null || tinyString == null) {
            return 2;
        }
        smilTime.offset = this.do(tinyString.data, 0, tinyString.count);
        return n;
    }
    
    int do(final char[] array, final int n, final int n2) {
        final TinyString tinyString = new TinyString("ms".toCharArray());
        final TinyString tinyString2 = new TinyString("s".toCharArray());
        final TinyString trim = new TinyString(array, n, n2).trim();
        int n3;
        if (trim.endsWith(tinyString)) {
            n3 = TinyNumber.parseFix(trim.data, 0, trim.count - 2) / 1000;
        }
        else if (trim.endsWith(tinyString2)) {
            n3 = TinyNumber.parseFix(trim.data, 0, trim.count - 1);
        }
        else {
            n3 = TinyNumber.parseFix(trim.data, 0, trim.count);
        }
        return n3;
    }
    
    static {
        (SVGAttr.try = new TinyHash(1, 17)).put(new TinyString("black".toCharArray()), new TinyColor(-16777216));
        SVGAttr.try.put(new TinyString("silver".toCharArray()), new TinyColor(-4144960));
        SVGAttr.try.put(new TinyString("white".toCharArray()), new TinyColor(-1));
        SVGAttr.try.put(new TinyString("maroon".toCharArray()), new TinyColor(-8388608));
        SVGAttr.try.put(new TinyString("red".toCharArray()), new TinyColor(-65536));
        SVGAttr.try.put(new TinyString("purple".toCharArray()), new TinyColor(-8388480));
        SVGAttr.try.put(new TinyString("fuchsia".toCharArray()), new TinyColor(-65281));
        SVGAttr.try.put(new TinyString("green".toCharArray()), new TinyColor(-16744448));
        SVGAttr.try.put(new TinyString("lime".toCharArray()), new TinyColor(-16711936));
        SVGAttr.try.put(new TinyString("olive".toCharArray()), new TinyColor(-8355840));
        SVGAttr.try.put(new TinyString("yellow".toCharArray()), new TinyColor(-256));
        SVGAttr.try.put(new TinyString("navy".toCharArray()), new TinyColor(-16777088));
        SVGAttr.try.put(new TinyString("blue".toCharArray()), new TinyColor(-16776961));
        SVGAttr.try.put(new TinyString("teal".toCharArray()), new TinyColor(-16744320));
        SVGAttr.try.put(new TinyString("aqua".toCharArray()), new TinyColor(-16711681));
        SVGAttr.new = new TinyString("%".toCharArray());
        SVGAttr.for = new TinyString("pc".toCharArray());
        SVGAttr.byte = new TinyString("pt".toCharArray());
        SVGAttr.do = new TinyString("in".toCharArray());
        SVGAttr.int = new TinyString("cm".toCharArray());
        SVGAttr.long = new TinyString("mm".toCharArray());
    }
}
