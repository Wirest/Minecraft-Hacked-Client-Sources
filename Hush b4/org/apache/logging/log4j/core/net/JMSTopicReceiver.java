// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.TopicSubscriber;
import javax.jms.TopicSession;
import javax.jms.TopicConnection;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;

public class JMSTopicReceiver extends AbstractJMSReceiver
{
    public JMSTopicReceiver(final String tcfBindingName, final String topicBindingName, final String username, final String password) {
        try {
            final Context ctx = new InitialContext();
            final TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)this.lookup(ctx, tcfBindingName);
            final TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
            topicConnection.start();
            final TopicSession topicSession = topicConnection.createTopicSession(false, 1);
            final Topic topic = (Topic)ctx.lookup(topicBindingName);
            final TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            topicSubscriber.setMessageListener((MessageListener)this);
        }
        catch (JMSException e) {
            this.logger.error("Could not read JMS message.", (Throwable)e);
        }
        catch (NamingException e2) {
            this.logger.error("Could not read JMS message.", e2);
        }
        catch (RuntimeException e3) {
            this.logger.error("Could not read JMS message.", e3);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        if (args.length != 4) {
            usage("Wrong number of arguments.");
        }
        final String tcfBindingName = args[0];
        final String topicBindingName = args[1];
        final String username = args[2];
        final String password = args[3];
        new JMSTopicReceiver(tcfBindingName, topicBindingName, username, password);
        final Charset enc = Charset.defaultCharset();
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
        System.out.println("Type \"exit\" to quit JMSTopicReceiver.");
        String line;
        do {
            line = stdin.readLine();
        } while (line != null && !line.equalsIgnoreCase("exit"));
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }
    
    private static void usage(final String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + JMSTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
        System.exit(1);
    }
}
