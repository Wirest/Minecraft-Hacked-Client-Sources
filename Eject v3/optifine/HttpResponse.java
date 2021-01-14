package optifine;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private int status = 0;
    private String statusLine = null;
    private Map<String, String> headers = new LinkedHashMap();
    private byte[] body = null;

    public HttpResponse(int paramInt, String paramString, Map paramMap, byte[] paramArrayOfByte) {
        this.status = paramInt;
        this.statusLine = paramString;
        this.headers = paramMap;
        this.body = paramArrayOfByte;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatusLine() {
        return this.statusLine;
    }

    public Map getHeaders() {
        return this.headers;
    }

    public String getHeader(String paramString) {
        return (String) this.headers.get(paramString);
    }

    public byte[] getBody() {
        return this.body;
    }
}




