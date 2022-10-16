package com.coderman.sync.pair;

import lombok.Data;

@Data
public class SyncPair<K,V> {

    private K key;

    private V value;
}
