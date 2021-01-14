package io.netty.channel;

public final class ChannelMetadata {
    private final boolean hasDisconnect;

    public ChannelMetadata(boolean paramBoolean) {
        this.hasDisconnect = paramBoolean;
    }

    public boolean hasDisconnect() {
        return this.hasDisconnect;
    }
}




