// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpResponseStatus;

public final class RtspResponseStatuses
{
    public static final HttpResponseStatus CONTINUE;
    public static final HttpResponseStatus OK;
    public static final HttpResponseStatus CREATED;
    public static final HttpResponseStatus LOW_STORAGE_SPACE;
    public static final HttpResponseStatus MULTIPLE_CHOICES;
    public static final HttpResponseStatus MOVED_PERMANENTLY;
    public static final HttpResponseStatus MOVED_TEMPORARILY;
    public static final HttpResponseStatus NOT_MODIFIED;
    public static final HttpResponseStatus USE_PROXY;
    public static final HttpResponseStatus BAD_REQUEST;
    public static final HttpResponseStatus UNAUTHORIZED;
    public static final HttpResponseStatus PAYMENT_REQUIRED;
    public static final HttpResponseStatus FORBIDDEN;
    public static final HttpResponseStatus NOT_FOUND;
    public static final HttpResponseStatus METHOD_NOT_ALLOWED;
    public static final HttpResponseStatus NOT_ACCEPTABLE;
    public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED;
    public static final HttpResponseStatus REQUEST_TIMEOUT;
    public static final HttpResponseStatus GONE;
    public static final HttpResponseStatus LENGTH_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_FAILED;
    public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE;
    public static final HttpResponseStatus REQUEST_URI_TOO_LONG;
    public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE;
    public static final HttpResponseStatus PARAMETER_NOT_UNDERSTOOD;
    public static final HttpResponseStatus CONFERENCE_NOT_FOUND;
    public static final HttpResponseStatus NOT_ENOUGH_BANDWIDTH;
    public static final HttpResponseStatus SESSION_NOT_FOUND;
    public static final HttpResponseStatus METHOD_NOT_VALID;
    public static final HttpResponseStatus HEADER_FIELD_NOT_VALID;
    public static final HttpResponseStatus INVALID_RANGE;
    public static final HttpResponseStatus PARAMETER_IS_READONLY;
    public static final HttpResponseStatus AGGREGATE_OPERATION_NOT_ALLOWED;
    public static final HttpResponseStatus ONLY_AGGREGATE_OPERATION_ALLOWED;
    public static final HttpResponseStatus UNSUPPORTED_TRANSPORT;
    public static final HttpResponseStatus DESTINATION_UNREACHABLE;
    public static final HttpResponseStatus KEY_MANAGEMENT_FAILURE;
    public static final HttpResponseStatus INTERNAL_SERVER_ERROR;
    public static final HttpResponseStatus NOT_IMPLEMENTED;
    public static final HttpResponseStatus BAD_GATEWAY;
    public static final HttpResponseStatus SERVICE_UNAVAILABLE;
    public static final HttpResponseStatus GATEWAY_TIMEOUT;
    public static final HttpResponseStatus RTSP_VERSION_NOT_SUPPORTED;
    public static final HttpResponseStatus OPTION_NOT_SUPPORTED;
    
    public static HttpResponseStatus valueOf(final int code) {
        switch (code) {
            case 250: {
                return RtspResponseStatuses.LOW_STORAGE_SPACE;
            }
            case 302: {
                return RtspResponseStatuses.MOVED_TEMPORARILY;
            }
            case 451: {
                return RtspResponseStatuses.PARAMETER_NOT_UNDERSTOOD;
            }
            case 452: {
                return RtspResponseStatuses.CONFERENCE_NOT_FOUND;
            }
            case 453: {
                return RtspResponseStatuses.NOT_ENOUGH_BANDWIDTH;
            }
            case 454: {
                return RtspResponseStatuses.SESSION_NOT_FOUND;
            }
            case 455: {
                return RtspResponseStatuses.METHOD_NOT_VALID;
            }
            case 456: {
                return RtspResponseStatuses.HEADER_FIELD_NOT_VALID;
            }
            case 457: {
                return RtspResponseStatuses.INVALID_RANGE;
            }
            case 458: {
                return RtspResponseStatuses.PARAMETER_IS_READONLY;
            }
            case 459: {
                return RtspResponseStatuses.AGGREGATE_OPERATION_NOT_ALLOWED;
            }
            case 460: {
                return RtspResponseStatuses.ONLY_AGGREGATE_OPERATION_ALLOWED;
            }
            case 461: {
                return RtspResponseStatuses.UNSUPPORTED_TRANSPORT;
            }
            case 462: {
                return RtspResponseStatuses.DESTINATION_UNREACHABLE;
            }
            case 463: {
                return RtspResponseStatuses.KEY_MANAGEMENT_FAILURE;
            }
            case 505: {
                return RtspResponseStatuses.RTSP_VERSION_NOT_SUPPORTED;
            }
            case 551: {
                return RtspResponseStatuses.OPTION_NOT_SUPPORTED;
            }
            default: {
                return HttpResponseStatus.valueOf(code);
            }
        }
    }
    
    private RtspResponseStatuses() {
    }
    
    static {
        CONTINUE = HttpResponseStatus.CONTINUE;
        OK = HttpResponseStatus.OK;
        CREATED = HttpResponseStatus.CREATED;
        LOW_STORAGE_SPACE = new HttpResponseStatus(250, "Low on Storage Space");
        MULTIPLE_CHOICES = HttpResponseStatus.MULTIPLE_CHOICES;
        MOVED_PERMANENTLY = HttpResponseStatus.MOVED_PERMANENTLY;
        MOVED_TEMPORARILY = new HttpResponseStatus(302, "Moved Temporarily");
        NOT_MODIFIED = HttpResponseStatus.NOT_MODIFIED;
        USE_PROXY = HttpResponseStatus.USE_PROXY;
        BAD_REQUEST = HttpResponseStatus.BAD_REQUEST;
        UNAUTHORIZED = HttpResponseStatus.UNAUTHORIZED;
        PAYMENT_REQUIRED = HttpResponseStatus.PAYMENT_REQUIRED;
        FORBIDDEN = HttpResponseStatus.FORBIDDEN;
        NOT_FOUND = HttpResponseStatus.NOT_FOUND;
        METHOD_NOT_ALLOWED = HttpResponseStatus.METHOD_NOT_ALLOWED;
        NOT_ACCEPTABLE = HttpResponseStatus.NOT_ACCEPTABLE;
        PROXY_AUTHENTICATION_REQUIRED = HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED;
        REQUEST_TIMEOUT = HttpResponseStatus.REQUEST_TIMEOUT;
        GONE = HttpResponseStatus.GONE;
        LENGTH_REQUIRED = HttpResponseStatus.LENGTH_REQUIRED;
        PRECONDITION_FAILED = HttpResponseStatus.PRECONDITION_FAILED;
        REQUEST_ENTITY_TOO_LARGE = HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;
        REQUEST_URI_TOO_LONG = HttpResponseStatus.REQUEST_URI_TOO_LONG;
        UNSUPPORTED_MEDIA_TYPE = HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
        PARAMETER_NOT_UNDERSTOOD = new HttpResponseStatus(451, "Parameter Not Understood");
        CONFERENCE_NOT_FOUND = new HttpResponseStatus(452, "Conference Not Found");
        NOT_ENOUGH_BANDWIDTH = new HttpResponseStatus(453, "Not Enough Bandwidth");
        SESSION_NOT_FOUND = new HttpResponseStatus(454, "Session Not Found");
        METHOD_NOT_VALID = new HttpResponseStatus(455, "Method Not Valid in This State");
        HEADER_FIELD_NOT_VALID = new HttpResponseStatus(456, "Header Field Not Valid for Resource");
        INVALID_RANGE = new HttpResponseStatus(457, "Invalid Range");
        PARAMETER_IS_READONLY = new HttpResponseStatus(458, "Parameter Is Read-Only");
        AGGREGATE_OPERATION_NOT_ALLOWED = new HttpResponseStatus(459, "Aggregate operation not allowed");
        ONLY_AGGREGATE_OPERATION_ALLOWED = new HttpResponseStatus(460, "Only Aggregate operation allowed");
        UNSUPPORTED_TRANSPORT = new HttpResponseStatus(461, "Unsupported transport");
        DESTINATION_UNREACHABLE = new HttpResponseStatus(462, "Destination unreachable");
        KEY_MANAGEMENT_FAILURE = new HttpResponseStatus(463, "Key management failure");
        INTERNAL_SERVER_ERROR = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        NOT_IMPLEMENTED = HttpResponseStatus.NOT_IMPLEMENTED;
        BAD_GATEWAY = HttpResponseStatus.BAD_GATEWAY;
        SERVICE_UNAVAILABLE = HttpResponseStatus.SERVICE_UNAVAILABLE;
        GATEWAY_TIMEOUT = HttpResponseStatus.GATEWAY_TIMEOUT;
        RTSP_VERSION_NOT_SUPPORTED = new HttpResponseStatus(505, "RTSP Version not supported");
        OPTION_NOT_SUPPORTED = new HttpResponseStatus(551, "Option not supported");
    }
}
