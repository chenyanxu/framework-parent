package com.kalix.framework.core.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-08-04.
 */
public class MapUtil {
    /**
     * convert dictionary to map
     * @param dictionary
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> valueOf(Dictionary<K, V> dictionary) {
        if (dictionary == null) {
            return null;
        }
        Map<K, V> map = new HashMap<K, V>(dictionary.size());
        Enumeration<K> keys = dictionary.keys();
        while (keys.hasMoreElements()) {
            K key = keys.nextElement();
            map.put(key, dictionary.get(key));
        }
        return map;
    }
}
