package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.RadioButton;
import org.darkstorm.minecraft.gui.listener.ComponentListener;
import org.darkstorm.minecraft.gui.listener.SelectableComponentListener;

public class BasicRadioButton extends BasicButton implements RadioButton {
	private boolean selected = false;

	public BasicRadioButton() {
	}

	public BasicRadioButton(String text) {
		this.text = text;
	}

	@Override
	public void press() {
		selected = true;
		for (Button button : getGroup().getButtons()) {
			if (equals(button) || !(button instanceof RadioButton))
				continue;
			((RadioButton) button).setSelected(false);
		}
		super.press();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		for (ComponentListener listener : getListeners()) {
			if (listener instanceof SelectableComponentListener) {
				try {
					((SelectableComponentListener) listener).onSelectedStateChanged(this);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	@Override
	public void addSelectableComponentListener(SelectableComponentListener listener) {
		addListener(listener);
	}

	@Override
	public void removeSelectableComponentListener(SelectableComponentListener listener) {
		removeListener(listener);
	}
}
