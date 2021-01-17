// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatUserSubscription
{
    TTV_CHAT_USERSUB_NONE(0), 
    TTV_CHAT_USERSUB_SUBSCRIBER(1), 
    TTV_CHAT_USERSUB_TURBO(2);
    
    private static Map<Integer, ChatUserSubscription> s_Map;
    private int m_Value;
    
    public static ChatUserSubscription lookupValue(final int i) {
        return ChatUserSubscription.s_Map.get(i);
    }
    
    private ChatUserSubscription(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        ChatUserSubscription.s_Map = new HashMap<Integer, ChatUserSubscription>();
        for (final ChatUserSubscription chatUserSubscription : EnumSet.allOf(ChatUserSubscription.class)) {
            ChatUserSubscription.s_Map.put(chatUserSubscription.getValue(), chatUserSubscription);
        }
    }
}
