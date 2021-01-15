package info.spicyclient;

import java.util.UUID;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;

import info.spicyclient.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionChanger {

	private static SessionChanger instance;
	private final UserAuthentication auth;

	public static SessionChanger getInstance() {
		if (instance == null) {
			instance = new SessionChanger();
		}

		return instance;
	}
	
	//Creates a new Authentication Service. 
	private SessionChanger() {
		UUID notSureWhyINeedThis = UUID.randomUUID(); //Idk, needs a UUID. Seems to be fine making it random
		AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), notSureWhyINeedThis.toString());
		auth = authService.createUserAuthentication(Agent.MINECRAFT);
		authService.createMinecraftSessionService();
	}
	
	//Online mode
	//Checks if your already logged in to the account.
	public void setUser(String email, String password) {
		if(!Minecraft.getMinecraft().getSession().getUsername().equals(email) || Minecraft.getMinecraft().getSession().getToken().equals("0")){

			this.auth.logOut();
			this.auth.setUsername(email);
			this.auth.setPassword(password);
			try {
				this.auth.logIn();
				Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
				setSession(session);
				
				if (SpicyClient.account.loggedIn) {
					try {
						JSONObject response = new JSONObject(NetworkManager.getNetworkManager().sendPost(new HttpPost("http://SpicyClient.info/api/accountApi.php"), new BasicNameValuePair("type", "updateCurrentAlt"), new BasicNameValuePair("session", SpicyClient.account.session), new BasicNameValuePair("alt", Minecraft.getMinecraft().session.getUsername())));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}

	//Sets the session.
	//You need to make this public, and remove the final modifier on the session Object.
	private void setSession(Session session) {
		Minecraft.getMinecraft().session = session;
	}

	//Login offline mode
	//Just like MCP does
	public void setUserOffline(String username) {
		this.auth.logOut();
		Session session = new Session(username, username, "0", "legacy");
		setSession(session);
		
		try {
			if (SpicyClient.account.loggedIn) {
				JSONObject response = new JSONObject(NetworkManager.getNetworkManager().sendPost(new HttpPost("http://SpicyClient.info/api/accountApi.php"), new BasicNameValuePair("type", "updateCurrentAlt"), new BasicNameValuePair("session", SpicyClient.account.session), new BasicNameValuePair("alt", Minecraft.getMinecraft().session.getUsername())));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}