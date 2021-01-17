// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.HttpMessage;
import java.io.IOException;
import org.apache.http.message.LineFormatter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpResponse;

@NotThreadSafe
public class DefaultHttpResponseWriter extends AbstractMessageWriter<HttpResponse>
{
    public DefaultHttpResponseWriter(final SessionOutputBuffer buffer, final LineFormatter formatter) {
        super(buffer, formatter);
    }
    
    public DefaultHttpResponseWriter(final SessionOutputBuffer buffer) {
        super(buffer, null);
    }
    
    @Override
    protected void writeHeadLine(final HttpResponse message) throws IOException {
        this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
