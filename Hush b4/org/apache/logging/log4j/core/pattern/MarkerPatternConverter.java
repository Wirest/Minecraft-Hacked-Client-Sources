// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MarkerPatternConverter", category = "Converter")
@ConverterKeys({ "marker" })
public final class MarkerPatternConverter extends LogEventPatternConverter
{
    private MarkerPatternConverter(final String[] options) {
        super("Marker", "marker");
    }
    
    public static MarkerPatternConverter newInstance(final String[] options) {
        return new MarkerPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final Marker marker = event.getMarker();
        if (marker != null) {
            toAppendTo.append(marker.toString());
        }
    }
}
