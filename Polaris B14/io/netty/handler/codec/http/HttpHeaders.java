package io.netty.handler.codec.http;

import io.netty.handler.codec.TextHeaders;

public abstract interface HttpHeaders
  extends TextHeaders
{
  public abstract HttpHeaders add(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  public abstract HttpHeaders add(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
  
  public abstract HttpHeaders add(CharSequence paramCharSequence, CharSequence... paramVarArgs);
  
  public abstract HttpHeaders addObject(CharSequence paramCharSequence, Object paramObject);
  
  public abstract HttpHeaders addObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
  
  public abstract HttpHeaders addObject(CharSequence paramCharSequence, Object... paramVarArgs);
  
  public abstract HttpHeaders addBoolean(CharSequence paramCharSequence, boolean paramBoolean);
  
  public abstract HttpHeaders addByte(CharSequence paramCharSequence, byte paramByte);
  
  public abstract HttpHeaders addChar(CharSequence paramCharSequence, char paramChar);
  
  public abstract HttpHeaders addShort(CharSequence paramCharSequence, short paramShort);
  
  public abstract HttpHeaders addInt(CharSequence paramCharSequence, int paramInt);
  
  public abstract HttpHeaders addLong(CharSequence paramCharSequence, long paramLong);
  
  public abstract HttpHeaders addFloat(CharSequence paramCharSequence, float paramFloat);
  
  public abstract HttpHeaders addDouble(CharSequence paramCharSequence, double paramDouble);
  
  public abstract HttpHeaders addTimeMillis(CharSequence paramCharSequence, long paramLong);
  
  public abstract HttpHeaders add(TextHeaders paramTextHeaders);
  
  public abstract HttpHeaders set(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  public abstract HttpHeaders set(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
  
  public abstract HttpHeaders set(CharSequence paramCharSequence, CharSequence... paramVarArgs);
  
  public abstract HttpHeaders setObject(CharSequence paramCharSequence, Object paramObject);
  
  public abstract HttpHeaders setObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
  
  public abstract HttpHeaders setObject(CharSequence paramCharSequence, Object... paramVarArgs);
  
  public abstract HttpHeaders setBoolean(CharSequence paramCharSequence, boolean paramBoolean);
  
  public abstract HttpHeaders setByte(CharSequence paramCharSequence, byte paramByte);
  
  public abstract HttpHeaders setChar(CharSequence paramCharSequence, char paramChar);
  
  public abstract HttpHeaders setShort(CharSequence paramCharSequence, short paramShort);
  
  public abstract HttpHeaders setInt(CharSequence paramCharSequence, int paramInt);
  
  public abstract HttpHeaders setLong(CharSequence paramCharSequence, long paramLong);
  
  public abstract HttpHeaders setFloat(CharSequence paramCharSequence, float paramFloat);
  
  public abstract HttpHeaders setDouble(CharSequence paramCharSequence, double paramDouble);
  
  public abstract HttpHeaders setTimeMillis(CharSequence paramCharSequence, long paramLong);
  
  public abstract HttpHeaders set(TextHeaders paramTextHeaders);
  
  public abstract HttpHeaders setAll(TextHeaders paramTextHeaders);
  
  public abstract HttpHeaders clear();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */