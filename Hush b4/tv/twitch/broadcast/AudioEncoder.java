// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AudioEncoder
{
    TTV_AUD_ENC_DEFAULT(-1), 
    TTV_AUD_ENC_LAMEMP3(0), 
    TTV_AUD_ENC_APPLEAAC(1);
    
    private static Map<Integer, AudioEncoder> s_Map;
    private int m_Value;
    
    public static AudioEncoder lookupValue(final int i) {
        return AudioEncoder.s_Map.get(i);
    }
    
    private AudioEncoder(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        AudioEncoder.s_Map = new HashMap<Integer, AudioEncoder>();
        for (final AudioEncoder audioEncoder : EnumSet.allOf(AudioEncoder.class)) {
            AudioEncoder.s_Map.put(audioEncoder.getValue(), audioEncoder);
        }
    }
}
