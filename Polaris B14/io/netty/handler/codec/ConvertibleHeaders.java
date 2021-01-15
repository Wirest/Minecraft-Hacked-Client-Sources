package io.netty.handler.codec;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface ConvertibleHeaders<UnconvertedType, ConvertedType>
  extends Headers<UnconvertedType>
{
  public abstract ConvertedType getAndConvert(UnconvertedType paramUnconvertedType);
  
  public abstract ConvertedType getAndConvert(UnconvertedType paramUnconvertedType, ConvertedType paramConvertedType);
  
  public abstract ConvertedType getAndRemoveAndConvert(UnconvertedType paramUnconvertedType);
  
  public abstract ConvertedType getAndRemoveAndConvert(UnconvertedType paramUnconvertedType, ConvertedType paramConvertedType);
  
  public abstract List<ConvertedType> getAllAndConvert(UnconvertedType paramUnconvertedType);
  
  public abstract List<ConvertedType> getAllAndRemoveAndConvert(UnconvertedType paramUnconvertedType);
  
  public abstract List<Map.Entry<ConvertedType, ConvertedType>> entriesConverted();
  
  public abstract Iterator<Map.Entry<ConvertedType, ConvertedType>> iteratorConverted();
  
  public abstract Set<ConvertedType> namesAndConvert(Comparator<ConvertedType> paramComparator);
  
  public static abstract interface TypeConverter<UnconvertedType, ConvertedType>
  {
    public abstract ConvertedType toConvertedType(UnconvertedType paramUnconvertedType);
    
    public abstract UnconvertedType toUnconvertedType(ConvertedType paramConvertedType);
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\ConvertibleHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */