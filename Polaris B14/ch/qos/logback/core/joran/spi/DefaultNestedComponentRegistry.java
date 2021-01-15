/*    */ package ch.qos.logback.core.joran.spi;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class DefaultNestedComponentRegistry
/*    */ {
/* 27 */   Map<HostClassAndPropertyDouble, Class<?>> defaultComponentMap = new HashMap();
/*    */   
/*    */   public void add(Class<?> hostClass, String propertyName, Class<?> componentClass) {
/* 30 */     HostClassAndPropertyDouble hpDouble = new HostClassAndPropertyDouble(hostClass, propertyName.toLowerCase());
/*    */     
/* 32 */     this.defaultComponentMap.put(hpDouble, componentClass);
/*    */   }
/*    */   
/*    */   public Class<?> findDefaultComponentType(Class<?> hostClass, String propertyName) {
/* 36 */     propertyName = propertyName.toLowerCase();
/* 37 */     while (hostClass != null) {
/* 38 */       Class<?> componentClass = oneShotFind(hostClass, propertyName);
/* 39 */       if (componentClass != null) {
/* 40 */         return componentClass;
/*    */       }
/* 42 */       hostClass = hostClass.getSuperclass();
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   private Class<?> oneShotFind(Class<?> hostClass, String propertyName) {
/* 48 */     HostClassAndPropertyDouble hpDouble = new HostClassAndPropertyDouble(hostClass, propertyName);
/*    */     
/* 50 */     return (Class)this.defaultComponentMap.get(hpDouble);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\DefaultNestedComponentRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */