// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;

public interface LogEventFactory
{
    LogEvent createEvent(final String p0, final Marker p1, final String p2, final Level p3, final Message p4, final List<Property> p5, final Throwable p6);
}
