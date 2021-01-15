package net.mcleaks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModApi {
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	private static final String API_URL = "http://auth.mcleaks.net/v1/";
	private static final Gson gson = new Gson();

	public static void redeem(final String token, final Callback<Object> callback) {
		String url = "http://auth.mcleaks.net/v1/redeem";

		EXECUTOR_SERVICE.execute(new Runnable() {
			public void run() {
				URLConnection connection = ModApi.preparePostRequest("http://auth.mcleaks.net/v1/redeem",
						"{\"token\":\"" + token + "\"}");
				if (connection == null) {
					callback.done("An error occured! [R1]");
					return;
				}
				Object o = ModApi.getResult(connection);
				if ((o instanceof String)) {
					callback.done(o);
					return;
				}
				JsonObject jsonObject = (JsonObject) o;
				if ((!jsonObject.has("mcname")) || (!jsonObject.has("session"))) {
					callback.done("An error occured! [R2]");
					return;
				}
				RedeemResponse response = new RedeemResponse();
				response.setMcName(jsonObject.get("mcname").getAsString());
				response.setSession(jsonObject.get("session").getAsString());

				callback.done(response);
			}
		});
	}

	private static URLConnection preparePostRequest(String url, String body) {
		try {
			HttpURLConnection con;
			if (url.toLowerCase().startsWith("https://")) {
				con = (HttpsURLConnection) new URL(url).openConnection();
			} else {
				con = (HttpURLConnection) new URL(url).openConnection();
			}
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);

			con.setRequestMethod("POST");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(body.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object getResult(URLConnection urlConnection) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();

			JsonElement jsonElement = (JsonElement) gson.fromJson(result.toString(), JsonElement.class);

			System.out.println(result.toString());
			if ((!jsonElement.isJsonObject()) || (!jsonElement.getAsJsonObject().has("success"))) {
				return "An error occured! [G1]";
			}
			if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
				return jsonElement.getAsJsonObject().has("errorMessage")
						? jsonElement.getAsJsonObject().get("errorMessage").getAsString() : "An error occured! [G4]";
			}
			if (!jsonElement.getAsJsonObject().has("result")) {
				return "An error occured! [G3]";
			}
			return jsonElement.getAsJsonObject().get("result").isJsonObject()
					? jsonElement.getAsJsonObject().get("result").getAsJsonObject() : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "An error occured! [G2]";
	}
}
