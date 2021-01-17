// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.stream;

import tv.twitch.chat.ChatChannelInfo;
import tv.twitch.chat.ChatEvent;
import java.util.ListIterator;
import com.google.common.collect.Lists;
import tv.twitch.chat.ChatBadgeData;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatRawMessage;
import java.util.LinkedList;
import tv.twitch.chat.ChatUserInfo;
import java.util.List;
import tv.twitch.chat.IChatChannelListener;
import tv.twitch.chat.ChatTokenizationOption;
import java.util.HashSet;
import tv.twitch.chat.ChatAPI;
import tv.twitch.chat.StandardChatAPI;
import tv.twitch.CoreAPI;
import tv.twitch.StandardCoreAPI;
import tv.twitch.ErrorCode;
import org.apache.logging.log4j.LogManager;
import tv.twitch.chat.IChatAPIListener;
import tv.twitch.chat.ChatEmoticonData;
import java.util.HashMap;
import tv.twitch.AuthToken;
import tv.twitch.chat.Chat;
import tv.twitch.Core;
import org.apache.logging.log4j.Logger;

public class ChatController
{
    private static final Logger LOGGER;
    protected ChatListener field_153003_a;
    protected String field_153004_b;
    protected String field_153006_d;
    protected String field_153007_e;
    protected Core field_175992_e;
    protected Chat field_153008_f;
    protected ChatState field_153011_i;
    protected AuthToken field_153012_j;
    protected HashMap<String, ChatChannelListener> field_175998_i;
    protected int field_153015_m;
    protected EnumEmoticonMode field_175997_k;
    protected EnumEmoticonMode field_175995_l;
    protected ChatEmoticonData field_175996_m;
    protected int field_175993_n;
    protected int field_175994_o;
    protected IChatAPIListener field_175999_p;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public void func_152990_a(final ChatListener p_152990_1_) {
        this.field_153003_a = p_152990_1_;
    }
    
    public void func_152994_a(final AuthToken p_152994_1_) {
        this.field_153012_j = p_152994_1_;
    }
    
    public void func_152984_a(final String p_152984_1_) {
        this.field_153006_d = p_152984_1_;
    }
    
    public void func_152998_c(final String p_152998_1_) {
        this.field_153004_b = p_152998_1_;
    }
    
    public ChatState func_153000_j() {
        return this.field_153011_i;
    }
    
    public boolean func_175990_d(final String p_175990_1_) {
        if (!this.field_175998_i.containsKey(p_175990_1_)) {
            return false;
        }
        final ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175990_1_);
        return chatcontroller$chatchannellistener.func_176040_a() == EnumChannelState.Connected;
    }
    
    public EnumChannelState func_175989_e(final String p_175989_1_) {
        if (!this.field_175998_i.containsKey(p_175989_1_)) {
            return EnumChannelState.Disconnected;
        }
        final ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175989_1_);
        return chatcontroller$chatchannellistener.func_176040_a();
    }
    
    public ChatController() {
        this.field_153003_a = null;
        this.field_153004_b = "";
        this.field_153006_d = "";
        this.field_153007_e = "";
        this.field_175992_e = null;
        this.field_153008_f = null;
        this.field_153011_i = ChatState.Uninitialized;
        this.field_153012_j = new AuthToken();
        this.field_175998_i = new HashMap<String, ChatChannelListener>();
        this.field_153015_m = 128;
        this.field_175997_k = EnumEmoticonMode.None;
        this.field_175995_l = EnumEmoticonMode.None;
        this.field_175996_m = null;
        this.field_175993_n = 500;
        this.field_175994_o = 2000;
        this.field_175999_p = new IChatAPIListener() {
            @Override
            public void chatInitializationCallback(final ErrorCode p_chatInitializationCallback_1_) {
                if (ErrorCode.succeeded(p_chatInitializationCallback_1_)) {
                    ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
                    ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
                    ChatController.this.func_153001_r();
                    ChatController.this.func_175985_a(ChatState.Initialized);
                }
                else {
                    ChatController.this.func_175985_a(ChatState.Uninitialized);
                }
                try {
                    if (ChatController.this.field_153003_a != null) {
                        ChatController.this.field_153003_a.func_176023_d(p_chatInitializationCallback_1_);
                    }
                }
                catch (Exception exception) {
                    ChatController.this.func_152995_h(exception.toString());
                }
            }
            
            @Override
            public void chatShutdownCallback(final ErrorCode p_chatShutdownCallback_1_) {
                if (ErrorCode.succeeded(p_chatShutdownCallback_1_)) {
                    final ErrorCode errorcode = ChatController.this.field_175992_e.shutdown();
                    if (ErrorCode.failed(errorcode)) {
                        final String s = ErrorCode.getString(errorcode);
                        ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", s));
                    }
                    ChatController.this.func_175985_a(ChatState.Uninitialized);
                }
                else {
                    ChatController.this.func_175985_a(ChatState.Initialized);
                    ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", p_chatShutdownCallback_1_));
                }
                try {
                    if (ChatController.this.field_153003_a != null) {
                        ChatController.this.field_153003_a.func_176022_e(p_chatShutdownCallback_1_);
                    }
                }
                catch (Exception exception) {
                    ChatController.this.func_152995_h(exception.toString());
                }
            }
            
            @Override
            public void chatEmoticonDataDownloadCallback(final ErrorCode p_chatEmoticonDataDownloadCallback_1_) {
                if (ErrorCode.succeeded(p_chatEmoticonDataDownloadCallback_1_)) {
                    ChatController.this.func_152988_s();
                }
            }
        };
        this.field_175992_e = Core.getInstance();
        if (this.field_175992_e == null) {
            this.field_175992_e = new Core(new StandardCoreAPI());
        }
        this.field_153008_f = new Chat(new StandardChatAPI());
    }
    
    public boolean func_175984_n() {
        if (this.field_153011_i != ChatState.Uninitialized) {
            return false;
        }
        this.func_175985_a(ChatState.Initializing);
        ErrorCode errorcode = this.field_175992_e.initialize(this.field_153006_d, null);
        if (ErrorCode.failed(errorcode)) {
            this.func_175985_a(ChatState.Uninitialized);
            final String s1 = ErrorCode.getString(errorcode);
            this.func_152995_h(String.format("Error initializing Twitch sdk: %s", s1));
            return false;
        }
        this.field_175995_l = this.field_175997_k;
        final HashSet<ChatTokenizationOption> hashset = new HashSet<ChatTokenizationOption>();
        switch (this.field_175997_k) {
            case None: {
                hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
                break;
            }
            case Url: {
                hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
                break;
            }
            case TextureAtlas: {
                hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
                break;
            }
        }
        errorcode = this.field_153008_f.initialize(hashset, this.field_175999_p);
        if (ErrorCode.failed(errorcode)) {
            this.field_175992_e.shutdown();
            this.func_175985_a(ChatState.Uninitialized);
            final String s2 = ErrorCode.getString(errorcode);
            this.func_152995_h(String.format("Error initializing Twitch chat: %s", s2));
            return false;
        }
        this.func_175985_a(ChatState.Initialized);
        return true;
    }
    
    public boolean func_152986_d(final String p_152986_1_) {
        return this.func_175987_a(p_152986_1_, false);
    }
    
    protected boolean func_175987_a(final String p_175987_1_, final boolean p_175987_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (this.field_175998_i.containsKey(p_175987_1_)) {
            this.func_152995_h("Already in channel: " + p_175987_1_);
            return false;
        }
        if (p_175987_1_ != null && !p_175987_1_.equals("")) {
            final ChatChannelListener chatcontroller$chatchannellistener = new ChatChannelListener(p_175987_1_);
            this.field_175998_i.put(p_175987_1_, chatcontroller$chatchannellistener);
            final boolean flag = chatcontroller$chatchannellistener.func_176038_a(p_175987_2_);
            if (!flag) {
                this.field_175998_i.remove(p_175987_1_);
            }
            return flag;
        }
        return false;
    }
    
    public boolean func_175991_l(final String p_175991_1_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175991_1_)) {
            this.func_152995_h("Not in channel: " + p_175991_1_);
            return false;
        }
        final ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175991_1_);
        return chatcontroller$chatchannellistener.func_176034_g();
    }
    
    public boolean func_152993_m() {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        final ErrorCode errorcode = this.field_153008_f.shutdown();
        if (ErrorCode.failed(errorcode)) {
            final String s = ErrorCode.getString(errorcode);
            this.func_152995_h(String.format("Error shutting down chat: %s", s));
            return false;
        }
        this.func_152996_t();
        this.func_175985_a(ChatState.ShuttingDown);
        return true;
    }
    
    public void func_175988_p() {
        if (this.func_153000_j() != ChatState.Uninitialized) {
            this.func_152993_m();
            if (this.func_153000_j() == ChatState.ShuttingDown) {
                while (this.func_153000_j() != ChatState.Uninitialized) {
                    try {
                        Thread.sleep(200L);
                        this.func_152997_n();
                    }
                    catch (InterruptedException ex) {}
                }
            }
        }
    }
    
    public void func_152997_n() {
        if (this.field_153011_i != ChatState.Uninitialized) {
            final ErrorCode errorcode = this.field_153008_f.flushEvents();
            if (ErrorCode.failed(errorcode)) {
                final String s = ErrorCode.getString(errorcode);
                this.func_152995_h(String.format("Error flushing chat events: %s", s));
            }
        }
    }
    
    public boolean func_175986_a(final String p_175986_1_, final String p_175986_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175986_1_)) {
            this.func_152995_h("Not in channel: " + p_175986_1_);
            return false;
        }
        final ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175986_1_);
        return chatcontroller$chatchannellistener.func_176037_b(p_175986_2_);
    }
    
    protected void func_175985_a(final ChatState p_175985_1_) {
        if (p_175985_1_ != this.field_153011_i) {
            this.field_153011_i = p_175985_1_;
            try {
                if (this.field_153003_a != null) {
                    this.field_153003_a.func_176017_a(p_175985_1_);
                }
            }
            catch (Exception exception) {
                this.func_152995_h(exception.toString());
            }
        }
    }
    
    protected void func_153001_r() {
        if (this.field_175995_l != EnumEmoticonMode.None && this.field_175996_m == null) {
            final ErrorCode errorcode = this.field_153008_f.downloadEmoticonData();
            if (ErrorCode.failed(errorcode)) {
                final String s = ErrorCode.getString(errorcode);
                this.func_152995_h(String.format("Error trying to download emoticon data: %s", s));
            }
        }
    }
    
    protected void func_152988_s() {
        if (this.field_175996_m == null) {
            this.field_175996_m = new ChatEmoticonData();
            final ErrorCode errorcode = this.field_153008_f.getEmoticonData(this.field_175996_m);
            if (ErrorCode.succeeded(errorcode)) {
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176021_d();
                    }
                }
                catch (Exception exception) {
                    this.func_152995_h(exception.toString());
                }
            }
            else {
                this.func_152995_h("Error preparing emoticon data: " + ErrorCode.getString(errorcode));
            }
        }
    }
    
    protected void func_152996_t() {
        if (this.field_175996_m != null) {
            final ErrorCode errorcode = this.field_153008_f.clearEmoticonData();
            if (ErrorCode.succeeded(errorcode)) {
                this.field_175996_m = null;
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176024_e();
                    }
                }
                catch (Exception exception) {
                    this.func_152995_h(exception.toString());
                }
            }
            else {
                this.func_152995_h("Error clearing emoticon data: " + ErrorCode.getString(errorcode));
            }
        }
    }
    
    protected void func_152995_h(final String p_152995_1_) {
        ChatController.LOGGER.error(TwitchStream.STREAM_MARKER, "[Chat controller] {}", p_152995_1_);
    }
    
    public enum ChatState
    {
        Uninitialized("Uninitialized", 0), 
        Initializing("Initializing", 1), 
        Initialized("Initialized", 2), 
        ShuttingDown("ShuttingDown", 3);
        
        private ChatState(final String name, final int ordinal) {
        }
    }
    
    public enum EnumChannelState
    {
        Created("Created", 0), 
        Connecting("Connecting", 1), 
        Connected("Connected", 2), 
        Disconnecting("Disconnecting", 3), 
        Disconnected("Disconnected", 4);
        
        private EnumChannelState(final String name, final int ordinal) {
        }
    }
    
    public enum EnumEmoticonMode
    {
        None("None", 0), 
        Url("Url", 1), 
        TextureAtlas("TextureAtlas", 2);
        
        private EnumEmoticonMode(final String name, final int ordinal) {
        }
    }
    
    public class ChatChannelListener implements IChatChannelListener
    {
        protected String field_176048_a;
        protected boolean field_176046_b;
        protected EnumChannelState field_176047_c;
        protected List<ChatUserInfo> field_176044_d;
        protected LinkedList<ChatRawMessage> field_176045_e;
        protected LinkedList<ChatTokenizedMessage> field_176042_f;
        protected ChatBadgeData field_176043_g;
        
        public ChatChannelListener(final String p_i46061_2_) {
            this.field_176048_a = null;
            this.field_176046_b = false;
            this.field_176047_c = EnumChannelState.Created;
            this.field_176044_d = (List<ChatUserInfo>)Lists.newArrayList();
            this.field_176045_e = new LinkedList<ChatRawMessage>();
            this.field_176042_f = new LinkedList<ChatTokenizedMessage>();
            this.field_176043_g = null;
            this.field_176048_a = p_i46061_2_;
        }
        
        public EnumChannelState func_176040_a() {
            return this.field_176047_c;
        }
        
        public boolean func_176038_a(final boolean p_176038_1_) {
            this.field_176046_b = p_176038_1_;
            ErrorCode errorcode = ErrorCode.TTV_EC_SUCCESS;
            if (p_176038_1_) {
                errorcode = ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, this);
            }
            else {
                errorcode = ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, this);
            }
            if (ErrorCode.failed(errorcode)) {
                final String s = ErrorCode.getString(errorcode);
                ChatController.this.func_152995_h(String.format("Error connecting: %s", s));
                this.func_176036_d(this.field_176048_a);
                return false;
            }
            this.func_176035_a(EnumChannelState.Connecting);
            this.func_176041_h();
            return true;
        }
        
        public boolean func_176034_g() {
            switch (this.field_176047_c) {
                case Connecting:
                case Connected: {
                    final ErrorCode errorcode = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
                    if (ErrorCode.failed(errorcode)) {
                        final String s = ErrorCode.getString(errorcode);
                        ChatController.this.func_152995_h(String.format("Error disconnecting: %s", s));
                        return false;
                    }
                    this.func_176035_a(EnumChannelState.Disconnecting);
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
        
        protected void func_176035_a(final EnumChannelState p_176035_1_) {
            if (p_176035_1_ != this.field_176047_c) {
                this.field_176047_c = p_176035_1_;
            }
        }
        
        public void func_176032_a(final String p_176032_1_) {
            if (ChatController.this.field_175995_l == EnumEmoticonMode.None) {
                this.field_176045_e.clear();
                this.field_176042_f.clear();
            }
            else {
                if (this.field_176045_e.size() > 0) {
                    final ListIterator<ChatRawMessage> listiterator = this.field_176045_e.listIterator();
                    while (listiterator.hasNext()) {
                        final ChatRawMessage chatrawmessage = listiterator.next();
                        if (chatrawmessage.userName.equals(p_176032_1_)) {
                            listiterator.remove();
                        }
                    }
                }
                if (this.field_176042_f.size() > 0) {
                    final ListIterator<ChatTokenizedMessage> listiterator2 = this.field_176042_f.listIterator();
                    while (listiterator2.hasNext()) {
                        final ChatTokenizedMessage chattokenizedmessage = listiterator2.next();
                        if (chattokenizedmessage.displayName.equals(p_176032_1_)) {
                            listiterator2.remove();
                        }
                    }
                }
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, p_176032_1_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }
        
        public boolean func_176037_b(final String p_176037_1_) {
            if (this.field_176047_c != EnumChannelState.Connected) {
                return false;
            }
            final ErrorCode errorcode = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, p_176037_1_);
            if (ErrorCode.failed(errorcode)) {
                final String s = ErrorCode.getString(errorcode);
                ChatController.this.func_152995_h(String.format("Error sending chat message: %s", s));
                return false;
            }
            return true;
        }
        
        protected void func_176041_h() {
            if (ChatController.this.field_175995_l != EnumEmoticonMode.None && this.field_176043_g == null) {
                final ErrorCode errorcode = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a);
                if (ErrorCode.failed(errorcode)) {
                    final String s = ErrorCode.getString(errorcode);
                    ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", s));
                }
            }
        }
        
        protected void func_176039_i() {
            if (this.field_176043_g == null) {
                this.field_176043_g = new ChatBadgeData();
                final ErrorCode errorcode = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
                if (ErrorCode.succeeded(errorcode)) {
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176016_c(this.field_176048_a);
                        }
                    }
                    catch (Exception exception) {
                        ChatController.this.func_152995_h(exception.toString());
                    }
                }
                else {
                    ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString(errorcode));
                }
            }
        }
        
        protected void func_176033_j() {
            if (this.field_176043_g != null) {
                final ErrorCode errorcode = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
                if (ErrorCode.succeeded(errorcode)) {
                    this.field_176043_g = null;
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176020_d(this.field_176048_a);
                        }
                    }
                    catch (Exception exception) {
                        ChatController.this.func_152995_h(exception.toString());
                    }
                }
                else {
                    ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString(errorcode));
                }
            }
        }
        
        protected void func_176031_c(final String p_176031_1_) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180606_a(p_176031_1_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }
        
        protected void func_176036_d(final String p_176036_1_) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180607_b(p_176036_1_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }
        
        private void func_176030_k() {
            if (this.field_176047_c != EnumChannelState.Disconnected) {
                this.func_176035_a(EnumChannelState.Disconnected);
                this.func_176036_d(this.field_176048_a);
                this.func_176033_j();
            }
        }
        
        @Override
        public void chatStatusCallback(final String p_chatStatusCallback_1_, final ErrorCode p_chatStatusCallback_2_) {
            if (!ErrorCode.succeeded(p_chatStatusCallback_2_)) {
                ChatController.this.field_175998_i.remove(p_chatStatusCallback_1_);
                this.func_176030_k();
            }
        }
        
        @Override
        public void chatChannelMembershipCallback(final String p_chatChannelMembershipCallback_1_, final ChatEvent p_chatChannelMembershipCallback_2_, final ChatChannelInfo p_chatChannelMembershipCallback_3_) {
            switch (p_chatChannelMembershipCallback_2_) {
                case TTV_CHAT_JOINED_CHANNEL: {
                    this.func_176035_a(EnumChannelState.Connected);
                    this.func_176031_c(p_chatChannelMembershipCallback_1_);
                    break;
                }
                case TTV_CHAT_LEFT_CHANNEL: {
                    this.func_176030_k();
                    break;
                }
            }
        }
        
        @Override
        public void chatChannelUserChangeCallback(final String p_chatChannelUserChangeCallback_1_, final ChatUserInfo[] p_chatChannelUserChangeCallback_2_, final ChatUserInfo[] p_chatChannelUserChangeCallback_3_, final ChatUserInfo[] p_chatChannelUserChangeCallback_4_) {
            for (int i = 0; i < p_chatChannelUserChangeCallback_3_.length; ++i) {
                final int j = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_3_[i]);
                if (j >= 0) {
                    this.field_176044_d.remove(j);
                }
            }
            for (int k = 0; k < p_chatChannelUserChangeCallback_4_.length; ++k) {
                final int i2 = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_4_[k]);
                if (i2 >= 0) {
                    this.field_176044_d.remove(i2);
                }
                this.field_176044_d.add(p_chatChannelUserChangeCallback_4_[k]);
            }
            for (int l = 0; l < p_chatChannelUserChangeCallback_2_.length; ++l) {
                this.field_176044_d.add(p_chatChannelUserChangeCallback_2_[l]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176018_a(this.field_176048_a, p_chatChannelUserChangeCallback_2_, p_chatChannelUserChangeCallback_3_, p_chatChannelUserChangeCallback_4_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }
        
        @Override
        public void chatChannelRawMessageCallback(final String p_chatChannelRawMessageCallback_1_, final ChatRawMessage[] p_chatChannelRawMessageCallback_2_) {
            for (int i = 0; i < p_chatChannelRawMessageCallback_2_.length; ++i) {
                this.field_176045_e.addLast(p_chatChannelRawMessageCallback_2_[i]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180605_a(this.field_176048_a, p_chatChannelRawMessageCallback_2_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
            while (this.field_176045_e.size() > ChatController.this.field_153015_m) {
                this.field_176045_e.removeFirst();
            }
        }
        
        @Override
        public void chatChannelTokenizedMessageCallback(final String p_chatChannelTokenizedMessageCallback_1_, final ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_2_) {
            for (int i = 0; i < p_chatChannelTokenizedMessageCallback_2_.length; ++i) {
                this.field_176042_f.addLast(p_chatChannelTokenizedMessageCallback_2_[i]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176025_a(this.field_176048_a, p_chatChannelTokenizedMessageCallback_2_);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
            while (this.field_176042_f.size() > ChatController.this.field_153015_m) {
                this.field_176042_f.removeFirst();
            }
        }
        
        @Override
        public void chatClearCallback(final String p_chatClearCallback_1_, final String p_chatClearCallback_2_) {
            this.func_176032_a(p_chatClearCallback_2_);
        }
        
        @Override
        public void chatBadgeDataDownloadCallback(final String p_chatBadgeDataDownloadCallback_1_, final ErrorCode p_chatBadgeDataDownloadCallback_2_) {
            if (ErrorCode.succeeded(p_chatBadgeDataDownloadCallback_2_)) {
                this.func_176039_i();
            }
        }
    }
    
    public interface ChatListener
    {
        void func_176023_d(final ErrorCode p0);
        
        void func_176022_e(final ErrorCode p0);
        
        void func_176021_d();
        
        void func_176024_e();
        
        void func_176017_a(final ChatState p0);
        
        void func_176025_a(final String p0, final ChatTokenizedMessage[] p1);
        
        void func_180605_a(final String p0, final ChatRawMessage[] p1);
        
        void func_176018_a(final String p0, final ChatUserInfo[] p1, final ChatUserInfo[] p2, final ChatUserInfo[] p3);
        
        void func_180606_a(final String p0);
        
        void func_180607_b(final String p0);
        
        void func_176019_a(final String p0, final String p1);
        
        void func_176016_c(final String p0);
        
        void func_176020_d(final String p0);
    }
}
