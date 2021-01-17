// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.List;
import java.util.Collection;

public interface OptionDescriptor
{
    Collection<String> options();
    
    String description();
    
    List<?> defaultValues();
    
    boolean isRequired();
    
    boolean acceptsArguments();
    
    boolean requiresArgument();
    
    String argumentDescription();
    
    String argumentTypeIndicator();
    
    boolean representsNonOptions();
}
