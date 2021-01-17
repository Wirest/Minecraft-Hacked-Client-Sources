// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Logger;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicConnection;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.AbstractManager;
import javax.naming.Context;

public class JMSTopicManager extends AbstractJMSManager
{
    private static final JMSTopicManagerFactory FACTORY;
    private TopicInfo info;
    private final String factoryBindingName;
    private final String topicBindingName;
    private final String userName;
    private final String password;
    private final Context context;
    
    protected JMSTopicManager(final String name, final Context context, final String factoryBindingName, final String topicBindingName, final String userName, final String password, final TopicInfo info) {
        super(name);
        this.context = context;
        this.factoryBindingName = factoryBindingName;
        this.topicBindingName = topicBindingName;
        this.userName = userName;
        this.password = password;
        this.info = info;
    }
    
    public static JMSTopicManager getJMSTopicManager(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials, final String factoryBindingName, final String topicBindingName, final String userName, final String password) {
        if (factoryBindingName == null) {
            JMSTopicManager.LOGGER.error("No factory name provided for JMSTopicManager");
            return null;
        }
        if (topicBindingName == null) {
            JMSTopicManager.LOGGER.error("No topic name provided for JMSTopicManager");
            return null;
        }
        final String name = "JMSTopic:" + factoryBindingName + '.' + topicBindingName;
        return AbstractManager.getManager(name, (ManagerFactory<JMSTopicManager, FactoryData>)JMSTopicManager.FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password));
    }
    
    @Override
    public void send(final Serializable object) throws Exception {
        if (this.info == null) {
            this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
        }
        try {
            super.send(object, (Session)this.info.session, (MessageProducer)this.info.publisher);
        }
        catch (Exception ex) {
            this.cleanup(true);
            throw ex;
        }
    }
    
    public void releaseSub() {
        if (this.info != null) {
            this.cleanup(false);
        }
    }
    
    private void cleanup(final boolean quiet) {
        try {
            this.info.session.close();
        }
        catch (Exception e) {
            if (!quiet) {
                JMSTopicManager.LOGGER.error("Error closing session for " + this.getName(), e);
            }
        }
        try {
            this.info.conn.close();
        }
        catch (Exception e) {
            if (!quiet) {
                JMSTopicManager.LOGGER.error("Error closing connection for " + this.getName(), e);
            }
        }
        this.info = null;
    }
    
    private static TopicInfo connect(final Context context, final String factoryBindingName, final String queueBindingName, final String userName, final String password, final boolean suppress) throws Exception {
        try {
            final TopicConnectionFactory factory = (TopicConnectionFactory)AbstractJMSManager.lookup(context, factoryBindingName);
            TopicConnection conn;
            if (userName != null) {
                conn = factory.createTopicConnection(userName, password);
            }
            else {
                conn = factory.createTopicConnection();
            }
            final TopicSession sess = conn.createTopicSession(false, 1);
            final Topic topic = (Topic)AbstractJMSManager.lookup(context, queueBindingName);
            final TopicPublisher publisher = sess.createPublisher(topic);
            conn.start();
            return new TopicInfo(conn, sess, publisher);
        }
        catch (NamingException ex) {
            JMSTopicManager.LOGGER.warn("Unable to locate connection factory " + factoryBindingName, ex);
            if (!suppress) {
                throw ex;
            }
        }
        catch (JMSException ex2) {
            JMSTopicManager.LOGGER.warn("Unable to create connection to queue " + queueBindingName, (Throwable)ex2);
            if (!suppress) {
                throw ex2;
            }
        }
        return null;
    }
    
    static {
        FACTORY = new JMSTopicManagerFactory();
    }
    
    private static class FactoryData
    {
        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String topicBindingName;
        private final String userName;
        private final String password;
        
        public FactoryData(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials, final String factoryBindingName, final String topicBindingName, final String userName, final String password) {
            this.factoryName = factoryName;
            this.providerURL = providerURL;
            this.urlPkgPrefixes = urlPkgPrefixes;
            this.securityPrincipalName = securityPrincipalName;
            this.securityCredentials = securityCredentials;
            this.factoryBindingName = factoryBindingName;
            this.topicBindingName = topicBindingName;
            this.userName = userName;
            this.password = password;
        }
    }
    
    private static class TopicInfo
    {
        private final TopicConnection conn;
        private final TopicSession session;
        private final TopicPublisher publisher;
        
        public TopicInfo(final TopicConnection conn, final TopicSession session, final TopicPublisher publisher) {
            this.conn = conn;
            this.session = session;
            this.publisher = publisher;
        }
    }
    
    private static class JMSTopicManagerFactory implements ManagerFactory<JMSTopicManager, FactoryData>
    {
        @Override
        public JMSTopicManager createManager(final String name, final FactoryData data) {
            try {
                final Context ctx = AbstractJMSManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
                final TopicInfo info = connect(ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, true);
                return new JMSTopicManager(name, ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, info);
            }
            catch (NamingException ex) {
                JMSTopicManager.LOGGER.error("Unable to locate resource", ex);
            }
            catch (Exception ex2) {
                JMSTopicManager.LOGGER.error("Unable to connect", ex2);
            }
            return null;
        }
    }
}
