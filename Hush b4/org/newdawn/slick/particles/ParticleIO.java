// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.particles;

import org.newdawn.slick.geom.Vector2f;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStreamWriter;
import org.w3c.dom.Node;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Color;

public class ParticleIO
{
    public static ParticleSystem loadConfiguredSystem(final String ref, final Color mask) throws IOException {
        return loadConfiguredSystem(ResourceLoader.getResourceAsStream(ref), null, null, mask);
    }
    
    public static ParticleSystem loadConfiguredSystem(final String ref) throws IOException {
        return loadConfiguredSystem(ResourceLoader.getResourceAsStream(ref), null, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final File ref) throws IOException {
        return loadConfiguredSystem(new FileInputStream(ref), null, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final InputStream ref, final Color mask) throws IOException {
        return loadConfiguredSystem(ref, null, null, mask);
    }
    
    public static ParticleSystem loadConfiguredSystem(final InputStream ref) throws IOException {
        return loadConfiguredSystem(ref, null, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final String ref, final ConfigurableEmitterFactory factory) throws IOException {
        return loadConfiguredSystem(ResourceLoader.getResourceAsStream(ref), factory, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final File ref, final ConfigurableEmitterFactory factory) throws IOException {
        return loadConfiguredSystem(new FileInputStream(ref), factory, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final InputStream ref, final ConfigurableEmitterFactory factory) throws IOException {
        return loadConfiguredSystem(ref, factory, null, null);
    }
    
    public static ParticleSystem loadConfiguredSystem(final InputStream ref, ConfigurableEmitterFactory factory, ParticleSystem system, final Color mask) throws IOException {
        if (factory == null) {
            factory = new ConfigurableEmitterFactory() {
                @Override
                public ConfigurableEmitter createEmitter(final String name) {
                    return new ConfigurableEmitter(name);
                }
            };
        }
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.parse(ref);
            final Element element = document.getDocumentElement();
            if (!element.getNodeName().equals("system")) {
                throw new IOException("Not a particle system file");
            }
            if (system == null) {
                system = new ParticleSystem("org/newdawn/slick/data/particle.tga", 2000, mask);
            }
            final boolean additive = "true".equals(element.getAttribute("additive"));
            if (additive) {
                system.setBlendingMode(1);
            }
            else {
                system.setBlendingMode(2);
            }
            final boolean points = "true".equals(element.getAttribute("points"));
            system.setUsePoints(points);
            final NodeList list = element.getElementsByTagName("emitter");
            for (int i = 0; i < list.getLength(); ++i) {
                final Element em = (Element)list.item(i);
                final ConfigurableEmitter emitter = factory.createEmitter("new");
                elementToEmitter(em, emitter);
                system.addEmitter(emitter);
            }
            system.setRemoveCompletedEmitters(false);
            return system;
        }
        catch (IOException e) {
            Log.error(e);
            throw e;
        }
        catch (Exception e2) {
            Log.error(e2);
            throw new IOException("Unable to load particle system config");
        }
    }
    
    public static void saveConfiguredSystem(final File file, final ParticleSystem system) throws IOException {
        saveConfiguredSystem(new FileOutputStream(file), system);
    }
    
    public static void saveConfiguredSystem(final OutputStream out, final ParticleSystem system) throws IOException {
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.newDocument();
            final Element root = document.createElement("system");
            root.setAttribute("additive", new StringBuilder().append(system.getBlendingMode() == 1).toString());
            root.setAttribute("points", new StringBuilder().append(system.usePoints()).toString());
            document.appendChild(root);
            for (int i = 0; i < system.getEmitterCount(); ++i) {
                final ParticleEmitter current = system.getEmitter(i);
                if (!(current instanceof ConfigurableEmitter)) {
                    throw new RuntimeException("Only ConfigurableEmitter instances can be stored");
                }
                final Element element = emitterToElement(document, (ConfigurableEmitter)current);
                root.appendChild(element);
            }
            final Result result = new StreamResult(new OutputStreamWriter(out, "utf-8"));
            final DOMSource source = new DOMSource(document);
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer xformer = factory.newTransformer();
            xformer.setOutputProperty("indent", "yes");
            xformer.transform(source, result);
        }
        catch (Exception e) {
            Log.error(e);
            throw new IOException("Unable to save configured particle system");
        }
    }
    
    public static ConfigurableEmitter loadEmitter(final String ref) throws IOException {
        return loadEmitter(ResourceLoader.getResourceAsStream(ref), null);
    }
    
    public static ConfigurableEmitter loadEmitter(final File ref) throws IOException {
        return loadEmitter(new FileInputStream(ref), null);
    }
    
    public static ConfigurableEmitter loadEmitter(final InputStream ref) throws IOException {
        return loadEmitter(ref, null);
    }
    
    public static ConfigurableEmitter loadEmitter(final String ref, final ConfigurableEmitterFactory factory) throws IOException {
        return loadEmitter(ResourceLoader.getResourceAsStream(ref), factory);
    }
    
    public static ConfigurableEmitter loadEmitter(final File ref, final ConfigurableEmitterFactory factory) throws IOException {
        return loadEmitter(new FileInputStream(ref), factory);
    }
    
    public static ConfigurableEmitter loadEmitter(final InputStream ref, ConfigurableEmitterFactory factory) throws IOException {
        if (factory == null) {
            factory = new ConfigurableEmitterFactory() {
                @Override
                public ConfigurableEmitter createEmitter(final String name) {
                    return new ConfigurableEmitter(name);
                }
            };
        }
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.parse(ref);
            if (!document.getDocumentElement().getNodeName().equals("emitter")) {
                throw new IOException("Not a particle emitter file");
            }
            final ConfigurableEmitter emitter = factory.createEmitter("new");
            elementToEmitter(document.getDocumentElement(), emitter);
            return emitter;
        }
        catch (IOException e) {
            Log.error(e);
            throw e;
        }
        catch (Exception e2) {
            Log.error(e2);
            throw new IOException("Unable to load emitter");
        }
    }
    
    public static void saveEmitter(final File file, final ConfigurableEmitter emitter) throws IOException {
        saveEmitter(new FileOutputStream(file), emitter);
    }
    
    public static void saveEmitter(final OutputStream out, final ConfigurableEmitter emitter) throws IOException {
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.newDocument();
            document.appendChild(emitterToElement(document, emitter));
            final Result result = new StreamResult(new OutputStreamWriter(out, "utf-8"));
            final DOMSource source = new DOMSource(document);
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer xformer = factory.newTransformer();
            xformer.setOutputProperty("indent", "yes");
            xformer.transform(source, result);
        }
        catch (Exception e) {
            Log.error(e);
            throw new IOException("Failed to save emitter");
        }
    }
    
    private static Element getFirstNamedElement(final Element element, final String name) {
        final NodeList list = element.getElementsByTagName(name);
        if (list.getLength() == 0) {
            return null;
        }
        return (Element)list.item(0);
    }
    
    private static void elementToEmitter(final Element element, final ConfigurableEmitter emitter) {
        emitter.name = element.getAttribute("name");
        emitter.setImageName(element.getAttribute("imageName"));
        final String renderType = element.getAttribute("renderType");
        emitter.usePoints = 1;
        if (renderType.equals("quads")) {
            emitter.usePoints = 3;
        }
        if (renderType.equals("points")) {
            emitter.usePoints = 2;
        }
        final String useOriented = element.getAttribute("useOriented");
        if (useOriented != null) {
            emitter.useOriented = "true".equals(useOriented);
        }
        final String useAdditive = element.getAttribute("useAdditive");
        if (useAdditive != null) {
            emitter.useAdditive = "true".equals(useAdditive);
        }
        parseRangeElement(getFirstNamedElement(element, "spawnInterval"), emitter.spawnInterval);
        parseRangeElement(getFirstNamedElement(element, "spawnCount"), emitter.spawnCount);
        parseRangeElement(getFirstNamedElement(element, "initialLife"), emitter.initialLife);
        parseRangeElement(getFirstNamedElement(element, "initialSize"), emitter.initialSize);
        parseRangeElement(getFirstNamedElement(element, "xOffset"), emitter.xOffset);
        parseRangeElement(getFirstNamedElement(element, "yOffset"), emitter.yOffset);
        parseRangeElement(getFirstNamedElement(element, "initialDistance"), emitter.initialDistance);
        parseRangeElement(getFirstNamedElement(element, "speed"), emitter.speed);
        parseRangeElement(getFirstNamedElement(element, "length"), emitter.length);
        parseRangeElement(getFirstNamedElement(element, "emitCount"), emitter.emitCount);
        parseValueElement(getFirstNamedElement(element, "spread"), emitter.spread);
        parseValueElement(getFirstNamedElement(element, "angularOffset"), emitter.angularOffset);
        parseValueElement(getFirstNamedElement(element, "growthFactor"), emitter.growthFactor);
        parseValueElement(getFirstNamedElement(element, "gravityFactor"), emitter.gravityFactor);
        parseValueElement(getFirstNamedElement(element, "windFactor"), emitter.windFactor);
        parseValueElement(getFirstNamedElement(element, "startAlpha"), emitter.startAlpha);
        parseValueElement(getFirstNamedElement(element, "endAlpha"), emitter.endAlpha);
        parseValueElement(getFirstNamedElement(element, "alpha"), emitter.alpha);
        parseValueElement(getFirstNamedElement(element, "size"), emitter.size);
        parseValueElement(getFirstNamedElement(element, "velocity"), emitter.velocity);
        parseValueElement(getFirstNamedElement(element, "scaleY"), emitter.scaleY);
        final Element color = getFirstNamedElement(element, "color");
        final NodeList steps = color.getElementsByTagName("step");
        emitter.colors.clear();
        for (int i = 0; i < steps.getLength(); ++i) {
            final Element step = (Element)steps.item(i);
            final float offset = Float.parseFloat(step.getAttribute("offset"));
            final float r = Float.parseFloat(step.getAttribute("r"));
            final float g = Float.parseFloat(step.getAttribute("g"));
            final float b = Float.parseFloat(step.getAttribute("b"));
            emitter.addColorPoint(offset, new Color(r, g, b, 1.0f));
        }
        emitter.replay();
    }
    
    private static Element emitterToElement(final Document document, final ConfigurableEmitter emitter) {
        final Element root = document.createElement("emitter");
        root.setAttribute("name", emitter.name);
        root.setAttribute("imageName", (emitter.imageName == null) ? "" : emitter.imageName);
        root.setAttribute("useOriented", emitter.useOriented ? "true" : "false");
        root.setAttribute("useAdditive", emitter.useAdditive ? "true" : "false");
        if (emitter.usePoints == 1) {
            root.setAttribute("renderType", "inherit");
        }
        if (emitter.usePoints == 2) {
            root.setAttribute("renderType", "points");
        }
        if (emitter.usePoints == 3) {
            root.setAttribute("renderType", "quads");
        }
        root.appendChild(createRangeElement(document, "spawnInterval", emitter.spawnInterval));
        root.appendChild(createRangeElement(document, "spawnCount", emitter.spawnCount));
        root.appendChild(createRangeElement(document, "initialLife", emitter.initialLife));
        root.appendChild(createRangeElement(document, "initialSize", emitter.initialSize));
        root.appendChild(createRangeElement(document, "xOffset", emitter.xOffset));
        root.appendChild(createRangeElement(document, "yOffset", emitter.yOffset));
        root.appendChild(createRangeElement(document, "initialDistance", emitter.initialDistance));
        root.appendChild(createRangeElement(document, "speed", emitter.speed));
        root.appendChild(createRangeElement(document, "length", emitter.length));
        root.appendChild(createRangeElement(document, "emitCount", emitter.emitCount));
        root.appendChild(createValueElement(document, "spread", emitter.spread));
        root.appendChild(createValueElement(document, "angularOffset", emitter.angularOffset));
        root.appendChild(createValueElement(document, "growthFactor", emitter.growthFactor));
        root.appendChild(createValueElement(document, "gravityFactor", emitter.gravityFactor));
        root.appendChild(createValueElement(document, "windFactor", emitter.windFactor));
        root.appendChild(createValueElement(document, "startAlpha", emitter.startAlpha));
        root.appendChild(createValueElement(document, "endAlpha", emitter.endAlpha));
        root.appendChild(createValueElement(document, "alpha", emitter.alpha));
        root.appendChild(createValueElement(document, "size", emitter.size));
        root.appendChild(createValueElement(document, "velocity", emitter.velocity));
        root.appendChild(createValueElement(document, "scaleY", emitter.scaleY));
        final Element color = document.createElement("color");
        final ArrayList list = emitter.colors;
        for (int i = 0; i < list.size(); ++i) {
            final ConfigurableEmitter.ColorRecord record = list.get(i);
            final Element step = document.createElement("step");
            step.setAttribute("offset", new StringBuilder().append(record.pos).toString());
            step.setAttribute("r", new StringBuilder().append(record.col.r).toString());
            step.setAttribute("g", new StringBuilder().append(record.col.g).toString());
            step.setAttribute("b", new StringBuilder().append(record.col.b).toString());
            color.appendChild(step);
        }
        root.appendChild(color);
        return root;
    }
    
    private static Element createRangeElement(final Document document, final String name, final ConfigurableEmitter.Range range) {
        final Element element = document.createElement(name);
        element.setAttribute("min", new StringBuilder().append(range.getMin()).toString());
        element.setAttribute("max", new StringBuilder().append(range.getMax()).toString());
        element.setAttribute("enabled", new StringBuilder().append(range.isEnabled()).toString());
        return element;
    }
    
    private static Element createValueElement(final Document document, final String name, final ConfigurableEmitter.Value value) {
        final Element element = document.createElement(name);
        if (value instanceof ConfigurableEmitter.SimpleValue) {
            element.setAttribute("type", "simple");
            element.setAttribute("value", new StringBuilder().append(value.getValue(0.0f)).toString());
        }
        else if (value instanceof ConfigurableEmitter.RandomValue) {
            element.setAttribute("type", "random");
            element.setAttribute("value", new StringBuilder().append(((ConfigurableEmitter.RandomValue)value).getValue()).toString());
        }
        else if (value instanceof ConfigurableEmitter.LinearInterpolator) {
            element.setAttribute("type", "linear");
            element.setAttribute("min", new StringBuilder().append(((ConfigurableEmitter.LinearInterpolator)value).getMin()).toString());
            element.setAttribute("max", new StringBuilder().append(((ConfigurableEmitter.LinearInterpolator)value).getMax()).toString());
            element.setAttribute("active", new StringBuilder().append(((ConfigurableEmitter.LinearInterpolator)value).isActive()).toString());
            final ArrayList curve = ((ConfigurableEmitter.LinearInterpolator)value).getCurve();
            for (int i = 0; i < curve.size(); ++i) {
                final Vector2f point = curve.get(i);
                final Element pointElement = document.createElement("point");
                pointElement.setAttribute("x", new StringBuilder().append(point.x).toString());
                pointElement.setAttribute("y", new StringBuilder().append(point.y).toString());
                element.appendChild(pointElement);
            }
        }
        else {
            Log.warn("unkown value type ignored: " + value.getClass());
        }
        return element;
    }
    
    private static void parseRangeElement(final Element element, final ConfigurableEmitter.Range range) {
        if (element == null) {
            return;
        }
        range.setMin(Float.parseFloat(element.getAttribute("min")));
        range.setMax(Float.parseFloat(element.getAttribute("max")));
        range.setEnabled("true".equals(element.getAttribute("enabled")));
    }
    
    private static void parseValueElement(final Element element, final ConfigurableEmitter.Value value) {
        if (element == null) {
            return;
        }
        final String type = element.getAttribute("type");
        final String v = element.getAttribute("value");
        if (type == null || type.length() == 0) {
            if (value instanceof ConfigurableEmitter.SimpleValue) {
                ((ConfigurableEmitter.SimpleValue)value).setValue(Float.parseFloat(v));
            }
            else if (value instanceof ConfigurableEmitter.RandomValue) {
                ((ConfigurableEmitter.RandomValue)value).setValue(Float.parseFloat(v));
            }
            else {
                Log.warn("problems reading element, skipping: " + element);
            }
        }
        else if (type.equals("simple")) {
            ((ConfigurableEmitter.SimpleValue)value).setValue(Float.parseFloat(v));
        }
        else if (type.equals("random")) {
            ((ConfigurableEmitter.RandomValue)value).setValue(Float.parseFloat(v));
        }
        else if (type.equals("linear")) {
            final String min = element.getAttribute("min");
            final String max = element.getAttribute("max");
            final String active = element.getAttribute("active");
            final NodeList points = element.getElementsByTagName("point");
            final ArrayList curve = new ArrayList();
            for (int i = 0; i < points.getLength(); ++i) {
                final Element point = (Element)points.item(i);
                final float x = Float.parseFloat(point.getAttribute("x"));
                final float y = Float.parseFloat(point.getAttribute("y"));
                curve.add(new Vector2f(x, y));
            }
            ((ConfigurableEmitter.LinearInterpolator)value).setCurve(curve);
            ((ConfigurableEmitter.LinearInterpolator)value).setMin(Integer.parseInt(min));
            ((ConfigurableEmitter.LinearInterpolator)value).setMax(Integer.parseInt(max));
            ((ConfigurableEmitter.LinearInterpolator)value).setActive("true".equals(active));
        }
        else {
            Log.warn("unkown type detected: " + type);
        }
    }
}
