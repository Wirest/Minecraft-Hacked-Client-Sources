// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Set;

public class DefaultCookie implements Cookie
{
    private final String name;
    private String value;
    private String domain;
    private String path;
    private String comment;
    private String commentUrl;
    private boolean discard;
    private Set<Integer> ports;
    private Set<Integer> unmodifiablePorts;
    private long maxAge;
    private int version;
    private boolean secure;
    private boolean httpOnly;
    
    public DefaultCookie(String name, final String value) {
        this.ports = Collections.emptySet();
        this.unmodifiablePorts = this.ports;
        this.maxAge = Long.MIN_VALUE;
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        int i = 0;
        while (i < name.length()) {
            final char c = name.charAt(i);
            if (c > '\u007f') {
                throw new IllegalArgumentException("name contains non-ascii character: " + name);
            }
            switch (c) {
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ' ':
                case ',':
                case ';':
                case '=': {
                    throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + name);
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        if (name.charAt(0) == '$') {
            throw new IllegalArgumentException("name starting with '$' not allowed: " + name);
        }
        this.name = name;
        this.setValue(value);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public void setValue(final String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        this.value = value;
    }
    
    @Override
    public String getDomain() {
        return this.domain;
    }
    
    @Override
    public void setDomain(final String domain) {
        this.domain = validateValue("domain", domain);
    }
    
    @Override
    public String getPath() {
        return this.path;
    }
    
    @Override
    public void setPath(final String path) {
        this.path = validateValue("path", path);
    }
    
    @Override
    public String getComment() {
        return this.comment;
    }
    
    @Override
    public void setComment(final String comment) {
        this.comment = validateValue("comment", comment);
    }
    
    @Override
    public String getCommentUrl() {
        return this.commentUrl;
    }
    
    @Override
    public void setCommentUrl(final String commentUrl) {
        this.commentUrl = validateValue("commentUrl", commentUrl);
    }
    
    @Override
    public boolean isDiscard() {
        return this.discard;
    }
    
    @Override
    public void setDiscard(final boolean discard) {
        this.discard = discard;
    }
    
    @Override
    public Set<Integer> getPorts() {
        if (this.unmodifiablePorts == null) {
            this.unmodifiablePorts = Collections.unmodifiableSet((Set<? extends Integer>)this.ports);
        }
        return this.unmodifiablePorts;
    }
    
    @Override
    public void setPorts(final int... ports) {
        if (ports == null) {
            throw new NullPointerException("ports");
        }
        final int[] portsCopy = ports.clone();
        if (portsCopy.length == 0) {
            final Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
        }
        else {
            final Set<Integer> newPorts = new TreeSet<Integer>();
            for (final int p : portsCopy) {
                if (p <= 0 || p > 65535) {
                    throw new IllegalArgumentException("port out of range: " + p);
                }
                newPorts.add(p);
            }
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }
    
    @Override
    public void setPorts(final Iterable<Integer> ports) {
        final Set<Integer> newPorts = new TreeSet<Integer>();
        for (final int p : ports) {
            if (p <= 0 || p > 65535) {
                throw new IllegalArgumentException("port out of range: " + p);
            }
            newPorts.add(p);
        }
        if (newPorts.isEmpty()) {
            final Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
        }
        else {
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }
    
    @Override
    public long getMaxAge() {
        return this.maxAge;
    }
    
    @Override
    public void setMaxAge(final long maxAge) {
        this.maxAge = maxAge;
    }
    
    @Override
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public void setVersion(final int version) {
        this.version = version;
    }
    
    @Override
    public boolean isSecure() {
        return this.secure;
    }
    
    @Override
    public void setSecure(final boolean secure) {
        this.secure = secure;
    }
    
    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }
    
    @Override
    public void setHttpOnly(final boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Cookie)) {
            return false;
        }
        final Cookie that = (Cookie)o;
        if (!this.getName().equalsIgnoreCase(that.getName())) {
            return false;
        }
        if (this.getPath() == null) {
            if (that.getPath() != null) {
                return false;
            }
        }
        else {
            if (that.getPath() == null) {
                return false;
            }
            if (!this.getPath().equals(that.getPath())) {
                return false;
            }
        }
        if (this.getDomain() == null) {
            return that.getDomain() == null;
        }
        return that.getDomain() != null && this.getDomain().equalsIgnoreCase(that.getDomain());
    }
    
    @Override
    public int compareTo(final Cookie c) {
        int v = this.getName().compareToIgnoreCase(c.getName());
        if (v != 0) {
            return v;
        }
        if (this.getPath() == null) {
            if (c.getPath() != null) {
                return -1;
            }
        }
        else {
            if (c.getPath() == null) {
                return 1;
            }
            v = this.getPath().compareTo(c.getPath());
            if (v != 0) {
                return v;
            }
        }
        if (this.getDomain() == null) {
            if (c.getDomain() != null) {
                return -1;
            }
            return 0;
        }
        else {
            if (c.getDomain() == null) {
                return 1;
            }
            v = this.getDomain().compareToIgnoreCase(c.getDomain());
            return v;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.getName());
        buf.append('=');
        buf.append(this.getValue());
        if (this.getDomain() != null) {
            buf.append(", domain=");
            buf.append(this.getDomain());
        }
        if (this.getPath() != null) {
            buf.append(", path=");
            buf.append(this.getPath());
        }
        if (this.getComment() != null) {
            buf.append(", comment=");
            buf.append(this.getComment());
        }
        if (this.getMaxAge() >= 0L) {
            buf.append(", maxAge=");
            buf.append(this.getMaxAge());
            buf.append('s');
        }
        if (this.isSecure()) {
            buf.append(", secure");
        }
        if (this.isHttpOnly()) {
            buf.append(", HTTPOnly");
        }
        return buf.toString();
    }
    
    private static String validateValue(final String name, String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.isEmpty()) {
            return null;
        }
        int i = 0;
        while (i < value.length()) {
            final char c = value.charAt(i);
            switch (c) {
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ';': {
                    throw new IllegalArgumentException(name + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + value + ')');
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        return value;
    }
}
