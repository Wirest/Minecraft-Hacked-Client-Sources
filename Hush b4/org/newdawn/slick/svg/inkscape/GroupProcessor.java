// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.w3c.dom.Element;

public class GroupProcessor implements ElementProcessor
{
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("g");
    }
    
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        loader.loadChildren(element, transform);
    }
}
