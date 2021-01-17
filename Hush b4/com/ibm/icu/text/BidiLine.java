// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Arrays;

final class BidiLine
{
    static void setTrailingWSStart(final Bidi bidi) {
        final byte[] dirProps = bidi.dirProps;
        final byte[] levels = bidi.levels;
        int start = bidi.length;
        final byte paraLevel = bidi.paraLevel;
        if (Bidi.NoContextRTL(dirProps[start - 1]) == 7) {
            bidi.trailingWSStart = start;
            return;
        }
        while (start > 0 && (Bidi.DirPropFlagNC(dirProps[start - 1]) & Bidi.MASK_WS) != 0x0) {
            --start;
        }
        while (start > 0 && levels[start - 1] == paraLevel) {
            --start;
        }
        bidi.trailingWSStart = start;
    }
    
    static Bidi setLine(final Bidi paraBidi, final int start, final int limit) {
        final Bidi bidi3;
        final Bidi bidi2;
        final Bidi bidi;
        final Bidi lineBidi = bidi = (bidi2 = (bidi3 = new Bidi()));
        final int length2 = limit - start;
        bidi.resultLength = length2;
        bidi2.originalLength = length2;
        bidi3.length = length2;
        final int length = length2;
        lineBidi.text = new char[length];
        System.arraycopy(paraBidi.text, start, lineBidi.text, 0, length);
        lineBidi.paraLevel = paraBidi.GetParaLevelAt(start);
        lineBidi.paraCount = paraBidi.paraCount;
        lineBidi.runs = new BidiRun[0];
        lineBidi.reorderingMode = paraBidi.reorderingMode;
        lineBidi.reorderingOptions = paraBidi.reorderingOptions;
        if (paraBidi.controlCount > 0) {
            for (int j = start; j < limit; ++j) {
                if (Bidi.IsBidiControlChar(paraBidi.text[j])) {
                    final Bidi bidi4 = lineBidi;
                    ++bidi4.controlCount;
                }
            }
            final Bidi bidi5 = lineBidi;
            bidi5.resultLength -= lineBidi.controlCount;
        }
        lineBidi.getDirPropsMemory(length);
        lineBidi.dirProps = lineBidi.dirPropsMemory;
        System.arraycopy(paraBidi.dirProps, start, lineBidi.dirProps, 0, length);
        lineBidi.getLevelsMemory(length);
        lineBidi.levels = lineBidi.levelsMemory;
        System.arraycopy(paraBidi.levels, start, lineBidi.levels, 0, length);
        lineBidi.runCount = -1;
        if (paraBidi.direction != 2) {
            lineBidi.direction = paraBidi.direction;
            if (paraBidi.trailingWSStart <= start) {
                lineBidi.trailingWSStart = 0;
            }
            else if (paraBidi.trailingWSStart < limit) {
                lineBidi.trailingWSStart = paraBidi.trailingWSStart - start;
            }
            else {
                lineBidi.trailingWSStart = length;
            }
        }
        else {
            final byte[] levels = lineBidi.levels;
            setTrailingWSStart(lineBidi);
            final int trailingWSStart = lineBidi.trailingWSStart;
            Label_0417: {
                if (trailingWSStart == 0) {
                    lineBidi.direction = (byte)(lineBidi.paraLevel & 0x1);
                }
                else {
                    final byte level = (byte)(levels[0] & 0x1);
                    if (trailingWSStart < length && (lineBidi.paraLevel & 0x1) != level) {
                        lineBidi.direction = 2;
                    }
                    else {
                        for (int i = 1; i != trailingWSStart; ++i) {
                            if ((levels[i] & 0x1) != level) {
                                lineBidi.direction = 2;
                                break Label_0417;
                            }
                        }
                        lineBidi.direction = level;
                    }
                }
            }
            switch (lineBidi.direction) {
                case 0: {
                    lineBidi.paraLevel = (byte)(lineBidi.paraLevel + 1 & 0xFFFFFFFE);
                    lineBidi.trailingWSStart = 0;
                    break;
                }
                case 1: {
                    final Bidi bidi6 = lineBidi;
                    bidi6.paraLevel |= 0x1;
                    lineBidi.trailingWSStart = 0;
                    break;
                }
            }
        }
        lineBidi.paraBidi = paraBidi;
        return lineBidi;
    }
    
    static byte getLevelAt(final Bidi bidi, final int charIndex) {
        if (bidi.direction != 2 || charIndex >= bidi.trailingWSStart) {
            return bidi.GetParaLevelAt(charIndex);
        }
        return bidi.levels[charIndex];
    }
    
    static byte[] getLevels(final Bidi bidi) {
        final int start = bidi.trailingWSStart;
        final int length = bidi.length;
        if (start != length) {
            Arrays.fill(bidi.levels, start, length, bidi.paraLevel);
            bidi.trailingWSStart = length;
        }
        if (length < bidi.levels.length) {
            final byte[] levels = new byte[length];
            System.arraycopy(bidi.levels, 0, levels, 0, length);
            return levels;
        }
        return bidi.levels;
    }
    
    static BidiRun getLogicalRun(final Bidi bidi, final int logicalPosition) {
        final BidiRun newRun = new BidiRun();
        getRuns(bidi);
        final int runCount = bidi.runCount;
        int visualStart = 0;
        int logicalLimit = 0;
        BidiRun iRun = bidi.runs[0];
        for (int i = 0; i < runCount; ++i) {
            iRun = bidi.runs[i];
            logicalLimit = iRun.start + iRun.limit - visualStart;
            if (logicalPosition >= iRun.start && logicalPosition < logicalLimit) {
                break;
            }
            visualStart = iRun.limit;
        }
        newRun.start = iRun.start;
        newRun.limit = logicalLimit;
        newRun.level = iRun.level;
        return newRun;
    }
    
    static BidiRun getVisualRun(final Bidi bidi, final int runIndex) {
        final int start = bidi.runs[runIndex].start;
        final byte level = bidi.runs[runIndex].level;
        int limit;
        if (runIndex > 0) {
            limit = start + bidi.runs[runIndex].limit - bidi.runs[runIndex - 1].limit;
        }
        else {
            limit = start + bidi.runs[0].limit;
        }
        return new BidiRun(start, limit, level);
    }
    
    static void getSingleRun(final Bidi bidi, final byte level) {
        bidi.runs = bidi.simpleRuns;
        bidi.runCount = 1;
        bidi.runs[0] = new BidiRun(0, bidi.length, level);
    }
    
    private static void reorderLine(final Bidi bidi, byte minLevel, byte maxLevel) {
        if (maxLevel <= (minLevel | 0x1)) {
            return;
        }
        ++minLevel;
        final BidiRun[] runs = bidi.runs;
        final byte[] levels = bidi.levels;
        int runCount = bidi.runCount;
        if (bidi.trailingWSStart < bidi.length) {
            --runCount;
        }
        while (true) {
            --maxLevel;
            if (maxLevel < minLevel) {
                break;
            }
            int firstRun = 0;
            while (true) {
                if (firstRun < runCount && levels[runs[firstRun].start] < maxLevel) {
                    ++firstRun;
                }
                else {
                    if (firstRun >= runCount) {
                        break;
                    }
                    int limitRun = firstRun;
                    while (++limitRun < runCount && levels[runs[limitRun].start] >= maxLevel) {}
                    for (int endRun = limitRun - 1; firstRun < endRun; ++firstRun, --endRun) {
                        final BidiRun tempRun = runs[firstRun];
                        runs[firstRun] = runs[endRun];
                        runs[endRun] = tempRun;
                    }
                    if (limitRun == runCount) {
                        break;
                    }
                    firstRun = limitRun + 1;
                }
            }
        }
        if ((minLevel & 0x1) == 0x0) {
            int firstRun = 0;
            if (bidi.trailingWSStart == bidi.length) {
                --runCount;
            }
            while (firstRun < runCount) {
                final BidiRun tempRun = runs[firstRun];
                runs[firstRun] = runs[runCount];
                runs[runCount] = tempRun;
                ++firstRun;
                --runCount;
            }
        }
    }
    
    static int getRunFromLogicalIndex(final Bidi bidi, final int logicalIndex) {
        final BidiRun[] runs = bidi.runs;
        final int runCount = bidi.runCount;
        int visualStart = 0;
        for (int i = 0; i < runCount; ++i) {
            final int length = runs[i].limit - visualStart;
            final int logicalStart = runs[i].start;
            if (logicalIndex >= logicalStart && logicalIndex < logicalStart + length) {
                return i;
            }
            visualStart += length;
        }
        throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
    }
    
    static void getRuns(final Bidi bidi) {
        if (bidi.runCount >= 0) {
            return;
        }
        if (bidi.direction != 2) {
            getSingleRun(bidi, bidi.paraLevel);
        }
        else {
            final int length = bidi.length;
            final byte[] levels = bidi.levels;
            byte level = 126;
            int limit = bidi.trailingWSStart;
            int runCount = 0;
            for (int i = 0; i < limit; ++i) {
                if (levels[i] != level) {
                    ++runCount;
                    level = levels[i];
                }
            }
            if (runCount == 1 && limit == length) {
                getSingleRun(bidi, levels[0]);
            }
            else {
                byte minLevel = 62;
                byte maxLevel = 0;
                if (limit < length) {
                    ++runCount;
                }
                bidi.getRunsMemory(runCount);
                final BidiRun[] runs = bidi.runsMemory;
                int runIndex = 0;
                int i = 0;
                do {
                    final int start = i;
                    level = levels[i];
                    if (level < minLevel) {
                        minLevel = level;
                    }
                    if (level > maxLevel) {
                        maxLevel = level;
                    }
                    while (++i < limit && levels[i] == level) {}
                    runs[runIndex] = new BidiRun(start, i - start, level);
                    ++runIndex;
                } while (i < limit);
                if (limit < length) {
                    runs[runIndex] = new BidiRun(limit, length - limit, bidi.paraLevel);
                    if (bidi.paraLevel < minLevel) {
                        minLevel = bidi.paraLevel;
                    }
                }
                bidi.runs = runs;
                bidi.runCount = runCount;
                reorderLine(bidi, minLevel, maxLevel);
                limit = 0;
                for (i = 0; i < runCount; ++i) {
                    runs[i].level = levels[runs[i].start];
                    final BidiRun bidiRun = runs[i];
                    final int limit2 = bidiRun.limit + limit;
                    bidiRun.limit = limit2;
                    limit = limit2;
                }
                if (runIndex < runCount) {
                    final int trailingRun = ((bidi.paraLevel & 0x1) != 0x0) ? 0 : runIndex;
                    runs[trailingRun].level = bidi.paraLevel;
                }
            }
        }
        if (bidi.insertPoints.size > 0) {
            for (int ip = 0; ip < bidi.insertPoints.size; ++ip) {
                final Bidi.Point point = bidi.insertPoints.points[ip];
                final int runIndex2 = getRunFromLogicalIndex(bidi, point.pos);
                final BidiRun bidiRun2 = bidi.runs[runIndex2];
                bidiRun2.insertRemove |= point.flag;
            }
        }
        if (bidi.controlCount > 0) {
            for (int ic = 0; ic < bidi.length; ++ic) {
                final char c = bidi.text[ic];
                if (Bidi.IsBidiControlChar(c)) {
                    final int runIndex3 = getRunFromLogicalIndex(bidi, ic);
                    final BidiRun bidiRun3 = bidi.runs[runIndex3];
                    --bidiRun3.insertRemove;
                }
            }
        }
    }
    
    static int[] prepareReorder(final byte[] levels, final byte[] pMinLevel, final byte[] pMaxLevel) {
        if (levels == null || levels.length <= 0) {
            return null;
        }
        byte minLevel = 62;
        byte maxLevel = 0;
        int start = levels.length;
        while (start > 0) {
            final byte level = levels[--start];
            if (level > 62) {
                return null;
            }
            if (level < minLevel) {
                minLevel = level;
            }
            if (level <= maxLevel) {
                continue;
            }
            maxLevel = level;
        }
        pMinLevel[0] = minLevel;
        pMaxLevel[0] = maxLevel;
        int[] indexMap;
        for (indexMap = new int[levels.length], start = levels.length; start > 0; --start, indexMap[start] = start) {}
        return indexMap;
    }
    
    static int[] reorderLogical(final byte[] levels) {
        final byte[] aMinLevel = { 0 };
        final byte[] aMaxLevel = { 0 };
        final int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
        if (indexMap == null) {
            return null;
        }
        byte minLevel = aMinLevel[0];
        byte maxLevel = aMaxLevel[0];
        if (minLevel == maxLevel && (minLevel & 0x1) == 0x0) {
            return indexMap;
        }
        minLevel |= 0x1;
        do {
            int start = 0;
            while (true) {
                if (start < levels.length && levels[start] < maxLevel) {
                    ++start;
                }
                else {
                    if (start >= levels.length) {
                        break;
                    }
                    int limit = start;
                    while (++limit < levels.length && levels[limit] >= maxLevel) {}
                    final int sumOfSosEos = start + limit - 1;
                    do {
                        indexMap[start] = sumOfSosEos - indexMap[start];
                    } while (++start < limit);
                    if (limit == levels.length) {
                        break;
                    }
                    start = limit + 1;
                }
            }
            --maxLevel;
        } while (maxLevel >= minLevel);
        return indexMap;
    }
    
    static int[] reorderVisual(final byte[] levels) {
        final byte[] aMinLevel = { 0 };
        final byte[] aMaxLevel = { 0 };
        final int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
        if (indexMap == null) {
            return null;
        }
        byte minLevel = aMinLevel[0];
        byte maxLevel = aMaxLevel[0];
        if (minLevel == maxLevel && (minLevel & 0x1) == 0x0) {
            return indexMap;
        }
        minLevel |= 0x1;
        do {
            int start = 0;
            while (true) {
                if (start < levels.length && levels[start] < maxLevel) {
                    ++start;
                }
                else {
                    if (start >= levels.length) {
                        break;
                    }
                    int limit = start;
                    while (++limit < levels.length && levels[limit] >= maxLevel) {}
                    for (int end = limit - 1; start < end; ++start, --end) {
                        final int temp = indexMap[start];
                        indexMap[start] = indexMap[end];
                        indexMap[end] = temp;
                    }
                    if (limit == levels.length) {
                        break;
                    }
                    start = limit + 1;
                }
            }
            --maxLevel;
        } while (maxLevel >= minLevel);
        return indexMap;
    }
    
    static int getVisualIndex(final Bidi bidi, final int logicalIndex) {
        int visualIndex = -1;
        switch (bidi.direction) {
            case 0: {
                visualIndex = logicalIndex;
                break;
            }
            case 1: {
                visualIndex = bidi.length - logicalIndex - 1;
                break;
            }
            default: {
                getRuns(bidi);
                final BidiRun[] runs = bidi.runs;
                int visualStart = 0;
                int i = 0;
                while (i < bidi.runCount) {
                    final int length = runs[i].limit - visualStart;
                    final int offset = logicalIndex - runs[i].start;
                    if (offset >= 0 && offset < length) {
                        if (runs[i].isEvenRun()) {
                            visualIndex = visualStart + offset;
                            break;
                        }
                        visualIndex = visualStart + length - offset - 1;
                        break;
                    }
                    else {
                        visualStart += length;
                        ++i;
                    }
                }
                if (i >= bidi.runCount) {
                    return -1;
                }
                break;
            }
        }
        if (bidi.insertPoints.size > 0) {
            final BidiRun[] runs = bidi.runs;
            int visualStart2 = 0;
            int markFound = 0;
            int i = 0;
            while (true) {
                final int length2 = runs[i].limit - visualStart2;
                final int insertRemove = runs[i].insertRemove;
                if ((insertRemove & 0x5) > 0) {
                    ++markFound;
                }
                if (visualIndex < runs[i].limit) {
                    break;
                }
                if ((insertRemove & 0xA) > 0) {
                    ++markFound;
                }
                ++i;
                visualStart2 += length2;
            }
            return visualIndex + markFound;
        }
        if (bidi.controlCount <= 0) {
            return visualIndex;
        }
        final BidiRun[] runs = bidi.runs;
        int visualStart3 = 0;
        int controlFound = 0;
        char uchar = bidi.text[logicalIndex];
        if (Bidi.IsBidiControlChar(uchar)) {
            return -1;
        }
        int i = 0;
        int length3;
        int insertRemove2;
        while (true) {
            length3 = runs[i].limit - visualStart3;
            insertRemove2 = runs[i].insertRemove;
            if (visualIndex < runs[i].limit) {
                break;
            }
            controlFound -= insertRemove2;
            ++i;
            visualStart3 += length3;
        }
        if (insertRemove2 == 0) {
            return visualIndex - controlFound;
        }
        int start;
        int limit;
        if (runs[i].isEvenRun()) {
            start = runs[i].start;
            limit = logicalIndex;
        }
        else {
            start = logicalIndex + 1;
            limit = runs[i].start + length3;
        }
        for (int j = start; j < limit; ++j) {
            uchar = bidi.text[j];
            if (Bidi.IsBidiControlChar(uchar)) {
                ++controlFound;
            }
        }
        return visualIndex - controlFound;
    }
    
    static int getLogicalIndex(final Bidi bidi, int visualIndex) {
        final BidiRun[] runs = bidi.runs;
        final int runCount = bidi.runCount;
        if (bidi.insertPoints.size > 0) {
            int markFound = 0;
            int visualStart = 0;
            int i = 0;
            while (true) {
                final int length = runs[i].limit - visualStart;
                final int insertRemove = runs[i].insertRemove;
                if ((insertRemove & 0x5) > 0) {
                    if (visualIndex <= visualStart + markFound) {
                        return -1;
                    }
                    ++markFound;
                }
                if (visualIndex < runs[i].limit + markFound) {
                    visualIndex -= markFound;
                    break;
                }
                if ((insertRemove & 0xA) > 0) {
                    if (visualIndex == visualStart + length + markFound) {
                        return -1;
                    }
                    ++markFound;
                }
                ++i;
                visualStart += length;
            }
        }
        else if (bidi.controlCount > 0) {
            int controlFound = 0;
            int visualStart2 = 0;
            int i = 0;
            int insertRemove;
            int length2;
            while (true) {
                length2 = runs[i].limit - visualStart2;
                insertRemove = runs[i].insertRemove;
                if (visualIndex < runs[i].limit - controlFound + insertRemove) {
                    break;
                }
                controlFound -= insertRemove;
                ++i;
                visualStart2 += length2;
            }
            if (insertRemove == 0) {
                visualIndex += controlFound;
            }
            else {
                final int logicalStart = runs[i].start;
                final boolean evenRun = runs[i].isEvenRun();
                final int logicalEnd = logicalStart + length2 - 1;
                for (int j = 0; j < length2; ++j) {
                    final int k = evenRun ? (logicalStart + j) : (logicalEnd - j);
                    final char uchar = bidi.text[k];
                    if (Bidi.IsBidiControlChar(uchar)) {
                        ++controlFound;
                    }
                    if (visualIndex + controlFound == visualStart2 + j) {
                        break;
                    }
                }
                visualIndex += controlFound;
            }
        }
        int i;
        if (runCount <= 10) {
            for (int i = 0; visualIndex >= runs[i].limit; ++i) {}
        }
        else {
            int begin = 0;
            int limit = runCount;
            while (true) {
                i = begin + limit >>> 1;
                if (visualIndex >= runs[i].limit) {
                    begin = i + 1;
                }
                else {
                    if (i == 0) {
                        break;
                    }
                    if (visualIndex >= runs[i - 1].limit) {
                        break;
                    }
                    limit = i;
                }
            }
        }
        final int start = runs[i].start;
        if (runs[i].isEvenRun()) {
            if (i > 0) {
                visualIndex -= runs[i - 1].limit;
            }
            return start + visualIndex;
        }
        return start + runs[i].limit - visualIndex - 1;
    }
    
    static int[] getLogicalMap(final Bidi bidi) {
        BidiRun[] runs = bidi.runs;
        final int[] indexMap = new int[bidi.length];
        if (bidi.length > bidi.resultLength) {
            Arrays.fill(indexMap, -1);
        }
        int visualStart = 0;
        for (int j = 0; j < bidi.runCount; ++j) {
            int logicalStart = runs[j].start;
            final int visualLimit = runs[j].limit;
            if (runs[j].isEvenRun()) {
                do {
                    indexMap[logicalStart++] = visualStart++;
                } while (visualStart < visualLimit);
            }
            else {
                logicalStart += visualLimit - visualStart;
                do {
                    indexMap[--logicalStart] = visualStart++;
                } while (visualStart < visualLimit);
            }
        }
        if (bidi.insertPoints.size > 0) {
            int markFound = 0;
            final int runCount = bidi.runCount;
            runs = bidi.runs;
            visualStart = 0;
            int length;
            for (int i = 0; i < runCount; ++i, visualStart += length) {
                length = runs[i].limit - visualStart;
                final int insertRemove = runs[i].insertRemove;
                if ((insertRemove & 0x5) > 0) {
                    ++markFound;
                }
                if (markFound > 0) {
                    final int logicalStart = runs[i].start;
                    for (int logicalLimit = logicalStart + length, k = logicalStart; k < logicalLimit; ++k) {
                        final int[] array = indexMap;
                        final int n = k;
                        array[n] += markFound;
                    }
                }
                if ((insertRemove & 0xA) > 0) {
                    ++markFound;
                }
            }
        }
        else if (bidi.controlCount > 0) {
            int controlFound = 0;
            final int runCount = bidi.runCount;
            runs = bidi.runs;
            visualStart = 0;
            int length;
            for (int i = 0; i < runCount; ++i, visualStart += length) {
                length = runs[i].limit - visualStart;
                final int insertRemove = runs[i].insertRemove;
                if (controlFound - insertRemove != 0) {
                    final int logicalStart = runs[i].start;
                    final boolean evenRun = runs[i].isEvenRun();
                    final int logicalLimit = logicalStart + length;
                    if (insertRemove == 0) {
                        for (int k = logicalStart; k < logicalLimit; ++k) {
                            final int[] array2 = indexMap;
                            final int n2 = k;
                            array2[n2] -= controlFound;
                        }
                    }
                    else {
                        for (int k = 0; k < length; ++k) {
                            final int l = evenRun ? (logicalStart + k) : (logicalLimit - k - 1);
                            final char uchar = bidi.text[l];
                            if (Bidi.IsBidiControlChar(uchar)) {
                                ++controlFound;
                                indexMap[l] = -1;
                            }
                            else {
                                final int[] array3 = indexMap;
                                final int n3 = l;
                                array3[n3] -= controlFound;
                            }
                        }
                    }
                }
            }
        }
        return indexMap;
    }
    
    static int[] getVisualMap(final Bidi bidi) {
        BidiRun[] runs = bidi.runs;
        final int allocLength = (bidi.length > bidi.resultLength) ? bidi.length : bidi.resultLength;
        final int[] indexMap = new int[allocLength];
        int visualStart = 0;
        int idx = 0;
        for (int j = 0; j < bidi.runCount; ++j) {
            int logicalStart = runs[j].start;
            final int visualLimit = runs[j].limit;
            if (runs[j].isEvenRun()) {
                do {
                    indexMap[idx++] = logicalStart++;
                } while (++visualStart < visualLimit);
            }
            else {
                logicalStart += visualLimit - visualStart;
                do {
                    indexMap[idx++] = --logicalStart;
                } while (++visualStart < visualLimit);
            }
        }
        if (bidi.insertPoints.size > 0) {
            int markFound = 0;
            final int runCount = bidi.runCount;
            runs = bidi.runs;
            for (int i = 0; i < runCount; ++i) {
                final int insertRemove = runs[i].insertRemove;
                if ((insertRemove & 0x5) > 0) {
                    ++markFound;
                }
                if ((insertRemove & 0xA) > 0) {
                    ++markFound;
                }
            }
            int k = bidi.resultLength;
            for (int i = runCount - 1; i >= 0 && markFound > 0; --i) {
                final int insertRemove = runs[i].insertRemove;
                if ((insertRemove & 0xA) > 0) {
                    indexMap[--k] = -1;
                    --markFound;
                }
                visualStart = ((i > 0) ? runs[i - 1].limit : 0);
                for (int l = runs[i].limit - 1; l >= visualStart && markFound > 0; --l) {
                    indexMap[--k] = indexMap[l];
                }
                if ((insertRemove & 0x5) > 0) {
                    indexMap[--k] = -1;
                    --markFound;
                }
            }
        }
        else if (bidi.controlCount > 0) {
            final int runCount2 = bidi.runCount;
            runs = bidi.runs;
            visualStart = 0;
            int m = 0;
            int length;
            for (int i2 = 0; i2 < runCount2; ++i2, visualStart += length) {
                length = runs[i2].limit - visualStart;
                final int insertRemove = runs[i2].insertRemove;
                if (insertRemove == 0 && m == visualStart) {
                    m += length;
                }
                else if (insertRemove == 0) {
                    for (int visualLimit = runs[i2].limit, j2 = visualStart; j2 < visualLimit; ++j2) {
                        indexMap[m++] = indexMap[j2];
                    }
                }
                else {
                    final int logicalStart = runs[i2].start;
                    final boolean evenRun = runs[i2].isEvenRun();
                    final int logicalEnd = logicalStart + length - 1;
                    for (int j2 = 0; j2 < length; ++j2) {
                        final int m2 = evenRun ? (logicalStart + j2) : (logicalEnd - j2);
                        final char uchar = bidi.text[m2];
                        if (!Bidi.IsBidiControlChar(uchar)) {
                            indexMap[m++] = m2;
                        }
                    }
                }
            }
        }
        if (allocLength == bidi.resultLength) {
            return indexMap;
        }
        final int[] newMap = new int[bidi.resultLength];
        System.arraycopy(indexMap, 0, newMap, 0, bidi.resultLength);
        return newMap;
    }
    
    static int[] invertMap(final int[] srcMap) {
        final int srcLength = srcMap.length;
        int destLength = -1;
        int count = 0;
        for (final int srcEntry : srcMap) {
            if (srcEntry > destLength) {
                destLength = srcEntry;
            }
            if (srcEntry >= 0) {
                ++count;
            }
        }
        final int[] destMap = new int[++destLength];
        if (count < destLength) {
            Arrays.fill(destMap, -1);
        }
        for (int i = 0; i < srcLength; ++i) {
            final int srcEntry = srcMap[i];
            if (srcEntry >= 0) {
                destMap[srcEntry] = i;
            }
        }
        return destMap;
    }
}
