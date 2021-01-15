package info.spicyclient.files;

import java.io.IOException;
import java.util.ArrayList;

public class AltInfo {
	
	public ArrayList<alt> alts = new ArrayList<AltInfo.alt>();
	
	public String API_Key = "api-xxxx-xxxx-xxxx";
	
	public void addAlt(String email, String password, boolean premium) {
		
		if (premium) {
			AltInfo.alt a = new alt(email, password, premium);
			this.alts.add(a);
			
			try {
				FileManager.saveAltInfo(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			AltInfo.alt a = new alt(email, password, premium);
			a.username = email;
			this.alts.add(a);
			
			try {
				FileManager.saveAltInfo(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void addCreatedAlt(alt a) {
		
		if (a.premium) {
			
			this.alts.add(a);
			
			try {
				FileManager.saveAltInfo(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			
			this.alts.add(a);
			
			try {
				FileManager.saveAltInfo(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static class alt{
		
		public String username = "Log in to view the username";
		public String email;
		public String password;
		public boolean premium;
		public int status = 0;
		
		
		public alt(String email, String password, boolean premium) {
			
			this.email = email;
			this.password = password;
			this.premium = premium;
			
		}
		
	}
	
}
