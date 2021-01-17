// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Collections;
import org.apache.logging.log4j.core.helpers.NameUtil;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Level;
import java.util.Iterator;
import org.apache.logging.log4j.core.lookup.Interpolator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.Appender;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import org.apache.logging.log4j.core.net.Advertiser;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public class BaseConfiguration extends AbstractFilterable implements Configuration
{
    protected static final Logger LOGGER;
    protected Node rootNode;
    protected final List<ConfigurationListener> listeners;
    protected ConfigurationMonitor monitor;
    private Advertiser advertiser;
    protected Map<String, String> advertisedConfiguration;
    private Node advertiserNode;
    private Object advertisement;
    protected boolean isShutdownHookEnabled;
    private String name;
    private ConcurrentMap<String, Appender> appenders;
    private ConcurrentMap<String, LoggerConfig> loggers;
    private final StrLookup tempLookup;
    private final StrSubstitutor subst;
    private LoggerConfig root;
    private final boolean started = false;
    private final ConcurrentMap<String, Object> componentMap;
    protected PluginManager pluginManager;
    
    protected BaseConfiguration() {
        this.listeners = new CopyOnWriteArrayList<ConfigurationListener>();
        this.monitor = new DefaultConfigurationMonitor();
        this.advertiser = new DefaultAdvertiser();
        this.advertiserNode = null;
        this.isShutdownHookEnabled = true;
        this.appenders = new ConcurrentHashMap<String, Appender>();
        this.loggers = new ConcurrentHashMap<String, LoggerConfig>();
        this.tempLookup = new Interpolator();
        this.subst = new StrSubstitutor(this.tempLookup);
        this.root = new LoggerConfig();
        this.componentMap = new ConcurrentHashMap<String, Object>();
        this.pluginManager = new PluginManager("Core");
        this.rootNode = new Node();
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.componentMap.get("ContextProperties");
    }
    
    @Override
    public void start() {
        this.pluginManager.collectPlugins();
        this.setup();
        this.setupAdvertisement();
        this.doConfigure();
        for (final LoggerConfig logger : this.loggers.values()) {
            logger.startFilter();
        }
        for (final Appender appender : this.appenders.values()) {
            appender.start();
        }
        this.root.startFilter();
        this.startFilter();
    }
    
    @Override
    public void stop() {
        final Appender[] array = this.appenders.values().toArray(new Appender[this.appenders.size()]);
        for (int i = array.length - 1; i >= 0; --i) {
            array[i].stop();
        }
        for (final LoggerConfig logger : this.loggers.values()) {
            logger.clearAppenders();
            logger.stopFilter();
        }
        this.root.stopFilter();
        this.stopFilter();
        if (this.advertiser != null && this.advertisement != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }
    
    @Override
    public boolean isShutdownHookEnabled() {
        return this.isShutdownHookEnabled;
    }
    
    protected void setup() {
    }
    
    protected Level getDefaultStatus() {
        final String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());
        try {
            return Level.toLevel(statusLevel);
        }
        catch (Exception ex) {
            return Level.ERROR;
        }
    }
    
    protected void createAdvertiser(final String advertiserString, final ConfigurationFactory.ConfigurationSource configSource, final byte[] buffer, final String contentType) {
        if (advertiserString != null) {
            final Node node = new Node(null, advertiserString, null);
            final Map<String, String> attributes = node.getAttributes();
            attributes.put("content", new String(buffer));
            attributes.put("contentType", contentType);
            attributes.put("name", "configuration");
            if (configSource.getLocation() != null) {
                attributes.put("location", configSource.getLocation());
            }
            this.advertiserNode = node;
        }
    }
    
    private void setupAdvertisement() {
        if (this.advertiserNode != null) {
            final String name = this.advertiserNode.getName();
            final PluginType<Advertiser> type = (PluginType<Advertiser>)this.pluginManager.getPluginType(name);
            if (type != null) {
                final Class<Advertiser> clazz = type.getPluginClass();
                try {
                    this.advertiser = clazz.newInstance();
                    this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
                }
                catch (InstantiationException e) {
                    System.err.println("InstantiationException attempting to instantiate advertiser: " + name);
                }
                catch (IllegalAccessException e2) {
                    System.err.println("IllegalAccessException attempting to instantiate advertiser: " + name);
                }
            }
        }
    }
    
    @Override
    public Object getComponent(final String name) {
        return this.componentMap.get(name);
    }
    
    @Override
    public void addComponent(final String name, final Object obj) {
        this.componentMap.putIfAbsent(name, obj);
    }
    
    protected void doConfigure() {
        boolean setRoot = false;
        boolean setLoggers = false;
        for (final Node child : this.rootNode.getChildren()) {
            this.createConfiguration(child, null);
            if (child.getObject() == null) {
                continue;
            }
            if (child.getName().equalsIgnoreCase("Properties")) {
                if (this.tempLookup == this.subst.getVariableResolver()) {
                    this.subst.setVariableResolver((StrLookup)child.getObject());
                }
                else {
                    BaseConfiguration.LOGGER.error("Properties declaration must be the first element in the configuration");
                }
            }
            else {
                if (this.tempLookup == this.subst.getVariableResolver()) {
                    final Map<String, String> map = this.componentMap.get("ContextProperties");
                    final StrLookup lookup = (map == null) ? null : new MapLookup(map);
                    this.subst.setVariableResolver(new Interpolator(lookup));
                }
                if (child.getName().equalsIgnoreCase("Appenders")) {
                    this.appenders = (ConcurrentMap<String, Appender>)child.getObject();
                }
                else if (child.getObject() instanceof Filter) {
                    this.addFilter((Filter)child.getObject());
                }
                else if (child.getName().equalsIgnoreCase("Loggers")) {
                    final Loggers l = (Loggers)child.getObject();
                    this.loggers = l.getMap();
                    setLoggers = true;
                    if (l.getRoot() == null) {
                        continue;
                    }
                    this.root = l.getRoot();
                    setRoot = true;
                }
                else {
                    BaseConfiguration.LOGGER.error("Unknown object \"" + child.getName() + "\" of type " + child.getObject().getClass().getName() + " is ignored");
                }
            }
        }
        if (!setLoggers) {
            BaseConfiguration.LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
            this.setToDefault();
            return;
        }
        if (!setRoot) {
            BaseConfiguration.LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
            this.setToDefault();
        }
        for (final Map.Entry<String, LoggerConfig> entry : this.loggers.entrySet()) {
            final LoggerConfig i = entry.getValue();
            for (final AppenderRef ref : i.getAppenderRefs()) {
                final Appender app = this.appenders.get(ref.getRef());
                if (app != null) {
                    i.addAppender(app, ref.getLevel(), ref.getFilter());
                }
                else {
                    BaseConfiguration.LOGGER.error("Unable to locate appender " + ref.getRef() + " for logger " + i.getName());
                }
            }
        }
        this.setParents();
    }
    
    private void setToDefault() {
        this.setName("Default");
        final Layout<? extends Serializable> layout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", null, null, null, null);
        final Appender appender = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "false", "true");
        appender.start();
        this.addAppender(appender);
        final LoggerConfig root = this.getRootLogger();
        root.addAppender(appender, null, null);
        final String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
        final Level level = (levelName != null && Level.valueOf(levelName) != null) ? Level.valueOf(levelName) : Level.ERROR;
        root.setLevel(level);
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void addListener(final ConfigurationListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public void removeListener(final ConfigurationListener listener) {
        this.listeners.remove(listener);
    }
    
    public Appender getAppender(final String name) {
        return this.appenders.get(name);
    }
    
    @Override
    public Map<String, Appender> getAppenders() {
        return this.appenders;
    }
    
    public void addAppender(final Appender appender) {
        this.appenders.put(appender.getName(), appender);
    }
    
    @Override
    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }
    
    @Override
    public void setConfigurationMonitor(final ConfigurationMonitor monitor) {
        this.monitor = monitor;
    }
    
    @Override
    public ConfigurationMonitor getConfigurationMonitor() {
        return this.monitor;
    }
    
    @Override
    public void setAdvertiser(final Advertiser advertiser) {
        this.advertiser = advertiser;
    }
    
    @Override
    public Advertiser getAdvertiser() {
        return this.advertiser;
    }
    
    @Override
    public synchronized void addLoggerAppender(final org.apache.logging.log4j.core.Logger logger, final Appender appender) {
        final String name = logger.getName();
        this.appenders.putIfAbsent(appender.getName(), appender);
        final LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.addAppender(appender, null, null);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
            nlc.addAppender(appender, null, null);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    @Override
    public synchronized void addLoggerFilter(final org.apache.logging.log4j.core.Logger logger, final Filter filter) {
        final String name = logger.getName();
        final LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.addFilter(filter);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
            nlc.addFilter(filter);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    @Override
    public synchronized void setLoggerAdditive(final org.apache.logging.log4j.core.Logger logger, final boolean additive) {
        final String name = logger.getName();
        final LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.setAdditive(additive);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), additive);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    public synchronized void removeAppender(final String name) {
        for (final LoggerConfig logger : this.loggers.values()) {
            logger.removeAppender(name);
        }
        final Appender app = this.appenders.remove(name);
        if (app != null) {
            app.stop();
        }
    }
    
    @Override
    public LoggerConfig getLoggerConfig(final String name) {
        if (this.loggers.containsKey(name)) {
            return this.loggers.get(name);
        }
        String substr = name;
        while ((substr = NameUtil.getSubName(substr)) != null) {
            if (this.loggers.containsKey(substr)) {
                return this.loggers.get(substr);
            }
        }
        return this.root;
    }
    
    public LoggerConfig getRootLogger() {
        return this.root;
    }
    
    @Override
    public Map<String, LoggerConfig> getLoggers() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends LoggerConfig>)this.loggers);
    }
    
    public LoggerConfig getLogger(final String name) {
        return this.loggers.get(name);
    }
    
    public void addLogger(final String name, final LoggerConfig loggerConfig) {
        this.loggers.put(name, loggerConfig);
        this.setParents();
    }
    
    public void removeLogger(final String name) {
        this.loggers.remove(name);
        this.setParents();
    }
    
    @Override
    public void createConfiguration(final Node node, final LogEvent event) {
        final PluginType<?> type = node.getType();
        if (type != null && type.isDeferChildren()) {
            node.setObject(this.createPluginObject(type, node, event));
        }
        else {
            for (final Node child : node.getChildren()) {
                this.createConfiguration(child, event);
            }
            if (type == null) {
                if (node.getParent() != null) {
                    BaseConfiguration.LOGGER.error("Unable to locate plugin for " + node.getName());
                }
            }
            else {
                node.setObject(this.createPluginObject(type, node, event));
            }
        }
    }
    
    private <T> Object createPluginObject(final PluginType<T> type, final Node node, final LogEvent event) {
        final Class<T> clazz = type.getPluginClass();
        if (Map.class.isAssignableFrom(clazz)) {
            try {
                final Map<String, Object> map = (Map<String, Object>)clazz.newInstance();
                for (final Node child : node.getChildren()) {
                    map.put(child.getName(), child.getObject());
                }
                return map;
            }
            catch (Exception ex) {
                BaseConfiguration.LOGGER.warn("Unable to create Map for " + type.getElementName() + " of class " + clazz);
            }
        }
        if (List.class.isAssignableFrom(clazz)) {
            try {
                final List<Object> list = (List<Object>)clazz.newInstance();
                for (final Node child : node.getChildren()) {
                    list.add(child.getObject());
                }
                return list;
            }
            catch (Exception ex) {
                BaseConfiguration.LOGGER.warn("Unable to create List for " + type.getElementName() + " of class " + clazz);
            }
        }
        Method factoryMethod = null;
        for (final Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(PluginFactory.class)) {
                factoryMethod = method;
                break;
            }
        }
        if (factoryMethod == null) {
            return null;
        }
        final Annotation[][] parmArray = factoryMethod.getParameterAnnotations();
        final Class<?>[] parmClasses = factoryMethod.getParameterTypes();
        if (parmArray.length != parmClasses.length) {
            BaseConfiguration.LOGGER.error("Number of parameter annotations does not equal the number of paramters");
        }
        final Object[] parms = new Object[parmClasses.length];
        int index = 0;
        final Map<String, String> attrs = node.getAttributes();
        final List<Node> children = node.getChildren();
        final StringBuilder sb = new StringBuilder();
        final List<Node> used = new ArrayList<Node>();
        for (final Annotation[] parmTypes : parmArray) {
            String[] aliases = null;
            for (final Annotation a : parmTypes) {
                if (a instanceof PluginAliases) {
                    aliases = ((PluginAliases)a).value();
                }
            }
            for (final Annotation a : parmTypes) {
                if (!(a instanceof PluginAliases)) {
                    if (sb.length() == 0) {
                        sb.append(" with params(");
                    }
                    else {
                        sb.append(", ");
                    }
                    if (a instanceof PluginNode) {
                        parms[index] = node;
                        sb.append("Node=").append(node.getName());
                    }
                    else if (a instanceof PluginConfiguration) {
                        parms[index] = this;
                        if (this.name != null) {
                            sb.append("Configuration(").append(this.name).append(")");
                        }
                        else {
                            sb.append("Configuration");
                        }
                    }
                    else if (a instanceof PluginValue) {
                        final String name = ((PluginValue)a).value();
                        String v = node.getValue();
                        if (v == null) {
                            v = this.getAttrValue("value", null, attrs);
                        }
                        final String value = this.subst.replace(event, v);
                        sb.append(name).append("=\"").append(value).append("\"");
                        parms[index] = value;
                    }
                    else if (a instanceof PluginAttribute) {
                        final PluginAttribute attr = (PluginAttribute)a;
                        final String name2 = attr.value();
                        final String value = this.subst.replace(event, this.getAttrValue(name2, aliases, attrs));
                        sb.append(name2).append("=\"").append(value).append("\"");
                        parms[index] = value;
                    }
                    else if (a instanceof PluginElement) {
                        final PluginElement elem = (PluginElement)a;
                        final String name2 = elem.value();
                        if (parmClasses[index].isArray()) {
                            final Class<?> parmClass = parmClasses[index].getComponentType();
                            final List<Object> list2 = new ArrayList<Object>();
                            sb.append(name2).append("={");
                            boolean first = true;
                            for (final Node child2 : children) {
                                final PluginType<?> childType = child2.getType();
                                if (elem.value().equalsIgnoreCase(childType.getElementName()) || parmClass.isAssignableFrom(childType.getPluginClass())) {
                                    used.add(child2);
                                    if (!first) {
                                        sb.append(", ");
                                    }
                                    first = false;
                                    final Object obj = child2.getObject();
                                    if (obj == null) {
                                        BaseConfiguration.LOGGER.error("Null object returned for " + child2.getName() + " in " + node.getName());
                                    }
                                    else {
                                        if (obj.getClass().isArray()) {
                                            this.printArray(sb, (Object[])obj);
                                            parms[index] = obj;
                                            break;
                                        }
                                        sb.append(child2.toString());
                                        list2.add(obj);
                                    }
                                }
                            }
                            sb.append("}");
                            if (parms[index] != null) {
                                break;
                            }
                            if (list2.size() > 0 && !parmClass.isAssignableFrom(list2.get(0).getClass())) {
                                BaseConfiguration.LOGGER.error("Attempted to assign List containing class " + list2.get(0).getClass().getName() + " to array of type " + parmClass + " for attribute " + name2);
                                break;
                            }
                            final Object[] array = (Object[])Array.newInstance(parmClass, list2.size());
                            int i = 0;
                            final Iterator i$6 = list2.iterator();
                            while (i$6.hasNext()) {
                                final Object obj = i$6.next();
                                array[i] = obj;
                                ++i;
                            }
                            parms[index] = array;
                        }
                        else {
                            final Class<?> parmClass = parmClasses[index];
                            boolean present = false;
                            for (final Node child3 : children) {
                                final PluginType<?> childType2 = child3.getType();
                                if (elem.value().equals(childType2.getElementName()) || parmClass.isAssignableFrom(childType2.getPluginClass())) {
                                    sb.append(child3.getName()).append("(").append(child3.toString()).append(")");
                                    present = true;
                                    used.add(child3);
                                    parms[index] = child3.getObject();
                                    break;
                                }
                            }
                            if (!present) {
                                sb.append("null");
                            }
                        }
                    }
                }
            }
            ++index;
        }
        if (sb.length() > 0) {
            sb.append(")");
        }
        if (attrs.size() > 0) {
            final StringBuilder eb = new StringBuilder();
            for (final String key : attrs.keySet()) {
                if (eb.length() == 0) {
                    eb.append(node.getName());
                    eb.append(" contains ");
                    if (attrs.size() == 1) {
                        eb.append("an invalid element or attribute ");
                    }
                    else {
                        eb.append("invalid attributes ");
                    }
                }
                else {
                    eb.append(", ");
                }
                eb.append("\"");
                eb.append(key);
                eb.append("\"");
            }
            BaseConfiguration.LOGGER.error(eb.toString());
        }
        if (!type.isDeferChildren() && used.size() != children.size()) {
            for (final Node child4 : children) {
                if (used.contains(child4)) {
                    continue;
                }
                final String nodeType = node.getType().getElementName();
                final String start = nodeType.equals(node.getName()) ? node.getName() : (nodeType + " " + node.getName());
                BaseConfiguration.LOGGER.error(start + " has no parameter that matches element " + child4.getName());
            }
        }
        try {
            final int mod = factoryMethod.getModifiers();
            if (!Modifier.isStatic(mod)) {
                BaseConfiguration.LOGGER.error(factoryMethod.getName() + " method is not static on class " + clazz.getName() + " for element " + node.getName());
                return null;
            }
            BaseConfiguration.LOGGER.debug("Calling {} on class {} for element {}{}", factoryMethod.getName(), clazz.getName(), node.getName(), sb.toString());
            return factoryMethod.invoke(null, parms);
        }
        catch (Exception e) {
            BaseConfiguration.LOGGER.error("Unable to invoke method " + factoryMethod.getName() + " in class " + clazz.getName() + " for element " + node.getName(), e);
            return null;
        }
    }
    
    private void printArray(final StringBuilder sb, final Object... array) {
        boolean first = true;
        for (final Object obj : array) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(obj.toString());
            first = false;
        }
    }
    
    private String getAttrValue(final String name, final String[] aliases, final Map<String, String> attrs) {
        for (final String key : attrs.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                final String attr = attrs.get(key);
                attrs.remove(key);
                return attr;
            }
            if (aliases == null) {
                continue;
            }
            for (final String alias : aliases) {
                if (key.equalsIgnoreCase(alias)) {
                    final String attr2 = attrs.get(key);
                    attrs.remove(key);
                    return attr2;
                }
            }
        }
        return null;
    }
    
    private void setParents() {
        for (final Map.Entry<String, LoggerConfig> entry : this.loggers.entrySet()) {
            final LoggerConfig logger = entry.getValue();
            String name = entry.getKey();
            if (!name.equals("")) {
                final int i = name.lastIndexOf(46);
                if (i > 0) {
                    name = name.substring(0, i);
                    LoggerConfig parent = this.getLoggerConfig(name);
                    if (parent == null) {
                        parent = this.root;
                    }
                    logger.setParent(parent);
                }
                else {
                    logger.setParent(this.root);
                }
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
