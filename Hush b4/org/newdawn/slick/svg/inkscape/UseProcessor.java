// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.w3c.dom.Element;

public class UseProcessor implements ElementProcessor
{
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("use");
    }
    
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform transform) throws ParsingException {
        final String ref = element.getAttributeNS("http://www.w3.org/1999/xlink", "href");
        final String href = Util.getAsReference(ref);
        final Figure referenced = diagram.getFigureByID(href);
        if (referenced == null) {
            throw new ParsingException(element, "Unable to locate referenced element: " + href);
        }
        final Transform local = Util.getTransform(element);
        final Transform trans = local.concatenate(referenced.getTransform());
        final NonGeometricData data = Util.getNonGeometricData(element);
        final Shape shape = referenced.getShape().transform(trans);
        data.addAttribute("fill", referenced.getData().getAttribute("fill"));
        data.addAttribute("stroke", referenced.getData().getAttribute("stroke"));
        data.addAttribute("opacity", referenced.getData().getAttribute("opacity"));
        data.addAttribute("stroke-width", referenced.getData().getAttribute("stroke-width"));
        final Figure figure = new Figure(referenced.getType(), shape, data, trans);
        diagram.addFigure(figure);
    }
}
