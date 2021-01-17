// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.message.ParserCursor;
import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.config.MessageConstraints;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.message.LineParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpResponse;

@NotThreadSafe
public class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse>
{
    private final HttpResponseFactory responseFactory;
    private final CharArrayBuffer lineBuf;
    
    @Deprecated
    public DefaultHttpResponseParser(final SessionInputBuffer buffer, final LineParser lineParser, final HttpResponseFactory responseFactory, final HttpParams params) {
        super(buffer, lineParser, params);
        this.responseFactory = Args.notNull(responseFactory, "Response factory");
        this.lineBuf = new CharArrayBuffer(128);
    }
    
    public DefaultHttpResponseParser(final SessionInputBuffer buffer, final LineParser lineParser, final HttpResponseFactory responseFactory, final MessageConstraints constraints) {
        super(buffer, lineParser, constraints);
        this.responseFactory = ((responseFactory != null) ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
        this.lineBuf = new CharArrayBuffer(128);
    }
    
    public DefaultHttpResponseParser(final SessionInputBuffer buffer, final MessageConstraints constraints) {
        this(buffer, null, null, constraints);
    }
    
    public DefaultHttpResponseParser(final SessionInputBuffer buffer) {
        this(buffer, null, null, MessageConstraints.DEFAULT);
    }
    
    @Override
    protected HttpResponse parseHead(final SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
        this.lineBuf.clear();
        final int i = sessionBuffer.readLine(this.lineBuf);
        if (i == -1) {
            throw new NoHttpResponseException("The target server failed to respond");
        }
        final ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
        final StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
        return this.responseFactory.newHttpResponse(statusline, null);
    }
}
