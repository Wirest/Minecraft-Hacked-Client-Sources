// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyVector;

class c
{
    int do;
    int if;
    Object a;
    TinyVector for;
    
    c(final int do1, final int if1) {
        this.do = do1;
        this.if = if1;
        this.a = null;
        this.for = new TinyVector(10);
    }
    
    void a(final SVGAnimationElem svgAnimationElem) {
        if (svgAnimationElem.long == 3) {
            svgAnimationElem.calcMode = 20;
        }
        if (this.a == null) {
            this.a = SVG.copyAttributeValue(svgAnimationElem.goto.getAttribute(this.do), this.if);
        }
        if (svgAnimationElem.t != null && svgAnimationElem.c == null) {
            if (this.a != null) {
                svgAnimationElem.c = this.a;
            }
            else {
                final Object t = svgAnimationElem.t;
                this.a = t;
                svgAnimationElem.c = t;
            }
        }
        if (svgAnimationElem.o != null && svgAnimationElem.c == null) {
            svgAnimationElem.c = this.a;
        }
        this.for.addElement(svgAnimationElem);
    }
    
    Object a() {
        Object o = null;
        switch (this.if) {
            case 11: {
                o = this.if();
                break;
            }
            default: {
                o = this.do();
                break;
            }
        }
        return o;
    }
    
    private void for() {
        int count = this.for.count;
        while (--count >= 0) {
            boolean b = false;
            for (int i = 0; i < count; ++i) {
                if (((SVGAnimationElem)this.for.data[i]).begin.greaterThan(((SVGAnimationElem)this.for.data[i + 1]).begin)) {
                    final SVGAnimationElem svgAnimationElem = (SVGAnimationElem)this.for.data[i];
                    this.for.data[i] = this.for.data[i + 1];
                    this.for.data[i + 1] = svgAnimationElem;
                    b = true;
                }
            }
            if (!b) {
                break;
            }
        }
    }
    
    private Object do() {
        this.for();
        final int count = this.for.count;
        if (count == 0) {
            return null;
        }
        Object a = this.a;
        for (int i = 0; i < count; ++i) {
            final SVGAnimationElem svgAnimationElem = (SVGAnimationElem)this.for.data[i];
            final SVGDocument ownerDocument = svgAnimationElem.ownerDocument;
            if (svgAnimationElem.u != 3) {
                final SVGDocument svgDocument = ownerDocument;
                ++svgDocument.nActiveAnimations;
            }
            final Object a2 = svgAnimationElem.a();
            if (a2 != null) {
                a = a2;
            }
        }
        return a;
    }
    
    private TinyTransform if() {
        this.for();
        final int count = this.for.count;
        if (count == 0) {
            return null;
        }
        final TinyTransform tinyTransform = new TinyTransform();
        if (this.a != null) {
            tinyTransform.setMatrix(new TinyMatrix(((TinyTransform)this.a).matrix));
        }
        for (int i = 0; i < count; ++i) {
            final SVGAnimationElem svgAnimationElem = (SVGAnimationElem)this.for.data[i];
            final SVGDocument ownerDocument = svgAnimationElem.ownerDocument;
            if (svgAnimationElem.u != 3) {
                final SVGDocument svgDocument = ownerDocument;
                ++svgDocument.nActiveAnimations;
            }
            if (svgAnimationElem.helem == 4) {
                final Object a = svgAnimationElem.a();
                if (a != null) {
                    if (svgAnimationElem.additive == 46) {
                        tinyTransform.setMatrix(new TinyMatrix(((TinyTransform)a).matrix));
                    }
                    else {
                        tinyTransform.matrix.preConcatenate(((TinyTransform)a).matrix);
                    }
                }
            }
        }
        final TinyTransform tinyTransform2 = new TinyTransform();
        for (int j = 0; j < count; ++j) {
            final SVGAnimationElem svgAnimationElem2 = (SVGAnimationElem)this.for.data[j];
            if (svgAnimationElem2.helem == 3) {
                final Object[] array = (Object[])svgAnimationElem2.a();
                if (array != null) {
                    if (svgAnimationElem2.additive == 46) {
                        tinyTransform2.matrix.reset();
                        tinyTransform2.matrix.preConcatenate(((TinyTransform)array[0]).matrix);
                        tinyTransform2.matrix.preConcatenate(((TinyTransform)array[1]).matrix);
                    }
                    else {
                        tinyTransform2.matrix.preConcatenate(((TinyTransform)array[0]).matrix);
                        tinyTransform2.matrix.preConcatenate(((TinyTransform)array[1]).matrix);
                    }
                }
            }
        }
        tinyTransform2.matrix.preConcatenate(tinyTransform.matrix);
        return tinyTransform2;
    }
}
