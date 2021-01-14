package me.memewaredevs.proxymanager;

import io.netty.channel.socket.oio.OioSocketChannel;

import java.lang.reflect.Method;
import java.net.*;

public class ProxyAdapter implements io.netty.bootstrap.ChannelFactory<OioSocketChannel> {

	private Proxy proxy;

	public ProxyAdapter(Proxy proxy) {
		this.proxy = proxy;
		XD2 = true;
	}

	boolean XD2;

	@Override
	public OioSocketChannel newChannel() {
		Socket socks = new Socket(this.proxy);
		try {
			Method m = socks.getClass().getDeclaredMethod("getImpl");
			m.setAccessible(true);
			Object sd = m.invoke(socks);
			m = sd.getClass().getDeclaredMethod("setV4");
			m.setAccessible(true);
			m.invoke(sd);
			return new OioSocketChannel(socks);
		} catch (Exception dyzapisfuckinggay) {
		}
		return new OioSocketChannel(socks);
	}
}
