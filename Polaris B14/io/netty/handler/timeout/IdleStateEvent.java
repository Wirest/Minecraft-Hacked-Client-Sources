/*    */ package io.netty.handler.timeout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class IdleStateEvent
/*    */ {
/* 24 */   public static final IdleStateEvent FIRST_READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, true);
/* 25 */   public static final IdleStateEvent READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, false);
/* 26 */   public static final IdleStateEvent FIRST_WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, true);
/* 27 */   public static final IdleStateEvent WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, false);
/* 28 */   public static final IdleStateEvent FIRST_ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, true);
/* 29 */   public static final IdleStateEvent ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, false);
/*    */   private final IdleState state;
/*    */   private final boolean first;
/*    */   
/*    */   private IdleStateEvent(IdleState state, boolean first)
/*    */   {
/* 35 */     this.state = state;
/* 36 */     this.first = first;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public IdleState state()
/*    */   {
/* 43 */     return this.state;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isFirst()
/*    */   {
/* 50 */     return this.first;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\timeout\IdleStateEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */