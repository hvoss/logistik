package de.hsbremen.kss.util;

import java.util.HashMap;

public class CounterMap<K> extends HashMap<K, Integer> {

    @Override
    public Integer put(final K key, final Integer value) {
        Integer num = get(key);
        if (num == null) {
            num = value;
        } else {
            num += value;
        }

        return super.put(key, num);
    }
}
