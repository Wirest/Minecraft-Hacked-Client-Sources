// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.io.InputStream;
import java.util.List;
import java.util.Collections;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.ibm.icu.impl.ICUData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class ResourceBasedPeriodFormatterDataService extends PeriodFormatterDataService
{
    private Collection<String> availableLocales;
    private PeriodFormatterData lastData;
    private String lastLocale;
    private Map<String, PeriodFormatterData> cache;
    private static final String PATH = "data/";
    private static final ResourceBasedPeriodFormatterDataService singleton;
    
    public static ResourceBasedPeriodFormatterDataService getInstance() {
        return ResourceBasedPeriodFormatterDataService.singleton;
    }
    
    private ResourceBasedPeriodFormatterDataService() {
        this.lastData = null;
        this.lastLocale = null;
        this.cache = new HashMap<String, PeriodFormatterData>();
        final List<String> localeNames = new ArrayList<String>();
        final InputStream is = ICUData.getRequiredStream(this.getClass(), "data/index.txt");
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String string = null;
            while (null != (string = br.readLine())) {
                string = string.trim();
                if (!string.startsWith("#")) {
                    if (string.length() == 0) {
                        continue;
                    }
                    localeNames.add(string);
                }
            }
            br.close();
        }
        catch (IOException e) {
            throw new IllegalStateException("IO Error reading data/index.txt: " + e.toString());
        }
        this.availableLocales = (Collection<String>)Collections.unmodifiableList((List<?>)localeNames);
    }
    
    @Override
    public PeriodFormatterData get(String localeName) {
        final int x = localeName.indexOf(64);
        if (x != -1) {
            localeName = localeName.substring(0, x);
        }
        synchronized (this) {
            if (this.lastLocale != null && this.lastLocale.equals(localeName)) {
                return this.lastData;
            }
            PeriodFormatterData ld = this.cache.get(localeName);
            if (ld == null) {
                String ln = localeName;
                while (!this.availableLocales.contains(ln)) {
                    final int ix = ln.lastIndexOf("_");
                    if (ix > -1) {
                        ln = ln.substring(0, ix);
                    }
                    else {
                        if ("test".equals(ln)) {
                            ln = null;
                            break;
                        }
                        ln = "test";
                    }
                }
                if (ln == null) {
                    throw new MissingResourceException("Duration data not found for  " + localeName, "data/", localeName);
                }
                final String name = "data/pfd_" + ln + ".xml";
                try {
                    final InputStream is = ICUData.getStream(this.getClass(), name);
                    if (is == null) {
                        throw new MissingResourceException("no resource named " + name, name, "");
                    }
                    final DataRecord dr = DataRecord.read(ln, new XMLRecordReader(new InputStreamReader(is, "UTF-8")));
                    if (dr != null) {
                        ld = new PeriodFormatterData(localeName, dr);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    throw new MissingResourceException("Unhandled Encoding for resource " + name, name, "");
                }
                this.cache.put(localeName, ld);
            }
            this.lastData = ld;
            this.lastLocale = localeName;
            return ld;
        }
    }
    
    @Override
    public Collection<String> getAvailableLocales() {
        return this.availableLocales;
    }
    
    static {
        singleton = new ResourceBasedPeriodFormatterDataService();
    }
}
