// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.rxtx;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelConfig;
import gnu.io.CommPort;
import io.netty.channel.ChannelOption;
import gnu.io.CommPortIdentifier;
import java.net.SocketAddress;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import gnu.io.SerialPort;
import io.netty.channel.oio.OioByteStreamChannel;

public class RxtxChannel extends OioByteStreamChannel
{
    private static final RxtxDeviceAddress LOCAL_ADDRESS;
    private final RxtxChannelConfig config;
    private boolean open;
    private RxtxDeviceAddress deviceAddress;
    private SerialPort serialPort;
    
    public RxtxChannel() {
        super(null);
        this.open = true;
        this.config = new DefaultRxtxChannelConfig(this);
    }
    
    @Override
    public RxtxChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isOpen() {
        return this.open;
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new RxtxUnsafe();
    }
    
    @Override
    protected void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        final RxtxDeviceAddress remote = (RxtxDeviceAddress)remoteAddress;
        final CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(remote.value());
        final CommPort commPort = cpi.open(this.getClass().getName(), 1000);
        commPort.enableReceiveTimeout((int)this.config().getOption(RxtxChannelOption.READ_TIMEOUT));
        this.deviceAddress = remote;
        this.serialPort = (SerialPort)commPort;
    }
    
    protected void doInit() throws Exception {
        this.serialPort.setSerialPortParams((int)this.config().getOption(RxtxChannelOption.BAUD_RATE), this.config().getOption(RxtxChannelOption.DATA_BITS).value(), this.config().getOption(RxtxChannelOption.STOP_BITS).value(), this.config().getOption(RxtxChannelOption.PARITY_BIT).value());
        this.serialPort.setDTR((boolean)this.config().getOption(RxtxChannelOption.DTR));
        this.serialPort.setRTS((boolean)this.config().getOption(RxtxChannelOption.RTS));
        this.activate(this.serialPort.getInputStream(), this.serialPort.getOutputStream());
    }
    
    @Override
    public RxtxDeviceAddress localAddress() {
        return (RxtxDeviceAddress)super.localAddress();
    }
    
    @Override
    public RxtxDeviceAddress remoteAddress() {
        return (RxtxDeviceAddress)super.remoteAddress();
    }
    
    @Override
    protected RxtxDeviceAddress localAddress0() {
        return RxtxChannel.LOCAL_ADDRESS;
    }
    
    @Override
    protected RxtxDeviceAddress remoteAddress0() {
        return this.deviceAddress;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.open = false;
        try {
            super.doClose();
        }
        finally {
            if (this.serialPort != null) {
                this.serialPort.removeEventListener();
                this.serialPort.close();
                this.serialPort = null;
            }
        }
    }
    
    static {
        LOCAL_ADDRESS = new RxtxDeviceAddress("localhost");
    }
    
    private final class RxtxUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                final boolean wasActive = RxtxChannel.this.isActive();
                RxtxChannel.this.doConnect(remoteAddress, localAddress);
                final int waitTime = RxtxChannel.this.config().getOption(RxtxChannelOption.WAIT_TIME);
                if (waitTime > 0) {
                    RxtxChannel.this.eventLoop().schedule((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RxtxChannel.this.doInit();
                                AbstractUnsafe.this.safeSetSuccess(promise);
                                if (!wasActive && RxtxChannel.this.isActive()) {
                                    RxtxChannel.this.pipeline().fireChannelActive();
                                }
                            }
                            catch (Throwable t) {
                                AbstractUnsafe.this.safeSetFailure(promise, t);
                                AbstractUnsafe.this.closeIfClosed();
                            }
                        }
                    }, (long)waitTime, TimeUnit.MILLISECONDS);
                }
                else {
                    RxtxChannel.this.doInit();
                    this.safeSetSuccess(promise);
                    if (!wasActive && RxtxChannel.this.isActive()) {
                        RxtxChannel.this.pipeline().fireChannelActive();
                    }
                }
            }
            catch (Throwable t) {
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
            }
        }
    }
}
