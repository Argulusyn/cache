package Entry;

import java.util.Map;

/**
 * Realization of Map.Entry.Entry
 * @param <K> key
 * @param <V> value
 */
public class Entry<K,V> implements Map.Entry<K, V> {
  private K key;
  private V value;

  public Entry(K key, V value) {
    this.key = key;
    this.value = value;
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
  public V setValue(V value) {
    V deletedValue = this.value;
    this.value = value;
    return deletedValue;
  }
}
