package io;

import constants.DatasetName;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import properties.PropReader;
import utils.Chrono;

import java.io.File;

public class Cache {

    public static void main(String[] args) {
        Cache.regenCache();
//        Cache.regenCache(Dimension.COMPLETE);
    }

    public static void regenCache() {

        Chrono c0 = new Chrono("Regenerating cache...");
        for (DatasetName name : DatasetName.values()) {
            Chrono c = new Chrono(String.format("Reading: %s...", name));

            Dataset d = DatasetReader.readDataset(name);
            Cache.writeToCache(name, d);

            c.millis("done (in %s %s) --> " + name + ": " + d);
        }
        c0.seconds();
    }

    public static void writeToCache(DatasetName datasetName, Dataset dataset) {
        String binPath = datasetName.getBinPath(dataset.getDimension());

        Chrono c = new Chrono(String.format("Writing: %s...", binPath));
        Utils.save(dataset, binPath);
        c.millis();
    }

    public static Dataset readFromCache(DatasetName datasetName) {
        PropReader.Dimension dim = PropReader.getInstance().dimension();

        File file = new File(datasetName.getBinPath(dim));
        Chrono c = new Chrono(String.format("Reading: %s...", datasetName));

        if (!file.exists()) {
            throw new RuntimeException("File cache not present: " + file.getPath());
        }
        Dataset d = Utils.restore(datasetName.getBinPath(dim));
        c.millis();
        return d;
    }

}
