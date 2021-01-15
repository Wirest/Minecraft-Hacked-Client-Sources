/*    */ package ch.qos.logback.classic.gaffer;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.spi.AppenderAttachable;
/*    */ import groovy.lang.Closure;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*    */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*    */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*    */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
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
/*    */ public class AppenderDelegate
/*    */   extends ComponentDelegate
/*    */ {
/*    */   private Map<String, Appender<?>> appendersByName;
/*    */   
/*    */   public AppenderDelegate(Appender appender)
/*    */   {
/* 29 */     super(appender);Map localMap = ScriptBytecodeAdapter.createMap(new Object[0]);this.appendersByName = localMap;
/*    */   }
/*    */   
/*    */   class _closure1 extends Closure implements GeneratedClosure { public _closure1(Object _thisObject) { super(_thisObject); }
/*    */     
/* 34 */     public Object doCall(Object it) { CallSite[] arrayOfCallSite = $getCallSiteArray();return ScriptBytecodeAdapter.createMap(new Object[] { arrayOfCallSite[0].callGetProperty(it), it });return null;
/*    */     }
/*    */     
/*    */     public Object doCall()
/*    */     {
/*    */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*    */       return doCall(null);
/*    */       return null;
/*    */     }
/*    */   }
/*    */   
/*    */   public AppenderDelegate(Appender appender, List<Appender<?>> appenders)
/*    */   {
/* 33 */     super(appender);Map localMap = ScriptBytecodeAdapter.createMap(new Object[0]);this.appendersByName = localMap;
/* 34 */     Object localObject = arrayOfCallSite[0].call(appenders, new _closure1(this));this.appendersByName = ((Map)ScriptBytecodeAdapter.castToType(localObject, Map.class));
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 38 */     CallSite[] arrayOfCallSite = $getCallSiteArray();return "appender";return null;
/*    */   }
/*    */   
/*    */   public void appenderRef(String name) {
/* 42 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((!DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(AppenderAttachable.class, arrayOfCallSite[2].callGetProperty(arrayOfCallSite[3].callGroovyObjectGetProperty(this)))) ? 1 : 0) != 0) {
/* 43 */       Object errorMessage = arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].callGetProperty(arrayOfCallSite[8].callGetProperty(arrayOfCallSite[9].callGroovyObjectGetProperty(this))), " does not implement "), arrayOfCallSite[10].callGetProperty(AppenderAttachable.class)), ".");
/* 44 */       throw ((Throwable)arrayOfCallSite[11].callConstructor(IllegalArgumentException.class, errorMessage));
/*    */     }
/* 46 */     arrayOfCallSite[12].call(arrayOfCallSite[13].callGroovyObjectGetProperty(this), arrayOfCallSite[14].call(this.appendersByName, name));
/*    */   }
/*    */   
/*    */   public Map<String, Appender<?>> getAppendersByName()
/*    */   {
/*    */     return this.appendersByName;
/*    */   }
/*    */   
/*    */   public void setAppendersByName(Map<String, Appender<?>> paramMap)
/*    */   {
/*    */     this.appendersByName = paramMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\gaffer\AppenderDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */