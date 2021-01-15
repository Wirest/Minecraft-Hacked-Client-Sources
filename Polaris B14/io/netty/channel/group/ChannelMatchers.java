/*     */ package io.netty.channel.group;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ServerChannel;
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
/*     */ public final class ChannelMatchers
/*     */ {
/*  26 */   private static final ChannelMatcher ALL_MATCHER = new ChannelMatcher()
/*     */   {
/*     */     public boolean matches(Channel channel) {
/*  29 */       return true;
/*     */     }
/*     */   };
/*     */   
/*  33 */   private static final ChannelMatcher SERVER_CHANNEL_MATCHER = isInstanceOf(ServerChannel.class);
/*  34 */   private static final ChannelMatcher NON_SERVER_CHANNEL_MATCHER = isNotInstanceOf(ServerChannel.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher all()
/*     */   {
/*  44 */     return ALL_MATCHER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher isNot(Channel channel)
/*     */   {
/*  51 */     return invert(is(channel));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher is(Channel channel)
/*     */   {
/*  58 */     return new InstanceMatcher(channel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher isInstanceOf(Class<? extends Channel> clazz)
/*     */   {
/*  66 */     return new ClassMatcher(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher isNotInstanceOf(Class<? extends Channel> clazz)
/*     */   {
/*  74 */     return invert(isInstanceOf(clazz));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher isServerChannel()
/*     */   {
/*  81 */     return SERVER_CHANNEL_MATCHER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher isNonServerChannel()
/*     */   {
/*  89 */     return NON_SERVER_CHANNEL_MATCHER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher invert(ChannelMatcher matcher)
/*     */   {
/*  96 */     return new InvertMatcher(matcher);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChannelMatcher compose(ChannelMatcher... matchers)
/*     */   {
/* 104 */     if (matchers.length < 1) {
/* 105 */       throw new IllegalArgumentException("matchers must at least contain one element");
/*     */     }
/* 107 */     if (matchers.length == 1) {
/* 108 */       return matchers[0];
/*     */     }
/* 110 */     return new CompositeMatcher(matchers);
/*     */   }
/*     */   
/*     */   private static final class CompositeMatcher implements ChannelMatcher {
/*     */     private final ChannelMatcher[] matchers;
/*     */     
/*     */     CompositeMatcher(ChannelMatcher... matchers) {
/* 117 */       this.matchers = matchers;
/*     */     }
/*     */     
/*     */     public boolean matches(Channel channel)
/*     */     {
/* 122 */       for (ChannelMatcher m : this.matchers) {
/* 123 */         if (!m.matches(channel)) {
/* 124 */           return false;
/*     */         }
/*     */       }
/* 127 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InvertMatcher implements ChannelMatcher {
/*     */     private final ChannelMatcher matcher;
/*     */     
/*     */     InvertMatcher(ChannelMatcher matcher) {
/* 135 */       this.matcher = matcher;
/*     */     }
/*     */     
/*     */     public boolean matches(Channel channel)
/*     */     {
/* 140 */       return !this.matcher.matches(channel);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InstanceMatcher implements ChannelMatcher {
/*     */     private final Channel channel;
/*     */     
/*     */     InstanceMatcher(Channel channel) {
/* 148 */       this.channel = channel;
/*     */     }
/*     */     
/*     */     public boolean matches(Channel ch)
/*     */     {
/* 153 */       return this.channel == ch;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ClassMatcher implements ChannelMatcher {
/*     */     private final Class<? extends Channel> clazz;
/*     */     
/*     */     ClassMatcher(Class<? extends Channel> clazz) {
/* 161 */       this.clazz = clazz;
/*     */     }
/*     */     
/*     */     public boolean matches(Channel ch)
/*     */     {
/* 166 */       return this.clazz.isInstance(ch);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\ChannelMatchers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */