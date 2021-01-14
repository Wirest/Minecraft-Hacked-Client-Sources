package splash.utilities.web;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Author: Ice
 * Created: 00:54, 30-May-20
 * Project: Client
 */
public class InterwebsUtils {

    public static ArrayList<String> getInArray(String url) throws IOException {
        ArrayList<String> read = new ArrayList<>();
        BufferedReader reader = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));


            String line = reader.readLine();
            while (line != null) {
                read.add(line);
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
    }

    public static String getContent(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        con.disconnect();
        in.close();

        return response.toString();
    }
}