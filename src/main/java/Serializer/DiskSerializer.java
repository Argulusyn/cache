package Serializer;

import java.io.*;

/**
 * Serializer.Serializer for work with disk
 * @param <V>
 */
public class DiskSerializer<V extends Serializable> implements Serializer<V> {

  @Override
  public void serialize(V value, String path) throws IOException {
    FileOutputStream fileStream = new FileOutputStream(path);
    ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

    objectStream.writeObject(value);
    objectStream.flush();
    objectStream.close();
    fileStream.flush();
    fileStream.close();
  }

  @Override
  public V deserialize(String path) throws IOException, ClassNotFoundException {
    FileInputStream fileStream = new FileInputStream(path);
    ObjectInputStream objectStream = new ObjectInputStream(fileStream);

    V deserializeObject =  (V) objectStream.readObject();

    fileStream.close();
    objectStream.close();

    return deserializeObject;
  }
}
