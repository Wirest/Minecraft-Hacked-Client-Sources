// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;
import java.io.InputStream;
import com.tinyline.tiny2d.TinyVector;

public class SVGParser implements XMLHandler
{
    int new;
    SVGAttr do;
    SVGDocument if;
    SVGNode a;
    int for;
    int byte;
    TinyVector int;
    XMLParser try;
    
    public SVGParser(final SVGAttr do1) {
        (this.try = new b()).setXMLHandler(this);
        this.int = new TinyVector(10);
        this.do = do1;
    }
    
    public static XMLParser createXMLParser() {
        return new b();
    }
    
    public final int load(final SVGDocument if1, final InputStream inputStream) {
        int error = 0;
        this.new = 0;
        final int n = 0;
        this.byte = n;
        this.for = n;
        this.try.init();
        this.try.setInputStream(inputStream);
        this.if = if1;
        this.a = this.if.root;
        this.int.count = 0;
        this.a(this.a);
        do {
            this.try.getNext();
            if (this.new != 0) {
                break;
            }
            error = this.try.getError();
            if (error != 0) {
                break;
            }
        } while (this.try.getType() != 8);
        if ((this.new != 0 || error != 0) && error != 0) {
            this.new |= error << 10;
        }
        return this.new;
    }
    
    public void startDocument() {
    }
    
    public void endDocument() {
    }
    
    public void startElement(final char[] array, final int n, final int n2) {
        this.for = TinyString.getIndex(SVG.ELEMENTS, array, n, n2);
        if (this.for != 30) {
            this.a = this.if.createElement(this.for);
        }
        switch (this.a.helem) {
            case 30: {}
            default: {
                final SVGNode svgNode = (SVGNode)this.if();
                if (svgNode == null) {
                    this.new |= 0x10;
                    return;
                }
                svgNode.addChild(this.a, -1);
                this.a(this.a);
            }
        }
    }
    
    public void endElement() {
        if (this.a.helem == 9) {
            final SVGFontElem svgFontElem = (SVGFontElem)this.a;
            svgFontElem.init();
            SVGDocument.addFont(this.if, svgFontElem);
        }
        if (this.a.helem != 30) {
            this.a = (SVGNode)this.a();
        }
        if (this.a == null) {
            this.new |= 0x10;
        }
    }
    
    public void attributeName(final char[] array, final int n, final int n2) {
        this.byte = TinyString.getIndex(SVG.ATTRIBUTES, array, n, n2);
    }
    
    public void attributeValue(final char[] array, final int n, final int n2) {
        final int n3 = 0;
        final Object attributeValue = this.do.attributeValue(this.for, this.byte, array, n, n2);
        try {
            final int n4 = n3 | this.a.setAttribute(this.byte, attributeValue);
        }
        catch (Exception ex) {}
    }
    
    public void charData(final char[] array, final int n, final int n2) {
        if (this.a.helem == 32) {
            ((SVGTextElem)this.a).setText(array, 0, n2);
        }
    }
    
    private final Object a(final Object o) {
        this.int.addElement(o);
        return o;
    }
    
    private final Object a() {
        final int count = this.int.count;
        final Object if1 = this.if();
        this.int.removeElementAt(count - 1);
        return if1;
    }
    
    private final Object if() {
        final int count = this.int.count;
        if (count == 0) {
            return null;
        }
        return this.int.data[count - 1];
    }
}
