// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Serializable;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.Context;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractJMSManager extends AbstractManager
{
    public AbstractJMSManager(final String name) {
        super(name);
    }
    
    protected static Context createContext(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials) throws NamingException {
        final Properties props = getEnvironment(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials);
        return new InitialContext(props);
    }
    
    protected static Object lookup(final Context ctx, final String name) throws NamingException {
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            AbstractJMSManager.LOGGER.warn("Could not find name [" + name + "].");
            throw e;
        }
    }
    
    protected static Properties getEnvironment(final String factoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipalName, final String securityCredentials) {
        final Properties props = new Properties();
        if (factoryName != null) {
            props.put("java.naming.factory.initial", factoryName);
            if (providerURL != null) {
                props.put("java.naming.provider.url", providerURL);
            }
            else {
                AbstractJMSManager.LOGGER.warn("The InitialContext factory name has been provided without a ProviderURL. This is likely to cause problems");
            }
            if (urlPkgPrefixes != null) {
                props.put("java.naming.factory.url.pkgs", urlPkgPrefixes);
            }
            if (securityPrincipalName != null) {
                props.put("java.naming.security.principal", securityPrincipalName);
                if (securityCredentials != null) {
                    props.put("java.naming.security.credentials", securityCredentials);
                }
                else {
                    AbstractJMSManager.LOGGER.warn("SecurityPrincipalName has been set without SecurityCredentials. This is likely to cause problems.");
                }
            }
            return props;
        }
        return null;
    }
    
    public abstract void send(final Serializable p0) throws Exception;
    
    public synchronized void send(final Serializable object, final Session session, final MessageProducer producer) throws Exception {
        try {
            Message msg;
            if (object instanceof String) {
                msg = (Message)session.createTextMessage();
                ((TextMessage)msg).setText((String)object);
            }
            else {
                msg = (Message)session.createObjectMessage();
                ((ObjectMessage)msg).setObject(object);
            }
            producer.send(msg);
        }
        catch (JMSException ex) {
            AbstractJMSManager.LOGGER.error("Could not publish message via JMS " + this.getName());
            throw ex;
        }
    }
}
