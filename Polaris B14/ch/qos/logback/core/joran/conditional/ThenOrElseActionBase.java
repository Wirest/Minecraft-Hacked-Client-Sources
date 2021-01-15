/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import java.util.List;
/*    */ import java.util.Stack;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public abstract class ThenOrElseActionBase
/*    */   extends Action
/*    */ {
/* 30 */   Stack<ThenActionState> stateStack = new Stack();
/*    */   
/*    */ 
/*    */   public void begin(InterpretationContext ic, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 36 */     if (!weAreActive(ic)) { return;
/*    */     }
/* 38 */     ThenActionState state = new ThenActionState();
/* 39 */     if (ic.isListenerListEmpty()) {
/* 40 */       ic.addInPlayListener(state);
/* 41 */       state.isRegistered = true;
/*    */     }
/* 43 */     this.stateStack.push(state);
/*    */   }
/*    */   
/*    */   boolean weAreActive(InterpretationContext ic) {
/* 47 */     Object o = ic.peekObject();
/* 48 */     if (!(o instanceof IfAction)) return false;
/* 49 */     IfAction ifAction = (IfAction)o;
/* 50 */     return ifAction.isActive();
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ic, String name) throws ActionException
/*    */   {
/* 55 */     if (!weAreActive(ic)) { return;
/*    */     }
/* 57 */     ThenActionState state = (ThenActionState)this.stateStack.pop();
/* 58 */     if (state.isRegistered) {
/* 59 */       ic.removeInPlayListener(state);
/* 60 */       Object o = ic.peekObject();
/* 61 */       if ((o instanceof IfAction)) {
/* 62 */         IfAction ifAction = (IfAction)o;
/* 63 */         removeFirstAndLastFromList(state.eventList);
/* 64 */         registerEventList(ifAction, state.eventList);
/*    */       } else {
/* 66 */         throw new IllegalStateException("Missing IfAction on top of stack");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   abstract void registerEventList(IfAction paramIfAction, List<SaxEvent> paramList);
/*    */   
/*    */   void removeFirstAndLastFromList(List<SaxEvent> eventList) {
/* 74 */     eventList.remove(0);
/* 75 */     eventList.remove(eventList.size() - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\conditional\ThenOrElseActionBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */