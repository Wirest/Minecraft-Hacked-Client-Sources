// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HeaderElement;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import java.util.Locale;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponseInterceptor;

@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor
{
    public static final String UNCOMPRESSED = "http.client.response.uncompressed";
    
    public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
        final HttpEntity entity = response.getEntity();
        if (entity != null && entity.getContentLength() != 0L) {
            final Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                final HeaderElement[] codecs = ceheader.getElements();
                boolean uncompressed = false;
                final HeaderElement[] arr$ = codecs;
                final int len$ = arr$.length;
                final int i$ = 0;
                if (i$ < len$) {
                    final HeaderElement codec = arr$[i$];
                    final String codecname = codec.getName().toLowerCase(Locale.US);
                    if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
                        response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                        uncompressed = true;
                    }
                    else if ("deflate".equals(codecname)) {
                        response.setEntity(new DeflateDecompressingEntity(response.getEntity()));
                        uncompressed = true;
                    }
                    else {
                        if ("identity".equals(codecname)) {
                            return;
                        }
                        throw new HttpException("Unsupported Content-Coding: " + codec.getName());
                    }
                }
                if (uncompressed) {
                    response.removeHeaders("Content-Length");
                    response.removeHeaders("Content-Encoding");
                    response.removeHeaders("Content-MD5");
                }
            }
        }
    }
}
