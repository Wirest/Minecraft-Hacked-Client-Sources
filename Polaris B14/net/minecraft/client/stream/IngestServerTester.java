/*     */ package net.minecraft.client.stream;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import tv.twitch.AuthToken;
/*     */ import tv.twitch.ErrorCode;
/*     */ import tv.twitch.broadcast.ArchivingState;
/*     */ import tv.twitch.broadcast.AudioParams;
/*     */ import tv.twitch.broadcast.ChannelInfo;
/*     */ import tv.twitch.broadcast.EncodingCpuUsage;
/*     */ import tv.twitch.broadcast.FrameBuffer;
/*     */ import tv.twitch.broadcast.GameInfoList;
/*     */ import tv.twitch.broadcast.IStatCallbacks;
/*     */ import tv.twitch.broadcast.IStreamCallbacks;
/*     */ import tv.twitch.broadcast.IngestList;
/*     */ import tv.twitch.broadcast.IngestServer;
/*     */ import tv.twitch.broadcast.PixelFormat;
/*     */ import tv.twitch.broadcast.RTMPState;
/*     */ import tv.twitch.broadcast.StatType;
/*     */ import tv.twitch.broadcast.Stream;
/*     */ import tv.twitch.broadcast.StreamInfo;
/*     */ import tv.twitch.broadcast.UserInfo;
/*     */ import tv.twitch.broadcast.VideoParams;
/*     */ 
/*     */ public class IngestServerTester
/*     */ {
/*  28 */   protected IngestTestListener field_153044_b = null;
/*  29 */   protected Stream field_153045_c = null;
/*  30 */   protected IngestList field_153046_d = null;
/*  31 */   protected IngestTestState field_153047_e = IngestTestState.Uninitalized;
/*  32 */   protected long field_153048_f = 8000L;
/*  33 */   protected long field_153049_g = 2000L;
/*  34 */   protected long field_153050_h = 0L;
/*  35 */   protected RTMPState field_153051_i = RTMPState.Invalid;
/*  36 */   protected VideoParams field_153052_j = null;
/*  37 */   protected AudioParams audioParameters = null;
/*  38 */   protected long field_153054_l = 0L;
/*  39 */   protected List<FrameBuffer> field_153055_m = null;
/*  40 */   protected boolean field_153056_n = false;
/*  41 */   protected IStreamCallbacks field_153057_o = null;
/*  42 */   protected IStatCallbacks field_153058_p = null;
/*  43 */   protected IngestServer field_153059_q = null;
/*  44 */   protected boolean field_153060_r = false;
/*  45 */   protected boolean field_153061_s = false;
/*  46 */   protected int field_153062_t = -1;
/*  47 */   protected int field_153063_u = 0;
/*  48 */   protected long field_153064_v = 0L;
/*  49 */   protected float field_153065_w = 0.0F;
/*  50 */   protected float field_153066_x = 0.0F;
/*  51 */   protected boolean field_176009_x = false;
/*  52 */   protected boolean field_176008_y = false;
/*  53 */   protected boolean field_176007_z = false;
/*  54 */   protected IStreamCallbacks field_176005_A = new IStreamCallbacks()
/*     */   {
/*     */     public void requestAuthTokenCallback(ErrorCode p_requestAuthTokenCallback_1_, AuthToken p_requestAuthTokenCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void loginCallback(ErrorCode p_loginCallback_1_, ChannelInfo p_loginCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void getIngestServersCallback(ErrorCode p_getIngestServersCallback_1_, IngestList p_getIngestServersCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void getUserInfoCallback(ErrorCode p_getUserInfoCallback_1_, UserInfo p_getUserInfoCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void getStreamInfoCallback(ErrorCode p_getStreamInfoCallback_1_, StreamInfo p_getStreamInfoCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void getArchivingStateCallback(ErrorCode p_getArchivingStateCallback_1_, ArchivingState p_getArchivingStateCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void runCommercialCallback(ErrorCode p_runCommercialCallback_1_) {}
/*     */     
/*     */ 
/*     */     public void setStreamInfoCallback(ErrorCode p_setStreamInfoCallback_1_) {}
/*     */     
/*     */ 
/*     */     public void getGameNameListCallback(ErrorCode p_getGameNameListCallback_1_, GameInfoList p_getGameNameListCallback_2_) {}
/*     */     
/*     */ 
/*     */     public void bufferUnlockCallback(long p_bufferUnlockCallback_1_) {}
/*     */     
/*     */ 
/*     */     public void startCallback(ErrorCode p_startCallback_1_)
/*     */     {
/*  88 */       IngestServerTester.this.field_176008_y = false;
/*     */       
/*  90 */       if (ErrorCode.succeeded(p_startCallback_1_))
/*     */       {
/*  92 */         IngestServerTester.this.field_176009_x = true;
/*  93 */         IngestServerTester.this.field_153054_l = System.currentTimeMillis();
/*  94 */         IngestServerTester.this.func_153034_a(IngestServerTester.IngestTestState.ConnectingToServer);
/*     */       }
/*     */       else
/*     */       {
/*  98 */         IngestServerTester.this.field_153056_n = false;
/*  99 */         IngestServerTester.this.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stopCallback(ErrorCode p_stopCallback_1_) {
/* 104 */       if (ErrorCode.failed(p_stopCallback_1_))
/*     */       {
/* 106 */         System.out.println("IngestTester.stopCallback failed to stop - " + IngestServerTester.this.field_153059_q.serverName + ": " + p_stopCallback_1_.toString());
/*     */       }
/*     */       
/* 109 */       IngestServerTester.this.field_176007_z = false;
/* 110 */       IngestServerTester.this.field_176009_x = false;
/* 111 */       IngestServerTester.this.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
/* 112 */       IngestServerTester.this.field_153059_q = null;
/*     */       
/* 114 */       if (IngestServerTester.this.field_153060_r)
/*     */       {
/* 116 */         IngestServerTester.this.func_153034_a(IngestServerTester.IngestTestState.Cancelling);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void sendActionMetaDataCallback(ErrorCode p_sendActionMetaDataCallback_1_) {}
/*     */     
/*     */ 
/*     */     public void sendStartSpanMetaDataCallback(ErrorCode p_sendStartSpanMetaDataCallback_1_) {}
/*     */     
/*     */ 
/*     */     public void sendEndSpanMetaDataCallback(ErrorCode p_sendEndSpanMetaDataCallback_1_) {}
/*     */   };
/* 129 */   protected IStatCallbacks field_176006_B = new IStatCallbacks()
/*     */   {
/*     */     public void statCallback(StatType p_statCallback_1_, long p_statCallback_2_)
/*     */     {
/* 133 */       switch (p_statCallback_1_)
/*     */       {
/*     */       case TTV_ST_RTMPDATASENT: 
/* 136 */         IngestServerTester.this.field_153051_i = RTMPState.lookupValue((int)p_statCallback_2_);
/* 137 */         break;
/*     */       
/*     */       case TTV_ST_RTMPSTATE: 
/* 140 */         IngestServerTester.this.field_153050_h = p_statCallback_2_;
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   public void func_153042_a(IngestTestListener p_153042_1_)
/*     */   {
/* 147 */     this.field_153044_b = p_153042_1_;
/*     */   }
/*     */   
/*     */   public IngestServer func_153040_c()
/*     */   {
/* 152 */     return this.field_153059_q;
/*     */   }
/*     */   
/*     */   public int func_153028_p()
/*     */   {
/* 157 */     return this.field_153062_t;
/*     */   }
/*     */   
/*     */   public boolean func_153032_e()
/*     */   {
/* 162 */     return (this.field_153047_e == IngestTestState.Finished) || (this.field_153047_e == IngestTestState.Cancelled) || (this.field_153047_e == IngestTestState.Failed);
/*     */   }
/*     */   
/*     */   public float func_153030_h()
/*     */   {
/* 167 */     return this.field_153066_x;
/*     */   }
/*     */   
/*     */   public IngestServerTester(Stream p_i1019_1_, IngestList p_i1019_2_)
/*     */   {
/* 172 */     this.field_153045_c = p_i1019_1_;
/* 173 */     this.field_153046_d = p_i1019_2_;
/*     */   }
/*     */   
/*     */   public void func_176004_j()
/*     */   {
/* 178 */     if (this.field_153047_e == IngestTestState.Uninitalized)
/*     */     {
/* 180 */       this.field_153062_t = 0;
/* 181 */       this.field_153060_r = false;
/* 182 */       this.field_153061_s = false;
/* 183 */       this.field_176009_x = false;
/* 184 */       this.field_176008_y = false;
/* 185 */       this.field_176007_z = false;
/* 186 */       this.field_153058_p = this.field_153045_c.getStatCallbacks();
/* 187 */       this.field_153045_c.setStatCallbacks(this.field_176006_B);
/* 188 */       this.field_153057_o = this.field_153045_c.getStreamCallbacks();
/* 189 */       this.field_153045_c.setStreamCallbacks(this.field_176005_A);
/* 190 */       this.field_153052_j = new VideoParams();
/* 191 */       this.field_153052_j.targetFps = 60;
/* 192 */       this.field_153052_j.maxKbps = 3500;
/* 193 */       this.field_153052_j.outputWidth = 1280;
/* 194 */       this.field_153052_j.outputHeight = 720;
/* 195 */       this.field_153052_j.pixelFormat = PixelFormat.TTV_PF_BGRA;
/* 196 */       this.field_153052_j.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
/* 197 */       this.field_153052_j.disableAdaptiveBitrate = true;
/* 198 */       this.field_153052_j.verticalFlip = false;
/* 199 */       this.field_153045_c.getDefaultParams(this.field_153052_j);
/* 200 */       this.audioParameters = new AudioParams();
/* 201 */       this.audioParameters.audioEnabled = false;
/* 202 */       this.audioParameters.enableMicCapture = false;
/* 203 */       this.audioParameters.enablePlaybackCapture = false;
/* 204 */       this.audioParameters.enablePassthroughAudio = false;
/* 205 */       this.field_153055_m = Lists.newArrayList();
/* 206 */       int i = 3;
/*     */       
/* 208 */       for (int j = 0; j < i; j++)
/*     */       {
/* 210 */         FrameBuffer framebuffer = this.field_153045_c.allocateFrameBuffer(this.field_153052_j.outputWidth * this.field_153052_j.outputHeight * 4);
/*     */         
/* 212 */         if (!framebuffer.getIsValid())
/*     */         {
/* 214 */           func_153031_o();
/* 215 */           func_153034_a(IngestTestState.Failed);
/* 216 */           return;
/*     */         }
/*     */         
/* 219 */         this.field_153055_m.add(framebuffer);
/* 220 */         this.field_153045_c.randomizeFrameBuffer(framebuffer);
/*     */       }
/*     */       
/* 223 */       func_153034_a(IngestTestState.Starting);
/* 224 */       this.field_153054_l = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_153041_j()
/*     */   {
/* 231 */     if ((!func_153032_e()) && (this.field_153047_e != IngestTestState.Uninitalized))
/*     */     {
/* 233 */       if ((!this.field_176008_y) && (!this.field_176007_z))
/*     */       {
/* 235 */         switch (this.field_153047_e)
/*     */         {
/*     */         case Cancelling: 
/*     */         case Failed: 
/* 239 */           if (this.field_153059_q != null)
/*     */           {
/* 241 */             if ((this.field_153061_s) || (!this.field_153056_n))
/*     */             {
/* 243 */               this.field_153059_q.bitrateKbps = 0.0F;
/*     */             }
/*     */             
/* 246 */             func_153035_b(this.field_153059_q);
/*     */           }
/*     */           else
/*     */           {
/* 250 */             this.field_153054_l = 0L;
/* 251 */             this.field_153061_s = false;
/* 252 */             this.field_153056_n = true;
/*     */             
/* 254 */             if (this.field_153047_e != IngestTestState.Starting)
/*     */             {
/* 256 */               this.field_153062_t += 1;
/*     */             }
/*     */             
/* 259 */             if (this.field_153062_t < this.field_153046_d.getServers().length)
/*     */             {
/* 261 */               this.field_153059_q = this.field_153046_d.getServers()[this.field_153062_t];
/* 262 */               func_153036_a(this.field_153059_q);
/*     */             }
/*     */             else
/*     */             {
/* 266 */               func_153034_a(IngestTestState.Finished);
/*     */             }
/*     */           }
/*     */           
/* 270 */           break;
/*     */         
/*     */         case ConnectingToServer: 
/*     */         case DoneTestingServer: 
/* 274 */           func_153029_c(this.field_153059_q);
/* 275 */           break;
/*     */         
/*     */         case Starting: 
/* 278 */           func_153034_a(IngestTestState.Cancelled);
/*     */         }
/*     */         
/* 281 */         func_153038_n();
/*     */         
/* 283 */         if ((this.field_153047_e == IngestTestState.Cancelled) || (this.field_153047_e == IngestTestState.Finished))
/*     */         {
/* 285 */           func_153031_o();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_153039_l()
/*     */   {
/* 293 */     if ((!func_153032_e()) && (!this.field_153060_r))
/*     */     {
/* 295 */       this.field_153060_r = true;
/*     */       
/* 297 */       if (this.field_153059_q != null)
/*     */       {
/* 299 */         this.field_153059_q.bitrateKbps = 0.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean func_153036_a(IngestServer p_153036_1_)
/*     */   {
/* 306 */     this.field_153056_n = true;
/* 307 */     this.field_153050_h = 0L;
/* 308 */     this.field_153051_i = RTMPState.Idle;
/* 309 */     this.field_153059_q = p_153036_1_;
/* 310 */     this.field_176008_y = true;
/* 311 */     func_153034_a(IngestTestState.ConnectingToServer);
/* 312 */     ErrorCode errorcode = this.field_153045_c.start(this.field_153052_j, this.audioParameters, p_153036_1_, tv.twitch.broadcast.StartFlags.TTV_Start_BandwidthTest, true);
/*     */     
/* 314 */     if (ErrorCode.failed(errorcode))
/*     */     {
/* 316 */       this.field_176008_y = false;
/* 317 */       this.field_153056_n = false;
/* 318 */       func_153034_a(IngestTestState.DoneTestingServer);
/* 319 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 323 */     this.field_153064_v = this.field_153050_h;
/* 324 */     p_153036_1_.bitrateKbps = 0.0F;
/* 325 */     this.field_153063_u = 0;
/* 326 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_153035_b(IngestServer p_153035_1_)
/*     */   {
/* 332 */     if (this.field_176008_y)
/*     */     {
/* 334 */       this.field_153061_s = true;
/*     */     }
/* 336 */     else if (this.field_176009_x)
/*     */     {
/* 338 */       this.field_176007_z = true;
/* 339 */       ErrorCode errorcode = this.field_153045_c.stop(true);
/*     */       
/* 341 */       if (ErrorCode.failed(errorcode))
/*     */       {
/* 343 */         this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
/* 344 */         System.out.println("Stop failed: " + errorcode.toString());
/*     */       }
/*     */       
/* 347 */       this.field_153045_c.pollStats();
/*     */     }
/*     */     else
/*     */     {
/* 351 */       this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
/*     */     }
/*     */   }
/*     */   
/*     */   protected long func_153037_m()
/*     */   {
/* 357 */     return System.currentTimeMillis() - this.field_153054_l;
/*     */   }
/*     */   
/*     */   protected void func_153038_n()
/*     */   {
/* 362 */     float f = (float)func_153037_m();
/*     */     
/* 364 */     switch (this.field_153047_e)
/*     */     {
/*     */     case Cancelled: 
/*     */     case Cancelling: 
/*     */     case ConnectingToServer: 
/*     */     case Finished: 
/*     */     case TestingServer: 
/*     */     case Uninitalized: 
/* 372 */       this.field_153066_x = 0.0F;
/* 373 */       break;
/*     */     
/*     */     case Failed: 
/* 376 */       this.field_153066_x = 1.0F;
/* 377 */       break;
/*     */     
/*     */     case DoneTestingServer: 
/*     */     case Starting: 
/*     */     default: 
/* 382 */       this.field_153066_x = (f / (float)this.field_153048_f);
/*     */     }
/*     */     
/* 385 */     switch (this.field_153047_e)
/*     */     {
/*     */     case Finished: 
/*     */     case TestingServer: 
/*     */     case Uninitalized: 
/* 390 */       this.field_153065_w = 1.0F;
/* 391 */       break;
/*     */     case Starting: 
/*     */     default: 
/* 394 */       this.field_153065_w = (this.field_153062_t / this.field_153046_d.getServers().length);
/* 395 */       this.field_153065_w += this.field_153066_x / this.field_153046_d.getServers().length;
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean func_153029_c(IngestServer p_153029_1_)
/*     */   {
/* 401 */     if ((!this.field_153061_s) && (!this.field_153060_r) && (func_153037_m() < this.field_153048_f))
/*     */     {
/* 403 */       if ((!this.field_176008_y) && (!this.field_176007_z))
/*     */       {
/* 405 */         ErrorCode errorcode = this.field_153045_c.submitVideoFrame((FrameBuffer)this.field_153055_m.get(this.field_153063_u));
/*     */         
/* 407 */         if (ErrorCode.failed(errorcode))
/*     */         {
/* 409 */           this.field_153056_n = false;
/* 410 */           func_153034_a(IngestTestState.DoneTestingServer);
/* 411 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 415 */         this.field_153063_u = ((this.field_153063_u + 1) % this.field_153055_m.size());
/* 416 */         this.field_153045_c.pollStats();
/*     */         
/* 418 */         if (this.field_153051_i == RTMPState.SendVideo)
/*     */         {
/* 420 */           func_153034_a(IngestTestState.TestingServer);
/* 421 */           long i = func_153037_m();
/*     */           
/* 423 */           if ((i > 0L) && (this.field_153050_h > this.field_153064_v))
/*     */           {
/* 425 */             p_153029_1_.bitrateKbps = ((float)(this.field_153050_h * 8L) / (float)func_153037_m());
/* 426 */             this.field_153064_v = this.field_153050_h;
/*     */           }
/*     */         }
/*     */         
/* 430 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 435 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 440 */     func_153034_a(IngestTestState.DoneTestingServer);
/* 441 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_153031_o()
/*     */   {
/* 447 */     this.field_153059_q = null;
/*     */     
/* 449 */     if (this.field_153055_m != null)
/*     */     {
/* 451 */       for (int i = 0; i < this.field_153055_m.size(); i++)
/*     */       {
/* 453 */         ((FrameBuffer)this.field_153055_m.get(i)).free();
/*     */       }
/*     */       
/* 456 */       this.field_153055_m = null;
/*     */     }
/*     */     
/* 459 */     if (this.field_153045_c.getStatCallbacks() == this.field_176006_B)
/*     */     {
/* 461 */       this.field_153045_c.setStatCallbacks(this.field_153058_p);
/* 462 */       this.field_153058_p = null;
/*     */     }
/*     */     
/* 465 */     if (this.field_153045_c.getStreamCallbacks() == this.field_176005_A)
/*     */     {
/* 467 */       this.field_153045_c.setStreamCallbacks(this.field_153057_o);
/* 468 */       this.field_153057_o = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_153034_a(IngestTestState p_153034_1_)
/*     */   {
/* 474 */     if (p_153034_1_ != this.field_153047_e)
/*     */     {
/* 476 */       this.field_153047_e = p_153034_1_;
/*     */       
/* 478 */       if (this.field_153044_b != null)
/*     */       {
/* 480 */         this.field_153044_b.func_152907_a(this, p_153034_1_);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface IngestTestListener
/*     */   {
/*     */     public abstract void func_152907_a(IngestServerTester paramIngestServerTester, IngestServerTester.IngestTestState paramIngestTestState);
/*     */   }
/*     */   
/*     */   public static enum IngestTestState
/*     */   {
/* 492 */     Uninitalized, 
/* 493 */     Starting, 
/* 494 */     ConnectingToServer, 
/* 495 */     TestingServer, 
/* 496 */     DoneTestingServer, 
/* 497 */     Finished, 
/* 498 */     Cancelling, 
/* 499 */     Cancelled, 
/* 500 */     Failed;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\IngestServerTester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */