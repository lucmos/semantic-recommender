package io;

import babelnet.WikiPageMapping;
import constants.DatasetName;
import constants.Dimension;
import constants.PathConstants;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import utils.Chrono;
import utils.IndexedSerializable;

import java.io.File;

public abstract class Cache {

    private static <E extends IndexedSerializable> void writeToCache(E obj, String name, String path) {
        Chrono c = new Chrono(String.format("Writing to cache: %s...", name));
//        Utils.saveObj(obj, path);
        Utils.saveJson(obj, path);
        c.millis();
    }

    // TODO: 29/10/18 check usage
    private static <E extends IndexedSerializable> E readFromCache(Class<E> classe, String name, String path) throws Utils.CacheNotPresent {
        Chrono c = new Chrono(String.format("Reading from cache: %s...", name));

        E d;
        try {
//            d = Utils.restoreObj(path);
            d = Utils.restoreJson(path, classe);
        } catch (Utils.CacheNotPresent e) {
            String err = String.format("[error: %s]", e);
            c.millis(String.format("%s - %s", "done (in %s %s)", err));
            throw new Utils.CacheNotPresent(e);
        }

        c.millis();
        assert d != null;
        return d;
    }


    public static class DatasetCache extends Cache {

        public static void main(String[] args) {
            DatasetCache.regenCache(Dimension.SMALL);
            DatasetCache.regenCache(Dimension.COMPLETE);
        }

        public static void regenCache(Dimension dim) {
            Chrono c0 = new Chrono(String.format("Regenerating cache with dimension: %s...", dim));
            for (DatasetName name : DatasetName.values()) {
                Chrono c = new Chrono(String.format("Reading: %s...", name));

                Dataset d = DatasetReader.readDataset(name, dim);
                DatasetCache.writeToCache(name, d);

                c.millis("done (in %s %s) --> " + name + ": " + d);
            }
            c0.seconds();
        }

        public static void writeToCache(DatasetName datasetName, Dataset dataset) {
            String binPath = datasetName.getBinPath(dataset.getDimension());
            Cache.writeToCache(dataset, binPath, binPath);
        }

        public static Dataset readFromCache(DatasetName datasetName, Dimension dim) throws Utils.CacheNotPresent {
            String path = new File(datasetName.getBinPath(dim)).getPath();
            return Cache.readFromCache(Dataset.class, datasetName.toString(), path);
        }
    }

    public static class WikiMappingCache extends Cache {
        public static void main(String[] args) throws Utils.CacheNotPresent {
            WikiMappingCache.regenCache(Dimension.SMALL);
            WikiMappingCache.regenCache(Dimension.COMPLETE);
        }

        public static void regenCache(Dimension dim) throws Utils.CacheNotPresent {
            WikiPageMapping mapping = new WikiPageMapping(dim);
            Cache.WikiMappingCache.writeToCache(mapping);
        }

        public static void writeToCache(WikiPageMapping mapping) {
            String binPath = PathConstants.WIKIPAGE_TO_BABELNET.getPath(mapping.getDimension());
            Cache.writeToCache(mapping, PathConstants.WIKIPAGE_TO_BABELNET.toString() + " " + mapping.getDimension(), binPath);
        }

        public static WikiPageMapping readFromCache(Dimension dimension) throws Utils.CacheNotPresent {
            String path = PathConstants.WIKIPAGE_TO_BABELNET.getPath(dimension);
            return Cache.readFromCache(WikiPageMapping.class,PathConstants.WIKIPAGE_TO_BABELNET.toString() + " " + dimension, path);
        }
    }
}
