package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap extends BaseAttributeMap {
    private final Set attributeInstanceSet = Sets.newHashSet();
    protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();
    private static final String __OBFID = "CL_00001569";

    public ModifiableAttributeInstance func_180795_e(IAttribute p_180795_1_) {
        return (ModifiableAttributeInstance) super.getAttributeInstance(p_180795_1_);
    }

    public ModifiableAttributeInstance func_180796_b(String p_180796_1_) {
        IAttributeInstance var2 = super.getAttributeInstanceByName(p_180796_1_);

        if (var2 == null) {
            var2 = (IAttributeInstance) this.descriptionToAttributeInstanceMap.get(p_180796_1_);
        }

        return (ModifiableAttributeInstance) var2;
    }

    /**
     * Registers an attribute with this AttributeMap, returns a modifiable AttributeInstance associated with this map
     */
    public IAttributeInstance registerAttribute(IAttribute p_111150_1_) {
        IAttributeInstance var2 = super.registerAttribute(p_111150_1_);

        if (p_111150_1_ instanceof RangedAttribute && ((RangedAttribute) p_111150_1_).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute) p_111150_1_).getDescription(), var2);
        }

        return var2;
    }

    protected IAttributeInstance func_180376_c(IAttribute p_180376_1_) {
        return new ModifiableAttributeInstance(this, p_180376_1_);
    }

    public void func_180794_a(IAttributeInstance p_180794_1_) {
        if (p_180794_1_.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(p_180794_1_);
        }

        Iterator var2 = this.field_180377_c.get(p_180794_1_.getAttribute()).iterator();

        while (var2.hasNext()) {
            IAttribute var3 = (IAttribute) var2.next();
            ModifiableAttributeInstance var4 = this.func_180795_e(var3);

            if (var4 != null) {
                var4.flagForUpdate();
            }
        }
    }

    public Set getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }

    public Collection getWatchedAttributes() {
        HashSet var1 = Sets.newHashSet();
        Iterator var2 = this.getAllAttributes().iterator();

        while (var2.hasNext()) {
            IAttributeInstance var3 = (IAttributeInstance) var2.next();

            if (var3.getAttribute().getShouldWatch()) {
                var1.add(var3);
            }
        }

        return var1;
    }

    public IAttributeInstance getAttributeInstanceByName(String p_111152_1_) {
        return this.func_180796_b(p_111152_1_);
    }

    public IAttributeInstance getAttributeInstance(IAttribute p_111151_1_) {
        return this.func_180795_e(p_111151_1_);
    }
}
