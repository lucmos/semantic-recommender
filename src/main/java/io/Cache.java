package io;

import babelnet.WikiPageMapping;
import clusters.ClusterGenerator;
import clusters.Clusters;
import constants.ClusterName;
import constants.DatasetName;

import constants.PathConstants;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import properties.Config;
import utils.Chrono;
import utils.IndexedSerializable;

import java.io.File;

public abstract class Cache {


    private static <E extends IndexedSerializable> void writeToCache(E obj, String name, String path, boolean pretty) {
        Chrono c = new Chrono(String.format("Writing to cache: %s...", name));
        if (pretty) {
            Utils.savePrettyJson(obj, path);
        } else {
            Utils.saveJson(obj, path);
        }
        c.millis();
    }

    private static <E extends IndexedSerializable> void writeToCache(E obj, String name, String path) {
        writeToCache(obj, name, path, false);
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
                DatasetCache.write(name, d);

                c.millis("done (in %s %s) --> " + name + ": " + d);
            }
        }

        public static void write(DatasetName datasetName, Dataset dataset) {
            String binPath = datasetName.getBinPath(dataset.getDimension());
            Cache.writeToCache(dataset, binPath, binPath);
        }

        public static Dataset read(DatasetName datasetName) throws Utils.CacheNotPresent {
            Config.Dimension dim = Config.getInstance().dimension();
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
            Cache.WikiMappingCache.write(mapping);
        }

        public static void write(WikiPageMapping mapping) {
            Config.Dimension dim = Config.getInstance().dimension();
            String binPath = PathConstants.WIKIPAGE_TO_BABELNET.getPath(dim);
            Cache.writeToCache(mapping, PathConstants.WIKIPAGE_TO_BABELNET.toString(), binPath);
        }

        public static WikiPageMapping read() throws Utils.CacheNotPresent {
            Config.Dimension dim = Config.getInstance().dimension();
            String path = PathConstants.WIKIPAGE_TO_BABELNET.getPath(dim);
            return Cache.readFromCache(WikiPageMapping.class, PathConstants.WIKIPAGE_TO_BABELNET.toString(), path);
        }
    }

    public static class ClustersWikiMidCache extends Cache {

        public static void main(String[] args) throws Utils.CacheNotPresent {
            ClustersWikiMidCache.regenCache();
        }

        public static void regenCache() throws Utils.CacheNotPresent {
            Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
            WikiPageMapping w = Cache.WikiMappingCache.read();
            ClusterGenerator c = new ClusterGenerator(d, w);

            Clusters c1  = c.generateCategoryClusters();
            Clusters c2  = c.generateDomainClusters();

            writeCategories(c1);
            writeDomains(c2);
        }

        public static void write(Clusters mapping, ClusterName name) {
            Config.Dimension dim = Config.getInstance().dimension();
            Config.ClusterType type = Config.getInstance().clusterType();

            String binPath = name.getPath(dim, type);
            Cache.writeToCache(mapping, binPath, binPath, true);
        }

        public static Clusters read(ClusterName path) throws Utils.CacheNotPresent {
            Config.Dimension dim = Config.getInstance().dimension();
            Config.ClusterType type = Config.getInstance().clusterType();

            String p = path.getPath(dim, type);
            return Cache.readFromCache(Clusters.class, path.toString(), p);
        }

        public static void writeCategories(Clusters mapping) {
            ClustersWikiMidCache.write(mapping, ClusterName.CLUSTERS_CAT);
        }

        public static void writeDomains(Clusters mapping) {
            ClustersWikiMidCache.write(mapping, ClusterName.CLUSTERS_DOM);
        }

        public static Clusters readCategories() throws Utils.CacheNotPresent {
            return ClustersWikiMidCache.read(ClusterName.CLUSTERS_CAT);
        }

        public static Clusters readDomains() throws Utils.CacheNotPresent {
            return ClustersWikiMidCache.read(ClusterName.CLUSTERS_DOM);
        }
    }
}
