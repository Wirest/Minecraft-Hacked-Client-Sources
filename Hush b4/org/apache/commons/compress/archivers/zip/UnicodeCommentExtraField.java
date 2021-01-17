// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

public class UnicodeCommentExtraField extends AbstractUnicodeExtraField
{
    public static final ZipShort UCOM_ID;
    
    public UnicodeCommentExtraField() {
    }
    
    public UnicodeCommentExtraField(final String text, final byte[] bytes, final int off, final int len) {
        super(text, bytes, off, len);
    }
    
    public UnicodeCommentExtraField(final String comment, final byte[] bytes) {
        super(comment, bytes);
    }
    
    public ZipShort getHeaderId() {
        return UnicodeCommentExtraField.UCOM_ID;
    }
    
    static {
        UCOM_ID = new ZipShort(25461);
    }
}
