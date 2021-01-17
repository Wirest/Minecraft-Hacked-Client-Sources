// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.gzip;

import java.util.Map;
import java.util.LinkedHashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public class GzipUtils
{
    private static final FileNameUtil fileNameUtil;
    
    private GzipUtils() {
    }
    
    public static boolean isCompressedFilename(final String filename) {
        return GzipUtils.fileNameUtil.isCompressedFilename(filename);
    }
    
    public static String getUncompressedFilename(final String filename) {
        return GzipUtils.fileNameUtil.getUncompressedFilename(filename);
    }
    
    public static String getCompressedFilename(final String filename) {
        return GzipUtils.fileNameUtil.getCompressedFilename(filename);
    }
    
    static {
        final Map<String, String> uncompressSuffix = new LinkedHashMap<String, String>();
        uncompressSuffix.put(".tgz", ".tar");
        uncompressSuffix.put(".taz", ".tar");
        uncompressSuffix.put(".svgz", ".svg");
        uncompressSuffix.put(".cpgz", ".cpio");
        uncompressSuffix.put(".wmz", ".wmf");
        uncompressSuffix.put(".emz", ".emf");
        uncompressSuffix.put(".gz", "");
        uncompressSuffix.put(".z", "");
        uncompressSuffix.put("-gz", "");
        uncompressSuffix.put("-z", "");
        uncompressSuffix.put("_z", "");
        fileNameUtil = new FileNameUtil(uncompressSuffix, ".gz");
    }
}
