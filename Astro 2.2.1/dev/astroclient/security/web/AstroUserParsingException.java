package dev.astroclient.security.web;

public final class AstroUserParsingException extends RuntimeException {
	public AstroUserParsingException(String msg) {
		super(msg);
	}

	public AstroUserParsingException(String errorType, Throwable cause) {
		super(errorType, cause);
	}
}