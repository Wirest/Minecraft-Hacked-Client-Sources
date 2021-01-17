// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import java.util.HashSet;

public abstract class StreamAPI
{
    public abstract void setStreamCallbacks(final IStreamCallbacks p0);
    
    public abstract IStreamCallbacks getStreamCallbacks();
    
    public abstract void setStatCallbacks(final IStatCallbacks p0);
    
    public abstract IStatCallbacks getStatCallbacks();
    
    public abstract ErrorCode requestAuthToken(final AuthParams p0, final HashSet<AuthFlag> p1);
    
    public abstract ErrorCode login(final AuthToken p0);
    
    public abstract ErrorCode getIngestServers(final AuthToken p0);
    
    public abstract ErrorCode getUserInfo(final AuthToken p0);
    
    public abstract ErrorCode getStreamInfo(final AuthToken p0, final String p1);
    
    public abstract ErrorCode setStreamInfo(final AuthToken p0, final String p1, final StreamInfoForSetting p2);
    
    public abstract ErrorCode getArchivingState(final AuthToken p0);
    
    public abstract ErrorCode runCommercial(final AuthToken p0);
    
    public abstract ErrorCode setVolume(final AudioDeviceType p0, final float p1);
    
    public abstract float getVolume(final AudioDeviceType p0);
    
    public abstract ErrorCode getGameNameList(final String p0);
    
    public abstract ErrorCode getDefaultParams(final VideoParams p0);
    
    public abstract int[] getMaxResolution(final int p0, final int p1, final float p2, final float p3);
    
    public abstract ErrorCode pollTasks();
    
    public abstract ErrorCode pollStats();
    
    public abstract ErrorCode sendActionMetaData(final AuthToken p0, final String p1, final long p2, final String p3, final String p4);
    
    public abstract long sendStartSpanMetaData(final AuthToken p0, final String p1, final long p2, final String p3, final String p4);
    
    public abstract ErrorCode sendEndSpanMetaData(final AuthToken p0, final String p1, final long p2, final long p3, final String p4, final String p5);
    
    public abstract ErrorCode submitVideoFrame(final long p0);
    
    public abstract ErrorCode start(final VideoParams p0, final AudioParams p1, final IngestServer p2, final int p3, final boolean p4);
    
    public abstract ErrorCode stop(final boolean p0);
    
    public abstract ErrorCode pauseVideo();
    
    public abstract long allocateFrameBuffer(final int p0);
    
    public abstract ErrorCode freeFrameBuffer(final long p0);
    
    public abstract ErrorCode memsetFrameBuffer(final long p0, final int p1, final int p2);
    
    public abstract ErrorCode randomizeFrameBuffer(final long p0, final int p1);
    
    public abstract ErrorCode captureFrameBuffer_ReadPixels(final long p0);
    
    public abstract long getStreamTime();
}
