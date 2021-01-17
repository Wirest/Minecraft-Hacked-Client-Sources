// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import tv.twitch.ErrorCode;

public interface IChatChannelListener
{
    void chatStatusCallback(final String p0, final ErrorCode p1);
    
    void chatChannelMembershipCallback(final String p0, final ChatEvent p1, final ChatChannelInfo p2);
    
    void chatChannelUserChangeCallback(final String p0, final ChatUserInfo[] p1, final ChatUserInfo[] p2, final ChatUserInfo[] p3);
    
    void chatChannelRawMessageCallback(final String p0, final ChatRawMessage[] p1);
    
    void chatChannelTokenizedMessageCallback(final String p0, final ChatTokenizedMessage[] p1);
    
    void chatClearCallback(final String p0, final String p1);
    
    void chatBadgeDataDownloadCallback(final String p0, final ErrorCode p1);
}
