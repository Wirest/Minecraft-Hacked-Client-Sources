// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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
