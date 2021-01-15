/*     */ package ch.qos.logback.classic.gaffer;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentDelegate
/*     */   extends ContextAwareBase
/*     */   implements GroovyObject
/*     */ {
/*     */   public ComponentDelegate(Object arg1) {}
/*     */   
/*     */   public String getLabel()
/*     */   {
/*  34 */     CallSite[] arrayOfCallSite = $getCallSiteArray();return "component";return null; }
/*     */   
/*  36 */   public String getLabelFistLetterInUpperCase() { CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) return (String)ShortTypeHandling.castToString(arrayOfCallSite[0].call(arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].callCurrent(this), Integer.valueOf(0))), arrayOfCallSite[4].call(arrayOfCallSite[5].callCurrent(this), Integer.valueOf(1)))); else return (String)ShortTypeHandling.castToString(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(getLabel(), Integer.valueOf(0))), arrayOfCallSite[9].call(getLabel(), Integer.valueOf(1)))); return null;
/*     */   }
/*     */   
/*  39 */   public void methodMissing(String name, Object args) { CallSite[] arrayOfCallSite = $getCallSiteArray();NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[10].call(PropertyUtil.class, this.component, name), NestingType.class);
/*  40 */     if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[11].callGetProperty(NestingType.class))) {
/*  41 */       arrayOfCallSite[12].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[13].callCurrent(this), arrayOfCallSite[14].callCurrent(this), arrayOfCallSite[15].callGetProperty(arrayOfCallSite[16].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*  42 */       return;
/*     */     }
/*     */     
/*  45 */     String subComponentName = null;
/*  46 */     Class clazz = null;
/*  47 */     Closure closure = null;
/*  48 */     Object localObject1 = arrayOfCallSite[17].callCurrent(this, args);subComponentName = (String)ShortTypeHandling.castToString(arrayOfCallSite[18].call(localObject1, Integer.valueOf(0)));clazz = (Class)ShortTypeHandling.castToClass(arrayOfCallSite[19].call(localObject1, Integer.valueOf(1)));closure = (Closure)ScriptBytecodeAdapter.castToType(arrayOfCallSite[20].call(localObject1, Integer.valueOf(2)), Closure.class);
/*  49 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(clazz, null)) {
/*  50 */         Object subComponent = arrayOfCallSite[21].call(clazz);
/*  51 */         String str1; if (((DefaultTypeTransformation.booleanUnbox(subComponentName)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[22].call(subComponent, name))) ? 1 : 0) != 0) {
/*  52 */           str1 = subComponentName;ScriptBytecodeAdapter.setProperty(str1, null, subComponent, "name"); }
/*     */         Object localObject2;
/*  54 */         if ((subComponent instanceof ContextAware)) {
/*  55 */           localObject2 = arrayOfCallSite[23].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject2, null, subComponent, "context");
/*     */         }
/*  57 */         if (DefaultTypeTransformation.booleanUnbox(closure)) {
/*  58 */           ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
/*     */           
/*  60 */           arrayOfCallSite[25].callCurrent(this, subDelegate);
/*  61 */           Object localObject3 = arrayOfCallSite[26].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject3, ComponentDelegate.class, subDelegate, "context");
/*  62 */           arrayOfCallSite[27].callCurrent(this, subComponent);
/*  63 */           ComponentDelegate localComponentDelegate1 = subDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate1, ComponentDelegate.class, closure, "delegate");
/*  64 */           Object localObject4 = arrayOfCallSite[28].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject4, ComponentDelegate.class, closure, "resolveStrategy");
/*  65 */           arrayOfCallSite[29].call(closure);
/*     */         }
/*  67 */         if ((((subComponent instanceof LifeCycle)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[30].call(NoAutoStartUtil.class, subComponent))) ? 1 : 0) != 0) {
/*  68 */           arrayOfCallSite[31].call(subComponent);
/*     */         }
/*  70 */         arrayOfCallSite[32].call(PropertyUtil.class, nestingType, this.component, subComponent, name);
/*     */       } else {
/*  72 */         arrayOfCallSite[33].callCurrent(this, new GStringImpl(new Object[] { name, arrayOfCallSite[34].callCurrent(this), arrayOfCallSite[35].callCurrent(this), arrayOfCallSite[36].callGetProperty(arrayOfCallSite[37].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" }));
/*     */       }
/*     */     }
/*  49 */     else if (ScriptBytecodeAdapter.compareNotEqual(clazz, null)) {
/*  50 */       Object subComponent = arrayOfCallSite[38].call(clazz);
/*  51 */       String str2; if (((DefaultTypeTransformation.booleanUnbox(subComponentName)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[39].call(subComponent, name))) ? 1 : 0) != 0) {
/*  52 */         str2 = subComponentName;ScriptBytecodeAdapter.setProperty(str2, null, subComponent, "name"); }
/*     */       Object localObject5;
/*  54 */       if ((subComponent instanceof ContextAware)) {
/*  55 */         localObject5 = arrayOfCallSite[40].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject5, null, subComponent, "context");
/*     */       }
/*  57 */       if (DefaultTypeTransformation.booleanUnbox(closure)) {
/*  58 */         ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
/*     */         
/*  60 */         arrayOfCallSite[42].callCurrent(this, subDelegate);
/*  61 */         Object localObject6 = arrayOfCallSite[43].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject6, ComponentDelegate.class, subDelegate, "context");
/*  62 */         arrayOfCallSite[44].callCurrent(this, subComponent);
/*  63 */         ComponentDelegate localComponentDelegate2 = subDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate2, ComponentDelegate.class, closure, "delegate");
/*  64 */         Object localObject7 = arrayOfCallSite[45].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject7, ComponentDelegate.class, closure, "resolveStrategy");
/*  65 */         arrayOfCallSite[46].call(closure);
/*     */       }
/*  67 */       if ((((subComponent instanceof LifeCycle)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[47].call(NoAutoStartUtil.class, subComponent))) ? 1 : 0) != 0) {
/*  68 */         arrayOfCallSite[48].call(subComponent);
/*     */       }
/*  70 */       arrayOfCallSite[49].call(PropertyUtil.class, nestingType, this.component, subComponent, name);
/*     */     } else {
/*  72 */       arrayOfCallSite[50].callCurrent(this, new GStringImpl(new Object[] { name, getLabel(), getComponentName(), arrayOfCallSite[51].callGetProperty(arrayOfCallSite[52].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" }));
/*     */     }
/*     */   }
/*     */   
/*     */   public void cascadeFields(ComponentDelegate subDelegate) {
/*  77 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String k = null; Object localObject; for (Iterator localIterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[53].call(this.fieldsToCascade), Iterator.class); localIterator.hasNext(); 
/*  78 */         ScriptBytecodeAdapter.setProperty(localObject, null, arrayOfCallSite[54].callGroovyObjectGetProperty(subDelegate), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" }))))
/*     */     {
/*  77 */       k = (String)ShortTypeHandling.castToString(localIterator.next());
/*  78 */       localObject = ScriptBytecodeAdapter.getGroovyObjectProperty(ComponentDelegate.class, this, (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" })));
/*     */     }
/*     */   }
/*     */   
/*     */   public void injectParent(Object subComponent) {
/*  83 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); Object localObject; if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[55].call(subComponent, "parent"))) {
/*  84 */       localObject = this.component;ScriptBytecodeAdapter.setProperty(localObject, null, subComponent, "parent");
/*     */     }
/*     */   }
/*     */   
/*     */   public void propertyMissing(String name, Object value) {
/*  89 */     CallSite[] arrayOfCallSite = $getCallSiteArray();NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[56].call(PropertyUtil.class, this.component, name), NestingType.class);
/*  90 */     if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[57].callGetProperty(NestingType.class))) {
/*  91 */       arrayOfCallSite[58].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[59].callCurrent(this), arrayOfCallSite[60].callCurrent(this), arrayOfCallSite[61].callGetProperty(arrayOfCallSite[62].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*  92 */       return;
/*     */     }
/*  94 */     arrayOfCallSite[63].call(PropertyUtil.class, nestingType, this.component, value, name);
/*     */   }
/*     */   
/*     */   public Object analyzeArgs(Object... args)
/*     */   {
/*  99 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String name = null;
/* 100 */     Class clazz = null;
/* 101 */     Closure closure = null;
/*     */     
/* 103 */     if (ScriptBytecodeAdapter.compareGreaterThan(arrayOfCallSite[64].call(args), Integer.valueOf(3))) {
/* 104 */       arrayOfCallSite[65].callCurrent(this, new GStringImpl(new Object[] { args }, new String[] { "At most 3 arguments allowed but you passed ", "" }));
/* 105 */       return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });
/*     */     }
/*     */     
/* 108 */     if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if ((arrayOfCallSite[66].call(args, Integer.valueOf(-1)) instanceof Closure)) {
/* 109 */         Object localObject1 = arrayOfCallSite[67].call(args, Integer.valueOf(-1));closure = (Closure)ScriptBytecodeAdapter.castToType(localObject1, Closure.class); Object 
/* 110 */           tmp202_197 = arrayOfCallSite[68].call(args, arrayOfCallSite[69].call(args, Integer.valueOf(-1)));args = (Object[])ScriptBytecodeAdapter.castToType(tmp202_197, Object[].class);tmp202_197;
/*     */       }
/*     */     }
/* 108 */     else if ((BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()) instanceof Closure)) {
/* 109 */       Object localObject2 = BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue());closure = (Closure)ScriptBytecodeAdapter.castToType(localObject2, Closure.class); Object 
/* 110 */         tmp287_282 = arrayOfCallSite[70].call(args, BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()));args = (Object[])ScriptBytecodeAdapter.castToType(tmp287_282, Object[].class);tmp287_282; }
/*     */     Object localObject3;
/*     */     Object localObject4;
/* 113 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[71].call(args), Integer.valueOf(1))) {
/* 114 */         localObject3 = arrayOfCallSite[72].callCurrent(this, arrayOfCallSite[73].call(args, Integer.valueOf(0)));clazz = (Class)ShortTypeHandling.castToClass(localObject3);
/*     */       }
/*     */     }
/* 113 */     else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[74].call(args), Integer.valueOf(1))) {
/* 114 */       localObject4 = arrayOfCallSite[75].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0));clazz = (Class)ShortTypeHandling.castToClass(localObject4); }
/*     */     Object localObject6;
/*     */     Object localObject8;
/* 117 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[76].call(args), Integer.valueOf(2))) {
/* 118 */         Object localObject5 = arrayOfCallSite[77].callCurrent(this, arrayOfCallSite[78].call(args, Integer.valueOf(0)));name = (String)ShortTypeHandling.castToString(localObject5);
/* 119 */         localObject6 = arrayOfCallSite[79].callCurrent(this, arrayOfCallSite[80].call(args, Integer.valueOf(1)));clazz = (Class)ShortTypeHandling.castToClass(localObject6);
/*     */       }
/*     */     }
/* 117 */     else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[81].call(args), Integer.valueOf(2))) {
/* 118 */       Object localObject7 = arrayOfCallSite[82].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0));name = (String)ShortTypeHandling.castToString(localObject7);
/* 119 */       localObject8 = arrayOfCallSite[83].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 1));clazz = (Class)ShortTypeHandling.castToClass(localObject8);
/*     */     }
/*     */     
/* 122 */     return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });return null;
/*     */   }
/*     */   
/*     */   public Class parseClassArgument(Object arg) {
/* 126 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((arg instanceof Class)) {
/* 127 */       return (Class)ShortTypeHandling.castToClass(arg);
/* 128 */     } else if ((arg instanceof String)) {
/* 129 */       return Class.forName((String)ShortTypeHandling.castToString(arg));
/*     */     } else {
/* 131 */       arrayOfCallSite[84].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[85].callGetProperty(arrayOfCallSite[86].call(arg)) }, new String[] { "Unexpected argument type ", "" }));
/* 132 */       return (Class)ShortTypeHandling.castToClass(null); } return null;
/*     */   }
/*     */   
/*     */   public String parseNameArgument(Object arg)
/*     */   {
/* 137 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((arg instanceof String)) {
/* 138 */       return (String)ShortTypeHandling.castToString(arg);
/*     */     } else {
/* 140 */       arrayOfCallSite[87].callCurrent(this, "With 2 or 3 arguments, the first argument must be the component name, i.e of type string");
/* 141 */       return (String)ShortTypeHandling.castToString(null); } return null;
/*     */   }
/*     */   
/*     */   public String getComponentName()
/*     */   {
/* 146 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[88].call(this.component, "name"))) {
/* 147 */       return (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { arrayOfCallSite[89].callGetProperty(this.component) }, new String[] { "[", "]" }));
/*     */     } else
/* 149 */       return ""; return null;
/*     */   }
/*     */   
/*     */   public final Object getComponent()
/*     */   {
/*     */     return this.component;
/*     */   }
/*     */   
/*     */   public final List getFieldsToCascade()
/*     */   {
/*     */     return this.fieldsToCascade;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\gaffer\ComponentDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */