// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.w3c.dom.Element;
import org.newdawn.slick.svg.Loader;

public class EllipseProcessor implements ElementProcessor
{
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        final float x = Util.getFloatAttribute(element, "cx");
        final float y = Util.getFloatAttribute(element, "cy");
        final float rx = Util.getFloatAttribute(element, "rx");
        final float ry = Util.getFloatAttribute(element, "ry");
        final Ellipse ellipse = new Ellipse(x, y, rx, ry);
        final Shape shape = ellipse.transform(transform);
        final NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("cx", new StringBuilder().append(x).toString());
        data.addAttribute("cy", new StringBuilder().append(y).toString());
        data.addAttribute("rx", new StringBuilder().append(rx).toString());
        data.addAttribute("ry", new StringBuilder().append(ry).toString());
        diagram.addFigure(new Figure(1, shape, data, transform));
    }
    
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("ellipse") || (element.getNodeName().equals("path") && "arc".equals(element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type")));
    }
}
