/*      */ package io.netty.handler.codec;
/*      */ 
/*      */ import io.netty.util.collection.IntObjectHashMap;
/*      */ import io.netty.util.collection.IntObjectMap;
/*      */ import io.netty.util.concurrent.FastThreadLocal;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultHeaders<T>
/*      */   implements Headers<T>
/*      */ {
/*      */   private static final int HASH_CODE_PRIME = 31;
/*      */   private static final int DEFAULT_BUCKET_SIZE = 17;
/*      */   private static final int DEFAULT_MAP_SIZE = 4;
/*      */   
/*      */   public static abstract interface HashCodeGenerator<T>
/*      */   {
/*      */     public abstract int generateHashCode(T paramT);
/*      */   }
/*      */   
/*      */   public static abstract interface NameConverter<T>
/*      */   {
/*      */     public abstract T convertName(T paramT);
/*      */   }
/*      */   
/*      */   public static final class IdentityNameConverter<T>
/*      */     implements DefaultHeaders.NameConverter<T>
/*      */   {
/*      */     public T convertName(T name)
/*      */     {
/*   75 */       return name;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   82 */   private static final NameConverter<Object> DEFAULT_NAME_CONVERTER = new IdentityNameConverter();
/*      */   
/*      */   private final IntObjectMap<DefaultHeaders<T>.HeaderEntry> entries;
/*      */   
/*      */   private final IntObjectMap<DefaultHeaders<T>.HeaderEntry> tailEntries;
/*      */   private final DefaultHeaders<T>.HeaderEntry head;
/*      */   private final Comparator<? super T> keyComparator;
/*      */   private final Comparator<? super T> valueComparator;
/*      */   private final HashCodeGenerator<T> hashCodeGenerator;
/*      */   private final Headers.ValueConverter<T> valueConverter;
/*      */   private final NameConverter<T> nameConverter;
/*      */   private final int bucketSize;
/*      */   int size;
/*      */   
/*      */   public DefaultHeaders(Comparator<? super T> keyComparator, Comparator<? super T> valueComparator, HashCodeGenerator<T> hashCodeGenerator, Headers.ValueConverter<T> typeConverter)
/*      */   {
/*   98 */     this(keyComparator, valueComparator, hashCodeGenerator, typeConverter, DEFAULT_NAME_CONVERTER);
/*      */   }
/*      */   
/*      */ 
/*      */   public DefaultHeaders(Comparator<? super T> keyComparator, Comparator<? super T> valueComparator, HashCodeGenerator<T> hashCodeGenerator, Headers.ValueConverter<T> typeConverter, NameConverter<T> nameConverter)
/*      */   {
/*  104 */     this(keyComparator, valueComparator, hashCodeGenerator, typeConverter, nameConverter, 17, 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DefaultHeaders(Comparator<? super T> keyComparator, Comparator<? super T> valueComparator, HashCodeGenerator<T> hashCodeGenerator, Headers.ValueConverter<T> valueConverter, NameConverter<T> nameConverter, int bucketSize, int initialMapSize)
/*      */   {
/*  111 */     if (keyComparator == null) {
/*  112 */       throw new NullPointerException("keyComparator");
/*      */     }
/*  114 */     if (valueComparator == null) {
/*  115 */       throw new NullPointerException("valueComparator");
/*      */     }
/*  117 */     if (hashCodeGenerator == null) {
/*  118 */       throw new NullPointerException("hashCodeGenerator");
/*      */     }
/*  120 */     if (valueConverter == null) {
/*  121 */       throw new NullPointerException("valueConverter");
/*      */     }
/*  123 */     if (nameConverter == null) {
/*  124 */       throw new NullPointerException("nameConverter");
/*      */     }
/*  126 */     if (bucketSize < 1) {
/*  127 */       throw new IllegalArgumentException("bucketSize must be a positive integer");
/*      */     }
/*  129 */     this.head = new HeaderEntry();
/*  130 */     this.head.before = (this.head.after = this.head);
/*  131 */     this.keyComparator = keyComparator;
/*  132 */     this.valueComparator = valueComparator;
/*  133 */     this.hashCodeGenerator = hashCodeGenerator;
/*  134 */     this.valueConverter = valueConverter;
/*  135 */     this.nameConverter = nameConverter;
/*  136 */     this.bucketSize = bucketSize;
/*  137 */     this.entries = new IntObjectHashMap(initialMapSize);
/*  138 */     this.tailEntries = new IntObjectHashMap(initialMapSize);
/*      */   }
/*      */   
/*      */   public T get(T name)
/*      */   {
/*  143 */     ObjectUtil.checkNotNull(name, "name");
/*      */     
/*  145 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  146 */     int i = index(h);
/*  147 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/*  148 */     while (e != null) {
/*  149 */       if ((e.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/*  150 */         return (T)e.value;
/*      */       }
/*  152 */       e = e.next;
/*      */     }
/*  154 */     return null;
/*      */   }
/*      */   
/*      */   public T get(T name, T defaultValue)
/*      */   {
/*  159 */     T value = get(name);
/*  160 */     if (value == null) {
/*  161 */       return defaultValue;
/*      */     }
/*  163 */     return value;
/*      */   }
/*      */   
/*      */   public T getAndRemove(T name)
/*      */   {
/*  168 */     ObjectUtil.checkNotNull(name, "name");
/*  169 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  170 */     int i = index(h);
/*  171 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/*  172 */     if (e == null) {
/*  173 */       return null;
/*      */     }
/*      */     
/*  176 */     T value = null;
/*      */     
/*  178 */     while ((e.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/*  179 */       if (value == null) {
/*  180 */         value = e.value;
/*      */       }
/*  182 */       e.remove();
/*  183 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/*  184 */       if (next != null) {
/*  185 */         this.entries.put(i, next);
/*  186 */         e = next;
/*      */       } else {
/*  188 */         this.entries.remove(i);
/*  189 */         this.tailEntries.remove(i);
/*  190 */         return value;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  198 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/*  199 */       if (next == null) {
/*      */         break;
/*      */       }
/*  202 */       if ((next.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/*  203 */         if (value == null) {
/*  204 */           value = next.value;
/*      */         }
/*  206 */         e.next = next.next;
/*  207 */         if (e.next == null) {
/*  208 */           this.tailEntries.put(i, e);
/*      */         }
/*  210 */         next.remove();
/*      */       } else {
/*  212 */         e = next;
/*      */       }
/*      */     }
/*      */     
/*  216 */     return value;
/*      */   }
/*      */   
/*      */   public T getAndRemove(T name, T defaultValue)
/*      */   {
/*  221 */     T value = getAndRemove(name);
/*  222 */     if (value == null) {
/*  223 */       return defaultValue;
/*      */     }
/*  225 */     return value;
/*      */   }
/*      */   
/*      */   public List<T> getAll(T name)
/*      */   {
/*  230 */     ObjectUtil.checkNotNull(name, "name");
/*  231 */     List<T> values = new ArrayList(4);
/*  232 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  233 */     int i = index(h);
/*  234 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/*  235 */     while (e != null) {
/*  236 */       if ((e.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/*  237 */         values.add(e.value);
/*      */       }
/*  239 */       e = e.next;
/*      */     }
/*      */     
/*  242 */     return values;
/*      */   }
/*      */   
/*      */   public List<T> getAllAndRemove(T name)
/*      */   {
/*  247 */     ObjectUtil.checkNotNull(name, "name");
/*  248 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  249 */     int i = index(h);
/*  250 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/*  251 */     if (e == null) {
/*  252 */       return null;
/*      */     }
/*      */     
/*  255 */     List<T> values = new ArrayList(4);
/*      */     
/*  257 */     while ((e.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/*  258 */       values.add(e.value);
/*  259 */       e.remove();
/*  260 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/*  261 */       if (next != null) {
/*  262 */         this.entries.put(i, next);
/*  263 */         e = next;
/*      */       } else {
/*  265 */         this.entries.remove(i);
/*  266 */         this.tailEntries.remove(i);
/*  267 */         return values;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  275 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/*  276 */       if (next == null) {
/*      */         break;
/*      */       }
/*  279 */       if ((next.hash == h) && (this.keyComparator.compare(next.name, name) == 0)) {
/*  280 */         values.add(next.value);
/*  281 */         e.next = next.next;
/*  282 */         if (e.next == null) {
/*  283 */           this.tailEntries.put(i, e);
/*      */         }
/*  285 */         next.remove();
/*      */       } else {
/*  287 */         e = next;
/*      */       }
/*      */     }
/*      */     
/*  291 */     return values;
/*      */   }
/*      */   
/*      */   public List<Map.Entry<T, T>> entries()
/*      */   {
/*  296 */     int size = size();
/*  297 */     List<Map.Entry<T, T>> localEntries = new ArrayList(size);
/*      */     
/*  299 */     DefaultHeaders<T>.HeaderEntry e = this.head.after;
/*  300 */     while (e != this.head) {
/*  301 */       localEntries.add(e);
/*  302 */       e = e.after;
/*      */     }
/*      */     
/*  305 */     assert (size == localEntries.size());
/*  306 */     return localEntries;
/*      */   }
/*      */   
/*      */   public boolean contains(T name)
/*      */   {
/*  311 */     return get(name) != null;
/*      */   }
/*      */   
/*      */   public boolean contains(T name, T value)
/*      */   {
/*  316 */     return contains(name, value, this.keyComparator, this.valueComparator);
/*      */   }
/*      */   
/*      */   public boolean containsObject(T name, Object value)
/*      */   {
/*  321 */     return contains(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
/*      */   }
/*      */   
/*      */   public boolean containsBoolean(T name, boolean value)
/*      */   {
/*  326 */     return contains(name, this.valueConverter.convertBoolean(((Boolean)ObjectUtil.checkNotNull(Boolean.valueOf(value), "value")).booleanValue()));
/*      */   }
/*      */   
/*      */   public boolean containsByte(T name, byte value)
/*      */   {
/*  331 */     return contains(name, this.valueConverter.convertByte(((Byte)ObjectUtil.checkNotNull(Byte.valueOf(value), "value")).byteValue()));
/*      */   }
/*      */   
/*      */   public boolean containsChar(T name, char value)
/*      */   {
/*  336 */     return contains(name, this.valueConverter.convertChar(((Character)ObjectUtil.checkNotNull(Character.valueOf(value), "value")).charValue()));
/*      */   }
/*      */   
/*      */   public boolean containsShort(T name, short value)
/*      */   {
/*  341 */     return contains(name, this.valueConverter.convertShort(((Short)ObjectUtil.checkNotNull(Short.valueOf(value), "value")).shortValue()));
/*      */   }
/*      */   
/*      */   public boolean containsInt(T name, int value)
/*      */   {
/*  346 */     return contains(name, this.valueConverter.convertInt(((Integer)ObjectUtil.checkNotNull(Integer.valueOf(value), "value")).intValue()));
/*      */   }
/*      */   
/*      */   public boolean containsLong(T name, long value)
/*      */   {
/*  351 */     return contains(name, this.valueConverter.convertLong(((Long)ObjectUtil.checkNotNull(Long.valueOf(value), "value")).longValue()));
/*      */   }
/*      */   
/*      */   public boolean containsFloat(T name, float value)
/*      */   {
/*  356 */     return contains(name, this.valueConverter.convertFloat(((Float)ObjectUtil.checkNotNull(Float.valueOf(value), "value")).floatValue()));
/*      */   }
/*      */   
/*      */   public boolean containsDouble(T name, double value)
/*      */   {
/*  361 */     return contains(name, this.valueConverter.convertDouble(((Double)ObjectUtil.checkNotNull(Double.valueOf(value), "value")).doubleValue()));
/*      */   }
/*      */   
/*      */   public boolean containsTimeMillis(T name, long value)
/*      */   {
/*  366 */     return contains(name, this.valueConverter.convertTimeMillis(((Long)ObjectUtil.checkNotNull(Long.valueOf(value), "value")).longValue()));
/*      */   }
/*      */   
/*      */   public boolean contains(T name, T value, Comparator<? super T> comparator)
/*      */   {
/*  371 */     return contains(name, value, comparator, comparator);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean contains(T name, T value, Comparator<? super T> keyComparator, Comparator<? super T> valueComparator)
/*      */   {
/*  377 */     ObjectUtil.checkNotNull(name, "name");
/*  378 */     ObjectUtil.checkNotNull(value, "value");
/*  379 */     ObjectUtil.checkNotNull(keyComparator, "keyComparator");
/*  380 */     ObjectUtil.checkNotNull(valueComparator, "valueComparator");
/*  381 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  382 */     int i = index(h);
/*  383 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/*  384 */     while (e != null) {
/*  385 */       if ((e.hash == h) && (keyComparator.compare(e.name, name) == 0) && (valueComparator.compare(e.value, value) == 0))
/*      */       {
/*      */ 
/*  388 */         return true;
/*      */       }
/*  390 */       e = e.next;
/*      */     }
/*  392 */     return false;
/*      */   }
/*      */   
/*      */   public boolean containsObject(T name, Object value, Comparator<? super T> comparator)
/*      */   {
/*  397 */     return containsObject(name, value, comparator, comparator);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean containsObject(T name, Object value, Comparator<? super T> keyComparator, Comparator<? super T> valueComparator)
/*      */   {
/*  403 */     return contains(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")), keyComparator, valueComparator);
/*      */   }
/*      */   
/*      */ 
/*      */   public int size()
/*      */   {
/*  409 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty()
/*      */   {
/*  414 */     return this.head == this.head.after;
/*      */   }
/*      */   
/*      */   public Set<T> names()
/*      */   {
/*  419 */     Set<T> names = new TreeSet(this.keyComparator);
/*      */     
/*  421 */     DefaultHeaders<T>.HeaderEntry e = this.head.after;
/*  422 */     while (e != this.head) {
/*  423 */       names.add(e.name);
/*  424 */       e = e.after;
/*      */     }
/*      */     
/*  427 */     return names;
/*      */   }
/*      */   
/*      */   public List<T> namesList()
/*      */   {
/*  432 */     List<T> names = new ArrayList(size());
/*      */     
/*  434 */     DefaultHeaders<T>.HeaderEntry e = this.head.after;
/*  435 */     while (e != this.head) {
/*  436 */       names.add(e.name);
/*  437 */       e = e.after;
/*      */     }
/*      */     
/*  440 */     return names;
/*      */   }
/*      */   
/*      */   public Headers<T> add(T name, T value)
/*      */   {
/*  445 */     name = convertName(name);
/*  446 */     ObjectUtil.checkNotNull(value, "value");
/*  447 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  448 */     int i = index(h);
/*  449 */     add0(h, i, name, value);
/*  450 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> add(T name, Iterable<? extends T> values)
/*      */   {
/*  455 */     name = convertName(name);
/*  456 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  458 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  459 */     int i = index(h);
/*  460 */     for (T v : values) {
/*  461 */       if (v == null) {
/*      */         break;
/*      */       }
/*  464 */       add0(h, i, name, v);
/*      */     }
/*  466 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> add(T name, T... values)
/*      */   {
/*  471 */     name = convertName(name);
/*  472 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  474 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  475 */     int i = index(h);
/*  476 */     for (T v : values) {
/*  477 */       if (v == null) {
/*      */         break;
/*      */       }
/*  480 */       add0(h, i, name, v);
/*      */     }
/*  482 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> addObject(T name, Object value)
/*      */   {
/*  487 */     return add(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
/*      */   }
/*      */   
/*      */   public Headers<T> addObject(T name, Iterable<?> values)
/*      */   {
/*  492 */     name = convertName(name);
/*  493 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  495 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  496 */     int i = index(h);
/*  497 */     for (Object o : values) {
/*  498 */       if (o == null) {
/*      */         break;
/*      */       }
/*  501 */       T converted = this.valueConverter.convertObject(o);
/*  502 */       ObjectUtil.checkNotNull(converted, "converted");
/*  503 */       add0(h, i, name, converted);
/*      */     }
/*  505 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> addObject(T name, Object... values)
/*      */   {
/*  510 */     name = convertName(name);
/*  511 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  513 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  514 */     int i = index(h);
/*  515 */     for (Object o : values) {
/*  516 */       if (o == null) {
/*      */         break;
/*      */       }
/*  519 */       T converted = this.valueConverter.convertObject(o);
/*  520 */       ObjectUtil.checkNotNull(converted, "converted");
/*  521 */       add0(h, i, name, converted);
/*      */     }
/*  523 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> addInt(T name, int value)
/*      */   {
/*  528 */     return add(name, this.valueConverter.convertInt(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addLong(T name, long value)
/*      */   {
/*  533 */     return add(name, this.valueConverter.convertLong(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addDouble(T name, double value)
/*      */   {
/*  538 */     return add(name, this.valueConverter.convertDouble(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addTimeMillis(T name, long value)
/*      */   {
/*  543 */     return add(name, this.valueConverter.convertTimeMillis(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addChar(T name, char value)
/*      */   {
/*  548 */     return add(name, this.valueConverter.convertChar(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addBoolean(T name, boolean value)
/*      */   {
/*  553 */     return add(name, this.valueConverter.convertBoolean(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addFloat(T name, float value)
/*      */   {
/*  558 */     return add(name, this.valueConverter.convertFloat(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addByte(T name, byte value)
/*      */   {
/*  563 */     return add(name, this.valueConverter.convertByte(value));
/*      */   }
/*      */   
/*      */   public Headers<T> addShort(T name, short value)
/*      */   {
/*  568 */     return add(name, this.valueConverter.convertShort(value));
/*      */   }
/*      */   
/*      */   public Headers<T> add(Headers<T> headers)
/*      */   {
/*  573 */     ObjectUtil.checkNotNull(headers, "headers");
/*      */     
/*  575 */     add0(headers);
/*  576 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> set(T name, T value)
/*      */   {
/*  581 */     name = convertName(name);
/*  582 */     ObjectUtil.checkNotNull(value, "value");
/*  583 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  584 */     int i = index(h);
/*  585 */     remove0(h, i, name);
/*  586 */     add0(h, i, name, value);
/*  587 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> set(T name, Iterable<? extends T> values)
/*      */   {
/*  592 */     name = convertName(name);
/*  593 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  595 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  596 */     int i = index(h);
/*  597 */     remove0(h, i, name);
/*  598 */     for (T v : values) {
/*  599 */       if (v == null) {
/*      */         break;
/*      */       }
/*  602 */       add0(h, i, name, v);
/*      */     }
/*      */     
/*  605 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> set(T name, T... values)
/*      */   {
/*  610 */     name = convertName(name);
/*  611 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  613 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  614 */     int i = index(h);
/*  615 */     remove0(h, i, name);
/*  616 */     for (T v : values) {
/*  617 */       if (v == null) {
/*      */         break;
/*      */       }
/*  620 */       add0(h, i, name, v);
/*      */     }
/*      */     
/*  623 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> setObject(T name, Object value)
/*      */   {
/*  628 */     return set(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
/*      */   }
/*      */   
/*      */   public Headers<T> setObject(T name, Iterable<?> values)
/*      */   {
/*  633 */     name = convertName(name);
/*  634 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  636 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  637 */     int i = index(h);
/*  638 */     remove0(h, i, name);
/*  639 */     for (Object o : values) {
/*  640 */       if (o == null) {
/*      */         break;
/*      */       }
/*  643 */       T converted = this.valueConverter.convertObject(o);
/*  644 */       ObjectUtil.checkNotNull(converted, "converted");
/*  645 */       add0(h, i, name, converted);
/*      */     }
/*      */     
/*  648 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> setObject(T name, Object... values)
/*      */   {
/*  653 */     name = convertName(name);
/*  654 */     ObjectUtil.checkNotNull(values, "values");
/*      */     
/*  656 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  657 */     int i = index(h);
/*  658 */     remove0(h, i, name);
/*  659 */     for (Object o : values) {
/*  660 */       if (o == null) {
/*      */         break;
/*      */       }
/*  663 */       T converted = this.valueConverter.convertObject(o);
/*  664 */       ObjectUtil.checkNotNull(converted, "converted");
/*  665 */       add0(h, i, name, converted);
/*      */     }
/*      */     
/*  668 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> setInt(T name, int value)
/*      */   {
/*  673 */     return set(name, this.valueConverter.convertInt(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setLong(T name, long value)
/*      */   {
/*  678 */     return set(name, this.valueConverter.convertLong(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setDouble(T name, double value)
/*      */   {
/*  683 */     return set(name, this.valueConverter.convertDouble(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setTimeMillis(T name, long value)
/*      */   {
/*  688 */     return set(name, this.valueConverter.convertTimeMillis(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setFloat(T name, float value)
/*      */   {
/*  693 */     return set(name, this.valueConverter.convertFloat(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setChar(T name, char value)
/*      */   {
/*  698 */     return set(name, this.valueConverter.convertChar(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setBoolean(T name, boolean value)
/*      */   {
/*  703 */     return set(name, this.valueConverter.convertBoolean(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setByte(T name, byte value)
/*      */   {
/*  708 */     return set(name, this.valueConverter.convertByte(value));
/*      */   }
/*      */   
/*      */   public Headers<T> setShort(T name, short value)
/*      */   {
/*  713 */     return set(name, this.valueConverter.convertShort(value));
/*      */   }
/*      */   
/*      */   public Headers<T> set(Headers<T> headers)
/*      */   {
/*  718 */     ObjectUtil.checkNotNull(headers, "headers");
/*      */     
/*  720 */     clear();
/*  721 */     add0(headers);
/*  722 */     return this;
/*      */   }
/*      */   
/*      */   public Headers<T> setAll(Headers<T> headers)
/*      */   {
/*  727 */     ObjectUtil.checkNotNull(headers, "headers");
/*      */     
/*  729 */     if ((headers instanceof DefaultHeaders)) {
/*  730 */       DefaultHeaders<T> m = (DefaultHeaders)headers;
/*  731 */       DefaultHeaders<T>.HeaderEntry e = m.head.after;
/*  732 */       while (e != m.head) {
/*  733 */         set(e.name, e.value);
/*  734 */         e = e.after;
/*      */       }
/*      */     } else {
/*      */       try {
/*  738 */         headers.forEachEntry(setAllVisitor());
/*      */       } catch (Exception ex) {
/*  740 */         PlatformDependent.throwException(ex);
/*      */       }
/*      */     }
/*      */     
/*  744 */     return this;
/*      */   }
/*      */   
/*      */   public boolean remove(T name)
/*      */   {
/*  749 */     ObjectUtil.checkNotNull(name, "name");
/*  750 */     int h = this.hashCodeGenerator.generateHashCode(name);
/*  751 */     int i = index(h);
/*  752 */     return remove0(h, i, name);
/*      */   }
/*      */   
/*      */   public Headers<T> clear()
/*      */   {
/*  757 */     this.entries.clear();
/*  758 */     this.tailEntries.clear();
/*  759 */     this.head.before = (this.head.after = this.head);
/*  760 */     this.size = 0;
/*  761 */     return this;
/*      */   }
/*      */   
/*      */   public Iterator<Map.Entry<T, T>> iterator()
/*      */   {
/*  766 */     return new KeyValueHeaderIterator();
/*      */   }
/*      */   
/*      */   public Map.Entry<T, T> forEachEntry(Headers.EntryVisitor<T> visitor) throws Exception
/*      */   {
/*  771 */     DefaultHeaders<T>.HeaderEntry e = this.head.after;
/*  772 */     while (e != this.head) {
/*  773 */       if (!visitor.visit(e)) {
/*  774 */         return e;
/*      */       }
/*  776 */       e = e.after;
/*      */     }
/*  778 */     return null;
/*      */   }
/*      */   
/*      */   public T forEachName(Headers.NameVisitor<T> visitor) throws Exception
/*      */   {
/*  783 */     DefaultHeaders<T>.HeaderEntry e = this.head.after;
/*  784 */     while (e != this.head) {
/*  785 */       if (!visitor.visit(e.name)) {
/*  786 */         return (T)e.name;
/*      */       }
/*  788 */       e = e.after;
/*      */     }
/*  790 */     return null;
/*      */   }
/*      */   
/*      */   public Boolean getBoolean(T name)
/*      */   {
/*  795 */     T v = get(name);
/*  796 */     if (v == null) {
/*  797 */       return null;
/*      */     }
/*      */     try {
/*  800 */       return Boolean.valueOf(this.valueConverter.convertToBoolean(v));
/*      */     } catch (Throwable ignored) {}
/*  802 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getBoolean(T name, boolean defaultValue)
/*      */   {
/*  808 */     Boolean v = getBoolean(name);
/*  809 */     return v == null ? defaultValue : v.booleanValue();
/*      */   }
/*      */   
/*      */   public Byte getByte(T name)
/*      */   {
/*  814 */     T v = get(name);
/*  815 */     if (v == null) {
/*  816 */       return null;
/*      */     }
/*      */     try {
/*  819 */       return Byte.valueOf(this.valueConverter.convertToByte(v));
/*      */     } catch (Throwable ignored) {}
/*  821 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte getByte(T name, byte defaultValue)
/*      */   {
/*  827 */     Byte v = getByte(name);
/*  828 */     return v == null ? defaultValue : v.byteValue();
/*      */   }
/*      */   
/*      */   public Character getChar(T name)
/*      */   {
/*  833 */     T v = get(name);
/*  834 */     if (v == null) {
/*  835 */       return null;
/*      */     }
/*      */     try {
/*  838 */       return Character.valueOf(this.valueConverter.convertToChar(v));
/*      */     } catch (Throwable ignored) {}
/*  840 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public char getChar(T name, char defaultValue)
/*      */   {
/*  846 */     Character v = getChar(name);
/*  847 */     return v == null ? defaultValue : v.charValue();
/*      */   }
/*      */   
/*      */   public Short getShort(T name)
/*      */   {
/*  852 */     T v = get(name);
/*  853 */     if (v == null) {
/*  854 */       return null;
/*      */     }
/*      */     try {
/*  857 */       return Short.valueOf(this.valueConverter.convertToShort(v));
/*      */     } catch (Throwable ignored) {}
/*  859 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public short getInt(T name, short defaultValue)
/*      */   {
/*  865 */     Short v = getShort(name);
/*  866 */     return v == null ? defaultValue : v.shortValue();
/*      */   }
/*      */   
/*      */   public Integer getInt(T name)
/*      */   {
/*  871 */     T v = get(name);
/*  872 */     if (v == null) {
/*  873 */       return null;
/*      */     }
/*      */     try {
/*  876 */       return Integer.valueOf(this.valueConverter.convertToInt(v));
/*      */     } catch (Throwable ignored) {}
/*  878 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getInt(T name, int defaultValue)
/*      */   {
/*  884 */     Integer v = getInt(name);
/*  885 */     return v == null ? defaultValue : v.intValue();
/*      */   }
/*      */   
/*      */   public Long getLong(T name)
/*      */   {
/*  890 */     T v = get(name);
/*  891 */     if (v == null) {
/*  892 */       return null;
/*      */     }
/*      */     try {
/*  895 */       return Long.valueOf(this.valueConverter.convertToLong(v));
/*      */     } catch (Throwable ignored) {}
/*  897 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getLong(T name, long defaultValue)
/*      */   {
/*  903 */     Long v = getLong(name);
/*  904 */     return v == null ? defaultValue : v.longValue();
/*      */   }
/*      */   
/*      */   public Float getFloat(T name)
/*      */   {
/*  909 */     T v = get(name);
/*  910 */     if (v == null) {
/*  911 */       return null;
/*      */     }
/*      */     try {
/*  914 */       return Float.valueOf(this.valueConverter.convertToFloat(v));
/*      */     } catch (Throwable ignored) {}
/*  916 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public float getFloat(T name, float defaultValue)
/*      */   {
/*  922 */     Float v = getFloat(name);
/*  923 */     return v == null ? defaultValue : v.floatValue();
/*      */   }
/*      */   
/*      */   public Double getDouble(T name)
/*      */   {
/*  928 */     T v = get(name);
/*  929 */     if (v == null) {
/*  930 */       return null;
/*      */     }
/*      */     try {
/*  933 */       return Double.valueOf(this.valueConverter.convertToDouble(v));
/*      */     } catch (Throwable ignored) {}
/*  935 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public double getDouble(T name, double defaultValue)
/*      */   {
/*  941 */     Double v = getDouble(name);
/*  942 */     return v == null ? defaultValue : v.doubleValue();
/*      */   }
/*      */   
/*      */   public Long getTimeMillis(T name)
/*      */   {
/*  947 */     T v = get(name);
/*  948 */     if (v == null) {
/*  949 */       return null;
/*      */     }
/*      */     try {
/*  952 */       return Long.valueOf(this.valueConverter.convertToTimeMillis(v));
/*      */     } catch (Throwable ignored) {}
/*  954 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getTimeMillis(T name, long defaultValue)
/*      */   {
/*  960 */     Long v = getTimeMillis(name);
/*  961 */     return v == null ? defaultValue : v.longValue();
/*      */   }
/*      */   
/*      */   public Boolean getBooleanAndRemove(T name)
/*      */   {
/*  966 */     T v = getAndRemove(name);
/*  967 */     if (v == null) {
/*  968 */       return null;
/*      */     }
/*      */     try {
/*  971 */       return Boolean.valueOf(this.valueConverter.convertToBoolean(v));
/*      */     } catch (Throwable ignored) {}
/*  973 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getBooleanAndRemove(T name, boolean defaultValue)
/*      */   {
/*  979 */     Boolean v = getBooleanAndRemove(name);
/*  980 */     return v == null ? defaultValue : v.booleanValue();
/*      */   }
/*      */   
/*      */   public Byte getByteAndRemove(T name)
/*      */   {
/*  985 */     T v = getAndRemove(name);
/*  986 */     if (v == null) {
/*  987 */       return null;
/*      */     }
/*      */     try {
/*  990 */       return Byte.valueOf(this.valueConverter.convertToByte(v));
/*      */     } catch (Throwable ignored) {}
/*  992 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte getByteAndRemove(T name, byte defaultValue)
/*      */   {
/*  998 */     Byte v = getByteAndRemove(name);
/*  999 */     return v == null ? defaultValue : v.byteValue();
/*      */   }
/*      */   
/*      */   public Character getCharAndRemove(T name)
/*      */   {
/* 1004 */     T v = getAndRemove(name);
/* 1005 */     if (v == null) {
/* 1006 */       return null;
/*      */     }
/*      */     try {
/* 1009 */       return Character.valueOf(this.valueConverter.convertToChar(v));
/*      */     } catch (Throwable ignored) {}
/* 1011 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public char getCharAndRemove(T name, char defaultValue)
/*      */   {
/* 1017 */     Character v = getCharAndRemove(name);
/* 1018 */     return v == null ? defaultValue : v.charValue();
/*      */   }
/*      */   
/*      */   public Short getShortAndRemove(T name)
/*      */   {
/* 1023 */     T v = getAndRemove(name);
/* 1024 */     if (v == null) {
/* 1025 */       return null;
/*      */     }
/*      */     try {
/* 1028 */       return Short.valueOf(this.valueConverter.convertToShort(v));
/*      */     } catch (Throwable ignored) {}
/* 1030 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public short getShortAndRemove(T name, short defaultValue)
/*      */   {
/* 1036 */     Short v = getShortAndRemove(name);
/* 1037 */     return v == null ? defaultValue : v.shortValue();
/*      */   }
/*      */   
/*      */   public Integer getIntAndRemove(T name)
/*      */   {
/* 1042 */     T v = getAndRemove(name);
/* 1043 */     if (v == null) {
/* 1044 */       return null;
/*      */     }
/*      */     try {
/* 1047 */       return Integer.valueOf(this.valueConverter.convertToInt(v));
/*      */     } catch (Throwable ignored) {}
/* 1049 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getIntAndRemove(T name, int defaultValue)
/*      */   {
/* 1055 */     Integer v = getIntAndRemove(name);
/* 1056 */     return v == null ? defaultValue : v.intValue();
/*      */   }
/*      */   
/*      */   public Long getLongAndRemove(T name)
/*      */   {
/* 1061 */     T v = getAndRemove(name);
/* 1062 */     if (v == null) {
/* 1063 */       return null;
/*      */     }
/*      */     try {
/* 1066 */       return Long.valueOf(this.valueConverter.convertToLong(v));
/*      */     } catch (Throwable ignored) {}
/* 1068 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getLongAndRemove(T name, long defaultValue)
/*      */   {
/* 1074 */     Long v = getLongAndRemove(name);
/* 1075 */     return v == null ? defaultValue : v.longValue();
/*      */   }
/*      */   
/*      */   public Float getFloatAndRemove(T name)
/*      */   {
/* 1080 */     T v = getAndRemove(name);
/* 1081 */     if (v == null) {
/* 1082 */       return null;
/*      */     }
/*      */     try {
/* 1085 */       return Float.valueOf(this.valueConverter.convertToFloat(v));
/*      */     } catch (Throwable ignored) {}
/* 1087 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public float getFloatAndRemove(T name, float defaultValue)
/*      */   {
/* 1093 */     Float v = getFloatAndRemove(name);
/* 1094 */     return v == null ? defaultValue : v.floatValue();
/*      */   }
/*      */   
/*      */   public Double getDoubleAndRemove(T name)
/*      */   {
/* 1099 */     T v = getAndRemove(name);
/* 1100 */     if (v == null) {
/* 1101 */       return null;
/*      */     }
/*      */     try {
/* 1104 */       return Double.valueOf(this.valueConverter.convertToDouble(v));
/*      */     } catch (Throwable ignored) {}
/* 1106 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public double getDoubleAndRemove(T name, double defaultValue)
/*      */   {
/* 1112 */     Double v = getDoubleAndRemove(name);
/* 1113 */     return v == null ? defaultValue : v.doubleValue();
/*      */   }
/*      */   
/*      */   public Long getTimeMillisAndRemove(T name)
/*      */   {
/* 1118 */     T v = getAndRemove(name);
/* 1119 */     if (v == null) {
/* 1120 */       return null;
/*      */     }
/*      */     try {
/* 1123 */       return Long.valueOf(this.valueConverter.convertToTimeMillis(v));
/*      */     } catch (Throwable ignored) {}
/* 1125 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getTimeMillisAndRemove(T name, long defaultValue)
/*      */   {
/* 1131 */     Long v = getTimeMillisAndRemove(name);
/* 1132 */     return v == null ? defaultValue : v.longValue();
/*      */   }
/*      */   
/*      */   public boolean equals(Object o)
/*      */   {
/* 1137 */     if (!(o instanceof DefaultHeaders)) {
/* 1138 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1142 */     DefaultHeaders<T> h2 = (DefaultHeaders)o;
/*      */     
/*      */ 
/* 1145 */     List<T> namesList = namesList();
/* 1146 */     List<T> otherNamesList = h2.namesList();
/* 1147 */     if (!equals(namesList, otherNamesList, this.keyComparator)) {
/* 1148 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1153 */     Set<T> names = new TreeSet(this.keyComparator);
/* 1154 */     names.addAll(namesList);
/* 1155 */     for (T name : names) {
/* 1156 */       if (!equals(getAll(name), h2.getAll(name), this.valueComparator)) {
/* 1157 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1161 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> boolean equals(List<T> lhs, List<T> rhs, Comparator<? super T> comparator)
/*      */   {
/* 1172 */     int lhsSize = lhs.size();
/* 1173 */     if (lhsSize != rhs.size()) {
/* 1174 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1179 */     Collections.sort(lhs, comparator);
/* 1180 */     Collections.sort(rhs, comparator);
/* 1181 */     for (int i = 0; i < lhsSize; i++) {
/* 1182 */       if (comparator.compare(lhs.get(i), rhs.get(i)) != 0) {
/* 1183 */         return false;
/*      */       }
/*      */     }
/* 1186 */     return true;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1191 */     int result = 1;
/* 1192 */     for (T name : names()) {
/* 1193 */       result = 31 * result + name.hashCode();
/* 1194 */       List<T> values = getAll(name);
/* 1195 */       Collections.sort(values, this.valueComparator);
/* 1196 */       for (int i = 0; i < values.size(); i++) {
/* 1197 */         result = 31 * result + this.hashCodeGenerator.generateHashCode(values.get(i));
/*      */       }
/*      */     }
/* 1200 */     return result;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1205 */     StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('[');
/* 1206 */     for (T name : names()) {
/* 1207 */       List<T> values = getAll(name);
/* 1208 */       Collections.sort(values, this.valueComparator);
/* 1209 */       for (int i = 0; i < values.size(); i++) {
/* 1210 */         builder.append(name).append(": ").append(values.get(i)).append(", ");
/*      */       }
/*      */     }
/* 1213 */     if (builder.length() > 2) {
/* 1214 */       builder.setLength(builder.length() - 2);
/*      */     }
/* 1216 */     return ']';
/*      */   }
/*      */   
/*      */   protected Headers.ValueConverter<T> valueConverter() {
/* 1220 */     return this.valueConverter;
/*      */   }
/*      */   
/*      */   private T convertName(T name) {
/* 1224 */     return (T)this.nameConverter.convertName(ObjectUtil.checkNotNull(name, "name"));
/*      */   }
/*      */   
/*      */   private int index(int hash) {
/* 1228 */     return Math.abs(hash % this.bucketSize);
/*      */   }
/*      */   
/*      */   private void add0(Headers<T> headers) {
/* 1232 */     if (headers.isEmpty()) {
/* 1233 */       return;
/*      */     }
/*      */     
/* 1236 */     if ((headers instanceof DefaultHeaders)) {
/* 1237 */       DefaultHeaders<T> m = (DefaultHeaders)headers;
/* 1238 */       DefaultHeaders<T>.HeaderEntry e = m.head.after;
/* 1239 */       while (e != m.head) {
/* 1240 */         add(e.name, e.value);
/* 1241 */         e = e.after;
/*      */       }
/*      */     } else {
/*      */       try {
/* 1245 */         headers.forEachEntry(addAllVisitor());
/*      */       } catch (Exception ex) {
/* 1247 */         PlatformDependent.throwException(ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void add0(int h, int i, T name, T value)
/*      */   {
/* 1254 */     DefaultHeaders<T>.HeaderEntry newEntry = new HeaderEntry(h, name, value);
/* 1255 */     DefaultHeaders<T>.HeaderEntry oldTail = (HeaderEntry)this.tailEntries.get(i);
/* 1256 */     if (oldTail == null) {
/* 1257 */       this.entries.put(i, newEntry);
/*      */     } else {
/* 1259 */       oldTail.next = newEntry;
/*      */     }
/* 1261 */     this.tailEntries.put(i, newEntry);
/*      */     
/*      */ 
/* 1264 */     newEntry.addBefore(this.head);
/*      */   }
/*      */   
/*      */   private boolean remove0(int h, int i, T name) {
/* 1268 */     DefaultHeaders<T>.HeaderEntry e = (HeaderEntry)this.entries.get(i);
/* 1269 */     if (e == null) {
/* 1270 */       return false;
/*      */     }
/*      */     
/* 1273 */     boolean removed = false;
/*      */     
/* 1275 */     while ((e.hash == h) && (this.keyComparator.compare(e.name, name) == 0)) {
/* 1276 */       e.remove();
/* 1277 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/* 1278 */       if (next != null) {
/* 1279 */         this.entries.put(i, next);
/* 1280 */         e = next;
/*      */       } else {
/* 1282 */         this.entries.remove(i);
/* 1283 */         this.tailEntries.remove(i);
/* 1284 */         return true;
/*      */       }
/* 1286 */       removed = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1293 */       DefaultHeaders<T>.HeaderEntry next = e.next;
/* 1294 */       if (next == null) {
/*      */         break;
/*      */       }
/* 1297 */       if ((next.hash == h) && (this.keyComparator.compare(next.name, name) == 0)) {
/* 1298 */         e.next = next.next;
/* 1299 */         if (e.next == null) {
/* 1300 */           this.tailEntries.put(i, e);
/*      */         }
/* 1302 */         next.remove();
/* 1303 */         removed = true;
/*      */       } else {
/* 1305 */         e = next;
/*      */       }
/*      */     }
/*      */     
/* 1309 */     return removed;
/*      */   }
/*      */   
/*      */   private Headers.EntryVisitor<T> setAllVisitor() {
/* 1313 */     new Headers.EntryVisitor()
/*      */     {
/*      */       public boolean visit(Map.Entry<T, T> entry) {
/* 1316 */         DefaultHeaders.this.set(entry.getKey(), entry.getValue());
/* 1317 */         return true;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   private Headers.EntryVisitor<T> addAllVisitor() {
/* 1323 */     new Headers.EntryVisitor()
/*      */     {
/*      */       public boolean visit(Map.Entry<T, T> entry) {
/* 1326 */         DefaultHeaders.this.add(entry.getKey(), entry.getValue());
/* 1327 */         return true;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */   private final class HeaderEntry
/*      */     implements Map.Entry<T, T>
/*      */   {
/*      */     final int hash;
/*      */     
/*      */     final T name;
/*      */     T value;
/*      */     DefaultHeaders<T>.HeaderEntry next;
/*      */     DefaultHeaders<T>.HeaderEntry before;
/*      */     DefaultHeaders<T>.HeaderEntry after;
/*      */     
/*      */     HeaderEntry(T hash, T name)
/*      */     {
/* 1346 */       this.hash = hash;
/* 1347 */       this.name = name;
/* 1348 */       this.value = value;
/*      */     }
/*      */     
/*      */     HeaderEntry() {
/* 1352 */       this.hash = -1;
/* 1353 */       this.name = null;
/* 1354 */       this.value = null;
/*      */     }
/*      */     
/*      */     void remove() {
/* 1358 */       this.before.after = this.after;
/* 1359 */       this.after.before = this.before;
/* 1360 */       DefaultHeaders.this.size -= 1;
/*      */     }
/*      */     
/*      */     void addBefore(DefaultHeaders<T>.HeaderEntry e) {
/* 1364 */       this.after = e;
/* 1365 */       this.before = e.before;
/* 1366 */       this.before.after = this;
/* 1367 */       this.after.before = this;
/* 1368 */       DefaultHeaders.this.size += 1;
/*      */     }
/*      */     
/*      */     public T getKey()
/*      */     {
/* 1373 */       return (T)this.name;
/*      */     }
/*      */     
/*      */     public T getValue()
/*      */     {
/* 1378 */       return (T)this.value;
/*      */     }
/*      */     
/*      */     public T setValue(T value)
/*      */     {
/* 1383 */       ObjectUtil.checkNotNull(value, "value");
/* 1384 */       T oldValue = this.value;
/* 1385 */       this.value = value;
/* 1386 */       return oldValue;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1391 */       return this.name + '=' + this.value;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final class KeyValueHeaderIterator
/*      */     implements Iterator<Map.Entry<T, T>>
/*      */   {
/* 1401 */     private DefaultHeaders<T>.HeaderEntry current = DefaultHeaders.this.head;
/*      */     
/*      */     protected KeyValueHeaderIterator() {}
/*      */     
/* 1405 */     public boolean hasNext() { return this.current.after != DefaultHeaders.this.head; }
/*      */     
/*      */ 
/*      */     public Map.Entry<T, T> next()
/*      */     {
/* 1410 */       this.current = this.current.after;
/*      */       
/* 1412 */       if (this.current == DefaultHeaders.this.head) {
/* 1413 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/* 1416 */       return this.current;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1421 */       throw new UnsupportedOperationException();
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
/*      */   static final class HeaderDateFormat
/*      */   {
/* 1434 */     private static final ParsePosition parsePos = new ParsePosition(0);
/* 1435 */     private static final FastThreadLocal<HeaderDateFormat> dateFormatThreadLocal = new FastThreadLocal()
/*      */     {
/*      */       protected DefaultHeaders.HeaderDateFormat initialValue()
/*      */       {
/* 1439 */         return new DefaultHeaders.HeaderDateFormat(null);
/*      */       }
/*      */     };
/*      */     
/*      */     static HeaderDateFormat get() {
/* 1444 */       return (HeaderDateFormat)dateFormatThreadLocal.get();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1454 */     private final DateFormat dateFormat1 = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1462 */     private final DateFormat dateFormat2 = new SimpleDateFormat("E, dd-MMM-yy HH:mm:ss z", Locale.ENGLISH);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1470 */     private final DateFormat dateFormat3 = new SimpleDateFormat("E MMM d HH:mm:ss yyyy", Locale.ENGLISH);
/*      */     
/*      */     private HeaderDateFormat() {
/* 1473 */       TimeZone tz = TimeZone.getTimeZone("GMT");
/* 1474 */       this.dateFormat1.setTimeZone(tz);
/* 1475 */       this.dateFormat2.setTimeZone(tz);
/* 1476 */       this.dateFormat3.setTimeZone(tz);
/*      */     }
/*      */     
/*      */     long parse(String text) throws ParseException {
/* 1480 */       Date date = this.dateFormat1.parse(text, parsePos);
/* 1481 */       if (date == null) {
/* 1482 */         date = this.dateFormat2.parse(text, parsePos);
/*      */       }
/* 1484 */       if (date == null) {
/* 1485 */         date = this.dateFormat3.parse(text, parsePos);
/*      */       }
/* 1487 */       if (date == null) {
/* 1488 */         throw new ParseException(text, 0);
/*      */       }
/* 1490 */       return date.getTime();
/*      */     }
/*      */     
/*      */     long parse(String text, long defaultValue) {
/* 1494 */       Date date = this.dateFormat1.parse(text, parsePos);
/* 1495 */       if (date == null) {
/* 1496 */         date = this.dateFormat2.parse(text, parsePos);
/*      */       }
/* 1498 */       if (date == null) {
/* 1499 */         date = this.dateFormat3.parse(text, parsePos);
/*      */       }
/* 1501 */       if (date == null) {
/* 1502 */         return defaultValue;
/*      */       }
/* 1504 */       return date.getTime();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\DefaultHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */