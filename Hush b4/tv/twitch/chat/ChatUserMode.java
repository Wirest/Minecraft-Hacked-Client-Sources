// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatUserMode
{
    TTV_CHAT_USERMODE_VIEWER(0), 
    TTV_CHAT_USERMODE_MODERATOR(1), 
    TTV_CHAT_USERMODE_BROADCASTER(2), 
    TTV_CHAT_USERMODE_ADMINSTRATOR(4), 
    TTV_CHAT_USERMODE_STAFF(8), 
    TTV_CHAT_USERMODE_BANNED(1073741824);
    
    private static Map<Integer, ChatUserMode> s_Map;
    private int m_Value;
    
    public static ChatUserMode lookupValue(final int i) {
        return ChatUserMode.s_Map.get(i);
    }
    
    private ChatUserMode(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        ChatUserMode.s_Map = new HashMap<Integer, ChatUserMode>();
        for (final ChatUserMode chatUserMode : EnumSet.allOf(ChatUserMode.class)) {
            ChatUserMode.s_Map.put(chatUserMode.getValue(), chatUserMode);
        }
    }
}
