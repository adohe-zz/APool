package com.xqbase.apool.exceptions;

/**
 * Represents an exception that exceed certain size limit.
 *
 * @author Tony He
 */
public class SizeLimitExceededException extends Exception {

    public SizeLimitExceededException(String message) {
        super(message);
    }
}
