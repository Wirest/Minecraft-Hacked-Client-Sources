package info.spicyclient.notifications;

public enum Color {
	
	RED(0xffff6052, "Red"),
	GREEN(0xff52ff8f, "Green"),
	BLUE(0xff52baff, "Blue"),
	YELLOW(0xffffe852, "Yellow"),
	PINK(0xffff6bfd, "Pink");
	
	public int color;
	public String fileSuffix;
	
	Color(int color, String fileSuffix){
		
		this.color = color;
		this.fileSuffix = fileSuffix;
		
	}
	
}
