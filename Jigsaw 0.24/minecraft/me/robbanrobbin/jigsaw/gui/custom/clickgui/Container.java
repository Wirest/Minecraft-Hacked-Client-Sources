package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.util.ArrayList;

public abstract class Container extends Component {
	
	protected boolean extended = true;
	protected ArrayList<Component> children = new ArrayList<Component>();
	
	public ArrayList<Component> getChildren() {
		return children;
	}
	
	public void addChild(Component comp) {
		comp.setParent(this);
		comp.setX(comp.getX() + getBorderX());
		children.add(comp);
	}
	
	public void removeChild(Component child) {
		child.setParent(null);
		children.remove(child);
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	@Override
	public void update() {
		if(!extended) {
			return;
		}
		for(Component child : children) {
			child.update();
		}
	}
	
	@Override
	public void draw() {
		if(!extended) {
			return;
		}
		for(Component child : children) {
			child.draw();
		}
	}
	
	public void setExtended(boolean extended) {
		this.extended = extended;
		this.setHeight(this.getPreferedHeight());
		this.layoutChildren();
	}
	
	public void layoutChildren() {
		setWidth(getPreferedWidth());
		setHeight(getPreferedHeight());
		justLayoutChildren();
	}
	
	public void justLayoutChildren() {
		for(Component child : children) {
			
		}
		for(Component child : children) {
			child.setWidth(getWidth() - getBorderX() * 2.0);
			child.setHeight(child.getPreferedHeight());
			child.setX(getBorderX());
			if(child instanceof Container) {
				((Container) child).justLayoutChildren();
			}
		}
		if(this.getParent() != null) {
			//this.getParent().layoutChildren();
		}
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(!extended) {
			return;
		}
		for(Component comp : children) {
			if(x > comp.getX() && x < comp.getX() + comp.getWidth()
				&& y > comp.getY() && y < comp.getY() + comp.getHeight()) {
				comp.onClicked(x - comp.getX(), y - comp.getY(), button);
			}
		}
	}
	
	@Override
	public void onReleased(double x, double y, int button) {
		super.onReleased(x, y, button);
		if(!extended) {
			return;
		}
		for(Component comp : children) {
			if(x > comp.getX() && x < comp.getX() + comp.getWidth()
				&& y > comp.getY() && y < comp.getY() + comp.getHeight()) {
				comp.onReleased(x - comp.getX(), y - comp.getY(), button);
			}
		}
	}
	
	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		super.onDragged(x, y, mx, my, button);
		for(Component child : children) {
			if(mx >= child.getX() && mx < child.getWidth() + child.getX() + 1
				&& my >= child.getY() && my < child.getHeight() + child.getY() + 1) {
				child.onDragged(x, y, mx - child.getX(), my - child.getY(), button);
			}
		}
	}
	
	@Override
	public void setWidth(double width) {
		if(width <= getPreferedWidth()) {
			width = getPreferedWidth();
		}
		super.setWidth(width);
	}
	
	@Override
	public void onReleased(int button) {
		super.onReleased(button);
		for(Component comp : children) {
			comp.onReleased(button);
		}
	}
	
	@Override
	public void setHeight(double height) {
		super.setHeight(height);
	}
	
	public double getBorderX() {
		return 2;
	}
	
	public double getBorderY() {
		return 0.0;
	}
	
}
