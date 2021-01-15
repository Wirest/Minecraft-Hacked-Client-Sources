package me.xatzdevelopments.xatz.client.thealtening.api.utils.exceptions;

public class TheAlteningException extends RuntimeException
{
    public TheAlteningException(final String error, final String errorMessage) {
        super(String.format("[%s]: %s", error, errorMessage));
    }
}
