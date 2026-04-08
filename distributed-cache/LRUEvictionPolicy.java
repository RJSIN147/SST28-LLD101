import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Least Recently Used (LRU) eviction policy.
 *
 * Uses a doubly linked list + HashMap for O(1) access, insert, and eviction.
 *
 * ── How it works ──
 * - On every access (get/put), the key is moved to the tail (most recent).
 * - On eviction, the head (least recent) is removed.
 *
 * ── Data Structures ──
 * - DoublyLinkedList: maintains access order (head = least recent, tail = most recent)
 * - HashMap<K, Node<K>>: enables O(1) lookup of any key's position in the list
 *
 * @param <K> the type of cache keys
 */
public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final Map<K, Node<K>> nodeMap;
    private final Node<K> head; // sentinel — head.next is the least recently used
    private final Node<K> tail; // sentinel — tail.prev is the most recently used

    public LRUEvictionPolicy() {
        this.nodeMap = new HashMap<>();
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void keyAccessed(K key) {
        if (nodeMap.containsKey(key)) {
            // Move existing node to tail (most recent)
            Node<K> node = nodeMap.get(key);
            removeNode(node);
            addToTail(node);
        } else {
            // New key — add to tail
            Node<K> node = new Node<>(key);
            addToTail(node);
            nodeMap.put(key, node);
        }
    }

    @Override
    public K evictKey() {
        if (head.next == tail) {
            throw new NoSuchElementException("No keys to evict — policy is empty");
        }
        Node<K> lru = head.next;
        removeNode(lru);
        nodeMap.remove(lru.key);
        return lru.key;
    }

    @Override
    public void keyRemoved(K key) {
        Node<K> node = nodeMap.remove(key);
        if (node != null) {
            removeNode(node);
        }
    }

    // ── Doubly Linked List helpers ──────────────────────────────

    private void addToTail(Node<K> node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    private void removeNode(Node<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // ── Inner Node class ────────────────────────────────────────

    private static class Node<K> {
        K key;
        Node<K> prev;
        Node<K> next;

        Node(K key) {
            this.key = key;
        }
    }
}
