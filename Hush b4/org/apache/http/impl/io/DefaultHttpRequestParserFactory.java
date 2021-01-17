// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.io.HttpMessageParser;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.message.BasicLineParser;
import org.apache.http.HttpRequestFactory;
import org.apache.http.message.LineParser;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageParserFactory;

@Immutable
public class DefaultHttpRequestParserFactory implements HttpMessageParserFactory<HttpRequest>
{
    public static final DefaultHttpRequestParserFactory INSTANCE;
    private final LineParser lineParser;
    private final HttpRequestFactory requestFactory;
    
    public DefaultHttpRequestParserFactory(final LineParser lineParser, final HttpRequestFactory requestFactory) {
        this.lineParser = ((lineParser != null) ? lineParser : BasicLineParser.INSTANCE);
        this.requestFactory = ((requestFactory != null) ? requestFactory : DefaultHttpRequestFactory.INSTANCE);
    }
    
    public DefaultHttpRequestParserFactory() {
        this(null, null);
    }
    
    public HttpMessageParser<HttpRequest> create(final SessionInputBuffer buffer, final MessageConstraints constraints) {
        return new DefaultHttpRequestParser(buffer, this.lineParser, this.requestFactory, constraints);
    }
    
    static {
        INSTANCE = new DefaultHttpRequestParserFactory();
    }
}
