// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MessageLevel
{
    TTV_ML_DEBUG(0), 
    TTV_ML_INFO(1), 
    TTV_ML_WARNING(2), 
    TTV_ML_ERROR(3), 
    TTV_ML_CHAT(4), 
    TTV_ML_NONE(5);
    
    private static Map<Integer, MessageLevel> s_Map;
    private int m_Value;
    
    public static MessageLevel lookupValue(final int i) {
        return MessageLevel.s_Map.get(i);
    }
    
    private MessageLevel(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        MessageLevel.s_Map = new HashMap<Integer, MessageLevel>();
        for (final MessageLevel messageLevel : EnumSet.allOf(MessageLevel.class)) {
            MessageLevel.s_Map.put(messageLevel.getValue(), messageLevel);
        }
    }
}
