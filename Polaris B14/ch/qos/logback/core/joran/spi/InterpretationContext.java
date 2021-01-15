/*     */ package ch.qos.logback.core.joran.spi;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.event.InPlayListener;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.PropertyContainer;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Locator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterpretationContext
/*     */   extends ContextAwareBase
/*     */   implements PropertyContainer
/*     */ {
/*     */   Stack<Object> objectStack;
/*     */   Map<String, Object> objectMap;
/*     */   Map<String, String> propertiesMap;
/*     */   Interpreter joranInterpreter;
/*  48 */   final List<InPlayListener> listenerList = new ArrayList();
/*  49 */   DefaultNestedComponentRegistry defaultNestedComponentRegistry = new DefaultNestedComponentRegistry();
/*     */   
/*     */   public InterpretationContext(Context context, Interpreter joranInterpreter) {
/*  52 */     this.context = context;
/*  53 */     this.joranInterpreter = joranInterpreter;
/*  54 */     this.objectStack = new Stack();
/*  55 */     this.objectMap = new HashMap(5);
/*  56 */     this.propertiesMap = new HashMap(5);
/*     */   }
/*     */   
/*     */   public DefaultNestedComponentRegistry getDefaultNestedComponentRegistry()
/*     */   {
/*  61 */     return this.defaultNestedComponentRegistry;
/*     */   }
/*     */   
/*     */   public Map<String, String> getCopyOfPropertyMap() {
/*  65 */     return new HashMap(this.propertiesMap);
/*     */   }
/*     */   
/*     */   void setPropertiesMap(Map<String, String> propertiesMap) {
/*  69 */     this.propertiesMap = propertiesMap;
/*     */   }
/*     */   
/*     */   String updateLocationInfo(String msg) {
/*  73 */     Locator locator = this.joranInterpreter.getLocator();
/*     */     
/*  75 */     if (locator != null) {
/*  76 */       return msg + locator.getLineNumber() + ":" + locator.getColumnNumber();
/*     */     }
/*  78 */     return msg;
/*     */   }
/*     */   
/*     */   public Locator getLocator()
/*     */   {
/*  83 */     return this.joranInterpreter.getLocator();
/*     */   }
/*     */   
/*     */   public Interpreter getJoranInterpreter() {
/*  87 */     return this.joranInterpreter;
/*     */   }
/*     */   
/*     */   public Stack<Object> getObjectStack() {
/*  91 */     return this.objectStack;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  95 */     return this.objectStack.isEmpty();
/*     */   }
/*     */   
/*     */   public Object peekObject() {
/*  99 */     return this.objectStack.peek();
/*     */   }
/*     */   
/*     */   public void pushObject(Object o) {
/* 103 */     this.objectStack.push(o);
/*     */   }
/*     */   
/*     */   public Object popObject() {
/* 107 */     return this.objectStack.pop();
/*     */   }
/*     */   
/*     */   public Object getObject(int i) {
/* 111 */     return this.objectStack.get(i);
/*     */   }
/*     */   
/*     */   public Map<String, Object> getObjectMap() {
/* 115 */     return this.objectMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSubstitutionProperty(String key, String value)
/*     */   {
/* 123 */     if ((key == null) || (value == null)) {
/* 124 */       return;
/*     */     }
/*     */     
/* 127 */     value = value.trim();
/* 128 */     this.propertiesMap.put(key, value);
/*     */   }
/*     */   
/*     */   public void addSubstitutionProperties(Properties props) {
/* 132 */     if (props == null) {
/* 133 */       return;
/*     */     }
/* 135 */     for (Object keyObject : props.keySet()) {
/* 136 */       String key = (String)keyObject;
/* 137 */       String val = props.getProperty(key);
/* 138 */       addSubstitutionProperty(key, val);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProperty(String key)
/*     */   {
/* 147 */     String v = (String)this.propertiesMap.get(key);
/* 148 */     if (v != null) {
/* 149 */       return v;
/*     */     }
/* 151 */     return this.context.getProperty(key);
/*     */   }
/*     */   
/*     */   public String subst(String value)
/*     */   {
/* 156 */     if (value == null) {
/* 157 */       return null;
/*     */     }
/* 159 */     return OptionHelper.substVars(value, this, this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isListenerListEmpty()
/*     */   {
/* 166 */     return this.listenerList.isEmpty();
/*     */   }
/*     */   
/*     */   public void addInPlayListener(InPlayListener ipl) {
/* 170 */     if (this.listenerList.contains(ipl)) {
/* 171 */       addWarn("InPlayListener " + ipl + " has been already registered");
/*     */     } else {
/* 173 */       this.listenerList.add(ipl);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean removeInPlayListener(InPlayListener ipl) {
/* 178 */     return this.listenerList.remove(ipl);
/*     */   }
/*     */   
/*     */   void fireInPlay(SaxEvent event) {
/* 182 */     for (InPlayListener ipl : this.listenerList) {
/* 183 */       ipl.inPlay(event);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\InterpretationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */