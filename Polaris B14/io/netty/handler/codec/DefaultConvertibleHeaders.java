/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConvertibleHeaders<UnconvertedType, ConvertedType>
/*     */   extends DefaultHeaders<UnconvertedType>
/*     */   implements ConvertibleHeaders<UnconvertedType, ConvertedType>
/*     */ {
/*     */   private final ConvertibleHeaders.TypeConverter<UnconvertedType, ConvertedType> typeConverter;
/*     */   
/*     */   public DefaultConvertibleHeaders(Comparator<? super UnconvertedType> keyComparator, Comparator<? super UnconvertedType> valueComparator, DefaultHeaders.HashCodeGenerator<UnconvertedType> hashCodeGenerator, Headers.ValueConverter<UnconvertedType> valueConverter, ConvertibleHeaders.TypeConverter<UnconvertedType, ConvertedType> typeConverter)
/*     */   {
/*  35 */     super(keyComparator, valueComparator, hashCodeGenerator, valueConverter);
/*  36 */     this.typeConverter = typeConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultConvertibleHeaders(Comparator<? super UnconvertedType> keyComparator, Comparator<? super UnconvertedType> valueComparator, DefaultHeaders.HashCodeGenerator<UnconvertedType> hashCodeGenerator, Headers.ValueConverter<UnconvertedType> valueConverter, ConvertibleHeaders.TypeConverter<UnconvertedType, ConvertedType> typeConverter, DefaultHeaders.NameConverter<UnconvertedType> nameConverter)
/*     */   {
/*  45 */     super(keyComparator, valueComparator, hashCodeGenerator, valueConverter, nameConverter);
/*  46 */     this.typeConverter = typeConverter;
/*     */   }
/*     */   
/*     */   public ConvertedType getAndConvert(UnconvertedType name)
/*     */   {
/*  51 */     return (ConvertedType)getAndConvert(name, null);
/*     */   }
/*     */   
/*     */   public ConvertedType getAndConvert(UnconvertedType name, ConvertedType defaultValue)
/*     */   {
/*  56 */     UnconvertedType v = get(name);
/*  57 */     if (v == null) {
/*  58 */       return defaultValue;
/*     */     }
/*  60 */     return (ConvertedType)this.typeConverter.toConvertedType(v);
/*     */   }
/*     */   
/*     */   public ConvertedType getAndRemoveAndConvert(UnconvertedType name)
/*     */   {
/*  65 */     return (ConvertedType)getAndRemoveAndConvert(name, null);
/*     */   }
/*     */   
/*     */   public ConvertedType getAndRemoveAndConvert(UnconvertedType name, ConvertedType defaultValue)
/*     */   {
/*  70 */     UnconvertedType v = getAndRemove(name);
/*  71 */     if (v == null) {
/*  72 */       return defaultValue;
/*     */     }
/*  74 */     return (ConvertedType)this.typeConverter.toConvertedType(v);
/*     */   }
/*     */   
/*     */   public List<ConvertedType> getAllAndConvert(UnconvertedType name)
/*     */   {
/*  79 */     List<UnconvertedType> all = getAll(name);
/*  80 */     List<ConvertedType> allConverted = new ArrayList(all.size());
/*  81 */     for (int i = 0; i < all.size(); i++) {
/*  82 */       allConverted.add(this.typeConverter.toConvertedType(all.get(i)));
/*     */     }
/*  84 */     return allConverted;
/*     */   }
/*     */   
/*     */   public List<ConvertedType> getAllAndRemoveAndConvert(UnconvertedType name)
/*     */   {
/*  89 */     List<UnconvertedType> all = getAllAndRemove(name);
/*  90 */     List<ConvertedType> allConverted = new ArrayList(all.size());
/*  91 */     for (int i = 0; i < all.size(); i++) {
/*  92 */       allConverted.add(this.typeConverter.toConvertedType(all.get(i)));
/*     */     }
/*  94 */     return allConverted;
/*     */   }
/*     */   
/*     */   public List<Map.Entry<ConvertedType, ConvertedType>> entriesConverted()
/*     */   {
/*  99 */     List<Map.Entry<UnconvertedType, UnconvertedType>> entries = entries();
/* 100 */     List<Map.Entry<ConvertedType, ConvertedType>> entriesConverted = new ArrayList(entries.size());
/*     */     
/* 102 */     for (int i = 0; i < entries.size(); i++) {
/* 103 */       entriesConverted.add(new ConvertedEntry((Map.Entry)entries.get(i)));
/*     */     }
/* 105 */     return entriesConverted;
/*     */   }
/*     */   
/*     */   public Iterator<Map.Entry<ConvertedType, ConvertedType>> iteratorConverted()
/*     */   {
/* 110 */     return new ConvertedIterator(null);
/*     */   }
/*     */   
/*     */   public Set<ConvertedType> namesAndConvert(Comparator<ConvertedType> comparator)
/*     */   {
/* 115 */     Set<UnconvertedType> names = names();
/* 116 */     Set<ConvertedType> namesConverted = new TreeSet(comparator);
/* 117 */     for (UnconvertedType unconverted : names) {
/* 118 */       namesConverted.add(this.typeConverter.toConvertedType(unconverted));
/*     */     }
/* 120 */     return namesConverted;
/*     */   }
/*     */   
/*     */   private final class ConvertedIterator implements Iterator<Map.Entry<ConvertedType, ConvertedType>> {
/* 124 */     private final Iterator<Map.Entry<UnconvertedType, UnconvertedType>> iter = DefaultConvertibleHeaders.this.iterator();
/*     */     
/*     */     private ConvertedIterator() {}
/*     */     
/* 128 */     public boolean hasNext() { return this.iter.hasNext(); }
/*     */     
/*     */ 
/*     */     public Map.Entry<ConvertedType, ConvertedType> next()
/*     */     {
/* 133 */       Map.Entry<UnconvertedType, UnconvertedType> next = (Map.Entry)this.iter.next();
/*     */       
/* 135 */       return new DefaultConvertibleHeaders.ConvertedEntry(DefaultConvertibleHeaders.this, next);
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 140 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ConvertedEntry implements Map.Entry<ConvertedType, ConvertedType> {
/*     */     private final Map.Entry<UnconvertedType, UnconvertedType> entry;
/*     */     private ConvertedType name;
/*     */     private ConvertedType value;
/*     */     
/*     */     ConvertedEntry() {
/* 150 */       this.entry = entry;
/*     */     }
/*     */     
/*     */     public ConvertedType getKey()
/*     */     {
/* 155 */       if (this.name == null) {
/* 156 */         this.name = DefaultConvertibleHeaders.this.typeConverter.toConvertedType(this.entry.getKey());
/*     */       }
/* 158 */       return (ConvertedType)this.name;
/*     */     }
/*     */     
/*     */     public ConvertedType getValue()
/*     */     {
/* 163 */       if (this.value == null) {
/* 164 */         this.value = DefaultConvertibleHeaders.this.typeConverter.toConvertedType(this.entry.getValue());
/*     */       }
/* 166 */       return (ConvertedType)this.value;
/*     */     }
/*     */     
/*     */     public ConvertedType setValue(ConvertedType value)
/*     */     {
/* 171 */       ConvertedType old = getValue();
/* 172 */       this.entry.setValue(DefaultConvertibleHeaders.this.typeConverter.toUnconvertedType(value));
/* 173 */       return old;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 178 */       return this.entry.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\DefaultConvertibleHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */