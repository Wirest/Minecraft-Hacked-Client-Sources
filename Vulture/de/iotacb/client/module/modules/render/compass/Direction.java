package de.iotacb.client.module.modules.render.compass;

public class Direction {
	
	private final String direction;
	private final int type;
	
	public Direction(String direction, int type) {
		this.direction = direction;
		this.type = type;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public int getType() {
		return type;
	}

}
