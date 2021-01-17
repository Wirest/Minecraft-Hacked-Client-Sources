// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import java.io.IOException;

public interface Attribute extends HttpData
{
    String getValue() throws IOException;
    
    void setValue(final String p0) throws IOException;
    
    Attribute copy();
    
    Attribute duplicate();
    
    Attribute retain();
    
    Attribute retain(final int p0);
}
