// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum VideoEncoder
{
    TTV_VID_ENC_DISABLE(-2), 
    TTV_VID_ENC_DEFAULT(-1), 
    TTV_VID_ENC_INTEL(0), 
    TTV_VID_ENC_APPLE(2), 
    TTV_VID_ENC_PLUGIN(100);
    
    private static Map<Integer, VideoEncoder> s_Map;
    private int m_Value;
    
    public static VideoEncoder lookupValue(final int i) {
        return VideoEncoder.s_Map.get(i);
    }
    
    private VideoEncoder(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        VideoEncoder.s_Map = new HashMap<Integer, VideoEncoder>();
        for (final VideoEncoder videoEncoder : EnumSet.allOf(VideoEncoder.class)) {
            VideoEncoder.s_Map.put(videoEncoder.getValue(), videoEncoder);
        }
    }
}
