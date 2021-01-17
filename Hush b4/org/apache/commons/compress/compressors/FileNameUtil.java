// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors;

import java.util.Locale;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileNameUtil
{
    private final Map<String, String> compressSuffix;
    private final Map<String, String> uncompressSuffix;
    private final int longestCompressedSuffix;
    private final int shortestCompressedSuffix;
    private final int longestUncompressedSuffix;
    private final int shortestUncompressedSuffix;
    private final String defaultExtension;
    
    public FileNameUtil(final Map<String, String> uncompressSuffix, final String defaultExtension) {
        this.compressSuffix = new HashMap<String, String>();
        this.uncompressSuffix = Collections.unmodifiableMap((Map<? extends String, ? extends String>)uncompressSuffix);
        int lc = Integer.MIN_VALUE;
        int sc = Integer.MAX_VALUE;
        int lu = Integer.MIN_VALUE;
        int su = Integer.MAX_VALUE;
        for (final Map.Entry<String, String> ent : uncompressSuffix.entrySet()) {
            final int cl = ent.getKey().length();
            if (cl > lc) {
                lc = cl;
            }
            if (cl < sc) {
                sc = cl;
            }
            final String u = ent.getValue();
            final int ul = u.length();
            if (ul > 0) {
                if (!this.compressSuffix.containsKey(u)) {
                    this.compressSuffix.put(u, ent.getKey());
                }
                if (ul > lu) {
                    lu = ul;
                }
                if (ul >= su) {
                    continue;
                }
                su = ul;
            }
        }
        this.longestCompressedSuffix = lc;
        this.longestUncompressedSuffix = lu;
        this.shortestCompressedSuffix = sc;
        this.shortestUncompressedSuffix = su;
        this.defaultExtension = defaultExtension;
    }
    
    public boolean isCompressedFilename(final String filename) {
        final String lower = filename.toLowerCase(Locale.ENGLISH);
        for (int n = lower.length(), i = this.shortestCompressedSuffix; i <= this.longestCompressedSuffix && i < n; ++i) {
            if (this.uncompressSuffix.containsKey(lower.substring(n - i))) {
                return true;
            }
        }
        return false;
    }
    
    public String getUncompressedFilename(final String filename) {
        final String lower = filename.toLowerCase(Locale.ENGLISH);
        for (int n = lower.length(), i = this.shortestCompressedSuffix; i <= this.longestCompressedSuffix && i < n; ++i) {
            final String suffix = this.uncompressSuffix.get(lower.substring(n - i));
            if (suffix != null) {
                return filename.substring(0, n - i) + suffix;
            }
        }
        return filename;
    }
    
    public String getCompressedFilename(final String filename) {
        final String lower = filename.toLowerCase(Locale.ENGLISH);
        for (int n = lower.length(), i = this.shortestUncompressedSuffix; i <= this.longestUncompressedSuffix && i < n; ++i) {
            final String suffix = this.compressSuffix.get(lower.substring(n - i));
            if (suffix != null) {
                return filename.substring(0, n - i) + suffix;
            }
        }
        return filename + this.defaultExtension;
    }
}
