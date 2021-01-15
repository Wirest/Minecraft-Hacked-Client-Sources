/*     */ package ch.qos.logback.core.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractComponentTracker<C>
/*     */   implements ComponentTracker<C>
/*     */ {
/*     */   private static final boolean ACCESS_ORDERED = true;
/*     */   public static final long LINGERING_TIMEOUT = 10000L;
/*     */   public static final long WAIT_BETWEEN_SUCCESSIVE_REMOVAL_ITERATIONS = 1000L;
/*     */   protected int maxComponents;
/*     */   protected long timeout;
/*     */   LinkedHashMap<String, Entry<C>> liveMap;
/*     */   LinkedHashMap<String, Entry<C>> lingerersMap;
/*     */   long lastCheck;
/*     */   private RemovalPredicator<C> byExcedent;
/*     */   private RemovalPredicator<C> byTimeout;
/*     */   private RemovalPredicator<C> byLingering;
/*     */   
/*     */   public int getComponentCount()
/*     */   {
/*  79 */     return this.liveMap.size() + this.lingerersMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Entry<C> getFromEitherMap(String key)
/*     */   {
/*  89 */     Entry<C> entry = (Entry)this.liveMap.get(key);
/*  90 */     if (entry != null) {
/*  91 */       return entry;
/*     */     }
/*  93 */     return (Entry)this.lingerersMap.get(key);
/*     */   }
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
/*     */   public synchronized C find(String key)
/*     */   {
/* 107 */     Entry<C> entry = getFromEitherMap(key);
/* 108 */     if (entry == null) return null;
/* 109 */     return (C)entry.component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized C getOrCreate(String key, long timestamp)
/*     */   {
/* 122 */     Entry<C> entry = getFromEitherMap(key);
/* 123 */     if (entry == null) {
/* 124 */       C c = buildComponent(key);
/* 125 */       entry = new Entry(key, c, timestamp);
/*     */       
/* 127 */       this.liveMap.put(key, entry);
/*     */     } else {
/* 129 */       entry.setTimestamp(timestamp);
/*     */     }
/* 131 */     return (C)entry.component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endOfLife(String key)
/*     */   {
/* 140 */     Entry entry = (Entry)this.liveMap.remove(key);
/* 141 */     if (entry == null)
/* 142 */       return;
/* 143 */     this.lingerersMap.put(key, entry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void removeStaleComponents(long now)
/*     */   {
/* 153 */     if (isTooSoonForRemovalIteration(now)) return;
/* 154 */     removeExcedentComponents();
/* 155 */     removeStaleComponentsFromMainMap(now);
/* 156 */     removeStaleComponentsFromLingerersMap(now);
/*     */   }
/*     */   
/*     */   private void removeExcedentComponents() {
/* 160 */     genericStaleComponentRemover(this.liveMap, 0L, this.byExcedent);
/*     */   }
/*     */   
/*     */   private void removeStaleComponentsFromMainMap(long now) {
/* 164 */     genericStaleComponentRemover(this.liveMap, now, this.byTimeout);
/*     */   }
/*     */   
/*     */   private void removeStaleComponentsFromLingerersMap(long now) {
/* 168 */     genericStaleComponentRemover(this.lingerersMap, now, this.byLingering);
/*     */   }
/*     */   
/*     */   private void genericStaleComponentRemover(LinkedHashMap<String, Entry<C>> map, long now, RemovalPredicator<C> removalPredicator)
/*     */   {
/* 173 */     Iterator<Map.Entry<String, Entry<C>>> iter = map.entrySet().iterator();
/* 174 */     while (iter.hasNext()) {
/* 175 */       Map.Entry<String, Entry<C>> mapEntry = (Map.Entry)iter.next();
/* 176 */       Entry<C> entry = (Entry)mapEntry.getValue();
/* 177 */       if (!removalPredicator.isSlatedForRemoval(entry, now)) break;
/* 178 */       iter.remove();
/* 179 */       C c = entry.component;
/* 180 */       processPriorToRemoval(c);
/*     */     }
/*     */   }
/*     */   
/*     */   public AbstractComponentTracker()
/*     */   {
/*  43 */     this.maxComponents = Integer.MAX_VALUE;
/*  44 */     this.timeout = 1800000L;
/*     */     
/*     */ 
/*  47 */     this.liveMap = new LinkedHashMap(32, 0.75F, true);
/*     */     
/*     */ 
/*  50 */     this.lingerersMap = new LinkedHashMap(16, 0.75F, true);
/*  51 */     this.lastCheck = 0L;
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
/* 187 */     this.byExcedent = new RemovalPredicator() {
/*     */       public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) {
/* 189 */         return AbstractComponentTracker.this.liveMap.size() > AbstractComponentTracker.this.maxComponents;
/*     */       }
/*     */       
/* 192 */     };
/* 193 */     this.byTimeout = new RemovalPredicator() {
/*     */       public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) {
/* 195 */         return AbstractComponentTracker.this.isEntryStale(entry, timestamp);
/*     */       }
/* 197 */     };
/* 198 */     this.byLingering = new RemovalPredicator()
/*     */     {
/* 200 */       public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) { return AbstractComponentTracker.this.isEntryDoneLingering(entry, timestamp); }
/*     */     };
/*     */   }
/*     */   
/*     */   private boolean isTooSoonForRemovalIteration(long now) {
/* 205 */     if (this.lastCheck + 1000L > now) {
/* 206 */       return true;
/*     */     }
/* 208 */     this.lastCheck = now;
/* 209 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isEntryStale(Entry<C> entry, long now)
/*     */   {
/* 215 */     C c = entry.component;
/* 216 */     if (isComponentStale(c)) {
/* 217 */       return true;
/*     */     }
/* 219 */     return entry.timestamp + this.timeout < now;
/*     */   }
/*     */   
/*     */   private boolean isEntryDoneLingering(Entry entry, long now) {
/* 223 */     return entry.timestamp + 10000L < now;
/*     */   }
/*     */   
/*     */   public Set<String> allKeys() {
/* 227 */     HashSet<String> allKeys = new HashSet(this.liveMap.keySet());
/* 228 */     allKeys.addAll(this.lingerersMap.keySet());
/* 229 */     return allKeys;
/*     */   }
/*     */   
/*     */   public Collection<C> allComponents() {
/* 233 */     List<C> allComponents = new ArrayList();
/* 234 */     for (Entry<C> e : this.liveMap.values())
/* 235 */       allComponents.add(e.component);
/* 236 */     for (Entry<C> e : this.lingerersMap.values()) {
/* 237 */       allComponents.add(e.component);
/*     */     }
/* 239 */     return allComponents;
/*     */   }
/*     */   
/*     */   public long getTimeout() {
/* 243 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public void setTimeout(long timeout) {
/* 247 */     this.timeout = timeout;
/*     */   }
/*     */   
/*     */   public int getMaxComponents() {
/* 251 */     return this.maxComponents;
/*     */   }
/*     */   
/*     */ 
/* 255 */   public void setMaxComponents(int maxComponents) { this.maxComponents = maxComponents; }
/*     */   
/*     */   protected abstract void processPriorToRemoval(C paramC);
/*     */   
/*     */   protected abstract C buildComponent(String paramString);
/*     */   
/*     */   protected abstract boolean isComponentStale(C paramC);
/*     */   
/*     */   private static class Entry<C> {
/*     */     String key;
/*     */     C component;
/*     */     long timestamp;
/*     */     
/*     */     Entry(String k, C c, long timestamp) {
/* 269 */       this.key = k;
/* 270 */       this.component = c;
/* 271 */       this.timestamp = timestamp;
/*     */     }
/*     */     
/*     */     public void setTimestamp(long timestamp) {
/* 275 */       this.timestamp = timestamp;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 280 */       return this.key.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 285 */       if (this == obj)
/* 286 */         return true;
/* 287 */       if (obj == null)
/* 288 */         return false;
/* 289 */       if (getClass() != obj.getClass())
/* 290 */         return false;
/* 291 */       Entry other = (Entry)obj;
/* 292 */       if (this.key == null) {
/* 293 */         if (other.key != null)
/* 294 */           return false;
/* 295 */       } else if (!this.key.equals(other.key))
/* 296 */         return false;
/* 297 */       if (this.component == null) {
/* 298 */         if (other.component != null)
/* 299 */           return false;
/* 300 */       } else if (!this.component.equals(other.component))
/* 301 */         return false;
/* 302 */       return true;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 307 */       return "(" + this.key + ", " + this.component + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract interface RemovalPredicator<C>
/*     */   {
/*     */     public abstract boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> paramEntry, long paramLong);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\AbstractComponentTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */