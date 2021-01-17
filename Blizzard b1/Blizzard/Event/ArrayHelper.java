/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event;

import java.util.Iterator;

public class ArrayHelper<T>
implements Iterable<T> {
    private T[] elements;

    public ArrayHelper(T[] array) {
        this.elements = array;
    }

    public ArrayHelper() {
        this.elements = (T[]) new Object[0];
    }

    public void add(T t) {
        if (t != null) {
            Object[] array = new Object[this.size() + 1];
            int i = 0;
            while (i < array.length) {
                array[i] = i < this.size() ? this.get(i) : t;
                ++i;
            }
            this.set((T[]) array);
        }
    }

    public boolean contains(T t) {
        T[] array = this.array();
        int lenght = array.length;
        int i = 0;
        while (i < lenght) {
            T entry = array[i];
            if (entry.equals(t)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public void remove(T t) {
        if (this.contains(t)) {
            Object[] array = new Object[this.size() - 1];
            boolean b = true;
            int i = 0;
            while (i < this.size()) {
                if (b && this.get(i).equals(t)) {
                    b = false;
                } else {
                    array[b != false ? i : i - 1] = this.get(i);
                }
                ++i;
            }
            this.set((T[]) array);
        }
    }

    public T[] array() {
        return this.elements;
    }

    public int size() {
        return this.array().length;
    }

    public void set(T[] array) {
        this.elements = array;
    }

    public T get(int index) {
        return this.array()[index];
    }

    public void clear() {
        this.elements = (T[]) new Object[0];
    }

    public boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            private int index;

            @Override
            public boolean hasNext() {
                if (this.index < ArrayHelper.this.size() && ArrayHelper.this.get(this.index) != null) {
                    return true;
                }
                return false;
            }

            @Override
            public T next() {
                return ArrayHelper.this.get(this.index++);
            }

            @Override
            public void remove() {
                ArrayHelper.this.remove(ArrayHelper.this.get(this.index));
            }
        };
    }

}

