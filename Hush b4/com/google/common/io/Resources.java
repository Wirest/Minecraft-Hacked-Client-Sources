// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Objects;
import java.io.OutputStream;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.net.URL;
import com.google.common.annotations.Beta;

@Beta
public final class Resources
{
    private Resources() {
    }
    
    @Deprecated
    public static InputSupplier<InputStream> newInputStreamSupplier(final URL url) {
        return ByteStreams.asInputSupplier(asByteSource(url));
    }
    
    public static ByteSource asByteSource(final URL url) {
        return new UrlByteSource(url);
    }
    
    @Deprecated
    public static InputSupplier<InputStreamReader> newReaderSupplier(final URL url, final Charset charset) {
        return CharStreams.asInputSupplier(asCharSource(url, charset));
    }
    
    public static CharSource asCharSource(final URL url, final Charset charset) {
        return asByteSource(url).asCharSource(charset);
    }
    
    public static byte[] toByteArray(final URL url) throws IOException {
        return asByteSource(url).read();
    }
    
    public static String toString(final URL url, final Charset charset) throws IOException {
        return asCharSource(url, charset).read();
    }
    
    public static <T> T readLines(final URL url, final Charset charset, final LineProcessor<T> callback) throws IOException {
        return CharStreams.readLines(newReaderSupplier(url, charset), callback);
    }
    
    public static List<String> readLines(final URL url, final Charset charset) throws IOException {
        return readLines(url, charset, (LineProcessor<List<String>>)new LineProcessor<List<String>>() {
            final List<String> result = Lists.newArrayList();
            
            @Override
            public boolean processLine(final String line) {
                this.result.add(line);
                return true;
            }
            
            @Override
            public List<String> getResult() {
                return this.result;
            }
        });
    }
    
    public static void copy(final URL from, final OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }
    
    public static URL getResource(final String resourceName) {
        final ClassLoader loader = Objects.firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
        final URL url = loader.getResource(resourceName);
        Preconditions.checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
    }
    
    public static URL getResource(final Class<?> contextClass, final String resourceName) {
        final URL url = contextClass.getResource(resourceName);
        Preconditions.checkArgument(url != null, "resource %s relative to %s not found.", resourceName, contextClass.getName());
        return url;
    }
    
    private static final class UrlByteSource extends ByteSource
    {
        private final URL url;
        
        private UrlByteSource(final URL url) {
            this.url = Preconditions.checkNotNull(url);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return this.url.openStream();
        }
        
        @Override
        public String toString() {
            return "Resources.asByteSource(" + this.url + ")";
        }
    }
}
