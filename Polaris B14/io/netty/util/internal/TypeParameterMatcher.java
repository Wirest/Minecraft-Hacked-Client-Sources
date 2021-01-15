/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class TypeParameterMatcher
/*     */ {
/*  29 */   private static final TypeParameterMatcher NOOP = new NoOpTypeParameterMatcher();
/*  30 */   private static final Object TEST_OBJECT = new Object();
/*     */   
/*     */   public static TypeParameterMatcher get(Class<?> parameterType) {
/*  33 */     Map<Class<?>, TypeParameterMatcher> getCache = InternalThreadLocalMap.get().typeParameterMatcherGetCache();
/*     */     
/*     */ 
/*  36 */     TypeParameterMatcher matcher = (TypeParameterMatcher)getCache.get(parameterType);
/*  37 */     if (matcher == null) {
/*  38 */       if (parameterType == Object.class) {
/*  39 */         matcher = NOOP;
/*  40 */       } else if (PlatformDependent.hasJavassist()) {
/*     */         try {
/*  42 */           matcher = JavassistTypeParameterMatcherGenerator.generate(parameterType);
/*  43 */           matcher.match(TEST_OBJECT);
/*     */         }
/*     */         catch (IllegalAccessError e) {
/*  46 */           matcher = null;
/*     */         }
/*     */         catch (Exception e) {
/*  49 */           matcher = null;
/*     */         }
/*     */       }
/*     */       
/*  53 */       if (matcher == null) {
/*  54 */         matcher = new ReflectiveMatcher(parameterType);
/*     */       }
/*     */       
/*  57 */       getCache.put(parameterType, matcher);
/*     */     }
/*     */     
/*  60 */     return matcher;
/*     */   }
/*     */   
/*     */ 
/*     */   public static TypeParameterMatcher find(Object object, Class<?> parameterizedSuperclass, String typeParamName)
/*     */   {
/*  66 */     Map<Class<?>, Map<String, TypeParameterMatcher>> findCache = InternalThreadLocalMap.get().typeParameterMatcherFindCache();
/*     */     
/*  68 */     Class<?> thisClass = object.getClass();
/*     */     
/*  70 */     Map<String, TypeParameterMatcher> map = (Map)findCache.get(thisClass);
/*  71 */     if (map == null) {
/*  72 */       map = new HashMap();
/*  73 */       findCache.put(thisClass, map);
/*     */     }
/*     */     
/*  76 */     TypeParameterMatcher matcher = (TypeParameterMatcher)map.get(typeParamName);
/*  77 */     if (matcher == null) {
/*  78 */       matcher = get(find0(object, parameterizedSuperclass, typeParamName));
/*  79 */       map.put(typeParamName, matcher);
/*     */     }
/*     */     
/*  82 */     return matcher;
/*     */   }
/*     */   
/*     */ 
/*     */   private static Class<?> find0(Object object, Class<?> parameterizedSuperclass, String typeParamName)
/*     */   {
/*  88 */     Class<?> thisClass = object.getClass();
/*  89 */     Class<?> currentClass = thisClass;
/*     */     do {
/*  91 */       while (currentClass.getSuperclass() == parameterizedSuperclass) {
/*  92 */         int typeParamIndex = -1;
/*  93 */         TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
/*  94 */         for (int i = 0; i < typeParams.length; i++) {
/*  95 */           if (typeParamName.equals(typeParams[i].getName())) {
/*  96 */             typeParamIndex = i;
/*  97 */             break;
/*     */           }
/*     */         }
/*     */         
/* 101 */         if (typeParamIndex < 0) {
/* 102 */           throw new IllegalStateException("unknown type parameter '" + typeParamName + "': " + parameterizedSuperclass);
/*     */         }
/*     */         
/*     */ 
/* 106 */         Type genericSuperType = currentClass.getGenericSuperclass();
/* 107 */         if (!(genericSuperType instanceof ParameterizedType)) {
/* 108 */           return Object.class;
/*     */         }
/*     */         
/* 111 */         Type[] actualTypeParams = ((ParameterizedType)genericSuperType).getActualTypeArguments();
/*     */         
/* 113 */         Type actualTypeParam = actualTypeParams[typeParamIndex];
/* 114 */         if ((actualTypeParam instanceof ParameterizedType)) {
/* 115 */           actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
/*     */         }
/* 117 */         if ((actualTypeParam instanceof Class)) {
/* 118 */           return (Class)actualTypeParam;
/*     */         }
/* 120 */         if ((actualTypeParam instanceof GenericArrayType)) {
/* 121 */           Type componentType = ((GenericArrayType)actualTypeParam).getGenericComponentType();
/* 122 */           if ((componentType instanceof ParameterizedType)) {
/* 123 */             componentType = ((ParameterizedType)componentType).getRawType();
/*     */           }
/* 125 */           if ((componentType instanceof Class)) {
/* 126 */             return Array.newInstance((Class)componentType, 0).getClass();
/*     */           }
/*     */         }
/* 129 */         if ((actualTypeParam instanceof TypeVariable))
/*     */         {
/* 131 */           TypeVariable<?> v = (TypeVariable)actualTypeParam;
/* 132 */           currentClass = thisClass;
/* 133 */           if (!(v.getGenericDeclaration() instanceof Class)) {
/* 134 */             return Object.class;
/*     */           }
/*     */           
/* 137 */           parameterizedSuperclass = (Class)v.getGenericDeclaration();
/* 138 */           typeParamName = v.getName();
/* 139 */           if (!parameterizedSuperclass.isAssignableFrom(thisClass))
/*     */           {
/*     */ 
/* 142 */             return Object.class;
/*     */           }
/*     */         }
/*     */         else {
/* 146 */           return fail(thisClass, typeParamName);
/*     */         } }
/* 148 */       currentClass = currentClass.getSuperclass();
/* 149 */     } while (currentClass != null);
/* 150 */     return fail(thisClass, typeParamName);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Class<?> fail(Class<?> type, String typeParamName)
/*     */   {
/* 156 */     throw new IllegalStateException("cannot determine the type of the type parameter '" + typeParamName + "': " + type);
/*     */   }
/*     */   
/*     */   public abstract boolean match(Object paramObject);
/*     */   
/*     */   private static final class ReflectiveMatcher extends TypeParameterMatcher
/*     */   {
/*     */     private final Class<?> type;
/*     */     
/*     */     ReflectiveMatcher(Class<?> type) {
/* 166 */       this.type = type;
/*     */     }
/*     */     
/*     */     public boolean match(Object msg)
/*     */     {
/* 171 */       return this.type.isInstance(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\TypeParameterMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */