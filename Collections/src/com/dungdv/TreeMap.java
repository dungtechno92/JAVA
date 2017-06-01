package com.dungdv;

import org.jetbrains.annotations.Nullable;

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

    public V put(K var1, V var2) {
        Entry var3 = this.root;
        if(var3 == null) {
            this.compare(var1, var1);
            this.root = new Entry(var1, var2, (Entry)null);
            this.size = 1;
            ++this.modCount;
            return null;
        } else {
            Comparator var6 = this.comparator;
            int var4;
            Entry var5;
            if(var6 != null) {
                do {
                    var5 = var3;
                    var4 = var6.compare(var1, var3.key);
                    if(var4 < 0) {
                        var3 = var3.left;
                    } else {
                        if(var4 <= 0) {
                            return (V) var3.setValue(var2);
                        }

                        var3 = var3.right;
                    }
                } while(var3 != null);
            } else {
                if(var1 == null) {
                    throw new NullPointerException();
                }

                Comparable var7 = (Comparable)var1;

                do {
                    var5 = var3;
                    var4 = var7.compareTo(var3.key);
                    if(var4 < 0) {
                        var3 = var3.left;
                    } else {
                        if(var4 <= 0) {
                            return (V) var3.setValue(var2);
                        }

                        var3 = var3.right;
                    }
                } while(var3 != null);
            }

            Entry var8 = new Entry(var1, var2, var5);
            if(var4 < 0) {
                var5.left = var8;
            } else {
                var5.right = var8;
            }

            this.fixAfterInsertion(var8);
            ++this.size;
            ++this.modCount;
            return null;
        }
    }

    private void deleteEntry(Entry<K,V> p) {
        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Entry<K,V> s = findReplament(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    static <K,V> TreeMap.Entry<K,V> findReplament(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void fixAfterDeletion(Entry<K,V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry<K,V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                Entry<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    private void fixAfterInsertion(Entry<K, V> var1) {
        var1.color = RED;

        while(var1 != null && var1 != this.root && var1.parent.color == RED) {
            Entry<K,V> parent = parentOf(var1);
            Entry<K,V> grandParent = parentOf(parent);
            if(parent == leftOf(grandParent)) {
                Entry<K,V> uncle = rightOf(grandParent);
                if(colorOf(uncle) == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    var1 = grandParent;
                    var1.color = RED;
                } else {
                    if(var1 == rightOf(parent)) {
                        rotateLeft(parent);
                        parent = var1;
                    }

                    parent.color = BLACK;
                    grandParent.color = RED;
                    rotateRight(grandParent);
                }
            } else {
                Entry<K,V> uncle = leftOf(grandParent);
                if(colorOf(uncle) == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    var1 = grandParent;
                    var1.color = RED;
                } else {
                    if(var1 == leftOf(parent)) {
                        rotateRight(parent);
                        parent = var1;
                    }

                    parent.color = BLACK;
                    grandParent.color = RED;
                    rotateLeft(grandParent);
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