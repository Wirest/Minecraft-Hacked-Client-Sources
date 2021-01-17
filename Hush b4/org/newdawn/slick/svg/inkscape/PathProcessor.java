// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import java.util.ArrayList;
import org.newdawn.slick.geom.Path;
import java.util.StringTokenizer;
import org.w3c.dom.Element;

public class PathProcessor implements ElementProcessor
{
    private static Path processPoly(final Element element, final StringTokenizer tokens) throws ParsingException {
        final int count = 0;
        final ArrayList pts = new ArrayList();
        boolean moved = false;
        boolean reasonToBePath = false;
        Path path = null;
        while (tokens.hasMoreTokens()) {
            try {
                final String nextToken = tokens.nextToken();
                if (nextToken.equals("L")) {
                    final float x = Float.parseFloat(tokens.nextToken());
                    final float y = Float.parseFloat(tokens.nextToken());
                    path.lineTo(x, y);
                }
                else if (nextToken.equals("z")) {
                    path.close();
                }
                else if (nextToken.equals("M")) {
                    if (!moved) {
                        moved = true;
                        final float x = Float.parseFloat(tokens.nextToken());
                        final float y = Float.parseFloat(tokens.nextToken());
                        path = new Path(x, y);
                    }
                    else {
                        reasonToBePath = true;
                        final float x = Float.parseFloat(tokens.nextToken());
                        final float y = Float.parseFloat(tokens.nextToken());
                        path.startHole(x, y);
                    }
                }
                else {
                    if (!nextToken.equals("C")) {
                        continue;
                    }
                    reasonToBePath = true;
                    final float cx1 = Float.parseFloat(tokens.nextToken());
                    final float cy1 = Float.parseFloat(tokens.nextToken());
                    final float cx2 = Float.parseFloat(tokens.nextToken());
                    final float cy2 = Float.parseFloat(tokens.nextToken());
                    final float x2 = Float.parseFloat(tokens.nextToken());
                    final float y2 = Float.parseFloat(tokens.nextToken());
                    path.curveTo(x2, y2, cx1, cy1, cx2, cy2);
                }
            }
            catch (NumberFormatException e) {
                throw new ParsingException(element.getAttribute("id"), "Invalid token in points list", e);
            }
        }
        if (!reasonToBePath) {
            return null;
        }
        return path;
    }
    
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        String points = element.getAttribute("points");
        if (element.getNodeName().equals("path")) {
            points = element.getAttribute("d");
        }
        final StringTokenizer tokens = new StringTokenizer(points, ", ");
        final Path path = processPoly(element, tokens);
        final NonGeometricData data = Util.getNonGeometricData(element);
        if (path != null) {
            final Shape shape = path.transform(transform);
            diagram.addFigure(new Figure(4, shape, data, transform));
        }
    }
    
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("path") && !"arc".equals(element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
    }
}
