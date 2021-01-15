package net.minecraft.network;

import me.xatzdevelopments.xatz.client.main.Xatz;

public abstract class AbstractPacket<T extends INetHandler> implements Packet<T> {

	protected boolean cancelled = false;
	public boolean crit = false;

	@Override
	public void processPacket(T handler) {
		Xatz.onPacketRecieved(this);
	}

	public void cancel() {
		this.cancelled = true;
	}

	public AbstractPacket setCrit(boolean crit) {
		this.crit = crit;
		return this;
	}
}
