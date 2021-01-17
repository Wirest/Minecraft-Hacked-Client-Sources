// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StatType
{
    TTV_ST_RTMPSTATE(0), 
    TTV_ST_RTMPDATASENT(1);
    
    private static Map<Integer, StatType> s_Map;
    private int m_Value;
    
    public static StatType lookupValue(final int i) {
        return StatType.s_Map.get(i);
    }
    
    private StatType(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        StatType.s_Map = new HashMap<Integer, StatType>();
        for (final StatType statType : EnumSet.allOf(StatType.class)) {
            StatType.s_Map.put(statType.getValue(), statType);
        }
    }
}
