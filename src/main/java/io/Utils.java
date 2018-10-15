package io;

import constants.PathConstants;
import utils.IndexedSerializable;

import java.io.*;

/**
 * A collections of utility methods
 */
public final class Utils {
    private Utils() {
    }

    /**
     * Save a given object in a file
     *
     * @param obj the object to save
     * @param path the path in which the obj must be saved
     * @param <E> the type of the object
     */
    public static <E extends IndexedSerializable> void save(E obj, PathConstants path) {
        try( FileOutputStream fileOutput = new FileOutputStream(path.getPath());
             ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput)) {
            objOutput.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving to file");
        }
    }

    /**
     * Restore a saved object
     *
     * @param path the path of the file
     * @param <E> the type of the object saved
     * @return the restored object
     */
    @SuppressWarnings("unchecked")
    public static <E extends IndexedSerializable> E restore(PathConstants path) {
        try (FileInputStream fileInput = new FileInputStream(path.getPath());
             ObjectInputStream objInput = new ObjectInputStream(fileInput)) {
            return (E) objInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while restoring to object");
        }
    }
}
