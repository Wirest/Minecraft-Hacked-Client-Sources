// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.NoSuchElementException;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

public class DefaultHttpHeaders extends HttpHeaders
{
    private static final int BUCKET_SIZE = 17;
    private final HeaderEntry[] entries;
    private final HeaderEntry head;
    protected final boolean validate;
    
    private static int index(final int hash) {
        return hash % 17;
    }
    
    public DefaultHttpHeaders() {
        this(true);
    }
    
    public DefaultHttpHeaders(final boolean validate) {
        this.entries = new HeaderEntry[17];
        this.head = new HeaderEntry();
        this.validate = validate;
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
    }
    
    void validateHeaderName0(final CharSequence headerName) {
        HttpHeaders.validateHeaderName(headerName);
    }
    
    @Override
    public HttpHeaders add(final HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            final DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
            for (HeaderEntry e = defaultHttpHeaders.head.after; e != defaultHttpHeaders.head; e = e.after) {
                this.add(e.key, e.value);
            }
            return this;
        }
        return super.add(headers);
    }
    
    @Override
    public HttpHeaders set(final HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.clear();
            final DefaultHttpHeaders defaultHttpHeaders = (DefaultHttpHeaders)headers;
            for (HeaderEntry e = defaultHttpHeaders.head.after; e != defaultHttpHeaders.head; e = e.after) {
                this.add(e.key, e.value);
            }
            return this;
        }
        return super.set(headers);
    }
    
    @Override
    public HttpHeaders add(final String name, final Object value) {
        return this.add((CharSequence)name, value);
    }
    
    @Override
    public HttpHeaders add(final CharSequence name, final Object value) {
        CharSequence strVal;
        if (this.validate) {
            this.validateHeaderName0(name);
            strVal = toCharSequence(value);
            HttpHeaders.validateHeaderValue(strVal);
        }
        else {
            strVal = toCharSequence(value);
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        this.add0(h, i, name, strVal);
        return this;
    }
    
    @Override
    public HttpHeaders add(final String name, final Iterable<?> values) {
        return this.add((CharSequence)name, values);
    }
    
    @Override
    public HttpHeaders add(final CharSequence name, final Iterable<?> values) {
        if (this.validate) {
            this.validateHeaderName0(name);
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        for (final Object v : values) {
            final CharSequence vstr = toCharSequence(v);
            if (this.validate) {
                HttpHeaders.validateHeaderValue(vstr);
            }
            this.add0(h, i, name, vstr);
        }
        return this;
    }
    
    private void add0(final int h, final int i, final CharSequence name, final CharSequence value) {
        final HeaderEntry e = this.entries[i];
        final HeaderEntry newEntry = this.entries[i] = new HeaderEntry(h, name, value);
        newEntry.next = e;
        newEntry.addBefore(this.head);
    }
    
    @Override
    public HttpHeaders remove(final String name) {
        return this.remove((CharSequence)name);
    }
    
    @Override
    public HttpHeaders remove(final CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        this.remove0(h, i, name);
        return this;
    }
    
    private void remove0(final int h, final int i, final CharSequence name) {
        HeaderEntry e = this.entries[i];
        if (e == null) {
            return;
        }
        while (e.hash == h && HttpHeaders.equalsIgnoreCase(name, e.key)) {
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
            if (next.hash == h && HttpHeaders.equalsIgnoreCase(name, next.key)) {
                e.next = next.next;
                next.remove();
            }
            else {
                e = next;
            }
        }
    }
    
    @Override
    public HttpHeaders set(final String name, final Object value) {
        return this.set((CharSequence)name, value);
    }
    
    @Override
    public HttpHeaders set(final CharSequence name, final Object value) {
        CharSequence strVal;
        if (this.validate) {
            this.validateHeaderName0(name);
            strVal = toCharSequence(value);
            HttpHeaders.validateHeaderValue(strVal);
        }
        else {
            strVal = toCharSequence(value);
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        this.remove0(h, i, name);
        this.add0(h, i, name, strVal);
        return this;
    }
    
    @Override
    public HttpHeaders set(final String name, final Iterable<?> values) {
        return this.set((CharSequence)name, values);
    }
    
    @Override
    public HttpHeaders set(final CharSequence name, final Iterable<?> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
        if (this.validate) {
            this.validateHeaderName0(name);
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        this.remove0(h, i, name);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            final CharSequence strVal = toCharSequence(v);
            if (this.validate) {
                HttpHeaders.validateHeaderValue(strVal);
            }
            this.add0(h, i, name, strVal);
        }
        return this;
    }
    
    @Override
    public HttpHeaders clear() {
        Arrays.fill(this.entries, null);
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
        return this;
    }
    
    @Override
    public String get(final String name) {
        return this.get((CharSequence)name);
    }
    
    @Override
    public String get(final CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        HeaderEntry e = this.entries[i];
        CharSequence value = null;
        while (e != null) {
            if (e.hash == h && HttpHeaders.equalsIgnoreCase(name, e.key)) {
                value = e.value;
            }
            e = e.next;
        }
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    @Override
    public List<String> getAll(final String name) {
        return this.getAll((CharSequence)name);
    }
    
    @Override
    public List<String> getAll(final CharSequence name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final LinkedList<String> values = new LinkedList<String>();
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        for (HeaderEntry e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && HttpHeaders.equalsIgnoreCase(name, e.key)) {
                values.addFirst(e.getValue());
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
    public boolean contains(final CharSequence name) {
        return this.get(name) != null;
    }
    
    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }
    
    @Override
    public boolean contains(final String name, final String value, final boolean ignoreCaseValue) {
        return this.contains(name, (CharSequence)value, ignoreCaseValue);
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value, final boolean ignoreCaseValue) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        final int h = HttpHeaders.hash(name);
        final int i = index(h);
        for (HeaderEntry e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && HttpHeaders.equalsIgnoreCase(name, e.key)) {
                if (ignoreCaseValue) {
                    if (HttpHeaders.equalsIgnoreCase(e.value, value)) {
                        return true;
                    }
                }
                else if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Set<String> names() {
        final Set<String> names = new LinkedHashSet<String>();
        for (HeaderEntry e = this.head.after; e != this.head; e = e.after) {
            names.add(e.getKey());
        }
        return names;
    }
    
    private static CharSequence toCharSequence(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return (CharSequence)value;
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Date) {
            return HttpHeaderDateFormat.get().format((Date)value);
        }
        if (value instanceof Calendar) {
            return HttpHeaderDateFormat.get().format(((Calendar)value).getTime());
        }
        return value.toString();
    }
    
    void encode(final ByteBuf buf) {
        for (HeaderEntry e = this.head.after; e != this.head; e = e.after) {
            e.encode(buf);
        }
    }
    
    private final class HeaderIterator implements Iterator<Map.Entry<String, String>>
    {
        private HeaderEntry current;
        
        private HeaderIterator() {
            this.current = DefaultHttpHeaders.this.head;
        }
        
        @Override
        public boolean hasNext() {
            return this.current.after != DefaultHttpHeaders.this.head;
        }
        
        @Override
        public Map.Entry<String, String> next() {
            this.current = this.current.after;
            if (this.current == DefaultHttpHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class HeaderEntry implements Map.Entry<String, String>
    {
        final int hash;
        final CharSequence key;
        CharSequence value;
        HeaderEntry next;
        HeaderEntry before;
        HeaderEntry after;
        
        HeaderEntry(final int hash, final CharSequence key, final CharSequence value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
        
        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.value = null;
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
            return this.key.toString();
        }
        
        @Override
        public String getValue() {
            return this.value.toString();
        }
        
        @Override
        public String setValue(final String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            HttpHeaders.validateHeaderValue(value);
            final CharSequence oldValue = this.value;
            this.value = value;
            return oldValue.toString();
        }
        
        @Override
        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
        
        void encode(final ByteBuf buf) {
            HttpHeaders.encode(this.key, this.value, buf);
        }
    }
}
