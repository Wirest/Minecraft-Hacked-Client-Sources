// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import java.io.ByteArrayOutputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import java.util.Iterator;
import org.w3c.dom.Document;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import java.net.URISyntaxException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.helpers.FileUtils;
import java.net.URI;
import org.apache.logging.log4j.Level;
import java.util.Map;
import java.io.InputStream;
import org.xml.sax.InputSource;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import org.w3c.dom.Element;
import java.util.List;

public class XMLConfiguration extends BaseConfiguration implements Reconfigurable
{
    private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    private static final String[] VERBOSE_CLASSES;
    private static final String LOG4J_XSD = "Log4j-config.xsd";
    private static final int BUF_SIZE = 16384;
    private final List<Status> status;
    private Element rootElement;
    private boolean strict;
    private String schema;
    private final File configFile;
    
    static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        enableXInclude(factory);
        return factory.newDocumentBuilder();
    }
    
    private static void enableXInclude(final DocumentBuilderFactory factory) {
        try {
            factory.setXIncludeAware(true);
        }
        catch (UnsupportedOperationException e) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory does not support XInclude: " + factory, e);
        }
        catch (AbstractMethodError err) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: " + factory);
        }
        try {
            factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
        }
        catch (ParserConfigurationException e2) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-base-uris" + "]", e2);
        }
        catch (AbstractMethodError err) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
        }
        try {
            factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
        }
        catch (ParserConfigurationException e2) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-language" + "]", e2);
        }
        catch (AbstractMethodError err) {
            XMLConfiguration.LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
        }
    }
    
    public XMLConfiguration(final ConfigurationFactory.ConfigurationSource configSource) {
        this.status = new ArrayList<Status>();
        this.configFile = configSource.getFile();
        byte[] buffer = null;
        try {
            final List<String> messages = new ArrayList<String>();
            final InputStream configStream = configSource.getInputStream();
            buffer = this.toByteArray(configStream);
            configStream.close();
            final InputSource source = new InputSource(new ByteArrayInputStream(buffer));
            final Document document = newDocumentBuilder().parse(source);
            this.rootElement = document.getDocumentElement();
            final Map<String, String> attrs = this.processAttributes(this.rootNode, this.rootElement);
            Level status = this.getDefaultStatus();
            boolean verbose = false;
            PrintStream stream = System.out;
            for (final Map.Entry<String, String> entry : attrs.entrySet()) {
                if ("status".equalsIgnoreCase(entry.getKey())) {
                    final Level stat = Level.toLevel(this.getStrSubstitutor().replace(entry.getValue()), null);
                    if (stat != null) {
                        status = stat;
                    }
                    else {
                        messages.add("Invalid status specified: " + entry.getValue() + ". Defaulting to " + status);
                    }
                }
                else if ("dest".equalsIgnoreCase(entry.getKey())) {
                    final String dest = this.getStrSubstitutor().replace(entry.getValue());
                    if (dest == null) {
                        continue;
                    }
                    if (dest.equalsIgnoreCase("err")) {
                        stream = System.err;
                    }
                    else {
                        try {
                            final File destFile = FileUtils.fileFromURI(new URI(dest));
                            final String enc = Charset.defaultCharset().name();
                            stream = new PrintStream(new FileOutputStream(destFile), true, enc);
                        }
                        catch (URISyntaxException use) {
                            System.err.println("Unable to write to " + dest + ". Writing to stdout");
                        }
                    }
                }
                else if ("shutdownHook".equalsIgnoreCase(entry.getKey())) {
                    final String hook = this.getStrSubstitutor().replace(entry.getValue());
                    this.isShutdownHookEnabled = !hook.equalsIgnoreCase("disable");
                }
                else if ("verbose".equalsIgnoreCase(entry.getKey())) {
                    verbose = Boolean.parseBoolean(this.getStrSubstitutor().replace(entry.getValue()));
                }
                else if ("packages".equalsIgnoreCase(this.getStrSubstitutor().replace(entry.getKey()))) {
                    final String[] arr$;
                    final String[] packages = arr$ = entry.getValue().split(",");
                    for (final String p : arr$) {
                        PluginManager.addPackage(p);
                    }
                }
                else if ("name".equalsIgnoreCase(entry.getKey())) {
                    this.setName(this.getStrSubstitutor().replace(entry.getValue()));
                }
                else if ("strict".equalsIgnoreCase(entry.getKey())) {
                    this.strict = Boolean.parseBoolean(this.getStrSubstitutor().replace(entry.getValue()));
                }
                else if ("schema".equalsIgnoreCase(entry.getKey())) {
                    this.schema = this.getStrSubstitutor().replace(entry.getValue());
                }
                else if ("monitorInterval".equalsIgnoreCase(entry.getKey())) {
                    final int interval = Integer.parseInt(this.getStrSubstitutor().replace(entry.getValue()));
                    if (interval <= 0 || this.configFile == null) {
                        continue;
                    }
                    this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, interval);
                }
                else {
                    if (!"advertiser".equalsIgnoreCase(entry.getKey())) {
                        continue;
                    }
                    this.createAdvertiser(this.getStrSubstitutor().replace(entry.getValue()), configSource, buffer, "text/xml");
                }
            }
            final Iterator<StatusListener> iter = ((StatusLogger)XMLConfiguration.LOGGER).getListeners();
            boolean found = false;
            while (iter.hasNext()) {
                final StatusListener listener = iter.next();
                if (listener instanceof StatusConsoleListener) {
                    found = true;
                    ((StatusConsoleListener)listener).setLevel(status);
                    if (verbose) {
                        continue;
                    }
                    ((StatusConsoleListener)listener).setFilters(XMLConfiguration.VERBOSE_CLASSES);
                }
            }
            if (!found && status != Level.OFF) {
                final StatusConsoleListener listener2 = new StatusConsoleListener(status, stream);
                if (!verbose) {
                    listener2.setFilters(XMLConfiguration.VERBOSE_CLASSES);
                }
                ((StatusLogger)XMLConfiguration.LOGGER).registerListener(listener2);
                for (final String msg : messages) {
                    XMLConfiguration.LOGGER.error(msg);
                }
            }
        }
        catch (SAXException domEx) {
            XMLConfiguration.LOGGER.error("Error parsing " + configSource.getLocation(), domEx);
        }
        catch (IOException ioe) {
            XMLConfiguration.LOGGER.error("Error parsing " + configSource.getLocation(), ioe);
        }
        catch (ParserConfigurationException pex) {
            XMLConfiguration.LOGGER.error("Error parsing " + configSource.getLocation(), pex);
        }
        if (this.strict && this.schema != null && buffer != null) {
            InputStream is = null;
            try {
                is = this.getClass().getClassLoader().getResourceAsStream(this.schema);
            }
            catch (Exception ex3) {
                XMLConfiguration.LOGGER.error("Unable to access schema " + this.schema);
            }
            if (is != null) {
                final Source src = new StreamSource(is, "Log4j-config.xsd");
                final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                Schema schema = null;
                try {
                    schema = factory.newSchema(src);
                }
                catch (SAXException ex) {
                    XMLConfiguration.LOGGER.error("Error parsing Log4j schema", ex);
                }
                if (schema != null) {
                    final Validator validator = schema.newValidator();
                    try {
                        validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
                    }
                    catch (IOException ioe2) {
                        XMLConfiguration.LOGGER.error("Error reading configuration for validation", ioe2);
                    }
                    catch (SAXException ex2) {
                        XMLConfiguration.LOGGER.error("Error validating configuration", ex2);
                    }
                }
            }
        }
        if (this.getName() == null) {
            this.setName(configSource.getLocation());
        }
    }
    
    public void setup() {
        if (this.rootElement == null) {
            XMLConfiguration.LOGGER.error("No logging configuration");
            return;
        }
        this.constructHierarchy(this.rootNode, this.rootElement);
        if (this.status.size() > 0) {
            for (final Status s : this.status) {
                XMLConfiguration.LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
            }
            return;
        }
        this.rootElement = null;
    }
    
    @Override
    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                final ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
                return new XMLConfiguration(source);
            }
            catch (FileNotFoundException ex) {
                XMLConfiguration.LOGGER.error("Cannot locate file " + this.configFile, ex);
            }
        }
        return null;
    }
    
    private void constructHierarchy(final Node node, final Element element) {
        this.processAttributes(node, element);
        final StringBuilder buffer = new StringBuilder();
        final NodeList list = element.getChildNodes();
        final List<Node> children = node.getChildren();
        for (int i = 0; i < list.getLength(); ++i) {
            final org.w3c.dom.Node w3cNode = list.item(i);
            if (w3cNode instanceof Element) {
                final Element child = (Element)w3cNode;
                final String name = this.getType(child);
                final PluginType<?> type = this.pluginManager.getPluginType(name);
                final Node childNode = new Node(node, name, type);
                this.constructHierarchy(childNode, child);
                if (type == null) {
                    final String value = childNode.getValue();
                    if (!childNode.hasChildren() && value != null) {
                        node.getAttributes().put(name, value);
                    }
                    else {
                        this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
                    }
                }
                else {
                    children.add(childNode);
                }
            }
            else if (w3cNode instanceof Text) {
                final Text data = (Text)w3cNode;
                buffer.append(data.getData());
            }
        }
        final String text = buffer.toString().trim();
        if (text.length() > 0 || (!node.hasChildren() && !node.isRoot())) {
            node.setValue(text);
        }
    }
    
    private String getType(final Element element) {
        if (this.strict) {
            final NamedNodeMap attrs = element.getAttributes();
            for (int i = 0; i < attrs.getLength(); ++i) {
                final org.w3c.dom.Node w3cNode = attrs.item(i);
                if (w3cNode instanceof Attr) {
                    final Attr attr = (Attr)w3cNode;
                    if (attr.getName().equalsIgnoreCase("type")) {
                        final String type = attr.getValue();
                        attrs.removeNamedItem(attr.getName());
                        return type;
                    }
                }
            }
        }
        return element.getTagName();
    }
    
    private byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final byte[] data = new byte[16384];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    private Map<String, String> processAttributes(final Node node, final Element element) {
        final NamedNodeMap attrs = element.getAttributes();
        final Map<String, String> attributes = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); ++i) {
            final org.w3c.dom.Node w3cNode = attrs.item(i);
            if (w3cNode instanceof Attr) {
                final Attr attr = (Attr)w3cNode;
                if (!attr.getName().equals("xml:base")) {
                    attributes.put(attr.getName(), attr.getValue());
                }
            }
        }
        return attributes;
    }
    
    static {
        VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
    }
    
    private enum ErrorType
    {
        CLASS_NOT_FOUND;
    }
    
    private class Status
    {
        private final Element element;
        private final String name;
        private final ErrorType errorType;
        
        public Status(final String name, final Element element, final ErrorType errorType) {
            this.name = name;
            this.element = element;
            this.errorType = errorType;
        }
    }
}
