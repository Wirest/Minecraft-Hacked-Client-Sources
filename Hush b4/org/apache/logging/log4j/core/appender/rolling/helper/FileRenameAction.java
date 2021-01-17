// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.helper;

import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class FileRenameAction extends AbstractAction
{
    private final File source;
    private final File destination;
    private final boolean renameEmptyFiles;
    
    public FileRenameAction(final File src, final File dst, final boolean renameEmptyFiles) {
        this.source = src;
        this.destination = dst;
        this.renameEmptyFiles = renameEmptyFiles;
    }
    
    @Override
    public boolean execute() {
        return execute(this.source, this.destination, this.renameEmptyFiles);
    }
    
    public static boolean execute(final File source, final File destination, final boolean renameEmptyFiles) {
        if (renameEmptyFiles || source.length() > 0L) {
            final File parent = destination.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                FileRenameAction.LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
                return false;
            }
            try {
                if (!source.renameTo(destination)) {
                    try {
                        copyFile(source, destination);
                        return source.delete();
                    }
                    catch (IOException iex) {
                        FileRenameAction.LOGGER.error("Unable to rename file {} to {} - {}", source.getAbsolutePath(), destination.getAbsolutePath(), iex.getMessage());
                    }
                }
                return true;
            }
            catch (Exception ex) {
                try {
                    copyFile(source, destination);
                    return source.delete();
                }
                catch (IOException iex2) {
                    FileRenameAction.LOGGER.error("Unable to rename file {} to {} - {}", source.getAbsolutePath(), destination.getAbsolutePath(), iex2.getMessage());
                }
            }
        }
        try {
            source.delete();
        }
        catch (Exception ex2) {
            FileRenameAction.LOGGER.error("Unable to delete empty file " + source.getAbsolutePath());
        }
        return false;
    }
    
    private static void copyFile(final File source, final File destination) throws IOException {
        if (!destination.exists()) {
            destination.createNewFile();
        }
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        FileInputStream srcStream = null;
        FileOutputStream destStream = null;
        try {
            srcStream = new FileInputStream(source);
            destStream = new FileOutputStream(destination);
            srcChannel = srcStream.getChannel();
            destChannel = destStream.getChannel();
            destChannel.transferFrom(srcChannel, 0L, srcChannel.size());
        }
        finally {
            if (srcChannel != null) {
                srcChannel.close();
            }
            if (srcStream != null) {
                srcStream.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
            if (destStream != null) {
                destStream.close();
            }
        }
    }
}
