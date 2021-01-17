// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.HashSet;

public class ChatRawMessage
{
    public String userName;
    public String message;
    public HashSet<ChatUserMode> modes;
    public HashSet<ChatUserSubscription> subscriptions;
    public int nameColorARGB;
    public boolean action;
    
    public ChatRawMessage() {
        this.userName = null;
        this.message = null;
        this.modes = new HashSet<ChatUserMode>();
        this.subscriptions = new HashSet<ChatUserSubscription>();
        this.nameColorARGB = 0;
        this.action = false;
    }
}
