// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatMessageTokenType
{
    TTV_CHAT_MSGTOKEN_TEXT(0), 
    TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE(1), 
    TTV_CHAT_MSGTOKEN_URL_IMAGE(2);
    
    private static Map<Integer, ChatMessageTokenType> s_Map;
    private int m_Value;
    
    public static ChatMessageTokenType lookupValue(final int i) {
        return ChatMessageTokenType.s_Map.get(i);
    }
    
    private ChatMessageTokenType(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        ChatMessageTokenType.s_Map = new HashMap<Integer, ChatMessageTokenType>();
        for (final ChatMessageTokenType chatMessageTokenType : EnumSet.allOf(ChatMessageTokenType.class)) {
            ChatMessageTokenType.s_Map.put(chatMessageTokenType.getValue(), chatMessageTokenType);
        }
    }
}
