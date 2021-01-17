// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class Region implements Comparable<Region>
{
    public static final int UNDEFINED_NUMERIC_CODE = -1;
    private String id;
    private int code;
    private RegionType type;
    private Region containingRegion;
    private Set<Region> containedRegions;
    private List<Region> preferredValues;
    private static boolean regionDataIsLoaded;
    private static Map<String, Region> regionIDMap;
    private static Map<Integer, Region> numericCodeMap;
    private static Map<String, Region> regionAliases;
    private static ArrayList<Region> regions;
    private static ArrayList<Set<Region>> availableRegions;
    private static final String UNKNOWN_REGION_ID = "ZZ";
    private static final String OUTLYING_OCEANIA_REGION_ID = "QO";
    private static final String WORLD_ID = "001";
    
    private Region() {
        this.containingRegion = null;
        this.containedRegions = new TreeSet<Region>();
        this.preferredValues = null;
    }
    
    private static synchronized void loadRegionData() {
        if (Region.regionDataIsLoaded) {
            return;
        }
        Region.regionAliases = new HashMap<String, Region>();
        Region.regionIDMap = new HashMap<String, Region>();
        Region.numericCodeMap = new HashMap<Integer, Region>();
        Region.availableRegions = new ArrayList<Set<Region>>(RegionType.values().length);
        UResourceBundle regionCodes = null;
        UResourceBundle territoryAlias = null;
        UResourceBundle codeMappings = null;
        UResourceBundle worldContainment = null;
        UResourceBundle territoryContainment = null;
        UResourceBundle groupingContainment = null;
        final UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        regionCodes = rb.get("regionCodes");
        territoryAlias = rb.get("territoryAlias");
        final UResourceBundle rb2 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        codeMappings = rb2.get("codeMappings");
        territoryContainment = rb2.get("territoryContainment");
        worldContainment = territoryContainment.get("001");
        groupingContainment = territoryContainment.get("grouping");
        final String[] continentsArr = worldContainment.getStringArray();
        final List<String> continents = Arrays.asList(continentsArr);
        final String[] groupingArr = groupingContainment.getStringArray();
        final List<String> groupings = Arrays.asList(groupingArr);
        final int regionCodeSize = regionCodes.getSize();
        Region.regions = new ArrayList<Region>(regionCodeSize);
        for (int i = 0; i < regionCodeSize; ++i) {
            final Region r = new Region();
            final String id = regionCodes.getString(i);
            r.id = id;
            r.type = RegionType.TERRITORY;
            Region.regionIDMap.put(id, r);
            if (id.matches("[0-9]{3}")) {
                r.code = Integer.valueOf(id);
                Region.numericCodeMap.put(r.code, r);
                r.type = RegionType.SUBCONTINENT;
            }
            else {
                r.code = -1;
            }
            Region.regions.add(r);
        }
        for (int i = 0; i < territoryAlias.getSize(); ++i) {
            final UResourceBundle res = territoryAlias.get(i);
            final String aliasFrom = res.getKey();
            final String aliasTo = res.getString();
            if (Region.regionIDMap.containsKey(aliasTo) && !Region.regionIDMap.containsKey(aliasFrom)) {
                Region.regionAliases.put(aliasFrom, Region.regionIDMap.get(aliasTo));
            }
            else {
                Region r2;
                if (Region.regionIDMap.containsKey(aliasFrom)) {
                    r2 = Region.regionIDMap.get(aliasFrom);
                }
                else {
                    r2 = new Region();
                    r2.id = aliasFrom;
                    Region.regionIDMap.put(aliasFrom, r2);
                    if (aliasFrom.matches("[0-9]{3}")) {
                        r2.code = Integer.valueOf(aliasFrom);
                        Region.numericCodeMap.put(r2.code, r2);
                    }
                    else {
                        r2.code = -1;
                    }
                    Region.regions.add(r2);
                }
                r2.type = RegionType.DEPRECATED;
                final List<String> aliasToRegionStrings = Arrays.asList(aliasTo.split(" "));
                r2.preferredValues = new ArrayList<Region>();
                for (final String s : aliasToRegionStrings) {
                    if (Region.regionIDMap.containsKey(s)) {
                        r2.preferredValues.add(Region.regionIDMap.get(s));
                    }
                }
            }
        }
        for (int i = 0; i < codeMappings.getSize(); ++i) {
            final UResourceBundle mapping = codeMappings.get(i);
            if (mapping.getType() == 8) {
                final String[] codeMappingStrings = mapping.getStringArray();
                final String codeMappingID = codeMappingStrings[0];
                final Integer codeMappingNumber = Integer.valueOf(codeMappingStrings[1]);
                final String codeMapping3Letter = codeMappingStrings[2];
                if (Region.regionIDMap.containsKey(codeMappingID)) {
                    final Region r3 = Region.regionIDMap.get(codeMappingID);
                    r3.code = codeMappingNumber;
                    Region.numericCodeMap.put(r3.code, r3);
                    Region.regionAliases.put(codeMapping3Letter, r3);
                }
            }
        }
        if (Region.regionIDMap.containsKey("001")) {
            final Region r4 = Region.regionIDMap.get("001");
            r4.type = RegionType.WORLD;
        }
        if (Region.regionIDMap.containsKey("ZZ")) {
            final Region r4 = Region.regionIDMap.get("ZZ");
            r4.type = RegionType.UNKNOWN;
        }
        for (final String continent : continents) {
            if (Region.regionIDMap.containsKey(continent)) {
                final Region r4 = Region.regionIDMap.get(continent);
                r4.type = RegionType.CONTINENT;
            }
        }
        for (final String grouping : groupings) {
            if (Region.regionIDMap.containsKey(grouping)) {
                final Region r4 = Region.regionIDMap.get(grouping);
                r4.type = RegionType.GROUPING;
            }
        }
        if (Region.regionIDMap.containsKey("QO")) {
            final Region r4 = Region.regionIDMap.get("QO");
            r4.type = RegionType.SUBCONTINENT;
        }
        for (int j = 0; j < territoryContainment.getSize(); ++j) {
            final UResourceBundle mapping2 = territoryContainment.get(j);
            final String parent = mapping2.getKey();
            final Region parentRegion = Region.regionIDMap.get(parent);
            for (int k = 0; k < mapping2.getSize(); ++k) {
                final String child = mapping2.getString(k);
                final Region childRegion = Region.regionIDMap.get(child);
                if (parentRegion != null && childRegion != null) {
                    parentRegion.containedRegions.add(childRegion);
                    if (parentRegion.getType() != RegionType.GROUPING) {
                        childRegion.containingRegion = parentRegion;
                    }
                }
            }
        }
        for (int j = 0; j < RegionType.values().length; ++j) {
            Region.availableRegions.add(new TreeSet<Region>());
        }
        for (final Region ar : Region.regions) {
            final Set<Region> currentSet = Region.availableRegions.get(ar.type.ordinal());
            currentSet.add(ar);
            Region.availableRegions.set(ar.type.ordinal(), currentSet);
        }
        Region.regionDataIsLoaded = true;
    }
    
    public static Region getInstance(final String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        loadRegionData();
        Region r = Region.regionIDMap.get(id);
        if (r == null) {
            r = Region.regionAliases.get(id);
        }
        if (r == null) {
            throw new IllegalArgumentException("Unknown region id: " + id);
        }
        if (r.type == RegionType.DEPRECATED && r.preferredValues.size() == 1) {
            r = r.preferredValues.get(0);
        }
        return r;
    }
    
    public static Region getInstance(final int code) {
        loadRegionData();
        Region r = Region.numericCodeMap.get(code);
        if (r == null) {
            String pad = "";
            if (code < 10) {
                pad = "00";
            }
            else if (code < 100) {
                pad = "0";
            }
            final String id = pad + Integer.toString(code);
            r = Region.regionAliases.get(id);
        }
        if (r == null) {
            throw new IllegalArgumentException("Unknown region code: " + code);
        }
        if (r.type == RegionType.DEPRECATED && r.preferredValues.size() == 1) {
            r = r.preferredValues.get(0);
        }
        return r;
    }
    
    public static Set<Region> getAvailable(final RegionType type) {
        loadRegionData();
        return Collections.unmodifiableSet((Set<? extends Region>)Region.availableRegions.get(type.ordinal()));
    }
    
    public Region getContainingRegion() {
        loadRegionData();
        return this.containingRegion;
    }
    
    public Region getContainingRegion(final RegionType type) {
        loadRegionData();
        if (this.containingRegion == null) {
            return null;
        }
        if (this.containingRegion.type.equals(type)) {
            return this.containingRegion;
        }
        return this.containingRegion.getContainingRegion(type);
    }
    
    public Set<Region> getContainedRegions() {
        loadRegionData();
        return Collections.unmodifiableSet((Set<? extends Region>)this.containedRegions);
    }
    
    public Set<Region> getContainedRegions(final RegionType type) {
        loadRegionData();
        final Set<Region> result = new TreeSet<Region>();
        final Set<Region> cr = this.getContainedRegions();
        for (final Region r : cr) {
            if (r.getType() == type) {
                result.add(r);
            }
            else {
                result.addAll(r.getContainedRegions(type));
            }
        }
        return Collections.unmodifiableSet((Set<? extends Region>)result);
    }
    
    public List<Region> getPreferredValues() {
        loadRegionData();
        if (this.type == RegionType.DEPRECATED) {
            return Collections.unmodifiableList((List<? extends Region>)this.preferredValues);
        }
        return null;
    }
    
    public boolean contains(final Region other) {
        loadRegionData();
        if (this.containedRegions.contains(other)) {
            return true;
        }
        for (final Region cr : this.containedRegions) {
            if (cr.contains(other)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.id;
    }
    
    public int getNumericCode() {
        return this.code;
    }
    
    public RegionType getType() {
        return this.type;
    }
    
    public int compareTo(final Region other) {
        return this.id.compareTo(other.id);
    }
    
    static {
        Region.regionDataIsLoaded = false;
        Region.regionIDMap = null;
        Region.numericCodeMap = null;
        Region.regionAliases = null;
        Region.regions = null;
        Region.availableRegions = null;
    }
    
    public enum RegionType
    {
        UNKNOWN, 
        TERRITORY, 
        WORLD, 
        CONTINENT, 
        SUBCONTINENT, 
        GROUPING, 
        DEPRECATED;
    }
}
