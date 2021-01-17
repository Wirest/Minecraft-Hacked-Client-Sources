// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public interface IStreamCallbacks
{
    void requestAuthTokenCallback(final ErrorCode p0, final AuthToken p1);
    
    void loginCallback(final ErrorCode p0, final ChannelInfo p1);
    
    void getIngestServersCallback(final ErrorCode p0, final IngestList p1);
    
    void getUserInfoCallback(final ErrorCode p0, final UserInfo p1);
    
    void getStreamInfoCallback(final ErrorCode p0, final StreamInfo p1);
    
    void getArchivingStateCallback(final ErrorCode p0, final ArchivingState p1);
    
    void runCommercialCallback(final ErrorCode p0);
    
    void setStreamInfoCallback(final ErrorCode p0);
    
    void getGameNameListCallback(final ErrorCode p0, final GameInfoList p1);
    
    void bufferUnlockCallback(final long p0);
    
    void startCallback(final ErrorCode p0);
    
    void stopCallback(final ErrorCode p0);
    
    void sendActionMetaDataCallback(final ErrorCode p0);
    
    void sendStartSpanMetaDataCallback(final ErrorCode p0);
    
    void sendEndSpanMetaDataCallback(final ErrorCode p0);
}
