// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.NoSuchElementException;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.util.List;

public class Cartesian
{
    public static <T> Iterable<T[]> cartesianProduct(final Class<T> clazz, final Iterable<? extends Iterable<? extends T>> sets) {
        return new Product<T>(clazz, toArray((Class<? super Iterable>)Iterable.class, (Iterable<? extends Iterable>)sets), null);
    }
    
    public static <T> Iterable<List<T>> cartesianProduct(final Iterable<? extends Iterable<? extends T>> sets) {
        return (Iterable<List<T>>)arraysAsLists(cartesianProduct(Object.class, sets));
    }
    
    private static <T> Iterable<List<T>> arraysAsLists(final Iterable<Object[]> arrays) {
        return Iterables.transform(arrays, (Function<? super Object[], ? extends List<T>>)new GetList(null));
    }
    
    private static <T> T[] toArray(final Class<? super T> clazz, final Iterable<? extends T> it) {
        final List<T> list = (List<T>)Lists.newArrayList();
        for (final T t : it) {
            list.add(t);
        }
        return list.toArray(createArray(clazz, list.size()));
    }
    
    private static <T> T[] createArray(final Class<? super T> p_179319_0_, final int p_179319_1_) {
        return (T[])Array.newInstance(p_179319_0_, p_179319_1_);
    }
    
    static class GetList<T> implements Function<Object[], List<T>>
    {
        private GetList() {
        }
        
        @Override
        public List<T> apply(final Object[] p_apply_1_) {
            return Arrays.asList((T[])p_apply_1_);
        }
    }
    
    static class Product<T> implements Iterable<T[]>
    {
        private final Class<T> clazz;
        private final Iterable<? extends T>[] iterables;
        
        private Product(final Class<T> clazz, final Iterable<? extends T>[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }
        
        @Override
        public Iterator<T[]> iterator() {
            return (Iterator<T[]>)((this.iterables.length <= 0) ? Collections.singletonList(createArray(this.clazz, 0)).iterator() : new ProductIterator(this.clazz, this.iterables, null));
        }
        
        static class ProductIterator<T> extends UnmodifiableIterator<T[]>
        {
            private int index;
            private final Iterable<? extends T>[] iterables;
            private final Iterator<? extends T>[] iterators;
            private final T[] results;
            
            private ProductIterator(final Class<T> clazz, final Iterable<? extends T>[] iterables) {
                this.index = -2;
                this.iterables = iterables;
                this.iterators = (Iterator<? extends T>[])createArray(Iterator.class, this.iterables.length);
                for (int i = 0; i < this.iterables.length; ++i) {
                    this.iterators[i] = iterables[i].iterator();
                }
                this.results = (T[])createArray(clazz, this.iterators.length);
            }
            
            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }
            
            @Override
            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    Iterator<? extends T>[] iterators;
                    for (int length = (iterators = this.iterators).length, i = 0; i < length; ++i) {
                        final Iterator<? extends T> iterator1 = iterators[i];
                        if (!iterator1.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }
                    return true;
                }
                if (this.index >= this.iterators.length) {
                    this.index = this.iterators.length - 1;
                    while (this.index >= 0) {
                        Iterator<? extends T> iterator2 = this.iterators[this.index];
                        if (iterator2.hasNext()) {
                            break;
                        }
                        if (this.index == 0) {
                            this.endOfData();
                            break;
                        }
                        iterator2 = this.iterables[this.index].iterator();
                        this.iterators[this.index] = iterator2;
                        if (!iterator2.hasNext()) {
                            this.endOfData();
                            break;
                        }
                        --this.index;
                    }
                }
                return this.index >= 0;
            }
            
            @Override
            public T[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                while (this.index < this.iterators.length) {
                    this.results[this.index] = (T)this.iterators[this.index].next();
                    ++this.index;
                }
                return this.results.clone();
            }
        }
    }
}
