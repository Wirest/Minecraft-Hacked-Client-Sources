// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.net.UnknownHostException;
import java.util.Arrays;
import org.apache.http.util.Args;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.LogFactory;
import java.net.InetAddress;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.http.conn.DnsResolver;

public class InMemoryDnsResolver implements DnsResolver
{
    private final Log log;
    private final Map<String, InetAddress[]> dnsMap;
    
    public InMemoryDnsResolver() {
        this.log = LogFactory.getLog(InMemoryDnsResolver.class);
        this.dnsMap = new ConcurrentHashMap<String, InetAddress[]>();
    }
    
    public void add(final String host, final InetAddress... ips) {
        Args.notNull(host, "Host name");
        Args.notNull(ips, "Array of IP addresses");
        this.dnsMap.put(host, ips);
    }
    
    public InetAddress[] resolve(final String host) throws UnknownHostException {
        final InetAddress[] resolvedAddresses = this.dnsMap.get(host);
        if (this.log.isInfoEnabled()) {
            this.log.info("Resolving " + host + " to " + Arrays.deepToString(resolvedAddresses));
        }
        if (resolvedAddresses == null) {
            throw new UnknownHostException(host + " cannot be resolved");
        }
        return resolvedAddresses;
    }
}
