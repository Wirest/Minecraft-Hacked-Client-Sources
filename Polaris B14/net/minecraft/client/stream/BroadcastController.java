/*      */ package net.minecraft.client.stream;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ThreadSafeBoundList;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import tv.twitch.AuthToken;
/*      */ import tv.twitch.Core;
/*      */ import tv.twitch.ErrorCode;
/*      */ import tv.twitch.MessageLevel;
/*      */ import tv.twitch.StandardCoreAPI;
/*      */ import tv.twitch.broadcast.ArchivingState;
/*      */ import tv.twitch.broadcast.AudioDeviceType;
/*      */ import tv.twitch.broadcast.AudioParams;
/*      */ import tv.twitch.broadcast.ChannelInfo;
/*      */ import tv.twitch.broadcast.DesktopStreamAPI;
/*      */ import tv.twitch.broadcast.EncodingCpuUsage;
/*      */ import tv.twitch.broadcast.FrameBuffer;
/*      */ import tv.twitch.broadcast.GameInfo;
/*      */ import tv.twitch.broadcast.GameInfoList;
/*      */ import tv.twitch.broadcast.IStatCallbacks;
/*      */ import tv.twitch.broadcast.IStreamCallbacks;
/*      */ import tv.twitch.broadcast.IngestList;
/*      */ import tv.twitch.broadcast.IngestServer;
/*      */ import tv.twitch.broadcast.PixelFormat;
/*      */ import tv.twitch.broadcast.StartFlags;
/*      */ import tv.twitch.broadcast.StatType;
/*      */ import tv.twitch.broadcast.Stream;
/*      */ import tv.twitch.broadcast.StreamInfo;
/*      */ import tv.twitch.broadcast.StreamInfoForSetting;
/*      */ import tv.twitch.broadcast.UserInfo;
/*      */ import tv.twitch.broadcast.VideoParams;
/*      */ 
/*      */ public class BroadcastController
/*      */ {
/*   41 */   private static final Logger logger = ;
/*   42 */   protected final int field_152865_a = 30;
/*   43 */   protected final int field_152866_b = 3;
/*   44 */   private static final ThreadSafeBoundList<String> field_152862_C = new ThreadSafeBoundList(String.class, 50);
/*   45 */   private String field_152863_D = null;
/*   46 */   protected BroadcastListener broadcastListener = null;
/*   47 */   protected String field_152868_d = "";
/*   48 */   protected String field_152869_e = "";
/*   49 */   protected String field_152870_f = "";
/*   50 */   protected boolean field_152871_g = true;
/*   51 */   protected Core field_152872_h = null;
/*   52 */   protected Stream field_152873_i = null;
/*   53 */   protected List<FrameBuffer> field_152874_j = Lists.newArrayList();
/*   54 */   protected List<FrameBuffer> field_152875_k = Lists.newArrayList();
/*   55 */   protected boolean field_152876_l = false;
/*   56 */   protected boolean field_152877_m = false;
/*   57 */   protected boolean field_152878_n = false;
/*   58 */   protected BroadcastState broadcastState = BroadcastState.Uninitialized;
/*   59 */   protected String field_152880_p = null;
/*   60 */   protected VideoParams videoParamaters = null;
/*   61 */   protected AudioParams audioParamaters = null;
/*   62 */   protected IngestList ingestList = new IngestList(new IngestServer[0]);
/*   63 */   protected IngestServer field_152884_t = null;
/*   64 */   protected AuthToken authenticationToken = new AuthToken();
/*   65 */   protected ChannelInfo channelInfo = new ChannelInfo();
/*   66 */   protected UserInfo userInfo = new UserInfo();
/*   67 */   protected StreamInfo streamInfo = new StreamInfo();
/*   68 */   protected ArchivingState field_152889_y = new ArchivingState();
/*   69 */   protected long field_152890_z = 0L;
/*   70 */   protected IngestServerTester field_152860_A = null;
/*      */   private ErrorCode errorCode;
/*   72 */   protected IStreamCallbacks field_177948_B = new IStreamCallbacks()
/*      */   {
/*      */     public void requestAuthTokenCallback(ErrorCode p_requestAuthTokenCallback_1_, AuthToken p_requestAuthTokenCallback_2_)
/*      */     {
/*   76 */       if (ErrorCode.succeeded(p_requestAuthTokenCallback_1_))
/*      */       {
/*   78 */         BroadcastController.this.authenticationToken = p_requestAuthTokenCallback_2_;
/*   79 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Authenticated);
/*      */       }
/*      */       else
/*      */       {
/*   83 */         BroadcastController.this.authenticationToken.data = "";
/*   84 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
/*   85 */         String s = ErrorCode.getString(p_requestAuthTokenCallback_1_);
/*   86 */         BroadcastController.this.func_152820_d(String.format("RequestAuthTokenDoneCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*   91 */         if (BroadcastController.this.broadcastListener != null)
/*      */         {
/*   93 */           BroadcastController.this.broadcastListener.func_152900_a(p_requestAuthTokenCallback_1_, p_requestAuthTokenCallback_2_);
/*      */         }
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*   98 */         BroadcastController.this.func_152820_d(exception.toString());
/*      */       }
/*      */     }
/*      */     
/*      */     public void loginCallback(ErrorCode p_loginCallback_1_, ChannelInfo p_loginCallback_2_) {
/*  103 */       if (ErrorCode.succeeded(p_loginCallback_1_))
/*      */       {
/*  105 */         BroadcastController.this.channelInfo = p_loginCallback_2_;
/*  106 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.LoggedIn);
/*  107 */         BroadcastController.this.field_152877_m = true;
/*      */       }
/*      */       else
/*      */       {
/*  111 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
/*  112 */         BroadcastController.this.field_152877_m = false;
/*  113 */         String s = ErrorCode.getString(p_loginCallback_1_);
/*  114 */         BroadcastController.this.func_152820_d(String.format("LoginCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  119 */         if (BroadcastController.this.broadcastListener != null)
/*      */         {
/*  121 */           BroadcastController.this.broadcastListener.func_152897_a(p_loginCallback_1_);
/*      */         }
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*  126 */         BroadcastController.this.func_152820_d(exception.toString());
/*      */       }
/*      */     }
/*      */     
/*      */     public void getIngestServersCallback(ErrorCode p_getIngestServersCallback_1_, IngestList p_getIngestServersCallback_2_) {
/*  131 */       if (ErrorCode.succeeded(p_getIngestServersCallback_1_))
/*      */       {
/*  133 */         BroadcastController.this.ingestList = p_getIngestServersCallback_2_;
/*  134 */         BroadcastController.this.field_152884_t = BroadcastController.this.ingestList.getDefaultServer();
/*  135 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReceivedIngestServers);
/*      */         
/*      */         try
/*      */         {
/*  139 */           if (BroadcastController.this.broadcastListener == null)
/*      */             return;
/*  141 */           BroadcastController.this.broadcastListener.func_152896_a(p_getIngestServersCallback_2_);
/*      */ 
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  146 */           BroadcastController.this.func_152820_d(exception.toString());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  151 */         String s = ErrorCode.getString(p_getIngestServersCallback_1_);
/*  152 */         BroadcastController.this.func_152820_d(String.format("IngestListCallback got failure: %s", new Object[] { s }));
/*  153 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.LoggingIn);
/*      */       }
/*      */     }
/*      */     
/*      */     public void getUserInfoCallback(ErrorCode p_getUserInfoCallback_1_, UserInfo p_getUserInfoCallback_2_) {
/*  158 */       BroadcastController.this.userInfo = p_getUserInfoCallback_2_;
/*      */       
/*  160 */       if (ErrorCode.failed(p_getUserInfoCallback_1_))
/*      */       {
/*  162 */         String s = ErrorCode.getString(p_getUserInfoCallback_1_);
/*  163 */         BroadcastController.this.func_152820_d(String.format("UserInfoDoneCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void getStreamInfoCallback(ErrorCode p_getStreamInfoCallback_1_, StreamInfo p_getStreamInfoCallback_2_) {
/*  168 */       if (ErrorCode.succeeded(p_getStreamInfoCallback_1_))
/*      */       {
/*  170 */         BroadcastController.this.streamInfo = p_getStreamInfoCallback_2_;
/*      */         
/*      */         try
/*      */         {
/*  174 */           if (BroadcastController.this.broadcastListener == null)
/*      */             return;
/*  176 */           BroadcastController.this.broadcastListener.func_152894_a(p_getStreamInfoCallback_2_);
/*      */ 
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  181 */           BroadcastController.this.func_152820_d(exception.toString());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  186 */         String s = ErrorCode.getString(p_getStreamInfoCallback_1_);
/*  187 */         BroadcastController.this.func_152832_e(String.format("StreamInfoDoneCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void getArchivingStateCallback(ErrorCode p_getArchivingStateCallback_1_, ArchivingState p_getArchivingStateCallback_2_) {
/*  192 */       BroadcastController.this.field_152889_y = p_getArchivingStateCallback_2_;
/*      */       
/*  194 */       if (ErrorCode.failed(p_getArchivingStateCallback_1_)) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void runCommercialCallback(ErrorCode p_runCommercialCallback_1_)
/*      */     {
/*  201 */       if (ErrorCode.failed(p_runCommercialCallback_1_))
/*      */       {
/*  203 */         String s = ErrorCode.getString(p_runCommercialCallback_1_);
/*  204 */         BroadcastController.this.func_152832_e(String.format("RunCommercialCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void setStreamInfoCallback(ErrorCode p_setStreamInfoCallback_1_) {
/*  209 */       if (ErrorCode.failed(p_setStreamInfoCallback_1_))
/*      */       {
/*  211 */         String s = ErrorCode.getString(p_setStreamInfoCallback_1_);
/*  212 */         BroadcastController.this.func_152832_e(String.format("SetStreamInfoCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void getGameNameListCallback(ErrorCode p_getGameNameListCallback_1_, GameInfoList p_getGameNameListCallback_2_) {
/*  217 */       if (ErrorCode.failed(p_getGameNameListCallback_1_))
/*      */       {
/*  219 */         String s = ErrorCode.getString(p_getGameNameListCallback_1_);
/*  220 */         BroadcastController.this.func_152820_d(String.format("GameNameListCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  225 */         if (BroadcastController.this.broadcastListener != null)
/*      */         {
/*  227 */           BroadcastController.this.broadcastListener.func_152898_a(p_getGameNameListCallback_1_, p_getGameNameListCallback_2_ == null ? new GameInfo[0] : p_getGameNameListCallback_2_.list);
/*      */         }
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*  232 */         BroadcastController.this.func_152820_d(exception.toString());
/*      */       }
/*      */     }
/*      */     
/*      */     public void bufferUnlockCallback(long p_bufferUnlockCallback_1_) {
/*  237 */       FrameBuffer framebuffer = FrameBuffer.lookupBuffer(p_bufferUnlockCallback_1_);
/*  238 */       BroadcastController.this.field_152875_k.add(framebuffer);
/*      */     }
/*      */     
/*      */     public void startCallback(ErrorCode p_startCallback_1_) {
/*  242 */       if (ErrorCode.succeeded(p_startCallback_1_))
/*      */       {
/*      */         try
/*      */         {
/*  246 */           if (BroadcastController.this.broadcastListener != null)
/*      */           {
/*  248 */             BroadcastController.this.broadcastListener.func_152899_b();
/*      */           }
/*      */         }
/*      */         catch (Exception exception1)
/*      */         {
/*  253 */           BroadcastController.this.func_152820_d(exception1.toString());
/*      */         }
/*      */         
/*  256 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Broadcasting);
/*      */       }
/*      */       else
/*      */       {
/*  260 */         BroadcastController.this.videoParamaters = null;
/*  261 */         BroadcastController.this.audioParamaters = null;
/*  262 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
/*      */         
/*      */         try
/*      */         {
/*  266 */           if (BroadcastController.this.broadcastListener != null)
/*      */           {
/*  268 */             BroadcastController.this.broadcastListener.func_152892_c(p_startCallback_1_);
/*      */           }
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  273 */           BroadcastController.this.func_152820_d(exception.toString());
/*      */         }
/*      */         
/*  276 */         String s = ErrorCode.getString(p_startCallback_1_);
/*  277 */         BroadcastController.this.func_152820_d(String.format("startCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void stopCallback(ErrorCode p_stopCallback_1_) {
/*  282 */       if (ErrorCode.succeeded(p_stopCallback_1_))
/*      */       {
/*  284 */         BroadcastController.this.videoParamaters = null;
/*  285 */         BroadcastController.this.audioParamaters = null;
/*  286 */         BroadcastController.this.func_152831_M();
/*      */         
/*      */         try
/*      */         {
/*  290 */           if (BroadcastController.this.broadcastListener != null)
/*      */           {
/*  292 */             BroadcastController.this.broadcastListener.func_152901_c();
/*      */           }
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  297 */           BroadcastController.this.func_152820_d(exception.toString());
/*      */         }
/*      */         
/*  300 */         if (BroadcastController.this.field_152877_m)
/*      */         {
/*  302 */           BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
/*      */         }
/*      */         else
/*      */         {
/*  306 */           BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  311 */         BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
/*  312 */         String s = ErrorCode.getString(p_stopCallback_1_);
/*  313 */         BroadcastController.this.func_152820_d(String.format("stopCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void sendActionMetaDataCallback(ErrorCode p_sendActionMetaDataCallback_1_) {
/*  318 */       if (ErrorCode.failed(p_sendActionMetaDataCallback_1_))
/*      */       {
/*  320 */         String s = ErrorCode.getString(p_sendActionMetaDataCallback_1_);
/*  321 */         BroadcastController.this.func_152820_d(String.format("sendActionMetaDataCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void sendStartSpanMetaDataCallback(ErrorCode p_sendStartSpanMetaDataCallback_1_) {
/*  326 */       if (ErrorCode.failed(p_sendStartSpanMetaDataCallback_1_))
/*      */       {
/*  328 */         String s = ErrorCode.getString(p_sendStartSpanMetaDataCallback_1_);
/*  329 */         BroadcastController.this.func_152820_d(String.format("sendStartSpanMetaDataCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */     
/*      */     public void sendEndSpanMetaDataCallback(ErrorCode p_sendEndSpanMetaDataCallback_1_) {
/*  334 */       if (ErrorCode.failed(p_sendEndSpanMetaDataCallback_1_))
/*      */       {
/*  336 */         String s = ErrorCode.getString(p_sendEndSpanMetaDataCallback_1_);
/*  337 */         BroadcastController.this.func_152820_d(String.format("sendEndSpanMetaDataCallback got failure: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */   };
/*  341 */   protected IStatCallbacks field_177949_C = new IStatCallbacks()
/*      */   {
/*      */     public void statCallback(StatType p_statCallback_1_, long p_statCallback_2_) {}
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */   public void func_152841_a(BroadcastListener p_152841_1_)
/*      */   {
/*  350 */     this.broadcastListener = p_152841_1_;
/*      */   }
/*      */   
/*      */   public boolean func_152858_b()
/*      */   {
/*  355 */     return this.field_152876_l;
/*      */   }
/*      */   
/*      */   public void func_152842_a(String p_152842_1_)
/*      */   {
/*  360 */     this.field_152868_d = p_152842_1_;
/*      */   }
/*      */   
/*      */   public StreamInfo getStreamInfo()
/*      */   {
/*  365 */     return this.streamInfo;
/*      */   }
/*      */   
/*      */   public ChannelInfo getChannelInfo()
/*      */   {
/*  370 */     return this.channelInfo;
/*      */   }
/*      */   
/*      */   public boolean isBroadcasting()
/*      */   {
/*  375 */     return (this.broadcastState == BroadcastState.Broadcasting) || (this.broadcastState == BroadcastState.Paused);
/*      */   }
/*      */   
/*      */   public boolean isReadyToBroadcast()
/*      */   {
/*  380 */     return this.broadcastState == BroadcastState.ReadyToBroadcast;
/*      */   }
/*      */   
/*      */   public boolean isIngestTesting()
/*      */   {
/*  385 */     return this.broadcastState == BroadcastState.IngestTesting;
/*      */   }
/*      */   
/*      */   public boolean isBroadcastPaused()
/*      */   {
/*  390 */     return this.broadcastState == BroadcastState.Paused;
/*      */   }
/*      */   
/*      */   public boolean func_152849_q()
/*      */   {
/*  395 */     return this.field_152877_m;
/*      */   }
/*      */   
/*      */   public IngestServer func_152833_s()
/*      */   {
/*  400 */     return this.field_152884_t;
/*      */   }
/*      */   
/*      */   public void func_152824_a(IngestServer p_152824_1_)
/*      */   {
/*  405 */     this.field_152884_t = p_152824_1_;
/*      */   }
/*      */   
/*      */   public IngestList func_152855_t()
/*      */   {
/*  410 */     return this.ingestList;
/*      */   }
/*      */   
/*      */   public void setRecordingDeviceVolume(float p_152829_1_)
/*      */   {
/*  415 */     this.field_152873_i.setVolume(AudioDeviceType.TTV_RECORDER_DEVICE, p_152829_1_);
/*      */   }
/*      */   
/*      */   public void setPlaybackDeviceVolume(float p_152837_1_)
/*      */   {
/*  420 */     this.field_152873_i.setVolume(AudioDeviceType.TTV_PLAYBACK_DEVICE, p_152837_1_);
/*      */   }
/*      */   
/*      */   public IngestServerTester isReady()
/*      */   {
/*  425 */     return this.field_152860_A;
/*      */   }
/*      */   
/*      */   public long func_152844_x()
/*      */   {
/*  430 */     return this.field_152873_i.getStreamTime();
/*      */   }
/*      */   
/*      */   protected boolean func_152848_y()
/*      */   {
/*  435 */     return true;
/*      */   }
/*      */   
/*      */   public ErrorCode getErrorCode()
/*      */   {
/*  440 */     return this.errorCode;
/*      */   }
/*      */   
/*      */   public BroadcastController()
/*      */   {
/*  445 */     this.field_152872_h = Core.getInstance();
/*      */     
/*  447 */     if (Core.getInstance() == null)
/*      */     {
/*  449 */       this.field_152872_h = new Core(new StandardCoreAPI());
/*      */     }
/*      */     
/*  452 */     this.field_152873_i = new Stream(new DesktopStreamAPI());
/*      */   }
/*      */   
/*      */   protected PixelFormat func_152826_z()
/*      */   {
/*  457 */     return PixelFormat.TTV_PF_RGBA;
/*      */   }
/*      */   
/*      */   public boolean func_152817_A()
/*      */   {
/*  462 */     if (this.field_152876_l)
/*      */     {
/*  464 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  468 */     this.field_152873_i.setStreamCallbacks(this.field_177948_B);
/*  469 */     ErrorCode errorcode = this.field_152872_h.initialize(this.field_152868_d, System.getProperty("java.library.path"));
/*      */     
/*  471 */     if (!func_152853_a(errorcode))
/*      */     {
/*  473 */       this.field_152873_i.setStreamCallbacks(null);
/*  474 */       this.errorCode = errorcode;
/*  475 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  479 */     errorcode = this.field_152872_h.setTraceLevel(MessageLevel.TTV_ML_ERROR);
/*      */     
/*  481 */     if (!func_152853_a(errorcode))
/*      */     {
/*  483 */       this.field_152873_i.setStreamCallbacks(null);
/*  484 */       this.field_152872_h.shutdown();
/*  485 */       this.errorCode = errorcode;
/*  486 */       return false;
/*      */     }
/*  488 */     if (ErrorCode.succeeded(errorcode))
/*      */     {
/*  490 */       this.field_152876_l = true;
/*  491 */       func_152827_a(BroadcastState.Initialized);
/*  492 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  496 */     this.errorCode = errorcode;
/*  497 */     this.field_152872_h.shutdown();
/*  498 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean func_152851_B()
/*      */   {
/*  506 */     if (!this.field_152876_l)
/*      */     {
/*  508 */       return true;
/*      */     }
/*  510 */     if (isIngestTesting())
/*      */     {
/*  512 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  516 */     this.field_152878_n = true;
/*  517 */     func_152845_C();
/*  518 */     this.field_152873_i.setStreamCallbacks(null);
/*  519 */     this.field_152873_i.setStatCallbacks(null);
/*  520 */     ErrorCode errorcode = this.field_152872_h.shutdown();
/*  521 */     func_152853_a(errorcode);
/*  522 */     this.field_152876_l = false;
/*  523 */     this.field_152878_n = false;
/*  524 */     func_152827_a(BroadcastState.Uninitialized);
/*  525 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public void statCallback()
/*      */   {
/*  531 */     if (this.broadcastState != BroadcastState.Uninitialized)
/*      */     {
/*  533 */       if (this.field_152860_A != null)
/*      */       {
/*  535 */         this.field_152860_A.func_153039_l();
/*      */       }
/*  538 */       for (; 
/*  538 */           this.field_152860_A != null; func_152821_H())
/*      */       {
/*      */         try
/*      */         {
/*  542 */           Thread.sleep(200L);
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  546 */           func_152820_d(exception.toString());
/*      */         }
/*      */       }
/*      */       
/*  550 */       func_152851_B();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean func_152818_a(String p_152818_1_, AuthToken p_152818_2_)
/*      */   {
/*  556 */     if (isIngestTesting())
/*      */     {
/*  558 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  562 */     func_152845_C();
/*      */     
/*  564 */     if ((p_152818_1_ != null) && (!p_152818_1_.isEmpty()))
/*      */     {
/*  566 */       if ((p_152818_2_ != null) && (p_152818_2_.data != null) && (!p_152818_2_.data.isEmpty()))
/*      */       {
/*  568 */         this.field_152880_p = p_152818_1_;
/*  569 */         this.authenticationToken = p_152818_2_;
/*      */         
/*  571 */         if (func_152858_b())
/*      */         {
/*  573 */           func_152827_a(BroadcastState.Authenticated);
/*      */         }
/*      */         
/*  576 */         return true;
/*      */       }
/*      */       
/*      */ 
/*  580 */       func_152820_d("Auth token must be valid");
/*  581 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  586 */     func_152820_d("Username must be valid");
/*  587 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean func_152845_C()
/*      */   {
/*  594 */     if (isIngestTesting())
/*      */     {
/*  596 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  600 */     if (isBroadcasting())
/*      */     {
/*  602 */       this.field_152873_i.stop(false);
/*      */     }
/*      */     
/*  605 */     this.field_152880_p = "";
/*  606 */     this.authenticationToken = new AuthToken();
/*      */     
/*  608 */     if (!this.field_152877_m)
/*      */     {
/*  610 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  614 */     this.field_152877_m = false;
/*      */     
/*  616 */     if (!this.field_152878_n)
/*      */     {
/*      */       try
/*      */       {
/*  620 */         if (this.broadcastListener != null)
/*      */         {
/*  622 */           this.broadcastListener.func_152895_a();
/*      */         }
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*  627 */         func_152820_d(exception.toString());
/*      */       }
/*      */     }
/*      */     
/*  631 */     func_152827_a(BroadcastState.Initialized);
/*  632 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean func_152828_a(String p_152828_1_, String p_152828_2_, String p_152828_3_)
/*      */   {
/*  639 */     if (!this.field_152877_m)
/*      */     {
/*  641 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  645 */     if ((p_152828_1_ == null) || (p_152828_1_.equals("")))
/*      */     {
/*  647 */       p_152828_1_ = this.field_152880_p;
/*      */     }
/*      */     
/*  650 */     if (p_152828_2_ == null)
/*      */     {
/*  652 */       p_152828_2_ = "";
/*      */     }
/*      */     
/*  655 */     if (p_152828_3_ == null)
/*      */     {
/*  657 */       p_152828_3_ = "";
/*      */     }
/*      */     
/*  660 */     StreamInfoForSetting streaminfoforsetting = new StreamInfoForSetting();
/*  661 */     streaminfoforsetting.streamTitle = p_152828_3_;
/*  662 */     streaminfoforsetting.gameName = p_152828_2_;
/*  663 */     ErrorCode errorcode = this.field_152873_i.setStreamInfo(this.authenticationToken, p_152828_1_, streaminfoforsetting);
/*  664 */     func_152853_a(errorcode);
/*  665 */     return ErrorCode.succeeded(errorcode);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean requestCommercial()
/*      */   {
/*  671 */     if (!isBroadcasting())
/*      */     {
/*  673 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  677 */     ErrorCode errorcode = this.field_152873_i.runCommercial(this.authenticationToken);
/*  678 */     func_152853_a(errorcode);
/*  679 */     return ErrorCode.succeeded(errorcode);
/*      */   }
/*      */   
/*      */ 
/*      */   public VideoParams func_152834_a(int maxKbps, int p_152834_2_, float p_152834_3_, float p_152834_4_)
/*      */   {
/*  685 */     int[] aint = this.field_152873_i.getMaxResolution(maxKbps, p_152834_2_, p_152834_3_, p_152834_4_);
/*  686 */     VideoParams videoparams = new VideoParams();
/*  687 */     videoparams.maxKbps = maxKbps;
/*  688 */     videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
/*  689 */     videoparams.pixelFormat = func_152826_z();
/*  690 */     videoparams.targetFps = p_152834_2_;
/*  691 */     videoparams.outputWidth = aint[0];
/*  692 */     videoparams.outputHeight = aint[1];
/*  693 */     videoparams.disableAdaptiveBitrate = false;
/*  694 */     videoparams.verticalFlip = false;
/*  695 */     return videoparams;
/*      */   }
/*      */   
/*      */   public boolean func_152836_a(VideoParams p_152836_1_)
/*      */   {
/*  700 */     if ((p_152836_1_ != null) && (isReadyToBroadcast()))
/*      */     {
/*  702 */       this.videoParamaters = p_152836_1_.clone();
/*  703 */       this.audioParamaters = new AudioParams();
/*  704 */       this.audioParamaters.audioEnabled = ((this.field_152871_g) && (func_152848_y()));
/*  705 */       this.audioParamaters.enableMicCapture = this.audioParamaters.audioEnabled;
/*  706 */       this.audioParamaters.enablePlaybackCapture = this.audioParamaters.audioEnabled;
/*  707 */       this.audioParamaters.enablePassthroughAudio = false;
/*      */       
/*  709 */       if (!func_152823_L())
/*      */       {
/*  711 */         this.videoParamaters = null;
/*  712 */         this.audioParamaters = null;
/*  713 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  717 */       ErrorCode errorcode = this.field_152873_i.start(p_152836_1_, this.audioParamaters, this.field_152884_t, StartFlags.None, true);
/*      */       
/*  719 */       if (ErrorCode.failed(errorcode))
/*      */       {
/*  721 */         func_152831_M();
/*  722 */         String s = ErrorCode.getString(errorcode);
/*  723 */         func_152820_d(String.format("Error while starting to broadcast: %s", new Object[] { s }));
/*  724 */         this.videoParamaters = null;
/*  725 */         this.audioParamaters = null;
/*  726 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  730 */       func_152827_a(BroadcastState.Starting);
/*  731 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  737 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean stopBroadcasting()
/*      */   {
/*  743 */     if (!isBroadcasting())
/*      */     {
/*  745 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  749 */     ErrorCode errorcode = this.field_152873_i.stop(true);
/*      */     
/*  751 */     if (ErrorCode.failed(errorcode))
/*      */     {
/*  753 */       String s = ErrorCode.getString(errorcode);
/*  754 */       func_152820_d(String.format("Error while stopping the broadcast: %s", new Object[] { s }));
/*  755 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  759 */     func_152827_a(BroadcastState.Stopping);
/*  760 */     return ErrorCode.succeeded(errorcode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean func_152847_F()
/*      */   {
/*  767 */     if (!isBroadcasting())
/*      */     {
/*  769 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  773 */     ErrorCode errorcode = this.field_152873_i.pauseVideo();
/*      */     
/*  775 */     if (ErrorCode.failed(errorcode))
/*      */     {
/*  777 */       stopBroadcasting();
/*  778 */       String s = ErrorCode.getString(errorcode);
/*  779 */       func_152820_d(String.format("Error pausing stream: %s\n", new Object[] { s }));
/*      */     }
/*      */     else
/*      */     {
/*  783 */       func_152827_a(BroadcastState.Paused);
/*      */     }
/*      */     
/*  786 */     return ErrorCode.succeeded(errorcode);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_152854_G()
/*      */   {
/*  792 */     if (!isBroadcastPaused())
/*      */     {
/*  794 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  798 */     func_152827_a(BroadcastState.Broadcasting);
/*  799 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_152840_a(String p_152840_1_, long p_152840_2_, String p_152840_4_, String p_152840_5_)
/*      */   {
/*  805 */     ErrorCode errorcode = this.field_152873_i.sendActionMetaData(this.authenticationToken, p_152840_1_, p_152840_2_, p_152840_4_, p_152840_5_);
/*      */     
/*  807 */     if (ErrorCode.failed(errorcode))
/*      */     {
/*  809 */       String s = ErrorCode.getString(errorcode);
/*  810 */       func_152820_d(String.format("Error while sending meta data: %s\n", new Object[] { s }));
/*  811 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  815 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public long func_177946_b(String p_177946_1_, long p_177946_2_, String p_177946_4_, String p_177946_5_)
/*      */   {
/*  821 */     long i = this.field_152873_i.sendStartSpanMetaData(this.authenticationToken, p_177946_1_, p_177946_2_, p_177946_4_, p_177946_5_);
/*      */     
/*  823 */     if (i == -1L)
/*      */     {
/*  825 */       func_152820_d(String.format("Error in SendStartSpanMetaData\n", new Object[0]));
/*      */     }
/*      */     
/*  828 */     return i;
/*      */   }
/*      */   
/*      */   public boolean func_177947_a(String p_177947_1_, long p_177947_2_, long p_177947_4_, String p_177947_6_, String p_177947_7_)
/*      */   {
/*  833 */     if (p_177947_4_ == -1L)
/*      */     {
/*  835 */       func_152820_d(String.format("Invalid sequence id: %d\n", new Object[] { Long.valueOf(p_177947_4_) }));
/*  836 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  840 */     ErrorCode errorcode = this.field_152873_i.sendEndSpanMetaData(this.authenticationToken, p_177947_1_, p_177947_2_, p_177947_4_, p_177947_6_, p_177947_7_);
/*      */     
/*  842 */     if (ErrorCode.failed(errorcode))
/*      */     {
/*  844 */       String s = ErrorCode.getString(errorcode);
/*  845 */       func_152820_d(String.format("Error in SendStopSpanMetaData: %s\n", new Object[] { s }));
/*  846 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  850 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void func_152827_a(BroadcastState p_152827_1_)
/*      */   {
/*  857 */     if (p_152827_1_ != this.broadcastState)
/*      */     {
/*  859 */       this.broadcastState = p_152827_1_;
/*      */       
/*      */       try
/*      */       {
/*  863 */         if (this.broadcastListener != null)
/*      */         {
/*  865 */           this.broadcastListener.func_152891_a(p_152827_1_);
/*      */         }
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*  870 */         func_152820_d(exception.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_152821_H()
/*      */   {
/*  877 */     if ((this.field_152873_i != null) && (this.field_152876_l))
/*      */     {
/*  879 */       ErrorCode errorcode = this.field_152873_i.pollTasks();
/*  880 */       func_152853_a(errorcode);
/*      */       
/*  882 */       if (isIngestTesting())
/*      */       {
/*  884 */         this.field_152860_A.func_153041_j();
/*      */         
/*  886 */         if (this.field_152860_A.func_153032_e())
/*      */         {
/*  888 */           this.field_152860_A = null;
/*  889 */           func_152827_a(BroadcastState.ReadyToBroadcast);
/*      */         }
/*      */       }
/*      */       
/*  893 */       switch (this.broadcastState)
/*      */       {
/*      */       case FindingIngestServer: 
/*  896 */         func_152827_a(BroadcastState.LoggingIn);
/*  897 */         errorcode = this.field_152873_i.login(this.authenticationToken);
/*      */         
/*  899 */         if (ErrorCode.failed(errorcode))
/*      */         {
/*  901 */           String s3 = ErrorCode.getString(errorcode);
/*  902 */           func_152820_d(String.format("Error in TTV_Login: %s\n", new Object[] { s3 }));
/*      */         }
/*      */         
/*  905 */         break;
/*      */       
/*      */       case Initialized: 
/*  908 */         func_152827_a(BroadcastState.FindingIngestServer);
/*  909 */         errorcode = this.field_152873_i.getIngestServers(this.authenticationToken);
/*      */         
/*  911 */         if (ErrorCode.failed(errorcode))
/*      */         {
/*  913 */           func_152827_a(BroadcastState.LoggedIn);
/*  914 */           String s2 = ErrorCode.getString(errorcode);
/*  915 */           func_152820_d(String.format("Error in TTV_GetIngestServers: %s\n", new Object[] { s2 }));
/*      */         }
/*      */         
/*  918 */         break;
/*      */       
/*      */       case LoggingIn: 
/*  921 */         func_152827_a(BroadcastState.ReadyToBroadcast);
/*  922 */         errorcode = this.field_152873_i.getUserInfo(this.authenticationToken);
/*      */         
/*  924 */         if (ErrorCode.failed(errorcode))
/*      */         {
/*  926 */           String s = ErrorCode.getString(errorcode);
/*  927 */           func_152820_d(String.format("Error in TTV_GetUserInfo: %s\n", new Object[] { s }));
/*      */         }
/*      */         
/*  930 */         func_152835_I();
/*  931 */         errorcode = this.field_152873_i.getArchivingState(this.authenticationToken);
/*      */         
/*  933 */         if (ErrorCode.failed(errorcode))
/*      */         {
/*  935 */           String s1 = ErrorCode.getString(errorcode);
/*  936 */           func_152820_d(String.format("Error in TTV_GetArchivingState: %s\n", new Object[] { s1 }));
/*      */         }
/*      */         break;
/*      */       case Authenticated: 
/*      */       case Authenticating: 
/*      */       case Broadcasting: 
/*      */       case IngestTesting: 
/*      */       case LoggedIn: 
/*      */       case Paused: 
/*      */       case ReadyToBroadcast: 
/*      */       case Starting: 
/*      */       case Uninitialized: 
/*      */       default: 
/*      */         break;
/*      */       case ReceivedIngestServers: case Stopping: 
/*  951 */         func_152835_I();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void func_152835_I()
/*      */   {
/*  958 */     long i = System.nanoTime();
/*  959 */     long j = (i - this.field_152890_z) / 1000000000L;
/*      */     
/*  961 */     if (j >= 30L)
/*      */     {
/*  963 */       this.field_152890_z = i;
/*  964 */       ErrorCode errorcode = this.field_152873_i.getStreamInfo(this.authenticationToken, this.field_152880_p);
/*      */       
/*  966 */       if (ErrorCode.failed(errorcode))
/*      */       {
/*  968 */         String s = ErrorCode.getString(errorcode);
/*  969 */         func_152820_d(String.format("Error in TTV_GetStreamInfo: %s", new Object[] { s }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public IngestServerTester func_152838_J()
/*      */   {
/*  976 */     if ((isReadyToBroadcast()) && (this.ingestList != null))
/*      */     {
/*  978 */       if (isIngestTesting())
/*      */       {
/*  980 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  984 */       this.field_152860_A = new IngestServerTester(this.field_152873_i, this.ingestList);
/*  985 */       this.field_152860_A.func_176004_j();
/*  986 */       func_152827_a(BroadcastState.IngestTesting);
/*  987 */       return this.field_152860_A;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  992 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean func_152823_L()
/*      */   {
/*  998 */     for (int i = 0; i < 3; i++)
/*      */     {
/* 1000 */       FrameBuffer framebuffer = this.field_152873_i.allocateFrameBuffer(this.videoParamaters.outputWidth * this.videoParamaters.outputHeight * 4);
/*      */       
/* 1002 */       if (!framebuffer.getIsValid())
/*      */       {
/* 1004 */         func_152820_d(String.format("Error while allocating frame buffer", new Object[0]));
/* 1005 */         return false;
/*      */       }
/*      */       
/* 1008 */       this.field_152874_j.add(framebuffer);
/* 1009 */       this.field_152875_k.add(framebuffer);
/*      */     }
/*      */     
/* 1012 */     return true;
/*      */   }
/*      */   
/*      */   protected void func_152831_M()
/*      */   {
/* 1017 */     for (int i = 0; i < this.field_152874_j.size(); i++)
/*      */     {
/* 1019 */       FrameBuffer framebuffer = (FrameBuffer)this.field_152874_j.get(i);
/* 1020 */       framebuffer.free();
/*      */     }
/*      */     
/* 1023 */     this.field_152875_k.clear();
/* 1024 */     this.field_152874_j.clear();
/*      */   }
/*      */   
/*      */   public FrameBuffer func_152822_N()
/*      */   {
/* 1029 */     if (this.field_152875_k.size() == 0)
/*      */     {
/* 1031 */       func_152820_d(String.format("Out of free buffers, this should never happen", new Object[0]));
/* 1032 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1036 */     FrameBuffer framebuffer = (FrameBuffer)this.field_152875_k.get(this.field_152875_k.size() - 1);
/* 1037 */     this.field_152875_k.remove(this.field_152875_k.size() - 1);
/* 1038 */     return framebuffer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void captureFramebuffer(FrameBuffer p_152846_1_)
/*      */   {
/*      */     try
/*      */     {
/* 1049 */       this.field_152873_i.captureFrameBuffer_ReadPixels(p_152846_1_);
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 1053 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Trying to submit a frame to Twitch");
/* 1054 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Broadcast State");
/* 1055 */       crashreportcategory.addCrashSection("Last reported errors", Arrays.toString(field_152862_C.func_152756_c()));
/* 1056 */       crashreportcategory.addCrashSection("Buffer", p_152846_1_);
/* 1057 */       crashreportcategory.addCrashSection("Free buffer count", Integer.valueOf(this.field_152875_k.size()));
/* 1058 */       crashreportcategory.addCrashSection("Capture buffer count", Integer.valueOf(this.field_152874_j.size()));
/* 1059 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ErrorCode submitStreamFrame(FrameBuffer p_152859_1_)
/*      */   {
/* 1068 */     if (isBroadcastPaused())
/*      */     {
/* 1070 */       func_152854_G();
/*      */     }
/* 1072 */     else if (!isBroadcasting())
/*      */     {
/* 1074 */       return ErrorCode.TTV_EC_STREAM_NOT_STARTED;
/*      */     }
/*      */     
/* 1077 */     ErrorCode errorcode = this.field_152873_i.submitVideoFrame(p_152859_1_);
/*      */     
/* 1079 */     if (errorcode != ErrorCode.TTV_EC_SUCCESS)
/*      */     {
/* 1081 */       String s = ErrorCode.getString(errorcode);
/*      */       
/* 1083 */       if (ErrorCode.succeeded(errorcode))
/*      */       {
/* 1085 */         func_152832_e(String.format("Warning in SubmitTexturePointer: %s\n", new Object[] { s }));
/*      */       }
/*      */       else
/*      */       {
/* 1089 */         func_152820_d(String.format("Error in SubmitTexturePointer: %s\n", new Object[] { s }));
/* 1090 */         stopBroadcasting();
/*      */       }
/*      */       
/* 1093 */       if (this.broadcastListener != null)
/*      */       {
/* 1095 */         this.broadcastListener.func_152893_b(errorcode);
/*      */       }
/*      */     }
/*      */     
/* 1099 */     return errorcode;
/*      */   }
/*      */   
/*      */   protected boolean func_152853_a(ErrorCode p_152853_1_)
/*      */   {
/* 1104 */     if (ErrorCode.failed(p_152853_1_))
/*      */     {
/* 1106 */       func_152820_d(ErrorCode.getString(p_152853_1_));
/* 1107 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1111 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_152820_d(String p_152820_1_)
/*      */   {
/* 1117 */     this.field_152863_D = p_152820_1_;
/* 1118 */     field_152862_C.func_152757_a("<Error> " + p_152820_1_);
/* 1119 */     logger.error(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[] { p_152820_1_ });
/*      */   }
/*      */   
/*      */   protected void func_152832_e(String p_152832_1_)
/*      */   {
/* 1124 */     field_152862_C.func_152757_a("<Warning> " + p_152832_1_);
/* 1125 */     logger.warn(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[] { p_152832_1_ });
/*      */   }
/*      */   
/*      */   public static abstract interface BroadcastListener
/*      */   {
/*      */     public abstract void func_152900_a(ErrorCode paramErrorCode, AuthToken paramAuthToken);
/*      */     
/*      */     public abstract void func_152897_a(ErrorCode paramErrorCode);
/*      */     
/*      */     public abstract void func_152898_a(ErrorCode paramErrorCode, GameInfo[] paramArrayOfGameInfo);
/*      */     
/*      */     public abstract void func_152891_a(BroadcastController.BroadcastState paramBroadcastState);
/*      */     
/*      */     public abstract void func_152895_a();
/*      */     
/*      */     public abstract void func_152894_a(StreamInfo paramStreamInfo);
/*      */     
/*      */     public abstract void func_152896_a(IngestList paramIngestList);
/*      */     
/*      */     public abstract void func_152893_b(ErrorCode paramErrorCode);
/*      */     
/*      */     public abstract void func_152899_b();
/*      */     
/*      */     public abstract void func_152901_c();
/*      */     
/*      */     public abstract void func_152892_c(ErrorCode paramErrorCode);
/*      */   }
/*      */   
/*      */   public static enum BroadcastState
/*      */   {
/* 1155 */     Uninitialized, 
/* 1156 */     Initialized, 
/* 1157 */     Authenticating, 
/* 1158 */     Authenticated, 
/* 1159 */     LoggingIn, 
/* 1160 */     LoggedIn, 
/* 1161 */     FindingIngestServer, 
/* 1162 */     ReceivedIngestServers, 
/* 1163 */     ReadyToBroadcast, 
/* 1164 */     Starting, 
/* 1165 */     Broadcasting, 
/* 1166 */     Stopping, 
/* 1167 */     Paused, 
/* 1168 */     IngestTesting;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\BroadcastController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */