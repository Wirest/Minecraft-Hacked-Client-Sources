// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.IOException;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.io.File;
import java.net.URI;
import org.apache.logging.log4j.Logger;

public final class FileUtils
{
    private static final String PROTOCOL_FILE = "file";
    private static final String JBOSS_FILE = "vfsfile";
    private static final Logger LOGGER;
    
    private FileUtils() {
    }
    
    public static File fileFromURI(URI uri) {
        if (uri == null || (uri.getScheme() != null && !"file".equals(uri.getScheme()) && !"vfsfile".equals(uri.getScheme()))) {
            return null;
        }
        if (uri.getScheme() == null) {
            try {
                uri = new File(uri.getPath()).toURI();
            }
            catch (Exception ex2) {
                FileUtils.LOGGER.warn("Invalid URI " + uri);
                return null;
            }
        }
        try {
            return new File(URLDecoder.decode(uri.toURL().getFile(), "UTF8"));
        }
        catch (MalformedURLException ex) {
            FileUtils.LOGGER.warn("Invalid URL " + uri, ex);
        }
        catch (UnsupportedEncodingException uee) {
            FileUtils.LOGGER.warn("Invalid encoding: UTF8", uee);
        }
        return null;
    }
    
    public static boolean isFile(final URL url) {
        return url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile"));
    }
    
    public static void mkdir(final File dir, final boolean createDirectoryIfNotExisting) throws IOException {
        if (!dir.exists()) {
            if (!createDirectoryIfNotExisting) {
                throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
            }
            if (!dir.mkdirs()) {
                throw new IOException("Could not create directory " + dir.getAbsolutePath());
            }
        }
        if (!dir.isDirectory()) {
            throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
