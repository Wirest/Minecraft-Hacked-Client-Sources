package dev.astroclient.security.uuid.attribute;

public interface Attribute<T> {

	String getId();

	int getKey();

	T getValue();

	void updateValue();
}
