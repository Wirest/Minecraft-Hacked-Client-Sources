// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public class UnsupportedZipFeatureException extends ZipException
{
    private final Feature reason;
    private final ZipArchiveEntry entry;
    private static final long serialVersionUID = 20130101L;
    
    public UnsupportedZipFeatureException(final Feature reason, final ZipArchiveEntry entry) {
        super("unsupported feature " + reason + " used in entry " + entry.getName());
        this.reason = reason;
        this.entry = entry;
    }
    
    public UnsupportedZipFeatureException(final ZipMethod method, final ZipArchiveEntry entry) {
        super("unsupported feature method '" + method.name() + "' used in entry " + entry.getName());
        this.reason = Feature.METHOD;
        this.entry = entry;
    }
    
    public UnsupportedZipFeatureException(final Feature reason) {
        super("unsupported feature " + reason + " used in archive.");
        this.reason = reason;
        this.entry = null;
    }
    
    public Feature getFeature() {
        return this.reason;
    }
    
    public ZipArchiveEntry getEntry() {
        return this.entry;
    }
    
    public static class Feature
    {
        public static final Feature ENCRYPTION;
        public static final Feature METHOD;
        public static final Feature DATA_DESCRIPTOR;
        public static final Feature SPLITTING;
        private final String name;
        
        private Feature(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            ENCRYPTION = new Feature("encryption");
            METHOD = new Feature("compression method");
            DATA_DESCRIPTOR = new Feature("data descriptor");
            SPLITTING = new Feature("splitting");
        }
    }
}
