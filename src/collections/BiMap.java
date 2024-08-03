package collections;

import java.util.*;

public class BiMap<K, V> {
    private final HashSet<Pair<K, V>> table;

    private BiMap(HashSet<Pair<K, V>> table) {
        this.table = table;
    }

    public BiMap() {
        this.table = new HashSet<>();
    }

    public V put(K key, V value) {
        if (keys().contains(key) || values().contains(value)) {
            throw new IllegalArgumentException();
        }
        table.add(new Pair<>(key, value));
        return value;
    }

    public void putAll(Map<K, V> map) {
        for (K key : map.keySet()) {
            put(key, map.get(key));
        }
    }

    public Set<V> values() {
        Set<V> values = new HashSet<>();
        for (Pair<K, V> pair : table) {
            values.add(pair.value);
        }
        return values;
    }

    public Set<K> keys() {
        Set<K> keys = new HashSet<>();
        for (Pair<K, V> pair : table) {
            keys.add(pair.key);
        }
        return keys;
    }

    public V forcePut(K key, V value) {
        Pair<K, V> foundByKey = findByKey(key);
        Pair<K, V> foundByValue = findByValue(value);
        if (foundByValue != null) {
            foundByValue.setKey(key);
        } else if (foundByKey != null) {
            foundByKey.setValue(value);
        } else {
            put(key, value);
        }
        return value;
    }

    public BiMap<V, K> inverse() {
        HashSet<Pair<V, K>> invertedTable = new HashSet<>();
        for (Pair<K, V> entry : table) {
            invertedTable.add(entry.inverse());
        }
        return new BiMap<>(invertedTable);
    }

    private Pair<K, V> findByKey(K key) {
        for (Pair<K, V> entry : table) {
            if (Objects.equals(entry.key, key)) {
                return entry;
            }
        }
        return null;
    }

    private Pair<K, V> findByValue(V value) {
        for (Pair<K, V> entry : table) {
            if (Objects.equals(entry.value, value)) {
                return entry;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator<Pair<K, V>> iterator = table.iterator();
        while (iterator.hasNext()) {
            Pair<K, V> pair = iterator.next();
            sb.append(pair.key).append("=").append(pair.value);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    static class Pair<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private Pair<V, K> inverse() {
            return new Pair<>(value, key);
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public void setKey(K newKey) {
            this.key = newKey;
        }
    }
}
