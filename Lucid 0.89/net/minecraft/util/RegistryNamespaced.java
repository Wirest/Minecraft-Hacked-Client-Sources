package net.minecraft.util;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class RegistryNamespaced extends RegistrySimple implements IObjectIntIterable
{
    /** The backing store that maps Integers to objects. */
    protected final ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();

    /** A BiMap of objects (key) to their names (value). */
    protected final Map inverseObjectRegistry;

    public RegistryNamespaced()
    {
        this.inverseObjectRegistry = ((BiMap)this.registryObjects).inverse();
    }

    public void register(int id, Object p_177775_2_, Object p_177775_3_)
    {
        this.underlyingIntegerMap.put(p_177775_3_, id);
        this.putObject(p_177775_2_, p_177775_3_);
    }

    /**
     * Creates the Map we will use to map keys to their registered values.
     */
    @Override
	protected Map createUnderlyingMap()
    {
        return HashBiMap.create();
    }

    @Override
	public Object getObject(Object name)
    {
        return super.getObject(name);
    }

    /**
     * Gets the name we use to identify the given object.
     */
    public Object getNameForObject(Object p_177774_1_)
    {
        return this.inverseObjectRegistry.get(p_177774_1_);
    }

    /**
     * Does this registry contain an entry for the given key?
     */
    @Override
	public boolean containsKey(Object p_148741_1_)
    {
        return super.containsKey(p_148741_1_);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getIDForObject(Object p_148757_1_)
    {
        return this.underlyingIntegerMap.get(p_148757_1_);
    }

    /**
     * Gets the object identified by the given ID.
     *  
     * @param id The id to fetch from the registry
     */
    public Object getObjectById(int id)
    {
        return this.underlyingIntegerMap.getByValue(id);
    }

    @Override
	public Iterator iterator()
    {
        return this.underlyingIntegerMap.iterator();
    }
}
