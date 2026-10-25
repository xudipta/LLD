package problems.lrucache;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap for O(1) lookup plus a doubly linked list for O(1) recency updates.
 * Not thread-safe: the two thread-safety variants below wrap it.
 */
class LruCore<K, V> {
    private static final class Node<K, V> {
        final K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node<K, V>> map = new HashMap<>();
    private final Node<K, V> head = new Node<>(null, null); // most recent after head
    private final Node<K, V> tail = new Node<>(null, null); // least recent before tail

    LruCore(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }
        unlink(node);
        addFirst(node);
        return node.value;
    }

    void put(K key, V value) {
        Node<K, V> node = map.get(key);
        if (node != null) {
            node.value = value;
            unlink(node);
            addFirst(node);
            return;
        }
        if (map.size() == capacity) {
            Node<K, V> eldest = tail.prev;
            unlink(eldest);
            map.remove(eldest.key);
        }
        node = new Node<>(key, value);
        map.put(key, node);
        addFirst(node);
    }

    int size() {
        return map.size();
    }

    /** Walks the list and checks it agrees with the map and the capacity. */
    boolean isConsistent() {
        int count = 0;
        Node<K, V> n = head.next;
        while (n != tail) {
            if (n == null || map.get(n.key) != n) {
                return false;
            }
            count++;
            if (count > capacity) {
                return false;
            }
            n = n.next;
        }
        return count == map.size() && map.size() <= capacity;
    }

    private void unlink(Node<K, V> n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    private void addFirst(Node<K, V> n) {
        n.next = head.next;
        n.prev = head;
        head.next.prev = n;
        head.next = n;
    }
}
