package javassist.scopedpool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoftValueHashMap extends AbstractMap implements Map {
   private Map hash;
   private ReferenceQueue queue;

   public Set entrySet() {
      this.processQueue();
      return this.hash.entrySet();
   }

   private void processQueue() {
      SoftValueHashMap.SoftValueRef ref;
      while((ref = (SoftValueHashMap.SoftValueRef)this.queue.poll()) != null) {
         if (ref == (SoftValueHashMap.SoftValueRef)this.hash.get(ref.key)) {
            this.hash.remove(ref.key);
         }
      }

   }

   public SoftValueHashMap(int initialCapacity, float loadFactor) {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap(initialCapacity, loadFactor);
   }

   public SoftValueHashMap(int initialCapacity) {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap(initialCapacity);
   }

   public SoftValueHashMap() {
      this.queue = new ReferenceQueue();
      this.hash = new HashMap();
   }

   public SoftValueHashMap(Map t) {
      this(Math.max(2 * t.size(), 11), 0.75F);
      this.putAll(t);
   }

   public int size() {
      this.processQueue();
      return this.hash.size();
   }

   public boolean isEmpty() {
      this.processQueue();
      return this.hash.isEmpty();
   }

   public boolean containsKey(Object key) {
      this.processQueue();
      return this.hash.containsKey(key);
   }

   public Object get(Object key) {
      this.processQueue();
      SoftReference ref = (SoftReference)this.hash.get(key);
      return ref != null ? ref.get() : null;
   }

   public Object put(Object key, Object value) {
      this.processQueue();
      Object rtn = this.hash.put(key, SoftValueHashMap.SoftValueRef.create(key, value, this.queue));
      if (rtn != null) {
         rtn = ((SoftReference)rtn).get();
      }

      return rtn;
   }

   public Object remove(Object key) {
      this.processQueue();
      return this.hash.remove(key);
   }

   public void clear() {
      this.processQueue();
      this.hash.clear();
   }

   private static class SoftValueRef extends SoftReference {
      public Object key;

      private SoftValueRef(Object key, Object val, ReferenceQueue q) {
         super(val, q);
         this.key = key;
      }

      private static SoftValueHashMap.SoftValueRef create(Object key, Object val, ReferenceQueue q) {
         return val == null ? null : new SoftValueHashMap.SoftValueRef(key, val, q);
      }
   }
}
