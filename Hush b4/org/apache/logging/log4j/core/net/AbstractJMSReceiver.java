// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.Context;
import javax.jms.JMSException;
import org.apache.logging.log4j.core.LogEvent;
import javax.jms.ObjectMessage;
import javax.jms.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.jms.MessageListener;
import org.apache.logging.log4j.core.AbstractServer;

public abstract class AbstractJMSReceiver extends AbstractServer implements MessageListener
{
    protected Logger logger;
    
    public AbstractJMSReceiver() {
        this.logger = LogManager.getLogger(this.getClass().getName());
    }
    
    public void onMessage(final Message message) {
        try {
            if (message instanceof ObjectMessage) {
                final ObjectMessage objectMessage = (ObjectMessage)message;
                this.log((LogEvent)objectMessage.getObject());
            }
            else {
                this.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
            }
        }
        catch (JMSException jmse) {
            this.logger.error("Exception thrown while processing incoming message.", (Throwable)jmse);
        }
    }
    
    protected Object lookup(final Context ctx, final String name) throws NamingException {
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            this.logger.error("Could not find name [" + name + "].");
            throw e;
        }
    }
}
