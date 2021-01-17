// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory<HttpResponse>
{
    public static final DefaultHttpResponseWriterFactory INSTANCE;
    private final LineFormatter lineFormatter;
    
    public DefaultHttpResponseWriterFactory(final LineFormatter lineFormatter) {
        this.lineFormatter = ((lineFormatter != null) ? lineFormatter : BasicLineFormatter.INSTANCE);
    }
    
    public DefaultHttpResponseWriterFactory() {
        this(null);
    }
    
    public HttpMessageWriter<HttpResponse> create(final SessionOutputBuffer buffer) {
        return new DefaultHttpResponseWriter(buffer, this.lineFormatter);
    }
    
    static {
        INSTANCE = new DefaultHttpResponseWriterFactory();
    }
}
