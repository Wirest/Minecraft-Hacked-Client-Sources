package nivia.managers;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class StaffManager {
	
public static ArrayList<StaffMember> Smembers = new ArrayList<StaffMember>();
	
	public static void addStaffMember(String name){
		Smembers.add(new StaffMember(name));
	}	
	public static void deleteStaffMember(String name){
		Smembers.stream().filter(s -> s.getName().equalsIgnoreCase(name)).forEach(s -> Smembers.remove(s));
	}	
	public static boolean isStaff(String name){
		AtomicReference<Boolean> staff = new AtomicReference<>();
		Smembers.stream().filter(s -> s.getName().equalsIgnoreCase(name)).forEach(s ->  {
			staff.set(true);
			return;
		});
		return staff.get();
	}
	public static class StaffMember {
		
		private String name;
		
		
		public StaffMember(String name) {
			this.name = name;		
		}
		
		public String getName() {
			return name;
		}
				
		public void setName(String s) {
			name = s;
		}				
	}
}
