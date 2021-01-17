// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.Color;
import java.util.Properties;

public class NonGeometricData
{
    public static final String ID = "id";
    public static final String FILL = "fill";
    public static final String STROKE = "stroke";
    public static final String OPACITY = "opacity";
    public static final String STROKE_WIDTH = "stroke-width";
    public static final String STROKE_MITERLIMIT = "stroke-miterlimit";
    public static final String STROKE_DASHARRAY = "stroke-dasharray";
    public static final String STROKE_DASHOFFSET = "stroke-dashoffset";
    public static final String STROKE_OPACITY = "stroke-opacity";
    public static final String NONE = "none";
    private String metaData;
    private Properties props;
    
    public NonGeometricData(final String metaData) {
        this.metaData = "";
        this.props = new Properties();
        this.metaData = metaData;
        this.addAttribute("stroke-width", "1");
    }
    
    private String morphColor(final String str) {
        if (str.equals("")) {
            return "#000000";
        }
        if (str.equals("white")) {
            return "#ffffff";
        }
        if (str.equals("black")) {
            return "#000000";
        }
        return str;
    }
    
    public void addAttribute(final String attribute, String value) {
        if (value == null) {
            value = "";
        }
        if (attribute.equals("fill")) {
            value = this.morphColor(value);
        }
        if (attribute.equals("stroke-opacity") && value.equals("0")) {
            this.props.setProperty("stroke", "none");
        }
        if (attribute.equals("stroke-width")) {
            if (value.equals("")) {
                value = "1";
            }
            if (value.endsWith("px")) {
                value = value.substring(0, value.length() - 2);
            }
        }
        if (attribute.equals("stroke")) {
            if ("none".equals(this.props.getProperty("stroke"))) {
                return;
            }
            if ("".equals(this.props.getProperty("stroke"))) {
                return;
            }
            value = this.morphColor(value);
        }
        this.props.setProperty(attribute, value);
    }
    
    public boolean isColor(final String attribute) {
        return this.getAttribute(attribute).startsWith("#");
    }
    
    public String getMetaData() {
        return this.metaData;
    }
    
    public String getAttribute(final String attribute) {
        return this.props.getProperty(attribute);
    }
    
    public Color getAsColor(final String attribute) {
        if (!this.isColor(attribute)) {
            throw new RuntimeException("Attribute " + attribute + " is not specified as a color:" + this.getAttribute(attribute));
        }
        final int col = Integer.parseInt(this.getAttribute(attribute).substring(1), 16);
        return new Color(col);
    }
    
    public String getAsReference(final String attribute) {
        String value = this.getAttribute(attribute);
        if (value.length() < 7) {
            return "";
        }
        value = value.substring(5, value.length() - 1);
        return value;
    }
    
    public float getAsFloat(final String attribute) {
        final String value = this.getAttribute(attribute);
        if (value == null) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("Attribute " + attribute + " is not specified as a float:" + this.getAttribute(attribute));
        }
    }
    
    public boolean isFilled() {
        return this.isColor("fill");
    }
    
    public boolean isStroked() {
        return this.isColor("stroke") && this.getAsFloat("stroke-width") > 0.0f;
    }
}
