// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyVector;
import com.tinyline.tiny2d.TinyHash;

public class SVGDocument
{
    int if;
    int for;
    boolean do;
    public SVGNode root;
    public SVGRaster renderer;
    public TinyHash fontTable;
    public static SVGFontElem defaultFont;
    public int nActiveAnimations;
    public TinyVector animTargets;
    public int currentTime;
    public AnimationCallback acb;
    public TinyVector linkTargets;
    public int linkIndex;
    private boolean a;
    
    public SVGDocument() {
        this.a = false;
        this.if = 0;
        this.for = 0;
        this.do = false;
        this.root = this.createElement(30);
        this.renderer = null;
        this.fontTable = new TinyHash(1, 11);
        this.nActiveAnimations = 0;
        this.animTargets = new TinyVector(4);
        this.linkTargets = new TinyVector(4);
        this.linkIndex = 0;
    }
    
    public SVGNode createElement(final int n) {
        SVGNode svgNode = new SVGUnknownElem();
        switch (n) {
            case 0:
            case 14: {
                svgNode = new SVGGroupElem();
                break;
            }
            case 30: {
                svgNode = new SVGSVGElem();
                break;
            }
            case 6: {
                svgNode = new SVGGroupElem();
                svgNode.display = 35;
                break;
            }
            case 34: {
                svgNode = new SVGUseElem();
                break;
            }
            case 31: {
                svgNode = new SVGGroupElem();
                svgNode.visibility = 24;
                break;
            }
            case 5:
            case 8: {
                svgNode = new SVGEllipseElem();
                break;
            }
            case 18: {
                svgNode = new SVGLineElem();
                break;
            }
            case 24:
            case 25: {
                svgNode = new SVGPolygonElem();
                break;
            }
            case 27: {
                svgNode = new SVGRectElem();
                break;
            }
            case 23: {
                svgNode = new SVGPathElem();
                break;
            }
            case 17: {
                svgNode = new SVGImageElem();
                break;
            }
            case 32: {
                svgNode = new SVGTextElem();
                break;
            }
            case 9: {
                svgNode = new SVGFontElem();
                break;
            }
            case 10: {
                svgNode = new SVGFontFaceElem();
                break;
            }
            case 15:
            case 21: {
                svgNode = new SVGGlyphElem();
                break;
            }
            case 22: {
                svgNode = new SVGMPathElem();
                break;
            }
            case 29: {
                svgNode = new SVGStopElem();
                svgNode.visibility = 24;
                break;
            }
            case 19:
            case 26: {
                svgNode = new SVGGradientElem();
                svgNode.helem = n;
                ((SVGGradientElem)svgNode).setDefaults();
                svgNode.visibility = 24;
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 28: {
                svgNode = new SVGAnimationElem();
                if (n == 3) {
                    ((SVGAnimationElem)svgNode).calcMode = 40;
                }
                svgNode.display = 35;
                break;
            }
        }
        svgNode.helem = n;
        svgNode.ownerDocument = this;
        return svgNode;
    }
    
    public static int addFont(final SVGDocument svgDocument, final SVGFontElem svgFontElem) {
        if (svgFontElem == null) {
            return 2;
        }
        svgDocument.fontTable.put(svgFontElem.else.fontFamily, svgFontElem);
        return 0;
    }
    
    public static SVGFontElem getFont(final SVGDocument svgDocument, final TinyString tinyString) {
        if (svgDocument == null || tinyString == null) {
            return null;
        }
        SVGFontElem defaultFont = (SVGFontElem)svgDocument.fontTable.get(tinyString);
        if (defaultFont == null) {
            defaultFont = SVGDocument.defaultFont;
        }
        return defaultFont;
    }
    
    public boolean isZoomAndPanAnable() {
        return this.root != null && ((SVGSVGElem)this.root).zoomAndPan != 19;
    }
    
    public void addLinks(final SVGNode svgNode) {
        if (svgNode.helem == 0 && ((SVGGroupElem)svgNode).xlink_href != null) {
            this.linkTargets.addElement(svgNode);
        }
        else {
            for (int count = svgNode.children.count, i = 0; i < count; ++i) {
                final SVGNode svgNode2 = (SVGNode)svgNode.children.data[i];
                if (svgNode2 != null) {
                    this.addLinks(svgNode2);
                }
            }
        }
    }
    
    public void addAnimations(final SVGNode svgNode) {
        switch (svgNode.helem) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 28: {
                this.a((SVGAnimationElem)svgNode);
                break;
            }
            default: {
                for (int count = svgNode.children.count, i = 0; i < count; ++i) {
                    final SVGNode svgNode2 = (SVGNode)svgNode.children.data[i];
                    if (svgNode2 != null) {
                        this.addAnimations(svgNode2);
                    }
                }
                break;
            }
        }
    }
    
    private void a(final SVGAnimationElem svgAnimationElem) {
        if (svgAnimationElem == null) {
            return;
        }
        if (svgAnimationElem.createOutline() != 0) {
            return;
        }
        final int index = this.animTargets.indexOf(svgAnimationElem.goto, 0);
        SVGNode goto1;
        if (index == -1) {
            final int count = this.animTargets.count;
            goto1 = svgAnimationElem.goto;
            this.animTargets.addElement(goto1);
        }
        else {
            goto1 = (SVGNode)this.animTargets.data[index];
        }
        if (goto1 == null) {
            return;
        }
        final int attributeName = svgAnimationElem.attributeName;
        final TinyVector animatedVals = goto1.animatedVals;
        boolean b = false;
        for (int i = 0; i < animatedVals.count; ++i) {
            final c c = (c)animatedVals.data[i];
            if (c.do == attributeName) {
                c.a(svgAnimationElem);
                b = true;
                break;
            }
        }
        if (!b) {
            final c c2 = new c(attributeName, svgAnimationElem.b);
            c2.a(svgAnimationElem);
            animatedVals.addElement(c2);
        }
    }
    
    public int getCurrentTime() {
        return this.currentTime;
    }
    
    public TinyRect animate(final int currentTime) {
        this.currentTime = currentTime;
        final TinyRect tinyRect = new TinyRect();
        tinyRect.setEmpty();
        this.nActiveAnimations = 0;
        for (int i = 0; i < this.animTargets.count; ++i) {
            final SVGNode svgNode = (SVGNode)this.animTargets.data[i];
            tinyRect.union(svgNode.getDevBounds(this.renderer));
            final TinyVector animatedVals = svgNode.animatedVals;
            for (int count = animatedVals.count, j = 0; j < count; ++j) {
                final c c = (c)animatedVals.data[j];
                final Object a = c.a();
                if (a != null) {
                    try {
                        svgNode.setAttribute(c.do, a);
                        svgNode.createOutline();
                    }
                    catch (Exception ex) {}
                }
            }
            tinyRect.union(svgNode.getDevBounds(this.renderer));
        }
        return tinyRect;
    }
    
    public void postSMILEvent(final int n, final TinyString tinyString) {
        if (this.acb != null) {
            this.acb.postSMILEvent(n, tinyString);
        }
    }
    
    public boolean resolveEventBased(final TinyString tinyString) {
        if (tinyString == null) {
            return false;
        }
        this.a = false;
        this.a(this.root, tinyString, false);
        return this.a;
    }
    
    public boolean resolveLinkBased(final TinyString tinyString) {
        if (tinyString == null) {
            return false;
        }
        this.a = false;
        this.a(this.root, tinyString, true);
        return this.a;
    }
    
    private void a(final SVGNode svgNode, final TinyString tinyString, final boolean b) {
        if (svgNode.animatedVals != null) {
            final TinyVector animatedVals = svgNode.animatedVals;
            for (int i = 0; i < animatedVals.count; ++i) {
                final c c = (c)animatedVals.data[0];
                for (int j = 0; j < c.for.count; ++j) {
                    final SVGAnimationElem svgAnimationElem = (SVGAnimationElem)c.for.data[j];
                    if (b) {
                        if (svgAnimationElem.id != null && svgAnimationElem.id.equals(tinyString)) {
                            svgAnimationElem.beginElementAt(0);
                            this.a = true;
                        }
                    }
                    else {
                        if (svgAnimationElem.begin.idValue != null && svgAnimationElem.begin.idValue.equals(tinyString)) {
                            svgAnimationElem.beginElementAt(0);
                            this.a = true;
                        }
                        if (svgAnimationElem.end.idValue != null && svgAnimationElem.end.idValue.equals(tinyString)) {
                            svgAnimationElem.endElementAt(0);
                            this.a = true;
                        }
                    }
                }
            }
        }
        if (svgNode instanceof SVGGroupElem) {
            for (int count = svgNode.children.count, k = 0; k < count; ++k) {
                final SVGNode svgNode2 = (SVGNode)svgNode.children.data[k];
                if (svgNode2 != null) {
                    this.a(svgNode2, tinyString, b);
                }
            }
        }
    }
}
