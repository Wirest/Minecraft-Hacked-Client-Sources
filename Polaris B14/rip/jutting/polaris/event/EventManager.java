/*     */ package rip.jutting.polaris.event;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class EventManager
/*     */ {
/*  10 */   private Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP = new java.util.HashMap();
/*     */   
/*     */   public void register(Object o) {
/*     */     Method[] arrayOfMethod;
/*  14 */     int j = (arrayOfMethod = o.getClass().getDeclaredMethods()).length; for (int i = 0; i < j; i++) { Method method = arrayOfMethod[i];
/*  15 */       if (!isMethodBad(method))
/*  16 */         register(method, o);
/*     */     }
/*     */   }
/*     */   
/*     */   public void register(Object o, Class<? extends Event> clazz) {
/*     */     Method[] arrayOfMethod;
/*  22 */     int j = (arrayOfMethod = o.getClass().getDeclaredMethods()).length; for (int i = 0; i < j; i++) { Method method = arrayOfMethod[i];
/*  23 */       if (!isMethodBad(method, clazz)) {
/*  24 */         register(method, o);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void register(Method method, Object o) {
/*  30 */     Class<?> clazz = method.getParameterTypes()[0];
/*  31 */     Data methodData = new Data(o, method, ((EventTarget)method.getAnnotation(EventTarget.class)).value());
/*     */     
/*  33 */     if (!methodData.target.isAccessible()) {
/*  34 */       methodData.target.setAccessible(true);
/*     */     }
/*     */     
/*  37 */     if (this.REGISTRY_MAP.containsKey(clazz)) {
/*  38 */       if (!((ArrayHelper)this.REGISTRY_MAP.get(clazz)).contains(methodData)) {
/*  39 */         ((ArrayHelper)this.REGISTRY_MAP.get(clazz)).add(methodData);
/*  40 */         sortListValue(clazz);
/*     */       }
/*     */     } else {
/*  43 */       this.REGISTRY_MAP.put(clazz, new ArrayHelper() {});
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void unregister(Object o)
/*     */   {
/*     */     Iterator localIterator2;
/*     */     
/*  53 */     for (Iterator localIterator1 = this.REGISTRY_MAP.values().iterator(); localIterator1.hasNext(); 
/*  54 */         localIterator2.hasNext())
/*     */     {
/*  53 */       ArrayHelper<Data> flexibalArray = (ArrayHelper)localIterator1.next();
/*  54 */       localIterator2 = flexibalArray.iterator(); continue;Data methodData = (Data)localIterator2.next();
/*  55 */       if (methodData.source.equals(o)) {
/*  56 */         flexibalArray.remove(methodData);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  61 */     cleanMap(true);
/*     */   }
/*     */   
/*     */   public void unregister(Object o, Class<? extends Event> clazz) {
/*  65 */     if (this.REGISTRY_MAP.containsKey(clazz)) {
/*  66 */       for (Data methodData : (ArrayHelper)this.REGISTRY_MAP.get(clazz)) {
/*  67 */         if (methodData.source.equals(o)) {
/*  68 */           ((ArrayHelper)this.REGISTRY_MAP.get(clazz)).remove(methodData);
/*     */         }
/*     */       }
/*     */       
/*  72 */       cleanMap(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanMap(boolean b)
/*     */   {
/*  78 */     Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = this.REGISTRY_MAP.entrySet().iterator();
/*     */     
/*  80 */     while (iterator.hasNext()) {
/*  81 */       if ((!b) || (((ArrayHelper)((Map.Entry)iterator.next()).getValue()).isEmpty())) {
/*  82 */         iterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeEnty(Class<? extends Event> clazz)
/*     */   {
/*  89 */     Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = this.REGISTRY_MAP.entrySet().iterator();
/*     */     
/*  91 */     while (iterator.hasNext()) {
/*  92 */       if (((Class)((Map.Entry)iterator.next()).getKey()).equals(clazz)) {
/*  93 */         iterator.remove();
/*  94 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void sortListValue(Class<? extends Event> clazz)
/*     */   {
/* 101 */     ArrayHelper<Data> flexibleArray = new ArrayHelper();
/*     */     byte[] arrayOfByte;
/* 103 */     int j = (arrayOfByte = Priority.VALUE_ARRAY).length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
/* 104 */       for (Data methodData : (ArrayHelper)this.REGISTRY_MAP.get(clazz)) {
/* 105 */         if (methodData.priority == b) {
/* 106 */           flexibleArray.add(methodData);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 111 */     this.REGISTRY_MAP.put(clazz, flexibleArray);
/*     */   }
/*     */   
/*     */   private boolean isMethodBad(Method method)
/*     */   {
/* 116 */     return (method.getParameterTypes().length != 1) || (!method.isAnnotationPresent(EventTarget.class));
/*     */   }
/*     */   
/*     */   private boolean isMethodBad(Method method, Class<? extends Event> clazz) {
/* 120 */     return (isMethodBad(method)) || (method.getParameterTypes()[0].equals(clazz));
/*     */   }
/*     */   
/*     */   public ArrayHelper<Data> get(Class<? extends Event> clazz) {
/* 124 */     return (ArrayHelper)this.REGISTRY_MAP.get(clazz);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 128 */     this.REGISTRY_MAP.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */