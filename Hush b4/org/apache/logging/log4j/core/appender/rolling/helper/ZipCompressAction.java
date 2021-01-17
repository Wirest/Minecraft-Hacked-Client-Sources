// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.helper;

import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public final class ZipCompressAction extends AbstractAction
{
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int level;
    
    public ZipCompressAction(final File source, final File destination, final boolean deleteSource, final int level) {
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (destination == null) {
            throw new NullPointerException("destination");
        }
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
        this.level = level;
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource, this.level);
    }
    
    public static boolean execute(final File source, final File destination, final boolean deleteSource, final int level) throws IOException {
        if (source.exists()) {
            final FileInputStream fis = new FileInputStream(source);
            final FileOutputStream fos = new FileOutputStream(destination);
            final ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setLevel(level);
            final ZipEntry zipEntry = new ZipEntry(source.getName());
            zos.putNextEntry(zipEntry);
            final byte[] inbuf = new byte[8102];
            int n;
            while ((n = fis.read(inbuf)) != -1) {
                zos.write(inbuf, 0, n);
            }
            zos.close();
            fis.close();
            if (deleteSource && !source.delete()) {
                ZipCompressAction.LOGGER.warn("Unable to delete " + source.toString() + '.');
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void reportException(final Exception ex) {
        ZipCompressAction.LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
    }
}
