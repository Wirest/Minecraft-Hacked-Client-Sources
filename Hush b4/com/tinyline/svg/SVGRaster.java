// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyPixbuf;
import com.tinyline.tiny2d.i;
import com.tinyline.tiny2d.TinyRect;

public class SVGRaster
{
    public static final int version = 110;
    public SVGDocument document;
    public SVGNode root;
    public SVGRect view;
    public SVGRect origview;
    public boolean isAntialiased;
    public TinyRect clipRect;
    public TinyRect viewport;
    i if;
    protected SVGImageProducer a;
    
    public SVGRaster(final TinyPixbuf tinyPixbuf) {
        (this.if = new i()).a(tinyPixbuf);
        this.clipRect = new TinyRect();
        this.viewport = new TinyRect(0, 0, tinyPixbuf.width, tinyPixbuf.height);
        this.document = this.createSVGDocument();
        this.root = this.document.root;
        this.document.if = tinyPixbuf.width;
        this.document.for = tinyPixbuf.height;
        this.view = new SVGRect();
        final SVGRect view = this.view;
        final SVGRect view2 = this.view;
        final int n = 0;
        view2.y = n;
        view.x = n;
        this.view.width = tinyPixbuf.width << 8;
        this.view.height = tinyPixbuf.height << 8;
        this.origview = new SVGRect(this.view);
    }
    
    public void setSVGDocument(final SVGDocument document) {
        if (document != null) {
            this.document = document;
            this.root = this.document.root;
            this.document.renderer = this;
        }
    }
    
    public SVGDocument createSVGDocument() {
        final SVGDocument svgDocument = new SVGDocument();
        final TinyPixbuf pixelBuffer = this.getPixelBuffer();
        svgDocument.if = pixelBuffer.width;
        svgDocument.for = pixelBuffer.height;
        svgDocument.renderer = this;
        svgDocument.root = svgDocument.createElement(30);
        return svgDocument;
    }
    
    public SVGDocument getSVGDocument() {
        return this.document;
    }
    
    public void setSVGImageProducer(final SVGImageProducer a) {
        this.a = a;
    }
    
    public SVGImageProducer getSVGImageProducer() {
        return this.a;
    }
    
    public void invalidate() {
        final TinyPixbuf pixelBuffer = this.getPixelBuffer();
        final TinyRect clipRect = this.clipRect;
        final TinyRect clipRect2 = this.clipRect;
        final int n = 0;
        clipRect2.ymin = n;
        clipRect.xmin = n;
        this.clipRect.xmax = pixelBuffer.width;
        this.clipRect.ymax = pixelBuffer.height;
    }
    
    public void clearRect(final TinyRect tinyRect) {
        this.if.if(tinyRect);
    }
    
    public void flush() {
        if (this.if != null) {
            this.if.d();
            this.if = null;
        }
    }
    
    public void setCamera() {
        final int min = TinyUtil.min(TinyUtil.div(TinyUtil.max(this.origview.width, 16), TinyUtil.max(this.view.width, 16)), TinyUtil.div(TinyUtil.max(this.origview.height, 16), TinyUtil.max(this.view.height, 16)));
        final int n = this.view.x + this.view.width / 2;
        final int n2 = this.origview.x + this.origview.width / 2;
        final int n3 = this.view.y + this.view.height / 2;
        final int n4 = this.origview.y + this.origview.height / 2;
        final int n5 = n2 - TinyUtil.mul(n, min);
        final int n6 = n4 - TinyUtil.mul(n3, min);
        this.invalidate();
        if (this.root != null) {
            final SVGSVGElem svgsvgElem = (SVGSVGElem)this.root;
            svgsvgElem.setCurrentScale(min);
            svgsvgElem.setCurrentTranslate(n5, n6);
            svgsvgElem.recalculateViewboxToViewportTransform();
            this.viewport = TinyTransform.getTinyMatrix(svgsvgElem.try).transformToDev(new TinyRect(0, 0, svgsvgElem.for << 8, svgsvgElem.new << 8));
        }
    }
    
    public void setBackground(final int n) {
        this.if.char(n);
    }
    
    public int getBackground() {
        return this.if.e();
    }
    
    public TinyPixbuf getPixelBuffer() {
        return this.if.c();
    }
    
    public void setAntialiased(final boolean isAntialiased) {
        this.isAntialiased = isAntialiased;
    }
    
    public boolean isAntialiased() {
        return this.isAntialiased;
    }
    
    public void setDevClip(final TinyRect tinyRect) {
        final TinyPixbuf pixelBuffer = this.getPixelBuffer();
        this.clipRect.xmin = tinyRect.xmin - 2;
        this.clipRect.xmax = tinyRect.xmax + 2;
        this.clipRect.ymin = tinyRect.ymin - 2;
        this.clipRect.ymax = tinyRect.ymax + 2;
        this.clipRect.xmin = TinyUtil.max(this.clipRect.xmin, 0);
        this.clipRect.xmax = TinyUtil.min(this.clipRect.xmax, pixelBuffer.width);
        this.clipRect.ymin = TinyUtil.max(this.clipRect.ymin, 0);
        this.clipRect.ymax = TinyUtil.min(this.clipRect.ymax, pixelBuffer.height);
        if (this.clipRect.xmin >= this.clipRect.xmax || this.clipRect.ymin >= this.clipRect.ymax) {
            this.clipRect.setEmpty();
        }
    }
    
    public TinyRect getDevClip() {
        return this.clipRect;
    }
    
    public void update() {
        this.a(true);
    }
    
    public void paint() {
        this.a(false);
    }
    
    public void sendPixels() {
        if (!this.clipRect.isEmpty() && this.a != null && this.a.hasConsumer()) {
            this.a.sendPixels();
            this.a.imageComplete();
        }
    }
    
    private final void a(final boolean b) {
        if (!this.clipRect.isEmpty()) {
            if (b) {
                this.if.if(this.clipRect);
            }
            if (this.root != null) {
                this.root.paint(this);
            }
        }
    }
}
