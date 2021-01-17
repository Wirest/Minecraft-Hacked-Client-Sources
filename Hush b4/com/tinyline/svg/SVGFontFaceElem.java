// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

public class SVGFontFaceElem extends SVGNode
{
    TinyString fontFamily;
    int K;
    int M;
    int J;
    int L;
    
    SVGFontFaceElem() {
        this.fontFamily = new TinyString("Helvetica".toCharArray());
        this.K = 1024;
        this.M = 1024;
        this.J = 0;
        this.L = 0;
    }
    
    public SVGFontFaceElem(final SVGFontFaceElem svgFontFaceElem) {
        super(svgFontFaceElem);
        this.fontFamily = new TinyString(svgFontFaceElem.fontFamily.data);
        this.K = svgFontFaceElem.K;
        this.M = svgFontFaceElem.M;
        this.J = svgFontFaceElem.J;
        this.L = svgFontFaceElem.L;
    }
    
    public SVGNode copyNode() {
        return new SVGFontFaceElem(this);
    }
    
    public void paint(final SVGRaster svgRaster) {
    }
    
    public int createOutline() {
        return 0;
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 5: {
                this.M = ((TinyNumber)o).val;
                break;
            }
            case 9: {
                this.L = ((TinyNumber)o).val;
                break;
            }
            case 21: {
                this.J = ((TinyNumber)o).val;
                break;
            }
            case 102: {
                this.K = ((TinyNumber)o).val;
                break;
            }
            case 28: {
                this.fontFamily = (TinyString)o;
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
        Object fontFamily = null;
        switch (n) {
            case 5: {
                n2 = this.M;
                break;
            }
            case 9: {
                n2 = this.L;
                break;
            }
            case 21: {
                n2 = this.J;
                break;
            }
            case 102: {
                n2 = this.K;
                break;
            }
            case 28: {
                fontFamily = this.fontFamily;
                break;
            }
            default: {
                return super.getAttribute(n);
            }
        }
        if (fontFamily != null) {
            return fontFamily;
        }
        return new TinyNumber(n2);
    }
}
