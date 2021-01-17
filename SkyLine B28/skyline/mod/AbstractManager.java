package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod;

import java.util.*;
import org.apache.commons.lang3.*;
import java.lang.reflect.*;

public abstract class AbstractManager<E>
{
    private final Class typeClass;
    protected Object[] array;
    
    public AbstractManager(final Class<E> typeClass, final int size) {
        this.typeClass = typeClass;
        this.reset(size);
    }
    
    public abstract void setup();
    
    public void set(final E item, final int index) {
        this.array[index] = item;
    }
    
    public E get(final int index) {
        return (E)this.array[index];
    }
    
    public E get(final Class clazz) {
        Object[] array;
        for (int length = (array = this.array).length, i = 0; i < length; ++i) {
            final E e = (E)array[i];
            if (e.getClass().equals(clazz)) {
                return e;
            }
        }
        return null;
    }
    
    public void add(final E e) {
        (this.array = Arrays.copyOf(this.array, this.array.length + 1))[this.array.length - 1] = e;
    }
    
    public void remove(final E e) {
        this.array = ArrayUtils.removeElement(this.array, (Object)e);
    }
    
    public void reset(final int size) {
        this.array = (Object[])Array.newInstance(this.typeClass, size);
    }
    
    public E[] getArray() {
        return (E[])this.array;
    }
}
