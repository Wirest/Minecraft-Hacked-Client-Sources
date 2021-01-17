// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpConnectionMetrics;

@NotThreadSafe
public class HttpConnectionMetricsImpl implements HttpConnectionMetrics
{
    public static final String REQUEST_COUNT = "http.request-count";
    public static final String RESPONSE_COUNT = "http.response-count";
    public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
    public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
    private final HttpTransportMetrics inTransportMetric;
    private final HttpTransportMetrics outTransportMetric;
    private long requestCount;
    private long responseCount;
    private Map<String, Object> metricsCache;
    
    public HttpConnectionMetricsImpl(final HttpTransportMetrics inTransportMetric, final HttpTransportMetrics outTransportMetric) {
        this.requestCount = 0L;
        this.responseCount = 0L;
        this.inTransportMetric = inTransportMetric;
        this.outTransportMetric = outTransportMetric;
    }
    
    public long getReceivedBytesCount() {
        if (this.inTransportMetric != null) {
            return this.inTransportMetric.getBytesTransferred();
        }
        return -1L;
    }
    
    public long getSentBytesCount() {
        if (this.outTransportMetric != null) {
            return this.outTransportMetric.getBytesTransferred();
        }
        return -1L;
    }
    
    public long getRequestCount() {
        return this.requestCount;
    }
    
    public void incrementRequestCount() {
        ++this.requestCount;
    }
    
    public long getResponseCount() {
        return this.responseCount;
    }
    
    public void incrementResponseCount() {
        ++this.responseCount;
    }
    
    public Object getMetric(final String metricName) {
        Object value = null;
        if (this.metricsCache != null) {
            value = this.metricsCache.get(metricName);
        }
        if (value == null) {
            if ("http.request-count".equals(metricName)) {
                value = this.requestCount;
            }
            else if ("http.response-count".equals(metricName)) {
                value = this.responseCount;
            }
            else if ("http.received-bytes-count".equals(metricName)) {
                if (this.inTransportMetric != null) {
                    return this.inTransportMetric.getBytesTransferred();
                }
                return null;
            }
            else if ("http.sent-bytes-count".equals(metricName)) {
                if (this.outTransportMetric != null) {
                    return this.outTransportMetric.getBytesTransferred();
                }
                return null;
            }
        }
        return value;
    }
    
    public void setMetric(final String metricName, final Object obj) {
        if (this.metricsCache == null) {
            this.metricsCache = new HashMap<String, Object>();
        }
        this.metricsCache.put(metricName, obj);
    }
    
    public void reset() {
        if (this.outTransportMetric != null) {
            this.outTransportMetric.reset();
        }
        if (this.inTransportMetric != null) {
            this.inTransportMetric.reset();
        }
        this.requestCount = 0L;
        this.responseCount = 0L;
        this.metricsCache = null;
    }
}
