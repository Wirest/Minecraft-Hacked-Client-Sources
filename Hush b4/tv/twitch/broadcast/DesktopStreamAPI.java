// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.HashSet;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;

public class DesktopStreamAPI extends StreamAPI
{
    @Override
    protected void finalize() {
        TTV_Java_SetStreamCallbacks(null);
        TTV_Java_SetStatCallbacks(null);
    }
    
    private static native void TTV_Java_SetStreamCallbacks(final IStreamCallbacks p0);
    
    private static native IStreamCallbacks TTV_Java_GetStreamCallbacks();
    
    private static native void TTV_Java_SetStatCallbacks(final IStatCallbacks p0);
    
    private static native IStatCallbacks TTV_Java_GetStatCallbacks();
    
    private static native ErrorCode TTV_Java_RequestAuthToken(final AuthParams p0, final int p1);
    
    private static native ErrorCode TTV_Java_Login(final AuthToken p0);
    
    private static native ErrorCode TTV_Java_GetIngestServers(final AuthToken p0);
    
    private static native ErrorCode TTV_Java_GetUserInfo(final AuthToken p0);
    
    private static native ErrorCode TTV_Java_GetStreamInfo(final AuthToken p0, final String p1);
    
    private static native ErrorCode TTV_Java_SetStreamInfo(final AuthToken p0, final String p1, final StreamInfoForSetting p2);
    
    private static native ErrorCode TTV_Java_GetArchivingState(final AuthToken p0);
    
    private static native ErrorCode TTV_Java_RunCommercial(final AuthToken p0);
    
    private static native ErrorCode TTV_Java_SetVolume(final AudioDeviceType p0, final float p1);
    
    private static native float TTV_Java_GetVolume(final AudioDeviceType p0);
    
    private static native ErrorCode TTV_Java_GetGameNameList(final String p0);
    
    private static native ErrorCode TTV_Java_GetDefaultParams(final VideoParams p0);
    
    private static native ErrorCode TTV_GetMaxResolution(final int p0, final int p1, final float p2, final float p3, final int[] p4);
    
    private static native ErrorCode TTV_Java_PollTasks();
    
    private static native ErrorCode TTV_Java_PollStats();
    
    private static native ErrorCode TTV_Java_Init(final int p0);
    
    private static native ErrorCode TTV_Java_Shutdown();
    
    private static native ErrorCode TTV_Java_SendActionMetaData(final AuthToken p0, final String p1, final long p2, final String p3, final String p4);
    
    private static native long TTV_Java_SendStartSpanMetaData(final AuthToken p0, final String p1, final long p2, final String p3, final String p4);
    
    private static native ErrorCode TTV_Java_SendEndSpanMetaData(final AuthToken p0, final String p1, final long p2, final long p3, final String p4, final String p5);
    
    private static native ErrorCode TTV_Java_SubmitVideoFrame(final long p0);
    
    private static native ErrorCode TTV_Java_Start(final VideoParams p0, final AudioParams p1, final IngestServer p2, final int p3, final boolean p4);
    
    private static native ErrorCode TTV_Java_Stop(final boolean p0);
    
    private static native ErrorCode TTV_Java_PauseVideo();
    
    private static native long TTV_Java_AllocateFrameBuffer(final int p0);
    
    private static native ErrorCode TTV_Java_FreeFrameBuffer(final long p0);
    
    private static native ErrorCode TTV_Java_MemsetFrameBuffer(final long p0, final int p1, final int p2);
    
    private static native ErrorCode TTV_Java_RandomizeFrameBuffer(final long p0, final int p1);
    
    private static native ErrorCode TTV_Java_CaptureFrameBuffer_ReadPixels(final long p0);
    
    private static native long TTV_Java_GetStreamTime();
    
    @Override
    public void setStreamCallbacks(final IStreamCallbacks streamCallbacks) {
        TTV_Java_SetStreamCallbacks(streamCallbacks);
    }
    
    @Override
    public IStreamCallbacks getStreamCallbacks() {
        return TTV_Java_GetStreamCallbacks();
    }
    
    @Override
    public void setStatCallbacks(final IStatCallbacks statCallbacks) {
        TTV_Java_SetStatCallbacks(statCallbacks);
    }
    
    @Override
    public IStatCallbacks getStatCallbacks() {
        return TTV_Java_GetStatCallbacks();
    }
    
    @Override
    public ErrorCode requestAuthToken(final AuthParams authParams, HashSet<AuthFlag> set) {
        if (set == null) {
            set = new HashSet<AuthFlag>();
        }
        return TTV_Java_RequestAuthToken(authParams, AuthFlag.getNativeValue(set));
    }
    
    @Override
    public ErrorCode login(final AuthToken authToken) {
        return TTV_Java_Login(authToken);
    }
    
    @Override
    public ErrorCode getIngestServers(final AuthToken authToken) {
        return TTV_Java_GetIngestServers(authToken);
    }
    
    @Override
    public ErrorCode getUserInfo(final AuthToken authToken) {
        return TTV_Java_GetUserInfo(authToken);
    }
    
    @Override
    public ErrorCode getStreamInfo(final AuthToken authToken, final String s) {
        return TTV_Java_GetStreamInfo(authToken, s);
    }
    
    @Override
    public ErrorCode setStreamInfo(final AuthToken authToken, final String s, final StreamInfoForSetting streamInfoForSetting) {
        return TTV_Java_SetStreamInfo(authToken, s, streamInfoForSetting);
    }
    
    @Override
    public ErrorCode getArchivingState(final AuthToken authToken) {
        return TTV_Java_GetArchivingState(authToken);
    }
    
    @Override
    public ErrorCode runCommercial(final AuthToken authToken) {
        return TTV_Java_RunCommercial(authToken);
    }
    
    @Override
    public ErrorCode setVolume(final AudioDeviceType audioDeviceType, final float n) {
        return TTV_Java_SetVolume(audioDeviceType, n);
    }
    
    @Override
    public float getVolume(final AudioDeviceType audioDeviceType) {
        return TTV_Java_GetVolume(audioDeviceType);
    }
    
    @Override
    public ErrorCode getGameNameList(final String s) {
        return TTV_Java_GetGameNameList(s);
    }
    
    @Override
    public ErrorCode getDefaultParams(final VideoParams videoParams) {
        return TTV_Java_GetDefaultParams(videoParams);
    }
    
    @Override
    public int[] getMaxResolution(final int n, final int n2, final float n3, final float n4) {
        final int[] array = { 0, 0 };
        TTV_GetMaxResolution(n, n2, n3, n4, array);
        return array;
    }
    
    @Override
    public ErrorCode pollTasks() {
        return TTV_Java_PollTasks();
    }
    
    @Override
    public ErrorCode pollStats() {
        return TTV_Java_PollStats();
    }
    
    @Override
    public ErrorCode sendActionMetaData(final AuthToken authToken, final String s, final long n, final String s2, final String s3) {
        return TTV_Java_SendActionMetaData(authToken, s, n, s2, s3);
    }
    
    @Override
    public long sendStartSpanMetaData(final AuthToken authToken, final String s, final long n, final String s2, final String s3) {
        return TTV_Java_SendStartSpanMetaData(authToken, s, n, s2, s3);
    }
    
    @Override
    public ErrorCode sendEndSpanMetaData(final AuthToken authToken, final String s, final long n, final long n2, final String s2, final String s3) {
        return TTV_Java_SendEndSpanMetaData(authToken, s, n, n2, s2, s3);
    }
    
    @Override
    public ErrorCode submitVideoFrame(final long n) {
        return TTV_Java_SubmitVideoFrame(n);
    }
    
    @Override
    public ErrorCode start(final VideoParams videoParams, final AudioParams audioParams, final IngestServer ingestServer, final int n, final boolean b) {
        return TTV_Java_Start(videoParams, audioParams, ingestServer, n, b);
    }
    
    @Override
    public ErrorCode stop(final boolean b) {
        return TTV_Java_Stop(b);
    }
    
    @Override
    public ErrorCode pauseVideo() {
        return TTV_Java_PauseVideo();
    }
    
    @Override
    public long allocateFrameBuffer(final int n) {
        return TTV_Java_AllocateFrameBuffer(n);
    }
    
    @Override
    public ErrorCode freeFrameBuffer(final long n) {
        return TTV_Java_FreeFrameBuffer(n);
    }
    
    @Override
    public ErrorCode memsetFrameBuffer(final long n, final int n2, final int n3) {
        return TTV_Java_MemsetFrameBuffer(n, n2, n3);
    }
    
    @Override
    public ErrorCode randomizeFrameBuffer(final long n, final int n2) {
        return TTV_Java_RandomizeFrameBuffer(n, n2);
    }
    
    @Override
    public ErrorCode captureFrameBuffer_ReadPixels(final long n) {
        return TTV_Java_CaptureFrameBuffer_ReadPixels(n);
    }
    
    @Override
    public long getStreamTime() {
        return TTV_Java_GetStreamTime();
    }
}
