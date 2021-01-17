// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.RandomAccess;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import java.util.Comparator;
import com.google.common.base.Preconditions;
import java.util.List;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
final class SortedLists
{
    private SortedLists() {
    }
    
    public static <E extends Comparable> int binarySearch(final List<? extends E> list, final E e, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.checkNotNull(e);
        return binarySearch(list, (Object)Preconditions.checkNotNull((E)e), (Comparator<? super Object>)Ordering.natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K extends Comparable> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, @Nullable final K key, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, @Nullable final K key, final Comparator<? super K> keyComparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        return binarySearch(Lists.transform(list, (Function<? super E, ? extends K>)keyFunction), key, keyComparator, presentBehavior, absentBehavior);
    }
    
    public static <E> int binarySearch(List<? extends E> list, @Nullable final E key, final Comparator<? super E> comparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(list);
        Preconditions.checkNotNull(presentBehavior);
        Preconditions.checkNotNull(absentBehavior);
        if (!(list instanceof RandomAccess)) {
            list = (List<? extends E>)Lists.newArrayList((Iterable<?>)list);
        }
        int lower = 0;
        int upper = list.size() - 1;
        while (lower <= upper) {
            final int middle = lower + upper >>> 1;
            final int c = comparator.compare((Object)key, (Object)list.get(middle));
            if (c < 0) {
                upper = middle - 1;
            }
            else {
                if (c <= 0) {
                    return lower + presentBehavior.resultIndex(comparator, key, list.subList(lower, upper + 1), middle - lower);
                }
                lower = middle + 1;
            }
        }
        return absentBehavior.resultIndex(lower);
    }
    
    public enum KeyPresentBehavior
    {
        ANY_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return foundIndex;
            }
        }, 
        LAST_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                int lower = foundIndex;
                int upper = list.size() - 1;
                while (lower < upper) {
                    final int middle = lower + upper + 1 >>> 1;
                    final int c = comparator.compare((Object)list.get(middle), (Object)key);
                    if (c > 0) {
                        upper = middle - 1;
                    }
                    else {
                        lower = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                int lower = 0;
                int upper = foundIndex;
                while (lower < upper) {
                    final int middle = lower + upper >>> 1;
                    final int c = comparator.compare((Object)list.get(middle), (Object)key);
                    if (c < 0) {
                        lower = middle + 1;
                    }
                    else {
                        upper = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_AFTER {
            public <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$4.LAST_PRESENT.resultIndex(comparator, key, list, foundIndex) + 1;
            }
        }, 
        LAST_BEFORE {
            public <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$5.FIRST_PRESENT.resultIndex(comparator, key, list, foundIndex) - 1;
            }
        };
        
        abstract <E> int resultIndex(final Comparator<? super E> p0, final E p1, final List<? extends E> p2, final int p3);
    }
    
    public enum KeyAbsentBehavior
    {
        NEXT_LOWER {
            @Override
            int resultIndex(final int higherIndex) {
                return higherIndex - 1;
            }
        }, 
        NEXT_HIGHER {
            public int resultIndex(final int higherIndex) {
                return higherIndex;
            }
        }, 
        INVERTED_INSERTION_INDEX {
            public int resultIndex(final int higherIndex) {
                return ~higherIndex;
            }
        };
        
        abstract int resultIndex(final int p0);
    }
}
