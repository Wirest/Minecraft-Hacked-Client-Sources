// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.HttpMessage;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.message.LineFormatter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpRequest;

@Deprecated
@NotThreadSafe
public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest>
{
    public HttpRequestWriter(final SessionOutputBuffer buffer, final LineFormatter formatter, final HttpParams params) {
        super(buffer, formatter, params);
    }
    
    @Override
    protected void writeHeadLine(final HttpRequest message) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
