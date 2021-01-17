// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.utils;

import java.io.Closeable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.HttpEntity;
import java.io.IOException;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;

public class HttpClientUtils
{
    private HttpClientUtils() {
    }
    
    public static void closeQuietly(final HttpResponse response) {
        if (response != null) {
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public static void closeQuietly(final CloseableHttpResponse response) {
        if (response != null) {
            try {
                try {
                    EntityUtils.consume(response.getEntity());
                }
                finally {
                    response.close();
                }
            }
            catch (IOException ex) {}
        }
    }
    
    public static void closeQuietly(final HttpClient httpClient) {
        if (httpClient != null && httpClient instanceof Closeable) {
            try {
                ((Closeable)httpClient).close();
            }
            catch (IOException ex) {}
        }
    }
}
