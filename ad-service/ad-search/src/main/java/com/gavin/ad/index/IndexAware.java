package com.gavin.ad.index;

public interface IndexAware<K, V> {
    V get(K k);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
