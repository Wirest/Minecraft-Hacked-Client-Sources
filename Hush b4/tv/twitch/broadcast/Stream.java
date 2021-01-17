// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import tv.twitch.AuthToken;
import java.util.HashSet;
import tv.twitch.ErrorCode;

public class Stream
{
    static Stream s_Instance;
    StreamAPI m_StreamAPI;
    
    public static Stream getInstance() {
        return Stream.s_Instance;
    }
    
    public Stream(final StreamAPI streamAPI) {
        this.m_StreamAPI = null;
        this.m_StreamAPI = streamAPI;
        if (Stream.s_Instance == null) {
            Stream.s_Instance = this;
        }
    }
    
    @Override
    protected void finalize() {
        if (Stream.s_Instance == this) {
            Stream.s_Instance = null;
        }
    }
    
    public IStreamCallbacks getStreamCallbacks() {
        return this.m_StreamAPI.getStreamCallbacks();
    }
    
    public void setStreamCallbacks(final IStreamCallbacks streamCallbacks) {
        this.m_StreamAPI.setStreamCallbacks(streamCallbacks);
    }
    
    public IStatCallbacks getStatCallbacks() {
        return this.m_StreamAPI.getStatCallbacks();
    }
    
    public void setStatCallbacks(final IStatCallbacks statCallbacks) {
        this.m_StreamAPI.setStatCallbacks(statCallbacks);
    }
    
    public FrameBuffer allocateFrameBuffer(final int n) {
        return new FrameBuffer(this.m_StreamAPI, n);
    }
    
    public ErrorCode memsetFrameBuffer(final FrameBuffer frameBuffer, final int n) {
        return this.m_StreamAPI.memsetFrameBuffer(frameBuffer.getAddress(), frameBuffer.getSize(), n);
    }
    
    public ErrorCode randomizeFrameBuffer(final FrameBuffer frameBuffer) {
        return this.m_StreamAPI.randomizeFrameBuffer(frameBuffer.getAddress(), frameBuffer.getSize());
    }
    
    public ErrorCode requestAuthToken(final AuthParams authParams, final HashSet<AuthFlag> set) {
        return this.m_StreamAPI.requestAuthToken(authParams, set);
    }
    
    public ErrorCode login(final AuthToken authToken) {
        return this.m_StreamAPI.login(authToken);
    }
    
    public ErrorCode getIngestServers(final AuthToken authToken) {
        return this.m_StreamAPI.getIngestServers(authToken);
    }
    
    public ErrorCode getUserInfo(final AuthToken authToken) {
        return this.m_StreamAPI.getUserInfo(authToken);
    }
    
    public ErrorCode getStreamInfo(final AuthToken authToken, final String s) {
        return this.m_StreamAPI.getStreamInfo(authToken, s);
    }
    
    public ErrorCode setStreamInfo(final AuthToken authToken, final String s, final StreamInfoForSetting streamInfoForSetting) {
        return this.m_StreamAPI.setStreamInfo(authToken, s, streamInfoForSetting);
    }
    
    public ErrorCode getArchivingState(final AuthToken authToken) {
        return this.m_StreamAPI.getArchivingState(authToken);
    }
    
    public ErrorCode runCommercial(final AuthToken authToken) {
        return this.m_StreamAPI.runCommercial(authToken);
    }
    
    public ErrorCode setVolume(final AudioDeviceType audioDeviceType, final float n) {
        return this.m_StreamAPI.setVolume(audioDeviceType, n);
    }
    
    public float getVolume(final AudioDeviceType audioDeviceType) {
        return this.m_StreamAPI.getVolume(audioDeviceType);
    }
    
    public ErrorCode getGameNameList(final String s) {
        return this.m_StreamAPI.getGameNameList(s);
    }
    
    public ErrorCode getDefaultParams(final VideoParams videoParams) {
        return this.m_StreamAPI.getDefaultParams(videoParams);
    }
    
    public int[] getMaxResolution(final int n, final int n2, final float n3, final float n4) {
        return this.m_StreamAPI.getMaxResolution(n, n2, n3, n4);
    }
    
    public ErrorCode pollTasks() {
        return this.m_StreamAPI.pollTasks();
    }
    
    public ErrorCode pollStats() {
        return this.m_StreamAPI.pollStats();
    }
    
    public ErrorCode sendActionMetaData(final AuthToken authToken, final String s, final long n, final String s2, final String s3) {
        return this.m_StreamAPI.sendActionMetaData(authToken, s, n, s2, s3);
    }
    
    public long sendStartSpanMetaData(final AuthToken authToken, final String s, final long n, final String s2, final String s3) {
        return this.m_StreamAPI.sendStartSpanMetaData(authToken, s, n, s2, s3);
    }
    
    public ErrorCode sendEndSpanMetaData(final AuthToken authToken, final String s, final long n, final long n2, final String s2, final String s3) {
        return this.m_StreamAPI.sendEndSpanMetaData(authToken, s, n, n2, s2, s3);
    }
    
    public ErrorCode submitVideoFrame(final FrameBuffer frameBuffer) {
        return this.m_StreamAPI.submitVideoFrame(frameBuffer.getAddress());
    }
    
    public ErrorCode captureFrameBuffer_ReadPixels(final FrameBuffer frameBuffer) {
        return this.m_StreamAPI.captureFrameBuffer_ReadPixels(frameBuffer.getAddress());
    }
    
    public ErrorCode start(final VideoParams videoParams, final AudioParams audioParams, final IngestServer ingestServer, StartFlags none, final boolean b) {
        if (none == null) {
            none = StartFlags.None;
        }
        return this.m_StreamAPI.start(videoParams, audioParams, ingestServer, none.getValue(), b);
    }
    
    public ErrorCode stop(final boolean b) {
        return this.m_StreamAPI.stop(b);
    }
    
    public ErrorCode pauseVideo() {
        return this.m_StreamAPI.pauseVideo();
    }
    
    public long getStreamTime() {
        return this.m_StreamAPI.getStreamTime();
    }
    
    static {
        Stream.s_Instance = null;
    }
}
