/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Mod;

import Blizzard.Category.Category;
import Blizzard.Event.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;

public class Mod {
	public static Minecraft mc = Minecraft.getMinecraft();
	private String name;
	private String ArrayListName;
	private int key;
	private boolean toggled;
	private String[] alias;
	private Category category;

	public Mod(String n, String an, int var1, Category c) {
		this.name = n;
		this.ArrayListName = an;
		this.key = var1;
		this.category = c;
		this.toggled = false;
	}

	public void toggle() {
		this.onToggle();
		this.toggled = !this.toggled;
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}

	public void onEnable() {
		EventManager.register(this);
	}

	public void onDisable() {
		EventManager.unregister(this);
	}

	public void onUpdate() {
	}

	public void onRender() {
	}

	public void onToggle() {
	}

	public Minecraft getMc() {
		return mc;
	}

	public void setMc(Minecraft mc) {
		Mod.mc = mc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String GetArrayListName() {
		return this.ArrayListName;
	}

	public void setArrayListName(String ArrayListName) {
		this.ArrayListName = ArrayListName;
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public Category getCategory() {
		return this.category;
	}

	public boolean onSendChatMessage(String s) {
		return true;
	}

	public boolean onReciveChatMessage(S02PacketChat packet) {
		return true;
	}

	public final boolean isCategory(Category s) {
		if (s == this.category) {
			return true;
		}
		return false;
	}

	public String[] getAlias() {
		return this.alias;
	}
}
