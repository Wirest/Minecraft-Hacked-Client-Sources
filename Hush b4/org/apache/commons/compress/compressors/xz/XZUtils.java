// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.xz;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public class XZUtils
{
    private static final FileNameUtil fileNameUtil;
    
    private XZUtils() {
    }
    
    public static boolean isXZCompressionAvailable() {
        try {
            XZCompressorInputStream.matches(null, 0);
            return true;
        }
        catch (NoClassDefFoundError error) {
            return false;
        }
    }
    
    public static boolean isCompressedFilename(final String filename) {
        return XZUtils.fileNameUtil.isCompressedFilename(filename);
    }
    
    public static String getUncompressedFilename(final String filename) {
        return XZUtils.fileNameUtil.getUncompressedFilename(filename);
    }
    
    public static String getCompressedFilename(final String filename) {
        return XZUtils.fileNameUtil.getCompressedFilename(filename);
    }
    
    static {
        final Map<String, String> uncompressSuffix = new HashMap<String, String>();
        uncompressSuffix.put(".txz", ".tar");
        uncompressSuffix.put(".xz", "");
        uncompressSuffix.put("-xz", "");
        fileNameUtil = new FileNameUtil(uncompressSuffix, ".xz");
    }
}
