package com.xqbase.apool.util;

import java.util.Iterator;

/**
 * This class provides the ability to remove the arbitrary interior element (
 * neither head nor tail) from the queue in O(1) time.
 *
 * Adding to and removing from head or tail also run in O(1) time, as expected.
 *
 * This is not thread safe.
 *
 * @author Tony He
 */
public class LinkedDeque<T> extends AbstractDeque<T> {

    @Override
    public boolean offerFirst(T t) {
        return false;
    }

    @Override
    public boolean offerLast(T t) {
        return false;
    }

    @Override
    public T peekFirst() {
        return null;
    }

    @Override
    public T peekLast() {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    @Override
    public T pollFirst() {
        return null;
    }

    @Override
    public T pollLast() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
