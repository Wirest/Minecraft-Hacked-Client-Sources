// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.conn.routing.HttpRoute;

public interface ClientExecChain
{
    CloseableHttpResponse execute(final HttpRoute p0, final HttpRequestWrapper p1, final HttpClientContext p2, final HttpExecutionAware p3) throws IOException, HttpException;
}
