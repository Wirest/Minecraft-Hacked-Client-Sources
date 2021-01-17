// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

public class UnicodePathExtraField extends AbstractUnicodeExtraField
{
    public static final ZipShort UPATH_ID;
    
    public UnicodePathExtraField() {
    }
    
    public UnicodePathExtraField(final String text, final byte[] bytes, final int off, final int len) {
        super(text, bytes, off, len);
    }
    
    public UnicodePathExtraField(final String name, final byte[] bytes) {
        super(name, bytes);
    }
    
    public ZipShort getHeaderId() {
        return UnicodePathExtraField.UPATH_ID;
    }
    
    static {
        UPATH_ID = new ZipShort(28789);
    }
}
