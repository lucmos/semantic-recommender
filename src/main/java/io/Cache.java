package io;

import clusterization.JavaExport;
import constants.JavaExportPath;
import constants.DatasetName;

import executors.DatasetMerger;
import model.twitter.Dataset;
import sun.security.x509.DNSName;
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
            DatasetName[] names = new DatasetName[]{DatasetName.WIKIMID, DatasetName.S21, DatasetName.S22, DatasetName.S23};

            for (DatasetName name : names) {
                for (Config.Dimension dimension : Config.Dimension.values()) {
                    Config.getInstance().setDimension(dimension);
                    Chrono c = new Chrono(String.format("Reading: %s...", name));

                    Dataset d = new DatasetReader(name).readDataset();
                    DatasetCache.write(name, d);

                    c.millis("done (in %s %s) --> " + name + ": " + d);
                }
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

    public static class JavaExportCache extends Cache {

        public static void main(String[] args) throws Utils.CacheNotPresent {
            JavaExportCache.export();
        }

        public static void export() throws Utils.CacheNotPresent {
            for (DatasetName name : DatasetName.values()) {
                for (Config.Dimension dimension : Config.Dimension.values()) {
                    Config.getInstance().setDimension(dimension);

//                    if (name.equals(DatasetName.UNION)) {
//                        DatasetMerger.unionAndSave();
//                    }

                    for (Config.ClusterOver over : Config.ClusterOver.values()) {
                        Config.getInstance().setClusterOver(over);

                        Chrono c = new Chrono(String.format("Generating export for: %s, %s, %s", name, over, dimension));

                        Dataset d = Cache.DatasetCache.read(name);


                        JavaExport exp = new JavaExport(d);

                        String path = JavaExportPath.EXPORT_PATH.getPath(name, over, dimension);
                        Cache.writeToCache(exp, path, path, true);
                        c.millis();

                    }
                }

            }
        }
    }
}
