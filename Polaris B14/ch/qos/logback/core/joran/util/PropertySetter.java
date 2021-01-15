/*     */ package ch.qos.logback.core.joran.util;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.DefaultClass;
/*     */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.AggregationType;
/*     */ import ch.qos.logback.core.util.PropertySetterException;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.MethodDescriptor;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class PropertySetter
/*     */   extends ContextAwareBase
/*     */ {
/*     */   protected Object obj;
/*     */   protected Class<?> objClass;
/*     */   protected PropertyDescriptor[] propertyDescriptors;
/*     */   protected MethodDescriptor[] methodDescriptors;
/*     */   
/*     */   public PropertySetter(Object obj)
/*     */   {
/*  69 */     this.obj = obj;
/*  70 */     this.objClass = obj.getClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void introspect()
/*     */   {
/*     */     try
/*     */     {
/*  79 */       BeanInfo bi = Introspector.getBeanInfo(this.obj.getClass());
/*  80 */       this.propertyDescriptors = bi.getPropertyDescriptors();
/*  81 */       this.methodDescriptors = bi.getMethodDescriptors();
/*     */     } catch (IntrospectionException ex) {
/*  83 */       addError("Failed to introspect " + this.obj + ": " + ex.getMessage());
/*  84 */       this.propertyDescriptors = new PropertyDescriptor[0];
/*  85 */       this.methodDescriptors = new MethodDescriptor[0];
/*     */     }
/*     */   }
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
/*     */   public void setProperty(String name, String value)
/*     */   {
/* 108 */     if (value == null) {
/* 109 */       return;
/*     */     }
/*     */     
/* 112 */     name = Introspector.decapitalize(name);
/*     */     
/* 114 */     PropertyDescriptor prop = getPropertyDescriptor(name);
/*     */     
/* 116 */     if (prop == null) {
/* 117 */       addWarn("No such property [" + name + "] in " + this.objClass.getName() + ".");
/*     */     } else {
/*     */       try {
/* 120 */         setProperty(prop, name, value);
/*     */       } catch (PropertySetterException ex) {
/* 122 */         addWarn("Failed to set property [" + name + "] to value \"" + value + "\". ", ex);
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public void setProperty(PropertyDescriptor prop, String name, String value)
/*     */     throws PropertySetterException
/*     */   {
/* 141 */     Method setter = prop.getWriteMethod();
/*     */     
/* 143 */     if (setter == null) {
/* 144 */       throw new PropertySetterException("No setter for property [" + name + "].");
/*     */     }
/*     */     
/*     */ 
/* 148 */     Class<?>[] paramTypes = setter.getParameterTypes();
/*     */     
/* 150 */     if (paramTypes.length != 1) {
/* 151 */       throw new PropertySetterException("#params for setter != 1");
/*     */     }
/*     */     
/*     */     Object arg;
/*     */     try
/*     */     {
/* 157 */       arg = StringToObjectConverter.convertArg(this, value, paramTypes[0]);
/*     */     } catch (Throwable t) {
/* 159 */       throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed. ", t);
/*     */     }
/*     */     
/*     */ 
/* 163 */     if (arg == null) {
/* 164 */       throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed.");
/*     */     }
/*     */     try
/*     */     {
/* 168 */       setter.invoke(this.obj, new Object[] { arg });
/*     */     } catch (Exception ex) {
/* 170 */       throw new PropertySetterException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public AggregationType computeAggregationType(String name) {
/* 175 */     String cName = capitalizeFirstLetter(name);
/*     */     
/* 177 */     Method addMethod = findAdderMethod(cName);
/*     */     
/*     */ 
/* 180 */     if (addMethod != null) {
/* 181 */       AggregationType type = computeRawAggregationType(addMethod);
/* 182 */       switch (type) {
/*     */       case NOT_FOUND: 
/* 184 */         return AggregationType.NOT_FOUND;
/*     */       case AS_BASIC_PROPERTY: 
/* 186 */         return AggregationType.AS_BASIC_PROPERTY_COLLECTION;
/*     */       case AS_COMPLEX_PROPERTY: 
/* 188 */         return AggregationType.AS_COMPLEX_PROPERTY_COLLECTION;
/*     */       }
/*     */       
/*     */     }
/* 192 */     Method setterMethod = findSetterMethod(name);
/* 193 */     if (setterMethod != null) {
/* 194 */       return computeRawAggregationType(setterMethod);
/*     */     }
/*     */     
/* 197 */     return AggregationType.NOT_FOUND;
/*     */   }
/*     */   
/*     */   private Method findAdderMethod(String name)
/*     */   {
/* 202 */     name = capitalizeFirstLetter(name);
/* 203 */     return getMethod("add" + name);
/*     */   }
/*     */   
/*     */   private Method findSetterMethod(String name) {
/* 207 */     String dName = Introspector.decapitalize(name);
/* 208 */     PropertyDescriptor propertyDescriptor = getPropertyDescriptor(dName);
/* 209 */     if (propertyDescriptor != null) {
/* 210 */       return propertyDescriptor.getWriteMethod();
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   private Class<?> getParameterClassForMethod(Method method)
/*     */   {
/* 217 */     if (method == null) {
/* 218 */       return null;
/*     */     }
/* 220 */     Class<?>[] classArray = method.getParameterTypes();
/* 221 */     if (classArray.length != 1) {
/* 222 */       return null;
/*     */     }
/* 224 */     return classArray[0];
/*     */   }
/*     */   
/*     */   private AggregationType computeRawAggregationType(Method method)
/*     */   {
/* 229 */     Class<?> parameterClass = getParameterClassForMethod(method);
/* 230 */     if (parameterClass == null) {
/* 231 */       return AggregationType.NOT_FOUND;
/*     */     }
/* 233 */     if (StringToObjectConverter.canBeBuiltFromSimpleString(parameterClass)) {
/* 234 */       return AggregationType.AS_BASIC_PROPERTY;
/*     */     }
/* 236 */     return AggregationType.AS_COMPLEX_PROPERTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isUnequivocallyInstantiable(Class<?> clazz)
/*     */   {
/* 248 */     if (clazz.isInterface()) {
/* 249 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 256 */       Object o = clazz.newInstance();
/* 257 */       if (o != null) {
/* 258 */         return true;
/*     */       }
/* 260 */       return false;
/*     */     }
/*     */     catch (InstantiationException e) {
/* 263 */       return false;
/*     */     } catch (IllegalAccessException e) {}
/* 265 */     return false;
/*     */   }
/*     */   
/*     */   public Class<?> getObjClass()
/*     */   {
/* 270 */     return this.objClass;
/*     */   }
/*     */   
/*     */   public void addComplexProperty(String name, Object complexProperty) {
/* 274 */     Method adderMethod = findAdderMethod(name);
/*     */     
/* 276 */     if (adderMethod != null) {
/* 277 */       Class<?>[] paramTypes = adderMethod.getParameterTypes();
/* 278 */       if (!isSanityCheckSuccessful(name, adderMethod, paramTypes, complexProperty))
/*     */       {
/* 280 */         return;
/*     */       }
/* 282 */       invokeMethodWithSingleParameterOnThisObject(adderMethod, complexProperty);
/*     */     } else {
/* 284 */       addError("Could not find method [add" + name + "] in class [" + this.objClass.getName() + "].");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void invokeMethodWithSingleParameterOnThisObject(Method method, Object parameter)
/*     */   {
/* 291 */     Class<?> ccc = parameter.getClass();
/*     */     try {
/* 293 */       method.invoke(this.obj, new Object[] { parameter });
/*     */     } catch (Exception e) {
/* 295 */       addError("Could not invoke method " + method.getName() + " in class " + this.obj.getClass().getName() + " with parameter of type " + ccc.getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBasicProperty(String name, String strValue)
/*     */   {
/* 303 */     if (strValue == null) {
/* 304 */       return;
/*     */     }
/*     */     
/* 307 */     name = capitalizeFirstLetter(name);
/* 308 */     Method adderMethod = findAdderMethod(name);
/*     */     
/* 310 */     if (adderMethod == null) {
/* 311 */       addError("No adder for property [" + name + "].");
/* 312 */       return;
/*     */     }
/*     */     
/* 315 */     Class<?>[] paramTypes = adderMethod.getParameterTypes();
/* 316 */     isSanityCheckSuccessful(name, adderMethod, paramTypes, strValue);
/*     */     Object arg;
/*     */     try
/*     */     {
/* 320 */       arg = StringToObjectConverter.convertArg(this, strValue, paramTypes[0]);
/*     */     } catch (Throwable t) {
/* 322 */       addError("Conversion to type [" + paramTypes[0] + "] failed. ", t);
/* 323 */       return;
/*     */     }
/* 325 */     if (arg != null) {
/* 326 */       invokeMethodWithSingleParameterOnThisObject(adderMethod, strValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setComplexProperty(String name, Object complexProperty) {
/* 331 */     String dName = Introspector.decapitalize(name);
/* 332 */     PropertyDescriptor propertyDescriptor = getPropertyDescriptor(dName);
/*     */     
/* 334 */     if (propertyDescriptor == null) {
/* 335 */       addWarn("Could not find PropertyDescriptor for [" + name + "] in " + this.objClass.getName());
/*     */       
/*     */ 
/* 338 */       return;
/*     */     }
/*     */     
/* 341 */     Method setter = propertyDescriptor.getWriteMethod();
/*     */     
/* 343 */     if (setter == null) {
/* 344 */       addWarn("Not setter method for property [" + name + "] in " + this.obj.getClass().getName());
/*     */       
/*     */ 
/* 347 */       return;
/*     */     }
/*     */     
/* 350 */     Class<?>[] paramTypes = setter.getParameterTypes();
/*     */     
/* 352 */     if (!isSanityCheckSuccessful(name, setter, paramTypes, complexProperty)) {
/* 353 */       return;
/*     */     }
/*     */     try {
/* 356 */       invokeMethodWithSingleParameterOnThisObject(setter, complexProperty);
/*     */     }
/*     */     catch (Exception e) {
/* 359 */       addError("Could not set component " + this.obj + " for parent component " + this.obj, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isSanityCheckSuccessful(String name, Method method, Class<?>[] params, Object complexProperty)
/*     */   {
/* 366 */     Class<?> ccc = complexProperty.getClass();
/* 367 */     if (params.length != 1) {
/* 368 */       addError("Wrong number of parameters in setter method for property [" + name + "] in " + this.obj.getClass().getName());
/*     */       
/*     */ 
/* 371 */       return false;
/*     */     }
/*     */     
/* 374 */     if (!params[0].isAssignableFrom(complexProperty.getClass())) {
/* 375 */       addError("A \"" + ccc.getName() + "\" object is not assignable to a \"" + params[0].getName() + "\" variable.");
/*     */       
/* 377 */       addError("The class \"" + params[0].getName() + "\" was loaded by ");
/* 378 */       addError("[" + params[0].getClassLoader() + "] whereas object of type ");
/* 379 */       addError("\"" + ccc.getName() + "\" was loaded by [" + ccc.getClassLoader() + "].");
/*     */       
/* 381 */       return false;
/*     */     }
/*     */     
/* 384 */     return true;
/*     */   }
/*     */   
/*     */   private String capitalizeFirstLetter(String name) {
/* 388 */     return name.substring(0, 1).toUpperCase() + name.substring(1);
/*     */   }
/*     */   
/*     */   protected Method getMethod(String methodName) {
/* 392 */     if (this.methodDescriptors == null) {
/* 393 */       introspect();
/*     */     }
/*     */     
/* 396 */     for (int i = 0; i < this.methodDescriptors.length; i++) {
/* 397 */       if (methodName.equals(this.methodDescriptors[i].getName())) {
/* 398 */         return this.methodDescriptors[i].getMethod();
/*     */       }
/*     */     }
/*     */     
/* 402 */     return null;
/*     */   }
/*     */   
/*     */   protected PropertyDescriptor getPropertyDescriptor(String name) {
/* 406 */     if (this.propertyDescriptors == null) {
/* 407 */       introspect();
/*     */     }
/*     */     
/* 410 */     for (int i = 0; i < this.propertyDescriptors.length; i++)
/*     */     {
/*     */ 
/* 413 */       if (name.equals(this.propertyDescriptors[i].getName()))
/*     */       {
/* 415 */         return this.propertyDescriptors[i];
/*     */       }
/*     */     }
/*     */     
/* 419 */     return null;
/*     */   }
/*     */   
/*     */   public Object getObj() {
/* 423 */     return this.obj;
/*     */   }
/*     */   
/*     */   Method getRelevantMethod(String name, AggregationType aggregationType) {
/* 427 */     String cName = capitalizeFirstLetter(name);
/*     */     Method relevantMethod;
/* 429 */     if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY_COLLECTION) {
/* 430 */       relevantMethod = findAdderMethod(cName); } else { Method relevantMethod;
/* 431 */       if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY) {
/* 432 */         relevantMethod = findSetterMethod(cName);
/*     */       } else
/* 434 */         throw new IllegalStateException(aggregationType + " not allowed here"); }
/*     */     Method relevantMethod;
/* 436 */     return relevantMethod;
/*     */   }
/*     */   
/*     */ 
/*     */   <T extends Annotation> T getAnnotation(String name, Class<T> annonationClass, Method relevantMethod)
/*     */   {
/* 442 */     if (relevantMethod != null) {
/* 443 */       return relevantMethod.getAnnotation(annonationClass);
/*     */     }
/* 445 */     return null;
/*     */   }
/*     */   
/*     */   Class<?> getDefaultClassNameByAnnonation(String name, Method relevantMethod)
/*     */   {
/* 450 */     DefaultClass defaultClassAnnon = (DefaultClass)getAnnotation(name, DefaultClass.class, relevantMethod);
/*     */     
/* 452 */     if (defaultClassAnnon != null) {
/* 453 */       return defaultClassAnnon.value();
/*     */     }
/* 455 */     return null;
/*     */   }
/*     */   
/*     */   Class<?> getByConcreteType(String name, Method relevantMethod)
/*     */   {
/* 460 */     Class<?> paramType = getParameterClassForMethod(relevantMethod);
/* 461 */     if (paramType == null) {
/* 462 */       return null;
/*     */     }
/*     */     
/* 465 */     boolean isUnequivocallyInstantiable = isUnequivocallyInstantiable(paramType);
/* 466 */     if (isUnequivocallyInstantiable) {
/* 467 */       return paramType;
/*     */     }
/* 469 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getClassNameViaImplicitRules(String name, AggregationType aggregationType, DefaultNestedComponentRegistry registry)
/*     */   {
/* 477 */     Class<?> registryResult = registry.findDefaultComponentType(this.obj.getClass(), name);
/*     */     
/* 479 */     if (registryResult != null) {
/* 480 */       return registryResult;
/*     */     }
/*     */     
/* 483 */     Method relevantMethod = getRelevantMethod(name, aggregationType);
/* 484 */     if (relevantMethod == null) {
/* 485 */       return null;
/*     */     }
/* 487 */     Class<?> byAnnotation = getDefaultClassNameByAnnonation(name, relevantMethod);
/* 488 */     if (byAnnotation != null) {
/* 489 */       return byAnnotation;
/*     */     }
/* 491 */     return getByConcreteType(name, relevantMethod);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\util\PropertySetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */