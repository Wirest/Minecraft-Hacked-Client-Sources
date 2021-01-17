// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import org.apache.http.annotation.Immutable;
import java.io.Serializable;

@Immutable
public final class HttpVersion extends ProtocolVersion implements Serializable
{
    private static final long serialVersionUID = -5856653513894415344L;
    public static final String HTTP = "HTTP";
    public static final HttpVersion HTTP_0_9;
    public static final HttpVersion HTTP_1_0;
    public static final HttpVersion HTTP_1_1;
    
    public HttpVersion(final int major, final int minor) {
        super("HTTP", major, minor);
    }
    
    @Override
    public ProtocolVersion forVersion(final int major, final int minor) {
        if (major == this.major && minor == this.minor) {
            return this;
        }
        if (major == 1) {
            if (minor == 0) {
                return HttpVersion.HTTP_1_0;
            }
            if (minor == 1) {
                return HttpVersion.HTTP_1_1;
            }
        }
        if (major == 0 && minor == 9) {
            return HttpVersion.HTTP_0_9;
        }
        return new HttpVersion(major, minor);
    }
    
    static {
        HTTP_0_9 = new HttpVersion(0, 9);
        HTTP_1_0 = new HttpVersion(1, 0);
        HTTP_1_1 = new HttpVersion(1, 1);
    }
}
