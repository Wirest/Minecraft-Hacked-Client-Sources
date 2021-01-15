package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.AbstractContainer;
import org.darkstorm.minecraft.gui.component.Panel;
import org.darkstorm.minecraft.gui.layout.LayoutManager;

public class BasicPanel extends AbstractContainer implements Panel {
	public BasicPanel() {
	}

	public BasicPanel(LayoutManager layoutManager) {
		setLayoutManager(layoutManager);
	}
}
