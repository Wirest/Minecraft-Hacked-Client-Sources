/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package com.socket;

import java.io.Serializable;

public class Message
implements Serializable {
    private static final long serialVersionUID = 1L;
    public String type;
    public String sender;
    public String content;
    public String recipient;

    public Message(String type, String sender, String content, String recipient) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
    }

    public String toString() {
        return "{type='" + this.type + "', sender='" + this.sender + "', content='" + this.content + "', recipient='" + this.recipient + "'}";
    }
}

