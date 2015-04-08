package com.xqbase.apool.util;

import java.util.Collection;
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

    /**
     * Internal Node Class
     */
    public static class Node<T> {

        private final T value;
        private Node<T> next;
        private Node<T> prev;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Construct a new instance.
     */
    public LinkedDeque() {
        super();
    }

    /**
     * Construct a new instance, adding all objects in the specified collection.
     *
     * @param collection a {@link Collection} of objects to be added.
     */
    public LinkedDeque(Collection<T> collection) {
        super();
        addAll(collection);
    }

    /**
     * Add a new item at the head of the queue.
     *
     * @param item the item to be added.
     * @return the {@link Node} of the newly added item.
     */
    public Node<T> addFirstNode(T item) {
        return addBeforeNode(head, item);
    }

    /**
     * Add a new item at the tail of the queue.
     *
     * @param item the item to be added.
     * @return the {@link Node} of the newly added item.
     */
    public Node<T> addLastNode(T item) {
        return addBeforeNode(null, item);
    }

    /**
     * Add a new item before the specified node.
     *
     * @param before before the {@link Node} before which the item should be added.
     * @param item the item to be added.
     * @return the {@link Node} of the newly added item.
     */
    public Node<T> addBeforeNode(Node<T> before, T item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (before != null && before != head && before.next == null && before.prev == null) {
            throw new IllegalStateException("node does not exist");
        }

        Node<T> node = new Node<>(item);
        if (before == null) {
            // add to tail
            node.next = null;
            node.prev = tail;
            if (tail != null) {
                tail.next = node;
            }
            tail = node;
            if (head == null) {
                head = node;
            }
        } else {
            node.next = before;
            node.prev = before.prev;
            before.prev = node;
            if (before == head) {
                head = node;
            }
        }
        size ++;
        return node;
    }

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
