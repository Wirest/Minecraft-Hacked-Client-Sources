// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.chat;

import tv.twitch.ErrorCode;
import java.util.HashSet;

public class Chat
{
    private static Chat s_Instance;
    private ChatAPI m_ChatAPI;
    
    public static Chat getInstance() {
        return Chat.s_Instance;
    }
    
    public Chat(final ChatAPI chatAPI) {
        this.m_ChatAPI = null;
        this.m_ChatAPI = chatAPI;
        if (Chat.s_Instance == null) {
            Chat.s_Instance = this;
        }
    }
    
    public ErrorCode initialize(final HashSet<ChatTokenizationOption> set, final IChatAPIListener chatAPIListener) {
        return this.m_ChatAPI.initialize(set, chatAPIListener);
    }
    
    public ErrorCode shutdown() {
        return this.m_ChatAPI.shutdown();
    }
    
    public ErrorCode connect(final String s, final String s2, final String s3, final IChatChannelListener chatChannelListener) {
        return this.m_ChatAPI.connect(s, s2, s3, chatChannelListener);
    }
    
    public ErrorCode connectAnonymous(final String s, final IChatChannelListener chatChannelListener) {
        return this.m_ChatAPI.connectAnonymous(s, chatChannelListener);
    }
    
    public ErrorCode disconnect(final String s) {
        return this.m_ChatAPI.disconnect(s);
    }
    
    public ErrorCode sendMessage(final String s, final String s2) {
        return this.m_ChatAPI.sendMessage(s, s2);
    }
    
    public ErrorCode flushEvents() {
        return this.m_ChatAPI.flushEvents();
    }
    
    public ErrorCode downloadEmoticonData() {
        return this.m_ChatAPI.downloadEmoticonData();
    }
    
    public ErrorCode getEmoticonData(final ChatEmoticonData chatEmoticonData) {
        return this.m_ChatAPI.getEmoticonData(chatEmoticonData);
    }
    
    public ErrorCode clearEmoticonData() {
        return this.m_ChatAPI.clearEmoticonData();
    }
    
    public ErrorCode downloadBadgeData(final String s) {
        return this.m_ChatAPI.downloadBadgeData(s);
    }
    
    public ErrorCode getBadgeData(final String s, final ChatBadgeData chatBadgeData) {
        return this.m_ChatAPI.getBadgeData(s, chatBadgeData);
    }
    
    public ErrorCode clearBadgeData(final String s) {
        return this.m_ChatAPI.clearBadgeData(s);
    }
    
    public int getMessageFlushInterval() {
        return this.m_ChatAPI.getMessageFlushInterval();
    }
    
    public ErrorCode setMessageFlushInterval(final int messageFlushInterval) {
        return this.m_ChatAPI.setMessageFlushInterval(messageFlushInterval);
    }
    
    public int getUserChangeEventInterval() {
        return this.m_ChatAPI.getUserChangeEventInterval();
    }
    
    public ErrorCode setUserChangeEventInterval(final int userChangeEventInterval) {
        return this.m_ChatAPI.setUserChangeEventInterval(userChangeEventInterval);
    }
    
    static {
        Chat.s_Instance = null;
    }
}
