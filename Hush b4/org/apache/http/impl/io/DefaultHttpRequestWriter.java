// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.HttpMessage;
import java.io.IOException;
import org.apache.http.message.LineFormatter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpRequest;

@NotThreadSafe
public class DefaultHttpRequestWriter extends AbstractMessageWriter<HttpRequest>
{
    public DefaultHttpRequestWriter(final SessionOutputBuffer buffer, final LineFormatter formatter) {
        super(buffer, formatter);
    }
    
    public DefaultHttpRequestWriter(final SessionOutputBuffer buffer) {
        this(buffer, null);
    }
    
    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
