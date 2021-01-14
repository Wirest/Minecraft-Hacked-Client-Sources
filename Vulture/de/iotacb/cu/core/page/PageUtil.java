package de.iotacb.cu.core.page;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PageUtil {
	
	/**
	 * Returns the content of the given web page
	 * @param url
	 * @param httpClient
	 * @return
	 */
	public static final String getContentOfPage(final String url, HttpClient httpClient) {
		if (httpClient == null) {
			httpClient = HttpClients.createDefault();
		}
		
		final HttpGet get = new HttpGet(url);
		
		try {
			final HttpResponse response = httpClient.execute(get);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the content of the given web page
	 * @param url
	 * @return
	 */
	public static final String getContentOfPage(final String url) {
		return getContentOfPage(url, null);
	}
	
}
