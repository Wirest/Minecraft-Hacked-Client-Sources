// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Policies", category = "Core", printObject = true)
public final class CompositeTriggeringPolicy implements TriggeringPolicy
{
    private final TriggeringPolicy[] policies;
    
    private CompositeTriggeringPolicy(final TriggeringPolicy... policies) {
        this.policies = policies;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        for (final TriggeringPolicy policy : this.policies) {
            policy.initialize(manager);
        }
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        for (final TriggeringPolicy policy : this.policies) {
            if (policy.isTriggeringEvent(event)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompositeTriggeringPolicy{");
        boolean first = true;
        for (final TriggeringPolicy policy : this.policies) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(policy.toString());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    @PluginFactory
    public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") final TriggeringPolicy... policies) {
        return new CompositeTriggeringPolicy(policies);
    }
}
