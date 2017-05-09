package com.dungdv;

import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    private void fixAfterInsertion(Entry<K, V> var1) {
        var1.color = false;

        while(var1 != null && var1 != this.root && !var1.parent.color) {
            Entry var2;
            if(parentOf(var1) == leftOf(parentOf(parentOf(var1)))) {
                var2 = rightOf(parentOf(parentOf(var1)));
                if(!colorOf(var2)) {
                    setColor(parentOf(var1), true);
                    setColor(var2, true);
                    setColor(parentOf(parentOf(var1)), false);
                    var1 = parentOf(parentOf(var1));
                } else {
                    if(var1 == rightOf(parentOf(var1))) {
                        var1 = parentOf(var1);
                        this.rotateLeft(var1);
                    }

                    setColor(parentOf(var1), true);
                    setColor(parentOf(parentOf(var1)), false);
                    this.rotateRight(parentOf(parentOf(var1)));
                }
            } else {
                var2 = leftOf(parentOf(parentOf(var1)));
                if(!colorOf(var2)) {
                    setColor(parentOf(var1), true);
                    setColor(var2, true);
                    setColor(parentOf(parentOf(var1)), false);
                    var1 = parentOf(parentOf(var1));
                } else {
                    if(var1 == leftOf(parentOf(var1))) {
                        var1 = parentOf(var1);
                        this.rotateRight(var1);
                    }

                    setColor(parentOf(var1), true);
                    setColor(parentOf(parentOf(var1)), false);
                    this.rotateLeft(parentOf(parentOf(var1)));
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

    public static void main(String[] args) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("key4", "value4");
        treeMap.put("key2", "value2");
        treeMap.put("key1", "value1");
        treeMap.put("key3", "value3");
        treeMap.put("key5", "value5");

        System.out.print("==========" + treeMap.getFirstEntry().getValue());
    }
}