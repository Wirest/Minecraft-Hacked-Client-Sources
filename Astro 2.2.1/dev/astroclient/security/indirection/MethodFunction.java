package dev.astroclient.security.indirection;

import java.util.function.Function;

@FunctionalInterface
public interface MethodFunction<A, R> extends Function<A, R> {

	@Override
	R apply(A argument);

}
