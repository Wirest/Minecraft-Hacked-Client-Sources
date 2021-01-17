// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.io.IOException;

public interface HttpServerConnection extends HttpConnection
{
    HttpRequest receiveRequestHeader() throws HttpException, IOException;
    
    void receiveRequestEntity(final HttpEntityEnclosingRequest p0) throws HttpException, IOException;
    
    void sendResponseHeader(final HttpResponse p0) throws HttpException, IOException;
    
    void sendResponseEntity(final HttpResponse p0) throws HttpException, IOException;
    
    void flush() throws IOException;
}
