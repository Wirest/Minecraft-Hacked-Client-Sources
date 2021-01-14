package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext.ContextStack;

import java.util.Collection;

public abstract interface ThreadContextStack
        extends ThreadContext.ContextStack, Collection<String> {
}




