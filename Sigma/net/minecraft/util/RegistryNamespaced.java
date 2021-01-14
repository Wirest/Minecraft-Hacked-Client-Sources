package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Map;

public class RegistryNamespaced extends RegistrySimple implements IObjectIntIterable {
    /**
     * The backing store that maps Integers to objects.
     */
    protected final ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();
    protected final Map field_148758_b;
    private static final String __OBFID = "CL_00001206";

    public RegistryNamespaced() {
        field_148758_b = ((BiMap) registryObjects).inverse();
    }

    public void register(int p_177775_1_, Object p_177775_2_, Object p_177775_3_) {
        underlyingIntegerMap.put(p_177775_3_, p_177775_1_);
        putObject(p_177775_2_, p_177775_3_);
    }

    /**
     * Creates the Map we will use to map keys to their registered values.
     */
    @Override
    protected Map createUnderlyingMap() {
        return HashBiMap.create();
    }

    @Override
    public Object getObject(Object p_82594_1_) {
        return super.getObject(p_82594_1_);
    }

    /**
     * Gets the name we use to identify the given object.
     */
    public Object getNameForObject(Object p_177774_1_) {
        return field_148758_b.get(p_177774_1_);
    }

    /**
     * Does this registry contain an entry for the given key?
     */
    @Override
    public boolean containsKey(Object p_148741_1_) {
        return super.containsKey(p_148741_1_);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getIDForObject(Object p_148757_1_) {
        return underlyingIntegerMap.get(p_148757_1_);
    }

    /**
     * Gets the object identified by the given ID.
     */
    public Object getObjectById(int p_148754_1_) {
        return underlyingIntegerMap.getByValue(p_148754_1_);
    }

    @Override
    public Iterator iterator() {
        return underlyingIntegerMap.iterator();
    }
}
