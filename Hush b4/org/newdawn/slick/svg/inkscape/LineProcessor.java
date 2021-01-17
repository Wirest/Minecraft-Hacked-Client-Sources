// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import java.util.StringTokenizer;
import org.w3c.dom.Element;
import org.newdawn.slick.geom.Polygon;

public class LineProcessor implements ElementProcessor
{
    private static int processPoly(final Polygon poly, final Element element, final StringTokenizer tokens) throws ParsingException {
        int count = 0;
        while (tokens.hasMoreTokens()) {
            final String nextToken = tokens.nextToken();
            if (nextToken.equals("L")) {
                continue;
            }
            if (nextToken.equals("z")) {
                break;
            }
            if (nextToken.equals("M")) {
                continue;
            }
            if (nextToken.equals("C")) {
                return 0;
            }
            final String tokenX = nextToken;
            final String tokenY = tokens.nextToken();
            try {
                final float x = Float.parseFloat(tokenX);
                final float y = Float.parseFloat(tokenY);
                poly.addPoint(x, y);
                ++count;
            }
            catch (NumberFormatException e) {
                throw new ParsingException(element.getAttribute("id"), "Invalid token in points list", e);
            }
        }
        return count;
    }
    
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        float x1;
        float x2;
        float y1;
        float y2;
        if (element.getNodeName().equals("line")) {
            x1 = Float.parseFloat(element.getAttribute("x1"));
            x2 = Float.parseFloat(element.getAttribute("x2"));
            y1 = Float.parseFloat(element.getAttribute("y1"));
            y2 = Float.parseFloat(element.getAttribute("y2"));
        }
        else {
            final String points = element.getAttribute("d");
            final StringTokenizer tokens = new StringTokenizer(points, ", ");
            final Polygon poly = new Polygon();
            if (processPoly(poly, element, tokens) != 2) {
                return;
            }
            x1 = poly.getPoint(0)[0];
            y1 = poly.getPoint(0)[1];
            x2 = poly.getPoint(1)[0];
            y2 = poly.getPoint(1)[1];
        }
        final float[] in = { x1, y1, x2, y2 };
        final float[] out = new float[4];
        transform.transform(in, 0, out, 0, 2);
        final Line line = new Line(out[0], out[1], out[2], out[3]);
        final NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("x1", new StringBuilder().append(x1).toString());
        data.addAttribute("x2", new StringBuilder().append(x2).toString());
        data.addAttribute("y1", new StringBuilder().append(y1).toString());
        data.addAttribute("y2", new StringBuilder().append(y2).toString());
        diagram.addFigure(new Figure(2, line, data, transform));
    }
    
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("line") || (element.getNodeName().equals("path") && !"arc".equals(element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type")));
    }
}
