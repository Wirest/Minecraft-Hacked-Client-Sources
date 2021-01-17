/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonObject
 *  delta.OVYt$968L
 *  org.apache.http.HttpEntity
 *  org.apache.http.HttpResponse
 *  org.apache.http.client.entity.UrlEncodedFormEntity
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClientBuilder
 *  org.apache.http.message.BasicNameValuePair
 *  org.apache.http.util.EntityUtils
 */
package delta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import delta.Class14;
import delta.Class23;
import delta.OVYt;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Class179 {
    private static Gson senegal$;
    private static String almost$;
    private static String[] cheap$;
    private static String unions$;

    public static void _bookmark() throws Exception {
        String string = unions$ + almost$ + OVYt.968L.FS1x((String)cheap$[16], (int)-855871329);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(string);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[17], (int)-769538220), Class14._guyana()));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList, OVYt.968L.FS1x((String)cheap$[18], (int)-1402481774)));
        closeableHttpClient.execute((HttpUriRequest)httpPost);
    }

    private static void _cycles() {
        cheap$ = new String[]{"\u49b7\u49fd\u49a3\u49bb\u49a3", "\ub06c", "\u1433", "\u7a77", "\uabec\uabed\uabff\uab94\uab81", "\uc502\uc54f\uc511\uc509\uc511", "", "\u8551", "\udc09", "\ud30e\ud30f\ud31d\ud376\ud363", "\ue68c", "\uc8a8", "\ua9d7", "", "\u0f92", "\uc921\uc930\uc929\uc96f\uc936\uc973\uc96f", "\u70fa\u70b1\u70ef\u70f7\u70ef", "\uc735", "\ud3c7\ud3c6\ud3d4\ud3bf\ud3aa", "\u9f94\u9fdb\u9f85\u9f9d\u9f85", "\ufcfa", "\ubb9f\ubb9e\ubb8c\ubbe7\ubbf2", "\uf60f", "\u2a60", "\ufb9d\ufbd1\ufb8f\ufb97\ufb8f", "\u19a1", "\u446e\u446f\u447d\u4416\u4403", "\u7261", "\u2cb6"};
    }

    public static boolean _badly(String string) throws Exception {
        String string2;
        JsonObject jsonObject;
        String string3 = unions$ + almost$ + OVYt.968L.FS1x((String)cheap$[5], (int)1646773601);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(string3);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        if (string != OVYt.968L.FS1x((String)cheap$[6], (int)1586138185)) {
            arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[7], (int)-1335851728), string));
        }
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[8], (int)-2100765589), Class14._guyana()));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList, OVYt.968L.FS1x((String)cheap$[9], (int)1268044635)));
        HttpResponse httpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null && (jsonObject = (JsonObject)senegal$.fromJson(string2 = EntityUtils.toString((HttpEntity)httpEntity), JsonObject.class)).has(OVYt.968L.FS1x((String)cheap$[10], (int)-2129008915))) {
            return Boolean.parseBoolean(jsonObject.get(OVYt.968L.FS1x((String)cheap$[11], (int)-444020535)).getAsString());
        }
        return 214 - 256 + 64 - 19 + -3;
    }

    static {
        Class179._cycles();
        unions$ = Class23._bunch();
        almost$ = (unions$.endsWith(OVYt.968L.FS1x((String)cheap$[12], (int)-625104392)) ? OVYt.968L.FS1x((String)cheap$[13], (int)1090610546) : OVYt.968L.FS1x((String)cheap$[14], (int)-1419702339)) + OVYt.968L.FS1x((String)cheap$[15], (int)-1207187136);
        senegal$ = new GsonBuilder().serializeNulls().create();
    }

    public static boolean _chronic() throws Exception {
        String string;
        JsonObject jsonObject;
        String string2 = unions$ + almost$ + OVYt.968L.FS1x((String)cheap$[19], (int)-504782859);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(string2);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[20], (int)-329450341), Class14._guyana()));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList, OVYt.968L.FS1x((String)cheap$[21], (int)295680970)));
        HttpResponse httpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null && (jsonObject = (JsonObject)senegal$.fromJson(string = EntityUtils.toString((HttpEntity)httpEntity), JsonObject.class)).has(OVYt.968L.FS1x((String)cheap$[22], (int)996734574))) {
            return Boolean.parseBoolean(jsonObject.get(OVYt.968L.FS1x((String)cheap$[23], (int)-647747071)).getAsString());
        }
        return 41 - 73 + 21 - 5 + 16;
    }

    public static boolean _craft() throws Exception {
        String string;
        JsonObject jsonObject;
        String string2 = unions$ + almost$ + OVYt.968L.FS1x((String)cheap$[24], (int)919534591);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(string2);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[25], (int)104864192), Class14._guyana()));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList, OVYt.968L.FS1x((String)cheap$[26], (int)1848984635)));
        HttpResponse httpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null && (jsonObject = (JsonObject)senegal$.fromJson(string = EntityUtils.toString((HttpEntity)httpEntity), JsonObject.class)).has(OVYt.968L.FS1x((String)cheap$[27], (int)-1282248192))) {
            return Boolean.parseBoolean(jsonObject.get(OVYt.968L.FS1x((String)cheap$[28], (int)-486789929)).getAsString());
        }
        return 215 - 410 + 169 + 26;
    }

    public static void _seeds(String string, String string2) throws Exception {
        String string3 = unions$ + almost$ + OVYt.968L.FS1x((String)cheap$[0], (int)961038803);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(string3);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[1], (int)-780619763), string));
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[2], (int)315888721), string2));
        arrayList.add(new BasicNameValuePair(OVYt.968L.FS1x((String)cheap$[3], (int)-1351386604), Class14._guyana()));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList, OVYt.968L.FS1x((String)cheap$[4], (int)1163701177)));
        closeableHttpClient.execute((HttpUriRequest)httpPost);
    }
}

