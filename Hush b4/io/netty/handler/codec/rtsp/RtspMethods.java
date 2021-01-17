// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

import java.util.HashMap;
import java.util.Map;
import io.netty.handler.codec.http.HttpMethod;

public final class RtspMethods
{
    public static final HttpMethod OPTIONS;
    public static final HttpMethod DESCRIBE;
    public static final HttpMethod ANNOUNCE;
    public static final HttpMethod SETUP;
    public static final HttpMethod PLAY;
    public static final HttpMethod PAUSE;
    public static final HttpMethod TEARDOWN;
    public static final HttpMethod GET_PARAMETER;
    public static final HttpMethod SET_PARAMETER;
    public static final HttpMethod REDIRECT;
    public static final HttpMethod RECORD;
    private static final Map<String, HttpMethod> methodMap;
    
    public static HttpMethod valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = name.trim().toUpperCase();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        final HttpMethod result = RtspMethods.methodMap.get(name);
        if (result != null) {
            return result;
        }
        return new HttpMethod(name);
    }
    
    private RtspMethods() {
    }
    
    static {
        OPTIONS = HttpMethod.OPTIONS;
        DESCRIBE = new HttpMethod("DESCRIBE");
        ANNOUNCE = new HttpMethod("ANNOUNCE");
        SETUP = new HttpMethod("SETUP");
        PLAY = new HttpMethod("PLAY");
        PAUSE = new HttpMethod("PAUSE");
        TEARDOWN = new HttpMethod("TEARDOWN");
        GET_PARAMETER = new HttpMethod("GET_PARAMETER");
        SET_PARAMETER = new HttpMethod("SET_PARAMETER");
        REDIRECT = new HttpMethod("REDIRECT");
        RECORD = new HttpMethod("RECORD");
        (methodMap = new HashMap<String, HttpMethod>()).put(RtspMethods.DESCRIBE.toString(), RtspMethods.DESCRIBE);
        RtspMethods.methodMap.put(RtspMethods.ANNOUNCE.toString(), RtspMethods.ANNOUNCE);
        RtspMethods.methodMap.put(RtspMethods.GET_PARAMETER.toString(), RtspMethods.GET_PARAMETER);
        RtspMethods.methodMap.put(RtspMethods.OPTIONS.toString(), RtspMethods.OPTIONS);
        RtspMethods.methodMap.put(RtspMethods.PAUSE.toString(), RtspMethods.PAUSE);
        RtspMethods.methodMap.put(RtspMethods.PLAY.toString(), RtspMethods.PLAY);
        RtspMethods.methodMap.put(RtspMethods.RECORD.toString(), RtspMethods.RECORD);
        RtspMethods.methodMap.put(RtspMethods.REDIRECT.toString(), RtspMethods.REDIRECT);
        RtspMethods.methodMap.put(RtspMethods.SETUP.toString(), RtspMethods.SETUP);
        RtspMethods.methodMap.put(RtspMethods.SET_PARAMETER.toString(), RtspMethods.SET_PARAMETER);
        RtspMethods.methodMap.put(RtspMethods.TEARDOWN.toString(), RtspMethods.TEARDOWN);
    }
}
