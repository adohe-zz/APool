package com.xqbase.apool.util;

/**
 * Object that represents no value.
 *
 * @author Tony He
 */
public class None {

    private static final None INSTANCE = new None();

    private None() {

    }

    public static None none() {
        return INSTANCE;
    }
}
