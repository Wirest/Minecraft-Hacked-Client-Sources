// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import org.apache.http.util.Args;
import org.apache.http.annotation.Immutable;
import java.io.Serializable;

@Immutable
public class ProtocolVersion implements Serializable, Cloneable
{
    private static final long serialVersionUID = 8950662842175091068L;
    protected final String protocol;
    protected final int major;
    protected final int minor;
    
    public ProtocolVersion(final String protocol, final int major, final int minor) {
        this.protocol = Args.notNull(protocol, "Protocol name");
        this.major = Args.notNegative(major, "Protocol minor version");
        this.minor = Args.notNegative(minor, "Protocol minor version");
    }
    
    public final String getProtocol() {
        return this.protocol;
    }
    
    public final int getMajor() {
        return this.major;
    }
    
    public final int getMinor() {
        return this.minor;
    }
    
    public ProtocolVersion forVersion(final int major, final int minor) {
        if (major == this.major && minor == this.minor) {
            return this;
        }
        return new ProtocolVersion(this.protocol, major, minor);
    }
    
    @Override
    public final int hashCode() {
        return this.protocol.hashCode() ^ this.major * 100000 ^ this.minor;
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProtocolVersion)) {
            return false;
        }
        final ProtocolVersion that = (ProtocolVersion)obj;
        return this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor;
    }
    
    public boolean isComparable(final ProtocolVersion that) {
        return that != null && this.protocol.equals(that.protocol);
    }
    
    public int compareToVersion(final ProtocolVersion that) {
        Args.notNull(that, "Protocol version");
        Args.check(this.protocol.equals(that.protocol), "Versions for different protocols cannot be compared: %s %s", this, that);
        int delta = this.getMajor() - that.getMajor();
        if (delta == 0) {
            delta = this.getMinor() - that.getMinor();
        }
        return delta;
    }
    
    public final boolean greaterEquals(final ProtocolVersion version) {
        return this.isComparable(version) && this.compareToVersion(version) >= 0;
    }
    
    public final boolean lessEquals(final ProtocolVersion version) {
        return this.isComparable(version) && this.compareToVersion(version) <= 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.protocol);
        buffer.append('/');
        buffer.append(Integer.toString(this.major));
        buffer.append('.');
        buffer.append(Integer.toString(this.minor));
        return buffer.toString();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
