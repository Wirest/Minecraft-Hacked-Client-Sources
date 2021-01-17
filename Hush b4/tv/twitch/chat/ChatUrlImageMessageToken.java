// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

public class ChatUrlImageMessageToken extends ChatMessageToken
{
    public String url;
    public short width;
    public short height;
    
    public ChatUrlImageMessageToken() {
        this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_URL_IMAGE;
    }
}
