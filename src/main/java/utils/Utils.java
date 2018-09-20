package utils;

import constants.PathConstants;

import java.io.*;

public final class Utils {
    private Utils() {
    }

//    todo: va deciso se integrare il path nell'oggetto stesso, per evitare incoerenze. Ciò darebbe meno flissibilità.

    public static <E extends IndexedSerializable> void save(E obj, PathConstants path) {
        try( FileOutputStream fileOutput = new FileOutputStream(path.getPath());
             ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput)) {
            objOutput.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving to file");
        }
    }

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
