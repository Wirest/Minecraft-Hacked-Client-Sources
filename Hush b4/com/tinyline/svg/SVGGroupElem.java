// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.i;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

public class SVGGroupElem extends SVGNode
{
    public TinyString xlink_href;
    public boolean showBounds;
    
    SVGGroupElem() {
    }
    
    public SVGGroupElem(final SVGGroupElem svgGroupElem) {
        super(svgGroupElem);
        if (svgGroupElem.xlink_href != null) {
            this.xlink_href = new TinyString(svgGroupElem.xlink_href.data);
        }
        this.showBounds = svgGroupElem.showBounds;
    }
    
    public SVGNode copyNode() {
        return new SVGGroupElem(this);
    }
    
    public int setAttribute(final int n, final Object o) throws Exception {
        super.outlined = false;
        switch (n) {
            case 115: {
                this.xlink_href = (TinyString)o;
                return 0;
            }
            default: {
                return super.setAttribute(n, o);
            }
        }
    }
    
    public Object getAttribute(final int n) {
        final int n2 = 0;
        switch (n) {
            case 115: {
                final TinyString xlink_href = this.xlink_href;
                if (xlink_href != null) {
                    return xlink_href;
                }
                return new TinyNumber(n2);
            }
            default: {
                return super.getAttribute(n);
            }
        }
    }
    
    public int createOutline() {
        final int count = super.children.count;
        if (super.helem == 31) {
            for (int i = 0; i < count; ++i) {
                final SVGNode svgNode = (SVGNode)super.children.data[i];
                if (svgNode != null) {
                    final SVGNode svgNode2 = svgNode;
                    svgNode2.visibility = 24;
                    if (svgNode2.matchUserAgent()) {
                        svgNode2.visibility = 58;
                        break;
                    }
                }
            }
        }
        else {
            for (int j = 0; j < count; ++j) {
                final SVGNode svgNode3 = (SVGNode)super.children.data[j];
                if (svgNode3 != null && svgNode3.isDisplay()) {
                    svgNode3.createOutline();
                }
            }
        }
        super.outlined = true;
        return 0;
    }
    
    public void paint(final SVGRaster svgRaster) {
        if (!super.outlined) {
            this.createOutline();
        }
        final int count = super.children.count;
        if (count == 0 || !this.isDisplay()) {
            return;
        }
        for (int i = 0; i < count; ++i) {
            final SVGNode svgNode = (SVGNode)super.children.data[i];
            if (svgNode != null) {
                svgNode.paint(svgRaster);
            }
        }
        if (this.showBounds && super.helem == 0) {
            this.a(svgRaster);
        }
    }
    
    public SVGNode nodeHitAt(final SVGRaster svgRaster, final TinyPoint tinyPoint) {
        final int count = super.children.count;
        if (count > 0) {
            for (int i = count - 1; i >= 0; --i) {
                final SVGNode svgNode = (SVGNode)super.children.data[i];
                if (svgNode != null) {
                    final SVGNode nodeHit = svgNode.nodeHitAt(svgRaster, tinyPoint);
                    if (nodeHit != null) {
                        return nodeHit;
                    }
                }
            }
        }
        return null;
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        final int count = super.children.count;
        final TinyRect tinyRect = new TinyRect();
        for (int i = 0; i < count; ++i) {
            final SVGNode svgNode = (SVGNode)super.children.data[i];
            if (svgNode != null) {
                final TinyRect devBounds = svgNode.getDevBounds(svgRaster);
                if (devBounds != null) {
                    tinyRect.union(devBounds);
                }
            }
        }
        return tinyRect;
    }
    
    private void a(final SVGRaster svgRaster) {
        final TinyRect devBounds = this.getDevBounds(svgRaster);
        if (devBounds == null || devBounds.isEmpty()) {
            return;
        }
        final TinyPath rectToPath = TinyPath.rectToPath(devBounds.xmin << 8, devBounds.ymin << 8, devBounds.xmax << 8, devBounds.ymax << 8);
        final i if1 = svgRaster.if;
        if1.byte(2);
        if1.a(TinyColor.NONE);
        if1.if(new TinyColor(-1543503617));
        if1.case(512);
        if1.a(new TinyMatrix());
        if1.a(svgRaster.isAntialiased);
        if1.a(svgRaster.clipRect);
        if1.do(this.getDevBounds(svgRaster));
        if1.a(rectToPath);
    }
}
