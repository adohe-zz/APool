package com.xqbase.apool.util;

import java.util.AbstractQueue;
import java.util.Deque;

/**
 * This class provides skeletal implementations of some {@link Deque}
 * operations.
 *
 * @author Tony He
 */
public abstract class AbstractDeque<E> extends AbstractQueue<E> implements Deque<E> {

    @Override
    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Queue full");
        }
    }

    @Override
    public void addLast(E e) {

    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E getLast() {
        return null;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    // java.util.Queue methods

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
