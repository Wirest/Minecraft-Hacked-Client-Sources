// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.bzip2;

import java.util.Map;
import java.util.LinkedHashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public abstract class BZip2Utils
{
    private static final FileNameUtil fileNameUtil;
    
    private BZip2Utils() {
    }
    
    public static boolean isCompressedFilename(final String filename) {
        return BZip2Utils.fileNameUtil.isCompressedFilename(filename);
    }
    
    public static String getUncompressedFilename(final String filename) {
        return BZip2Utils.fileNameUtil.getUncompressedFilename(filename);
    }
    
    public static String getCompressedFilename(final String filename) {
        return BZip2Utils.fileNameUtil.getCompressedFilename(filename);
    }
    
    static {
        final Map<String, String> uncompressSuffix = new LinkedHashMap<String, String>();
        uncompressSuffix.put(".tar.bz2", ".tar");
        uncompressSuffix.put(".tbz2", ".tar");
        uncompressSuffix.put(".tbz", ".tar");
        uncompressSuffix.put(".bz2", "");
        uncompressSuffix.put(".bz", "");
        fileNameUtil = new FileNameUtil(uncompressSuffix, ".bz2");
    }
}
