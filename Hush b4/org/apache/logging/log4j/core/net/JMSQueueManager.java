// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Logger;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.QueueConnection;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.AbstractManager;
import javax.naming.Context;

public class JMSQueueManager extends AbstractJMSManager
{
    private static final JMSQueueManagerFactory FACTORY;
    private QueueInfo info;
    private final String factoryBindingName;
    private final String queueBindingName;
    private final String userName;
    private final String password;
    private final Context context;
    
    protected JMSQueueManager(final String name, final Context context, final String factoryBindingName, final String queueBindingName, final String userName, final String password, final QueueInfo info) {
        super(name);
        this.context = context;
        this.factoryBindingName = factoryBindingName;
        this.queueBindingName = queueBindingName;
        this.userName = userName;
        this.password = password;
        this.info = info;
    }
    
    public static JMSQueueManager getJMSQueueManager(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials, final String factoryBindingName, final String queueBindingName, final String userName, final String password) {
        if (factoryBindingName == null) {
            JMSQueueManager.LOGGER.error("No factory name provided for JMSQueueManager");
            return null;
        }
        if (queueBindingName == null) {
            JMSQueueManager.LOGGER.error("No topic name provided for JMSQueueManager");
            return null;
        }
        final String name = "JMSQueue:" + factoryBindingName + '.' + queueBindingName;
        return AbstractManager.getManager(name, (ManagerFactory<JMSQueueManager, FactoryData>)JMSQueueManager.FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password));
    }
    
    @Override
    public synchronized void send(final Serializable object) throws Exception {
        if (this.info == null) {
            this.info = connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
        }
        try {
            super.send(object, (Session)this.info.session, (MessageProducer)this.info.sender);
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
                JMSQueueManager.LOGGER.error("Error closing session for " + this.getName(), e);
            }
        }
        try {
            this.info.conn.close();
        }
        catch (Exception e) {
            if (!quiet) {
                JMSQueueManager.LOGGER.error("Error closing connection for " + this.getName(), e);
            }
        }
        this.info = null;
    }
    
    private static QueueInfo connect(final Context context, final String factoryBindingName, final String queueBindingName, final String userName, final String password, final boolean suppress) throws Exception {
        try {
            final QueueConnectionFactory factory = (QueueConnectionFactory)AbstractJMSManager.lookup(context, factoryBindingName);
            QueueConnection conn;
            if (userName != null) {
                conn = factory.createQueueConnection(userName, password);
            }
            else {
                conn = factory.createQueueConnection();
            }
            final QueueSession sess = conn.createQueueSession(false, 1);
            final Queue queue = (Queue)AbstractJMSManager.lookup(context, queueBindingName);
            final QueueSender sender = sess.createSender(queue);
            conn.start();
            return new QueueInfo(conn, sess, sender);
        }
        catch (NamingException ex) {
            JMSQueueManager.LOGGER.warn("Unable to locate connection factory " + factoryBindingName, ex);
            if (!suppress) {
                throw ex;
            }
        }
        catch (JMSException ex2) {
            JMSQueueManager.LOGGER.warn("Unable to create connection to queue " + queueBindingName, (Throwable)ex2);
            if (!suppress) {
                throw ex2;
            }
        }
        return null;
    }
    
    static {
        FACTORY = new JMSQueueManagerFactory();
    }
    
    private static class FactoryData
    {
        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String queueBindingName;
        private final String userName;
        private final String password;
        
        public FactoryData(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials, final String factoryBindingName, final String queueBindingName, final String userName, final String password) {
            this.factoryName = factoryName;
            this.providerURL = providerURL;
            this.urlPkgPrefixes = urlPkgPrefixes;
            this.securityPrincipalName = securityPrincipalName;
            this.securityCredentials = securityCredentials;
            this.factoryBindingName = factoryBindingName;
            this.queueBindingName = queueBindingName;
            this.userName = userName;
            this.password = password;
        }
    }
    
    private static class QueueInfo
    {
        private final QueueConnection conn;
        private final QueueSession session;
        private final QueueSender sender;
        
        public QueueInfo(final QueueConnection conn, final QueueSession session, final QueueSender sender) {
            this.conn = conn;
            this.session = session;
            this.sender = sender;
        }
    }
    
    private static class JMSQueueManagerFactory implements ManagerFactory<JMSQueueManager, FactoryData>
    {
        @Override
        public JMSQueueManager createManager(final String name, final FactoryData data) {
            try {
                final Context ctx = AbstractJMSManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
                final QueueInfo info = connect(ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, true);
                return new JMSQueueManager(name, ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, info);
            }
            catch (NamingException ex) {
                JMSQueueManager.LOGGER.error("Unable to locate resource", ex);
            }
            catch (Exception ex2) {
                JMSQueueManager.LOGGER.error("Unable to connect", ex2);
            }
            return null;
        }
    }
}
