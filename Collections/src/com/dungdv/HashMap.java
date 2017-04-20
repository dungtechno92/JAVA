package com.dungdv;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import sun.security.action.*;;import javax.management.Query;

public class HashMap<K, V> {
    private static final long serialVersionUID = 362498820763181265L;

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final int TREEIFY_THRESHOLD = 8;

    static final int MIN_TREEIFY_CAPACITY = 64;

    transient Node<K,V>[] table;

    transient Set<Map.Entry<K,V>> entrySet;

    transient int size;

    transient int modCount;

    int threshold;

    float loadFactor = DEFAULT_LOAD_FACTOR;

    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = tableSizeFor(DEFAULT_INITIAL_CAPACITY);
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab = table;
        Node<K,V> p;
        int n;
        int i;

        if (tab == null || (n = table.length) == 0) {
            resize();
            n = table.length;
        }

        i = indexOfHash(hash);
        Node<K, V> newNode = new Node(hash, key, value, null);
        Node<K, V> nodeEnd = null;
        Node<K, V> nodeI = tab[i];
        boolean exitKey = false;
        if (nodeI == null) {
            tab[i] = newNode;
        } else {
            for (Node<K,V> nodeNext = nodeI; nodeNext != null; nodeNext = nodeNext.next) {
                if (nodeNext.key == key || nodeNext.key.equals(key)) {
                    nodeNext.value = value;
                    exitKey = true;
                }
                nodeEnd = nodeNext;
            }

            if (!exitKey) {
                nodeEnd.next = newNode;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        return null;
    }

    public V get(K key) {
        Node<K, V> node = getNode(key);
        if(node != null){
            return node.value;
        }
        return null;
    }

    public Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = indexOfHash(hash);
        Node<K, V> nodeI = table[index];
        if (nodeI == null) {
            return null;
        } else {
            for (Node<K,V> nodeNext = nodeI; nodeNext != null; nodeNext = nodeNext.next) {
                if (nodeNext.key == key || nodeNext.key.equals(key)) {
                    return nodeNext;
                }
            }
        }
        return null;
    }

    private int indexOfHash(int hashKey) {
        return (table.length - 1) & hashKey;
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] nodes = null;

        return nodes;
    }
    public static void main(String[] args) {
        HashMap<String, String> mapExample = new HashMap<String, String>();
        mapExample.put("1", "dungdv1");
        mapExample.put("2", "dungdv2");
        mapExample.put("2", "dungdv3");
        System.out.print(mapExample.get("2"));
    }
}
