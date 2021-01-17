// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.NodeList;
import org.newdawn.slick.Color;
import org.newdawn.slick.svg.Gradient;
import java.util.ArrayList;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.w3c.dom.Element;

public class DefsProcessor implements ElementProcessor
{
    @Override
    public boolean handles(final Element element) {
        return element.getNodeName().equals("defs");
    }
    
    @Override
    public void process(final Loader loader, final Element element, final Diagram diagram, final Transform transform) throws ParsingException {
        final NodeList patterns = element.getElementsByTagName("pattern");
        for (int i = 0; i < patterns.getLength(); ++i) {
            final Element pattern = (Element)patterns.item(i);
            final NodeList list = pattern.getElementsByTagName("image");
            if (list.getLength() == 0) {
                Log.warn("Pattern 1981 does not specify an image. Only image patterns are supported.");
            }
            else {
                final Element image = (Element)list.item(0);
                final String patternName = pattern.getAttribute("id");
                final String ref = image.getAttributeNS("http://www.w3.org/1999/xlink", "href");
                diagram.addPatternDef(patternName, ref);
            }
        }
        final NodeList linear = element.getElementsByTagName("linearGradient");
        final ArrayList toResolve = new ArrayList();
        for (int j = 0; j < linear.getLength(); ++j) {
            final Element lin = (Element)linear.item(j);
            final String name = lin.getAttribute("id");
            final Gradient gradient = new Gradient(name, false);
            gradient.setTransform(Util.getTransform(lin, "gradientTransform"));
            if (this.stringLength(lin.getAttribute("x1")) > 0) {
                gradient.setX1(Float.parseFloat(lin.getAttribute("x1")));
            }
            if (this.stringLength(lin.getAttribute("x2")) > 0) {
                gradient.setX2(Float.parseFloat(lin.getAttribute("x2")));
            }
            if (this.stringLength(lin.getAttribute("y1")) > 0) {
                gradient.setY1(Float.parseFloat(lin.getAttribute("y1")));
            }
            if (this.stringLength(lin.getAttribute("y2")) > 0) {
                gradient.setY2(Float.parseFloat(lin.getAttribute("y2")));
            }
            final String ref2 = lin.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            if (this.stringLength(ref2) > 0) {
                gradient.reference(ref2.substring(1));
                toResolve.add(gradient);
            }
            else {
                final NodeList steps = lin.getElementsByTagName("stop");
                for (int k = 0; k < steps.getLength(); ++k) {
                    final Element s = (Element)steps.item(k);
                    final float offset = Float.parseFloat(s.getAttribute("offset"));
                    final String colInt = Util.extractStyle(s.getAttribute("style"), "stop-color");
                    final String opaInt = Util.extractStyle(s.getAttribute("style"), "stop-opacity");
                    final int col = Integer.parseInt(colInt.substring(1), 16);
                    final Color stopColor = new Color(col);
                    stopColor.a = Float.parseFloat(opaInt);
                    gradient.addStep(offset, stopColor);
                }
                gradient.getImage();
            }
            diagram.addGradient(name, gradient);
        }
        final NodeList radial = element.getElementsByTagName("radialGradient");
        for (int l = 0; l < radial.getLength(); ++l) {
            final Element rad = (Element)radial.item(l);
            final String name2 = rad.getAttribute("id");
            final Gradient gradient2 = new Gradient(name2, true);
            gradient2.setTransform(Util.getTransform(rad, "gradientTransform"));
            if (this.stringLength(rad.getAttribute("cx")) > 0) {
                gradient2.setX1(Float.parseFloat(rad.getAttribute("cx")));
            }
            if (this.stringLength(rad.getAttribute("cy")) > 0) {
                gradient2.setY1(Float.parseFloat(rad.getAttribute("cy")));
            }
            if (this.stringLength(rad.getAttribute("fx")) > 0) {
                gradient2.setX2(Float.parseFloat(rad.getAttribute("fx")));
            }
            if (this.stringLength(rad.getAttribute("fy")) > 0) {
                gradient2.setY2(Float.parseFloat(rad.getAttribute("fy")));
            }
            if (this.stringLength(rad.getAttribute("r")) > 0) {
                gradient2.setR(Float.parseFloat(rad.getAttribute("r")));
            }
            final String ref3 = rad.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            if (this.stringLength(ref3) > 0) {
                gradient2.reference(ref3.substring(1));
                toResolve.add(gradient2);
            }
            else {
                final NodeList steps2 = rad.getElementsByTagName("stop");
                for (int m = 0; m < steps2.getLength(); ++m) {
                    final Element s2 = (Element)steps2.item(m);
                    final float offset2 = Float.parseFloat(s2.getAttribute("offset"));
                    final String colInt2 = Util.extractStyle(s2.getAttribute("style"), "stop-color");
                    final String opaInt2 = Util.extractStyle(s2.getAttribute("style"), "stop-opacity");
                    final int col2 = Integer.parseInt(colInt2.substring(1), 16);
                    final Color stopColor2 = new Color(col2);
                    stopColor2.a = Float.parseFloat(opaInt2);
                    gradient2.addStep(offset2, stopColor2);
                }
                gradient2.getImage();
            }
            diagram.addGradient(name2, gradient2);
        }
        for (int l = 0; l < toResolve.size(); ++l) {
            toResolve.get(l).resolve(diagram);
            toResolve.get(l).getImage();
        }
    }
    
    private int stringLength(final String value) {
        if (value == null) {
            return 0;
        }
        return value.length();
    }
}
