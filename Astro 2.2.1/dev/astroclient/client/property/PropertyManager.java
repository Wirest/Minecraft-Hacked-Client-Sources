package dev.astroclient.client.property;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Feature;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class PropertyManager {

    private HashMap<Feature, List<Property>> propertyMap;

    public PropertyManager() {
        propertyMap = new HashMap<>();
    }

    public Feature getParent(Property property) {
        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            for (Property property1 : getPropertiesForFeature(feature)) {
                if (property1 == property)
                    return feature;
            }
        }
        return null;
    }

    public Property getPropertyFromList(List<Property> properties, String name) {
        for (Property property : properties) {
            if (property.getName().equalsIgnoreCase(name))
                return property;
        }
        return null;
    }

    public void registerFeature(Feature feature) {
        List<Property> values = new ArrayList<>();
        for (final Field field : feature.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object obj = field.get(feature);

                if (obj instanceof Property)
                    values.add((Property) obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        propertyMap.put(feature, values);
    }

    public List<Property> getPropertiesForFeature(Feature parent) {
        return propertyMap.get(parent);
    }

    public Property getProperty(Feature feature, String label) {
        for(Property property : getPropertiesForFeature(feature)) {
            if(property.getName().equalsIgnoreCase(label)) {
                return property;
            }
        }

        return null;
    }

    public List<Property> getProperties() {
        List<Property> propertyCollection = new ArrayList<>();
        for (List<Property> property : propertyMap.values())
            propertyCollection.addAll(property);
        return propertyCollection;
    }
}
