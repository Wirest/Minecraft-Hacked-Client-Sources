// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

public class SVGTextElem extends SVGNode
{
    public int x;
    public int y;
    public TinyString str;
    SVGPathElem w;
    SVGFontElem A;
    boolean z;
    
    SVGTextElem() {
        this.x = 0;
        this.y = 0;
        this.str = null;
        this.A = null;
        this.z = false;
        this.w = null;
    }
    
    public SVGTextElem(final SVGTextElem svgTextElem) {
        super(svgTextElem);
        this.x = svgTextElem.x;
        this.y = svgTextElem.y;
        if (svgTextElem.str != null) {
            this.str = new TinyString(svgTextElem.str.data);
        }
        this.A = null;
        this.z = false;
        this.w = null;
    }
    
    public SVGNode copyNode() {
        return new SVGTextElem(this);
    }
    
    public TinyString getText() {
        return this.str;
    }
    
    public int setText(final char[] array, final int n, final int n2) {
        if (array == null) {
            return 2;
        }
        this.str = new TinyString(array, n, n2);
        this.z = false;
        super.outlined = false;
        return 0;
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 109: {
                this.x = ((TinyNumber)o).val;
                break;
            }
            case 123: {
                this.y = ((TinyNumber)o).val;
                break;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
        return 0;
    }
    
    public Object getAttribute(final int n) {
        final Object o = null;
        int n2 = 0;
        switch (n) {
            case 109: {
                n2 = this.x;
                break;
            }
            case 123: {
                n2 = this.y;
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
        if (!this.isDisplay() || !this.isVisible()) {
            return;
        }
        if (!super.outlined) {
            this.createOutline();
        }
        if (this.w == null) {
            return;
        }
        this.w.paint(svgRaster);
    }
    
    public TinyRect getBounds() {
        if (!super.outlined) {
            this.createOutline();
        }
        if (this.w == null) {
            return null;
        }
        return this.w.getBounds();
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        if (!super.outlined) {
            this.createOutline();
        }
        if (this.w == null) {
            return null;
        }
        return this.w.getDevBounds(svgRaster);
    }
    
    public int createOutline() {
        if (this.w == null) {
            this.w = (SVGPathElem)super.ownerDocument.createElement(23);
            (this.w.path = new TinyPath(6)).moveTo(0, 0);
            this.w.path.lineTo(25600, 0);
            this.w.path.lineTo(25600, 25600);
            this.w.path.lineTo(0, 25600);
            this.w.path.closePath();
            this.w.bounds = this.w.path.getBBox();
            this.w.outlined = true;
            this.w.fillRule = 22;
            this.w.fill = TinyColor.NONE;
            this.w.stroke = TinyColor.NONE;
        }
        if (super.children.indexOf(this.w, 0) == -1) {
            this.addChild(this.w, -1);
        }
        if (this.str == null || this.str.count == 0) {
            return 0;
        }
        final int x = this.x;
        final int y = this.y;
        final TinyString fontFamily = this.getFontFamily();
        SVGFontElem svgFontElem = null;
        if (this.A != null) {
            svgFontElem = this.A;
        }
        if (this.A == null || svgFontElem == null || !svgFontElem.else.fontFamily.equals(fontFamily)) {
            this.z = false;
            this.A = SVGDocument.getFont(super.ownerDocument, fontFamily);
            if (this.A == null) {
                this.A = SVGDocument.defaultFont;
            }
            if (this.A == null) {
                return 2;
            }
            svgFontElem = this.A;
        }
        if (!this.z) {
            this.w.path.reset();
            this.w.outlined = false;
            final TinyPath path = this.w.path;
            int count = this.str.count;
            int i = 0;
            final char[] array = new char[this.str.count];
            final char[] data = this.str.data;
            int n = 0;
            int n4;
            if (super.xmlSpace == 18) {
                int n2 = 65535;
                int j;
                int n3 = j = 0;
                while (j < count) {
                    char c = data[j++];
                    if (c == '\t') {
                        c = ' ';
                    }
                    if (c == '\n' || c == '\r') {
                        continue;
                    }
                    if (n2 == 32 && c == ' ') {
                        n2 = c;
                    }
                    else {
                        array[n3++] = c;
                        n2 = c;
                    }
                }
                for (n4 = 0, count = n3; n4 < count && array[n4] <= ' '; ++n4) {}
                while (n4 < count && array[count - 1] <= ' ') {
                    --count;
                }
            }
            else {
                while (i < count) {
                    char c2 = data[i++];
                    if (c2 == '\r') {
                        continue;
                    }
                    if (c2 == '\t' || c2 == '\n') {
                        c2 = ' ';
                    }
                    array[n++] = c2;
                }
                n4 = 0;
            }
            this.w.path = TinyPath.charsToPath(svgFontElem.char, array, n4, count);
            this.w.fillRule = 22;
            this.w.createOutline();
            this.w.isAntialiased = true;
            this.z = true;
        }
        final int fontSize = this.getFontSize();
        final int textAnchor = this.getTextAnchor();
        int n5;
        if (textAnchor == 21) {
            n5 = 3;
        }
        else if (textAnchor == 32) {
            n5 = 2;
        }
        else {
            n5 = 1;
        }
        final TinyMatrix charToUserTransform = svgFontElem.char.charToUserTransform(this.w.path, fontSize, x, y, n5);
        this.w.fill = TinyColor.INHERIT;
        this.w.stroke = TinyColor.INHERIT;
        this.w.strokeWidth = TinyUtil.div(this.getLineThickness(), charToUserTransform.a);
        this.w.transform.setMatrix(charToUserTransform);
        super.outlined = true;
        return 0;
    }
}
