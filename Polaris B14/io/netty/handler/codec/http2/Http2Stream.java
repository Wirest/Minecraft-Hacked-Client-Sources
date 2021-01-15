/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ public abstract interface Http2Stream { public abstract int id();
/*    */   
/*    */   public abstract State state();
/*    */   
/*    */   public abstract Http2Stream open(boolean paramBoolean) throws Http2Exception;
/*    */   
/*    */   public abstract Http2Stream close();
/*    */   
/*    */   public abstract Http2Stream closeLocalSide();
/*    */   
/*    */   public abstract Http2Stream closeRemoteSide();
/*    */   
/*    */   public abstract boolean isResetSent();
/*    */   
/*    */   public abstract Http2Stream resetSent();
/*    */   
/*    */   public abstract boolean remoteSideOpen();
/*    */   
/*    */   public abstract boolean localSideOpen();
/*    */   
/*    */   public abstract Object setProperty(Object paramObject1, Object paramObject2);
/*    */   
/*    */   public abstract <V> V getProperty(Object paramObject);
/*    */   
/* 29 */   public static enum State { IDLE, 
/* 30 */     RESERVED_LOCAL, 
/* 31 */     RESERVED_REMOTE, 
/* 32 */     OPEN, 
/* 33 */     HALF_CLOSED_LOCAL, 
/* 34 */     HALF_CLOSED_REMOTE, 
/* 35 */     CLOSED;
/*    */     
/*    */     private State() {}
/*    */   }
/*    */   
/*    */   public abstract <V> V removeProperty(Object paramObject);
/*    */   
/*    */   public abstract Http2Stream setPriority(int paramInt, short paramShort, boolean paramBoolean)
/*    */     throws Http2Exception;
/*    */   
/*    */   public abstract boolean isRoot();
/*    */   
/*    */   public abstract boolean isLeaf();
/*    */   
/*    */   public abstract short weight();
/*    */   
/*    */   public abstract int totalChildWeights();
/*    */   
/*    */   public abstract Http2Stream parent();
/*    */   
/*    */   public abstract boolean isDescendantOf(Http2Stream paramHttp2Stream);
/*    */   
/*    */   public abstract int numChildren();
/*    */   
/*    */   public abstract boolean hasChild(int paramInt);
/*    */   
/*    */   public abstract Http2Stream child(int paramInt);
/*    */   
/*    */   public abstract Collection<? extends Http2Stream> children();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Stream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */