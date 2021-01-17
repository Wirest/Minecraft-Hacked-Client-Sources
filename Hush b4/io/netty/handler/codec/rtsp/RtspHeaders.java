// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

public final class RtspHeaders
{
    private RtspHeaders() {
    }
    
    public static final class Names
    {
        public static final String ACCEPT = "Accept";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ALLOW = "Allow";
        public static final String AUTHORIZATION = "Authorization";
        public static final String BANDWIDTH = "Bandwidth";
        public static final String BLOCKSIZE = "Blocksize";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String CONFERENCE = "Conference";
        public static final String CONNECTION = "Connection";
        public static final String CONTENT_BASE = "Content-Base";
        public static final String CONTENT_ENCODING = "Content-Encoding";
        public static final String CONTENT_LANGUAGE = "Content-Language";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String CONTENT_LOCATION = "Content-Location";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CSEQ = "CSeq";
        public static final String DATE = "Date";
        public static final String EXPIRES = "Expires";
        public static final String FROM = "From";
        public static final String HOST = "Host";
        public static final String IF_MATCH = "If-Match";
        public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
        public static final String KEYMGMT = "KeyMgmt";
        public static final String LAST_MODIFIED = "Last-Modified";
        public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
        public static final String PROXY_REQUIRE = "Proxy-Require";
        public static final String PUBLIC = "Public";
        public static final String RANGE = "Range";
        public static final String REFERER = "Referer";
        public static final String REQUIRE = "Require";
        public static final String RETRT_AFTER = "Retry-After";
        public static final String RTP_INFO = "RTP-Info";
        public static final String SCALE = "Scale";
        public static final String SESSION = "Session";
        public static final String SERVER = "Server";
        public static final String SPEED = "Speed";
        public static final String TIMESTAMP = "Timestamp";
        public static final String TRANSPORT = "Transport";
        public static final String UNSUPPORTED = "Unsupported";
        public static final String USER_AGENT = "User-Agent";
        public static final String VARY = "Vary";
        public static final String VIA = "Via";
        public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
        
        private Names() {
        }
    }
    
    public static final class Values
    {
        public static final String APPEND = "append";
        public static final String AVP = "AVP";
        public static final String BYTES = "bytes";
        public static final String CHARSET = "charset";
        public static final String CLIENT_PORT = "client_port";
        public static final String CLOCK = "clock";
        public static final String CLOSE = "close";
        public static final String COMPRESS = "compress";
        public static final String CONTINUE = "100-continue";
        public static final String DEFLATE = "deflate";
        public static final String DESTINATION = "destination";
        public static final String GZIP = "gzip";
        public static final String IDENTITY = "identity";
        public static final String INTERLEAVED = "interleaved";
        public static final String KEEP_ALIVE = "keep-alive";
        public static final String LAYERS = "layers";
        public static final String MAX_AGE = "max-age";
        public static final String MAX_STALE = "max-stale";
        public static final String MIN_FRESH = "min-fresh";
        public static final String MODE = "mode";
        public static final String MULTICAST = "multicast";
        public static final String MUST_REVALIDATE = "must-revalidate";
        public static final String NONE = "none";
        public static final String NO_CACHE = "no-cache";
        public static final String NO_TRANSFORM = "no-transform";
        public static final String ONLY_IF_CACHED = "only-if-cached";
        public static final String PORT = "port";
        public static final String PRIVATE = "private";
        public static final String PROXY_REVALIDATE = "proxy-revalidate";
        public static final String PUBLIC = "public";
        public static final String RTP = "RTP";
        public static final String RTPTIME = "rtptime";
        public static final String SEQ = "seq";
        public static final String SERVER_PORT = "server_port";
        public static final String SSRC = "ssrc";
        public static final String TCP = "TCP";
        public static final String TIME = "time";
        public static final String TIMEOUT = "timeout";
        public static final String TTL = "ttl";
        public static final String UDP = "UDP";
        public static final String UNICAST = "unicast";
        public static final String URL = "url";
        
        private Values() {
        }
    }
}
