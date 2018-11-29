package Cache;

import Entry.Entry;
import Serializer.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Class for caching into the files
 * @param <K> key for map
 * @param <V> value for map
 */
public class DiskCache<K, V extends Serializable> implements Cache<K, V> {
  private HashMap<K, String> pathMap;
  private Serializer<V> serializer;
  private final String tempFolderPath = "temp\\";
  private K lastAddedObject;
  private int fileCounter;

  private final int maxBufferSize;

  public DiskCache(int maxBufferSize) {
    pathMap = new HashMap<>();
    serializer = new DiskSerializer<V>();
    createTempFolder(tempFolderPath);
    fileCounter = 0;

    if (maxBufferSize < 0) {
      this.maxBufferSize = 0;
    } else {
      this.maxBufferSize = maxBufferSize;
    }
  }

  @Override
  public boolean cache(K key, V value) throws IOException {
    if (size() < maxBufferSize) {
      String pathToObject;
      pathToObject = tempFolderPath + ++fileCounter + ".data";

      serializer.serialize(value, pathToObject);

      pathMap.put(key, pathToObject);
      lastAddedObject = key;
      return true;
    }
    return false;
  }

  @Override
  public V getObject(K key) throws IOException, ClassNotFoundException {
    if (pathMap.containsKey(key)){
      String pathToObject = pathMap.get(key);

      return serializer.deserialize(pathToObject);
    } else {
      return null;
    }
  }

  @Override
  public V deleteObject(K key) throws IOException, ClassNotFoundException {
    if(pathMap.containsKey(key)) {
      V deletedObject = getObject(key);
      File deleteFile = new File(pathMap.remove(key));
      deleteFile.delete();
      return deletedObject;
    } else {
      return null;
    }
  }

  @Override
  public void clear() {
    for(K key : pathMap.keySet()){
      File deleteFile = new File(pathMap.get(key));
      deleteFile.delete();
    }

    pathMap.clear();
  }

  @Override
  public int size() {
    return pathMap.size();
  }

  @Override
  public Entry<K, V> removeLastAddedEntry() throws IOException, ClassNotFoundException {
    V deletedObject = deleteObject(lastAddedObject);
    return new Entry<>(lastAddedObject, deletedObject);
  }

  private void createTempFolder(String path){
    File tempFolder = new File(path);
    if(!tempFolder.exists()){
      tempFolder.mkdirs();
    }
  }
}
