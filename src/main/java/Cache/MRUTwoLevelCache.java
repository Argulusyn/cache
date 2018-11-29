package Cache;

import Cache.Cache;
import Entry.Entry;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cache.Cache that use 2 abstract caches and creates from them a two-level structure for caching
 * uses the MRU algorithm
 * @param <K>
 * @param <V>
 */
public class MRUTwoLevelCache<K, V extends Serializable> implements Cache<K, V> {
  private Cache<K, V> firstLevelCache;
  private Cache<K, V> secondLevelCache;

  private List<K> firstLevelKeys;
  private List<K> secondLevelKeys;

  private K lastAddedEntry;

  public MRUTwoLevelCache(Cache<K, V> firstLevelCache, Cache<K, V> secondLevelCache) {
    this.firstLevelCache = firstLevelCache;
    this.secondLevelCache = secondLevelCache;

    this.firstLevelKeys = new ArrayList<>();
    this.secondLevelKeys = new ArrayList<>();
  }

  @Override
  public boolean cache(K key, V value) throws IOException, ClassNotFoundException {
    if(!firstLevelCache.cache(key, value)) {

      moveLastAddedEntryToSecondLevelCache();
      firstLevelCache.cache(key, value);

      firstLevelKeys.add(key);

      lastAddedEntry = key;
      return true;
    } else {
      firstLevelKeys.add(key);
    }
    return false;
  }

  @Override
  public V getObject(K key) throws IOException, ClassNotFoundException {
    if (firstLevelKeys.contains(key))
      return firstLevelCache.getObject(key);
    if (secondLevelKeys.contains(key))
      return secondLevelCache.getObject(key);
    return null;
  }

  @Override
  public V deleteObject(K key) throws IOException, ClassNotFoundException {
    if (firstLevelKeys.contains(key)) {
      firstLevelKeys.remove(key);
      return firstLevelCache.deleteObject(key);
    }
    if (secondLevelKeys.contains(key)) {
      secondLevelKeys.remove(key);
      return secondLevelCache.deleteObject(key);
    }
    return null;
  }

  @Override
  public void clear() {
    firstLevelCache.clear();
    secondLevelCache.clear();
  }

  @Override
  public int size() {
    return firstLevelCache.size() + secondLevelCache.size();
  }

  @Override
  public Entry<K, V> removeLastAddedEntry() throws IOException, ClassNotFoundException {
    return new Entry<>(lastAddedEntry, deleteObject(lastAddedEntry));
  }

  private void moveLastAddedEntryToSecondLevelCache() throws IOException, ClassNotFoundException {
    Entry<K, V> lastAddedEntry = firstLevelCache.removeLastAddedEntry();
    firstLevelKeys.remove(lastAddedEntry.getKey());

    if(!secondLevelCache.cache(lastAddedEntry.getKey(), lastAddedEntry.getValue())){
      secondLevelKeys.remove(secondLevelCache.removeLastAddedEntry().getKey());
      secondLevelCache.cache(lastAddedEntry.getKey(), lastAddedEntry.getValue());
    }

    secondLevelKeys.add(lastAddedEntry.getKey());
  }
}
