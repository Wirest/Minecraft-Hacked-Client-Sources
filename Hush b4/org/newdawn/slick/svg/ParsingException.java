// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.w3c.dom.Element;
import org.newdawn.slick.SlickException;

public class ParsingException extends SlickException
{
    public ParsingException(final String nodeID, final String message, final Throwable cause) {
        super("(" + nodeID + ") " + message, cause);
    }
    
    public ParsingException(final Element element, final String message, final Throwable cause) {
        super("(" + element.getAttribute("id") + ") " + message, cause);
    }
    
    public ParsingException(final String nodeID, final String message) {
        super("(" + nodeID + ") " + message);
    }
    
    public ParsingException(final Element element, final String message) {
        super("(" + element.getAttribute("id") + ") " + message);
    }
}
