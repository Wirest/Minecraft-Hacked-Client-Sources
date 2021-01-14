package com.etb.client.friend;

public class Friend {
	private String name, alias;

	public Friend(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public Friend(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getAlias() {
		return this.alias;
	}
}
