// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T>
{
    private static final Set<Class<?>> field_181158_a;
    private final Map<Class<?>, List<T>> map;
    private final Set<Class<?>> knownKeys;
    private final Class<T> baseClass;
    private final List<T> field_181745_e;
    
    static {
        field_181158_a = Sets.newHashSet();
    }
    
    public ClassInheritanceMultiMap(final Class<T> baseClassIn) {
        this.map = (Map<Class<?>, List<T>>)Maps.newHashMap();
        this.knownKeys = Sets.newIdentityHashSet();
        this.field_181745_e = (List<T>)Lists.newArrayList();
        this.baseClass = baseClassIn;
        this.knownKeys.add(baseClassIn);
        this.map.put((Class<?>)baseClassIn, (List<?>)this.field_181745_e);
        for (final Class<?> oclass : ClassInheritanceMultiMap.field_181158_a) {
            this.createLookup(oclass);
        }
    }
    
    protected void createLookup(final Class<?> clazz) {
        ClassInheritanceMultiMap.field_181158_a.add(clazz);
        for (final T t : this.field_181745_e) {
            if (clazz.isAssignableFrom(t.getClass())) {
                this.func_181743_a(t, clazz);
            }
        }
        this.knownKeys.add(clazz);
    }
    
    protected Class<?> func_181157_b(final Class<?> p_181157_1_) {
        if (this.baseClass.isAssignableFrom(p_181157_1_)) {
            if (!this.knownKeys.contains(p_181157_1_)) {
                this.createLookup(p_181157_1_);
            }
            return p_181157_1_;
        }
        throw new IllegalArgumentException("Don't know how to search for " + p_181157_1_);
    }
    
    @Override
    public boolean add(final T p_add_1_) {
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(p_add_1_.getClass())) {
                this.func_181743_a(p_add_1_, oclass);
            }
        }
        return true;
    }
    
    private void func_181743_a(final T p_181743_1_, final Class<?> p_181743_2_) {
        final List<T> list = this.map.get(p_181743_2_);
        if (list == null) {
            this.map.put(p_181743_2_, Lists.newArrayList(p_181743_1_));
        }
        else {
            list.add(p_181743_1_);
        }
    }
    
    @Override
    public boolean remove(final Object p_remove_1_) {
        final T t = (T)p_remove_1_;
        boolean flag = false;
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(t.getClass())) {
                final List<T> list = this.map.get(oclass);
                if (list == null || !list.remove(t)) {
                    continue;
                }
                flag = true;
            }
        }
        return flag;
    }
    
    @Override
    public boolean contains(final Object p_contains_1_) {
        return Iterators.contains(this.getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
    }
    
    public <S> Iterable<S> getByClass(final Class<S> clazz) {
        return new Iterable<S>() {
            @Override
            public Iterator<S> iterator() {
                final List<T> list = ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.func_181157_b(clazz));
                if (list == null) {
                    return (Iterator<S>)Iterators.emptyIterator();
                }
                final Iterator<T> iterator = list.iterator();
                return (Iterator<S>)Iterators.filter(iterator, (Class<Object>)clazz);
            }
        };
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.field_181745_e.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator());
    }
    
    @Override
    public int size() {
        return this.field_181745_e.size();
    }
}
