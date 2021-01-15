/*     */ package io.netty.channel;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ChannelHandlerAppender
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final boolean selfRemoval;
/*     */   
/*     */   private static final class Entry
/*     */   {
/*     */     final String name;
/*     */     final ChannelHandler handler;
/*     */     
/*     */     Entry(String name, ChannelHandler handler)
/*     */     {
/*  34 */       this.name = name;
/*  35 */       this.handler = handler;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  40 */   private final List<Entry> handlers = new ArrayList();
/*     */   
/*     */ 
/*     */   private boolean added;
/*     */   
/*     */ 
/*     */   protected ChannelHandlerAppender()
/*     */   {
/*  48 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChannelHandlerAppender(boolean selfRemoval)
/*     */   {
/*  59 */     this.selfRemoval = selfRemoval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelHandlerAppender(Iterable<? extends ChannelHandler> handlers)
/*     */   {
/*  66 */     this(true, handlers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelHandlerAppender(ChannelHandler... handlers)
/*     */   {
/*  73 */     this(true, handlers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelHandlerAppender(boolean selfRemoval, Iterable<? extends ChannelHandler> handlers)
/*     */   {
/*  83 */     this.selfRemoval = selfRemoval;
/*  84 */     add(handlers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelHandlerAppender(boolean selfRemoval, ChannelHandler... handlers)
/*     */   {
/*  94 */     this.selfRemoval = selfRemoval;
/*  95 */     add(handlers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ChannelHandlerAppender add(String name, ChannelHandler handler)
/*     */   {
/* 107 */     if (handler == null) {
/* 108 */       throw new NullPointerException("handler");
/*     */     }
/*     */     
/* 111 */     if (this.added) {
/* 112 */       throw new IllegalStateException("added to the pipeline already");
/*     */     }
/*     */     
/* 115 */     this.handlers.add(new Entry(name, handler));
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ChannelHandlerAppender add(ChannelHandler handler)
/*     */   {
/* 127 */     return add(null, handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ChannelHandlerAppender add(Iterable<? extends ChannelHandler> handlers)
/*     */   {
/* 136 */     if (handlers == null) {
/* 137 */       throw new NullPointerException("handlers");
/*     */     }
/*     */     
/* 140 */     for (ChannelHandler h : handlers) {
/* 141 */       if (h == null) {
/*     */         break;
/*     */       }
/* 144 */       add(h);
/*     */     }
/*     */     
/* 147 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ChannelHandlerAppender add(ChannelHandler... handlers)
/*     */   {
/* 156 */     if (handlers == null) {
/* 157 */       throw new NullPointerException("handlers");
/*     */     }
/*     */     
/* 160 */     for (ChannelHandler h : handlers) {
/* 161 */       if (h == null) {
/*     */         break;
/*     */       }
/*     */       
/* 165 */       add(h);
/*     */     }
/*     */     
/* 168 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final <T extends ChannelHandler> T handlerAt(int index)
/*     */   {
/* 176 */     return ((Entry)this.handlers.get(index)).handler;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 181 */     this.added = true;
/*     */     
/* 183 */     AbstractChannelHandlerContext dctx = (AbstractChannelHandlerContext)ctx;
/* 184 */     DefaultChannelPipeline pipeline = (DefaultChannelPipeline)dctx.pipeline();
/* 185 */     String name = dctx.name();
/*     */     try {
/* 187 */       for (Entry e : this.handlers) {
/* 188 */         String oldName = name;
/* 189 */         if (e.name == null) {
/* 190 */           name = pipeline.generateName(e.handler);
/*     */         } else {
/* 192 */           name = e.name;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 197 */         pipeline.addAfter(dctx.invoker, oldName, name, e.handler);
/*     */       }
/*     */     } finally {
/* 200 */       if (this.selfRemoval) {
/* 201 */         pipeline.remove(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelHandlerAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */