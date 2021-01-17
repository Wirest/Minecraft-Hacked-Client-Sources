// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import org.apache.http.annotation.Immutable;
import java.io.IOException;

@Immutable
public class UnsupportedSchemeException extends IOException
{
    private static final long serialVersionUID = 3597127619218687636L;
    
    public UnsupportedSchemeException(final String message) {
        super(message);
    }
}
