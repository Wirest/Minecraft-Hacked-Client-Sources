// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.sctp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import com.sun.nio.sctp.MessageInfo;
import io.netty.buffer.DefaultByteBufHolder;

public final class SctpMessage extends DefaultByteBufHolder
{
    private final int streamIdentifier;
    private final int protocolIdentifier;
    private final MessageInfo msgInfo;
    
    public SctpMessage(final int protocolIdentifier, final int streamIdentifier, final ByteBuf payloadBuffer) {
        super(payloadBuffer);
        this.protocolIdentifier = protocolIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.msgInfo = null;
    }
    
    public SctpMessage(final MessageInfo msgInfo, final ByteBuf payloadBuffer) {
        super(payloadBuffer);
        if (msgInfo == null) {
            throw new NullPointerException("msgInfo");
        }
        this.msgInfo = msgInfo;
        this.streamIdentifier = msgInfo.streamNumber();
        this.protocolIdentifier = msgInfo.payloadProtocolID();
    }
    
    public int streamIdentifier() {
        return this.streamIdentifier;
    }
    
    public int protocolIdentifier() {
        return this.protocolIdentifier;
    }
    
    public MessageInfo messageInfo() {
        return this.msgInfo;
    }
    
    public boolean isComplete() {
        return this.msgInfo == null || this.msgInfo.isComplete();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SctpMessage sctpFrame = (SctpMessage)o;
        return this.protocolIdentifier == sctpFrame.protocolIdentifier && this.streamIdentifier == sctpFrame.streamIdentifier && this.content().equals(sctpFrame.content());
    }
    
    @Override
    public int hashCode() {
        int result = this.streamIdentifier;
        result = 31 * result + this.protocolIdentifier;
        result = 31 * result + this.content().hashCode();
        return result;
    }
    
    @Override
    public SctpMessage copy() {
        if (this.msgInfo == null) {
            return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().copy());
        }
        return new SctpMessage(this.msgInfo, this.content().copy());
    }
    
    @Override
    public SctpMessage duplicate() {
        if (this.msgInfo == null) {
            return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().duplicate());
        }
        return new SctpMessage(this.msgInfo, this.content().copy());
    }
    
    @Override
    public SctpMessage retain() {
        super.retain();
        return this;
    }
    
    @Override
    public SctpMessage retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public String toString() {
        if (this.refCnt() == 0) {
            return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=(FREED)}";
        }
        return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=" + ByteBufUtil.hexDump(this.content()) + '}';
    }
}
