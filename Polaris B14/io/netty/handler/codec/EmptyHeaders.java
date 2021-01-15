/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class EmptyHeaders<T>
/*     */   implements Headers<T>
/*     */ {
/*     */   public T get(T name)
/*     */   {
/*  27 */     return null;
/*     */   }
/*     */   
/*     */   public T get(T name, T defaultValue)
/*     */   {
/*  32 */     return null;
/*     */   }
/*     */   
/*     */   public T getAndRemove(T name)
/*     */   {
/*  37 */     return null;
/*     */   }
/*     */   
/*     */   public T getAndRemove(T name, T defaultValue)
/*     */   {
/*  42 */     return null;
/*     */   }
/*     */   
/*     */   public List<T> getAll(T name)
/*     */   {
/*  47 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<T> getAllAndRemove(T name)
/*     */   {
/*  52 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public Boolean getBoolean(T name)
/*     */   {
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   public boolean getBoolean(T name, boolean defaultValue)
/*     */   {
/*  62 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Byte getByte(T name)
/*     */   {
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public byte getByte(T name, byte defaultValue)
/*     */   {
/*  72 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Character getChar(T name)
/*     */   {
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   public char getChar(T name, char defaultValue)
/*     */   {
/*  82 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Short getShort(T name)
/*     */   {
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public short getInt(T name, short defaultValue)
/*     */   {
/*  92 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Integer getInt(T name)
/*     */   {
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public int getInt(T name, int defaultValue)
/*     */   {
/* 102 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Long getLong(T name)
/*     */   {
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   public long getLong(T name, long defaultValue)
/*     */   {
/* 112 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Float getFloat(T name)
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public float getFloat(T name, float defaultValue)
/*     */   {
/* 122 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Double getDouble(T name)
/*     */   {
/* 127 */     return null;
/*     */   }
/*     */   
/*     */   public double getDouble(T name, double defaultValue)
/*     */   {
/* 132 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Long getTimeMillis(T name)
/*     */   {
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   public long getTimeMillis(T name, long defaultValue)
/*     */   {
/* 142 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Boolean getBooleanAndRemove(T name)
/*     */   {
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   public boolean getBooleanAndRemove(T name, boolean defaultValue)
/*     */   {
/* 152 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Byte getByteAndRemove(T name)
/*     */   {
/* 157 */     return null;
/*     */   }
/*     */   
/*     */   public byte getByteAndRemove(T name, byte defaultValue)
/*     */   {
/* 162 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Character getCharAndRemove(T name)
/*     */   {
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   public char getCharAndRemove(T name, char defaultValue)
/*     */   {
/* 172 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Short getShortAndRemove(T name)
/*     */   {
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   public short getShortAndRemove(T name, short defaultValue)
/*     */   {
/* 182 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Integer getIntAndRemove(T name)
/*     */   {
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   public int getIntAndRemove(T name, int defaultValue)
/*     */   {
/* 192 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Long getLongAndRemove(T name)
/*     */   {
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   public long getLongAndRemove(T name, long defaultValue)
/*     */   {
/* 202 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Float getFloatAndRemove(T name)
/*     */   {
/* 207 */     return null;
/*     */   }
/*     */   
/*     */   public float getFloatAndRemove(T name, float defaultValue)
/*     */   {
/* 212 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Double getDoubleAndRemove(T name)
/*     */   {
/* 217 */     return null;
/*     */   }
/*     */   
/*     */   public double getDoubleAndRemove(T name, double defaultValue)
/*     */   {
/* 222 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Long getTimeMillisAndRemove(T name)
/*     */   {
/* 227 */     return null;
/*     */   }
/*     */   
/*     */   public long getTimeMillisAndRemove(T name, long defaultValue)
/*     */   {
/* 232 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public List<Map.Entry<T, T>> entries()
/*     */   {
/* 237 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public boolean contains(T name)
/*     */   {
/* 242 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(T name, T value)
/*     */   {
/* 247 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsObject(T name, Object value)
/*     */   {
/* 252 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsBoolean(T name, boolean value)
/*     */   {
/* 257 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsByte(T name, byte value)
/*     */   {
/* 262 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsChar(T name, char value)
/*     */   {
/* 267 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsShort(T name, short value)
/*     */   {
/* 272 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsInt(T name, int value)
/*     */   {
/* 277 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsLong(T name, long value)
/*     */   {
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsFloat(T name, float value)
/*     */   {
/* 287 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsDouble(T name, double value)
/*     */   {
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsTimeMillis(T name, long value)
/*     */   {
/* 297 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(T name, T value, Comparator<? super T> comparator)
/*     */   {
/* 302 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contains(T name, T value, Comparator<? super T> keyComparator, Comparator<? super T> valueComparator)
/*     */   {
/* 308 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsObject(T name, Object value, Comparator<? super T> comparator)
/*     */   {
/* 313 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsObject(T name, Object value, Comparator<? super T> keyComparator, Comparator<? super T> valueComparator)
/*     */   {
/* 319 */     return false;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 324 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 329 */     return true;
/*     */   }
/*     */   
/*     */   public Set<T> names()
/*     */   {
/* 334 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   public List<T> namesList()
/*     */   {
/* 339 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public Headers<T> add(T name, T value)
/*     */   {
/* 344 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> add(T name, Iterable<? extends T> values)
/*     */   {
/* 349 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> add(T name, T... values)
/*     */   {
/* 354 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addObject(T name, Object value)
/*     */   {
/* 359 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addObject(T name, Iterable<?> values)
/*     */   {
/* 364 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addObject(T name, Object... values)
/*     */   {
/* 369 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addBoolean(T name, boolean value)
/*     */   {
/* 374 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addByte(T name, byte value)
/*     */   {
/* 379 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addChar(T name, char value)
/*     */   {
/* 384 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addShort(T name, short value)
/*     */   {
/* 389 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addInt(T name, int value)
/*     */   {
/* 394 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addLong(T name, long value)
/*     */   {
/* 399 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addFloat(T name, float value)
/*     */   {
/* 404 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addDouble(T name, double value)
/*     */   {
/* 409 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> addTimeMillis(T name, long value)
/*     */   {
/* 414 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> add(Headers<T> headers)
/*     */   {
/* 419 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> set(T name, T value)
/*     */   {
/* 424 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> set(T name, Iterable<? extends T> values)
/*     */   {
/* 429 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> set(T name, T... values)
/*     */   {
/* 434 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setObject(T name, Object value)
/*     */   {
/* 439 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setObject(T name, Iterable<?> values)
/*     */   {
/* 444 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setObject(T name, Object... values)
/*     */   {
/* 449 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setBoolean(T name, boolean value)
/*     */   {
/* 454 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setByte(T name, byte value)
/*     */   {
/* 459 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setChar(T name, char value)
/*     */   {
/* 464 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setShort(T name, short value)
/*     */   {
/* 469 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setInt(T name, int value)
/*     */   {
/* 474 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setLong(T name, long value)
/*     */   {
/* 479 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setFloat(T name, float value)
/*     */   {
/* 484 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setDouble(T name, double value)
/*     */   {
/* 489 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setTimeMillis(T name, long value)
/*     */   {
/* 494 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> set(Headers<T> headers)
/*     */   {
/* 499 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public Headers<T> setAll(Headers<T> headers)
/*     */   {
/* 504 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */   
/*     */   public boolean remove(T name)
/*     */   {
/* 509 */     return false;
/*     */   }
/*     */   
/*     */   public Headers<T> clear()
/*     */   {
/* 514 */     return this;
/*     */   }
/*     */   
/*     */   public Iterator<Map.Entry<T, T>> iterator()
/*     */   {
/* 519 */     return entries().iterator();
/*     */   }
/*     */   
/*     */   public Map.Entry<T, T> forEachEntry(Headers.EntryVisitor<T> visitor) throws Exception
/*     */   {
/* 524 */     return null;
/*     */   }
/*     */   
/*     */   public T forEachName(Headers.NameVisitor<T> visitor) throws Exception
/*     */   {
/* 529 */     return null;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 534 */     if (!(o instanceof Headers)) {
/* 535 */       return false;
/*     */     }
/*     */     
/* 538 */     Headers<?> rhs = (Headers)o;
/* 539 */     return (isEmpty()) && (rhs.isEmpty());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 544 */     return 1;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 549 */     return getClass().getSimpleName() + '[' + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\EmptyHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */