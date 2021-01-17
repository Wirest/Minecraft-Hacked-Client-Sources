// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import java.util.HashSet;
import tv.twitch.ErrorCode;

public class StandardChatAPI extends ChatAPI
{
    @Override
    protected void finalize() {
    }
    
    private static native ErrorCode TTV_Java_Chat_Initialize(final int p0, final IChatAPIListener p1);
    
    private static native ErrorCode TTV_Java_Chat_Shutdown();
    
    private static native ErrorCode TTV_Java_Chat_Connect(final String p0, final String p1, final String p2, final IChatChannelListener p3);
    
    private static native ErrorCode TTV_Java_Chat_ConnectAnonymous(final String p0, final IChatChannelListener p1);
    
    private static native ErrorCode TTV_Java_Chat_Disconnect(final String p0);
    
    private static native ErrorCode TTV_Java_Chat_SendMessage(final String p0, final String p1);
    
    private static native ErrorCode TTV_Java_Chat_FlushEvents();
    
    private static native ErrorCode TTV_Java_Chat_DownloadEmoticonData();
    
    private static native ErrorCode TTV_Java_Chat_GetEmoticonData(final ChatEmoticonData p0);
    
    private static native ErrorCode TTV_Java_Chat_ClearEmoticonData();
    
    private static native ErrorCode TTV_Java_Chat_DownloadBadgeData(final String p0);
    
    private static native ErrorCode TTV_Java_Chat_GetBadgeData(final String p0, final ChatBadgeData p1);
    
    private static native ErrorCode TTV_Java_Chat_ClearBadgeData(final String p0);
    
    private static native long TTV_Java_Chat_GetMessageFlushInterval();
    
    private static native ErrorCode TTV_Java_Chat_SetMessageFlushInterval(final long p0);
    
    private static native long TTV_Java_Chat_GetUserChangeEventInterval();
    
    private static native ErrorCode TTV_Java_Chat_SetUserChangeEventInterval(final long p0);
    
    @Override
    public ErrorCode initialize(final HashSet<ChatTokenizationOption> set, final IChatAPIListener chatAPIListener) {
        return TTV_Java_Chat_Initialize(ChatTokenizationOption.getNativeValue(set), chatAPIListener);
    }
    
    @Override
    public ErrorCode shutdown() {
        return TTV_Java_Chat_Shutdown();
    }
    
    @Override
    public ErrorCode connect(final String s, final String s2, final String s3, final IChatChannelListener chatChannelListener) {
        return TTV_Java_Chat_Connect(s, s2, s3, chatChannelListener);
    }
    
    @Override
    public ErrorCode connectAnonymous(final String s, final IChatChannelListener chatChannelListener) {
        return TTV_Java_Chat_ConnectAnonymous(s, chatChannelListener);
    }
    
    @Override
    public ErrorCode disconnect(final String s) {
        return TTV_Java_Chat_Disconnect(s);
    }
    
    @Override
    public ErrorCode sendMessage(final String s, final String s2) {
        return TTV_Java_Chat_SendMessage(s, s2);
    }
    
    @Override
    public ErrorCode flushEvents() {
        return TTV_Java_Chat_FlushEvents();
    }
    
    @Override
    public ErrorCode downloadEmoticonData() {
        return TTV_Java_Chat_DownloadEmoticonData();
    }
    
    @Override
    public ErrorCode getEmoticonData(final ChatEmoticonData chatEmoticonData) {
        return TTV_Java_Chat_GetEmoticonData(chatEmoticonData);
    }
    
    @Override
    public ErrorCode clearEmoticonData() {
        return TTV_Java_Chat_ClearEmoticonData();
    }
    
    @Override
    public ErrorCode downloadBadgeData(final String s) {
        return TTV_Java_Chat_DownloadBadgeData(s);
    }
    
    @Override
    public ErrorCode getBadgeData(final String s, final ChatBadgeData chatBadgeData) {
        return TTV_Java_Chat_GetBadgeData(s, chatBadgeData);
    }
    
    @Override
    public ErrorCode clearBadgeData(final String s) {
        return TTV_Java_Chat_ClearBadgeData(s);
    }
    
    @Override
    public int getMessageFlushInterval() {
        return (int)TTV_Java_Chat_GetMessageFlushInterval();
    }
    
    @Override
    public ErrorCode setMessageFlushInterval(final int n) {
        return TTV_Java_Chat_SetMessageFlushInterval(n);
    }
    
    @Override
    public int getUserChangeEventInterval() {
        return (int)TTV_Java_Chat_GetUserChangeEventInterval();
    }
    
    @Override
    public ErrorCode setUserChangeEventInterval(final int n) {
        return TTV_Java_Chat_SetUserChangeEventInterval(n);
    }
}
