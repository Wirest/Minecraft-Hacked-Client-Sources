// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

public class SVGUseElem extends SVGGroupElem
{
    public int x;
    public int y;
    public int width;
    public int height;
    private SVGGroupElem case;
    private TinyString byte;
    
    SVGUseElem() {
        this.case = null;
        this.byte = new TinyString("".toCharArray());
    }
    
    public SVGUseElem(final SVGUseElem svgUseElem) {
        super(svgUseElem);
        this.x = svgUseElem.x;
        this.y = svgUseElem.y;
        this.width = svgUseElem.width;
        this.height = svgUseElem.height;
        this.case = null;
        this.byte = new TinyString("".toCharArray());
    }
    
    public SVGNode copyNode() {
        return new SVGUseElem(this);
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
            case 107: {
                n2 = this.width;
                break;
            }
            case 41: {
                n2 = this.height;
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
        if (super.xlink_href == null || super.xlink_href.count == 0) {
            return 2;
        }
        if (this.case == null) {
            this.case = (SVGGroupElem)super.ownerDocument.createElement(14);
        }
        if (super.children.indexOf(this.case, 0) == -1) {
            this.addChild(this.case, -1);
        }
        SVGNode nodeById = null;
        if (TinyString.compareTo(super.xlink_href.data, 0, super.xlink_href.count, this.byte.data, 0, this.byte.count) != 0) {
            final int index = super.xlink_href.indexOf(35, 0);
            if (index != -1) {
                nodeById = SVGNode.getNodeById(super.ownerDocument.root, super.xlink_href.substring(index + 1));
            }
            if (nodeById == null) {
                return 2;
            }
            final SVGNode copyNode = nodeById.copyNode();
            this.case.children.count = 0;
            this.case.addChild(copyNode, -1);
            this.byte = new TinyString(super.xlink_href.data);
        }
        this.case.transform.setTranslate(this.x, this.y);
        this.case.createOutline();
        super.outlined = true;
        return 0;
    }
}
