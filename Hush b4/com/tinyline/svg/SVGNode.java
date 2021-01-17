// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyVector;

public abstract class SVGNode
{
    public int helem;
    public SVGDocument ownerDocument;
    public SVGNode parent;
    public TinyVector children;
    public TinyVector animatedVals;
    public TinyString id;
    public int xmlSpace;
    public TinyColor color;
    public TinyColor fill;
    public int fillRule;
    public TinyColor stroke;
    public int[] strokeDashArray;
    public int strokeDashOffset;
    public int strokeLineCap;
    public int strokeLineJoin;
    public int strokeMiterLimit;
    public int strokeWidth;
    public int visibility;
    public int display;
    public TinyString fontFamily;
    public int fontSize;
    public int fontStyle;
    public int fontWeight;
    public int textAnchor;
    public int fillOpacity;
    public int stopOpacity;
    public int strokeOpacity;
    public int opacity;
    public TinyColor stopColor;
    public TinyTransform transform;
    TinyString do;
    TinyString if;
    TinyString a;
    public TinyRect bounds;
    public boolean outlined;
    
    public SVGNode() {
        this.helem = 35;
        this.ownerDocument = null;
        this.parent = null;
        this.children = new TinyVector(4);
        this.id = null;
        this.animatedVals = new TinyVector(4);
        this.xmlSpace = 18;
        this.color = TinyColor.INHERIT;
        this.fill = TinyColor.INHERIT;
        this.fillRule = 26;
        this.stroke = TinyColor.INHERIT;
        this.stopColor = TinyColor.INHERIT;
        this.strokeDashArray = SVG.VAL_STROKEDASHARRAYINHERIT;
        this.strokeDashOffset = Integer.MIN_VALUE;
        this.strokeLineCap = 26;
        this.strokeLineJoin = 26;
        this.strokeMiterLimit = Integer.MIN_VALUE;
        this.strokeWidth = Integer.MIN_VALUE;
        this.stopOpacity = Integer.MIN_VALUE;
        this.strokeOpacity = Integer.MIN_VALUE;
        this.fillOpacity = Integer.MIN_VALUE;
        this.opacity = Integer.MIN_VALUE;
        this.visibility = 26;
        this.display = 26;
        this.fontFamily = null;
        this.fontSize = Integer.MIN_VALUE;
        this.fontStyle = 26;
        this.fontWeight = 26;
        this.textAnchor = 26;
        this.transform = new TinyTransform();
    }
    
    public SVGNode(final SVGNode svgNode) {
        this.helem = svgNode.helem;
        this.ownerDocument = svgNode.ownerDocument;
        this.parent = null;
        this.id = null;
        this.children = new TinyVector(4);
        for (int count = svgNode.children.count, i = 0; i < count; ++i) {
            final SVGNode svgNode2 = (SVGNode)svgNode.children.data[i];
            if (svgNode2 != null) {
                this.addChild(svgNode2.copyNode(), -1);
            }
        }
        this.animatedVals = new TinyVector(4);
        this.color = svgNode.color.copyColor();
        this.fill = svgNode.fill.copyColor();
        this.fillRule = svgNode.fillRule;
        this.stroke = svgNode.stroke.copyColor();
        this.stopColor = svgNode.stopColor.copyColor();
        if (svgNode.strokeDashArray != null) {
            this.strokeDashArray = svgNode.strokeDashArray;
            this.strokeDashArray = new int[svgNode.strokeDashArray.length];
            System.arraycopy(svgNode.strokeDashArray, 0, this.strokeDashArray, 0, this.strokeDashArray.length);
        }
        this.strokeDashOffset = svgNode.strokeDashOffset;
        this.strokeLineCap = svgNode.strokeLineCap;
        this.strokeLineJoin = svgNode.strokeLineJoin;
        this.strokeMiterLimit = svgNode.strokeMiterLimit;
        this.strokeWidth = svgNode.strokeWidth;
        this.fillOpacity = svgNode.fillOpacity;
        this.stopOpacity = svgNode.stopOpacity;
        this.strokeOpacity = svgNode.strokeOpacity;
        this.opacity = svgNode.opacity;
        this.visibility = svgNode.visibility;
        this.display = svgNode.display;
        if (svgNode.fontFamily != null) {
            this.fontFamily = new TinyString(svgNode.fontFamily.data);
        }
        this.fontSize = svgNode.fontSize;
        this.fontStyle = svgNode.fontStyle;
        this.fontWeight = svgNode.fontWeight;
        this.textAnchor = svgNode.textAnchor;
        this.transform = new TinyTransform(svgNode.transform);
        this.do = svgNode.do;
        this.if = svgNode.if;
        this.a = svgNode.a;
    }
    
    public void addChild(final SVGNode svgNode, final int n) {
        if (n < 0) {
            this.children.addElement(svgNode);
        }
        else {
            this.children.insertElementAt(svgNode, n);
        }
        svgNode.parent = this;
        svgNode.ownerDocument = this.ownerDocument;
        this.ownerDocument.do = true;
    }
    
    public int removeChild(final int n) {
        final int removeElement = this.children.removeElementAt(n);
        if (removeElement == 0) {
            this.ownerDocument.do = true;
        }
        return removeElement;
    }
    
    public abstract void paint(final SVGRaster p0);
    
    public abstract int createOutline();
    
    public abstract SVGNode copyNode();
    
    public int setAttribute(final int n, final Object o) throws Exception {
        this.outlined = false;
        switch (n) {
            case 15: {
                this.color = (TinyColor)o;
                break;
            }
            case 25: {
                this.fill = (TinyColor)o;
                break;
            }
            case 27: {
                this.fillRule = ((TinyNumber)o).val;
                break;
            }
            case 82: {
                this.stroke = (TinyColor)o;
                break;
            }
            case 83: {
                this.strokeDashArray = (int[])o;
                break;
            }
            case 84: {
                this.strokeDashOffset = ((TinyNumber)o).val;
                break;
            }
            case 85: {
                this.strokeLineCap = ((TinyNumber)o).val;
                break;
            }
            case 86: {
                this.strokeLineJoin = ((TinyNumber)o).val;
                break;
            }
            case 87: {
                this.strokeMiterLimit = ((TinyNumber)o).val;
                break;
            }
            case 89: {
                this.strokeWidth = ((TinyNumber)o).val;
                break;
            }
            case 16: {
                break;
            }
            case 26: {
                this.fillOpacity = ((TinyNumber)o).val;
                break;
            }
            case 88: {
                this.strokeOpacity = ((TinyNumber)o).val;
                break;
            }
            case 56: {
                this.opacity = ((TinyNumber)o).val;
                break;
            }
            case 78: {
                this.stopColor = (TinyColor)o;
                break;
            }
            case 79: {
                this.stopOpacity = ((TinyNumber)o).val;
                break;
            }
            case 22: {
                this.display = ((TinyNumber)o).val;
                break;
            }
            case 106: {
                this.visibility = ((TinyNumber)o).val;
                break;
            }
            case 28: {
                this.fontFamily = (TinyString)o;
                break;
            }
            case 29: {
                this.fontSize = ((TinyNumber)o).val;
                break;
            }
            case 31: {
                break;
            }
            case 33: {
                break;
            }
            case 92: {
                this.textAnchor = ((TinyNumber)o).val;
                break;
            }
            case 94: {
                this.transform = (TinyTransform)o;
                break;
            }
            case 90: {
                this.a = (TinyString)o;
                break;
            }
            case 68: {
                this.if = (TinyString)o;
                break;
            }
            case 69: {
                this.do = (TinyString)o;
                break;
            }
            case 44: {
                this.id = (TinyString)o;
                break;
            }
            case 122: {
                this.xmlSpace = ((TinyNumber)o).val;
                break;
            }
            default: {
                return 8;
            }
        }
        return 0;
    }
    
    public Object getAttribute(final int n) {
        int n2 = 0;
        Object o = null;
        switch (n) {
            case 15: {
                o = this.color;
                break;
            }
            case 25: {
                o = this.fill;
                break;
            }
            case 27: {
                n2 = this.fillRule;
                break;
            }
            case 82: {
                o = this.stroke;
                break;
            }
            case 83: {
                o = this.strokeDashArray;
                break;
            }
            case 84: {
                n2 = this.strokeDashOffset;
                break;
            }
            case 85: {
                n2 = this.strokeLineCap;
                break;
            }
            case 86: {
                n2 = this.strokeLineJoin;
                break;
            }
            case 87: {
                n2 = this.strokeMiterLimit;
                break;
            }
            case 89: {
                n2 = this.strokeWidth;
                break;
            }
            case 16: {
                break;
            }
            case 26: {
                n2 = this.fillOpacity;
                break;
            }
            case 88: {
                n2 = this.strokeOpacity;
                break;
            }
            case 56: {
                n2 = this.opacity;
                break;
            }
            case 78: {
                o = this.stopColor;
                break;
            }
            case 79: {
                n2 = this.strokeOpacity;
                break;
            }
            case 22: {
                n2 = this.display;
                break;
            }
            case 106: {
                n2 = this.visibility;
                break;
            }
            case 28: {
                o = this.fontFamily;
                break;
            }
            case 29: {
                n2 = this.fontSize;
                break;
            }
            case 31: {
                break;
            }
            case 33: {
                break;
            }
            case 92: {
                n2 = this.textAnchor;
                break;
            }
            case 94: {
                o = this.transform;
                break;
            }
            case 90: {
                o = this.a;
                break;
            }
            case 68: {
                o = this.if;
                break;
            }
            case 69: {
                o = this.do;
                break;
            }
            case 44: {
                o = this.id;
                break;
            }
            case 122: {
                n2 = this.xmlSpace;
                break;
            }
            default: {
                return null;
            }
        }
        if (o != null) {
            return o;
        }
        return new TinyNumber(n2);
    }
    
    public boolean matchUserAgent() {
        if (this.a != null) {
            return TinyString.compareTo(this.a.data, 0, this.a.count, "en".toCharArray(), 0, 2) == 0;
        }
        return this.do != null || this.if == null;
    }
    
    public TinyColor getCurrentColor() {
        if (this.color != TinyColor.INHERIT) {
            return this.color;
        }
        return (this.parent != null) ? this.parent.getCurrentColor() : TinyColor.INHERIT;
    }
    
    public TinyColor getFillColor() {
        if (this.fill == TinyColor.CURRENT) {
            return this.getCurrentColor();
        }
        if (this.fill != TinyColor.INHERIT) {
            return this.resolveColor(this.fill);
        }
        return (this.parent != null) ? this.parent.getFillColor() : TinyColor.INHERIT;
    }
    
    public int getFillRule() {
        if (this.fillRule != 26) {
            return this.fillRule;
        }
        return (this.parent != null) ? this.parent.getFillRule() : 36;
    }
    
    public TinyColor getStrokeColor() {
        if (this.stroke == TinyColor.CURRENT) {
            return this.getCurrentColor();
        }
        if (this.stroke != TinyColor.INHERIT) {
            return this.resolveColor(this.stroke);
        }
        return (this.parent != null) ? this.parent.getStrokeColor() : TinyColor.INHERIT;
    }
    
    public TinyColor resolveColor(final TinyColor tinyColor) {
        if (tinyColor.fillType != 5) {
            return tinyColor;
        }
        final SVGNode nodeById = getNodeById(this.ownerDocument.root, tinyColor.uri);
        if (nodeById == null) {
            return tinyColor;
        }
        if (nodeById.helem == 19 || nodeById.helem == 26) {
            final SVGGradientElem svgGradientElem = (SVGGradientElem)nodeById;
            if (!svgGradientElem.outlined) {
                svgGradientElem.createOutline();
            }
            return svgGradientElem.gcolor;
        }
        return tinyColor;
    }
    
    public int[] getDashArray() {
        if (this.strokeDashArray != SVG.VAL_STROKEDASHARRAYINHERIT) {
            return this.strokeDashArray;
        }
        return (this.parent != null) ? this.parent.getDashArray() : SVG.VAL_STROKEDASHARRAYNONE;
    }
    
    public int getDashOffset() {
        if (this.strokeDashOffset != Integer.MIN_VALUE) {
            return this.strokeDashOffset;
        }
        return (this.parent != null) ? this.parent.getDashOffset() : 0;
    }
    
    public int getCapStyle() {
        if (this.strokeLineCap != 26) {
            return this.strokeLineCap;
        }
        return (this.parent != null) ? this.parent.getCapStyle() : 15;
    }
    
    public int getJoinStyle() {
        if (this.strokeLineJoin != 26) {
            return this.strokeLineJoin;
        }
        return (this.parent != null) ? this.parent.getJoinStyle() : 33;
    }
    
    public int getMiterLimit() {
        if (this.strokeMiterLimit != Integer.MIN_VALUE) {
            return this.strokeMiterLimit;
        }
        return (this.parent != null) ? this.parent.getMiterLimit() : 1024;
    }
    
    public int getLineThickness() {
        if (this.strokeWidth != Integer.MIN_VALUE) {
            return this.strokeWidth;
        }
        return (this.parent != null) ? this.parent.getLineThickness() : 256;
    }
    
    public int getFillOpacity() {
        if (this.fillOpacity != Integer.MIN_VALUE) {
            return this.fillOpacity;
        }
        return (this.parent != null) ? this.parent.getFillOpacity() : 255;
    }
    
    public int getStrokeOpacity() {
        if (this.strokeOpacity != Integer.MIN_VALUE) {
            return this.strokeOpacity;
        }
        return (this.parent != null) ? this.parent.getStrokeOpacity() : 255;
    }
    
    public int getOpacity() {
        if (this.opacity != Integer.MIN_VALUE) {
            return this.opacity;
        }
        return (this.parent != null) ? this.parent.getOpacity() : 255;
    }
    
    public TinyColor getStopColor() {
        if (this.stopColor == TinyColor.CURRENT) {
            return this.getCurrentColor();
        }
        if (this.stopColor != TinyColor.INHERIT) {
            return this.stopColor;
        }
        return (this.parent != null) ? this.parent.getStopColor() : TinyColor.INHERIT;
    }
    
    public int getStopOpacity() {
        if (this.stopOpacity != Integer.MIN_VALUE) {
            return this.stopOpacity;
        }
        return (this.parent != null) ? this.parent.getStopOpacity() : 256;
    }
    
    public int getDisplay() {
        if (this.display != 26) {
            return this.display;
        }
        return (this.parent != null) ? this.parent.getDisplay() : 27;
    }
    
    public int getVisibility() {
        if (this.visibility != 26) {
            return this.visibility;
        }
        return (this.parent != null) ? this.parent.getVisibility() : 58;
    }
    
    public int getFontSize() {
        if (this.fontSize != Integer.MIN_VALUE) {
            return this.fontSize;
        }
        return (this.parent != null) ? this.parent.getFontSize() : 3072;
    }
    
    public TinyString getFontFamily() {
        if (this.fontFamily != null) {
            return this.fontFamily;
        }
        return (this.parent != null) ? this.parent.getFontFamily() : SVG.VAL_DEFAULT_FONTFAMILY;
    }
    
    public int getTextAnchor() {
        if (this.textAnchor != 26) {
            return this.textAnchor;
        }
        return (this.parent != null) ? this.parent.getTextAnchor() : 54;
    }
    
    public boolean isVisible() {
        return this.getVisibility() == 58;
    }
    
    public boolean isDisplay() {
        return this.getDisplay() == 27;
    }
    
    public boolean contains(final SVGRaster svgRaster, final TinyPoint tinyPoint) {
        final TinyRect devBounds = this.getDevBounds(svgRaster);
        return devBounds != null && devBounds.contains(tinyPoint);
    }
    
    public boolean intersects(final SVGRaster svgRaster, final TinyRect tinyRect) {
        final TinyRect devBounds = this.getDevBounds(svgRaster);
        return devBounds != null && devBounds.intersects(tinyRect);
    }
    
    public SVGNode nodeHitAt(final SVGRaster svgRaster, final TinyPoint tinyPoint) {
        if (this.isVisible() && this.getFillColor() != TinyColor.NONE && this.contains(svgRaster, tinyPoint)) {
            return this;
        }
        return null;
    }
    
    public SVGNode seekAElem() {
        for (SVGNode parent = this; parent != null; parent = parent.parent) {
            final SVGNode parent2 = parent.parent;
            if (parent2 != null && parent2.helem == 0) {
                return parent2;
            }
        }
        return null;
    }
    
    public TinyMatrix getGlobalTransform() {
        SVGNode parent = this;
        final TinyMatrix tinyMatrix = new TinyMatrix();
        while (parent != null) {
            if (parent.transform != null) {
                tinyMatrix.concatenate(parent.transform.matrix);
            }
            parent = parent.parent;
        }
        return tinyMatrix;
    }
    
    public TinyRect getBounds() {
        if (!this.outlined || this.bounds == null) {
            this.createOutline();
        }
        return this.bounds;
    }
    
    public TinyRect getDevBounds(final SVGRaster svgRaster) {
        final TinyRect bounds = this.getBounds();
        if (bounds == null) {
            return null;
        }
        final TinyRect tinyRect = new TinyRect();
        return svgRaster.if.a(this.getGlobalTransform(), this.getLineThickness(), bounds);
    }
    
    public static SVGNode getNodeById(final SVGNode svgNode, final TinyString tinyString) {
        if (svgNode.id != null && tinyString != null && TinyString.compareTo(svgNode.id.data, 0, svgNode.id.count, tinyString.data, 0, tinyString.count) == 0) {
            return svgNode;
        }
        for (int i = 0; i < svgNode.children.count; ++i) {
            final SVGNode nodeById = getNodeById((SVGNode)svgNode.children.data[i], tinyString);
            if (nodeById != null) {
                return nodeById;
            }
        }
        return null;
    }
}
