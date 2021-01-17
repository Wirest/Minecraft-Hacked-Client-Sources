// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.ParseException;
import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.RequestLine;
import org.apache.http.message.ParserCursor;
import org.apache.http.ConnectionClosedException;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.message.LineParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.HttpRequestFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpMessage;

@Deprecated
@NotThreadSafe
public class HttpRequestParser extends AbstractMessageParser<HttpMessage>
{
    private final HttpRequestFactory requestFactory;
    private final CharArrayBuffer lineBuf;
    
    public HttpRequestParser(final SessionInputBuffer buffer, final LineParser parser, final HttpRequestFactory requestFactory, final HttpParams params) {
        super(buffer, parser, params);
        this.requestFactory = Args.notNull(requestFactory, "Request factory");
        this.lineBuf = new CharArrayBuffer(128);
    }
    
    @Override
    protected HttpMessage parseHead(final SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
        this.lineBuf.clear();
        final int i = sessionBuffer.readLine(this.lineBuf);
        if (i == -1) {
            throw new ConnectionClosedException("Client closed connection");
        }
        final ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
        final RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
        return this.requestFactory.newHttpRequest(requestline);
    }
}
