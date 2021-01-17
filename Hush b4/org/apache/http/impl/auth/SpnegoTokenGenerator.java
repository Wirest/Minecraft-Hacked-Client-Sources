// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
public interface SpnegoTokenGenerator
{
    byte[] generateSpnegoDERObject(final byte[] p0) throws IOException;
}
