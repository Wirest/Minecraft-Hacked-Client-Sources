// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.xml;

import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;

public class XMLElement
{
    private Element dom;
    private XMLElementList children;
    private String name;
    
    XMLElement(final Element xmlElement) {
        this.dom = xmlElement;
        this.name = this.dom.getTagName();
    }
    
    public String[] getAttributeNames() {
        final NamedNodeMap map = this.dom.getAttributes();
        final String[] names = new String[map.getLength()];
        for (int i = 0; i < names.length; ++i) {
            names[i] = map.item(i).getNodeName();
        }
        return names;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAttribute(final String name) {
        return this.dom.getAttribute(name);
    }
    
    public String getAttribute(final String name, final String def) {
        final String value = this.dom.getAttribute(name);
        if (value == null || value.length() == 0) {
            return def;
        }
        return value;
    }
    
    public int getIntAttribute(final String name) throws SlickXMLException {
        try {
            return Integer.parseInt(this.getAttribute(name));
        }
        catch (NumberFormatException e) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not an integer", e);
        }
    }
    
    public int getIntAttribute(final String name, final int def) throws SlickXMLException {
        try {
            return Integer.parseInt(this.getAttribute(name, new StringBuilder().append(def).toString()));
        }
        catch (NumberFormatException e) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name, new StringBuilder().append(def).toString()) + "' is not an integer", e);
        }
    }
    
    public double getDoubleAttribute(final String name) throws SlickXMLException {
        try {
            return Double.parseDouble(this.getAttribute(name));
        }
        catch (NumberFormatException e) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not a double", e);
        }
    }
    
    public double getDoubleAttribute(final String name, final double def) throws SlickXMLException {
        try {
            return Double.parseDouble(this.getAttribute(name, new StringBuilder().append(def).toString()));
        }
        catch (NumberFormatException e) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name, new StringBuilder().append(def).toString()) + "' is not a double", e);
        }
    }
    
    public boolean getBooleanAttribute(final String name) throws SlickXMLException {
        final String value = this.getAttribute(name);
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not a boolean");
    }
    
    public boolean getBooleanAttribute(final String name, final boolean def) throws SlickXMLException {
        final String value = this.getAttribute(name, new StringBuilder().append(def).toString());
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new SlickXMLException("Value read: '" + this.getAttribute(name, new StringBuilder().append(def).toString()) + "' is not a boolean");
    }
    
    public String getContent() {
        String content = "";
        final NodeList list = this.dom.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            if (list.item(i) instanceof Text) {
                content = String.valueOf(content) + list.item(i).getNodeValue();
            }
        }
        return content;
    }
    
    public XMLElementList getChildren() {
        if (this.children != null) {
            return this.children;
        }
        final NodeList list = this.dom.getChildNodes();
        this.children = new XMLElementList();
        for (int i = 0; i < list.getLength(); ++i) {
            if (list.item(i) instanceof Element) {
                this.children.add(new XMLElement((Element)list.item(i)));
            }
        }
        return this.children;
    }
    
    public XMLElementList getChildrenByName(final String name) {
        final XMLElementList selected = new XMLElementList();
        final XMLElementList children = this.getChildren();
        for (int i = 0; i < children.size(); ++i) {
            if (children.get(i).getName().equals(name)) {
                selected.add(children.get(i));
            }
        }
        return selected;
    }
    
    @Override
    public String toString() {
        String value = "[XML " + this.getName();
        final String[] attrs = this.getAttributeNames();
        for (int i = 0; i < attrs.length; ++i) {
            value = String.valueOf(value) + " " + attrs[i] + "=" + this.getAttribute(attrs[i]);
        }
        value = String.valueOf(value) + "]";
        return value;
    }
}
