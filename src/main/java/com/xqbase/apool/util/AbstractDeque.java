package com.xqbase.apool.util;

import java.util.AbstractQueue;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        if (!offerLast(e)) {
            throw new IllegalStateException("Queue full");
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Iterator<E> i = iterator(); i.hasNext(); ) {
            if (i.next().equals(o)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Iterator<E> i = descendingIterator(); i.hasNext(); ) {
            if (i.next().equals(o)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public E removeFirst() {
        E e = pollFirst();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override
    public E removeLast() {
        E e = pollLast();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override
    public E getFirst() {
        E e = peekFirst();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override
    public E getLast() {
        E e = peekLast();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // java.util.Queue methods

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }
}
