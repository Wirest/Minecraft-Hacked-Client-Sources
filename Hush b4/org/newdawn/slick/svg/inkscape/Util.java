// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.geom.Transform;
import java.util.StringTokenizer;
import org.newdawn.slick.svg.NonGeometricData;
import org.w3c.dom.Element;

public class Util
{
    public static final String INKSCAPE = "http://www.inkscape.org/namespaces/inkscape";
    public static final String SODIPODI = "http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd";
    public static final String XLINK = "http://www.w3.org/1999/xlink";
    
    static NonGeometricData getNonGeometricData(final Element element) {
        final String meta = getMetaData(element);
        final NonGeometricData data = new InkscapeNonGeometricData(meta, element);
        data.addAttribute("id", element.getAttribute("id"));
        data.addAttribute("fill", getStyle(element, "fill"));
        data.addAttribute("stroke", getStyle(element, "stroke"));
        data.addAttribute("opacity", getStyle(element, "opacity"));
        data.addAttribute("stroke-dasharray", getStyle(element, "stroke-dasharray"));
        data.addAttribute("stroke-dashoffset", getStyle(element, "stroke-dashoffset"));
        data.addAttribute("stroke-miterlimit", getStyle(element, "stroke-miterlimit"));
        data.addAttribute("stroke-opacity", getStyle(element, "stroke-opacity"));
        data.addAttribute("stroke-width", getStyle(element, "stroke-width"));
        return data;
    }
    
    static String getMetaData(final Element element) {
        final String label = element.getAttributeNS("http://www.inkscape.org/namespaces/inkscape", "label");
        if (label != null && !label.equals("")) {
            return label;
        }
        return element.getAttribute("id");
    }
    
    static String getStyle(final Element element, final String styleName) {
        final String value = element.getAttribute(styleName);
        if (value != null && value.length() > 0) {
            return value;
        }
        final String style = element.getAttribute("style");
        return extractStyle(style, styleName);
    }
    
    static String extractStyle(final String style, final String attribute) {
        if (style == null) {
            return "";
        }
        final StringTokenizer tokens = new StringTokenizer(style, ";");
        while (tokens.hasMoreTokens()) {
            final String token = tokens.nextToken();
            final String key = token.substring(0, token.indexOf(58));
            if (key.equals(attribute)) {
                return token.substring(token.indexOf(58) + 1);
            }
        }
        return "";
    }
    
    static Transform getTransform(final Element element) {
        return getTransform(element, "transform");
    }
    
    static Transform getTransform(final Element element, final String attribute) {
        String str = element.getAttribute(attribute);
        if (str == null) {
            return new Transform();
        }
        if (str.equals("")) {
            return new Transform();
        }
        if (str.startsWith("translate")) {
            str = str.substring(0, str.length() - 1);
            str = str.substring("translate(".length());
            final StringTokenizer tokens = new StringTokenizer(str, ", ");
            final float x = Float.parseFloat(tokens.nextToken());
            final float y = Float.parseFloat(tokens.nextToken());
            return Transform.createTranslateTransform(x, y);
        }
        if (str.startsWith("matrix")) {
            final float[] pose = new float[6];
            str = str.substring(0, str.length() - 1);
            str = str.substring("matrix(".length());
            final StringTokenizer tokens2 = new StringTokenizer(str, ", ");
            final float[] tr = new float[6];
            for (int j = 0; j < tr.length; ++j) {
                tr[j] = Float.parseFloat(tokens2.nextToken());
            }
            pose[0] = tr[0];
            pose[1] = tr[2];
            pose[2] = tr[4];
            pose[3] = tr[1];
            pose[4] = tr[3];
            pose[5] = tr[5];
            return new Transform(pose);
        }
        return new Transform();
    }
    
    static float getFloatAttribute(final Element element, final String attr) throws ParsingException {
        String cx = element.getAttribute(attr);
        if (cx == null || cx.equals("")) {
            cx = element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", attr);
        }
        try {
            return Float.parseFloat(cx);
        }
        catch (NumberFormatException e) {
            throw new ParsingException(element, "Invalid value for: " + attr, e);
        }
    }
    
    public static String getAsReference(String value) {
        if (value.length() < 2) {
            return "";
        }
        value = value.substring(1, value.length());
        return value;
    }
}
