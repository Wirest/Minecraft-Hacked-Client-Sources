/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONArray
/*      */   implements Iterable<Object>
/*      */ {
/*      */   private final ArrayList<Object> myArrayList;
/*      */   
/*      */   public JSONArray()
/*      */   {
/*   95 */     this.myArrayList = new ArrayList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(JSONTokener x)
/*      */     throws JSONException
/*      */   {
/*  107 */     this();
/*  108 */     if (x.nextClean() != '[') {
/*  109 */       throw x.syntaxError("A JSONArray text must start with '['");
/*      */     }
/*      */     
/*  112 */     char nextChar = x.nextClean();
/*  113 */     if (nextChar == 0)
/*      */     {
/*  115 */       throw x.syntaxError("Expected a ',' or ']'");
/*      */     }
/*  117 */     if (nextChar != ']') {
/*  118 */       x.back();
/*      */       for (;;) {
/*  120 */         if (x.nextClean() == ',') {
/*  121 */           x.back();
/*  122 */           this.myArrayList.add(JSONObject.NULL);
/*      */         } else {
/*  124 */           x.back();
/*  125 */           this.myArrayList.add(x.nextValue());
/*      */         }
/*  127 */         switch (x.nextClean())
/*      */         {
/*      */         case '\000': 
/*  130 */           throw x.syntaxError("Expected a ',' or ']'");
/*      */         case ',': 
/*  132 */           nextChar = x.nextClean();
/*  133 */           if (nextChar == 0)
/*      */           {
/*  135 */             throw x.syntaxError("Expected a ',' or ']'");
/*      */           }
/*  137 */           if (nextChar == ']') {
/*  138 */             return;
/*      */           }
/*  140 */           x.back();
/*      */         }
/*      */       }
/*  143 */       return;
/*      */       
/*  145 */       throw x.syntaxError("Expected a ',' or ']'");
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
/*      */   public JSONArray(String source)
/*      */     throws JSONException
/*      */   {
/*  162 */     this(new JSONTokener(source));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(Collection<?> collection)
/*      */   {
/*  172 */     if (collection == null) {
/*  173 */       this.myArrayList = new ArrayList();
/*      */     } else {
/*  175 */       this.myArrayList = new ArrayList(collection.size());
/*  176 */       for (Object o : collection) {
/*  177 */         this.myArrayList.add(JSONObject.wrap(o));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(Object array)
/*      */     throws JSONException
/*      */   {
/*  189 */     this();
/*  190 */     if (array.getClass().isArray()) {
/*  191 */       int length = Array.getLength(array);
/*  192 */       this.myArrayList.ensureCapacity(length);
/*  193 */       for (int i = 0; i < length; i++) {
/*  194 */         put(JSONObject.wrap(Array.get(array, i)));
/*      */       }
/*      */     } else {
/*  197 */       throw new JSONException(
/*  198 */         "JSONArray initial value should be a string or collection or array.");
/*      */     }
/*      */   }
/*      */   
/*      */   public Iterator<Object> iterator()
/*      */   {
/*  204 */     return this.myArrayList.iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object get(int index)
/*      */     throws JSONException
/*      */   {
/*  217 */     Object object = opt(index);
/*  218 */     if (object == null) {
/*  219 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  221 */     return object;
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
/*      */   public boolean getBoolean(int index)
/*      */     throws JSONException
/*      */   {
/*  236 */     Object object = get(index);
/*  237 */     if ((object.equals(Boolean.FALSE)) || (
/*  238 */       ((object instanceof String)) && 
/*  239 */       (((String)object).equalsIgnoreCase("false"))))
/*  240 */       return false;
/*  241 */     if ((object.equals(Boolean.TRUE)) || (
/*  242 */       ((object instanceof String)) && 
/*  243 */       (((String)object).equalsIgnoreCase("true")))) {
/*  244 */       return true;
/*      */     }
/*  246 */     throw new JSONException("JSONArray[" + index + "] is not a boolean.");
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
/*      */   public double getDouble(int index)
/*      */     throws JSONException
/*      */   {
/*  260 */     Object object = get(index);
/*      */     try {
/*  262 */       return (object instanceof Number) ? ((Number)object).doubleValue() : 
/*  263 */         Double.parseDouble((String)object);
/*      */     } catch (Exception e) {
/*  265 */       throw new JSONException("JSONArray[" + index + "] is not a number.", e);
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
/*      */   public float getFloat(int index)
/*      */     throws JSONException
/*      */   {
/*  280 */     Object object = get(index);
/*      */     try {
/*  282 */       return (object instanceof Number) ? ((Number)object).floatValue() : 
/*  283 */         Float.parseFloat(object.toString());
/*      */     } catch (Exception e) {
/*  285 */       throw new JSONException("JSONArray[" + index + 
/*  286 */         "] is not a number.", e);
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
/*      */   public Number getNumber(int index)
/*      */     throws JSONException
/*      */   {
/*  301 */     Object object = get(index);
/*      */     try {
/*  303 */       if ((object instanceof Number)) {
/*  304 */         return (Number)object;
/*      */       }
/*  306 */       return JSONObject.stringToNumber(object.toString());
/*      */     } catch (Exception e) {
/*  308 */       throw new JSONException("JSONArray[" + index + "] is not a number.", e);
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
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, int index)
/*      */     throws JSONException
/*      */   {
/*  325 */     E val = optEnum(clazz, index);
/*  326 */     if (val == null)
/*      */     {
/*      */ 
/*      */ 
/*  330 */       throw new JSONException("JSONArray[" + index + "] is not an enum of type " + 
/*  331 */         JSONObject.quote(clazz.getSimpleName()) + ".");
/*      */     }
/*  333 */     return val;
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
/*      */   public BigDecimal getBigDecimal(int index)
/*      */     throws JSONException
/*      */   {
/*  347 */     Object object = get(index);
/*      */     try {
/*  349 */       return new BigDecimal(object.toString());
/*      */     } catch (Exception e) {
/*  351 */       throw new JSONException("JSONArray[" + index + 
/*  352 */         "] could not convert to BigDecimal.", e);
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
/*      */   public BigInteger getBigInteger(int index)
/*      */     throws JSONException
/*      */   {
/*  367 */     Object object = get(index);
/*      */     try {
/*  369 */       return new BigInteger(object.toString());
/*      */     } catch (Exception e) {
/*  371 */       throw new JSONException("JSONArray[" + index + 
/*  372 */         "] could not convert to BigInteger.", e);
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
/*      */   public int getInt(int index)
/*      */     throws JSONException
/*      */   {
/*  386 */     Object object = get(index);
/*      */     try {
/*  388 */       return (object instanceof Number) ? ((Number)object).intValue() : 
/*  389 */         Integer.parseInt((String)object);
/*      */     } catch (Exception e) {
/*  391 */       throw new JSONException("JSONArray[" + index + "] is not a number.", e);
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
/*      */   public JSONArray getJSONArray(int index)
/*      */     throws JSONException
/*      */   {
/*  406 */     Object object = get(index);
/*  407 */     if ((object instanceof JSONArray)) {
/*  408 */       return (JSONArray)object;
/*      */     }
/*  410 */     throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
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
/*      */   public JSONObject getJSONObject(int index)
/*      */     throws JSONException
/*      */   {
/*  424 */     Object object = get(index);
/*  425 */     if ((object instanceof JSONObject)) {
/*  426 */       return (JSONObject)object;
/*      */     }
/*  428 */     throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
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
/*      */   public long getLong(int index)
/*      */     throws JSONException
/*      */   {
/*  442 */     Object object = get(index);
/*      */     try {
/*  444 */       return (object instanceof Number) ? ((Number)object).longValue() : 
/*  445 */         Long.parseLong((String)object);
/*      */     } catch (Exception e) {
/*  447 */       throw new JSONException("JSONArray[" + index + "] is not a number.", e);
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
/*      */   public String getString(int index)
/*      */     throws JSONException
/*      */   {
/*  461 */     Object object = get(index);
/*  462 */     if ((object instanceof String)) {
/*  463 */       return (String)object;
/*      */     }
/*  465 */     throw new JSONException("JSONArray[" + index + "] not a string.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNull(int index)
/*      */   {
/*  476 */     return JSONObject.NULL.equals(opt(index));
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
/*      */   public String join(String separator)
/*      */     throws JSONException
/*      */   {
/*  491 */     int len = length();
/*  492 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  494 */     for (int i = 0; i < len; i++) {
/*  495 */       if (i > 0) {
/*  496 */         sb.append(separator);
/*      */       }
/*  498 */       sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
/*      */     }
/*  500 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int length()
/*      */   {
/*  509 */     return this.myArrayList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object opt(int index)
/*      */   {
/*  520 */     return (index < 0) || (index >= length()) ? null : this.myArrayList
/*  521 */       .get(index);
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
/*      */   public boolean optBoolean(int index)
/*      */   {
/*  534 */     return optBoolean(index, false);
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
/*      */   public boolean optBoolean(int index, boolean defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  550 */       return getBoolean(index);
/*      */     } catch (Exception e) {}
/*  552 */     return defaultValue;
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
/*      */   public double optDouble(int index)
/*      */   {
/*  566 */     return optDouble(index, NaN.0D);
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
/*      */   public double optDouble(int index, double defaultValue)
/*      */   {
/*  581 */     Object val = opt(index);
/*  582 */     if (JSONObject.NULL.equals(val)) {
/*  583 */       return defaultValue;
/*      */     }
/*  585 */     if ((val instanceof Number)) {
/*  586 */       return ((Number)val).doubleValue();
/*      */     }
/*  588 */     if ((val instanceof String)) {
/*      */       try {
/*  590 */         return Double.parseDouble((String)val);
/*      */       } catch (Exception e) {
/*  592 */         return defaultValue;
/*      */       }
/*      */     }
/*  595 */     return defaultValue;
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
/*      */   public float optFloat(int index)
/*      */   {
/*  608 */     return optFloat(index, NaN.0F);
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
/*      */   public float optFloat(int index, float defaultValue)
/*      */   {
/*  623 */     Object val = opt(index);
/*  624 */     if (JSONObject.NULL.equals(val)) {
/*  625 */       return defaultValue;
/*      */     }
/*  627 */     if ((val instanceof Number)) {
/*  628 */       return ((Number)val).floatValue();
/*      */     }
/*  630 */     if ((val instanceof String)) {
/*      */       try {
/*  632 */         return Float.parseFloat((String)val);
/*      */       } catch (Exception e) {
/*  634 */         return defaultValue;
/*      */       }
/*      */     }
/*  637 */     return defaultValue;
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
/*      */   public int optInt(int index)
/*      */   {
/*  650 */     return optInt(index, 0);
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
/*      */   public int optInt(int index, int defaultValue)
/*      */   {
/*  665 */     Object val = opt(index);
/*  666 */     if (JSONObject.NULL.equals(val)) {
/*  667 */       return defaultValue;
/*      */     }
/*  669 */     if ((val instanceof Number)) {
/*  670 */       return ((Number)val).intValue();
/*      */     }
/*      */     
/*  673 */     if ((val instanceof String)) {
/*      */       try {
/*  675 */         return new BigDecimal(val.toString()).intValue();
/*      */       } catch (Exception e) {
/*  677 */         return defaultValue;
/*      */       }
/*      */     }
/*  680 */     return defaultValue;
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index)
/*      */   {
/*  693 */     return optEnum(clazz, index, null);
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  710 */       Object val = opt(index);
/*  711 */       if (JSONObject.NULL.equals(val)) {
/*  712 */         return defaultValue;
/*      */       }
/*  714 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */ 
/*  717 */         return (Enum)val;
/*      */       }
/*      */       
/*  720 */       return Enum.valueOf(clazz, val.toString());
/*      */     } catch (IllegalArgumentException e) {
/*  722 */       return defaultValue;
/*      */     } catch (NullPointerException e) {}
/*  724 */     return defaultValue;
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
/*      */   public BigInteger optBigInteger(int index, BigInteger defaultValue)
/*      */   {
/*  741 */     Object val = opt(index);
/*  742 */     if (JSONObject.NULL.equals(val)) {
/*  743 */       return defaultValue;
/*      */     }
/*  745 */     if ((val instanceof BigInteger)) {
/*  746 */       return (BigInteger)val;
/*      */     }
/*  748 */     if ((val instanceof BigDecimal)) {
/*  749 */       return ((BigDecimal)val).toBigInteger();
/*      */     }
/*  751 */     if (((val instanceof Double)) || ((val instanceof Float))) {
/*  752 */       return new BigDecimal(((Number)val).doubleValue()).toBigInteger();
/*      */     }
/*  754 */     if (((val instanceof Long)) || ((val instanceof Integer)) || 
/*  755 */       ((val instanceof Short)) || ((val instanceof Byte))) {
/*  756 */       return BigInteger.valueOf(((Number)val).longValue());
/*      */     }
/*      */     try {
/*  759 */       String valStr = val.toString();
/*  760 */       if (JSONObject.isDecimalNotation(valStr)) {
/*  761 */         return new BigDecimal(valStr).toBigInteger();
/*      */       }
/*  763 */       return new BigInteger(valStr);
/*      */     } catch (Exception e) {}
/*  765 */     return defaultValue;
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
/*      */   public BigDecimal optBigDecimal(int index, BigDecimal defaultValue)
/*      */   {
/*  781 */     Object val = opt(index);
/*  782 */     if (JSONObject.NULL.equals(val)) {
/*  783 */       return defaultValue;
/*      */     }
/*  785 */     if ((val instanceof BigDecimal)) {
/*  786 */       return (BigDecimal)val;
/*      */     }
/*  788 */     if ((val instanceof BigInteger)) {
/*  789 */       return new BigDecimal((BigInteger)val);
/*      */     }
/*  791 */     if (((val instanceof Double)) || ((val instanceof Float))) {
/*  792 */       return new BigDecimal(((Number)val).doubleValue());
/*      */     }
/*  794 */     if (((val instanceof Long)) || ((val instanceof Integer)) || 
/*  795 */       ((val instanceof Short)) || ((val instanceof Byte))) {
/*  796 */       return new BigDecimal(((Number)val).longValue());
/*      */     }
/*      */     try {
/*  799 */       return new BigDecimal(val.toString());
/*      */     } catch (Exception e) {}
/*  801 */     return defaultValue;
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
/*      */   public JSONArray optJSONArray(int index)
/*      */   {
/*  814 */     Object o = opt(index);
/*  815 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
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
/*      */   public JSONObject optJSONObject(int index)
/*      */   {
/*  828 */     Object o = opt(index);
/*  829 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
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
/*      */   public long optLong(int index)
/*      */   {
/*  842 */     return optLong(index, 0L);
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
/*      */   public long optLong(int index, long defaultValue)
/*      */   {
/*  857 */     Object val = opt(index);
/*  858 */     if (JSONObject.NULL.equals(val)) {
/*  859 */       return defaultValue;
/*      */     }
/*  861 */     if ((val instanceof Number)) {
/*  862 */       return ((Number)val).longValue();
/*      */     }
/*      */     
/*  865 */     if ((val instanceof String)) {
/*      */       try {
/*  867 */         return new BigDecimal(val.toString()).longValue();
/*      */       } catch (Exception e) {
/*  869 */         return defaultValue;
/*      */       }
/*      */     }
/*  872 */     return defaultValue;
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
/*      */   public Number optNumber(int index)
/*      */   {
/*  886 */     return optNumber(index, null);
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
/*      */   public Number optNumber(int index, Number defaultValue)
/*      */   {
/*  902 */     Object val = opt(index);
/*  903 */     if (JSONObject.NULL.equals(val)) {
/*  904 */       return defaultValue;
/*      */     }
/*  906 */     if ((val instanceof Number)) {
/*  907 */       return (Number)val;
/*      */     }
/*      */     
/*  910 */     if ((val instanceof String)) {
/*      */       try {
/*  912 */         return JSONObject.stringToNumber((String)val);
/*      */       } catch (Exception e) {
/*  914 */         return defaultValue;
/*      */       }
/*      */     }
/*  917 */     return defaultValue;
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
/*      */   public String optString(int index)
/*      */   {
/*  930 */     return optString(index, "");
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
/*      */   public String optString(int index, String defaultValue)
/*      */   {
/*  944 */     Object object = opt(index);
/*  945 */     return JSONObject.NULL.equals(object) ? defaultValue : object
/*  946 */       .toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(boolean value)
/*      */   {
/*  957 */     return put(value ? Boolean.TRUE : Boolean.FALSE);
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
/*      */   public JSONArray put(Collection<?> value)
/*      */   {
/*  971 */     return put(new JSONArray(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(double value)
/*      */     throws JSONException
/*      */   {
/*  984 */     return put(Double.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(float value)
/*      */     throws JSONException
/*      */   {
/*  997 */     return put(Float.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int value)
/*      */   {
/* 1008 */     return put(Integer.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(long value)
/*      */   {
/* 1019 */     return put(Long.valueOf(value));
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
/*      */   public JSONArray put(Map<?, ?> value)
/*      */   {
/* 1035 */     return put(new JSONObject(value));
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
/*      */   public JSONArray put(Object value)
/*      */   {
/* 1050 */     JSONObject.testValidity(value);
/* 1051 */     this.myArrayList.add(value);
/* 1052 */     return this;
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
/*      */   public JSONArray put(int index, boolean value)
/*      */     throws JSONException
/*      */   {
/* 1069 */     return put(index, value ? Boolean.TRUE : Boolean.FALSE);
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
/*      */   public JSONArray put(int index, Collection<?> value)
/*      */     throws JSONException
/*      */   {
/* 1085 */     return put(index, new JSONArray(value));
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
/*      */   public JSONArray put(int index, double value)
/*      */     throws JSONException
/*      */   {
/* 1102 */     return put(index, Double.valueOf(value));
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
/*      */   public JSONArray put(int index, float value)
/*      */     throws JSONException
/*      */   {
/* 1119 */     return put(index, Float.valueOf(value));
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
/*      */   public JSONArray put(int index, int value)
/*      */     throws JSONException
/*      */   {
/* 1136 */     return put(index, Integer.valueOf(value));
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
/*      */   public JSONArray put(int index, long value)
/*      */     throws JSONException
/*      */   {
/* 1153 */     return put(index, Long.valueOf(value));
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
/*      */   public JSONArray put(int index, Map<?, ?> value)
/*      */     throws JSONException
/*      */   {
/* 1172 */     put(index, new JSONObject(value));
/* 1173 */     return this;
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
/*      */   public JSONArray put(int index, Object value)
/*      */     throws JSONException
/*      */   {
/* 1193 */     if (index < 0) {
/* 1194 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/* 1196 */     if (index < length()) {
/* 1197 */       JSONObject.testValidity(value);
/* 1198 */       this.myArrayList.set(index, value);
/* 1199 */       return this;
/*      */     }
/* 1201 */     if (index == length())
/*      */     {
/* 1203 */       return put(value);
/*      */     }
/*      */     
/*      */ 
/* 1207 */     this.myArrayList.ensureCapacity(index + 1);
/* 1208 */     while (index != length())
/*      */     {
/* 1210 */       this.myArrayList.add(JSONObject.NULL);
/*      */     }
/* 1212 */     return put(value);
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
/* 1235 */     return query(new JSONPointer(jsonPointer));
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
/*      */   public Object query(JSONPointer jsonPointer)
/*      */   {
/* 1258 */     return jsonPointer.queryFrom(this);
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
/* 1270 */     return optQuery(new JSONPointer(jsonPointer));
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
/* 1283 */       return jsonPointer.queryFrom(this);
/*      */     } catch (JSONPointerException e) {}
/* 1285 */     return null;
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
/*      */   public Object remove(int index)
/*      */   {
/* 1298 */     return (index >= 0) && (index < length()) ? 
/* 1299 */       this.myArrayList.remove(index) : 
/* 1300 */       null;
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
/* 1311 */     if (!(other instanceof JSONArray)) {
/* 1312 */       return false;
/*      */     }
/* 1314 */     int len = length();
/* 1315 */     if (len != ((JSONArray)other).length()) {
/* 1316 */       return false;
/*      */     }
/* 1318 */     for (int i = 0; i < len; i++) {
/* 1319 */       Object valueThis = this.myArrayList.get(i);
/* 1320 */       Object valueOther = ((JSONArray)other).myArrayList.get(i);
/* 1321 */       if (valueThis != valueOther)
/*      */       {
/*      */ 
/* 1324 */         if (valueThis == null) {
/* 1325 */           return false;
/*      */         }
/* 1327 */         if ((valueThis instanceof JSONObject)) {
/* 1328 */           if (!((JSONObject)valueThis).similar(valueOther)) {
/* 1329 */             return false;
/*      */           }
/* 1331 */         } else if ((valueThis instanceof JSONArray)) {
/* 1332 */           if (!((JSONArray)valueThis).similar(valueOther)) {
/* 1333 */             return false;
/*      */           }
/* 1335 */         } else if (!valueThis.equals(valueOther))
/* 1336 */           return false;
/*      */       }
/*      */     }
/* 1339 */     return true;
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
/*      */   public JSONObject toJSONObject(JSONArray names)
/*      */     throws JSONException
/*      */   {
/* 1355 */     if ((names == null) || (names.length() == 0) || (length() == 0)) {
/* 1356 */       return null;
/*      */     }
/* 1358 */     JSONObject jo = new JSONObject(names.length());
/* 1359 */     for (int i = 0; i < names.length(); i++) {
/* 1360 */       jo.put(names.getString(i), opt(i));
/*      */     }
/* 1362 */     return jo;
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
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 1380 */       return toString(0);
/*      */     } catch (Exception e) {}
/* 1382 */     return null;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String toString(int indentFactor)
/*      */     throws JSONException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 496	java/io/StringWriter
/*      */     //   3: dup
/*      */     //   4: invokespecial 497	java/io/StringWriter:<init>	()V
/*      */     //   7: astore_2
/*      */     //   8: aload_2
/*      */     //   9: invokevirtual 501	java/io/StringWriter:getBuffer	()Ljava/lang/StringBuffer;
/*      */     //   12: dup
/*      */     //   13: astore_3
/*      */     //   14: monitorenter
/*      */     //   15: aload_0
/*      */     //   16: aload_2
/*      */     //   17: iload_1
/*      */     //   18: iconst_0
/*      */     //   19: invokevirtual 505	org/json/JSONArray:write	(Ljava/io/Writer;II)Ljava/io/Writer;
/*      */     //   22: invokevirtual 216	java/lang/Object:toString	()Ljava/lang/String;
/*      */     //   25: aload_3
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: aload_3
/*      */     //   29: monitorexit
/*      */     //   30: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1414	-> byte code offset #0
/*      */     //   Java source line #1415	-> byte code offset #8
/*      */     //   Java source line #1416	-> byte code offset #15
/*      */     //   Java source line #1415	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	31	0	this	JSONArray
/*      */     //   0	31	1	indentFactor	int
/*      */     //   7	10	2	sw	java.io.StringWriter
/*      */     //   13	16	3	Ljava/lang/Object;	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   15	27	28	finally
/*      */     //   28	30	28	finally
/*      */   }
/*      */   
/*      */   public Writer write(Writer writer)
/*      */     throws JSONException
/*      */   {
/* 1431 */     return write(writer, 0, 0);
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
/*      */   public Writer write(Writer writer, int indentFactor, int indent)
/*      */     throws JSONException
/*      */   {
/*      */     try
/*      */     {
/* 1465 */       boolean commanate = false;
/* 1466 */       int length = length();
/* 1467 */       writer.write(91);
/*      */       
/* 1469 */       if (length == 1) {
/*      */         try {
/* 1471 */           JSONObject.writeValue(writer, this.myArrayList.get(0), 
/* 1472 */             indentFactor, indent);
/*      */         } catch (Exception e) {
/* 1474 */           throw new JSONException("Unable to write JSONArray value at index: 0", e);
/*      */         }
/* 1476 */       } else if (length != 0) {
/* 1477 */         int newindent = indent + indentFactor;
/*      */         
/* 1479 */         for (int i = 0; i < length; i++) {
/* 1480 */           if (commanate) {
/* 1481 */             writer.write(44);
/*      */           }
/* 1483 */           if (indentFactor > 0) {
/* 1484 */             writer.write(10);
/*      */           }
/* 1486 */           JSONObject.indent(writer, newindent);
/*      */           try {
/* 1488 */             JSONObject.writeValue(writer, this.myArrayList.get(i), 
/* 1489 */               indentFactor, newindent);
/*      */           } catch (Exception e) {
/* 1491 */             throw new JSONException("Unable to write JSONArray value at index: " + i, e);
/*      */           }
/* 1493 */           commanate = true;
/*      */         }
/* 1495 */         if (indentFactor > 0) {
/* 1496 */           writer.write(10);
/*      */         }
/* 1498 */         JSONObject.indent(writer, indent);
/*      */       }
/* 1500 */       writer.write(93);
/* 1501 */       return writer;
/*      */     } catch (IOException e) {
/* 1503 */       throw new JSONException(e);
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
/*      */   public List<Object> toList()
/*      */   {
/* 1517 */     List<Object> results = new ArrayList(this.myArrayList.size());
/* 1518 */     for (Object element : this.myArrayList) {
/* 1519 */       if ((element == null) || (JSONObject.NULL.equals(element))) {
/* 1520 */         results.add(null);
/* 1521 */       } else if ((element instanceof JSONArray)) {
/* 1522 */         results.add(((JSONArray)element).toList());
/* 1523 */       } else if ((element instanceof JSONObject)) {
/* 1524 */         results.add(((JSONObject)element).toMap());
/*      */       } else {
/* 1526 */         results.add(element);
/*      */       }
/*      */     }
/* 1529 */     return results;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\JSONArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */