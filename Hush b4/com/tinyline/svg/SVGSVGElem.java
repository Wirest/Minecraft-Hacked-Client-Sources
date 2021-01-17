// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyVector;
import com.tinyline.tiny2d.TinyString;

public class SVGSVGElem extends SVGGroupElem
{
    public static boolean AUTOFIT;
    public SVGRect viewPort;
    public SVGRect viewBox;
    public int preserveAspectRatio;
    public int zoomAndPan;
    public TinyString version;
    public TinyString baseProfile;
    TinyVector int;
    TinyVector try;
    int for;
    int new;
    
    public SVGSVGElem() {
        super.color = new TinyColor(-16777216);
        super.fillRule = 2;
        super.fill = new TinyColor(-16777216);
        super.stroke = TinyColor.NONE;
        super.stopColor = new TinyColor(-16777216);
        super.strokeDashArray = SVG.VAL_STROKEDASHARRAYNONE;
        super.strokeDashOffset = 0;
        super.strokeWidth = 256;
        super.strokeLineCap = 15;
        super.strokeLineJoin = 33;
        super.strokeMiterLimit = 1024;
        super.textAnchor = 54;
        super.fontFamily = SVG.VAL_DEFAULT_FONTFAMILY;
        super.fontSize = 3072;
        super.transform = new TinyTransform();
        super.stopOpacity = 255;
        super.strokeOpacity = 255;
        super.fillOpacity = 255;
        super.opacity = 255;
        super.visibility = 58;
        super.display = 27;
        this.zoomAndPan = 31;
        this.preserveAspectRatio = 60;
        super.textAnchor = 54;
        super.helem = 30;
        this.viewPort = new SVGRect();
        this.viewBox = new SVGRect();
        (this.int = new TinyVector(2)).addElement(new TinyTransform());
        this.int.addElement(new TinyTransform());
        (this.try = new TinyVector(3)).addElement(new TinyTransform());
        this.try.addElement(new TinyTransform());
        this.try.addElement(new TinyTransform());
        this.version = new TinyString("1.1".toCharArray());
        this.baseProfile = new TinyString("tiny".toCharArray());
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 109: {
                this.viewPort.x = ((TinyNumber)o).val;
                break;
            }
            case 123: {
                this.viewPort.y = ((TinyNumber)o).val;
                break;
            }
            case 107: {
                this.viewPort.width = ((TinyNumber)o).val;
                break;
            }
            case 41: {
                this.viewPort.height = ((TinyNumber)o).val;
                break;
            }
            case 105: {
                this.viewBox = (SVGRect)o;
                break;
            }
            case 64: {
                this.preserveAspectRatio = ((TinyNumber)o).val;
                break;
            }
            case 104: {
                this.version = (TinyString)o;
                break;
            }
            case 8: {
                this.baseProfile = (TinyString)o;
                break;
            }
            case 126: {
                this.zoomAndPan = ((TinyNumber)o).val;
                break;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
        return 0;
    }
    
    public Object getAttribute(final int n) {
        int n2 = 0;
        Object o = null;
        switch (n) {
            case 109: {
                n2 = this.viewPort.x;
                break;
            }
            case 123: {
                n2 = this.viewPort.y;
                break;
            }
            case 107: {
                n2 = this.viewPort.width;
                break;
            }
            case 41: {
                n2 = this.viewPort.height;
                break;
            }
            case 105: {
                o = this.viewBox;
                break;
            }
            case 64: {
                n2 = this.preserveAspectRatio;
                break;
            }
            case 104: {
                o = this.version;
                break;
            }
            case 8: {
                o = this.baseProfile;
                break;
            }
            case 126: {
                n2 = this.zoomAndPan;
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
    
    public int createOutline() {
        final int recalculateViewboxToViewportTransform = this.recalculateViewboxToViewportTransform();
        super.outlined = true;
        return recalculateViewboxToViewportTransform;
    }
    
    public int setCurrentScale(final int n) {
        ((TinyTransform)this.try.data[1]).setScale(n, n);
        super.outlined = false;
        return 0;
    }
    
    public int setCurrentTranslate(final int n, final int n2) {
        ((TinyTransform)this.try.data[0]).setTranslate(n, n2);
        super.outlined = false;
        return 0;
    }
    
    public int getCurrentScale() {
        return ((TinyTransform)this.try.data[1]).matrix.a;
    }
    
    public int recalculateViewboxToViewportTransform() {
        if (this.viewPort.width == 0) {
            this.viewPort.width = super.ownerDocument.if;
        }
        if (this.viewPort.height == 0) {
            this.viewPort.height = super.ownerDocument.for;
        }
        if (SVGSVGElem.AUTOFIT) {
            this.for = super.ownerDocument.if;
            this.new = super.ownerDocument.for;
        }
        else {
            this.for = this.viewPort.width;
            this.new = this.viewPort.height;
        }
        if (this.viewBox.width == 0 || this.viewBox.height == 0) {
            final SVGRect viewBox = this.viewBox;
            final SVGRect viewBox2 = this.viewBox;
            final int n = 0;
            viewBox2.y = n;
            viewBox.x = n;
            this.viewBox.width = this.viewPort.width << 8;
            this.viewBox.height = this.viewPort.height << 8;
        }
        final int div = TinyUtil.div(this.for, this.viewBox.width);
        final int div2 = TinyUtil.div(this.new, this.viewBox.height);
        if (this.preserveAspectRatio == 35) {
            ((TinyTransform)this.int.data[0]).setScale(div * 256, div2 * 256);
            ((TinyTransform)this.int.data[1]).setTranslate(-this.viewBox.x, -this.viewBox.y);
        }
        else {
            final int min = TinyUtil.min(div, div2);
            int n2;
            int n3;
            if (div2 < div) {
                n2 = TinyUtil.div(this.viewPort.y, min) - this.viewBox.y;
                n3 = TinyUtil.div(this.viewPort.x + this.for / 2, min) - (this.viewBox.x + this.viewBox.width / 2);
            }
            else {
                n3 = TinyUtil.div(this.viewPort.x, min) - this.viewBox.x;
                n2 = TinyUtil.div(this.viewPort.y + this.new / 2, min) - (this.viewBox.y + this.viewBox.height / 2);
            }
            ((TinyTransform)this.int.data[0]).setScale(min * 256, min * 256);
            ((TinyTransform)this.int.data[1]).setTranslate(n3, n2);
        }
        (super.transform.matrix = TinyTransform.getTinyMatrix(this.int)).concatenate(TinyTransform.getTinyMatrix(this.try));
        return 0;
    }
    
    static {
        SVGSVGElem.AUTOFIT = true;
    }
}
