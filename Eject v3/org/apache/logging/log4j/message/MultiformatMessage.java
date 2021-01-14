package org.apache.logging.log4j.message;

public abstract interface MultiformatMessage
        extends Message {
    public abstract String getFormattedMessage(String[] paramArrayOfString);

    public abstract String[] getFormats();
}




