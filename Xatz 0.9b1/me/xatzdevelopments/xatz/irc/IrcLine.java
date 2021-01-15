package me.xatzdevelopments.xatz.irc;

public class IrcLine {
	private int		index;
	private static String	line;
	private static String	sender;
	private boolean	read;

	public IrcLine(int index, String line, String sender, boolean read) {
		this.index = index;
		this.line = line;
		this.sender = sender;
		this.read = read;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public static String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
