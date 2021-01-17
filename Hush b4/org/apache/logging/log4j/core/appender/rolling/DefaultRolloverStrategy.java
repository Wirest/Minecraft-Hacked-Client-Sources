// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;
import org.apache.logging.log4j.core.appender.rolling.helper.FileRenameAction;
import org.apache.logging.log4j.core.appender.rolling.helper.ZipCompressAction;
import org.apache.logging.log4j.core.appender.rolling.helper.GZCompressAction;
import java.io.File;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
public class DefaultRolloverStrategy implements RolloverStrategy
{
    protected static final Logger LOGGER;
    private static final int MIN_WINDOW_SIZE = 1;
    private static final int DEFAULT_WINDOW_SIZE = 7;
    private final int maxIndex;
    private final int minIndex;
    private final boolean useMax;
    private final StrSubstitutor subst;
    private final int compressionLevel;
    
    protected DefaultRolloverStrategy(final int minIndex, final int maxIndex, final boolean useMax, final int compressionLevel, final StrSubstitutor subst) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.useMax = useMax;
        this.compressionLevel = compressionLevel;
        this.subst = subst;
    }
    
    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        if (this.maxIndex < 0) {
            return null;
        }
        final int fileIndex;
        if ((fileIndex = this.purge(this.minIndex, this.maxIndex, manager)) < 0) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        manager.getPatternProcessor().formatFileName(this.subst, buf, fileIndex);
        final String currentFileName = manager.getFileName();
        final String compressedName;
        String renameTo = compressedName = buf.toString();
        Action compressAction = null;
        if (renameTo.endsWith(".gz")) {
            renameTo = renameTo.substring(0, renameTo.length() - 3);
            compressAction = new GZCompressAction(new File(renameTo), new File(compressedName), true);
        }
        else if (renameTo.endsWith(".zip")) {
            renameTo = renameTo.substring(0, renameTo.length() - 4);
            compressAction = new ZipCompressAction(new File(renameTo), new File(compressedName), true, this.compressionLevel);
        }
        final FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), false);
        return new RolloverDescriptionImpl(currentFileName, false, renameAction, compressAction);
    }
    
    private int purge(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        return this.useMax ? this.purgeAscending(lowIndex, highIndex, manager) : this.purgeDescending(lowIndex, highIndex, manager);
    }
    
    private int purgeDescending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        int suffixLength = 0;
        final List<FileRenameAction> renames = new ArrayList<FileRenameAction>();
        final StringBuilder buf = new StringBuilder();
        manager.getPatternProcessor().formatFileName(buf, (Object)lowIndex);
        String lowFilename = this.subst.replace(buf);
        if (lowFilename.endsWith(".gz")) {
            suffixLength = 3;
        }
        else if (lowFilename.endsWith(".zip")) {
            suffixLength = 4;
        }
        int i = lowIndex;
        while (i <= highIndex) {
            File toRename = new File(lowFilename);
            boolean isBase = false;
            if (suffixLength > 0) {
                final File toRenameBase = new File(lowFilename.substring(0, lowFilename.length() - suffixLength));
                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        toRenameBase.delete();
                    }
                }
                else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }
            if (!toRename.exists()) {
                break;
            }
            if (i == highIndex) {
                if (!toRename.delete()) {
                    return -1;
                }
                break;
            }
            else {
                buf.setLength(0);
                manager.getPatternProcessor().formatFileName(buf, (Object)(i + 1));
                String renameTo;
                final String highFilename = renameTo = this.subst.replace(buf);
                if (isBase) {
                    renameTo = highFilename.substring(0, highFilename.length() - suffixLength);
                }
                renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                lowFilename = highFilename;
                ++i;
            }
        }
        for (i = renames.size() - 1; i >= 0; --i) {
            final Action action = renames.get(i);
            try {
                if (!action.execute()) {
                    return -1;
                }
            }
            catch (Exception ex) {
                DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                return -1;
            }
        }
        return lowIndex;
    }
    
    private int purgeAscending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        int suffixLength = 0;
        final List<FileRenameAction> renames = new ArrayList<FileRenameAction>();
        final StringBuilder buf = new StringBuilder();
        manager.getPatternProcessor().formatFileName(buf, (Object)highIndex);
        String highFilename = this.subst.replace(buf);
        if (highFilename.endsWith(".gz")) {
            suffixLength = 3;
        }
        else if (highFilename.endsWith(".zip")) {
            suffixLength = 4;
        }
        int maxIndex = 0;
        for (int i = highIndex; i >= lowIndex; --i) {
            File toRename = new File(highFilename);
            if (i == highIndex && toRename.exists()) {
                maxIndex = highIndex;
            }
            else if (maxIndex == 0 && toRename.exists()) {
                maxIndex = i + 1;
                break;
            }
            boolean isBase = false;
            if (suffixLength > 0) {
                final File toRenameBase = new File(highFilename.substring(0, highFilename.length() - suffixLength));
                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        toRenameBase.delete();
                    }
                }
                else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }
            if (toRename.exists()) {
                if (i == lowIndex) {
                    if (!toRename.delete()) {
                        return -1;
                    }
                    break;
                }
                else {
                    buf.setLength(0);
                    manager.getPatternProcessor().formatFileName(buf, (Object)(i - 1));
                    String renameTo;
                    final String lowFilename = renameTo = this.subst.replace(buf);
                    if (isBase) {
                        renameTo = lowFilename.substring(0, lowFilename.length() - suffixLength);
                    }
                    renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                    highFilename = lowFilename;
                }
            }
            else {
                buf.setLength(0);
                manager.getPatternProcessor().formatFileName(buf, (Object)(i - 1));
                highFilename = this.subst.replace(buf);
            }
        }
        if (maxIndex == 0) {
            maxIndex = lowIndex;
        }
        for (int i = renames.size() - 1; i >= 0; --i) {
            final Action action = renames.get(i);
            try {
                if (!action.execute()) {
                    return -1;
                }
            }
            catch (Exception ex) {
                DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                return -1;
            }
        }
        return maxIndex;
    }
    
    @Override
    public String toString() {
        return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ")";
    }
    
    @PluginFactory
    public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") final String max, @PluginAttribute("min") final String min, @PluginAttribute("fileIndex") final String fileIndex, @PluginAttribute("compressionLevel") final String compressionLevelStr, @PluginConfiguration final Configuration config) {
        final boolean useMax = fileIndex == null || fileIndex.equalsIgnoreCase("max");
        int minIndex;
        if (min != null) {
            minIndex = Integer.parseInt(min);
            if (minIndex < 1) {
                DefaultRolloverStrategy.LOGGER.error("Minimum window size too small. Limited to 1");
                minIndex = 1;
            }
        }
        else {
            minIndex = 1;
        }
        int maxIndex;
        if (max != null) {
            maxIndex = Integer.parseInt(max);
            if (maxIndex < minIndex) {
                maxIndex = ((minIndex < 7) ? 7 : minIndex);
                DefaultRolloverStrategy.LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
            }
        }
        else {
            maxIndex = 7;
        }
        final int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
        return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, config.getStrSubstitutor());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
