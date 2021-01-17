// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EncodingCpuUsage
{
    TTV_ECU_LOW(0), 
    TTV_ECU_MEDIUM(1), 
    TTV_ECU_HIGH(2);
    
    private static Map<Integer, EncodingCpuUsage> s_Map;
    private int m_Value;
    
    public static EncodingCpuUsage lookupValue(final int i) {
        return EncodingCpuUsage.s_Map.get(i);
    }
    
    private EncodingCpuUsage(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        EncodingCpuUsage.s_Map = new HashMap<Integer, EncodingCpuUsage>();
        for (final EncodingCpuUsage encodingCpuUsage : EnumSet.allOf(EncodingCpuUsage.class)) {
            EncodingCpuUsage.s_Map.put(encodingCpuUsage.getValue(), encodingCpuUsage);
        }
    }
}
