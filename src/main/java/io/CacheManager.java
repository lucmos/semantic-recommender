package io;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import utils.Chrono;

import java.io.File;

public class CacheManager {

    public static void main(String[] args) {
        CacheManager.regenCache(Dimension.SMALL);
//        CacheManager.regenCache(Dimension.COMPLETE);
    }

    public static void regenCache(Dimension dim) {
        Chrono c0 = new Chrono(String.format("Regenerating cache with dimension %s...", dim));
        for (DatasetName name : DatasetName.values()) {
            Chrono c = new Chrono(String.format("Reading %s...", name));

            Dataset d = DatasetReader.readDataset(name, dim);
            CacheManager.writeToCache(name, d);

            c.millis("done (in %s %s) --> " + name + ": " + d);
        }
        c0.seconds();
    }

    public static void writeToCache(DatasetName datasetName, Dataset dataset) {
        String binPath = datasetName.getBinPath(dataset.getDimension());
        Chrono c = new Chrono(String.format("Writing %s...", binPath));

        Utils.save(dataset, binPath);

        c.millis();
    }

    public static Dataset readFromCache(DatasetName datasetName, Dimension dim) {
        File file = new File(datasetName.getBinPath(dim));
        Chrono c = new Chrono(String.format("Reading %s...", file.getPath()));

        if (!file.exists()) {
            throw new RuntimeException("File cache not present: " + file.getPath());
        }
        Dataset d = Utils.restore(datasetName.getBinPath(dim));
        c.millis();
        return d;
    }

}
