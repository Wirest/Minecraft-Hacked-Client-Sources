package me.xatzdevelopments.xatz.client;

import java.util.ArrayList;

public class AutoBuildConf {

	public String name;

	public AutoBuildConf(String name) {
		this.name = name;
		blocks = new ArrayList<AutoBuildBlock>();
	}

	public ArrayList<AutoBuildBlock> blocks;

}
