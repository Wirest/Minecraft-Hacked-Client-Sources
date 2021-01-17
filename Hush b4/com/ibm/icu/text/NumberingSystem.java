// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.ULocale;
import java.util.Locale;
import com.ibm.icu.impl.ICUCache;

public class NumberingSystem
{
    private String desc;
    private int radix;
    private boolean algorithmic;
    private String name;
    private static ICUCache<String, NumberingSystem> cachedLocaleData;
    private static ICUCache<String, NumberingSystem> cachedStringData;
    
    public NumberingSystem() {
        this.radix = 10;
        this.algorithmic = false;
        this.desc = "0123456789";
        this.name = "latn";
    }
    
    public static NumberingSystem getInstance(final int radix_in, final boolean isAlgorithmic_in, final String desc_in) {
        return getInstance(null, radix_in, isAlgorithmic_in, desc_in);
    }
    
    private static NumberingSystem getInstance(final String name_in, final int radix_in, final boolean isAlgorithmic_in, final String desc_in) {
        if (radix_in < 2) {
            throw new IllegalArgumentException("Invalid radix for numbering system");
        }
        if (!isAlgorithmic_in && (desc_in.length() != radix_in || !isValidDigitString(desc_in))) {
            throw new IllegalArgumentException("Invalid digit string for numbering system");
        }
        final NumberingSystem ns = new NumberingSystem();
        ns.radix = radix_in;
        ns.algorithmic = isAlgorithmic_in;
        ns.desc = desc_in;
        ns.name = name_in;
        return ns;
    }
    
    public static NumberingSystem getInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale));
    }
    
    public static NumberingSystem getInstance(final ULocale locale) {
        final String[] OTHER_NS_KEYWORDS = { "native", "traditional", "finance" };
        Boolean nsResolved = true;
        String numbersKeyword = locale.getKeywordValue("numbers");
        if (numbersKeyword != null) {
            for (final String keyword : OTHER_NS_KEYWORDS) {
                if (numbersKeyword.equals(keyword)) {
                    nsResolved = false;
                    break;
                }
            }
        }
        else {
            numbersKeyword = "default";
            nsResolved = false;
        }
        if (nsResolved) {
            final NumberingSystem ns = getInstanceByName(numbersKeyword);
            if (ns != null) {
                return ns;
            }
            numbersKeyword = "default";
            nsResolved = false;
        }
        final String baseName = locale.getBaseName();
        NumberingSystem ns = NumberingSystem.cachedLocaleData.get(baseName + "@numbers=" + numbersKeyword);
        if (ns != null) {
            return ns;
        }
        final String originalNumbersKeyword = numbersKeyword;
        String resolvedNumberingSystem = null;
        while (!nsResolved) {
            try {
                ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
                rb = rb.getWithFallback("NumberElements");
                resolvedNumberingSystem = rb.getStringWithFallback(numbersKeyword);
                nsResolved = true;
            }
            catch (MissingResourceException ex) {
                if (numbersKeyword.equals("native") || numbersKeyword.equals("finance")) {
                    numbersKeyword = "default";
                }
                else if (numbersKeyword.equals("traditional")) {
                    numbersKeyword = "native";
                }
                else {
                    nsResolved = true;
                }
            }
        }
        if (resolvedNumberingSystem != null) {
            ns = getInstanceByName(resolvedNumberingSystem);
        }
        if (ns == null) {
            ns = new NumberingSystem();
        }
        NumberingSystem.cachedLocaleData.put(baseName + "@numbers=" + originalNumbersKeyword, ns);
        return ns;
    }
    
    public static NumberingSystem getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public static NumberingSystem getInstanceByName(final String name) {
        NumberingSystem ns = NumberingSystem.cachedStringData.get(name);
        if (ns != null) {
            return ns;
        }
        String description;
        int radix;
        boolean isAlgorithmic;
        try {
            final UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
            final UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
            final UResourceBundle nsTop = nsCurrent.get(name);
            description = nsTop.getString("desc");
            final UResourceBundle nsRadixBundle = nsTop.get("radix");
            final UResourceBundle nsAlgBundle = nsTop.get("algorithmic");
            radix = nsRadixBundle.getInt();
            final int algorithmic = nsAlgBundle.getInt();
            isAlgorithmic = (algorithmic == 1);
        }
        catch (MissingResourceException ex) {
            return null;
        }
        ns = getInstance(name, radix, isAlgorithmic, description);
        NumberingSystem.cachedStringData.put(name, ns);
        return ns;
    }
    
    public static String[] getAvailableNames() {
        final UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
        final UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
        final ArrayList<String> output = new ArrayList<String>();
        final UResourceBundleIterator it = nsCurrent.getIterator();
        while (it.hasNext()) {
            final UResourceBundle temp = it.next();
            final String nsName = temp.getKey();
            output.add(nsName);
        }
        return output.toArray(new String[output.size()]);
    }
    
    public static boolean isValidDigitString(final String str) {
        int i = 0;
        final UCharacterIterator it = UCharacterIterator.getInstance(str);
        it.setToStart();
        int c;
        while ((c = it.nextCodePoint()) != -1) {
            if (UCharacter.isSupplementary(c)) {
                return false;
            }
            ++i;
        }
        return i == 10;
    }
    
    public int getRadix() {
        return this.radix;
    }
    
    public String getDescription() {
        return this.desc;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isAlgorithmic() {
        return this.algorithmic;
    }
    
    static {
        NumberingSystem.cachedLocaleData = new SimpleCache<String, NumberingSystem>();
        NumberingSystem.cachedStringData = new SimpleCache<String, NumberingSystem>();
    }
}
