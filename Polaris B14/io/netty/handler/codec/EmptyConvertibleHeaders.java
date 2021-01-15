/*    */ package io.netty.handler.codec;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmptyConvertibleHeaders<UnconvertedType, ConvertedType>
/*    */   extends EmptyHeaders<UnconvertedType>
/*    */   implements ConvertibleHeaders<UnconvertedType, ConvertedType>
/*    */ {
/*    */   public ConvertedType getAndConvert(UnconvertedType name)
/*    */   {
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public ConvertedType getAndConvert(UnconvertedType name, ConvertedType defaultValue)
/*    */   {
/* 34 */     return defaultValue;
/*    */   }
/*    */   
/*    */   public ConvertedType getAndRemoveAndConvert(UnconvertedType name)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public ConvertedType getAndRemoveAndConvert(UnconvertedType name, ConvertedType defaultValue)
/*    */   {
/* 44 */     return defaultValue;
/*    */   }
/*    */   
/*    */   public List<ConvertedType> getAllAndConvert(UnconvertedType name)
/*    */   {
/* 49 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public List<ConvertedType> getAllAndRemoveAndConvert(UnconvertedType name)
/*    */   {
/* 54 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public List<Map.Entry<ConvertedType, ConvertedType>> entriesConverted()
/*    */   {
/* 59 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public Iterator<Map.Entry<ConvertedType, ConvertedType>> iteratorConverted()
/*    */   {
/* 64 */     return entriesConverted().iterator();
/*    */   }
/*    */   
/*    */   public Set<ConvertedType> namesAndConvert(Comparator<ConvertedType> comparator)
/*    */   {
/* 69 */     return Collections.emptySet();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\EmptyConvertibleHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */