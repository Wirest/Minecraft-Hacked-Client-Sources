// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.xml;

import com.google.common.escape.Escapers;
import com.google.common.escape.Escaper;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public class XmlEscapers
{
    private static final char MIN_ASCII_CONTROL_CHAR = '\0';
    private static final char MAX_ASCII_CONTROL_CHAR = '\u001f';
    private static final Escaper XML_ESCAPER;
    private static final Escaper XML_CONTENT_ESCAPER;
    private static final Escaper XML_ATTRIBUTE_ESCAPER;
    
    private XmlEscapers() {
    }
    
    public static Escaper xmlContentEscaper() {
        return XmlEscapers.XML_CONTENT_ESCAPER;
    }
    
    public static Escaper xmlAttributeEscaper() {
        return XmlEscapers.XML_ATTRIBUTE_ESCAPER;
    }
    
    static {
        final Escapers.Builder builder = Escapers.builder();
        builder.setSafeRange('\0', '\uffff');
        builder.setUnsafeReplacement("");
        for (char c = '\0'; c <= '\u001f'; ++c) {
            if (c != '\t' && c != '\n' && c != '\r') {
                builder.addEscape(c, "");
            }
        }
        builder.addEscape('&', "&amp;");
        builder.addEscape('<', "&lt;");
        builder.addEscape('>', "&gt;");
        XML_CONTENT_ESCAPER = builder.build();
        builder.addEscape('\'', "&apos;");
        builder.addEscape('\"', "&quot;");
        XML_ESCAPER = builder.build();
        builder.addEscape('\t', "&#x9;");
        builder.addEscape('\n', "&#xA;");
        builder.addEscape('\r', "&#xD;");
        XML_ATTRIBUTE_ESCAPER = builder.build();
    }
}
