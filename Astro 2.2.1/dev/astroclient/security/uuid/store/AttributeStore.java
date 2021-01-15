package dev.astroclient.security.uuid.store;

import dev.astroclient.security.uuid.attribute.Attribute;
import dev.astroclient.security.uuid.attribute.AttributeSupplier;

public interface AttributeStore {

	int VALID = 1;
	int INVALID = -1;
	int UNKNOWN = 0;

	void init();

	int storeKey();

	int getAttributeKey(String id);

	int getAttributeKeyCombination();

	int getAttributeKeyCombinationChina();

	void addAttribute(String id, Attribute<?> attribute);

	void addAttribute(String id, AttributeSupplier supplier);

	int validateAttribute(String id);

	int validateAll();

	boolean invalidateAttribute(String id);

	void invalidateAll();

}

