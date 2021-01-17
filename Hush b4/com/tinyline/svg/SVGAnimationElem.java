// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyVector;
import com.tinyline.tiny2d.TinyString;

public class SVGAnimationElem extends SVGNode
{
    public TinyString xlink_href;
    public int attributeName;
    public int additive;
    public int accumulate;
    public int calcMode;
    public TinyVector svalues;
    public TinyVector keyTimes;
    public TinyVector keySplines;
    public TinyString sfrom;
    public TinyString sto;
    public TinyString sby;
    public SMILTime begin;
    public SMILTime dur;
    public SMILTime end;
    public SMILTime min;
    public SMILTime max;
    public int restart;
    public int repeatCount;
    public SMILTime repeatDur;
    public int fillType;
    public int transformType;
    public TinyPath path;
    public TinyVector keyPoints;
    public int rotate;
    public int origin;
    public static final int BEGIN_EVENT = 0;
    public static final int END_EVENT = 1;
    public static final int REPEAT_EVENT = 2;
    static char[][] r;
    static final int k = 0;
    static final int h = 1;
    static final int p = 2;
    static final int j = 3;
    static final int m = 4;
    static final int void = 5;
    static final int g = 6;
    static final int n = 7;
    int long;
    SVGNode goto;
    Object i;
    int b;
    Object d;
    TinyVector s;
    Object c;
    Object t;
    Object o;
    static final int f = 0;
    static final int q = 1;
    static final int l = 2;
    static final int e = 3;
    int u;
    int null;
    
    public SVGAnimationElem() {
        this.xlink_href = null;
        this.attributeName = 127;
        this.additive = 46;
        this.accumulate = 35;
        this.calcMode = 30;
        this.svalues = null;
        this.keyTimes = null;
        this.keySplines = null;
        final TinyString sfrom = null;
        this.sby = sfrom;
        this.sto = sfrom;
        this.sfrom = sfrom;
        this.begin = new SMILTime(1, 0);
        this.end = new SMILTime();
        this.dur = new SMILTime();
        this.min = new SMILTime(1, 0);
        this.max = new SMILTime();
        this.restart = 9;
        this.repeatDur = new SMILTime(2, 0);
        this.fillType = 44;
        this.repeatCount = 0;
        this.u = 0;
        this.transformType = 56;
        this.path = null;
        this.keyPoints = null;
        this.rotate = 0;
        this.origin = 18;
    }
    
    public SVGAnimationElem(final SVGAnimationElem svgAnimationElem) {
        super(svgAnimationElem);
        if (svgAnimationElem.xlink_href != null) {
            this.xlink_href = new TinyString(svgAnimationElem.xlink_href.data);
        }
        this.attributeName = svgAnimationElem.attributeName;
        this.additive = svgAnimationElem.additive;
        this.accumulate = svgAnimationElem.accumulate;
        this.calcMode = svgAnimationElem.calcMode;
        if (svgAnimationElem.svalues != null) {
            final int count = svgAnimationElem.svalues.count;
            this.svalues = new TinyVector(count);
            for (int i = 0; i < count; ++i) {
                this.svalues.addElement(new TinyString(((TinyString)svgAnimationElem.svalues.data[i]).data));
            }
        }
        if (svgAnimationElem.keyTimes != null) {
            final int count2 = svgAnimationElem.keyTimes.count;
            this.keyTimes = new TinyVector(count2);
            for (int j = 0; j < count2; ++j) {
                this.keyTimes.addElement(new TinyNumber(((TinyNumber)svgAnimationElem.keyTimes.data[j]).val));
            }
        }
        if (svgAnimationElem.keySplines != null) {
            final int count3 = svgAnimationElem.keySplines.count;
            this.keyTimes = new TinyVector(count3);
            for (int k = 0; k < count3; ++k) {
                this.keySplines.addElement(new TinyPath((TinyPath)svgAnimationElem.keySplines.data[k]));
            }
        }
        if (svgAnimationElem.sfrom != null) {
            this.sfrom = new TinyString(svgAnimationElem.sfrom.data);
        }
        if (svgAnimationElem.sto != null) {
            this.sto = new TinyString(svgAnimationElem.sto.data);
        }
        if (svgAnimationElem.sby != null) {
            this.sby = new TinyString(svgAnimationElem.sby.data);
        }
        this.begin = new SMILTime(svgAnimationElem.begin);
        this.end = new SMILTime(svgAnimationElem.end);
        this.dur = new SMILTime(svgAnimationElem.dur);
        this.min = new SMILTime(svgAnimationElem.min);
        this.max = new SMILTime(svgAnimationElem.max);
        this.restart = svgAnimationElem.restart;
        this.repeatDur = new SMILTime(svgAnimationElem.repeatDur);
        this.fillType = svgAnimationElem.fillType;
        this.repeatCount = svgAnimationElem.repeatCount;
        this.transformType = svgAnimationElem.transformType;
        if (svgAnimationElem.path != null) {
            this.path = new TinyPath(svgAnimationElem.path);
        }
        if (svgAnimationElem.keyPoints != null) {
            final int count4 = svgAnimationElem.keyPoints.count;
            this.keyPoints = new TinyVector(count4);
            for (int l = 0; l < count4; ++l) {
                this.keyPoints.addElement(new TinyNumber(((TinyNumber)svgAnimationElem.keyPoints.data[l]).val));
            }
        }
        this.rotate = svgAnimationElem.rotate;
        this.origin = svgAnimationElem.origin;
    }
    
    public SVGNode copyNode() {
        return new SVGAnimationElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 115: {
                this.xlink_href = (TinyString)o;
                break;
            }
            case 6: {
                this.attributeName = ((TinyNumber)o).val;
                break;
            }
            case 2: {
                this.additive = ((TinyNumber)o).val;
                break;
            }
            case 1: {
                this.accumulate = ((TinyNumber)o).val;
                break;
            }
            case 13: {
                this.calcMode = ((TinyNumber)o).val;
                break;
            }
            case 103: {
                this.svalues = (TinyVector)o;
                break;
            }
            case 49: {
                this.keyTimes = (TinyVector)o;
                break;
            }
            case 48: {
                this.keySplines = (TinyVector)o;
                break;
            }
            case 34: {
                this.sfrom = (TinyString)o;
                break;
            }
            case 93: {
                this.sto = (TinyString)o;
                break;
            }
            case 12: {
                this.sby = (TinyString)o;
                break;
            }
            case 25: {
                this.fillType = ((TinyNumber)o).val;
                break;
            }
            case 66: {
                this.repeatCount = ((TinyNumber)o).val;
                break;
            }
            case 11: {
                this.begin = (SMILTime)o;
                break;
            }
            case 24: {
                this.end = (SMILTime)o;
                break;
            }
            case 23: {
                this.dur = (SMILTime)o;
                break;
            }
            case 53: {
                this.min = (SMILTime)o;
                break;
            }
            case 52: {
                this.max = (SMILTime)o;
                break;
            }
            case 67: {
                this.repeatDur = (SMILTime)o;
                break;
            }
            case 70: {
                this.restart = ((TinyNumber)o).val;
                break;
            }
            case 95: {
                this.transformType = ((TinyNumber)o).val;
                break;
            }
            case 61: {
                this.path = (TinyPath)o;
                break;
            }
            case 47: {
                this.keyPoints = (TinyVector)o;
                break;
            }
            case 71: {
                this.rotate = ((TinyNumber)o).val;
                break;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
        super.outlined = false;
        return 0;
    }
    
    public Object getAttribute(final int n) {
        int n2 = 0;
        Object o = null;
        switch (n) {
            case 115: {
                o = this.xlink_href;
                break;
            }
            case 6: {
                n2 = this.attributeName;
                break;
            }
            case 2: {
                n2 = this.additive;
                break;
            }
            case 1: {
                n2 = this.accumulate;
                break;
            }
            case 13: {
                n2 = this.calcMode;
                break;
            }
            case 103: {
                o = this.svalues;
                break;
            }
            case 49: {
                o = this.keyTimes;
                break;
            }
            case 48: {
                o = this.keySplines;
                break;
            }
            case 34: {
                o = this.sfrom;
                break;
            }
            case 93: {
                o = this.sto;
                break;
            }
            case 12: {
                o = this.sby;
                break;
            }
            case 25: {
                n2 = this.fillType;
                break;
            }
            case 66: {
                n2 = this.repeatCount;
                break;
            }
            case 11: {
                o = this.begin;
                break;
            }
            case 24: {
                o = this.end;
                break;
            }
            case 23: {
                o = this.dur;
                break;
            }
            case 53: {
                o = this.min;
                break;
            }
            case 52: {
                o = this.max;
                break;
            }
            case 67: {
                o = this.repeatDur;
                break;
            }
            case 70: {
                n2 = this.restart;
                break;
            }
            case 95: {
                n2 = this.transformType;
                break;
            }
            case 61: {
                o = this.path;
                break;
            }
            case 47: {
                o = this.keyPoints;
                break;
            }
            case 71: {
                n2 = this.rotate;
                break;
            }
            default: {
                return super.getAttribute(n);
            }
        }
        if (o != null) {
            return o;
        }
        return new TinyNumber(n2);
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public TinyRect getBounds() {
        return null;
    }
    
    public int createOutline() {
        super.outlined = true;
        this.goto = null;
        if (this.xlink_href != null && this.xlink_href.count > 0 && super.ownerDocument != null && super.ownerDocument.root != null) {
            final int index = this.xlink_href.indexOf(35, 0);
            if (index != -1) {
                this.goto = SVGNode.getNodeById(super.ownerDocument.root, this.xlink_href.substring(index + 1));
            }
        }
        else {
            this.goto = super.parent;
        }
        if (this.goto == null) {
            return 2;
        }
        if (super.helem == 3 || super.helem == 4) {
            this.attributeName = 94;
        }
        if (this.attributeName == 127) {
            return 2;
        }
        if (!SVG.isElementAnimatable(this.goto.helem)) {
            return 2;
        }
        this.b = SVG.attributeDataType(this.goto.helem, this.attributeName);
        if (this.b == 0) {
            return 2;
        }
        this.long = for(super.helem, this.b);
        if (this.long == 0) {
            return 2;
        }
        final SVGAttr svgAttr = new SVGAttr(super.ownerDocument.if, super.ownerDocument.for);
        this.s = null;
        if (this.svalues != null && this.svalues.count > 0) {
            this.s = new TinyVector(this.svalues.count);
            for (int i = 0; i < this.svalues.count; ++i) {
                final TinyString tinyString = (TinyString)this.svalues.data[i];
                if (tinyString != null) {
                    this.s.addElement(this.a(svgAttr, tinyString.data, tinyString.count));
                }
            }
        }
        this.c = null;
        if (this.sfrom != null && this.sfrom.count > 0) {
            this.c = this.a(svgAttr, this.sfrom.data, this.sfrom.count);
        }
        this.o = null;
        if (this.sby != null && this.sby.count > 0) {
            this.o = this.a(svgAttr, this.sby.data, this.sby.count);
        }
        this.t = null;
        if (this.sto != null && this.sto.count > 0) {
            this.t = this.a(svgAttr, this.sto.data, this.sto.count);
        }
        if (super.helem == 3) {
            this.int();
        }
        return 0;
    }
    
    private Object a(final SVGAttr svgAttr, final char[] array, final int n) {
        final char[] array2 = new char[n + 32];
        Object o = null;
        switch (super.helem) {
            case 3: {
                o = svgAttr.char.parsePoint(array, n);
                break;
            }
            case 4: {
                final char[] array3 = SVG.VALUES[this.transformType];
                int n2 = 0;
                int length = array3.length;
                int n3 = 0;
                while (length-- != 0) {
                    array2[n2++] = array3[n3++];
                }
                array2[n2++] = '(';
                int n4 = n;
                int n5 = 0;
                while (n4-- != 0) {
                    array2[n2++] = array[n5++];
                }
                array2[n2++] = ')';
                o = svgAttr.char.parseTransfroms(array2, n2).data[0];
                break;
            }
            default: {
                o = svgAttr.attributeValue(this.goto.helem, this.attributeName, array, 0, n);
                break;
            }
        }
        return o;
    }
    
    private void int() {
        if (this.path != null) {
            this.s = TinyPath.pathToPoints(this.path);
            return;
        }
        final int count = super.children.count;
        SVGMPathElem svgmPathElem = null;
        for (int i = 0; i < count; ++i) {
            final SVGNode svgNode = (SVGNode)super.children.data[i];
            if (svgNode != null && svgNode.helem == 22) {
                svgmPathElem = (SVGMPathElem)svgNode;
                break;
            }
        }
        if (svgmPathElem != null && svgmPathElem.xlink_href != null) {
            final int index = svgmPathElem.xlink_href.indexOf(35, 0);
            if (index != -1) {
                final SVGPathElem svgPathElem = (SVGPathElem)SVGNode.getNodeById(super.ownerDocument.root, svgmPathElem.xlink_href.substring(index + 1));
                if (svgPathElem != null && svgPathElem.path != null) {
                    this.s = TinyPath.pathToPoints(svgPathElem.path);
                }
            }
        }
    }
    
    static int for(final int n, final int n2) {
        int n3 = 0;
        switch (n2) {
            case 1: {
                n3 = 1;
                break;
            }
            case 11: {
                if (n == 4) {
                    n3 = 4;
                }
                else if (n == 3) {
                    n3 = 7;
                }
                else {
                    n3 = 0;
                }
                break;
            }
            case 4: {
                n3 = 2;
                break;
            }
            case 6: {
                n3 = 5;
                break;
            }
            case 8: {
                n3 = 6;
                break;
            }
            case 2:
            case 3:
            case 9:
            case 14: {
                n3 = 3;
                break;
            }
        }
        return n3;
    }
    
    public boolean beginElementAt(int offset) {
        if (this.u == 1 && this.restart == 59) {
            return false;
        }
        if (this.restart == 34 && (this.u == 1 || this.u == 3)) {
            return false;
        }
        if (offset <= 0) {
            offset = 0;
        }
        this.begin.timeValue = super.ownerDocument.getCurrentTime();
        this.begin.offset = offset;
        this.u = 0;
        return true;
    }
    
    public boolean endElementAt(int offset) {
        if (this.u != 1) {
            return false;
        }
        if (this.end.getResolvedOffset() != -1) {
            return false;
        }
        if (offset <= 0) {
            offset = 0;
        }
        this.end.timeValue = super.ownerDocument.getCurrentTime();
        this.end.offset = offset;
        return true;
    }
    
    int a(final int n) {
        if (this.repeatCount <= 0 && this.repeatDur.getResolvedOffset() <= 0) {
            return 256;
        }
        if (this.repeatCount > 0 && this.repeatDur.getResolvedOffset() < 0) {
            return this.repeatCount;
        }
        if (this.repeatCount == 0 && this.repeatDur.getResolvedOffset() > 0) {
            return this.if(this.repeatDur.getResolvedOffset(), n);
        }
        return TinyUtil.min(this.repeatCount, this.if(this.repeatDur.getResolvedOffset(), n));
    }
    
    boolean for() {
        return this.repeatCount == -1 || this.repeatDur.type == 0;
    }
    
    private int a(int n, final int n2, final int n3, final int n4, final boolean b) {
        if (n < n2 || n2 == -1) {
            this.u = 0;
            return -1;
        }
        if (n >= n2 && n3 == -1) {
            if (this.u != 1) {
                this.if(0);
                this.u = 1;
                this.null = 0;
            }
            return 256;
        }
        if (n >= n2 && (b || n < n2 + this.a(n3, n4))) {
            if (this.u != 1) {
                this.if(0);
                this.u = 1;
                this.null = 0;
            }
            final int null = this.if(n - n2, n3) >> 8;
            if (null != this.null) {
                this.null = null;
            }
            this.if(2);
            return this.if(n - n2 - this.a(null << 8, n3), n3);
        }
        if (this.u == 1) {
            this.if(1);
        }
        if (this.fillType == 44) {
            this.u = 3;
            return -1;
        }
        this.u = 2;
        n = n2 + this.a(n3, n4);
        final int null2 = this.if(n - n2, n3) >> 8;
        if (null2 != this.null) {
            this.null = null2;
        }
        this.if(2);
        int n5 = null2 << 8;
        if (n5 == n4) {
            n5 -= 256;
        }
        return this.if(n - n2 - this.a(n5, n3), n3);
    }
    
    private final void if(final int n) {
        if (super.id == null) {
            return;
        }
        final char[] array = SVGAnimationElem.r[n];
        final char[] array2 = new char[super.id.count + array.length];
        int count = super.id.count;
        int n2 = 0;
        int n3 = 0;
        while (count-- != 0) {
            array2[n3++] = super.id.data[n2++];
        }
        int length = array.length;
        int n4 = 0;
        while (length-- != 0) {
            array2[n3++] = array[n4++];
        }
        super.ownerDocument.postSMILEvent(n, new TinyString(array2));
    }
    
    private final void do() {
        int count = this.s.count;
        this.keyTimes = new TinyVector(count);
        int n = 0;
        if (count == 1) {
            this.keyTimes.addElement(new TinyNumber(256));
            return;
        }
        if (count == 2) {
            this.keyTimes.addElement(new TinyNumber(0));
            this.keyTimes.addElement(new TinyNumber(256));
            return;
        }
        final int n2 = 256 / (count - 1);
        while (count-- > 0) {
            this.keyTimes.addElement(new TinyNumber(n));
            n += n2;
        }
        this.keyTimes.data[this.keyTimes.count - 1] = new TinyNumber(256);
    }
    
    Object a() {
        if (this.u == 3) {
            return this.d;
        }
        final Object if1 = this.if();
        if (if1 == null && this.u == 3 && this.fillType == 23) {
            return this.d;
        }
        return this.d = if1;
    }
    
    private Object if() {
        final int currentTime = super.ownerDocument.getCurrentTime();
        final int resolvedOffset = this.begin.getResolvedOffset();
        int resolvedOffset2 = this.dur.getResolvedOffset();
        if (resolvedOffset2 == -1) {
            final int resolvedOffset3 = this.end.getResolvedOffset();
            if (resolvedOffset3 != -1) {
                resolvedOffset2 = resolvedOffset3 - resolvedOffset;
            }
        }
        final int a = this.a(currentTime, resolvedOffset, resolvedOffset2, this.a(resolvedOffset2), this.for());
        if (a < 0) {
            return null;
        }
        if (this.s != null && this.s.count > 0) {
            TinyNumber tinyNumber2;
            TinyNumber tinyNumber = tinyNumber2 = new TinyNumber(0);
            Object o2;
            Object o = o2 = null;
            int n = 0;
            if (this.keyTimes != null && this.keyTimes.count != this.s.count) {
                this.keyTimes = null;
            }
            if (this.keyTimes == null) {
                this.do();
            }
            for (int i = 0; i < this.keyTimes.count - 1; ++i) {
                tinyNumber2 = (TinyNumber)this.keyTimes.data[i];
                tinyNumber = (TinyNumber)this.keyTimes.data[i + 1];
                if (a >= tinyNumber2.val && a <= tinyNumber.val) {
                    o2 = this.s.data[i];
                    o = this.s.data[i + 1];
                    break;
                }
                if (i == this.keyTimes.count - 2 && this.calcMode == 20 && a > tinyNumber.val) {
                    o2 = this.s.data[i + 1];
                    o = this.s.data[i + 1];
                    break;
                }
                ++n;
            }
            if (o2 != null && o != null) {
                final int if1 = this.if(a - tinyNumber2.val, tinyNumber.val - tinyNumber2.val);
                if (this.calcMode == 30 || this.calcMode == 40) {
                    return this.do(if1, o2, o, null);
                }
                if (this.calcMode == 20) {
                    if (if1 < 256) {
                        return this.do(0, o2, o, null);
                    }
                    return this.do(256, o2, o, null);
                }
                else if (this.calcMode == 52) {
                    return this.do(this.do(n, if1), o2, o, null);
                }
            }
        }
        else if (this.c != null || this.t != null || this.o != null) {
            return this.do(a, this.c, this.t, this.o);
        }
        return null;
    }
    
    private Object do(final int n, final Object o, final Object o2, final Object o3) {
        switch (this.long) {
            case 1: {
                return this.a(n, o, o2, o3);
            }
            case 2: {
                return this.int(n, o, o2, o3);
            }
            case 4: {
                return this.if(n, o, o2, o3);
            }
            case 7: {
                return this.new(n, o, o2, o3);
            }
            case 3: {
                return this.byte(n, o, o2, o3);
            }
            case 5: {
                return this.for(n, o, o2, o3);
            }
            case 6: {
                return this.try(n, o, o2, o3);
            }
            default: {
                return null;
            }
        }
    }
    
    private Object byte(final int n, final Object o, final Object o2, final Object o3) {
        if (o == null || o2 == null) {
            return null;
        }
        if (n <= 0) {
            return o;
        }
        return o2;
    }
    
    private Object if(final int n, final Object o, final Object o2, final Object o3) {
        final TinyTransform tinyTransform = new TinyTransform();
        final TinyTransform tinyTransform2 = (TinyTransform)o;
        final TinyTransform tinyTransform3 = (TinyTransform)o2;
        final TinyTransform tinyTransform4 = (TinyTransform)o3;
        switch (this.transformType) {
            case 49: {
                if (o != null) {
                    tinyTransform.type = 3;
                    if (tinyTransform3 != null) {
                        tinyTransform.matrix.a = tinyTransform2.matrix.a + TinyUtil.mul(n << 8, tinyTransform3.matrix.a - tinyTransform2.matrix.a);
                        tinyTransform.matrix.d = tinyTransform2.matrix.d + TinyUtil.mul(n << 8, tinyTransform3.matrix.d - tinyTransform2.matrix.d);
                    }
                    else if (tinyTransform4 != null) {
                        tinyTransform.matrix.a = tinyTransform2.matrix.a + TinyUtil.mul(n << 8, tinyTransform4.matrix.a);
                        tinyTransform.matrix.d = tinyTransform2.matrix.d + TinyUtil.mul(n << 8, tinyTransform4.matrix.d);
                    }
                }
                break;
            }
            case 47: {
                if (o != null) {
                    if (tinyTransform3 != null) {
                        tinyTransform.setRotate(tinyTransform2.angle + this.a(n, tinyTransform3.angle - tinyTransform2.angle), tinyTransform2.rotateOriginX + this.a(n, tinyTransform3.rotateOriginX - tinyTransform2.rotateOriginX), tinyTransform2.rotateOriginY + this.a(n, tinyTransform3.rotateOriginY - tinyTransform2.rotateOriginY));
                    }
                    else if (tinyTransform4 != null) {
                        tinyTransform.setRotate(tinyTransform2.angle + this.a(n, tinyTransform4.angle), tinyTransform2.rotateOriginX + this.a(n, tinyTransform4.rotateOriginX), tinyTransform2.rotateOriginY + this.a(n, tinyTransform4.rotateOriginY));
                    }
                }
                break;
            }
            case 50: {
                if (o != null) {
                    if (tinyTransform3 != null) {
                        tinyTransform.setSkewX(tinyTransform2.angle + this.a(n, tinyTransform3.angle - tinyTransform2.angle));
                    }
                    else if (tinyTransform4 != null) {
                        tinyTransform.setSkewX(tinyTransform2.angle + this.a(n, tinyTransform4.angle));
                    }
                }
                break;
            }
            case 51: {
                if (o != null) {
                    if (tinyTransform3 != null) {
                        tinyTransform.setSkewY(tinyTransform2.angle + this.a(n, tinyTransform3.angle - tinyTransform2.angle));
                    }
                    else if (tinyTransform4 != null) {
                        tinyTransform.setSkewY(tinyTransform2.angle + this.a(n, tinyTransform4.angle));
                    }
                }
                break;
            }
            default: {
                if (o == null) {
                    break;
                }
                if (tinyTransform3 != null) {
                    tinyTransform.matrix.tx = tinyTransform2.matrix.tx + this.a(n, tinyTransform3.matrix.tx - tinyTransform2.matrix.tx);
                    tinyTransform.matrix.ty = tinyTransform2.matrix.ty + this.a(n, tinyTransform3.matrix.ty - tinyTransform2.matrix.ty);
                    break;
                }
                if (tinyTransform4 != null) {
                    tinyTransform.matrix.tx = tinyTransform2.matrix.tx + this.a(n, tinyTransform4.matrix.tx);
                    tinyTransform.matrix.ty = tinyTransform2.matrix.ty + this.a(n, tinyTransform4.matrix.ty);
                    break;
                }
                break;
            }
        }
        return tinyTransform;
    }
    
    private Object a(final int n, final Object o, final Object o2, final Object o3) {
        if (n == 0) {
            return o;
        }
        final TinyColor tinyColor = new TinyColor(-256);
        if (o != null && (o2 != null || o3 != null)) {
            final int value = ((TinyColor)o).value;
            final int n2 = value >> 24 & 0xFF;
            final int n3 = value >> 16 & 0xFF;
            final int n4 = value >> 8 & 0xFF;
            final int n5 = value & 0xFF;
            TinyColor tinyColor2;
            if (o2 != null) {
                if (n == 256) {
                    return o2;
                }
                tinyColor2 = (TinyColor)o2;
            }
            else {
                tinyColor2 = (TinyColor)o3;
            }
            final int value2 = tinyColor2.value;
            int n6 = value2 >> 24 & 0xFF;
            int n7 = value2 >> 16 & 0xFF;
            int n8 = value2 >> 8 & 0xFF;
            int n9 = value2 & 0xFF;
            if (o2 != null) {
                n6 -= n2;
                n7 -= n3;
                n8 -= n4;
                n9 -= n5;
            }
            tinyColor.value = (n2 + this.a(n, n6) << 24) + (n3 + this.a(n, n7) << 16) + (n4 + this.a(n, n8) << 8) + (n5 + this.a(n, n9));
        }
        return tinyColor;
    }
    
    private Object new(final int n, final Object o, final Object o2, final Object o3) {
        final TinyTransform[] array = new TinyTransform[2];
        final TinyTransform tinyTransform = new TinyTransform();
        final TinyTransform tinyTransform2 = new TinyTransform();
        final TinyPoint tinyPoint = new TinyPoint();
        TinyPoint tinyPoint2 = (TinyPoint)o;
        TinyPoint tinyPoint3 = (TinyPoint)o2;
        final TinyPoint tinyPoint4 = (TinyPoint)o3;
        if (tinyPoint2 == null) {
            tinyPoint2 = new TinyPoint(0, 0);
        }
        if (tinyPoint3 == null && tinyPoint4 == null) {
            return null;
        }
        if (tinyPoint4 != null) {
            tinyPoint3 = new TinyPoint(tinyPoint2.x + tinyPoint4.x, tinyPoint2.y + tinyPoint4.y);
        }
        int fastDistance = TinyUtil.fastDistance(tinyPoint3.x - tinyPoint2.x, tinyPoint3.y - tinyPoint2.y);
        if (fastDistance == 0) {
            fastDistance = 128;
        }
        final int if1 = this.if(this.a(n, fastDistance), fastDistance);
        tinyPoint.x = this.a(if1, tinyPoint3.x - tinyPoint2.x) + tinyPoint2.x;
        tinyPoint.y = this.a(if1, tinyPoint3.y - tinyPoint2.y) + tinyPoint2.y;
        tinyTransform.setTranslate(tinyPoint.x, tinyPoint.y);
        if (this.rotate != 0) {
            int n2;
            if (this.rotate == Integer.MIN_VALUE) {
                n2 = TinyUtil.atan2(tinyPoint3.y - tinyPoint2.y, tinyPoint3.x - tinyPoint2.x);
            }
            else if (this.rotate == Integer.MAX_VALUE) {
                n2 = TinyUtil.atan2(tinyPoint3.y - tinyPoint2.y, tinyPoint3.x - tinyPoint2.x) + 46080;
            }
            else {
                n2 = this.rotate;
            }
            tinyTransform2.setRotate(n2, 0, 0);
        }
        array[0] = tinyTransform;
        array[1] = tinyTransform2;
        return array;
    }
    
    private Object int(final int n, final Object o, final Object o2, final Object o3) {
        final TinyNumber tinyNumber = new TinyNumber(0);
        if (o != null && o2 != null) {
            final TinyNumber tinyNumber2 = (TinyNumber)o;
            tinyNumber.val = tinyNumber2.val + this.a(n, ((TinyNumber)o2).val - tinyNumber2.val);
        }
        else if (o != null && o3 != null) {
            tinyNumber.val = ((TinyNumber)o).val + this.a(n, ((TinyNumber)o3).val);
        }
        return tinyNumber;
    }
    
    private Object for(final int n, final Object o, final Object o2, final Object o3) {
        TinyPath tinyPath = null;
        if (o != null && o2 != null) {
            final TinyPath tinyPath2 = (TinyPath)o;
            final TinyPath tinyPath3 = (TinyPath)o2;
            final int numPoints = tinyPath2.numPoints();
            if (numPoints != tinyPath3.numPoints()) {
                return null;
            }
            tinyPath = new TinyPath(numPoints);
            for (int i = 0; i < numPoints; ++i) {
                tinyPath.addPoint(tinyPath2.getX(i) + this.a(n, tinyPath3.getX(i) - tinyPath2.getX(i)), tinyPath2.getY(i) + this.a(n, tinyPath3.getY(i) - tinyPath2.getY(i)), tinyPath2.getType(i));
            }
        }
        else if (o != null && o3 != null) {
            final TinyPath tinyPath4 = (TinyPath)o;
            final TinyPath tinyPath5 = (TinyPath)o3;
            final int numPoints2 = tinyPath4.numPoints();
            if (numPoints2 != tinyPath5.numPoints()) {
                return null;
            }
            tinyPath = new TinyPath(numPoints2);
            for (int j = 0; j < numPoints2; ++j) {
                tinyPath.addPoint(tinyPath4.getX(j) + this.a(n, tinyPath5.getX(j)), tinyPath4.getY(j) + this.a(n, tinyPath5.getY(j)), tinyPath4.getType(j));
            }
        }
        return tinyPath;
    }
    
    private Object try(final int n, final Object o, final Object o2, final Object o3) {
        TinyVector tinyVector = null;
        if (o != null && o2 != null) {
            final TinyVector tinyVector2 = (TinyVector)o;
            final TinyVector tinyVector3 = (TinyVector)o2;
            final int count = tinyVector2.count;
            if (count != tinyVector3.count) {
                return null;
            }
            tinyVector = new TinyVector(count);
            for (int i = 0; i < count; ++i) {
                final TinyPoint tinyPoint = (TinyPoint)tinyVector2.data[i];
                final TinyPoint tinyPoint2 = (TinyPoint)tinyVector3.data[i];
                tinyVector.addElement(new TinyPoint(tinyPoint.x + this.a(n, tinyPoint2.x - tinyPoint.x), tinyPoint.y + this.a(n, tinyPoint2.y - tinyPoint.y)));
            }
        }
        else if (o != null && o3 != null) {
            final TinyVector tinyVector4 = (TinyVector)o;
            final TinyVector tinyVector5 = (TinyVector)o3;
            final int count2 = tinyVector4.count;
            if (count2 != tinyVector5.count) {
                return null;
            }
            tinyVector = new TinyVector(count2);
            for (int j = 0; j < count2; ++j) {
                final TinyPoint tinyPoint3 = (TinyPoint)tinyVector4.data[j];
                final TinyPoint tinyPoint4 = (TinyPoint)tinyVector5.data[j];
                tinyVector.addElement(new TinyPoint(tinyPoint3.x + this.a(n, tinyPoint4.x), tinyPoint3.y + this.a(n, tinyPoint4.y)));
            }
        }
        return tinyVector;
    }
    
    private int do(final int n, final int n2) {
        final TinyVector keySplines = this.keySplines;
        final int n3 = 256;
        if (keySplines == null || keySplines.count == 0 || n > keySplines.count - 1) {
            return n3;
        }
        if (n == keySplines.count - 1 && n2 == n3) {
            return n3;
        }
        final TinyVector tinyVector = (TinyVector)keySplines.data[n];
        if (tinyVector == null || tinyVector.count == 0) {
            return n3;
        }
        final TinyPoint tinyPoint = new TinyPoint(0, 0);
        final int count = tinyVector.count;
        TinyPoint tinyPoint2 = tinyPoint;
        for (int i = 1; i < count; ++i) {
            final TinyPoint tinyPoint3 = (TinyPoint)tinyVector.data[i];
            if (n2 >= tinyPoint2.x && n2 <= tinyPoint3.x) {
                return tinyPoint2.y + this.a(this.if(n2 - tinyPoint2.x, tinyPoint3.x - tinyPoint2.x), tinyPoint3.y - tinyPoint2.y);
            }
            tinyPoint2 = tinyPoint3;
        }
        return tinyPoint2.y;
    }
    
    int if(final int n, final int n2) {
        return TinyUtil.div(n, n2) >> 8;
    }
    
    int a(final int n, final int n2) {
        return TinyUtil.mul(n << 8, n2);
    }
    
    static {
        SVGAnimationElem.r = new char[][] { ".begin".toCharArray(), ".end".toCharArray(), ".repeat".toCharArray() };
    }
}
