// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.chmv8;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.atomic.AtomicReference;
import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.Enumeration;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapV8<K, V> implements ConcurrentMap<K, V>, Serializable
{
    private static final long serialVersionUID = 7249069246763182397L;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MOVED = -1;
    static final int TREEBIN = -2;
    static final int RESERVED = -3;
    static final int HASH_BITS = Integer.MAX_VALUE;
    static final int NCPU;
    private static final ObjectStreamField[] serialPersistentFields;
    transient volatile Node<K, V>[] table;
    private transient volatile Node<K, V>[] nextTable;
    private transient volatile long baseCount;
    private transient volatile int sizeCtl;
    private transient volatile int transferIndex;
    private transient volatile int transferOrigin;
    private transient volatile int cellsBusy;
    private transient volatile CounterCell[] counterCells;
    private transient KeySetView<K, V> keySet;
    private transient ValuesView<K, V> values;
    private transient EntrySetView<K, V> entrySet;
    static final AtomicInteger counterHashCodeGenerator;
    static final int SEED_INCREMENT = 1640531527;
    private static final Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long TRANSFERORIGIN;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;
    
    static final int spread(final int h) {
        return (h ^ h >>> 16) & Integer.MAX_VALUE;
    }
    
    private static final int tableSizeFor(final int c) {
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : ((n >= 1073741824) ? 1073741824 : (n + 1));
    }
    
    static Class<?> comparableClassFor(final Object x) {
        if (x instanceof Comparable) {
            final Class<?> c;
            if ((c = x.getClass()) == String.class) {
                return c;
            }
            final Type[] ts;
            if ((ts = c.getGenericInterfaces()) != null) {
                for (int i = 0; i < ts.length; ++i) {
                    final Type t;
                    final ParameterizedType p;
                    final Type[] as;
                    if ((t = ts[i]) instanceof ParameterizedType && (p = (ParameterizedType)t).getRawType() == Comparable.class && (as = p.getActualTypeArguments()) != null && as.length == 1 && as[0] == c) {
                        return c;
                    }
                }
            }
        }
        return null;
    }
    
    static int compareComparables(final Class<?> kc, final Object k, final Object x) {
        return (x == null || x.getClass() != kc) ? 0 : ((Comparable)k).compareTo(x);
    }
    
    static final <K, V> Node<K, V> tabAt(final Node<K, V>[] tab, final int i) {
        return (Node<K, V>)ConcurrentHashMapV8.U.getObjectVolatile(tab, ((long)i << ConcurrentHashMapV8.ASHIFT) + ConcurrentHashMapV8.ABASE);
    }
    
    static final <K, V> boolean casTabAt(final Node<K, V>[] tab, final int i, final Node<K, V> c, final Node<K, V> v) {
        return ConcurrentHashMapV8.U.compareAndSwapObject(tab, ((long)i << ConcurrentHashMapV8.ASHIFT) + ConcurrentHashMapV8.ABASE, c, v);
    }
    
    static final <K, V> void setTabAt(final Node<K, V>[] tab, final int i, final Node<K, V> v) {
        ConcurrentHashMapV8.U.putObjectVolatile(tab, ((long)i << ConcurrentHashMapV8.ASHIFT) + ConcurrentHashMapV8.ABASE, v);
    }
    
    public ConcurrentHashMapV8() {
    }
    
    public ConcurrentHashMapV8(final int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        final int cap = (initialCapacity >= 536870912) ? 1073741824 : tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1);
        this.sizeCtl = cap;
    }
    
    public ConcurrentHashMapV8(final Map<? extends K, ? extends V> m) {
        this.sizeCtl = 16;
        this.putAll(m);
    }
    
    public ConcurrentHashMapV8(final int initialCapacity, final float loadFactor) {
        this(initialCapacity, loadFactor, 1);
    }
    
    public ConcurrentHashMapV8(int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        if (loadFactor <= 0.0f || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity < concurrencyLevel) {
            initialCapacity = concurrencyLevel;
        }
        final long size = (long)(1.0 + initialCapacity / loadFactor);
        final int cap = (size >= 1073741824L) ? 1073741824 : tableSizeFor((int)size);
        this.sizeCtl = cap;
    }
    
    @Override
    public int size() {
        final long n = this.sumCount();
        return (n < 0L) ? 0 : ((n > 2147483647L) ? Integer.MAX_VALUE : ((int)n));
    }
    
    @Override
    public boolean isEmpty() {
        return this.sumCount() <= 0L;
    }
    
    @Override
    public V get(final Object key) {
        final int h = spread(key.hashCode());
        final Node<K, V>[] tab;
        final int n;
        Node<K, V> e;
        if ((tab = this.table) != null && (n = tab.length) > 0 && (e = tabAt(tab, n - 1 & h)) != null) {
            final int eh;
            if ((eh = e.hash) == h) {
                final K ek;
                if ((ek = e.key) == key || (ek != null && key.equals(ek))) {
                    return e.val;
                }
            }
            else if (eh < 0) {
                final Node<K, V> p;
                return ((p = e.find(h, key)) != null) ? p.val : null;
            }
            while ((e = e.next) != null) {
                final K ek;
                if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                    return e.val;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.get(key) != null;
    }
    
    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] t;
        if ((t = this.table) != null) {
            final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                final V v;
                if ((v = p.val) == value || (v != null && value.equals(v))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public V put(final K key, final V value) {
        return this.putVal(key, value, false);
    }
    
    final V putVal(final K key, final V value, final boolean onlyIfAbsent) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        final int hash = spread(key.hashCode());
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            final int n;
            if (tab == null || (n = tab.length) == 0) {
                tab = this.initTable();
            }
            else {
                final int i;
                final Node<K, V> f;
                if ((f = tabAt(tab, i = (n - 1 & hash))) == null) {
                    if (casTabAt(tab, i, null, new Node<K, V>(hash, key, value, null))) {
                        break;
                    }
                    continue;
                }
                else {
                    final int fh;
                    if ((fh = f.hash) == -1) {
                        tab = this.helpTransfer(tab, f);
                    }
                    else {
                        V oldVal = null;
                        synchronized (f) {
                            Label_0308: {
                                if (tabAt(tab, i) == f) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> e = f;
                                        K ek;
                                        while (e.hash != hash || ((ek = e.key) != key && (ek == null || !key.equals(ek)))) {
                                            final Node<K, V> pred = e;
                                            if ((e = e.next) == null) {
                                                pred.next = (Node<K, V>)new Node<Object, Object>(hash, (K)key, (V)value, null);
                                                break Label_0308;
                                            }
                                            ++binCount;
                                        }
                                        oldVal = e.val;
                                        if (!onlyIfAbsent) {
                                            e.val = value;
                                        }
                                    }
                                    else if (f instanceof TreeBin) {
                                        binCount = 2;
                                        final Node<K, V> p;
                                        if ((p = ((TreeBin)f).putTreeVal(hash, key, value)) != null) {
                                            oldVal = p.val;
                                            if (!onlyIfAbsent) {
                                                p.val = value;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (binCount == 0) {
                            continue;
                        }
                        if (binCount >= 8) {
                            this.treeifyBin(tab, i);
                        }
                        if (oldVal != null) {
                            return oldVal;
                        }
                        break;
                    }
                }
            }
        }
        this.addCount(1L, binCount);
        return null;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.tryPresize(m.size());
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.putVal(e.getKey(), e.getValue(), false);
        }
    }
    
    @Override
    public V remove(final Object key) {
        return this.replaceNode(key, null, null);
    }
    
    final V replaceNode(final Object key, final V value, final Object cv) {
        final int hash = spread(key.hashCode());
        Node<K, V>[] tab = this.table;
        int n;
        while (tab != null && (n = tab.length) != 0) {
            final int i;
            final Node<K, V> f;
            if ((f = tabAt(tab, i = (n - 1 & hash))) == null) {
                break;
            }
            final int fh;
            if ((fh = f.hash) == -1) {
                tab = this.helpTransfer(tab, f);
            }
            else {
                V oldVal = null;
                boolean validated = false;
                synchronized (f) {
                    Label_0372: {
                        if (tabAt(tab, i) == f) {
                            if (fh >= 0) {
                                validated = true;
                                Node<K, V> e = f;
                                Node<K, V> pred = null;
                                K ek;
                                while (e.hash != hash || ((ek = e.key) != key && (ek == null || !key.equals(ek)))) {
                                    pred = e;
                                    if ((e = e.next) == null) {
                                        break Label_0372;
                                    }
                                }
                                final V ev = e.val;
                                if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                                    oldVal = ev;
                                    if (value != null) {
                                        e.val = value;
                                    }
                                    else if (pred != null) {
                                        pred.next = e.next;
                                    }
                                    else {
                                        setTabAt(tab, i, e.next);
                                    }
                                }
                            }
                            else if (f instanceof TreeBin) {
                                validated = true;
                                final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                final TreeNode<K, V> r;
                                final TreeNode<K, V> p;
                                if ((r = t.root) != null && (p = r.findTreeNode(hash, key, null)) != null) {
                                    final V pv = p.val;
                                    if (cv == null || cv == pv || (pv != null && cv.equals(pv))) {
                                        oldVal = pv;
                                        if (value != null) {
                                            p.val = value;
                                        }
                                        else if (t.removeTreeNode(p)) {
                                            setTabAt(tab, i, (Node<K, V>)untreeify((Node<K, V>)t.first));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!validated) {
                    continue;
                }
                if (oldVal != null) {
                    if (value == null) {
                        this.addCount(-1L, -1);
                    }
                    return oldVal;
                }
                break;
            }
        }
        return null;
    }
    
    @Override
    public void clear() {
        long delta = 0L;
        int i = 0;
        Node<K, V>[] tab = this.table;
        while (tab != null && i < tab.length) {
            final Node<K, V> f = tabAt(tab, i);
            if (f == null) {
                ++i;
            }
            else {
                final int fh;
                if ((fh = f.hash) == -1) {
                    tab = this.helpTransfer(tab, f);
                    i = 0;
                }
                else {
                    synchronized (f) {
                        if (tabAt(tab, i) != f) {
                            continue;
                        }
                        final Object o = (fh >= 0) ? f : ((f instanceof TreeBin) ? ((TreeBin)f).first : null);
                        Node<K, V> p = f;
                        while (f != null) {
                            --delta;
                            p = f.next;
                        }
                        setTabAt(tab, i++, null);
                    }
                }
            }
        }
        if (delta != 0L) {
            this.addCount(delta, -1);
        }
    }
    
    @Override
    public KeySetView<K, V> keySet() {
        final KeySetView<K, V> ks;
        return ((ks = this.keySet) != null) ? ks : (this.keySet = new KeySetView<K, V>(this, null));
    }
    
    @Override
    public Collection<V> values() {
        final ValuesView<K, V> vs;
        return (Collection<V>)(((vs = this.values) != null) ? vs : (this.values = new ValuesView<K, V>(this)));
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final EntrySetView<K, V> es;
        return ((es = this.entrySet) != null) ? es : (this.entrySet = new EntrySetView<K, V>(this));
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        final Node<K, V>[] t;
        if ((t = this.table) != null) {
            final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                h += (p.key.hashCode() ^ p.val.hashCode());
            }
        }
        return h;
    }
    
    @Override
    public String toString() {
        final Node<K, V>[] t;
        final int f = ((t = this.table) == null) ? 0 : t.length;
        final Traverser<K, V> it = new Traverser<K, V>(t, f, 0, f);
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> p;
        if ((p = it.advance()) != null) {
            while (true) {
                final K k = p.key;
                final V v = p.val;
                sb.append((k == this) ? "(this Map)" : k);
                sb.append('=');
                sb.append((v == this) ? "(this Map)" : v);
                if ((p = it.advance()) == null) {
                    break;
                }
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof Map)) {
                return false;
            }
            final Map<?, ?> m = (Map<?, ?>)o;
            final Node<K, V>[] t;
            final int f = ((t = this.table) == null) ? 0 : t.length;
            final Traverser<K, V> it = new Traverser<K, V>(t, f, 0, f);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                final V val = p.val;
                final Object v = m.get(p.key);
                if (v == null || (v != val && !v.equals(val))) {
                    return false;
                }
            }
            for (final Map.Entry<?, ?> e : m.entrySet()) {
                final Object mk;
                final Object mv;
                final Object v2;
                if ((mk = e.getKey()) == null || (mv = e.getValue()) == null || (v2 = this.get(mk)) == null || (mv != v2 && !mv.equals(v2))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        int sshift = 0;
        int ssize;
        for (ssize = 1; ssize < 16; ssize <<= 1) {
            ++sshift;
        }
        final int segmentShift = 32 - sshift;
        final int segmentMask = ssize - 1;
        Segment<K, V>[] segments = (Segment<K, V>[])new Segment[16];
        for (int i = 0; i < segments.length; ++i) {
            segments[i] = new Segment<K, V>(0.75f);
        }
        s.putFields().put("segments", segments);
        s.putFields().put("segmentShift", segmentShift);
        s.putFields().put("segmentMask", segmentMask);
        s.writeFields();
        final Node<K, V>[] t;
        if ((t = this.table) != null) {
            final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                s.writeObject(p.key);
                s.writeObject(p.val);
            }
        }
        s.writeObject(null);
        s.writeObject(null);
        segments = null;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        this.sizeCtl = -1;
        s.defaultReadObject();
        long size = 0L;
        Node<K, V> p = null;
        while (true) {
            final K k = (K)s.readObject();
            final V v = (V)s.readObject();
            if (k == null || v == null) {
                break;
            }
            p = new Node<K, V>(spread(k.hashCode()), k, v, p);
            ++size;
        }
        if (size == 0L) {
            this.sizeCtl = 0;
        }
        else {
            int n;
            if (size >= 536870912L) {
                n = 1073741824;
            }
            else {
                final int sz = (int)size;
                n = tableSizeFor(sz + (sz >>> 1) + 1);
            }
            final Node<K, V>[] tab = (Node<K, V>[])new Node[n];
            final int mask = n - 1;
            long added = 0L;
            while (p != null) {
                final Node<K, V> next = p.next;
                final int h = p.hash;
                final int j = h & mask;
                final Node<K, V> first;
                boolean insertAtFront;
                if ((first = tabAt(tab, j)) == null) {
                    insertAtFront = true;
                }
                else {
                    final K i = p.key;
                    if (first.hash < 0) {
                        final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)first;
                        if (t.putTreeVal(h, i, p.val) == null) {
                            ++added;
                        }
                        insertAtFront = false;
                    }
                    else {
                        int binCount = 0;
                        insertAtFront = true;
                        for (Node<K, V> q = first; q != null; q = q.next) {
                            final K qk;
                            if (q.hash == h && ((qk = q.key) == i || (qk != null && i.equals(qk)))) {
                                insertAtFront = false;
                                break;
                            }
                            ++binCount;
                        }
                        if (insertAtFront && binCount >= 8) {
                            insertAtFront = false;
                            ++added;
                            p.next = first;
                            TreeNode<K, V> hd = null;
                            TreeNode<K, V> tl = null;
                            for (Node<K, V> q = p; q != null; q = q.next) {
                                final TreeNode<K, V> t2 = new TreeNode<K, V>(q.hash, q.key, q.val, null, null);
                                if ((t2.prev = tl) == null) {
                                    hd = t2;
                                }
                                else {
                                    tl.next = t2;
                                }
                                tl = t2;
                            }
                            setTabAt(tab, j, new TreeBin<K, V>(hd));
                        }
                    }
                }
                if (insertAtFront) {
                    ++added;
                    p.next = first;
                    setTabAt(tab, j, p);
                }
                p = next;
            }
            this.table = tab;
            this.sizeCtl = n - (n >>> 2);
            this.baseCount = added;
        }
    }
    
    @Override
    public V putIfAbsent(final K key, final V value) {
        return this.putVal(key, value, true);
    }
    
    @Override
    public boolean remove(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        return value != null && this.replaceNode(key, null, value) != null;
    }
    
    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(key, newValue, oldValue) != null;
    }
    
    @Override
    public V replace(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(key, value, null);
    }
    
    @Override
    public V getOrDefault(final Object key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) == null) ? defaultValue : v;
    }
    
    public void forEach(final BiAction<? super K, ? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] t;
        if ((t = this.table) != null) {
            final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                action.apply((Object)p.key, (Object)p.val);
            }
        }
    }
    
    public void replaceAll(final BiFun<? super K, ? super V, ? extends V> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] t;
        if ((t = this.table) != null) {
            final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
            Node<K, V> p;
            while ((p = it.advance()) != null) {
                V oldValue = p.val;
                final K key = p.key;
                V newValue;
                do {
                    newValue = (V)function.apply((Object)key, (Object)oldValue);
                    if (newValue == null) {
                        throw new NullPointerException();
                    }
                } while (this.replaceNode(key, newValue, oldValue) == null && (oldValue = this.get(key)) != null);
            }
        }
    }
    
    public V computeIfAbsent(final K key, final Fun<? super K, ? extends V> mappingFunction) {
        if (key == null || mappingFunction == null) {
            throw new NullPointerException();
        }
        final int h = spread(key.hashCode());
        V val = null;
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            final int n;
            if (tab == null || (n = tab.length) == 0) {
                tab = this.initTable();
            }
            else {
                final int i;
                final Node<K, V> f;
                if ((f = tabAt(tab, i = (n - 1 & h))) == null) {
                    final Node<K, V> r = new ReservationNode<K, V>();
                    synchronized (r) {
                        if (casTabAt(tab, i, null, r)) {
                            binCount = 1;
                            Node<K, V> node = null;
                            try {
                                if ((val = (V)mappingFunction.apply((Object)key)) != null) {
                                    node = new Node<K, V>(h, key, val, null);
                                }
                            }
                            finally {
                                setTabAt(tab, i, node);
                            }
                        }
                    }
                    if (binCount != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    final int fh;
                    if ((fh = f.hash) == -1) {
                        tab = this.helpTransfer(tab, f);
                    }
                    else {
                        boolean added = false;
                        synchronized (f) {
                            Label_0433: {
                                if (tabAt(tab, i) == f) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> e = f;
                                        K ek;
                                        while (e.hash != h || ((ek = e.key) != key && (ek == null || !key.equals(ek)))) {
                                            final Node<K, V> pred = e;
                                            if ((e = e.next) == null) {
                                                if ((val = (V)mappingFunction.apply((Object)key)) != null) {
                                                    added = true;
                                                    pred.next = (Node<K, V>)new Node<Object, Object>(h, (K)key, (V)val, null);
                                                }
                                                break Label_0433;
                                            }
                                            ++binCount;
                                        }
                                        val = e.val;
                                    }
                                    else if (f instanceof TreeBin) {
                                        binCount = 2;
                                        final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                        final TreeNode<K, V> r2;
                                        final TreeNode<K, V> p;
                                        if ((r2 = t.root) != null && (p = r2.findTreeNode(h, key, null)) != null) {
                                            val = p.val;
                                        }
                                        else if ((val = (V)mappingFunction.apply((Object)key)) != null) {
                                            added = true;
                                            t.putTreeVal(h, key, val);
                                        }
                                    }
                                }
                            }
                        }
                        if (binCount == 0) {
                            continue;
                        }
                        if (binCount >= 8) {
                            this.treeifyBin(tab, i);
                        }
                        if (!added) {
                            return val;
                        }
                        break;
                    }
                }
            }
        }
        if (val != null) {
            this.addCount(1L, binCount);
        }
        return val;
    }
    
    public V computeIfPresent(final K key, final BiFun<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null || remappingFunction == null) {
            throw new NullPointerException();
        }
        final int h = spread(key.hashCode());
        V val = null;
        int delta = 0;
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            final int n;
            if (tab == null || (n = tab.length) == 0) {
                tab = this.initTable();
            }
            else {
                final int i;
                final Node<K, V> f;
                if ((f = tabAt(tab, i = (n - 1 & h))) == null) {
                    break;
                }
                final int fh;
                if ((fh = f.hash) == -1) {
                    tab = this.helpTransfer(tab, f);
                }
                else {
                    synchronized (f) {
                        Label_0371: {
                            if (tabAt(tab, i) == f) {
                                if (fh >= 0) {
                                    binCount = 1;
                                    Node<K, V> e = f;
                                    Node<K, V> pred = null;
                                    K ek;
                                    while (f.hash != h || ((ek = f.key) != key && (ek == null || !key.equals(ek)))) {
                                        pred = f;
                                        if ((e = f.next) == null) {
                                            break Label_0371;
                                        }
                                        ++binCount;
                                    }
                                    val = (V)remappingFunction.apply((Object)key, (Object)f.val);
                                    if (val != null) {
                                        f.val = val;
                                    }
                                    else {
                                        delta = -1;
                                        final Node<K, V> en = f.next;
                                        if (pred != null) {
                                            pred.next = en;
                                        }
                                        else {
                                            setTabAt(tab, i, en);
                                        }
                                    }
                                }
                                else if (f instanceof TreeBin) {
                                    binCount = 2;
                                    final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                    final TreeNode<K, V> r;
                                    final TreeNode<K, V> p;
                                    if ((r = t.root) != null && (p = r.findTreeNode(h, key, null)) != null) {
                                        val = (V)remappingFunction.apply((Object)key, (Object)p.val);
                                        if (val != null) {
                                            p.val = val;
                                        }
                                        else {
                                            delta = -1;
                                            if (t.removeTreeNode(p)) {
                                                setTabAt(tab, i, (Node<K, V>)untreeify((Node<K, V>)t.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (binCount != 0) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val;
    }
    
    public V compute(final K key, final BiFun<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null || remappingFunction == null) {
            throw new NullPointerException();
        }
        final int h = spread(key.hashCode());
        V val = null;
        int delta = 0;
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            final int n;
            if (tab == null || (n = tab.length) == 0) {
                tab = this.initTable();
            }
            else {
                final int i;
                final Node<K, V> f;
                if ((f = tabAt(tab, i = (n - 1 & h))) == null) {
                    final Node<K, V> r = new ReservationNode<K, V>();
                    synchronized (r) {
                        if (casTabAt(tab, i, null, r)) {
                            binCount = 1;
                            Node<K, V> node = null;
                            try {
                                if ((val = (V)remappingFunction.apply((Object)key, (Object)null)) != null) {
                                    delta = 1;
                                    node = new Node<K, V>(h, key, val, null);
                                }
                            }
                            finally {
                                setTabAt(tab, i, node);
                            }
                        }
                    }
                    if (binCount != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    final int fh;
                    if ((fh = f.hash) == -1) {
                        tab = this.helpTransfer(tab, f);
                    }
                    else {
                        synchronized (f) {
                            Label_0560: {
                                if (tabAt(tab, i) == f) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> e = f;
                                        Node<K, V> pred = null;
                                        K ek;
                                        while (f.hash != h || ((ek = f.key) != key && (ek == null || !key.equals(ek)))) {
                                            pred = f;
                                            if ((e = f.next) == null) {
                                                val = (V)remappingFunction.apply((Object)key, (Object)null);
                                                if (val != null) {
                                                    delta = 1;
                                                    f.next = (Node<K, V>)new Node<Object, Object>(h, (K)key, (V)val, null);
                                                }
                                                break Label_0560;
                                            }
                                            ++binCount;
                                        }
                                        val = (V)remappingFunction.apply((Object)key, (Object)f.val);
                                        if (val != null) {
                                            f.val = val;
                                        }
                                        else {
                                            delta = -1;
                                            final Node<K, V> en = f.next;
                                            if (pred != null) {
                                                pred.next = en;
                                            }
                                            else {
                                                setTabAt(tab, i, en);
                                            }
                                        }
                                    }
                                    else if (f instanceof TreeBin) {
                                        binCount = 1;
                                        final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                        final TreeNode<K, V> r2;
                                        TreeNode<K, V> p;
                                        if ((r2 = t.root) != null) {
                                            p = r2.findTreeNode(h, key, null);
                                        }
                                        else {
                                            p = null;
                                        }
                                        final V pv = (p == null) ? null : p.val;
                                        val = (V)remappingFunction.apply((Object)key, (Object)pv);
                                        if (val != null) {
                                            if (p != null) {
                                                p.val = val;
                                            }
                                            else {
                                                delta = 1;
                                                t.putTreeVal(h, key, val);
                                            }
                                        }
                                        else if (p != null) {
                                            delta = -1;
                                            if (t.removeTreeNode(p)) {
                                                setTabAt(tab, i, (Node<K, V>)untreeify((Node<K, V>)t.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (binCount == 0) {
                            continue;
                        }
                        if (binCount >= 8) {
                            this.treeifyBin(tab, i);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val;
    }
    
    public V merge(final K key, final V value, final BiFun<? super V, ? super V, ? extends V> remappingFunction) {
        if (key == null || value == null || remappingFunction == null) {
            throw new NullPointerException();
        }
        final int h = spread(key.hashCode());
        V val = null;
        int delta = 0;
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            final int n;
            if (tab == null || (n = tab.length) == 0) {
                tab = this.initTable();
            }
            else {
                final int i;
                final Node<K, V> f;
                if ((f = tabAt(tab, i = (n - 1 & h))) == null) {
                    if (casTabAt(tab, i, null, new Node<K, V>(h, key, value, null))) {
                        delta = 1;
                        val = value;
                        break;
                    }
                    continue;
                }
                else {
                    final int fh;
                    if ((fh = f.hash) == -1) {
                        tab = this.helpTransfer(tab, f);
                    }
                    else {
                        synchronized (f) {
                            Label_0469: {
                                if (tabAt(tab, i) == f) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> e = f;
                                        Node<K, V> pred = null;
                                        K ek;
                                        while (f.hash != h || ((ek = f.key) != key && (ek == null || !key.equals(ek)))) {
                                            pred = f;
                                            if ((e = f.next) == null) {
                                                delta = 1;
                                                val = value;
                                                f.next = (Node<K, V>)new Node<Object, Object>(h, (K)key, (V)val, null);
                                                break Label_0469;
                                            }
                                            ++binCount;
                                        }
                                        val = (V)remappingFunction.apply((Object)f.val, (Object)value);
                                        if (val != null) {
                                            f.val = val;
                                        }
                                        else {
                                            delta = -1;
                                            final Node<K, V> en = f.next;
                                            if (pred != null) {
                                                pred.next = en;
                                            }
                                            else {
                                                setTabAt(tab, i, en);
                                            }
                                        }
                                    }
                                    else if (f instanceof TreeBin) {
                                        binCount = 2;
                                        final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                        final TreeNode<K, V> r = t.root;
                                        final TreeNode<K, V> p = (r == null) ? null : r.findTreeNode(h, key, null);
                                        val = ((p == null) ? value : remappingFunction.apply((Object)p.val, (Object)value));
                                        if (val != null) {
                                            if (p != null) {
                                                p.val = val;
                                            }
                                            else {
                                                delta = 1;
                                                t.putTreeVal(h, key, val);
                                            }
                                        }
                                        else if (p != null) {
                                            delta = -1;
                                            if (t.removeTreeNode(p)) {
                                                setTabAt(tab, i, (Node<K, V>)untreeify((Node<K, V>)t.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (binCount == 0) {
                            continue;
                        }
                        if (binCount >= 8) {
                            this.treeifyBin(tab, i);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val;
    }
    
    @Deprecated
    public boolean contains(final Object value) {
        return this.containsValue(value);
    }
    
    public Enumeration<K> keys() {
        final Node<K, V>[] t;
        final int f = ((t = this.table) == null) ? 0 : t.length;
        return new KeyIterator<K, Object>(t, f, 0, f, this);
    }
    
    public Enumeration<V> elements() {
        final Node<K, V>[] t;
        final int f = ((t = this.table) == null) ? 0 : t.length;
        return new ValueIterator<Object, V>(t, f, 0, f, this);
    }
    
    public long mappingCount() {
        final long n = this.sumCount();
        return (n < 0L) ? 0L : n;
    }
    
    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8<K, Boolean>(), Boolean.TRUE);
    }
    
    public static <K> KeySetView<K, Boolean> newKeySet(final int initialCapacity) {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8<K, Boolean>(initialCapacity), Boolean.TRUE);
    }
    
    public KeySetView<K, V> keySet(final V mappedValue) {
        if (mappedValue == null) {
            throw new NullPointerException();
        }
        return new KeySetView<K, V>(this, mappedValue);
    }
    
    private final Node<K, V>[] initTable() {
        Node<K, V>[] tab;
        while ((tab = this.table) == null || tab.length == 0) {
            int sc;
            if ((sc = this.sizeCtl) < 0) {
                Thread.yield();
            }
            else {
                if (ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, -1)) {
                    try {
                        if ((tab = this.table) == null || tab.length == 0) {
                            final int n = (sc > 0) ? sc : 16;
                            final Node<K, V>[] nt = (Node<K, V>[])new Node[n];
                            tab = (this.table = nt);
                            sc = n - (n >>> 2);
                        }
                    }
                    finally {
                        this.sizeCtl = sc;
                    }
                    break;
                }
                continue;
            }
        }
        return tab;
    }
    
    private final void addCount(final long x, final int check) {
        long s = 0L;
        Label_0142: {
            final CounterCell[] as;
            if ((as = this.counterCells) == null) {
                final Unsafe u = ConcurrentHashMapV8.U;
                final long basecount = ConcurrentHashMapV8.BASECOUNT;
                final long b = this.baseCount;
                if (u.compareAndSwapLong(this, basecount, b, s = b + x)) {
                    break Label_0142;
                }
            }
            boolean uncontended = true;
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final IntegerHolder hc;
            final int m;
            final CounterCell a;
            final long v;
            if ((hc = threadLocals.counterHashCode()) == null || as == null || (m = as.length - 1) < 0 || (a = as[m & hc.value]) == null || !(uncontended = ConcurrentHashMapV8.U.compareAndSwapLong(a, ConcurrentHashMapV8.CELLVALUE, v = a.value, v + x))) {
                this.fullAddCount(threadLocals, x, hc, uncontended);
                return;
            }
            if (check <= 1) {
                return;
            }
            s = this.sumCount();
        }
        if (check >= 0) {
            int sc;
            Node<K, V>[] tab;
            while (s >= (sc = this.sizeCtl) && (tab = this.table) != null && tab.length < 1073741824) {
                if (sc < 0) {
                    if (sc == -1 || this.transferIndex <= this.transferOrigin) {
                        break;
                    }
                    final Node<K, V>[] nt;
                    if ((nt = this.nextTable) == null) {
                        break;
                    }
                    if (ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, sc - 1)) {
                        this.transfer(tab, nt);
                    }
                }
                else if (ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, -2)) {
                    this.transfer(tab, null);
                }
                s = this.sumCount();
            }
        }
    }
    
    final Node<K, V>[] helpTransfer(final Node<K, V>[] tab, final Node<K, V> f) {
        final Node<K, V>[] nextTab;
        if (f instanceof ForwardingNode && (nextTab = (Node<K, V>[])((ForwardingNode)f).nextTable) != null) {
            final int sc;
            if (nextTab == this.nextTable && tab == this.table && this.transferIndex > this.transferOrigin && (sc = this.sizeCtl) < -1 && ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, sc - 1)) {
                this.transfer(tab, nextTab);
            }
            return nextTab;
        }
        return this.table;
    }
    
    private final void tryPresize(final int size) {
        final int c = (size >= 536870912) ? 1073741824 : tableSizeFor(size + (size >>> 1) + 1);
        int sc;
        while ((sc = this.sizeCtl) >= 0) {
            final Node<K, V>[] tab = this.table;
            int n;
            if (tab == null || (n = tab.length) == 0) {
                n = ((sc > c) ? sc : c);
                if (!ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, -1)) {
                    continue;
                }
                try {
                    if (this.table != tab) {
                        continue;
                    }
                    final Node<K, V>[] nt = (Node<K, V>[])new Node[n];
                    this.table = nt;
                    sc = n - (n >>> 2);
                }
                finally {
                    this.sizeCtl = sc;
                }
            }
            else {
                if (c <= sc) {
                    break;
                }
                if (n >= 1073741824) {
                    break;
                }
                if (tab != this.table || !ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, -2)) {
                    continue;
                }
                this.transfer(tab, null);
            }
        }
    }
    
    private final void transfer(final Node<K, V>[] tab, Node<K, V>[] nextTab) {
        final int n = tab.length;
        int stride;
        if ((stride = ((ConcurrentHashMapV8.NCPU > 1) ? ((n >>> 3) / ConcurrentHashMapV8.NCPU) : n)) < 16) {
            stride = 16;
        }
        if (nextTab == null) {
            try {
                final Node<K, V>[] nt = nextTab = (Node<K, V>[])new Node[n << 1];
            }
            catch (Throwable ex) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
            this.nextTable = nextTab;
            this.transferOrigin = n;
            this.transferIndex = n;
            final ForwardingNode<K, V> rev = new ForwardingNode<K, V>(tab);
            int k = n;
            while (k > 0) {
                int m;
                int nextk;
                for (nextk = (m = ((k > stride) ? (k - stride) : 0)); m < k; ++m) {
                    nextTab[m] = rev;
                }
                for (m = n + nextk; m < n + k; ++m) {
                    nextTab[m] = rev;
                }
                ConcurrentHashMapV8.U.putOrderedInt(this, ConcurrentHashMapV8.TRANSFERORIGIN, k = nextk);
            }
        }
        final int nextn = nextTab.length;
        final ForwardingNode<K, V> fwd = new ForwardingNode<K, V>(nextTab);
        boolean advance = true;
        boolean finishing = false;
        int i = 0;
        int bound = 0;
        while (true) {
            if (advance) {
                if (--i >= bound || finishing) {
                    advance = false;
                }
                else {
                    final int nextIndex;
                    if ((nextIndex = this.transferIndex) <= this.transferOrigin) {
                        i = -1;
                        advance = false;
                    }
                    else {
                        final int expected;
                        final int nextBound;
                        if (!ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.TRANSFERINDEX, expected, nextBound = (((expected = nextIndex) > stride) ? (nextIndex - stride) : 0))) {
                            continue;
                        }
                        bound = nextBound;
                        i = nextIndex - 1;
                        advance = false;
                    }
                }
            }
            else if (i < 0 || i >= n || i + n >= nextn) {
                if (finishing) {
                    this.nextTable = null;
                    this.table = nextTab;
                    this.sizeCtl = (n << 1) - (n >>> 1);
                    return;
                }
                int sc;
                while (!ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc = this.sizeCtl, ++sc)) {}
                if (sc != -1) {
                    return;
                }
                advance = (finishing = true);
                i = n;
            }
            else {
                final Node<K, V> f;
                if ((f = tabAt(tab, i)) == null) {
                    if (!casTabAt(tab, i, null, fwd)) {
                        continue;
                    }
                    setTabAt(nextTab, i, null);
                    setTabAt(nextTab, i + n, null);
                    advance = true;
                }
                else {
                    final int fh;
                    if ((fh = f.hash) == -1) {
                        advance = true;
                    }
                    else {
                        synchronized (f) {
                            if (tabAt(tab, i) != f) {
                                continue;
                            }
                            if (fh >= 0) {
                                int runBit = fh & n;
                                Node<K, V> lastRun = f;
                                for (Node<K, V> p = f.next; p != null; p = p.next) {
                                    final int b = p.hash & n;
                                    if (b != runBit) {
                                        runBit = b;
                                        lastRun = p;
                                    }
                                }
                                if (runBit == 0) {
                                    final Node<K, V> ln = f;
                                    final Node<K, V> hn = null;
                                }
                                else {
                                    final Node<K, V> hn = f;
                                    final Node<K, V> ln = null;
                                }
                                Node<K, V> p = f;
                                while (f != f) {
                                    final int ph = f.hash;
                                    final K pk = f.key;
                                    final V pv = f.val;
                                    if ((ph & n) == 0x0) {
                                        final Node<K, V> ln = new Node<K, V>(ph, pk, pv, f);
                                    }
                                    else {
                                        final Node<K, V> hn = new Node<K, V>(ph, pk, pv, f);
                                    }
                                    p = f.next;
                                }
                                setTabAt(nextTab, i, f);
                                setTabAt(nextTab, i + n, f);
                                setTabAt(tab, i, fwd);
                                advance = true;
                            }
                            else {
                                if (!(f instanceof TreeBin)) {
                                    continue;
                                }
                                final TreeBin<K, V> t = (TreeBin<K, V>)(TreeBin)f;
                                TreeNode<K, V> lo = null;
                                TreeNode<K, V> loTail = null;
                                TreeNode<K, V> hi = null;
                                TreeNode<K, V> hiTail = null;
                                int lc = 0;
                                int hc = 0;
                                for (Node<K, V> e = t.first; e != null; e = e.next) {
                                    final int h = e.hash;
                                    final TreeNode<K, V> p2 = new TreeNode<K, V>(h, e.key, e.val, null, null);
                                    if ((h & n) == 0x0) {
                                        if ((p2.prev = loTail) == null) {
                                            lo = p2;
                                        }
                                        else {
                                            loTail.next = p2;
                                        }
                                        loTail = p2;
                                        ++lc;
                                    }
                                    else {
                                        if ((p2.prev = hiTail) == null) {
                                            hi = p2;
                                        }
                                        else {
                                            hiTail.next = p2;
                                        }
                                        hiTail = p2;
                                        ++hc;
                                    }
                                }
                                final Node<K, V> ln = (lc <= 6) ? untreeify(lo) : ((hc != 0) ? new TreeBin<K, V>(lo) : t);
                                final Node<K, V> hn = (hc <= 6) ? untreeify(hi) : ((lc != 0) ? new TreeBin<K, V>(hi) : t);
                                setTabAt(nextTab, i, ln);
                                setTabAt(nextTab, i + n, hn);
                                setTabAt(tab, i, fwd);
                                advance = true;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private final void treeifyBin(final Node<K, V>[] tab, final int index) {
        if (tab != null) {
            final int n;
            if ((n = tab.length) < 64) {
                final int sc;
                if (tab == this.table && (sc = this.sizeCtl) >= 0 && ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.SIZECTL, sc, -2)) {
                    this.transfer(tab, null);
                }
            }
            else {
                final Node<K, V> b;
                if ((b = tabAt(tab, index)) != null && b.hash >= 0) {
                    synchronized (b) {
                        if (tabAt(tab, index) == b) {
                            TreeNode<K, V> hd = null;
                            TreeNode<K, V> tl = null;
                            for (Node<K, V> e = b; e != null; e = e.next) {
                                final TreeNode<K, V> p = new TreeNode<K, V>(e.hash, e.key, e.val, null, null);
                                if ((p.prev = tl) == null) {
                                    hd = p;
                                }
                                else {
                                    tl.next = p;
                                }
                                tl = p;
                            }
                            setTabAt(tab, index, new TreeBin<K, V>(hd));
                        }
                    }
                }
            }
        }
    }
    
    static <K, V> Node<K, V> untreeify(final Node<K, V> b) {
        Node<K, V> hd = null;
        Node<K, V> tl = null;
        for (Node<K, V> q = b; q != null; q = q.next) {
            final Node<K, V> p = new Node<K, V>(q.hash, q.key, q.val, null);
            if (tl == null) {
                hd = p;
            }
            else {
                tl.next = p;
            }
            tl = p;
        }
        return hd;
    }
    
    final int batchFor(final long b) {
        long n;
        if (b == Long.MAX_VALUE || (n = this.sumCount()) <= 1L || n < b) {
            return 0;
        }
        final int sp = ForkJoinPool.getCommonPoolParallelism() << 2;
        return (b <= 0L || (n /= b) >= sp) ? sp : ((int)n);
    }
    
    public void forEach(final long parallelismThreshold, final BiAction<? super K, ? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (BiAction<? super K, ? super V>)action).invoke();
    }
    
    public <U> void forEach(final long parallelismThreshold, final BiFun<? super K, ? super V, ? extends U> transformer, final Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (BiFun<? super K, ? super V, ? extends U>)transformer, (Action<? super U>)action).invoke();
    }
    
    public <U> U search(final long parallelismThreshold, final BiFun<? super K, ? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchMappingsTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, (BiFun<? super Object, ? super Object, ?>)searchFunction, new AtomicReference<Object>()).invoke();
    }
    
    public <U> U reduce(final long parallelismThreshold, final BiFun<? super K, ? super V, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceMappingsTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (BiFun<? super Object, ? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public double reduceToDouble(final long parallelismThreshold, final ObjectByObjectToDouble<? super K, ? super V> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToDoubleTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectByObjectToDouble<? super Object, ? super Object>)transformer, basis, reducer).invoke();
    }
    
    public long reduceToLong(final long parallelismThreshold, final ObjectByObjectToLong<? super K, ? super V> transformer, final long basis, final LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToLongTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectByObjectToLong<? super Object, ? super Object>)transformer, basis, reducer).invoke();
    }
    
    public int reduceToInt(final long parallelismThreshold, final ObjectByObjectToInt<? super K, ? super V> transformer, final int basis, final IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToIntTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectByObjectToInt<? super Object, ? super Object>)transformer, basis, reducer).invoke();
    }
    
    public void forEachKey(final long parallelismThreshold, final Action<? super K> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Action<? super K>)action).invoke();
    }
    
    public <U> void forEachKey(final long parallelismThreshold, final Fun<? super K, ? extends U> transformer, final Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Fun<? super K, ? extends U>)transformer, (Action<? super U>)action).invoke();
    }
    
    public <U> U searchKeys(final long parallelismThreshold, final Fun<? super K, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchKeysTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, (Fun<? super Object, ?>)searchFunction, new AtomicReference<Object>()).invoke();
    }
    
    public K reduceKeys(final long parallelismThreshold, final BiFun<? super K, ? super K, ? extends K> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return (K)new ReduceKeysTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public <U> U reduceKeys(final long parallelismThreshold, final Fun<? super K, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceKeysTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (Fun<? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public double reduceKeysToDouble(final long parallelismThreshold, final ObjectToDouble<? super K> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToDoubleTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToDouble<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public long reduceKeysToLong(final long parallelismThreshold, final ObjectToLong<? super K> transformer, final long basis, final LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToLongTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToLong<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public int reduceKeysToInt(final long parallelismThreshold, final ObjectToInt<? super K> transformer, final int basis, final IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToIntTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToInt<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public void forEachValue(final long parallelismThreshold, final Action<? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Action<? super V>)action).invoke();
    }
    
    public <U> void forEachValue(final long parallelismThreshold, final Fun<? super V, ? extends U> transformer, final Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Fun<? super V, ? extends U>)transformer, (Action<? super U>)action).invoke();
    }
    
    public <U> U searchValues(final long parallelismThreshold, final Fun<? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchValuesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, (Fun<? super Object, ?>)searchFunction, new AtomicReference<Object>()).invoke();
    }
    
    public V reduceValues(final long parallelismThreshold, final BiFun<? super V, ? super V, ? extends V> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return (V)new ReduceValuesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public <U> U reduceValues(final long parallelismThreshold, final Fun<? super V, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceValuesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (Fun<? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public double reduceValuesToDouble(final long parallelismThreshold, final ObjectToDouble<? super V> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToDoubleTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToDouble<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public long reduceValuesToLong(final long parallelismThreshold, final ObjectToLong<? super V> transformer, final long basis, final LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToLongTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToLong<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public int reduceValuesToInt(final long parallelismThreshold, final ObjectToInt<? super V> transformer, final int basis, final IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToIntTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToInt<? super Object>)transformer, basis, reducer).invoke();
    }
    
    public void forEachEntry(final long parallelismThreshold, final Action<? super Map.Entry<K, V>> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Action<? super Map.Entry<K, V>>)action).invoke();
    }
    
    public <U> void forEachEntry(final long parallelismThreshold, final Fun<Map.Entry<K, V>, ? extends U> transformer, final Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<K, V>[])this.table, (Fun<Map.Entry<K, V>, ? extends U>)transformer, (Action<? super U>)action).invoke();
    }
    
    public <U> U searchEntries(final long parallelismThreshold, final Fun<Map.Entry<K, V>, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchEntriesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, (Fun<Map.Entry<Object, Object>, ?>)searchFunction, new AtomicReference<Object>()).invoke();
    }
    
    public Map.Entry<K, V> reduceEntries(final long parallelismThreshold, final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return new ReduceEntriesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (BiFun<Map.Entry<Object, Object>, Map.Entry<Object, Object>, ? extends Map.Entry<Object, Object>>)reducer).invoke();
    }
    
    public <U> U reduceEntries(final long parallelismThreshold, final Fun<Map.Entry<K, V>, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceEntriesTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (Fun<Map.Entry<Object, Object>, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer).invoke();
    }
    
    public double reduceEntriesToDouble(final long parallelismThreshold, final ObjectToDouble<Map.Entry<K, V>> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToDoubleTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToDouble<Map.Entry<Object, Object>>)transformer, basis, reducer).invoke();
    }
    
    public long reduceEntriesToLong(final long parallelismThreshold, final ObjectToLong<Map.Entry<K, V>> transformer, final long basis, final LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToLongTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToLong<Map.Entry<Object, Object>>)transformer, basis, reducer).invoke();
    }
    
    public int reduceEntriesToInt(final long parallelismThreshold, final ObjectToInt<Map.Entry<K, V>> transformer, final int basis, final IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToIntTask(null, this.batchFor(parallelismThreshold), 0, 0, (Node<Object, Object>[])this.table, null, (ObjectToInt<Map.Entry<Object, Object>>)transformer, basis, reducer).invoke();
    }
    
    final long sumCount() {
        final CounterCell[] as = this.counterCells;
        long sum = this.baseCount;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                final CounterCell a;
                if ((a = as[i]) != null) {
                    sum += a.value;
                }
            }
        }
        return sum;
    }
    
    private final void fullAddCount(final InternalThreadLocalMap threadLocals, final long x, IntegerHolder hc, boolean wasUncontended) {
        int h;
        if (hc == null) {
            hc = new IntegerHolder();
            final int s = ConcurrentHashMapV8.counterHashCodeGenerator.addAndGet(1640531527);
            final IntegerHolder integerHolder = hc;
            final int value = (s == 0) ? 1 : s;
            integerHolder.value = value;
            h = value;
            threadLocals.setCounterHashCode(hc);
        }
        else {
            h = hc.value;
        }
        boolean collide = false;
        while (true) {
            final CounterCell[] as;
            final int n;
            if ((as = this.counterCells) != null && (n = as.length) > 0) {
                final CounterCell a;
                if ((a = as[n - 1 & h]) == null) {
                    if (this.cellsBusy == 0) {
                        final CounterCell r = new CounterCell(x);
                        if (this.cellsBusy == 0 && ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.CELLSBUSY, 0, 1)) {
                            boolean created = false;
                            try {
                                final CounterCell[] rs;
                                final int m;
                                final int j;
                                if ((rs = this.counterCells) != null && (m = rs.length) > 0 && rs[j = (m - 1 & h)] == null) {
                                    rs[j] = r;
                                    created = true;
                                }
                            }
                            finally {
                                this.cellsBusy = 0;
                            }
                            if (created) {
                                break;
                            }
                            continue;
                        }
                    }
                    collide = false;
                }
                else if (!wasUncontended) {
                    wasUncontended = true;
                }
                else {
                    final long v;
                    if (ConcurrentHashMapV8.U.compareAndSwapLong(a, ConcurrentHashMapV8.CELLVALUE, v = a.value, v + x)) {
                        break;
                    }
                    if (this.counterCells != as || n >= ConcurrentHashMapV8.NCPU) {
                        collide = false;
                    }
                    else if (!collide) {
                        collide = true;
                    }
                    else if (this.cellsBusy == 0 && ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.CELLSBUSY, 0, 1)) {
                        try {
                            if (this.counterCells == as) {
                                final CounterCell[] rs2 = new CounterCell[n << 1];
                                for (int i = 0; i < n; ++i) {
                                    rs2[i] = as[i];
                                }
                                this.counterCells = rs2;
                            }
                        }
                        finally {
                            this.cellsBusy = 0;
                        }
                        collide = false;
                        continue;
                    }
                }
                h ^= h << 13;
                h ^= h >>> 17;
                h ^= h << 5;
            }
            else if (this.cellsBusy == 0 && this.counterCells == as && ConcurrentHashMapV8.U.compareAndSwapInt(this, ConcurrentHashMapV8.CELLSBUSY, 0, 1)) {
                boolean init = false;
                try {
                    if (this.counterCells == as) {
                        final CounterCell[] rs3 = new CounterCell[2];
                        rs3[h & 0x1] = new CounterCell(x);
                        this.counterCells = rs3;
                        init = true;
                    }
                }
                finally {
                    this.cellsBusy = 0;
                }
                if (init) {
                    break;
                }
                continue;
            }
            else {
                final long v;
                if (ConcurrentHashMapV8.U.compareAndSwapLong(this, ConcurrentHashMapV8.BASECOUNT, v = this.baseCount, v + x)) {
                    break;
                }
                continue;
            }
        }
        hc.value = h;
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Class<Unsafe> k = Unsafe.class;
                        for (final Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            final Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return k.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
    
    static {
        NCPU = Runtime.getRuntime().availableProcessors();
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE) };
        counterHashCodeGenerator = new AtomicInteger();
        try {
            U = getUnsafe();
            final Class<?> k = ConcurrentHashMapV8.class;
            SIZECTL = ConcurrentHashMapV8.U.objectFieldOffset(k.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = ConcurrentHashMapV8.U.objectFieldOffset(k.getDeclaredField("transferIndex"));
            TRANSFERORIGIN = ConcurrentHashMapV8.U.objectFieldOffset(k.getDeclaredField("transferOrigin"));
            BASECOUNT = ConcurrentHashMapV8.U.objectFieldOffset(k.getDeclaredField("baseCount"));
            CELLSBUSY = ConcurrentHashMapV8.U.objectFieldOffset(k.getDeclaredField("cellsBusy"));
            final Class<?> ck = CounterCell.class;
            CELLVALUE = ConcurrentHashMapV8.U.objectFieldOffset(ck.getDeclaredField("value"));
            final Class<?> ak = Node[].class;
            ABASE = ConcurrentHashMapV8.U.arrayBaseOffset(ak);
            final int scale = ConcurrentHashMapV8.U.arrayIndexScale(ak);
            if ((scale & scale - 1) != 0x0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }
    
    static class Node<K, V> implements Map.Entry<K, V>
    {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;
        
        Node(final int hash, final K key, final V val, final Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }
        
        @Override
        public final K getKey() {
            return this.key;
        }
        
        @Override
        public final V getValue() {
            return this.val;
        }
        
        @Override
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }
        
        @Override
        public final String toString() {
            return this.key + "=" + this.val;
        }
        
        @Override
        public final V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final boolean equals(final Object o) {
            final Map.Entry<?, ?> e;
            final Object k;
            final Object v;
            final Object u;
            return o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == (u = this.val) || v.equals(u));
        }
        
        Node<K, V> find(final int h, final Object k) {
            Node<K, V> e = this;
            if (k != null) {
                K ek;
                while (e.hash != h || ((ek = e.key) != k && (ek == null || !k.equals(ek)))) {
                    if ((e = e.next) == null) {
                        return null;
                    }
                }
                return e;
            }
            return null;
        }
    }
    
    static class Segment<K, V> extends ReentrantLock implements Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;
        
        Segment(final float lf) {
            this.loadFactor = lf;
        }
    }
    
    static final class ForwardingNode<K, V> extends Node<K, V>
    {
        final Node<K, V>[] nextTable;
        
        ForwardingNode(final Node<K, V>[] tab) {
            super(-1, null, null, null);
            this.nextTable = tab;
        }
        
        @Override
        Node<K, V> find(final int h, final Object k) {
            Node<K, V>[] tab = this.nextTable;
            int n;
            Node<K, V> e;
        Label_0005:
            while (k != null && tab != null && (n = tab.length) != 0 && (e = ConcurrentHashMapV8.tabAt(tab, n - 1 & h)) != null) {
                int eh;
                K ek;
                while ((eh = e.hash) != h || ((ek = e.key) != k && (ek == null || !k.equals(ek)))) {
                    if (eh < 0) {
                        if (e instanceof ForwardingNode) {
                            tab = ((ForwardingNode)e).nextTable;
                            continue Label_0005;
                        }
                        return e.find(h, k);
                    }
                    else {
                        if ((e = e.next) == null) {
                            return null;
                        }
                        continue;
                    }
                }
                return e;
            }
            return null;
        }
    }
    
    static final class ReservationNode<K, V> extends Node<K, V>
    {
        ReservationNode() {
            super(-3, null, null, null);
        }
        
        @Override
        Node<K, V> find(final int h, final Object k) {
            return null;
        }
    }
    
    static final class TreeNode<K, V> extends Node<K, V>
    {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;
        
        TreeNode(final int hash, final K key, final V val, final Node<K, V> next, final TreeNode<K, V> parent) {
            super(hash, key, val, next);
            this.parent = parent;
        }
        
        @Override
        Node<K, V> find(final int h, final Object k) {
            return this.findTreeNode(h, k, null);
        }
        
        final TreeNode<K, V> findTreeNode(final int h, final Object k, Class<?> kc) {
            if (k != null) {
                TreeNode<K, V> p = this;
                do {
                    final TreeNode<K, V> pl = p.left;
                    final TreeNode<K, V> pr = p.right;
                    final int ph;
                    if ((ph = p.hash) > h) {
                        p = pl;
                    }
                    else if (ph < h) {
                        p = pr;
                    }
                    else {
                        final K pk;
                        if ((pk = p.key) == k || (pk != null && k.equals(pk))) {
                            return p;
                        }
                        if (pl == null && pr == null) {
                            break;
                        }
                        final int dir;
                        if ((kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(k)) != null) && (dir = ConcurrentHashMapV8.compareComparables(kc, k, pk)) != 0) {
                            p = ((dir < 0) ? pl : pr);
                        }
                        else if (pl == null) {
                            p = pr;
                        }
                        else {
                            final TreeNode<K, V> q;
                            if (pr != null && (q = pr.findTreeNode(h, k, kc)) != null) {
                                return q;
                            }
                            p = pl;
                        }
                    }
                } while (p != null);
            }
            return null;
        }
    }
    
    static final class TreeBin<K, V> extends Node<K, V>
    {
        TreeNode<K, V> root;
        volatile TreeNode<K, V> first;
        volatile Thread waiter;
        volatile int lockState;
        static final int WRITER = 1;
        static final int WAITER = 2;
        static final int READER = 4;
        private static final Unsafe U;
        private static final long LOCKSTATE;
        
        TreeBin(final TreeNode<K, V> b) {
            super(-2, null, null, null);
            this.first = b;
            TreeNode<K, V> r = null;
            TreeNode<K, V> next;
            for (TreeNode<K, V> x = b; x != null; x = next) {
                next = (TreeNode<K, V>)(TreeNode)x.next;
                final TreeNode<K, V> treeNode = x;
                final TreeNode<K, V> treeNode2 = x;
                final TreeNode<K, V> treeNode3 = null;
                treeNode2.right = (TreeNode<K, V>)treeNode3;
                treeNode.left = (TreeNode<K, V>)treeNode3;
                if (r == null) {
                    x.parent = null;
                    x.red = false;
                    r = x;
                }
                else {
                    final Object key = x.key;
                    final int hash = x.hash;
                    Class<?> kc = null;
                    TreeNode<K, V> p = r;
                    int dir;
                    TreeNode<K, V> xp;
                    do {
                        final int ph;
                        if ((ph = p.hash) > hash) {
                            dir = -1;
                        }
                        else if (ph < hash) {
                            dir = 1;
                        }
                        else if (kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(key)) != null) {
                            dir = ConcurrentHashMapV8.compareComparables(kc, key, p.key);
                        }
                        else {
                            dir = 0;
                        }
                        xp = p;
                    } while ((p = ((dir <= 0) ? p.left : p.right)) != null);
                    x.parent = xp;
                    if (dir <= 0) {
                        xp.left = x;
                    }
                    else {
                        xp.right = x;
                    }
                    r = balanceInsertion(r, x);
                }
            }
            this.root = r;
        }
        
        private final void lockRoot() {
            if (!TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, 0, 1)) {
                this.contendedLock();
            }
        }
        
        private final void unlockRoot() {
            this.lockState = 0;
        }
        
        private final void contendedLock() {
            boolean waiting = false;
            while (true) {
                final int s;
                if (((s = this.lockState) & 0x1) == 0x0) {
                    if (TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, s, 1)) {
                        break;
                    }
                    continue;
                }
                else if ((s & 0x2) == 0x0) {
                    if (!TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, s, s | 0x2)) {
                        continue;
                    }
                    waiting = true;
                    this.waiter = Thread.currentThread();
                }
                else {
                    if (!waiting) {
                        continue;
                    }
                    LockSupport.park(this);
                }
            }
            if (waiting) {
                this.waiter = null;
            }
        }
        
        @Override
        final Node<K, V> find(final int h, final Object k) {
            if (k != null) {
                for (Node<K, V> e = this.first; e != null; e = e.next) {
                    final int s;
                    if (((s = this.lockState) & 0x3) != 0x0) {
                        final K ek;
                        if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek)))) {
                            return e;
                        }
                    }
                    else if (TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, s, s + 4)) {
                        TreeNode<K, V> p;
                        try {
                            final TreeNode<K, V> r;
                            p = (((r = this.root) == null) ? null : r.findTreeNode(h, k, null));
                        }
                        finally {
                            int ls;
                            while (!TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, ls = this.lockState, ls - 4)) {}
                            final Thread w;
                            if (ls == 6 && (w = this.waiter) != null) {
                                LockSupport.unpark(w);
                            }
                        }
                        return p;
                    }
                }
            }
            return null;
        }
        
        final TreeNode<K, V> putTreeVal(final int h, final K k, final V v) {
            Class<?> kc = null;
            TreeNode<K, V> p = this.root;
            while (true) {
                while (p != null) {
                    final int ph;
                    int dir;
                    if ((ph = p.hash) > h) {
                        dir = -1;
                    }
                    else if (ph < h) {
                        dir = 1;
                    }
                    else {
                        final K pk;
                        if ((pk = p.key) == k || (pk != null && k.equals(pk))) {
                            return p;
                        }
                        if ((kc == null && (kc = ConcurrentHashMapV8.comparableClassFor(k)) == null) || (dir = ConcurrentHashMapV8.compareComparables(kc, k, pk)) == 0) {
                            if (p.left == null) {
                                dir = 1;
                            }
                            else {
                                final TreeNode<K, V> pr;
                                final TreeNode<K, V> q;
                                if ((pr = p.right) != null && (q = pr.findTreeNode(h, k, kc)) != null) {
                                    return q;
                                }
                                dir = -1;
                            }
                        }
                    }
                    final TreeNode<K, V> xp = p;
                    if ((p = ((dir < 0) ? p.left : p.right)) == null) {
                        final TreeNode<K, V> f = this.first;
                        final TreeNode<K, V> x = this.first = new TreeNode<K, V>(h, k, v, f, xp);
                        if (f != null) {
                            f.prev = x;
                        }
                        if (dir < 0) {
                            xp.left = x;
                        }
                        else {
                            xp.right = x;
                        }
                        if (!xp.red) {
                            x.red = true;
                        }
                        else {
                            this.lockRoot();
                            try {
                                this.root = balanceInsertion(this.root, x);
                            }
                            finally {
                                this.unlockRoot();
                            }
                        }
                        assert checkInvariants(this.root);
                        return null;
                    }
                }
                final TreeNode<K, V> treeNode = new TreeNode<K, V>(h, k, v, null, null);
                this.root = treeNode;
                this.first = treeNode;
                continue;
            }
        }
        
        final boolean removeTreeNode(final TreeNode<K, V> p) {
            final TreeNode<K, V> next = (TreeNode<K, V>)(TreeNode)p.next;
            final TreeNode<K, V> pred = p.prev;
            if (pred == null) {
                this.first = next;
            }
            else {
                pred.next = next;
            }
            if (next != null) {
                next.prev = pred;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> r;
            final TreeNode<K, V> rl;
            if ((r = this.root) == null || r.right == null || (rl = r.left) == null || rl.left == null) {
                return true;
            }
            this.lockRoot();
            try {
                final TreeNode<K, V> pl = p.left;
                final TreeNode<K, V> pr = p.right;
                TreeNode<K, V> replacement;
                if (pl != null && pr != null) {
                    TreeNode<K, V> s;
                    TreeNode<K, V> sl;
                    for (s = pr; (sl = s.left) != null; s = sl) {}
                    final boolean c = s.red;
                    s.red = p.red;
                    p.red = c;
                    final TreeNode<K, V> sr = s.right;
                    final TreeNode<K, V> pp = p.parent;
                    if (s == pr) {
                        p.parent = s;
                        s.right = p;
                    }
                    else {
                        final TreeNode<K, V> sp = s.parent;
                        if ((p.parent = sp) != null) {
                            if (s == sp.left) {
                                sp.left = p;
                            }
                            else {
                                sp.right = p;
                            }
                        }
                        s.right = pr;
                        pr.parent = s;
                    }
                    p.left = null;
                    s.left = pl;
                    pl.parent = s;
                    final TreeNode<K, V> right = sr;
                    p.right = right;
                    if (right != null) {
                        sr.parent = p;
                    }
                    if ((s.parent = pp) == null) {
                        r = s;
                    }
                    else if (p == pp.left) {
                        pp.left = s;
                    }
                    else {
                        pp.right = s;
                    }
                    if (sr != null) {
                        replacement = sr;
                    }
                    else {
                        replacement = p;
                    }
                }
                else if (pl != null) {
                    replacement = pl;
                }
                else if (pr != null) {
                    replacement = pr;
                }
                else {
                    replacement = p;
                }
                if (replacement != p) {
                    final TreeNode<K, V> treeNode = replacement;
                    final TreeNode<K, V> parent = p.parent;
                    treeNode.parent = parent;
                    final TreeNode<K, V> pp2 = parent;
                    if (pp2 == null) {
                        r = replacement;
                    }
                    else if (p == pp2.left) {
                        pp2.left = replacement;
                    }
                    else {
                        pp2.right = replacement;
                    }
                    final TreeNode<K, V> left = null;
                    p.parent = (TreeNode<K, V>)left;
                    p.right = (TreeNode<K, V>)left;
                    p.left = (TreeNode<K, V>)left;
                }
                this.root = (p.red ? r : balanceDeletion(r, replacement));
                TreeNode<K, V> pp2;
                if (p == replacement && (pp2 = p.parent) != null) {
                    if (p == pp2.left) {
                        pp2.left = null;
                    }
                    else if (p == pp2.right) {
                        pp2.right = null;
                    }
                    p.parent = null;
                }
            }
            finally {
                this.unlockRoot();
            }
            assert checkInvariants(this.root);
            return false;
        }
        
        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root, final TreeNode<K, V> p) {
            final TreeNode<K, V> r;
            if (p != null && (r = p.right) != null) {
                final TreeNode<K, V> left = r.left;
                p.right = left;
                final TreeNode<K, V> rl;
                if ((rl = left) != null) {
                    rl.parent = p;
                }
                final TreeNode<K, V> treeNode = r;
                final TreeNode<K, V> parent = p.parent;
                treeNode.parent = parent;
                final TreeNode<K, V> pp;
                if ((pp = parent) == null) {
                    (root = r).red = false;
                }
                else if (pp.left == p) {
                    pp.left = r;
                }
                else {
                    pp.right = r;
                }
                r.left = p;
                p.parent = r;
            }
            return root;
        }
        
        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root, final TreeNode<K, V> p) {
            final TreeNode<K, V> l;
            if (p != null && (l = p.left) != null) {
                final TreeNode<K, V> right = l.right;
                p.left = right;
                final TreeNode<K, V> lr;
                if ((lr = right) != null) {
                    lr.parent = p;
                }
                final TreeNode<K, V> treeNode = l;
                final TreeNode<K, V> parent = p.parent;
                treeNode.parent = parent;
                final TreeNode<K, V> pp;
                if ((pp = parent) == null) {
                    (root = l).red = false;
                }
                else if (pp.right == p) {
                    pp.right = l;
                }
                else {
                    pp.left = l;
                }
                l.right = p;
                p.parent = l;
            }
            return root;
        }
        
        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x) {
            x.red = true;
            TreeNode<K, V> xp;
            while ((xp = x.parent) != null) {
                TreeNode<K, V> xpp;
                if (!xp.red || (xpp = xp.parent) == null) {
                    return root;
                }
                final TreeNode<K, V> xppl;
                if (xp == (xppl = xpp.left)) {
                    final TreeNode<K, V> xppr;
                    if ((xppr = xpp.right) != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.right) {
                            root = (TreeNode<K, V>)rotateLeft((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)(x = xp));
                            xpp = (((xp = x.parent) == null) ? null : xp.parent);
                        }
                        if (xp == null) {
                            continue;
                        }
                        xp.red = false;
                        if (xpp == null) {
                            continue;
                        }
                        xpp.red = true;
                        root = (TreeNode<K, V>)rotateRight((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xpp);
                    }
                }
                else if (xppl != null && xppl.red) {
                    xppl.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                }
                else {
                    if (x == xp.left) {
                        root = (TreeNode<K, V>)rotateRight((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)(x = xp));
                        xpp = (((xp = x.parent) == null) ? null : xp.parent);
                    }
                    if (xp == null) {
                        continue;
                    }
                    xp.red = false;
                    if (xpp == null) {
                        continue;
                    }
                    xpp.red = true;
                    root = (TreeNode<K, V>)rotateLeft((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xpp);
                }
            }
            x.red = false;
            return x;
        }
        
        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x) {
            while (x != null && x != root) {
                TreeNode<K, V> xp;
                if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                }
                if (x.red) {
                    x.red = false;
                    return root;
                }
                TreeNode<K, V> xpl;
                if ((xpl = xp.left) == x) {
                    TreeNode<K, V> xpr;
                    if ((xpr = xp.right) != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = (TreeNode<K, V>)rotateLeft((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xp);
                        xpr = (((xp = x.parent) == null) ? null : xp.right);
                    }
                    if (xpr == null) {
                        x = xp;
                    }
                    else {
                        final TreeNode<K, V> sl = xpr.left;
                        TreeNode<K, V> sr = xpr.right;
                        if ((sr == null || !sr.red) && (sl == null || !sl.red)) {
                            xpr.red = true;
                            x = xp;
                        }
                        else {
                            if (sr == null || !sr.red) {
                                if (sl != null) {
                                    sl.red = false;
                                }
                                xpr.red = true;
                                root = (TreeNode<K, V>)rotateRight((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xpr);
                                xpr = (((xp = x.parent) == null) ? null : xp.right);
                            }
                            if (xpr != null) {
                                xpr.red = (xp != null && xp.red);
                                if ((sr = xpr.right) != null) {
                                    sr.red = false;
                                }
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = (TreeNode<K, V>)rotateLeft((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xp);
                            }
                            x = root;
                        }
                    }
                }
                else {
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = (TreeNode<K, V>)rotateRight((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xp);
                        xpl = (((xp = x.parent) == null) ? null : xp.left);
                    }
                    if (xpl == null) {
                        x = xp;
                    }
                    else {
                        TreeNode<K, V> sl = xpl.left;
                        final TreeNode<K, V> sr = xpl.right;
                        if ((sl == null || !sl.red) && (sr == null || !sr.red)) {
                            xpl.red = true;
                            x = xp;
                        }
                        else {
                            if (sl == null || !sl.red) {
                                if (sr != null) {
                                    sr.red = false;
                                }
                                xpl.red = true;
                                root = (TreeNode<K, V>)rotateLeft((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xpl);
                                xpl = (((xp = x.parent) == null) ? null : xp.left);
                            }
                            if (xpl != null) {
                                xpl.red = (xp != null && xp.red);
                                if ((sl = xpl.left) != null) {
                                    sl.red = false;
                                }
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = (TreeNode<K, V>)rotateRight((TreeNode<Object, Object>)root, (TreeNode<Object, Object>)xp);
                            }
                            x = root;
                        }
                    }
                }
            }
            return root;
        }
        
        static <K, V> boolean checkInvariants(final TreeNode<K, V> t) {
            final TreeNode<K, V> tp = t.parent;
            final TreeNode<K, V> tl = t.left;
            final TreeNode<K, V> tr = t.right;
            final TreeNode<K, V> tb = t.prev;
            final TreeNode<K, V> tn = (TreeNode<K, V>)(TreeNode)t.next;
            return (tb == null || tb.next == t) && (tn == null || tn.prev == t) && (tp == null || t == tp.left || t == tp.right) && (tl == null || (tl.parent == t && tl.hash <= t.hash)) && (tr == null || (tr.parent == t && tr.hash >= t.hash)) && (!t.red || tl == null || !tl.red || tr == null || !tr.red) && (tl == null || checkInvariants((TreeNode<Object, Object>)tl)) && (tr == null || checkInvariants((TreeNode<Object, Object>)tr));
        }
        
        static {
            try {
                U = getUnsafe();
                final Class<?> k = TreeBin.class;
                LOCKSTATE = TreeBin.U.objectFieldOffset(k.getDeclaredField("lockState"));
            }
            catch (Exception e) {
                throw new Error(e);
            }
        }
    }
    
    static class Traverser<K, V>
    {
        Node<K, V>[] tab;
        Node<K, V> next;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        
        Traverser(final Node<K, V>[] tab, final int size, final int index, final int limit) {
            this.tab = tab;
            this.baseSize = size;
            this.index = index;
            this.baseIndex = index;
            this.baseLimit = limit;
            this.next = null;
        }
        
        final Node<K, V> advance() {
            Node<K, V> e;
            if ((e = this.next) != null) {
                e = e.next;
            }
            while (e == null) {
                final Node<K, V>[] t;
                final int n;
                final int i;
                if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0) {
                    return this.next = null;
                }
                if ((e = ConcurrentHashMapV8.tabAt(t, this.index)) != null && e.hash < 0) {
                    if (e instanceof ForwardingNode) {
                        this.tab = (Node<K, V>[])((ForwardingNode)e).nextTable;
                        e = null;
                        continue;
                    }
                    if (e instanceof TreeBin) {
                        e = (Node<K, V>)((TreeBin)e).first;
                    }
                    else {
                        e = null;
                    }
                }
                if ((this.index += this.baseSize) < n) {
                    continue;
                }
                this.index = ++this.baseIndex;
            }
            return this.next = e;
        }
    }
    
    static class BaseIterator<K, V> extends Traverser<K, V>
    {
        final ConcurrentHashMapV8<K, V> map;
        Node<K, V> lastReturned;
        
        BaseIterator(final Node<K, V>[] tab, final int size, final int index, final int limit, final ConcurrentHashMapV8<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            this.advance();
        }
        
        public final boolean hasNext() {
            return this.next != null;
        }
        
        public final boolean hasMoreElements() {
            return this.next != null;
        }
        
        public final void remove() {
            final Node<K, V> p;
            if ((p = this.lastReturned) == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(p.key, null, null);
        }
    }
    
    static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K>
    {
        KeyIterator(final Node<K, V>[] tab, final int index, final int size, final int limit, final ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }
        
        @Override
        public final K next() {
            final Node<K, V> p;
            if ((p = this.next) == null) {
                throw new NoSuchElementException();
            }
            final K k = p.key;
            this.lastReturned = p;
            this.advance();
            return k;
        }
        
        @Override
        public final K nextElement() {
            return this.next();
        }
    }
    
    static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V>
    {
        ValueIterator(final Node<K, V>[] tab, final int index, final int size, final int limit, final ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }
        
        @Override
        public final V next() {
            final Node<K, V> p;
            if ((p = this.next) == null) {
                throw new NoSuchElementException();
            }
            final V v = p.val;
            this.lastReturned = p;
            this.advance();
            return v;
        }
        
        @Override
        public final V nextElement() {
            return this.next();
        }
    }
    
    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>>
    {
        EntryIterator(final Node<K, V>[] tab, final int index, final int size, final int limit, final ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }
        
        @Override
        public final Map.Entry<K, V> next() {
            final Node<K, V> p;
            if ((p = this.next) == null) {
                throw new NoSuchElementException();
            }
            final K k = p.key;
            final V v = p.val;
            this.lastReturned = p;
            this.advance();
            return new MapEntry<K, V>(k, v, this.map);
        }
    }
    
    static final class MapEntry<K, V> implements Map.Entry<K, V>
    {
        final K key;
        V val;
        final ConcurrentHashMapV8<K, V> map;
        
        MapEntry(final K key, final V val, final ConcurrentHashMapV8<K, V> map) {
            this.key = key;
            this.val = val;
            this.map = map;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.val;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.val;
        }
        
        @Override
        public boolean equals(final Object o) {
            final Map.Entry<?, ?> e;
            final Object k;
            final Object v;
            return o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == this.val || v.equals(this.val));
        }
        
        @Override
        public V setValue(final V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            final V v = this.val;
            this.val = value;
            this.map.put(this.key, value);
            return v;
        }
    }
    
    static final class KeySpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<K>
    {
        long est;
        
        KeySpliterator(final Node<K, V>[] tab, final int size, final int index, final int limit, final long est) {
            super(tab, size, index, limit);
            this.est = est;
        }
        
        @Override
        public ConcurrentHashMapSpliterator<K> trySplit() {
            final int i = this.baseIndex;
            final int f;
            final int h;
            ConcurrentHashMapSpliterator<K> concurrentHashMapSpliterator;
            if ((h = i + (f = this.baseLimit) >>> 1) <= i) {
                concurrentHashMapSpliterator = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int n;
                concurrentHashMapSpliterator = new KeySpliterator<K, Object>((Node<Object, Object>[])tab, baseSize, n, f, this.est >>>= 1);
                tab = this.tab;
                baseSize = this.baseSize;
                n = h;
                this.baseLimit = n;
            }
            return concurrentHashMapSpliterator;
        }
        
        @Override
        public void forEachRemaining(final Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V> p;
            while ((p = this.advance()) != null) {
                action.apply((Object)p.key);
            }
        }
        
        @Override
        public boolean tryAdvance(final Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V> p;
            if ((p = this.advance()) == null) {
                return false;
            }
            action.apply((Object)p.key);
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
    }
    
    static final class ValueSpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<V>
    {
        long est;
        
        ValueSpliterator(final Node<K, V>[] tab, final int size, final int index, final int limit, final long est) {
            super(tab, size, index, limit);
            this.est = est;
        }
        
        @Override
        public ConcurrentHashMapSpliterator<V> trySplit() {
            final int i = this.baseIndex;
            final int f;
            final int h;
            ConcurrentHashMapSpliterator<V> concurrentHashMapSpliterator;
            if ((h = i + (f = this.baseLimit) >>> 1) <= i) {
                concurrentHashMapSpliterator = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int n;
                concurrentHashMapSpliterator = new ValueSpliterator<Object, V>((Node<Object, Object>[])tab, baseSize, n, f, this.est >>>= 1);
                tab = this.tab;
                baseSize = this.baseSize;
                n = h;
                this.baseLimit = n;
            }
            return concurrentHashMapSpliterator;
        }
        
        @Override
        public void forEachRemaining(final Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V> p;
            while ((p = this.advance()) != null) {
                action.apply((Object)p.val);
            }
        }
        
        @Override
        public boolean tryAdvance(final Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V> p;
            if ((p = this.advance()) == null) {
                return false;
            }
            action.apply((Object)p.val);
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
    }
    
    static final class EntrySpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<Map.Entry<K, V>>
    {
        final ConcurrentHashMapV8<K, V> map;
        long est;
        
        EntrySpliterator(final Node<K, V>[] tab, final int size, final int index, final int limit, final long est, final ConcurrentHashMapV8<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            this.est = est;
        }
        
        @Override
        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> trySplit() {
            final int i = this.baseIndex;
            final int f;
            final int h;
            Object o;
            if ((h = i + (f = this.baseLimit) >>> 1) <= i) {
                o = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int n;
                o = new EntrySpliterator<Object, Object>((Node<Object, Object>[])tab, baseSize, n, f, this.est >>>= 1, (ConcurrentHashMapV8<Object, Object>)this.map);
                tab = this.tab;
                baseSize = this.baseSize;
                n = h;
                this.baseLimit = n;
            }
            return (ConcurrentHashMapSpliterator<Map.Entry<K, V>>)o;
        }
        
        @Override
        public void forEachRemaining(final Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V> p;
            while ((p = this.advance()) != null) {
                action.apply((Object)new MapEntry((K)p.key, (V)p.val, (ConcurrentHashMapV8<K, V>)this.map));
            }
        }
        
        @Override
        public boolean tryAdvance(final Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V> p;
            if ((p = this.advance()) == null) {
                return false;
            }
            action.apply((Object)new MapEntry((K)p.key, (V)p.val, (ConcurrentHashMapV8<K, V>)this.map));
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
    }
    
    abstract static class CollectionView<K, V, E> implements Collection<E>, Serializable
    {
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMapV8<K, V> map;
        private static final String oomeMsg = "Required array size too large";
        
        CollectionView(final ConcurrentHashMapV8<K, V> map) {
            this.map = map;
        }
        
        public ConcurrentHashMapV8<K, V> getMap() {
            return this.map;
        }
        
        @Override
        public final void clear() {
            this.map.clear();
        }
        
        @Override
        public final int size() {
            return this.map.size();
        }
        
        @Override
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public abstract Iterator<E> iterator();
        
        @Override
        public abstract boolean contains(final Object p0);
        
        @Override
        public abstract boolean remove(final Object p0);
        
        @Override
        public final Object[] toArray() {
            final long sz = this.map.mappingCount();
            if (sz > 2147483639L) {
                throw new OutOfMemoryError("Required array size too large");
            }
            int n = (int)sz;
            Object[] r = new Object[n];
            int i = 0;
            for (final E e : this) {
                if (i == n) {
                    if (n >= 2147483639) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    if (n >= 1073741819) {
                        n = 2147483639;
                    }
                    else {
                        n += (n >>> 1) + 1;
                    }
                    r = Arrays.copyOf(r, n);
                }
                r[i++] = e;
            }
            return (i == n) ? r : Arrays.copyOf(r, i);
        }
        
        @Override
        public final <T> T[] toArray(final T[] a) {
            final long sz = this.map.mappingCount();
            if (sz > 2147483639L) {
                throw new OutOfMemoryError("Required array size too large");
            }
            final int m = (int)sz;
            T[] r = (T[])((a.length >= m) ? a : ((Object[])Array.newInstance(a.getClass().getComponentType(), m)));
            int n = r.length;
            int i = 0;
            for (final E e : this) {
                if (i == n) {
                    if (n >= 2147483639) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    if (n >= 1073741819) {
                        n = 2147483639;
                    }
                    else {
                        n += (n >>> 1) + 1;
                    }
                    r = Arrays.copyOf(r, n);
                }
                r[i++] = (T)e;
            }
            if (a == r && i < n) {
                r[i] = null;
                return r;
            }
            return (i == n) ? r : Arrays.copyOf(r, i);
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            final Iterator<E> it = this.iterator();
            if (it.hasNext()) {
                while (true) {
                    final Object e = it.next();
                    sb.append((e == this) ? "(this Collection)" : e);
                    if (!it.hasNext()) {
                        break;
                    }
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
        }
        
        @Override
        public final boolean containsAll(final Collection<?> c) {
            if (c != this) {
                for (final Object e : c) {
                    if (e == null || !this.contains(e)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        public final boolean removeAll(final Collection<?> c) {
            boolean modified = false;
            final Iterator<E> it = this.iterator();
            while (it.hasNext()) {
                if (c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        @Override
        public final boolean retainAll(final Collection<?> c) {
            boolean modified = false;
            final Iterator<E> it = this.iterator();
            while (it.hasNext()) {
                if (!c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
    }
    
    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable
    {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;
        
        KeySetView(final ConcurrentHashMapV8<K, V> map, final V value) {
            super(map);
            this.value = value;
        }
        
        public V getMappedValue() {
            return this.value;
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.map.containsKey(o);
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.map.remove(o) != null;
        }
        
        @Override
        public Iterator<K> iterator() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new KeyIterator<K, Object>(t, f, 0, f, m);
        }
        
        @Override
        public boolean add(final K e) {
            final V v;
            if ((v = this.value) == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal((K)e, (V)v, true) == null;
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
            boolean added = false;
            final V v;
            if ((v = this.value) == null) {
                throw new UnsupportedOperationException();
            }
            for (final K e : c) {
                if (this.map.putVal((K)e, (V)v, true) == null) {
                    added = true;
                }
            }
            return added;
        }
        
        @Override
        public int hashCode() {
            int h = 0;
            for (final K e : this) {
                h += e.hashCode();
            }
            return h;
        }
        
        @Override
        public boolean equals(final Object o) {
            final Set<?> c;
            return o instanceof Set && ((c = (Set<?>)o) == this || (this.containsAll(c) && c.containsAll(this)));
        }
        
        public ConcurrentHashMapSpliterator<K> spliterator166() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final long n = m.sumCount();
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new KeySpliterator<K, Object>(t, f, 0, f, (n < 0L) ? 0L : n);
        }
        
        public void forEach(final Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] t;
            if ((t = (Node<K, V>[])this.map.table) != null) {
                final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
                Node<K, V> p;
                while ((p = it.advance()) != null) {
                    action.apply((Object)p.key);
                }
            }
        }
    }
    
    static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        
        ValuesView(final ConcurrentHashMapV8<K, V> map) {
            super(map);
        }
        
        @Override
        public final boolean contains(final Object o) {
            return this.map.containsValue(o);
        }
        
        @Override
        public final boolean remove(final Object o) {
            if (o != null) {
                final Iterator<V> it = this.iterator();
                while (it.hasNext()) {
                    if (o.equals(it.next())) {
                        it.remove();
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public final Iterator<V> iterator() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new ValueIterator<Object, V>(t, f, 0, f, m);
        }
        
        @Override
        public final boolean add(final V e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final boolean addAll(final Collection<? extends V> c) {
            throw new UnsupportedOperationException();
        }
        
        public ConcurrentHashMapSpliterator<V> spliterator166() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final long n = m.sumCount();
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new ValueSpliterator<Object, V>(t, f, 0, f, (n < 0L) ? 0L : n);
        }
        
        public void forEach(final Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] t;
            if ((t = (Node<K, V>[])this.map.table) != null) {
                final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
                Node<K, V> p;
                while ((p = it.advance()) != null) {
                    action.apply((Object)p.val);
                }
            }
        }
    }
    
    static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        
        EntrySetView(final ConcurrentHashMapV8<K, V> map) {
            super(map);
        }
        
        @Override
        public boolean contains(final Object o) {
            final Map.Entry<?, ?> e;
            final Object k;
            final Object r;
            final Object v;
            return o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (r = this.map.get(k)) != null && (v = e.getValue()) != null && (v == r || v.equals(r));
        }
        
        @Override
        public boolean remove(final Object o) {
            final Map.Entry<?, ?> e;
            final Object k;
            final Object v;
            return o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && this.map.remove(k, v);
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new EntryIterator<K, V>(t, f, 0, f, m);
        }
        
        @Override
        public boolean add(final Map.Entry<K, V> e) {
            return this.map.putVal((K)e.getKey(), (V)e.getValue(), false) == null;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> c) {
            boolean added = false;
            for (final Map.Entry<K, V> e : c) {
                if (this.add(e)) {
                    added = true;
                }
            }
            return added;
        }
        
        @Override
        public final int hashCode() {
            int h = 0;
            final Node<K, V>[] t;
            if ((t = (Node<K, V>[])this.map.table) != null) {
                final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
                Node<K, V> p;
                while ((p = it.advance()) != null) {
                    h += p.hashCode();
                }
            }
            return h;
        }
        
        @Override
        public final boolean equals(final Object o) {
            final Set<?> c;
            return o instanceof Set && ((c = (Set<?>)o) == this || (this.containsAll(c) && c.containsAll(this)));
        }
        
        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> spliterator166() {
            final ConcurrentHashMapV8<K, V> m = (ConcurrentHashMapV8<K, V>)this.map;
            final long n = m.sumCount();
            final Node<K, V>[] t;
            final int f = ((t = m.table) == null) ? 0 : t.length;
            return new EntrySpliterator<K, V>(t, f, 0, f, (n < 0L) ? 0L : n, m);
        }
        
        public void forEach(final Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] t;
            if ((t = (Node<K, V>[])this.map.table) != null) {
                final Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
                Node<K, V> p;
                while ((p = it.advance()) != null) {
                    action.apply((Object)new MapEntry((K)p.key, (V)p.val, (ConcurrentHashMapV8<K, V>)this.map));
                }
            }
        }
    }
    
    abstract static class BulkTask<K, V, R> extends CountedCompleter<R>
    {
        Node<K, V>[] tab;
        Node<K, V> next;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;
        
        BulkTask(final BulkTask<K, V, ?> par, final int b, final int i, final int f, final Node<K, V>[] t) {
            super(par);
            this.batch = b;
            this.baseIndex = i;
            this.index = i;
            this.tab = t;
            if (t == null) {
                final int n = 0;
                this.baseLimit = n;
                this.baseSize = n;
            }
            else if (par == null) {
                final int length = t.length;
                this.baseLimit = length;
                this.baseSize = length;
            }
            else {
                this.baseLimit = f;
                this.baseSize = par.baseSize;
            }
        }
        
        final Node<K, V> advance() {
            Node<K, V> e;
            if ((e = this.next) != null) {
                e = e.next;
            }
            while (e == null) {
                final Node<K, V>[] t;
                final int n;
                final int i;
                if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0) {
                    return this.next = null;
                }
                if ((e = ConcurrentHashMapV8.tabAt(t, this.index)) != null && e.hash < 0) {
                    if (e instanceof ForwardingNode) {
                        this.tab = (Node<K, V>[])((ForwardingNode)e).nextTable;
                        e = null;
                        continue;
                    }
                    if (e instanceof TreeBin) {
                        e = (Node<K, V>)((TreeBin)e).first;
                    }
                    else {
                        e = null;
                    }
                }
                if ((this.index += this.baseSize) < n) {
                    continue;
                }
                this.index = ++this.baseIndex;
            }
            return this.next = e;
        }
    }
    
    static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void>
    {
        final Action<? super K> action;
        
        ForEachKeyTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Action<? super K> action) {
            super(p, b, i, f, t);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Action<? super K> action;
            if ((action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachKeyTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    action.apply((Object)p.key);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void>
    {
        final Action<? super V> action;
        
        ForEachValueTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Action<? super V> action) {
            super(p, b, i, f, t);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Action<? super V> action;
            if ((action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachValueTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    action.apply((Object)p.val);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void>
    {
        final Action<? super Map.Entry<K, V>> action;
        
        ForEachEntryTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Action<? super Map.Entry<K, V>> action) {
            super(p, b, i, f, t);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Action<? super Map.Entry<K, V>> action;
            if ((action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachEntryTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Action<? super Map.Entry<Object, Object>>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    action.apply(p);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void>
    {
        final BiAction<? super K, ? super V> action;
        
        ForEachMappingTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final BiAction<? super K, ? super V> action) {
            super(p, b, i, f, t);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final BiAction<? super K, ? super V> action;
            if ((action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachMappingTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (BiAction<? super Object, ? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    action.apply((Object)p.key, (Object)p.val);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Fun<? super K, ? extends U> transformer;
        final Action<? super U> action;
        
        ForEachTransformedKeyTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<? super K, ? extends U> transformer, final Action<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Fun<? super K, ? extends U> transformer;
            final Action<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachTransformedKeyTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<? super Object, ?>)transformer, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.key)) != null) {
                        action.apply((Object)u);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Fun<? super V, ? extends U> transformer;
        final Action<? super U> action;
        
        ForEachTransformedValueTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<? super V, ? extends U> transformer, final Action<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Fun<? super V, ? extends U> transformer;
            final Action<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachTransformedValueTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<? super Object, ?>)transformer, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.val)) != null) {
                        action.apply((Object)u);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final Action<? super U> action;
        
        ForEachTransformedEntryTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<Map.Entry<K, V>, ? extends U> transformer, final Action<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Fun<Map.Entry<K, V>, ? extends U> transformer;
            final Action<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachTransformedEntryTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<Map.Entry<Object, Object>, ?>)transformer, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply(p)) != null) {
                        action.apply((Object)u);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        final Action<? super U> action;
        
        ForEachTransformedMappingTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final BiFun<? super K, ? super V, ? extends U> transformer, final Action<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final BiFun<? super K, ? super V, ? extends U> transformer;
            final Action<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new ForEachTransformedMappingTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (BiFun<? super Object, ? super Object, ?>)transformer, (Action<? super Object>)action).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.key, (Object)p.val)) != null) {
                        action.apply((Object)u);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<? super K, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchKeysTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<? super K, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Fun<? super K, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new SearchKeysTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> p;
                    if ((p = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U u;
                    if ((u = (U)searchFunction.apply((Object)p.key)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, u)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchValuesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<? super V, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Fun<? super V, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new SearchValuesTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> p;
                    if ((p = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U u;
                    if ((u = (U)searchFunction.apply((Object)p.val)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, u)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchEntriesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final Fun<Map.Entry<K, V>, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Fun<Map.Entry<K, V>, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new SearchEntriesTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (Fun<Map.Entry<Object, Object>, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> p;
                    if ((p = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U u;
                    if ((u = (U)searchFunction.apply(p)) != null) {
                        if (result.compareAndSet(null, u)) {
                            this.quietlyCompleteRoot();
                        }
                    }
                }
            }
        }
    }
    
    static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U>
    {
        final BiFun<? super K, ? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchMappingsTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final BiFun<? super K, ? super V, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final BiFun<? super K, ? super V, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    new SearchMappingsTask((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (BiFun<? super Object, ? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> p;
                    if ((p = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U u;
                    if ((u = (U)searchFunction.apply((Object)p.key, (Object)p.val)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, u)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K>
    {
        final BiFun<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;
        
        ReduceKeysTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final ReduceKeysTask<K, V> nextRight, final BiFun<? super K, ? super K, ? extends K> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final K getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFun<? super K, ? super K, ? extends K> reducer;
            if ((reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new ReduceKeysTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (ReduceKeysTask<Object, Object>)this.rights, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                K r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final K u = p.key;
                    r = ((r == null) ? u : ((u == null) ? r : reducer.apply((Object)r, (Object)u)));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final ReduceKeysTask<K, V> t = (ReduceKeysTask)c;
                    ReduceKeysTask<K, V> nextRight;
                    for (ReduceKeysTask<K, V> s = t.rights; s != null; s = nextRight) {
                        final K sr;
                        if ((sr = s.result) != null) {
                            final K tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final ReduceKeysTask<K, V> reduceKeysTask = t;
                        nextRight = s.nextRight;
                        reduceKeysTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V>
    {
        final BiFun<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;
        ReduceValuesTask<K, V> nextRight;
        
        ReduceValuesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final ReduceValuesTask<K, V> nextRight, final BiFun<? super V, ? super V, ? extends V> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final V getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFun<? super V, ? super V, ? extends V> reducer;
            if ((reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new ReduceValuesTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (ReduceValuesTask<Object, Object>)this.rights, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                V r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final V v = p.val;
                    r = ((r == null) ? v : reducer.apply((Object)r, (Object)v));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final ReduceValuesTask<K, V> t = (ReduceValuesTask)c;
                    ReduceValuesTask<K, V> nextRight;
                    for (ReduceValuesTask<K, V> s = t.rights; s != null; s = nextRight) {
                        final V sr;
                        if ((sr = s.result) != null) {
                            final V tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final ReduceValuesTask<K, V> reduceValuesTask = t;
                        nextRight = s.nextRight;
                        reduceValuesTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>>
    {
        final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;
        ReduceEntriesTask<K, V> nextRight;
        
        ReduceEntriesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final ReduceEntriesTask<K, V> nextRight, final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
            if ((reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new ReduceEntriesTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (ReduceEntriesTask<Object, Object>)this.rights, (BiFun<Map.Entry<Object, Object>, Map.Entry<Object, Object>, ? extends Map.Entry<Object, Object>>)reducer)).fork();
                }
                Map.Entry<K, V> r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = ((r == null) ? p : ((Map.Entry)reducer.apply(r, p)));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final ReduceEntriesTask<K, V> t = (ReduceEntriesTask)c;
                    ReduceEntriesTask<K, V> nextRight;
                    for (ReduceEntriesTask<K, V> s = t.rights; s != null; s = nextRight) {
                        final Map.Entry<K, V> sr;
                        if ((sr = s.result) != null) {
                            final Map.Entry<K, V> tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply(tr, sr));
                        }
                        final ReduceEntriesTask<K, V> reduceEntriesTask = t;
                        nextRight = s.nextRight;
                        reduceEntriesTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<? super K, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        MapReduceKeysTask<K, V, U> nextRight;
        
        MapReduceKeysTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceKeysTask<K, V, U> nextRight, final Fun<? super K, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Fun<? super K, ? extends U> transformer;
            final BiFun<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceKeysTask<K, V, U>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceKeysTask<Object, Object, Object>)this.rights, (Fun<? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.key)) != null) {
                        r = ((r == null) ? u : reducer.apply((Object)r, (Object)u));
                    }
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceKeysTask<K, V, U> t = (MapReduceKeysTask)c;
                    MapReduceKeysTask<K, V, U> nextRight;
                    for (MapReduceKeysTask<K, V, U> s = t.rights; s != null; s = nextRight) {
                        final U sr;
                        if ((sr = s.result) != null) {
                            final U tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final MapReduceKeysTask<K, V, U> mapReduceKeysTask = t;
                        nextRight = s.nextRight;
                        mapReduceKeysTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<? super V, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        MapReduceValuesTask<K, V, U> nextRight;
        
        MapReduceValuesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceValuesTask<K, V, U> nextRight, final Fun<? super V, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Fun<? super V, ? extends U> transformer;
            final BiFun<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceValuesTask<K, V, U>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceValuesTask<Object, Object, Object>)this.rights, (Fun<? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.val)) != null) {
                        r = ((r == null) ? u : reducer.apply((Object)r, (Object)u));
                    }
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceValuesTask<K, V, U> t = (MapReduceValuesTask)c;
                    MapReduceValuesTask<K, V, U> nextRight;
                    for (MapReduceValuesTask<K, V, U> s = t.rights; s != null; s = nextRight) {
                        final U sr;
                        if ((sr = s.result) != null) {
                            final U tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final MapReduceValuesTask<K, V, U> mapReduceValuesTask = t;
                        nextRight = s.nextRight;
                        mapReduceValuesTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        MapReduceEntriesTask<K, V, U> nextRight;
        
        MapReduceEntriesTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceEntriesTask<K, V, U> nextRight, final Fun<Map.Entry<K, V>, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Fun<Map.Entry<K, V>, ? extends U> transformer;
            final BiFun<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceEntriesTask<K, V, U>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceEntriesTask<Object, Object, Object>)this.rights, (Fun<Map.Entry<Object, Object>, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply(p)) != null) {
                        r = ((r == null) ? u : reducer.apply((Object)r, (Object)u));
                    }
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceEntriesTask<K, V, U> t = (MapReduceEntriesTask)c;
                    MapReduceEntriesTask<K, V, U> nextRight;
                    for (MapReduceEntriesTask<K, V, U> s = t.rights; s != null; s = nextRight) {
                        final U sr;
                        if ((sr = s.result) != null) {
                            final U tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final MapReduceEntriesTask<K, V, U> mapReduceEntriesTask = t;
                        nextRight = s.nextRight;
                        mapReduceEntriesTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U>
    {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        MapReduceMappingsTask<K, V, U> nextRight;
        
        MapReduceMappingsTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceMappingsTask<K, V, U> nextRight, final BiFun<? super K, ? super V, ? extends U> transformer, final BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFun<? super K, ? super V, ? extends U> transformer;
            final BiFun<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceMappingsTask<K, V, U>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceMappingsTask<Object, Object, Object>)this.rights, (BiFun<? super Object, ? super Object, ?>)transformer, (BiFun<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U r = null;
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    final U u;
                    if ((u = (U)transformer.apply((Object)p.key, (Object)p.val)) != null) {
                        r = ((r == null) ? u : reducer.apply((Object)r, (Object)u));
                    }
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceMappingsTask<K, V, U> t = (MapReduceMappingsTask)c;
                    MapReduceMappingsTask<K, V, U> nextRight;
                    for (MapReduceMappingsTask<K, V, U> s = t.rights; s != null; s = nextRight) {
                        final U sr;
                        if ((sr = s.result) != null) {
                            final U tr;
                            t.result = (((tr = t.result) == null) ? sr : reducer.apply((Object)tr, (Object)sr));
                        }
                        final MapReduceMappingsTask<K, V, U> mapReduceMappingsTask = t;
                        nextRight = s.nextRight;
                        mapReduceMappingsTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ObjectToDouble<? super K> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        MapReduceKeysToDoubleTask<K, V> nextRight;
        
        MapReduceKeysToDoubleTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceKeysToDoubleTask<K, V> nextRight, final ObjectToDouble<? super K> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToDouble<? super K> transformer;
            final DoubleByDoubleToDouble reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceKeysToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceKeysToDoubleTask<Object, Object>)this.rights, (ObjectToDouble<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceKeysToDoubleTask<K, V> t = (MapReduceKeysToDoubleTask)c;
                    MapReduceKeysToDoubleTask<K, V> nextRight;
                    for (MapReduceKeysToDoubleTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask = t;
                        nextRight = s.nextRight;
                        mapReduceKeysToDoubleTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ObjectToDouble<? super V> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        MapReduceValuesToDoubleTask<K, V> nextRight;
        
        MapReduceValuesToDoubleTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceValuesToDoubleTask<K, V> nextRight, final ObjectToDouble<? super V> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToDouble<? super V> transformer;
            final DoubleByDoubleToDouble reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceValuesToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceValuesToDoubleTask<Object, Object>)this.rights, (ObjectToDouble<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceValuesToDoubleTask<K, V> t = (MapReduceValuesToDoubleTask)c;
                    MapReduceValuesToDoubleTask<K, V> nextRight;
                    for (MapReduceValuesToDoubleTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask = t;
                        nextRight = s.nextRight;
                        mapReduceValuesToDoubleTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ObjectToDouble<Map.Entry<K, V>> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        MapReduceEntriesToDoubleTask<K, V> nextRight;
        
        MapReduceEntriesToDoubleTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceEntriesToDoubleTask<K, V> nextRight, final ObjectToDouble<Map.Entry<K, V>> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToDouble<Map.Entry<K, V>> transformer;
            final DoubleByDoubleToDouble reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceEntriesToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceEntriesToDoubleTask<Object, Object>)this.rights, (ObjectToDouble<Map.Entry<Object, Object>>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply(p));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceEntriesToDoubleTask<K, V> t = (MapReduceEntriesToDoubleTask)c;
                    MapReduceEntriesToDoubleTask<K, V> nextRight;
                    for (MapReduceEntriesToDoubleTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask = t;
                        nextRight = s.nextRight;
                        mapReduceEntriesToDoubleTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ObjectByObjectToDouble<? super K, ? super V> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        MapReduceMappingsToDoubleTask<K, V> nextRight;
        
        MapReduceMappingsToDoubleTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceMappingsToDoubleTask<K, V> nextRight, final ObjectByObjectToDouble<? super K, ? super V> transformer, final double basis, final DoubleByDoubleToDouble reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectByObjectToDouble<? super K, ? super V> transformer;
            final DoubleByDoubleToDouble reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceMappingsToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceMappingsToDoubleTask<Object, Object>)this.rights, (ObjectByObjectToDouble<? super Object, ? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key, (Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceMappingsToDoubleTask<K, V> t = (MapReduceMappingsToDoubleTask)c;
                    MapReduceMappingsToDoubleTask<K, V> nextRight;
                    for (MapReduceMappingsToDoubleTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask = t;
                        nextRight = s.nextRight;
                        mapReduceMappingsToDoubleTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ObjectToLong<? super K> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        MapReduceKeysToLongTask<K, V> nextRight;
        
        MapReduceKeysToLongTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceKeysToLongTask<K, V> nextRight, final ObjectToLong<? super K> transformer, final long basis, final LongByLongToLong reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToLong<? super K> transformer;
            final LongByLongToLong reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceKeysToLongTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceKeysToLongTask<Object, Object>)this.rights, (ObjectToLong<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceKeysToLongTask<K, V> t = (MapReduceKeysToLongTask)c;
                    MapReduceKeysToLongTask<K, V> nextRight;
                    for (MapReduceKeysToLongTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask = t;
                        nextRight = s.nextRight;
                        mapReduceKeysToLongTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ObjectToLong<? super V> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        MapReduceValuesToLongTask<K, V> nextRight;
        
        MapReduceValuesToLongTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceValuesToLongTask<K, V> nextRight, final ObjectToLong<? super V> transformer, final long basis, final LongByLongToLong reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToLong<? super V> transformer;
            final LongByLongToLong reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceValuesToLongTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceValuesToLongTask<Object, Object>)this.rights, (ObjectToLong<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceValuesToLongTask<K, V> t = (MapReduceValuesToLongTask)c;
                    MapReduceValuesToLongTask<K, V> nextRight;
                    for (MapReduceValuesToLongTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask = t;
                        nextRight = s.nextRight;
                        mapReduceValuesToLongTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ObjectToLong<Map.Entry<K, V>> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        MapReduceEntriesToLongTask<K, V> nextRight;
        
        MapReduceEntriesToLongTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceEntriesToLongTask<K, V> nextRight, final ObjectToLong<Map.Entry<K, V>> transformer, final long basis, final LongByLongToLong reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToLong<Map.Entry<K, V>> transformer;
            final LongByLongToLong reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceEntriesToLongTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceEntriesToLongTask<Object, Object>)this.rights, (ObjectToLong<Map.Entry<Object, Object>>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply(p));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceEntriesToLongTask<K, V> t = (MapReduceEntriesToLongTask)c;
                    MapReduceEntriesToLongTask<K, V> nextRight;
                    for (MapReduceEntriesToLongTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask = t;
                        nextRight = s.nextRight;
                        mapReduceEntriesToLongTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ObjectByObjectToLong<? super K, ? super V> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        MapReduceMappingsToLongTask<K, V> nextRight;
        
        MapReduceMappingsToLongTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceMappingsToLongTask<K, V> nextRight, final ObjectByObjectToLong<? super K, ? super V> transformer, final long basis, final LongByLongToLong reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectByObjectToLong<? super K, ? super V> transformer;
            final LongByLongToLong reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceMappingsToLongTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceMappingsToLongTask<Object, Object>)this.rights, (ObjectByObjectToLong<? super Object, ? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key, (Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceMappingsToLongTask<K, V> t = (MapReduceMappingsToLongTask)c;
                    MapReduceMappingsToLongTask<K, V> nextRight;
                    for (MapReduceMappingsToLongTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask = t;
                        nextRight = s.nextRight;
                        mapReduceMappingsToLongTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ObjectToInt<? super K> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        MapReduceKeysToIntTask<K, V> nextRight;
        
        MapReduceKeysToIntTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceKeysToIntTask<K, V> nextRight, final ObjectToInt<? super K> transformer, final int basis, final IntByIntToInt reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToInt<? super K> transformer;
            final IntByIntToInt reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceKeysToIntTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceKeysToIntTask<Object, Object>)this.rights, (ObjectToInt<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceKeysToIntTask<K, V> t = (MapReduceKeysToIntTask)c;
                    MapReduceKeysToIntTask<K, V> nextRight;
                    for (MapReduceKeysToIntTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask = t;
                        nextRight = s.nextRight;
                        mapReduceKeysToIntTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ObjectToInt<? super V> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        MapReduceValuesToIntTask<K, V> nextRight;
        
        MapReduceValuesToIntTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceValuesToIntTask<K, V> nextRight, final ObjectToInt<? super V> transformer, final int basis, final IntByIntToInt reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToInt<? super V> transformer;
            final IntByIntToInt reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceValuesToIntTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceValuesToIntTask<Object, Object>)this.rights, (ObjectToInt<? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceValuesToIntTask<K, V> t = (MapReduceValuesToIntTask)c;
                    MapReduceValuesToIntTask<K, V> nextRight;
                    for (MapReduceValuesToIntTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask = t;
                        nextRight = s.nextRight;
                        mapReduceValuesToIntTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ObjectToInt<Map.Entry<K, V>> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        MapReduceEntriesToIntTask<K, V> nextRight;
        
        MapReduceEntriesToIntTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceEntriesToIntTask<K, V> nextRight, final ObjectToInt<Map.Entry<K, V>> transformer, final int basis, final IntByIntToInt reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectToInt<Map.Entry<K, V>> transformer;
            final IntByIntToInt reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceEntriesToIntTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceEntriesToIntTask<Object, Object>)this.rights, (ObjectToInt<Map.Entry<Object, Object>>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply(p));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceEntriesToIntTask<K, V> t = (MapReduceEntriesToIntTask)c;
                    MapReduceEntriesToIntTask<K, V> nextRight;
                    for (MapReduceEntriesToIntTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask = t;
                        nextRight = s.nextRight;
                        mapReduceEntriesToIntTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ObjectByObjectToInt<? super K, ? super V> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        MapReduceMappingsToIntTask<K, V> nextRight;
        
        MapReduceMappingsToIntTask(final BulkTask<K, V, ?> p, final int b, final int i, final int f, final Node<K, V>[] t, final MapReduceMappingsToIntTask<K, V> nextRight, final ObjectByObjectToInt<? super K, ? super V> transformer, final int basis, final IntByIntToInt reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ObjectByObjectToInt<? super K, ? super V> transformer;
            final IntByIntToInt reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int r = this.basis;
                final int i = this.baseIndex;
                int f;
                int h;
                while (this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i) {
                    this.addToPendingCount(1);
                    final int n = this.batch >>> 1;
                    this.batch = n;
                    (this.rights = new MapReduceMappingsToIntTask<K, V>((BulkTask<Object, Object, ?>)this, n, this.baseLimit = h, f, (Node<Object, Object>[])this.tab, (MapReduceMappingsToIntTask<Object, Object>)this.rights, (ObjectByObjectToInt<? super Object, ? super Object>)transformer, r, reducer)).fork();
                }
                Node<K, V> p;
                while ((p = this.advance()) != null) {
                    r = reducer.apply(r, transformer.apply((Object)p.key, (Object)p.val));
                }
                this.result = r;
                for (CountedCompleter<?> c = this.firstComplete(); c != null; c = c.nextComplete()) {
                    final MapReduceMappingsToIntTask<K, V> t = (MapReduceMappingsToIntTask)c;
                    MapReduceMappingsToIntTask<K, V> nextRight;
                    for (MapReduceMappingsToIntTask<K, V> s = t.rights; s != null; s = nextRight) {
                        t.result = reducer.apply(t.result, s.result);
                        final MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask = t;
                        nextRight = s.nextRight;
                        mapReduceMappingsToIntTask.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class CounterCell
    {
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long value;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        
        CounterCell(final long x) {
            this.value = x;
        }
    }
    
    static final class CounterHashCode
    {
        int code;
    }
    
    public interface ObjectByObjectToInt<A, B>
    {
        int apply(final A p0, final B p1);
    }
    
    public interface IntByIntToInt
    {
        int apply(final int p0, final int p1);
    }
    
    public interface ObjectToInt<A>
    {
        int apply(final A p0);
    }
    
    public interface ObjectByObjectToLong<A, B>
    {
        long apply(final A p0, final B p1);
    }
    
    public interface LongByLongToLong
    {
        long apply(final long p0, final long p1);
    }
    
    public interface ObjectToLong<A>
    {
        long apply(final A p0);
    }
    
    public interface ObjectByObjectToDouble<A, B>
    {
        double apply(final A p0, final B p1);
    }
    
    public interface DoubleByDoubleToDouble
    {
        double apply(final double p0, final double p1);
    }
    
    public interface ObjectToDouble<A>
    {
        double apply(final A p0);
    }
    
    public interface BiFun<A, B, T>
    {
        T apply(final A p0, final B p1);
    }
    
    public interface Fun<A, T>
    {
        T apply(final A p0);
    }
    
    public interface Action<A>
    {
        void apply(final A p0);
    }
    
    public interface BiAction<A, B>
    {
        void apply(final A p0, final B p1);
    }
    
    public interface ConcurrentHashMapSpliterator<T>
    {
        ConcurrentHashMapSpliterator<T> trySplit();
        
        long estimateSize();
        
        void forEachRemaining(final Action<? super T> p0);
        
        boolean tryAdvance(final Action<? super T> p0);
    }
}
