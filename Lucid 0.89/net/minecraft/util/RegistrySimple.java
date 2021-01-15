package net.minecraft.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

public class RegistrySimple implements IRegistry
{
    private static final Logger logger = LogManager.getLogger();

    /** Objects registered on this registry. */
    protected final Map registryObjects = this.createUnderlyingMap();

    /**
     * Creates the Map we will use to map keys to their registered values.
     */
    protected Map createUnderlyingMap()
    {
        return Maps.newHashMap();
    }

    @Override
	public Object getObject(Object name)
    {
        return this.registryObjects.get(name);
    }

    /**
     * Register an object on this registry.
     */
    @Override
	public void putObject(Object p_82595_1_, Object p_82595_2_)
    {
        Validate.notNull(p_82595_1_);
        Validate.notNull(p_82595_2_);

        if (this.registryObjects.containsKey(p_82595_1_))
        {
            logger.debug("Adding duplicate key \'" + p_82595_1_ + "\' to registry");
        }

        this.registryObjects.put(p_82595_1_, p_82595_2_);
    }

    /**
     * Gets all the keys recognized by this registry.
     */
    public Set getKeys()
    {
        return Collections.unmodifiableSet(this.registryObjects.keySet());
    }

    /**
     * Does this registry contain an entry for the given key?
     */
    public boolean containsKey(Object p_148741_1_)
    {
        return this.registryObjects.containsKey(p_148741_1_);
    }

    @Override
	public Iterator iterator()
    {
        return this.registryObjects.values().iterator();
    }
}
