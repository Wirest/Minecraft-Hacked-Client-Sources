// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;

public class XmlStreamReaderException extends IOException
{
    private static final long serialVersionUID = 1L;
    private final String bomEncoding;
    private final String xmlGuessEncoding;
    private final String xmlEncoding;
    private final String contentTypeMime;
    private final String contentTypeEncoding;
    
    public XmlStreamReaderException(final String msg, final String bomEnc, final String xmlGuessEnc, final String xmlEnc) {
        this(msg, null, null, bomEnc, xmlGuessEnc, xmlEnc);
    }
    
    public XmlStreamReaderException(final String msg, final String ctMime, final String ctEnc, final String bomEnc, final String xmlGuessEnc, final String xmlEnc) {
        super(msg);
        this.contentTypeMime = ctMime;
        this.contentTypeEncoding = ctEnc;
        this.bomEncoding = bomEnc;
        this.xmlGuessEncoding = xmlGuessEnc;
        this.xmlEncoding = xmlEnc;
    }
    
    public String getBomEncoding() {
        return this.bomEncoding;
    }
    
    public String getXmlGuessEncoding() {
        return this.xmlGuessEncoding;
    }
    
    public String getXmlEncoding() {
        return this.xmlEncoding;
    }
    
    public String getContentTypeMime() {
        return this.contentTypeMime;
    }
    
    public String getContentTypeEncoding() {
        return this.contentTypeEncoding;
    }
}
