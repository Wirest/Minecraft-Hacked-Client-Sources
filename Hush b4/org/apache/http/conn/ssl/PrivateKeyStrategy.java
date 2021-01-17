// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import java.net.Socket;
import java.util.Map;

public interface PrivateKeyStrategy
{
    String chooseAlias(final Map<String, PrivateKeyDetails> p0, final Socket p1);
}
