// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Collection;
import java.util.TreeSet;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MapPatternConverter", category = "Converter")
@ConverterKeys({ "K", "map", "MAP" })
public final class MapPatternConverter extends LogEventPatternConverter
{
    private final String key;
    
    private MapPatternConverter(final String[] options) {
        super((options != null && options.length > 0) ? ("MAP{" + options[0] + "}") : "MAP", "map");
        this.key = ((options != null && options.length > 0) ? options[0] : null);
    }
    
    public static MapPatternConverter newInstance(final String[] options) {
        return new MapPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        if (event.getMessage() instanceof MapMessage) {
            final MapMessage msg = (MapMessage)event.getMessage();
            final Map<String, String> map = msg.getData();
            if (this.key == null) {
                if (map.size() == 0) {
                    toAppendTo.append("{}");
                    return;
                }
                final StringBuilder sb = new StringBuilder("{");
                final Set<String> keys = new TreeSet<String>(map.keySet());
                for (final String key : keys) {
                    if (sb.length() > 1) {
                        sb.append(", ");
                    }
                    sb.append(key).append("=").append(map.get(key));
                }
                sb.append("}");
                toAppendTo.append((CharSequence)sb);
            }
            else {
                final String val = map.get(this.key);
                if (val != null) {
                    toAppendTo.append(val);
                }
            }
        }
    }
}
