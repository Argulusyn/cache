package Serializer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Works with files and serializable objects
 * @param <V>
 */
public interface Serializer<V extends Serializable> {
  void serialize(V value, String path) throws IOException;
  V deserialize(String path) throws IOException, ClassNotFoundException;
}
