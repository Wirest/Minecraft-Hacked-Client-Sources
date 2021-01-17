// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Set;

public interface SpdySettingsFrame extends SpdyFrame
{
    public static final int SETTINGS_MINOR_VERSION = 0;
    public static final int SETTINGS_UPLOAD_BANDWIDTH = 1;
    public static final int SETTINGS_DOWNLOAD_BANDWIDTH = 2;
    public static final int SETTINGS_ROUND_TRIP_TIME = 3;
    public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 4;
    public static final int SETTINGS_CURRENT_CWND = 5;
    public static final int SETTINGS_DOWNLOAD_RETRANS_RATE = 6;
    public static final int SETTINGS_INITIAL_WINDOW_SIZE = 7;
    public static final int SETTINGS_CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
    
    Set<Integer> ids();
    
    boolean isSet(final int p0);
    
    int getValue(final int p0);
    
    SpdySettingsFrame setValue(final int p0, final int p1);
    
    SpdySettingsFrame setValue(final int p0, final int p1, final boolean p2, final boolean p3);
    
    SpdySettingsFrame removeValue(final int p0);
    
    boolean isPersistValue(final int p0);
    
    SpdySettingsFrame setPersistValue(final int p0, final boolean p1);
    
    boolean isPersisted(final int p0);
    
    SpdySettingsFrame setPersisted(final int p0, final boolean p1);
    
    boolean clearPreviouslyPersistedSettings();
    
    SpdySettingsFrame setClearPreviouslyPersistedSettings(final boolean p0);
}
