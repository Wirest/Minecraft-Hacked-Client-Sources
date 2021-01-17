// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.selector.ContextSelector;
import javax.management.ObjectName;

public class ContextSelectorAdmin implements ContextSelectorAdminMBean
{
    private final ObjectName objectName;
    private final ContextSelector selector;
    
    public ContextSelectorAdmin(final ContextSelector selector) {
        this.selector = Assert.isNotNull(selector, "ContextSelector");
        try {
            this.objectName = new ObjectName("org.apache.logging.log4j2:type=ContextSelector");
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    @Override
    public String getImplementationClassName() {
        return this.selector.getClass().getName();
    }
}
