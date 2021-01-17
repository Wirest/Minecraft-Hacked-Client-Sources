// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import tv.twitch.ErrorCode;
import java.util.HashSet;

public abstract class ChatAPI
{
    public abstract ErrorCode initialize(final HashSet<ChatTokenizationOption> p0, final IChatAPIListener p1);
    
    public abstract ErrorCode shutdown();
    
    public abstract ErrorCode connect(final String p0, final String p1, final String p2, final IChatChannelListener p3);
    
    public abstract ErrorCode connectAnonymous(final String p0, final IChatChannelListener p1);
    
    public abstract ErrorCode disconnect(final String p0);
    
    public abstract ErrorCode sendMessage(final String p0, final String p1);
    
    public abstract ErrorCode flushEvents();
    
    public abstract ErrorCode downloadEmoticonData();
    
    public abstract ErrorCode getEmoticonData(final ChatEmoticonData p0);
    
    public abstract ErrorCode clearEmoticonData();
    
    public abstract ErrorCode downloadBadgeData(final String p0);
    
    public abstract ErrorCode getBadgeData(final String p0, final ChatBadgeData p1);
    
    public abstract ErrorCode clearBadgeData(final String p0);
    
    public abstract int getMessageFlushInterval();
    
    public abstract ErrorCode setMessageFlushInterval(final int p0);
    
    public abstract int getUserChangeEventInterval();
    
    public abstract ErrorCode setUserChangeEventInterval(final int p0);
}
