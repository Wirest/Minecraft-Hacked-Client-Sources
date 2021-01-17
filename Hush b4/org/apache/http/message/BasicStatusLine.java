// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.Args;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import java.io.Serializable;
import org.apache.http.StatusLine;

@Immutable
public class BasicStatusLine implements StatusLine, Cloneable, Serializable
{
    private static final long serialVersionUID = -2443303766890459269L;
    private final ProtocolVersion protoVersion;
    private final int statusCode;
    private final String reasonPhrase;
    
    public BasicStatusLine(final ProtocolVersion version, final int statusCode, final String reasonPhrase) {
        this.protoVersion = Args.notNull(version, "Version");
        this.statusCode = Args.notNegative(statusCode, "Status code");
        this.reasonPhrase = reasonPhrase;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
    
    public ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }
    
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
    
    @Override
    public String toString() {
        return BasicLineFormatter.INSTANCE.formatStatusLine(null, this).toString();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
