package io.netty.handler.codec;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface Headers<T>
  extends Iterable<Map.Entry<T, T>>
{
  public abstract T get(T paramT);
  
  public abstract T get(T paramT1, T paramT2);
  
  public abstract T getAndRemove(T paramT);
  
  public abstract T getAndRemove(T paramT1, T paramT2);
  
  public abstract List<T> getAll(T paramT);
  
  public abstract List<T> getAllAndRemove(T paramT);
  
  public abstract Boolean getBoolean(T paramT);
  
  public abstract boolean getBoolean(T paramT, boolean paramBoolean);
  
  public abstract Byte getByte(T paramT);
  
  public abstract byte getByte(T paramT, byte paramByte);
  
  public abstract Character getChar(T paramT);
  
  public abstract char getChar(T paramT, char paramChar);
  
  public abstract Short getShort(T paramT);
  
  public abstract short getInt(T paramT, short paramShort);
  
  public abstract Integer getInt(T paramT);
  
  public abstract int getInt(T paramT, int paramInt);
  
  public abstract Long getLong(T paramT);
  
  public abstract long getLong(T paramT, long paramLong);
  
  public abstract Float getFloat(T paramT);
  
  public abstract float getFloat(T paramT, float paramFloat);
  
  public abstract Double getDouble(T paramT);
  
  public abstract double getDouble(T paramT, double paramDouble);
  
  public abstract Long getTimeMillis(T paramT);
  
  public abstract long getTimeMillis(T paramT, long paramLong);
  
  public abstract Boolean getBooleanAndRemove(T paramT);
  
  public abstract boolean getBooleanAndRemove(T paramT, boolean paramBoolean);
  
  public abstract Byte getByteAndRemove(T paramT);
  
  public abstract byte getByteAndRemove(T paramT, byte paramByte);
  
  public abstract Character getCharAndRemove(T paramT);
  
  public abstract char getCharAndRemove(T paramT, char paramChar);
  
  public abstract Short getShortAndRemove(T paramT);
  
  public abstract short getShortAndRemove(T paramT, short paramShort);
  
  public abstract Integer getIntAndRemove(T paramT);
  
  public abstract int getIntAndRemove(T paramT, int paramInt);
  
  public abstract Long getLongAndRemove(T paramT);
  
  public abstract long getLongAndRemove(T paramT, long paramLong);
  
  public abstract Float getFloatAndRemove(T paramT);
  
  public abstract float getFloatAndRemove(T paramT, float paramFloat);
  
  public abstract Double getDoubleAndRemove(T paramT);
  
  public abstract double getDoubleAndRemove(T paramT, double paramDouble);
  
  public abstract Long getTimeMillisAndRemove(T paramT);
  
  public abstract long getTimeMillisAndRemove(T paramT, long paramLong);
  
  public abstract List<Map.Entry<T, T>> entries();
  
  public abstract boolean contains(T paramT);
  
  public abstract boolean contains(T paramT1, T paramT2);
  
  public abstract boolean containsObject(T paramT, Object paramObject);
  
  public abstract boolean containsBoolean(T paramT, boolean paramBoolean);
  
  public abstract boolean containsByte(T paramT, byte paramByte);
  
  public abstract boolean containsChar(T paramT, char paramChar);
  
  public abstract boolean containsShort(T paramT, short paramShort);
  
  public abstract boolean containsInt(T paramT, int paramInt);
  
  public abstract boolean containsLong(T paramT, long paramLong);
  
  public abstract boolean containsFloat(T paramT, float paramFloat);
  
  public abstract boolean containsDouble(T paramT, double paramDouble);
  
  public abstract boolean containsTimeMillis(T paramT, long paramLong);
  
  public abstract boolean contains(T paramT1, T paramT2, Comparator<? super T> paramComparator);
  
  public abstract boolean contains(T paramT1, T paramT2, Comparator<? super T> paramComparator1, Comparator<? super T> paramComparator2);
  
  public abstract boolean containsObject(T paramT, Object paramObject, Comparator<? super T> paramComparator);
  
  public abstract boolean containsObject(T paramT, Object paramObject, Comparator<? super T> paramComparator1, Comparator<? super T> paramComparator2);
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract Set<T> names();
  
  public abstract List<T> namesList();
  
  public abstract Headers<T> add(T paramT1, T paramT2);
  
  public abstract Headers<T> add(T paramT, Iterable<? extends T> paramIterable);
  
  public abstract Headers<T> add(T paramT, T... paramVarArgs);
  
  public abstract Headers<T> addObject(T paramT, Object paramObject);
  
  public abstract Headers<T> addObject(T paramT, Iterable<?> paramIterable);
  
  public abstract Headers<T> addObject(T paramT, Object... paramVarArgs);
  
  public abstract Headers<T> addBoolean(T paramT, boolean paramBoolean);
  
  public abstract Headers<T> addByte(T paramT, byte paramByte);
  
  public abstract Headers<T> addChar(T paramT, char paramChar);
  
  public abstract Headers<T> addShort(T paramT, short paramShort);
  
  public abstract Headers<T> addInt(T paramT, int paramInt);
  
  public abstract Headers<T> addLong(T paramT, long paramLong);
  
  public abstract Headers<T> addFloat(T paramT, float paramFloat);
  
  public abstract Headers<T> addDouble(T paramT, double paramDouble);
  
  public abstract Headers<T> addTimeMillis(T paramT, long paramLong);
  
  public abstract Headers<T> add(Headers<T> paramHeaders);
  
  public abstract Headers<T> set(T paramT1, T paramT2);
  
  public abstract Headers<T> set(T paramT, Iterable<? extends T> paramIterable);
  
  public abstract Headers<T> set(T paramT, T... paramVarArgs);
  
  public abstract Headers<T> setObject(T paramT, Object paramObject);
  
  public abstract Headers<T> setObject(T paramT, Iterable<?> paramIterable);
  
  public abstract Headers<T> setObject(T paramT, Object... paramVarArgs);
  
  public abstract Headers<T> setBoolean(T paramT, boolean paramBoolean);
  
  public abstract Headers<T> setByte(T paramT, byte paramByte);
  
  public abstract Headers<T> setChar(T paramT, char paramChar);
  
  public abstract Headers<T> setShort(T paramT, short paramShort);
  
  public abstract Headers<T> setInt(T paramT, int paramInt);
  
  public abstract Headers<T> setLong(T paramT, long paramLong);
  
  public abstract Headers<T> setFloat(T paramT, float paramFloat);
  
  public abstract Headers<T> setDouble(T paramT, double paramDouble);
  
  public abstract Headers<T> setTimeMillis(T paramT, long paramLong);
  
  public abstract Headers<T> set(Headers<T> paramHeaders);
  
  public abstract Headers<T> setAll(Headers<T> paramHeaders);
  
  public abstract boolean remove(T paramT);
  
  public abstract Headers<T> clear();
  
  public abstract Iterator<Map.Entry<T, T>> iterator();
  
  public abstract Map.Entry<T, T> forEachEntry(EntryVisitor<T> paramEntryVisitor)
    throws Exception;
  
  public abstract T forEachName(NameVisitor<T> paramNameVisitor)
    throws Exception;
  
  public static abstract interface ValueConverter<T>
  {
    public abstract T convertObject(Object paramObject);
    
    public abstract T convertBoolean(boolean paramBoolean);
    
    public abstract boolean convertToBoolean(T paramT);
    
    public abstract T convertByte(byte paramByte);
    
    public abstract byte convertToByte(T paramT);
    
    public abstract T convertChar(char paramChar);
    
    public abstract char convertToChar(T paramT);
    
    public abstract T convertShort(short paramShort);
    
    public abstract short convertToShort(T paramT);
    
    public abstract T convertInt(int paramInt);
    
    public abstract int convertToInt(T paramT);
    
    public abstract T convertLong(long paramLong);
    
    public abstract long convertToLong(T paramT);
    
    public abstract T convertTimeMillis(long paramLong);
    
    public abstract long convertToTimeMillis(T paramT);
    
    public abstract T convertFloat(float paramFloat);
    
    public abstract float convertToFloat(T paramT);
    
    public abstract T convertDouble(double paramDouble);
    
    public abstract double convertToDouble(T paramT);
  }
  
  public static abstract interface NameVisitor<T>
  {
    public abstract boolean visit(T paramT)
      throws Exception;
  }
  
  public static abstract interface EntryVisitor<T>
  {
    public abstract boolean visit(Map.Entry<T, T> paramEntry)
      throws Exception;
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */