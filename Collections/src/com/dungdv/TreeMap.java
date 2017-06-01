package com.dungdv;

import java.util.*;
import java.util.ArrayList;

/**
 * Created by DungDV on 4/22/2017.
 */
public class TreeMap<K, V> {

    private final Comparator<Object> comparator;
    private transient Entry<K, V> root;
    private transient int size = 0;
    private transient int modCount = 0;
    private transient NavigableMap<K, V> descendingMap;
    private static final Object UNBOUNDED = new Object();
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private Object var1;

    public TreeMap() {
        this.comparator = null;
    }

    static final boolean valEquals(Object var0, Object var1) {
        return var0 == null?var1 == null:var0.equals(var1);
    }

    static final class Entry<K, V> implements java.util.Map.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        boolean color = true;

        Entry(K var1, V var2, Entry<K, V> var3) {
            this.key = var1;
            this.value = var2;
            this.parent = var3;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V var1) {
            V var2 = this.value;
            this.value = var1;
            return var2;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry))
                return false;
            Entry<?,?> e = (Entry<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int var1 = this.key == null?0:this.key.hashCode();
            int var2 = this.value == null?0:this.value.hashCode();
            return var1 ^ var2;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    final Entry<K, V> getEntry(Object var1) {
        if(this.comparator != null) {
            return this.getEntryUsingComparator(var1);
        } else if(var1 == null) {
            throw new NullPointerException();
        } else {
            Comparable var2 = (Comparable)var1;
            Entry var3 = this.root;

            while(var3 != null) {
                int var4 = var2.compareTo(var3.key);
                if(var4 < 0) {
                    var3 = var3.left;
                } else {
                    if(var4 <= 0) {
                        return var3;
                    }

                    var3 = var3.right;
                }
            }

            return null;
        }
    }

    final Entry<K, V> getEntryUsingComparator(Object var1) {
        Object var2 = var1;
        Comparator var3 = this.comparator;
        if(var3 != null) {
            Entry var4 = this.root;

            while(var4 != null) {
                int var5 = var3.compare(var2, var4.key);
                if(var5 < 0) {
                    var4 = var4.left;
                } else {
                    if(var5 <= 0) {
                        return var4;
                    }

                    var4 = var4.right;
                }
            }
        }

        return null;
    }

    public V put(K newKey, V newValue) {
        Entry entryRoot = this.root;
        // if tree is empty
        if(entryRoot == null) {
            this.compare(newKey, newKey);
            this.root = new Entry(newKey, newValue, (Entry)null);
            this.size = 1;
            ++this.modCount;
            return null;
        } else {
            Comparator comparator = this.comparator;
            int compareResult;
            Entry parentEntry;
            if(newKey == null) {
                throw new NullPointerException();
            }

            Comparable var7 = (Comparable)newKey;

            // find parent for new entry
            do {
                parentEntry = entryRoot;
                compareResult = var7.compareTo(entryRoot.key);
                if(compareResult < 0) {
                    entryRoot = entryRoot.left;
                } else {
                    // If key exiting in tree
                    if(compareResult == 0) {
                        return (V) entryRoot.setValue(newValue);
                    }

                    entryRoot = entryRoot.right;
                }
            } while(entryRoot != null);

            // New entry to insert into Tree
            Entry newEntry = new Entry(newKey, newValue, parentEntry);
            if(compareResult < 0) {
                parentEntry.left = newEntry;
            } else {
                parentEntry.right = newEntry;
            }

            this.fixAfterInsertion(newEntry);
            ++this.size;
            ++this.modCount;
            return null;
        }
    }

    private void fixAfterInsertion(Entry<K, V> newEntry) {
        newEntry.color = RED;

        while(newEntry != null && newEntry != this.root && newEntry.parent.color == RED) {
            Entry uncelEntry;
            if(parentOf(newEntry) == leftOf(parentOf(parentOf(newEntry)))) {
                uncelEntry = rightOf(parentOf(parentOf(newEntry)));
                // uncel is red
                if(!colorOf(uncelEntry)) {
                    setColor(parentOf(newEntry), BLACK);
                    setColor(uncelEntry, BLACK);
                    setColor(parentOf(parentOf(newEntry)), RED);
                    newEntry = parentOf(parentOf(newEntry));
                } else {
                    if(newEntry == rightOf(parentOf(newEntry))) {
                        newEntry = parentOf(newEntry);
                        this.rotateLeft(newEntry);
                    }

                    setColor(parentOf(newEntry), BLACK);
                    setColor(parentOf(parentOf(newEntry)), RED);
                    this.rotateRight(parentOf(parentOf(newEntry)));
                }
            } else {
                uncelEntry = leftOf(parentOf(parentOf(newEntry)));
                if(!colorOf(uncelEntry)) {
                    setColor(parentOf(newEntry), BLACK);
                    setColor(uncelEntry, BLACK);
                    setColor(parentOf(parentOf(newEntry)), RED);
                    newEntry = parentOf(parentOf(newEntry));
                } else {
                    if(newEntry == leftOf(parentOf(newEntry))) {
                        newEntry = parentOf(newEntry);
                        this.rotateRight(newEntry);
                    }

                    setColor(parentOf(newEntry), BLACK);
                    setColor(parentOf(parentOf(newEntry)), RED);
                    this.rotateLeft(parentOf(parentOf(newEntry)));
                }
            }
        }

        this.root.color = true;
    }

    private static <K, V> void setColor(Entry<K, V> var0, boolean var1) {
        if(var0 != null) {
            var0.color = var1;
        }

    }

    final int compare(Object var1, Object var2) {
        return this.comparator == null?((Comparable)var1).compareTo(var2):this.comparator.compare(var1, var2);
    }

    private static <K, V> Entry<K, V> rightOf(Entry<K, V> var0) {
        return var0 == null?null:var0.right;
    }

    private void rotateLeft(Entry<K, V> var1) {
        if(var1 != null) {
            Entry var2 = var1.right;
            var1.right = var2.left;
            if(var2.left != null) {
                var2.left.parent = var1;
            }

            var2.parent = var1.parent;
            if(var1.parent == null) {
                this.root = var2;
            } else if(var1.parent.left == var1) {
                var1.parent.left = var2;
            } else {
                var1.parent.right = var2;
            }

            var2.left = var1;
            var1.parent = var2;
        }

    }

    private void rotateRight(Entry<K, V> var1) {
        if(var1 != null) {
            Entry var2 = var1.left;
            var1.left = var2.right;
            if(var2.right != null) {
                var2.right.parent = var1;
            }

            var2.parent = var1.parent;
            if(var1.parent == null) {
                this.root = var2;
            } else if(var1.parent.right == var1) {
                var1.parent.right = var2;
            } else {
                var1.parent.left = var2;
            }

            var2.right = var1;
            var1.parent = var2;
        }

    }

    private static <K, V> Entry<K, V> leftOf(Entry<K, V> var0) {
        return var0 == null?null:var0.left;
    }

    private static <K, V> boolean colorOf(Entry<K, V> var0) {
        return var0 == null?true:var0.color;
    }

    private static <K, V> Entry<K, V> parentOf(Entry<K, V> var0) {
        return var0 == null?null:var0.parent;
    }

    final Entry<K,V> getFirstEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    public Entry<K, V> getRoot() {
        return root;
    }

    public void printTree(Entry<K, V> root) {
        List<Entry<K, V>> nodes = new ArrayList<>();
        nodes.add(root);
        int level = 0;
        while(nodes.size() > 0) {
            List<Entry<K, V>> nodeTemps = new ArrayList<>();
            System.out.println("level" + level);
            for (Entry<K, V> node : nodes) {
                StringBuilder text = new StringBuilder("==========" + node.getKey());
                if(parentOf(node) != null) {
                    text.append("=" + parentOf(node).getKey());
                } else {
                    text.append("=" + null);
                }
                text.append("=" + node.color);
                System.out.println(text.toString());
                if (node.left != null) {
                    nodeTemps.add(node.left);
                }
                if (node.right != null) {
                    nodeTemps.add(node.right);
                }
            }
            nodes = nodeTemps;
            level ++;
        }
    }

    public static void main(String[] args) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
        treeMap.put(10, 10);
        treeMap.put(2, 2);
        treeMap.put(4, 4);
        treeMap.put(12, 12);
        treeMap.put(6, 6);
        treeMap.put(3, 3);
        treeMap.put(1, 1);
        treeMap.put(7, 7);
        treeMap.put(13, 13);
        treeMap.put(8, 8);
        treeMap.put(9, 9);
        treeMap.put(5, 5);
        treeMap.put(11, 11);

        treeMap.printTree(treeMap.getRoot());
    }
}