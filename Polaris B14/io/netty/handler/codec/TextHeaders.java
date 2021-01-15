package io.netty.handler.codec;

public abstract interface TextHeaders
  extends ConvertibleHeaders<CharSequence, String>
{
  public abstract boolean contains(CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean);
  
  public abstract boolean containsObject(CharSequence paramCharSequence, Object paramObject, boolean paramBoolean);
  
  public abstract TextHeaders add(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  public abstract TextHeaders add(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
  
  public abstract TextHeaders add(CharSequence paramCharSequence, CharSequence... paramVarArgs);
  
  public abstract TextHeaders addObject(CharSequence paramCharSequence, Object paramObject);
  
  public abstract TextHeaders addObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
  
  public abstract TextHeaders addObject(CharSequence paramCharSequence, Object... paramVarArgs);
  
  public abstract TextHeaders addBoolean(CharSequence paramCharSequence, boolean paramBoolean);
  
  public abstract TextHeaders addByte(CharSequence paramCharSequence, byte paramByte);
  
  public abstract TextHeaders addChar(CharSequence paramCharSequence, char paramChar);
  
  public abstract TextHeaders addShort(CharSequence paramCharSequence, short paramShort);
  
  public abstract TextHeaders addInt(CharSequence paramCharSequence, int paramInt);
  
  public abstract TextHeaders addLong(CharSequence paramCharSequence, long paramLong);
  
  public abstract TextHeaders addFloat(CharSequence paramCharSequence, float paramFloat);
  
  public abstract TextHeaders addDouble(CharSequence paramCharSequence, double paramDouble);
  
  public abstract TextHeaders addTimeMillis(CharSequence paramCharSequence, long paramLong);
  
  public abstract TextHeaders add(TextHeaders paramTextHeaders);
  
  public abstract TextHeaders set(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  public abstract TextHeaders set(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
  
  public abstract TextHeaders set(CharSequence paramCharSequence, CharSequence... paramVarArgs);
  
  public abstract TextHeaders setObject(CharSequence paramCharSequence, Object paramObject);
  
  public abstract TextHeaders setObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
  
  public abstract TextHeaders setObject(CharSequence paramCharSequence, Object... paramVarArgs);
  
  public abstract TextHeaders setBoolean(CharSequence paramCharSequence, boolean paramBoolean);
  
  public abstract TextHeaders setByte(CharSequence paramCharSequence, byte paramByte);
  
  public abstract TextHeaders setChar(CharSequence paramCharSequence, char paramChar);
  
  public abstract TextHeaders setShort(CharSequence paramCharSequence, short paramShort);
  
  public abstract TextHeaders setInt(CharSequence paramCharSequence, int paramInt);
  
  public abstract TextHeaders setLong(CharSequence paramCharSequence, long paramLong);
  
  public abstract TextHeaders setFloat(CharSequence paramCharSequence, float paramFloat);
  
  public abstract TextHeaders setDouble(CharSequence paramCharSequence, double paramDouble);
  
  public abstract TextHeaders setTimeMillis(CharSequence paramCharSequence, long paramLong);
  
  public abstract TextHeaders set(TextHeaders paramTextHeaders);
  
  public abstract TextHeaders setAll(TextHeaders paramTextHeaders);
  
  public abstract TextHeaders clear();
  
  public static abstract interface NameVisitor
    extends Headers.NameVisitor<CharSequence>
  {}
  
  public static abstract interface EntryVisitor
    extends Headers.EntryVisitor<CharSequence>
  {}
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\TextHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */