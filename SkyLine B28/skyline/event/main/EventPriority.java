package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main;

public enum EventPriority {

	LOWEST, LOW, NORMAL, HIGH, HIGHEST;

	public int getPriority(){
		switch (this) {
			case LOWEST:
				return 0;
			case LOW:
				return 1;
			case NORMAL:
				return 2;
			case HIGH:
				return 3;
			case HIGHEST:
				return 4;
			default:
				return 2;
		}
	}

	public EventPriority getPriorityFromInt(int priority){
		switch (priority) {
			case 0:
				return LOWEST;
			case 1:
				return LOW;
			case 2:
				return NORMAL;
			case 3:
				return HIGH;
			case 4:
				return HIGHEST;
			default:
				return NORMAL;
		}
	}

}
