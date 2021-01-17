// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.w3c.dom.Element;
import org.newdawn.slick.svg.Loader;

public interface ElementProcessor
{
    void process(final Loader p0, final Element p1, final Diagram p2, final Transform p3) throws ParsingException;
    
    boolean handles(final Element p0);
}
