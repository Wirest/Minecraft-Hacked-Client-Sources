// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.net;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import com.google.common.base.Strings;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.concurrent.Immutable;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@Immutable
@GwtCompatible
public final class HostAndPort implements Serializable
{
    private static final int NO_PORT = -1;
    private final String host;
    private final int port;
    private final boolean hasBracketlessColons;
    private static final long serialVersionUID = 0L;
    
    private HostAndPort(final String host, final int port, final boolean hasBracketlessColons) {
        this.host = host;
        this.port = port;
        this.hasBracketlessColons = hasBracketlessColons;
    }
    
    public String getHostText() {
        return this.host;
    }
    
    public boolean hasPort() {
        return this.port >= 0;
    }
    
    public int getPort() {
        Preconditions.checkState(this.hasPort());
        return this.port;
    }
    
    public int getPortOrDefault(final int defaultPort) {
        return this.hasPort() ? this.port : defaultPort;
    }
    
    public static HostAndPort fromParts(final String host, final int port) {
        Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
        final HostAndPort parsedHost = fromString(host);
        Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
        return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
    }
    
    public static HostAndPort fromHost(final String host) {
        final HostAndPort parsedHost = fromString(host);
        Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
        return parsedHost;
    }
    
    public static HostAndPort fromString(final String hostPortString) {
        Preconditions.checkNotNull(hostPortString);
        String portString = null;
        boolean hasBracketlessColons = false;
        String host;
        if (hostPortString.startsWith("[")) {
            final String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
            host = hostAndPort[0];
            portString = hostAndPort[1];
        }
        else {
            final int colonPos = hostPortString.indexOf(58);
            if (colonPos >= 0 && hostPortString.indexOf(58, colonPos + 1) == -1) {
                host = hostPortString.substring(0, colonPos);
                portString = hostPortString.substring(colonPos + 1);
            }
            else {
                host = hostPortString;
                hasBracketlessColons = (colonPos >= 0);
            }
        }
        int port = -1;
        if (!Strings.isNullOrEmpty(portString)) {
            Preconditions.checkArgument(!portString.startsWith("+"), "Unparseable port number: %s", hostPortString);
            try {
                port = Integer.parseInt(portString);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
            }
            Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
        }
        return new HostAndPort(host, port, hasBracketlessColons);
    }
    
    private static String[] getHostAndPortFromBracketedHost(final String hostPortString) {
        int colonIndex = 0;
        int closeBracketIndex = 0;
        final boolean hasPort = false;
        Preconditions.checkArgument(hostPortString.charAt(0) == '[', "Bracketed host-port string must start with a bracket: %s", hostPortString);
        colonIndex = hostPortString.indexOf(58);
        closeBracketIndex = hostPortString.lastIndexOf(93);
        Preconditions.checkArgument(colonIndex > -1 && closeBracketIndex > colonIndex, "Invalid bracketed host/port: %s", hostPortString);
        final String host = hostPortString.substring(1, closeBracketIndex);
        if (closeBracketIndex + 1 == hostPortString.length()) {
            return new String[] { host, "" };
        }
        Preconditions.checkArgument(hostPortString.charAt(closeBracketIndex + 1) == ':', "Only a colon may follow a close bracket: %s", hostPortString);
        for (int i = closeBracketIndex + 2; i < hostPortString.length(); ++i) {
            Preconditions.checkArgument(Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
        }
        return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
    }
    
    public HostAndPort withDefaultPort(final int defaultPort) {
        Preconditions.checkArgument(isValidPort(defaultPort));
        if (this.hasPort() || this.port == defaultPort) {
            return this;
        }
        return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
    }
    
    public HostAndPort requireBracketsForIPv6() {
        Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
        return this;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof HostAndPort) {
            final HostAndPort that = (HostAndPort)other;
            return Objects.equal(this.host, that.host) && this.port == that.port && this.hasBracketlessColons == that.hasBracketlessColons;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.host, this.port, this.hasBracketlessColons);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.host.length() + 8);
        if (this.host.indexOf(58) >= 0) {
            builder.append('[').append(this.host).append(']');
        }
        else {
            builder.append(this.host);
        }
        if (this.hasPort()) {
            builder.append(':').append(this.port);
        }
        return builder.toString();
    }
    
    private static boolean isValidPort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
