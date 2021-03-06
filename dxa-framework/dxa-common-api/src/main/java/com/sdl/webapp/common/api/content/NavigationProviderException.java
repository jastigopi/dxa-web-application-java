package com.sdl.webapp.common.api.content;

/**
 * Thrown when an error occurs related to a navigation provider.
 * @deprecated since 1.6, use {@link com.sdl.webapp.common.api.navigation.NavigationProviderException} instead
 */
@Deprecated
public class NavigationProviderException extends ContentProviderException {

    public NavigationProviderException() {
    }

    public NavigationProviderException(String message) {
        super(message);
    }

    public NavigationProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NavigationProviderException(Throwable cause) {
        super(cause);
    }

    public NavigationProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
