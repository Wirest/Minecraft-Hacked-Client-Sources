// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.io;

import org.apache.http.config.MessageConstraints;
import org.apache.http.HttpMessage;

public interface HttpMessageParserFactory<T extends HttpMessage>
{
    HttpMessageParser<T> create(final SessionInputBuffer p0, final MessageConstraints p1);
}
