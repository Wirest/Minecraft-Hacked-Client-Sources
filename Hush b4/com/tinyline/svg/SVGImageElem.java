// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyBitmap;
import com.tinyline.tiny2d.TinyString;

public class SVGImageElem extends SVGNode
{
    public int x;
    public int y;
    public int width;
    public int height;
    public TinyString xlink_href;
    private SVGPathElem B;
    private TinyBitmap G;
    private TinyString H;
    private static ImageLoader C;
    private static TinyString E;
    private static TinyString D;
    private static final char[] F;
    private static final int[] I;
    
    SVGImageElem() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.xlink_href = null;
        this.B = null;
        this.G = null;
        this.H = new TinyString("".toCharArray());
    }
    
    public SVGImageElem(final SVGImageElem svgImageElem) {
        super(svgImageElem);
        this.x = svgImageElem.x;
        this.y = svgImageElem.y;
        this.width = svgImageElem.width;
        this.height = svgImageElem.height;
        if (svgImageElem.xlink_href != null) {
            this.xlink_href = new TinyString(svgImageElem.xlink_href.data);
        }
        this.B = null;
        this.G = svgImageElem.G;
        this.H = new TinyString("".toCharArray());
    }
    
    public SVGNode copyNode() {
        return new SVGImageElem(this);
    }
    
    public static void setImageLoader(final ImageLoader c) {
        SVGImageElem.C = c;
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
            case 107: {
                this.width = ((TinyNumber)o).val;
                break;
            }
            case 41: {
                this.height = ((TinyNumber)o).val;
                break;
            }
            case 115: {
                this.xlink_href = (TinyString)o;
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
        Object xlink_href = null;
        switch (n) {
            case 109: {
                n2 = this.x;
                break;
            }
            case 123: {
                n2 = this.y;
                break;
            }
            case 107: {
                n2 = this.width;
                break;
            }
            case 41: {
                n2 = this.height;
                break;
            }
            case 115: {
                xlink_href = this.xlink_href;
                break;
            }
            default: {
                return super.getAttribute(n);
            }
        }
        if (xlink_href != null) {
            return xlink_href;
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
        this.B.paint(svgRaster);
    }
    
    public TinyRect getBounds() {
        if (this.B == null) {
            return null;
        }
        return this.B.getBounds();
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        if (this.B == null) {
            return null;
        }
        return this.B.getDevBounds(svgRaster);
    }
    
    public int createOutline() {
        if (this.B == null) {
            this.B = (SVGPathElem)super.ownerDocument.createElement(23);
            (this.B.path = new TinyPath(6)).moveTo(0, 0);
            this.B.path.lineTo(25600, 0);
            this.B.path.lineTo(25600, 25600);
            this.B.path.lineTo(0, 25600);
            this.B.path.closePath();
            this.B.bounds = this.B.path.getBBox();
            this.B.outlined = true;
            this.B.fillRule = 22;
            this.B.fill = TinyColor.NONE;
            this.B.stroke = TinyColor.NONE;
        }
        if (super.children.indexOf(this.B, 0) == -1) {
            this.addChild(this.B, -1);
        }
        if (SVGImageElem.C == null || this.xlink_href == null) {
            return 2;
        }
        if (this.G == null || TinyString.compareTo(this.xlink_href.data, 0, this.xlink_href.count, this.H.data, 0, this.H.count) != 0) {
            this.G = null;
            if (this.xlink_href.startsWith(SVGImageElem.E, 0) || this.xlink_href.startsWith(SVGImageElem.D, 0)) {
                final byte[] a = a(new TinyString(this.xlink_href.data, SVGImageElem.D.count, this.xlink_href.count - SVGImageElem.D.count).data);
                this.G = SVGImageElem.C.createTinyBitmap(a, 0, a.length);
            }
            else {
                this.G = SVGImageElem.C.createTinyBitmap(this.xlink_href);
            }
            if (this.G == null || this.G.height <= 0 || this.G.width <= 0) {
                return 2;
            }
            this.H = new TinyString(this.xlink_href.data);
        }
        this.B.path.reset();
        this.B.path.moveTo(0, 0);
        this.B.path.lineTo(this.G.width << 8, 0);
        this.B.path.lineTo(this.G.width << 8, this.G.height << 8);
        this.B.path.lineTo(0, this.G.height << 8);
        this.B.path.closePath();
        this.B.bounds = this.B.path.getBBox();
        this.B.outlined = true;
        this.B.fillRule = 22;
        final TinyColor fill = new TinyColor(3, new TinyMatrix());
        fill.bitmap = this.G;
        this.B.fill = fill;
        final TinyMatrix matrix = new TinyMatrix();
        final int min = TinyUtil.min(TinyUtil.div(this.width, this.G.width << 8), TinyUtil.div(this.height, this.G.height << 8));
        matrix.scale(min, min);
        final TinyMatrix tinyMatrix = new TinyMatrix();
        tinyMatrix.translate(this.x, this.y);
        matrix.concatenate(tinyMatrix);
        this.B.transform.setMatrix(matrix);
        super.outlined = true;
        return 0;
    }
    
    static final byte[] a(final char[] array) {
        if (array == null || array.length == 0) {
            return new byte[0];
        }
        final int length = array.length;
        int n2;
        int n = n2 = 0;
        for (final char c : array) {
            if (c == '=') {
                ++n;
            }
            if (SVGImageElem.I[c] < 0) {
                ++n2;
            }
        }
        final int n3 = length - n2;
        if (n3 % 4 != 0) {
            return null;
        }
        final int n4 = (n3 * 6 >> 3) - n;
        final byte[] array2 = new byte[n4];
        int n5;
        int j = n5 = 0;
        while (j < n4) {
            int n6 = 0;
            for (int k = 0; k < 4; ++k) {
                final int n7 = SVGImageElem.I[array[n5++]];
                if (n7 >= 0) {
                    n6 |= n7 << 18 - k * 6;
                }
                else {
                    --k;
                }
            }
            array2[j++] = (byte)(n6 >> 16);
            if (j < n4) {
                array2[j++] = (byte)(n6 >> 8);
                if (j >= n4) {
                    continue;
                }
                array2[j++] = (byte)n6;
            }
        }
        return array2;
    }
    
    static {
        SVGImageElem.E = new TinyString("data:image/jpg;base64,".toCharArray());
        SVGImageElem.D = new TinyString("data:image/png;base64,".toCharArray());
        F = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        I = new int[256];
        for (int i = 0; i < 256; ++i) {
            SVGImageElem.I[i] = -1;
        }
        for (int j = 0; j < 64; ++j) {
            SVGImageElem.I[SVGImageElem.F[j]] = j;
        }
        SVGImageElem.I[61] = 0;
    }
}
