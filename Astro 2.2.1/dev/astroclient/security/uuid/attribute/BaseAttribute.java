package dev.astroclient.security.uuid.attribute;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Supplier;

public class BaseAttribute<T> implements Attribute<T> {

    private Supplier<T> supplier;

    protected String id;
    protected T value;

    public BaseAttribute(String id, T value) {
        this.id = id;
        this.value = value;
    }

    public BaseAttribute(String id, Supplier<T> supplier) {
        this(id, supplier.get());
        this.supplier = supplier;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getKey() {
        return this.id.hashCode() + this.value.hashCode();
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void updateValue() {
        if (this.supplier != null) {
            this.value = supplier.get();
        }
    }
}
