package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A collections of utility methods
 */
public final class Utils {

    public static class CacheNotPresent extends Exception {
        public CacheNotPresent() {

        }

        public CacheNotPresent(Exception e) {
            super(e);
        }
    }

    private Utils() {
    }


    public static String timestamp;
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("d-MM_hh-mm");
        timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
    }

    private static Gson gsonDefault = new Gson();
    private static Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    public static <E extends IndexedSerializable> void saveJson(E obj, String path, Gson gson) {
        try (BufferedWriter fileOutput = new BufferedWriter(new FileWriter(path)) ){
            gson.toJson(obj, fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving to file");
        }
    }

    public static <E extends IndexedSerializable> void saveJson(E obj, String path, boolean pretty) {
        saveJson(obj, path, pretty ? gsonPretty : gsonDefault);
    }

    @SuppressWarnings("unchecked")
    public static <E extends IndexedSerializable> E restoreJson(String path, Class classe) throws CacheNotPresent {
        try (BufferedReader fileInput = new BufferedReader(new FileReader(path)) ){
            return (E) gsonDefault.fromJson(fileInput, classe);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CacheNotPresent(e);
        }
    }

    /**
     * Save a given object in a file
     *
     * @param obj the object to saveObj
     * @param path the path in which the obj must be saved
     * @param <E> the type of the object
     */
    public static <E extends IndexedSerializable> void saveObj(E obj, String path) {
        try( FileOutputStream fileOutput = new FileOutputStream(path);
             ObjectOutputStream objOutput = new ObjectOutputStream(new BufferedOutputStream(fileOutput))) {
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
    public static <E extends IndexedSerializable> E restoreObj(String path) throws CacheNotPresent {
        try (FileInputStream fileInput = new FileInputStream(path);
             ObjectInputStream objInput = new ObjectInputStream(new BufferedInputStream(fileInput))) {
            return (E) objInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CacheNotPresent(e);
        }
    }
}
