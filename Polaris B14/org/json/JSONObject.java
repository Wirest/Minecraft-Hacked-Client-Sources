/*      */ package org.json;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONObject
/*      */ {
/*      */   private final Map<String, Object> map;
/*      */   
/*      */   private static final class Null
/*      */   {
/*      */     protected final Object clone()
/*      */     {
/*  118 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object object)
/*      */     {
/*  131 */       return (object == null) || (object == this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  140 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  150 */       return "null";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  165 */   public static final Object NULL = new Null(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject()
/*      */   {
/*  177 */     this.map = new HashMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(JSONObject jo, String[] names)
/*      */   {
/*  191 */     this(names.length);
/*  192 */     for (int i = 0; i < names.length; i++) {
/*      */       try {
/*  194 */         putOnce(names[i], jo.opt(names[i]));
/*      */       }
/*      */       catch (Exception localException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(JSONTokener x)
/*      */     throws JSONException
/*      */   {
/*  210 */     this();
/*      */     
/*      */ 
/*      */ 
/*  214 */     if (x.nextClean() != '{') {
/*  215 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     for (;;) {
/*  218 */       char c = x.nextClean();
/*  219 */       switch (c) {
/*      */       case '\000': 
/*  221 */         throw x.syntaxError("A JSONObject text must end with '}'");
/*      */       case '}': 
/*  223 */         return;
/*      */       }
/*  225 */       x.back();
/*  226 */       String key = x.nextValue().toString();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  231 */       c = x.nextClean();
/*  232 */       if (c != ':') {
/*  233 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  238 */       if (key != null)
/*      */       {
/*  240 */         if (opt(key) != null)
/*      */         {
/*  242 */           throw x.syntaxError("Duplicate key \"" + key + "\"");
/*      */         }
/*      */         
/*  245 */         Object value = x.nextValue();
/*  246 */         if (value != null) {
/*  247 */           put(key, value);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  253 */       switch (x.nextClean()) {
/*      */       case ',': 
/*      */       case ';': 
/*  256 */         if (x.nextClean() == '}') {
/*  257 */           return;
/*      */         }
/*  259 */         x.back();
/*      */       }
/*      */     }
/*  262 */     return;
/*      */     
/*  264 */     throw x.syntaxError("Expected a ',' or '}'");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(Map<?, ?> m)
/*      */   {
/*  281 */     if (m == null) {
/*  282 */       this.map = new HashMap();
/*      */     } else {
/*  284 */       this.map = new HashMap(m.size());
/*  285 */       for (Map.Entry<?, ?> e : m.entrySet()) {
/*  286 */         if (e.getKey() == null) {
/*  287 */           throw new NullPointerException("Null key.");
/*      */         }
/*  289 */         Object value = e.getValue();
/*  290 */         if (value != null) {
/*  291 */           this.map.put(String.valueOf(e.getKey()), wrap(value));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(Object bean)
/*      */   {
/*  356 */     this();
/*  357 */     populateMap(bean);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(Object object, String[] names)
/*      */   {
/*  375 */     this(names.length);
/*  376 */     Class<?> c = object.getClass();
/*  377 */     for (int i = 0; i < names.length; i++) {
/*  378 */       String name = names[i];
/*      */       try {
/*  380 */         putOpt(name, c.getField(name).get(object));
/*      */       }
/*      */       catch (Exception localException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(String source)
/*      */     throws JSONException
/*      */   {
/*  399 */     this(new JSONTokener(source));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject(String baseName, Locale locale)
/*      */     throws JSONException
/*      */   {
/*  413 */     this();
/*  414 */     ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
/*  415 */       Thread.currentThread().getContextClassLoader());
/*      */     
/*      */ 
/*      */ 
/*  419 */     Enumeration<String> keys = bundle.getKeys();
/*  420 */     while (keys.hasMoreElements()) {
/*  421 */       Object key = keys.nextElement();
/*  422 */       if (key != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  428 */         String[] path = ((String)key).split("\\.");
/*  429 */         int last = path.length - 1;
/*  430 */         JSONObject target = this;
/*  431 */         for (int i = 0; i < last; i++) {
/*  432 */           String segment = path[i];
/*  433 */           JSONObject nextTarget = target.optJSONObject(segment);
/*  434 */           if (nextTarget == null) {
/*  435 */             nextTarget = new JSONObject();
/*  436 */             target.put(segment, nextTarget);
/*      */           }
/*  438 */           target = nextTarget;
/*      */         }
/*  440 */         target.put(path[last], bundle.getString((String)key));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JSONObject(int initialCapacity)
/*      */   {
/*  453 */     this.map = new HashMap(initialCapacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject accumulate(String key, Object value)
/*      */     throws JSONException
/*      */   {
/*  478 */     testValidity(value);
/*  479 */     Object object = opt(key);
/*  480 */     if (object == null) {
/*  481 */       put(key, 
/*  482 */         (value instanceof JSONArray) ? new JSONArray().put(value) : 
/*  483 */         value);
/*  484 */     } else if ((object instanceof JSONArray)) {
/*  485 */       ((JSONArray)object).put(value);
/*      */     } else {
/*  487 */       put(key, new JSONArray().put(object).put(value));
/*      */     }
/*  489 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject append(String key, Object value)
/*      */     throws JSONException
/*      */   {
/*  510 */     testValidity(value);
/*  511 */     Object object = opt(key);
/*  512 */     if (object == null) {
/*  513 */       put(key, new JSONArray().put(value));
/*  514 */     } else if ((object instanceof JSONArray)) {
/*  515 */       put(key, ((JSONArray)object).put(value));
/*      */     } else {
/*  517 */       throw new JSONException("JSONObject[" + key + 
/*  518 */         "] is not a JSONArray.");
/*      */     }
/*  520 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String doubleToString(double d)
/*      */   {
/*  532 */     if ((Double.isInfinite(d)) || (Double.isNaN(d))) {
/*  533 */       return "null";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  538 */     String string = Double.toString(d);
/*  539 */     if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0) && 
/*  540 */       (string.indexOf('E') < 0)) {
/*  541 */       while (string.endsWith("0")) {
/*  542 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  544 */       if (string.endsWith(".")) {
/*  545 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     }
/*  548 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object get(String key)
/*      */     throws JSONException
/*      */   {
/*  561 */     if (key == null) {
/*  562 */       throw new JSONException("Null key.");
/*      */     }
/*  564 */     Object object = opt(key);
/*  565 */     if (object == null) {
/*  566 */       throw new JSONException("JSONObject[" + quote(key) + "] not found.");
/*      */     }
/*  568 */     return object;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, String key)
/*      */     throws JSONException
/*      */   {
/*  584 */     E val = optEnum(clazz, key);
/*  585 */     if (val == null)
/*      */     {
/*      */ 
/*      */ 
/*  589 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  590 */         "] is not an enum of type " + quote(clazz.getSimpleName()) + 
/*  591 */         ".");
/*      */     }
/*  593 */     return val;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getBoolean(String key)
/*      */     throws JSONException
/*      */   {
/*  607 */     Object object = get(key);
/*  608 */     if ((object.equals(Boolean.FALSE)) || (
/*  609 */       ((object instanceof String)) && 
/*  610 */       (((String)object).equalsIgnoreCase("false"))))
/*  611 */       return false;
/*  612 */     if ((object.equals(Boolean.TRUE)) || (
/*  613 */       ((object instanceof String)) && 
/*  614 */       (((String)object).equalsIgnoreCase("true")))) {
/*  615 */       return true;
/*      */     }
/*  617 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  618 */       "] is not a Boolean.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigInteger getBigInteger(String key)
/*      */     throws JSONException
/*      */   {
/*  632 */     Object object = get(key);
/*      */     try {
/*  634 */       return new BigInteger(object.toString());
/*      */     } catch (Exception e) {
/*  636 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  637 */         "] could not be converted to BigInteger.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal getBigDecimal(String key)
/*      */     throws JSONException
/*      */   {
/*  652 */     Object object = get(key);
/*  653 */     if ((object instanceof BigDecimal)) {
/*  654 */       return (BigDecimal)object;
/*      */     }
/*      */     try {
/*  657 */       return new BigDecimal(object.toString());
/*      */     } catch (Exception e) {
/*  659 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  660 */         "] could not be converted to BigDecimal.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDouble(String key)
/*      */     throws JSONException
/*      */   {
/*  675 */     Object object = get(key);
/*      */     try {
/*  677 */       return (object instanceof Number) ? ((Number)object).doubleValue() : 
/*  678 */         Double.parseDouble(object.toString());
/*      */     } catch (Exception e) {
/*  680 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  681 */         "] is not a number.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getFloat(String key)
/*      */     throws JSONException
/*      */   {
/*  696 */     Object object = get(key);
/*      */     try {
/*  698 */       return (object instanceof Number) ? ((Number)object).floatValue() : 
/*  699 */         Float.parseFloat(object.toString());
/*      */     } catch (Exception e) {
/*  701 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  702 */         "] is not a number.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number getNumber(String key)
/*      */     throws JSONException
/*      */   {
/*  717 */     Object object = get(key);
/*      */     try {
/*  719 */       if ((object instanceof Number)) {
/*  720 */         return (Number)object;
/*      */       }
/*  722 */       return stringToNumber(object.toString());
/*      */     } catch (Exception e) {
/*  724 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  725 */         "] is not a number.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getInt(String key)
/*      */     throws JSONException
/*      */   {
/*  740 */     Object object = get(key);
/*      */     try {
/*  742 */       return (object instanceof Number) ? ((Number)object).intValue() : 
/*  743 */         Integer.parseInt((String)object);
/*      */     } catch (Exception e) {
/*  745 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  746 */         "] is not an int.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray getJSONArray(String key)
/*      */     throws JSONException
/*      */   {
/*  760 */     Object object = get(key);
/*  761 */     if ((object instanceof JSONArray)) {
/*  762 */       return (JSONArray)object;
/*      */     }
/*  764 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  765 */       "] is not a JSONArray.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject getJSONObject(String key)
/*      */     throws JSONException
/*      */   {
/*  778 */     Object object = get(key);
/*  779 */     if ((object instanceof JSONObject)) {
/*  780 */       return (JSONObject)object;
/*      */     }
/*  782 */     throw new JSONException("JSONObject[" + quote(key) + 
/*  783 */       "] is not a JSONObject.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLong(String key)
/*      */     throws JSONException
/*      */   {
/*  797 */     Object object = get(key);
/*      */     try {
/*  799 */       return (object instanceof Number) ? ((Number)object).longValue() : 
/*  800 */         Long.parseLong((String)object);
/*      */     } catch (Exception e) {
/*  802 */       throw new JSONException("JSONObject[" + quote(key) + 
/*  803 */         "] is not a long.", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getNames(JSONObject jo)
/*      */   {
/*  813 */     int length = jo.length();
/*  814 */     if (length == 0) {
/*  815 */       return null;
/*      */     }
/*  817 */     return (String[])jo.keySet().toArray(new String[length]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getNames(Object object)
/*      */   {
/*  826 */     if (object == null) {
/*  827 */       return null;
/*      */     }
/*  829 */     Class<?> klass = object.getClass();
/*  830 */     Field[] fields = klass.getFields();
/*  831 */     int length = fields.length;
/*  832 */     if (length == 0) {
/*  833 */       return null;
/*      */     }
/*  835 */     String[] names = new String[length];
/*  836 */     for (int i = 0; i < length; i++) {
/*  837 */       names[i] = fields[i].getName();
/*      */     }
/*  839 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(String key)
/*      */     throws JSONException
/*      */   {
/*  852 */     Object object = get(key);
/*  853 */     if ((object instanceof String)) {
/*  854 */       return (String)object;
/*      */     }
/*  856 */     throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean has(String key)
/*      */   {
/*  867 */     return this.map.containsKey(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject increment(String key)
/*      */     throws JSONException
/*      */   {
/*  883 */     Object value = opt(key);
/*  884 */     if (value == null) {
/*  885 */       put(key, 1);
/*  886 */     } else if ((value instanceof BigInteger)) {
/*  887 */       put(key, ((BigInteger)value).add(BigInteger.ONE));
/*  888 */     } else if ((value instanceof BigDecimal)) {
/*  889 */       put(key, ((BigDecimal)value).add(BigDecimal.ONE));
/*  890 */     } else if ((value instanceof Integer)) {
/*  891 */       put(key, ((Integer)value).intValue() + 1);
/*  892 */     } else if ((value instanceof Long)) {
/*  893 */       put(key, ((Long)value).longValue() + 1L);
/*  894 */     } else if ((value instanceof Double)) {
/*  895 */       put(key, ((Double)value).doubleValue() + 1.0D);
/*  896 */     } else if ((value instanceof Float)) {
/*  897 */       put(key, ((Float)value).floatValue() + 1.0F);
/*      */     } else {
/*  899 */       throw new JSONException("Unable to increment [" + quote(key) + "].");
/*      */     }
/*  901 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNull(String key)
/*      */   {
/*  914 */     return NULL.equals(opt(key));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<String> keys()
/*      */   {
/*  926 */     return keySet().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> keySet()
/*      */   {
/*  938 */     return this.map.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Set<Map.Entry<String, Object>> entrySet()
/*      */   {
/*  954 */     return this.map.entrySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int length()
/*      */   {
/*  963 */     return this.map.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray names()
/*      */   {
/*  974 */     if (this.map.isEmpty()) {
/*  975 */       return null;
/*      */     }
/*  977 */     return new JSONArray(this.map.keySet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String numberToString(Number number)
/*      */     throws JSONException
/*      */   {
/*  990 */     if (number == null) {
/*  991 */       throw new JSONException("Null pointer");
/*      */     }
/*  993 */     testValidity(number);
/*      */     
/*      */ 
/*      */ 
/*  997 */     String string = number.toString();
/*  998 */     if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0) && 
/*  999 */       (string.indexOf('E') < 0)) {
/* 1000 */       while (string.endsWith("0")) {
/* 1001 */         string = string.substring(0, string.length() - 1);
/*      */       }
/* 1003 */       if (string.endsWith(".")) {
/* 1004 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     }
/* 1007 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object opt(String key)
/*      */   {
/* 1018 */     return key == null ? null : this.map.get(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key)
/*      */   {
/* 1031 */     return optEnum(clazz, key, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue)
/*      */   {
/*      */     try
/*      */     {
/* 1048 */       Object val = opt(key);
/* 1049 */       if (NULL.equals(val)) {
/* 1050 */         return defaultValue;
/*      */       }
/* 1052 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */ 
/* 1055 */         return (Enum)val;
/*      */       }
/*      */       
/* 1058 */       return Enum.valueOf(clazz, val.toString());
/*      */     } catch (IllegalArgumentException e) {
/* 1060 */       return defaultValue;
/*      */     } catch (NullPointerException e) {}
/* 1062 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean optBoolean(String key)
/*      */   {
/* 1075 */     return optBoolean(key, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean optBoolean(String key, boolean defaultValue)
/*      */   {
/* 1090 */     Object val = opt(key);
/* 1091 */     if (NULL.equals(val)) {
/* 1092 */       return defaultValue;
/*      */     }
/* 1094 */     if ((val instanceof Boolean)) {
/* 1095 */       return ((Boolean)val).booleanValue();
/*      */     }
/*      */     try
/*      */     {
/* 1099 */       return getBoolean(key);
/*      */     } catch (Exception e) {}
/* 1101 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal optBigDecimal(String key, BigDecimal defaultValue)
/*      */   {
/* 1117 */     Object val = opt(key);
/* 1118 */     if (NULL.equals(val)) {
/* 1119 */       return defaultValue;
/*      */     }
/* 1121 */     if ((val instanceof BigDecimal)) {
/* 1122 */       return (BigDecimal)val;
/*      */     }
/* 1124 */     if ((val instanceof BigInteger)) {
/* 1125 */       return new BigDecimal((BigInteger)val);
/*      */     }
/* 1127 */     if (((val instanceof Double)) || ((val instanceof Float))) {
/* 1128 */       return new BigDecimal(((Number)val).doubleValue());
/*      */     }
/* 1130 */     if (((val instanceof Long)) || ((val instanceof Integer)) || 
/* 1131 */       ((val instanceof Short)) || ((val instanceof Byte))) {
/* 1132 */       return new BigDecimal(((Number)val).longValue());
/*      */     }
/*      */     try
/*      */     {
/* 1136 */       return new BigDecimal(val.toString());
/*      */     } catch (Exception e) {}
/* 1138 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigInteger optBigInteger(String key, BigInteger defaultValue)
/*      */   {
/* 1154 */     Object val = opt(key);
/* 1155 */     if (NULL.equals(val)) {
/* 1156 */       return defaultValue;
/*      */     }
/* 1158 */     if ((val instanceof BigInteger)) {
/* 1159 */       return (BigInteger)val;
/*      */     }
/* 1161 */     if ((val instanceof BigDecimal)) {
/* 1162 */       return ((BigDecimal)val).toBigInteger();
/*      */     }
/* 1164 */     if (((val instanceof Double)) || ((val instanceof Float))) {
/* 1165 */       return new BigDecimal(((Number)val).doubleValue()).toBigInteger();
/*      */     }
/* 1167 */     if (((val instanceof Long)) || ((val instanceof Integer)) || 
/* 1168 */       ((val instanceof Short)) || ((val instanceof Byte))) {
/* 1169 */       return BigInteger.valueOf(((Number)val).longValue());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1178 */       String valStr = val.toString();
/* 1179 */       if (isDecimalNotation(valStr)) {
/* 1180 */         return new BigDecimal(valStr).toBigInteger();
/*      */       }
/* 1182 */       return new BigInteger(valStr);
/*      */     } catch (Exception e) {}
/* 1184 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double optDouble(String key)
/*      */   {
/* 1198 */     return optDouble(key, NaN.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double optDouble(String key, double defaultValue)
/*      */   {
/* 1213 */     Object val = opt(key);
/* 1214 */     if (NULL.equals(val)) {
/* 1215 */       return defaultValue;
/*      */     }
/* 1217 */     if ((val instanceof Number)) {
/* 1218 */       return ((Number)val).doubleValue();
/*      */     }
/* 1220 */     if ((val instanceof String)) {
/*      */       try {
/* 1222 */         return Double.parseDouble((String)val);
/*      */       } catch (Exception e) {
/* 1224 */         return defaultValue;
/*      */       }
/*      */     }
/* 1227 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float optFloat(String key)
/*      */   {
/* 1240 */     return optFloat(key, NaN.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float optFloat(String key, float defaultValue)
/*      */   {
/* 1255 */     Object val = opt(key);
/* 1256 */     if (NULL.equals(val)) {
/* 1257 */       return defaultValue;
/*      */     }
/* 1259 */     if ((val instanceof Number)) {
/* 1260 */       return ((Number)val).floatValue();
/*      */     }
/* 1262 */     if ((val instanceof String)) {
/*      */       try {
/* 1264 */         return Float.parseFloat((String)val);
/*      */       } catch (Exception e) {
/* 1266 */         return defaultValue;
/*      */       }
/*      */     }
/* 1269 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int optInt(String key)
/*      */   {
/* 1282 */     return optInt(key, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int optInt(String key, int defaultValue)
/*      */   {
/* 1297 */     Object val = opt(key);
/* 1298 */     if (NULL.equals(val)) {
/* 1299 */       return defaultValue;
/*      */     }
/* 1301 */     if ((val instanceof Number)) {
/* 1302 */       return ((Number)val).intValue();
/*      */     }
/*      */     
/* 1305 */     if ((val instanceof String)) {
/*      */       try {
/* 1307 */         return new BigDecimal((String)val).intValue();
/*      */       } catch (Exception e) {
/* 1309 */         return defaultValue;
/*      */       }
/*      */     }
/* 1312 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray optJSONArray(String key)
/*      */   {
/* 1324 */     Object o = opt(key);
/* 1325 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject optJSONObject(String key)
/*      */   {
/* 1337 */     Object object = opt(key);
/* 1338 */     return (object instanceof JSONObject) ? (JSONObject)object : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long optLong(String key)
/*      */   {
/* 1351 */     return optLong(key, 0L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long optLong(String key, long defaultValue)
/*      */   {
/* 1366 */     Object val = opt(key);
/* 1367 */     if (NULL.equals(val)) {
/* 1368 */       return defaultValue;
/*      */     }
/* 1370 */     if ((val instanceof Number)) {
/* 1371 */       return ((Number)val).longValue();
/*      */     }
/*      */     
/* 1374 */     if ((val instanceof String)) {
/*      */       try {
/* 1376 */         return new BigDecimal((String)val).longValue();
/*      */       } catch (Exception e) {
/* 1378 */         return defaultValue;
/*      */       }
/*      */     }
/* 1381 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number optNumber(String key)
/*      */   {
/* 1395 */     return optNumber(key, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number optNumber(String key, Number defaultValue)
/*      */   {
/* 1411 */     Object val = opt(key);
/* 1412 */     if (NULL.equals(val)) {
/* 1413 */       return defaultValue;
/*      */     }
/* 1415 */     if ((val instanceof Number)) {
/* 1416 */       return (Number)val;
/*      */     }
/*      */     
/* 1419 */     if ((val instanceof String)) {
/*      */       try {
/* 1421 */         return stringToNumber((String)val);
/*      */       } catch (Exception e) {
/* 1423 */         return defaultValue;
/*      */       }
/*      */     }
/* 1426 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String optString(String key)
/*      */   {
/* 1439 */     return optString(key, "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String optString(String key, String defaultValue)
/*      */   {
/* 1453 */     Object object = opt(key);
/* 1454 */     return NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void populateMap(Object bean)
/*      */   {
/* 1467 */     Class<?> klass = bean.getClass();
/*      */     
/*      */ 
/*      */ 
/* 1471 */     boolean includeSuperClass = klass.getClassLoader() != null;
/*      */     
/* 1473 */     Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
/* 1474 */     Method[] arrayOfMethod1; int j = (arrayOfMethod1 = methods).length; for (int i = 0; i < j;) { Method method = arrayOfMethod1[i];
/* 1475 */       int modifiers = method.getModifiers();
/* 1476 */       if ((Modifier.isPublic(modifiers)) && 
/* 1477 */         (!Modifier.isStatic(modifiers)) && 
/* 1478 */         (method.getParameterTypes().length == 0) && 
/* 1479 */         (!method.isBridge()) && 
/* 1480 */         (method.getReturnType() != Void.TYPE) && 
/* 1481 */         (isValidMethodName(method.getName()))) {
/* 1482 */         String key = getKeyNameFromMethod(method);
/* 1483 */         if ((key != null) && (!key.isEmpty())) {
/*      */           try {
/* 1485 */             Object result = method.invoke(bean, new Object[0]);
/* 1486 */             if (result != null) {
/* 1487 */               this.map.put(key, wrap(result));
/*      */               
/*      */ 
/*      */ 
/* 1491 */               if ((result instanceof Closeable)) {
/*      */                 try {
/* 1493 */                   ((Closeable)result).close();
/*      */                 }
/*      */                 catch (IOException localIOException) {}
/*      */               }
/*      */             }
/* 1474 */             i++;
/*      */           }
/*      */           catch (IllegalAccessException localIllegalAccessException) {}catch (IllegalArgumentException localIllegalArgumentException) {}catch (InvocationTargetException localInvocationTargetException) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isValidMethodName(String name)
/*      */   {
/* 1508 */     return (!"getClass".equals(name)) && (!"getDeclaringClass".equals(name));
/*      */   }
/*      */   
/*      */   private String getKeyNameFromMethod(Method method) {
/* 1512 */     int ignoreDepth = getAnnotationDepth(method, JSONPropertyIgnore.class);
/* 1513 */     if (ignoreDepth > 0) {
/* 1514 */       int forcedNameDepth = getAnnotationDepth(method, JSONPropertyName.class);
/* 1515 */       if ((forcedNameDepth < 0) || (ignoreDepth <= forcedNameDepth))
/*      */       {
/*      */ 
/* 1518 */         return null;
/*      */       }
/*      */     }
/* 1521 */     JSONPropertyName annotation = (JSONPropertyName)getAnnotation(method, JSONPropertyName.class);
/* 1522 */     if ((annotation != null) && (annotation.value() != null) && (!annotation.value().isEmpty())) {
/* 1523 */       return annotation.value();
/*      */     }
/*      */     
/* 1526 */     String name = method.getName();
/* 1527 */     String key; if ((name.startsWith("get")) && (name.length() > 3)) {
/* 1528 */       key = name.substring(3); } else { String key;
/* 1529 */       if ((name.startsWith("is")) && (name.length() > 2)) {
/* 1530 */         key = name.substring(2);
/*      */       } else {
/* 1532 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */     String key;
/* 1537 */     if (Character.isLowerCase(key.charAt(0))) {
/* 1538 */       return null;
/*      */     }
/* 1540 */     if (key.length() == 1) {
/* 1541 */       key = key.toLowerCase(Locale.ROOT);
/* 1542 */     } else if (!Character.isUpperCase(key.charAt(1))) {
/* 1543 */       key = key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
/*      */     }
/* 1545 */     return key;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <A extends Annotation> A getAnnotation(Method m, Class<A> annotationClass)
/*      */   {
/* 1564 */     if ((m == null) || (annotationClass == null)) {
/* 1565 */       return null;
/*      */     }
/*      */     
/* 1568 */     if (m.isAnnotationPresent(annotationClass)) {
/* 1569 */       return m.getAnnotation(annotationClass);
/*      */     }
/*      */     
/*      */ 
/* 1573 */     Class<?> c = m.getDeclaringClass();
/* 1574 */     if (c.getSuperclass() == null) {
/* 1575 */       return null;
/*      */     }
/*      */     
/*      */     Class[] arrayOfClass;
/* 1579 */     int j = (arrayOfClass = c.getInterfaces()).length; for (int i = 0; i < j; i++) { Class<?> i = arrayOfClass[i];
/*      */       try {
/* 1581 */         Method im = i.getMethod(m.getName(), m.getParameterTypes());
/* 1582 */         return getAnnotation(im, annotationClass);
/*      */       }
/*      */       catch (SecurityException ex) {}catch (NoSuchMethodException localNoSuchMethodException1) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1591 */       return getAnnotation(
/* 1592 */         c.getSuperclass().getMethod(m.getName(), m.getParameterTypes()), 
/* 1593 */         annotationClass);
/*      */     } catch (SecurityException ex) {
/* 1595 */       return null;
/*      */     } catch (NoSuchMethodException ex) {}
/* 1597 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getAnnotationDepth(Method m, Class<? extends Annotation> annotationClass)
/*      */   {
/* 1617 */     if ((m == null) || (annotationClass == null)) {
/* 1618 */       return -1;
/*      */     }
/*      */     
/* 1621 */     if (m.isAnnotationPresent(annotationClass)) {
/* 1622 */       return 1;
/*      */     }
/*      */     
/*      */ 
/* 1626 */     Class<?> c = m.getDeclaringClass();
/* 1627 */     if (c.getSuperclass() == null) {
/* 1628 */       return -1;
/*      */     }
/*      */     
/*      */     Class[] arrayOfClass;
/* 1632 */     int j = (arrayOfClass = c.getInterfaces()).length; for (int i = 0; i < j; i++) { Class<?> i = arrayOfClass[i];
/*      */       try {
/* 1634 */         Method im = i.getMethod(m.getName(), m.getParameterTypes());
/* 1635 */         int d = getAnnotationDepth(im, annotationClass);
/* 1636 */         if (d > 0)
/*      */         {
/* 1638 */           return d + 1;
/*      */         }
/*      */       }
/*      */       catch (SecurityException ex) {}catch (NoSuchMethodException localNoSuchMethodException1) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1648 */       int d = getAnnotationDepth(
/* 1649 */         c.getSuperclass().getMethod(m.getName(), m.getParameterTypes()), 
/* 1650 */         annotationClass);
/* 1651 */       if (d > 0)
/*      */       {
/* 1653 */         return d + 1;
/*      */       }
/* 1655 */       return -1;
/*      */     } catch (SecurityException ex) {
/* 1657 */       return -1;
/*      */     } catch (NoSuchMethodException ex) {}
/* 1659 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, boolean value)
/*      */     throws JSONException
/*      */   {
/* 1677 */     return put(key, value ? Boolean.TRUE : Boolean.FALSE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, Collection<?> value)
/*      */     throws JSONException
/*      */   {
/* 1695 */     return put(key, new JSONArray(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, double value)
/*      */     throws JSONException
/*      */   {
/* 1712 */     return put(key, Double.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, float value)
/*      */     throws JSONException
/*      */   {
/* 1729 */     return put(key, Float.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, int value)
/*      */     throws JSONException
/*      */   {
/* 1746 */     return put(key, Integer.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, long value)
/*      */     throws JSONException
/*      */   {
/* 1763 */     return put(key, Long.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, Map<?, ?> value)
/*      */     throws JSONException
/*      */   {
/* 1781 */     return put(key, new JSONObject(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject put(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1801 */     if (key == null) {
/* 1802 */       throw new NullPointerException("Null key.");
/*      */     }
/* 1804 */     if (value != null) {
/* 1805 */       testValidity(value);
/* 1806 */       this.map.put(key, value);
/*      */     } else {
/* 1808 */       remove(key);
/*      */     }
/* 1810 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject putOnce(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1825 */     if ((key != null) && (value != null)) {
/* 1826 */       if (opt(key) != null) {
/* 1827 */         throw new JSONException("Duplicate key \"" + key + "\"");
/*      */       }
/* 1829 */       return put(key, value);
/*      */     }
/* 1831 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject putOpt(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1849 */     if ((key != null) && (value != null)) {
/* 1850 */       return put(key, value);
/*      */     }
/* 1852 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object query(String jsonPointer)
/*      */   {
/* 1875 */     return query(new JSONPointer(jsonPointer));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object query(JSONPointer jsonPointer)
/*      */   {
/* 1897 */     return jsonPointer.queryFrom(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object optQuery(String jsonPointer)
/*      */   {
/* 1909 */     return optQuery(new JSONPointer(jsonPointer));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object optQuery(JSONPointer jsonPointer)
/*      */   {
/*      */     try
/*      */     {
/* 1922 */       return jsonPointer.queryFrom(this);
/*      */     } catch (JSONPointerException e) {}
/* 1924 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String quote(String string)
/*      */   {
/* 1939 */     StringWriter sw = new StringWriter();
/* 1940 */     synchronized (sw.getBuffer()) {
/*      */       try {
/* 1942 */         return quote(string, sw).toString();
/*      */       }
/*      */       catch (IOException ignored) {
/* 1945 */         return "";
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Writer quote(String string, Writer w) throws IOException {
/* 1951 */     if ((string == null) || (string.length() == 0)) {
/* 1952 */       w.write("\"\"");
/* 1953 */       return w;
/*      */     }
/*      */     
/*      */ 
/* 1957 */     char c = '\000';
/*      */     
/*      */ 
/* 1960 */     int len = string.length();
/*      */     
/* 1962 */     w.write(34);
/* 1963 */     for (int i = 0; i < len; i++) {
/* 1964 */       char b = c;
/* 1965 */       c = string.charAt(i);
/* 1966 */       switch (c) {
/*      */       case '"': 
/*      */       case '\\': 
/* 1969 */         w.write(92);
/* 1970 */         w.write(c);
/* 1971 */         break;
/*      */       case '/': 
/* 1973 */         if (b == '<') {
/* 1974 */           w.write(92);
/*      */         }
/* 1976 */         w.write(c);
/* 1977 */         break;
/*      */       case '\b': 
/* 1979 */         w.write("\\b");
/* 1980 */         break;
/*      */       case '\t': 
/* 1982 */         w.write("\\t");
/* 1983 */         break;
/*      */       case '\n': 
/* 1985 */         w.write("\\n");
/* 1986 */         break;
/*      */       case '\f': 
/* 1988 */         w.write("\\f");
/* 1989 */         break;
/*      */       case '\r': 
/* 1991 */         w.write("\\r");
/* 1992 */         break;
/*      */       default: 
/* 1994 */         if ((c < ' ') || ((c >= '') && (c < ' ')) || (
/* 1995 */           (c >= ' ') && (c < '℀'))) {
/* 1996 */           w.write("\\u");
/* 1997 */           String hhhh = Integer.toHexString(c);
/* 1998 */           w.write("0000", 0, 4 - hhhh.length());
/* 1999 */           w.write(hhhh);
/*      */         } else {
/* 2001 */           w.write(c);
/*      */         }
/*      */         break; }
/*      */     }
/* 2005 */     w.write(34);
/* 2006 */     return w;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object remove(String key)
/*      */   {
/* 2018 */     return this.map.remove(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean similar(Object other)
/*      */   {
/*      */     try
/*      */     {
/* 2031 */       if (!(other instanceof JSONObject)) {
/* 2032 */         return false;
/*      */       }
/* 2034 */       if (!keySet().equals(((JSONObject)other).keySet())) {
/* 2035 */         return false;
/*      */       }
/* 2037 */       for (Map.Entry<String, ?> entry : entrySet()) {
/* 2038 */         String name = (String)entry.getKey();
/* 2039 */         Object valueThis = entry.getValue();
/* 2040 */         Object valueOther = ((JSONObject)other).get(name);
/* 2041 */         if (valueThis != valueOther)
/*      */         {
/*      */ 
/* 2044 */           if (valueThis == null) {
/* 2045 */             return false;
/*      */           }
/* 2047 */           if ((valueThis instanceof JSONObject)) {
/* 2048 */             if (!((JSONObject)valueThis).similar(valueOther)) {
/* 2049 */               return false;
/*      */             }
/* 2051 */           } else if ((valueThis instanceof JSONArray)) {
/* 2052 */             if (!((JSONArray)valueThis).similar(valueOther)) {
/* 2053 */               return false;
/*      */             }
/* 2055 */           } else if (!valueThis.equals(valueOther))
/* 2056 */             return false;
/*      */         }
/*      */       }
/* 2059 */       return true;
/*      */     } catch (Throwable exception) {}
/* 2061 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static boolean isDecimalNotation(String val)
/*      */   {
/* 2072 */     return (val.indexOf('.') > -1) || (val.indexOf('e') > -1) || 
/* 2073 */       (val.indexOf('E') > -1) || ("-0".equals(val));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static Number stringToNumber(String val)
/*      */     throws NumberFormatException
/*      */   {
/* 2087 */     char initial = val.charAt(0);
/* 2088 */     if (((initial >= '0') && (initial <= '9')) || (initial == '-'))
/*      */     {
/* 2090 */       if (isDecimalNotation(val))
/*      */       {
/*      */ 
/* 2093 */         if (val.length() > 14) {
/* 2094 */           return new BigDecimal(val);
/*      */         }
/* 2096 */         Double d = Double.valueOf(val);
/* 2097 */         if ((d.isInfinite()) || (d.isNaN()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2102 */           return new BigDecimal(val);
/*      */         }
/* 2104 */         return d;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2128 */       BigInteger bi = new BigInteger(val);
/* 2129 */       if (bi.bitLength() <= 31) {
/* 2130 */         return Integer.valueOf(bi.intValue());
/*      */       }
/* 2132 */       if (bi.bitLength() <= 63) {
/* 2133 */         return Long.valueOf(bi.longValue());
/*      */       }
/* 2135 */       return bi;
/*      */     }
/* 2137 */     throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object stringToValue(String string)
/*      */   {
/* 2151 */     if (string.equals("")) {
/* 2152 */       return string;
/*      */     }
/* 2154 */     if (string.equalsIgnoreCase("true")) {
/* 2155 */       return Boolean.TRUE;
/*      */     }
/* 2157 */     if (string.equalsIgnoreCase("false")) {
/* 2158 */       return Boolean.FALSE;
/*      */     }
/* 2160 */     if (string.equalsIgnoreCase("null")) {
/* 2161 */       return NULL;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2169 */     char initial = string.charAt(0);
/* 2170 */     if (((initial >= '0') && (initial <= '9')) || (initial == '-'))
/*      */     {
/*      */       try
/*      */       {
/* 2174 */         if (isDecimalNotation(string)) {
/* 2175 */           Double d = Double.valueOf(string);
/* 2176 */           if ((!d.isInfinite()) && (!d.isNaN())) {
/* 2177 */             return d;
/*      */           }
/*      */         } else {
/* 2180 */           Long myLong = Long.valueOf(string);
/* 2181 */           if (string.equals(myLong.toString())) {
/* 2182 */             if (myLong.longValue() == myLong.intValue()) {
/* 2183 */               return Integer.valueOf(myLong.intValue());
/*      */             }
/* 2185 */             return myLong;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception localException) {}
/*      */     }
/* 2191 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void testValidity(Object o)
/*      */     throws JSONException
/*      */   {
/* 2203 */     if (o != null) {
/* 2204 */       if ((o instanceof Double)) {
/* 2205 */         if ((((Double)o).isInfinite()) || (((Double)o).isNaN())) {
/* 2206 */           throw new JSONException(
/* 2207 */             "JSON does not allow non-finite numbers.");
/*      */         }
/* 2209 */       } else if (((o instanceof Float)) && (
/* 2210 */         (((Float)o).isInfinite()) || (((Float)o).isNaN()))) {
/* 2211 */         throw new JSONException(
/* 2212 */           "JSON does not allow non-finite numbers.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray toJSONArray(JSONArray names)
/*      */     throws JSONException
/*      */   {
/* 2230 */     if ((names == null) || (names.length() == 0)) {
/* 2231 */       return null;
/*      */     }
/* 2233 */     JSONArray ja = new JSONArray();
/* 2234 */     for (int i = 0; i < names.length(); i++) {
/* 2235 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 2237 */     return ja;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 2256 */       return toString(0);
/*      */     } catch (Exception e) {}
/* 2258 */     return null;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String toString(int indentFactor)
/*      */     throws JSONException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 795	java/io/StringWriter
/*      */     //   3: dup
/*      */     //   4: invokespecial 796	java/io/StringWriter:<init>	()V
/*      */     //   7: astore_2
/*      */     //   8: aload_2
/*      */     //   9: invokevirtual 800	java/io/StringWriter:getBuffer	()Ljava/lang/StringBuffer;
/*      */     //   12: dup
/*      */     //   13: astore_3
/*      */     //   14: monitorenter
/*      */     //   15: aload_0
/*      */     //   16: aload_2
/*      */     //   17: iload_1
/*      */     //   18: iconst_0
/*      */     //   19: invokevirtual 908	org/json/JSONObject:write	(Ljava/io/Writer;II)Ljava/io/Writer;
/*      */     //   22: invokevirtual 84	java/lang/Object:toString	()Ljava/lang/String;
/*      */     //   25: aload_3
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: aload_3
/*      */     //   29: monitorexit
/*      */     //   30: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2289	-> byte code offset #0
/*      */     //   Java source line #2290	-> byte code offset #8
/*      */     //   Java source line #2291	-> byte code offset #15
/*      */     //   Java source line #2290	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	31	0	this	JSONObject
/*      */     //   0	31	1	indentFactor	int
/*      */     //   7	10	2	w	StringWriter
/*      */     //   13	16	3	Ljava/lang/Object;	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   15	27	28	finally
/*      */     //   28	30	28	finally
/*      */   }
/*      */   
/*      */   public static String valueToString(Object value)
/*      */     throws JSONException
/*      */   {
/* 2324 */     return JSONWriter.valueToString(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object wrap(Object object)
/*      */   {
/*      */     try
/*      */     {
/* 2341 */       if (object == null) {
/* 2342 */         return NULL;
/*      */       }
/* 2344 */       if (((object instanceof JSONObject)) || ((object instanceof JSONArray)) || 
/* 2345 */         (NULL.equals(object)) || ((object instanceof JSONString)) || 
/* 2346 */         ((object instanceof Byte)) || ((object instanceof Character)) || 
/* 2347 */         ((object instanceof Short)) || ((object instanceof Integer)) || 
/* 2348 */         ((object instanceof Long)) || ((object instanceof Boolean)) || 
/* 2349 */         ((object instanceof Float)) || ((object instanceof Double)) || 
/* 2350 */         ((object instanceof String)) || ((object instanceof BigInteger)) || 
/* 2351 */         ((object instanceof BigDecimal)) || ((object instanceof Enum))) {
/* 2352 */         return object;
/*      */       }
/*      */       
/* 2355 */       if ((object instanceof Collection)) {
/* 2356 */         Collection<?> coll = (Collection)object;
/* 2357 */         return new JSONArray(coll);
/*      */       }
/* 2359 */       if (object.getClass().isArray()) {
/* 2360 */         return new JSONArray(object);
/*      */       }
/* 2362 */       if ((object instanceof Map)) {
/* 2363 */         Map<?, ?> map = (Map)object;
/* 2364 */         return new JSONObject(map);
/*      */       }
/* 2366 */       Package objectPackage = object.getClass().getPackage();
/* 2367 */       String objectPackageName = objectPackage != null ? objectPackage
/* 2368 */         .getName() : "";
/* 2369 */       if ((objectPackageName.startsWith("java.")) || 
/* 2370 */         (objectPackageName.startsWith("javax.")) || 
/* 2371 */         (object.getClass().getClassLoader() == null)) {
/* 2372 */         return object.toString();
/*      */       }
/* 2374 */       return new JSONObject(object);
/*      */     } catch (Exception exception) {}
/* 2376 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Writer write(Writer writer)
/*      */     throws JSONException
/*      */   {
/* 2391 */     return write(writer, 0, 0);
/*      */   }
/*      */   
/*      */   static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException
/*      */   {
/* 2396 */     if ((value == null) || (value.equals(null))) {
/* 2397 */       writer.write("null");
/* 2398 */     } else if ((value instanceof JSONString))
/*      */     {
/*      */       try {
/* 2401 */         o = ((JSONString)value).toJSONString();
/*      */       } catch (Exception e) { Object o;
/* 2403 */         throw new JSONException(e); }
/*      */       Object o;
/* 2405 */       writer.write(o != null ? o.toString() : quote(value.toString()));
/* 2406 */     } else if ((value instanceof Number))
/*      */     {
/* 2408 */       String numberAsString = numberToString((Number)value);
/*      */       
/*      */       try
/*      */       {
/* 2412 */         BigDecimal testNum = new BigDecimal(numberAsString);
/*      */         
/* 2414 */         writer.write(numberAsString);
/*      */       }
/*      */       catch (NumberFormatException ex)
/*      */       {
/* 2418 */         quote(numberAsString, writer);
/*      */       }
/* 2420 */     } else if ((value instanceof Boolean)) {
/* 2421 */       writer.write(value.toString());
/* 2422 */     } else if ((value instanceof Enum)) {
/* 2423 */       writer.write(quote(((Enum)value).name()));
/* 2424 */     } else if ((value instanceof JSONObject)) {
/* 2425 */       ((JSONObject)value).write(writer, indentFactor, indent);
/* 2426 */     } else if ((value instanceof JSONArray)) {
/* 2427 */       ((JSONArray)value).write(writer, indentFactor, indent);
/* 2428 */     } else if ((value instanceof Map)) {
/* 2429 */       Map<?, ?> map = (Map)value;
/* 2430 */       new JSONObject(map).write(writer, indentFactor, indent);
/* 2431 */     } else if ((value instanceof Collection)) {
/* 2432 */       Collection<?> coll = (Collection)value;
/* 2433 */       new JSONArray(coll).write(writer, indentFactor, indent);
/* 2434 */     } else if (value.getClass().isArray()) {
/* 2435 */       new JSONArray(value).write(writer, indentFactor, indent);
/*      */     } else {
/* 2437 */       quote(value.toString(), writer);
/*      */     }
/* 2439 */     return writer;
/*      */   }
/*      */   
/*      */   static final void indent(Writer writer, int indent) throws IOException {
/* 2443 */     for (int i = 0; i < indent; i++) {
/* 2444 */       writer.write(32);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Writer write(Writer writer, int indentFactor, int indent)
/*      */     throws JSONException
/*      */   {
/*      */     try
/*      */     {
/* 2477 */       boolean commanate = false;
/* 2478 */       int length = length();
/* 2479 */       writer.write(123);
/*      */       
/* 2481 */       if (length == 1) {
/* 2482 */         Map.Entry<String, ?> entry = (Map.Entry)entrySet().iterator().next();
/* 2483 */         String key = (String)entry.getKey();
/* 2484 */         writer.write(quote(key));
/* 2485 */         writer.write(58);
/* 2486 */         if (indentFactor > 0) {
/* 2487 */           writer.write(32);
/*      */         }
/*      */         try {
/* 2490 */           writeValue(writer, entry.getValue(), indentFactor, indent);
/*      */         } catch (Exception e) {
/* 2492 */           throw new JSONException("Unable to write JSONObject value for key: " + key, e);
/*      */         }
/* 2494 */       } else if (length != 0) {
/* 2495 */         int newindent = indent + indentFactor;
/* 2496 */         for (Map.Entry<String, ?> entry : entrySet()) {
/* 2497 */           if (commanate) {
/* 2498 */             writer.write(44);
/*      */           }
/* 2500 */           if (indentFactor > 0) {
/* 2501 */             writer.write(10);
/*      */           }
/* 2503 */           indent(writer, newindent);
/* 2504 */           String key = (String)entry.getKey();
/* 2505 */           writer.write(quote(key));
/* 2506 */           writer.write(58);
/* 2507 */           if (indentFactor > 0) {
/* 2508 */             writer.write(32);
/*      */           }
/*      */           try {
/* 2511 */             writeValue(writer, entry.getValue(), indentFactor, newindent);
/*      */           } catch (Exception e) {
/* 2513 */             throw new JSONException("Unable to write JSONObject value for key: " + key, e);
/*      */           }
/* 2515 */           commanate = true;
/*      */         }
/* 2517 */         if (indentFactor > 0) {
/* 2518 */           writer.write(10);
/*      */         }
/* 2520 */         indent(writer, indent);
/*      */       }
/* 2522 */       writer.write(125);
/* 2523 */       return writer;
/*      */     } catch (IOException exception) {
/* 2525 */       throw new JSONException(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, Object> toMap()
/*      */   {
/* 2539 */     Map<String, Object> results = new HashMap();
/* 2540 */     for (Map.Entry<String, Object> entry : entrySet()) { Object value;
/*      */       Object value;
/* 2542 */       if ((entry.getValue() == null) || (NULL.equals(entry.getValue()))) {
/* 2543 */         value = null; } else { Object value;
/* 2544 */         if ((entry.getValue() instanceof JSONObject)) {
/* 2545 */           value = ((JSONObject)entry.getValue()).toMap(); } else { Object value;
/* 2546 */           if ((entry.getValue() instanceof JSONArray)) {
/* 2547 */             value = ((JSONArray)entry.getValue()).toList();
/*      */           } else
/* 2549 */             value = entry.getValue();
/*      */         } }
/* 2551 */       results.put((String)entry.getKey(), value);
/*      */     }
/* 2553 */     return results;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\JSONObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */