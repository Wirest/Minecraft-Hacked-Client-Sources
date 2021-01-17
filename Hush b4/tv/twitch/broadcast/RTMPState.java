// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum RTMPState
{
    Invalid(-1), 
    Idle(0), 
    Initialize(1), 
    Handshake(2), 
    Connect(3), 
    CreateStream(4), 
    Publish(5), 
    SendVideo(6), 
    Shutdown(7), 
    Error(8);
    
    private static Map<Integer, RTMPState> s_Map;
    private int m_Value;
    
    public static RTMPState lookupValue(final int i) {
        return RTMPState.s_Map.get(i);
    }
    
    private RTMPState(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        RTMPState.s_Map = new HashMap<Integer, RTMPState>();
        for (final RTMPState rtmpState : EnumSet.allOf(RTMPState.class)) {
            RTMPState.s_Map.put(rtmpState.getValue(), rtmpState);
        }
    }
}
