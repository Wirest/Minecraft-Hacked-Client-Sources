package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public final class GZCompressAction
        extends AbstractAction {
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;

    public GZCompressAction(File paramFile1, File paramFile2, boolean paramBoolean) {
        if (paramFile1 == null) {
            throw new NullPointerException("source");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("destination");
        }
        this.source = paramFile1;
        this.destination = paramFile2;
        this.deleteSource = paramBoolean;
    }

    public static boolean execute(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile1.exists()) {
            FileInputStream localFileInputStream = new FileInputStream(paramFile1);
            FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
            GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(localFileOutputStream);
            BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localGZIPOutputStream);
            byte[] arrayOfByte = new byte['ᾦ'];
            int i;
            while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
                localBufferedOutputStream.write(arrayOfByte, 0, i);
            }
            localBufferedOutputStream.close();
            localFileInputStream.close();
            if ((paramBoolean) && (!paramFile1.delete())) {
                LOGGER.warn("Unable to delete " + paramFile1.toString() + '.');
            }
            return true;
        }
        return false;
    }

    public boolean execute()
            throws IOException {
        return execute(this.source, this.destination, this.deleteSource);
    }

    protected void reportException(Exception paramException) {
        LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", paramException);
    }
}




