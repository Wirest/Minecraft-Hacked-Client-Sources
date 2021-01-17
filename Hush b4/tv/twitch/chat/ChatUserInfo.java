// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.HashSet;

public class ChatUserInfo
{
    public String displayName;
    public HashSet<ChatUserMode> modes;
    public HashSet<ChatUserSubscription> subscriptions;
    public int nameColorARGB;
    public boolean ignore;
    
    public ChatUserInfo() {
        this.displayName = null;
        this.modes = new HashSet<ChatUserMode>();
        this.subscriptions = new HashSet<ChatUserSubscription>();
        this.nameColorARGB = 0;
        this.ignore = false;
    }
}
