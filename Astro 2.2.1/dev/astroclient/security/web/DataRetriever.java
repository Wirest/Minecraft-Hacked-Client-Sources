package dev.astroclient.security.web;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class DataRetriever {

	protected final static Gson gson = new Gson();

	public JsonObject retrieveData(String url) throws AstroUserParsingException {
		String response;
		JsonObject jsonObject;
		try {
			response = connect(url);
			jsonObject = gson.fromJson(response, JsonObject.class);
		} catch (IOException e) {
			throw new AstroUserParsingException("IO", e.getCause());
		}

		if (jsonObject == null) {
			throw new AstroUserParsingException("JSON - Parsing error: \n" + response);
		}

		if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
			throw new AstroUserParsingException("Connection - Bad response");
		}

		return jsonObject;
	}

	protected boolean isSuccess(JsonObject jsonObject) {
		return jsonObject.has("success")
				&& jsonObject.get("success").getAsBoolean();
	}

	public String connect(String link) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();

		URLConnection connection = new URL(link).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		InputStream connectionStream = connection.getInputStream();

		String encodingId = connection.getContentEncoding();
		Charset encoding;
		try {
			encoding = encodingId == null ? StandardCharsets.UTF_8 : Charset.forName(encodingId);
		} catch (UnsupportedCharsetException ex) {
			encoding = StandardCharsets.UTF_8;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connectionStream, encoding))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder
						.append(line)
						.append("\n");
			}
		}

		return stringBuilder.toString();
	}
}