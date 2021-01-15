package info.spicyclient.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetworkManager {
	
	public static NetworkManager networkManager;
	
	public static NetworkManager getNetworkManager() {
		
		if (networkManager == null) {
			
			networkManager = new NetworkManager();
			
		}
		
		return networkManager;
		
	}
	
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	// The code that is "unused" is used, eclipse just thinks that the request will never return null. It sometimes might, better safe than sorry
	@SuppressWarnings("unused")
	public String sendGet(HttpGet request) throws Exception {

        //HttpGet request = new HttpGet("https://www.google.com/search?q=mkyong");

        // add request headers
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                //System.out.println(result);
                return result;
            }else {
            	System.err.println("Failed to return get request... Returning error string instead");
            	return "An unknown error has occurred";
            }

        }catch (Exception e) {
        	
        	e.printStackTrace();
        	System.err.println("Failed to return get request... Returning error string instead");
        	return "An unknown error has occurred";
        	
		}

    }

    public String sendPost(HttpPost post, BasicNameValuePair... args) throws Exception {

    	//HttpPost post = new HttpPost("https://httpbin.org/post");

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        
        for (BasicNameValuePair basicNameValuePair : args) {
        	
        	urlParameters.add(basicNameValuePair);
        	
        }
        
        /*
        urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));
        */
        
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
        	
        	String stringResponse = EntityUtils.toString(response.getEntity());
        	
            //System.out.println(stringResponse);
            //return response;
            return stringResponse;
        }

    }
	
}
