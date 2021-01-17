// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.EventListener;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.SortedMap;
import com.ibm.icu.util.ULocale;
import java.util.ListIterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.lang.ref.SoftReference;
import java.util.List;

public class ICUService extends ICUNotifier
{
    protected final String name;
    private static final boolean DEBUG;
    private final ICURWLock factoryLock;
    private final List<Factory> factories;
    private int defaultSize;
    private SoftReference<Map<String, CacheEntry>> cacheref;
    private SoftReference<Map<String, Factory>> idref;
    private LocaleRef dnref;
    
    public ICUService() {
        this.factoryLock = new ICURWLock();
        this.factories = new ArrayList<Factory>();
        this.defaultSize = 0;
        this.name = "";
    }
    
    public ICUService(final String name) {
        this.factoryLock = new ICURWLock();
        this.factories = new ArrayList<Factory>();
        this.defaultSize = 0;
        this.name = name;
    }
    
    public Object get(final String descriptor) {
        return this.getKey(this.createKey(descriptor), null);
    }
    
    public Object get(final String descriptor, final String[] actualReturn) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor must not be null");
        }
        return this.getKey(this.createKey(descriptor), actualReturn);
    }
    
    public Object getKey(final Key key) {
        return this.getKey(key, null);
    }
    
    public Object getKey(final Key key, final String[] actualReturn) {
        return this.getKey(key, actualReturn, null);
    }
    
    public Object getKey(final Key key, final String[] actualReturn, final Factory factory) {
        if (this.factories.size() == 0) {
            return this.handleDefault(key, actualReturn);
        }
        if (ICUService.DEBUG) {
            System.out.println("Service: " + this.name + " key: " + key.canonicalID());
        }
        CacheEntry result = null;
        if (key != null) {
            try {
                this.factoryLock.acquireRead();
                Map<String, CacheEntry> cache = null;
                SoftReference<Map<String, CacheEntry>> cref = this.cacheref;
                if (cref != null) {
                    if (ICUService.DEBUG) {
                        System.out.println("Service " + this.name + " ref exists");
                    }
                    cache = cref.get();
                }
                if (cache == null) {
                    if (ICUService.DEBUG) {
                        System.out.println("Service " + this.name + " cache was empty");
                    }
                    cache = Collections.synchronizedMap(new HashMap<String, CacheEntry>());
                    cref = new SoftReference<Map<String, CacheEntry>>(cache);
                }
                String currentDescriptor = null;
                ArrayList<String> cacheDescriptorList = null;
                boolean putInCache = false;
                int NDebug = 0;
                int startIndex = 0;
                final int limit = this.factories.size();
                boolean cacheResult = true;
                if (factory != null) {
                    for (int i = 0; i < limit; ++i) {
                        if (factory == this.factories.get(i)) {
                            startIndex = i + 1;
                            break;
                        }
                    }
                    if (startIndex == 0) {
                        throw new IllegalStateException("Factory " + factory + "not registered with service: " + this);
                    }
                    cacheResult = false;
                }
            Label_0704:
                do {
                    currentDescriptor = key.currentDescriptor();
                    if (ICUService.DEBUG) {
                        System.out.println(this.name + "[" + NDebug++ + "] looking for: " + currentDescriptor);
                    }
                    result = cache.get(currentDescriptor);
                    if (result != null) {
                        if (ICUService.DEBUG) {
                            System.out.println(this.name + " found with descriptor: " + currentDescriptor);
                            break;
                        }
                        break;
                    }
                    else {
                        if (ICUService.DEBUG) {
                            System.out.println("did not find: " + currentDescriptor + " in cache");
                        }
                        putInCache = cacheResult;
                        int index = startIndex;
                        while (index < limit) {
                            final Factory f = this.factories.get(index++);
                            if (ICUService.DEBUG) {
                                System.out.println("trying factory[" + (index - 1) + "] " + f.toString());
                            }
                            final Object service = f.create(key, this);
                            if (service != null) {
                                result = new CacheEntry(currentDescriptor, service);
                                if (ICUService.DEBUG) {
                                    System.out.println(this.name + " factory supported: " + currentDescriptor + ", caching");
                                    break Label_0704;
                                }
                                break Label_0704;
                            }
                            else {
                                if (!ICUService.DEBUG) {
                                    continue;
                                }
                                System.out.println("factory did not support: " + currentDescriptor);
                            }
                        }
                        if (cacheDescriptorList == null) {
                            cacheDescriptorList = new ArrayList<String>(5);
                        }
                        cacheDescriptorList.add(currentDescriptor);
                    }
                } while (key.fallback());
                if (result != null) {
                    if (putInCache) {
                        if (ICUService.DEBUG) {
                            System.out.println("caching '" + result.actualDescriptor + "'");
                        }
                        cache.put(result.actualDescriptor, result);
                        if (cacheDescriptorList != null) {
                            for (final String desc : cacheDescriptorList) {
                                if (ICUService.DEBUG) {
                                    System.out.println(this.name + " adding descriptor: '" + desc + "' for actual: '" + result.actualDescriptor + "'");
                                }
                                cache.put(desc, result);
                            }
                        }
                        this.cacheref = cref;
                    }
                    if (actualReturn != null) {
                        if (result.actualDescriptor.indexOf("/") == 0) {
                            actualReturn[0] = result.actualDescriptor.substring(1);
                        }
                        else {
                            actualReturn[0] = result.actualDescriptor;
                        }
                    }
                    if (ICUService.DEBUG) {
                        System.out.println("found in service: " + this.name);
                    }
                    return result.service;
                }
            }
            finally {
                this.factoryLock.releaseRead();
            }
        }
        if (ICUService.DEBUG) {
            System.out.println("not found in service: " + this.name);
        }
        return this.handleDefault(key, actualReturn);
    }
    
    protected Object handleDefault(final Key key, final String[] actualIDReturn) {
        return null;
    }
    
    public Set<String> getVisibleIDs() {
        return this.getVisibleIDs(null);
    }
    
    public Set<String> getVisibleIDs(final String matchID) {
        Set<String> result = this.getVisibleIDMap().keySet();
        final Key fallbackKey = this.createKey(matchID);
        if (fallbackKey != null) {
            final Set<String> temp = new HashSet<String>(result.size());
            for (final String id : result) {
                if (fallbackKey.isFallbackOf(id)) {
                    temp.add(id);
                }
            }
            result = temp;
        }
        return result;
    }
    
    private Map<String, Factory> getVisibleIDMap() {
        Map<String, Factory> idcache = null;
        SoftReference<Map<String, Factory>> ref = this.idref;
        if (ref != null) {
            idcache = ref.get();
        }
        while (idcache == null) {
            synchronized (this) {
                if (ref != this.idref) {
                    if (this.idref != null) {
                        ref = this.idref;
                        idcache = ref.get();
                        continue;
                    }
                }
                try {
                    this.factoryLock.acquireRead();
                    idcache = new HashMap<String, Factory>();
                    final ListIterator<Factory> lIter = this.factories.listIterator(this.factories.size());
                    while (lIter.hasPrevious()) {
                        final Factory f = lIter.previous();
                        f.updateVisibleIDs(idcache);
                    }
                    idcache = Collections.unmodifiableMap((Map<? extends String, ? extends Factory>)idcache);
                    this.idref = new SoftReference<Map<String, Factory>>(idcache);
                }
                finally {
                    this.factoryLock.releaseRead();
                }
            }
        }
        return idcache;
    }
    
    public String getDisplayName(final String id) {
        return this.getDisplayName(id, ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String getDisplayName(final String id, final ULocale locale) {
        final Map<String, Factory> m = this.getVisibleIDMap();
        Factory f = m.get(id);
        if (f != null) {
            return f.getDisplayName(id, locale);
        }
        final Key key = this.createKey(id);
        while (key.fallback()) {
            f = m.get(key.currentID());
            if (f != null) {
                return f.getDisplayName(id, locale);
            }
        }
        return null;
    }
    
    public SortedMap<String, String> getDisplayNames() {
        final ULocale locale = ULocale.getDefault(ULocale.Category.DISPLAY);
        return this.getDisplayNames(locale, null, null);
    }
    
    public SortedMap<String, String> getDisplayNames(final ULocale locale) {
        return this.getDisplayNames(locale, null, null);
    }
    
    public SortedMap<String, String> getDisplayNames(final ULocale locale, final Comparator<Object> com) {
        return this.getDisplayNames(locale, com, null);
    }
    
    public SortedMap<String, String> getDisplayNames(final ULocale locale, final String matchID) {
        return this.getDisplayNames(locale, null, matchID);
    }
    
    public SortedMap<String, String> getDisplayNames(final ULocale locale, final Comparator<Object> com, final String matchID) {
        SortedMap<String, String> dncache = null;
        LocaleRef ref = this.dnref;
        if (ref != null) {
            dncache = ref.get(locale, com);
        }
        while (dncache == null) {
            synchronized (this) {
                if (ref == this.dnref || this.dnref == null) {
                    dncache = new TreeMap<String, String>(com);
                    final Map<String, Factory> m = this.getVisibleIDMap();
                    for (final Map.Entry<String, Factory> e : m.entrySet()) {
                        final String id = e.getKey();
                        final Factory f = e.getValue();
                        dncache.put(f.getDisplayName(id, locale), id);
                    }
                    dncache = Collections.unmodifiableSortedMap((SortedMap<String, ? extends String>)dncache);
                    this.dnref = new LocaleRef(dncache, locale, com);
                }
                else {
                    ref = this.dnref;
                    dncache = ref.get(locale, com);
                }
            }
        }
        final Key matchKey = this.createKey(matchID);
        if (matchKey == null) {
            return dncache;
        }
        final SortedMap<String, String> result = new TreeMap<String, String>(dncache);
        final Iterator<Map.Entry<String, String>> iter = result.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, String> e2 = iter.next();
            if (!matchKey.isFallbackOf(e2.getValue())) {
                iter.remove();
            }
        }
        return result;
    }
    
    public final List<Factory> factories() {
        try {
            this.factoryLock.acquireRead();
            return new ArrayList<Factory>(this.factories);
        }
        finally {
            this.factoryLock.releaseRead();
        }
    }
    
    public Factory registerObject(final Object obj, final String id) {
        return this.registerObject(obj, id, true);
    }
    
    public Factory registerObject(final Object obj, final String id, final boolean visible) {
        final String canonicalID = this.createKey(id).canonicalID();
        return this.registerFactory(new SimpleFactory(obj, canonicalID, visible));
    }
    
    public final Factory registerFactory(final Factory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        try {
            this.factoryLock.acquireWrite();
            this.factories.add(0, factory);
            this.clearCaches();
        }
        finally {
            this.factoryLock.releaseWrite();
        }
        this.notifyChanged();
        return factory;
    }
    
    public final boolean unregisterFactory(final Factory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        boolean result = false;
        try {
            this.factoryLock.acquireWrite();
            if (this.factories.remove(factory)) {
                result = true;
                this.clearCaches();
            }
        }
        finally {
            this.factoryLock.releaseWrite();
        }
        if (result) {
            this.notifyChanged();
        }
        return result;
    }
    
    public final void reset() {
        try {
            this.factoryLock.acquireWrite();
            this.reInitializeFactories();
            this.clearCaches();
        }
        finally {
            this.factoryLock.releaseWrite();
        }
        this.notifyChanged();
    }
    
    protected void reInitializeFactories() {
        this.factories.clear();
    }
    
    public boolean isDefault() {
        return this.factories.size() == this.defaultSize;
    }
    
    protected void markDefault() {
        this.defaultSize = this.factories.size();
    }
    
    public Key createKey(final String id) {
        return (id == null) ? null : new Key(id);
    }
    
    protected void clearCaches() {
        this.cacheref = null;
        this.idref = null;
        this.dnref = null;
    }
    
    protected void clearServiceCache() {
        this.cacheref = null;
    }
    
    @Override
    protected boolean acceptsListener(final EventListener l) {
        return l instanceof ServiceListener;
    }
    
    @Override
    protected void notifyListener(final EventListener l) {
        ((ServiceListener)l).serviceChanged(this);
    }
    
    public String stats() {
        final ICURWLock.Stats stats = this.factoryLock.resetStats();
        if (stats != null) {
            return stats.toString();
        }
        return "no stats";
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return super.toString() + "{" + this.name + "}";
    }
    
    static {
        DEBUG = ICUDebug.enabled("service");
    }
    
    public static class Key
    {
        private final String id;
        
        public Key(final String id) {
            this.id = id;
        }
        
        public final String id() {
            return this.id;
        }
        
        public String canonicalID() {
            return this.id;
        }
        
        public String currentID() {
            return this.canonicalID();
        }
        
        public String currentDescriptor() {
            return "/" + this.currentID();
        }
        
        public boolean fallback() {
            return false;
        }
        
        public boolean isFallbackOf(final String idToCheck) {
            return this.canonicalID().equals(idToCheck);
        }
    }
    
    public static class SimpleFactory implements Factory
    {
        protected Object instance;
        protected String id;
        protected boolean visible;
        
        public SimpleFactory(final Object instance, final String id) {
            this(instance, id, true);
        }
        
        public SimpleFactory(final Object instance, final String id, final boolean visible) {
            if (instance == null || id == null) {
                throw new IllegalArgumentException("Instance or id is null");
            }
            this.instance = instance;
            this.id = id;
            this.visible = visible;
        }
        
        public Object create(final Key key, final ICUService service) {
            if (this.id.equals(key.currentID())) {
                return this.instance;
            }
            return null;
        }
        
        public void updateVisibleIDs(final Map<String, Factory> result) {
            if (this.visible) {
                result.put(this.id, this);
            }
            else {
                result.remove(this.id);
            }
        }
        
        public String getDisplayName(final String identifier, final ULocale locale) {
            return (this.visible && this.id.equals(identifier)) ? identifier : null;
        }
        
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder(super.toString());
            buf.append(", id: ");
            buf.append(this.id);
            buf.append(", visible: ");
            buf.append(this.visible);
            return buf.toString();
        }
    }
    
    private static final class CacheEntry
    {
        final String actualDescriptor;
        final Object service;
        
        CacheEntry(final String actualDescriptor, final Object service) {
            this.actualDescriptor = actualDescriptor;
            this.service = service;
        }
    }
    
    private static class LocaleRef
    {
        private final ULocale locale;
        private SoftReference<SortedMap<String, String>> ref;
        private Comparator<Object> com;
        
        LocaleRef(final SortedMap<String, String> dnCache, final ULocale locale, final Comparator<Object> com) {
            this.locale = locale;
            this.com = com;
            this.ref = new SoftReference<SortedMap<String, String>>(dnCache);
        }
        
        SortedMap<String, String> get(final ULocale loc, final Comparator<Object> comp) {
            final SortedMap<String, String> m = this.ref.get();
            if (m != null && this.locale.equals(loc) && (this.com == comp || (this.com != null && this.com.equals(comp)))) {
                return m;
            }
            return null;
        }
    }
    
    public interface ServiceListener extends EventListener
    {
        void serviceChanged(final ICUService p0);
    }
    
    public interface Factory
    {
        Object create(final Key p0, final ICUService p1);
        
        void updateVisibleIDs(final Map<String, Factory> p0);
        
        String getDisplayName(final String p0, final ULocale p1);
    }
}
