package Cache;

import Cache.Cache;
import Entry.Entry;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Class for caching into the RAM
 * @param <K> key for map
 * @param <V> value for map
 */
public class RAMCache<K, V> implements Cache<K, V> {
  private HashMap<K, SoftReference<V>> ramCacheMap;
  private K lastAddedObject;
  private final int maxBufferSize;

  public RAMCache(int maxBufferSize) {
    this.ramCacheMap = new HashMap<>();

    if (maxBufferSize < 0) {
      this.maxBufferSize = 0;
    } else {
      this.maxBufferSize = maxBufferSize;
    }
  }

  @Override
  public boolean cache(K key, V value) {
    if(size() < maxBufferSize) {
      SoftReference<V> valueReference = new SoftReference<>(value);
      ramCacheMap.put(key, valueReference);
      lastAddedObject = key;
      return true;
    }

    return false;
  }

  @Override
  public V getObject(K key) {
    return ramCacheMap.get(key).get();
  }

  @Override
  public V deleteObject(K key) {
    return ramCacheMap.remove(key).get();
  }

  @Override
  public void clear() {
    ramCacheMap.clear();
  }

  @Override
  public int size() {
    return ramCacheMap.size();
  }

  @Override
  public Entry<K, V> removeLastAddedEntry() {
    return new Entry<>(lastAddedObject, deleteObject(lastAddedObject));
  }


}
