// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

public class ChatTextureImageMessageToken extends ChatMessageToken
{
    public int sheetIndex;
    public short x1;
    public short y1;
    public short x2;
    public short y2;
    
    public ChatTextureImageMessageToken() {
        this.sheetIndex = -1;
        this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE;
    }
}
