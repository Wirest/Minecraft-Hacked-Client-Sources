package org.reflections;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.lang.annotation.Inherited;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.Utils;

public class Store {
   private final Map storeMap;
   private final transient boolean concurrent;
   private static final transient Supplier listSupplier = new Supplier() {
      public List get() {
         return Lists.newArrayList();
      }
   };
   private static final Predicate isConstructor = new Predicate() {
      public boolean apply(@Nullable String input) {
         return Utils.isConstructor(input);
      }
   };

   protected Store() {
      this(false);
   }

   protected Store(boolean concurrent) {
      this.concurrent = concurrent;
      this.storeMap = new HashMap();
   }

   protected ListMultimap createMultimap() {
      ListMultimap multimap = Multimaps.newListMultimap(new HashMap(), listSupplier);
      return this.concurrent ? Multimaps.synchronizedListMultimap(multimap) : multimap;
   }

   public Multimap getOrCreate(String indexName) {
      if (indexName.contains(".")) {
         indexName = indexName.substring(indexName.lastIndexOf(".") + 1);
      }

      Multimap mmap = (Multimap)this.storeMap.get(indexName);
      if (mmap == null) {
         this.storeMap.put(indexName, mmap = this.createMultimap());
      }

      return (Multimap)mmap;
   }

   @Nullable
   public Multimap get(Class scannerClass) {
      return (Multimap)this.storeMap.get(scannerClass.getSimpleName());
   }

   public Set get(Class scannerClass, String... keys) {
      Set result = Sets.newHashSet();
      Multimap map = this.get(scannerClass);
      if (map != null) {
         String[] arr$ = keys;
         int len$ = keys.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String key = arr$[i$];
            result.addAll(map.get(key));
         }
      }

      return result;
   }

   public Set get(Class scannerClass, Iterable keys) {
      Set result = Sets.newHashSet();
      Multimap map = this.get(scannerClass);
      if (map != null) {
         Iterator i$ = keys.iterator();

         while(i$.hasNext()) {
            String key = (String)i$.next();
            result.addAll(map.get(key));
         }
      }

      return result;
   }

   public Map getStoreMap() {
      return this.storeMap;
   }

   void merge(Store outer) {
      if (outer != null) {
         Iterator i$ = outer.storeMap.keySet().iterator();

         while(i$.hasNext()) {
            String indexName = (String)i$.next();
            this.getOrCreate(indexName).putAll((Multimap)outer.storeMap.get(indexName));
         }
      }

   }

   public Integer getKeysCount() {
      Integer keys = 0;

      Multimap multimap;
      for(Iterator i$ = this.storeMap.values().iterator(); i$.hasNext(); keys = keys + multimap.keySet().size()) {
         multimap = (Multimap)i$.next();
      }

      return keys;
   }

   public Integer getValuesCount() {
      Integer values = 0;

      Multimap multimap;
      for(Iterator i$ = this.storeMap.values().iterator(); i$.hasNext(); values = values + multimap.size()) {
         multimap = (Multimap)i$.next();
      }

      return values;
   }

   public Set getSubTypesOf(String type) {
      Set result = new HashSet();
      Set subTypes = this.get(SubTypesScanner.class, type);
      result.addAll(subTypes);
      Iterator i$ = subTypes.iterator();

      while(i$.hasNext()) {
         String subType = (String)i$.next();
         result.addAll(this.getSubTypesOf(subType));
      }

      return result;
   }

   public Set getTypesAnnotatedWithDirectly(String annotation) {
      return this.get(TypeAnnotationsScanner.class, annotation);
   }

   public Set getTypesAnnotatedWith(String annotation) {
      return this.getTypesAnnotatedWith(annotation, true);
   }

   public Set getTypesAnnotatedWith(String annotation, boolean honorInherited) {
      Set result = new HashSet();
      if (this.isAnnotation(annotation)) {
         Set types = this.getTypesAnnotatedWithDirectly(annotation);
         Set inherited = this.getInheritedSubTypes(types, annotation, honorInherited);
         result.addAll(inherited);
      }

      return result;
   }

   public Set getInheritedSubTypes(Iterable types, String annotation, boolean honorInherited) {
      Set result = Sets.newHashSet(types);
      Iterator i$;
      String type;
      if (honorInherited && this.isInheritedAnnotation(annotation)) {
         i$ = types.iterator();

         while(i$.hasNext()) {
            type = (String)i$.next();
            if (this.isClass(type)) {
               result.addAll(this.getSubTypesOf(type));
            }
         }
      } else if (!honorInherited) {
         i$ = types.iterator();

         while(i$.hasNext()) {
            type = (String)i$.next();
            if (this.isAnnotation(type)) {
               result.addAll(this.getTypesAnnotatedWith(type, false));
            } else {
               result.addAll(this.getSubTypesOf(type));
            }
         }
      }

      return result;
   }

   public Set getMethodsAnnotatedWith(String annotation) {
      return Sets.filter(this.get(MethodAnnotationsScanner.class, annotation), Predicates.not(isConstructor));
   }

   public Set getFieldsAnnotatedWith(String annotation) {
      return this.get(FieldAnnotationsScanner.class, annotation);
   }

   public Set getConstructorsAnnotatedWith(String annotation) {
      return Sets.filter(this.get(MethodAnnotationsScanner.class, annotation), isConstructor);
   }

   public Set getResources(String key) {
      return this.get(ResourcesScanner.class, key);
   }

   public Set getResources(Predicate namePredicate) {
      Multimap mmap = this.get(ResourcesScanner.class);
      return (Set)(mmap != null ? this.get(ResourcesScanner.class, (Iterable)Collections2.filter(mmap.keySet(), namePredicate)) : Sets.newHashSet());
   }

   public Set getResources(final Pattern pattern) {
      return this.getResources(new Predicate() {
         public boolean apply(String input) {
            return pattern.matcher(input).matches();
         }
      });
   }

   public boolean isClass(String type) {
      return !ReflectionUtils.forName(type).isInterface();
   }

   public boolean isAnnotation(String typeAnnotatedWith) {
      Multimap mmap = this.get(TypeAnnotationsScanner.class);
      return mmap != null && mmap.keySet().contains(typeAnnotatedWith);
   }

   public boolean isInheritedAnnotation(String typeAnnotatedWith) {
      Multimap mmap = this.get(TypeAnnotationsScanner.class);
      return mmap != null && mmap.get(Inherited.class.getName()).contains(typeAnnotatedWith);
   }
}
