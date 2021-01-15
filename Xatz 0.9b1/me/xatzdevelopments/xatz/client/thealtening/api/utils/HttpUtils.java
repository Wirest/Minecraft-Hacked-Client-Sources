package me.xatzdevelopments.xatz.client.thealtening.api.utils;

import java.net.*;
import java.io.*;

public class HttpUtils
{
    protected String connect(final String link) throws IOException {
        final URL url = new URL(link);
        final InputStream inputStream = url.openStream();
        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }
}
