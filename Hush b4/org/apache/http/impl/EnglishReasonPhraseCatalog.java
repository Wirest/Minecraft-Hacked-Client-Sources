// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import org.apache.http.util.Args;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.ReasonPhraseCatalog;

@Immutable
public class EnglishReasonPhraseCatalog implements ReasonPhraseCatalog
{
    public static final EnglishReasonPhraseCatalog INSTANCE;
    private static final String[][] REASON_PHRASES;
    
    protected EnglishReasonPhraseCatalog() {
    }
    
    public String getReason(final int status, final Locale loc) {
        Args.check(status >= 100 && status < 600, "Unknown category for status code " + status);
        final int category = status / 100;
        final int subcode = status - 100 * category;
        String reason = null;
        if (EnglishReasonPhraseCatalog.REASON_PHRASES[category].length > subcode) {
            reason = EnglishReasonPhraseCatalog.REASON_PHRASES[category][subcode];
        }
        return reason;
    }
    
    private static void setReason(final int status, final String reason) {
        final int category = status / 100;
        final int subcode = status - 100 * category;
        EnglishReasonPhraseCatalog.REASON_PHRASES[category][subcode] = reason;
    }
    
    static {
        INSTANCE = new EnglishReasonPhraseCatalog();
        REASON_PHRASES = new String[][] { null, new String[3], new String[8], new String[8], new String[25], new String[8] };
        setReason(200, "OK");
        setReason(201, "Created");
        setReason(202, "Accepted");
        setReason(204, "No Content");
        setReason(301, "Moved Permanently");
        setReason(302, "Moved Temporarily");
        setReason(304, "Not Modified");
        setReason(400, "Bad Request");
        setReason(401, "Unauthorized");
        setReason(403, "Forbidden");
        setReason(404, "Not Found");
        setReason(500, "Internal Server Error");
        setReason(501, "Not Implemented");
        setReason(502, "Bad Gateway");
        setReason(503, "Service Unavailable");
        setReason(100, "Continue");
        setReason(307, "Temporary Redirect");
        setReason(405, "Method Not Allowed");
        setReason(409, "Conflict");
        setReason(412, "Precondition Failed");
        setReason(413, "Request Too Long");
        setReason(414, "Request-URI Too Long");
        setReason(415, "Unsupported Media Type");
        setReason(300, "Multiple Choices");
        setReason(303, "See Other");
        setReason(305, "Use Proxy");
        setReason(402, "Payment Required");
        setReason(406, "Not Acceptable");
        setReason(407, "Proxy Authentication Required");
        setReason(408, "Request Timeout");
        setReason(101, "Switching Protocols");
        setReason(203, "Non Authoritative Information");
        setReason(205, "Reset Content");
        setReason(206, "Partial Content");
        setReason(504, "Gateway Timeout");
        setReason(505, "Http Version Not Supported");
        setReason(410, "Gone");
        setReason(411, "Length Required");
        setReason(416, "Requested Range Not Satisfiable");
        setReason(417, "Expectation Failed");
        setReason(102, "Processing");
        setReason(207, "Multi-Status");
        setReason(422, "Unprocessable Entity");
        setReason(419, "Insufficient Space On Resource");
        setReason(420, "Method Failure");
        setReason(423, "Locked");
        setReason(507, "Insufficient Storage");
        setReason(424, "Failed Dependency");
    }
}
