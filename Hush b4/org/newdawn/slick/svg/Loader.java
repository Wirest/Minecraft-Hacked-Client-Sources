// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Transform;
import org.w3c.dom.Element;

public interface Loader
{
    void loadChildren(final Element p0, final Transform p1) throws ParsingException;
}
