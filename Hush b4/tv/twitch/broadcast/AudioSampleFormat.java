// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AudioSampleFormat
{
    TTV_ASF_PCM_S16(0);
    
    private static Map<Integer, AudioSampleFormat> s_Map;
    private int m_Value;
    
    public static AudioSampleFormat lookupValue(final int i) {
        return AudioSampleFormat.s_Map.get(i);
    }
    
    private AudioSampleFormat(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        AudioSampleFormat.s_Map = new HashMap<Integer, AudioSampleFormat>();
        for (final AudioSampleFormat audioSampleFormat : EnumSet.allOf(AudioSampleFormat.class)) {
            AudioSampleFormat.s_Map.put(audioSampleFormat.getValue(), audioSampleFormat);
        }
    }
}
