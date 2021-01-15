package io.netty.handler.codec;

public abstract interface BinaryHeaders
  extends Headers<AsciiString>
{
  public abstract BinaryHeaders add(AsciiString paramAsciiString1, AsciiString paramAsciiString2);
  
  public abstract BinaryHeaders add(AsciiString paramAsciiString, Iterable<? extends AsciiString> paramIterable);
  
  public abstract BinaryHeaders add(AsciiString paramAsciiString, AsciiString... paramVarArgs);
  
  public abstract BinaryHeaders addObject(AsciiString paramAsciiString, Object paramObject);
  
  public abstract BinaryHeaders addObject(AsciiString paramAsciiString, Iterable<?> paramIterable);
  
  public abstract BinaryHeaders addObject(AsciiString paramAsciiString, Object... paramVarArgs);
  
  public abstract BinaryHeaders addBoolean(AsciiString paramAsciiString, boolean paramBoolean);
  
  public abstract BinaryHeaders addByte(AsciiString paramAsciiString, byte paramByte);
  
  public abstract BinaryHeaders addChar(AsciiString paramAsciiString, char paramChar);
  
  public abstract BinaryHeaders addShort(AsciiString paramAsciiString, short paramShort);
  
  public abstract BinaryHeaders addInt(AsciiString paramAsciiString, int paramInt);
  
  public abstract BinaryHeaders addLong(AsciiString paramAsciiString, long paramLong);
  
  public abstract BinaryHeaders addFloat(AsciiString paramAsciiString, float paramFloat);
  
  public abstract BinaryHeaders addDouble(AsciiString paramAsciiString, double paramDouble);
  
  public abstract BinaryHeaders addTimeMillis(AsciiString paramAsciiString, long paramLong);
  
  public abstract BinaryHeaders add(BinaryHeaders paramBinaryHeaders);
  
  public abstract BinaryHeaders set(AsciiString paramAsciiString1, AsciiString paramAsciiString2);
  
  public abstract BinaryHeaders set(AsciiString paramAsciiString, Iterable<? extends AsciiString> paramIterable);
  
  public abstract BinaryHeaders set(AsciiString paramAsciiString, AsciiString... paramVarArgs);
  
  public abstract BinaryHeaders setObject(AsciiString paramAsciiString, Object paramObject);
  
  public abstract BinaryHeaders setObject(AsciiString paramAsciiString, Iterable<?> paramIterable);
  
  public abstract BinaryHeaders setObject(AsciiString paramAsciiString, Object... paramVarArgs);
  
  public abstract BinaryHeaders setBoolean(AsciiString paramAsciiString, boolean paramBoolean);
  
  public abstract BinaryHeaders setByte(AsciiString paramAsciiString, byte paramByte);
  
  public abstract BinaryHeaders setChar(AsciiString paramAsciiString, char paramChar);
  
  public abstract BinaryHeaders setShort(AsciiString paramAsciiString, short paramShort);
  
  public abstract BinaryHeaders setInt(AsciiString paramAsciiString, int paramInt);
  
  public abstract BinaryHeaders setLong(AsciiString paramAsciiString, long paramLong);
  
  public abstract BinaryHeaders setFloat(AsciiString paramAsciiString, float paramFloat);
  
  public abstract BinaryHeaders setDouble(AsciiString paramAsciiString, double paramDouble);
  
  public abstract BinaryHeaders setTimeMillis(AsciiString paramAsciiString, long paramLong);
  
  public abstract BinaryHeaders set(BinaryHeaders paramBinaryHeaders);
  
  public abstract BinaryHeaders setAll(BinaryHeaders paramBinaryHeaders);
  
  public abstract BinaryHeaders clear();
  
  public static abstract interface NameVisitor
    extends Headers.NameVisitor<AsciiString>
  {}
  
  public static abstract interface EntryVisitor
    extends Headers.EntryVisitor<AsciiString>
  {}
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\BinaryHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */