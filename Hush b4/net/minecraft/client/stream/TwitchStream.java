// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.stream;

import tv.twitch.chat.ChatTokenizedMessage;
import java.util.Iterator;
import net.minecraft.event.HoverEvent;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import tv.twitch.chat.ChatUserSubscription;
import tv.twitch.chat.ChatUserMode;
import java.util.Set;
import tv.twitch.chat.ChatRawMessage;
import net.minecraft.util.MathHelper;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.VideoParams;
import net.minecraft.client.settings.GameSettings;
import tv.twitch.broadcast.EncodingCpuUsage;
import net.minecraft.client.renderer.WorldRenderer;
import tv.twitch.broadcast.FrameBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import com.google.gson.JsonObject;
import java.io.IOException;
import tv.twitch.AuthToken;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParser;
import net.minecraft.util.HttpUtil;
import java.net.URL;
import java.net.URLEncoder;
import net.minecraft.client.renderer.OpenGlHelper;
import com.google.common.base.Strings;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Maps;
import net.minecraft.util.ChatComponentText;
import com.mojang.authlib.properties.Property;
import net.minecraft.util.Util;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.shader.Framebuffer;
import tv.twitch.chat.ChatUserInfo;
import java.util.Map;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;

public class TwitchStream implements BroadcastController.BroadcastListener, ChatController.ChatListener, IngestServerTester.IngestTestListener, IStream
{
    private static final Logger LOGGER;
    public static final Marker STREAM_MARKER;
    private final BroadcastController broadcastController;
    private final ChatController chatController;
    private String field_176029_e;
    private final Minecraft mc;
    private final IChatComponent twitchComponent;
    private final Map<String, ChatUserInfo> field_152955_g;
    private Framebuffer framebuffer;
    private boolean field_152957_i;
    private int targetFPS;
    private long field_152959_k;
    private boolean field_152960_l;
    private boolean loggedIn;
    private boolean field_152962_n;
    private boolean field_152963_o;
    private AuthFailureReason authFailureReason;
    private static boolean field_152965_q;
    
    static {
        LOGGER = LogManager.getLogger();
        STREAM_MARKER = MarkerManager.getMarker("STREAM");
        try {
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                System.loadLibrary("avutil-ttv-51");
                System.loadLibrary("swresample-ttv-0");
                System.loadLibrary("libmp3lame-ttv");
                if (System.getProperty("os.arch").contains("64")) {
                    System.loadLibrary("libmfxsw64");
                }
                else {
                    System.loadLibrary("libmfxsw32");
                }
            }
            TwitchStream.field_152965_q = true;
        }
        catch (Throwable var1) {
            TwitchStream.field_152965_q = false;
        }
    }
    
    public TwitchStream(final Minecraft mcIn, final Property streamProperty) {
        this.twitchComponent = new ChatComponentText("Twitch");
        this.field_152955_g = (Map<String, ChatUserInfo>)Maps.newHashMap();
        this.targetFPS = 30;
        this.field_152959_k = 0L;
        this.field_152960_l = false;
        this.authFailureReason = AuthFailureReason.ERROR;
        this.mc = mcIn;
        this.broadcastController = new BroadcastController();
        this.chatController = new ChatController();
        this.broadcastController.func_152841_a(this);
        this.chatController.func_152990_a(this);
        this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.twitchComponent.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
        if (streamProperty != null && !Strings.isNullOrEmpty(streamProperty.getValue()) && OpenGlHelper.framebufferSupported) {
            final Thread thread = new Thread("Twitch authenticator") {
                @Override
                public void run() {
                    try {
                        final URL url = new URL("https://api.twitch.tv/kraken?oauth_token=" + URLEncoder.encode(streamProperty.getValue(), "UTF-8"));
                        final String s = HttpUtil.get(url);
                        final JsonObject jsonobject = JsonUtils.getJsonObject(new JsonParser().parse(s), "Response");
                        final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "token");
                        if (JsonUtils.getBoolean(jsonobject2, "valid")) {
                            final String s2 = JsonUtils.getString(jsonobject2, "user_name");
                            TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Authenticated with twitch; username is {}", s2);
                            final AuthToken authtoken = new AuthToken();
                            authtoken.data = streamProperty.getValue();
                            TwitchStream.this.broadcastController.func_152818_a(s2, authtoken);
                            TwitchStream.this.chatController.func_152998_c(s2);
                            TwitchStream.this.chatController.func_152994_a(authtoken);
                            Runtime.getRuntime().addShutdownHook(new Thread("Twitch shutdown hook") {
                                @Override
                                public void run() {
                                    TwitchStream.this.shutdownStream();
                                }
                            });
                            TwitchStream.this.broadcastController.func_152817_A();
                            TwitchStream.this.chatController.func_175984_n();
                        }
                        else {
                            TwitchStream.access$3(TwitchStream.this, AuthFailureReason.INVALID_TOKEN);
                            TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Given twitch access token is invalid");
                        }
                    }
                    catch (IOException ioexception) {
                        TwitchStream.access$3(TwitchStream.this, AuthFailureReason.ERROR);
                        TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Could not authenticate with twitch", ioexception);
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
    }
    
    @Override
    public void shutdownStream() {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Shutdown streaming");
        this.broadcastController.statCallback();
        this.chatController.func_175988_p();
    }
    
    @Override
    public void func_152935_j() {
        final int i = this.mc.gameSettings.streamChatEnabled;
        final boolean flag = this.field_176029_e != null && this.chatController.func_175990_d(this.field_176029_e);
        final boolean flag2 = this.chatController.func_153000_j() == ChatController.ChatState.Initialized && (this.field_176029_e == null || this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected);
        if (i == 2) {
            if (flag) {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Disconnecting from twitch chat per user options");
                this.chatController.func_175991_l(this.field_176029_e);
            }
        }
        else if (i == 1) {
            if (flag2 && this.broadcastController.func_152849_q()) {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Connecting to twitch chat per user options");
                this.func_152942_I();
            }
        }
        else if (i == 0) {
            if (flag && !this.isBroadcasting()) {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Disconnecting from twitch chat as user is no longer streaming");
                this.chatController.func_175991_l(this.field_176029_e);
            }
            else if (flag2 && this.isBroadcasting()) {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Connecting to twitch chat as user is streaming");
                this.func_152942_I();
            }
        }
        this.broadcastController.func_152821_H();
        this.chatController.func_152997_n();
    }
    
    protected void func_152942_I() {
        final ChatController.ChatState chatcontroller$chatstate = this.chatController.func_153000_j();
        final String s = this.broadcastController.getChannelInfo().name;
        this.field_176029_e = s;
        if (chatcontroller$chatstate != ChatController.ChatState.Initialized) {
            TwitchStream.LOGGER.warn("Invalid twitch chat state {}", chatcontroller$chatstate);
        }
        else if (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected) {
            this.chatController.func_152986_d(s);
        }
        else {
            TwitchStream.LOGGER.warn("Invalid twitch chat state {}", chatcontroller$chatstate);
        }
    }
    
    @Override
    public void func_152922_k() {
        if (this.broadcastController.isBroadcasting() && !this.broadcastController.isBroadcastPaused()) {
            final long i = System.nanoTime();
            final long j = 1000000000 / this.targetFPS;
            final long k = i - this.field_152959_k;
            final boolean flag = k >= j;
            if (flag) {
                final FrameBuffer framebuffer = this.broadcastController.func_152822_N();
                final Framebuffer framebuffer2 = this.mc.getFramebuffer();
                this.framebuffer.bindFramebuffer(true);
                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight, 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.viewport(0, 0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight);
                GlStateManager.enableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                final float f = (float)this.framebuffer.framebufferWidth;
                final float f2 = (float)this.framebuffer.framebufferHeight;
                final float f3 = framebuffer2.framebufferWidth / (float)framebuffer2.framebufferTextureWidth;
                final float f4 = framebuffer2.framebufferHeight / (float)framebuffer2.framebufferTextureHeight;
                framebuffer2.bindFramebufferTexture();
                GL11.glTexParameterf(3553, 10241, 9729.0f);
                GL11.glTexParameterf(3553, 10240, 9729.0f);
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(0.0, f2, 0.0).tex(0.0, f4).endVertex();
                worldrenderer.pos(f, f2, 0.0).tex(f3, f4).endVertex();
                worldrenderer.pos(f, 0.0, 0.0).tex(f3, 0.0).endVertex();
                worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex();
                tessellator.draw();
                framebuffer2.unbindFramebufferTexture();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.broadcastController.captureFramebuffer(framebuffer);
                this.framebuffer.unbindFramebuffer();
                this.broadcastController.submitStreamFrame(framebuffer);
                this.field_152959_k = i;
            }
        }
    }
    
    @Override
    public boolean func_152936_l() {
        return this.broadcastController.func_152849_q();
    }
    
    @Override
    public boolean isReadyToBroadcast() {
        return this.broadcastController.isReadyToBroadcast();
    }
    
    @Override
    public boolean isBroadcasting() {
        return this.broadcastController.isBroadcasting();
    }
    
    @Override
    public void func_152911_a(final Metadata p_152911_1_, final long p_152911_2_) {
        if (this.isBroadcasting() && this.field_152957_i) {
            final long i = this.broadcastController.func_152844_x();
            if (!this.broadcastController.func_152840_a(p_152911_1_.func_152810_c(), i + p_152911_2_, p_152911_1_.func_152809_a(), p_152911_1_.func_152806_b())) {
                TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Couldn't send stream metadata action at {}: {}", i + p_152911_2_, p_152911_1_);
            }
            else {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Sent stream metadata action at {}: {}", i + p_152911_2_, p_152911_1_);
            }
        }
    }
    
    @Override
    public void func_176026_a(final Metadata p_176026_1_, final long p_176026_2_, final long p_176026_4_) {
        if (this.isBroadcasting() && this.field_152957_i) {
            final long i = this.broadcastController.func_152844_x();
            final String s = p_176026_1_.func_152809_a();
            final String s2 = p_176026_1_.func_152806_b();
            final long j = this.broadcastController.func_177946_b(p_176026_1_.func_152810_c(), i + p_176026_2_, s, s2);
            if (j < 0L) {
                TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Could not send stream metadata sequence from {} to {}: {}", i + p_176026_2_, i + p_176026_4_, p_176026_1_);
            }
            else if (this.broadcastController.func_177947_a(p_176026_1_.func_152810_c(), i + p_176026_4_, j, s, s2)) {
                TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Sent stream metadata sequence from {} to {}: {}", i + p_176026_2_, i + p_176026_4_, p_176026_1_);
            }
            else {
                TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Half-sent stream metadata sequence from {} to {}: {}", i + p_176026_2_, i + p_176026_4_, p_176026_1_);
            }
        }
    }
    
    @Override
    public boolean isPaused() {
        return this.broadcastController.isBroadcastPaused();
    }
    
    @Override
    public void requestCommercial() {
        if (this.broadcastController.requestCommercial()) {
            TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Requested commercial from Twitch");
        }
        else {
            TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Could not request commercial from Twitch");
        }
    }
    
    @Override
    public void pause() {
        this.broadcastController.func_152847_F();
        this.field_152962_n = true;
        this.updateStreamVolume();
    }
    
    @Override
    public void unpause() {
        this.broadcastController.func_152854_G();
        this.field_152962_n = false;
        this.updateStreamVolume();
    }
    
    @Override
    public void updateStreamVolume() {
        if (this.isBroadcasting()) {
            final float f = this.mc.gameSettings.streamGameVolume;
            final boolean flag = this.field_152962_n || f <= 0.0f;
            this.broadcastController.setPlaybackDeviceVolume(flag ? 0.0f : f);
            this.broadcastController.setRecordingDeviceVolume(this.func_152929_G() ? 0.0f : this.mc.gameSettings.streamMicVolume);
        }
    }
    
    @Override
    public void func_152930_t() {
        final GameSettings gamesettings = this.mc.gameSettings;
        final VideoParams videoparams = this.broadcastController.func_152834_a(formatStreamKbps(gamesettings.streamKbps), formatStreamFps(gamesettings.streamFps), formatStreamBps(gamesettings.streamBytesPerPixel), Minecraft.displayWidth / (float)Minecraft.displayHeight);
        switch (gamesettings.streamCompression) {
            case 0: {
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
                break;
            }
            case 1: {
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
                break;
            }
            case 2: {
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
                break;
            }
        }
        if (this.framebuffer == null) {
            this.framebuffer = new Framebuffer(videoparams.outputWidth, videoparams.outputHeight, false);
        }
        else {
            this.framebuffer.createBindFramebuffer(videoparams.outputWidth, videoparams.outputHeight);
        }
        if (gamesettings.streamPreferredServer != null && gamesettings.streamPreferredServer.length() > 0) {
            IngestServer[] func_152925_v;
            for (int length = (func_152925_v = this.func_152925_v()).length, i = 0; i < length; ++i) {
                final IngestServer ingestserver = func_152925_v[i];
                if (ingestserver.serverUrl.equals(gamesettings.streamPreferredServer)) {
                    this.broadcastController.func_152824_a(ingestserver);
                    break;
                }
            }
        }
        this.targetFPS = videoparams.targetFps;
        this.field_152957_i = gamesettings.streamSendMetadata;
        this.broadcastController.func_152836_a(videoparams);
        TwitchStream.LOGGER.info(TwitchStream.STREAM_MARKER, "Streaming at {}/{} at {} kbps to {}", videoparams.outputWidth, videoparams.outputHeight, videoparams.maxKbps, this.broadcastController.func_152833_s().serverUrl);
        this.broadcastController.func_152828_a(null, "Minecraft", null);
    }
    
    @Override
    public void stopBroadcasting() {
        if (this.broadcastController.stopBroadcasting()) {
            TwitchStream.LOGGER.info(TwitchStream.STREAM_MARKER, "Stopped streaming to Twitch");
        }
        else {
            TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Could not stop streaming to Twitch");
        }
    }
    
    @Override
    public void func_152900_a(final ErrorCode p_152900_1_, final AuthToken p_152900_2_) {
    }
    
    @Override
    public void func_152897_a(final ErrorCode p_152897_1_) {
        if (ErrorCode.succeeded(p_152897_1_)) {
            TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Login attempt successful");
            this.loggedIn = true;
        }
        else {
            TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Login attempt unsuccessful: {} (error code {})", ErrorCode.getString(p_152897_1_), p_152897_1_.getValue());
            this.loggedIn = false;
        }
    }
    
    @Override
    public void func_152898_a(final ErrorCode p_152898_1_, final GameInfo[] p_152898_2_) {
    }
    
    @Override
    public void func_152891_a(final BroadcastController.BroadcastState p_152891_1_) {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Broadcast state changed to {}", p_152891_1_);
        if (p_152891_1_ == BroadcastController.BroadcastState.Initialized) {
            this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
        }
    }
    
    @Override
    public void func_152895_a() {
        TwitchStream.LOGGER.info(TwitchStream.STREAM_MARKER, "Logged out of twitch");
    }
    
    @Override
    public void func_152894_a(final StreamInfo p_152894_1_) {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Stream info updated; {} viewers on stream ID {}", p_152894_1_.viewers, p_152894_1_.streamId);
    }
    
    @Override
    public void func_152896_a(final IngestList p_152896_1_) {
    }
    
    @Override
    public void func_152893_b(final ErrorCode p_152893_1_) {
        TwitchStream.LOGGER.warn(TwitchStream.STREAM_MARKER, "Issue submitting frame: {} (Error code {})", ErrorCode.getString(p_152893_1_), p_152893_1_.getValue());
        this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + p_152893_1_ + " (" + ErrorCode.getString(p_152893_1_) + ")"), 2);
    }
    
    @Override
    public void func_152899_b() {
        this.updateStreamVolume();
        TwitchStream.LOGGER.info(TwitchStream.STREAM_MARKER, "Broadcast to Twitch has started");
    }
    
    @Override
    public void func_152901_c() {
        TwitchStream.LOGGER.info(TwitchStream.STREAM_MARKER, "Broadcast to Twitch has stopped");
    }
    
    @Override
    public void func_152892_c(final ErrorCode p_152892_1_) {
        if (p_152892_1_ == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED) {
            final IChatComponent ichatcomponent = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
            ichatcomponent.getChatStyle().setUnderlined(true);
            final IChatComponent ichatcomponent2 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", new Object[] { ichatcomponent });
            ichatcomponent2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent2);
        }
        else {
            final IChatComponent ichatcomponent3 = new ChatComponentTranslation("stream.unavailable.unknown.chat", new Object[] { ErrorCode.getString(p_152892_1_) });
            ichatcomponent3.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent3);
        }
    }
    
    @Override
    public void func_152907_a(final IngestServerTester p_152907_1_, final IngestServerTester.IngestTestState p_152907_2_) {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Ingest test state changed to {}", p_152907_2_);
        if (p_152907_2_ == IngestServerTester.IngestTestState.Finished) {
            this.field_152960_l = true;
        }
    }
    
    public static int formatStreamFps(final float p_152948_0_) {
        return MathHelper.floor_float(10.0f + p_152948_0_ * 50.0f);
    }
    
    public static int formatStreamKbps(final float p_152946_0_) {
        return MathHelper.floor_float(230.0f + p_152946_0_ * 3270.0f);
    }
    
    public static float formatStreamBps(final float p_152947_0_) {
        return 0.1f + p_152947_0_ * 0.1f;
    }
    
    @Override
    public IngestServer[] func_152925_v() {
        return this.broadcastController.func_152855_t().getServers();
    }
    
    @Override
    public void func_152909_x() {
        final IngestServerTester ingestservertester = this.broadcastController.func_152838_J();
        if (ingestservertester != null) {
            ingestservertester.func_153042_a(this);
        }
    }
    
    @Override
    public IngestServerTester func_152932_y() {
        return this.broadcastController.isReady();
    }
    
    @Override
    public boolean func_152908_z() {
        return this.broadcastController.isIngestTesting();
    }
    
    @Override
    public int func_152920_A() {
        return this.isBroadcasting() ? this.broadcastController.getStreamInfo().viewers : 0;
    }
    
    @Override
    public void func_176023_d(final ErrorCode p_176023_1_) {
        if (ErrorCode.failed(p_176023_1_)) {
            TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Chat failed to initialize");
        }
    }
    
    @Override
    public void func_176022_e(final ErrorCode p_176022_1_) {
        if (ErrorCode.failed(p_176022_1_)) {
            TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Chat failed to shutdown");
        }
    }
    
    @Override
    public void func_176017_a(final ChatController.ChatState p_176017_1_) {
    }
    
    @Override
    public void func_180605_a(final String p_180605_1_, final ChatRawMessage[] p_180605_2_) {
        for (final ChatRawMessage chatrawmessage : p_180605_2_) {
            this.func_176027_a(chatrawmessage.userName, chatrawmessage);
            if (this.func_176028_a(chatrawmessage.modes, chatrawmessage.subscriptions, this.mc.gameSettings.streamChatUserFilter)) {
                final IChatComponent ichatcomponent = new ChatComponentText(chatrawmessage.userName);
                final IChatComponent ichatcomponent2 = new ChatComponentTranslation("chat.stream." + (chatrawmessage.action ? "emote" : "text"), new Object[] { this.twitchComponent, ichatcomponent, EnumChatFormatting.getTextWithoutFormattingCodes(chatrawmessage.message) });
                if (chatrawmessage.action) {
                    ichatcomponent2.getChatStyle().setItalic(true);
                }
                final IChatComponent ichatcomponent3 = new ChatComponentText("");
                ichatcomponent3.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
                for (final IChatComponent ichatcomponent4 : GuiTwitchUserMode.func_152328_a(chatrawmessage.modes, chatrawmessage.subscriptions, null)) {
                    ichatcomponent3.appendText("\n");
                    ichatcomponent3.appendSibling(ichatcomponent4);
                }
                ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ichatcomponent3));
                ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, chatrawmessage.userName));
                this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent2);
            }
        }
    }
    
    @Override
    public void func_176025_a(final String p_176025_1_, final ChatTokenizedMessage[] p_176025_2_) {
    }
    
    private void func_176027_a(final String p_176027_1_, final ChatRawMessage p_176027_2_) {
        ChatUserInfo chatuserinfo = this.field_152955_g.get(p_176027_1_);
        if (chatuserinfo == null) {
            chatuserinfo = new ChatUserInfo();
            chatuserinfo.displayName = p_176027_1_;
            this.field_152955_g.put(p_176027_1_, chatuserinfo);
        }
        chatuserinfo.subscriptions = p_176027_2_.subscriptions;
        chatuserinfo.modes = p_176027_2_.modes;
        chatuserinfo.nameColorARGB = p_176027_2_.nameColorARGB;
    }
    
    private boolean func_176028_a(final Set<ChatUserMode> p_176028_1_, final Set<ChatUserSubscription> p_176028_2_, final int p_176028_3_) {
        return !p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED) && (p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) || p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) || p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) || p_176028_3_ == 0 || (p_176028_3_ == 1 && p_176028_2_.contains(ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER)));
    }
    
    @Override
    public void func_176018_a(final String p_176018_1_, final ChatUserInfo[] p_176018_2_, final ChatUserInfo[] p_176018_3_, final ChatUserInfo[] p_176018_4_) {
        for (final ChatUserInfo chatuserinfo : p_176018_3_) {
            this.field_152955_g.remove(chatuserinfo.displayName);
        }
        for (final ChatUserInfo chatuserinfo2 : p_176018_4_) {
            this.field_152955_g.put(chatuserinfo2.displayName, chatuserinfo2);
        }
        for (final ChatUserInfo chatuserinfo3 : p_176018_2_) {
            this.field_152955_g.put(chatuserinfo3.displayName, chatuserinfo3);
        }
    }
    
    @Override
    public void func_180606_a(final String p_180606_1_) {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Chat connected");
    }
    
    @Override
    public void func_180607_b(final String p_180607_1_) {
        TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Chat disconnected");
        this.field_152955_g.clear();
    }
    
    @Override
    public void func_176019_a(final String p_176019_1_, final String p_176019_2_) {
    }
    
    @Override
    public void func_176021_d() {
    }
    
    @Override
    public void func_176024_e() {
    }
    
    @Override
    public void func_176016_c(final String p_176016_1_) {
    }
    
    @Override
    public void func_176020_d(final String p_176020_1_) {
    }
    
    @Override
    public boolean func_152927_B() {
        return this.field_176029_e != null && this.field_176029_e.equals(this.broadcastController.getChannelInfo().name);
    }
    
    @Override
    public String func_152921_C() {
        return this.field_176029_e;
    }
    
    @Override
    public ChatUserInfo func_152926_a(final String p_152926_1_) {
        return this.field_152955_g.get(p_152926_1_);
    }
    
    @Override
    public void func_152917_b(final String p_152917_1_) {
        this.chatController.func_175986_a(this.field_176029_e, p_152917_1_);
    }
    
    @Override
    public boolean func_152928_D() {
        return TwitchStream.field_152965_q && this.broadcastController.func_152858_b();
    }
    
    @Override
    public ErrorCode func_152912_E() {
        return TwitchStream.field_152965_q ? this.broadcastController.getErrorCode() : ErrorCode.TTV_EC_OS_TOO_OLD;
    }
    
    @Override
    public boolean func_152913_F() {
        return this.loggedIn;
    }
    
    @Override
    public void muteMicrophone(final boolean p_152910_1_) {
        this.field_152963_o = p_152910_1_;
        this.updateStreamVolume();
    }
    
    @Override
    public boolean func_152929_G() {
        final boolean flag = this.mc.gameSettings.streamMicToggleBehavior == 1;
        return this.field_152962_n || this.mc.gameSettings.streamMicVolume <= 0.0f || flag != this.field_152963_o;
    }
    
    @Override
    public AuthFailureReason func_152918_H() {
        return this.authFailureReason;
    }
    
    static /* synthetic */ void access$3(final TwitchStream twitchStream, final AuthFailureReason authFailureReason) {
        twitchStream.authFailureReason = authFailureReason;
    }
}
