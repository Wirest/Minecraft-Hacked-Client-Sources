package dev.astroclient.security.uuid.store;

import dev.astroclient.security.uuid.attribute.Attribute;
import dev.astroclient.security.uuid.attribute.AttributeSupplier;
import dev.astroclient.security.uuid.attribute.AttributeValidator;
import dev.astroclient.security.uuid.attribute.NullAttribute;
import dev.astroclient.security.uuid.attribute.ValidatableAttribute;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SimpleAttributeStore implements AttributeStore {

    private final static Map<String, Attribute<?>> ATTRIBUTE_MAP = new ConcurrentHashMap<>();
    public static final AttributeSupplier[] WANTED_ATTRIBUTES;

    static {
        if (getOSType() == OSType.WIN) {
            WANTED_ATTRIBUTES = new AttributeSupplier[]{
                    AttributeSupplier.SYSTEM_PROCESSOR_ID,
                    AttributeSupplier.COMPUTER_NAME
            };
        } else {
            WANTED_ATTRIBUTES = new AttributeSupplier[]{
                    AttributeSupplier.OS_NAME,
                    AttributeSupplier.USER_HOME
            };
        }
    }

    private static OSType getOSType() {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? OSType.WIN : (s.contains("mac") ? OSType.MAC : OSType.LINUX);

    }

    @Override
    public void init() {
        for (AttributeSupplier supplier : WANTED_ATTRIBUTES) {
            Attribute attribute = supplier.get();
            this.addAttribute(attribute.getId(), attribute);
        }
    }

    @Override
    public int storeKey() {
        int validationStatus = 0;
        for (Attribute<?> attribute : ATTRIBUTE_MAP.values()) {
            validationStatus += attribute instanceof ValidatableAttribute ? VALID : UNKNOWN;
        }

        return validationStatus;
    }

    @Override
    public int getAttributeKey(String id) {
        if (ATTRIBUTE_MAP.containsKey(id)) {
            return ATTRIBUTE_MAP.get(id).getKey();
        }

        return -1;
    }

    @Override
    public int getAttributeKeyCombination() {
        int key = 0;
        for (Attribute<?> attribute : ATTRIBUTE_MAP.values()) {
            key += attribute.getKey();
        }

        return key;
    }

    @Override
    public int getAttributeKeyCombinationChina() {
        int key = 0;

        int[] values = new int[]{
                AttributeSupplier.USER_HOME.get().getKey(),
                AttributeSupplier.COMPUTER_NAME.get().getKey(),
				AttributeSupplier.USER_NAME.get().getKey()
        };

        for (int value : values)
            key += value;
        return key;
    }

    @Override
    public void addAttribute(String id, Attribute<?> attribute) {
        if (!ATTRIBUTE_MAP.containsKey(id)) {
            ATTRIBUTE_MAP.put(id, attribute);
        }
    }

    @Override
    public void addAttribute(String id, AttributeSupplier supplier) {
        if (!ATTRIBUTE_MAP.containsKey(id)) {
            ATTRIBUTE_MAP.put(id, supplier.get());
        }
    }

    @Override
    public int validateAttribute(String id) {
        Attribute<?> attribute = ATTRIBUTE_MAP.get(id);

        if (attribute instanceof ValidatableAttribute) {
            ValidatableAttribute<?> validatableAttribute = (ValidatableAttribute<?>) attribute;
            List<? extends AttributeValidator<?>> validators = validatableAttribute.getValidators();

            boolean valid = false;
            if (!validators.isEmpty()) {
                for (AttributeValidator validator : validators) {
                    valid = validator.test(attribute);

                    if (!valid) {
                        break;
                    }
                }
            }

            return valid ? VALID : INVALID;
        }

        return UNKNOWN;
    }

    @Override
    public int validateAll() {
        int validationStatus = 0;
        for (String id : ATTRIBUTE_MAP.keySet()) {
            validationStatus += validateAttribute(id);
        }

        return validationStatus;
    }

    @Override
    public boolean invalidateAttribute(String id) {
        if (ATTRIBUTE_MAP.containsKey(id)) {
            return ATTRIBUTE_MAP.remove(id) != null;
        }

        return false;
    }

    @Override
    public void invalidateAll() {
        ATTRIBUTE_MAP.replaceAll((s, attribute) -> new NullAttribute<>());
    }

    private enum OSType {
        WIN, MAC, LINUX
    }
}
