/*     */ package net.minecraft.client.stream;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiIngame;
/*     */ import net.minecraft.client.gui.GuiNewChat;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Util.EnumOS;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import tv.twitch.AuthToken;
/*     */ import tv.twitch.ErrorCode;
/*     */ import tv.twitch.broadcast.ChannelInfo;
/*     */ import tv.twitch.broadcast.EncodingCpuUsage;
/*     */ import tv.twitch.broadcast.GameInfo;
/*     */ import tv.twitch.broadcast.IngestList;
/*     */ import tv.twitch.broadcast.IngestServer;
/*     */ import tv.twitch.broadcast.StreamInfo;
/*     */ import tv.twitch.broadcast.VideoParams;
/*     */ import tv.twitch.chat.ChatRawMessage;
/*     */ import tv.twitch.chat.ChatTokenizedMessage;
/*     */ import tv.twitch.chat.ChatUserInfo;
/*     */ import tv.twitch.chat.ChatUserMode;
/*     */ import tv.twitch.chat.ChatUserSubscription;
/*     */ 
/*     */ public class TwitchStream implements BroadcastController.BroadcastListener, ChatController.ChatListener, IngestServerTester.IngestTestListener, IStream
/*     */ {
/*  54 */   private static final Logger LOGGER = ;
/*  55 */   public static final Marker STREAM_MARKER = org.apache.logging.log4j.MarkerManager.getMarker("STREAM");
/*     */   
/*     */   private final BroadcastController broadcastController;
/*     */   
/*     */   private final ChatController chatController;
/*     */   private String field_176029_e;
/*     */   private final Minecraft mc;
/*  62 */   private final IChatComponent twitchComponent = new ChatComponentText("Twitch");
/*  63 */   private final Map<String, ChatUserInfo> field_152955_g = Maps.newHashMap();
/*     */   
/*     */   private Framebuffer framebuffer;
/*     */   
/*     */   private boolean field_152957_i;
/*  68 */   private int targetFPS = 30;
/*  69 */   private long field_152959_k = 0L;
/*  70 */   private boolean field_152960_l = false;
/*     */   private boolean loggedIn;
/*     */   private boolean field_152962_n;
/*     */   private boolean field_152963_o;
/*  74 */   private IStream.AuthFailureReason authFailureReason = IStream.AuthFailureReason.ERROR;
/*     */   private static boolean field_152965_q;
/*     */   
/*     */   public TwitchStream(Minecraft mcIn, final Property streamProperty)
/*     */   {
/*  79 */     this.mc = mcIn;
/*  80 */     this.broadcastController = new BroadcastController();
/*  81 */     this.chatController = new ChatController();
/*  82 */     this.broadcastController.func_152841_a(this);
/*  83 */     this.chatController.func_152990_a(this);
/*  84 */     this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
/*  85 */     this.chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
/*  86 */     this.twitchComponent.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
/*     */     
/*  88 */     if ((streamProperty != null) && (!Strings.isNullOrEmpty(streamProperty.getValue())) && (OpenGlHelper.framebufferSupported))
/*     */     {
/*  90 */       Thread thread = new Thread("Twitch authenticator")
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/*  96 */             URL url = new URL("https://api.twitch.tv/kraken?oauth_token=" + java.net.URLEncoder.encode(streamProperty.getValue(), "UTF-8"));
/*  97 */             String s = HttpUtil.get(url);
/*  98 */             JsonObject jsonobject = JsonUtils.getJsonObject(new JsonParser().parse(s), "Response");
/*  99 */             JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "token");
/*     */             
/* 101 */             if (JsonUtils.getBoolean(jsonobject1, "valid"))
/*     */             {
/* 103 */               String s1 = JsonUtils.getString(jsonobject1, "user_name");
/* 104 */               TwitchStream.LOGGER.debug(TwitchStream.STREAM_MARKER, "Authenticated with twitch; username is {}", new Object[] { s1 });
/* 105 */               AuthToken authtoken = new AuthToken();
/* 106 */               authtoken.data = streamProperty.getValue();
/* 107 */               TwitchStream.this.broadcastController.func_152818_a(s1, authtoken);
/* 108 */               TwitchStream.this.chatController.func_152998_c(s1);
/* 109 */               TwitchStream.this.chatController.func_152994_a(authtoken);
/* 110 */               Runtime.getRuntime().addShutdownHook(new Thread("Twitch shutdown hook")
/*     */               {
/*     */                 public void run()
/*     */                 {
/* 114 */                   TwitchStream.this.shutdownStream();
/*     */                 }
/* 116 */               });
/* 117 */               TwitchStream.this.broadcastController.func_152817_A();
/* 118 */               TwitchStream.this.chatController.func_175984_n();
/*     */             }
/*     */             else
/*     */             {
/* 122 */               TwitchStream.this.authFailureReason = IStream.AuthFailureReason.INVALID_TOKEN;
/* 123 */               TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Given twitch access token is invalid");
/*     */             }
/*     */           }
/*     */           catch (IOException ioexception)
/*     */           {
/* 128 */             TwitchStream.this.authFailureReason = IStream.AuthFailureReason.ERROR;
/* 129 */             TwitchStream.LOGGER.error(TwitchStream.STREAM_MARKER, "Could not authenticate with twitch", ioexception);
/*     */           }
/*     */         }
/* 132 */       };
/* 133 */       thread.setDaemon(true);
/* 134 */       thread.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void shutdownStream()
/*     */   {
/* 143 */     LOGGER.debug(STREAM_MARKER, "Shutdown streaming");
/* 144 */     this.broadcastController.statCallback();
/* 145 */     this.chatController.func_175988_p();
/*     */   }
/*     */   
/*     */   public void func_152935_j()
/*     */   {
/* 150 */     int i = this.mc.gameSettings.streamChatEnabled;
/* 151 */     boolean flag = (this.field_176029_e != null) && (this.chatController.func_175990_d(this.field_176029_e));
/* 152 */     boolean flag1 = (this.chatController.func_153000_j() == ChatController.ChatState.Initialized) && ((this.field_176029_e == null) || (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected));
/*     */     
/* 154 */     if (i == 2)
/*     */     {
/* 156 */       if (flag)
/*     */       {
/* 158 */         LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat per user options");
/* 159 */         this.chatController.func_175991_l(this.field_176029_e);
/*     */       }
/*     */     }
/* 162 */     else if (i == 1)
/*     */     {
/* 164 */       if ((flag1) && (this.broadcastController.func_152849_q()))
/*     */       {
/* 166 */         LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat per user options");
/* 167 */         func_152942_I();
/*     */       }
/*     */     }
/* 170 */     else if (i == 0)
/*     */     {
/* 172 */       if ((flag) && (!isBroadcasting()))
/*     */       {
/* 174 */         LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat as user is no longer streaming");
/* 175 */         this.chatController.func_175991_l(this.field_176029_e);
/*     */       }
/* 177 */       else if ((flag1) && (isBroadcasting()))
/*     */       {
/* 179 */         LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat as user is streaming");
/* 180 */         func_152942_I();
/*     */       }
/*     */     }
/*     */     
/* 184 */     this.broadcastController.func_152821_H();
/* 185 */     this.chatController.func_152997_n();
/*     */   }
/*     */   
/*     */   protected void func_152942_I()
/*     */   {
/* 190 */     ChatController.ChatState chatcontroller$chatstate = this.chatController.func_153000_j();
/* 191 */     String s = this.broadcastController.getChannelInfo().name;
/* 192 */     this.field_176029_e = s;
/*     */     
/* 194 */     if (chatcontroller$chatstate != ChatController.ChatState.Initialized)
/*     */     {
/* 196 */       LOGGER.warn("Invalid twitch chat state {}", new Object[] { chatcontroller$chatstate });
/*     */     }
/* 198 */     else if (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected)
/*     */     {
/* 200 */       this.chatController.func_152986_d(s);
/*     */     }
/*     */     else
/*     */     {
/* 204 */       LOGGER.warn("Invalid twitch chat state {}", new Object[] { chatcontroller$chatstate });
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_152922_k()
/*     */   {
/* 210 */     if ((this.broadcastController.isBroadcasting()) && (!this.broadcastController.isBroadcastPaused()))
/*     */     {
/* 212 */       long i = System.nanoTime();
/* 213 */       long j = 1000000000 / this.targetFPS;
/* 214 */       long k = i - this.field_152959_k;
/* 215 */       boolean flag = k >= j;
/*     */       
/* 217 */       if (flag)
/*     */       {
/* 219 */         tv.twitch.broadcast.FrameBuffer framebuffer = this.broadcastController.func_152822_N();
/* 220 */         Framebuffer framebuffer1 = this.mc.getFramebuffer();
/* 221 */         this.framebuffer.bindFramebuffer(true);
/* 222 */         GlStateManager.matrixMode(5889);
/* 223 */         GlStateManager.pushMatrix();
/* 224 */         GlStateManager.loadIdentity();
/* 225 */         GlStateManager.ortho(0.0D, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight, 0.0D, 1000.0D, 3000.0D);
/* 226 */         GlStateManager.matrixMode(5888);
/* 227 */         GlStateManager.pushMatrix();
/* 228 */         GlStateManager.loadIdentity();
/* 229 */         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 230 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 231 */         GlStateManager.viewport(0, 0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight);
/* 232 */         GlStateManager.enableTexture2D();
/* 233 */         GlStateManager.disableAlpha();
/* 234 */         GlStateManager.disableBlend();
/* 235 */         float f = this.framebuffer.framebufferWidth;
/* 236 */         float f1 = this.framebuffer.framebufferHeight;
/* 237 */         float f2 = framebuffer1.framebufferWidth / framebuffer1.framebufferTextureWidth;
/* 238 */         float f3 = framebuffer1.framebufferHeight / framebuffer1.framebufferTextureHeight;
/* 239 */         framebuffer1.bindFramebufferTexture();
/* 240 */         GL11.glTexParameterf(3553, 10241, 9729.0F);
/* 241 */         GL11.glTexParameterf(3553, 10240, 9729.0F);
/* 242 */         Tessellator tessellator = Tessellator.getInstance();
/* 243 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 244 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 245 */         worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, f3).endVertex();
/* 246 */         worldrenderer.pos(f, f1, 0.0D).tex(f2, f3).endVertex();
/* 247 */         worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, 0.0D).endVertex();
/* 248 */         worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
/* 249 */         tessellator.draw();
/* 250 */         framebuffer1.unbindFramebufferTexture();
/* 251 */         GlStateManager.popMatrix();
/* 252 */         GlStateManager.matrixMode(5889);
/* 253 */         GlStateManager.popMatrix();
/* 254 */         GlStateManager.matrixMode(5888);
/* 255 */         this.broadcastController.captureFramebuffer(framebuffer);
/* 256 */         this.framebuffer.unbindFramebuffer();
/* 257 */         this.broadcastController.submitStreamFrame(framebuffer);
/* 258 */         this.field_152959_k = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_152936_l()
/*     */   {
/* 265 */     return this.broadcastController.func_152849_q();
/*     */   }
/*     */   
/*     */   public boolean isReadyToBroadcast()
/*     */   {
/* 270 */     return this.broadcastController.isReadyToBroadcast();
/*     */   }
/*     */   
/*     */   public boolean isBroadcasting()
/*     */   {
/* 275 */     return this.broadcastController.isBroadcasting();
/*     */   }
/*     */   
/*     */   public void func_152911_a(Metadata p_152911_1_, long p_152911_2_)
/*     */   {
/* 280 */     if ((isBroadcasting()) && (this.field_152957_i))
/*     */     {
/* 282 */       long i = this.broadcastController.func_152844_x();
/*     */       
/* 284 */       if (!this.broadcastController.func_152840_a(p_152911_1_.func_152810_c(), i + p_152911_2_, p_152911_1_.func_152809_a(), p_152911_1_.func_152806_b()))
/*     */       {
/* 286 */         LOGGER.warn(STREAM_MARKER, "Couldn't send stream metadata action at {}: {}", new Object[] { Long.valueOf(i + p_152911_2_), p_152911_1_ });
/*     */       }
/*     */       else
/*     */       {
/* 290 */         LOGGER.debug(STREAM_MARKER, "Sent stream metadata action at {}: {}", new Object[] { Long.valueOf(i + p_152911_2_), p_152911_1_ });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_176026_a(Metadata p_176026_1_, long p_176026_2_, long p_176026_4_)
/*     */   {
/* 297 */     if ((isBroadcasting()) && (this.field_152957_i))
/*     */     {
/* 299 */       long i = this.broadcastController.func_152844_x();
/* 300 */       String s = p_176026_1_.func_152809_a();
/* 301 */       String s1 = p_176026_1_.func_152806_b();
/* 302 */       long j = this.broadcastController.func_177946_b(p_176026_1_.func_152810_c(), i + p_176026_2_, s, s1);
/*     */       
/* 304 */       if (j < 0L)
/*     */       {
/* 306 */         LOGGER.warn(STREAM_MARKER, "Could not send stream metadata sequence from {} to {}: {}", new Object[] { Long.valueOf(i + p_176026_2_), Long.valueOf(i + p_176026_4_), p_176026_1_ });
/*     */       }
/* 308 */       else if (this.broadcastController.func_177947_a(p_176026_1_.func_152810_c(), i + p_176026_4_, j, s, s1))
/*     */       {
/* 310 */         LOGGER.debug(STREAM_MARKER, "Sent stream metadata sequence from {} to {}: {}", new Object[] { Long.valueOf(i + p_176026_2_), Long.valueOf(i + p_176026_4_), p_176026_1_ });
/*     */       }
/*     */       else
/*     */       {
/* 314 */         LOGGER.warn(STREAM_MARKER, "Half-sent stream metadata sequence from {} to {}: {}", new Object[] { Long.valueOf(i + p_176026_2_), Long.valueOf(i + p_176026_4_), p_176026_1_ });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isPaused()
/*     */   {
/* 321 */     return this.broadcastController.isBroadcastPaused();
/*     */   }
/*     */   
/*     */   public void requestCommercial()
/*     */   {
/* 326 */     if (this.broadcastController.requestCommercial())
/*     */     {
/* 328 */       LOGGER.debug(STREAM_MARKER, "Requested commercial from Twitch");
/*     */     }
/*     */     else
/*     */     {
/* 332 */       LOGGER.warn(STREAM_MARKER, "Could not request commercial from Twitch");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pause()
/*     */   {
/* 341 */     this.broadcastController.func_152847_F();
/* 342 */     this.field_152962_n = true;
/* 343 */     updateStreamVolume();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unpause()
/*     */   {
/* 351 */     this.broadcastController.func_152854_G();
/* 352 */     this.field_152962_n = false;
/* 353 */     updateStreamVolume();
/*     */   }
/*     */   
/*     */   public void updateStreamVolume()
/*     */   {
/* 358 */     if (isBroadcasting())
/*     */     {
/* 360 */       float f = this.mc.gameSettings.streamGameVolume;
/* 361 */       boolean flag = (this.field_152962_n) || (f <= 0.0F);
/* 362 */       this.broadcastController.setPlaybackDeviceVolume(flag ? 0.0F : f);
/* 363 */       this.broadcastController.setRecordingDeviceVolume(func_152929_G() ? 0.0F : this.mc.gameSettings.streamMicVolume);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_152930_t()
/*     */   {
/* 369 */     GameSettings gamesettings = this.mc.gameSettings;
/* 370 */     VideoParams videoparams = this.broadcastController.func_152834_a(formatStreamKbps(gamesettings.streamKbps), formatStreamFps(gamesettings.streamFps), formatStreamBps(gamesettings.streamBytesPerPixel), this.mc.displayWidth / this.mc.displayHeight);
/*     */     
/* 372 */     switch (gamesettings.streamCompression)
/*     */     {
/*     */     case 0: 
/* 375 */       videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
/* 376 */       break;
/*     */     
/*     */     case 1: 
/* 379 */       videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
/* 380 */       break;
/*     */     
/*     */     case 2: 
/* 383 */       videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
/*     */     }
/*     */     
/* 386 */     if (this.framebuffer == null)
/*     */     {
/* 388 */       this.framebuffer = new Framebuffer(videoparams.outputWidth, videoparams.outputHeight, false);
/*     */     }
/*     */     else
/*     */     {
/* 392 */       this.framebuffer.createBindFramebuffer(videoparams.outputWidth, videoparams.outputHeight);
/*     */     }
/*     */     
/* 395 */     if ((gamesettings.streamPreferredServer != null) && (gamesettings.streamPreferredServer.length() > 0)) {
/*     */       IngestServer[] arrayOfIngestServer;
/* 397 */       int j = (arrayOfIngestServer = func_152925_v()).length; for (int i = 0; i < j; i++) { IngestServer ingestserver = arrayOfIngestServer[i];
/*     */         
/* 399 */         if (ingestserver.serverUrl.equals(gamesettings.streamPreferredServer))
/*     */         {
/* 401 */           this.broadcastController.func_152824_a(ingestserver);
/* 402 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 407 */     this.targetFPS = videoparams.targetFps;
/* 408 */     this.field_152957_i = gamesettings.streamSendMetadata;
/* 409 */     this.broadcastController.func_152836_a(videoparams);
/* 410 */     LOGGER.info(STREAM_MARKER, "Streaming at {}/{} at {} kbps to {}", new Object[] { Integer.valueOf(videoparams.outputWidth), Integer.valueOf(videoparams.outputHeight), Integer.valueOf(videoparams.maxKbps), this.broadcastController.func_152833_s().serverUrl });
/* 411 */     this.broadcastController.func_152828_a(null, "Minecraft", null);
/*     */   }
/*     */   
/*     */   public void stopBroadcasting()
/*     */   {
/* 416 */     if (this.broadcastController.stopBroadcasting())
/*     */     {
/* 418 */       LOGGER.info(STREAM_MARKER, "Stopped streaming to Twitch");
/*     */     }
/*     */     else
/*     */     {
/* 422 */       LOGGER.warn(STREAM_MARKER, "Could not stop streaming to Twitch");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_152897_a(ErrorCode p_152897_1_)
/*     */   {
/* 432 */     if (ErrorCode.succeeded(p_152897_1_))
/*     */     {
/* 434 */       LOGGER.debug(STREAM_MARKER, "Login attempt successful");
/* 435 */       this.loggedIn = true;
/*     */     }
/*     */     else
/*     */     {
/* 439 */       LOGGER.warn(STREAM_MARKER, "Login attempt unsuccessful: {} (error code {})", new Object[] { ErrorCode.getString(p_152897_1_), Integer.valueOf(p_152897_1_.getValue()) });
/* 440 */       this.loggedIn = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_152891_a(BroadcastController.BroadcastState p_152891_1_)
/*     */   {
/* 450 */     LOGGER.debug(STREAM_MARKER, "Broadcast state changed to {}", new Object[] { p_152891_1_ });
/*     */     
/* 452 */     if (p_152891_1_ == BroadcastController.BroadcastState.Initialized)
/*     */     {
/* 454 */       this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_152895_a()
/*     */   {
/* 460 */     LOGGER.info(STREAM_MARKER, "Logged out of twitch");
/*     */   }
/*     */   
/*     */   public void func_152894_a(StreamInfo p_152894_1_)
/*     */   {
/* 465 */     LOGGER.debug(STREAM_MARKER, "Stream info updated; {} viewers on stream ID {}", new Object[] { Integer.valueOf(p_152894_1_.viewers), Long.valueOf(p_152894_1_.streamId) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_152893_b(ErrorCode p_152893_1_)
/*     */   {
/* 474 */     LOGGER.warn(STREAM_MARKER, "Issue submitting frame: {} (Error code {})", new Object[] { ErrorCode.getString(p_152893_1_), Integer.valueOf(p_152893_1_.getValue()) });
/* 475 */     this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + p_152893_1_ + " (" + ErrorCode.getString(p_152893_1_) + ")"), 2);
/*     */   }
/*     */   
/*     */   public void func_152899_b()
/*     */   {
/* 480 */     updateStreamVolume();
/* 481 */     LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has started");
/*     */   }
/*     */   
/*     */   public void func_152901_c()
/*     */   {
/* 486 */     LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has stopped");
/*     */   }
/*     */   
/*     */   public void func_152892_c(ErrorCode p_152892_1_)
/*     */   {
/* 491 */     if (p_152892_1_ == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED)
/*     */     {
/* 493 */       IChatComponent ichatcomponent = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
/* 494 */       ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
/* 495 */       ichatcomponent.getChatStyle().setUnderlined(Boolean.valueOf(true));
/* 496 */       IChatComponent ichatcomponent1 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", new Object[] { ichatcomponent });
/* 497 */       ichatcomponent1.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
/* 498 */       this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent1);
/*     */     }
/*     */     else
/*     */     {
/* 502 */       IChatComponent ichatcomponent2 = new ChatComponentTranslation("stream.unavailable.unknown.chat", new Object[] { ErrorCode.getString(p_152892_1_) });
/* 503 */       ichatcomponent2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
/* 504 */       this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_152907_a(IngestServerTester p_152907_1_, IngestServerTester.IngestTestState p_152907_2_)
/*     */   {
/* 510 */     LOGGER.debug(STREAM_MARKER, "Ingest test state changed to {}", new Object[] { p_152907_2_ });
/*     */     
/* 512 */     if (p_152907_2_ == IngestServerTester.IngestTestState.Finished)
/*     */     {
/* 514 */       this.field_152960_l = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static int formatStreamFps(float p_152948_0_)
/*     */   {
/* 520 */     return MathHelper.floor_float(10.0F + p_152948_0_ * 50.0F);
/*     */   }
/*     */   
/*     */   public static int formatStreamKbps(float p_152946_0_)
/*     */   {
/* 525 */     return MathHelper.floor_float(230.0F + p_152946_0_ * 3270.0F);
/*     */   }
/*     */   
/*     */   public static float formatStreamBps(float p_152947_0_)
/*     */   {
/* 530 */     return 0.1F + p_152947_0_ * 0.1F;
/*     */   }
/*     */   
/*     */   public IngestServer[] func_152925_v()
/*     */   {
/* 535 */     return this.broadcastController.func_152855_t().getServers();
/*     */   }
/*     */   
/*     */   public void func_152909_x()
/*     */   {
/* 540 */     IngestServerTester ingestservertester = this.broadcastController.func_152838_J();
/*     */     
/* 542 */     if (ingestservertester != null)
/*     */     {
/* 544 */       ingestservertester.func_153042_a(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public IngestServerTester func_152932_y()
/*     */   {
/* 550 */     return this.broadcastController.isReady();
/*     */   }
/*     */   
/*     */   public boolean func_152908_z()
/*     */   {
/* 555 */     return this.broadcastController.isIngestTesting();
/*     */   }
/*     */   
/*     */   public int func_152920_A()
/*     */   {
/* 560 */     return isBroadcasting() ? this.broadcastController.getStreamInfo().viewers : 0;
/*     */   }
/*     */   
/*     */   public void func_176023_d(ErrorCode p_176023_1_)
/*     */   {
/* 565 */     if (ErrorCode.failed(p_176023_1_))
/*     */     {
/* 567 */       LOGGER.error(STREAM_MARKER, "Chat failed to initialize");
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_176022_e(ErrorCode p_176022_1_)
/*     */   {
/* 573 */     if (ErrorCode.failed(p_176022_1_))
/*     */     {
/* 575 */       LOGGER.error(STREAM_MARKER, "Chat failed to shutdown");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_180605_a(String p_180605_1_, ChatRawMessage[] p_180605_2_)
/*     */   {
/*     */     ChatRawMessage[] arrayOfChatRawMessage;
/*     */     
/* 585 */     int j = (arrayOfChatRawMessage = p_180605_2_).length; for (int i = 0; i < j; i++) { ChatRawMessage chatrawmessage = arrayOfChatRawMessage[i];
/*     */       
/* 587 */       func_176027_a(chatrawmessage.userName, chatrawmessage);
/*     */       
/* 589 */       if (func_176028_a(chatrawmessage.modes, chatrawmessage.subscriptions, this.mc.gameSettings.streamChatUserFilter))
/*     */       {
/* 591 */         IChatComponent ichatcomponent = new ChatComponentText(chatrawmessage.userName);
/* 592 */         IChatComponent ichatcomponent1 = new ChatComponentTranslation("chat.stream." + (chatrawmessage.action ? "emote" : "text"), new Object[] { this.twitchComponent, ichatcomponent, EnumChatFormatting.getTextWithoutFormattingCodes(chatrawmessage.message) });
/*     */         
/* 594 */         if (chatrawmessage.action)
/*     */         {
/* 596 */           ichatcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
/*     */         }
/*     */         
/* 599 */         IChatComponent ichatcomponent2 = new ChatComponentText("");
/* 600 */         ichatcomponent2.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
/*     */         
/* 602 */         for (IChatComponent ichatcomponent3 : net.minecraft.client.gui.stream.GuiTwitchUserMode.func_152328_a(chatrawmessage.modes, chatrawmessage.subscriptions, null))
/*     */         {
/* 604 */           ichatcomponent2.appendText("\n");
/* 605 */           ichatcomponent2.appendSibling(ichatcomponent3);
/*     */         }
/*     */         
/* 608 */         ichatcomponent.getChatStyle().setChatHoverEvent(new net.minecraft.event.HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, ichatcomponent2));
/* 609 */         ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, chatrawmessage.userName));
/* 610 */         this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void func_176027_a(String p_176027_1_, ChatRawMessage p_176027_2_)
/*     */   {
/* 621 */     ChatUserInfo chatuserinfo = (ChatUserInfo)this.field_152955_g.get(p_176027_1_);
/*     */     
/* 623 */     if (chatuserinfo == null)
/*     */     {
/* 625 */       chatuserinfo = new ChatUserInfo();
/* 626 */       chatuserinfo.displayName = p_176027_1_;
/* 627 */       this.field_152955_g.put(p_176027_1_, chatuserinfo);
/*     */     }
/*     */     
/* 630 */     chatuserinfo.subscriptions = p_176027_2_.subscriptions;
/* 631 */     chatuserinfo.modes = p_176027_2_.modes;
/* 632 */     chatuserinfo.nameColorARGB = p_176027_2_.nameColorARGB;
/*     */   }
/*     */   
/*     */   private boolean func_176028_a(Set<ChatUserMode> p_176028_1_, Set<ChatUserSubscription> p_176028_2_, int p_176028_3_)
/*     */   {
/* 637 */     return !p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED);
/*     */   }
/*     */   
/*     */   public void func_176018_a(String p_176018_1_, ChatUserInfo[] p_176018_2_, ChatUserInfo[] p_176018_3_, ChatUserInfo[] p_176018_4_) {
/*     */     ChatUserInfo[] arrayOfChatUserInfo;
/* 642 */     int j = (arrayOfChatUserInfo = p_176018_3_).length; for (int i = 0; i < j; i++) { ChatUserInfo chatuserinfo = arrayOfChatUserInfo[i];
/*     */       
/* 644 */       this.field_152955_g.remove(chatuserinfo.displayName);
/*     */     }
/*     */     
/* 647 */     j = (arrayOfChatUserInfo = p_176018_4_).length; for (i = 0; i < j; i++) { ChatUserInfo chatuserinfo1 = arrayOfChatUserInfo[i];
/*     */       
/* 649 */       this.field_152955_g.put(chatuserinfo1.displayName, chatuserinfo1);
/*     */     }
/*     */     
/* 652 */     j = (arrayOfChatUserInfo = p_176018_2_).length; for (i = 0; i < j; i++) { ChatUserInfo chatuserinfo2 = arrayOfChatUserInfo[i];
/*     */       
/* 654 */       this.field_152955_g.put(chatuserinfo2.displayName, chatuserinfo2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_180606_a(String p_180606_1_)
/*     */   {
/* 660 */     LOGGER.debug(STREAM_MARKER, "Chat connected");
/*     */   }
/*     */   
/*     */   public void func_180607_b(String p_180607_1_)
/*     */   {
/* 665 */     LOGGER.debug(STREAM_MARKER, "Chat disconnected");
/* 666 */     this.field_152955_g.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean func_152927_B()
/*     */   {
/* 691 */     return (this.field_176029_e != null) && (this.field_176029_e.equals(this.broadcastController.getChannelInfo().name));
/*     */   }
/*     */   
/*     */   public String func_152921_C()
/*     */   {
/* 696 */     return this.field_176029_e;
/*     */   }
/*     */   
/*     */   public ChatUserInfo func_152926_a(String p_152926_1_)
/*     */   {
/* 701 */     return (ChatUserInfo)this.field_152955_g.get(p_152926_1_);
/*     */   }
/*     */   
/*     */   public void func_152917_b(String p_152917_1_)
/*     */   {
/* 706 */     this.chatController.func_175986_a(this.field_176029_e, p_152917_1_);
/*     */   }
/*     */   
/*     */   public boolean func_152928_D()
/*     */   {
/* 711 */     return (field_152965_q) && (this.broadcastController.func_152858_b());
/*     */   }
/*     */   
/*     */   public ErrorCode func_152912_E()
/*     */   {
/* 716 */     return !field_152965_q ? ErrorCode.TTV_EC_OS_TOO_OLD : this.broadcastController.getErrorCode();
/*     */   }
/*     */   
/*     */   public boolean func_152913_F()
/*     */   {
/* 721 */     return this.loggedIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void muteMicrophone(boolean p_152910_1_)
/*     */   {
/* 729 */     this.field_152963_o = p_152910_1_;
/* 730 */     updateStreamVolume();
/*     */   }
/*     */   
/*     */   public boolean func_152929_G()
/*     */   {
/* 735 */     boolean flag = this.mc.gameSettings.streamMicToggleBehavior == 1;
/* 736 */     return (this.field_152962_n) || (this.mc.gameSettings.streamMicVolume <= 0.0F) || (flag != this.field_152963_o);
/*     */   }
/*     */   
/*     */   public IStream.AuthFailureReason func_152918_H()
/*     */   {
/* 741 */     return this.authFailureReason;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 748 */       if (net.minecraft.util.Util.getOSType() == Util.EnumOS.WINDOWS)
/*     */       {
/* 750 */         System.loadLibrary("avutil-ttv-51");
/* 751 */         System.loadLibrary("swresample-ttv-0");
/* 752 */         System.loadLibrary("libmp3lame-ttv");
/*     */         
/* 754 */         if (System.getProperty("os.arch").contains("64"))
/*     */         {
/* 756 */           System.loadLibrary("libmfxsw64");
/*     */         }
/*     */         else
/*     */         {
/* 760 */           System.loadLibrary("libmfxsw32");
/*     */         }
/*     */       }
/*     */       
/* 764 */       field_152965_q = true;
/*     */     }
/*     */     catch (Throwable var1)
/*     */     {
/* 768 */       field_152965_q = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_152900_a(ErrorCode p_152900_1_, AuthToken p_152900_2_) {}
/*     */   
/*     */   public void func_152898_a(ErrorCode p_152898_1_, GameInfo[] p_152898_2_) {}
/*     */   
/*     */   public void func_152896_a(IngestList p_152896_1_) {}
/*     */   
/*     */   public void func_176017_a(ChatController.ChatState p_176017_1_) {}
/*     */   
/*     */   public void func_176025_a(String p_176025_1_, ChatTokenizedMessage[] p_176025_2_) {}
/*     */   
/*     */   public void func_176019_a(String p_176019_1_, String p_176019_2_) {}
/*     */   
/*     */   public void func_176021_d() {}
/*     */   
/*     */   public void func_176024_e() {}
/*     */   
/*     */   public void func_176016_c(String p_176016_1_) {}
/*     */   
/*     */   public void func_176020_d(String p_176020_1_) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\TwitchStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */