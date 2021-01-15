package dev.astroclient.client.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImgurUtil {

    public static final String CLIENT_ID;

    static {
        CLIENT_ID = "db711c715a9abaa";
    }

    public static String post(String fileDir, String clientID) {
        String address = "https://api.imgur.com/3/image";
        HttpPost post = new HttpPost(address);
        HttpClient httpClient = HttpClientBuilder.create().build();
        File file = new File(fileDir);

        try {
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArray);

            byte[] byteImage = byteArray.toByteArray();
            String dataImage = new Base64().encodeAsString(byteImage);

            post.addHeader("Authorization", "Client-ID " + clientID);

            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("image", dataImage));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();

            while (rd.readLine() != null) {
                sb.append(" : ").append(rd.readLine());
            }

            return sb.toString();

        } catch (Exception e) {
            return "Failed to upload image!";
        }
    }
}
