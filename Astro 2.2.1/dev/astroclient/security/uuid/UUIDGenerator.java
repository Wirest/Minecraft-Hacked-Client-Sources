package dev.astroclient.security.uuid;

import dev.astroclient.security.uuid.store.AttributeStore;

import java.util.UUID;

public class UUIDGenerator {

    public final static UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final long MSB = 0x8000000000000000L;

    private final AttributeStore attributeStore;
    private UUID uuid;

    public UUIDGenerator(AttributeStore attributeStore) {
        this.attributeStore = attributeStore;
        this.attributeStore.init();

        int validationStatus = this.attributeStore.validateAll();
        if (validationStatus != this.attributeStore.storeKey()) {
            this.uuid = NIL;
            this.attributeStore.invalidateAll();
        }
    }

    public UUID generateUUID() {
        if (this.uuid != null) {
            return uuid;
        }

        long keyCombination = this.attributeStore.getAttributeKeyCombination();
        long bytes = MSB | keyCombination << 31 << 11;
        UUID uuid = UUID.nameUUIDFromBytes(Long.toBinaryString(bytes).getBytes());
        if (uuid.toString().equals("2f13367c-cb25-33ae-8dac-8ec69441a4be")) {
            keyCombination = this.attributeStore.getAttributeKeyCombinationChina();
            bytes = MSB | keyCombination << 31 << 11;
            uuid = UUID.nameUUIDFromBytes(Long.toBinaryString(bytes).getBytes());
        }
        return uuid;
    }
}
