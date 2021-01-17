// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class MapGenStructureIO
{
    private static final Logger logger;
    private static Map<String, Class<? extends StructureStart>> startNameToClassMap;
    private static Map<Class<? extends StructureStart>, String> startClassToNameMap;
    private static Map<String, Class<? extends StructureComponent>> componentNameToClassMap;
    private static Map<Class<? extends StructureComponent>, String> componentClassToNameMap;
    
    static {
        logger = LogManager.getLogger();
        MapGenStructureIO.startNameToClassMap = (Map<String, Class<? extends StructureStart>>)Maps.newHashMap();
        MapGenStructureIO.startClassToNameMap = (Map<Class<? extends StructureStart>, String>)Maps.newHashMap();
        MapGenStructureIO.componentNameToClassMap = (Map<String, Class<? extends StructureComponent>>)Maps.newHashMap();
        MapGenStructureIO.componentClassToNameMap = (Map<Class<? extends StructureComponent>, String>)Maps.newHashMap();
        registerStructure(StructureMineshaftStart.class, "Mineshaft");
        registerStructure(MapGenVillage.Start.class, "Village");
        registerStructure(MapGenNetherBridge.Start.class, "Fortress");
        registerStructure(MapGenStronghold.Start.class, "Stronghold");
        registerStructure(MapGenScatteredFeature.Start.class, "Temple");
        registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
        StructureMineshaftPieces.registerStructurePieces();
        StructureVillagePieces.registerVillagePieces();
        StructureNetherBridgePieces.registerNetherFortressPieces();
        StructureStrongholdPieces.registerStrongholdPieces();
        ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
        StructureOceanMonumentPieces.registerOceanMonumentPieces();
    }
    
    private static void registerStructure(final Class<? extends StructureStart> startClass, final String structureName) {
        MapGenStructureIO.startNameToClassMap.put(structureName, startClass);
        MapGenStructureIO.startClassToNameMap.put(startClass, structureName);
    }
    
    static void registerStructureComponent(final Class<? extends StructureComponent> componentClass, final String componentName) {
        MapGenStructureIO.componentNameToClassMap.put(componentName, componentClass);
        MapGenStructureIO.componentClassToNameMap.put(componentClass, componentName);
    }
    
    public static String getStructureStartName(final StructureStart start) {
        return MapGenStructureIO.startClassToNameMap.get(start.getClass());
    }
    
    public static String getStructureComponentName(final StructureComponent component) {
        return MapGenStructureIO.componentClassToNameMap.get(component.getClass());
    }
    
    public static StructureStart getStructureStart(final NBTTagCompound tagCompound, final World worldIn) {
        StructureStart structurestart = null;
        try {
            final Class<? extends StructureStart> oclass = MapGenStructureIO.startNameToClassMap.get(tagCompound.getString("id"));
            if (oclass != null) {
                structurestart = (StructureStart)oclass.newInstance();
            }
        }
        catch (Exception exception) {
            MapGenStructureIO.logger.warn("Failed Start with id " + tagCompound.getString("id"));
            exception.printStackTrace();
        }
        if (structurestart != null) {
            structurestart.readStructureComponentsFromNBT(worldIn, tagCompound);
        }
        else {
            MapGenStructureIO.logger.warn("Skipping Structure with id " + tagCompound.getString("id"));
        }
        return structurestart;
    }
    
    public static StructureComponent getStructureComponent(final NBTTagCompound tagCompound, final World worldIn) {
        StructureComponent structurecomponent = null;
        try {
            final Class<? extends StructureComponent> oclass = MapGenStructureIO.componentNameToClassMap.get(tagCompound.getString("id"));
            if (oclass != null) {
                structurecomponent = (StructureComponent)oclass.newInstance();
            }
        }
        catch (Exception exception) {
            MapGenStructureIO.logger.warn("Failed Piece with id " + tagCompound.getString("id"));
            exception.printStackTrace();
        }
        if (structurecomponent != null) {
            structurecomponent.readStructureBaseNBT(worldIn, tagCompound);
        }
        else {
            MapGenStructureIO.logger.warn("Skipping Piece with id " + tagCompound.getString("id"));
        }
        return structurecomponent;
    }
}
