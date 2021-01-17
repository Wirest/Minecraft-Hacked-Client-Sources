// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.io;

import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.HttpMessage;

public interface HttpMessageWriter<T extends HttpMessage>
{
    void write(final T p0) throws IOException, HttpException;
}
