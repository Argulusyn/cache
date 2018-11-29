package Cache;

import Entry.Entry;

import java.io.IOException;

/**
 * Interface for caching classes
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {
    boolean cache(K key, V value) throws IOException, ClassNotFoundException;
    V getObject(K key) throws IOException, ClassNotFoundException;
    V deleteObject(K key) throws IOException, ClassNotFoundException;
    void clear();
    int size();
    Entry<K,V> removeLastAddedEntry() throws IOException, ClassNotFoundException;
}
