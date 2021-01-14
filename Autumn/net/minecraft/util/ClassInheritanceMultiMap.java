package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap extends AbstractSet {
   private static final Set field_181158_a = Sets.newHashSet();
   private final Map map = Maps.newHashMap();
   private final Set knownKeys = Sets.newIdentityHashSet();
   private final Class baseClass;
   private final List field_181745_e = Lists.newArrayList();

   public ClassInheritanceMultiMap(Class baseClassIn) {
      this.baseClass = baseClassIn;
      this.knownKeys.add(baseClassIn);
      this.map.put(baseClassIn, this.field_181745_e);
      Iterator var2 = field_181158_a.iterator();

      while(var2.hasNext()) {
         Class oclass = (Class)var2.next();
         this.createLookup(oclass);
      }

   }

   protected void createLookup(Class clazz) {
      field_181158_a.add(clazz);
      Iterator var2 = this.field_181745_e.iterator();

      while(var2.hasNext()) {
         Object t = var2.next();
         if (clazz.isAssignableFrom(t.getClass())) {
            this.func_181743_a(t, clazz);
         }
      }

      this.knownKeys.add(clazz);
   }

   protected Class func_181157_b(Class p_181157_1_) {
      if (this.baseClass.isAssignableFrom(p_181157_1_)) {
         if (!this.knownKeys.contains(p_181157_1_)) {
            this.createLookup(p_181157_1_);
         }

         return p_181157_1_;
      } else {
         throw new IllegalArgumentException("Don't know how to search for " + p_181157_1_);
      }
   }

   public boolean add(Object p_add_1_) {
      Iterator var2 = this.knownKeys.iterator();

      while(var2.hasNext()) {
         Class oclass = (Class)var2.next();
         if (oclass.isAssignableFrom(p_add_1_.getClass())) {
            this.func_181743_a(p_add_1_, oclass);
         }
      }

      return true;
   }

   private void func_181743_a(Object p_181743_1_, Class p_181743_2_) {
      List list = (List)this.map.get(p_181743_2_);
      if (list == null) {
         this.map.put(p_181743_2_, Lists.newArrayList(new Object[]{p_181743_1_}));
      } else {
         list.add(p_181743_1_);
      }

   }

   public boolean remove(Object p_remove_1_) {
      Object t = p_remove_1_;
      boolean flag = false;
      Iterator var4 = this.knownKeys.iterator();

      while(var4.hasNext()) {
         Class oclass = (Class)var4.next();
         if (oclass.isAssignableFrom(t.getClass())) {
            List list = (List)this.map.get(oclass);
            if (list != null && list.remove(t)) {
               flag = true;
            }
         }
      }

      return flag;
   }

   public boolean contains(Object p_contains_1_) {
      return Iterators.contains(this.getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
   }

   public Iterable getByClass(final Class clazz) {
      return new Iterable() {
         public Iterator iterator() {
            List list = (List)ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.func_181157_b(clazz));
            if (list == null) {
               return Iterators.emptyIterator();
            } else {
               Iterator iterator = list.iterator();
               return Iterators.filter(iterator, clazz);
            }
         }
      };
   }

   public Iterator iterator() {
      return this.field_181745_e.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator());
   }

   public int size() {
      return this.field_181745_e.size();
   }
}
