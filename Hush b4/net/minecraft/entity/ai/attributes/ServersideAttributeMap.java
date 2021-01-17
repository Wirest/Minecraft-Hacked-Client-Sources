// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.management.LowerStringMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class ServersideAttributeMap extends BaseAttributeMap
{
    private final Set<IAttributeInstance> attributeInstanceSet;
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap;
    
    public ServersideAttributeMap() {
        this.attributeInstanceSet = (Set<IAttributeInstance>)Sets.newHashSet();
        this.descriptionToAttributeInstanceMap = new LowerStringMap<IAttributeInstance>();
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstance(final IAttribute attribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstanceByName(final String attributeName) {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
        if (iattributeinstance == null) {
            iattributeinstance = this.descriptionToAttributeInstanceMap.get(attributeName);
        }
        return (ModifiableAttributeInstance)iattributeinstance;
    }
    
    @Override
    public IAttributeInstance registerAttribute(final IAttribute attribute) {
        final IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
        if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
        }
        return iattributeinstance;
    }
    
    @Override
    protected IAttributeInstance func_180376_c(final IAttribute p_180376_1_) {
        return new ModifiableAttributeInstance(this, p_180376_1_);
    }
    
    @Override
    public void func_180794_a(final IAttributeInstance p_180794_1_) {
        if (p_180794_1_.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(p_180794_1_);
        }
        for (final IAttribute iattribute : this.field_180377_c.get(p_180794_1_.getAttribute())) {
            final ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.flagForUpdate();
            }
        }
    }
    
    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }
    
    public Collection<IAttributeInstance> getWatchedAttributes() {
        final Set<IAttributeInstance> set = (Set<IAttributeInstance>)Sets.newHashSet();
        for (final IAttributeInstance iattributeinstance : this.getAllAttributes()) {
            if (iattributeinstance.getAttribute().getShouldWatch()) {
                set.add(iattributeinstance);
            }
        }
        return set;
    }
}
