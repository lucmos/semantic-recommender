package io;

import clusterization.ClusterGenerator;
import model.clusters.Clusters;
import constants.ClustersPath;
import constants.DatasetName;

import model.twitter.Dataset;
import datasetsreader.DatasetReader;
import utils.Chrono;

import java.io.File;

public abstract class Cache {
    private static <E extends IndexedSerializable> void writeToCache(E obj, String name, String path, boolean pretty) {
        Chrono c = new Chrono(String.format("Writing to cache: %s...", name));
        Utils.saveJson(obj, path, pretty);
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

                Dataset d = new DatasetReader(name).readDataset();
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

    public static class ClustersWikiMidCache extends Cache {

        public static void main(String[] args) throws Utils.CacheNotPresent {
            ClustersWikiMidCache.regenCache();
        }

        public static void regenCache() throws Utils.CacheNotPresent {
            Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);

            Config.getInstance().setClusterOver(Config.ClusterOver.CATEGORIES);
            ClusterGenerator gen = new ClusterGenerator(d);
            Clusters clu  = gen.generate();
            write(clu);

            Config.getInstance().setClusterOver(Config.ClusterOver.DOMAINS);
            gen = new ClusterGenerator(d);
            clu  = gen.generate();
            write(clu);
        }

        public static void write(Clusters mapping) {
            ClustersPath path = ClustersPath.CLUSTERS;

            Config.Dimension dim = Config.getInstance().dimension();
            Config.ClusterMethod type = Config.getInstance().clusterMethod();
            Config.ClusterOver over = Config.getInstance().clusterOver();

            Cache.writeToCache(mapping, path.getPath(over, type, dim), path.getPath(over, type, dim), true);
        }

        public static Clusters read() throws Utils.CacheNotPresent {
            ClustersPath path = ClustersPath.CLUSTERS;

            Config.Dimension dim = Config.getInstance().dimension();
            Config.ClusterMethod type = Config.getInstance().clusterMethod();
            Config.ClusterOver over = Config.getInstance().clusterOver();

            return Cache.readFromCache(Clusters.class, path.getPath(over, type, dim), path.getPath(over, type, dim));
        }
    }
}
