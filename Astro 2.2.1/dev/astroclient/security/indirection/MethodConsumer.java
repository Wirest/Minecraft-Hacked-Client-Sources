package dev.astroclient.security.indirection;

import java.util.function.Consumer;

@FunctionalInterface
public interface MethodConsumer<A> extends Consumer<A> {

	@Override
	void accept(A a);

}

