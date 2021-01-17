// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.net;

import com.google.common.escape.Escaper;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class UrlEscapers
{
    static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";
    static final String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS = "-._~!$'()*,;&=@:";
    private static final Escaper URL_FORM_PARAMETER_ESCAPER;
    private static final Escaper URL_PATH_SEGMENT_ESCAPER;
    private static final Escaper URL_FRAGMENT_ESCAPER;
    
    private UrlEscapers() {
    }
    
    public static Escaper urlFormParameterEscaper() {
        return UrlEscapers.URL_FORM_PARAMETER_ESCAPER;
    }
    
    public static Escaper urlPathSegmentEscaper() {
        return UrlEscapers.URL_PATH_SEGMENT_ESCAPER;
    }
    
    public static Escaper urlFragmentEscaper() {
        return UrlEscapers.URL_FRAGMENT_ESCAPER;
    }
    
    static {
        URL_FORM_PARAMETER_ESCAPER = new PercentEscaper("-_.*", true);
        URL_PATH_SEGMENT_ESCAPER = new PercentEscaper("-._~!$'()*,;&=@:+", false);
        URL_FRAGMENT_ESCAPER = new PercentEscaper("-._~!$'()*,;&=@:+/?", false);
    }
}
