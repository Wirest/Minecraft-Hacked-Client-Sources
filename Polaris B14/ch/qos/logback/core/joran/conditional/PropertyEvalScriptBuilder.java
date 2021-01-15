/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import ch.qos.logback.core.spi.PropertyContainer;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.codehaus.commons.compiler.CompileException;
/*    */ import org.codehaus.janino.ClassBodyEvaluator;
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
/*    */ public class PropertyEvalScriptBuilder
/*    */   extends ContextAwareBase
/*    */ {
/* 29 */   private static String SCRIPT_PREFIX = "public boolean evaluate() { return ";
/*    */   
/* 31 */   private static String SCRIPT_SUFFIX = "; }";
/*    */   final PropertyContainer localPropContainer;
/*    */   
/*    */   PropertyEvalScriptBuilder(PropertyContainer localPropContainer)
/*    */   {
/* 36 */     this.localPropContainer = localPropContainer;
/*    */   }
/*    */   
/* 39 */   Map<String, String> map = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Condition build(String script)
/*    */     throws IllegalAccessException, CompileException, InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
/*    */   {
/* 47 */     ClassBodyEvaluator cbe = new ClassBodyEvaluator();
/* 48 */     cbe.setImplementedInterfaces(new Class[] { Condition.class });
/* 49 */     cbe.setExtendedClass(PropertyWrapperForScripts.class);
/* 50 */     cbe.setParentClassLoader(ClassBodyEvaluator.class.getClassLoader());
/* 51 */     cbe.cook(SCRIPT_PREFIX + script + SCRIPT_SUFFIX);
/*    */     
/* 53 */     Class<?> clazz = cbe.getClazz();
/* 54 */     Condition instance = (Condition)clazz.newInstance();
/* 55 */     Method setMapMethod = clazz.getMethod("setPropertyContainers", new Class[] { PropertyContainer.class, PropertyContainer.class });
/* 56 */     setMapMethod.invoke(instance, new Object[] { this.localPropContainer, this.context });
/*    */     
/* 58 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\conditional\PropertyEvalScriptBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */