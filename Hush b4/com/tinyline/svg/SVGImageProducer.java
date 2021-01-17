// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

public interface SVGImageProducer
{
    boolean hasConsumer();
    
    void sendPixels();
    
    void imageComplete();
}
