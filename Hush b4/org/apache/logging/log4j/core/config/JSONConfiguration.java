// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.InputStream;
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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.io.File;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class JSONConfiguration extends BaseConfiguration implements Reconfigurable
{
    private static final String[] VERBOSE_CLASSES;
    private static final int BUF_SIZE = 16384;
    private final List<Status> status;
    private JsonNode root;
    private final List<String> messages;
    private final File configFile;
    
    public JSONConfiguration(final ConfigurationFactory.ConfigurationSource configSource) {
        this.status = new ArrayList<Status>();
        this.messages = new ArrayList<String>();
        this.configFile = configSource.getFile();
        try {
            final InputStream configStream = configSource.getInputStream();
            final byte[] buffer = this.toByteArray(configStream);
            configStream.close();
            final InputStream is = new ByteArrayInputStream(buffer);
            final ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            this.root = mapper.readTree(is);
            if (this.root.size() == 1) {
                final Iterator<JsonNode> i = (Iterator<JsonNode>)this.root.elements();
                this.root = i.next();
            }
            this.processAttributes(this.rootNode, this.root);
            Level status = this.getDefaultStatus();
            boolean verbose = false;
            PrintStream stream = System.out;
            for (final Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
                if ("status".equalsIgnoreCase(entry.getKey())) {
                    status = Level.toLevel(this.getStrSubstitutor().replace(entry.getValue()), null);
                    if (status != null) {
                        continue;
                    }
                    status = Level.ERROR;
                    this.messages.add("Invalid status specified: " + entry.getValue() + ". Defaulting to ERROR");
                }
                else if ("dest".equalsIgnoreCase(entry.getKey())) {
                    final String dest = entry.getValue();
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
                else if ("packages".equalsIgnoreCase(entry.getKey())) {
                    final String[] arr$;
                    final String[] packages = arr$ = this.getStrSubstitutor().replace(entry.getValue()).split(",");
                    for (final String p : arr$) {
                        PluginManager.addPackage(p);
                    }
                }
                else if ("name".equalsIgnoreCase(entry.getKey())) {
                    this.setName(this.getStrSubstitutor().replace(entry.getValue()));
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
                    this.createAdvertiser(this.getStrSubstitutor().replace(entry.getValue()), configSource, buffer, "application/json");
                }
            }
            final Iterator<StatusListener> statusIter = ((StatusLogger)JSONConfiguration.LOGGER).getListeners();
            boolean found = false;
            while (statusIter.hasNext()) {
                final StatusListener listener = statusIter.next();
                if (listener instanceof StatusConsoleListener) {
                    found = true;
                    ((StatusConsoleListener)listener).setLevel(status);
                    if (verbose) {
                        continue;
                    }
                    ((StatusConsoleListener)listener).setFilters(JSONConfiguration.VERBOSE_CLASSES);
                }
            }
            if (!found && status != Level.OFF) {
                final StatusConsoleListener listener2 = new StatusConsoleListener(status, stream);
                if (!verbose) {
                    listener2.setFilters(JSONConfiguration.VERBOSE_CLASSES);
                }
                ((StatusLogger)JSONConfiguration.LOGGER).registerListener(listener2);
                for (final String msg : this.messages) {
                    JSONConfiguration.LOGGER.error(msg);
                }
            }
            if (this.getName() == null) {
                this.setName(configSource.getLocation());
            }
        }
        catch (Exception ex) {
            JSONConfiguration.LOGGER.error("Error parsing " + configSource.getLocation(), ex);
            ex.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        super.stop();
    }
    
    public void setup() {
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)this.root.fields();
        final List<Node> children = this.rootNode.getChildren();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            final JsonNode n = entry.getValue();
            if (n.isObject()) {
                JSONConfiguration.LOGGER.debug("Processing node for object " + entry.getKey());
                children.add(this.constructNode(entry.getKey(), this.rootNode, n));
            }
            else {
                if (!n.isArray()) {
                    continue;
                }
                JSONConfiguration.LOGGER.error("Arrays are not supported at the root configuration.");
            }
        }
        JSONConfiguration.LOGGER.debug("Completed parsing configuration");
        if (this.status.size() > 0) {
            for (final Status s : this.status) {
                JSONConfiguration.LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
            }
        }
    }
    
    @Override
    public Configuration reconfigure() {
        if (this.configFile != null) {
            try {
                final ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
                return new JSONConfiguration(source);
            }
            catch (FileNotFoundException ex) {
                JSONConfiguration.LOGGER.error("Cannot locate file " + this.configFile, ex);
            }
        }
        return null;
    }
    
    private Node constructNode(final String name, final Node parent, final JsonNode jsonNode) {
        final PluginType<?> type = this.pluginManager.getPluginType(name);
        final Node node = new Node(parent, name, type);
        this.processAttributes(node, jsonNode);
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)jsonNode.fields();
        final List<Node> children = node.getChildren();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            final JsonNode n = entry.getValue();
            if (n.isArray() || n.isObject()) {
                if (type == null) {
                    this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
                }
                if (n.isArray()) {
                    JSONConfiguration.LOGGER.debug("Processing node for array " + entry.getKey());
                    for (int i = 0; i < n.size(); ++i) {
                        final String pluginType = this.getType(n.get(i), entry.getKey());
                        final PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
                        final Node item = new Node(node, entry.getKey(), entryType);
                        this.processAttributes(item, n.get(i));
                        if (pluginType.equals(entry.getKey())) {
                            JSONConfiguration.LOGGER.debug("Processing " + entry.getKey() + "[" + i + "]");
                        }
                        else {
                            JSONConfiguration.LOGGER.debug("Processing " + pluginType + " " + entry.getKey() + "[" + i + "]");
                        }
                        final Iterator<Map.Entry<String, JsonNode>> itemIter = (Iterator<Map.Entry<String, JsonNode>>)n.get(i).fields();
                        final List<Node> itemChildren = item.getChildren();
                        while (itemIter.hasNext()) {
                            final Map.Entry<String, JsonNode> itemEntry = itemIter.next();
                            if (itemEntry.getValue().isObject()) {
                                JSONConfiguration.LOGGER.debug("Processing node for object " + itemEntry.getKey());
                                itemChildren.add(this.constructNode(itemEntry.getKey(), item, itemEntry.getValue()));
                            }
                        }
                        children.add(item);
                    }
                }
                else {
                    JSONConfiguration.LOGGER.debug("Processing node for object " + entry.getKey());
                    children.add(this.constructNode(entry.getKey(), node, n));
                }
            }
        }
        String t;
        if (type == null) {
            t = "null";
        }
        else {
            t = type.getElementName() + ":" + type.getPluginClass();
        }
        final String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
        JSONConfiguration.LOGGER.debug("Returning " + node.getName() + " with parent " + p + " of type " + t);
        return node;
    }
    
    private String getType(final JsonNode node, final String name) {
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)node.fields();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            if (entry.getKey().equalsIgnoreCase("type")) {
                final JsonNode n = entry.getValue();
                if (n.isValueNode()) {
                    return n.asText();
                }
                continue;
            }
        }
        return name;
    }
    
    private void processAttributes(final Node parent, final JsonNode node) {
        final Map<String, String> attrs = parent.getAttributes();
        final Iterator<Map.Entry<String, JsonNode>> iter = (Iterator<Map.Entry<String, JsonNode>>)node.fields();
        while (iter.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iter.next();
            if (!entry.getKey().equalsIgnoreCase("type")) {
                final JsonNode n = entry.getValue();
                if (!n.isValueNode()) {
                    continue;
                }
                attrs.put(entry.getKey(), n.asText());
            }
        }
    }
    
    protected byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final byte[] data = new byte[16384];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
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
        private final JsonNode node;
        private final String name;
        private final ErrorType errorType;
        
        public Status(final String name, final JsonNode node, final ErrorType errorType) {
            this.name = name;
            this.node = node;
            this.errorType = errorType;
        }
    }
}
