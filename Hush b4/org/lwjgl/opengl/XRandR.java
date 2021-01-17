// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.util.Arrays;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.List;
import org.lwjgl.LWJGLUtil;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Map;

public class XRandR
{
    private static Screen[] current;
    private static String primaryScreenIdentifier;
    private static Screen[] savedConfiguration;
    private static Map<String, Screen[]> screens;
    private static final Pattern WHITESPACE_PATTERN;
    private static final Pattern SCREEN_HEADER_PATTERN;
    private static final Pattern SCREEN_MODELINE_PATTERN;
    private static final Pattern FREQ_PATTERN;
    
    private static void populate() {
        if (XRandR.screens != null) {
            return;
        }
        XRandR.screens = new HashMap<String, Screen[]>();
        try {
            final Process p = Runtime.getRuntime().exec(new String[] { "xrandr", "-q" });
            final List<Screen> currentList = new ArrayList<Screen>();
            final List<Screen> possibles = new ArrayList<Screen>();
            String name = null;
            final int[] currentScreenPosition = new int[2];
            final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                final String[] sa = XRandR.WHITESPACE_PATTERN.split(line);
                if ("connected".equals(sa[1])) {
                    if (name != null) {
                        XRandR.screens.put(name, possibles.toArray(new Screen[possibles.size()]));
                        possibles.clear();
                    }
                    name = sa[0];
                    if ("primary".equals(sa[2])) {
                        parseScreenHeader(currentScreenPosition, sa[3]);
                        XRandR.primaryScreenIdentifier = name;
                    }
                    else {
                        parseScreenHeader(currentScreenPosition, sa[2]);
                    }
                }
                else {
                    final Matcher m = XRandR.SCREEN_MODELINE_PATTERN.matcher(sa[0]);
                    if (!m.matches()) {
                        continue;
                    }
                    parseScreenModeline(possibles, currentList, name, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), sa, currentScreenPosition);
                }
            }
            XRandR.screens.put(name, possibles.toArray(new Screen[possibles.size()]));
            XRandR.current = currentList.toArray(new Screen[currentList.size()]);
            if (XRandR.primaryScreenIdentifier == null) {
                long totalPixels = Long.MIN_VALUE;
                for (final Screen screen : XRandR.current) {
                    if (1L * screen.width * screen.height > totalPixels) {
                        XRandR.primaryScreenIdentifier = screen.name;
                        totalPixels = 1L * screen.width * screen.height;
                    }
                }
            }
        }
        catch (Throwable e) {
            LWJGLUtil.log("Exception in XRandR.populate(): " + e.getMessage());
            XRandR.screens.clear();
            XRandR.current = new Screen[0];
        }
    }
    
    public static Screen[] getConfiguration() {
        populate();
        for (final Screen screen : XRandR.current) {
            if (screen.name.equals(XRandR.primaryScreenIdentifier)) {
                return new Screen[] { screen };
            }
        }
        return XRandR.current.clone();
    }
    
    public static void setConfiguration(final boolean disableOthers, final Screen... screens) {
        if (screens.length == 0) {
            throw new IllegalArgumentException("Must specify at least one screen");
        }
        final List<String> cmd = new ArrayList<String>();
        cmd.add("xrandr");
        if (disableOthers) {
            for (final Screen screen : XRandR.current) {
                boolean disable = true;
                for (final Screen screen2 : screens) {
                    if (screen2.name.equals(screen.name)) {
                        disable = false;
                        break;
                    }
                }
                if (disable) {
                    cmd.add("--output");
                    cmd.add(screen.name);
                    cmd.add("--off");
                }
            }
        }
        for (final Screen screen : screens) {
            screen.getArgs(cmd);
        }
        try {
            final Process p = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
            final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                LWJGLUtil.log("Unexpected output from xrandr process: " + line);
            }
            XRandR.current = screens;
        }
        catch (IOException e) {
            LWJGLUtil.log("XRandR exception in setConfiguration(): " + e.getMessage());
        }
    }
    
    public static void saveConfiguration() {
        populate();
        XRandR.savedConfiguration = XRandR.current.clone();
    }
    
    public static void restoreConfiguration() {
        if (XRandR.savedConfiguration != null) {
            setConfiguration(true, XRandR.savedConfiguration);
        }
    }
    
    public static String[] getScreenNames() {
        populate();
        return XRandR.screens.keySet().toArray(new String[XRandR.screens.size()]);
    }
    
    public static Screen[] getResolutions(final String name) {
        populate();
        return XRandR.screens.get(name).clone();
    }
    
    private static void parseScreenModeline(final List<Screen> allModes, final List<Screen> current, final String name, final int width, final int height, final String[] modeLine, final int[] screenPosition) {
        for (int i = 1; i < modeLine.length; ++i) {
            final String freqS = modeLine[i];
            if (!"+".equals(freqS)) {
                final Matcher m = XRandR.FREQ_PATTERN.matcher(freqS);
                if (!m.matches()) {
                    LWJGLUtil.log("Frequency match failed: " + Arrays.toString(modeLine));
                    return;
                }
                final int freq = Integer.parseInt(m.group(1));
                final Screen s = new Screen(name, width, height, freq, 0, 0);
                if (freqS.contains("*")) {
                    current.add(new Screen(name, width, height, freq, screenPosition[0], screenPosition[1]));
                    allModes.add(0, s);
                }
                else {
                    allModes.add(s);
                }
            }
        }
    }
    
    private static void parseScreenHeader(final int[] screenPosition, final String resPos) {
        final Matcher m = XRandR.SCREEN_HEADER_PATTERN.matcher(resPos);
        if (!m.matches()) {
            screenPosition[1] = (screenPosition[0] = 0);
            return;
        }
        screenPosition[0] = Integer.parseInt(m.group(3));
        screenPosition[1] = Integer.parseInt(m.group(4));
    }
    
    static Screen DisplayModetoScreen(final DisplayMode mode) {
        populate();
        final Screen primary = findPrimary(XRandR.current);
        return new Screen(primary.name, mode.getWidth(), mode.getHeight(), mode.getFrequency(), primary.xPos, primary.yPos);
    }
    
    static DisplayMode ScreentoDisplayMode(final Screen... screens) {
        populate();
        final Screen primary = findPrimary(screens);
        return new DisplayMode(primary.width, primary.height, 24, primary.freq);
    }
    
    private static Screen findPrimary(final Screen... screens) {
        for (final Screen screen : screens) {
            if (screen.name.equals(XRandR.primaryScreenIdentifier)) {
                return screen;
            }
        }
        return screens[0];
    }
    
    static {
        WHITESPACE_PATTERN = Pattern.compile("\\s+");
        SCREEN_HEADER_PATTERN = Pattern.compile("^(\\d+)x(\\d+)[+](\\d+)[+](\\d+)$");
        SCREEN_MODELINE_PATTERN = Pattern.compile("^(\\d+)x(\\d+)$");
        FREQ_PATTERN = Pattern.compile("^(\\d+)[.](\\d+)(?:\\s*[*])?(?:\\s*[+])?$");
    }
    
    public static class Screen implements Cloneable
    {
        public final String name;
        public final int width;
        public final int height;
        public final int freq;
        public int xPos;
        public int yPos;
        
        Screen(final String name, final int width, final int height, final int freq, final int xPos, final int yPos) {
            this.name = name;
            this.width = width;
            this.height = height;
            this.freq = freq;
            this.xPos = xPos;
            this.yPos = yPos;
        }
        
        private void getArgs(final List<String> argList) {
            argList.add("--output");
            argList.add(this.name);
            argList.add("--mode");
            argList.add(this.width + "x" + this.height);
            argList.add("--rate");
            argList.add(Integer.toString(this.freq));
            argList.add("--pos");
            argList.add(this.xPos + "x" + this.yPos);
        }
        
        @Override
        public String toString() {
            return this.name + " " + this.width + "x" + this.height + " @ " + this.xPos + "x" + this.yPos + " with " + this.freq + "Hz";
        }
    }
}
