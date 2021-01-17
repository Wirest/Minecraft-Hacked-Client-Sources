// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatEvent
{
    TTV_CHAT_JOINED_CHANNEL(0), 
    TTV_CHAT_LEFT_CHANNEL(1);
    
    private static Map<Integer, ChatEvent> s_Map;
    private int m_Value;
    
    public static ChatEvent lookupValue(final int i) {
        return ChatEvent.s_Map.get(i);
    }
    
    private ChatEvent(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        ChatEvent.s_Map = new HashMap<Integer, ChatEvent>();
        for (final ChatEvent chatEvent : EnumSet.allOf(ChatEvent.class)) {
            ChatEvent.s_Map.put(chatEvent.getValue(), chatEvent);
        }
    }
}
