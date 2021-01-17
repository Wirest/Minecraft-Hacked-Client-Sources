// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import tv.twitch.ErrorCode;

public interface IChatAPIListener
{
    void chatInitializationCallback(final ErrorCode p0);
    
    void chatShutdownCallback(final ErrorCode p0);
    
    void chatEmoticonDataDownloadCallback(final ErrorCode p0);
}
