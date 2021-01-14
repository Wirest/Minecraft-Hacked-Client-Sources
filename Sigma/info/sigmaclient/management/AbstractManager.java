package info.sigmaclient.management;

import java.lang.reflect.Array;
import java.util.Arrays;

import info.sigmaclient.module.Module;
import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractManager<E> {
    private final Class typeClass;
    protected E[] array;

    public AbstractManager(Class<E> typeClass, int size) {
        this.typeClass = typeClass;
        reset(size);
    }

    public abstract void setup();

    /**
     * Set's the item at a given index.
     *
     * @param item
     * @param index
     */
    public void set(E item, int index) {
        array[index] = item;
    }

    /**
     * Returns the object at the given index.
     *
     * @param index
     */
    public E get(int index) {
        return array[index];
    }

    /**
     * Returns the first object that matches the given class.
     */
    public E get(Class clazz) {
        for (E e : array) {
            if (e.getClass().equals(clazz)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Adds an object to the last position in the array.
     */
    public void add(E e) {
        if (e instanceof Module) {
            if (!((Module) e).shouldRegister()) {
                return;
            }
        }
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = e;
    }

    /**
     * Removes an object from the array
     */
    public void remove(E e) {
        array = ArrayUtils.removeElement(array, e);
    }

    /**
     * Resets the array with the given size.
     *
     * @param size
     */
    public void reset(int size) {
        array = (E[]) Array.newInstance(typeClass, size);
    }

    /**
     * Returns the array of objects
     */
    public E[] getArray() {
        return array;
    }
}
