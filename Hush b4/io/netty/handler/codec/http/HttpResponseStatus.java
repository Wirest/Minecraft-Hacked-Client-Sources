// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class HttpResponseStatus implements Comparable<HttpResponseStatus>
{
    public static final HttpResponseStatus CONTINUE;
    public static final HttpResponseStatus SWITCHING_PROTOCOLS;
    public static final HttpResponseStatus PROCESSING;
    public static final HttpResponseStatus OK;
    public static final HttpResponseStatus CREATED;
    public static final HttpResponseStatus ACCEPTED;
    public static final HttpResponseStatus NON_AUTHORITATIVE_INFORMATION;
    public static final HttpResponseStatus NO_CONTENT;
    public static final HttpResponseStatus RESET_CONTENT;
    public static final HttpResponseStatus PARTIAL_CONTENT;
    public static final HttpResponseStatus MULTI_STATUS;
    public static final HttpResponseStatus MULTIPLE_CHOICES;
    public static final HttpResponseStatus MOVED_PERMANENTLY;
    public static final HttpResponseStatus FOUND;
    public static final HttpResponseStatus SEE_OTHER;
    public static final HttpResponseStatus NOT_MODIFIED;
    public static final HttpResponseStatus USE_PROXY;
    public static final HttpResponseStatus TEMPORARY_REDIRECT;
    public static final HttpResponseStatus BAD_REQUEST;
    public static final HttpResponseStatus UNAUTHORIZED;
    public static final HttpResponseStatus PAYMENT_REQUIRED;
    public static final HttpResponseStatus FORBIDDEN;
    public static final HttpResponseStatus NOT_FOUND;
    public static final HttpResponseStatus METHOD_NOT_ALLOWED;
    public static final HttpResponseStatus NOT_ACCEPTABLE;
    public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED;
    public static final HttpResponseStatus REQUEST_TIMEOUT;
    public static final HttpResponseStatus CONFLICT;
    public static final HttpResponseStatus GONE;
    public static final HttpResponseStatus LENGTH_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_FAILED;
    public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE;
    public static final HttpResponseStatus REQUEST_URI_TOO_LONG;
    public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE;
    public static final HttpResponseStatus REQUESTED_RANGE_NOT_SATISFIABLE;
    public static final HttpResponseStatus EXPECTATION_FAILED;
    public static final HttpResponseStatus UNPROCESSABLE_ENTITY;
    public static final HttpResponseStatus LOCKED;
    public static final HttpResponseStatus FAILED_DEPENDENCY;
    public static final HttpResponseStatus UNORDERED_COLLECTION;
    public static final HttpResponseStatus UPGRADE_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_REQUIRED;
    public static final HttpResponseStatus TOO_MANY_REQUESTS;
    public static final HttpResponseStatus REQUEST_HEADER_FIELDS_TOO_LARGE;
    public static final HttpResponseStatus INTERNAL_SERVER_ERROR;
    public static final HttpResponseStatus NOT_IMPLEMENTED;
    public static final HttpResponseStatus BAD_GATEWAY;
    public static final HttpResponseStatus SERVICE_UNAVAILABLE;
    public static final HttpResponseStatus GATEWAY_TIMEOUT;
    public static final HttpResponseStatus HTTP_VERSION_NOT_SUPPORTED;
    public static final HttpResponseStatus VARIANT_ALSO_NEGOTIATES;
    public static final HttpResponseStatus INSUFFICIENT_STORAGE;
    public static final HttpResponseStatus NOT_EXTENDED;
    public static final HttpResponseStatus NETWORK_AUTHENTICATION_REQUIRED;
    private final int code;
    private final String reasonPhrase;
    private final byte[] bytes;
    
    public static HttpResponseStatus valueOf(final int code) {
        switch (code) {
            case 100: {
                return HttpResponseStatus.CONTINUE;
            }
            case 101: {
                return HttpResponseStatus.SWITCHING_PROTOCOLS;
            }
            case 102: {
                return HttpResponseStatus.PROCESSING;
            }
            case 200: {
                return HttpResponseStatus.OK;
            }
            case 201: {
                return HttpResponseStatus.CREATED;
            }
            case 202: {
                return HttpResponseStatus.ACCEPTED;
            }
            case 203: {
                return HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION;
            }
            case 204: {
                return HttpResponseStatus.NO_CONTENT;
            }
            case 205: {
                return HttpResponseStatus.RESET_CONTENT;
            }
            case 206: {
                return HttpResponseStatus.PARTIAL_CONTENT;
            }
            case 207: {
                return HttpResponseStatus.MULTI_STATUS;
            }
            case 300: {
                return HttpResponseStatus.MULTIPLE_CHOICES;
            }
            case 301: {
                return HttpResponseStatus.MOVED_PERMANENTLY;
            }
            case 302: {
                return HttpResponseStatus.FOUND;
            }
            case 303: {
                return HttpResponseStatus.SEE_OTHER;
            }
            case 304: {
                return HttpResponseStatus.NOT_MODIFIED;
            }
            case 305: {
                return HttpResponseStatus.USE_PROXY;
            }
            case 307: {
                return HttpResponseStatus.TEMPORARY_REDIRECT;
            }
            case 400: {
                return HttpResponseStatus.BAD_REQUEST;
            }
            case 401: {
                return HttpResponseStatus.UNAUTHORIZED;
            }
            case 402: {
                return HttpResponseStatus.PAYMENT_REQUIRED;
            }
            case 403: {
                return HttpResponseStatus.FORBIDDEN;
            }
            case 404: {
                return HttpResponseStatus.NOT_FOUND;
            }
            case 405: {
                return HttpResponseStatus.METHOD_NOT_ALLOWED;
            }
            case 406: {
                return HttpResponseStatus.NOT_ACCEPTABLE;
            }
            case 407: {
                return HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED;
            }
            case 408: {
                return HttpResponseStatus.REQUEST_TIMEOUT;
            }
            case 409: {
                return HttpResponseStatus.CONFLICT;
            }
            case 410: {
                return HttpResponseStatus.GONE;
            }
            case 411: {
                return HttpResponseStatus.LENGTH_REQUIRED;
            }
            case 412: {
                return HttpResponseStatus.PRECONDITION_FAILED;
            }
            case 413: {
                return HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;
            }
            case 414: {
                return HttpResponseStatus.REQUEST_URI_TOO_LONG;
            }
            case 415: {
                return HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
            }
            case 416: {
                return HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
            }
            case 417: {
                return HttpResponseStatus.EXPECTATION_FAILED;
            }
            case 422: {
                return HttpResponseStatus.UNPROCESSABLE_ENTITY;
            }
            case 423: {
                return HttpResponseStatus.LOCKED;
            }
            case 424: {
                return HttpResponseStatus.FAILED_DEPENDENCY;
            }
            case 425: {
                return HttpResponseStatus.UNORDERED_COLLECTION;
            }
            case 426: {
                return HttpResponseStatus.UPGRADE_REQUIRED;
            }
            case 428: {
                return HttpResponseStatus.PRECONDITION_REQUIRED;
            }
            case 429: {
                return HttpResponseStatus.TOO_MANY_REQUESTS;
            }
            case 431: {
                return HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
            }
            case 500: {
                return HttpResponseStatus.INTERNAL_SERVER_ERROR;
            }
            case 501: {
                return HttpResponseStatus.NOT_IMPLEMENTED;
            }
            case 502: {
                return HttpResponseStatus.BAD_GATEWAY;
            }
            case 503: {
                return HttpResponseStatus.SERVICE_UNAVAILABLE;
            }
            case 504: {
                return HttpResponseStatus.GATEWAY_TIMEOUT;
            }
            case 505: {
                return HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED;
            }
            case 506: {
                return HttpResponseStatus.VARIANT_ALSO_NEGOTIATES;
            }
            case 507: {
                return HttpResponseStatus.INSUFFICIENT_STORAGE;
            }
            case 510: {
                return HttpResponseStatus.NOT_EXTENDED;
            }
            case 511: {
                return HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED;
            }
            default: {
                String reasonPhrase;
                if (code < 100) {
                    reasonPhrase = "Unknown Status";
                }
                else if (code < 200) {
                    reasonPhrase = "Informational";
                }
                else if (code < 300) {
                    reasonPhrase = "Successful";
                }
                else if (code < 400) {
                    reasonPhrase = "Redirection";
                }
                else if (code < 500) {
                    reasonPhrase = "Client Error";
                }
                else if (code < 600) {
                    reasonPhrase = "Server Error";
                }
                else {
                    reasonPhrase = "Unknown Status";
                }
                return new HttpResponseStatus(code, reasonPhrase + " (" + code + ')');
            }
        }
    }
    
    public HttpResponseStatus(final int code, final String reasonPhrase) {
        this(code, reasonPhrase, false);
    }
    
    private HttpResponseStatus(final int code, final String reasonPhrase, final boolean bytes) {
        if (code < 0) {
            throw new IllegalArgumentException("code: " + code + " (expected: 0+)");
        }
        if (reasonPhrase == null) {
            throw new NullPointerException("reasonPhrase");
        }
        int i = 0;
        while (i < reasonPhrase.length()) {
            final char c = reasonPhrase.charAt(i);
            switch (c) {
                case '\n':
                case '\r': {
                    throw new IllegalArgumentException("reasonPhrase contains one of the following prohibited characters: \\r\\n: " + reasonPhrase);
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        this.code = code;
        this.reasonPhrase = reasonPhrase;
        if (bytes) {
            this.bytes = (code + " " + reasonPhrase).getBytes(CharsetUtil.US_ASCII);
        }
        else {
            this.bytes = null;
        }
    }
    
    public int code() {
        return this.code;
    }
    
    public String reasonPhrase() {
        return this.reasonPhrase;
    }
    
    @Override
    public int hashCode() {
        return this.code();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HttpResponseStatus && this.code() == ((HttpResponseStatus)o).code();
    }
    
    @Override
    public int compareTo(final HttpResponseStatus o) {
        return this.code() - o.code();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(this.reasonPhrase.length() + 5);
        buf.append(this.code);
        buf.append(' ');
        buf.append(this.reasonPhrase);
        return buf.toString();
    }
    
    void encode(final ByteBuf buf) {
        if (this.bytes == null) {
            HttpHeaders.encodeAscii0(String.valueOf(this.code()), buf);
            buf.writeByte(32);
            HttpHeaders.encodeAscii0(String.valueOf(this.reasonPhrase()), buf);
        }
        else {
            buf.writeBytes(this.bytes);
        }
    }
    
    static {
        CONTINUE = new HttpResponseStatus(100, "Continue", true);
        SWITCHING_PROTOCOLS = new HttpResponseStatus(101, "Switching Protocols", true);
        PROCESSING = new HttpResponseStatus(102, "Processing", true);
        OK = new HttpResponseStatus(200, "OK", true);
        CREATED = new HttpResponseStatus(201, "Created", true);
        ACCEPTED = new HttpResponseStatus(202, "Accepted", true);
        NON_AUTHORITATIVE_INFORMATION = new HttpResponseStatus(203, "Non-Authoritative Information", true);
        NO_CONTENT = new HttpResponseStatus(204, "No Content", true);
        RESET_CONTENT = new HttpResponseStatus(205, "Reset Content", true);
        PARTIAL_CONTENT = new HttpResponseStatus(206, "Partial Content", true);
        MULTI_STATUS = new HttpResponseStatus(207, "Multi-Status", true);
        MULTIPLE_CHOICES = new HttpResponseStatus(300, "Multiple Choices", true);
        MOVED_PERMANENTLY = new HttpResponseStatus(301, "Moved Permanently", true);
        FOUND = new HttpResponseStatus(302, "Found", true);
        SEE_OTHER = new HttpResponseStatus(303, "See Other", true);
        NOT_MODIFIED = new HttpResponseStatus(304, "Not Modified", true);
        USE_PROXY = new HttpResponseStatus(305, "Use Proxy", true);
        TEMPORARY_REDIRECT = new HttpResponseStatus(307, "Temporary Redirect", true);
        BAD_REQUEST = new HttpResponseStatus(400, "Bad Request", true);
        UNAUTHORIZED = new HttpResponseStatus(401, "Unauthorized", true);
        PAYMENT_REQUIRED = new HttpResponseStatus(402, "Payment Required", true);
        FORBIDDEN = new HttpResponseStatus(403, "Forbidden", true);
        NOT_FOUND = new HttpResponseStatus(404, "Not Found", true);
        METHOD_NOT_ALLOWED = new HttpResponseStatus(405, "Method Not Allowed", true);
        NOT_ACCEPTABLE = new HttpResponseStatus(406, "Not Acceptable", true);
        PROXY_AUTHENTICATION_REQUIRED = new HttpResponseStatus(407, "Proxy Authentication Required", true);
        REQUEST_TIMEOUT = new HttpResponseStatus(408, "Request Timeout", true);
        CONFLICT = new HttpResponseStatus(409, "Conflict", true);
        GONE = new HttpResponseStatus(410, "Gone", true);
        LENGTH_REQUIRED = new HttpResponseStatus(411, "Length Required", true);
        PRECONDITION_FAILED = new HttpResponseStatus(412, "Precondition Failed", true);
        REQUEST_ENTITY_TOO_LARGE = new HttpResponseStatus(413, "Request Entity Too Large", true);
        REQUEST_URI_TOO_LONG = new HttpResponseStatus(414, "Request-URI Too Long", true);
        UNSUPPORTED_MEDIA_TYPE = new HttpResponseStatus(415, "Unsupported Media Type", true);
        REQUESTED_RANGE_NOT_SATISFIABLE = new HttpResponseStatus(416, "Requested Range Not Satisfiable", true);
        EXPECTATION_FAILED = new HttpResponseStatus(417, "Expectation Failed", true);
        UNPROCESSABLE_ENTITY = new HttpResponseStatus(422, "Unprocessable Entity", true);
        LOCKED = new HttpResponseStatus(423, "Locked", true);
        FAILED_DEPENDENCY = new HttpResponseStatus(424, "Failed Dependency", true);
        UNORDERED_COLLECTION = new HttpResponseStatus(425, "Unordered Collection", true);
        UPGRADE_REQUIRED = new HttpResponseStatus(426, "Upgrade Required", true);
        PRECONDITION_REQUIRED = new HttpResponseStatus(428, "Precondition Required", true);
        TOO_MANY_REQUESTS = new HttpResponseStatus(429, "Too Many Requests", true);
        REQUEST_HEADER_FIELDS_TOO_LARGE = new HttpResponseStatus(431, "Request Header Fields Too Large", true);
        INTERNAL_SERVER_ERROR = new HttpResponseStatus(500, "Internal Server Error", true);
        NOT_IMPLEMENTED = new HttpResponseStatus(501, "Not Implemented", true);
        BAD_GATEWAY = new HttpResponseStatus(502, "Bad Gateway", true);
        SERVICE_UNAVAILABLE = new HttpResponseStatus(503, "Service Unavailable", true);
        GATEWAY_TIMEOUT = new HttpResponseStatus(504, "Gateway Timeout", true);
        HTTP_VERSION_NOT_SUPPORTED = new HttpResponseStatus(505, "HTTP Version Not Supported", true);
        VARIANT_ALSO_NEGOTIATES = new HttpResponseStatus(506, "Variant Also Negotiates", true);
        INSUFFICIENT_STORAGE = new HttpResponseStatus(507, "Insufficient Storage", true);
        NOT_EXTENDED = new HttpResponseStatus(510, "Not Extended", true);
        NETWORK_AUTHENTICATION_REQUIRED = new HttpResponseStatus(511, "Network Authentication Required", true);
    }
}
