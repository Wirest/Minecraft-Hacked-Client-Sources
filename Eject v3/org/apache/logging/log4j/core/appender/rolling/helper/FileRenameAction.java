package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileRenameAction
        extends AbstractAction {
    private final File source;
    private final File destination;
    private final boolean renameEmptyFiles;

    public FileRenameAction(File paramFile1, File paramFile2, boolean paramBoolean) {
        this.source = paramFile1;
        this.destination = paramFile2;
        this.renameEmptyFiles = paramBoolean;
    }

    public static boolean execute(File paramFile1, File paramFile2, boolean paramBoolean) {
        if ((paramBoolean) || (paramFile1.length() > 0L)) {
            File localFile = paramFile2.getParentFile();
            if ((localFile != null) && (!localFile.exists()) && (!localFile.mkdirs())) {
                LOGGER.error("Unable to create directory {}", new Object[]{localFile.getAbsolutePath()});
                return false;
            }
            try {
                if (!paramFile1.renameTo(paramFile2)) {
                    try {
                        copyFile(paramFile1, paramFile2);
                        return paramFile1.delete();
                    } catch (IOException localIOException1) {
                        LOGGER.error("Unable to rename file {} to {} - {}", new Object[]{paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath(), localIOException1.getMessage()});
                    }
                }
                return true;
            } catch (Exception localException2) {
                try {
                    copyFile(paramFile1, paramFile2);
                    return paramFile1.delete();
                } catch (IOException localIOException2) {
                    LOGGER.error("Unable to rename file {} to {} - {}", new Object[]{paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath(), localIOException2.getMessage()});
                }
            }
        } else {
            try {
                paramFile1.delete();
            } catch (Exception localException1) {
                LOGGER.error("Unable to delete empty file " + paramFile1.getAbsolutePath());
            }
        }
        return false;
    }

    private static void copyFile(File paramFile1, File paramFile2)
            throws IOException {
        if (!paramFile2.exists()) {
            paramFile2.createNewFile();
        }
        FileChannel localFileChannel1 = null;
        FileChannel localFileChannel2 = null;
        FileInputStream localFileInputStream = null;
        FileOutputStream localFileOutputStream = null;
        try {
            localFileInputStream = new FileInputStream(paramFile1);
            localFileOutputStream = new FileOutputStream(paramFile2);
            localFileChannel1 = localFileInputStream.getChannel();
            localFileChannel2 = localFileOutputStream.getChannel();
            localFileChannel2.transferFrom(localFileChannel1, 0L, localFileChannel1.size());
        } finally {
            if (localFileChannel1 != null) {
                localFileChannel1.close();
            }
            if (localFileInputStream != null) {
                localFileInputStream.close();
            }
            if (localFileChannel2 != null) {
                localFileChannel2.close();
            }
            if (localFileOutputStream != null) {
                localFileOutputStream.close();
            }
        }
    }

    public boolean execute() {
        return execute(this.source, this.destination, this.renameEmptyFiles);
    }
}




