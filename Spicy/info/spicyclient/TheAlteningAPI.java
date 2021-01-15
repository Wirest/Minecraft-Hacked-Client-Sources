package info.spicyclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
public class TheAlteningAPI {
	
	public static void getAccountAndLogin() {
		
		
		try {
			TheAlteningAPI.call_me();
	    }
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
		   
	public static JSONObject call_me() throws Exception {
	     String url = "http://api.thealtening.com/v2/generate?key=" + SpicyClient.altInfo.API_Key;
	     URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("GET");
	     //add request header
	     con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     int responseCode = con.getResponseCode();
	     //System.out.println("\nSending 'GET' request to URL : " + url);
	     //System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     //print in String
	     //System.out.println(response.toString());
	     //Read JSON response and print
	     JSONObject myResponse = new JSONObject(response.toString());
	     
	     // Return the account info
	     return myResponse;
	     
	     // Login to the account
	     //SessionChanger.getInstance().setUser(myResponse.getString("token"), myResponse.getString("password"));
	}
}