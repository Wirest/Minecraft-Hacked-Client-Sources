/*     */ package ch.qos.logback.core.joran.conditional;
/*     */ 
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.spi.ActionException;
/*     */ import ch.qos.logback.core.joran.spi.EventPlayer;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.Interpreter;
/*     */ import ch.qos.logback.core.util.EnvUtil;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class IfAction
/*     */   extends Action
/*     */ {
/*     */   private static final String CONDITION_ATTR = "condition";
/*     */   public static final String MISSING_JANINO_MSG = "Could not find Janino library on the class path. Skipping conditional processing.";
/*     */   public static final String MISSING_JANINO_SEE = "See also http://logback.qos.ch/codes.html#ifJanino";
/*  36 */   Stack<IfState> stack = new Stack();
/*     */   
/*     */ 
/*     */   public void begin(InterpretationContext ic, String name, Attributes attributes)
/*     */     throws ActionException
/*     */   {
/*  42 */     IfState state = new IfState();
/*  43 */     boolean emptyStack = this.stack.isEmpty();
/*  44 */     this.stack.push(state);
/*     */     
/*  46 */     if (!emptyStack) {
/*  47 */       return;
/*     */     }
/*     */     
/*  50 */     ic.pushObject(this);
/*  51 */     if (!EnvUtil.isJaninoAvailable()) {
/*  52 */       addError("Could not find Janino library on the class path. Skipping conditional processing.");
/*  53 */       addError("See also http://logback.qos.ch/codes.html#ifJanino");
/*  54 */       return;
/*     */     }
/*     */     
/*  57 */     state.active = true;
/*  58 */     Condition condition = null;
/*  59 */     String conditionAttribute = attributes.getValue("condition");
/*     */     
/*     */ 
/*  62 */     if (!OptionHelper.isEmpty(conditionAttribute)) {
/*  63 */       conditionAttribute = OptionHelper.substVars(conditionAttribute, ic, this.context);
/*  64 */       PropertyEvalScriptBuilder pesb = new PropertyEvalScriptBuilder(ic);
/*  65 */       pesb.setContext(this.context);
/*     */       try {
/*  67 */         condition = pesb.build(conditionAttribute);
/*     */       } catch (Exception e) {
/*  69 */         addError("Failed to parse condition [" + conditionAttribute + "]", e);
/*     */       }
/*     */       
/*  72 */       if (condition != null) {
/*  73 */         state.boolResult = Boolean.valueOf(condition.evaluate());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ic, String name)
/*     */     throws ActionException
/*     */   {
/*  83 */     IfState state = (IfState)this.stack.pop();
/*  84 */     if (!state.active) {
/*  85 */       return;
/*     */     }
/*     */     
/*     */ 
/*  89 */     Object o = ic.peekObject();
/*  90 */     if (o == null) {
/*  91 */       throw new IllegalStateException("Unexpected null object on stack");
/*     */     }
/*  93 */     if (!(o instanceof IfAction)) {
/*  94 */       throw new IllegalStateException("Unexpected object of type [" + o.getClass() + "] on stack");
/*     */     }
/*     */     
/*     */ 
/*  98 */     if (o != this) {
/*  99 */       throw new IllegalStateException("IfAction different then current one on stack");
/*     */     }
/*     */     
/* 102 */     ic.popObject();
/*     */     
/* 104 */     if (state.boolResult == null) {
/* 105 */       addError("Failed to determine \"if then else\" result");
/* 106 */       return;
/*     */     }
/*     */     
/* 109 */     Interpreter interpreter = ic.getJoranInterpreter();
/* 110 */     List<SaxEvent> listToPlay = state.thenSaxEventList;
/* 111 */     if (!state.boolResult.booleanValue()) {
/* 112 */       listToPlay = state.elseSaxEventList;
/*     */     }
/*     */     
/*     */ 
/* 116 */     if (listToPlay != null)
/*     */     {
/* 118 */       interpreter.getEventPlayer().addEventsDynamically(listToPlay, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setThenSaxEventList(List<SaxEvent> thenSaxEventList)
/*     */   {
/* 125 */     IfState state = (IfState)this.stack.firstElement();
/* 126 */     if (state.active) {
/* 127 */       state.thenSaxEventList = thenSaxEventList;
/*     */     } else {
/* 129 */       throw new IllegalStateException("setThenSaxEventList() invoked on inactive IfAction");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setElseSaxEventList(List<SaxEvent> elseSaxEventList) {
/* 134 */     IfState state = (IfState)this.stack.firstElement();
/* 135 */     if (state.active) {
/* 136 */       state.elseSaxEventList = elseSaxEventList;
/*     */     } else {
/* 138 */       throw new IllegalStateException("setElseSaxEventList() invoked on inactive IfAction");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 144 */     if (this.stack == null) return false;
/* 145 */     if (this.stack.isEmpty()) return false;
/* 146 */     return ((IfState)this.stack.peek()).active;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\conditional\IfAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */