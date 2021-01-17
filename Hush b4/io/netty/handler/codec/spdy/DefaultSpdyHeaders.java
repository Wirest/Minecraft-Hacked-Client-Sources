// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class DefaultSpdyHeaders extends SpdyHeaders
{
    private static final int BUCKET_SIZE = 17;
    private final HeaderEntry[] entries;
    private final HeaderEntry head;
    
    private static int hash(final String name) {
        int h = 0;
        for (int i = name.length() - 1; i >= 0; --i) {
            char c = name.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c += ' ';
            }
            h = 31 * h + c;
        }
        if (h > 0) {
            return h;
        }
        if (h == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return -h;
    }
    
    private static boolean eq(final String name1, final String name2) {
        final int nameLen = name1.length();
        if (nameLen != name2.length()) {
            return false;
        }
        for (int i = nameLen - 1; i >= 0; --i) {
            char c1 = name1.charAt(i);
            char c2 = name2.charAt(i);
            if (c1 != c2) {
                if (c1 >= 'A' && c1 <= 'Z') {
                    c1 += ' ';
                }
                if (c2 >= 'A' && c2 <= 'Z') {
                    c2 += ' ';
                }
                if (c1 != c2) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static int index(final int hash) {
        return hash % 17;
    }
    
    DefaultSpdyHeaders() {
        this.entries = new HeaderEntry[17];
        this.head = new HeaderEntry(-1, null, null);
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
    }
    
    @Override
    public SpdyHeaders add(final String name, final Object value) {
        final String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        final String strVal = toString(value);
        SpdyCodecUtil.validateHeaderValue(strVal);
        final int h = hash(lowerCaseName);
        final int i = index(h);
        this.add0(h, i, lowerCaseName, strVal);
        return this;
    }
    
    private void add0(final int h, final int i, final String name, final String value) {
        final HeaderEntry e = this.entries[i];
        final HeaderEntry newEntry = this.entries[i] = new HeaderEntry(h, name, value);
        newEntry.next = e;
        newEntry.addBefore(this.head);
    }
    
    @Override
    public SpdyHeaders remove(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final String lowerCaseName = name.toLowerCase();
        final int h = hash(lowerCaseName);
        final int i = index(h);
        this.remove0(h, i, lowerCaseName);
        return this;
    }
    
    private void remove0(final int h, final int i, final String name) {
        HeaderEntry e = this.entries[i];
        if (e == null) {
            return;
        }
        while (e.hash == h && eq(name, e.key)) {
            e.remove();
            final HeaderEntry next = e.next;
            if (next == null) {
                this.entries[i] = null;
                return;
            }
            this.entries[i] = next;
            e = next;
        }
        while (true) {
            final HeaderEntry next = e.next;
            if (next == null) {
                break;
            }
            if (next.hash == h && eq(name, next.key)) {
                e.next = next.next;
                next.remove();
            }
            else {
                e = next;
            }
        }
    }
    
    @Override
    public SpdyHeaders set(final String name, final Object value) {
        final String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        final String strVal = toString(value);
        SpdyCodecUtil.validateHeaderValue(strVal);
        final int h = hash(lowerCaseName);
        final int i = index(h);
        this.remove0(h, i, lowerCaseName);
        this.add0(h, i, lowerCaseName, strVal);
        return this;
    }
    
    @Override
    public SpdyHeaders set(final String name, final Iterable<?> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
        final String lowerCaseName = name.toLowerCase();
        SpdyCodecUtil.validateHeaderName(lowerCaseName);
        final int h = hash(lowerCaseName);
        final int i = index(h);
        this.remove0(h, i, lowerCaseName);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            final String strVal = toString(v);
            SpdyCodecUtil.validateHeaderValue(strVal);
            this.add0(h, i, lowerCaseName, strVal);
        }
        return this;
    }
    
    @Override
    public SpdyHeaders clear() {
        for (int i = 0; i < this.entries.length; ++i) {
            this.entries[i] = null;
        }
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
        return this;
    }
    
    @Override
    public String get(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final int h = hash(name);
        final int i = index(h);
        for (HeaderEntry e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && eq(name, e.key)) {
                return e.value;
            }
        }
        return null;
    }
    
    @Override
    public List<String> getAll(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final LinkedList<String> values = new LinkedList<String>();
        final int h = hash(name);
        final int i = index(h);
        for (HeaderEntry e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && eq(name, e.key)) {
                values.addFirst(e.value);
            }
        }
        return values;
    }
    
    @Override
    public List<Map.Entry<String, String>> entries() {
        final List<Map.Entry<String, String>> all = new LinkedList<Map.Entry<String, String>>();
        for (HeaderEntry e = this.head.after; e != this.head; e = e.after) {
            all.add(e);
        }
        return all;
    }
    
    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new HeaderIterator();
    }
    
    @Override
    public boolean contains(final String name) {
        return this.get(name) != null;
    }
    
    @Override
    public Set<String> names() {
        final Set<String> names = new TreeSet<String>();
        for (HeaderEntry e = this.head.after; e != this.head; e = e.after) {
            names.add(e.key);
        }
        return names;
    }
    
    @Override
    public SpdyHeaders add(final String name, final Iterable<?> values) {
        SpdyCodecUtil.validateHeaderValue(name);
        final int h = hash(name);
        final int i = index(h);
        for (final Object v : values) {
            final String vstr = toString(v);
            SpdyCodecUtil.validateHeaderValue(vstr);
            this.add0(h, i, name, vstr);
        }
        return this;
    }
    
    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }
    
    private static String toString(final Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    private final class HeaderIterator implements Iterator<Map.Entry<String, String>>
    {
        private HeaderEntry current;
        
        private HeaderIterator() {
            this.current = DefaultSpdyHeaders.this.head;
        }
        
        @Override
        public boolean hasNext() {
            return this.current.after != DefaultSpdyHeaders.this.head;
        }
        
        @Override
        public Map.Entry<String, String> next() {
            this.current = this.current.after;
            if (this.current == DefaultSpdyHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private static final class HeaderEntry implements Map.Entry<String, String>
    {
        final int hash;
        final String key;
        String value;
        HeaderEntry next;
        HeaderEntry before;
        HeaderEntry after;
        
        HeaderEntry(final int hash, final String key, final String value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
        
        void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }
        
        void addBefore(final HeaderEntry e) {
            this.after = e;
            this.before = e.before;
            this.before.after = this;
            this.after.before = this;
        }
        
        @Override
        public String getKey() {
            return this.key;
        }
        
        @Override
        public String getValue() {
            return this.value;
        }
        
        @Override
        public String setValue(final String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            SpdyCodecUtil.validateHeaderValue(value);
            final String oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public String toString() {
            return this.key + '=' + this.value;
        }
    }
}
