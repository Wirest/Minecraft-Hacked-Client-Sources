// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import java.io.Serializable;

public class PluginType<T> implements Serializable
{
    private static final long serialVersionUID = 4743255148794846612L;
    private final Class<T> pluginClass;
    private final String elementName;
    private final boolean printObject;
    private final boolean deferChildren;
    
    public PluginType(final Class<T> clazz, final String name, final boolean printObj, final boolean deferChildren) {
        this.pluginClass = clazz;
        this.elementName = name;
        this.printObject = printObj;
        this.deferChildren = deferChildren;
    }
    
    public Class<T> getPluginClass() {
        return this.pluginClass;
    }
    
    public String getElementName() {
        return this.elementName;
    }
    
    public boolean isObjectPrintable() {
        return this.printObject;
    }
    
    public boolean isDeferChildren() {
        return this.deferChildren;
    }
}
