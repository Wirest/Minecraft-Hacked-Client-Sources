package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.AbstractComponent;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.ButtonGroup;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.ComponentListener;

import me.robbanrobbin.jigsaw.module.Module;

public class BasicButton extends AbstractComponent implements Button {
	protected String text = "";
	protected ButtonGroup group;
	protected Module gluedModule;

	public BasicButton() {
	}

	public BasicButton(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void press() {
		for (ComponentListener listener : getListeners())
			((ButtonListener) listener).onButtonPress(this);
	}

	@Override
	public void rightPress() {
		for (ComponentListener listener : getListeners())
			((ButtonListener) listener).onRightButtonPress(this);
	}

	@Override
	public void addButtonListener(ButtonListener listener) {
		addListener(listener);
	}

	@Override
	public void removeButtonListener(ButtonListener listener) {
		removeListener(listener);
	}

	@Override
	public ButtonGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(ButtonGroup group) {
		this.group = group;
	}

	public Module getModule() {
		return gluedModule;
	}

	public void setModule(Module mod) {
		this.gluedModule = mod;
	}
}
