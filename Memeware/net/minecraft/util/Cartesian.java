package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Cartesian {
    private static final String __OBFID = "CL_00002327";

    /**
     * Create the cartesian product. This method returns an Iterable of arrays of type clazz.
     *
     * @param sets the Sets to combine. This is an Iterable of Iterables of type clazz
     */
    public static Iterable cartesianProduct(Class clazz, Iterable sets) {
        return new Cartesian.Product(clazz, (Iterable[]) toArray(Iterable.class, sets), null);
    }

    /**
     * Like cartesianProduct(Class, Iterable) but returns an Iterable of Lists instead.
     */
    public static Iterable cartesianProduct(Iterable sets) {
        return arraysAsLists(cartesianProduct(Object.class, sets));
    }

    /**
     * Convert an Iterable of Arrays (Object[]) to an Iterable of Lists
     */
    private static Iterable arraysAsLists(Iterable arrays) {
        return Iterables.transform(arrays, new Cartesian.GetList(null));
    }

    /**
     * Create a new Array of type clazz with the contents of the given Iterable
     */
    private static Object[] toArray(Class clazz, Iterable it) {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = it.iterator();

        while (var3.hasNext()) {
            Object var4 = var3.next();
            var2.add(var4);
        }

        return (Object[]) var2.toArray(createArray(clazz, var2.size()));
    }

    private static Object[] createArray(Class p_179319_0_, int p_179319_1_) {
        return (Object[]) ((Object[]) Array.newInstance(p_179319_0_, p_179319_1_));
    }

    static class GetList implements Function {
        private static final String __OBFID = "CL_00002325";

        private GetList() {
        }

        public List apply(Object[] array) {
            return Arrays.asList((Object[]) array);
        }

        public Object apply(Object p_apply_1_) {
            return this.apply((Object[]) p_apply_1_);
        }

        GetList(Object p_i46022_1_) {
            this();
        }
    }

    static class Product implements Iterable {
        private final Class clazz;
        private final Iterable[] iterables;
        private static final String __OBFID = "CL_00002324";

        private Product(Class clazz, Iterable[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }

        public Iterator iterator() {
            return (Iterator) (this.iterables.length <= 0 ? Collections.singletonList((Object[]) Cartesian.createArray(this.clazz, 0)).iterator() : new Cartesian.Product.ProductIterator(this.clazz, this.iterables, null));
        }

        Product(Class p_i46021_1_, Iterable[] p_i46021_2_, Object p_i46021_3_) {
            this(p_i46021_1_, p_i46021_2_);
        }

        static class ProductIterator extends UnmodifiableIterator {
            private int index;
            private final Iterable[] iterables;
            private final Iterator[] iterators;
            private final Object[] results;
            private static final String __OBFID = "CL_00002323";

            private ProductIterator(Class clazz, Iterable[] iterables) {
                this.index = -2;
                this.iterables = iterables;
                this.iterators = (Iterator[]) Cartesian.createArray(Iterator.class, this.iterables.length);

                for (int var3 = 0; var3 < this.iterables.length; ++var3) {
                    this.iterators[var3] = iterables[var3].iterator();
                }

                this.results = Cartesian.createArray(clazz, this.iterators.length);
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, (Object) null);
                Arrays.fill(this.results, (Object) null);
            }

            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    Iterator[] var5 = this.iterators;
                    int var2 = var5.length;

                    for (int var3 = 0; var3 < var2; ++var3) {
                        Iterator var4 = var5[var3];

                        if (!var4.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }

                    return true;
                } else {
                    if (this.index >= this.iterators.length) {
                        for (this.index = this.iterators.length - 1; this.index >= 0; --this.index) {
                            Iterator var1 = this.iterators[this.index];

                            if (var1.hasNext()) {
                                break;
                            }

                            if (this.index == 0) {
                                this.endOfData();
                                break;
                            }

                            var1 = this.iterables[this.index].iterator();
                            this.iterators[this.index] = var1;

                            if (!var1.hasNext()) {
                                this.endOfData();
                                break;
                            }
                        }
                    }

                    return this.index >= 0;
                }
            }

            public Object[] next0() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    while (this.index < this.iterators.length) {
                        this.results[this.index] = this.iterators[this.index].next();
                        ++this.index;
                    }

                    return (Object[]) this.results.clone();
                }
            }

            public Object next() {
                return this.next0();
            }

            ProductIterator(Class p_i46019_1_, Iterable[] p_i46019_2_, Object p_i46019_3_) {
                this(p_i46019_1_, p_i46019_2_);
            }
        }
    }
}
