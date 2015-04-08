package com.xqbase.apool.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

        private final LinkedDeque queue;
        private final T value;
        private Node<T> next;
        private Node<T> prev;

        public Node(LinkedDeque queue, T value) {
            this.queue = queue;
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

        if (before != null && before.queue != this) {
            throw new IllegalStateException("before does not belong to this queue");
        }

        if (before != null && before != head && before.next == null && before.prev == null) {
            throw new IllegalStateException("node does not exist");
        }

        Node<T> node = new Node<>(this, item);
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

    /**
     * Remove the specified node from the queue.
     *
     * @param node the Node to be removed.
     * @return the item contained in the Node which was removed.
     */
    public T removeNode(Node<T> node) {
        if (node.queue != this) {
            throw new IllegalArgumentException("node does not belong to this queue");
        }

        if (node != head && node.next == null && node.prev == null) {
            return null;
        }

        if (head == node) {
            head = node.next;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (tail == node) {
            tail = node.prev;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.next = null;
        node.prev = null;
        return node.value;
    }

    @Override
    public boolean offerFirst(T t) {
        addFirstNode(t);
        return true;
    }

    @Override
    public boolean offerLast(T t) {
        addLastNode(t);
        return true;
    }

    @Override
    public T peekFirst() {
        if (head == null) {
            return null;
        }
        return head.value;
    }

    @Override
    public T peekLast() {
        if (tail == null) {
            return null;
        }
        return tail.value;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new LinkedQueueIterator(Direction.DESCENDING);
    }

    @Override
    public T pollFirst() {
        if (head == null) {
            return null;
        }
        return removeNode(head);
    }

    @Override
    public T pollLast() {
        if (tail == null) {
            return null;
        }
        return removeNode(tail);
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedQueueIterator(Direction.ASCENDING);
    }

    @Override
    public int size() {
        return size;
    }

    private enum Direction { ASCENDING, DESCENDING }

    private class LinkedQueueIterator implements Iterator<T> {

        private Direction direction;
        private Node<T> index;
        private Node<T> last;

        public LinkedQueueIterator(Direction direction) {
            this.direction = direction;
            index = direction == Direction.ASCENDING ? head : tail;
        }

        @Override
        public boolean hasNext() {
            return index != null;
        }

        @Override
        public T next() {
            if (index == null) {
                throw new NoSuchElementException();
            }
            last = index;
            T value = index.value;
            index = direction == Direction.ASCENDING ? index.next : index.prev;
            return value;
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            removeNode(last);
            last = null;
        }
    }
}
