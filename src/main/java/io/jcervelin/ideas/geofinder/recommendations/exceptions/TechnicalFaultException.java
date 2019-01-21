package io.jcervelin.ideas.geofinder.recommendations.exceptions;

public class TechnicalFaultException extends RuntimeException {
    public TechnicalFaultException(String message) {
        super(message);
    }

    public TechnicalFaultException(String message, Throwable e) {
        super(message,e);
    }
}
