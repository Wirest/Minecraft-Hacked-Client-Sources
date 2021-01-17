package skyline.specc.extras.tabgui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import skyline.specc.render.modules.tabgui.main.TabGui;
import skyline.specc.render.modules.tabgui.main.TabObject;
import skyline.specc.render.modules.tabgui.main.TabPart;


public class TabPanel extends TabObject {

	public static int maxIndex = -1;
	private int index;

	public int speed = 1;
	private int initialHeight = 20;
	private int x = 2;

	private boolean visible = false;
	private TabGui tabGui;
	private List<TabPart> elements = new ArrayList<TabPart>();
	private TabSelectionBox selectionBox;

	public TabPanel(TabGui tabGui) {
		super(null);

		for (TabPanel panel : tabGui.getPanels()) {
			if (panel.getIndex() > maxIndex) {
				maxIndex = panel.getIndex();
			}
		}

		this.setIndex(maxIndex + 1);

		this.tabGui = tabGui;
		this.selectionBox = new TabSelectionBox(this);
		this.selectionBox.setY(initialHeight);
	}

	public List<TabPart> getElements() {
		return elements;
	}

	public void addElement(TabPart part) {
		elements.add(part);
	}

	public void setSelectedPart(TabPart part) {
		for (TabPart p : elements) p.setSelected(false);
		part.setSelected(true);
	}

	public TabPart getSelectedPart() {
		for (TabPart part : elements) {
			if (part.isSelected()) return part;
		}

		this.elements.get(0).setSelected(true);

		return this.elements.get(0);
	}

	@Override
	public void onKeyPress(int key) {
		TabPart part = this.getSelectedPart();

		int partIndex = elements.indexOf(part);

		if (key == Keyboard.KEY_UP) {
			partIndex--;
			if (partIndex < 0) {
				partIndex = elements.size() - 1;
				speed = 20;
			} else {
				speed = 3;
			}
		} else if (key == Keyboard.KEY_DOWN) {
			partIndex++;
			if (partIndex > elements.size() - 1) {
				partIndex = 0;
				speed = 20;
			} else {
				speed = 3;
			}

		} else if (key == Keyboard.KEY_LEFT) {
			if (this.getIndex() != 0) {
				this.setVisible(false);
				tabGui.removePanel(this);
			}
		}
		part.onKeyPress(key);

		setSelectedPart(elements.get(partIndex));
	}

	public void onUpdate() {
		int y = this.getSelectedPart().getY();

		for (int i = 0; i <= speed; i++) {
			if (this.selectionBox.getY() > y) {
				this.selectionBox.setY(this.selectionBox.getY() - 2);
			}

			if (this.selectionBox.getY() < y) {
				this.selectionBox.setY(this.selectionBox.getY() + 2);
			}
		}
	}

	public void renderPanel() {
		if (!isVisible()) return;

		int baseX = x;

		for (TabPanel panel : tabGui.getPanels()) {
			if (panel.getIndex() < this.index) {
				baseX += panel.getWidth() + 4;
			}
		}

		translate(baseX, initialHeight, false);
		this.tabGui.getTabTheme().renderElement(this);
		translate(baseX, initialHeight, true);

		int y = this.initialHeight;

		for (TabPart part : this.elements) {
			part.setY(y);
			y += this.tabGui.getTabTheme().getFontRenderer().getHeight() + 3;
		}

		translate(baseX, this.selectionBox.getY(), false);
		this.tabGui.getTabTheme().renderElement(selectionBox);
		translate(baseX, this.selectionBox.getY(), true);

		for (TabPart part : this.elements) {
			translate(baseX, part.getY(), false);
			tabGui.getTabTheme().renderElement(part);
			translate(baseX, part.getY(), true);
		}
	}

	public int getHeight() {
		int height = 0;

		for (TabPart part : elements) {
			height += this.tabGui.getTabTheme().getFontRenderer().getHeight() + 3;
		}

		return height;
	}

	public int getWidth() {
		int maxWidth = 0;

		for (TabPart part : elements) {
			int width = this.tabGui.getTabTheme().getFontRenderer().getStringWidth(part.getText());
			if (width > maxWidth) {
				maxWidth = width;
			}
		}

		return maxWidth + 12;
	}

	public boolean isVisible() {
		return visible;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public TabGui getTabGui() {
		return tabGui;
	}

	public static void translate(float x, float y, boolean restore) {
		if (restore) {
			x *= -1;
			y *= -1;
		}
		GL11.glTranslatef(x, y, 0);
	}

}
