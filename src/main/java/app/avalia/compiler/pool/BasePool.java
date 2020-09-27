package app.avalia.compiler.pool;

import java.util.HashMap;
import java.util.Map;

public class BasePool<K, V> {

    private final Map<K, V> pool = new HashMap<>();

    public void push(K key, V value) {
        pool.put(key, value);
    }

    public V get(K key) {
        return pool.get(key);
    }

    public Map<K, V> getPool() {
        return pool;
    }

}
