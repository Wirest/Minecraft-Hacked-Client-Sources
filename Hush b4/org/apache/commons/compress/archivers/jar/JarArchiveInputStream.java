// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.jar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class JarArchiveInputStream extends ZipArchiveInputStream
{
    public JarArchiveInputStream(final InputStream inputStream) {
        super(inputStream);
    }
    
    public JarArchiveEntry getNextJarEntry() throws IOException {
        final ZipArchiveEntry entry = this.getNextZipEntry();
        return (entry == null) ? null : new JarArchiveEntry(entry);
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        return this.getNextJarEntry();
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return ZipArchiveInputStream.matches(signature, length);
    }
}
