// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.w3c.dom.Element;
import org.newdawn.slick.svg.Loader;

public class RectProcessor implements ElementProcessor
{
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        final float width = Float.parseFloat(element.getAttribute("width"));
        final float height = Float.parseFloat(element.getAttribute("height"));
        final float x = Float.parseFloat(element.getAttribute("x"));
        final float y = Float.parseFloat(element.getAttribute("y"));
        final Rectangle rect = new Rectangle(x, y, width + 1.0f, height + 1.0f);
        final Shape shape = rect.transform(transform);
        final NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("width", new StringBuilder().append(width).toString());
        data.addAttribute("height", new StringBuilder().append(height).toString());
        data.addAttribute("x", new StringBuilder().append(x).toString());
        data.addAttribute("y", new StringBuilder().append(y).toString());
        diagram.addFigure(new Figure(3, shape, data, transform));
    }
    
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("rect");
    }
}
