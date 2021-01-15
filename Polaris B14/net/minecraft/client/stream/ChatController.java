/*     */ package net.minecraft.client.stream;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import tv.twitch.AuthToken;
/*     */ import tv.twitch.Core;
/*     */ import tv.twitch.ErrorCode;
/*     */ import tv.twitch.StandardCoreAPI;
/*     */ import tv.twitch.chat.Chat;
/*     */ import tv.twitch.chat.ChatBadgeData;
/*     */ import tv.twitch.chat.ChatChannelInfo;
/*     */ import tv.twitch.chat.ChatEmoticonData;
/*     */ import tv.twitch.chat.ChatEvent;
/*     */ import tv.twitch.chat.ChatRawMessage;
/*     */ import tv.twitch.chat.ChatTokenizationOption;
/*     */ import tv.twitch.chat.ChatTokenizedMessage;
/*     */ import tv.twitch.chat.ChatUserInfo;
/*     */ import tv.twitch.chat.IChatAPIListener;
/*     */ import tv.twitch.chat.IChatChannelListener;
/*     */ import tv.twitch.chat.StandardChatAPI;
/*     */ 
/*     */ public class ChatController
/*     */ {
/*  30 */   private static final Logger LOGGER = ;
/*  31 */   protected ChatListener field_153003_a = null;
/*  32 */   protected String field_153004_b = "";
/*  33 */   protected String field_153006_d = "";
/*  34 */   protected String field_153007_e = "";
/*  35 */   protected Core field_175992_e = null;
/*  36 */   protected Chat field_153008_f = null;
/*  37 */   protected ChatState field_153011_i = ChatState.Uninitialized;
/*  38 */   protected AuthToken field_153012_j = new AuthToken();
/*  39 */   protected HashMap<String, ChatChannelListener> field_175998_i = new HashMap();
/*  40 */   protected int field_153015_m = 128;
/*  41 */   protected EnumEmoticonMode field_175997_k = EnumEmoticonMode.None;
/*  42 */   protected EnumEmoticonMode field_175995_l = EnumEmoticonMode.None;
/*  43 */   protected ChatEmoticonData field_175996_m = null;
/*  44 */   protected int field_175993_n = 500;
/*  45 */   protected int field_175994_o = 2000;
/*  46 */   protected IChatAPIListener field_175999_p = new IChatAPIListener()
/*     */   {
/*     */     public void chatInitializationCallback(ErrorCode p_chatInitializationCallback_1_)
/*     */     {
/*  50 */       if (ErrorCode.succeeded(p_chatInitializationCallback_1_))
/*     */       {
/*  52 */         ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
/*  53 */         ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
/*  54 */         ChatController.this.func_153001_r();
/*  55 */         ChatController.this.func_175985_a(ChatController.ChatState.Initialized);
/*     */       }
/*     */       else
/*     */       {
/*  59 */         ChatController.this.func_175985_a(ChatController.ChatState.Uninitialized);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  64 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/*  66 */           ChatController.this.field_153003_a.func_176023_d(p_chatInitializationCallback_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/*  71 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatShutdownCallback(ErrorCode p_chatShutdownCallback_1_) {
/*  76 */       if (ErrorCode.succeeded(p_chatShutdownCallback_1_))
/*     */       {
/*  78 */         ErrorCode errorcode = ChatController.this.field_175992_e.shutdown();
/*     */         
/*  80 */         if (ErrorCode.failed(errorcode))
/*     */         {
/*  82 */           String s = ErrorCode.getString(errorcode);
/*  83 */           ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", new Object[] { s }));
/*     */         }
/*     */         
/*  86 */         ChatController.this.func_175985_a(ChatController.ChatState.Uninitialized);
/*     */       }
/*     */       else
/*     */       {
/*  90 */         ChatController.this.func_175985_a(ChatController.ChatState.Initialized);
/*  91 */         ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", new Object[] { p_chatShutdownCallback_1_ }));
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  96 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/*  98 */           ChatController.this.field_153003_a.func_176022_e(p_chatShutdownCallback_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 103 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatEmoticonDataDownloadCallback(ErrorCode p_chatEmoticonDataDownloadCallback_1_) {
/* 108 */       if (ErrorCode.succeeded(p_chatEmoticonDataDownloadCallback_1_))
/*     */       {
/* 110 */         ChatController.this.func_152988_s();
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   public void func_152990_a(ChatListener p_152990_1_)
/*     */   {
/* 117 */     this.field_153003_a = p_152990_1_;
/*     */   }
/*     */   
/*     */   public void func_152994_a(AuthToken p_152994_1_)
/*     */   {
/* 122 */     this.field_153012_j = p_152994_1_;
/*     */   }
/*     */   
/*     */   public void func_152984_a(String p_152984_1_)
/*     */   {
/* 127 */     this.field_153006_d = p_152984_1_;
/*     */   }
/*     */   
/*     */   public void func_152998_c(String p_152998_1_)
/*     */   {
/* 132 */     this.field_153004_b = p_152998_1_;
/*     */   }
/*     */   
/*     */   public ChatState func_153000_j()
/*     */   {
/* 137 */     return this.field_153011_i;
/*     */   }
/*     */   
/*     */   public boolean func_175990_d(String p_175990_1_)
/*     */   {
/* 142 */     if (!this.field_175998_i.containsKey(p_175990_1_))
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 148 */     ChatChannelListener chatcontroller$chatchannellistener = (ChatChannelListener)this.field_175998_i.get(p_175990_1_);
/* 149 */     return chatcontroller$chatchannellistener.func_176040_a() == EnumChannelState.Connected;
/*     */   }
/*     */   
/*     */ 
/*     */   public EnumChannelState func_175989_e(String p_175989_1_)
/*     */   {
/* 155 */     if (!this.field_175998_i.containsKey(p_175989_1_))
/*     */     {
/* 157 */       return EnumChannelState.Disconnected;
/*     */     }
/*     */     
/*     */ 
/* 161 */     ChatChannelListener chatcontroller$chatchannellistener = (ChatChannelListener)this.field_175998_i.get(p_175989_1_);
/* 162 */     return chatcontroller$chatchannellistener.func_176040_a();
/*     */   }
/*     */   
/*     */ 
/*     */   public ChatController()
/*     */   {
/* 168 */     this.field_175992_e = Core.getInstance();
/*     */     
/* 170 */     if (this.field_175992_e == null)
/*     */     {
/* 172 */       this.field_175992_e = new Core(new StandardCoreAPI());
/*     */     }
/*     */     
/* 175 */     this.field_153008_f = new Chat(new StandardChatAPI());
/*     */   }
/*     */   
/*     */   public boolean func_175984_n()
/*     */   {
/* 180 */     if (this.field_153011_i != ChatState.Uninitialized)
/*     */     {
/* 182 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 186 */     func_175985_a(ChatState.Initializing);
/* 187 */     ErrorCode errorcode = this.field_175992_e.initialize(this.field_153006_d, null);
/*     */     
/* 189 */     if (ErrorCode.failed(errorcode))
/*     */     {
/* 191 */       func_175985_a(ChatState.Uninitialized);
/* 192 */       String s1 = ErrorCode.getString(errorcode);
/* 193 */       func_152995_h(String.format("Error initializing Twitch sdk: %s", new Object[] { s1 }));
/* 194 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 198 */     this.field_175995_l = this.field_175997_k;
/* 199 */     HashSet<ChatTokenizationOption> hashset = new HashSet();
/*     */     
/* 201 */     switch (this.field_175997_k)
/*     */     {
/*     */     case None: 
/* 204 */       hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
/* 205 */       break;
/*     */     
/*     */     case TextureAtlas: 
/* 208 */       hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
/* 209 */       break;
/*     */     
/*     */     case Url: 
/* 212 */       hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
/*     */     }
/*     */     
/* 215 */     errorcode = this.field_153008_f.initialize(hashset, this.field_175999_p);
/*     */     
/* 217 */     if (ErrorCode.failed(errorcode))
/*     */     {
/* 219 */       this.field_175992_e.shutdown();
/* 220 */       func_175985_a(ChatState.Uninitialized);
/* 221 */       String s = ErrorCode.getString(errorcode);
/* 222 */       func_152995_h(String.format("Error initializing Twitch chat: %s", new Object[] { s }));
/* 223 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 227 */     func_175985_a(ChatState.Initialized);
/* 228 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean func_152986_d(String p_152986_1_)
/*     */   {
/* 236 */     return func_175987_a(p_152986_1_, false);
/*     */   }
/*     */   
/*     */   protected boolean func_175987_a(String p_175987_1_, boolean p_175987_2_)
/*     */   {
/* 241 */     if (this.field_153011_i != ChatState.Initialized)
/*     */     {
/* 243 */       return false;
/*     */     }
/* 245 */     if (this.field_175998_i.containsKey(p_175987_1_))
/*     */     {
/* 247 */       func_152995_h("Already in channel: " + p_175987_1_);
/* 248 */       return false;
/*     */     }
/* 250 */     if ((p_175987_1_ != null) && (!p_175987_1_.equals("")))
/*     */     {
/* 252 */       ChatChannelListener chatcontroller$chatchannellistener = new ChatChannelListener(p_175987_1_);
/* 253 */       this.field_175998_i.put(p_175987_1_, chatcontroller$chatchannellistener);
/* 254 */       boolean flag = chatcontroller$chatchannellistener.func_176038_a(p_175987_2_);
/*     */       
/* 256 */       if (!flag)
/*     */       {
/* 258 */         this.field_175998_i.remove(p_175987_1_);
/*     */       }
/*     */       
/* 261 */       return flag;
/*     */     }
/*     */     
/*     */ 
/* 265 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_175991_l(String p_175991_1_)
/*     */   {
/* 271 */     if (this.field_153011_i != ChatState.Initialized)
/*     */     {
/* 273 */       return false;
/*     */     }
/* 275 */     if (!this.field_175998_i.containsKey(p_175991_1_))
/*     */     {
/* 277 */       func_152995_h("Not in channel: " + p_175991_1_);
/* 278 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 282 */     ChatChannelListener chatcontroller$chatchannellistener = (ChatChannelListener)this.field_175998_i.get(p_175991_1_);
/* 283 */     return chatcontroller$chatchannellistener.func_176034_g();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_152993_m()
/*     */   {
/* 289 */     if (this.field_153011_i != ChatState.Initialized)
/*     */     {
/* 291 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 295 */     ErrorCode errorcode = this.field_153008_f.shutdown();
/*     */     
/* 297 */     if (ErrorCode.failed(errorcode))
/*     */     {
/* 299 */       String s = ErrorCode.getString(errorcode);
/* 300 */       func_152995_h(String.format("Error shutting down chat: %s", new Object[] { s }));
/* 301 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 305 */     func_152996_t();
/* 306 */     func_175985_a(ChatState.ShuttingDown);
/* 307 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_175988_p()
/*     */   {
/* 314 */     if (func_153000_j() != ChatState.Uninitialized)
/*     */     {
/* 316 */       func_152993_m();
/*     */       
/* 318 */       if (func_153000_j() == ChatState.ShuttingDown)
/*     */       {
/* 320 */         while (func_153000_j() != ChatState.Uninitialized)
/*     */         {
/*     */           try
/*     */           {
/* 324 */             Thread.sleep(200L);
/* 325 */             func_152997_n();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException) {}
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_152997_n()
/*     */   {
/* 338 */     if (this.field_153011_i != ChatState.Uninitialized)
/*     */     {
/* 340 */       ErrorCode errorcode = this.field_153008_f.flushEvents();
/*     */       
/* 342 */       if (ErrorCode.failed(errorcode))
/*     */       {
/* 344 */         String s = ErrorCode.getString(errorcode);
/* 345 */         func_152995_h(String.format("Error flushing chat events: %s", new Object[] { s }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_175986_a(String p_175986_1_, String p_175986_2_)
/*     */   {
/* 352 */     if (this.field_153011_i != ChatState.Initialized)
/*     */     {
/* 354 */       return false;
/*     */     }
/* 356 */     if (!this.field_175998_i.containsKey(p_175986_1_))
/*     */     {
/* 358 */       func_152995_h("Not in channel: " + p_175986_1_);
/* 359 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 363 */     ChatChannelListener chatcontroller$chatchannellistener = (ChatChannelListener)this.field_175998_i.get(p_175986_1_);
/* 364 */     return chatcontroller$chatchannellistener.func_176037_b(p_175986_2_);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_175985_a(ChatState p_175985_1_)
/*     */   {
/* 370 */     if (p_175985_1_ != this.field_153011_i)
/*     */     {
/* 372 */       this.field_153011_i = p_175985_1_;
/*     */       
/*     */       try
/*     */       {
/* 376 */         if (this.field_153003_a != null)
/*     */         {
/* 378 */           this.field_153003_a.func_176017_a(p_175985_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 383 */         func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_153001_r()
/*     */   {
/* 390 */     if (this.field_175995_l != EnumEmoticonMode.None)
/*     */     {
/* 392 */       if (this.field_175996_m == null)
/*     */       {
/* 394 */         ErrorCode errorcode = this.field_153008_f.downloadEmoticonData();
/*     */         
/* 396 */         if (ErrorCode.failed(errorcode))
/*     */         {
/* 398 */           String s = ErrorCode.getString(errorcode);
/* 399 */           func_152995_h(String.format("Error trying to download emoticon data: %s", new Object[] { s }));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_152988_s()
/*     */   {
/* 407 */     if (this.field_175996_m == null)
/*     */     {
/* 409 */       this.field_175996_m = new ChatEmoticonData();
/* 410 */       ErrorCode errorcode = this.field_153008_f.getEmoticonData(this.field_175996_m);
/*     */       
/* 412 */       if (ErrorCode.succeeded(errorcode))
/*     */       {
/*     */         try
/*     */         {
/* 416 */           if (this.field_153003_a == null)
/*     */             return;
/* 418 */           this.field_153003_a.func_176021_d();
/*     */ 
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 423 */           func_152995_h(exception.toString());
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 428 */         func_152995_h("Error preparing emoticon data: " + ErrorCode.getString(errorcode));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_152996_t()
/*     */   {
/* 435 */     if (this.field_175996_m != null)
/*     */     {
/* 437 */       ErrorCode errorcode = this.field_153008_f.clearEmoticonData();
/*     */       
/* 439 */       if (ErrorCode.succeeded(errorcode))
/*     */       {
/* 441 */         this.field_175996_m = null;
/*     */         
/*     */         try
/*     */         {
/* 445 */           if (this.field_153003_a == null)
/*     */             return;
/* 447 */           this.field_153003_a.func_176024_e();
/*     */ 
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 452 */           func_152995_h(exception.toString());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 457 */         func_152995_h("Error clearing emoticon data: " + ErrorCode.getString(errorcode));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_152995_h(String p_152995_1_)
/*     */   {
/* 464 */     LOGGER.error(TwitchStream.STREAM_MARKER, "[Chat controller] {}", new Object[] { p_152995_1_ });
/*     */   }
/*     */   
/*     */   public class ChatChannelListener implements IChatChannelListener
/*     */   {
/* 469 */     protected String field_176048_a = null;
/* 470 */     protected boolean field_176046_b = false;
/* 471 */     protected ChatController.EnumChannelState field_176047_c = ChatController.EnumChannelState.Created;
/* 472 */     protected List<ChatUserInfo> field_176044_d = Lists.newArrayList();
/* 473 */     protected LinkedList<ChatRawMessage> field_176045_e = new LinkedList();
/* 474 */     protected LinkedList<ChatTokenizedMessage> field_176042_f = new LinkedList();
/* 475 */     protected ChatBadgeData field_176043_g = null;
/*     */     
/*     */     public ChatChannelListener(String p_i46061_2_)
/*     */     {
/* 479 */       this.field_176048_a = p_i46061_2_;
/*     */     }
/*     */     
/*     */     public ChatController.EnumChannelState func_176040_a()
/*     */     {
/* 484 */       return this.field_176047_c;
/*     */     }
/*     */     
/*     */     public boolean func_176038_a(boolean p_176038_1_)
/*     */     {
/* 489 */       this.field_176046_b = p_176038_1_;
/* 490 */       ErrorCode errorcode = ErrorCode.TTV_EC_SUCCESS;
/*     */       
/* 492 */       if (p_176038_1_)
/*     */       {
/* 494 */         errorcode = ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, this);
/*     */       }
/*     */       else
/*     */       {
/* 498 */         errorcode = ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, this);
/*     */       }
/*     */       
/* 501 */       if (ErrorCode.failed(errorcode))
/*     */       {
/* 503 */         String s = ErrorCode.getString(errorcode);
/* 504 */         ChatController.this.func_152995_h(String.format("Error connecting: %s", new Object[] { s }));
/* 505 */         func_176036_d(this.field_176048_a);
/* 506 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 510 */       func_176035_a(ChatController.EnumChannelState.Connecting);
/* 511 */       func_176041_h();
/* 512 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean func_176034_g()
/*     */     {
/* 518 */       switch (this.field_176047_c)
/*     */       {
/*     */       case Connecting: 
/*     */       case Created: 
/* 522 */         ErrorCode errorcode = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
/*     */         
/* 524 */         if (ErrorCode.failed(errorcode))
/*     */         {
/* 526 */           String s = ErrorCode.getString(errorcode);
/* 527 */           ChatController.this.func_152995_h(String.format("Error disconnecting: %s", new Object[] { s }));
/* 528 */           return false;
/*     */         }
/*     */         
/* 531 */         func_176035_a(ChatController.EnumChannelState.Disconnecting);
/* 532 */         return true;
/*     */       }
/*     */       
/*     */       
/*     */ 
/*     */ 
/* 538 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     protected void func_176035_a(ChatController.EnumChannelState p_176035_1_)
/*     */     {
/* 544 */       if (p_176035_1_ != this.field_176047_c)
/*     */       {
/* 546 */         this.field_176047_c = p_176035_1_;
/*     */       }
/*     */     }
/*     */     
/*     */     public void func_176032_a(String p_176032_1_)
/*     */     {
/* 552 */       if (ChatController.this.field_175995_l == ChatController.EnumEmoticonMode.None)
/*     */       {
/* 554 */         this.field_176045_e.clear();
/* 555 */         this.field_176042_f.clear();
/*     */       }
/*     */       else
/*     */       {
/* 559 */         if (this.field_176045_e.size() > 0)
/*     */         {
/* 561 */           ListIterator<ChatRawMessage> listiterator = this.field_176045_e.listIterator();
/*     */           
/* 563 */           while (listiterator.hasNext())
/*     */           {
/* 565 */             ChatRawMessage chatrawmessage = (ChatRawMessage)listiterator.next();
/*     */             
/* 567 */             if (chatrawmessage.userName.equals(p_176032_1_))
/*     */             {
/* 569 */               listiterator.remove();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 574 */         if (this.field_176042_f.size() > 0)
/*     */         {
/* 576 */           ListIterator<ChatTokenizedMessage> listiterator1 = this.field_176042_f.listIterator();
/*     */           
/* 578 */           while (listiterator1.hasNext())
/*     */           {
/* 580 */             ChatTokenizedMessage chattokenizedmessage = (ChatTokenizedMessage)listiterator1.next();
/*     */             
/* 582 */             if (chattokenizedmessage.displayName.equals(p_176032_1_))
/*     */             {
/* 584 */               listiterator1.remove();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 592 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 594 */           ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, p_176032_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 599 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean func_176037_b(String p_176037_1_)
/*     */     {
/* 605 */       if (this.field_176047_c != ChatController.EnumChannelState.Connected)
/*     */       {
/* 607 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 611 */       ErrorCode errorcode = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, p_176037_1_);
/*     */       
/* 613 */       if (ErrorCode.failed(errorcode))
/*     */       {
/* 615 */         String s = ErrorCode.getString(errorcode);
/* 616 */         ChatController.this.func_152995_h(String.format("Error sending chat message: %s", new Object[] { s }));
/* 617 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 621 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void func_176041_h()
/*     */     {
/* 628 */       if (ChatController.this.field_175995_l != ChatController.EnumEmoticonMode.None)
/*     */       {
/* 630 */         if (this.field_176043_g == null)
/*     */         {
/* 632 */           ErrorCode errorcode = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a);
/*     */           
/* 634 */           if (ErrorCode.failed(errorcode))
/*     */           {
/* 636 */             String s = ErrorCode.getString(errorcode);
/* 637 */             ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", new Object[] { s }));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_176039_i()
/*     */     {
/* 645 */       if (this.field_176043_g == null)
/*     */       {
/* 647 */         this.field_176043_g = new ChatBadgeData();
/* 648 */         ErrorCode errorcode = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
/*     */         
/* 650 */         if (ErrorCode.succeeded(errorcode))
/*     */         {
/*     */           try
/*     */           {
/* 654 */             if (ChatController.this.field_153003_a == null)
/*     */               return;
/* 656 */             ChatController.this.field_153003_a.func_176016_c(this.field_176048_a);
/*     */ 
/*     */           }
/*     */           catch (Exception exception)
/*     */           {
/* 661 */             ChatController.this.func_152995_h(exception.toString());
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 666 */           ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString(errorcode));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_176033_j()
/*     */     {
/* 673 */       if (this.field_176043_g != null)
/*     */       {
/* 675 */         ErrorCode errorcode = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
/*     */         
/* 677 */         if (ErrorCode.succeeded(errorcode))
/*     */         {
/* 679 */           this.field_176043_g = null;
/*     */           
/*     */           try
/*     */           {
/* 683 */             if (ChatController.this.field_153003_a == null)
/*     */               return;
/* 685 */             ChatController.this.field_153003_a.func_176020_d(this.field_176048_a);
/*     */ 
/*     */           }
/*     */           catch (Exception exception)
/*     */           {
/* 690 */             ChatController.this.func_152995_h(exception.toString());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 695 */           ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString(errorcode));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_176031_c(String p_176031_1_)
/*     */     {
/*     */       try
/*     */       {
/* 704 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 706 */           ChatController.this.field_153003_a.func_180606_a(p_176031_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 711 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_176036_d(String p_176036_1_)
/*     */     {
/*     */       try
/*     */       {
/* 719 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 721 */           ChatController.this.field_153003_a.func_180607_b(p_176036_1_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 726 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_176030_k()
/*     */     {
/* 732 */       if (this.field_176047_c != ChatController.EnumChannelState.Disconnected)
/*     */       {
/* 734 */         func_176035_a(ChatController.EnumChannelState.Disconnected);
/* 735 */         func_176036_d(this.field_176048_a);
/* 736 */         func_176033_j();
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatStatusCallback(String p_chatStatusCallback_1_, ErrorCode p_chatStatusCallback_2_)
/*     */     {
/* 742 */       if (!ErrorCode.succeeded(p_chatStatusCallback_2_))
/*     */       {
/* 744 */         ChatController.this.field_175998_i.remove(p_chatStatusCallback_1_);
/* 745 */         func_176030_k();
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatChannelMembershipCallback(String p_chatChannelMembershipCallback_1_, ChatEvent p_chatChannelMembershipCallback_2_, ChatChannelInfo p_chatChannelMembershipCallback_3_)
/*     */     {
/* 751 */       switch (p_chatChannelMembershipCallback_2_)
/*     */       {
/*     */       case TTV_CHAT_JOINED_CHANNEL: 
/* 754 */         func_176035_a(ChatController.EnumChannelState.Connected);
/* 755 */         func_176031_c(p_chatChannelMembershipCallback_1_);
/* 756 */         break;
/*     */       
/*     */       case TTV_CHAT_LEFT_CHANNEL: 
/* 759 */         func_176030_k();
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatChannelUserChangeCallback(String p_chatChannelUserChangeCallback_1_, ChatUserInfo[] p_chatChannelUserChangeCallback_2_, ChatUserInfo[] p_chatChannelUserChangeCallback_3_, ChatUserInfo[] p_chatChannelUserChangeCallback_4_)
/*     */     {
/* 765 */       for (int i = 0; i < p_chatChannelUserChangeCallback_3_.length; i++)
/*     */       {
/* 767 */         int j = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_3_[i]);
/*     */         
/* 769 */         if (j >= 0)
/*     */         {
/* 771 */           this.field_176044_d.remove(j);
/*     */         }
/*     */       }
/*     */       
/* 775 */       for (int k = 0; k < p_chatChannelUserChangeCallback_4_.length; k++)
/*     */       {
/* 777 */         int i1 = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_4_[k]);
/*     */         
/* 779 */         if (i1 >= 0)
/*     */         {
/* 781 */           this.field_176044_d.remove(i1);
/*     */         }
/*     */         
/* 784 */         this.field_176044_d.add(p_chatChannelUserChangeCallback_4_[k]);
/*     */       }
/*     */       
/* 787 */       for (int l = 0; l < p_chatChannelUserChangeCallback_2_.length; l++)
/*     */       {
/* 789 */         this.field_176044_d.add(p_chatChannelUserChangeCallback_2_[l]);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 794 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 796 */           ChatController.this.field_153003_a.func_176018_a(this.field_176048_a, p_chatChannelUserChangeCallback_2_, p_chatChannelUserChangeCallback_3_, p_chatChannelUserChangeCallback_4_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 801 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatChannelRawMessageCallback(String p_chatChannelRawMessageCallback_1_, ChatRawMessage[] p_chatChannelRawMessageCallback_2_)
/*     */     {
/* 807 */       for (int i = 0; i < p_chatChannelRawMessageCallback_2_.length; i++)
/*     */       {
/* 809 */         this.field_176045_e.addLast(p_chatChannelRawMessageCallback_2_[i]);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 814 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 816 */           ChatController.this.field_153003_a.func_180605_a(this.field_176048_a, p_chatChannelRawMessageCallback_2_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 821 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */       
/* 824 */       while (this.field_176045_e.size() > ChatController.this.field_153015_m)
/*     */       {
/* 826 */         this.field_176045_e.removeFirst();
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatChannelTokenizedMessageCallback(String p_chatChannelTokenizedMessageCallback_1_, ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_2_)
/*     */     {
/* 832 */       for (int i = 0; i < p_chatChannelTokenizedMessageCallback_2_.length; i++)
/*     */       {
/* 834 */         this.field_176042_f.addLast(p_chatChannelTokenizedMessageCallback_2_[i]);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 839 */         if (ChatController.this.field_153003_a != null)
/*     */         {
/* 841 */           ChatController.this.field_153003_a.func_176025_a(this.field_176048_a, p_chatChannelTokenizedMessageCallback_2_);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 846 */         ChatController.this.func_152995_h(exception.toString());
/*     */       }
/*     */       
/* 849 */       while (this.field_176042_f.size() > ChatController.this.field_153015_m)
/*     */       {
/* 851 */         this.field_176042_f.removeFirst();
/*     */       }
/*     */     }
/*     */     
/*     */     public void chatClearCallback(String p_chatClearCallback_1_, String p_chatClearCallback_2_)
/*     */     {
/* 857 */       func_176032_a(p_chatClearCallback_2_);
/*     */     }
/*     */     
/*     */     public void chatBadgeDataDownloadCallback(String p_chatBadgeDataDownloadCallback_1_, ErrorCode p_chatBadgeDataDownloadCallback_2_)
/*     */     {
/* 862 */       if (ErrorCode.succeeded(p_chatBadgeDataDownloadCallback_2_))
/*     */       {
/* 864 */         func_176039_i();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ChatListener
/*     */   {
/*     */     public abstract void func_176023_d(ErrorCode paramErrorCode);
/*     */     
/*     */     public abstract void func_176022_e(ErrorCode paramErrorCode);
/*     */     
/*     */     public abstract void func_176021_d();
/*     */     
/*     */     public abstract void func_176024_e();
/*     */     
/*     */     public abstract void func_176017_a(ChatController.ChatState paramChatState);
/*     */     
/*     */     public abstract void func_176025_a(String paramString, ChatTokenizedMessage[] paramArrayOfChatTokenizedMessage);
/*     */     
/*     */     public abstract void func_180605_a(String paramString, ChatRawMessage[] paramArrayOfChatRawMessage);
/*     */     
/*     */     public abstract void func_176018_a(String paramString, ChatUserInfo[] paramArrayOfChatUserInfo1, ChatUserInfo[] paramArrayOfChatUserInfo2, ChatUserInfo[] paramArrayOfChatUserInfo3);
/*     */     
/*     */     public abstract void func_180606_a(String paramString);
/*     */     
/*     */     public abstract void func_180607_b(String paramString);
/*     */     
/*     */     public abstract void func_176019_a(String paramString1, String paramString2);
/*     */     
/*     */     public abstract void func_176016_c(String paramString);
/*     */     
/*     */     public abstract void func_176020_d(String paramString);
/*     */   }
/*     */   
/*     */   public static enum ChatState
/*     */   {
/* 900 */     Uninitialized, 
/* 901 */     Initializing, 
/* 902 */     Initialized, 
/* 903 */     ShuttingDown;
/*     */   }
/*     */   
/*     */   public static enum EnumChannelState
/*     */   {
/* 908 */     Created, 
/* 909 */     Connecting, 
/* 910 */     Connected, 
/* 911 */     Disconnecting, 
/* 912 */     Disconnected;
/*     */   }
/*     */   
/*     */   public static enum EnumEmoticonMode
/*     */   {
/* 917 */     None, 
/* 918 */     Url, 
/* 919 */     TextureAtlas;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\ChatController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */