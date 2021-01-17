// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;

public interface ResponseHandler<T>
{
    T handleResponse(final HttpResponse p0) throws ClientProtocolException, IOException;
}
