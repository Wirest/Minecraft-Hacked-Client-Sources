// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public final class GZCompressAction extends AbstractAction
{
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    
    public GZCompressAction(final File source, final File destination, final boolean deleteSource) {
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (destination == null) {
            throw new NullPointerException("destination");
        }
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource);
    }
    
    public static boolean execute(final File source, final File destination, final boolean deleteSource) throws IOException {
        if (source.exists()) {
            final FileInputStream fis = new FileInputStream(source);
            final FileOutputStream fos = new FileOutputStream(destination);
            final GZIPOutputStream gzos = new GZIPOutputStream(fos);
            final BufferedOutputStream os = new BufferedOutputStream(gzos);
            final byte[] inbuf = new byte[8102];
            int n;
            while ((n = fis.read(inbuf)) != -1) {
                os.write(inbuf, 0, n);
            }
            os.close();
            fis.close();
            if (deleteSource && !source.delete()) {
                GZCompressAction.LOGGER.warn("Unable to delete " + source.toString() + '.');
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void reportException(final Exception ex) {
        GZCompressAction.LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
    }
}
