package io;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheManager {



    public static void writeToCache(DatasetName datasetName, Dataset dataset) {
        System.out.println("Writing");
        System.out.println(datasetName.getBinPath(dataset.getDimension()));
        Utils.save(dataset, datasetName.getBinPath(dataset.getDimension()));
    }

    public static Dataset readFromCache(DatasetName datasetName, Dimension dim) {
        File file = new File(datasetName.getBinPath(dim));
        if (!file.exists()) {
            throw new RuntimeException("File cache not present: " + file.getPath());
        }
        return Utils.restore(datasetName.getBinPath(dim));
    }
}
