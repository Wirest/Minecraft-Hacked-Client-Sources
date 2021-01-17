// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.Header;
import java.io.InterruptedIOException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;

@Immutable
public class ServiceUnavailableRetryExec implements ClientExecChain
{
    private final Log log;
    private final ClientExecChain requestExecutor;
    private final ServiceUnavailableRetryStrategy retryStrategy;
    
    public ServiceUnavailableRetryExec(final ClientExecChain requestExecutor, final ServiceUnavailableRetryStrategy retryStrategy) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(retryStrategy, "Retry strategy");
        this.requestExecutor = requestExecutor;
        this.retryStrategy = retryStrategy;
    }
    
    public CloseableHttpResponse execute(final HttpRoute route, final HttpRequestWrapper request, final HttpClientContext context, final HttpExecutionAware execAware) throws IOException, HttpException {
        final Header[] origheaders = request.getAllHeaders();
        int c = 1;
        while (true) {
            final CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
            try {
                if (!this.retryStrategy.retryRequest(response, c, context)) {
                    return response;
                }
                response.close();
                final long nextInterval = this.retryStrategy.getRetryInterval();
                if (nextInterval > 0L) {
                    try {
                        this.log.trace("Wait for " + nextInterval);
                        Thread.sleep(nextInterval);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                request.setHeaders(origheaders);
            }
            catch (RuntimeException ex) {
                response.close();
                throw ex;
            }
            ++c;
        }
    }
}
