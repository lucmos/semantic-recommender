package io;

import babelnet.WikiPageMapping;
import clusters.Clusters;
import constants.DatasetName;

import constants.PathConstants;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import properties.PropReader;
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
            e.printStackTrace();
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
            DatasetCache.regenCache();
        }

        public static void regenCache() {
            for (DatasetName name : DatasetName.values()) {
                Chrono c = new Chrono(String.format("Reading: %s...", name));

                Dataset d = DatasetReader.readDataset(name);
                DatasetCache.writeToCache(name, d);

                c.millis("done (in %s %s) --> " + name + ": " + d);
            }
        }

        public static void writeToCache(DatasetName datasetName, Dataset dataset) {
            String binPath = datasetName.getBinPath(dataset.getDimension());
            Cache.writeToCache(dataset, binPath, binPath);
        }

        public static Dataset readFromCache(DatasetName datasetName) throws Utils.CacheNotPresent {
            PropReader.Dimension dim = PropReader.getInstance().dimension();
            String path = new File(datasetName.getBinPath(dim)).getPath();
            return Cache.readFromCache(Dataset.class, datasetName.toString(), path);
        }
    }


    public static class WikiMappingCache extends Cache {
        public static void main(String[] args) throws Utils.CacheNotPresent {
            WikiMappingCache.regenCache();
        }

        public static void regenCache() throws Utils.CacheNotPresent {
            WikiPageMapping mapping = new WikiPageMapping();
            mapping.compute();
            Cache.WikiMappingCache.writeToCache(mapping);
        }

        public static void writeToCache(WikiPageMapping mapping) {
            PropReader.Dimension dim = PropReader.getInstance().dimension();
            String binPath = PathConstants.WIKIPAGE_TO_BABELNET.getPath(dim);
            Cache.writeToCache(mapping, PathConstants.WIKIPAGE_TO_BABELNET.toString(), binPath);
        }

        public static WikiPageMapping readFromCache() throws Utils.CacheNotPresent {
            PropReader.Dimension dim = PropReader.getInstance().dimension();
            String path = PathConstants.WIKIPAGE_TO_BABELNET.getPath(dim);
            return Cache.readFromCache(WikiPageMapping.class, PathConstants.WIKIPAGE_TO_BABELNET.toString(), path);
        }
    }

    public static class ClustersCache extends Cache {
        public static void writeToCache(Clusters mapping, PathConstants path) {
            PropReader.Dimension dim = PropReader.getInstance().dimension();
            String binPath = path.getPath(dim);
            Cache.writeToCache(mapping, path.toString(), binPath);
        }

        public static Clusters readFromCache(PathConstants path) throws Utils.CacheNotPresent {
            PropReader.Dimension dim = PropReader.getInstance().dimension();
            String p = path.getPath(dim);
            return Cache.readFromCache(Clusters.class, path.toString(), p);
        }
    }
}
