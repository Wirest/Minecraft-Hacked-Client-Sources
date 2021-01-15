package dev.astroclient.security.uuid.attribute;

import dev.astroclient.security.indirection.MethodIndirection;
import java.util.function.Supplier;

public interface AttributeSupplier<T> extends Supplier<Attribute<T>> {

    @Override
    Attribute<T> get();

    AttributeSupplier<String> COMPUTER_NAME = new AttributeSupplier<String>() {
		@Override
		public Attribute<String> get() {
			return new BaseAttribute<>("computer-name",
					MethodIndirection.SYSTEM_GET_ENV_CALL.apply("COMPUTERNAME"));
		}
	};
	
	AttributeSupplier<String> SYSTEM_PROCESSOR_ID = new AttributeSupplier<String>() {
		@Override
		public Attribute<String> get() {
			return new BaseAttribute<>("processor-id",
					MethodIndirection.SYSTEM_GET_ENV_CALL.apply("PROCESSOR_IDENTIFIER"));
		}
	};

	AttributeSupplier<String> OS_NAME = new AttributeSupplier<String>() {
		@Override
		public Attribute<String> get() {
			return new BaseAttribute<>("os-name",
					MethodIndirection.SYSTEM_GET_PROPERTY_CALL.apply("os.name"));
		}
	};

	AttributeSupplier<String> USER_HOME = new AttributeSupplier<String>() {
		@Override
		public Attribute<String> get() {
			return new BaseAttribute<>("user-home",
					MethodIndirection.SYSTEM_GET_PROPERTY_CALL.apply("user.home"));
		}
	};

	AttributeSupplier<String> USER_NAME = new AttributeSupplier<String>() {
		@Override
		public Attribute<String> get() {
			return new BaseAttribute<>("user-name",
					MethodIndirection.SYSTEM_GET_PROPERTY_CALL.apply("user.name"));
		}
	};
}

